package com.tfcards.tf_cards_rest.tf_cards_rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;

@Component
public class PhraseCommandV2ToPhraseCommand implements Converter<PhraseDtoV2, PhraseBaseCommand> {

    @Override
    @Nullable
    public PhraseBaseCommand convert(PhraseDtoV2 source) {
        if (source == null)
            throw new RuntimeException("Object to data bind cannot be null");
        var phraseV1 = new PhraseBaseCommand();
        phraseV1.setId(source.getId());
        phraseV1.setPhrase(source.getMsg());
        phraseV1.setPhraseType(source.getPhraseType());
        return phraseV1;
    }

}
