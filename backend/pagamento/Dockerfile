FROM maven:3.9.11-openjdk-17 AS build
COPY src /app/src
COPY pom.xml /app
WORKDIR /app
RUN mvn clean install package

FROM openjdk:17-jre-alpine
COPY --from=build /app/target/pagamento-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
EXPOSE 8089
CMD ["java", "-jar", "app.jar"]