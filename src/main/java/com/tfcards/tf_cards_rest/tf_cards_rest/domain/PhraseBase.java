package com.tfcards.tf_cards_rest.tf_cards_rest.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phrases")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PhraseBase extends BaseEntity {

    // These @Generate* annotations does really nothig beacause it works only on
    // primary-key fields
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar", nullable = false, unique = false, updatable = false)
    private UUID phraseId;

    @Size(min = 6, max = 150)
    @NotNull
    @NotEmpty
    @Column(length = 150)
    private String phrase;

    @NotNull
    private EPhraseType phraseType;

    private String author;

    private LocalDate publishDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    // @Versio
    // private Integer version;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private EDropdownCollection.Lang lang;

    public PhraseBase(String phrase) {
        setPhraseType(EPhraseType.EXPRESSION);
        setPhrase(phrase);
        setPhraseId(UUID.randomUUID());
    }

    public PhraseBase(String phrase, EPhraseType phraseType, String author,
            LocalDate publishDate, EDropdownCollection.Lang pLang) {
        this.phrase = phrase;
        this.phraseType = phraseType;
        this.author = author;
        this.publishDate = publishDate;
        this.createdAt = LocalDate.now();
        setPhraseId(UUID.randomUUID());
        setLang(pLang);
    }

}
