package com.spring.sample.b2c.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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
    return "home";
  }

  @ResponseBody
  @GetMapping(value = { "/hello" })
  @PreAuthorize("hasAuthority('SCOPE_Delegated.Permission.Scope1')")
  public String hello() {
    return "this is a resource-server protected by Azure Active Directory B2C. ";
  }

}
