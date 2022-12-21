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
     <module>aad/spring-cloud-spring-cloud-azure-starter-active-directory/web-client-access-resource-server/aad-resource-server-obo</module>
   </modules>
   ```

## 1.3 Update Java code

- Update class path for class `com.azure.spring.sample.aad.controller.SampleController`.

  Current package path for Spring Boot 2:

    ```java
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    ```

  Updated for Spring Boot 3 to the following:

    ```java
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    ```
  