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
  @GetMapping(value = { "/resource-server-1/role-1-scope-1" })
  @PreAuthorize("hasAnyAuthority('ROLE_scope-1'," +
          " 'SCOPE_role-1')")
  public String helloForRoleAndScope() {
    return "this is a resource-server protected by Azure Active Directory B2C. ";
  }
}
