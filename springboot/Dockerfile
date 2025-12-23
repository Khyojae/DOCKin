FROM amazoncorretto:21-alpine
ARG JAR_FILE=build/libs/DOCKin-0.0.1-SNAPSHOT-plain.jar
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]