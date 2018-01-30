package pl.lukpecyn.minigrant;

import java.math.BigDecimal;

public class Grant {

	private Integer id;
	private String name;
	private String dateBegin;
	private String dateEnd;
	private String description;
	private Integer status;
	private BigDecimal dotation;
	private BigDecimal contributionOwn;
	private BigDecimal contributionPersonal;
	private BigDecimal contributionInkind;
	
	public Grant() {
		id=-1;
		
	}
	public void setId(Integer i) {
		this.id = i;
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

	public void setDateBegin(String s) {
		this.dateBegin = s;
	}	
	public String getDateBegin() {
		return this.dateBegin;
	}

	public void setDateEnd(String s) {
		this.dateEnd = s;
	}	
	public String getDateEnd() {
		return this.dateEnd;
	}

	public void setDescription(String s) {
		this.description = s;
	}	
	public String getDescription() {
		return this.description;
	}

	public void setStatus(Integer i) {
		this.status = i;
	}	
	public Integer getStatus() {
		return this.status;
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
}
