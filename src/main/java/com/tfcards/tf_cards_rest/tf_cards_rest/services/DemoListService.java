package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseBaseToPhraseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCommandToPhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list.IPhraseRepoList;

@Service
@Primary
@Profile({ "LIST_DB", "default" })
public class DemoListService implements IDemoService {

    private final IPhraseRepoList phrasesListRepo;
    private final PhraseCommandToPhraseBase phraseConverter;
    private final PhraseBaseToPhraseCommand phraseCmdConverter;

    public DemoListService(PhraseCommandToPhraseBase pPhraseConverter, IPhraseRepoList phrasesListRepo,
            PhraseBaseToPhraseCommand phraseCmdConverter) {
        this.phraseConverter = pPhraseConverter;
        this.phrasesListRepo = phrasesListRepo;
        this.phraseCmdConverter = phraseCmdConverter;
        this.phrasesListRepo.save(new PhraseBase("Hello {0}!"));
        this.phrasesListRepo.save(new PhraseBase("How is it going?"));
        this.phrasesListRepo.save(new PhraseBase("Hey there, {0}!"));
        this.phrasesListRepo.save(new PhraseBase("What's up bro?"));
    }

    @Override
    public PhraseBase get(Long id) {
        return phrasesListRepo.getById(id);
    }

    @Override
    public PhraseBase get(String pPhraseSubstr) {
        return this.phrasesListRepo.getAll().stream()
                .filter(p -> p.getPhrase().toLowerCase().contains(pPhraseSubstr.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Phrase with the given  was not found"));
    }

    @Override
    public PhraseBaseCommand create(PhraseBaseCommand newPhrase) {
        var phraseStored = this.phrasesListRepo.save(this.phraseConverter.convert(newPhrase));
        return this.phraseCmdConverter.convert(phraseStored);
    }

    @Override
    public Set<PhraseBaseCommand> getAll() {
        return this.phrasesListRepo.getAll().stream()
                .map(this.phraseCmdConverter::convert)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PhraseBaseCommand> getAll(Optional<Integer> limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}
