package org.dragberry.eshop.cms.service.impl;

import java.text.MessageFormat;

import org.dragberry.eshop.cms.security.JwtUserFactory;
import org.dragberry.eshop.dal.entity.UserAccount;
import org.dragberry.eshop.dal.repo.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private static final String USER_NOT_FOUND_ERR = "No user found with username  {0}!";

	@Autowired
    private UserAccountRepository userAccountRepo;
    
    @Autowired
    private JwtUserFactory factory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format(USER_NOT_FOUND_ERR, username));
        } else {
            return factory.create(user);
        }
    }
}
