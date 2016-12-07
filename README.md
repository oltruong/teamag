# TEAMAG
* [Team Management Web Application] (https://github.com/oltruong/TeaMag)
* Team management web application using Java8 /JavaEE7 to learn while getting a useful tool as a manager :)

## Demo
[teamag.herokuapp.com](https://teamag.herokuapp.com)

(default login: admin, password: leave blank)



## Build info
[![Build Status](https://travis-ci.org/oltruong/teamag.svg?branch=master)](https://travis-ci.org/oltruong/teamag)
[![Coverage Status](https://coveralls.io/repos/github/oltruong/teamag/badge.svg?branch=master)](https://coveralls.io/github/oltruong/teamag?branch=master)

## Frameworks and librairies used

* [JavaEE7] (http://www.oracle.com/technetwork/java/javaee/overview/index.html)
* [Wildfly Swarm] (http://wildfly-swarm.io/): forget about having a server for deployment.
* [Guava] (https://code.google.com/p/guava-libraries/): Google core librairies
* [LogBack] (http://logback.qos.ch/): nice logger
* [AssertJ] (https://github.com/joel-costigliola/assertj-core): powerful assertions for testing


## Getting started

### Required software

* [Java8] (http://www.oracle.com/technetwork/java/index.html)
* [Maven] (http://maven.apache.org)

### Build

mvn clean install

### Run

java -jar target/teamag-swarm.jar

### Default configuration
* Default user login : admin. Default password is blank
Please set Database in persistence.xml file
