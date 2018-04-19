package model;

import java.util.Hashtable;
import java.util.Set;

import response.TodoList;

public class User {
	
	private String username; //this is also userId
	Hashtable<Integer, Todo> todoList = new Hashtable<Integer, Todo>();
	int currentTodoId; 
	
	public User(String username) {
		this.username = username;
		currentTodoId = 0;
	}
	
	public void addTodoItem(Todo todo) {
		todoList.put(currentTodoId, todo); 
		currentTodoId++;
	}
	
	public boolean deleteTodo(int id) {
		if(!todoList.containsKey(id)) return false;
		todoList.remove(id); 
		return true;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public TodoList getTodoList() {
		Set<Integer> keys = todoList.keySet();
		String values[][] = new String[keys.size()][2];
		for(int i = 0; i < keys.size(); i++) {
			values[i][0] = ""+i;
			values[i][1] = todoList.get(i).getTitle();
		}
		return new TodoList(values);
	}
	
	public boolean doesHaveTodo(int id) {
		return todoList.containsKey(id);
	}
	
	public Todo getTodo(int id) {
		return todoList.get(id);
	}
	
	public Todo updateTodo(int id, Todo todo) {
		if(todo.getDesc().length() != 0) todoList.get(id).setDesc(todo.getDesc());
		if(todo.getTitle().length() != 0) todoList.get(id).setTitle(todo.getTitle());
		if(todo.getDueDate() != 0) todoList.get(id).setDueDate(todo.getDueDate());
		return todoList.get(id);
	}
	
}
