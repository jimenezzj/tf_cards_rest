package com.tfcards.tf_cards_rest.tf_cards_rest.bootstrap;

import java.time.LocalDate;
import java.util.Arrays;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.AppUserDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IAppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseTranslationDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.DropdownOptions;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection.Lang;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDemoRepo;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IDropdownOptsRepo;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IDemoServiceV2;

import lombok.RequiredArgsConstructor;

@Component
@Profile({"H2_DB", "MYSQL_DB"})
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final IDemoRepo demoRepo;
    private final IDemoServiceV2 demoServiceV2;
    private final IDropdownOptsRepo dropdownOptsRepo;

    private final IAppUserService userService;
    private final PasswordEncoder passEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadDropdownOpts();
        loadDummyPhrases();
        loadDummyInMemoryUsers();
    }

    private void loadDummyInMemoryUsers() {
        var user = AppUserDto.builder()
                .email("laknight@wwe.com")
                .password(this.passEncoder.encode("yeah!"))
                .name("Los")
                .build();
        var user2 = AppUserDto.builder()
                .email("drewm@wwe.com")
                .password(this.passEncoder.encode("password"))
                .name("Drew")
                .build();
        this.userService.saveUser(user);
        this.userService.saveUser(user2);
    }

    private void loadDropdownOpts() {
        Arrays.asList(Lang.values())
                .forEach(l -> {
                    if (l != Lang.FR)
                        this.dropdownOptsRepo.save(new DropdownOptions(
                                EDropdownCollection.API_LANG, l.toString()));
                });
    }

    private void loadDummyPhrases() {
        if (this.demoRepo.count() == 0L) {
            var patrickPhrase = this.demoRepo
                    .save(new PhraseBase("Im Patrick!", EPhraseType.GREET, "Patrick",
                            LocalDate.now().minusDays(10),
                            EDropdownCollection.Lang.EN));
            var squidPhrase = this.demoRepo.save(new PhraseBase(
                    "I Order The Food, You Cook The Food. The Customer Gets The Food. We Do That For 40 Years, And Then We Die",
                    EPhraseType.EXPRESSION, "SquidWard", LocalDate.now().minusYears(5),
                    EDropdownCollection.Lang.EN));
            this.demoServiceV2.addTranslationTo(patrickPhrase.getPhraseId(),
                    new PhraseTranslationDto("Hola soy Patricio", Lang.EN, Lang.ES));
            this.demoServiceV2.addTranslationTo(squidPhrase.getPhraseId(), new PhraseTranslationDto(
                    "Yo ordeno la comida, tu preparas la comida. El cliente recoge la comida. Y hacemos esto por 40 años, para luego morir",
                    Lang.EN, Lang.ES));
        }
    }

}
