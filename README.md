[![CodeQL](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml) [![CI](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml) [![Markdown Links Check](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml) 

# Spring Cloud Azure Samples  
- The repository hold samples about using [Spring Cloud Azure](https://learn.microsoft.com/azure/developer/java/spring-framework/) libraries. 
- The **main** branch is using the latest stable version of Spring Cloud Azure. If you want to find sample about specific version of Spring Cloud Azure, please switch to corresponding tag in this repository.

## Getting Help
- If you have any question about using these samples, please [create an new issue](https://github.com/Azure-Samples/azure-spring-boot-samples/issues/new/choose).

## Run Samples Based On Spring Native in Spring Boot 2
Two Maven profiles have been defined in this project to support compiling Spring applications to native executables: `buildpack` and `native`. The `buildpack` profile will use [Buildpacks](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-buildpacks) and the `native` profile will use [Native Build Tools](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-native-build-tools). Please follow the [storage-blob-native sample](spring-native/storage-blob-native) for more details.

**Note**: This section is only suitable for Spring Native in Spring Boot 2.

## Samples for Spring Boot 3

To get samples for Spring Boot 3, please refer to the content in each sample's *README.md* like this:
> Current sample is using Spring Cloud Azure 4.x (which is compatible with Spring Boot 2.x). 
> If you want sample about Spring Cloud Azure 6.x (which is compatible with Spring Boot 3.x), 
> please refer to [CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3.md](./CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md).

If you're adding a new example, here's a template that converts the example to support Spring Boot 3: [./CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md](CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md).
