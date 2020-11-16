package cn.osworks.aos.system.modules.controller.chulinprint;
import java.sql.*;


/**
 * 此类提供基本的数据库操作，包括打开和关闭数据库连接，执行SQL语句获取数据集
 * 此类是只是服务于测试数据，使用时请自行替换和改写
 * @author Administrator
 *
 */
public class SqlHelper {
	/**
	 * 数据库连接
	 */
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

	public SqlHelper() {
		conn = null;
		stmt = null;
		rs = null;
	}

	/**
	 * 打开数据库连接
	 */
	public void OpenDataConnection() {
		if (conn == null) {
			try {
				String url = "jdbc:sqlserver://localhost:1433; DatabaseName=aosyb";
				String user = "sa";
				String password = "sasys";
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				conn = DriverManager.getConnection(url, user, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭数据库连接
	 */
	public void Close() {
		CloseResultStatement();
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		conn = null;
	}
	
	/**
	 * 执行一个SQL语句，判断记录是否存在
	 * @param strSql
	 * @return
	 */
	public boolean CheckIsExist(String strSql ) {
		boolean bIsExist = false;
		if( conn == null ) {
			OpenDataConnection();
		}
		
		try {
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(strSql);
		    bIsExist = rs2.next();
		    rs2.close();
			stmt2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return bIsExist;
	}
	
	/**
	 * 读取第一条记录的指定的日期字段的值
	 * @param strSql
	 * @param strFieldName
	 * @return
	 */
	public java.util.Date GetOneDateTime( String strSql, String strFieldName) {
		java.util.Date dtValue = new java.util.Date(System.currentTimeMillis() - 600*1000);
		if( conn == null ) {
			OpenDataConnection();
		}
		
		try {
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(strSql);
			if( rs2.next() ) {
				dtValue = rs2.getTimestamp(strFieldName);
			}
		    rs2.close();
			stmt2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return dtValue;
	}
	
	
	/**
	 * 执行一条SQL语句
	 * @param strSql
	 */
	public void  Execute(String strSql ) {
		if( conn == null ) {
			OpenDataConnection();
		}
		
		try {
			Statement stmt2 = conn.createStatement();
			stmt2.executeUpdate(strSql);
			stmt2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
	/**
	 * 通过执行SQL语句，获取一个数据表
	 * @param strSql
	 * @return
	 */
	public ResultSet GetTable(String strSql) {
		if( conn == null ) {
			OpenDataConnection();
		}
		
		try {
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery(strSql);
			
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * 关闭Result Statement
	 */
	public void CloseResultStatement() {
		if( rs != null ) {
			try {
		    	rs.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		rs = null;
		if( stmt != null ) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		stmt = null;
	}
	
}
