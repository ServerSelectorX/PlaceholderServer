FROM docker.io/eclipse-temurin:21 AS base

COPY target/PlaceholderServer-*.jar /PlaceholderServer.jar

ENTRYPOINT ["java", "-jar", "/PlaceholderServer.jar"]
