package com.tfcards.tf_cards_rest.tf_cards_rest.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.tfcards.tf_cards_rest.tf_cards_rest.configuration.interceptors.ApiVersionsInterceptor;

@Configuration
public class ApiVersionsConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new ApiVersionsInterceptor()).addPathPatterns("/demo/**");
    }

}
