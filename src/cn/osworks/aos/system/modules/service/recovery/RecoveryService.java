package cn.osworks.aos.system.modules.service.recovery;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;

/**
 * 
 * 回收站
 * 
 * @author shq
 * 
 * @date 2016-9-14
 */
@Service
public class RecoveryService extends JdbcDaoSupport {

	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}
	/**
	 * 回收站表明
	 */
	public void listAccounts() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 删除当前条件下的数据信息
	 * @param qDto
	 * @return 
	 */
	public boolean deleteData(Dto qDto) {
		// TODO Auto-generated method stub
		String tablename = qDto.getString("tablename");
		String querySession=qDto.getString("querySession");
		try {
			jdbcTemplate.execute("delete from "+tablename+" where 1=1 "+querySession);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 删除数据表中的所有数据信息
	 * @param qDto
	 * @return 
	 */
	public boolean deleteAllData(Dto qDto) {
		// TODO Auto-generated method stub
		String tablename = qDto.getString("tablename");
		try {
			jdbcTemplate.execute("delete from "+tablename);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 还原条件数据
	 * @param qDto
	 * @return
	 */
	public boolean returnData(Dto qDto) {
		// TODO Auto-generated method stub
		String tablename = qDto.getString("tablename");
		String querySession=qDto.getString("querySession");
		try {
			String zs_tablename=tablename.substring(0, tablename.length()-7);
			String sql="insert into "+zs_tablename+"  select * from "+tablename+" where 1=1  "+querySession;
			jdbcTemplate.execute(sql);
			jdbcTemplate.execute("delete from "+tablename+" where 1=1 "+querySession);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 还原数据表所有数据
	 * @param qDto
	 * @return
	 */
	public boolean returnAllData(Dto qDto) {
		// TODO Auto-generated method stub
		String tablename = qDto.getString("tablename");
		try {
			String zs_tablename=tablename.substring(0, tablename.length()-7);
			String sql="insert into "+zs_tablename+"  select * from "+tablename;
			jdbcTemplate.execute(sql);
			jdbcTemplate.execute("delete from "+tablename);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
