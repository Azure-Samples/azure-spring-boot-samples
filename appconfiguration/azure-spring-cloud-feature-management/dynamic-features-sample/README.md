# Feature Variant Sample

This sample describes how to use [spring-cloud-feature-management](https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.6.0/sdk/appconfiguration/spring-cloud-azure-feature-management/README.md) to manage feature variants.

## Getting Started

`spring-cloud-azure-feature-management` doesn't require use of the App Configuration service, but can be integrated with it. The next section shows how to use the library without the App Configuration service, the section after shows how to update the example to use it with the App Configuration service.

### How to run
Start the application and check the resulting console output to check the returned value.

1. Load features from application.yml
```
mvn spring-boot:run
```

1. Go to `http://localhost:8080/?user=Alec`, and you should see:

```console
Shopping Cart: Size 400 Color green
```

This shows a user getting there assigned variant.

1. Go to `http://localhost:8080/?group=Ring1`, and you should see:

```console
Shopping Cart: Size 150 Color gray
```

This shows a group getting there assigned variant.

1. Go to `http://localhost:8080/?user=Alec&group=Ring1`, and you should see:

```console
Shopping Cart: Size 400 Color green
```

This shows a user getting there assigned group even though they are part of Ring 1.

1. Go to `http://localhost:8080/?user=Alice&group=Ring1`, and you should see:

```console
Shopping Cart: Size 150 Color gray
```

THis shows a user not assigned a variant getting a variant based on there group.
