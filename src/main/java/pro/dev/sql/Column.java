package pro.dev.sql;

public class Column<T> {
	
	private boolean isPk;
	private boolean isForeignKey;//是否外键
	private String name;
	private T defaultValue;
	
	public Column(String name,T defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String format(T value) {
		return null;
	}
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	public boolean isForeignKey() {
		return isForeignKey;
	}
	public void setForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}
	
	
}
