FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source & test config
COPY src ./src
COPY testng.xml .

# Pre-compile tests (FAIL FAST)
RUN mvn -q -DskipTests compile test-compile

# Default command (overridden by K8s args if needed)
CMD ["mvn", "test"]
