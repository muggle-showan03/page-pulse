FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy maven wrapper and pom file
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission to the wrapper
RUN chmod +x ./mvnw

# Go offline to cache dependencies
RUN ./mvnw dependency:go-offline

# Copy source code and build
COPY src src
RUN ./mvnw package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
