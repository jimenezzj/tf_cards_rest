package com.tfcards.tf_cards_rest.tf_cards_rest.configuration.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.ApiVersionNotFound;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiVersionsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean validReq = false;
        var versionHeader = request.getHeader("x-api-version");
        if (versionHeader == null)
            throw new ApiVersionNotFound();
        else {
            var redirectPath = String.format("/v%s%s", versionHeader, request.getServletPath());
            response.sendRedirect(redirectPath);
        }
        return validReq;
    }

}
