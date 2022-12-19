# How to run samples with Spring Boot 3?

The current examples are built based on Spring Cloud Azure 4.x by default. If you want to use Spring Cloud Azure 6.x based on Spring Boot 3, this document will tell you how to do it.

## How to make a sample support running with Spring Boot 3?

1. It's required to add a document *HOW_TO_RUN_THIS_SAMPLE_WITH_SPRING_BOOT_3.md* to introduce the differences, it should be put together with the sample's *README.md*. The outline and contents of the document are as follows:

```markdown
# How to run this sample with Spring Boot 3?

1. Update parent pom path

2. Update dependencies(optional)

3. Update Java code(optional)

```

2. Update the parent pom file to *azure-spring-boot-samples-6.x-pom.xml*.

3. It's optional to list the different dependencies that need to be updated if any other dependencies are required.

4. It's optional to list the source code change list to be updated if any code needs to be updated.

## How to know if a sample can run with Spring Boot 3?

If a sample can run in Spring Cloud Azure 6.x, the *README.md* file of the sample will clearly state that it supports running with Spring Boot 3, and there will be *HOW_TO_RUN_SAMPLES_WITH_SPRING_BOOT_3.md* file to guide.
