#!/bin/bash

#start or (restart) presto
presto-server-0.97/bin/launcher stop
#start our jerseyjetty server
kill -9 $(ps aux | grep java | grep multidbsqueryserverapi-0.1-SNAPSHOT.jar | awk '{print $2}')
