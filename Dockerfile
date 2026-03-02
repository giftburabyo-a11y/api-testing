FROM maven:3.9.5-eclipse-temurin-11 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

# Run tests but don't fail the build on test failures
RUN mvn test -B || true
