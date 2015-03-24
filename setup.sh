#!/bin/bash

#install oracle java 8
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
sudo apt-get install oracle-java8-set-default

#install others
sudo apt-get install maven
sudo apt-get install mysql-server
sudo apt-get install nginx

#folders and environments
cd /opt
sudo mkdir project
cd project

#clone project
sudo git clone https://github.com/infsci2711/MultiDBs-Query-Server.git
sudo git clone https://github.com/infsci2711/MultiDBs-Utils.git

sudo mkdir presto-bk

mysql -u root -pproot < dbinit.sql
