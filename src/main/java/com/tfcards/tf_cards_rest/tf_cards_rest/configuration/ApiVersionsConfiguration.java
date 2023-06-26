package com.tfcards.tf_cards_rest.tf_cards_rest.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tfcards.tf_cards_rest.tf_cards_rest.configuration.interceptors.ApiVersionsInterceptor;

@Configuration
public class ApiVersionsConfiguration extends WebMvcConfigurationSupport {

    public ApiVersionsConfiguration(ObjectMapper objectMapper) {
        objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new ApiVersionsInterceptor()).addPathPatterns("/demo/**");
    }
}
