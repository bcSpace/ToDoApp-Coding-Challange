package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.Todo;
import response.ErrorResponse;
import response.SimpleResponse;
import response.TodoList;
import response.UserList;
import service.EventLog;
import service.TodoService;

@Controller
public class AppController {
	
	@Autowired
	TodoService service; 
	
	@Autowired
	EventLog log;
	
	
	//Event log related 
	@RequestMapping(method = RequestMethod.GET, value = "/events")
	public ResponseEntity<?> getEventLog(@CookieValue(value="key", defaultValue = "01") String key) {
		if(key == null) return ResponseEntity.status(401).body(new ErrorResponse("Invalid authentication"));
		if(key.equals(log.getKey())) {
			log.addEvent("Log accessed");
			return ResponseEntity.ok(log.getEventLog());
		}
		return ResponseEntity.status(401).body(new ErrorResponse("Invalid authentication"));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/login") 
	public ResponseEntity<?> login(String password) {
		if(log.isLogedIn())
			ResponseEntity.status(400).body(new ErrorResponse("User already logged in"));
		
		boolean success = log.attemptLogin(password);
		if(!success)
			ResponseEntity.status(401).body(new ErrorResponse("Invalid password"));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie", "key="+log.getKey());
		log.addEvent("User loged in");
		return ResponseEntity.status(200).headers(headers).body(new SimpleResponse("Password accpeted"));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/logout") 
	public ResponseEntity<?> logout(@CookieValue(value="key", defaultValue = "01") String key) {
		System.out.println(key + ", " + log.getKey());
		if(!key.equals(log.getKey()))
			return ResponseEntity.status(401).body(new ErrorResponse("Invalid authentication"));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie", "key= 1");
		log.addEvent("User loged out");
		return ResponseEntity.status(200).headers(headers).body(new SimpleResponse("Loged out"));
	}
	
	//Event log end
	
	
	//user Management 
	
	@RequestMapping(method = RequestMethod.POST, value = "/addUser")
	public ResponseEntity<?> addUser(String username) {
		boolean success = service.addUser(username);
		if(success) {  
			log.addEvent("User added: " + username);
			return ResponseEntity.ok(new SimpleResponse("User Added")); 
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User ID is already taken")); 
	}
	
	@RequestMapping("/userlist")
	public ResponseEntity<?> getUserList() {
		log.addEvent("User list accessed");
		UserList users = service.getUserList();
		return ResponseEntity.ok(users);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/deleteUser")
	public ResponseEntity<?> deleteUser(String username) {
		boolean success = service.deleteUser(username);
		if(success) { 
			log.addEvent("User deleted: " + username);
			return ResponseEntity.ok(new SimpleResponse("User Deleted")); 
		}
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User ID does not exist")); 
	}
	
	//user management end
	
	//Todo related 
	
	@RequestMapping(method = RequestMethod.GET, value = "/user/{username}/todolist")
	public ResponseEntity<?> getUserTodoList(@PathVariable("username") String username) {
		
		if(!service.userExist(username)) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User does not exist"));
		
		TodoList todoList = service.getUserTodoList(username);
		log.addEvent("Todo list for " + username + " accessed");
		return ResponseEntity.ok(todoList); 
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/user/{username}/todo/{id}")
	public ResponseEntity<?> getTodo(@PathVariable("username") String username, @PathVariable("id") int id) {
		if(!service.userExist(username)) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User does not exist"));
		
		if(!service.doesUserHaveTodo(username, id))
			return ResponseEntity.status(404).body(new ErrorResponse("Todo ID does not exist"));
		
		log.addEvent("Todo for " + username + " accessed");
		return ResponseEntity.ok(service.getUserTodo(username, id));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/user/{username}/addTodo")
	public ResponseEntity<?> getUserTodoList(@PathVariable("username") String username, Todo todo) {
		if(!service.userExist(username)) 
			return ResponseEntity.status(400).body(new ErrorResponse("User does not exist"));
		service.addUserTodo(username, todo);
		log.addEvent("Todo added for " + username);
		return ResponseEntity.status(201).body(todo);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/user/{username}/deleteTodo")
	public ResponseEntity<?> deleteTodo(@PathVariable("username") String username, int id) {
		if(!service.userExist(username)) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User does not exist"));

		boolean success = service.deleteUserTodo(username, id);
		if(success)  {
			log.addEvent("Todo deleted for " + username);
			return ResponseEntity.status(201).body(new SimpleResponse("Deleted Todo"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Id does not exist"));
	}
	
	
	@RequestMapping(method = RequestMethod.PATCH, value = "/user/{username}/todo/{id}")
	public ResponseEntity<?> modifyTodo(@PathVariable("username") String username, @PathVariable("id") int id, String title, String desc, String dueDate) {
		if(!service.userExist(username)) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User does not exist"));
		
		
		if(!service.doesUserHaveTodo(username, id))
			return ResponseEntity.status(404).body(new ErrorResponse("Todo ID does not exist"));
		long time = 0;
		try {
			time = Long.parseLong(dueDate);
		} catch(Exception e) {}
		
		Todo todo = new Todo();
		todo.setTitle(title);
		todo.setDesc(desc);
		todo.setDueDate(time);
		log.addEvent("Todo changed for user " + username);
		return ResponseEntity.status(201).body(service.updateTodo(username, id, todo));
	}
	
	//Todo end
	
}
