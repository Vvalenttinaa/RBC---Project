# RBC---Project

# Project Setup and Run Instructions

## Prerequisites

Ensure you have the following installed on your machine:

- **Java Development Kit (JDK) 17 or higher**
- **Node.js** and **npm** (for Angular)
- **MySQL** (for the database)
- **Maven** (if using it to build the Spring Boot project)
- **IDEA IntelliJ** or another IDE for Spring and Angular development

## Step 1: Run the MySQL Script

Before running the application, you need to set up the database:

1. **Start MySQL Server**: Ensure that your MySQL server is running.
2. **Create the Database**: Run the provided SQL script (`your_script.sql`) to create the necessary schema and tables.

   ```bash
   mysql -u your_username -p your_database_name < path/to/my_budget.sql
3. **Configure the Application Properties**: Make sure your Spring Boot application (application.properties or application.yml) is configured to connect to your MySQL database.

    ```bash
    spring.datasource.url=jdbc:mysql://localhost:3306/my_budget
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
## Step 2: Set Up and Run the Spring Boot Application
1. **Open the Spring Boot Project**: In IntelliJ IDEA or your preferred IDE, open the Spring Boot project.
2. **Build the Project**: If you are using Maven, you can build the project using:
    
    ```bash
    mvn clean install
3. **Run the Application**: You can run the application from your IDE or by using:
    
    ```bash
    mvn spring-boot:run
The Spring Boot application should now be running at http://localhost:8080.

## Step 3: Set Up and Run the Angular Application
1. **Navigate to the Angular Project Directory**: Install all dependencies
    
    ```bash
    cd path/to/My_Budget
2. **Install all dependencies**:
    
    ```bash
    npm install
3. **Start the Angular Development Server**:
    
    ```bash
    ng serve
The Angular application should now be running at http://localhost:4200.
