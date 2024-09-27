FROM maven:3.9-amazoncorretto-21-debian AS build

WORKDIR /server

COPY ./server /server

RUN mvn package

######################

FROM amazoncorretto:21

WORKDIR /app

COPY --from=build /server/target /app

EXPOSE 80

CMD ["java", "-jar", "DietiDeals24-backend-0.0.1-SNAPSHOT.jar"]
