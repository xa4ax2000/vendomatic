#!/bin/bash

mvn clean package
cd target
java -jar vendomatic-0.0.1-SNAPSHOT.jar