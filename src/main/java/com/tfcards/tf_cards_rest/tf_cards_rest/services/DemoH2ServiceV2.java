package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCmdV2ToPhrase;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseToPhraseCommandV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.mappers.IPhraseMapper;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;

@Service
@Profile({ "H2_DB", "MYSQL_DB" })
public class DemoH2ServiceV2 implements IDemoServiceV2 {

    private final IDemoRepo demoRepo;
    private final IPhraseMapper phraseMapper;

    public DemoH2ServiceV2(IDemoRepo demoRepo, IPhraseMapper pPhraseMapper) {
        this.demoRepo = demoRepo;
        this.phraseMapper = pPhraseMapper;
    }

    @Override
    public Optional<PhraseDtoV2> get(Long id) {
        return Optional.of(
                this.phraseMapper.phraseBaseToPhraseDtoV2(
                        this.demoRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Phrase with given id was not found"))));
    }

    @Override
    public Optional<PhraseDtoV2> get(String pPhraseSubstr) {
        var foundPhrase = this.demoRepo.findByPhraseContaining(pPhraseSubstr);
        if (foundPhrase.isEmpty())
            throw new RuntimeException("Phrase with given id was not found");
        return foundPhrase.map(this.phraseMapper::phraseBaseToPhraseDtoV2);
    }

    @Override
    public Set<PhraseDtoV2> getAll() {
        return this.getAll(Optional.of(50));
    }

    @Override
    public Set<PhraseDtoV2> getAll(Optional<Integer> limit) {
        if (limit.isEmpty())
            limit = Optional.of(10);
        return this.demoRepo.findAll().stream().limit(limit.get())
                .map(this.phraseMapper::phraseBaseToPhraseDtoV2)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<PhraseDtoV2> create(PhraseDtoV2 newPhrase) {
        return Optional.of(this.phraseMapper.phraseBaseToPhraseDtoV2(
                this.demoRepo.save(this.phraseMapper.phraseDtoV2ToPhraseBase(newPhrase))));
    }

    @Override
    public Optional<PhraseDtoV2> update(Long id, PhraseDtoV2 newPhrase) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
