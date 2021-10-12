package com.spring.sample.b2c.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * controller.
 */
@Controller
public class SampleController {

  @GetMapping(value = { "/", "/home" })
  public String index() {
    return "home";
  }
}
