package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationArrDto {
    private List<TranslationResDto> translations;
}
