# opa-demoapi-spring

In this case the API implements AccessDecisionVoter for Spring Security that uses OPA for making authorization decisions. 

The OPA agent based on a rego policy will grant/deny access to the requested endpoint, given the HTTP method, requested path and JWT.

## Run the API

### Run locally

* Clone this repository
```
git clone https://github.com/embesozzi/opa-demoapi-spring.git
```
- Adjust the [aplication.yml](/src/main/resources/application.yml)
- Run the app
```
mvn spring-boot:run
```
- You can access to the API on http://hostname:8081/api/v1

### Run as docker componse

```sh
docker-compose up
```
