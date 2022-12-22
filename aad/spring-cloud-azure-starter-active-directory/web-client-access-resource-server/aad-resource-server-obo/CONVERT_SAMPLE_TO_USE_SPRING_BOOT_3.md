# 1 Convert sample to use Spring Boot 3

## 1.1 Update your infra

- [JDK17](https://www.oracle.com/java/technologies/downloads/).

## 1.2 Update Java code

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
  