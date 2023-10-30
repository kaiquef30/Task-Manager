API REST Readme
This is the documentation for the API REST service, which provides endpoints for user authentication, client management, task management, and team management. The API is designed to be used for various roles, such as users and administrators. Below is a comprehensive guide on how to use this API.

Table of Contents
Authentication
User Authentication
Register a New User
Log In
Verify Email
Retrieve User Information
Forgot Password
Reset Password
Client Management
Display All Clients
Register a New Client
Get Client by ID
Delete Client
Update Client
Task Management
Display All Tasks
View All Tasks for a User
Create Task
Edit Task
Search Task by ID
Delete Task
Team Management
View All Teams
Create New Team
Delete Team
Edit Team
Associate User with Team
Remove User from Team
Authentication
Before accessing any protected endpoints, you need to authenticate. This API uses JWT (JSON Web Tokens) for authentication. To authenticate, you need to include your JWT token in the Authorization header of your HTTP requests.

User Authentication
Endpoints related to user authentication and management.

Register a New User
URL: /api/auth/register

HTTP Method: POST
Description: Register a new user.
Request Body: A JSON object representing the user registration details.
Response: A successful registration will return the user information. In case of an error, you will receive an appropriate error message.
Log In
URL: /api/auth/login

HTTP Method: POST
Description: Log in as a user.
Request Body: A JSON object containing the user's login credentials.
Response: A successful login will return the user's information and a JWT token. In case of an error, you will receive an appropriate error message.
Verify Email
URL: /api/auth/verify

HTTP Method: POST
Description: Verify a user's email using a token.
Request Parameters: token (String) - The verification token.
Response: If the email is verified successfully, you'll receive a success response. If the email is already verified or an error occurs, you'll receive an appropriate error message.
Retrieve User Information
URL: /api/auth/me

HTTP Method: GET
Description: Retrieve information about the logged-in user.
Response: Returns the user's information.
Forgot Password
URL: /api/auth/forgot

HTTP Method: POST
Description: Request a password reset email.
Request Parameters: email (String) - The email for which to reset the password.
Response: If the email exists and the request is successful, an email for resetting the password will be sent. If the email is not found or an error occurs, you'll receive an appropriate error message.
Reset Password
URL: /api/auth/reset

HTTP Method: POST
Description: Reset the password of a user.
Request Body: A JSON object containing the user's new password.
Response: If the password reset is successful, you'll receive a success response. In case of an error, you'll receive an appropriate error message.
Client Management
Endpoints related to client management.

Display All Clients
URL: /api/clients

HTTP Method: GET
Description: Get a list of all clients.
Response: Returns a list of client objects.
Register a New Client
URL: /api/clients/create

HTTP Method: POST
Description: Register a new client.
Request Body: A JSON object representing the client details.
Response: If the client registration is successful, you'll receive the client information. In case of an error, you'll receive an appropriate error message.
Get Client by ID
URL: /api/clients/{clientId}

HTTP Method: GET
Description: Get client information by client ID.
Response: Returns the client's information if found. If not found, you'll receive an error message.
Delete Client
URL: /api/clients/{clientId}

HTTP Method: DELETE
Description: Delete a client by ID.
Response: If the deletion is successful, you'll receive a success response. If the client is not found, you'll receive an appropriate error message.
Update Client
URL: /api/clients/{clientId}

HTTP Method: PUT
Description: Update client information by ID.
Request Body: A JSON object containing the updated client details.
Response: If the update is successful, you'll receive the updated client information. In case of an error, you'll receive an appropriate error message.
Task Management
Endpoints related to task management.

Display All Tasks
URL: /api/tasks

HTTP Method: GET
Description: Get a list of all tasks.
Response: Returns a list of task objects.
View All Tasks for a User
URL: /api/tasks/user-tasks

HTTP Method: GET
Description: Get a list of all tasks for the currently logged-in user.
Response: Returns a list of task objects associated with the user.
Create Task
URL: /api/tasks/create

HTTP Method: POST
Description: Create a new task.
Request Body: A JSON object representing the task details.
Request Parameters: attachments (Multipart Form Data, optional) - Attachments for the task.
Response: If the task creation is successful, you'll receive the created task information. In case of an error, you'll receive an appropriate error message.
Edit Task
URL: /api/tasks/update/{taskId}

HTTP Method: PUT
Description: Edit a task by ID.
Request Body: A JSON object containing the updated task details.
Request Parameters: attachments (Multipart Form Data, optional) - Attachments for the task.
Response: If the task is successfully edited, you'll receive the updated task information. In case of an error, you'll receive an appropriate error message.
Search Task by ID
URL: /api/tasks/search/{taskId}

HTTP Method: GET
Description: Search for a task by ID.
Response: Returns the task information if found. If not found, you'll receive an error message.
Delete Task
URL: /api/tasks/{taskId}

HTTP Method: DELETE
Description: Delete a task by ID.
Response: If the deletion is successful, you'll receive a success response. If the task is not found, you'll receive an appropriate error message.
Team Management
Endpoints related to team management.

View All Teams
URL: /api/teams

HTTP Method: GET
Description: Get a list of all teams.
Response: Returns a list of team objects.
Create New Team
URL: /api/teams/create

HTTP Method: POST
Description: Create a new team.
Request Body: A JSON object representing the team details.
Response: If the team creation is successful, you'll receive the created team information. In case of an error, you'll receive an appropriate error message.
Delete Team
URL: /api/teams/{teamId}

HTTP Method: DELETE
Description: Delete a team by ID.
Response: If the deletion is successful, you'll receive a success response. If the team is not found, you'll receive an appropriate error message.
Edit Team
URL: /api/teams/{teamId}

HTTP Method: PUT
Description: Edit a team by ID.
Request Body: A JSON object containing the updated team details.
Response: If the team is successfully edited, you'll receive the updated team information. In case of an error, you'll receive an appropriate error message.
Associate User with Team
URL: /api/teams/{teamId}/addUser/{userId}

HTTP Method: POST
Description: Associate a user with a team.
Response: If the user is successfully added to the team, you'll receive a success response. If the team or user is not found, or the user is already on the team, you'll receive an appropriate error message.
Remove User from Team
URL: /api/teams/{teamId}/removeUser/{userId}

HTTP Method: DELETE
Description: Remove a user from a team.
Response: If the user is successfully removed from the team, you'll receive a success response. If the team or user is not found, you'll receive an appropriate error message.
Feel free to use the Swagger documentation provided for each endpoint for detailed information on request and response payloads.

Note: For certain operations, you need to have the appropriate roles (e.g., ADMIN, USER) to access them, as indicated in the endpoint descriptions. Make sure to include the necessary JWT token with the required roles when making these requests.
