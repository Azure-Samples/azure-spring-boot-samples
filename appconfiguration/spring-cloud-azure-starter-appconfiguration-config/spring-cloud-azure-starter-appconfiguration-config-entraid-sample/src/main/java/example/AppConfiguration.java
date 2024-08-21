package example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public AppConfigClientCustomizer clientCustomizers() {
        return new AppConfigClientCustomizer();
    }

}
