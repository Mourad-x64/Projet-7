package com.nnk.springboot.config;

import com.nnk.springboot.services.CustomUserDetailsService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuration class for spring security
 *
 */
@Configuration
@EnableWebSecurity
@EnableEncryptableProperties
public class SecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Initialize Bean for passwordEncoder (Bcrypt).
     * @return
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * this function is responsible for all the security
     * (protecting the application URLs, validating submitted username and passwords,
     * redirecting to the login form, and so on) within the application.
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/static/**", "/templates/**").permitAll()
                                .requestMatchers("/", "/home").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("ADMIN")
                                .anyRequest().authenticated()


                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .defaultSuccessUrl("/bidList/list", true)
                                .loginProcessingUrl("/login")
                                .successHandler(new CustomAuthenticationSuccessHandler())
                                .permitAll()

                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );


        return http.build();
    }

    /**
     * Function to set the password encoder (Bcrypt)
     * and userDetailService for the custom login in spring security.
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}