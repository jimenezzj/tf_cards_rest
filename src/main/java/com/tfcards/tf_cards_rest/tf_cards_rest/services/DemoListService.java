package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCommandToPhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Service
@Primary
public class DemoListService implements IDemoService {

    private static Set<PhraseBase> phrasesList = new HashSet<>();

    static {
        phrasesList.add(new PhraseBase(Long.valueOf(phrasesList.size() + 1), "Hello {0}!"));
        phrasesList.add(new PhraseBase(Long.valueOf(phrasesList.size() + 1), "How is it going?"));
        phrasesList.add(new PhraseBase(Long.valueOf(phrasesList.size() + 1), "Hey there, {0}!"));
        phrasesList.add(new PhraseBase(Long.valueOf(phrasesList.size() + 1), "What's up bro?"));
    }

    private final PhraseCommandToPhraseBase phraseConverter;

    public DemoListService(PhraseCommandToPhraseBase pPhraseConverter) {
        this.phraseConverter = pPhraseConverter;
    }

    @Override
    public PhraseBase get(Long id) {
        return phrasesList.stream().filter(p -> p.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Phrase with the given id was not found"));
        // ? TODO: add custom exception
    }

    @Override
    public PhraseBase get(String pPhraseSubstr) {
        return phrasesList.stream().filter(p -> p.getPhrase().toLowerCase().contains(pPhraseSubstr.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Phrase with the given  was not found"));
    }

    @Override
    public PhraseBaseCommand create(PhraseBaseCommand newPhrase) {
        newPhrase.setId(Long.valueOf(phrasesList.size() + 1));
        phrasesList.add(this.phraseConverter.convert(newPhrase));
        return newPhrase;
    }

    @Override
    public Set<PhraseBase> getAll() {
        return phrasesList;
    }

    @Override
    public Set<PhraseBase> getAll(Optional<Integer> limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}
