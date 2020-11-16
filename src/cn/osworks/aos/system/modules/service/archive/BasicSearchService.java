package cn.osworks.aos.system.modules.service.archive;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BasicSearchService extends JdbcDaoSupport {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public DataService dataService;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private Aos_sys_userMapper aos_sys_user;
	@Autowired
	private Archive_remoteMapper archive_remoteMapper;
	@Autowired
    private Archive_zizhu_applyMapper archive_zizhu_applyMapper;
	@Autowired
    private Archive_zizhuremote_detailMapper archive_zizhuremote_detailMapper;
	@Resource
	public void setJd(JdbcTemplate jd) {
		super.setJdbcTemplate(jd);
	}

	/**
	 * 得到数据表集合
	 * 
	 * @author PX
	 * @param listtablename
	 * 
	 *            2018-8-23
	 * @return
	 */
	public List<Map<String, Object>> getListData(String listtablename) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select * from " + listtablename;
		list = jdbcTemplate.queryForList(sql);
		return list;
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
	/*
	 * 查询表名
	 */
	public List<Archive_tablenamePO> findTablename_zizhu() {
		// TODO Auto-generated method stub
		List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
		Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
		archive_tablenamePO.setTablename("gdws");
		archive_tablenamePO.setTabledesc("归档文书");
		alist.add(archive_tablenamePO);
		Archive_tablenamePO archive_tablenamePO2 = new Archive_tablenamePO();
		archive_tablenamePO2.setTablename("jnws");
		archive_tablenamePO2.setTabledesc("卷内文书");
		alist.add(archive_tablenamePO2);
		return alist;
	}
	/*
	 * 查询表名
	 */
	public List<Archive_tablenamePO> findTablename() {
		// TODO Auto-generated method stub
		List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
		String sql = "select * from archive_TableName";
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
	public List<Dto> getDataFieldListDisplayAll(Dto qDto, HttpSession session) {
		String sql = "";
		String query = queryConditions2(qDto);
		String fieldName;
		String enfield = "id_";
		String xm = qDto.getString("text_name");

		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit) + 1;
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAllAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		if (qDto.getString("page") != null && qDto.getString("page") != "") {

			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+"and tm = "+"'"+xm+"'"
					+ query
					+ ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ pageStart
					+ " AND "
					+ limit
					* page
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query
					+ ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
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
	public List<Dto> getDataFieldListDisplayAll_ys(Dto qDto, HttpSession session) {
		String sql = "";
		String query = queryConditions2(qDto);
		String fieldName;
		String enfield = "id_";
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit) + 1;
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAllAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query
					+ ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ pageStart
					+ " AND "
					+ limit
					* page
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query
					+ ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
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
	public List<Dto> getDataFieldListDisplayAll2(Dto qDto, HttpSession session) {
		String sql = "";
		String query = queryConditions2(qDto);
		String fieldName;
		String enfield = "id_";
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit) + 1;
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAllAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query
					+ ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ pageStart
					+ " AND "
					+ limit
					* page
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query
					+ ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
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
	public List<Dto> getDataFieldListDisplayAll3(Dto qDto, HttpSession session) {
		String sql = "";
		String query = queryConditions2(qDto);
		String querySession=qDto.getString("querySession");
		String fieldName;
		String enfield = "id_";
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit) + 1;
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAllAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query+" "+querySession
					+ ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ pageStart
					+ " AND "
					+ limit
					* page
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query+" "+querySession
					+ ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
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
	public List<Map<String, Object>> getDataFieldListDisplayAllCd(Dto qDto, HttpSession session) {
		String sql = "";
		String query = qDto.getString("query");
		String fieldName;
		String enfield = "id_";
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit) + 1;
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAllAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		if (qDto.getString("page") != null && qDto.getString("page") != "") {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query
					+ ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ pageStart
					+ " AND "
					+ limit
					* page
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query
					+ ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List<Map<String, Object>> listDto= jdbcTemplate.queryForList(sql);
		return listDto;
	}
	/**
	 * 存入信息
	 * 
	 * @author PX
	 * @param outdto
	 * 
	 *            2018-8-29
	 * @return
	 * @throws ParseException
	 */
	public Map<String, Object> jymessage(Dto outdto) throws ParseException {
		// TODO Auto-generated method stub
		String id_ = outdto.getString("id_");
		// 查询借阅的时间
		String sql = "select * from archive_borrow where id_='" + id_ + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 借阅保存
	 * 
	 * @author PX
	 * @param outDto
	 * 
	 *            2018-8-29
	 * @return
	 */
	public boolean savejy(Dto outDto) {
		// TODO Auto-generated method stub
		// 添加之前判断是否存在
		String sql2 = "select * from archive_jy where tablename='"
				+ outDto.getString("tablename") + "' and archive_id='"
				+ outDto.getString("archivenumber")
				+ "' and archivestate='已借阅' and gh=''";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
		if (list != null && list.size() > 0) {
			return false;
		} else {
			String sql = "insert into archive_jy(id_,jytime,users,archive_id,tablename,jyday,archivestate,gh,person,borrowreason) values('"
					+ AOSId.uuid()
					+ "','"
					+ outDto.getString("borrowData")
					+ "','"
					+ outDto.getString("user")
					+ "','"
					+ outDto.getString("archivenumber")
					+ "','"
					+ outDto.getString("tablename")
					+ "','"
					+ outDto.getString("borrowday")
					+ "','已借阅','','"
					+ outDto.getString("person")
					+ "','"
					+ outDto.getString("borrowreason") + "')";
			jdbcTemplate.execute(sql);
			// 修改条目状态
			String sql3 = "update " + outDto.getString("tablename")
					+ " set _jy='1' where id_='"
					+ outDto.getString("archivenumber") + "'";
			jdbcTemplate.execute(sql3);
		}
		return true;
	}

	public void updatejy(Dto outDto) throws Exception {
		// TODO Auto-generated method stub
		// 修改借阅天数，完成续借
		// 查询原有天数
		String sql = "select * from archive_jy where tablename='"
				+ outDto.getString("tablename") + "' and archive_id='"
				+ outDto.getString("archivenumber") + "' and gh=''";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			String jyday = (String) list.get(0).get("jyday");
			if (jyday != null || !jyday.equals("")) {
				String sql2 = "update archive_jy set jyday='"
						+ (Integer.valueOf(jyday) + Integer.valueOf(outDto
								.getString("reletday")))
						+ "' where tablename='" + outDto.getString("tablename")
						+ "' and archive_id='"
						+ outDto.getString("archivenumber") + "'";
				jdbcTemplate.execute(sql2);
			}
		}
	}

	/**
	 * 归还
	 * 
	 * @author PX
	 * @param outDto
	 * @param session
	 * @return
	 * 
	 *         2018-8-29
	 */
	public int returnjy(Dto outDto, HttpSession session) {
		// TODO Auto-generated method stub
		// 判断是否有权限
		String sql = "select * from archive_jy where tablename='"
				+ outDto.getString("listTablename") + "' and archive_id='"
				+ outDto.getString("id_") + "' and gh=''";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			String users = (String) list.get(0).get("users");
			// 判断当前用户和这个用户是不是一个人
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			String user = userInfoVO.getAccount_();
			if (!users.equals(user)) {
				return -2;
			}
			try {
				// 执行归还操作
				String sql2 = "update archive_jy set archivestate='已归还',gh='1' where tablename='"
						+ outDto.getString("listTablename")
						+ "' and archive_id='" + outDto.getString("id_") + "'";
				jdbcTemplate.execute(sql2);
				// 修改条目标记
				String sql3 = "update " + outDto.getString("listTablename")
						+ " set _jy=null where id_='" + outDto.getString("id_")
						+ "'";
				jdbcTemplate.execute(sql3);
				return 1;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return -1;
			}
		}
		return -1;
	}

	/**
	 * 
	 * 
	 * @author PX
	 * 
	 *         2018-8-30
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> listjy(Dto dto) {
		// TODO Auto-generated method stub
		String tablename = dto.getString("tablename");
		String sql = "";
		if (tablename != null && !tablename.equals("")) {
			sql = "select * from archive_jy where 1=1 and tablename='"
					+ tablename + "'";
		} else {
			sql = "select * from archive_jy where 1=1";
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
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
						query.append("and " + qDto.getString("filedname" + i));
					} else {
						query.append("or " + qDto.getString("filedname" + i)
								+ " ");
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
	 * 用户列表
	 * 
	 * @author PX
	 * @return
	 * 
	 *         2018-11-28
	 */
	public List<Aos_sys_userPO> queryUsers() {
		// TODO Auto-generated method stub
		return aos_sys_user.list2();

	}

	/**
	 * approval_status 1.待审批（只要提交就是审批了） 2.已审批（审核通过。可以借阅了） 3.未通过（审核了，但是没有通过，不能借阅）
	 * borrow_status 1.没有值，或者为空，档案没有借阅，也没有归还 2.档案借阅了，可以借阅了 3.档案归还了，不能借阅了
	 *
	 * @author PX
	 * @param qDto
	 *
	 *            2018-11-30
	 * @return
	 */
	public boolean submit_application(Dto qDto) {
		// TODO Auto-generated method stub
		try {
			String id_ = AOSId.uuid();
			String borrow_card = qDto.getString("borrow_card");
			String login_card = qDto.getString("login_card");
			String data_ids_ = qDto.getString("data_ids_");
			String borrow_purpose = qDto.getString("borrow_purpose");
			String borrow_day = qDto.getString("borrow_day");
			String borrow_type_ = qDto.getString("borrow_type_");
			String borrow_explain = qDto.getString("borrow_explain");
			String checked_person = qDto.getString("checked_person");
			String borrow_person = qDto.getString("borrow_person");
			String borrow_date = qDto.getString("borrow_date");
			String expire_date = qDto.getString("expire_date");
			String tablename = qDto.getString("tablename");
			String borrow_ip = qDto.getString("borrow_ip");
			// 待审核状态
			String approval_status = "1";
			// 借阅与归还
			String borrow_status = "";
			String sql = "insert into archive_borrow(id_,borrow_card,login_card,data_ids_,borrow_purpose,borrow_day,borrow_type_,borrow_explain,checked_person,borrow_person,borrow_date,expire_date,tablename,borrow_ip,approval_status,borrow_status,flag,expire_message) values('"
					+ id_
					+ "','"
					+ borrow_card
					+ "','"
					+ login_card
					+ "','"
					+ data_ids_
					+ "','"
					+ borrow_purpose
					+ "','"
					+ borrow_day
					+ "','"
					+ borrow_type_
					+ "','"
					+ borrow_explain
					+ "','"
					+ checked_person
					+ "','"
					+ borrow_person
					+ "','"
					+ borrow_date
					+ "','"
					+ expire_date
					+"','"
					+ tablename
					+ "','"
					+ borrow_ip
					+ "','"
					+ approval_status
					+ "','"+borrow_status +"','','0')";
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 我的借阅列表
	 * 
	 * @author PX
	 * @param qDto
	 *
	 * 2018-12-1
	 * @param name 
	 * @return 
	 */
	public List<Map<String, Object>> myborrow(Dto qDto, String name) {
		// TODO Auto-generated method stub
		//tableTemplate  borrow_order
		String queryString="";
		String tableTemlate=qDto.getString("tableTemplate");
		String borrow_order=qDto.getString("borrow_order");
		if(tableTemlate!=null&&tableTemlate!=""){
			queryString+=" and tablename='"+tableTemlate+"'";
		}
		if(borrow_order!=null&&borrow_order!=""){
			queryString+=" and borrow_explain like '%"+borrow_order+"%'";
		}
		String sql="select * from archive_borrow where 1=1 "+queryString+" and login_card='"+name+"'";
		return jdbcTemplate.queryForList(sql);
	}

	public List<Map<String, Object>> listExamine() {
		// TODO Auto-generated method stub
		String sql="select * from archive_borrow where 1=1 and login_card!='admin' and approval_status='1'";
		return jdbcTemplate.queryForList(sql);
	}
	
	public boolean againExamine(Dto qDto) {
		// TODO Auto-generated method stub
		String sql="update archive_borrow set approval_status='2', borrow_status='1' where id_='"+qDto.getString("id_")+"'";
		try {
			int i = jdbcTemplate.update(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
		
	}

	public boolean unagainExamine(Dto qDto) {
		// TODO Auto-generated method stub
		String id_ = qDto.getString("ids").split(",")[0];
		String sql="update archive_borrow set approval_status='3' where id_='"+id_+"'";
		String sql2="insert into archive_borrow_message(id,tid,description) values('"+AOSId.uuid()+"','"+id_+"','"+qDto.getString("unagain_why")+"')";
		try {
			jdbcTemplate.execute(sql);
			jdbcTemplate.update(sql2);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public List<Map<String, Object>> openmessage(Dto qDto) {
		// TODO Auto-generated method stub
		String id_=qDto.getString("ids").split(",")[0];
		String sql="select * from archive_borrow_message where tid='"+id_+"'";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	public List<Map<String, Object>> listuserExamine(String user) {
		// TODO Auto-generated method stub
		String sql="select * from archive_borrow where 1=1 and login_card='"+user+"' and flag <>1";
		return jdbcTemplate.queryForList(sql);
	}

	public boolean updateReadBorrow(UserInfoVO userInfoVO, Dto qDto) {
		// TODO Auto-generated method stub
		String sql="update archive_borrow set flag='1' where login_card='"+userInfoVO.getAccount_()+"' and id_='"+qDto.getString("id_")+"'";
		try {
			jdbcTemplate.update(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * 档案续借提交
	 * 
	 * @author PX
	 * @param qDto
	 *
	 * 2018-12-6
	 * @return 
	 */
	public boolean updateRenewalBorrow(Dto qDto) {
		// TODO Auto-generated method stub
		String exprie_date=qDto.getString("expire_date");
		String sql="update archive_borrow set expire_date='"+exprie_date+"' , expire_message='0' where id_='"+qDto.getString("id_")+"'";
		try {
			jdbcTemplate.update(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	public boolean returnRenewalBorrow(Dto qDto) {
		// TODO Auto-generated method stub
		String sql="update archive_borrow set borrow_status='2' where id_='"+qDto.getString("id_")+"'";
		try {
			jdbcTemplate.update(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public List<Map<String, Object>> getFiles(Dto qDto) {
		// TODO Auto-generated method stub
		Dto out=Dtos.newDto();
		String id_=qDto.getString("id_");
		String tablename=qDto.getString("tablename");
		String sql="select * from "+tablename+"_path where tid='"+id_+"' order by _index";
		try {
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;

		}
	}

	/**
	 * 得到当前用户的岗位
	 * @param outDto
	 * @return
	 */
	@RequestMapping("getPostUser")
	public String getPostUser(Dto outDto) {
		String user=outDto.getString("user");
		String posts="";
		String sql="select aos_sys_post.name_ as name_ from aos_sys_user,aos_sys_post,aos_sys_user_post where aos_sys_user.id_=aos_sys_user_post.user_id_ and aos_sys_user_post.post_id_ = aos_sys_post.id_" +
				" and aos_sys_user.account_='"+user+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				if(i==0){
					posts=(String)list.get(i).get("name_");
				}else{
					posts+=","+(String)list.get(i).get("name_");
				}
			}
		}
		return posts;
	}

	public String findModule(String id_) {
		String sql="select tablename from aos_sys_module where id_='"+id_+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			return (String)list.get(0).get("tablename");
		}else{
			return "";
		}
	}
	/**
	 *
	 * 查询记录总数
	 *
	 * @return
	 */
	public int getPageTotal(Dto qDto) {
		String query = queryConditions2(qDto);
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
				+ " WHERE 1=1 " + query ;
		return jdbcTemplate.queryForInt(sql);
	}
	/**
	 *
	 * 自助查询记录总数
	 *
	 * @return
	 */
	public int getPageTotal_zizhu(Dto qDto) {
		String query = "and sfkf='开放'";
		String text=qDto.getString("text");
		query+=" and ( dh like '%"+text+"%'";
		query+=" or qzdw like '%"+text+"%'";
		query+=" or tm like '%"+text+"%'";
		query+=" or wjbh like '%"+text+"%'";
		query+=" or zrz like '%"+text+"%'";
		query+=" or cwrq like '%"+text+"%'";
		query+=" or cz_bh like '%"+text+"%' ) ";
		String fieldName;
		String enfield = "id_";
		Integer listsum=0;
		String sql="select count(id_) as counts  from jnws  where 1=1 "+query;
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		if(listDto!=null&&listDto.size()>0){
			listsum=Integer.valueOf(listDto.get(0).get("counts").toString());
		}
		String sql2="select count(id_)  from gdws  where 1=1 "+query;
		List<Map<String,Object>> listDto2 = jdbcTemplate.queryForList(sql);
		if(listDto!=null&&listDto.size()>0){
			listsum+=Integer.valueOf(listDto.get(0).get("counts").toString());
		}
		return listsum;
	}
	/**
	 *
	 * 查询记录总数
	 *
	 * @return
	 */
	public int getPageTotal3(Dto qDto) {
		String query = qDto.getString("query");
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
				+ " WHERE 1=1 " + query ;
		return jdbcTemplate.queryForInt(sql);
	}
	/**
	 *
	 * 查询记录总数
	 *
	 * @return
	 */
	public int getPageTotal5(Dto qDto) {
		String query = queryConditions2(qDto);
		String querySession = qDto.getString("querySession");
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
				+ " WHERE 1=1 " + query +" "+querySession;
		return jdbcTemplate.queryForInt(sql);
	}
	/**
	 *
	 * 查询记录总数(自助查档)
	 *
	 * @return
	 */
	public int getPageTotal4(Dto qDto) {
		String text=qDto.getString("text");
		String tablename=qDto.getString("tablename");
		String query="";
		List<Archive_tablefieldlistPO> titleDtos = getDataFieldListTitle(tablename);
		if(titleDtos!=null&&titleDtos.size()>0){
			for(int i=0;i<titleDtos.size();i++){
                if(i==0&&i==titleDtos.size()-1){
                    query+=" and  "+titleDtos.get(i).getFieldenname()+" like '%"+text+"%'";
                }else if(i==0){
                    query+=" and ( "+titleDtos.get(i).getFieldenname()+" like '%"+text+"%'";
                }else if(i==titleDtos.size()-1){
                    query+=" or "+titleDtos.get(i).getFieldenname()+" like '%"+text+"%' ) ";
                }else{
                    query+=" or "+titleDtos.get(i).getFieldenname()+" like '%"+text+"%' ";
                }
			}
		}
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
				+ " WHERE 1=1 " + query ;
		return jdbcTemplate.queryForInt(sql);
	}
	/**
	 *
	 * 查询记录总数
	 *
	 * @return
	 */
	public int getPageTotal2(Dto qDto) {
		String query = queryConditions2(qDto);
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
				+ " WHERE 1=1 " + query ;
		return jdbcTemplate.queryForInt(sql);
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
		String query=qDto.getString("query");
		String sql="";
		if(query==null||query=="null"||query==""){
			sql = "WITH aos_query_ AS (SELECT " + enfield + " FROM "
					+ qDto.getString("tablename") + " WHERE 1=1) " + "SELECT * FROM aos_query_ ";
		}else{
			sql = "WITH aos_query_ AS (SELECT " + enfield + " FROM "
					+ qDto.getString("tablename") + " WHERE 1=1 "
					+ query + " ) " + "SELECT * FROM aos_query_ ";
		}

		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;

	}
	/**
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
	public Dto addSFXX(Dto qDto){
		Dto outDto = Dtos.newDto();

		String columns = "id_";
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
			values =  values+ "," +val;
		}
		String sql = " INSERT INTO " + "archive_zzremote" + " ("
				+ "id_,zzbh,xm,xb,mzgj,cs,dz,sfzh,qfjg,textresult,textjson,state" + ") VALUES ('" + AOSId.uuid() + "'"
				+ values +"," +"'1'"
				+ ")";
		jdbcTemplate.execute(sql);
		//此时查询哪些字段是自动加1
		// List<Archive_tableinputPO> list = archive_tableinputMapper.
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，加存成功。");
		return outDto;
	}

	/**
	 *
	 *
	 * @param qDto
	 * @param session
	 * @return
	 */
	public List<Dto> getDataFieldListDisplayAll_zz(Dto qDto, HttpSession session) {
		String sql = "";
		String query = queryConditions2(qDto);
		String fieldName;
		String enfield = "id_";
		String xm = qDto.getString("text_name");
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit) + 1;
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAllAll(qDto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
		if (qDto.getString("page") != null && qDto.getString("page") != "") {

			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+"and tm like  "+"'%"+xm+"%'"
					+ query
					+ ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ pageStart
					+ " AND "
					+ limit
					* page
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		} else {
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
					+ orderenfield
					+ ") AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ qDto.getString("tablename")
					+ " where 1=1  "
					+ query
					+ ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}
	/**
	 *
	 *
	 * @param qDto
	 * @param session
	 * @return
	 */
	public List<Map<String,Object>> getDataFieldListDisplayAll_zizhu(Dto qDto, HttpSession session) {
		String text=qDto.getString("text");
		String tablename=qDto.getString("tablename");
		String query="";
		List<Archive_tablefieldlistPO> titleDtos = getDataFieldListTitle(tablename);
		if(titleDtos!=null&&titleDtos.size()>0){
			for(int i=0;i<titleDtos.size();i++){
				if(i==0&&i==titleDtos.size()-1){
					query+=" and  "+titleDtos.get(i).getFieldenname()+" like '%"+text+"%'";
				}else if(i==0){
					query+=" and ( "+titleDtos.get(i).getFieldenname()+" like '%"+text+"%'";
				}else if(i==titleDtos.size()-1){
					query+=" or "+titleDtos.get(i).getFieldenname()+" like '%"+text+"%' ) ";
				}else{
					query+=" or "+titleDtos.get(i).getFieldenname()+" like '%"+text+"%' ";
				}
			}
		}
		String fieldName;
		String enfield = "id_";
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit) + 1;
		String sql="select *  from "+tablename+"  where 1=1 "+query+" order by id_ offset "+pageStart+" rows fetch next "+limit+" rows only";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}
	/**
	 *
	 *
	 * @param qDto
	 * @param session
	 * @return
	 */
	public List<Map<String,Object>> getDataFieldListDisplayAll_all(Dto qDto, HttpSession session) {
		String text=qDto.getString("text");
		//此时进行门类查询，查询到门类中字段包含档号和题名的列表
		List<Map<String, Object>> linkedList = getTablenameforQuery("dh", "tm", text);
		//list集合进行分页
		return linkedList;
	}
	public List<Map<String, Object>> startPage(List<Map<String, Object>> list, Integer pageNum,
								 Integer pageSize) {
		if (list == null) {
			return null;
		}
		if (list.size() == 0) {
			return null;
		}
		Integer count = list.size(); // 记录总数
		Integer pageCount = 0; // 页数
		if (count % pageSize == 0) {
			pageCount = count / pageSize;
		} else {
			pageCount = count / pageSize + 1;
		}
		int fromIndex = 0; // 开始索引
		int toIndex = 0; // 结束索引

		if (pageNum != pageCount) {
			fromIndex = (pageNum - 1) * pageSize;
			toIndex = fromIndex + pageSize;
		} else {
			fromIndex = (pageNum - 1) * pageSize;
			toIndex = count;
		}
		return list.subList(fromIndex, toIndex);
	}
	private List<Map<String, Object>> getTablenameforQuery(String dh, String tm,String text) {
		String sql="select * from archive_tablename where  id_ in ( select tid from archive_tablefieldList  where " +
				"archive_tablefieldList.FieldEnName ='"+dh+"' and tid in ( select tid from archive_tablefieldList where archive_tablefieldList.FieldEnName ='"+tm+"')) ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List<Map<String, Object>> linked = new ArrayList<Map<String,Object>>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String sql2="select id_,_path,tm,dh from "+ list.get(i).get("TableName") + " where tm like '%"+text+"%'";
				List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
				if(list2!=null&&list2.size()>0){
					for(int j=0;j<list2.size();j++){
						list2.get(j).put("tablename",list.get(i).get("TableName"));
						list2.get(j).put("tabledesc",list.get(i).get("TableDesc"));
						linked.add(list2.get(j));
					}
				}
			}
		}
	return linked;
	}

	public Dto updateMakeDetail_select(Dto dto) {
        Dto outDto=Dtos.newDto();
        try {
            String id_=  dto.getString("id_");
            String cxjg=dto.getString("cxjg");
            Archive_remotePO archive_remotePO=new Archive_remotePO();
            archive_remotePO.setId_(id_);
            archive_remotePO.setCxjg(cxjg);
            archive_remoteMapper.updateByKey(archive_remotePO);
            outDto.setAppCode(AOSCons.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            outDto.setAppCode(AOSCons.ERROR);
        }
        return outDto;
    }

    /**
     * 把用户信息存放到zizhu信息中
     * @param userInfoVO
     */
    public String addzizhuMessage(UserInfoVO userInfoVO) {
		String bh="";

    	String sql="select zzbh from archive_zizhu_apply order by zzbh desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			String zzbh = (String)list.get(0).get("zzbh");
			if (zzbh != null && zzbh.length() != 0) {
				String index = zzbh.substring(zzbh.length()-4, zzbh.length());
				int lx = Integer.valueOf(index) + 1;
				bh=String.format("%4d", lx).replace(" ", "0");
			}else {
				bh= "0001";
			}
		}else{
			bh= "0001";
		}
		Archive_zizhu_applyPO archive_zizhu_applyPO=new Archive_zizhu_applyPO();
        archive_zizhu_applyPO.setXm(userInfoVO.getName_());
        archive_zizhu_applyPO.setZzbh("ZZ"+new SimpleDateFormat("yyyy").format(new Date())+bh);
        archive_zizhu_applyPO.setId_(AOSId.uuid());
        archive_zizhu_applyMapper.insert(archive_zizhu_applyPO);
        return archive_zizhu_applyPO.getId_();
    }
	public void updateZizhuMessage(Dto inDto) {
    	Archive_zizhuremote_detailPO archive_zizhuremote_detailPO=new Archive_zizhuremote_detailPO();
    	archive_zizhuremote_detailPO.setId_(AOSId.uuid());
    	archive_zizhuremote_detailPO.setTablename(inDto.getString("tablename"));
    	archive_zizhuremote_detailPO.setTid(inDto.getString("zz_id_"));
    	archive_zizhuremote_detailPO.setFormid(inDto.getString("id_"));
    	archive_zizhuremote_detailMapper.insert(archive_zizhuremote_detailPO);
	}

	public List<Map<String, Object>> listZiZhuMake(Dto qDto) {
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit);
		String sql="select *  from  archive_zizhu_apply  where 1=1 order by zzbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	public int getPageTotal_ZizhuMake(Dto inDto) {
    	Integer listsum=0;
		String sql="select count(id_) as counts  from archive_zizhu_apply  where 1=1 ";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		if(listDto!=null&&listDto.size()>0){
			listsum=Integer.valueOf(listDto.get(0).get("counts").toString());
		}
		return listsum;
	}
	public List<Map<String, Object>> getDataFieldListDisplay_ZiZhu(Dto qDto) {
		// 排列方式，页码，每页条目数
		String formid=qDto.getString("formid");

		String sql="select *  from  archive_zizhuremote_detail where  formid='"+formid+"'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		String tablename="";
		if(list!=null&&list.size()>0){
			tablename=(String)list.get(0).get("tablename");
		}else{
		    return list;
        }
		String sql2="select * from "+tablename+" where id_ in ( select tid from archive_zizhuremote_detail where formid='"+formid+"')";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql2);
		if(listDto!=null&&listDto.size()>0){
			for(int i=0;i<listDto.size();i++){
				listDto.get(i).put("tablename",tablename);
			}
		}
		return listDto;
	}
	public int getPageTotalData_zizhu(Dto inDto) {
		// 排列方式，页码，每页条目数
		String formid=inDto.getString("formid");

		String sql="select *  from  archive_zizhuremote_detail where  formid='"+formid+"'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		return list.size();
	}

	/**
	 * 结合用户信息进行自助查看操作
	 * @param inDto
	 * @return
	 */
	public boolean getZizhuMessage(Dto inDto) {
		String id_=inDto.getString("id_");
		String zz_id_=inDto.getString("zz_id_");
		String tablename=inDto.getString("tablename");
		String sql="select * from archive_zizhuremote_detail where formid ='"+zz_id_+"' and tablename='"+tablename+"' and tid='"+id_+"'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			String sql2="select * from archive_zizhu_apply where id_='"+zz_id_+"'";
			List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql2);
			if(maps!=null&&maps.size()>0){
				String nftg = (String)maps.get(0).get("nftg");
				if(nftg!=null&&nftg!=""&&nftg.equals("可以提供")){
					return true;
				}else{
					return false;
				}
			}
			return false;
		}else{
			return false;
		}
	}
}
