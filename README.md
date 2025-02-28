# Spring Boot Golden Examples

This project is a reference on adopting the Spring Boot Framework to build Java based microservices.
The project is divided into submodules. Each submodule will provide sample code on adopting Spring Boot libraries, that
follow the best practices. The project is kept up-to-date with the latest Spring Boot version. The modules are 
accompanied by unit and integration tests. CRUD operations are demonstrated using a sample entity called `Customers`.

## Features
* The project is based on the latest LTS release of Java (21) and Spring Boot 3.4.x
* Each module is containerized using Docker and deployable on Kubernetes
* Docker images are built using the `spring-boot-maven-plugin`
* Docker images are multi-architecture (x86, arm64) compliant
* The project's CI is managed using GitHub Actions
* The project uses the `spotless-maven-plugin` for code formatting


## Submodules

### Commons
This module holds the classes and objects that are common to other modules. e.g. Data Transfer Objects (DTOs).  
For details, please refer to the documentation of the [Commons module](common/README.md).

### Customers Basic
This module is the most basic REST based microservice that can be created with Spring Boot. It uses the `spring-web` 
dependency to provide REST APIs for CRUD operations on the `Customers` entity. It is accompanied by unit and 
integration tests. Integration tests use the WebMvcTest framework. It also showcases custom exception handling in 
Spring Boot. API documentation is auto-generated using the springdoc-openapi java library. It uses `spring-boot-actuator` 
to expose the default metrics and health information.  
For details, please refer to the documentation of the [Customers Basic module](customers-basic/README.md).

### Client App

This module is a representation of a client microservice created with Spring Boot. It uses the new `RestClient` introduced
in Spring Framework 6.x to invoke other microservice APIs over REST. It is accompanied by stable unit and integration tests. Integration tests use the `WebMvcTest` and `RestClientTest` frameworks.
For details, please refer to the documentation of the [Client App module](client-app/README.md).