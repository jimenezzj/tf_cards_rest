package com.tfcards.tf_cards_rest.tf_cards_rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Component
public class PhraseCmdV2ToPhrase implements Converter<PhraseDtoV2, PhraseBase> {

    @Override
    @Nullable
    public PhraseBase convert(PhraseDtoV2 source) {
        if(source == null) throw new RuntimeException("Object to data bind cannot be null");
        var entity = new PhraseBase();
        entity.setId(source.getId());
        entity.setPhrase(source.getMsg());
        entity.setPhraseType(source.getPhraseType());
        entity.setAuthor(source.getAuthor());
        entity.setPublishDate(source.getPublishDate());
        entity.setCreatedAt(source.getCreatedAt());
        return entity;
    }

}
