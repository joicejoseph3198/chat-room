FROM eclipse-temurin:21-jdk-focal AS runtime
WORKDIR /app
# RUN mvn clean install -DskipTests after every change and rebuild the image
COPY ./target/chatroom-0.0.1-SNAPSHOT.jar /app/chatroom.jar
CMD ["java", "-jar", "chatroom.jar"]
