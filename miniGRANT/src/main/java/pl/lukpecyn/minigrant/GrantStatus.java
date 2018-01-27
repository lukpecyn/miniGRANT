package pl.lukpecyn.minigrant;

public class GrantStatus {

	Integer id;
	String name;
	
	public GrantStatus() {
		this.id=-1;
	}
	
	public GrantStatus(Integer i,String s) {
		this.id = i;
		this.name = s;
	}
	
	public void setId(Integer i) {
		this.id=i;
	}
	public Integer getId() {
		return this.id;
	}
	
	public void setName(String s) {
		this.name = s;
	}
	public String getName() {
		return this.name;
	}

}
