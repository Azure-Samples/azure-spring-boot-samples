# 1 Convert sample to use Spring Boot 3

## 1.1 Update your infra

- [JDK17](https://www.oracle.com/java/technologies/downloads/).

## 1.2 Update the pom parent

Update the pom parent file:

Current for Spring Boot 2:
   ```xml
   <parent>
     <groupId>com.azure.spring</groupId>
     <artifactId>azure-spring-boot-samples-4.x</artifactId>
     <version>1.0.0</version>
     <relativePath>../../../azure-spring-boot-samples-4.x-pom.xml</relativePath>
   </parent>
   ```

Updated for Spring Boot 3 to the following:
   ```xml
   <parent>
     <groupId>com.azure.spring</groupId>
     <artifactId>azure-spring-boot-samples-6.x</artifactId>
     <version>1.0.0</version>
     <relativePath>../../../azure-spring-boot-samples-6.x-pom.xml</relativePath>
   </parent>
   ```

If you want to build from the root pom file *azure-spring-boot-samples-6.x-pom.xml*, add this sample as a submodule like below:

   ```xml
   <modules>
     <module>cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample</module>
   </modules>
   ```
   