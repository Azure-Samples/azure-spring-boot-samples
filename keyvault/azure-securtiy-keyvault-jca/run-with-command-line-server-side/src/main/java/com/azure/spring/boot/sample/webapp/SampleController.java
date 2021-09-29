// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.boot.sample.webapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * A controller that accepts http requests and returns response.
 */
@RestController
public class SampleController {
  @GetMapping("/")
  public String helloWorld() {
    return "Hello World!";
  }
}
