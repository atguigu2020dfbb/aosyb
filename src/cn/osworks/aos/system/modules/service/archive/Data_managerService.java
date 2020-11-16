package cn.osworks.aos.system.modules.service.archive;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.exception.AOSException;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 数据服务
 *
 * @author shq
 *
 * @date 2016-9-14
 */
@Service
public class Data_managerService extends JdbcDaoSupport {

	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private Aos_sys_moduleMapper aos_sys_moduleMapper;
	@Autowired
	private Aos_sys_userMapper aos_sys_userMapper;
	@Autowired
	private Archive_zdyjMapper archive_zdyjMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	public static String url = "";
	@Autowired
	public static String username = "";
	@Autowired
	public static String password = "";
	@Autowired
	public static Connection connection = null;
	@Autowired
	public static PreparedStatement ps;
	@Autowired
	private LogMapper logMapper;

	// 加载配置文件
	static {
		try {
			Properties props = new Properties();
			InputStream in = Data_managerService.class
					.getResourceAsStream("/jdbc.properties");

			props.load(in);

			url = props.getProperty("jdbc.url");
			username = props.getProperty("jdbc.username");
			password = props.getProperty("jdbc.password");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}

	/**
	 *
	 * 显示表头
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataFieldListDisplayAll(Dto qDto, String username) {
		List listDto =new ArrayList();
		String query = queryConditions2(qDto);
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
		String selectmark = qDto.getString("selectmark");
		if (selectmark.equals("1")) {
			// 存入缓存表中
			addSearchData(qDto, username);
		}
		String sql = "";
		String term = "";
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablename"));
		if(list.size()<=0&&list!=null){
			return listDto;
		}
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		// 到这一步进行全宗筛选
		/*Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}*/
		// 排列条件传递过去
		String orderBy = getOrderBy(qDto, username);
		if (orderenfield != null) {
			if (orderBy != null){
				orderBy = "( ORDER BY "+orderBy+")";
			}else{
				orderBy = "( ORDER BY "+orderenfield+")";
			}

		} else {
			orderBy = "( ORDER BY "+orderenfield+")";
		}
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")+""
					+ " WHERE 1=1"
					+ qDto.getString("queryclass")
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ qDto.getPageLimit()
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")+""
					+ " WHERE 1=1"
					+ qDto.getString("queryclass")
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	/**
	 *
	 * 显示表头
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataFieldListDisplayAll_data(Dto qDto, String username) {
		String query = queryConditions2(qDto);
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
		String selectmark = qDto.getString("selectmark");
		if (selectmark.equals("1")) {
			// 存入缓存表中
			addSearchData(qDto, username);
		}
		String sql = "";
		String term = "";
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		// 到这一步进行全宗筛选
		Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}
		// 排列条件传递过去
		String orderBy = getOrderBy(qDto, username);
		if (orderenfield != null) {
			if (orderBy != null){
				orderBy = "( ORDER BY "+orderBy+")";
			}else{
				orderBy = "( ORDER BY "+orderenfield+")";
			}
		} else {
			orderBy = "( ORDER BY "+orderenfield+")";
		}
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " WHERE 1=1 "
					+ query
					+ ") "
					+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ qDto.getPageLimit()
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " WHERE 1=1 "
					+ query
					+ ") "
					+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	/**
	 *
	 * 显示表头
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataFieldListDisplayAll_backup(Dto qDto, String username) {
		String query = queryConditions2(qDto);
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
		String selectmark = qDto.getString("selectmark");
		if (selectmark.equals("1")) {
			// 存入缓存表中
			addSearchData(qDto, username);
		}
		String sql = "";
		String term = "";
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		// 到这一步进行全宗筛选
		Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}
		// 排列条件传递过去
		String orderBy = getOrderBy(qDto, username);
		if (orderenfield != null) {
			if (orderBy != null){
				orderBy = "( ORDER BY "+orderBy+")";
			}else{
				orderBy = "( ORDER BY "+orderenfield+")";
			}

		} else {
			orderBy = "( ORDER BY "+orderenfield+")";
		}
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")+"_backup"
					+ " WHERE 1=1 "
					+ query
					+ ") "
					+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ qDto.getPageLimit()
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")+"_backup"
					+ " WHERE 1=1 "
					+ query
					+ ") "
					+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	/**
	 *
	 * (子表)显示表头
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataFieldListDisplayAllSon(Dto qDto, String username) {
		String query = queryConditions2(qDto);
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
		String selectmark = qDto.getString("selectmarkson");
		if (selectmark.equals("1")) {
			// 存入缓存表中
			addSearchDataSon(qDto, username);
		}
		//此时在根据子父表关系，查询到绑定的条件


		//得到子父表关系
		query+=getSonQuery(qDto.getString("tablename"),qDto.getString("tablenameson"),qDto.getString("fid"));

		String sql = "";
		String term = "";
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablenameson"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		// 到这一步进行全宗筛选
		Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}
		// 排列条件传递过去
		String orderBy = getOrderBySon(qDto, username);
		if (orderenfield != null) {
			if (orderBy != null){
				orderBy = "( ORDER BY "+orderBy+")";
			}else{
				orderBy = "( ORDER BY "+orderenfield+")";
			}

		} else {
			orderBy = "( ORDER BY "+orderenfield+")";
		}
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablenameson")
					+ " WHERE "
					+ qDto.getString("queryclass")
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ qDto.getPageLimit()
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablenameson")
					+ " WHERE "
					+ qDto.getString("queryclass")
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	/**
	 * 得到子父表详细信息
	 * @param tablename
	 * @param tablename_z
	 * @return
	 */
	private String getSonQuery(String tablename, String tablename_z,String id_) {
		String sql="select  s_field,t_field from archive_JOIN  where s_table='"+tablename+"' and  t_table='"+tablename_z+"'";

		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		String query="";
		if(list!=null&& list.size()>0){
			for(int i=0;i<list.size();i++){
				String s_field=(String)list.get(i).get("s_field");
				String t_field=(String)list.get(i).get("t_field");
				String sql_s="select FieldEnName from archive_TableFieldList where id_='"+s_field+"'";
				String sql_t="select FieldEnName from archive_TableFieldList where id_='"+t_field+"'";
				List<Map<String,Object>> list_s=jdbcTemplate.queryForList(sql_s);
				List<Map<String,Object>> list_t=jdbcTemplate.queryForList(sql_t);
				if(list_t!=null&&list_t.size()>0&&list_s!=null&&list_s.size()>0){
					String FieldEnName_s=(String)list_s.get(0).get("FieldEnName");
					String FieldEnName_t=(String)list_t.get(0).get("FieldEnName");
					//组合条件
					query=query+" and "+FieldEnName_t+" in (select "+FieldEnName_s+" from "+tablename+" where id_='"+id_+"')";
				}
			}
		}
		return query;
	}
	/**
	 * 得到父表携带的值
	 * @param tablename
	 * @param tablename_z
	 * @return
	 */
	private Dto getSonValue(String tablename, String tablename_z,String id_) {
		String sql="select  s_field,t_field from archive_JOIN  where s_table='"+tablename+"' and  t_table='"+tablename_z+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		Dto dto=Dtos.newDto();
		String query="";
		if(list!=null&& list.size()>0){
			for(int i=0;i<list.size();i++){

				String s_field=(String)list.get(i).get("s_field");
				String t_field=(String)list.get(i).get("t_field");
				String sql_s="select FieldEnName from archive_TableFieldList where id_='"+s_field+"'";
				String sql_t="select FieldEnName from archive_TableFieldList where id_='"+t_field+"'";
				List<Map<String,Object>> list_s=jdbcTemplate.queryForList(sql_s);
				List<Map<String,Object>> list_t=jdbcTemplate.queryForList(sql_t);
				if(list_t!=null&&list_t.size()>0&&list_s!=null&&list_s.size()>0){
					String FieldEnName_s=(String)list_s.get(0).get("FieldEnName");
					String FieldEnName_t=(String)list_t.get(0).get("FieldEnName");
					//组合条件
					//此时根据当前父表选中的条目
					String sql2="select "+FieldEnName_s+" from "+tablename+" where id_='"+id_+"'";
					if(jdbcTemplate.queryForList(sql2).size()>0){
						String value_s=(String)jdbcTemplate.queryForList(sql2).get(0).get(FieldEnName_s);
						dto.put(FieldEnName_t,value_s);
					}
				}
			}
		}
		return dto;
	}


	/**
	 *
	 * 初检数据信息查询
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataFieldListDisplayAll2(Dto qDto, String username) {
		String query = qDto.getString("query");
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
		String selectmark = qDto.getString("selectmark");
		if (selectmark.equals("1")) {
			// 存入缓存表中
			addSearchData(qDto, username);
		}
		String sql = "";
		String term = "";
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		// 到这一步进行全宗筛选
		Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}
		// 排列条件传递过去
		String orderBy = getOrderBy(qDto, username);
		if (orderenfield != null) {
			if (orderBy != null){
				orderBy = "( ORDER BY "+orderBy+")";
			}else{
				orderBy = "( ORDER BY "+orderenfield+")";
			}
		} else {
			orderBy = "( ORDER BY "+orderenfield+")";
		}
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER  "
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " WHERE "
					+ qDto.getString("queryclass")
					+ " and (_xh is null or _xh='')"
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ qDto.getPageLimit()
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER  "
					+ orderBy
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " WHERE "
					+ qDto.getString("queryclass")
					+ " and (_xh is null  or _xh='')"
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	/**
	 * 存入查询缓存
	 *
	 * @author PX
	 * @param qDto
	 * @param username2
	 *
	 *            2019-1-14
	 */
	private void addSearchData(Dto qDto, String username) {
		// TODO Auto-generated method stub
		// 得到当前时间毫秒
		// 查询用户中文名
		String chinese = getUserChinese(username);
		long nowtime = System.currentTimeMillis();
		SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String bh = dfDateFormat.format(new Date());
		for (int q = 1; q <= 8; q++) {
			String filedname = qDto.getString("filedname" + q);
			String filedcnname = qDto.getString("filedcnname" + q);
			String and = qDto.getString("and" + q);
			String content = qDto.getString("content" + q);
			String condition = qDto.getString("condition" + q);
			String sql = "insert into archive_Reseach_Data(bh,gx,zdmc,nr,tj,tablename,mk,cxr,mk_en) values ('"
					+ bh
					+ "','"
					+ and
					+ "','"
					+ filedname
					+ "','"
					+ content
					+ "','"
					+ condition
					+ "','"
					+ qDto.getString("tablename")
					+ "','"
					+ chinese
					+ "','"
					+ filedcnname
					+ "','"
					+ username
					+ "')";
			// 存入到数据库中
			jdbcTemplate.execute(sql);
		}
	}
	/**
	 * 存入查询缓存(子表)
	 *
	 * @author PX
	 * @param qDto
	 * @param username2
	 *
	 *            2019-1-14
	 */
	private void addSearchDataSon(Dto qDto, String username) {
		// TODO Auto-generated method stub
		// 得到当前时间毫秒
		// 查询用户中文名
		String chinese = getUserChinese(username);
		long nowtime = System.currentTimeMillis();
		SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String bh = dfDateFormat.format(new Date());
		for (int q = 1; q <= 8; q++) {
			String filedname = qDto.getString("filedname" + q);
			String filedcnname = qDto.getString("filedcnname" + q);
			String and = qDto.getString("and" + q);
			String content = qDto.getString("content" + q);
			String condition = qDto.getString("condition" + q);
			String sql = "insert into archive_Reseach_Data(bh,gx,zdmc,nr,tj,tablename,mk,cxr,mk_en) values ('"
					+ bh
					+ "','"
					+ and
					+ "','"
					+ filedname
					+ "','"
					+ content
					+ "','"
					+ condition
					+ "','"
					+ qDto.getString("tablenameson")
					+ "','"
					+ chinese
					+ "','"
					+ filedcnname
					+ "','"
					+ username
					+ "')";
			// 存入到数据库中
			jdbcTemplate.execute(sql);
		}
	}
	/**
	 * 得到中文登录名
	 *
	 * @author PX
	 * @param username2
	 *
	 *            2019-1-14
	 * @return
	 */
	private String getUserChinese(String username2) {
		// TODO Auto-generated method stub
		String sql = "select * from aos_sys_user where account_='" + username2
				+ "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			String name_ = (String) list.get(0).get("name_");
			return name_;
		}
		return null;
	}

	/**
	 *
	 * 获取排列条件(根据表名和用户名)
	 *
	 * @author PX
	 * @param qDto
	 *
	 *            2019-1-7
	 * @param username
	 * @return
	 */
	private String getOrderBy(Dto qDto, String username) {
		// TODO Auto-generated method stub
		String sql = "select * from archive_Sort_Data where tablename='"
				+ qDto.getString("tablename") + "' and cxr='" + username + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list.size() > 0) {
			return list.get(0).get("nr").toString();
		} else {
			return null;
		}
	}
	/**
	 *
	 * 获取排列条件(根据表名和用户名)(子表)
	 *
	 * @author PX
	 * @param qDto
	 *
	 *            2019-1-7
	 * @param username
	 * @return
	 */
	private String getOrderBySon(Dto qDto, String username) {
		// TODO Auto-generated method stub
		String sql = "select * from archive_Sort_Data where tablename='"
				+ qDto.getString("tablenameson") + "' and cxr='" + username + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list.size() > 0) {
			return list.get(0).get("nr").toString();
		} else {
			return null;
		}
	}

	/**
	 * 查找该用户全宗
	 *
	 * @author PX
	 * @param username2
	 *
	 *            2018-11-8
	 * @return
	 */
	private Map<String, Object> findGeneral(String username2) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select * from archive_general where create_person='"
				+ username2 + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			map = list.get(0);
		}
		return map;
	}

	/**
	 *
	 * 页面初始化
	 *
	 * @param tablename
	 * @return
	 */
	public List<Archive_tablefieldlistPO> getDataFieldListTitle(String tablename) {
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(tablename);
		return list;
	}

	/**
	 *
	 *
	 *
	 * @author Sun
	 * @param tablename
	 * @return
	 *
	 *         2018-8-14
	 */
	public List<Archive_tablefieldlistPO> getInfoFieldListTitle(String tablename) {
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getInfoFieldDisplayAll(tablename);
		return list;
	}

	/**getDataFieldDisplayAll
	 * 功能：转换MapList为数组List
	 *
	 * @param list
	 * @return
	 */
	public static List convertMapListToArrayList(List list) {
		Map map = null;
		/*
		 * Dto qDto = Dtos.newDto(); if (list != null) { Iterator iterator =
		 * list.iterator(); while (iterator.hasNext()) { map = (Map)
		 * iterator.next(); Iterator<?> keyIt = map.keySet().iterator(); while
		 * (keyIt.hasNext()) { String key = (String) keyIt.next(); String value
		 * = ((Object) map.get(key)) == null ? "" : ((Object)
		 * map.get(key)).toString(); qDto.put(key, value); } } }
		 */

		return list;
	}

	/**
	 * 功能：转换MapList为Dto
	 *
	 * @param list
	 * @return
	 */
	public static Dto convertMapListToDto(List list) {
		Map map = null;
		Dto qDto = Dtos.newDto();
		if (list != null) {
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				map = (Map) iterator.next();
				Iterator<?> keyIt = map.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = (String) keyIt.next();
					String value = ((Object) map.get(key)) == null ? ""
							: ((Object) map.get(key)).toString();
					qDto.put(key, value);
				}
			}
		}

		return qDto;
	}

	/**
	 *
	 * 查询记录总数
	 *
	 * @param tablename
	 * @return
	 */
	public int getPageTotal(Dto qDto) {
		if(qDto.getString("tablename")==null||qDto.getString("tablename")==""){
			return 0;
		}
		String query = queryConditions2(qDto);
		String queryclass=qDto.getString("queryclass");
		if(queryclass!=null&&queryclass!=""){
			query=" and "+queryclass+" "+query;
		}
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
				+ " WHERE 1=1 " + query;
		return jdbcTemplate.queryForInt(sql);
	}
	/**
	 *
	 * 查询记录总数
	 *
	 * @param tablename
	 * @return
	 */
	public int getPageTotal_backup(Dto qDto) {
		String query = queryConditions2(qDto);
		String queryclass=qDto.getString("queryclass");
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
				+ " WHERE 1=1 " + query;
		return jdbcTemplate.queryForInt(sql);
	}
	/**
	 *
	 * 查询记录总数(子表)
	 *
	 * @param tablename
	 * @return
	 */
	public int getPageTotalSon(Dto qDto) {
		String query = queryConditions2(qDto);
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablenameson")
				+ " WHERE 1=1 " + query;
		return jdbcTemplate.queryForInt(sql);
	}
	public Object getData(Dto qDto) {
		String tablename = qDto.getString("tablename");
		String id = qDto.getString("id_");
		String sql = "SELECT * FROM " + tablename + " WHERE id_='" + id + "'";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto.get(0);
	}
	public Dto getupperLowerData(Dto qDto) {
		String tablename = qDto.getString("tablename");
		String tablenameson = qDto.getString("tablenameson");
		String id_=qDto.getString("id_");
		//查询关联关系
		Dto dto=getSonValue(tablename,tablenameson,id_);
		return dto;
	}
	//子表查询录入信息
	public Object getDataSon(Dto qDto) {
		String tablename = qDto.getString("tablename_z");
		String id = qDto.getString("id_");
		String sql = "SELECT * FROM " + tablename + " WHERE id_='" + id + "'";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto.get(0);
	}

	/**
	 *
	 * 获得电子文件信息
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getPath(Dto qDto) {
		String tablename = qDto.getString("tablename") + "_path";
		String tid = qDto.getString("tid");
		String sql = " SELECT id_,tid,_path,dirname,sdatetime,_s_path ,RIGHT(RTRIM(_Path), CHARINDEX('.',REVERSE(RTRIM(_path))) - 1) as filetype FROM "
				+ tablename + " WHERE tid ='" + tid + "'";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;
	}

	/**
	 *
	 * 获得全部电子文件信息
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getAllPath(Dto qDto) {
		String tablename = qDto.getString("tablename") + "_path";
		// String tid = qDto.getString("tid");
		// String sql = " SELECT dirname+_s_path as path,tid From " + tablename;
		String sql = "SELECT b.id_,dirname+_s_path as path,tid,b.tm From "
				+ tablename + " a  " + "left join "
				+ qDto.getString("tablename") + " b on a.tid=b.id_";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;
	}

	/**
	 *
	 * 电子文件路径
	 *
	 * @param qDto
	 * @return
	 */
	public String getDocumentPath(Dto qDto) {
		// dirname+_s_path 文件路径
		String tablename = qDto.getString("tablename") + "_path";
		String pid = qDto.getString("id");
		String sql = " SELECT dirname+_s_path as path From " + tablename
				+ " WHERE id_='" + pid + "'";
		/*
		 * if(tablename.equals("ctda_path")){
		 *
		 * sql="  SELECT '" + qDto.getString("base") +
		 * "'+'/'+dirname+'/'+_path as path From " + tablename + " WHERE id_='"
		 * + pid + "'"; }if(tablename.equals("zpda_path")){
		 *
		 * sql="  SELECT '" + qDto.getString("base") +
		 * "'+'/'+dirname+'/'+_s_path as path From " + tablename +
		 * " WHERE id_='" + pid + "'"; }
		 */

		List<Map<String, Object>> listDto = jdbcTemplate.queryForList(sql);
		String path = listDto.get(0).get("path").toString();
		// String path=" ";
		return path;
	}

	/**
	 *
	 * 删除条目
	 *
	 * @param qDto
	 * @return
	 */
	public Dto deleteData(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		String tablename = qDto.getString("tablename");
		int del = 0;
		for (String id_ : selections) {
			jdbcTemplate.execute(" delete from " + tablename + " where id_='"
					+ id_ + "'");
			del++;
		}
		String msg = "操作完成，";
		if (del > 0) {
			msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
		}
		outDto.setAppMsg(msg);
		return outDto;

	}
	/**
	 *
	 * 删除条目
	 *
	 * @param qDto
	 * @return
	 */
	public Dto deleteData_thorough(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		String tablename = qDto.getString("tablename");
		int del = 0;
		for (String id_ : selections) {
			jdbcTemplate.execute(" delete from " + tablename + "_backup where id_='"
					+ id_ + "'");
			del++;
		}
		String msg = "操作完成，";
		if (del > 0) {
			msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
		}
		outDto.setAppMsg(msg);
		return outDto;

	}
	/**
	 *
	 * 删除条目
	 *
	 * @param qDto
	 * @return
	 */
	public Dto deleteData_back(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		String tablename = qDto.getString("tablename");
		int del = 0;
		for (String id_ : selections) {
			String sql3="insert into "+qDto.getString("tablename")+"_backup select * from "+qDto.getString("tablename")+" where id_='"+id_+"'";
			jdbcTemplate.execute(sql3);
			jdbcTemplate.execute(" delete from " + tablename + " where id_='"
					+ id_ + "'");
			del++;
		}
		String msg = "操作完成，";
		if (del > 0) {
			msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
		}
		outDto.setAppMsg(msg);
		return outDto;

	}

	/**
	 *
	 * 删除单个电子文件
	 *
	 * @param qDto
	 * @return
	 */
	public Dto deletePath(Dto qDto) {
		Dto outDto = Dtos.newDto();
		Dto dto = Dtos.newDto();
		dto.put("tablename", qDto.getString("tablename"));
		int del = 1;
		String id = qDto.getString("id_");
		String tablename = qDto.getString("tablename") + "_path";
		String tableinfo = qDto.getString("tablename") + "_info";
		jdbcTemplate.execute(" delete from " + tablename + " where id_='" + id
				+ "'");
		Archive_tablenamePO archive_tablenamePO = archive_tablenameMapper
				.selectOne(dto);
		if (archive_tablenamePO.getTablemedia() != null
				&& archive_tablenamePO.getTablemedia() != "") {
			jdbcTemplate.execute(" delete from " + tableinfo + " where id_='"
					+ id + "'");
		}
		String msg = AOSUtils.merge("操作完成，成功删除信息[{0}]个", del);
		outDto.setAppMsg(msg);
		return outDto;
	}

	/**
	 *
	 * 删除全部电子文件
	 *
	 * @param qDto
	 * @return
	 */
	public Dto deletePathAll(Dto qDto) {
		Dto outDto = Dtos.newDto();
		int del = 0;
		String[] selections = qDto.getRows();
		String tablename = qDto.getString("tablename") + "_path";
		String tableinfo = qDto.getString("tablename") + "_info";
		Dto dto = Dtos.newDto();
		dto.put("tablename", qDto.getString("tablename"));
		for (String id : selections) {
			jdbcTemplate.execute(" DELETE FROM " + tablename + " WHERE tid='"
					+ id + "'");
			Archive_tablenamePO archive_tablenamePO = archive_tablenameMapper
					.selectOne(dto);
			if (archive_tablenamePO.getTablemedia() != null
					&& archive_tablenamePO.getTablemedia() != "") {
				jdbcTemplate.execute(" DELETE FROM " + tableinfo
						+ " WHERE tid='" + id + "'");
			}

			jdbcTemplate.execute(" UPDATE  " + qDto.getString("tablename")
					+ " SET _path='0' WHERE id_='" + id + "'");
			del++;
		}
		String msg = AOSUtils.merge("操作完成，成功删除", del);
		outDto.setAppMsg(msg);
		return outDto;
	}

	/**
	 *
	 * 更新电子文件个数
	 *
	 * @param qDto
	 * @return
	 */
	public void refreshPath(Dto qDto) {
		String tablename = qDto.getString("tablename");
		String tid = qDto.getString("tid");
		String sql = " UPDATE " + tablename
				+ " set _path =(select count(id_) from " + tablename
				+ "_path where tid='" + tid + "' ) where id_='" + tid + "'";
		jdbcTemplate.execute(sql);
	}

	/**
	 * 下载电子文件
	 *
	 * @param dto
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void download_electronic_file(String filePath, Dto dto,
										 HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		// TODO Auto-generated method stub
		String filename = null;
		String filepath = null;
		String _path = null;
		// 查询文件路径信息
		List<Map<String, Object>> list = find_pathmessage(dto.getString("id_"),
				dto.getString("tablename"));
		if (list != null && list.size() > 0) {
			filename = (String) list.get(0).get("dirname");
			filepath = (String) list.get(0).get("_s_path");
			_path = (String) list.get(0).get("_path");
			response.setContentType("application/x-" + filepath.split("\\.")[1]
					+ ";charset=gb2312");
			// 名称点上后缀
			response.addHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(_path, "utf-8"));
			OutputStream outputStream = response.getOutputStream();
			try {
				FileInputStream fileInputStream = new FileInputStream(new File(
						filePath

								+ File.separator + filename + filepath));
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = fileInputStream.read(b)) != -1) {
					outputStream.write(b, 0, len);
					outputStream.flush();
				}
			} catch (Exception e) {
				throw new AOSException(e);
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException ex) {

					}
				}
			}
		}
	}

	/**
	 * 根据id查询文件详细信息
	 *
	 * @param id
	 * @param tablename
	 * @return
	 */
	public List<Map<String, Object>> find_pathmessage(String id,
													  String tablename) {
		// TODO Auto-generated method stub
		String sql = "select*from " + tablename + "_path " + " where id_='"
				+ id + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	/**
	 *
	 * 保存信息
	 *
	 * @param qDto
	 * @return
	 */
	public Dto saveData(Dto qDto) {
		Dto outDto = Dtos.newDto();
		// Map map = new HashMap();
		String columns = "id_";
		// String _lrr=outDto.getUserInfo().getName_();
		// String _lrrq="";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String date = df.format(new Date());
		String values = "";
		Iterator iter = qDto.entrySet().iterator(); // 获取key和value的set
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next(); // 把hashmap转成Iterator再迭代到entry
			Object key = entry.getKey(); // 从entry获取keyObject
			if (key.equals("app") || key.equals("tablename")
					|| key.equals("_userInfoVO")) {
				continue;
			}
			columns = columns + "," + key;
			Object val = "'" + entry.getValue() + "'"; // 从entry获取value}
			values = values + "," + val;
		}
		String sql = " INSERT INTO " + qDto.getString("tablename") + " ("
				+ columns + ",_lrr,_lrrq) VALUES ('" + AOSId.uuid() + "'"
				+ values + ",'" + qDto.getUserInfo().getName_() + "','" + date
				+ "')";
		jdbcTemplate.execute(sql);

		// List<Archive_tableinputPO> list = archive_tableinputMapper.
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，加存成功。");
		return outDto;
	}

	public Dto updateData(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String columns = "id_";
		String strsql = "";
		Iterator iter = qDto.entrySet().iterator(); // 获取key和value的set
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next(); // 把hashmap转成Iterator再迭代到entry
			Object key = entry.getKey(); // 从entry获取keyObject
			if (key.equals("app") || key.equals("tablename")
					|| key.equals("_userInfoVO") || key.equals("id")) {
				continue;
			}
			Object val = "'" + entry.getValue() + "'"; // 从entry获取value}
			// strsql=key+"="+val+"";
			if (strsql.equals("")) {
				strsql = key + "=" + val + "";
			} else
				strsql = strsql + "," + key + "=" + val + "";
		}
		String sql = " UPDATE " + qDto.getString("tablename") + " SET "
				+ strsql + " WHERE id_='" + qDto.getString("id") + "'";
		jdbcTemplate.execute(sql);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，保存成功。");
		return outDto;
	}

	/**
	 *
	 * 查询条件拼接
	 *
	 * @param qDto
	 * @return
	 */
	public String queryConditions(Dto qDto) {

		if (qDto.getString("columnnum") != "") {
			int contentLength = Integer.parseInt(qDto.getString("columnnum"));
			StringBuffer query = new StringBuffer();
			for (int i = 1; i <= contentLength + 1; i++) {
				if (qDto.getString("content" + i) != null
						&& qDto.getString("content" + i) != "") {

					if (qDto.getString("and" + i).equals("on")) {
						query.append(" and " + qDto.getString("filedname" + i));
					}
					if (qDto.getString("or" + i).equals("on")) {
						query.append(" or " + qDto.getString("filedname" + i)
								+ " " + qDto.getString("condition" + i));
					}
					if (qDto.getString("condition" + i).equals("like")) {
						query.append(" " + qDto.getString("condition" + i)
								+ " '%" + qDto.getString("content" + i) + "%'");
					}
					if (qDto.getString("condition" + i).equals("left")) {
						query.append(" like '%" + qDto.getString("content" + i)
								+ "'");
					}
					if (qDto.getString("condition" + i).equals("right")) {
						query.append(" like '" + qDto.getString("content" + i)
								+ "%'");
					}
					if (qDto.getString("condition" + i).equals("<>")) {
						query.append(" <>'" + qDto.getString("content" + i)
								+ "'");
					}
					if (qDto.getString("condition" + i).equals(">")) {
						query.append(" >'" + qDto.getString("content" + i)+ "'");
					}
					if (qDto.getString("condition" + i).equals("<")) {
						query.append(" <'" + qDto.getString("content" + i)+ "'");
					}
					if (qDto.getString("condition" + i).equals("<=")) {
						query.append(" <='" + qDto.getString("content" + i)+ "'");
					}
					if (qDto.getString("condition" + i).equals(">=")) {
						query.append(" >='" + qDto.getString("content" + i)+ "'");
					}
					if (qDto.getString("condition" + i).equals("=")) {
						query.append(" = '" + qDto.getString("content" + i)
								+ "'");
					}

				}

			}
			return query.toString();
		} else
			return "";

	}
	/**
	 *
	 * 查询条件拼接
	 *
	 * @param qDto
	 * @return
	 */
	public String queryConditions2(Dto qDto) {

		if (qDto.getString("columnnum") != "") {
			int contentLength = Integer.parseInt(qDto.getString("columnnum"));
			StringBuffer query = new StringBuffer();
			for (int i = 1; i <= contentLength + 1; i++) {
				if (qDto.getString("content" + i) != null
						&& qDto.getString("content" + i) != "") {

					if (qDto.getString("and" + i).equals("on")) {
						query.append(" and " + qDto.getString("filedname" + i));
					} else {
						query.append(" or " + qDto.getString("filedname" + i)
								+ " ");
					}
					if (qDto.getString("condition" + i).equals("like")) {
						query.append(" " + qDto.getString("condition" + i)
								+ " '%" + qDto.getString("content" + i) + "%'");
					}
					if (qDto.getString("condition" + i).equals("left")) {
						query.append(" like '" + qDto.getString("content" + i)
								+ "%'");
					}
					if (qDto.getString("condition" + i).equals("right")) {
						query.append(" like '%" + qDto.getString("content" + i)
								+ "'");
					}
					if (qDto.getString("condition" + i).equals("<>")) {
						query.append(" <> '" + qDto.getString("content" + i)
								+ "'");
					}
					if (qDto.getString("condition" + i).equals(">")) {
						query.append(" > " + qDto.getString("content" + i));
					}
					if (qDto.getString("condition" + i).equals("<")) {
						query.append(" < " + qDto.getString("content" + i));
					}
					if (qDto.getString("condition" + i).equals("<=")) {
						query.append(" <= " + qDto.getString("content" + i));
					}
					if (qDto.getString("condition" + i).equals(">=")) {
						query.append(" >= " + qDto.getString("content" + i));
					}
					if (qDto.getString("condition" + i).equals("=")) {
						query.append(" = '" + qDto.getString("content" + i)
								+ "'");
					}

				}

			}
			return query.toString();
		} else
			return "";

	}

	/**
	 *
	 * 记录更新
	 *
	 * @param qDto
	 * @return
	 */
	public Dto updateRecord(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String sql = " update " + qDto.getString("tablename") + " set "
				+ qDto.getString("filedname") + " ='"
				+ qDto.getString("update_content") + "' WHERE 1=1 "
				+ qDto.getString("query");
		int count = jdbcTemplate.update(sql);
		String msg = AOSUtils.merge("操作完成，成功更新[{0}]条记录", count);
		outDto.setAppMsg(msg);
		return outDto;
	}

	/**
	 *
	 * 记录替换
	 *
	 * @param qDto
	 * @return
	 */
	public Dto replaceRecord(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String sql = " UPDATE " + qDto.getString("tablename") + " SET "
				+ qDto.getString("filedname") + " =REPLACE("
				+ qDto.getString("filedname") + ",'"
				+ qDto.getString("replace_source") + "','"
				+ qDto.getString("replace_new") + "') WHERE 1=1 "
				+ qDto.getString("query");
		int count = jdbcTemplate.update(sql);
		String msg = AOSUtils.merge("操作完成，成功替换[{0}]条记录", count);
		outDto.setAppMsg(msg);
		return outDto;
	}

	/**
	 *
	 * 前后辍
	 *
	 * @param qDto
	 * @return
	 */
	public Dto suffixRecord(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String sql = " UPDATE " + qDto.getString("tablename") + " SET "
				+ qDto.getString("filedname") + "=";
		if (qDto.getString("suffix_front") != "") {
			sql = sql + "'" + qDto.getString("suffix_front") + "' +";
		}
		sql = sql + qDto.getString("filedname");
		if (qDto.getString("suffix_after") != "") {
			sql = sql + "+'" + qDto.getString("suffix_after") + "'";
		}
		sql = sql + " WHERE 1=1 " + qDto.getString("query");
		int count = jdbcTemplate.update(sql);
		String msg = AOSUtils.merge("操作完成，成功替换[{0}]条记录", count);
		outDto.setAppMsg(msg);
		return outDto;
	}

	/**
	 *
	 * 补位
	 *
	 * @param qDto
	 * @return
	 */
	public Dto repairRecord(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String sql = " UPDATE " + qDto.getString("tablename") + " SET "
				+ qDto.getString("filedname") + "=RIGHT('"
				+ qDto.getString("repair_value") + "' + "
				+ qDto.getString("filedname") + ","
				+ qDto.getString("repair_long") + ") WHERE 1=1 "
				+ qDto.getString("query");
		int count = jdbcTemplate.update(sql);
		String msg = AOSUtils.merge("操作完成，成功替换[{0}]条记录", count);
		outDto.setAppMsg(msg);
		return outDto;
	}

	/**
	 *
	 * 导出EXCEL
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> exportData(Dto qDto) {
		// String query = queryConditions(qDto);
		String fieldName;
		String enfield = "";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablename"));
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			if (enfield.equals("")) {
				enfield = fieldName;
				continue;
			}
			enfield = enfield + "," + fieldName;
		}
		String query=" and _classtree='" +qDto.getString("_classtree")+"' "+qDto.getString("query");
		String sql = "WITH aos_query_ AS (SELECT " + enfield + " FROM "
				+ qDto.getString("tablename") + " WHERE 1=1 "
				+ query + " ) " + "SELECT * FROM aos_query_ ";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;

	}
	/**
	 *
	 * 导出EXCEL
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> exportData_backup(Dto qDto) {
		// String query = queryConditions(qDto);
		String fieldName;
		String enfield = "";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablename"));
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			if (enfield.equals("")) {
				enfield = fieldName;
				continue;
			}
			enfield = enfield + "," + fieldName;
		}
		String query="  "+qDto.getString("query");
		String sql = "WITH aos_query_ AS (SELECT " + enfield + " FROM "
				+ qDto.getString("tablename") + " WHERE 1=1 "
				+ query + " ) " + "SELECT * FROM aos_query_ ";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;

	}
	/**
	 *
	 * 导出EXCEL
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> exportDatagd(Dto qDto) {
		// String query = queryConditions(qDto);

		String sql = "WITH aos_query_ AS (SELECT " + qDto.getString("enfield")
				+ " FROM " + qDto.getString("tablename") + " WHERE 1=1 "
				+ qDto.getString("query") + " ) " + "SELECT * FROM aos_query_ ";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;

	}

	/**
	 *
	 * 查询单条数据
	 *
	 * @param id
	 * @param tablename
	 * @return
	 */
	public Dto selectOne(String id, String tablename) {
		Dto qDto = Dtos.newDto();
		// String tablename = qDto.getString("tablename");
		// String id = qDto.getString("id_");
		String sql = "SELECT * FROM " + tablename + " WHERE id_='" + id + "'";
		qDto = convertMapListToDto(jdbcTemplate.queryForList(sql));
		return qDto;
	}

	/**
	 *
	 * 判断是不是全部
	 *
	 * @param pDto
	 * @return
	 */

	public String isAll(Dto pDto) {
		Dto qDto = Dtos.newDto();
		String sql = "SELECT * FROM aos_sys_module  WHERE cascade_id_='"
				+ pDto.getString("cascode_id_") + "'";
		qDto = convertMapListToDto(jdbcTemplate.queryForList(sql));
		String queryclass = " 1=1";
		if (!qDto.getString("name_").equals("全部")) {
			queryclass = " _classtree='" + pDto.getString("cascode_id_") + "'";
		}
		return queryclass;
	}

	/**
	 *
	 * 显示档案元数据
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataExifs(Dto qDto) {
		String query = queryConditions2(qDto);
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getInfoFieldDisplayAll(qDto.getString("tablename") + "_info");
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
		}

		String sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY CURRENT_TIMESTAMP) AS aos_rn_,"
				+ enfield
				+ " FROM "
				+ qDto.getString("tablename")
				+ "_info"
				+ " WHERE tid='"
				+ qDto.getString("id")
				+ "'"
				+ " ) "
				+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	/**
	 *
	 * 显示文书档案元数据
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataWsdaExifs(Dto qDto) {
		String query = queryConditions2(qDto);
		// String fieldName;
		String enfield = "id_,xh,tid,intw,inth,dpi,fsize,paper,sckj,ysl,sblx,sbzzs,sbxh,sbxlh,sbggq,szhrjmc,szhrjbb,szhrjscs,szhsj,szhms,szhgs";
		String sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY CURRENT_TIMESTAMP) AS aos_rn_,"
				+ enfield
				+ " FROM "
				+ qDto.getString("tablename")
				+ "_info"
				+ " WHERE tid in( select tid from "
				+ qDto.getString("tablename")
				+ "_path where id_='"
				+ qDto.getString("id")
				+ "' "
				+ " )) "
				+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
				+ qDto.getPageStart()
				+ " AND "
				+ qDto.getPageLimit()
				* Integer.valueOf(qDto.getString("page")) + " ORDER BY aos_rn_";
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	/**
	 *
	 * 设置显示行数
	 *
	 * @param qDto
	 * @return
	 */
	public Dto setPagesize(Dto inDto) {
		Dto outDto = Dtos.newDto();

		Aos_sys_userPO aos_sys_user = new Aos_sys_userPO();

		AOSUtils.copyProperties(inDto, aos_sys_user);

		aos_sys_userMapper.updateByKey(aos_sys_user);

		String msg = AOSUtils.merge("操作完成，请重新登录！！！");
		outDto.setAppMsg(msg);
		return outDto;
	}

	// 开启事务
	public static void connOpen() throws SQLException {
		connection = DriverManager.getConnection(url, username, password);
	}

	// 关闭事务
	public static void connClose() throws SQLException {
		if (ps != null) {
			ps.close();
			ps = null;
		}
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	/**
	 * 根据表英文名查询属性信息
	 *
	 * @param dto
	 * @return
	 */
	public Archive_tablenamePO findtablenameid(Dto dto) {
		// TODO Auto-generated method stub
		Archive_tablenamePO tablenamePO = archive_tablenameMapper
				.selectOne(dto);
		return tablenamePO;
	}

	/**
	 * 根据tid和FieldCnName查询到FieldEnName
	 *
	 * @param id_
	 * @param string
	 */
	private String findenname(String id_, String string) {
		// TODO Auto-generated method stub
		String sql = "select * from archive_TableFieldList where tid='" + id_
				+ "' and FieldCnName='" + string + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			return list.get(0).get("FieldEnName").toString();
		}
		return null;
	}

	/**
	 * 查询鉴定数据
	 *
	 * @param outDto
	 */
	public List<Map<String, Object>> findSj(Dto outDto) {
		// TODO Auto-generated method stub
		String sql = "select * from " + outDto.getString("tablename")
				+ " where 1=1  " + outDto.getString("querySession");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List<Map<String, Object>> appraisallist = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		sdf.format(date);
		// 当前年度
		Integer newnd = Integer.valueOf(sdf.format(date));
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				// 获得成文日期
				Object cwrq = map.get("cwrq");
				if (cwrq == null || cwrq == "") {
					continue;
				}
				String cwrq2 = cwrq.toString();
				Integer cwrq3 = Integer.valueOf(cwrq2);

				// 保管期限
				Object bgqx = map.get("bgqx");
				if (bgqx == null || bgqx == "") {
					continue;
				}
				String bgqx2 = bgqx.toString();
				Integer bgqx3 = Integer.valueOf(bgqx2);
				if (bgqx2.endsWith("Y")) {
					continue;
				}
				if ((newnd - Integer.valueOf(cwrq2) > Integer.valueOf(bgqx2))
						&& !bgqx2.endsWith("Y")) {
					appraisallist.add(map);
					continue;
				}
			}
		}
		return appraisallist;
	}

	/**
	 * 修改标记
	 *
	 * @param outDto
	 */
	public boolean updateappraisal(Dto outDto) {
		// TODO Auto-generated method stub
		try {
			String sql = "update " + outDto.getString("tablename")
					+ " set zzjjg='1' where id_='"
					+ outDto.getString("aos_rows_").split(",")[0] + "'";
			int update = jdbcTemplate.update(sql);
			if (update == 1) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据条件删除
	 *
	 * @param outDto
	 */
	public Dto deleteTermData(Dto outDto) {
		// TODO Auto-generated method stub
		Dto out = Dtos.newDto();
		int d = 0;
		try {
			String conditions = queryConditions2(outDto);
			String sql = "SELECT COUNT(*) FROM "
					+ outDto.getString("tablename") + " WHERE 1=1 "
					+ conditions;
			d = jdbcTemplate.queryForInt(sql);
			String sql2 = "delete from " + outDto.getString("tablename")
					+ " where 1=1 " + conditions;
			jdbcTemplate.execute(sql2);
			String msg = "操作完成,";
			if (d > 0) {
				msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", d);
			}
			out.setAppMsg(msg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return out;
	}

	/**
	 * 进行文件夹和移交电子文件的创建
	 *
	 * @param path
	 * @param fieldDtos
	 * @param outDto
	 * @throws Exception
	 */
	public void createfiletransfer(String path, String filepath,
								   List<Dto> fieldDtos, Dto outDto) {
		// TODO Auto-generated method stub
		// 比如是一个固定的文件夹
		try {
			String tablename = outDto.getString("tablename");
			String transferpath = path;
			String filedname = outDto.getString("filedname");
			if (fieldDtos != null && fieldDtos.size() > 0) {
				for (int i = 0; i < fieldDtos.size(); i++) {
					Map<String, Object> fieldmap = fieldDtos.get(i);
					// 根据当前条目进行电子文件路径的查询，执行读写操作
					String id_ = (String) fieldmap.get("id_");
					// 得到移交文件夹名称
					String transferfile = (String) fieldmap.get(filedname);
					//
					if (transferfile == null || transferfile == "") {
						continue;
					}
					// 根据id_查询到对应电子文件的完整路径
					List<Map<String, Object>> listpath = find_path(id_,
							tablename);
					if (listpath != null && listpath.size() > 0) {
						for (int t = 0; t < listpath.size(); t++) {
							Map<String, Object> map = listpath.get(t);
							String dirname = (String) map.get("dirname");
							String _s_path = (String) map.get("_s_path");
							// 拼接上传路径
							String splituploadpath = filepath + tablename
									+ "\\" + dirname + _s_path;
							// 拼接移交路径
							String splittransferpath = transferpath
									+ File.separator + tablename
									+ File.separator + transferfile;
							// 执行读写操作
							ReadWriterfilename(splituploadpath,
									splittransferpath);
						}
					} else {
						// 此时不存在电子文件，
						// 创建空文件加
						File f = new File(transferpath + transferfile);
						if (!f.exists()) {
							f.mkdirs();
						}
						continue;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 移交文件读写操作
	 *
	 * @param splituploadpath
	 * @param splittransferpath
	 * @throws Exception
	 */
	private void ReadWriterfilename(String splituploadpath,
									String splittransferpath) {
		// TODO Auto-generated method stub
		try {
			// 完整上传路径
			String[] split = splituploadpath.split("\\.");
			String suffix = splituploadpath.split("\\.")[split.length - 1];
			// wsda\003feb45e18041e6b15f91827a705f00\2aba031077644ab58bcf51cb2ede9165.jpg
			// D:\移交\1
			// 得到文件名称
			String filename = splituploadpath.split("\\\\")[splituploadpath
					.split("\\\\").length - 1];
			// 完整下载路径
			String allsplituploadpath = splituploadpath;
			String allsplittransferpath = splittransferpath + File.separator
					+ filename;
			// 源文件
			// 目的文件
			// 写入文件
			File f = new File(allsplituploadpath);
			File s = new File(splittransferpath);
			File w = new File(allsplittransferpath);
			InputStream in = null;
			OutputStream out = null;
			try {
				if (!s.exists()) {
					s.mkdirs();
				}
				if (!w.exists()) {
					w.createNewFile();
				}
				in = new FileInputStream(f);
				out = new FileOutputStream(w, true);
				byte[] temp = new byte[1024];
				int length = 0;
				while ((length = in.read(temp)) != -1) {
					out.write(temp, 0, length);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				try {
					in.close();
					out.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 查询电子文件路径集合
	 *
	 * @param id_
	 * @param tablename
	 */
	private List<Map<String, Object>> find_path(String id_, String tablename) {
		// TODO Auto-generated method stub
		String sql = "select * from " + tablename + "_path where tid='" + id_
				+ "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	public List<Dto> exportTransfer(Dto outDto) {
		// TODO Auto-generated method stub
		String fieldName;
		String enfield = "";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(outDto.getString("tablename"));
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			if (enfield.equals("")) {
				enfield = fieldName;
				continue;
			}
			enfield = enfield + "," + fieldName;
		}
		String sql = "WITH aos_query_ AS (SELECT " + enfield + " FROM "
				+ outDto.getString("tablename") + " WHERE 1=1 "
				+ outDto.getString("query") + " and "
				+ outDto.getString("queryclass") + ") "
				+ "SELECT * FROM aos_query_ ";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;

	}

	/**
	 * 弹出文件夹窗口选择
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public String transferfilepathData(HttpServletRequest request,
									   HttpServletResponse response, HttpSession session) {
		// TODO Auto-generated method stub
		JFileChooser fileChooser = new JFileChooser("D:\\");

		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = fileChooser.showOpenDialog(fileChooser);

		if (returnVal == JFileChooser.APPROVE_OPTION)

		{
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();// 这个就是你选择的文件夹的
			return filePath;
		}
		return null;
	}

	public Dto transferfile(Dto outDto, String path, String filepath,
							HttpServletRequest request, HttpServletResponse response,
							HttpSession session) {
		// TODO Auto-generated method stub
		// 查询到所有数据(得到指定分类下面的所有分类信息)
		Dto out = Dtos.newDto();
		List fieldDtos = getDataFieldListDisplayAll(outDto, "");
		// 通过选泽的文件夹名字段，进行执行路径进行文件名的创建
		// 电子文件路径创建\
		try {
			createfiletransfer(path, filepath, fieldDtos, outDto);
			// 添加日志
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
					userInfoVO.getAccount_(),
					"移交电子文件[" + outDto.getString("tablename") + "]", "", "移交",
					request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			logMapper.insert(logPO);
			//
			out.setAppCode(1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			out.setAppCode(-1);
		}
		return out;
	}

	/**
	 * 移交电子条目
	 *
	 * @param outDto
	 * @param path
	 * @param session
	 * @param response
	 * @param request
	 */
	public void transferReport(Dto outDto, String path,
							   HttpServletRequest request, HttpServletResponse response,
							   HttpSession session) {
		// TODO Auto-generated method stub
		try {
			outDto.put("query",
					request.getSession().getAttribute("querySession"));
			List<Dto> list = exportTransfer(outDto);
			List<Archive_tablefieldlistPO> titleDtos = getDataFieldListTitle(outDto
					.getString("tablename"));
			// 组装报表数据模型
			AOSReportModel reportModel = new AOSReportModel();
			reportModel.setFileName(outDto.getString("tablename"));
			// 设置报表集合
			reportModel.setFieldsList(list);
			reportModel.setExcelHeader(titleDtos);
			// 填充报表
			session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
			// 添加日志
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
					userInfoVO.getAccount_(),
					"移交条目[" + outDto.getString("tablename") + "]", "", "移交条目",
					request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			logMapper.insert(logPO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 移交电子条目
	 *
	 */
	public List<Archive_tablefieldlistPO> getExcelTitle(Dto qDto) {
		// TODO Auto-generated method stub
		// String query = queryConditions(qDto);

		String sql = "WITH aos_query_ AS (SELECT " + qDto.getString("enfield")
				+ " FROM " + qDto.getString("tablename") + " WHERE 1=1 "
				+ qDto.getString("query") + " ) " + "SELECT * FROM aos_query_ ";
		List<Archive_tablefieldlistPO> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;
	}

	public Dto deleteAllData(Dto qDto) {
		// TODO Auto-generated method stub
		Dto outDto = Dtos.newDto();
		String tablename = qDto.getString("tablename");
		// 查询条目数
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select * from " + tablename + " where 1=1 "
						+ qDto.getString("query"));
		try {
			jdbcTemplate.execute(" delete from " + tablename + " where 1=1  "
					+ qDto.getString("query"));
			String msg = "操作完成,";
			msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", list.size());
			outDto.setAppMsg(msg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.setAppMsg("删除失败!");
		}
		return outDto;
	}
	public Dto deleteAllData_back(Dto qDto) {
		// TODO Auto-generated method stub
		Dto outDto = Dtos.newDto();
		String tablename = qDto.getString("tablename");
		// 查询条目数
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select * from " + tablename + " where 1=1 "
						+ qDto.getString("query"));
		try {
			//
			String sql3="insert into "+qDto.getString("tablename")+"_backup select * from "+qDto.getString("tablename")+" where 1=1  "+ qDto.getString("query");
			jdbcTemplate.execute(sql3);
			jdbcTemplate.execute(" delete from " + tablename + " where 1=1  "
					+ qDto.getString("query"));
			String msg = "操作完成,";
			msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", list.size());
			outDto.setAppMsg(msg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.setAppMsg("删除失败!");
		}
		return outDto;
	}
	/**
	 * 鉴定档案是否过期
	 *
	 * @author PX
	 * @param qDto
	 *
	 *            2018-9-29
	 * @return
	 */
	public int jdappraisal(Dto qDto) {
		// TODO Auto-generated method stub
		// 根据门类名称，判断是否存在年度和保管期限，如果不存在直接跳过，不做鉴定
		String tablename = qDto.getString("tablename");
		String sql = "select * from archive_TableFieldList where tid in(select id_ from archive_TableName where TableName='"
				+ tablename + "') and FieldEnName='nd'";
		String sql2 = "select * from archive_TableFieldList where tid in(select id_ from archive_TableName where TableName='"
				+ tablename + "') and FieldEnName='bgqx'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
		if (list != null && list.size() > 0 && list2 != null
				&& list2.size() > 0) {
			if (list.size() >= 1 && list2.size() >= 1) {
				String aos_rows_id = qDto.getString("aos_rows_").split(",")[0];
				String sql3 = "select nd,bgqx from " + tablename
						+ " where id_='" + aos_rows_id + "'";
				List<Map<String, Object>> list3 = jdbcTemplate
						.queryForList(sql3);
				if (list3 != null && list3.size() > 0) {
					String nd = (String) list3.get(0).get("nd");
					String bgqx = (String) list3.get(0).get("bgqx");
					if (nd == null || nd == "" || bgqx == null || bgqx == "") {
						return -1;
					}
					if (bgqx.equals("D30")) {
						// 得到当前年
						String nownd = new SimpleDateFormat("yyyy")
								.format(new Date());
						if (Integer.valueOf(nd) + 30 > Integer.valueOf(nownd)) {
							return 0;
						} else {
							return 1;
						}
					}
					if (bgqx.equals("D10")) {
						// 得到当前年
						String nownd = new SimpleDateFormat("yyyy")
								.format(new Date());
						if (Integer.valueOf(nd) + 10 > Integer.valueOf(nownd)) {
							return 0;
						} else {
							return 1;
						}
					}
					if (bgqx.equals("Y") || bgqx.equals("C")) {
						return 0;
					}
				}
			} else {
				return -1;
			}
		}
		return 0;
	}

	public int removeappraisal2(Dto qDto) {
		// TODO Auto-generated method stub
		String tablename = qDto.getString("tablename");
		String aos_rows_id = qDto.getString("aos_rows_").split(",")[0];
		String sql = "update " + tablename + "  set  _xh ='1' where id_='"
				+ aos_rows_id + "'";
		try {
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
		return 1;
	}

	public List<Dto> countarchive(Dto qDto) {
		// TODO Auto-generated method stub
		String tablename = qDto.getString("tablename");
		String countname = qDto.getString("tid");
		List<Dto> l = new ArrayList<Dto>();
		String sql = "select " + countname + " as " + countname
				+ " ,count(*) as math from " + tablename + " GROUP BY "
				+ countname + "";
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Dto dto = Dtos.newDto();
					dto.put("type", list.get(i).get(countname));
					dto.put("number", list.get(i).get("math"));
					l.add(dto);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return l;
	}

	public List<Dto> queryCatelogys(Dto out) {
		// TODO Auto-generated method stub
		String sql = "select * from aos_sys_module where tablename='"
				+ out.getString("tablename") + "'";
		List<Dto> listdto = new ArrayList<Dto>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Dto outDto = Dtos.newDto();
				String cascade_id_ = (String) list.get(i).get("cascade_id_");
				String name_ = (String) list.get(i).get("name_");
				outDto.put("cascade_id_", cascade_id_);
				outDto.put("name_", name_);
				listdto.add(outDto);
			}
		}
		return listdto;
	}

	public Dto refreshcategory(Dto out) {
		// TODO Auto-generated method stub
		// 1.得到查询到的条件
		Dto outDto = Dtos.newDto();
		String tablename = out.getString("tablename");
		try {
			jdbcTemplate.execute(" update " + tablename + " set _classtree='"
					+ out.getString("cascade") + "'  where 1=1 "
					+ out.getString("query"));
			outDto.setAppMsg("更新成功！");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.setAppMsg("更新失败!");
		}
		return outDto;
	}

	public boolean toOrder_archive(Dto out, HttpSession session,
								   HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			String tablename = out.getString("tablename");
			// 切割
			// rders=xydm:信用代码:1:;zch:注册号:1:,
			String[] split = out.getString("orders").split(";");
			String orderby = "";
			if (split.length > 0) {
				for (int i = 0; i < split.length; i++) {
					String order = split[i];
					String[] os = order.split(":");
					if (os.length > 0) {
						String orderenname = os[0];
						String ordercnname = os[1];
						String value = os[2];
						String ordermath = os[3];
						// 存放到数据库中
						if (value.equals("1")) {
							value = "asc";
						} else if (value.equals("2")) {
							value = "desc";
						}
						if (ordermath.equals("false")) {
							// 不是数值排列
							orderby += orderenname + " " + value + ",";

						} else if (ordermath.equals("true")) {
							orderby += "convert(int," + orderenname + ") "
									+ value + ",";
						}
					}
				}
				// 此时条件制作完毕，需要去除最后一个逗号
				orderby = orderby.substring(0, orderby.length() - 1);
			}
			// 此时存入到数据库中
			String sql = "insert into archive_Sort_Data (bh,gx,zdmc,nr,tablename,cxr,mk) values('"
					+ tablename
					+ System.currentTimeMillis()
					+ "','','','"
					+ orderby
					+ "','"
					+ tablename
					+ "','"
					+ session.getAttribute("user") + "','')";
			jdbcTemplate.execute(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除原有的排列条件
	 *
	 * @author PX
	 * @param out
	 *
	 *            2019-1-7
	 * @param session
	 */
	public void deleteOrderby(Dto out, HttpSession session) {
		// TODO Auto-generated method stub
		String tablename = out.getString("tablename");
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		String sql = "delete from Archive_Sort_Data where tablename='"
				+ tablename + "' and cxr='" + userInfoVO.getAccount_() + "'";
		jdbcTemplate.execute(sql);
	}

	/**
	 * 获取查询的上一条记录
	 *
	 * @author PX
	 * @param out
	 *
	 *            2019-1-18
	 * @return
	 */
	public List<Map<String, Object>> getSelectWhereLast(Dto out) {
		// TODO Auto-generated method stub
		Long selectmath = Long.parseLong(out.getString("selectmath"));
		String tablename = out.getString("tablename");
		// 此时第一次进入
		if (selectmath == null || selectmath == 0) {
			// 获取当前查询的最大值
			String sql = "select max(bh) as bh from archive_Reseach_Data where tablename='"
					+ tablename
					+ "' and mk_en='"
					+ out.getString("Username")
					+ "'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.get(0).get("bh") != null) {
				long bh = Long.parseLong(list.get(0).get("bh").toString());
				String sql2 = "select * from archive_Reseach_Data where tablename='"
						+ tablename
						+ "' and bh='"
						+ bh
						+ "' and mk_en='"
						+ out.getString("Username") + "'";
				List<Map<String, Object>> list2 = jdbcTemplate
						.queryForList(sql2);
				// 此时list2集合传递到前台
				return list2;
			}
		} else {
			// 此时多次进入
			// 获取当前查询的最大值
			String sql = "select max(bh) as bh from archive_Reseach_Data where tablename='"
					+ tablename
					+ "' and bh<'"
					+ selectmath
					+ "' and mk_en='"
					+ out.getString("Username") + "'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.get(0).get("bh") != null) {
				long bh = Long.parseLong(list.get(0).get("bh").toString());
				String sql2 = "select * from archive_Reseach_Data where tablename='"
						+ tablename
						+ "' and bh='"
						+ bh
						+ "' and mk_en='"
						+ out.getString("Username") + "'";
				List<Map<String, Object>> list2 = jdbcTemplate
						.queryForList(sql2);
				return list2;

			} else {
				String sql2 = "select * from archive_Reseach_Data where tablename='"
						+ tablename
						+ "' and bh='"
						+ selectmath
						+ "' and mk_en='" + out.getString("Username") + "'";
				List<Map<String, Object>> list2 = jdbcTemplate
						.queryForList(sql2);
				return list2;
			}
		}
		return null;
	}

	/**
	 * 得到上一次json数据传递给前台
	 *
	 * @author PX
	 * @param list
	 *
	 *            2019-1-18
	 * @param out
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String addLastJson(List<Map<String, Object>> list, Dto out,
							  HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String gridJson = "{message:'error!',data:[";
		if (list != null && list.size() > 0) {
			for (int i = 1; i < list.size() + 1; i++) {
				String gx = (String) list.get(i - 1).get("gx");
				String zdmc = (String) list.get(i - 1).get("zdmc");
				String nr = (String) list.get(i - 1).get("nr");
				String tj = (String) list.get(i - 1).get("tj");
				gridJson += "{and:'" + gx + "',";
				gridJson += "filedname:'" + zdmc + "',";
				gridJson += "condition:'" + tj + "',";
				gridJson += "content:'" + nr + "'},";
			}
			gridJson.substring(0, gridJson.length() - 1);
			gridJson += "],selectmath:'" + list.get(0).get("bh") + "',";
			return gridJson += "total:'" + list.size() + "'}";
		} else {
			return gridJson += "],selectmath:'" + out.getString("selectmath")
					+ "',total:'0'}";
		}

	}

	public List<Map<String, Object>> getSelectWhereNext(Dto out) {
		// TODO Auto-generated method stub
		Long selectmath = Long.parseLong(out.getString("selectmath"));
		String tablename = out.getString("tablename");
		// 此时第一次进入
		if (selectmath == null || selectmath == 0) {
			// 获取当前查询的最大值
			String sql = "select max(bh) as bh from archive_Reseach_Data where tablename='"
					+ tablename
					+ "' and mk_en='"
					+ out.getString("Username")
					+ "'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.get(0).get("bh") != null) {
				long bh = Long.parseLong(list.get(0).get("bh").toString());
				String sql2 = "select * from archive_Reseach_Data where tablename='"
						+ tablename
						+ "' and bh='"
						+ bh
						+ "' and mk_en='"
						+ out.getString("Username") + "'";
				List<Map<String, Object>> list2 = jdbcTemplate
						.queryForList(sql2);
				// 此时list2集合传递到前台
				return list2;
			}
		} else {
			// 此时多次进入
			// 获取当前查询的最大值
			String sql = "select min(bh) as bh from archive_Reseach_Data where tablename='"
					+ tablename
					+ "' and bh>'"
					+ selectmath
					+ "' and mk_en='"
					+ out.getString("Username") + "'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.get(0).get("bh") != null) {
				long bh = Long.parseLong(list.get(0).get("bh").toString());
				String sql2 = "select * from archive_Reseach_Data where tablename='"
						+ tablename
						+ "' and bh='"
						+ bh
						+ "' and mk_en='"
						+ out.getString("Username") + "'";
				List<Map<String, Object>> list2 = jdbcTemplate
						.queryForList(sql2);
				return list2;

			} else {
				String sql2 = "select * from archive_Reseach_Data where tablename='"
						+ tablename
						+ "' and bh='"
						+ selectmath
						+ "' and  mk_en='" + out.getString("Username") + "'";
				List<Map<String, Object>> list2 = jdbcTemplate
						.queryForList(sql2);
				return list2;
			}
		}
		return null;
	}

	/**
	 * 得到下一次json数据传递给前台
	 *
	 * @author PX
	 * @param list
	 *
	 *            2019-1-18
	 * @param out
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String addNextJson(List<Map<String, Object>> list, Dto out,
							  HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String gridJson = "{message:'error!',data:[";
		if (list != null && list.size() > 0) {
			for (int i = 1; i < list.size() + 1; i++) {
				String gx = (String) list.get(i - 1).get("gx");
				String zdmc = (String) list.get(i - 1).get("zdmc");
				String nr = (String) list.get(i - 1).get("nr");
				String tj = (String) list.get(i - 1).get("tj");
				gridJson += "{and:'" + gx + "',";
				gridJson += "filedname:'" + zdmc + "',";
				gridJson += "condition:'" + tj + "',";
				gridJson += "content:'" + nr + "'},";
			}
			gridJson.substring(0, gridJson.length() - 1);
			gridJson += "],selectmath:'" + list.get(0).get("bh") + "',";
			return gridJson += "total:'" + list.size() + "'}";
		} else {
			//
			return gridJson += "],selectmath:'" + out.getString("selectmath")
					+ "',total:'0'}";
		}

	}

	public void removDataWhere(Dto out) {
		// TODO Auto-generated method stub
		String sql = "delete  from archive_Reseach_Data where tablename='"
				+ out.getString("tablename") + "' and mk_en='"
				+ out.getString("Username") + "'";
		jdbcTemplate.execute(sql);
	}

	/**
	 * 获取当前选中条目的数据信息
	 *
	 * @author PX
	 * @param out
	 *
	 *            2019-1-25
	 * @return
	 */
	public Map<String, Object> firstcheckOpen(Dto out) {
		// TODO Auto-generated method stub
		String id_ = out.getString("id_").split(",")[0];
		String tablename = out.getString("tablename");
		String limit = out.getString("limit");
		String page = out.getString("page");
		String query = out.getString("query");
		String index = out.getString("index");
		String sql = "select * from " + tablename + " where 1=1 and id_='"
				+ id_ + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			Map<String, Object> map = list.get(0);
			map.put("limit", limit);
			map.put("page", page);
			map.put("query", query);
			map.put("index", index);
			return map;
		}
		return null;

	}

	/**
	 * 根据pdf标志和条目id值得到pdf的文件信息
	 *
	 * @param fileid
	 * @param oldfilename
	 * @param tablename
	 * @return
	 */
	public List<Map<String, Object>> find_path2pdf(String fileid,
												   String tablename) {
		// TODO Auto-generated method stub
		String sql = "select * from " + tablename + "_path " + "where tid='"
				+ fileid + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	public Dto getData1(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String tablename = qDto.getString("tablename");
		String id = qDto.getString("id_");
		String sql = "SELECT * FROM " + tablename + " WHERE id_='" + id + "'";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		if (queryForList != null && queryForList.size() > 0) {
			Map<String, Object> map = queryForList.get(0);
			Set<String> set = map.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String k = it.next();
				Object v = map.get(k);
				outDto.put(k, v);
			}
		}
		return outDto;
	}

	/**
	 * 初检保存
	 *
	 * @author PX
	 * @param userInfoVO
	 * @param dto
	 * @return
	 *
	 *         2019-2-13
	 */
	public Dto firstchecksaveData(Dto qDto, UserInfoVO userInfoVO) {
		// TODO Auto-generated method stub
		Dto outDto = Dtos.newDto();
		String tablename = qDto.getString("tablename");
		// Map map = new HashMap();
		String id_ = qDto.getString("id").split(",")[0];
		// String _lrr=outDto.getUserInfo().getName_();
		// String _lrrq="";
		String fieldName = "";
		// 判断是否存在初检人和初检日期
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName += list.get(i).getFieldenname() + ",";
		}
		if (!fieldName.contains("_cjr") || !fieldName.contains("_cjrq")) {
			Dto out = Dtos.newDto();
			out.setAppCode(-1);
			return out;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String date = df.format(new Date());
		String user_ = userInfoVO.getName_();
		String sql = "update " + tablename + " set _cjr='" + user_
				+ "' , _cjrq='" + date + "' where id_='" + id_ + "'";
		jdbcTemplate.execute(sql);
		// List<Archive_tableinputPO> list = archive_tableinputMapper.
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，初检保存成功。");
		return outDto;
	}

	/**
	 * 根据条件，得到要导出Xml格式的数据信息
	 *
	 * @author PX
	 * @param inDto
	 * @param tablename
	 *
	 *            2019-2-18
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getDataFieldListDisplayAllXml(Dto inDto,
																   String tablename, String user) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String query = inDto.getString("query");
		Integer limit = Integer.valueOf(inDto.getString("pagesize"));
		Integer page = Integer.valueOf(inDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
		String selectmark = inDto.getString("selectmark");
		if (selectmark.equals("1")) {
			// 存入缓存表中
			addSearchData(inDto, username);
		}
		String sql = "";
		String term = "";
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(inDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		// 到这一步进行全宗筛选
		Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}
		// 排列条件传递过去
		String orderBy = getOrderBy(inDto, user);
		if (orderBy != null) {
			if (orderenfield != null) {
				orderBy = "," + orderBy;
			}
		} else {
			orderBy = "";
		}
		if (inDto.getString("page") != null && inDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ orderBy
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ inDto.getString("tablename")
					+ " WHERE "
					+ inDto.getString("queryclass")
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ limit
					* Integer.valueOf(inDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ orderBy
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ inDto.getString("tablename")
					+ " WHERE "
					+ inDto.getString("queryclass")
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List<Map<String, Object>> listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	/**
	 * 查询表头信息
	 *
	 * @author PX
	 * @param tablename
	 *
	 *            2019-2-18
	 * @return
	 */
	public List<Archive_tablefieldlistPO> getTitleListDisplayAll(
			String tablename) {
		// TODO Auto-generated method stub
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(tablename);
		return list;
	}

	/**
	 * 进行读写操作
	 *
	 * @author PX
	 * @param list
	 * @param titlelist
	 *
	 *            2019-2-18
	 * @return
	 */
	public StringBuilder ReadWriterHtml(
			List<Archive_tablefieldlistPO> titlelist,
			List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		// 1.加载html表头
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML>");
		sb.append("<HEAD>");
		sb.append("<TITLE>html</TITLE>");
		sb.append("<STYLE TYPE='text/css'>");
		sb.append(".Style0 { border-style: solid; padding: 3; border-left-width: 1; border-top-width: 1; border-right-width: 1; border-bottom-width: 1; border-left-color: rgb(160,160,160); border-top-color: rgb(160,160,160); border-right-color: rgb(160,160,160); border-bottom-color: rgb(160,160,160); font-family: 'Tahoma'; mso-font-charset: 0; font-size: 8pt; color: rgb(0,0,255); background-color: rgb(255,255,255)}");
		sb.append(".Style1 { border-style: solid; padding: 3; border-left-width: 1; border-top-width: 1; border-right-width: 1; border-bottom-width: 1; border-left-color: rgb(160,160,160); border-top-color: rgb(160,160,160); border-right-color: rgb(160,160,160); border-bottom-color: rgb(160,160,160); font-family: 'Tahoma'; mso-font-charset: 0; font-size: 8pt; color: rgb(0,0,0); background-color: rgb(240,240,240)}");
		sb.append(".Style2 { border-style: solid; padding: 3; border-left-width: 1; border-top-width: 1; border-right-width: 1; border-bottom-width: 1; border-left-color: rgb(160,160,160); border-top-color: rgb(160,160,160); border-right-color: rgb(160,160,160); border-bottom-color: rgb(160,160,160); font-family: 'Tahoma'; mso-font-charset: 0; font-size: 8pt; color: rgb(0,0,0); background-color: rgb(255,255,255)}");
		sb.append(".Style3 { border-style: solid; padding: 3; border-left-width: 1; border-top-width: 1; border-right-width: 1; border-bottom-width: 1; border-left-color: rgb(160,160,160); border-top-color: rgb(160,160,160); border-right-color: rgb(160,160,160); border-bottom-color: rgb(160,160,160); font-family: 'MS Sans Serif'; mso-font-charset: 0; font-weight: bold; font-size: 16pt; color: rgb(255,255,255); background-color: rgb(96,121,197)}");
		sb.append(".Style4 { border-style: solid; padding: 3; border-left-width: 1; border-top-width: 1; border-right-width: 1; border-bottom-width: 1; border-left-color: rgb(160,160,160); border-top-color: rgb(160,160,160); border-right-color: rgb(160,160,160); border-bottom-color: rgb(160,160,160); font-family: 'Tahoma'; mso-font-charset: 0; font-size: 8pt; color: rgb(0,0,0); background-color: rgb(255,255,255)}");
		sb.append(".Style5 { border-style: solid; padding: 3; border-left-width: 1; border-top-width: 1; border-right-width: 1; border-bottom-width: 1; border-left-color: rgb(160,160,160); border-top-color: rgb(160,160,160); border-right-color: rgb(160,160,160); border-bottom-color: rgb(160,160,160); font-family: 'Tahoma'; mso-font-charset: 0; font-size: 8pt; color: rgb(0,0,0); background-color: rgb(255,255,255)}");
		sb.append(".Style6 { border-style: solid; padding: 3; border-left-width: 1; border-top-width: 1; border-right-width: 1; border-bottom-width: 1; border-left-color: rgb(160,160,160); border-top-color: rgb(160,160,160); border-right-color: rgb(160,160,160); border-bottom-color: rgb(160,160,160); font-family: 'Tahoma'; mso-font-charset: 0; font-size: 8pt; color: rgb(0,0,0); background-color: rgb(240,240,240)}");
		sb.append(".Style7 { border-style: solid; padding: 3; border-left-width: 1; border-top-width: 1; border-right-width: 1; border-bottom-width: 1; border-left-color: rgb(160,160,160); border-top-color: rgb(160,160,160); border-right-color: rgb(160,160,160); border-bottom-color: rgb(160,160,160); font-family: 'Tahoma'; mso-font-charset: 0; font-size: 8pt; color: rgb(0,0,0); background-color: rgb(240,240,240)}");
		sb.append("</STYLE>");
		sb.append("</HEAD>");
		sb.append("<BODY>");
		sb.append("<TABLE BORDER CELLSPACING=0>");
		// 第一次把表头写入进去
		sb.append("<TR>");
		if (titlelist != null && titlelist.size() > 0) {
			for (int i = 0; i < titlelist.size(); i++) {

				Integer dislen = titlelist.get(i).getDislen();
				if (dislen == 0 || dislen.equals("")) {
					dislen = 100;
				}
				String fieldcnname = titlelist.get(i).getFieldcnname() + "";
				sb.append("<TD NOWRAP WIDTH=" + dislen
						+ " ALIGN=LEFT CLASS=Style3>" + fieldcnname + "</TD>");

			}
			sb.append("</TR>");
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sb.append("<TR>");
				for (int k = 0; k < titlelist.size(); k++) {
					Integer dislen = titlelist.get(k).getDislen();
					if (dislen == 0 || dislen.equals("")) {
						dislen = 100;
					}
					String fieldenname = titlelist.get(k).getFieldenname() + "";
					String enname = list.get(i).get(fieldenname) + "";
					if (enname == null || enname.equals("null") || enname == "") {
						enname = "";
					}
					sb.append("<TD NOWRAP WIDTH=" + dislen
							+ " ALIGN=LEFT CLASS=Style3>" + enname + "</TD>");
				}
				sb.append("</TR>");
			}
		}
		sb.append("<TABLE>");
		sb.append("</BODY>");
		sb.append("</HTML>");
		// 把sb中的数据写到response中
		return sb;

	}

	/**
	 * 导出txt格式
	 *
	 * @author PX
	 * @param titlelist
	 * @param list
	 * @return
	 *
	 *         2019-2-19
	 * @throws UnsupportedEncodingException
	 */
	public StringBuilder ReadWriterTxt(
			List<Archive_tablefieldlistPO> titlelist,
			List<Map<String, Object>> list) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		int[] listlength = new int[titlelist.size()];
		if (list != null && list.size() > 0) {
			for (int k = 0; k < titlelist.size(); k++) {
				// 此時創建一個集合
				int[] str = new int[list.size() + 1];
				String f = titlelist.get(k).getFieldenname();
				for (int i = 0; i < list.size(); i++) {
					String fieldenname = titlelist.get(k).getFieldenname();
					// 根据英文名称得到集合信息
					String enname = list.get(i).get(fieldenname) + "";
					if (enname == null || enname == "null") {
						str[i] = 0;
					} else {
						str[i] = new String(enname.getBytes("GB2312"),
								"ISO-8859-1").length();
					}
				}
				// 此时再把标题的长度加进去
				String title = titlelist.get(k).getFieldcnname() + "";
				str[list.size()] = new String(title.getBytes("GB2312"),
						"ISO-8859-1").length();
				// 在把集合添加到list中
				// 此时将str数组中取出最大值
				int max = getMax(str);
				listlength[k] = max;
			}
		}
		// 标题加入
		for (int y = 0; y < titlelist.size(); y++) {
			String field = titlelist.get(y).getFieldcnname();
			// 得到对应列长度
			int le = listlength[y];
			field = Format(field, le);
			// 证明是一行的最后，加入换行符号
			if (y == titlelist.size() - 1) {
				sb.append(field + "\r");
			} else {
				// 之间加两个空格
				sb.append(field + "  ");
			}
		}
		// 此时将每一列的最大长度找到了
		for (int q = 0; q < list.size(); q++) {
			for (int r = 0; r < titlelist.size(); r++) {
				String fieldenname = titlelist.get(r).getFieldenname();
				// 根据英文名称得到集合信息
				String enname = list.get(q).get(fieldenname) + "";
				// 得到对应列长度
				int len = listlength[r];
				enname = Format(enname, len);
				// 证明是一行的最后，加入换行符号
				if (r == titlelist.size() - 1) {
					sb.append(enname + "\r\n");
				} else {
					sb.append(enname + "\t");
				}
			}
		}
		return sb;
	}

	/**
	 * 右侧补空格
	 *
	 * @author PX
	 * @param enname
	 * @param len
	 * @return
	 *
	 *         2019-2-20
	 */
	private String Format(String enname, int len) {
		// TODO Auto-generated method stub
		for (int i = 0; i <= len; i++) {
			enname += " ";
		}
		return enname;
	}

	/**
	 * 得到最大值
	 *
	 * @author PX
	 * @param str
	 *
	 *            2019-2-19
	 * @return
	 */
	private int getMax(int[] str) {
		// TODO Auto-generated method stub
		int max = str[0];
		for (int i = 0; i < str.length; i++) {
			if (max < str[i]) {
				max = str[i];
			}
		}
		return max;
	}

	/**
	 * 查询统计
	 *
	 * @author PX
	 * @param inDto
	 *
	 *            2019-2-22
	 * @return
	 * @return
	 */
	public List<Map<String, Object>> selectCount(Dto inDto) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<>();
		// _classtree=0.005.002
		//此时判断一下统计的是什么条件
		String _classtree = inDto.getString("_classtree");
		// 进行查询操作
		String tablename = inDto.getString("tablename");
		// 查询条件
		String selectorders = inDto.getString("selectorders");
		// 查询值
		String selectmaths = inDto.getString("selectmaths");
		String whereString = "";
		String queryclass = inDto.getString("queryclass");

		// 拼接出最新条件
		if (selectorders != "") {
			String[] selectorder = selectorders.split(",");
			String[] selectmath = selectmaths.split(",");
			for (int i = 0; i < selectorder.length; i++) {
				String order = selectorder[i];
				String math = selectmath[i];
				// 组成查询条件
				whereString += order + math;
			}
		}
		// 此时得到条件
		String countfield = inDto.getString("countfield");
		String countmethod = inDto.getString("countmethod");
		String sql = "";
		if(countfield.equals("qzh")){
			sql="select count(qzh) as coun,sum(Conver(int ,ys)) as ys,qzh from "+tablename+" where qzh in( select distinct(qzh) as qzhsum from "+tablename+") group by qzh";
			list=jdbcTemplate.queryForList(sql);
			return list;

		}
		else if(countfield.equals("ys")){
			String sql2="select sum(Conver(int ,ys)) as ys from "+tablename+" where 1=1 "+whereString;
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("ys",jdbcTemplate.queryForList(sql2).get(0).get("ys"));
			list.add(map);
		}else if(countfield.equals("_path")){
			String sql2="select sum(Conver(int ,_path)) as _path from "+tablename+" where 1=1 "+whereString;
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("_path",jdbcTemplate.queryForList(sql2).get(0).get("_path"));
			list.add(map);
		}
		return list;
	}

	/**
	 *
	 *
	 * @author PX
	 * @param inDto
	 *
	 *            2019-3-4
	 * @return
	 */
	public Dto selectjoint(Dto inDto) {
		// TODO Auto-generated method stub
		Dto dto=Dtos.newDto();
		String updatetablefield = inDto.getString("updatetablefield");
		String jointorders = inDto.getString("jointorders");
		String tablename = inDto.getString("tablename");
		String jointmaths = inDto.getString("jointmaths");
		String query=inDto.getString("query");
		// 组合拼接字符串
		String jointString = "";
		if (jointorders != null && jointmaths != null) {
			String[] maths = jointmaths.split(",");
			String[] orders = jointorders.split(",");
			if (maths.length == 0) {
				for (int i = 0; i < orders.length; i++) {
					String order = orders[i];
					jointString += "CONVERT(varchar(1024)," + order + ")" + "+";
				}
			} else {
				for (int i = 0; i < orders.length; i++) {
					if(i==orders.length-1){
						//此时最后一个
						String order = orders[i];
						jointString += "CONVERT(varchar(1024)," + order + ")" + "+";
					}else{
						String order = orders[i];
						String math = maths[i];
						jointString += "CONVERT(varchar(1024)," + order + ")"
								+ "+'" + math + "'+";
					}
				}
			}
			jointString = jointString.substring(0, jointString.length() - 1);
		}

		String sql = "update " + tablename + " set " + updatetablefield + "="
				+ jointString+" where 1=1 "+query;
		try {
			int i = jdbcTemplate.update(sql);
			dto.setAppCode(1);
			dto.setAppMsg("更新成功");
			return dto;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			dto.setAppCode(-1);
			dto.setAppMsg(e.getMessage());
			return dto;
		}
	}

	/**
	 * 行编辑
	 *
	 * @author PX
	 * @param inDto
	 *
	 *            2019-3-4
	 * @return
	 */
	public boolean editGrid(Dto inDto) {
		// TODO Auto-generated method stub
		String tablename = inDto.getString("tablename");
		String id_ = inDto.getString("id_");
		String strsql = "";
		Iterator iter = inDto.entrySet().iterator(); // 获取key和value的set
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next(); // 把hashmap转成Iterator再迭代到entry
			Object key = entry.getKey(); // 从entry获取keyObject
			if (key.equals("app") || key.equals("tablename")
					|| key.equals("_userInfoVO") || key.equals("id_")) {
				continue;
			}
			Object val = "'" + entry.getValue() + "'"; // 从entry获取value}
			// strsql=key+"="+val+"";
			if (strsql.equals("")) {
				strsql = key + "=" + val + "";
			} else
				strsql = strsql + "," + key + "=" + val + "";
		}
		String sql = " UPDATE " + inDto.getString("tablename") + " SET "
				+ strsql + " WHERE id_='" + inDto.getString("id_") + "'";
		try {
			jdbcTemplate.execute(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 得到指导意见
	 *
	 * @author PX
	 * @param inDto
	 *
	 * 2019-4-30
	 * @return
	 */
	public List<Archive_zdyjPO> getZDYJ(Dto inDto) {
		// TODO Auto-generated method stub
		List<Archive_zdyjPO> list = archive_zdyjMapper.list(inDto);
		//String sqlString="select * from archive_zdyj where tid='"+inDto.getString("id_")+"' and zd_tablename ='"+inDto.getString("tablename")+"'";
		return list;
	}
	/**
	 * 添加指导意见
	 *
	 * @author PX
	 * @param inDto
	 *
	 * 2019-4-30
	 * @return
	 */
	public boolean addZddescription(Dto inDto,HttpSession session) {
		// TODO Auto-generated method stub
		try {
			inDto.put("id_", AOSId.uuid());
			UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
			inDto.put("zd_person",userInfoVO.getAccount_());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			inDto.put("zd_time",df.format(new Date()));
			Archive_zdyjPO archive_zdyjPO = new Archive_zdyjPO();
			AOSUtils.copyProperties(inDto, archive_zdyjPO);
			archive_zdyjMapper.insert(archive_zdyjPO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;

	}
	/**
	 * 生成xml文件
	 *
	 * @author PX
	 * @param inDto
	 *
	 * 2019-4-30
	 */
	public void deriveXML(Dto inDto) {
		// TODO Auto-generated method stub
		//得到当前目录的所有数据
	}

	/**
	 * 查询当前条目加存是否存在索引
	 * @param dto
	 */
	public boolean  getIndex(Dto dto) {
		String sql="select indexdata_en from archive_INDEX where tablename ='"+dto.getString("tablename")+"'";
		String where="";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String indexdata_en=(String)list.get(i).get("indexdata_en");
				if(i==0){
					where=indexdata_en+"= '"+dto.getString(indexdata_en)+"'";
				}else{
					where+="or "+indexdata_en+"= '"+dto.getString(indexdata_en)+"'";
				}
			}
			String sql2="select top 1* from "+dto.getString("tablename")+" where "+where;
			List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
			if(list2.size()<=0){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	/**
	 * 查询当前条目修改保存是否存在索引
	 * @param dto
	 */
	public boolean  getIndex_u(Dto dto) {
		//通过id查询当前的索引数值
		//在查询当前文本框添加的

		String sql="select indexdata_en from archive_INDEX where tablename ='"+dto.getString("tablename")+"'";
		String where="";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String indexdata_en=(String)list.get(i).get("indexdata_en");

				where+=" and "+indexdata_en+"= '"+dto.getString(indexdata_en)+"'";
			}
			String sql2="select top 1* from "+dto.getString("tablename")+" where id_='"+dto.getString("id")+"' "+where;
			//先判断都是一样的，
			//
			///此时看看是不是在这种输入的条件的下和之前的条目索引的条件值是不是完全一样的，如果是一行的，就允许用户修改操作
			List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
			if(list2.size()<=0){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

	/**
	 * 获得关联表的子表
	 * @param tablename
	 */
	public List<Map<String,Object>> getSonTablename(String tablename) {
		String sql="select distinct(t_table) as t_table from archive_JOIN where s_table='"+tablename+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}

	/**
	 * 根据标识查询数据字典
	 */
	public List<Map<String,Object>> orderComboBox(Dto inDto) {
		String dicname=inDto.getString("dicname");
		String sql="select code_ as orderascenname    ,desc_ as  orderasccnname   from aos_sys_dic where dic_index_id_ in( select id_  from aos_sys_dic_index where key_='"+dicname+"')";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}

	/**
	 * 排列方式查询
	 * @param inDto
	 */
	public List<Map<String,Object>> addOrder(Dto inDto){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		String ordermath=inDto.getString("ordermath");
		String orderascing=inDto.getString("orderascing");
		String rightcnzd=inDto.getString("rightcnzd");
		String rightenzd=inDto.getString("rightenzd");
		String orderascdesc=inDto.getString("orderascdesc");
		String leftcnzd=inDto.getString("leftcnzd");
		String leftenzd=inDto.getString("leftenzd");
		//1.把旧的得到并放到一个list集合中
		if(rightcnzd.length()!=0&&rightenzd.length()!=0){
			String[] rightcnzds=rightcnzd.split(",");
			String[] rightenzds=rightenzd.split(",");
			String[] orderascdescs=orderascdesc.split(",");
			String[] ordermaths=ordermath.split(",");
			String[] orderascings=orderascing.split(",");
			for(int i=0;i<rightcnzds.length;i++){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("orderenname",rightenzds[i]);
				map.put("ordercnname",rightcnzds[i]);
				map.put("orderascdesc",orderascdescs[i]);
				map.put("orderascing",orderascings[i]);

				map.put("ordermath",Boolean.valueOf(ordermaths[i]));
				list.add(map);
			}
		}
		//2.得到新的并放到list集合中
		String[] leftcnzds=leftcnzd.split(",");
		String[] leftenzds=leftenzd.split(",");
		for(int i=0;i<leftcnzds.length;i++){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("orderenname",leftenzds[i]);
			map.put("ordercnname",leftcnzds[i]);
			map.put("orderascdesc","升序");
			map.put("orderascing",1);
			map.put("ordermath",Boolean.valueOf(false));
			list.add(map);
		}
		return list;
	}
	/**
	 *
	 * 页面初始化(回收表)
	 *
	 * @param tablename
	 * @return
	 */
	public List<Archive_tablefieldlistPO> getDataFieldListTitle_recovery(String tablename) {
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll_Recovery(tablename);
		return list;
	}
	/**
	 *
	 * 显示表头(回收表)
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataFieldListDisplayAll_Recovery(Dto qDto) {
		String query = queryConditions2(qDto);
		String sql="";
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll_Recovery(qDto.getString("tablename"));
		String orderenfield = "" ;
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if(i==3){
				orderenfield=enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		if(qDto.getString("page")!=null&&qDto.getString("page")!=""){
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "+orderenfield+") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " WHERE "+qDto.getString("queryclass")+" "
					+ query
					+ " ) "
					+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ qDto.getPageStart()
					+ " AND "
					+ qDto.getPageLimit()
					* Integer.valueOf(qDto.getString("page")) + " ORDER BY aos_rn_";
		}else{
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "+orderenfield+") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " WHERE "+qDto.getString("queryclass")+" "
					+ query
					+ " ) "
					+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}
	/**
	 * 保存记忆
	 * @param inDto
	 * @param userInfoVO
	 */
	public void saveRemember(Dto inDto, UserInfoVO userInfoVO) {
		String tablename=inDto.getString("tablename");
		String type=inDto.getString("type");
		String flag=inDto.getString("flag");
		String name=inDto.getString("name");
		String username=userInfoVO.getAccount_();
		//删除之前原有的，添加一个新的
		String sql2="delete from archive_remember where type='"+type+"' and name='"+name+"' and username='"+username+"' and tablename='"+tablename+"'";
		jdbcTemplate.execute(sql2);
		String sql="insert into archive_remember(id_,type,name,username,flag,tablename) values('"+AOSId.uuid()+"','"+type+"','"+name+"','"+username+"','"+flag+"','"+tablename+"')";
		jdbcTemplate.execute(sql);
	}

	/**
	 * 获得记忆
	 * @param inDto
	 * @param userInfoVO
	 */
	public Map<String,Object> getRemember(Dto inDto, UserInfoVO userInfoVO) {
		String tablename=inDto.getString("tablename");
		String username=userInfoVO.getAccount_();
		String type=inDto.getString("type");
		String sql="select flag,name  from archive_remember where tablename='"+tablename+"' and username='"+username+"' and type='"+type+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		Map<String,Object> map=new HashMap<String,Object>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String flag=(String)list.get(i).get("flag");
				String name=(String)list.get(i).get("name");
				map.put(name,flag);
			}
		}
		return map;
	}

	/**
	 * 得到子表目录树（默认直接目录树没有限定套件）
	 * @param inDto
	 * @return
	 */
	public String sonisAll(Dto inDto) {
		String queryclass = " 1=1";
		return queryclass;
	}
	/*
	 * 查询表名
	 */
	public List<Archive_tablenamePO> findTablename() {
		// TODO Auto-generated method stub
		List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
		String sql = "select * from archive_TableName where TableName like '%_receive%'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String tablename = (String) map.get("TableName");
				String tabledesc = (String) map.get("TableDesc");
				Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
				archive_tablenamePO.setTablename(tablename);
				archive_tablenamePO.setTabledesc(tabledesc);
				alist.add(archive_tablenamePO);
			}
		}
		return alist;
	}
	/**
	 *
	 * 还原条目
	 *
	 * @param qDto
	 * @return
	 */
	public Dto return_data(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		String tablename = qDto.getString("tablename");
		int del = 0;
		for (String id_ : selections) {

			String sql3="insert into "+qDto.getString("tablename")+" select * from "+qDto.getString("tablename")+"_backup where id_='"+id_+"'";
			jdbcTemplate.execute(sql3);
			jdbcTemplate.execute(" delete from " + tablename + "_backup where id_='"
					+ id_ + "'");
			del++;
		}
		String msg = "操作完成，";
		if (del > 0) {
			msg = AOSUtils.merge(msg + "成功还原信息[{0}]个", del);
		}
		outDto.setAppMsg(msg);
		return outDto;

	}
	/*
	 * 查询表名
	 */
	public List<Archive_tablenamePO> findZLKTablename() {
		// TODO Auto-generated method stub
		List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
		String sql = "select * from aos_sys_dic where  dic_index_id_ in( select id_ from aos_sys_dic_index where key_='zlk')";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String tablename = (String) map.get("code_");
				String tabledesc = (String) map.get("desc_");
				Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
				archive_tablenamePO.setTablename(tablename);
				archive_tablenamePO.setTabledesc(tabledesc);
				alist.add(archive_tablenamePO);
			}
		}
		return alist;
	}

	public List<Map<String, Object>> fillReport(Dto out) {
		String sql="select * from "+out.getString("tablename");
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}

	public Dto getSensitive(Dto dtos) {
		Dto out=Dtos.newDto();
		for(int i=1;i<9;i++){
			String content=dtos.getString("content"+i);
			//查询敏感词
			if(content!=null&&!content.equals("")){
				String sql="select * from archive_sensitive where sensitive_list like '%"+content+"%'";
				List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
				if(list.size()>0&&list!=null){
					out.setAppMsg(content);
					out.setAppCode(-1);
					return out;
				}
			}
		}
		out.setAppCode(1);
		return out;
	}
}