FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/grupo-nordan-backend.jar grupo-nordan-backend.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar","grupo-nordan-backend.jar"]