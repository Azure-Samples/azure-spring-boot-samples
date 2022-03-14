package com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.azure.activedirectory;

/**
 * This exception is thrown when failed to create Azure Active Directory jwt assertion.
 *
 * @author Rujun Chen
 * @since 2022-02-23
 */
public class AzureActiveDirectoryAssertionException extends Exception {

    /**
     * Creates a {@code AzureActiveDirectoryAssertionException} with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A {@code
     * null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 2022-02-23
     */
    public AzureActiveDirectoryAssertionException(String message, Throwable cause) {
        super(message, cause);
    }
}
