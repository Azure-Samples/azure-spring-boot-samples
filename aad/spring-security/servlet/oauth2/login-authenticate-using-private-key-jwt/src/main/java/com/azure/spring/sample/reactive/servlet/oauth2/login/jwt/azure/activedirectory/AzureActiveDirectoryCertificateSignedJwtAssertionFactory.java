package com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.azure.activedirectory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.factories.DefaultJWSSignerFactory;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

/**
 * A factory used to create Azure AD JWT assertion signed with a certificate.
 *
 * @author Rujun Chen
 * @see <a href="https://docs.microsoft.com/azure/active-directory/develop/active-directory-certificate-credentials">
 * Certificate credentials</a>
 * @since 2022-02-18
 */
public class AzureActiveDirectoryCertificateSignedJwtAssertionFactory {

    private final JWSSigner signer;
    private final JWSHeader header;
    private final JWTClaimsSet templateClaims;

    /**
     * @param file Path of certificate file. The file should contain encrypted private key and certificate.
     * And the file name should have ".pfx" as suffix.
     * @param password The password of the encrypted private key in certificate file.
     * @throws AzureActiveDirectoryAssertionException if failed to create factory.
     */
    public AzureActiveDirectoryCertificateSignedJwtAssertionFactory(String file, String password, String tenantId,
                                                                    String clientId) throws AzureActiveDirectoryAssertionException {
        try {
            String fileExtension = file.substring(file.lastIndexOf(".") + 1);
            Assert.isTrue("pfx".equals(fileExtension), "Only support file with '.pfx' extension.");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(file), password.toCharArray());
            String alias = keyStore.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
            X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
            PublicKey publicKey = x509Certificate.getPublicKey();
            signer = createJWSSigner(publicKey, privateKey);
            header = createJWSHeader(x509Certificate);
            templateClaims = createTemplateJWTClaims(tenantId, clientId);
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException |
            UnrecoverableKeyException | JOSEException exception) {
            throw new AzureActiveDirectoryAssertionException("Failed to create factory.", exception);
        }
    }

    /**
     * Create JWT assertion
     *
     * @throws AzureActiveDirectoryAssertionException If failed to create assertion.
     */
    public String createJwtAssertion() throws AzureActiveDirectoryAssertionException {
        JWTClaimsSet claims = createJWTClaimsSet();
        SignedJWT signedJwt = new SignedJWT(header, claims);
        try {
            signedJwt.sign(signer);
        } catch (JOSEException exception) {
            throw new AzureActiveDirectoryAssertionException("Failed to sign JWT.", exception);
        }
        return signedJwt.serialize();
    }

    private JWSSigner createJWSSigner(PublicKey publicKey, PrivateKey privateKey) throws JOSEException {
        // Azure AD currently supports only RSA.
        // Refs: https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-create-self-signed-certificate
        JWK jwk = new RSAKey.Builder((RSAPublicKey) publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
        return new DefaultJWSSignerFactory().createJWSSigner(jwk);
    }

    @SuppressWarnings("deprecation")
    private JWSHeader createJWSHeader(X509Certificate x509Certificate) throws CertificateEncodingException,
        NoSuchAlgorithmException {
        return new JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .x509CertThumbprint(Base64URL.encode(getX5t(x509Certificate)))
            .build();
    }

    private byte[] getX5t(X509Certificate cert)
        throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();
        digest.update(der);
        return digest.digest();
    }

    private JWTClaimsSet createTemplateJWTClaims(String tenantId, String clientId) {
        return new JWTClaimsSet.Builder()
            .audience(String.format("https://login.microsoftonline.com/%s/v2.0", tenantId))
            .issuer(clientId)
            .subject(clientId)
            .build();
    }

    private JWTClaimsSet createJWTClaimsSet() {
        Date currentTime = new Date();
        return new JWTClaimsSet.Builder(templateClaims)
            .expirationTime(Date.from(currentTime.toInstant().plusSeconds(300))) // 5 minutes after currentTime.
            .jwtID(UUID.randomUUID().toString())
            .notBeforeTime(currentTime)
            .issueTime(currentTime)
            .build();
    }


}
