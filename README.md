
## Background
This repo is migrated from [Azure/azure-sdk-for-java](https://github.com/Azure/azure-sdk-for-java/tree/azure-spring-boot-starter_3.6.0/sdk/spring/azure-spring-boot-samples)

why



## All samples in this repo

|                                                              |      |      |
| ------------------------------------------------------------ | ---- | ---- |
| [azure-spring-boot-sample-active-directory-b2c-oidc](/aad/azure-spring-boot-sample-active-directory-b2c-oidc) |      |      |
| azure-spring-boot-sample-active-directory-b2c-resource-server |      |      |
| azure-spring-boot-sample-active-directory-resource-server    |      |      |
| azure-spring-boot-sample-active-directory-resource-server-by-filter |      |      |
| azure-spring-boot-sample-active-directory-resource-server-by-filter-stateless |      |      |
| azure-spring-boot-sample-active-directory-resource-server-obo |      |      |
| azure-spring-boot-sample-active-directory-webapp             |      |      |
| azure-appconfiguration-conversion-sample-complete            |      |      |
| azure-appconfiguration-conversion-sample-initial             |      |      |
| azure-appconfiguration-sample                                |      |      |
| feature-management-sample                                    |      |      |
| feature-management-web-sample                                |      |      |




  - points
        - main branch is depend on released libraries,depend on unreleased libraries is forbidden
  - flow of development
        - main branch; spring release;released tag;
            - based on azure-spring-boot-starter's version;
        - main branch: latest sample codes depend on latest released libraries.
        - tag names;
        -   3 develop scenarios
            - develop sample with latest released libraries
            - develop sample unreleased libraries
                checkout a new branch based on main branch, and write code on new branch
                
            - develop with specific version of libraries
                - for some reason, our library have track1 or track2 ,different versions
                - if you want to change different version with diffrent sample code
                - you can checkout a develop branch
                - you can tag the the branch;
                - and in the main branch's 