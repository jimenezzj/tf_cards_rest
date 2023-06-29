package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationResDto {
    private String text;
    private String to;
}
