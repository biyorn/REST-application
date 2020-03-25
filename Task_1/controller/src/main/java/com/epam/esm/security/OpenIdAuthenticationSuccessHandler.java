package com.epam.esm.security;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.security.token.TokenUtil;
import com.epam.esm.service.impl.UserDetailsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class OpenIdAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Long USER_ROLE_ID = 2L;
    private static final String USER_ROLE_NAME = "user";

    private UserDetailsServiceImpl userDetailsService;
    private TokenUtil tokenUtil;

    public OpenIdAuthenticationSuccessHandler(UserDetailsServiceImpl userDetailsService, TokenUtil tokenUtil) {
        this.userDetailsService = userDetailsService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = authenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        UserDetails userDetails = userDetailsService.findOrRegister(getUser(email));

        String token = generateToken(userDetails);
        response.addHeader("Authorization", "Bearer " + token);
    }

    private UserEntityDTO getUser(String email) {
        UserEntityDTO local = new UserEntityDTO();
        local.setLogin(email);
        local.setRoles(Set.of(getDefaultRole()));
        return local;
    }

    private RoleDTO getDefaultRole() {
        RoleDTO role = new RoleDTO();
        role.setId(USER_ROLE_ID);
        role.setName(USER_ROLE_NAME);
        return role;
    }

    private String generateToken(UserDetails userDetails) {
        return tokenUtil.generateAccessToken(userDetails);
    }
}
