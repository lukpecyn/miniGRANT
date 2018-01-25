package pl.lukpecyn.minigrant;

public class Grant {

	private Integer id;
	private String name;
	private String dateBegin;
	private String dateEnd;
	private String description;
	private Integer status;

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
	public String getStatusText() {
		switch (this.status) {
			case 0:
				return "Planowany";
			case 10:
				return "Rozpoczęty";
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
}
