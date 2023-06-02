package com.tfcards.tf_cards_rest.tf_cards_rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Component
public class PhraseToPhraseCommandV2 implements Converter<PhraseBase, PhraseDtoV2> {

    @Override
    @Nullable
    public PhraseDtoV2 convert(PhraseBase source) {
        var command = new PhraseDtoV2();
        command.setId(source.getId());
        command.setMsg(source.getPhrase());
        command.setPhraseType(source.getPhraseType());
        command.setAuthor(source.getAuthor());
        command.setPublishDate(source.getPublishDate());
        command.setPublishDate(source.getPublishDate());
        return command;
    }

}
