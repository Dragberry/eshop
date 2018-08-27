package org.dragberry.eshop.service;

import org.dragberry.eshop.model.common.AppInfo;

public interface AppInfoService {

	/**
	 * Returns a general application info such as contact details, working days, etc.
	 * @return
	 */
	AppInfo getAppInfo();
	
}
