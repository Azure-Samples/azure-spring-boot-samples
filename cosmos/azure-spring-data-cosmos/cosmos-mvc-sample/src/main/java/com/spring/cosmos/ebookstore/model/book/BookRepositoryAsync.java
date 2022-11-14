// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.spring.cosmos.ebookstore.model.book;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedFlux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookRepositoryAsync {
    private CosmosAsyncClient cosmosAsyncClient;

    @Autowired
    public BookRepositoryAsync(CosmosAsyncClient cosmosAsyncClient) {
        this.cosmosAsyncClient = cosmosAsyncClient;
    }

    private CosmosAsyncContainer getAsyncContainer() {
        return cosmosAsyncClient
                .getDatabase("store")
                .getContainer("book");
    }

    public Response getBooks(int preferredPageSize, int pagesToReturn) {
        CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
        String query = "SELECT * FROM Book";
        CosmosPagedFlux<Book> pagedFluxResponse =
                getAsyncContainer()
                        .queryItems(
                                query, queryOptions, Book.class);
        return pagedFluxResponse
                .byPage(preferredPageSize)
                .take(pagesToReturn)
                .map(page -> {
                    return new Response(page.getContinuationToken(), page.getResults());
                })
                .blockFirst();
    }

    public Response getBooks(int preferredPageSize, int pagesToReturn, String category) {
        CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
        String query = "SELECT * FROM Book where Book.category = '"+category+"'";
        CosmosPagedFlux<Book> pagedFluxResponse =
                getAsyncContainer()
                        .queryItems(
                                query, queryOptions, Book.class);
        return pagedFluxResponse
                .byPage(preferredPageSize)
                .take(pagesToReturn)
                .map(page -> {
                    return new Response(page.getContinuationToken(), page.getResults());
                })
                .blockFirst();
    }    


    public Response getBooks(String continuationToken, int preferredPageSize, int pagesToReturn) {
        CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
        String query = "SELECT * FROM Book";
        CosmosPagedFlux<Book> pagedFluxResponse = getAsyncContainer()
                .queryItems(
                        query, queryOptions, Book.class);
        return pagedFluxResponse
                .byPage(continuationToken, preferredPageSize)
                .take(pagesToReturn)
                .map(page -> {
                    return new Response(page.getContinuationToken(), page.getResults());
                })
                .blockFirst();
    }


}
