FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/*.jar customer-service.jar

EXPOSE 7000

# Specify the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "customer-service.jar"]
