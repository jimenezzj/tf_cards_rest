package com.tfcards.tf_cards_rest.tf_cards_rest.clients;

import org.springframework.data.domain.Page;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseTranslationDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.TranslationResDto;

public interface IMsTranslatorClient {
    Page<PhraseDtoV2> translatePhrase(PhraseDtoV2 pDtoV2);

    TranslationResDto getTranslatedPhrase(PhraseTranslationDto phraseTranslationDto);
}
