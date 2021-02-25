package by.nosevich.internship.task2;

public class Entry {
	private Integer id;
	private Integer parrentId;
	private String name;
	
	public Entry(Integer id, Integer parrentId, String name) {
		super();
		this.id = id;
		this.parrentId = parrentId;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getParrentId() {
		return parrentId;
	}
	public void setParrentId(int parrentId) {
		this.parrentId = parrentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
