# Step 1: Use a base image that has JDK
FROM openjdk:17-jdk-slim

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the jar file into the container
COPY target/task-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port your app will run on
EXPOSE 8080

# Step 5: Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

# docker build -t your-app-name .
# docker run -d -p 8080:8080 your-app-name
# docker ps
# docker stop <container-id>