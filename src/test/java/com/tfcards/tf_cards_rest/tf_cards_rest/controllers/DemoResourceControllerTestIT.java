package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.EntityNotFoundException;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;

@SpringBootTest
@ActiveProfiles("dev")
public class DemoResourceControllerTestIT {

    @Autowired
    DemoResourceController demoController;

    @Autowired
    IDemoRepo demoRepo;

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
}
