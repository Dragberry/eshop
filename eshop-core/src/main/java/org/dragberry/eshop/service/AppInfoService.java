package org.dragberry.eshop.service;

import org.dragberry.eshop.model.common.AppInfo;
import org.dragberry.eshop.model.common.Features;
import org.dragberry.eshop.model.common.SystemInfo;

public interface AppInfoService {

    /**
     * Get the shop's name
     * @return
     */
    String shopName();
    
	/**
	 * Returns a general application info such as contact details, working days, etc.
	 * @return
	 */
	AppInfo getAppInfo();
	
	/**
	 * Return a system info which shouldn't be exposed to customer
	 * @return
	 */
	SystemInfo getSystemInfo();
	
	/**
	 * Get the list of available features
	 * @return
	 */
	Features getFeatures();
	
}
