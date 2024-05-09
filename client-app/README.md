# Client App

This module is a representation of a client microservice created with Spring Boot. It uses the new `RestClient` introduced 
in Spring Framework 6.x

## Features
* Based on `spring-web` library and `RestClient` introduced in Spring Framework 6.x
* Accompanied by stable unit and integration tests. Integration tests use the `WebMvcTest` and `RestClientTest` frameworks
* Showcases Junit tests for client application by mocking server invocations
* API documentation is auto-generated using the springdoc-openapi java library 
* Uses `spring-boot-actuator` to expose the default metrics and health information.

## Quick Start

### Run on Local
**Prerequisite:** Java 21, Maven 3.9.x

#### Build
```
git clone https://github.com/adityasamant25/spring-boot-golden-examples.git
```
```
cd spring-boot-golden-examples
```
```
./mvnw clean install
```

#### Run the server application
```
java -jar customers-basic/target/customers-basic-0.0.2.jar
```

#### From a new terminal, run the client application
```
java -jar client-app/target/client-app-0.0.1.jar
```

#### Access from a new terminal
```
curl localhost:8081/api/customers
```

The expected output is:
```json
[{"id":1,"firstName":"John","lastName":"Doe","country":"Australia"},{"id":2,"firstName":"Alice","lastName":"Smith","country":"USA"},{"id":3,"firstName":"Bob","lastName":"Stevens","country":"England"}]
```

#### Access from a browser
http://localhost:8082/api/customers

#### Cleanup
Terminate the application using Ctrl+C on the terminal running the client and server processes.


### Run on Docker
**Prerequisite:** A running Docker Engine. Docker Desktop is preferred for local development.

#### Build the server image
See appropriate instructions in the README.md for the customers-basic application.

#### Build the client image
```
git clone https://github.com/adityasamant25/spring-boot-golden-examples.git
```
```
cd spring-boot-golden-examples/client-app
```
```
./mvnw clean install
```

Please check the following before running the next command:
* In the parent pom (spring-boot-golden-examples/pom.xml) change the value of the below property
```
<dockerhub.username>adityasamantlearnings</dockerhub.username>
```

* In case you are running on a machine with x86 CPU architecture, comment out the `image` section in `client-app/pom.xml`
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </exclude>
        </excludes>
        <!-- <image>
            <name>${dockerImageName}</name>
            <builder>${docker.imagebuilder}</builder>
        </image> -->
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>build-info</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

```
./mvnw spring-boot:build-image -DskipTests
```

This will create a Docker image as per the naming convention defined:  
e.g. `adityasamantlearnings/springboot-client-app:0.0.1`

#### Run the application using docker-compose
> [!NOTE] 
> The below command will run the application with the images residing on the `adityasamantlearnings/springboot-client-app` and `adityasamantlearnings/springboot-customers-basic` repositories which are built for arm64 architecture.
Replace the image paths with the appropriate name and tag of the images you wish to run in the docker-compose.yml file.
```
docker-compose up
```

#### Access from a terminal
```
curl localhost:8082/api/customers
```

The expected output is:
```json
[{"id":1,"firstName":"John","lastName":"Doe","country":"Australia"},{"id":2,"firstName":"Alice","lastName":"Smith","country":"USA"},{"id":3,"firstName":"Bob","lastName":"Stevens","country":"England"}]
```

#### Access from a browser
http://localhost:8082/api/customers

#### Push the image to DockerHub
Example:
```
docker image push adityasamantlearnings/springboot-client-app:0.0.1
```

#### Cleanup
```
Terminate the running docker-compose command
docker-compose stop
docker-compose rm
docker rmi adityasamantlearnings/springboot-client-app:0.0.1
```

### Run on Kubernetes
**Prerequisite:** A running Kubernetes cluster. To quickly spin-up a local Kubernetes cluster, [minikube](https://minikube.sigs.k8s.io/docs/start/) is a great choice.  
[kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl) should be installed.

> [!NOTE]
> The below command will run the application with the images residing on the `adityasamantlearnings/springboot-client-app` and `adityasamantlearnings/springboot-customers-basic` repositories which is built for arm64 architecture.
> If you wish to build your own image, follow the steps mentioned in the `Run on Docker` section.

#### Apply the manifest using Kustomize
```
git clone https://github.com/adityasamant25/spring-boot-golden-examples.git
```
```
cd spring-boot-golden-examples/client-app/kubernetes/overlay/dev
```
```
kubectl apply -k .
```

#### Get the URL of the minikube service
```
minikube service springboot-client-app --url
```

#### Access using curl from a new terminal
```
curl <minikube service url>/api/customers
```

#### Access from a browser
http://<minikube service url>/api/customers

#### Cleanup
```
kubectl delete -k .
```

Terminate the minikube tunnel using Ctrl+C on the terminal running it.


