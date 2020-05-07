#!/usr/bin/env bash

./mvnw clean package -Dmaven.test.skip=true

docker build -t yuko-service:0.0.1 .
docker push yuko-service:0.0.1
