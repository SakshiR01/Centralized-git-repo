package com.gemini.Contripoint.filter;

import com.gemini.Contripoint.model.UserInfo;
import com.gemini.Contripoint.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@Component
public class JwtVerificationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String jwtToken;
        System.out.println("INSIDE FILTER");
        String authorizationHeader = request.getHeader("Authorization");
        UserInfo userInfo = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            userInfo = jwtTokenUtils.parseToken(jwtToken);
        }
        if (userInfo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = new User(userInfo.getUserId(), "", new ArrayList<>());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
