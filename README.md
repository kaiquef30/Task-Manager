# Authentication and Management API RESTful

🔐 Welcome to our Authentication and Management API, where we provide essential features for user registration, login, client management, task creation, and team management.

## API Endpoints

### Authentication

- **Register a New User**
  - 📝 `POST /api/auth/register`
  - Register a new user on the platform.

- **Login**
  - 🔑 `POST /api/auth/login`
  - Allows users to log in to the platform.

- **Verify Email**
  - 📧 `POST /api/auth/verify`
  - Verify the user's email.

- **Get Information of the Logged-in User**
  - 🧑‍💻 `GET /api/auth/me`
  - Retrieve information about the currently logged-in user.

### Clients

- **Get All Clients**
  - 📋 `GET /api/clients`
  - Returns a list of all clients.

- **Register a New Client**
  - 📝 `POST /api/clients/create`
  - Register a new client on the platform.

- **Retrieve Client by ID**
  - 📌 `GET /api/clients/{clientId}`
  - Retrieve information about a specific client based on the ID.

- **Delete Client**
  - 🗑️ `DELETE /api/clients/{clientId}`
  - Delete a client based on the ID.

- **Update Client**
  - 🔄 `PUT /api/clients/{clientId}`
  - Update information about a client based on the ID.

### Tasks

- **Get All Tasks**
  - 📋 `GET /api/tasks`
  - Returns a list of all tasks.

- **Get All User's Tasks**
  - 🧑‍💼 `GET /api/tasks/user-tasks`
  - Returns a list of all tasks for a specific user.

- **Create a New Task**
  - ➕ `POST /api/tasks/create`
  - Create a new task on the platform.

- **Edit a Task**
  - ✏️ `PUT /api/tasks/update/{taskId}`
  - Edit information about a task based on the ID.

- **Retrieve Task by ID**
  - 📌 `GET /api/tasks/search/{taskId}`
  - Retrieve information about a specific task based on the ID.

- **Delete Task**
  - 🗑️ `DELETE /api/tasks/{taskId}`
  - Delete a task based on the ID.

### Teams

- **Get All Teams**
  - 🏢 `GET /api/teams`
  - Returns a list of all teams.

- **Create a New Team**
  - ➕ `POST /api/teams/create`
  - Create a new team on the platform.

- **Delete Team**
  - 🗑️ `DELETE /api/teams/{teamId}`
  - Delete a team based on the ID.

- **Edit Team**
  - 🔄 `PUT /api/teams/{teamId}`
  - Update information about a team based on the ID.

- **Add User to Team**
  - ➕ `POST /api/teams/{teamId}/addUser/{userId}`
  - Add a user to a team.

- **Remove User from Team**
  - 🚫 `DELETE /api/teams/{teamId}/removeUser/{userId}`
  - Remove a user from a team.

## Some of the technologies used

Our Authentication and Management API has been developed using the following technologies:

- ☕ **Java**: The primary programming language used for building the application.

- 🚀 **Spring Boot**: A framework that simplifies the development of Spring-based Java applications.

- ✅ **Spring Validation**: Used to validate input data, ensuring data integrity.

- 🐘 **PostgreSQL**: A relational database management system used to store user, client, task, and team information.

- 🧰 **Maven**: An automation and dependency management tool used to manage project libraries.

- 🔒 **Spring Security**: Used to implement security features like authentication and authorization.

- 🛠️ **Hibernate**: An Object-Relational Mapping (ORM) framework that simplifies database interactions.

- 📚 **Swagger**: A framework for API documentation, making it easy for developers to understand and test API endpoints.

- 📊 **Spring Data**: Simplifies database interactions by providing simplified CRUD operations.

- ✅ **JUnit**: A unit testing framework used to automate testing of application functionality.

- 🃏 **Mockito**: A testing framework that enables the creation and configuration of mock objects for unit tests.

- 🌐 **JWT (JSON Web Tokens)**: Used for secure authentication and access token generation.

- 🔄 **Redis**: A high-performance, in-memory data store used for caching and session management.
  
- ⚙️ **AspectJ**: Utilized for aspect-oriented programming (AOP) to modularize cross-cutting concerns such as logging, security, and transaction management in a clean and efficient manner.

- 🛠️ Lombok: Used to simplify and reduce boilerplate code in Java classes. Lombok provides annotations like @Data, @Getter, @Setter, and more, to automatically generate methods such as getters, setters, and constructors, enhancing code conciseness and maintainability.

These technologies were chosen to create a robust, secure, and high-performance API that meets the needs of user authentication, client and task management, and team collaboration.

## Autor

- [Kaique Fernando](https://github.com/kaiquef30)

