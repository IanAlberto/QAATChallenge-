# PetStoreProject

This project is the web test automation scripts for the pet store API

Programing language: Java
Framework: testNG
Library for Test API: rest assured
Testing Approach: BDD
IDE: Intellij 

## Dependencies

* Java
* Maven
* TestNG
* Rest assured
* json-path
* json
* allure-rest-assured


## Installation

To get started with the project,

Install java(JDK, JRE) and maven and allure reports, download and complete the installation and
restart the machine

Go to the pom.xml and in the terminal

```console
mvn compile
```

## Test and Deploy

To run all the tcs:

```console
mvn -P ExecuteTestSuite test
```

To execute the report:
```console
allure serve allure-results
```

# Note

There some negative test cases that I can't automate cause it's not returning the right response or status code.

First you have to the download and execute the pet store api (https://github.com/swagger-api/swagger-petstore)
Maybe in this project you will have to delete this line:
```console
<monitoredDirName>.</monitoredDirName>
```
                    

