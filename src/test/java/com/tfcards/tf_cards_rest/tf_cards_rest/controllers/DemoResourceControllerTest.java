package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.Arrays;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.InstanceOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.util.StandardCharset;
import com.tfcards.tf_cards_rest.configuration.SecurityConfigTest;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.configuration.SecurityConfiguration;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseBaseToPhraseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.converters.PhraseCommandToPhraseBase;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;
import com.tfcards.tf_cards_rest.tf_cards_rest.services.IDemoService;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@WebMvcTest(DemoResourceController.class)
@Import(SecurityConfigTest.class)
class DemoResourceControllerTest {

        private static final String MOCK_USERNAME = "admin";
        private static final String MOCK_USER_PASS = "dummyadmin";

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ObjectMapper objWrapper;

        @Autowired
        MessageSource msgSrc;

        @MockBean
        IDemoService demoServiceMock;

        // @MockBean
        // MessageSource msgSourceMock;

        PhraseCommandToPhraseBase phraseConverter;

        PhraseBaseToPhraseCommand phraseCmdConverter;

        @Test
        @WithMockUser(username = "admin", password = "dummyadmin", roles = { "CLIENT", "ADMIN" })
        void testGetPhrase() throws Exception {
                var user = "admin";
                var pass = "dummyadmin";
                var userName = "Kratos";
                //
                var phraseExpected = this.msgSrc.getMessage("demo.phrase", new String[] { userName }, "",
                                new Locale("en"));
                // given(this.msgSourceMock.getMessage(eq("demo.phrase"), any(String[].class),
                // anyString(), any(Locale.class)))
                // .willReturn(phraseExpected);
                //
                mockMvc.perform(
                                get("/v1/demo/hello")
                                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(user, pass))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .param("name", userName)
                                                .header("Accept-Language", "en"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.msg", instanceOf(String.class)))
                                .andExpect(jsonPath("$.msg", is(phraseExpected)));
        }

        @Test
        @WithMockUser(username = MOCK_USERNAME, password = MOCK_USER_PASS, roles = { "CLIENT" })
        void testGetPhraseInEs() throws Exception {
                var userName = "Patricio";
                var phraseExpected = this.msgSrc.getMessage("demo.phrase", new String[] { userName }, "",
                                new Locale("es"));
                //
                mockMvc.perform(
                                get("/v1/demo/hello")
                                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(MOCK_USERNAME,
                                                                MOCK_USERNAME))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .param("name", userName)
                                                .header("Accept-Language", "es"))
                                .andExpectAll(
                                                status().isOk(),
                                                content().contentType(MediaType.APPLICATION_JSON),
                                                jsonPath("$.msg", instanceOf(String.class)),
                                                jsonPath("$.msg", is(phraseExpected)));
        }

        @Test
        @WithMockUser(username = MOCK_USERNAME, password = MOCK_USER_PASS, roles = { "CLIENT" })
        void testPostPhrase() throws Exception {
                Map<String, String> newPhrase = new Hashtable<>(
                                Map.of("id", "0", "phrase", "Heeeeeeeeeeeey DUDE?", "phraseType", "5"));
                var mockPhrase = new PhraseBaseCommand(0L, newPhrase.get("phrase"));
                mockPhrase.setPhraseType(EPhraseType.GREET);
                //
                given(this.demoServiceMock.create(any(PhraseBaseCommand.class))).willReturn(mockPhrase);
                //
                mockMvc.perform(
                                post("/v1/demo")
                                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(MOCK_USERNAME,
                                                                MOCK_USER_PASS))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objWrapper.writeValueAsString(newPhrase)))
                                .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(username = MOCK_USERNAME, password = MOCK_USER_PASS, roles = { "CLIENT" })
        public void testUpdatePhrase() throws Exception {
                var mockPhrase = new PhraseBaseCommand(2L, "Hi, how is it going?");
                mockPhrase.setPhraseType(EPhraseType.GREET);
                //
                given(this.demoServiceMock.update(any(PhraseBaseCommand.class))).willReturn(mockPhrase);
                //
                mockMvc.perform(
                                put("/v1/demo/{phraseId}", mockPhrase.getId())
                                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(MOCK_USERNAME,
                                                                MOCK_USER_PASS))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(this.objWrapper.writeValueAsString(mockPhrase)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.msg", instanceOf(String.class)))
                                .andExpect(jsonPath("$.obj", instanceOf(Map.class)));
                //
                verify(this.demoServiceMock).update(any(PhraseBaseCommand.class));
        }
}
