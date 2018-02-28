package pl.lukpecyn.minigrant.models;

public class Coworker {

	private String username;
	private Integer idBeneficiary;
	
	public Coworker() {
		
	}
	
	public Coworker(String username, Integer idBeneficiary) {
		this.username = username;
		this.idBeneficiary = idBeneficiary;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return this.username;
	}
	
	public void setIdBeneficiary(Integer idBeneficiary) {
		this.idBeneficiary = idBeneficiary;
	}
	public Integer getIdBeneficiary() {
		return this.idBeneficiary;
	}
}
