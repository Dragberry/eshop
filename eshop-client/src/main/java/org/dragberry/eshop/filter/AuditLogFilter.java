package org.dragberry.eshop.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.dragberry.eshop.dal.entity.AuditRecord;
import org.dragberry.eshop.dal.repo.AuditRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * This intercepter which logs customer's requests
 * 
 * @author Drahun Maksim
 *
 */
@Component
@Order(1)
public class AuditLogFilter extends GenericFilterBean {

    private static final List<String> EXCLUDE_STATIC = Arrays.asList("/images", "/js", "/css", "/webfonts");
    
    @Autowired
    private AuditRecordRepository auditRecordRepo;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        if (EXCLUDE_STATIC.stream().anyMatch(staticUri -> uri.startsWith(staticUri))) {
            chain.doFilter(request, response);
            return;
        }
        CachedHttpServletRequestWrapper cachedReq = new CachedHttpServletRequestWrapper((HttpServletRequest) request);
        AuditRecord record = new AuditRecord();
        record.setAddress(cachedReq.getRemoteAddr());
        record.setEncoding(cachedReq.getCharacterEncoding());
        record.setLocale(cachedReq.getLocale().toString());
        record.setMethod(cachedReq.getMethod());
        record.setSessionId(cachedReq.getSession() != null ? cachedReq.getSession().getId() : null);
        record.setUri(cachedReq.getRequestURI());
        record.setQueryString(cachedReq.getQueryString());
        record.setBody(IOUtils.toString(cachedReq.getInputStream(), Charset.forName(cachedReq.getCharacterEncoding())));
        auditRecordRepo.save(record);
        chain.doFilter(cachedReq, response);
    }

    private class CachedHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private byte[] body;

        public CachedHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            try {
                body = IOUtils.toByteArray(request.getInputStream());
            } catch (IOException ex) {
                body = new byte[0];
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStream() {
                ByteArrayInputStream bais = new ByteArrayInputStream(body);

                @Override
                public int read() throws IOException {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener arg0) {
                }
            };
        }
    }
}


