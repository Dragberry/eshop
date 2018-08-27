package org.dragberry.eshop.dal.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ROLE")
@NamedQueries({
	@NamedQuery(
			name = Role.FIND_BY_MODULE_AND_ACTION_QUERY,
			query = "select r from Role r where r.module = :module and r.action = :action")
})
@TableGenerator(
		name = "ROLE_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "ROLE_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Cacheable(true)
public class Role extends AbstractEntity {
	
	private static final long serialVersionUID = 2965399771263638041L;

	private static final String ROLE_PREFIX = "ROLE_";

	public static final String FIND_BY_MODULE_AND_ACTION_QUERY = "Role.FindByModuleAndAction";

	@Id
	@Column(name = "ROLE_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ROLE_GEN")
	private Long entityKey;
	
	@Column(name = "MODULE")
	private String module;
	
	@Column(name = "ACTION")
	private String action;
	
	@Override
	public Long getEntityKey() {
		return entityKey;
	}
	
	@Override
	public void setEntityKey(Long entityKey) {
		this.entityKey = entityKey;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
		return ROLE_PREFIX + module + "_" + action;
	}

}
