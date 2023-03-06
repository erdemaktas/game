## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Swagger](https://swagger.io/)
* [Docker](https://www.docker.com/)


### Project Installation & Requirements

Requirement: Docker

```shell script
sh build_image.sh
docker-compose up
```

### API Documentation

http://localhost:8081/swagger-ui/


###Project Dependecies

*   Lombok
*   Swagger
*   Mongo
*   HATEOAS
*   Spring Actuator

###Used Design Pattern

Builder => from Lombok library

Chain of Responsibility => Game Rules are defined as a handler. 
Each handler decides either to process the request or to pass it to the next handler in the chain.

I used spring Order annotation for chanining the rules. 
I could have used custom order implementation but Order gave me the simplicity.








