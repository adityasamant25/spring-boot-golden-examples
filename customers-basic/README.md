# Customers Basic

This module is the most basic REST based microservice that can be created with Spring Boot. 

## Features
* Based on `spring-web` library
* Accompanied by stable unit and integration tests. Integration tests use the `WebMvcTest` framework
* Showcases custom exception handling in Spring Boot 
* API documentation is auto-generated using the springdoc-openapi java library 
* Uses `spring-boot-actuator` to expose the default metrics and health information.

## What's new
### v0.0.5-SNAPSHOT
Added distributed tracing support using Jaeger

### v0.0.4
* Enabled prometheus metrics via micrometer

### v0.0.3
* Enabled multi-architecture support for the Docker image

### v0.0.2
* Aligned as per the package changes in the `common` module

## Quick Start

### Run on Local
**Prerequisite:** Java 21, Maven 3.9.x

#### Build
```
git clone https://github.com/adityasamant25/spring-boot-golden-examples.git
```
```
cd spring-boot-golden-examples/customers-basic
```
```
./mvnw clean install
```

#### Run
```
java -jar target/customers-basic-0.0.3.jar
```

#### Access from a terminal
```
curl localhost:8081/api/customers
```

The expected output is:
```json
[{"id":1,"firstName":"John","lastName":"Doe","country":"Australia"},{"id":2,"firstName":"Alice","lastName":"Smith","country":"USA"},{"id":3,"firstName":"Bob","lastName":"Stevens","country":"England"}]
```

#### Access from a browser
http://localhost:8081/api/customers

#### Cleanup
Terminate the application using Ctrl+C on the terminal running the process.


### Run on Docker
**Prerequisite:** A running Docker Engine. Docker Desktop is preferred for local development.
#### Build the image (using buildpack)
```
git clone https://github.com/adityasamant25/spring-boot-golden-examples.git
```
```
cd spring-boot-golden-examples/customers-basic
```
```
./mvnw clean install
```

Please check the following before running the next command:
* In the parent pom (spring-boot-golden-examples/pom.xml) change the value of the below property
```
<dockerhub.username>adityasamantlearnings</dockerhub.username>
```

* In case you are running on a machine with x86 CPU architecture, comment out the `image` section in `customers-basic/pom.xml`
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
e.g. `adityasamantlearnings/springboot-customers-basic:0.0.3`

#### Build the image (using Docker file)
```
git clone https://github.com/adityasamant25/spring-boot-golden-examples.git
```
```
cd spring-boot-golden-examples/customers-basic
```
```
./mvnw clean install
```

```
docker build . -t adityasamantlearnings/springboot-customers-basic:0.0.3
```

This will create a Docker image as per the tag defined. In case you wish to push the image to your own DockerHub repository, 
please tag it appropriately.

#### Run the application
> [!NOTE] 
> The below command will run the application with the image residing on the `adityasamantlearnings/springboot-customers-basic` repository which supports both x86 and arm64 CPU architectures.
Replace `adityasamantlearnings/springboot-customers-basic:0.0.3` with the appropriate name and tag of the image you wish to run.
```
docker run -d --name customers-basic -p 8081:8081 adityasamantlearnings/springboot-customers-basic:0.0.3
```

#### Access from a terminal
```
curl localhost:8081/api/customers
```

The expected output is:
```json
[{"id":1,"firstName":"John","lastName":"Doe","country":"Australia"},{"id":2,"firstName":"Alice","lastName":"Smith","country":"USA"},{"id":3,"firstName":"Bob","lastName":"Stevens","country":"England"}]
```

#### Access from a browser
http://localhost:8081/api/customers

#### Push the image to DockerHub
Example:
```
docker image push adityasamantlearnings/springboot-customers-basic:0.0.3
```

#### Build and Push a multi-architecture supported image to DockerHub
Example:
```
docker buildx build --platform linux/amd64,linux/arm64 -t adityasamantlearnings/springboot-customers-basic:0.0.3 --push .
```


#### Cleanup
```
docker stop customers-basic
docker rm customers-basic
docker rmi adityasamantlearnings/springboot-customers-basic:0.0.3
```

### Run on Kubernetes
**Prerequisite:** A running Kubernetes cluster. To quickly spin-up a local Kubernetes cluster, [minikube](https://minikube.sigs.k8s.io/docs/start/) is a great choice.  
[kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl) should be installed.

> [!NOTE]
> The below command will run the application with the image residing on the `adityasamantlearnings/springboot-customers-basic` repository which supports both x86 and arm64 architectures.
> If you wish to build your own image, follow the steps mentioned in the `Run on Docker` section.

#### Apply the manifest using Kustomize
```
git clone https://github.com/adityasamant25/spring-boot-golden-examples.git
```
```
cd spring-boot-golden-examples/customers-basic/kubernetes/overlay/dev
```
```
kubectl apply -k .
```

#### Get the URL of the minikube service
```
minikube service springboot-customers-basic --url
```

#### Access using curl from a new terminal
```
curl <minikube service url>/api/customers
```

#### Access from a browser
http://<minikube service url>/api/customers

#### Cleanup
```
kubectl delete deployment springboot-customers-basic
kubectl delete service springboot-customers-basic
```

Terminate the minikube tunnel using Ctrl+C on the terminal running it.


