package com.azure.sample.active.directory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInformationController {

    ObjectMapper objectMapper;

    public UserInformationController() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @GetMapping(
        path = "/user-information",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String userInformation() throws JsonProcessingException {
        return objectMapper.writeValueAsString(SecurityContextHolder.getContext().getAuthentication());
    }
}
