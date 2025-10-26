# ----- Stage 1: Build the application -----
# Use an official Gradle image with JDK 21
FROM gradle:8.7-jdk21 AS builder

# Set the working directory
WORKDIR /app

# Copy the build files to leverage Docker layer caching
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Download dependencies (this layer is cached if build files don't change)
# Using --no-daemon is recommended for CI/container environments
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the source code
COPY src/ src/

# Build the application, skipping tests (tests should be run in a separate CI step)
RUN ./gradlew build --no-daemon -x test


# ----- Stage 2: Create the final, lightweight runtime image -----
# Use a slim JRE (Java Runtime Environment) image. It's much smaller.
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the built .jar file from the 'builder' stage
# The wildcard (*) accounts for your project's version number in the filename
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port 8080 (which you specified)
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]