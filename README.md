# DietiDeals24-Backend

This repository hosts the backend for an immaginary e-commerce webapp. The project is composed by
a relational database managed with postgresql, an in-memory-cache managed with redis and a monolith-server, 
written in java-21 and that uses the spring-boot backend-framework to provide the client with a REST-API.

### Setup
To run the server the host machine must provide the following enviroment variables:
- DIETIDEALS24_AWS_S3_ACCESS_KEY
- DIETIDEALS24_AWS_S3_SECRET_KEY
- DIETIDEALS24_GEO_API_KEY
- DIETIDEALS24_GOOGLE_CLIENT_ID
- DIETIDEALS24_GOOGLE_CLIENT_SECRET
- DIETIDEALS24_GOOGLE_SMTP_PASSWORD


### Build from source
To build and run a local instance of the server, provided that `docker-compose` is installed on your machine, 
and that enviroment variables are correctly set, all you have to do is running the following command
```bash
$ docker-compose up
```

### Testing
To run the tests, provided that `docker-compose` is installed on your machine, 
and that enviroment variables are correctly set, all you have to do is running the following command
```bash
$ docker-compose -f docker-compose-server-unit-tests up
```