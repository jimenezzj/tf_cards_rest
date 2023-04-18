package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCommandToPhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IDemoService;

import jakarta.validation.Valid;
import lombok.var;

@RestController
@RequestMapping(value = { "/v1/demo", "/demo" }, produces = { MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE, "application/com.jimenezzj.tfcards-v1+json",
        "application/com.jimenezzj.tfcards-v1+xml" })
public class DemoResourceController {

    private MessageSource msgSrc;
    private IDemoService demoService;
    private PhraseCommandToPhraseBase phraseConverter;

    @GetMapping({ "/hello" })
    public Map<String, String> getPhrase(@RequestParam(required = false, value = "") String name) {
        var res = new HashMap<String, String>();
        var defaultPhrase = "Hi there, {0}!";
        res.put("msg", defaultPhrase);
        if (name != null && !name.isBlank()) {
            var propsMsgArgs = Arrays.asList(name).toArray(String[]::new);
            String propPhrase = this.msgSrc.getMessage("demo.phrase", propsMsgArgs, defaultPhrase,
                    LocaleContextHolder.getLocale());
            res.put("msg", propPhrase);
        }
        return res;
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Map<String, Object>> createPhrase(@Valid @RequestBody PhraseBaseCommand newPhrase) {
        Map<String, Object> res = new HashMap<>();
        res.put("object", this.demoService.create(newPhrase));
        res.put("msg", "Phrase was saved successfully!");
        var newResourceUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(((PhraseBaseCommand) res.get("object")).getId())
                .toUri();
        return ResponseEntity.created(newResourceUri).build();
    }

    @GetMapping({ "/", "" })
    public Map<String, Object> getAll() {
        Map<String, Object> res = new HashMap<>();
        res.put("object", this.demoService.getAll());
        res.put("msg", "Phrases were fetched sucessfully!");
        return res;
    }

    @GetMapping({ "/{id}" })
    public Map<String, Object> getById(@PathVariable("id") Long pId) {
        Map<String, Object> res = new HashMap<>();
        var phraseFound = this.demoService.get(pId);
        res.put("object", phraseFound);
        res.put("msg", String.format("Phrase with id %s was fetched sucessfully!", pId));
        return res;
    }

    @Autowired
    public void setMsgSrc(MessageSource msgSrc) {
        this.msgSrc = msgSrc;
    }

    @Autowired
    public void setDemoService(IDemoService pDemoService) {
        this.demoService = pDemoService;
    }

    @Autowired
    public void setPhraseConverter(PhraseCommandToPhraseBase phraseConverter) {
        this.phraseConverter = phraseConverter;
    }

}
