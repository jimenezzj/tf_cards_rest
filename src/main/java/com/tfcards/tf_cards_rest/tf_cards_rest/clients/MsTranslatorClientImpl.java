package com.tfcards.tf_cards_rest.tf_cards_rest.clients;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseTranslationDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.TranslationArrDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.TranslationResDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MsTranslatorClientImpl implements IMsTranslatorClient {

    private static final String BASE_URL = "https://25524bb1-f33f-4053-8619-1e9daaa26473.mock.pstmn.io";
    private static final String TRANSLATE_PATH = "/translate";
    private static final String API_VERSION = "3.0";
    private static final String API_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqGKukO1De7zhZj6+H0qtjTkVxwTCpvKe4eCZ0FPqri0cb2JZfXJ";

    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<PhraseDtoV2> translatePhrase(PhraseDtoV2 pDtoV2) {
        RestTemplate template = this.restTemplateBuilder.build();
        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .queryParam("from", "en")
                .queryParam("to", "pr");
        var entity = this.getEntity(Optional.of(pDtoV2));
        ResponseEntity<TranslationArrDto> jsonRes = template.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity,
                TranslationArrDto.class);
        var body = jsonRes.getBody();
        return null;
    }

    @Override
    public TranslationResDto getTranslatedPhrase(PhraseTranslationDto phraseTranslationDto) {
        RestTemplate template = this.restTemplateBuilder.build();
        String from = phraseTranslationDto.getFrom().toString().toLowerCase(),
                to = phraseTranslationDto.getTo().toString().toLowerCase();
        UriComponentsBuilder uBuilder = this.getUriBuilder().queryParam("from", from).queryParam("to", to);
        var body = Optional.of(Map.of("Text", phraseTranslationDto.getPhraseToTranslate()));
        ResponseEntity<TranslationArrDto[]> jsonRes = template.exchange(uBuilder.toUriString(), HttpMethod.POST,
                this.getEntity(body), TranslationArrDto[].class);
        //
        if (jsonRes.getStatusCode() != HttpStatus.ACCEPTED && jsonRes.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("Translation error");
        var transRes = Arrays.asList(jsonRes.getBody()).get(0);
        return transRes.getTranslations().get(0);
    }

    private <T> HttpEntity<T> getEntity(Optional<T> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", API_KEY);
        headers.set("Ocp-Apim-Subscription-Region", "westus2");
        HttpEntity<T> entity = body.isPresent() ? new HttpEntity<T>(body.get(), headers) : new HttpEntity<T>(headers);
        return entity;
    }

    private <T> HttpEntity<T> getEntity() {
        return this.getEntity(Optional.empty());
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(TRANSLATE_PATH)
                .queryParam("api-version", API_VERSION);
    }

}
