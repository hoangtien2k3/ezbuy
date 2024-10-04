# Use Eclipse Temurin JDK 21 image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy JAR file from local machine to container
COPY target/ezbuy.jar /app/ezbuy.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/ezbuy.jar"]
