package pro.dev.sql;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 代表一个表的字段
 * @author YalikWang
 *
 */
public class Table {
	private Column pkColumn;
	private String name;
	private Collection<Column> columns = new ArrayList<>();
	private Table parentTable;//父表
	private Collection<Table> children;//子表
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<Column> getColumns() {
		return columns;
	}
	public void setColumns(Collection<Column> columns) {
		this.columns = columns;
	}
	public Table getParentTable() {
		return parentTable;
	}
	public void setParentTable(Table parentTable) {
		this.parentTable = parentTable;
	}
	public Column getPkColumn() {
		return pkColumn;
	}
	public void setPkColumn(Column pkColumn) {
		this.pkColumn = pkColumn;
	}
	public Collection<Table> getChildren() {
		return children;
	}
	public void setChildren(Collection<Table> children) {
		this.children = children;
	}
	
	
}
