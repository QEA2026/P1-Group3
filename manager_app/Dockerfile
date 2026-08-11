# --- Build stage ---
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY manager_app/pom.xml .
RUN mvn dependency:go-offline

COPY manager_app/src ./src
RUN mvn package -DskipTests

# --- Run stage ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]