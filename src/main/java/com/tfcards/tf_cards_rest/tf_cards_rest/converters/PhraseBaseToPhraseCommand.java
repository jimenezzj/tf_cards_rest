package com.tfcards.tf_cards_rest.tf_cards_rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Component
public class PhraseBaseToPhraseCommand implements Converter<PhraseBase, PhraseBaseCommand> {

    @Override
    @Nullable
    public PhraseBaseCommand convert(PhraseBase source) {
        var phraseCmd = new PhraseBaseCommand();
        phraseCmd.setId(source.getId());
        phraseCmd.setPhrase(source.getPhrase());
        phraseCmd.setPhraseType(source.getPhraseType());
        return phraseCmd;
    }

}
