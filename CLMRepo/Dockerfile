# Build stage
FROM maven:3.8.7-openjdk-18-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean -B package -DskipTests

# Package stage
FROM openjdk:17.0.2
COPY --from=build /home/app/target/CLMRepo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
