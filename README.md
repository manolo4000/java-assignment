# TechLean JavaAssignment

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project-">About the Project</a>
    </li>
    <li>
      <a href="#getting-started-">Getting Started</a>
    </li>
     <ul>
            <li><a href="#prerequisites-">Prerequisites</a></li>
        </ul>
    <li><a href="#enviroment-configuration-%EF%B8%8F">Enviroment configuration</a></li>
        <ul>
            <li><a href="#docker-compose-configuration-">Docker compose configuration</a></li>
            <li><a href="#application-configuration-">Application configuration</a></li>
            <li><a href="#profile-configuration-">Profile configuration</a></li>
            <ul>
                <li><a href="#kafka---">Kafka</a></li>
                <li><a href="#database-connection-">Database connection</a></li>
                <li><a href="#jwt-secret-">JWT</a></li>
            </ul>
            <li>
            <a href="#email-configuration-">Email configuration</a></li>
            <li>
            <a href="#email-properties-configuration-">Email properties configuration</a></li>
        </ul>
    <li><a href="#dependencies-">Dependencies</a></li>
    <li><a href="#how-to-run--%EF%B8%8F">How to run</a></li>
    <li><a href="#run-unittesting-">Run UnitTesting</a></li>
    <li><a href="#api-testing-%EF%B8%8F">API testing</a></li>
    <ul>
                <li><a href="#swagger-ui-">Swagger Ui</a></li>
                <ul>
                    <li><a href="#add-booking">Add booking</a></li>
                    <li><a href="#consult-booking">Consult booking</a></li>
                </ul>
            </ul>
    <li><a href="#author-%EF%B8%8F">Author</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About the project 🔍

"You have been hired as a backend developer for a major tourism company, currently the registration of reservations is done through email or phone calls with advisors, which has caused inconvenience to some clients because some requests take a long time for confirmation.


Technology area required to implement a system that allows the recording of bookings using a message broker (Kafka, Rabbit MQ, etc) which will reduce the current waiting time. A good handling of possible errors must be taken into account, whether due to a message with invalid information or possible problems in the network."

## Getting started 🚀

_These instructions allow you to get a copy of the running project on your local machine for development and testing purposes._

### Prerequisites 📋

_you need to install these on your system to be able to run the project_

* [Java JDK 8](https://www.oracle.com/co/java/technologies/javase/javase-jdk8-downloads.html) - Java SE Development Kit 8

* [Java JRE 8](https://www.oracle.com/co/java/technologies/javase-jre8-downloads.html) - Java SE Runtime Environment 8

* [Maven](https://maven.apache.org/) - Dependency manager

* [Spring Tools Suite 4 for Eclipse](https://spring.io/tools) - (Optional) Code editor

* [Lombok](https://projectlombok.org/setup/eclipse) - Java library that automates your logging variables, and much more.

* [Docker Desktop](https://www.docker.com/products/docker-desktop) - an open platform for developing, shipping, and running applications.

* [Docker Compose](https://docs.docker.com/compose/install/) - _(Install if your OS doesn't support Docker Desktop)_

* [Postman](https://www.postman.com/downloads/) - (Optional) API Platform

* [Git](https://git-scm.com/downloads) - Distributed version control system

## Enviroment configuration ⚙️

### Docker compose configuration 🐳

/**docker-compose.yml** 

Links and creates the configuration between the containers ("zookeeper" and "kafka") and your local machine.

This define and create a new topic in Kafka on startup, named "bookings" with **1** partition and **1** replica.
    
``` yaml
KAFKA_CREATE_TOPICS: "bookings:1:1"
```

### Application configuration 🌱

/src/main/resources/**application.yml** 

Currently is only set to run on port 9000 and run the configuration of the "stg" profile .

``` yaml
server:
  port: 9000
  
spring:
  
  profiles:
    active:
    - stg   
```

### Profile configuration 👤
/src/main/resources/**application-stg.yml**

#### **Kafka** 🗣 💬 👂🏽

This part configures the default values tha Kafka will use with the producer and connect to with the consumer. the bootstrap-servers are comming from the docker machine. 
It is important to set well the values for key & value serializer as well as the deserializer.
Currently they are set to key as a String, and Value as a Json.

This is because a complete object is being sent to the Topic of "bookings", and this Json contains all the necessary information to make a booking, so it needs to be sent and received correctly in Json format and thus no errors will occur.

``` yaml
spring:
  kafka:
    
    consumer:
      key-deserializer: ...StringDeserializer
      value-deserializer: ...JsonDeserializer
    producer:
      key-serializer: ...StringSerializer
      value-serializer: ....JsonSerializer
      
```
#### **Database connection** 🐬 

_All the default values should work because they are connected to online servers_

This project has a connection configuration to a MySQL database

If you want to configure a new connection you can use the following template and replace the values respectively

_Keep in mind that the password of the "Users" table must be encrypted with the [Bcrypt](https://bcrypt-generator.com/) algorithm so that the authentication can work correctly, in case you need to add manually a user_


``` yaml
  datasource:
     driver-class-name: com.mysql.cj.jdbc.Driver
     username: <USERNAME>
     password: <PASSWORD>
     url: jdbc:mysql://<HOST>:<PORT>/<DB-NAME>?serverTimezone=UTC
  
  jpa:
    hibernate:
      ddl-auto: update
```
#### **JWT secret** 🔐

Uses a secret key for JWT to work

``` yaml
  jwt:
    secret: <SECRET>
```

### Email configuration 📤
/src/main/resources/mail/**emailconfig.properties**

_All the default values should work because they are connected to online  SMTP servers_

Here you can configure the values corresponding to the JavaMailSender, so then java can connect for example to an SMTP server and send emails with the authenticated user.

```properties
mail.server.host=<DOMAIN>
mail.server.protocol=<PROTOCOL>
mail.server.port=<PORT>
mail.server.username=<USERNAME>@<DOMAIN>
mail.server.password=<PASSWORD>

```
### Email properties configuration 📨
/src/main/resources/mail/**javamail.properties**

Those are the properties added to the JavaMailSender so it can work properly

_I suggest you leave the default values_

```properties
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.smtp.quitwait=false
mail.store.protocol=imaps

```

## Dependencies 📦

_the file **/pom.xml** has all the configurations of dependencies and plugins._

Example: 

```xml
    <dependencies>
		<dependency>
			<groupId>org.springframework.kafka</groupId>
			<artifactId>spring-kafka</artifactId>
		</dependency>
        <dependency> 
	      <groupId>mysql</groupId>  
	      <artifactId>mysql-connector-java</artifactId>  
	      <scope>runtime</scope> 
	    </dependency>  
        <dependency>
		    <groupId>org.springframework.security</groupId>
		    <artifactId>spring-security-test</artifactId>
		    <version>5.5.2</version>
		    <scope>test</scope>
		</dependency>
    </dependencies>
```
## How to run  ▶️

_This application is packaged as a war which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary._


**1. Clone the application**

```bash
git clone https://github.com/manolo4000/java-assignment.git
```

**2 Open the terminal and navigate to the directory where the repository was cloned**

Example:

```bash
cd Desktop/Spring/kafka/ 
```

**3 Run Docker Compose and wait until all containers are running**

```bash
docker compose up
```

**4. Run the project**

_There are two main ways to run the project, at the end both do the same, but you can choose the way you like._


* **4.1 The simple way - Terminal**

    Run the project via terminal in the project main folder:

    ```bash
    mvn spring-boot:run   
    ```

* **4.2 The visual way - Spring Tools Suite 4**

    **4.2.1 Import the project once**

    Open Spring Tools Suite 4
    
    click on **File > Import** 
    
    then select **Maven > Existing Maven Projects** and click "Next"
    
    in the "Root Directory" browse into the project folder, open it an the click on "Finish"

    **4.2.2 Run the project**

    Right click the main project folder and select **Run As > Spring Boot App**

    <img width="531" alt="run-spring" src="https://user-images.githubusercontent.com/43767110/130311019-fb863e27-7ca0-4353-b09d-9b83e084b5d3.png">


## Run UnitTesting 🔧

_These will check if all the functionalities are working correctly_

* **The simple way - Terminal**

    Run the project via terminal in the project main folder:

    ```bash
    mvn test  
    ```

* **The visual way - Spring Tools Suite 4**

    Right click the main project folder and select **Run As > JUnit Test**

    Click on the tab **JUnit** and there you can see all the test files and results

    <img width="513" alt="Run-UnitTesting" src="https://user-images.githubusercontent.com/43767110/130311723-2511ecef-869d-41ea-a962-862ce4a81173.png">


## API testing 🛠️

### **Swagger Ui** 🤖

_Using the Swagger we are able to autogenerate all the documentation for the API and testing the endpoints as well_

Please make sure you are running the project, then go to Swagger Ui

[Swagger Ui](http://localhost:9000/swagger-ui/index.html#/) - http://localhost:9000/swagger-ui/index.html#/, the PORT can change depending on your configuration

You will find this
<img width="1444" alt="Swagger-Ui" src="https://user-images.githubusercontent.com/43767110/130321937-2a7d235e-7931-4b97-bd56-63e057fbcc4c.png">

#### **Swagger Authentication** 

First of all, you need to authenticate, so click on /auth and then on "Try it out".

Finally change the username and password values with your user credentials and click on "excecute".

<img width="1453" alt="Auth" src="https://user-images.githubusercontent.com/43767110/130323292-c5095f48-89c2-4630-bc93-a056dc59bc44.png">

If your user credentials are correct, the API will return a Token like this:

```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW51ZWxwYXRzYW4yQGhvdG1haWwuY29tIiwiZXhwIjoxNjI5NTcwODczLCJpYXQiOjE2Mjk1NTI4NzN9.xa198GtaUgS84aoDR-ChGvlJ2gqvLF-K90nGvGb0WfJZOxYrXHwF08KLxyxyGJ5F3OmeYa0HYwW7YuWbbdzNkg"
} 
```

You need to copy the token value, then at the upper right part of the screen click on "Authorize".

This will open a modal where you need to paste the token value as shown.
Bearer \<TOKEN>

And finally click on Authorize.

<img width="658" alt="Token-auth" src="https://user-images.githubusercontent.com/43767110/130323718-6f47f61a-332e-451a-9026-e61241d6c309.png">

After that every request you make will be authenticated.

#### **Add booking**

Click on **bookings-controller > /registrar-reserva** and then on "Try it out"

Eventhough there is a base model example for the request, these are the only body request values accepted.

Example
```json
{
    "fechaIngreso": "2021-08-21",
    "fechaSalida": "2021-09-20",
    "numeroPersonas": 12,
    "numeroMenores": 3,
    "numeroHabitaciones": 4
}
```
If you add to the body other values of the model, then it will return a String list with all the validation errors.

Bad request example
```json
{
    "id": 200,
    "fechaIngreso": "2021-08-25",
    "fechaSalida": "2021-08-23",
    "numeroPersonas": 0,
    "numeroMenores": -3,
    "numeroHabitaciones": -4
}
```

Then it will return:
```json
[
    "No puede asignar un Id a la reserva",
    "Como mínimo debe asistir un adulto",
    "No puede poner números negativos en el campo numeroMenores",
    "Como mínimo debe seleccionar una habitación",
    "No puede poner números negativos en el campo numeroHabitaciones",
    "La fecha de ingreso debe ser antes que la fecha salida"
]
```

And Finally you will get an email where it says the all the reasons why the booking failed.

<img width="1339" alt="Booking-errors" src="https://user-images.githubusercontent.com/43767110/130324981-b4182ecd-bae0-4073-8923-bfd115e0ed07.png">

Otherwise, if you follow these instructions correctly and make a request with the accepted values.

Good request example:
```json 
{
    "fechaIngreso": "2021-08-21",
    "fechaSalida": "2021-09-21",
    "numeroPersonas": 12,
    "numeroMenores": 3,
    "numeroHabitaciones": 4
}
```
It will return
```json 
[
    "Registro satisfactorio"
]
```
and then you will get a confirmation email.

<img width="1343" alt="Booking-confirmation" src="https://user-images.githubusercontent.com/43767110/130325537-ae62fbb8-3225-4275-8cc7-f7ff4a005606.png">

#### **Consult booking**

Click on **bookings-controller > /consultar-reserva/{id}**    and then on "Try it out"

The only thing you need to do is place the Booking Id in the field. Only if that booking corresponds to the authenticated user, it will return all the booking data.

<img width="1434" alt="Booking-data" src="https://user-images.githubusercontent.com/43767110/130326038-2b33d1c4-f728-43b0-8a77-1a0a78357098.png">


## Author ✒️

* **Manuel Eduardo Patarroyo Santos** - [manolo4000](https://github.com/manolo4000)
