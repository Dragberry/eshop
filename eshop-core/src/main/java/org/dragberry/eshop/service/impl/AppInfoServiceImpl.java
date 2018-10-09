package org.dragberry.eshop.service.impl;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import org.dragberry.eshop.model.common.AppInfo;
import org.dragberry.eshop.model.common.Features;
import org.dragberry.eshop.model.common.Phone;
import org.dragberry.eshop.model.common.Shop;
import org.dragberry.eshop.model.common.SystemInfo;
import org.dragberry.eshop.model.common.WorkingDay;
import org.dragberry.eshop.model.common.WorkingDays;
import org.dragberry.eshop.service.AppInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppInfoServiceImpl implements AppInfoService {
    
    @Value("${application.title}")
    private String shopName;
    
    @Override
    public String shopName() {
        return shopName;
    }

    public List<Phone> getPhones() {
        return Arrays.asList(new Phone(Phone.MTS, "8033 375-90-80"), new Phone(Phone.VELCOM, "8029 375-90-80"));
    }
    
	@Override
	public AppInfo getAppInfo() {
		AppInfo info = new AppInfo();
		info.setShopName(shopName);
		info.setCurrency("BYN");
		info.setEmail("info@smartvitrina.by");
		info.setPhones(getPhones());
		info.setWorkingDays(new WorkingDays(Arrays.asList(
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

    @Override
    public SystemInfo getSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setEmail("max-hellfire@mail.ru");
        return systemInfo;
    }
    
    @Override
    public Features getFeatures() {
    	return new Features();
    }
    
    @Override
    public Shop getShopDetails() {
    	Shop shop = new Shop();
    	shop.setName(shopName);
    	shop.setNameAlt("Смартвитрина.бай");
    	shop.setCurrency("BYN");
    	shop.setEmail("info@smartvitrina.by");
    	shop.setPhones(getPhones());
    	shop.setOnlineWorkingHours("круглосуточно");
    	shop.setWorkingHours("ежедневно с 9:00 до 21:00");
    	shop.setShippingWorkingHours("ежедневно с 10:00 до 22:00");
    	shop.setLegalAddress("220049, Республика Беларусь, г. Минск, ул. Кнорина, д. 10а, кв. 19");
    	shop.setLegalDetails("УНП 192176034, IBAN: BY81AKBB30131604233855300000 В бел.рублях в филиале №514 ОАО «АСБ Беларусбанк», код 614, AKBBBY21514, Сурганова 47А");
    	return shop;
    }
    
}
