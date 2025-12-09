ARG BASE_JRE_IMAGE=eclipse-temurin:21-jre-alpine
# Stage 1: Download the Dependency
FROM eclipse-temurin:21-jdk-alpine AS dependencies
RUN apk add --no-cache maven
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

# Stage 2: Build the Application
FROM dependencies AS builder
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 3: Create a Lightweight Runtime Image
# We use a smaller JRE image (Alpine) for production
FROM ${BASE_JRE_IMAGE} AS runtime
WORKDIR /app

ARG JAR_FILE=*.jar
COPY --from=builder /build/target/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]