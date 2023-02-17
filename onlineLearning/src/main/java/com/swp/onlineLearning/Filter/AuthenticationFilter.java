package com.swp.onlineLearning.Filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swp.onlineLearning.Config.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class AuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String token;
//        if(request.getMethod() == "get"){
//            token = request.getParameter("token");
//        }else{
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
//            token = String.valueOf(jsonNode.get("token")).substring(1,String.valueOf(jsonNode.get("token")).length()-1);
//        }
//        System.out.println(token);
//        UsernamePasswordAuthenticationToken authentication;
//        if (token != null) {
//            String gmail = JWTUtil.getIdFromToken(token);
//            if (gmail != null) {
//                authentication = new UsernamePasswordAuthenticationToken(gmail, null, new ArrayList<>());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }

        filterChain.doFilter(request, response);
    }
}
