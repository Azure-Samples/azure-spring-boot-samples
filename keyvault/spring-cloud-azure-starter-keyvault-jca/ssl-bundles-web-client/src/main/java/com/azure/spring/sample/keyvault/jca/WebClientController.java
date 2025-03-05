// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.keyvault.jca;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class WebClientController {

//    private final WebClient webClientWithTLS;
    private final WebClient webClientWithMTLS;

    private static final String SERVER_SIDE_ENDPOINT = "https://localhost:8444/ssl-test";

    public WebClientController( WebClient webClientWithMTLS) {
//        this.webClientWithTLS = webClientWithTLS;
        this.webClientWithMTLS = webClientWithMTLS;
    }

//    @GetMapping("/webclient/tls")
//    public Mono<String> webclientTls() {
//        return webClientWithTLS.get()
//                               .uri(SERVER_SIDE_ENDPOINT)
//                               .retrieve()
//                               .bodyToMono(String.class)
//                               .map(response -> String.format("Response from webClient tls \"%s\": %s", SERVER_SIDE_ENDPOINT, response))
//                               .onErrorResume(e -> Mono.just(String.format("Error occurred: %s", e.getMessage())));
//    }

    @GetMapping("/webclient/mtls")
    public Mono<String> webclientMtls() {
        return webClientWithMTLS.get()
                               .uri(SERVER_SIDE_ENDPOINT)
                               .retrieve()
                               .bodyToMono(String.class)
                               .map(response -> String.format("Response from webClient mtls \"%s\": %s", SERVER_SIDE_ENDPOINT, response))
                               .onErrorResume(e -> Mono.just(String.format("Error occurred: %s", e.getMessage())));
    }
}
