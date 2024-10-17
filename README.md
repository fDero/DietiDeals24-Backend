# DietiDeals24-Backend

This repository hosts the backend for an immaginary e-commerce webapp. The project is composed by
a relational database managed with postgresql, an in-memory-cache managed with redis and a monolith-server, 
written in java-21 and that uses the spring-boot backend-framework to provide the client with a REST-API.

### Build from source
To build and run a local instance of the server, provided that `docker-compose` is installed on your machine, 
all you have to do is running the following command
```bash
$ docker-compose up
```

### Testing
To run the tests, provided that `docker-compose` is installed on your machine, 
all you have to do is running the following command
```bash
$ docker-compose -f docker-compose-server-unit-tests up
```