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
@Table(name = "IMAGE")
@TableGenerator(
		name = "IMAGE_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "IMAGE_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class Image extends BaseEntity {

    private static final long serialVersionUID = -4450634988574108295L;

	@Id
	@Column(name = "IMAGE_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IMAGE_GEN")
	private Long entityKey;
	
	@Column(name = "CONTENT")
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "NAME")
    private String name;
}
