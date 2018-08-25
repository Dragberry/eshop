package org.dragberry.eshop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dragberry.eshop.model.common.AppInfo;
import org.dragberry.eshop.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This interceptor injects common application information which can be used on all pages
 * @author Drahun Maksim
 *
 */
public class AppInfoInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private AppInfoService appInfoService;

    private AppInfo appInfo;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    	if (appInfo == null) {
    		appInfo = appInfoService.getAppInfo();
    	}
        if (modelAndView != null) {
            modelAndView.addObject("appInfo", appInfo);
        }
    }
}
