package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommandV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCmdV2ToPhrase;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseToPhraseCommandV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list.IPhraseRepoList;

@Service
@Profile({ "LIST_DB" })
public class DemoListServiceV2 implements IDemoServiceV2 {

    private final IDemoService demoServ;
    private final IPhraseRepoList phraseRepo;
    private final PhraseCmdV2ToPhrase phraseConverterv2;
    private final PhraseToPhraseCommandV2 phraseCmdConverterv2;

    public DemoListServiceV2(PhraseCmdV2ToPhrase phraseConverterv2, IDemoService demoServ,
            PhraseToPhraseCommandV2 pPhraseCmdConverterV2, IPhraseRepoList phraseRepo) {
        this.phraseConverterv2 = phraseConverterv2;
        this.demoServ = demoServ;
        this.phraseCmdConverterv2 = pPhraseCmdConverterV2;
        this.phraseRepo = phraseRepo;
    }

    @Override
    public PhraseBaseCommandV2 get(Long id) {
        var foundPhrase = this.demoServ.get(id);
        // TODO: use custom exception
        if (foundPhrase == null)
            throw new RuntimeException();
        return this.phraseCmdConverterv2.convert(foundPhrase);
    }

    @Override
    public PhraseBaseCommandV2 get(String pPhraseSubstr) {
        return this.phraseCmdConverterv2.convert(this.demoServ.get(pPhraseSubstr));
    }

    @Override
    public Set<PhraseBaseCommandV2> getAll() {
        return this.getAll(Optional.of(Integer.MAX_VALUE));
    }

    @Override
    public Set<PhraseBaseCommandV2> getAll(Optional<Integer> limit) {
        if (limit.isEmpty())
            limit = Optional.of(10);
        return this.phraseRepo.getAll().stream().limit(limit.get())
                .map(this.phraseCmdConverterv2::convert)
                .collect(Collectors.toSet());
    }

    @Override
    public PhraseBaseCommandV2 create(PhraseBaseCommandV2 newPhrase) {
        var pConverted = this.phraseConverterv2.convert(newPhrase);
        var storedPhrase = this.phraseRepo.save(pConverted);
        return this.phraseCmdConverterv2.convert(storedPhrase);
    }

}
