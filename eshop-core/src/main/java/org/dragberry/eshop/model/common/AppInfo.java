package org.dragberry.eshop.model.common;

import java.util.List;

import lombok.Data;

@Data
public class AppInfo {

	private WorkingDays workingDays;
	
	private List<Phone> phones;
	
	private String email;
	
	private String currency;
	
}
