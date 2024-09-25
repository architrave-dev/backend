FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY build/libs/portfolio-0.0.2-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "portfolio-0.0.2-SNAPSHOT.jar"]