package pro.dev.sql;

import java.util.Collection;

public class SqlRecord {
	
	private Table table;
	private Collection<ColumnValue> values;
	
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public Collection<ColumnValue> getValues() {
		return values;
	}
	public void setValues(Collection<ColumnValue> values) {
		this.values = values;
	}
	
}
