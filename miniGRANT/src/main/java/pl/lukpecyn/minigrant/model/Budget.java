package pl.lukpecyn.minigrant.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class Budget {

	Integer id;
	Integer idGrant;
	CostType costType;
	String description;
	BigDecimal dotation;
	BigDecimal contributionOwn;
	BigDecimal contributionPersonal;
	BigDecimal contributionInkind;
	BigDecimal paidDotation;
	BigDecimal paidContributionOwn;
	BigDecimal paidContributionPersonal;
	BigDecimal paidContributionInkind;
	
	public Budget() {
		id = -1;
		dotation = new BigDecimal("0.00");
		contributionOwn = new BigDecimal("0.00");
		contributionPersonal = new BigDecimal("0.00");
		contributionInkind = new BigDecimal("0.00");
		paidDotation = new BigDecimal("0.00");
		paidContributionOwn = new BigDecimal("0.00");
		paidContributionPersonal = new BigDecimal("0.00");
		paidContributionInkind = new BigDecimal("0.00");
	}
	
	public void setId(Integer i) {
		this.id = i;
		//paidDotation = paymentService.getPaymentForBudgetDotationSum(this.id);
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

	public void setPaidDotation(BigDecimal bd) {
		this.paidDotation = bd;
	}
	public BigDecimal getPaidDotation() {
		return this.paidDotation;
	}

	public void setPaidContributionOwn(BigDecimal bd) {
		this.paidContributionOwn = bd;
	}
	public BigDecimal getPaidContributionOwn() {
		return this.paidContributionOwn;
	}

	public void setPaidContributionPersonal(BigDecimal bd) {
		this.paidContributionPersonal = bd;
	}	
	public BigDecimal getPaidContributionPersonal() {
		return this.paidContributionPersonal;
	}

	public void setPaidContributionInkind(BigDecimal bd) {
		this.paidContributionInkind = bd;
	}
	public BigDecimal getPaidContributionInkind() {
		return this.paidContributionInkind;
	}
	
	public BigDecimal getSum() {
		return dotation.add(contributionOwn.add(contributionPersonal.add(contributionInkind)));
	}
	
	public BigDecimal getPaidSum() {
		return paidDotation.add(paidContributionOwn.add(paidContributionPersonal.add(paidContributionInkind)));
	}
}
