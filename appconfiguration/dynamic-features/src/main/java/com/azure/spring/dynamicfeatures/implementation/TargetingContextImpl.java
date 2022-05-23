package com.azure.spring.dynamicfeatures.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.azure.spring.cloud.feature.manager.targeting.ITargetingContextAccessor;
import com.azure.spring.cloud.feature.manager.targeting.TargetingContext;

import reactor.core.publisher.Mono;

@Component
@RequestScope
public class TargetingContextImpl implements ITargetingContextAccessor {
    
    @Autowired
    private HttpServletRequest request;

    @Override
    public Mono<TargetingContext> getContextAsync() {
        TargetingContext context = new TargetingContext();
        
        context.setUserId(request.getParameter("user"));
        
        List<String> groups = new ArrayList<>();
        
        groups.add(request.getParameter("group"));
        
        context.setGroups(groups);
        
        return Mono.just(context);
    }

}
