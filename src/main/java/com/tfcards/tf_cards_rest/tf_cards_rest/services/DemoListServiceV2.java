package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseTranslationDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCommandV2ToPhraseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection.Lang;
import com.tfcards.tf_cards_rest.tf_cards_rest.mappers.IPhraseMapper;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list.IPhraseRepoList;

@Service
@Profile({ "LIST_DB" })
public class DemoListServiceV2 implements IDemoServiceV2 {

    private final IPhraseRepoList phraseRepo;
    private final IPhraseMapper phraseMapper;
    public DemoListServiceV2(IPhraseRepoList phraseRepo, IPhraseMapper pPhraseMapper,
            PhraseCommandV2ToPhraseCommand phraseBaseToPhraseDtoV2) {
        this.phraseRepo = phraseRepo;
        this.phraseMapper = pPhraseMapper;
    }

    @Override
    public Optional<PhraseDtoV2> get(Long id) {
        // TODO: use custom exception
        var foundPhrase = this.phraseRepo.getById(id);
        if (foundPhrase == null)
            throw new RuntimeException("There's no such phrase with given id");
        return Optional.of(this.phraseMapper.phraseBaseToPhraseDtoV2(foundPhrase));
    }

    @Override
    public Optional<PhraseDtoV2> get(String pPhraseSubstr) {
        return Optional.of(
                this.phraseMapper.phraseBaseToPhraseDtoV2(
                        this.phraseRepo.findByPhraseContaining(pPhraseSubstr)));
    }

    @Override
    public Set<PhraseDtoV2> getAll() {
        return this.getAll(Optional.of(Integer.MAX_VALUE));
    }

    @Override
    public Set<PhraseDtoV2> getAll(Optional<Integer> limit) {
        if (limit.isEmpty())
            limit = Optional.of(10);
        return this.phraseRepo.getAll().stream().limit(limit.get())
                .map(this.phraseMapper::phraseBaseToPhraseDtoV2)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<PhraseDtoV2> create(PhraseDtoV2 newPhrase) {
        var pConverted = this.phraseMapper.phraseDtoV2ToPhraseBase(newPhrase);
        return Optional.of(
                this.phraseMapper.phraseBaseToPhraseDtoV2(this.phraseRepo.save(pConverted)));
    }

    @Override
    public Optional<PhraseDtoV2> update(Long id, PhraseDtoV2 newPhrase) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public PhraseDtoV2 addTranslationTo(UUID idPhrase, PhraseTranslationDto pTranslationDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTranslationTo'");
    }

	@Override
	public Optional<PhraseDtoV2> get(Long id, Optional<Locale> locale) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'get'");
	}

	@Override
	public Optional<PhraseDtoV2> get(UUID phraseId, Optional<Locale> locale) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'get'");
	}

	@Override
	public Set<PhraseDtoV2> getAll(UUID phraseId, Optional<Locale> locale) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAll'");
	}

    @Override
    public Optional<PhraseDtoV2> get(Long id, Optional<Locale> locale, Boolean autoTranslate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Optional<PhraseDtoV2> get(UUID phraseId, Optional<Locale> locale, Boolean autoTranslate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }


}
