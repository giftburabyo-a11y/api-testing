FROM maven:3.9.5-eclipse-temurin-11 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn test -B --no-transfer-progress -Dmaven.test.failure.ignore=true || true

CMD ["mvn", "test", "-B", "--no-transfer-progress", "-Dmaven.test.failure.ignore=true"]