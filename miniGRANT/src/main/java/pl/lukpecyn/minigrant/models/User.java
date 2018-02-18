package pl.lukpecyn.minigrant.models;

import java.util.ArrayList;
import java.util.UUID;

public class User {

	String username;
	String password;
	String fullname;
	String email;
	UUID guid;
	boolean confirmed;
	boolean enabled;
	Role role;
	    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String u) {
        this.fullname = u;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String p) {
        this.email = p;
    }

    public void setGuid(UUID guid) {
    	this.guid = guid;
    }
    public UUID getGuid() {
    	return this.guid;
    }
    
	public void setConfirmed(boolean b) {
		this.confirmed = b;
	}
	public boolean getConfirmed() {
		return this.confirmed;
	}
	
	public void setEnabled(boolean b) {
		this.enabled = b;
	}
	public boolean getEnabled() {
		return this.enabled;
	}
	
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role r) {
        this.role = r;
    }

    public String toString(){
	    StringBuilder builder = new StringBuilder();
	    builder.append(this.getUsername())
	      .append(", ")
	      .append(this.getPassword())
	      .append(", ")
	      .append(this.getRole().getRole());
	    
	    return builder.toString();
	  }
}
