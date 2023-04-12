package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/v1/demo", "/demo" }, produces = { MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE, "application/com.jimenezzj.tfcards-v1+json",
        "application/com.jimenezzj.tfcards-v1+xml" })
public class DemoResourceController {

    private MessageSource msgSrc;

    @GetMapping({ "", "/" })
    public Map<String, String> getPhrase(@RequestParam(required = false, value = "") String name) {
        var res = new HashMap<String, String>();
        var defaultPhrase = "Hi, there!";
        res.put("msg", defaultPhrase);
        if (name != null && !name.isBlank()) {
            var propsMsgArgs = Arrays.asList(name).toArray(String[]::new);
            String propPhrase = this.msgSrc.getMessage("demo.phrase", propsMsgArgs, defaultPhrase,
                    LocaleContextHolder.getLocale());
            res.put("msg", propPhrase);
        }
        return res;
    }

    @Autowired
    public void setMsgSrc(MessageSource msgSrc) {
        this.msgSrc = msgSrc;
    }

}
