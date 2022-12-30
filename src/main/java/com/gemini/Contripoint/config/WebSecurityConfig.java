package com.gemini.Contripoint.config;


import com.gemini.Contripoint.filter.JwtVerificationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtVerificationFilter jwtVerificationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and().authorizeRequests()
                .antMatchers("/api/authenticate", "*/topic/notification", "/socket/*", "/socket/*/*", "/socket/*/*/*").permitAll()
//                .antMatchers("/*", "/*/*", "/*/*/*", "/*/*/*/*").permitAll()
                .anyRequest().authenticated().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

