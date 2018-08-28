package org.dragberry.eshop.service.impl;

import java.time.DayOfWeek;
import java.util.List;

import org.dragberry.eshop.model.common.AppInfo;
import org.dragberry.eshop.model.common.Phone;
import org.dragberry.eshop.model.common.WorkingDay;
import org.dragberry.eshop.model.common.WorkingDays;
import org.dragberry.eshop.service.AppInfoService;
import org.springframework.stereotype.Service;

@Service
public class AppInfoServiceImpl implements AppInfoService {

	@Override
	public AppInfo getAppInfo() {
		AppInfo info = new AppInfo();
		info.setCurrency("BYN");
		info.setEmail("info@smartvitrina.by");
		info.setPhones(List.of(new Phone(Phone.MTS, "8033 375-90-80"), new Phone(Phone.VELCOM, "8029 375-90-80")));
		info.setWorkingDays(new WorkingDays(List.of(
				new WorkingDay(DayOfWeek.MONDAY, "9:00", "21:00", false),
	            new WorkingDay(DayOfWeek.TUESDAY, "9:00", "21:00", false),
	            new WorkingDay(DayOfWeek.WEDNESDAY, "9:00", "21:00", false),
	            new WorkingDay(DayOfWeek.THURSDAY, "9:00", "21:00", false),
	            new WorkingDay(DayOfWeek.FRIDAY, "9:00", "21:00", false),
	            new WorkingDay(DayOfWeek.SATURDAY, "9:00", "21:00", false),
	            new WorkingDay(DayOfWeek.SUNDAY, "9:00", "21:00", false)),
				"Работаем: с 9:00 до 21:00 без выходных"));
		return info;
	}

}