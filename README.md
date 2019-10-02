# Simple Spring Salary app

## Overview

This is a simple RESTful Java Spring Boot web application that supports the 2 following functionalities as described in the requirements document.

* A single REST GET API to retrieve a list of users with salary >= 0.00 and <= 4000.00.
* A command line argument when run to load a CSV file containing the list of users and salaries.

Spring 2.1.8 and Java 11 was used to develop the software. However, Java 8 is the minimum Java version required.

## Building and Installation

The Java Spring Boot application is checked in as an Eclipse Maven project to GitHub. The software was developed on Windows, but tested and runnable on MacOS as Java is platform independent.

```bash
# Instructions on MacOS or Linux; Windows vary very slightly.
# This compiles the source code, runs the unit tests, and then packages application into single jar.
git clone https://github.com/cxcheng/spring-salary.git
cd spring-salary
./mvnw package
```

## Configuration

The Spring Boot configuration parameters are in a file `./src/main/resources/application.properties`. Currently, it contains the following specification for embededded Apache Derby database.

```
spring.datasource.url=jdbc:derby:salary;create=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.DerbyTenSevenDialect
spring.jpa.hibernate.ddl-auto=update
```

Feel free to modify or add to it for typical application configurations:

* Replace Apache Derby with an external database such as Postgres, or MongoDB, as well as adding more database configuration parameters such as connection pool size etc.
* Change the `server.port` from default of 8080 to something else.
* Change the logging configuration - log levels by component, and log output. By default, we log at INFO level, and we log to standard out.

## Running

The following starts the application using existing default Apache Derby embedded database which would be empty for the first run.

```bash
java -jar target/salary-0.0.1-SNAPSHOT.jar 
```

The following starts the application by first loading the database with a `csv` file. We are using a pre-created sample `csv` file containing a list of US presidents. This would remove any existing data before importing. You may substitute if you have an alternative file.

```bash
java -jar target/salary-0.0.1-SNAPSHOT.jar --csv gencsv/salary.csv
```

If the application is running successfully, you would see logs being spilled out on standard out and the app would not exit. You can then test it by making an HTTP GET request to it on default port of 8080 with `curl`, `wget`, or Postman.

```
thunderhill:~ cxcheng$ curl http://localhost:8080/users
{"results":[{"name":"Abraham Lincoln","salary":3443.17},{"name":"Andrew Jackson","salary":2957.34},{"name":"Andrew Johnson","salary":2265.21},{"name":"Barack Obama","salary":3301.41},{"name":"Benjamin Harrison","salary":3771.91},{"name":"Bill Clinton","salary":2140.28},{"name":"Calvin Coolidge","salary":2758.94},{"name":"Franklin D. Roosevelt","salary":89.14},{"name":"Franklin Pierce","salary":2781.82},{"name":"George H. W. Bush","salary":1714.75},{"name":"George W. Bush","salary":31.89},{"name":"George Washington","salary":284.97},{"name":"Gerald Ford","salary":44.93},{"name":"Grover Cleveland","salary":1138.72},{"name":"Grover Cleveland (2nd term)","salary":462.1},{"name":"Harry S. Truman","salary":3055.72},{"name":"Herbert Hoover","salary":1568.45},{"name":"James Buchanan","salary":3879.57},{"name":"James Monroe","salary":3068.96},{"name":"Jimmy Carter","salary":2980.08},{"name":"John Adams","salary":2186.68},{"name":"John F. Kennedy","salary":1300.2},{"name":"John Quincy Adams","salary":3933.95},{"name":"John Tyler","salary":2264.29},{"name":"Lyndon B. Johnson","salary":3357.65},{"name":"Martin Van Buren","salary":456.59},{"name":"Millard Fillmore","salary":952.31},{"name":"Richard Nixon","salary":1666.9},{"name":"Ronald Reagan","salary":716.25},{"name":"Rutherford B. Hayes","salary":370.18},{"name":"Ulysses S. Grant","salary":1459.48},{"name":"William Henry Harrison","salary":1189.72},{"name":"Woodrow Wilson","salary":2505.43},{"name":"Zachary Taylor","salary":1983.45}]}
```

## Design and Implementation

### Assumptions

* Names can be duplicated. Name is not the primary key in the data. I have a separate numeric ID sequence.
* Loading of CSV data replaces *all* of the existing data instead of merging. Otherwise, we would have a problem with merging because of possible repeated names.
* Salary numbers are stored with all decimal places, although output rounds to 2 decimal places only.
* List of users returned sorted by name.

### Components

We use a standard Spring Boot application setup.

Component | Depends On | Purpose
--------- | ---------- | -------
`SalaryApplication.java` | `PersonController` | Main application class.
`Person.java` | | Main entity encapsulating a User. It is named Person instead of User to avoid conflict with database.
`PersonController.java` | `PersonService` | REST controller to respond to GET requests using `PersonService`.
`CommandLineLoader.java` | `PersonService.java` | Command line processor using `PersonService` to load CSV.
`PersonService.java` | `PersonRepository` | Implements the 2 application services to return list of users with valid salaries, as well as the load CSV function.
`PersonRepository` | JPA data source | Abstraction using JPA API to map data source to entity.

The files are laid out as follows.
* `.mvn` - `mvwn` dependencies.
* `mvnw` - Maven script to use on MacOS and Linux.
* `mvnw.cmd` - Maven script to use on Windows.
* `pom.xml` - Maven application descriptor.
* `src` - All source files including unit tests.
* `gencsv` - Sample CSV I used and Python script to convert it to the format required.
* `README.md` - This README.
* `LICENSE` - Apache License.

In addition, OpenCSV library is included for parsing CSV input. This is to ensure we don't have any trouble with more problematic CSV files that include `"` or `'` in the data.

After running, you will see the following:
* `derby.log` - Apache Derby log file.
* `salary` - Directory created by Apache Derby for its data.

### Testing

There are 5 unit tests written. Do the following to run them.

```
./mvnw test
```

The tests are as follows:

* Test that the components all load successfully on startup.
* Test that it returns HTTP Status 404 for *all* but `/users`.
* Test that `PersonService` correctly returns list of users.
* Test that `PersonService` correctly loads CSVs.
* Test that `PersonController` returns correctly formatted JSON output.

What I can do more:

* Test that command line CSV loading works.
* Test for badly formatted CSV files.
* Test with more salary data and also duplicated names (allowed).

## Notes

Application is read-only except when loading users. Users are only loaded when starting up. No problem with concurrent access.

JPQL to filter list of users by salary from the database. This may need to be refined as I'm unconvertible with spliting the logic between repository and service.

I was rusty with Spring Boot so it took me awhile to get used to it, particularly on the `Mockito` unit tests.

Did not really document the code in depth as I personally don't like too many comments in the code, preferring to go with standardized coding and contracts between callers and callees. The nice thing about Spring is that it lays down a structure and convention on how to divide up components and how they need to interact with each other, as well as a convention for dependency injection. However, I agree that I can try to document the service call API params and outputs.


