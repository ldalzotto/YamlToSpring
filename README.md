# YamlToSpring


This project aims to transform Swagger YAML file to SpringMVC ready to use project.

Through a user friendly node interface, the user will be able to transform a simple Swagger YAML file to SpringMVC ready to use project.
This will include :
* Creation of appropriate Controller
* Integration of swagger file
* Creation of feign client

This project is built upon JavaFX API.
This project is built upon Maven.

This project is under development.

![alt tag](https://raw.githubusercontent.com/ldalzotto/YamlToSpring/master/example.png)

#How to try the app ?
To try the app, you simply have to build it via Maven : mvn package.
Then, move to the folder target/jfx/native/node-yaml-to-spring/ and run the .exe file.

#Things TO DO :
* All Spring code generation
* Work on design -> getting a better expercience on workspace

#Refactoring TO DO :
* Make UINodePoints & AbstractUINodePoints more readable and understandable

#Things done :
* Load of Swagger YAML file
* Drag & Drop ressources name to main workspace
* Link between AbstractNodes
* Creations of Spring controller nodes
* Manage input of nodes
* Create a data to list node -> add execution to workflow
* Added add input button for list node
* During workflow, make output transit through nodes
