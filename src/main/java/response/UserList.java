package response;

import java.util.Set;

public class UserList {

	private String total;
	private Set<String> users;

	public UserList(Set<String> users) {
		this.users = users;
		total = users.size()+"";
	}
	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> users) {
		this.users = users;
	}

}
