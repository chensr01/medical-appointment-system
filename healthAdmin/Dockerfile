FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

# Set the working directory
COPY ./ /usr/src/app
WORKDIR /usr/src/app

# Build the project
RUN ./gradlew build

EXPOSE 8080

CMD ["java", "-jar", "build/libs/backend-0.0.1-SNAPSHOT.jar"]
