package org.dragberry.eshop.dal.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 54446865118204573L;
	
	public abstract Long getEntityKey();

	public abstract void setEntityKey(Long entityKey);
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append("[").append(getEntityKey()).append("]=[\n");
		for (Method method : getClass().getMethods()) {
			if (method.getName().startsWith("get") && !"getClass".equals(method.getName())) {
				sb.append("\t[");
				sb.append(method.getName().replace("get", StringUtils.EMPTY));
				sb.append("=");
				try {
					sb.append(method.invoke(this));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					sb.append("error");
				}
				sb.append("]\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
}
