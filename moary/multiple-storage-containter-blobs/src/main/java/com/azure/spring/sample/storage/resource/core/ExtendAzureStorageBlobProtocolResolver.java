package com.azure.spring.sample.storage.resource.core;

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
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class ExtendAzureStorageBlobProtocolResolver extends ExtendAbstractAzureStorageProtocolResolver {
    private Map<String, BlobServiceClient> blobServiceClientMap = new HashMap<>();

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
    protected Stream<AbstractAzureStorageProtocolResolver.StorageContainerItem> listStorageContainers(String containerPrefix) {

        ListBlobContainersOptions options = new ListBlobContainersOptions();
        options.setPrefix(containerPrefix);
        options.setDetails(RETRIEVE_NOTHING_CONTAINER_DETAILS);
        return getBlobServiceClient(containerPrefix)
            .listBlobContainers(options, null)
            .stream()
            .map(BlobContainerItem::getName)
            .map(AbstractAzureStorageProtocolResolver.StorageContainerItem::new);
    }

    @Override
    protected AbstractAzureStorageProtocolResolver.StorageContainerClient getStorageContainerClient(String name) {
        return new ExtendAzureStorageBlobProtocolResolver.StorageBlobContainerClient(name);
    }

    private class StorageBlobContainerClient implements AbstractAzureStorageProtocolResolver.StorageContainerClient {

        private final String name;

        StorageBlobContainerClient(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Stream<AbstractAzureStorageProtocolResolver.StorageItem> listItems(String itemPrefix) {
            ListBlobsOptions options = new ListBlobsOptions();
            options.setPrefix(itemPrefix);
            options.setDetails(RETRIEVE_NOTHING_DETAILS);
            BlobContainerClient containerClient = getBlobServiceClient(itemPrefix).getBlobContainerClient(name);
            if (containerClient.exists()) {
                return containerClient.listBlobs(options, null)
                                      .stream()
                                      .map(blob -> new AbstractAzureStorageProtocolResolver.StorageItem(name, blob.getName(), getStorageType()));
            } else {
                return Stream.empty();
            }
        }
    }

    @Override
    protected Resource getStorageResource(String location, Boolean autoCreate) {
        return new StorageBlobResource(getBlobServiceClient(location), location, autoCreate);
    }

    private BlobServiceClient getBlobServiceClient(String locationPrefix) {
        String storageAccount = ExtendAzureStorageUtils.getStorageAccountName(locationPrefix, getStorageType());
        Assert.notNull(storageAccount, "storageAccount can not be null.");
        String accountKey = storageAccount.toLowerCase(Locale.ROOT);
        if (blobServiceClientMap.containsKey(accountKey)) {
            return blobServiceClientMap.get(accountKey);
        }

        BlobServiceClient blobServiceClient = beanFactory.getBean(
            accountKey + BlobContainerClient.class.getSimpleName(), BlobServiceClient.class);
        Assert.notNull(blobServiceClient, "blobServiceClient can not be null.");
        blobServiceClientMap.put(accountKey, blobServiceClient);
        return blobServiceClient;
    }
}
