package pro.dev.sql;

public class ColumnValue<T> {
	
	private Column<T> column;
	private T value;
	
	public Column<T> getColumn() {
		return column;
	}

	public void setColumn(Column<T> column) {
		this.column = column;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String format() {
		return "";
	}
}
