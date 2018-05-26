# Registration Service (Web Application)
Ref: https://www.javadevjournal.com/spring/rest-web-services-bascis/

## Requirements

* Java 8 or Above
* Maven
* GIT

## Installation
```
  > git clone https://github.com/ripsamm004/coding-exercise.git
  > cd registration-service
  > mvn clean install

```

## Run the server
```
 > java -jar target/registration-service-1.0-SNAPSHOT.jar
```

## REST service running on port [8081]

```
 > http://localhost:8081

```

## Service details

```

# Register service endpoint : /register


* Register a user

# Sample request

URL : http://localhost:8081/register
Request-Type: POST
Content-Type: application/json
Request Body :
{
"username":"ripsamm004",
"password":"443908rR",
"dob":"1986-05-20",
"ssn":"ssn101"
}


# Sample response

Content-Type : application/json
Response code : 201
Response Body :
{
"username":"ripsamm004",
"password":"443908rR",
"dob":"1986-05-20",
"ssn":"ssn101"
}

# Sample Error response

Content-Type : application/json
Response code : 400

Response Body 01 :
{
    "code": "A0007",
    "message": "USER ALREADY EXIST"
}


Response Body 02:
{
    "code": "A0003",
    "message": "USER NAME NOT CORRECT"
}


Response Body 03:
{
    "code": "A0005",
    "message": "DATE FORMAT NOT CORRECT"
}

Response Body 04:
{
    "code": "A0004",
    "message": "USER PASSWORD NOT CORRECT"
}

Response Body 05:

{
    "code": "A0002",
    "message": "USER BLACK LISTED"
}

Response Body 06:

{
    "code": "A0008",
    "message": "USER DATA INVALID"
}

Response Body 07:

{
    "code": "A0009",
    "message": "REQUEST BODY INVALID"
}

* Get a register user by Social Security Number (SSN)

> http://localhost:8081/register/{ssn}

# Sample request

Request-Method : GET
URL : http://localhost:8081/register/ssn101

# Sample response

Content-Type : application/json
Response code : 200
Response Body:
{
"username":"ripsamm004",
"password":"443908rR",
"dob":"1986-05-20",
"ssn":"ssn101"
}


# Sample Error response

Content-Type : application/json
Response code : 404

Response Body 01 :
{
    "code": "A0001",
    "message": "USER NOT FOUND"
}


* Update a register user by Social Security Number (SSN) and Update Data

> http://localhost:8081/register/{ssn}

# Sample request

Request-Method : PUT
URL : http://localhost:8081/register/ssn101
Content-Type: application/json
Request Body :
{
"username":"ripsamm004",
"password":"443908rRUpdate",
"dob":"1986-05-20",
"ssn":"ssn101"
}

# Sample response

Content-Type : application/json
Response code : 200
Response Body:
{
"username": "ripsamm004",
"password":"443908rRUpdate",
"dob":"1986-05-20",
"ssn":"ssn101"
}


* Remove a register user by Social Security Number (SSN)

> http://localhost:8081/register/{ssn}

# Sample request

Request-Method : DELETE
URL : http://localhost:8081/register/ssn101

# Sample response

Response code : 200

```

