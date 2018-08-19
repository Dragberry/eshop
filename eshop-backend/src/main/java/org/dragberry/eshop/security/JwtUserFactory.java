package org.dragberry.eshop.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.dragberry.eshop.dal.entity.Role;
import org.dragberry.eshop.dal.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUserFactory {

	@Autowired
	private RoleCache roleCache;
	
    public JwtUser create(UserAccount user) {
        return new JwtUser(
                user.getEntityKey(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles()),
                user.getEnabled(),
                user.getLastPasswordResetDate()
        );
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> authorities) {
        return authorities.stream().map(roleCache::getGrantedAuthority).collect(Collectors.toList());
    }
}
