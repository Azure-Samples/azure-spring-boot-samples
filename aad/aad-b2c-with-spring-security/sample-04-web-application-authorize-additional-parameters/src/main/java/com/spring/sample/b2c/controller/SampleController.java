package com.spring.sample.b2c.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * controller.
 */
@Controller
public class SampleController {

  @GetMapping(value = { "/", "/home" })
  public String index() {
    return "home.html";
  }

  @GetMapping(value = { "/resourceServer" })
  @ResponseBody
  public String getResourceServer(
          @RegisteredOAuth2AuthorizedClient("resource-server")
                  OAuth2AuthorizedClient resourceServer) {
    return resourceServer.getAccessToken().getTokenValue();
  }
}
