package pl.lukpecyn.minigrant;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Payment {

	private static final Logger logger = LoggerFactory.getLogger(Payment.class);
	
	Integer id;
	Budget budget;
	Document document;
	BigDecimal dotation;
	BigDecimal contributionOwn;
	BigDecimal contributionPersonal;
	BigDecimal contributionInkind;
	
	public Payment() {
		this.id=-1;
		dotation = new BigDecimal("0.00");
		contributionOwn = new BigDecimal("0.00");
		contributionPersonal = new BigDecimal("0.00");
		contributionInkind = new BigDecimal("0.00");
	}
	
	public Payment(Integer id, Budget budget, Document document, BigDecimal dotation, BigDecimal contributionOwn, BigDecimal contributionPersonal, BigDecimal contributionInkind) {
		this.id = id;
		this.budget = budget;
		this.document = document;
		this.dotation = dotation;
		this.contributionOwn = contributionOwn;
		this.contributionPersonal = contributionPersonal;
		this.contributionInkind = contributionInkind;
	}
	
	
	public void setId(Integer i) {
		this.id = i;
	}
	public Integer getId() {
		return this.id;
	}
	
	public void setBudget(Budget b) {
		this.budget = b;
	}
	public Budget getBudget() {
		return this.budget;
	}
	
	public void setDocument(Document d) {
		this.document = d;
	}
	public Document getDocument() {
		return this.document;
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
