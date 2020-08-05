# eCommerce Application

A e-commerce web application using Java, Spring Boot and JWT.

Completed as part of the Udacity Java Web Developer Nano Degree.

## Getting Started

### Installation

1. Clone or download this repository.
2. Open IntelliJ IDEA.
3. In IDEA, select `File` -> `Open` and navigate to the `e-commerce-app` directory within this repository. Select that directory to open.
4. Navigate to `src/main/java/com.example.demo.`. 
5. Within that directory, click on ECommerceApplication.java and select `Run` -> `ECommerceApplication`. 
6. Open Postman.
7. Select the `Import` button.
8. Import the file found in this repository under `src/main/resource/eCommerce.postman_collection.json`
9. Expand the eCommerce folder in postman.
10. Use the saved requests to play around with the API (going from top to bottom gives the full flow)

## Testing

1.Right click on `src/main/java/com.example.demo.` and click `Run 'All Tests' with coverage`.

A window should open showing you the test executions.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - Framework providing dependency injection, web framework, data binding, resource management, transaction management, and more.
* [Google Guava](https://github.com/google/guava) - A set of core libraries used in this project for their collections utilities.
* [H2 Database Engine](https://www.h2database.com/html/main.html) - An in-memory database used in this project to run unit tests.
* [MySQL Connector/J](https://www.mysql.com/products/connector/) - JDBC Drivers to allow Java to connect to MySQL Server

## License

This project is licensed under the MIT License - see the [LICENSE.md]()
