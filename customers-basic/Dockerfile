FROM eclipse-temurin:21-jre-ubi9-minimal
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]