// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource.extend.storage.blob;

import com.azure.spring.cloud.core.resource.AbstractAzureStorageProtocolResolver;
import com.azure.spring.cloud.core.resource.StorageBlobResource;
import com.azure.spring.cloud.core.resource.StorageType;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobContainerListDetails;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.ListBlobContainersOptions;
import com.azure.storage.blob.models.ListBlobsOptions;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

/**
 * A {@link ProtocolResolver} implementation for the {@code azure-blob://} protocol.
 */
public final class ExtendAzureStorageBlobProtocolResolver extends AbstractAzureStorageProtocolResolver {

    private BlobServiceClient blobServiceClient;
    private String blobServiceClientBeanName;

    private static final BlobListDetails RETRIEVE_NOTHING_DETAILS = new BlobListDetails();
    private static final BlobContainerListDetails RETRIEVE_NOTHING_CONTAINER_DETAILS = new BlobContainerListDetails();

    static {
        RETRIEVE_NOTHING_DETAILS.setRetrieveCopy(false);
        RETRIEVE_NOTHING_DETAILS.setRetrieveDeletedBlobs(false);
        RETRIEVE_NOTHING_DETAILS.setRetrieveDeletedBlobsWithVersions(false);
        RETRIEVE_NOTHING_DETAILS.setRetrieveImmutabilityPolicy(false);
        RETRIEVE_NOTHING_DETAILS.setRetrieveMetadata(false);
        RETRIEVE_NOTHING_DETAILS.setRetrieveLegalHold(false);
        RETRIEVE_NOTHING_DETAILS.setRetrieveSnapshots(false);
        RETRIEVE_NOTHING_DETAILS.setRetrieveTags(false);
        RETRIEVE_NOTHING_DETAILS.setRetrieveUncommittedBlobs(false);
        RETRIEVE_NOTHING_CONTAINER_DETAILS.setRetrieveMetadata(false);
        RETRIEVE_NOTHING_CONTAINER_DETAILS.setRetrieveDeleted(false);
    }

    /**
     * The storageType of current protocolResolver.
     *
     * @return StorageType.BLOB;
     */
    @Override
    protected StorageType getStorageType() {
        return StorageType.BLOB;
    }

    @Override
    protected Stream<StorageContainerItem> listStorageContainers(String containerPrefix) {

        ListBlobContainersOptions options = new ListBlobContainersOptions();
        options.setPrefix(containerPrefix);
        options.setDetails(RETRIEVE_NOTHING_CONTAINER_DETAILS);
        return getBlobServiceClient().listBlobContainers(options, null)
                                     .stream()
                                     .map(BlobContainerItem::getName)
                                     .map(StorageContainerItem::new);
    }

    @Override
    protected StorageContainerClient getStorageContainerClient(String name) {
        return new StorageBlobContainerClient(name);
    }

    private class StorageBlobContainerClient implements StorageContainerClient {

        private final String name;

        StorageBlobContainerClient(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Stream<StorageItem> listItems(String itemPrefix) {
            ListBlobsOptions options = new ListBlobsOptions();
            options.setPrefix(itemPrefix);
            options.setDetails(RETRIEVE_NOTHING_DETAILS);
            BlobContainerClient containerClient = getBlobServiceClient().getBlobContainerClient(name);
            if (containerClient.exists()) {
                return containerClient.listBlobs(options, null)
                                      .stream()
                                      .map(blob -> new StorageItem(name, blob.getName(), getStorageType()));
            } else {
                return Stream.empty();
            }
        }
    }

    @Override
    protected Resource getStorageResource(String location, Boolean autoCreate) {
        return new StorageBlobResource(getBlobServiceClient(), location, autoCreate);
    }

    private BlobServiceClient getBlobServiceClient() {
        if (blobServiceClient == null) {
            if (StringUtils.hasText(blobServiceClientBeanName)) {
                blobServiceClient = beanFactory.getBean(blobServiceClientBeanName, BlobServiceClient.class);
            } else {
                blobServiceClient = beanFactory.getBean(BlobServiceClient.class);
            }
        }
        return blobServiceClient;
    }

    public void setBlobServiceClient(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    public void setBlobServiceClientBeanName(String blobServiceClientBeanName) {
        this.blobServiceClientBeanName = blobServiceClientBeanName;
    }
}
