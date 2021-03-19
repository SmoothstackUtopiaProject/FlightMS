# FlightMS
Flight Microservice for Utopia Airlines
## Requirements & Quick Start
##### -Maven
##### -MySQL
`$ mvn spring-boot:run` - run FlightMS as a spring boot application. The application will run by default on port `8085`.

Configure the port by changing the `server.port` value in the `application.properties` file which is located in the resources folder.

The application can be run with a local MySQL database. Configure the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` in the `application.properties` file according to your needs.
## API
`/flights` - GET : Get a list of all the flights from the DB.

`/flights/{id}` - GET : Get a flight by id.

`/flights` - POST : Create a flight by providing a correct request body

`/flights` - PUT : Update a flight by providing a correct request body including the id

`/flights/{id}` - DELETE : Delete a flight by id.
