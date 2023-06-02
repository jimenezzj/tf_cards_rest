package com.tfcards.tf_cards_rest.tf_cards_rest.mappers;

import org.mapstruct.Mapper;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Mapper
public interface IPhraseMapper {

    PhraseBaseCommand phraseBaseToPhraseDtoV1(PhraseBase phraseBase);

    PhraseBase phraseDtoV1ToPhraseBase(PhraseBaseCommand phraseDto);

    PhraseDtoV2 phraseBaseToPhraseDtoV2(PhraseBase phraseBase);

    PhraseBase phraseDtoV2ToPhraseBase(PhraseDtoV2 phraseDto);

}
