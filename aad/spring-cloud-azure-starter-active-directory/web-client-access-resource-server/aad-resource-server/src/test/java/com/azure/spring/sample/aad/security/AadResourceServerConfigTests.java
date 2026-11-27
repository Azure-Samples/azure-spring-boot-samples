// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import com.azure.spring.sample.aad.AadOAuth2ResourceServerSampleApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AadOAuth2ResourceServerSampleApplication.class, properties = {
    "spring.cloud.azure.active-directory.enabled=true",
    "spring.cloud.azure.active-directory.profile.tenant-id=22222222-2222-2222-2222-222222222222",
    "spring.cloud.azure.active-directory.credential.client-id=11111111-1111-1111-1111-111111111111",
    "spring.cloud.azure.active-directory.app-id-uri=api://11111111-1111-1111-1111-111111111111"
})
@AutoConfigureMockMvc
class AadResourceServerConfigTests {

    private static final String TOKEN = "sample-token";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void anonymousClientCredentialIsAllowed() throws Exception {
        mockMvc.perform(get("/webapiB/clientCredential"))
            .andExpect(status().isOk())
            .andExpect(content().string("Response from webApiB: clientCredential"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/webapiB", "/user", "/not-public"})
    void anonymousOtherRequestsRequireAuthentication(String path) throws Exception {
        mockMvc.perform(get(path))
            .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource({
        "/webapiB, WebApiB.ExampleScope, Response from webApiB.",
        "/user, User.Read, User read success."
    })
    void authenticatedRequestsWithRequiredScopeAreAllowed(String path, String scope, String response) throws Exception {
        tokenHasScope(scope);

        mockMvc.perform(get(path).header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN))
            .andExpect(status().isOk())
            .andExpect(content().string(response));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/webapiB", "/user"})
    void authenticatedRequestsWithoutRequiredScopeAreForbidden(String path) throws Exception {
        tokenHasScope("unrelated");

        mockMvc.perform(get(path).header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN))
            .andExpect(status().isForbidden());
    }

    private void tokenHasScope(String scope) {
        Jwt jwt = Jwt.withTokenValue(TOKEN)
            .header("alg", "RS256")
            .subject("sample-user")
            .claim("scp", scope)
            .build();
        when(jwtDecoder.decode(TOKEN)).thenReturn(jwt);
    }
}
