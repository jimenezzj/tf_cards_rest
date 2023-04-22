package com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.BaseEntity;

public abstract class AbstractCrudRepo<T extends BaseEntity> {

    protected Set<T> phrasesList = new HashSet<>();

    Set<T> selectAll() {
        return this.phrasesList;
    }

    T selectById(Long id) {
        return phrasesList.stream().filter(p -> p.getId().equals(id)).limit(1).findFirst()
                .orElseThrow(() -> new RuntimeException("Entity with given id was not found!"));
    }

    T store(T newObj) {
        if (newObj != null) {
            if (newObj.getId() == null)
                newObj.setId(generateNextId());
        } else
            throw new RuntimeException("Entity was tried to be stored is null");
        if (!phrasesList.add(newObj))
            throw new RuntimeException("Entity could not be saved");
        return this.selectById(newObj.getId());
    }

    void deleteById(Long id) {
        var foundEntity = this.selectById(id);
        phrasesList.remove(foundEntity);
    }

    void delete(T pObj) {
        var isDeleted = phrasesList.removeIf(obj -> obj.equals(pObj));
        if (!isDeleted)
            throw new RuntimeException("There\'s no entity that matches");
    }

    private Long generateNextId() {
        Long resNextId = null;
        if (phrasesList.isEmpty())
            resNextId = 1L;
        else {
            Long maxVal = Collections.max(phrasesList, Comparator.comparingLong(T::getId)).getId() % 10;
            for (long i = maxVal; i > 0; i--) {
                try {
                    this.selectById(i);
                } catch (Exception e) {
                    resNextId = i;
                }
                // if (!phrasesList.containsKey(i))
                // resNextId = i;
            }
        }
        if (resNextId == null)
            resNextId = (long) phrasesList.size() + 1;
        return resNextId;
    }

}
