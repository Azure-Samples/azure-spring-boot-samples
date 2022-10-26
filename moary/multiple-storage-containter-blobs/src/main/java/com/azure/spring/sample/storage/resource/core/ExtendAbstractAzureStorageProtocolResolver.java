package com.azure.spring.sample.storage.resource.core;

import com.azure.spring.cloud.core.resource.AbstractAzureStorageProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

public abstract class ExtendAbstractAzureStorageProtocolResolver extends AbstractAzureStorageProtocolResolver {
    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (ExtendAzureStorageUtils.isAzureStorageResource(location, getStorageType())) {
            return getResource(location);
        }
        return null;
    }

    /**
     * @see ResourcePatternResolver#getResources(java.lang.String)
     */
    @Override
    public Resource[] getResources(String pattern) throws IOException {
        Resource[] resources = null;

        if (ExtendAzureStorageUtils.isAzureStorageResource(pattern, getStorageType())) {
            String account =  getStorageAccountName(pattern);
            if (matcher.isPattern(ExtendAzureStorageUtils.stripProtocol(pattern, account, getStorageType()))) {
                String containerPattern = ExtendAzureStorageUtils.getContainerName(pattern, getStorageType());
                String filePattern = ExtendAzureStorageUtils.getFilename(pattern, getStorageType());
                resources = resolveResources(containerPattern, filePattern);
            } else {
                return new Resource[] { getResource(pattern) };
            }
        }
        if (null == resources) {
            throw new IOException("Resources not found at " + pattern);
        }
        return resources;
    }

    protected String getStorageAccountName(String pattern) {
        return null;
    }

    /**
     * @see ResourcePatternResolver#getResource(java.lang.String)
     */
    @Override
    public Resource getResource(String location) {
        Resource resource = null;

        if (ExtendAzureStorageUtils.isAzureStorageResource(location, getStorageType())) {
            resource = getStorageResource(location, true);
        }

        if (null == resource) {
            throw new IllegalArgumentException("Resource not found at " + location);
        }
        return resource;
    }
}
