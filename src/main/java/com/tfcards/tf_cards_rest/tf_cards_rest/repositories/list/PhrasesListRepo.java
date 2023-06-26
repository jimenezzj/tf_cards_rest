package com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;

@Repository
public class PhrasesListRepo extends AbstractCrudRepo<PhraseBase> implements IPhraseRepoList {

    public PhrasesListRepo() {
        this.phrasesList.add(new PhraseBase("Hello {0}!", EPhraseType.GREET, "",
                LocalDate.now().minusYears(getRandomPublishYear(Optional.empty())), EDropdownCollection.Lang.EN));
        this.phrasesList.add(new PhraseBase("How is it going?", EPhraseType.GREET, "",
                LocalDate.now().minusYears(getRandomPublishYear(Optional.empty())), EDropdownCollection.Lang.EN));
        this.phrasesList.add(new PhraseBase("Hey there, {0}!", EPhraseType.GREET, "",
                LocalDate.now().minusYears(getRandomPublishYear(Optional.empty())), EDropdownCollection.Lang.EN));
        this.phrasesList.add(new PhraseBase("What's up bro?", EPhraseType.GREET, "",
                LocalDate.now().minusYears(getRandomPublishYear(Optional.empty())), EDropdownCollection.Lang.EN));
    }

    @Override
    public Set<PhraseBase> getAll() {
        return super.selectAll();
    }

    @Override
    public PhraseBase getById(Long id) {
        return super.selectById(id);
    }

    @Override
    public PhraseBase save(PhraseBase newEntity) {
        return super.store(newEntity);
    }

    @Override
    public void remove(PhraseBase entity) {
        super.delete(entity);
    }

    @Override
    public void removeById(Long id) {
        super.deleteById(id);
    }

    @Override
    public PhraseBase findByPhraseContaining(String pPhraseSubstr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByPhraseContaining'");
    }

    protected Long getRandomPublishYear(Optional<Long> maxMinusYear) {
        Long leftYearLimit = 0L;
        Long rightYearLimit = 41L;
        if (maxMinusYear.isPresent())
            rightYearLimit = maxMinusYear.get();
        return leftYearLimit + (long) (Math.random() * (rightYearLimit - leftYearLimit));
    }

}
