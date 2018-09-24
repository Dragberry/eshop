package org.dragberry.eshop.model.product;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentDetails {

	private Long id;
	
	private String name;
	
	private String email;
	
	private String text;
	
	private LocalDateTime date;

	private Integer mark;
	
	private String ip;
}
