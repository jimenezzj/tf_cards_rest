package com.tfcards.tf_cards_rest.tf_cards_rest.configuration.interceptors;

import java.util.Arrays;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.web.servlet.HandlerInterceptor;

import com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.ApiVersionNotFound;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiVersionsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean validReq = false;
        var versionHeader = request.getHeader("x-api-version");
        // try to get api version from Accept header
        if (versionHeader == null)
            versionHeader = this.getVersionFromHeader(request);
        if (versionHeader == null)
            throw new ApiVersionNotFound();
        var redirectPath = String.format("/v%s%s?%s", versionHeader, request.getServletPath(),
                request.getQueryString());
        response.sendRedirect(redirectPath);
        // request.getHeaders("Accept").asIterator().forEachRemaining(h ->
        // log.debug("Accept->", h););
        return validReq;
    }

    private String getVersionFromHeader(HttpServletRequest request) {
        String apiVersion = null;
        var spliteratorHeaders = Spliterators.spliteratorUnknownSize(request.getHeaders("Accept").asIterator(),
                Spliterator.ORDERED);
        Pattern pattern = Pattern.compile(".*-v(\\d+)+.*");
        var headersList = StreamSupport.stream(spliteratorHeaders, false)
                .filter(h -> pattern.matcher(h).find()).findFirst().orElse("");
        Matcher matcher = pattern.matcher(headersList);
        if (matcher.find())
            apiVersion = matcher.group(1);
        return apiVersion;
    }

}
