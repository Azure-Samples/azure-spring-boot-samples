package com.azure.spring.sample.storage.resource.core;

import com.azure.spring.cloud.core.resource.StorageType;
import org.springframework.util.Assert;

import java.util.Locale;

final class ExtendAzureStorageUtils {

    private ExtendAzureStorageUtils() {
    }

    /**
     * Path separator character for resource location.
     */
    public static final String PATH_DELIMITER = "/";

//    public static final String DOUBLE_PATH_DELIMITER = "//";
    public static final String PROTOCOL_SCHEMA_DASH_DELIMITER = "-";
    public static final String PROTOCOL_SCHEMA_COLON_DELIMITER = ":";
//    public static final String PROTOCOL_SCHEMA_PART_DELIMITER = PROTOCOL_SCHEMA_COLON_DELIMITER + DOUBLE_PATH_DELIMITER;


    /**
     * Prefix stands for storage protocol.
     */
    private static final String STORAGE_PROTOCOL_PREFIX = "azure-%s-";

    private static final String STORAGE_PROTOCOL_ACCOUNT_PREFIX = "azure-%s-%s://";

    /**
     * Whether the given combination of location and storageType represents a valid Azure storage resource.
     *
     * @param location the location
     * @param storageType the storagetype of current resource
     * @return true - valid Azure storage resource<br>
     *         false - not valid Azure storage resource
     */
    static boolean isAzureStorageResource(String location, StorageType storageType) {
        Assert.notNull(location, "Location must not be null");
        return location.toLowerCase(Locale.ROOT).startsWith(getStorageAccountProtocolPrefix(location, storageType));
    }

    /**
     * Get the storage protocal prefix string of storageType.
     *
     * @param storageType the storage type of current resource
     * @return the exact storage protocal prefix string
     */
    static String getStorageProtocolPrefix(StorageType storageType) {
        return String.format(STORAGE_PROTOCOL_PREFIX, storageType.getType());
    }

    static String getStorageAccountProtocolPrefix(String location, StorageType storageType) {
        return String.format(STORAGE_PROTOCOL_ACCOUNT_PREFIX, storageType.getType(), getStorageAccountName(location, storageType));
    }

    /**
     * Get the location's path.
     *
     * @param location the location represents the resource
     * @param account the Storage Account
     * @param storageType the storageType
     * @return the location's path
     */
    public static String stripProtocol(String location, String account, StorageType storageType) {
        Assert.notNull(location, "Location must not be null");
        assertIsAzureStorageLocation(location, storageType);
        return location.substring(getStorageAccountProtocolPrefix(location, storageType).length());
    }

    /**
     * Get the storage container(fileShare) name from the given location.
     *
     * @param location the location represents the resource
     * @param storageType the storageType
     * @return the container(fileShare) name  name of current location
     */
    static String getContainerName(String location, StorageType storageType) {
        assertIsAzureStorageLocation(location, storageType);
        int containerEndIndex = assertContainerValid(location, storageType);
        return location.substring(getStorageAccountProtocolPrefix(location, storageType).length(),
            containerEndIndex);
    }

    static String getStorageAccountName(String location, StorageType storageType) {
        String storageProtocolPrefix = ExtendAzureStorageUtils.getStorageProtocolPrefix(storageType);
        int accountStartIndex = location.indexOf(PROTOCOL_SCHEMA_DASH_DELIMITER, storageProtocolPrefix.length() - 1);
        int accountEndIndex = location.indexOf(PROTOCOL_SCHEMA_COLON_DELIMITER, accountStartIndex);
        if (accountStartIndex == -1 || accountEndIndex == -1 || accountEndIndex == accountStartIndex) {
            throw new IllegalArgumentException(
                String.format("The location '%s' does not contain a valid account name", location));
        }
        return location.substring(accountStartIndex + 1, accountEndIndex);
    }

    /**
     * Get the file name from the given location.
     *
     * @param location the location represents the resource
     * @param storageType the storageType
     * @return the file name of current location
     */
    static String getFilename(String location, StorageType storageType) {
        assertIsAzureStorageLocation(location, storageType);
        int containerEndIndex = assertContainerValid(location, storageType);
        if (location.endsWith(PATH_DELIMITER)) {
            return location.substring(++containerEndIndex, location.length() - 1);
        }
        return location.substring(++containerEndIndex);
    }


    /**
     * Assert the given combination of location and storageType represents a valid Azure storage resource.
     *
     * @param location the location
     * @param storageType the storagetype of current resource
     */
    static void assertIsAzureStorageLocation(String location, StorageType storageType) {
        if (!ExtendAzureStorageUtils.isAzureStorageResource(location, storageType)) {
            throw new IllegalArgumentException(
                String.format("The location '%s' is not a valid Azure storage %s location", location,
                    storageType.getType()));
        }
    }

    /**
     * Assert the given combination of location and storageType contains a valid Azure storage container.
     *
     * @param location the location
     * @param storageType the storagetype of current resource
     * @return the end index of container in the location
     */
    private static int assertContainerValid(String location, StorageType storageType) {
        String storageProtocolPrefix = getStorageAccountProtocolPrefix(location, storageType);
        int containerEndIndex = location.indexOf(PATH_DELIMITER, storageProtocolPrefix.length());
        if (containerEndIndex == -1 || containerEndIndex == storageProtocolPrefix.length()) {
            throw new IllegalArgumentException(
                String.format("The location '%s' does not contain a valid container name", location));
        }
        return containerEndIndex;
    }
}
