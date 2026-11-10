
# ==========================================
# Stage 1: Build application using Maven
# ==========================================
FROM maven:3.9.11 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Copy application source code
COPY src ./src

# Build the Spring Boot application
RUN mvn -B -ntp clean package -DskipTests

# Rename generated JAR to a consistent name
RUN JAR_FILE=$(find target -maxdepth 1 -type f -name '*.jar' \
    ! -name '*.original' | head -n 1) && \
    test -n "$JAR_FILE" && \
    cp "$JAR_FILE" /app/shipping.jar


# ==========================================
# Stage 2: Runtime image
# ==========================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create non-root user
RUN groupadd --system roboshop && \
    useradd --system --gid roboshop roboshop

# Application metadata
LABEL project="roboshop" \
      author="krishna" \
      component="shipping"

# Copy application JAR from build stage
COPY --from=build --chown=roboshop:roboshop \
    /app/shipping.jar /app/shipping.jar
# ENV CART_ENDPOINT=cart:8080 \
#     DB_HOST=mysql
# Run as non-root user
USER roboshop

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/shipping.jar"]
