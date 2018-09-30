package org.dragberry.eshop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dragberry.eshop.model.common.AppInfo;
import org.dragberry.eshop.model.common.Features;
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
	
	private static final String APP_INFO = "appInfo";

	private static final String FEATURES = "features";

	@Autowired
	private AppInfoService appInfoService;

    private AppInfo appInfo;
    
    private Features features;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    	if (appInfo == null) {
    		appInfo = appInfoService.getAppInfo();
    	}
    	if (features == null) {
    		features = appInfoService.getFeatures();
    	}
        if (modelAndView != null) {
            modelAndView.addObject(APP_INFO, appInfo);
            modelAndView.addObject(FEATURES, features);
        }
    }
}
