package com.example.demo.configuration;
import com.azure.spring.cloud.feature.management.filters.TargetingFilter;
import com.azure.spring.cloud.feature.management.targeting.TargetingEvaluationOptions;
import com.example.demo.impl.TargetingContextImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfiguration {

    @Bean(name = "targetingFilter")
    @Scope("request")
    public TargetingFilter targetingFilter(TargetingContextImpl context) {
        return new TargetingFilter(context, new TargetingEvaluationOptions().setIgnoreCase(true));
    }
}
