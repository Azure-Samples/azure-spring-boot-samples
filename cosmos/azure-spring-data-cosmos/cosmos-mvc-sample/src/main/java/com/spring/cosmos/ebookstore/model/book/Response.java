// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.spring.cosmos.ebookstore.model.book;

import java.util.List;

public final class Response {
    private final String continuationToken;
    private final List<Book> books;

    public Response(String continuationToken, List<Book> books) {
        this.continuationToken = continuationToken;
        this.books = books;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "Response{" +
                "continuationToken='" + continuationToken + '\'' +
                '}';
    }
}
