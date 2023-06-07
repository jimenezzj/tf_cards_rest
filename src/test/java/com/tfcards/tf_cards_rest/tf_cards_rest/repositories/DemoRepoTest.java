package com.tfcards.tf_cards_rest.tf_cards_rest.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@ActiveProfiles("dev")
public class DemoRepoTest {

    @Autowired
    IDemoRepo demoRepo;

    @Test
    public void testSavePhrase() {
        PhraseBase savedPhrase = this.demoRepo.save(
                PhraseBase.builder()
                        .phrase("What' up, bro?")
                        .phraseType(EPhraseType.GREET)
                        .build());
        this.demoRepo.flush();
        assertNotNull(savedPhrase, "Returned phrase should not be null");
        assertNotNull(savedPhrase.getId());
        assertNotNull(savedPhrase.getPhrase());
    }

    @Test
    void testSaveWithMissingFields() {
        assertThrows(ConstraintViolationException.class, () -> {
            this.demoRepo.save(PhraseBase.builder().build());
            this.demoRepo.flush();
        });
    }

}
