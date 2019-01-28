package com.paytm.inventory.config;

import com.paytm.inventory.filters.AuthenticationFilter;
import com.paytm.inventory.service.UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfiguration implements WebMvcConfigurer {
    @Bean
    public FilterRegistrationBean authenticationFilter(UserService userService) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter(userService));
        registration.addUrlPatterns("/auzmor/*");
        registration.setName("authenticationFilter");
        registration.setOrder(2);
        return registration;
    }
}
