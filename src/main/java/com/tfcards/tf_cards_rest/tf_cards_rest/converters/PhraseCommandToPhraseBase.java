package com.tfcards.tf_cards_rest.tf_cards_rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Component
public class PhraseCommandToPhraseBase implements Converter<PhraseBaseCommand, PhraseBase> {

    @Override
    @Nullable
    public PhraseBase convert(PhraseBaseCommand source) {
        PhraseBase newPhrase = new PhraseBase();
        newPhrase.setId(source.getId());
        newPhrase.setPhrase(source.getPhrase());
        newPhrase.setPhraseType(source.getPhraseType());
        return newPhrase;
    }

}
