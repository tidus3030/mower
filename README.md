# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.1/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides

This application allows you to move mowers on a map following instructions.

All the necessary information is provided in the form of a file of this format:<br/>
5 5 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>(coordinates of the upper right corner of the plan)</i><br/>
1 2 N &nbsp;<i>(initial position and orientation of a mower)</i><br/>
GAD &nbsp;&nbsp;<i>(movement to be performed by the mower)</i>

List of allowed positions:

- S(south)
- E(east)
- N(north)
- W(west)

List of allowed moves:

- G (90° rotation to the left)
- D (90° rotation to the right)
- A (move one position forward)

You can launch the springboot application by starting the jar or by launching MowerApplication.java You can test the application
using [swagger](http://localhost:8080/swagger-ui/), some test files are present in directory src/main/resources/files

Application is also launched on [hiroku](https://tidus3030-mower.herokuapp.com/swagger-ui/)