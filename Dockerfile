# ==============================
# 1️⃣ Build Stage
# ==============================
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the Maven configuration first (for dependency caching)
COPY pom.xml .

# Copy your local custom JARs into the image (if present)
# Adjust these paths if your JARs are in a different folder
COPY libs/*.jar /app/libs/

# Install custom JARs into Maven local repo (if they exist)
RUN mvn install:install-file -Dfile=/app/libs/smartapi-java-2.2.5.jar \
    -DgroupId=com.angelbroking \
    -DartifactId=smartapi-java \
    -Dversion=2.2.5 \
    -Dpackaging=jar || echo "smartapi-java.jar not found, skipping"

RUN mvn install:install-file -Dfile=/app/libs/an-trading-1.3.6.jar \
    -DgroupId=com.angelbroking.smartapi \
    -DartifactId=an-trading \
    -Dversion=1.3.6 \
    -Dpackaging=jar || echo "an-trading.jar not found, skipping"

# Now copy the rest of the project files
COPY . .

# Build the Spring Boot app (skip tests for faster CI builds)
RUN mvn clean package -DskipTests

# ==============================
# 2️⃣ Runtime Stage
# ==============================
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
