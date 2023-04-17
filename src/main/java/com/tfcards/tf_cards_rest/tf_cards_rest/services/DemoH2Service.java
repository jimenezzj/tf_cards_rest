package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseBaseToPhraseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCommandToPhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;

@Service
public class DemoH2Service implements IDemoService {

    private final IDemoRepo demoRepo;
    private final PhraseCommandToPhraseBase phraseConverter;
    private final PhraseBaseToPhraseCommand phraseConverterCmd;

    public DemoH2Service(IDemoRepo demoRepo, PhraseCommandToPhraseBase pPhraseConverter, PhraseBaseToPhraseCommand pPhraseConverterCmd) {
        this.demoRepo = demoRepo;
        this.phraseConverter = pPhraseConverter;
        this.phraseConverterCmd = pPhraseConverterCmd;
    }

    @Override
    public PhraseBase get(Long id) {
        var foundPhrase = this.demoRepo.findById(id);
        if (foundPhrase.isEmpty())
            throw new RuntimeException("Phrase with id specified was not found");
        return foundPhrase.get();
    }

    @Override
    public PhraseBase get(String pPhraseSubstr) {
        var pFound = this.demoRepo.findByPhraseContaining(pPhraseSubstr);
        if (pFound == null)
            throw new RuntimeException("Phrase with the given  was not found");
        return pFound;
    }

    @Override
    public PhraseBaseCommand create(PhraseBaseCommand newPhrase) {
        var convetedPhrase = this.phraseConverter.convert(newPhrase);
        newPhrase = this.phraseConverterCmd.convert(this.demoRepo.save(convetedPhrase));
        return  newPhrase;
    }

    @Override
    public Set<PhraseBase> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public Set<PhraseBase> getAll(Optional<Integer> limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}
