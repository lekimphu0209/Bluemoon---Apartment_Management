FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/\*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
