// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.security.keyvault.certificates.sample.client.side.common;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public final class ConfigurableAliasKeyManager extends X509ExtendedKeyManager {
    private final X509ExtendedKeyManager delegate;
    private final String alias;

    public ConfigurableAliasKeyManager(X509ExtendedKeyManager keyManager, String alias) {
        this.delegate = keyManager;
        this.alias = alias;
    }

    public String chooseEngineClientAlias(String[] strings, Principal[] principals, SSLEngine sslEngine) {
        return alias;
    }

    public String chooseEngineServerAlias(String s, Principal[] principals, SSLEngine sslEngine) {
        return alias;
    }

    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return alias;
    }

    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return alias;
    }

    public X509Certificate[] getCertificateChain(String alias) {
        return this.delegate.getCertificateChain(alias);
    }

    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return this.delegate.getClientAliases(keyType, issuers);
    }

    public PrivateKey getPrivateKey(String alias) {
        return this.delegate.getPrivateKey(alias);
    }

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return this.delegate.getServerAliases(keyType, issuers);
    }
}
