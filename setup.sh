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
#sudo mkdir project
cd project

#clone project
sudo git clone https://github.com/infsci2711/MultiDBs-Query-WebClient.git
sudo git clone https://github.com/infsci2711/MultiDBs-Utils.git

#presto backup folder (required)
sudo mkdir presto-bk

#go back
cd MultiDBs-Query-Server

#init db
mysql -u root -pproot < dbinit.sql
