package service;

import java.util.Hashtable;

import org.springframework.stereotype.Service;

import model.Todo;
import model.User;
import response.TodoList;
import response.UserList;

@Service
public class TodoService {
	
	private Hashtable<String, User> users = new Hashtable<String, User>();
	
	//user
	public UserList getUserList() {
		return new UserList(users.keySet());
	}
	
	public boolean addUser(String username) {
		if(users.containsKey(username)) return false;
		users.put(username, new User(username)); 
		return true;
	}	
	
	public boolean deleteUser(String username) {
		if(!users.containsKey(username)) return false;
		users.remove(username);
		return true;
	}
	//user end
	
	//Todo
	public TodoList getUserTodoList(String username) {
		return users.get(username).getTodoList();
	}
	
	public Todo getUserTodo(String username, int id) {
		return users.get(username).getTodo(id);
	}
	
	public void addUserTodo(String username, Todo todo) {
		users.get(username).addTodoItem(todo);
	}
	
	public boolean deleteUserTodo(String username, int id) {
		return users.get(username).deleteTodo(id);
	}
	
	public Todo updateTodo(String username, int id, Todo todo) {
		return users.get(username).updateTodo(id, todo);
	}
	
	//Todo end
	
	//UTIL
	public boolean doesUserHaveTodo(String username, int id) { return users.get(username).doesHaveTodo(id);}
	public boolean userExist(String username) { return users.containsKey(username);}
	
	
	public void clearUsers() {
		users.clear();
	}
	
}
