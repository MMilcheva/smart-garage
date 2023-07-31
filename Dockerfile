FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ./build/libs/smart_garage-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]