package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IDemoServiceV2;

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
    public ResponseEntity<MappingJacksonValue> createPhrase(
            @Valid @RequestBody PhraseDtoV2 newPhrase) {
        var storedPhrase = this.demoServ2.create(newPhrase);
        if (storedPhrase.isEmpty()) {
            // ? Here die under control
        }
        var p = storedPhrase.get();
        Map<String, Object> res = new HashMap<>();
        res.put("body", storedPhrase);
        res.put("msg", "Phrase was saved successfully!");
        // Builds a location URI baesd on the current method
        var newResourceUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(p.getId())
                .toUri();
        // HATEOAS impl, decorates response with resource URI to guide the user
        EntityModel<Map<String, Object>> resLinkWrapp = EntityModel.of(res);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(this.getClass(),
                this.getById(p.getId(), false));
        resLinkWrapp.add(linkBuilder.withSelfRel());
        // Use Jackson to set dynamic filters
        MappingJacksonValue mappingJackVal = new MappingJacksonValue(resLinkWrapp);
        mappingJackVal.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));

        return ResponseEntity.created(newResourceUri).body(mappingJackVal);
    }

    @GetMapping({ "/", "" })
    public MappingJacksonValue getAll() {
        Map<String, Object> res = new HashMap<>();
        var fetchedLPhrases = this.demoServ2.getAll();
        res.put("object", fetchedLPhrases);
        res.put("msg", "Phrases were fetched sucessfully!");

        MappingJacksonValue mappingJacksonVal = new MappingJacksonValue(res);
        mappingJacksonVal.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));

        return mappingJacksonVal;
    }

    @GetMapping({ "/{id}" })
    public MappingJacksonValue getById(@PathVariable("id") Long pId,
            @RequestParam(required = false) Boolean withId) {
        Map<String, Object> res = new HashMap<>();
        var phraseFound = this.demoServ2.get(pId);
        if (phraseFound.isEmpty()) {
            // ? Here die under control
        }
        // HATEOAS impl, decorates response with resource URI to guide the user
        EntityModel<Map<String, Object>> resMdl = EntityModel.of(res);
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(this.getClass(), this.getAll());
        resMdl.add(link.withRel("all-phrases"));

        res.put("object", phraseFound.get());
        res.put("msg", String.format("Phrase with id %s was fetched sucessfully!", pId));
        // Apply some dymanic filter, to Dto annotaed with PhraseFilter id
        // TODO: Currently are fixed due to filter values are fixed
        MappingJacksonValue mappingJackVal = new MappingJacksonValue(resMdl);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("msg", "phraseType",
                "publishDate");
        FilterProvider filters = new SimpleFilterProvider().addFilter("PhraseFilter", filter);
        // FilterProvider filters = new
        // SimpleFilterProvider().setFailOnUnknownId(false);
        mappingJackVal.setFilters(filters);

        return mappingJackVal;
    }

}
