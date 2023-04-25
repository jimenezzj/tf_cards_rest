package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommandV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCmdV2ToPhrase;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseToPhraseCommandV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;

@Service
@Profile({ "H2_DB", "MYSQL_DB" })
public class DemoH2ServiceV2 implements IDemoServiceV2 {

    private final IDemoRepo demoRepo;
    private final PhraseToPhraseCommandV2 phraseCmdConverterV2;
    private final PhraseCmdV2ToPhrase phraseConverterV2;

    public DemoH2ServiceV2(IDemoRepo demoRepo, PhraseToPhraseCommandV2 phraseCmdConverterV2,
            PhraseCmdV2ToPhrase phraseConverterV2) {
        this.demoRepo = demoRepo;
        this.phraseCmdConverterV2 = phraseCmdConverterV2;
        this.phraseConverterV2 = phraseConverterV2;
    }

    @Override
    public PhraseBaseCommandV2 get(Long id) {
        var foundPhrase = this.demoRepo.findById(id);
        if (foundPhrase.isEmpty())
            throw new RuntimeException("Phrase with given id was not found");
        return this.phraseCmdConverterV2.convert(foundPhrase.get());
    }

    @Override
    public PhraseBaseCommandV2 get(String pPhraseSubstr) {
        var foundPhrase = this.demoRepo.findByPhraseContaining(pPhraseSubstr);
        if (foundPhrase == null)
            throw new RuntimeException("Phrase with given id was not found");
        return this.phraseCmdConverterV2.convert(foundPhrase);
    }

    @Override
    public Set<PhraseBaseCommandV2> getAll() {
        return this.getAll(Optional.of(50));
    }

    @Override
    public Set<PhraseBaseCommandV2> getAll(Optional<Integer> limit) {
        if (limit.isEmpty())
            limit = Optional.of(10);
        return this.demoRepo.findAll().stream().limit(limit.get())
                .map(this.phraseCmdConverterV2::convert)
                .collect(Collectors.toSet());
    }

    @Override
    public PhraseBaseCommandV2 create(PhraseBaseCommandV2 newPhrase) {
        var savedEntity = this.demoRepo.save(this.phraseConverterV2.convert(newPhrase));
        return this.phraseCmdConverterV2.convert(savedEntity);
    }

}
