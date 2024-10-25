FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests=true

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/TimeTracker-0.0.1-SNAPSHOT.jar /app/TimeTracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/TimeTracker.jar"]