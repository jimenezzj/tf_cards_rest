package com.tfcards.tf_cards_rest.tf_cards_rest.domain;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @JsonIgnore
    // private Long id;

    @Size(min = 5, max = 255)
    @NotNull
    @NotEmpty
    private String phrase;

    private EPhraseType phraseType;

    private String author;

    private LocalDate publishDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    // @Versio
    // private Integer version;

    public PhraseBase(Long id, String phrase) {
        setPhraseType(EPhraseType.EXPRESSION);
        super.setId(id);
        setPhrase(phrase);
    }

    public PhraseBase(String phrase) {
        setPhraseType(EPhraseType.EXPRESSION);
        setPhrase(phrase);
    }

    public PhraseBase(@Size(min = 5, max = 255) @NotNull @NotEmpty String phrase, EPhraseType phraseType, String author,
            LocalDate publishDate) {
        this.phrase = phrase;
        this.phraseType = phraseType;
        this.author = author;
        this.publishDate = publishDate;
        this.createdAt = LocalDate.now();
    }

    
}
