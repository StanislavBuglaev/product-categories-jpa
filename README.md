#Test project "Categories of products"


This is sample web application.

###The app includes the following functionality:
·   view a list of categories of products;
·   view a list of products;
·   ability to add a new category and product;
·   the ability to edit and delete data for each lists;
·   created multi-module: model, repository, service API, service, service-rest, web-app, rest-app;
·   connected to the database using Spring JPA;
·   created profile for working with database H2 in memory;
·   created view of web-application using Thymleaf.
 
### Technologies used
- Spring (Spring MVC, Spring Data JPA, Spring Rest)
- Maven
- H2 database
- Hibernate



### Requirements

* JDK 11
* Apache Maven

### Build application:

mvn clean install

To start Rest server:
java -jar ./rest-app/target/rest-app-1.0-SNAPSHOT.jar

### Available REST endpoints 

###  categories

#### findAll

curl --request GET 'http://localhost:8088/categories' | json_pp

#### create

curl --request POST 'http://localhost:8088/categories' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"categoryName": "Test category"
}'

#### delete

curl --request DELETE 'http://localhost:8088/categories/1'

###  products

#### findAll

curl --request GET 'http://localhost:8088/products' | json_pp

#### create

curl --request POST 'http://localhost:8088/products' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"productName": "Test product"
}'

### delete

curl --request DELETE 'http://localhost:8088/products/1'


