package pl.lukpecyn.minigrant;

import java.math.BigDecimal;

public class Budget {
	Integer id;
	Integer idGrant;
	CostType costType;
	String description;
	BigDecimal dotation;
	BigDecimal own;
	BigDecimal volunteerism;
	
	public Budget() {
		id = -1;
		dotation = new BigDecimal("0.00");
		own = new BigDecimal("0.00");
		volunteerism = new BigDecimal("0.00");
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

	public void setCostType(CostType ct) {
		this.costType = ct;
	}
	public CostType getCostType() {
		return this.costType;
	}
	
	public void setDescription(String s) {
		this.description = s;
	}	
	public String getDescription() {
		return this.description;
	}

	public void setDotation(BigDecimal bd) {
		this.dotation = bd;
	}
	public BigDecimal getDotation() {
		return this.dotation;
	}

	public void setOwn(BigDecimal bd) {
		this.own = bd;
	}
	public BigDecimal getOwn() {
		return this.own;
	}

	public void setVolunteerism(BigDecimal bd) {
		this.volunteerism = bd;
	}	public BigDecimal getVolunteerism() {
		return this.volunteerism;
	}
	
	public BigDecimal getSum() {
		return dotation.add(own.add(volunteerism));
	}
}
