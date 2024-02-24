package com.nnk.springboot.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

/**
 * Class for redirecting users after login success.
 */
public class CustomAuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    SimpleUrlAuthenticationSuccessHandler userSuccessHandler =
            new SimpleUrlAuthenticationSuccessHandler("/bidList/list");
    SimpleUrlAuthenticationSuccessHandler adminSuccessHandler =
            new SimpleUrlAuthenticationSuccessHandler("/admin");

    /**
     *
     * Function for redirecting the user to the correct page
     * after successfully login.
     * If user has ROLE_ADMIN then redirect to "/admin".
     * If user has ROLE_USER then redirect to "/bidList/list".
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (authorityName.equals("ROLE_ADMIN")) {
                // if the user is an ADMIN delegate to the adminSuccessHandler
                this.adminSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }
        }
        // if the user is not an admin delegate to the userSuccessHandler
        this.userSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }
}