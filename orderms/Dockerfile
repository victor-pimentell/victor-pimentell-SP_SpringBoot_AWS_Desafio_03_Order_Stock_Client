FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /builder
COPY . .
RUN mvn clean package -DskipTests --batch-mode
FROM eclipse-temurin:21-alpine
COPY --from=builder /builder/target/*.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]