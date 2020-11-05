FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ecommerce.jar
ENTRYPOINT ["java","-jar","/ecommerce.jar"]