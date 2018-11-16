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
@Table(name = "FILE")
@TableGenerator(
		name = "FILE_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "FILE_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class File extends AuditableEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "FILE_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "FILE_GEN")
	private Long entityKey;
	
	@Column(name = "PATH")
    private String path;
	
	@Column(name = "CONTENT_TYPE")
    private String contentType;
	
}
