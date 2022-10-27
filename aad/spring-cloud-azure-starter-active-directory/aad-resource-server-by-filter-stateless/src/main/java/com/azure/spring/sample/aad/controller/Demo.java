package com.azure.spring.sample.aad.controller;

import com.azure.spring.cloud.autoconfigure.aad.implementation.constants.AadJwtClaimNames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Demo {
    public static void main(String[] args) {
        User user = new User();
//        List<String> cs = new ArrayList<>();
//        cs.add("test");
//        cs.add("test2");
//
//        user.setClaim(cs);
//        Set<String> roles = Optional.of(user.getClaim()).orElse(Collections.emptyList());
//        Set<String> roles = new HashSet<>(strings);
//        System.out.println(roles);
//        Set<String> roles = Optional.of(userPrincipal)
//                                    .map(p -> p.getClaim(AadJwtClaimNames.ROLES))
//                                    .map(JSONArray.class::cast)
//                                    .map(Collection<Object>::stream)
//                                    .orElseGet(Stream::empty)
//                                    .map(Object::toString)
//                                    .collect(Collectors.toSet());
    }

    static class User {
        List<String> claim;

        public List<String> getClaim() {
            return claim;
        }

        public void setClaim(List<String> claim) {
            this.claim = claim;
        }
    }
}
