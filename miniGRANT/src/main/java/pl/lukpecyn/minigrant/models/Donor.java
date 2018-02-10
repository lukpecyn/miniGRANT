package pl.lukpecyn.minigrant.models;


public class Donor {

	Integer id;
	String name;
	
	public Donor() {
		id=-1;
	}
	
	public Donor(Integer id, String name) {
		this.id = id;
		this.name = name;
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
