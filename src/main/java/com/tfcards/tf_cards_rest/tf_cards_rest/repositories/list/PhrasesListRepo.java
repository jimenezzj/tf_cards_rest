package com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@Repository
public class PhrasesListRepo extends AbstractCrudRepo<PhraseBase> implements IPhraseRepoList {

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

}
