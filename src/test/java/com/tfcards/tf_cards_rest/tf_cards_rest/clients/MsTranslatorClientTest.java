package com.tfcards.tf_cards_rest.tf_cards_rest.clients;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;

@SpringBootTest
@ActiveProfiles("dev")
public class MsTranslatorClientTest {

    @Autowired
    MsTranslatorClientImpl msTranslator;

    public PhraseDtoV2 getMockPhraseDtoV2() {
        return PhraseDtoV2.builder().msg("Let me talk to you").phraseType(EPhraseType.ANGER).author("LA Knight")
                .publishDate(LocalDate.of(2021, Month.JUNE, 20))
                .createdAt(LocalDate.of(2020, Month.FEBRUARY, 20)).build();
    }

    @Test
    public void testTranslatePhrase() {
        this.msTranslator.translatePhrase(this.getMockPhraseDtoV2());
    }

}
