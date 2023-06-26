package com.tfcards.tf_cards_rest.tf_cards_rest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Mapper
public interface IPhraseMapper {

    PhraseBaseCommand phraseBaseToPhraseDtoV1(PhraseBase phraseBase);

    PhraseBase phraseDtoV1ToPhraseBase(PhraseBaseCommand phraseDto);

    @Mapping(source = "phraseBase.phrase", target = "msg")
    PhraseDtoV2 phraseBaseToPhraseDtoV2(PhraseBase phraseBase);

    @Mapping(source = "phraseDto.msg", target = "phrase")
    PhraseBase phraseDtoV2ToPhraseBase(PhraseDtoV2 phraseDto);

}
