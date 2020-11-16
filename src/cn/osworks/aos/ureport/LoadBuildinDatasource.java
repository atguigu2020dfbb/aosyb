package cn.osworks.aos.ureport;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.bstek.ureport.definition.datasource.BuildinDatasource;

public class LoadBuildinDatasource implements BuildinDatasource {

	private DataSource dataSource;
	
	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "内置数据源Demo";
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
