package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.nimbusds.jose.util.StandardCharset;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseBaseToPhraseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCommandToPhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IDemoService;

@WebMvcTest(DemoResourceController.class)
class DemoResourceControllerTest {

    @Autowired
    MockMvc mockMvc;

    MessageSource msgSrc;

    @MockBean
    IDemoService demoService;

    PhraseCommandToPhraseBase phraseConverter;

    PhraseBaseToPhraseCommand phraseCmdConverter;

    @Test
    void getPhrase() throws Exception {
        var user = "admin";
        var pass = "dummyadmin";
        var auth = user + ":" + pass;
        byte[] encoedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharset.UTF_8));
        var authHeader = "Basic " + encoedAuth.toString();
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/demo/hello")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(user, pass))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // .header("Authorization", "Basic YWRtaW46ZHVtbXlhZG1pbg=="))
    }
}
