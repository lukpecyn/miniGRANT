package pl.lukpecyn.minigrant;

import java.math.BigDecimal;

public class Budget {
	Integer id;
	Integer idGrant;
	CostType costType;
	String description;
	BigDecimal dotation;
	BigDecimal contributionOwn;
	BigDecimal contributionPersonal;
	BigDecimal contributionInkind;
	
	public Budget() {
		id = -1;
		dotation = new BigDecimal("0.00");
		contributionOwn = new BigDecimal("0.00");
		contributionPersonal = new BigDecimal("0.00");
		contributionInkind = new BigDecimal("0.00");
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

	public void setContributionOwn(BigDecimal bd) {
		this.contributionOwn = bd;
	}
	public BigDecimal getContributionOwn() {
		return this.contributionOwn;
	}

	public void setContributionPersonal(BigDecimal bd) {
		this.contributionPersonal = bd;
	}	
	public BigDecimal getContributionPersonal() {
		return this.contributionPersonal;
	}

	public void setContributionInkind(BigDecimal bd) {
		this.contributionInkind = bd;
	}
	public BigDecimal getContributionInkind() {
		return this.contributionInkind;
	}

	public BigDecimal getSum() {
		return dotation.add(contributionOwn.add(contributionPersonal.add(contributionInkind)));
	}

}
