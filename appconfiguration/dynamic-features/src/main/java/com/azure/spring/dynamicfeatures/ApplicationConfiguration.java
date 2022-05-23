package com.azure.spring.dynamicfeatures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.azure.spring.cloud.feature.manager.feature.evaluators.TargetingEvaluator;
import com.azure.spring.cloud.feature.manager.targeting.TargetingEvaluationOptions;
import com.azure.spring.dynamicfeatures.implementation.TargetingContextImpl;

@Configuration
public class ApplicationConfiguration {

    @Bean(name = "Microsoft.Targeting")
    @Scope("request")
    public TargetingEvaluator targettingFilter(TargetingContextImpl context) {
        return new TargetingEvaluator(context, new TargetingEvaluationOptions().setIgnoreCase(true));
    }
}