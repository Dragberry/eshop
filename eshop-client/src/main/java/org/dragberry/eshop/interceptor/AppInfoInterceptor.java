package org.dragberry.eshop.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dragberry.eshop.dal.dto.MenuPageDTO;
import org.dragberry.eshop.dal.entity.MenuPage.Status;
import org.dragberry.eshop.dal.repo.MenuPageRepository;
import org.dragberry.eshop.model.common.AppInfo;
import org.dragberry.eshop.model.common.Features;
import org.dragberry.eshop.model.common.Shop;
import org.dragberry.eshop.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This interceptor injects common application information which can be used on all pages
 * @author Drahun Maksim
 *
 */
@Component
public class AppInfoInterceptor extends HandlerInterceptorAdapter {
	
	private static final String APP_INFO = "appInfo";

	private static final String FEATURES = "features";
	
	private static final String MENU = "menu";
	
	private static final String SHOP = "shop";

	@Autowired
	private AppInfoService appInfoService;
	
	@Autowired
	private MenuPageRepository menuPageRepo;

    private AppInfo appInfo;
    
    private Features features;
    
    private Shop shop;
    
    private List<MenuPageDTO> menu;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        request.getRemoteAddr();
        request.getRequestURI();
        request.getSession().getId();
        request.getLocale();
        request.getCharacterEncoding();
        request.getParameterMap();
        request.getMethod();
        request.getHeaderNames();
        
        if (shop == null) {
        	shop = appInfoService.getShopDetails();
        }
    	if (appInfo == null) {
    		appInfo = appInfoService.getAppInfo();
    	}
    	if (features == null) {
    		features = appInfoService.getFeatures();
    	}
    	if (menu == null) {
    	    menu = menuPageRepo.getMenu(Status.ACTIVE);
    	}
        if (modelAndView != null) {
            modelAndView.addObject(APP_INFO, appInfo);
            modelAndView.addObject(FEATURES, features);
            modelAndView.addObject(MENU, menu);
            modelAndView.addObject(SHOP, shop);
        }
    }
}
