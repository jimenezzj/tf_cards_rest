package com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list;

import java.util.Set;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.BaseEntity;

public interface ICrudService<T extends BaseEntity, I> {

    Set<T> getAll();

    T getById(I id);

    T save(T newEntity);

    void remove(T entity);

    void removeById(I id);

}
