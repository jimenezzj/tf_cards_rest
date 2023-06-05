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

import jakarta.validation.Valid;

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
    }

    @Override
    public PhraseBaseCommand get(Long id) {
        return this.phraseCmdConverter.convert(phrasesListRepo.getById(id));
    }

    @Override
    public PhraseBaseCommand get(String pPhraseSubstr) {
        return this.phrasesListRepo.getAll().stream()
                .filter(p -> p.getPhrase().toLowerCase().contains(pPhraseSubstr.toLowerCase()))
                .findFirst()
                .map(this.phraseCmdConverter::convert)
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

    @Override
    public PhraseBaseCommand update(PhraseBaseCommand phraseToUpdate) {
        var foundPhrase = this.get(phraseToUpdate.getId());
        if (phraseToUpdate.getId() == null || foundPhrase.getId() == null)
            throw new RuntimeException("Phrase with such a id could not be found");
        var newBasePhrase = this.phraseConverter.convert(phraseToUpdate);
        newBasePhrase.setId(foundPhrase.getId());
        return this.phraseCmdConverter.convert(this.phrasesListRepo.save(newBasePhrase));
    }

    @Override
    public PhraseBaseCommand patchPhrase(Long id, @Valid PhraseBaseCommand patchedPhrase) {
        var foundPhrase = this.phrasesListRepo.getById(id);
        if (foundPhrase == null)
            throw new RuntimeException("Phrase with such a id could not be found");
        if (patchedPhrase.getPhrase() != null)
            foundPhrase.setPhrase(patchedPhrase.getPhrase());
        if (patchedPhrase.getPhraseType() != null)
            foundPhrase.setPhraseType(patchedPhrase.getPhraseType());
        this.phrasesListRepo.save(this.phraseConverter.convert(patchedPhrase));
        return null;
    }

}
