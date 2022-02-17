FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/Backend_Task-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]