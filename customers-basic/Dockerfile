FROM eclipse-temurin:21-jre-ubi9-minimal
RUN microdnf install -y hostname && microdnf clean all
VOLUME /tmp
COPY target/*.jar app.jar
COPY resilience-agent /resilience-agent
ENTRYPOINT ["java","-jar","/app.jar"]