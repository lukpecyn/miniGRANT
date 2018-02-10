package pl.lukpecyn.minigrant.models;


public class Beneficiary {

	Integer id;
	String name;
	
	public Beneficiary() {
		id=-1;
	}
	
	public Beneficiary(Integer id, String name) {
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
