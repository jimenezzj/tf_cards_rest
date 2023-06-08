package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;
import com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.EntityNotFoundException;
import com.tfcards.tf_cards_rest.tf_cards_rest.mappers.IPhraseMapper;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;

import jakarta.validation.ConstraintViolationException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

@SpringBootTest
@ActiveProfiles("dev")
public class DemoResourceControllerTestIT {

    private static final String USERNAME = "admin";
    private static final String USER_PASS = "dummyadmin";

    @Autowired
    DemoResourceController demoController;

    @Autowired
    IDemoRepo demoRepo;

    @Autowired
    IPhraseMapper phraseMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testPatchPhraseFull() throws Exception {
        var existPhrase = this.demoRepo.findAll().get(0);
        var foundPhrase = this.phraseMapper.phraseBaseToPhraseDtoV1(existPhrase);
        foundPhrase.setPhraseType(null);
        mockMvc.perform(
                patch("/v1/demo/{id}", foundPhrase.getId())
                        .with(httpBasic(USERNAME, USER_PASS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objMapper.writeValueAsString(foundPhrase)))
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAll() {
        var res = demoController.getAll();
        assertNotEquals(0, res.size());
    }

    @Test
    @Transactional
    @Rollback
    void testGetEmptyList() {
        this.demoRepo.deleteAll();
        var phrasesListRes = this.demoController.getAll();
        Set<PhraseBaseCommand> phrasesList = (Set<PhraseBaseCommand>) phrasesListRes.get("object");
        assertEquals(0, phrasesList.size());
    }

    @Test
    void testGetById() {
        PhraseBase phraseEnty = this.demoRepo.findAll().get(0);
        Map<String, Object> res = this.demoController.getById(phraseEnty.getId());
        assertNotNull(res);
        assertNotNull(res.get("object"));
        assertNotNull(res.get("msg"));
        assertEquals(res.get("msg").getClass(), String.class);
    }

    @Test
    void testNotFoundPhrase() {
        assertThrows(EntityNotFoundException.class, () -> {
            this.demoController.getById(-1L);
        }, "Expected result is controller method throws a custom exception");
    }

    @Test
    void testSavePhrase() {
        var newPhrase = PhraseBaseCommand.builder().phrase("Que pasa chavales?").phraseType(EPhraseType.EXPRESSION)
                .build();
        var createPhraseRes = this.demoController.createPhrase(newPhrase);
        assertNotNull(createPhraseRes);
        assertNotNull(createPhraseRes.getBody());
        assertEquals(createPhraseRes.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(createPhraseRes.getHeaders().getLocation());
        assertNotNull(createPhraseRes.getBody().get("object"));
        assertNotNull(createPhraseRes.getBody().get("msg"));
        assertEquals(createPhraseRes.getBody().get("msg").getClass(), String.class);
        //
        var locationArr = createPhraseRes.getHeaders().getLocation().getPath().split("/");
        Long newPhraseId = Long.valueOf(locationArr[locationArr.length - 1]);
        assertEquals(newPhraseId.getClass(), Long.class);
        //
        var storedPhrase = this.demoRepo.findById(newPhraseId);
        assertNotNull(storedPhrase);
    }

    @Test
    void testUpdatePhrase() {
        var existPhrase = this.demoRepo.findAll().get(0);
        var existPhraseDto = phraseMapper.phraseBaseToPhraseDtoV1(existPhrase);
        existPhraseDto.setPhrase("Acknowledge me!");
        existPhraseDto.setPhraseType(EPhraseType.GRATITUDE);
        var upRes = this.demoController.updatePhrase(existPhrase.getId(), existPhraseDto);
        var upBody = upRes.getBody();
        assertNotNull(upBody);
        assertEquals(HttpStatus.OK, upRes.getStatusCode());
        PhraseBaseCommand bodyObj = (PhraseBaseCommand) upBody.get("object");
        assertNotNull(bodyObj);
        assertNotEquals(existPhrase.getPhrase(), bodyObj.getPhrase());
        assertNotEquals(existPhrase.getPhraseType(), bodyObj.getPhraseType());
    }

    @Test
    void testUpdateNotFoundPhrase() {
        assertThrows(EntityNotFoundException.class, () -> {
            this.demoController.updatePhrase(-1L, PhraseBaseCommand.builder().phrase("Random Greeting Phrase")
                    .phraseType(EPhraseType.EXPRESSION).build());
        });
    }

    @Test
    void testPatchPhrase() {
        var foundPhrase = this.demoRepo.findAll().get(0);
        var foundPhraseMap = this.phraseMapper.phraseBaseToPhraseDtoV1(foundPhrase);
        var updatedPhrase = "Heeeeey Randyyy!";
        foundPhraseMap.setPhraseType(null);
        foundPhraseMap.setPhrase(updatedPhrase);
        var patchRes = this.demoController.patchPhrase(foundPhraseMap.getId(), foundPhraseMap);
        assertNotNull(patchRes);
        assertEquals(patchRes.getStatusCode(), HttpStatus.NO_CONTENT);
        foundPhrase = this.demoRepo.findById(foundPhraseMap.getId()).orElse(null);
        assertEquals(updatedPhrase, foundPhrase.getPhrase(), "Should be the same message");
    }

    @Test
    void testPatchInvalidPhrase() {
        var ex = assertThrowsExactly(TransactionSystemException.class, () -> {
            var foundPhrase = this.demoRepo.findAll().get(0);
            var foundPhraseMap = this.phraseMapper.phraseBaseToPhraseDtoV1(foundPhrase);
            var updatedPhrase = "Brooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo";
            foundPhraseMap.setPhraseType(null);
            foundPhraseMap.setPhrase(updatedPhrase);
            this.demoController.patchPhrase(foundPhraseMap.getId(), foundPhraseMap);
        });
        assertInstanceOf(ConstraintViolationException.class, ex.getCause().getCause());
        var exCons = (ConstraintViolationException) ex.getCause().getCause();
        assertTrue(exCons.getConstraintViolations().size() >= 1);
    }
}
