package com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

public interface IPhraseRepoList extends ICrudService<PhraseBase, Long> {
    PhraseBase findByPhraseContaining(String pPhraseSubstr);

}
