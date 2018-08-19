package org.dragberry.eshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dragberry.eshop.dal.entity.Role;
import org.dragberry.eshop.dal.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class RoleCache {

	private static final String UNDERSCORE = "_";

	@Autowired
	private RoleRepository roleDao;

	private final Map<String, Role> CACHE = new ConcurrentHashMap<>();
	private final Map<Role, SimpleGrantedAuthority> ROLES_GA_CACHE = new ConcurrentHashMap<>();

	public Role getRoleByFullName(String fullRoleName) {
		return CACHE.computeIfAbsent(fullRoleName, key -> {
			String[] roleParts = key.split(UNDERSCORE);
			return roleDao.findByModuleAndAction(roleParts[1], roleParts[2]);
		});
	}

	public String getRoleName(Role role) {
		return getGrantedAuthority(role).getAuthority();
	}
	
	public GrantedAuthority getGrantedAuthority(Role role) {
        return ROLES_GA_CACHE.computeIfAbsent(role, key -> new SimpleGrantedAuthority(key.toString()));
    }

}
