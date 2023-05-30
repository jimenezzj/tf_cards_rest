package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseBaseToPhraseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCommandToPhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;

import jakarta.validation.Valid;

@Service
@Profile({ "H2_DB", "MYSQL_DB" })
public class DemoH2Service implements IDemoService {

    private final IDemoRepo demoRepo;
    private final PhraseCommandToPhraseBase phraseConverter;
    private final PhraseBaseToPhraseCommand phraseConverterCmd;

    public DemoH2Service(IDemoRepo demoRepo, PhraseCommandToPhraseBase pPhraseConverter,
            PhraseBaseToPhraseCommand pPhraseConverterCmd) {
        this.demoRepo = demoRepo;
        this.phraseConverter = pPhraseConverter;
        this.phraseConverterCmd = pPhraseConverterCmd;
        this.demoRepo.save(new PhraseBase("Im Patrick!", EPhraseType.GREET, "Patrick", LocalDate.now().minusDays(10)));
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
        return newPhrase;
    }

    @Override
    public Set<PhraseBaseCommand> getAll() {
        return this.demoRepo.findAll().stream()
                .map(this.phraseConverterCmd::convert).collect(Collectors.toSet());
    }

    @Override
    public Set<PhraseBaseCommand> getAll(Optional<Integer> limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public PhraseBaseCommand update(PhraseBaseCommand phraseToUpdate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public PhraseBaseCommand patchPhrase(Long id, @Valid PhraseBaseCommand patchedPhrase) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'patchPhrase'");
    }

}
