# Microservices Architecture: Order, Client, and Stock

This repository contains three microservices built with **Spring Boot**, communicating via **OpenFeign**. These microservices have been deployed on an AWS EC2 instance for hosting and scaling purposes. The managed services are:

- **OrderMS**: Manages orders.
- **ClientMS**: Manages client information.
- **StockMS**: Manages product stock.

Each microservice has detailed endpoints to handle different functionalities.

---

## Technologies Used

### Backend
- **Spring Boot** :leaves:
- **OpenFeign** for communication between microservices :mailbox_with_mail:
- **Spring HATEOAS** for efficient endpoint navigation :incoming_envelope:
- **Slf4j** for logging :bookmark_tabs:

### Databases
- **PostgreSQL** (production) :card_index:
- **H2 Database** (tests) :wrench:

### Containers
- **Docker Compose** for container management and deployment :whale:

### Testing
- **JUnit 5** for unit and integration testing :bug:

### CI/CD
- **GitHub Actions** for continuous integration, including building and running tests. :octocat:

---

## Architecture

The microservices are independent, each featuring:
- Interactive documentation with **Swagger**.
- Dedicated database managed via Docker containers.
- Modular structure for scalability and maintainability.

### Communication Flow
Microservices communicate via HTTP calls using **OpenFeign**, enabling each service to consume the other's endpoints.

### Access the Swagger documentation:
- OrderMS: http://localhost:8081/swagger-ui/index.html
- ClientMS: http://localhost:8080/swagger-ui/index.html
- StockMS: http://localhost:8082/swagger-ui/index.html

---

## Endpoints

### **StockMS**
Manages product stock.

| Method | Endpoint                          | Description                       |
|--------|-----------------------------------|-----------------------------------|
| GET    | `/api/v1/stock/products`          | Retrieves all products (paginated). |
| PUT    | `/api/v1/stock/products`          | Updates product quantities.       |
| POST   | `/api/v1/stock/products`          | Creates a new product.            |
| PATCH  | `/api/v1/stock/products/quantity` | Updates a product's quantity.     |
| PATCH  | `/api/v1/stock/products/name`     | Updates a product's name.         |
| GET    | `/api/v1/stock/products/{id}`     | Retrieves a product by ID.        |
| DELETE | `/api/v1/stock/products/{id}`     | Deletes a product by ID.          |

### **ClientMS**
Manages client information.

| Method | Endpoint                         | Description                       |
|--------|----------------------------------|-----------------------------------|
| POST   | `/api/v1/clients`               | Creates a new client.            |
| PATCH  | `/api/v1/clients/name`          | Updates a client's name.         |
| PATCH  | `/api/v1/clients/email`         | Updates a client's email.        |
| GET    | `/api/v1/clients/orders/{email}`| Retrieves a client's orders by email. |
| GET    | `/api/v1/clients/id/{id}`       | Retrieves a client by ID.        |
| GET    | `/api/v1/clients/email/{email}` | Retrieves a client by email.     |
| DELETE | `/api/v1/clients/{id}`          | Deletes a client by ID.          |

### **OrderMS**
Manages orders.

| Method | Endpoint                     | Description                       |
|--------|------------------------------|-----------------------------------|
| PUT    | `/api/v1/orders`             | Updates an order's products.     |
| POST   | `/api/v1/orders`             | Creates a new order.             |
| PUT    | `/api/v1/orders/email`       | Updates an order's email.        |
| GET    | `/api/v1/orders/id/{id}`     | Retrieves an order by ID.        |
| GET    | `/api/v1/orders/email/{email}` | Retrieves a client's orders by email. |
| DELETE | `/api/v1/orders/{id}`        | Deletes an order by ID.          |

---

## Setup and Execution

### Prerequisites
1. **Docker** and **Docker Compose** installed.
2. **Java 21** and **Maven** configured on your system.

### Running the Microservices on docker
1. Clone the repository:
   ```bash
   git clone git@github.com:victor-pimentell/victor-pimentell-SP_SpringBoot_AWS_Desafio_03_Order_Stock_Client.git
    ```
2. Enter the repository folder:
   ```bash
   cd victor-pimentell/victor-pimentell-SP_SpringBoot_AWS_Desafio_03_Order_Stock_Client
   ```
3. Run the docker-compose file:
   ```bash
   docker-compose up -d
    ```

   