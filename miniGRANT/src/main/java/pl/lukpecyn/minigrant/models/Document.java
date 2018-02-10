package pl.lukpecyn.minigrant.models;

import java.math.BigDecimal;

public class Document {

	Integer id;
	Integer idGrant;
	String name;
	String description;
	BigDecimal value;
	
	//TODO: Lista powiązanych pozycji preliminarza (Budgets)
	
	public Document() {
		this.id=-1;
	}
	
	public void setId(Integer i) {
		this.id = i;
	}
	public Integer getId() {
		return this.id;
	}
	
	public void setIdGrant(Integer i) {
		this.idGrant = i;
	}
	public Integer getIdGrant() {
		return this.idGrant;
	}

	public void setName(String s) {
		this.name = s;
	}
	public String getName() {
		return this.name;
	}

	public void setDescription(String s) {
		this.description = s;
	}
	public String getDescription() {
		return this.description;
	}
	
	public void setValue(BigDecimal bd) {
		this.value = bd;
	}
	public BigDecimal getValue() {
		return this.value;
	}
}
