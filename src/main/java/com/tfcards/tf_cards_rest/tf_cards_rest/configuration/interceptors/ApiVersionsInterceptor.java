package com.tfcards.tf_cards_rest.tf_cards_rest.configuration.interceptors;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

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
        Map<String, String> metaData = Collections.emptyMap();
        // try to get api version from Accept header
        if (versionHeader == null) {
            metaData = this.getMetaDataFromAccept(request);
            if (metaData.get("version") == null || metaData.get("version").isBlank())
                throw new ApiVersionNotFound();
            if (metaData.get("type") == null || metaData.get("type").isBlank())
                throw new ApiVersionNotFound("Type data request is invalid. It must be either json or xml");
            versionHeader = metaData.get("version");
        }
        var redirectPath = String.format("/v%s%s?%s", versionHeader, request.getServletPath(),
                request.getQueryString());
        if (!metaData.isEmpty())
            rewriteDataTypeRequested(request, response, metaData);
        response.sendRedirect(redirectPath);
        request.setAttribute("Accept", MediaType.TEXT_XML_VALUE);
        return validReq;
    }

    private void rewriteDataTypeRequested(HttpServletRequest req, HttpServletResponse res,
            Map<String, String> pMetaData) {
        String reqType = pMetaData.get("type");
        Pattern patternType = Pattern.compile(".*(json|xml)$");
        Matcher matcherType = patternType.matcher(reqType);
        if (!matcherType.find())
            throw new ApiVersionNotFound("MIME type request is invalid. It must be either json or xml");
        res.setHeader("Accept", pMetaData.get("header"));
        res.addHeader("Accept", "application/" + matcherType.group(1));
        req.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, MediaType.TEXT_XML_VALUE);
    }

    private Map<String, String> getMetaDataFromAccept(HttpServletRequest req) {
        Pattern patterMeta = Pattern.compile("^application\\/com\\.jimenezzj\\.tfcards-v(\\d)\\+(json|xml)$");
        var spliteratorHeaders = Spliterators.spliteratorUnknownSize(req.getHeaders("Accept").asIterator(),
                Spliterator.ORDERED);
        var headerMatched = StreamSupport.stream(spliteratorHeaders, false)
                .filter(h -> h.matches(patterMeta.pattern()))
                .findFirst().orElse(null);
        if (headerMatched == null)
            return Collections.emptyMap();
        Matcher matcherMeta = patterMeta.matcher(headerMatched);
        if (!matcherMeta.find())
            return Collections.emptyMap();
        return Map.of("header", headerMatched, "version", matcherMeta.group(1),
                "type", Optional.of(matcherMeta.group(2)).orElse(""));
    }

}
