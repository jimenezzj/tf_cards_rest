package com.tfcards.tf_cards_rest.tf_cards_rest.bootstrap;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseTranslationDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection.Lang;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IDemoServiceV2;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final IDemoRepo demoRepo;
    private final IDemoServiceV2 demoServiceV2;

    @Override
    public void run(String... args) throws Exception {
        loadDummyPhrases();
    }

    private void loadDummyPhrases() {
        if (this.demoRepo.count() == 0L) {
            var patrickPhrase = this.demoRepo
                    .save(new PhraseBase("Im Patrick!", EPhraseType.GREET, "Patrick", LocalDate.now().minusDays(10),
                            EDropdownCollection.Lang.EN));
            var squidPhrase = this.demoRepo.save(new PhraseBase(
                    "I Order The Food, You Cook The Food. The Customer Gets The Food. We Do That For 40 Years, And Then We Die",
                    EPhraseType.EXPRESSION, "SquidWard", LocalDate.now().minusYears(5), EDropdownCollection.Lang.EN));
            this.demoServiceV2.addTranslationTo(patrickPhrase.getPhraseId(),
                    new PhraseTranslationDto("Hola soy Patricio", Lang.EN));
            this.demoServiceV2.addTranslationTo(squidPhrase.getPhraseId(), new PhraseTranslationDto(
                    "Yo ordeno la comida, tu preparas la comida. El cliente recoge la comida. Y hacemos esto por 40 años, para luego morir",
                    Lang.EN));
        }
    }

}
