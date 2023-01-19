// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.security.keyvault.certificates.sample.client.side;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.azure.spring.security.keyvault.certificates.sample.client.side.common.AzureKeyVaultKeyStoreUtil.SERVER_SIDE_ENDPOINT;

@RestController
public class RestTemplateController {

    final RestTemplate restTemplateWithTLS;
    final RestTemplate restTemplateWithMTLS;

    public RestTemplateController(RestTemplate restTemplateWithTLS, RestTemplate restTemplateWithMTLS) {
        this.restTemplateWithTLS = restTemplateWithTLS;
        this.restTemplateWithMTLS = restTemplateWithMTLS;
    }

    @GetMapping("/resttemplate/tls")
    public String tls() {
        return String.format("Response from restTemplate tls \"%s\": %s",
            SERVER_SIDE_ENDPOINT,
            restTemplateWithTLS.getForObject(SERVER_SIDE_ENDPOINT, String.class));
    }

    @GetMapping("/resttemplate/mtls")
    public String mtls() {
        return String.format("Response from restTemplate mtls \"%s\": %s",
            SERVER_SIDE_ENDPOINT,
            restTemplateWithMTLS.getForObject(SERVER_SIDE_ENDPOINT, String.class));
    }
}
