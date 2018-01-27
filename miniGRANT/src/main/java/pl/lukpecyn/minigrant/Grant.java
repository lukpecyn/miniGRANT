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
	private BigDecimal own;
	private BigDecimal volunteerism;

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
/*	public String getStatusText() {
		switch (this.status) {
			case 0:
				return "Projekt";
			case 10:
				return "Realizacja";
			case 20:
				return "Zakończony";
			case 30: 
				return "Rozliczony";
			case 99:
				return "Zarchiwizowany";
			default:
				return "Nieznany";
		}
	}
*/
	
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
	}
	public BigDecimal getVolunteerism() {
		return this.volunteerism;
	}
}
