# USocieTy: Student Society Event and Resource Management Platform (Backend)

This is a backend web server using Spring Boot to support the web and cross-platform mobile application that centralizes
the promotion and registration of all student events at Hong Kong University of Science and Technology.

## Features

- Connected with HKUST ITSC Central Authentication Server (CAS)
- Role Based Access Control for society administrator
- CRUD operation of event, society member, financial record
- AWS S3 integration
- Auto JUnit testing on every pull request (PR) to master branch
- Auto deployment to heroku cloud server when merged PR to master

## Requirements

- Java version >= 11
- Kotlin version >= 1.6.21
- IntelliJ IDEA / Eclipse
- AWS account
- HKUST ITSC CAS application
- PostgreSQL server
- Heroku account

## Quickstart

1. Request HKUST CAS connection from HKUST ITSC
2. Set up AWS Account
3. Set up PostgreSQL in cloud server or localhost
4. Install either IntelliJ IDEA or Eclipse IDE
5. Install Java and Kotlin with correct version
6. Replace all ${} variable with suitable value in application.yaml
7. Compile and run the kotlin application in your IDE