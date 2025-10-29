# ==============================
# 1️⃣ Build Stage
# ==============================
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the Maven configuration first (for caching dependencies)
COPY pom.xml .

# Copy your local custom JARs into the image (if they exist in your repo)
# Adjust these paths to wherever your jars actually are
COPY libs/*.jar /app/libs/

# Install custom JARs into Maven local repo
RUN mvn install:install-file -Dfile=/app/libs/smartapi-java-2.2.5.jar \
    -DgroupId=com.angelbroking \
    -DartifactId=smartapi-java \
    -Dversion=2.2.5 \
    -Dpackaging=jar || echo "smartapi-java.jar not found, skipping"

RUN mvn install:install-file -Dfile=/app/libs/an-trading-1.3.3.jar \
    -DgroupId=com.angelbroking.smartapi \
    -DartifactId=an-trading \
    -Dversion=1.3.3 \
    -Dpackaging=jar || echo "an-trading.jar not found, skipping"

# Now copy the full source code
COPY . .

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# ==============================
# 2️⃣ Runtime Stage
# ==============================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy only the final JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port (Spring Boot default)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
