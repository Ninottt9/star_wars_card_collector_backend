# Use a base image with Java and Spring Boot support
FROM openjdk:19-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container at path /app
COPY target/star_wars_card_collector-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your backend application runs on
EXPOSE 8081

# Command to run the backend application
CMD ["java", "-jar", "app.jar"]
