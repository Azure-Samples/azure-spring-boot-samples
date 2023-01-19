// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.security.keyvault.certificates.sample.client.side.common;

import com.azure.security.keyvault.jca.KeyVaultLoadStoreParameter;
import java.security.KeyStore;

public final class AzureKeyVaultKeyStoreUtil {
    public static final String CLIENT_ALIAS = "self-signed";             // It should be your certificate alias used in client-side
    public static final String SERVER_SIDE_ENDPOINT = "https://localhost:8443/";

    private static final CredentialType CREDENTIAL_TYPE = CredentialType.ServicePrinciple;
    private static KeyStore azureKeyVaultKeyStore;

    public static KeyStore buildAzureKeyVaultKeyStore() throws Exception {
        if (azureKeyVaultKeyStore == null) {
            azureKeyVaultKeyStore = KeyStore.getInstance("AzureKeyVault");
            KeyVaultLoadStoreParameter parameter = null;
            if (CredentialType.ServicePrinciple.equals(CREDENTIAL_TYPE)) {
                parameter = new KeyVaultLoadStoreParameter(
                        System.getProperty("azure.keyvault.uri"),
                        System.getProperty("azure.keyvault.tenant-id"),
                        System.getProperty("azure.keyvault.client-id"),
                        System.getProperty("azure.keyvault.client-secret"));
            } else if (CredentialType.ManagedIdentity.equals(CREDENTIAL_TYPE)) {
                parameter = new KeyVaultLoadStoreParameter(
                        System.getProperty("azure.keyvault.uri"),
                        System.getProperty("azure.keyvault.managed-identity"));
            }
            azureKeyVaultKeyStore.load(parameter);
        }
        return azureKeyVaultKeyStore;
    }

    enum CredentialType {
        ServicePrinciple,
        ManagedIdentity
    }
}
