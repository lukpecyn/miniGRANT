package pl.lukpecyn.minigrant;

public class CostType {
	Integer id;
	String name;
	String description;
	
	public CostType() {
		id=-1;
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
	
	public void setDescription(String s) {
		this.description =s;
	}
	public String getDescription() {
		return this.description;
	}
}
