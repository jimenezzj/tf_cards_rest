package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommandV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IDemoServiceV2;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = { "/v2/demo" }, produces = { "application/com.jimenezzj.tfcards-v2+json",
        "application/com.jimenezzj.tfcards-v2+xml", MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class DemoResourceV2Controller {

    private final IDemoServiceV2 demoServ2;

    public DemoResourceV2Controller(IDemoServiceV2 pDemoServ2) {
        this.demoServ2 = pDemoServ2;
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Map<String, Object>> createPhrase(@Valid @RequestBody PhraseBaseCommandV2 newPhrase) {
        Map<String, Object> res = new HashMap<>();
        res.put("object", this.demoServ2.create(newPhrase));
        res.put("msg", "Phrase was saved successfully!");
        var newResourceUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(((PhraseBaseCommandV2) res.get("object")).getId())
                .toUri();
        return ResponseEntity.created(newResourceUri).build();
    }

    @GetMapping({ "/", "" })
    public Map<String, Object> getAll() {
        Map<String, Object> res = new HashMap<>();
        res.put("object", this.demoServ2.getAll());
        res.put("msg", "Phrases were fetched sucessfully!");
        return res;
    }

    // @GetMapping({ "/{id}" })
    // public Map<String, Object> getById(@PathVariable("id") Long pId) {
    // Map<String, Object> res = new HashMap<>();
    // var phraseFound = this.demoService.get(pId);
    // res.put("object", phraseFound);
    // res.put("msg", String.format("Phrase with id %s was fetched sucessfully!",
    // pId));
    // return res;
    // }

}
