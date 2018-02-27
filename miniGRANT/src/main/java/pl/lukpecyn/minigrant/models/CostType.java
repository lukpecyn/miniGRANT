package pl.lukpecyn.minigrant.models;

public class CostType {
	Integer id;
	String name;
	String description;
	Integer idBeneficiary;
	
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

	public void setIdBeneficiary(Integer i) {
		this.idBeneficiary=i;
	}
	public Integer getIdBeneficiary() {
		return this.idBeneficiary;
	}
}
