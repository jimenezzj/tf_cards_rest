package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.clients.IMsTranslatorClient;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseTranslationDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.ETranslationType;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection.Lang;
import com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.EntityNotFoundException;
import com.tfcards.tf_cards_rest.tf_cards_rest.mappers.IPhraseMapper;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDropdownOptsRepo;

import static org.apache.commons.lang3.StringUtils.*;

@Service
@Profile({ "H2_DB", "MYSQL_DB" })
public class DemoH2ServiceV2 implements IDemoServiceV2 {

    private final IDemoRepo demoRepo;
    private final IPhraseMapper phraseMapper;
    private final IDropdownOptsRepo dropdownOptsRepo;
    private final IMsTranslatorClient msTranslatorClnt;

    public DemoH2ServiceV2(IDemoRepo demoRepo, IPhraseMapper pPhraseMapper, IDropdownOptsRepo dropdownOptsRepo,
            IMsTranslatorClient pMsTranslatorClient) {
        this.demoRepo = demoRepo;
        this.phraseMapper = pPhraseMapper;
        this.dropdownOptsRepo = dropdownOptsRepo;
        this.msTranslatorClnt = pMsTranslatorClient;
    }

    @Override
    public Optional<PhraseDtoV2> get(Long id) {
        return Optional.of(
                this.phraseMapper.phraseBaseToPhraseDtoV2(
                        this.demoRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Phrase with given id was not found"))));
    }

    @Override
    public Optional<PhraseDtoV2> get(Long id, Optional<Locale> locale) {
        if (locale.isEmpty())
            return this.get(id);
        var eLang = this.getSanatizeLang(locale.get());
        return Optional.of(
                this.phraseMapper.phraseBaseToPhraseDtoV2(
                        this.demoRepo.findByIdAndLangIs(id, eLang)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Phrase with given id and provided language was not found"))));
    }

    @Override
    public Optional<PhraseDtoV2> get(Long id, Optional<Locale> locale, Boolean autoTranslate) {
        Optional<PhraseDtoV2> phraseRes;
        if (autoTranslate == null || !autoTranslate || locale.isEmpty())
            return this.get(id, locale);
        else {
            try {
                phraseRes = this.get(id, locale);
                phraseRes.get().setTranslationType(ETranslationType.OWN);
            } catch (Exception e) {
                phraseRes = Optional.empty();
                Lang eLang = this.getSanatizeLang(locale.get());
                var foundPhrase = this.get(id).get();
                var transPhrase = this.msTranslatorClnt
                        .getTranslatedPhrase(
                                new PhraseTranslationDto(foundPhrase.getMsg(), foundPhrase.getLang(), eLang));
                foundPhrase.setLang(eLang);
                foundPhrase.setMsg(transPhrase.getText());
                foundPhrase.setTranslationType(ETranslationType.EXTERNAL);
                phraseRes = Optional.of(foundPhrase);
            }
        }
        return phraseRes;
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
        return this.create(newPhrase, null);
    }

    @Override
    public Optional<PhraseDtoV2> create(PhraseDtoV2 newPhrase, Locale locale) {
        newPhrase.setLang(this.getSanatizeLang(locale));
        return Optional.of(this.phraseMapper.phraseBaseToPhraseDtoV2(
                this.demoRepo.save(this.phraseMapper.phraseDtoV2ToPhraseBase(newPhrase))));
    }

    @Override
    public Optional<PhraseDtoV2> update(Long id, PhraseDtoV2 newPhrase) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public PhraseDtoV2 addTranslationTo(UUID idPhrase, PhraseTranslationDto pTranslationDto) {
        // ? it checks if there's any phrase with given phraseId
        var phraseCollection = this.demoRepo.findByPhraseIdIs(idPhrase);
        if (phraseCollection.isEmpty())
            throw new EntityNotFoundException(String.format("Phrase with given phraseID(%s) was not found",
                    idPhrase.toString()));
        // ? It checkt if one of the found phrases its exact the same withe new one
        // TODO: this could be a custom validation
        phraseCollection.forEach(p -> {
            var isPhraseEqual = containsAnyIgnoreCase(normalizeSpace(p.getPhrase()),
                    normalizeSpace(pTranslationDto.getPhraseToTranslate()));
            if (isPhraseEqual)
                throw new RuntimeException("Phrase with the same chars is already stored");
        });
        // ? It updates the found phrase and reset some values in order to hibernate can
        // store id
        var foundPhrase = phraseCollection.get(0);
        foundPhrase.setId(null);
        foundPhrase.setPhrase(pTranslationDto.getPhraseToTranslate());
        foundPhrase.setPublishDate(LocalDate.now());
        foundPhrase.setLang(pTranslationDto.getTo());
        return this.phraseMapper.phraseBaseToPhraseDtoV2(this.demoRepo.save(foundPhrase));
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
    public Optional<PhraseDtoV2> get(UUID phraseId, Optional<Locale> locale, Boolean autoTranslate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    private Lang getSanatizeLang(Locale locale) {
        // TODO: Maybe a validation would make this code resusable. Its ok but maybe
        // somethign else could be done
        var eLang = Lang.EN;
        if (locale == null || isBlank(stripToEmpty(locale.getLanguage())))
            return eLang;
        var notSupportedEx = new RuntimeException(
                "The language your are requesting is not supported. Use the trans query param to use auto translate");
        var crrLang = locale.getLanguage().toUpperCase();
        if (!Lang.contains(crrLang))
            throw notSupportedEx;
        eLang = Lang.valueOf(crrLang);
        var supportedLang = this.dropdownOptsRepo.findByCollectionNameAndValue(EDropdownCollection.API_LANG,
                eLang.toString());
        if (supportedLang.isEmpty())
            throw notSupportedEx;
        return eLang;
    }

}
