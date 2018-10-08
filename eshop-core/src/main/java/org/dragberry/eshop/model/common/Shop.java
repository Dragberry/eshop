package org.dragberry.eshop.model.common;

import java.util.List;

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
}