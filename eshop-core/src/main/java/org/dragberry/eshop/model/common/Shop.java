package org.dragberry.eshop.model.common;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shop {

	private String name;
	
	private String nameAlt;
	
	private String currency;
	
	private String email;
	
	private List<Phone> phones;
	
	private String onlineWorkingHours;
	
	private String workingHours;
	
	private String shippingWorkingHours;
	
	private String legalAddress;
	
	private String legalDetails;

	public String getPhonesAsLine() {
	    return phones.stream().map(p -> "☎" + p.getNumber()).collect(Collectors.joining(", "));
	}
}
