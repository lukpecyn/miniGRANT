package pl.lukpecyn.minigrant.models;


public class Donor {

	Integer id;
	String name;
	Integer idBeneficiary;
	
	public Donor() {
		id=-1;
	}
	
	public Donor(Integer id, String name,Integer idBeneficiary) {
		this.id = id;
		this.name = name;
		this.idBeneficiary = idBeneficiary;
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
	
	public void setIdBeneficiary(Integer i) {
		this.idBeneficiary=i;
	}
	public Integer getIdBeneficiary() {
		return this.idBeneficiary;
	}
}
