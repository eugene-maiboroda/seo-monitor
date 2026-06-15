# Stage 1: Build frontend
FROM node:22-alpine AS frontend
WORKDIR /build/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build
# Output: /build/src/main/resources/static/ (configured via vite.config.js outDir)

# Stage 2: Build backend (with frontend already built into resources/static)
FROM gradle:8.14.3-jdk21 AS build
WORKDIR /app
COPY . .
COPY --from=frontend /build/src/main/resources/static ./src/main/resources/static
RUN gradle bootJar --no-daemon

# Stage 3: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
