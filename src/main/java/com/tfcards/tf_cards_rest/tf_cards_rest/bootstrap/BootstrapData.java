package com.tfcards.tf_cards_rest.tf_cards_rest.bootstrap;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final IDemoRepo demoRepo;

    @Override
    public void run(String... args) throws Exception {
        loadDummyPhrases();
    }

    private void loadDummyPhrases() {
        if (this.demoRepo.count() == 0L) {
            this.demoRepo
                    .save(new PhraseBase("Im Patrick!", EPhraseType.GREET, "Patrick", LocalDate.now().minusDays(10)));
            this.demoRepo.save(new PhraseBase(
                    "I Order The Food, You Cook The Food. The Customer Gets The Food. We Do That For 40 Years, And Then We Die",
                    EPhraseType.EXPRESSION, "SquidWard", LocalDate.now().minusYears(5)));
        }
    }

}
