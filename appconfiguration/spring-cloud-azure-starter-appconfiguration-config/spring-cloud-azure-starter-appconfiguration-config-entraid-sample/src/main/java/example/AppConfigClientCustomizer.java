package example;

import com.azure.data.appconfiguration.ConfigurationClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.spring.cloud.appconfiguration.config.ConfigurationClientCustomizer;
import com.azure.spring.cloud.appconfiguration.config.SecretClientCustomizer;

public class AppConfigClientCustomizer implements ConfigurationClientCustomizer, SecretClientCustomizer {

    @Override
    public void customize(ConfigurationClientBuilder builder, String endpoint) {
        builder.credential(new DefaultAzureCredentialBuilder().build());
    }

    @Override
    public void customize(SecretClientBuilder builder, String endpoint) {
        builder.credential(new DefaultAzureCredentialBuilder().build());
    }

}
