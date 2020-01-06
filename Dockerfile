FROM openjdk:8
VOLUME /tmp
ADD ./target/servicio-Account-service-0.0.1-SNAPSHOT.jar servicio-account.jar
ENTRYPOINT ["java","-jar","/servicio-account.jar"]