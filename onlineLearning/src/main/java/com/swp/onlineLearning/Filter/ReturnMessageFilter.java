package com.swp.onlineLearning.Filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;

public class ReturnMessageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//            chain.doFilter(request, response);
//        } finally {
//            HttpServletResponse resp = (HttpServletResponse) response;
//            HashMap<String, Object> json = response.get
//            resp.setStatus(205);
//            String type = "false";
//            try{
//                type = json.get("type").toString();
//            }catch (Exception e){
//                log.error("type value of save account message unavailable \n" +e.getMessage());
//            }
//
//            if(type.equals("true")){
//                return new ResponseEntity<>(json, HttpStatus.OK);
//            }else{
//                return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
//            }
//        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
