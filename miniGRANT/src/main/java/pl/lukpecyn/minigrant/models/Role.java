package pl.lukpecyn.minigrant.models;

public class Role {
	
	String username;
	String role;
	
	public Role() {
		
	}
	
	public Role(String u, String r) {
		this.username = u;
		this.role = r;
	}
	
	public void setUsername(String u) {
		this.username = u;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setRole(String r) {
		this.role = r;
	}
	
	public String getRole() {
		return this.role;
	}
}
