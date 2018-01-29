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
	BigDecimal own;
	BigDecimal volunteerism;
	
	public Payment() {
		this.id=-1;
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
		logger.info("Payment sum = " + dotation.add(own.add(volunteerism)).toString());
		return dotation.add(own.add(volunteerism));
	}

	
}
