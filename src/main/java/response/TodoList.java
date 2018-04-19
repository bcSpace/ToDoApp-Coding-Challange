package response;

public class TodoList {
	
	private String[][] todoList;
	private String total;

	public TodoList(String[][] todoList) {
		this.todoList = todoList;
		total = ""+todoList.length;
	}

	public String[][] getTodoList() {
		return todoList;
	}

	public void setTodoList(String[][] todoList) {
		this.todoList = todoList;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
}
