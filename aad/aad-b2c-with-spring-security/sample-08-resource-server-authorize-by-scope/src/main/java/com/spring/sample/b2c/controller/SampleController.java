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

  @ResponseBody
  @GetMapping(value = { "/hello" })
  public String hello() {
    return "this is a resource-server protected by Azure Active Directory B2C. ";
  }
  @ResponseBody
  @GetMapping(value = { "/helloForScope1" })
  @PreAuthorize("hasAuthority('SCOPE_Delegated.Permission.Scope1')")
  public String helloForScope1() {
    return "this is a resource-server protected by Azure Active Directory B2C. ";
  }

  @ResponseBody
  @GetMapping(value = { "/helloForScope2" })
  @PreAuthorize("hasAuthority('SCOPE_Delegated.Permission.Scope2')")
  public String helloForScope2() {
    return "this is a resource-server protected by Azure Active Directory B2C. ";
  }
}
