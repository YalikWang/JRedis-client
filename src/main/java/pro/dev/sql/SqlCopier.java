package pro.dev.sql;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class SqlCopier {
	
	private Collection<Table> tables = new ArrayList<>();
	
	private Function<Column,Object> func;

	public SqlCopier(ResultSet rs) {
		this.parseColumn(rs);
	}
	
	public StringBuffer copy(Table table,ColumnValue parentPkValue) {
		Collection<Column> columns = table.getColumns();
		StringBuffer insert = new StringBuffer();
		insert.append("INSERT INTO ");
		insert.append(table.getName());
		insert.append("(");
		List<String> fields = new ArrayList<>();
		List<String> values = new ArrayList<>();
		ColumnValue pkValue = null;
		for(Column c:columns) {
			fields.add(c.getName());
			if(c.isForeignKey()) {
				values.add(parentPkValue.format());
			}else {
				//从外部注入的函数读取随机值
				Object value = func.apply(c);
				if(value==null) {
					value = c.getDefaultValue();
				}
				values.add(formatValue(c,value));
			}
			if(c.isPk()){
				pkValue = new ColumnValue();
				pkValue.setColumn(c);
				pkValue.setValue(PkFactory.getPk());
			}
		}
		insert.append(String.join(",", fields));
		insert.append(") VALUES(");
		insert.append(String.join(",", values));
		insert.append(");");
		Collection<Table> children = table.getChildren();
		if(children!=null&&!children.isEmpty()) {
			for(Table t:children) {
				insert.append("\n");
				insert.append(copy(t,pkValue));
			}
		}
		return insert;
	}
	
	private String formatValue(Column c,Object value) {
		return "";
	}
	
	public void parseColumn(ResultSet resultSet) {
		try {
			ResultSetMetaData rsm = resultSet.getMetaData();
			int col = rsm.getColumnCount();
			String[] colNames = new String[col];
			String[] colClassNames = new String[col];
			for (int i = 0; i < col; i++) {
				colNames[i] = rsm.getColumnName(i + 1);
				colClassNames[i] = rsm.getColumnClassName(i + 1);
			}
			while (resultSet.next()) {
				Collection<Column> columnList = new ArrayList<>();
				Table table = new Table();
				for (int i = 0; i < col; i++) {
					String column = colNames[i];
					String columType = colClassNames[i];
					Column c = null;
					if ("java.lang.Long".equals(columType)) {
						c = new Column<Long>(column,resultSet.getLong(column));
					} else if ("java.lang.String".equals(columType)) {
						c = new Column<String>(column,resultSet.getString(column));
					} else if ("java.lang.Integer".equals(columType)) {
						c = new Column<Integer>(column,resultSet.getInt(column));
					} else if ("java.sql.Timestamp".equals(columType)) {
						java.sql.Date sqlDate = resultSet.getDate(column);
						Date date = null;
						if(sqlDate!=null) {
							date = new Date(sqlDate.getTime());
						}
						c = new Column<Date>(column,date);
					} else if ("java.math.BigDecimal".equals(columType)) {
						c = new Column<BigDecimal>(column,resultSet.getBigDecimal(column));
					} else {
						throw new RuntimeException("未知的数据类型:"+columType);
					}
					columnList.add(c);
				}
				table.setColumns(columnList);
				tables.add(table);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static List<String> toInsertSql(ResultSet resultSet, String tableName) {
		List<String> insertSqlResult = new ArrayList<>();
		try {
			ResultSetMetaData rsm = resultSet.getMetaData();
			int col = rsm.getColumnCount();
			String[] colNames = new String[col];
			String[] colClassNames = new String[col];
			for (int i = 0; i < col; i++) {
				colNames[i] = rsm.getColumnName(i + 1);
				colClassNames[i] = rsm.getColumnClassName(i + 1);
			}
			while (resultSet.next()) {
				StringBuffer result = new StringBuffer();
				StringBuffer insertBuffer = new StringBuffer();
				insertBuffer.append("INSERT INTO ");
				insertBuffer.append(tableName);
				insertBuffer.append('(');
				StringBuffer fieldBuffer = new StringBuffer();
				StringBuffer valueBuffer = new StringBuffer();
				for (int i = 0; i < col; i++) {
					String column = colNames[i];
					fieldBuffer.append(column);
					fieldBuffer.append(", ");
					StringBuffer tem = new StringBuffer();
					String columType = colClassNames[i];
					if ("java.lang.Long".equals(columType)) {
						tem.append(resultSet.getLong(column));
					} else if ("java.lang.String".equals(columType)) {
						tem.append('\'');
						String value = resultSet.getString(column);
						if (value != null)
							tem.append(value);
						tem.append('\'');
					} else if ("java.lang.Integer".equals(columType)) {
						tem.append(resultSet.getInt(column));
					} else if ("java.sql.Timestamp".equals(columType)) {
						Object date = resultSet.getObject(column);
						if (date != null) {
							tem.append('\'');
							tem.append(date.toString());
							tem.append('\'');
						} else {
							tem.append("NULL");
						}
					} else if ("java.math.BigDecimal".equals(columType)) {
						BigDecimal bigDecimal = resultSet.getBigDecimal(column);
						tem.append(bigDecimal.toString());
					} else {
						throw new RuntimeException("00" + columType + "00");
					}
					if ("FID".equalsIgnoreCase(column) || "FEntryID".equalsIgnoreCase(column)
							|| "FBillNO".equalsIgnoreCase(column)) {
						valueBuffer.append("'%s'");
					} else {
						valueBuffer.append(tem);
					}
					valueBuffer.append(", ");
				}
				fieldBuffer = fieldBuffer.deleteCharAt(fieldBuffer.length() - 2);
				valueBuffer = valueBuffer.deleteCharAt(valueBuffer.length() - 2);
				insertBuffer.append(fieldBuffer);
				insertBuffer.append(')');
				insertBuffer.append(" VALUES(");
				insertBuffer.append(valueBuffer);
				insertBuffer.append(");\n");
				result.append(insertBuffer);
				insertSqlResult.add(result.toString());
			}
			return insertSqlResult;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
