# TweetIngestor

## What

Simple Sprint-Boot application that ingests a filtered (to PA region & EN language) twitter stream and outputs it to a RabbitMQ queue for later processing.

## Why
Why not?  I wanted to write a short project that would exercise the Spring-Boot framework and micro-service approach to problem solving

## Pre-requists

1. Java SDK 1.7+

2. Running RabbitMQ Server on local system

3. Twitter account with registered application

# Installation Instructions

```
 git clone git://github.com/singram/twitteranalyser.git
 cd twitteranalyser/TweetIngestor
 cp src/main/resources/application.properties.sample src/main/resources/application.properties
 vi src/main/resources/application.properties
 ./gradlew build
```

### Eclipse

To generate Eclipse metadata (.classpath and .project files), do the following:
```
 ./gradlew eclipse
```
Once complete, you may then import the projects into Eclipse as usual:

 File -> Import -> Existing projects into workspace

# Contributions

Inspiration from http://blog.pivotal.io/cloud-foundry-pivotal/features/how-we-found-out-the-patriots-beat-the-seahawks-on-twitter-too

Spring assistance from Bill Koch (https://github.com/billkoch)

# TODO
- Fix Twitter account validation

