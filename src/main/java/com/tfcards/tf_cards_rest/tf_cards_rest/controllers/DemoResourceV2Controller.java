package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cglib.core.Local;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseTranslationDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection.Lang;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IDemoServiceV2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = { "/v2/demo" }, produces = { "application/com.jimenezzj.tfcards-v2+json",
        "application/com.jimenezzj.tfcards-v2+xml", MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class DemoResourceV2Controller {

    public final static String BASE_PATH = "/v2/demo";
    public final static String GET_PHRASE_BY_ID = "/{id}";

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
        // Builds a location URI baesd on the current method
        var newResourceUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(p.getId())
                .toUri();
        // HATEOAS impl, decorates response with resource URI to guide the user
        EntityModel<Map<String, Object>> resLinkWrapp = EntityModel.of(res);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(this.getClass(),
                this.getById(p.getId(), false, false, "", Locale.getDefault()));
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

    @GetMapping({ GET_PHRASE_BY_ID })
    public MappingJacksonValue getById(@PathVariable("id") Long pId,
            @RequestParam(required = false) Boolean withId,
            @RequestParam(required = false, defaultValue = "false") Boolean trans,
            @RequestHeader(name = "Accept-Language", required = false) String header,
            Locale locale) {
        Map<String, Object> res = new HashMap<>();
        var phraseFound = this.demoServ2.get(pId, Optional.of(locale), trans);
        if (phraseFound.isEmpty()) {
            // ? Here die under control or do it into the service Impl
        }
        res.put("object", phraseFound.get());
        res.put("msg", String.format("Phrase with id %s was fetched sucessfully!", pId));
        // HATEOAS impl, decorates response with resource URI to guide the user
        EntityModel<Map<String, Object>> resMdl = EntityModel.of(res);
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(this.getClass(), this.getAll());
        resMdl.add(link.withRel("all-phrases"));
        // Apply some dymanic filter, to Dto annotaed with PhraseFilter id
        // TODO: Currently are fixed due to filter values are fixed
        MappingJacksonValue mappingJackVal = new MappingJacksonValue(resMdl);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("msg", "phraseType",
                "publishDate");
        FilterProvider filters = new SimpleFilterProvider().addFilter("PhraseFilter", filter);
        mappingJackVal.setFilters(filters);
        return mappingJackVal;
    }

    @PostMapping(path = "/translate/{phraseUUID}")
    public ResponseEntity<MappingJacksonValue> addPhraseTranslation(@PathVariable(name = "phraseUUID") UUID phraseId,
            @RequestBody @Valid PhraseTranslationDto body, @RequestHeader("Accept-Language") String header) {
        // TODO: Maybe res for accept-lang could be custome obj
        // ? If body lang is not specified it takes Accept-Header value
        var crrLang = header.split("-")[0].toUpperCase();
        // var langRegion = header.split("-")[0];
        if (body.getTo() == null) {
            if (!Lang.contains(crrLang))
                throw new RuntimeException("The language of the phrase is not supported");
            body.setTo(Lang.valueOf(crrLang));
        }
        // ? Try add the translation to an already existing phrase
        Map<String, Object> res = new HashMap<>();
        var newPhrase = this.demoServ2.addTranslationTo(phraseId, body);
        res.put("object", newPhrase);
        res.put("msg", String.format("Translation for %s was added sucessfully!", crrLang.toLowerCase()));
        // ? HATEOAS
        // ? MAPPING OBJECT
        MappingJacksonValue mappingJackVal = new MappingJacksonValue(res);
        mappingJackVal.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));

        return ResponseEntity.created(UriComponentsBuilder.fromUriString(GET_PHRASE_BY_ID).build(newPhrase.getId()))
                .body(mappingJackVal);
        // return ResponseEntity.ok(null);
    }

}
