package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CATEGORY")
@TableGenerator(
		name = "CATEGORY_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "CATEGORY_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class Category extends BaseEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "CATEGORY_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CATEGORY_GEN")
	private Long entityKey;
	
	@Column(name = "NAME")
    private String name;
	
	@Column(name = "REFERENCE")
    private String reference;
	
	@Column(name = "`ORDER`")
    private Integer order;
    
}
