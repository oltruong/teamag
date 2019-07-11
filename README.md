# TEAMAG
[Team Management Web Application] (https://github.com/oltruong/teamag)

The purpose of this app was twofold:
* get a useful tool as a manager to make paperwork faster
* experiment JavaEE7, AngularJS

## Build info
[![Build Status](https://travis-ci.org/oltruong/teamag.svg?branch=master)](https://travis-ci.org/oltruong/teamag)
[![Coverage Status](https://codecov.io/github/oltruong/teamag/coverage.svg?branch=master)](https://codecov.io/github/oltruong/teamag/)

## Demo
[teamag.herokuapp.com](https://teamag.herokuapp.com)

(default login: admin, password: leave blank)
Data is reset everyday.

## Frameworks used

* [JavaEE7] (http://www.oracle.com/technetwork/java/javaee/overview/index.html)
* [AngularJS] (https://angularjs.org/)
* [Wildfly Swarm] (http://wildfly-swarm.io/): forget about having a server for deployment.


## Getting started

### Required software

* [Java8] (http://www.oracle.com/technetwork/java/index.html)
* [Maven] (http://maven.apache.org)

### Build
```bash
mvn clean install
```

### Run

java -jar target/teamag-swarm.jar

### Default configuration
* Default user login : admin. Default password is blank
Please set Database in persistence.xml file
