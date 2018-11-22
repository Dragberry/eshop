package org.dragberry.eshop.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.dragberry.eshop.dal.entity.RequestLog;
import org.dragberry.eshop.dal.entity.RequestLog.Device;
import org.dragberry.eshop.dal.entity.RequestLogData;
import org.dragberry.eshop.dal.repo.RequestLogRepository;
import org.springframework.lang.Nullable;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter logs customer's requests
 * 
 * @author Drahun Maksim
 *
 */
public class RequestLogFilter extends OncePerRequestFilter {

	private static final List<String> EXCLUDE_STATIC = Arrays.asList("/files", "/images", "/js", "/css", "/webfonts", "/favicon.ico");

	private RequestLogRepository requestLogRepo;

	public RequestLogFilter(RequestLogRepository repo) {
		this.requestLogRepo = repo;
	}

	/**
	 * The default value is "false" so that the filter may log a "before" message at
	 * the start of request processing and an "after" message at the end from when
	 * the last asynchronously dispatched thread is exiting.
	 */
	@Override
	protected boolean shouldNotFilterAsyncDispatch() {
		return false;
	}

	/**
	 * Forwards the request to the next filter in the chain and delegates down to
	 * the subclasses to perform the actual request logging both before and after
	 * the request is processed.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		boolean isFirstRequest = !isAsyncDispatch(request);
		HttpServletRequest requestToUse = request;

		if (isFirstRequest && !(request instanceof CustomHttpServletRequestWrapper)) {
			requestToUse = new CustomHttpServletRequestWrapper(request);
		}
		
		if (shouldLog(requestToUse) && isFirstRequest) {
			requestLogRepo.save(createRequestLog(requestToUse));
		}

		filterChain.doFilter(requestToUse, response);
	}

	/**
	 * Creates an RequestLog from request
	 * 
	 * @return
	 */
	private RequestLog createRequestLog(HttpServletRequest request) {
		RequestLog record = new RequestLog();
		record.setAddress(request.getRemoteAddr());
		record.setEncoding(request.getCharacterEncoding());
		record.setLocale(request.getLocale().toString());
		record.setMethod(request.getMethod());
		record.setDevice(Device.valueOf(DeviceUtils.getCurrentDevice(request)));
		record.setSessionId(request.getSession(false) != null ? request.getSession(false).getId() : null);
		record.setUri(request.getRequestURI());
		StringBuilder sb = new StringBuilder();
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String header = headers.nextElement();
			sb.append(header).append("\t").append(request.getHeader(header)).append("\n");
		}
		record.setHeaders(sb.length() > 1024 ? sb.substring(0, 1024) : sb.toString());
		RequestLogData data = new RequestLogData();
		try {
		    data.setQueryString(URLDecoder.decode(request.getQueryString(), request.getCharacterEncoding()));
		} catch (Exception exc) {
		    data.setQueryString(request.getQueryString());
		}
		data.setBody(getMessagePayload(request));
		if (!data.isEmpty()) {
		    data.setRequestLog(record);
		    record.setData(data);
		}
		return record;
	}

	/**
	 * Extracts the message payload portion of the message created by
	 */
	@Nullable
	protected String getMessagePayload(HttpServletRequest request) {
		try {
			return IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
		} catch (IOException ex) {
			return "[unknown]";
		}
	}

	/**
	 * Determine if the audit record should be logged. Static resources should not
	 * be processed
	 * 
	 * @param request
	 * @return
	 */
	protected boolean shouldLog(HttpServletRequest request) {
		return EXCLUDE_STATIC.stream().noneMatch(staticUri -> request.getRequestURI().startsWith(staticUri));
	}
	
	private class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

		private final byte[] body; 
		
		public CustomHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
			super(request);
			body = IOUtils.toByteArray(request.getReader(), request.getCharacterEncoding());
		}
		
		@Override
		public ServletInputStream getInputStream() throws IOException {
			final ByteArrayInputStream bais = new ByteArrayInputStream(body);
			return new ServletInputStream() {
				
				@Override
				public int read() throws IOException {
					return bais.read();
				}
				
				@Override
				public void setReadListener(ReadListener listener) {
				}
				
				@Override
				public boolean isReady() {
					return false;
				}
				
				@Override
				public boolean isFinished() {
					return false;
				}
			};
		}
		
	}

}
