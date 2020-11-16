package cn.osworks.aos.system.modules.service.topic;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TopicService extends JdbcDaoSupport {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public DataService dataService;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private Aos_sys_userMapper aos_sys_user;
	@Autowired
	private Archive_topicMapper archive_topicMapper;
	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	@Autowired
	private Archive_compilationMapper archive_compilationMapper;
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
	public List<Map<String,Object>> findTablename(String id_) {
		// TODO Auto-generated method stub
		String sql="select * from archive_topic where id_='"+id_+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	/**
	 *
	 * 显示表头（常规组合方式）
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
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ qDto.getPageLimit()
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
	public List<Dto> getDataFieldListDisplayAll(Dto qDto, HttpSession session) {
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
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ qDto.getPageLimit()
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
	 * 查询记录总数
	 *
	 * @return
	 */
	public int getPageTotal2(Dto qDto) {
		String query = qDto.getString("query");
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

		return list;
	}

	/**
	 * 保存专题
	 * @param inDto
	 */
	public Integer savetopic(Dto inDto) {
		try{
			//查询是否存在重复的专题名称
			Dto outDto=Dtos.newOutDto();
			outDto.put("name_",inDto.getString("name_"));
			List<Archive_topicPO> list2=archive_topicMapper.list(outDto);
			if(list2!=null&&list2.size()>0){
				return -2;
			}
			Archive_topicPO archive_topicPO=new Archive_topicPO();
			AOSUtils.copyProperties(inDto,archive_topicPO);
			archive_topicPO.setId_(AOSId.uuid());
			archive_topicMapper.insert(archive_topicPO);
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 得到对应的id的tablename
	 * @param id_
	 * @return
	 */
	public String getTablename(String id_) {
		Archive_tablenamePO archive_tablenamePO=archive_tablenameMapper.selectByKey(id_);
		return archive_tablenamePO.getTablename();
	}

	/**
	 *
	 * @param inDto
	 */
	public boolean addTopicTableFieldList(Dto inDto) {
		try{
			String tablename_id=inDto.getString("id_");
			String query=inDto.getString("query");
			String topic_id_=inDto.getString("topic_id_");
			Archive_tablenamePO archive_tablenamePO=archive_tablenameMapper.selectByKey(tablename_id);
			String tablename=archive_tablenamePO.getTablename();
			Archive_topicPO archive_topicPO=new Archive_topicPO();
			inDto.put("id_",topic_id_);
			inDto.put("tablename",tablename);
			inDto.put("query",query);
			AOSUtils.copyProperties(inDto,archive_topicPO);
			archive_topicMapper.updateByKey(archive_topicPO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}

	/**
	 *
	 * @param id_
	 * @return
	 */
	public Archive_topicPO getTopicById(String id_) {
		Archive_topicPO archive_topicPO=archive_topicMapper.selectByKey(id_);
		return archive_topicPO;
	}

    public boolean updateTopic(Dto inDto) {
		try{
			Archive_topicPO archive_topicPO=new Archive_topicPO();
			AOSUtils.copyProperties(inDto,archive_topicPO);
			archive_topicMapper.updateByKey(archive_topicPO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
    }

	/**
	 * 通过id删除
	 * @param id_
	 * @return
	 */
	public boolean deleteTopic(String id_) {
		try{
			archive_topicMapper.deleteByKey(id_);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 得到门类列表
	 * @param inDto
	 */
	public List<Archive_tablefieldlistPO> getQueryTitle(Dto inDto) {
		List<Archive_tablefieldlistPO> list=new ArrayList<Archive_tablefieldlistPO>();
		try{
			Archive_topicPO archive_topicPO=archive_topicMapper.selectByKey(inDto.getString("topic_id_"));
			String tablename=archive_topicPO.getTablename();
			Dto out=Dtos.newDto();
			out.put("tablename",tablename);
			Archive_tablenamePO archive_tablenamePO=archive_tablenameMapper.selectOne(out);
			Dto outDto=Dtos.newDto();
			outDto.put("tid",archive_tablenamePO.getId_());
			list=archive_tablefieldlistMapper.list(outDto);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return list;
		}
	}
	public Dto gettable(Dto inDto) {
		String topic_id_=inDto.getString("topic_id_");
		Archive_topicPO archive_topicPO=archive_topicMapper.selectByKey(topic_id_);
		String tablename=archive_topicPO.getTablename();
		Dto dto=Dtos.newDto();
		dto.put("tablename",tablename);
		Archive_tablenamePO archive_tablenamePO=archive_tablenameMapper.selectOne(dto);
		dto.put("tabledesc",archive_tablenamePO.getTabledesc());
		return dto;
	}

	public String compilationForm(Dto inDto) {
		try{
			Archive_compilationPO archive_compilationPO=new Archive_compilationPO();
			AOSUtils.copyProperties(inDto,archive_compilationPO);
			archive_compilationPO.setId_(AOSId.uuid());
			archive_compilationMapper.insert(archive_compilationPO);
			return archive_compilationPO.getId_();
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}

	}
}
