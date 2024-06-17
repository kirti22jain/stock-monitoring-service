# stock-monitoring-service
This service will be getting near-to-real time stock data to send updates to the clients.

https://stock-monitoring-service-test.abc.com/swagger-ui.html

Build Status, Docs and Pipelines
-----------

| Job       |                                                                                                                    Status                                                                                                                     |
|-----------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| Master    | [![Build Status](https://github.com/kirti22jain/stock-monitoring-service] |
| Spinnaker |                                    [![Spinnaker Pipeline](https://img.shields.io/badge/Spinnaker-Executions-037fac.svg?logo=spinnaker)]                                 |

Running the application locally
===============================

### Pre-requisites

1. Configure authentication to Artifactory for Gradle globally by creating a file `~/.gradle/gradle.properties`
2. JDK 21 is installed on the local machine.

### Build locally
```
./gradlew clean build
```
### Fix ktlint format issues
```
./gradlew ktlintFormat
```

### Running locally with intellij
Run or debug the app with the Application main class, at the root of your Kotlin package hierarchy. Add the following VM options in order for logging to work properly:
It is a Java-21 application, it is required to have the Temurin JDK 21 installed.
```
-Dapplication.home=.
-Dproject.name=stock-monitoring-service
-Dspring.profiles.active=dev
-Dapplication.environment=dev
-Dapplication.name=stock-monitoring-service
-Dreactor.netty.http.server.accessLogEnabled=false
-Djavax.net.ssl.trustStore=D:\mongoDBFiles\mongodbStore
-Djavax.net.ssl.trustStorePassword=password
-Djavax.security.auth.useSubjectCredsOnly=false$MAVEN_REPOSITORY$
```

Open http://localhost:8080/ to connect to the application.

### Run unit tests only

`./gradlew test`

### Docker
#### Build with docker
```docker login kumorelease-docker-virtual.artylab.expedia.biz
docker build -t stock-monitoring-service .
```
#### Run with docker
```
docker run -p 8080:8080 stock-monitoring-service
```
