package com.swp.onlineLearning.Config;

import com.swp.onlineLearning.Filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${role.user}")
    private String roleUserName;
    @Value("${role.courseExpert}")
    private String roleCourseExpert;
    @Value("${role.sale}")
    private String roleSale;
    @Value("${role.admin}")
    private String roleAdmin;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
        http.authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()));
    }
//.antMatchers("/api/admin/**","/api/type_course/**",
//                     "/api/course/create","/api/course/admin_get","/api/course/update_pakage/id={id}", "/api/course/update_pakage/id={id}","/api/course/create/change_status", "/api/course/delete", "/api/course/get_purchase_course",
//                     "/api/voucher/**","/api/dashboard",
//                     "/api/account", "/api/account/change_role/id={id}","/api/account/course_expert","/api/account/get_user","/api/account/update","/api/account/change_password",
//                     "/api/comment/report").hasRole(roleAdmin.substring(5))
//            .antMatchers("/api/marketing/**","/api/voucher/**","/api/course/get_purchase_course","/api/comment/report").hasRole(roleSale.substring(5))
//            .antMatchers("/api/blog/**","/api/course/update_pakage/id={id}","/api/course/admin_get","/api/comment/**").hasRole(roleCourseExpert.substring(5))
//            .antMatchers("/api/course/enroll","/api/course/lesson",
//                                 "/api/course/rating/create", "/api/course/getAllVoucherForUser",
//                                 "/api/blog/**","/api/lesson/**",
//                                 "/api/account/get_user","/api/account/update","/api/account/change_password","/api/account/getpayment",
//                                 "/api/comment/**").hasRole(roleUser.substring(5))
//            .antMatchers("/api/auth/**","/api/common/**", "/api/comment/get").permitAll()
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
