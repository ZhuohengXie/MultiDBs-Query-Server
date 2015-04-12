#!/bin/bash

#start or (restart) presto
presto-server-0.97/bin/launcher restart
#start our jerseyjetty server
nohup java -jar MultiDBsQueryServerAPI/target/multidbsqueryserverapi-0.1-SNAPSHOT.jar /opt/project/MultiDBs-Query-Server/MultiDBsQueryServerAPI/src/main/resources/config.properties> log.out 2> error.log < /dev/null &
