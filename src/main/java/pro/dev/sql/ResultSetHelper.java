package pro.dev.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetHelper {
	
	public static <T> T handle(ResultSet resultSet,ResultSetHandler<T> handler){
		try{
			return handler.handle(resultSet);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			try{
				if(!resultSet.isClosed()){
					resultSet.close();
				}
			}catch(SQLException e){
				throw new RuntimeException(e);
			}
		}
	}
}
