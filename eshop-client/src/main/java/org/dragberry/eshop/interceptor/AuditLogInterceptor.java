package org.dragberry.eshop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.dragberry.eshop.dal.entity.AuditRecord;
import org.dragberry.eshop.dal.repo.AuditRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This intercepter which logs customer's requests
 * @author Drahun Maksim
 *
 */
public class AuditLogInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired
    private AuditRecordRepository auditRecordRepo;
    
    private final static ObjectMapper MAPPER = new ObjectMapper();
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        AuditRecord record = new AuditRecord();
        record.setAddress(request.getRemoteAddr());
        record.setEncoding(request.getCharacterEncoding());
        record.setLocale(request.getLocale().toString());
        record.setMethod(request.getMethod());
        record.setSessionId(request.getSession() != null ? request.getSession().getId() : null);
        record.setUri(request.getRequestURI());
        if (MapUtils.isNotEmpty(request.getParameterMap())) {
            record.setParams(MAPPER.writeValueAsString(request.getParameterMap()));
        }
        auditRecordRepo.save(record);
        return true;
    }
}
