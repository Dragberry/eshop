package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity extends AbstractEntity {

	private static final long serialVersionUID = -6408937559886379887L;
	
	@Version
	@Column(name = "VERSION")
	private Long version;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
