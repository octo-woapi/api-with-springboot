# api-with-springboot
> Kata products implemented in Spring Boot

## Getting started
  
 - Install [Gradle](https://gradle.org/install/)   
 - Create a new directory and clone the project : ```git clone https://github.com/octo-woapi/api-with-springboot.git```
 - Build the project : ```$ gradle clean build```
 - Run the project : ```java -jar build/libs/api-with-springboot.jar```
 - Open a new tab in your browser and go to http://localhost:8080/products

## Features

Shopping API with products, orders and bills.

Products
  * Can be listed, created, found by id, deleted
  * Have an identifier, a name, a price and a weight
  * Products can be sorted by name, price or weight

Orders
  * Can be created, listed, found by id and deleted
  * Have a status, a product list, a shipment amount, a total amount and a weight
  * Orders status can be pending, paid or canceled
  * Are offered 5% discount when the price exceeds 1000€
  * Shipment costs 25€ for every 10 more kg (50€ for 20kg, 75€ for 30kg, etc.)

Bills
  * Can be listed
  * Have an amount and a creation date
  * Are automatically generated when an order status is set to paid
