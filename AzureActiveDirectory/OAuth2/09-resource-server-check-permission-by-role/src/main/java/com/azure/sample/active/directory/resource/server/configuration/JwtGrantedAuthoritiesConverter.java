package com.azure.sample.active.directory.resource.server.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final Log logger = LogFactory.getLog(getClass());
    private final Map<String, String> claimToAuthorityPrefixMap;

    public JwtGrantedAuthoritiesConverter(Map<String, String> claimToAuthorityPrefixMap) {
        this.claimToAuthorityPrefixMap = Collections.unmodifiableMap(claimToAuthorityPrefixMap);
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return getAuthorityStrings(jwt, this.claimToAuthorityPrefixMap);
    }

    private Collection<GrantedAuthority> getAuthorityStrings(Jwt jwt, Map<String, String> claimToAuthorityPrefixMap) {
        Collection<String> authorityStrings = new HashSet<>();
        for (String claim : claimToAuthorityPrefixMap.keySet()) {
            String authorityPrefix = claimToAuthorityPrefixMap.get(claim);
            getClaimValues(jwt, claim).stream()
                                      .map(claimValue -> authorityPrefix + claimValue)
                                      .forEach(authorityStrings::add);
        }
        return authorityStrings.stream()
                               .map(SimpleGrantedAuthority::new)
                               .collect(Collectors.toSet());
    }

    private Collection<String> getClaimValues(Jwt jwt, String claimName) {
        if (claimName == null) {
            this.logger.trace("Returning no authorities since could not find any claims that might contain scopes");
            return Collections.emptyList();
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.format("Looking for scopes in claim %s", claimName));
        }
        Object claimValue = jwt.getClaim(claimName);
        if (claimValue instanceof String) {
            if (StringUtils.hasText((String) claimValue)) {
                return Arrays.asList(((String) claimValue).split(" "));
            }
            return Collections.emptyList();
        }
        if (claimValue instanceof Collection) {
            return castToCollection(claimValue);
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private Collection<String> castToCollection(Object object) {
        return (Collection<String>) object;
    }
}
