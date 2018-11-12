package org.dragberry.eshop.cms.security;

import java.io.Serializable;

/**
 * Created by stephan on 20.03.16.
 */
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    
    private JwtUser userDetails;

    public JwtAuthenticationResponse(String token, JwtUser userDetails) {
        this.token = token;
        this.userDetails = userDetails;
    }

    public String getToken() {
        return this.token;
    }
    
    public JwtUser getUserDetails() {
		return userDetails;
	}
}
