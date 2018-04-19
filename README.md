# ToDoApp-Coding-Challange

# User Management 
## Manages user addition, deletion and showing the list of users

### GET /userlist Used for getting the list of users
### POST /adduser Used for adding a user, needs "username" form
### POST /deleteUser used for deleting a user, needs "username" form

# Todo Management
## Manages todo addition, deletion and updating for a single user, also can show the entire list of todos for a user

### GET /user/{username}/todolist returns the list of todo items for the user
### GET /user/{username}/todo/{todoId} return details on a single todo item
### POST /user/{username}/addtodo adds a todo for the user, needs "title" "desc" "dueDate" (epoch time)
### POST /user/{username}/deletetodo removes a todo for the user, needs "id"
### PATCH /user/{username}/todo/{todoId} updates values for a todo item, requires all feilds from addtodo, fields that are blank won't be updated


# Event Management
## Logs all the api requests and is password protected (authentication is given by cookie) and only one user can have access to the logs at a single time

### POST /login get a access key by passing the correct password through "password"
### POST /logout logout and free up the login for someone else, requires the key from login to use
### GET /events get a event log by passing through the key(cookie) given by the login request
