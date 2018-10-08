package org.dragberry.eshop.dal.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PAGE")
@TableGenerator(
		name = "PAGE_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "PAGE_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class Page extends AuditableEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "PAGE_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PAGE_GEN")
	private Long entityKey;
	
	@Column(name = "TITLE")
    private String title;
	
	@Column(name = "BREADCRUMB_TITLE")
    private String breadcrumbTitle;
	
	@Column(name = "REFERENCE")
    private String reference;
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT")
    private String content;
	
}
