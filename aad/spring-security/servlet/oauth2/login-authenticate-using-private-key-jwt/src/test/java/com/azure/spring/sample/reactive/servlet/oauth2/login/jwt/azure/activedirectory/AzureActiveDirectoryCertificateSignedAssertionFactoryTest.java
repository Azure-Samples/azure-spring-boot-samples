package com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.azure.activedirectory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AzureActiveDirectoryCertificateSignedAssertionFactoryTest {

    private static final String TEST_TENANT_ID = "test-tenant-id";
    private static final String TEST_CLIENT_ID = "test-client-id";

    @Test
    public void testPfx() throws UnrecoverableKeyException, CertificateException, KeyStoreException, IOException,
        NoSuchAlgorithmException, JOSEException {
        test(new AzureActiveDirectoryCertificateSignedJwtAssertionFactory(
            "src/test/resources/encrypted-private-key-and-certificate.pfx", "myPassword1", TEST_TENANT_ID,
            TEST_CLIENT_ID));
    }

    // TODO support pem file.
    @Disabled("Pem file is not supported now.")
    @Test
    public void testPem() throws UnrecoverableKeyException, CertificateException, KeyStoreException, IOException,
        NoSuchAlgorithmException, JOSEException {
        test(new AzureActiveDirectoryCertificateSignedJwtAssertionFactory(
            "src/test/resources/encrypted-private-key-and-certificate.pem", "myPassword1", TEST_TENANT_ID,
            TEST_CLIENT_ID));
    }

    public void test(AzureActiveDirectoryCertificateSignedJwtAssertionFactory factory) throws JOSEException,
        JsonProcessingException {
        String assertion = factory.createJwtAssertion();
        String[] chunks = assertion.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        String signature = new String(decoder.decode(chunks[2]));
        assertNotNull(header);
        assertNotNull(payload);
        assertNotNull(signature);

        ObjectMapper mapper = new ObjectMapper();
        AssertionHeader assertionHeader = mapper.readValue(header, AssertionHeader.class);
        assertNotNull(assertionHeader);
        assertEquals("RS256", assertionHeader.alg);
        assertEquals("JWT", assertionHeader.typ);
        // The value of "x5t" is same to the "Thumbprint" in the Azure Portal.
        assertEquals("D829DB4885D1C22B1207F72F533DFA8125861174",
            DatatypeConverter.printHexBinary(Base64.getUrlDecoder().decode(assertionHeader.x5t)));

        AssertionClaims assertionClaims = mapper.readValue(payload, AssertionClaims.class);
        assertNotNull(assertionClaims);
        assertEquals(String.format("https://login.microsoftonline.com/%s/v2.0", TEST_TENANT_ID), assertionClaims.aud);
        assertNotNull(assertionClaims.exp);
        assertEquals(TEST_CLIENT_ID, assertionClaims.iss);
        assertNotNull(assertionClaims.jti);
        assertNotNull(assertionClaims.nbf);
        assertEquals(TEST_CLIENT_ID, assertionClaims.sub);
        assertNotNull(assertionClaims.iat);
    }

    private static class AssertionHeader {
        public String alg;
        public String typ;
        public String x5t;
    }

    private static class AssertionClaims {
        public String aud;
        public String exp;
        public String iss;
        public String jti;
        public String nbf;
        public String sub;
        public String iat;
    }

}
