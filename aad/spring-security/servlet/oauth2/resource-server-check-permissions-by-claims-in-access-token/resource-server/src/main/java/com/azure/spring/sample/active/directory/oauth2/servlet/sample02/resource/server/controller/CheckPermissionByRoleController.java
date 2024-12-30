package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.resource.server.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckPermissionByRoleController {

    @GetMapping("/role/resource-server-1-role-1")
    @PreAuthorize("hasAuthority('ROLE_resource-server-1-role-1')")
    public String resourceServer1Role1() {
        return "Hi, this is resource-server-1. You can access my endpoint: /role/resource-server-1-role-1";
    }

    @GetMapping("/role/resource-server-1-role-2")
    @PreAuthorize("hasAuthority('ROLE_resource-server-1-role-2')")
    public String resourceServer1Role2() {
        return "Hi, this is resource-server-1. You can access my endpoint: /role/resource-server-1-role-2";
    }
}
