package com.tfcards.tf_cards_rest.tf_cards_rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommandV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Component
public class PhraseToPhraseCommandV2 implements Converter<PhraseBase, PhraseBaseCommandV2> {

    @Override
    @Nullable
    public PhraseBaseCommandV2 convert(PhraseBase source) {
        var command = new PhraseBaseCommandV2();
        command.setId(source.getId());
        command.setMsg(source.getPhrase());
        command.setPhraseType(source.getPhraseType());
        command.setAuthor(source.getAuthor());
        command.setPublishDate(source.getPublishDate());
        command.setPublishDate(source.getPublishDate());
        return command;
    }

}
