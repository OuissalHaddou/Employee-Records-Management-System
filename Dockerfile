FROM openjdk:17-jdk-slim

WORKDIR /app

COPY dist\Employee_Records_Management_System.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
