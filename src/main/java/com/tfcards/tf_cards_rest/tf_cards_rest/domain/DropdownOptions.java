package com.tfcards.tf_cards_rest.tf_cards_rest.domain;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DropdownOptions extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @NotNull
    private EDropdownCollection collectionName;
    
    @NotEmpty
    @NotNull
    @Column(name = "collection_value")
    private String value;

}
