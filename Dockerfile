FROM openjdk:17
COPY ./build/libs/Server-0.0.1-SNAPSHOT.jar /boot.jar
WORKDIR /
CMD ["java", "-jar ","/boot.jar"]
