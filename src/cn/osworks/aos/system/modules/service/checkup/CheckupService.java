package cn.osworks.aos.system.modules.service.checkup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.AOSXmlOptionsHandler;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import cn.osworks.aos.system.modules.service.resource.ModuleService;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;

@Service
public class CheckupService extends JdbcDaoSupport {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public DataService dataService;
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private Aos_sys_orgMapper aos_sys_orgMapper;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private Archive_topicMapper archive_topicMapper;
	@Autowired
	private Aos_sys_userMapper aos_sys_userMapper;
	@Autowired
	private Archive_checkup_rwMapper archive_checkup_rwMapper;
	@Autowired
	private Archive_check_tablenameidMapper archive_check_tablenameidMapper;
	@Autowired
	private Archive_checkup_seminarMapper archive_checkup_seminarMapper;
	@Autowired
	private Checkup_topic_tablenameidMapper checkup_topic_tablenameidMapper;
	@Autowired
	private Aos_sys_moduleMapper aos_sys_moduleMapper;
	@Autowired
	private Aos_sys_module_userMapper aos_sys_module_userMapper;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private Archive_topic_tablenameidMapper archive_topic_tablenameidMapper;
	@Autowired
	private Checkup_topic_logMapper checkup_topic_logMapper;
	@Autowired
	private Checkup_topicMapper checkup_topicMapper;
	@Resource
	public void setJd(JdbcTemplate jd) {
		super.setJdbcTemplate(jd);
	}
	public static String filePath = "";
	public static String linkAddress = "";
	public static String topicAddress = "";
	public final String DEFAULT_PICTURE_FOLDER="pictures";
	public final String DEFAULT_HTML_TYPE=".html";
	// 通过静态代码块读取配置文件
	static {
		try {
			Properties props = new Properties();
			InputStream in = DataService.class
					.getResourceAsStream("/config.properties");
			props.load(in);
			filePath = props.getProperty("filePath");
			linkAddress = props.getProperty("linkAddress");
			topicAddress = props.getProperty("topicAddress");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	/**
	 *
	 * 页面初始化

	 * @return
	 */
	public List<Dto> getDataUsersListTitle(Dto qDto) {
		Dto dto=Dtos.newDto();
		dto.setOrder("id_ DESC");
		dto.put("delete_flag_",0);
		dto.put("org_cascade_id_",0);
		dto.put("limit",50);
		dto.put("start",0);
		String sql="select account_,name_ from aos_sys_user where delete_flag_='0' and org_cascade_id_='0' order by id_ desc";
		List<Dto> userInfos =(List) jdbcTemplate.queryForList(sql);
		return userInfos;
	}
	//得到用户列表
	public List<Map<String,Object>> getUserList(String user) {
		//根据用户名称得到它的id
		String sql="select org_id_ from aos_sys_user where account_='"+user+"'";
		List<Map<String,Object>> userlist=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			String id_=(String)list.get(0).get("org_id_");
			String sql2="select * from aos_sys_user where org_id_='"+id_+"' and account_<>'"+user+"'";
			userlist=jdbcTemplate.queryForList(sql2);
		}
		return userlist;
	}
	//得到所有用户列表
	public List<Map<String,Object>> getUserAllList(String user) {
		//根据用户名称得到它的id
		List<Map<String,Object>> userlist=new ArrayList<Map<String,Object>>();
			String sql2="select * from aos_sys_user ";
			userlist=jdbcTemplate.queryForList(sql2);
		return userlist;
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
	/*
	 * 查询表名
	 */
	public String findTablename(String id_) {
		// TODO Auto-generated method stub
		String sql="select tablename from archive_check_tablenameid where pid='"+id_+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			return (String)list.get(0).get("tablename");
		}else{
			return "";
		}
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
		// 此时把query存入到session中
		session.setAttribute("query", query);
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
					+ " ) SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ pageStart
					+ " AND "
					+ limit
					* page
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
					+ "  ) SELECT * FROM aos_query_  ORDER BY aos_rn_";
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
		String query = queryConditions2(qDto);
		// 此时把query存入到session中
		session.setAttribute("query", query);

		//查询年度条件是否存在
		String nd=qDto.getString("nd");
		if(nd!=null&&nd!="null"&&!nd.equals(null)&&!nd.equals("null")&&!nd.equals("")&&nd.length()>0){
			query=query+" and nd='"+nd+"'";
		}
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
					+ " ) SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ pageStart
					+ " AND "
					+ limit
					* page
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
					+ "  ) SELECT * FROM aos_query_  ORDER BY aos_rn_";
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
	public List<Map<String,Object>> getDataFieldListDisplayAll2(Dto qDto, HttpSession session) {
		String sql="select * from "+qDto.getString("tablename")+" where id_ in (select tablename_id from checkup_topic_tablenameid where pid='"+qDto.getString("zt_id")+"')";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		return listDto;
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
	 * 存入信息
	 * 
	 * @author PX
	 * @param outdto
	 * 
	 *            2018-8-29
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	public Dto jymessage(Dto outdto, HttpSession session) throws ParseException {
		// TODO Auto-generated method stub
		Dto out = Dtos.newDto();
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		String user = userInfoVO.getAccount_();
		out.put("user", user);
		return out;
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
	 * 开始鉴定
	 * 
	 * @author PX
	 * @param dto
	 * @param query
	 * 
	 *            2018-11-27
	 * @param request
	 */
	public Dto startcheckup(Dto dto, String query, HttpServletRequest request) {
		// TODO Auto-generated method stub
		// 1.判断是否含有成文日期和保管期限代码字段
		Dto outDto = Dtos.newDto();
		String tablename = dto.getString("tablename");

		String sql = "select FieldEnName  from archive_TableFieldList where tid in ( select id_ from archive_tablename where TableName='"
				+ tablename + "') ";
		int index = 0;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			String[] str = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				str[i] = (String) list.get(i).get("FieldEnName");
			}
			// 判断是否存在保管期限代码和成文日期字段
			if (Arrays.asList(str).contains("cwsj")) {
				if (Arrays.asList(str).contains("bgqxdm")) {
					// 此时进行鉴定
					// 通过查询得到进行鉴定的所有档案条目数据
					String sql2 = "select * from " + tablename + " where 1=1 "
							+ query + " and _jd is null or _jd = 0";
					List<Map<String, Object>> datalist = jdbcTemplate
							.queryForList(sql2);
					if (datalist != null && datalist.size() > 0) {
						for (int t = 0; t < datalist.size(); t++) {
							String cwsj = (String) datalist.get(t).get("cwsj");
							String bgqxdm = (String) datalist.get(t).get(
									"bgqxdm");
							String id_ = (String) datalist.get(t).get("id_");
							if (cwsj == null || cwsj == "" || bgqxdm == null
									|| bgqxdm == "") {
								continue;
							}
							// 获取当前时间
							String dateNowStr = getData();
							// 获取保管期限值
							int bgqx = getbgqx(bgqxdm);
							if (bgqx == 0) {
								continue;
							}
							if (bgqx == 10000) {
								continue;
							}
							if (cwsj.matches("[0-9]+")) {
								if (cwsj.length() != 8) {
									continue;
								}
								// 加上保管期限时间
								String nd = cwsj.substring(0, 4);
								String gqnd = Integer.valueOf(nd) + bgqx + "";
								String gqsj = gqnd + cwsj.substring(4, 8);
								try {
									// 把年月日转成毫秒值
									long gqtime = new SimpleDateFormat(
											"yyyyMMdd").parse(gqsj).getTime();
									long nowtime = new SimpleDateFormat(
											"yyyyMMdd").parse(dateNowStr)
											.getTime();
									if (gqtime > nowtime) {
										continue;
									} else {
										// 档案过期
										// 修改标记
										String sql3 = "update " + tablename
												+ " set _jd='1' where id_='"
												+ id_ + "'";
										jdbcTemplate.execute(sql3);
										index = index + 1;
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									continue;
								}
							} else {
								continue;
							}
						}
					}
					// 把销毁信息存入到数据库中
					String sql4 = "insert into archive_appraise (id_,tablename,tabledesc,appraise_time,users,name,appraise_count,destory_count) values('"
							+ AOSId.uuid()
							+ "','"
							+ dto.getString("tablename")
							+ "','"
							+ dto.getString("tabledesc")
							+ "','"
							+ dto.getString("checkupData")
							+ "','"
							+ dto.getString("user")
							+ "','"
							+ dto.getString("person")
							+ "','"
							+ dto.getString("_total")
							+ "','" 
							+ index + "')";
					jdbcTemplate.execute(sql4);
				} else {
					outDto.setAppCode(-2);
					return outDto;
				}
			} else {
				outDto.setAppCode(-1);
				return outDto;
			}
		}
		outDto.setAppCode(index);
		return outDto;

	}

	/**
	 * 获取保管期限值
	 * 
	 * @author PX
	 * @return
	 * 
	 *         2018-11-27
	 */
	private int getbgqx(String bgqxdm) {
		// TODO Auto-generated method stub
		if (bgqxdm.equalsIgnoreCase("C")) {
			return 30;
		} else if (bgqxdm.equalsIgnoreCase("D10")) {
			return 10;
		} else if (bgqxdm.equalsIgnoreCase("D30")) {
			return 30;
		} else if (bgqxdm.equalsIgnoreCase("D")) {
			return 10;
		} else if (bgqxdm.equalsIgnoreCase("Y")) {
			return 10000;
		} else {
			return 0;
		}
	}

	/**
	 * 获取当前时间
	 * 
	 * @author PX
	 * @return
	 * 
	 *         2018-11-27
	 */
	private String getData() {
		// TODO Auto-generated method stub
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNowStr = sdf.format(d);
		return dateNowStr;
	}

	/**
	 * 查询待销毁列表
	 * 
	 * @author PX
	 * @param dto
	 * 
	 *            2018-11-28
	 * @return
	 */
	public List<Map<String, Object>> listwaitdestroy(Dto dto) {
		// TODO Auto-generated method stub
		String tablename = dto.getString("tablename");
		String enfield = "id_";
		String fieldName="";
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(dto.getString("tablename"));
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 1) {
				orderenfield = enfield;
			}
		}
		int start=(Integer.valueOf(dto.getString("page"))-1)*dto.getPageLimit();
		String sql = "select * from " + tablename + " where _jd='1'";
		sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY id_) AS aos_rn_,"
				+ "id_,_xuhao,tm,nd,dh,ys,bgqxdm,_oldnd,bz,cwsj"
				+ " FROM "
				+ dto.getString("tablename")
				+ " WHERE 1=1 and _jd='1') "
				+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
				+ (start+1)
				+ " AND "
				+ dto.getPageLimit()
				* Integer.valueOf(dto.getString("page"))
				+ " ORDER BY aos_rn_";
		List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql);
		LinkedList<Map<String, Object>> linkedlist=new LinkedList<Map<String, Object>>();
		
		//进行迭代，把已保管年限加到里面
		if(list2!=null&&list2.size()>0){
			for(int i=0;i<list2.size();i++){
				try {
					String cwrq=list2.get(i).get("cwsj")+"";
					String cwnd=cwrq.substring(0, 4);
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				        Date date = new Date();
				        String nownd = sdf.format(date);
				        Integer datend=Integer.valueOf(nownd)-Integer.valueOf(cwnd);
				        list2.get(i).put("_oldnd",datend);
				        
				        list2.get(i).put("_xuhao",i);
				        list2.get(i).remove("aos_rn_");
				} catch (Exception e) {
					// TODO: handle exception
						list2.get(i).put("_oldnd",0);
						list2.get(i).remove("aos_rn_");
				}
			}
		}
		return list2;
	}

	/**
	 * 销毁档案
	 * 
	 * @author PX
	 * @param qDto
	 * 
	 *            2018-11-28
	 * @return
	 */
	public boolean deleteData(Dto qDto) {
		// TODO Auto-generated method stub
		String tablename = qDto.getString("tablename");
		String id_ = qDto.getString("id_");
		try {
			String sql = "delete  from " + tablename + " where id_='" + id_
					+ "'";
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * 还原档案
	 * 
	 * @author PX
	 * @param qDto
	 * 
	 *            2018-11-28
	 * @return
	 */
	public boolean returnData(Dto qDto) {
		// TODO Auto-generated method stub
		String tablename = qDto.getString("tablename");
		String id_ = qDto.getString("id_");
		try {
			String sql = "update " + tablename + " set _jd = NULL where id_='"
					+ id_ + "'";
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 鉴定记录
	 * 
	 * @author PX
	 * @return
	 *
	 * 2018-11-28
	 */
	public List<Map<String, Object>> listappraisedestroy() {
		// TODO Auto-generated method stub
		String sql="select * from archive_appraise";
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 得到销毁数据当前要展示页面的
	 * 
	 * @author PX
	 * @param sql
	 *
	 * 2019-3-19
	 * @return 
	 */
	public List<Map<String, Object>> getlistString(String sql) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	/**
	 * 得到制定门类总数
	 * 
	 * @author PX
	 * @param qDto
	 *
	 * 2019-3-19
	 * @return 
	 */
	public int getSumPage(Dto qDto) {
		// TODO Auto-generated method stub
		String sql="select * from "+qDto.getString("tablename")+" where _jd='1'";
		try {
			return jdbcTemplate.queryForList(sql).size();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		
	}
	public boolean addForm(Dto qDto){
		//根据所有的数据进行添加
		try{
			Archive_checkup_rwPO archive_checkup_rwPO=new Archive_checkup_rwPO();
			AOSUtils.copyProperties(qDto,archive_checkup_rwPO);

			archive_checkup_rwPO.setId_(AOSId.uuid());
			archive_checkup_rwMapper.insert(archive_checkup_rwPO);
			String[] selections = qDto.getRows();

			for (String id_ : selections) {
				Archive_check_tablenameidPO archive_check_tablenameidPO=new Archive_check_tablenameidPO();
				archive_check_tablenameidPO.setId_(AOSId.uuid());
				archive_check_tablenameidPO.setCheck_type(qDto.getString("checkup_type"));
				archive_check_tablenameidPO.setPid(archive_checkup_rwPO.getId_());
				archive_check_tablenameidPO.setTablename(qDto.getString("tablename"));
				archive_check_tablenameidPO.setTablename_id(id_);
				archive_check_tablenameidMapper.insert(archive_check_tablenameidPO);
			}
			return  true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 操作记录
	 * @param qDto
	 */
	public List<Archive_checkup_rwPO> listopertion(Dto qDto) {
		List<Archive_checkup_rwPO> list=archive_checkup_rwMapper.list(qDto);
		return list;
	}
	/**
	 * 查询当前用户编研状态
	 * @param userInfo
	 * @return
	 */
	public String findCheckup_flag(UserInfoVO userInfo) {
		String checkup_flag="";
		String sql="select * from aos_sys_user where account_='"+userInfo.getAccount_()+"'";
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0) {
			checkup_flag=(String)list.get(0).get("checkup_flag_");
		}
		return checkup_flag;
	}
	public String findButton_jianding(UserInfoVO userInfoVO) {
		String compilation_flag="";
		String sql="select * from aos_sys_user where account_='"+userInfoVO.getAccount_()+"'";
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0) {
			compilation_flag=(String)list.get(0).get("jianding_module");
		}
		return compilation_flag;
	}
	/** 操作记录
	 * @param qDto
	 */
	public boolean adddetails(Dto qDto,UserInfoVO userInfoVO) {
		try{
			Archive_checkup_rwPO archive_checkup_rwPO=new Archive_checkup_rwPO();
			AOSUtils.copyProperties(qDto,archive_checkup_rwPO);
			archive_checkup_rwPO.setFirst_compilationperson(qDto.getString("first_fieldennames"));
			archive_checkup_rwPO.setFirst_compilationperson_cn(qDto.getString("first_fieldcnnames"));
			archive_checkup_rwPO.setNext_compilationperson(qDto.getString("next_fieldennames"));
			archive_checkup_rwPO.setNext_compilationperson_cn(qDto.getString("next_fieldcnnames"));
			archive_checkup_rwPO.setLast_compilationperson(qDto.getString("last_fieldennames"));
			archive_checkup_rwPO.setLast_compilationperson_cn(qDto.getString("last_fieldcnnames"));
			archive_checkup_rwMapper.updateByKey(archive_checkup_rwPO);
			//针对所有用户授予目录树权限
			addtree_user(qDto);
			//此时给初鉴人一个消息
			notificationService.SubmitJdFirst(qDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 对于所有用户都目录树赋权
	 * @param qDto
	 */
	private void addtree_user(Dto qDto) {
		String jdrs=qDto.getString("first_fieldennames");
		String fhrs=qDto.getString("next_fieldennames");
		String zsrs=qDto.getString("last_fieldennames");
		//添加到数组中,保证数组不重复就可以
		List list=new ArrayList<>();
		String[] jdr=jdrs.split(",");
		for(int i=0;i<jdr.length;i++){
			list.add(jdr[i]);
		}
		String[] fhr=fhrs.split(",");
		for(int i=0;i<fhr.length;i++){
			list.add(fhr[i]);
		}
		String[] zsr=zsrs.split(",");
		for(int i=0;i<zsr.length;i++){
			list.add(zsr[i]);
		}
		list= ridRepeat1(list);
		//迭代依次赋予权限
		addtreeuser(qDto,list);
	}
	public List<String> ridRepeat1(List<String> list) {
		List<String> listNew = new ArrayList<String>();
		for (String str : list) {
			if (!listNew.contains(str)) {
				listNew.add(str);
			}
		}
		return listNew;
	}


	/**
	 * 添加目录梳全线
	 * @param qDto
	 * @param list
	 */
	private void addtreeuser(Dto qDto, List list) {
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String user=(String)list.get(i);
				//得到用户和操作用户的id值
				String user_id=getUserId(user);
				String operator_id_= getUserId(qDto.getUserInfo().getAccount_());
				//得到当前任务的节点的id目录树值(在线课题下面的子节点目录树的id值)
				//String kt_children_id_=getModule_kt_id(qDto.getString("id_"));
				//判断是否存在授权，如果存在就调过，不过在添加
				//addTree_rw(user,user_id,operator_id_,kt_children_id_);
				//得到当前任务的节点的id目录数值的父id值（根据在线课题的子节点的id值，查找在线专题id值）
				//String kt_id_= getModule_parentid(kt_children_id_);
				//判断是否存在授权，如果存在就调过，不过在添加
				// addTree_rw(user,user_id,operator_id_,kt_id_);
				//得到任务的节点的id目录树值（在编课题下面的子节点目录树的id值）
				String sj_children_id_=getModule_sj_id(qDto.getString("id_"));
				//判断是否存在授权，如果存在就调过，不过在添加
				addTree_rw(user,user_id,operator_id_,sj_children_id_);
				//得到数据库id（根据专题数据下面的子节点的id值，得到专题数据的id值）
				//String sj_id_= getModule_parentid(sj_children_id_);
				//同时再添加任务的数据库
				//addTree_rw(user,user_id,operator_id_,sj_id_);

			}
		}
	}
	private String getModule_sj_id(String id_) {
		String module_id_="";
		Archive_checkup_rwPO archive_checkup_rwPO=archive_checkup_rwMapper.selectByKey(id_);
		Dto out=Dtos.newDto();
		out.put("name_",archive_checkup_rwPO.getTask_number()+archive_checkup_rwPO.getTask_name());
		List<Aos_sys_modulePO> list=aos_sys_moduleMapper.list(out);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String cascade_id_=list.get(i).getCascade_id_();
				// if(cascade_id_.contains("0.024.006")){
				module_id_=list.get(i).getId_();
				// }
			}
		}
		return module_id_;
	}
	/**
	 * 添加授权
	 * @param user
	 * @param user_id
	 * @param operator_id_
	 * @param module_id_
	 */
	private void addTree_rw(String user, String user_id, String operator_id_, String module_id_) {
		Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
		aos_sys_module_userPO.setUser_id_(user_id);
		aos_sys_module_userPO.setModule_id_(module_id_);

		aos_sys_module_userPO.setOperator_id_(operator_id_);
		aos_sys_module_userPO.setGrant_type_("1");
		String sql="select * from aos_sys_module_user where user_id_='"+user_id+"' and module_id_='"+module_id_+"' and grant_type_='1'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list==null||list.size()<=0){
			aos_sys_module_userPO.setId_(AOSId.uuid());
			aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
			aos_sys_module_userMapper.insert(aos_sys_module_userPO);
		}
	}
	/**
	 * 删除分类
	 */
	public boolean deleteexamine(Dto qDto){
		try{
			String[] selections = qDto.getRows();
			for (String id_ : selections) {
				archive_checkup_rwMapper.deleteByKey(id_);
			}
			return true;
		}catch (Exception e){
				e.printStackTrace();
				return false;
		}
	}
	public List<Map<String,Object>> listfirsttrial(Dto qDto) {
		String user=qDto.getString("user");
		List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
		String sql="select * from archive_check_rw  where first_trialperson like '%"+user+"%' and flag ='1'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object persons=list.get(i).get("first_trialperson");
				if(persons!=null&&persons!=""){
					String personss=(String)persons;
					String[] person=personss.split(",");
					for(int k=0;k<person.length;k++){
						if(user.equals(person[k])){
							listt.add(list.get(i));
						}
					}
				}
			}
		}
		return listt;
	}
    public List<Map<String,Object>> listnexttrial(Dto qDto) {
        String user=qDto.getString("user");
        List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
        String sql="select * from archive_checkup_rw  where next_compilationperson like '%"+user+"%' and flag ='2'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Object persons=list.get(i).get("next_compilationperson");
                if(persons!=null&&persons!=""){
                    String personss=(String)persons;
                    String[] person=personss.split(",");
                    for(int k=0;k<person.length;k++){
                        if(user.equals(person[k])){
                            listt.add(list.get(i));
                        }
                    }
                }
            }
        }
        return listt;
    }
	public List<Map<String,Object>> listnexttrial(Dto qDto,HttpSession session) {
		List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
		String sql="select * from archive_checkup_rw  where next_compilationperson like '%"+session.getAttribute("user")+"%' and flag ='2'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object persons=list.get(i).get("next_compilationperson");
				if(persons!=null&&persons!=""){
					String personss=(String)persons;
					String[] person=personss.split(",");
					for(int k=0;k<person.length;k++){
						if(session.getAttribute("user").equals(person[k])){
							listt.add(list.get(i));
						}
					}
				}
			}
		}
		return listt;
	}

	public List<Map<String,Object>> listlasttrial(Dto qDto) {
		String user=qDto.getString("user");
		List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
		String sql="select * from archive_checkup_rw  where last_compilationperson like '%"+user+"%' and flag ='3'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object persons=list.get(i).get("last_compilationperson");
				if(persons!=null&&persons!=""){
					String personss=(String)persons;
					String[] person=personss.split(",");
					for(int k=0;k<person.length;k++){
						if(user.equals(person[k])){
							listt.add(list.get(i));
						}
					}
				}
			}
		}
		return listt;
	}
	public List<Map<String,Object>> listlasttrial(Dto qDto,HttpSession session) {
		List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
		String sql="select * from archive_checkup_rw  where last_compilationperson like '%"+session.getAttribute("user")+"%' and flag ='3'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object persons=list.get(i).get("last_compilationperson");
				if(persons!=null&&persons!=""){
					String personss=(String)persons;
					String[] person=personss.split(",");
					for(int k=0;k<person.length;k++){
						if(session.getAttribute("user").equals(person[k])){
							listt.add(list.get(i));
						}
					}
				}
			}
		}
		return listt;
	}
	/**
	 * 查询审核类型
	 * @param id_
	 */
	public Archive_checkup_rwPO findCheck(String id_) {
		Archive_checkup_rwPO archive_checkup_rwPO=archive_checkup_rwMapper.selectByKey(id_);
		return archive_checkup_rwPO;
	}

	public boolean savefirstkfdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String first_kf_message=qDto.getString("first_kf_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setDescription(first_kf_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			String sql="update archive_checkup_rw set flag='2' , jdr_description='"+first_kf_message+"'  where id_='"+check_id_+"' ";
			jdbcTemplate.execute(sql);
			notificationService.SubmitKFFirst(qDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private String findcnuser(String enuser) {
		Dto out=Dtos.newDto();
		String cnuser="";
		out.put("account_",enuser);
		List<Aos_sys_userPO> list=aos_sys_userMapper.list(out);
		if(list!=null&&list.size()>0){
			cnuser=list.get(0).getName_();
		}
		return cnuser;
	}

	public boolean savenextkfdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String next_kf_message=qDto.getString("next_kf_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setDescription(next_kf_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			if(qDto.getString("next").equals("1")){
				String sql="update archive_checkup_rw set flag='3',fsr_description='"+next_kf_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.SubmitKFNext(qDto,userInfoVO);
			}else if(qDto.getString("next").equals("-1")){
				String sql="update archive_checkup_rw set flag='1',fsr_description='"+next_kf_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskKFFirst(qDto,userInfoVO);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}
	public boolean savelastkfdetails(Dto qDto, UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String last_kf_message=qDto.getString("last_kf_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			/*if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String id_=(String)list.get(i).get("id_");
					String _kaifang=(String)list.get(i).get("_kaifang");
					String sql="update "+tablename+" set _kaifang='"+_kaifang+"' where id_='"+id_+"'";
					jdbcTemplate.execute(sql);
				}
			}*/
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setDescription(last_kf_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			if(qDto.getString("next").equals("1")){
				String sql="update archive_checkup_rw set flag='4',zsr_description='"+last_kf_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskKFLastYes(qDto,userInfoVO);
			}else if(qDto.getString("next").equals("-1")){
				String sql="update archive_checkup_rw set flag='2',zsr_description='"+last_kf_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskKFLast(qDto,userInfoVO);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}
	public boolean savenextxhdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String next_xh_message=qDto.getString("next_xh_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			/*if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String id_=(String)list.get(i).get("id_");
					String _xh=(String)list.get(i).get("_xh");
					String sql="update "+tablename+" set _xh='"+_xh+"' where id_='"+id_+"'";
					jdbcTemplate.execute(sql);
				}
			}*/
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setDescription(next_xh_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			if(qDto.getString("next").equals("1")){
				String sql="update archive_checkup_rw set flag='3',fsr_description='"+next_xh_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.SubmitXHNext(qDto,userInfoVO);
			}else if(qDto.getString("next").equals("-1")){
				String sql="update archive_checkup_rw set flag='1',fsr_description='"+next_xh_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.SubmitXHFirst(qDto,userInfoVO);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}
	public boolean savelastxhdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String last_xh_message=qDto.getString("last_xh_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			/*if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String id_=(String)list.get(i).get("id_");
					String _xh=(String)list.get(i).get("_xh");
					String sql="update "+tablename+" set _xh='"+_xh+"' where id_='"+id_+"'";
					jdbcTemplate.execute(sql);
				}
			}*/
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setDescription(last_xh_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			if(qDto.getString("next").equals("1")){
				String sql="update archive_checkup_rw set flag='4',zsr_description='"+last_xh_message+"' where id_='"+check_id_+"' ";

				jdbcTemplate.execute(sql);
				notificationService.TaskXHLastYes(qDto,userInfoVO);
			}else if(qDto.getString("next").equals("-1")){
				String sql="update archive_checkup_rw set flag='2',zsr_description='"+last_xh_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskXHLast(qDto,userInfoVO);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean savefirstmjdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String first_mj_message=qDto.getString("first_mj_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			/*if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String id_=(String)list.get(i).get("id_");
					String mj=(String)list.get(i).get("mj");
					String sql="update "+tablename+" set mj='"+mj+"' where id_='"+id_+"'";
					jdbcTemplate.execute(sql);
				}
			}*/
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setDescription(first_mj_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			String sql="update archive_checkup_rw set flag='2' , jdr_description='"+first_mj_message+"'  where id_='"+check_id_+"' ";
			jdbcTemplate.execute(sql);
			notificationService.SubmitBGQXFirst(qDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean savenextmjdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String next_mj_message=qDto.getString("next_mj_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			/*if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String id_=(String)list.get(i).get("id_");
					String mj=(String)list.get(i).get("mj");
					String sql="update "+tablename+" set mj='"+mj+"' where id_='"+id_+"'";
					jdbcTemplate.execute(sql);
				}
			}*/
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setDescription(next_mj_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			if(qDto.getString("next").equals("1")){
				String sql="update archive_checkup_rw set flag='3',fsr_description='"+next_mj_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.SubmitMJNext(qDto,userInfoVO);
			}else if(qDto.getString("next").equals("-1")){
				String sql="update archive_checkup_rw set flag='1',fsr_description='"+next_mj_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskMjFirst(qDto,userInfoVO);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}
	public boolean savelastmjdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String last_mj_message=qDto.getString("last_mj_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			/*if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String id_=(String)list.get(i).get("id_");
					String mj=(String)list.get(i).get("mj");
					String sql="update "+tablename+" set mj='"+mj+"' where id_='"+id_+"'";
					jdbcTemplate.execute(sql);
				}
			}*/
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setDescription(last_mj_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			if(qDto.getString("next").equals("1")){
				String sql="update archive_checkup_rw set flag='4',zsr_description='"+last_mj_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskMjLastYes(qDto,userInfoVO);
			}else if(qDto.getString("next").equals("-1")){
				String sql="update archive_checkup_rw set flag='2',zsr_description='"+last_mj_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskMjLast(qDto,userInfoVO);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}
    public boolean savefirstxhdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String first_xh_message=qDto.getString("first_xh_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
        //批量修改
        try{
            /*if(list!=null&&list.size()>0){
                for(int i=0;i<list.size();i++){
                    String id_=(String)list.get(i).get("id_");
                    String _xh=(String)list.get(i).get("_xh");
                    String sql="update "+tablename+" set _xh='"+_xh+"' where id_='"+id_+"'";
                    jdbcTemplate.execute(sql);
                }
            }*/
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setDescription(first_xh_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			String sql="update archive_checkup_rw set flag='2' , jdr_description='"+first_xh_message+"'  where id_='"+check_id_+"' ";
			jdbcTemplate.execute(sql);
			notificationService.SubmitXHFirst(qDto,userInfoVO);
			return true;
        }catch (Exception e){
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
	public boolean savefirstbgqxdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String first_bgqx_message=qDto.getString("first_jiazhi_message");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		//批量修改
		try{
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setDescription(first_bgqx_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			String sql="update archive_checkup_rw set flag='2' , jdr_description='"+first_bgqx_message+"'  where id_='"+check_id_+"' ";
			jdbcTemplate.execute(sql);
			notificationService.SubmitBGQXFirst(qDto,userInfoVO);
			//添加消息提醒
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean savenextbgqxdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String next_bgqx_message=qDto.getString("next_jiazhi_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setDescription(next_bgqx_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);

			//修改标记（此时把标记改为2）
			if(qDto.getString("next").equals("1")){
				String sql="update archive_checkup_rw set flag='3',fsr_description='"+next_bgqx_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
                notificationService.SubmitBGQXNext(qDto,userInfoVO);
			}else if(qDto.getString("next").equals("-1")){
				String sql="update archive_checkup_rw set flag='1',fsr_description='"+next_bgqx_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskBgqxFirst(qDto,userInfoVO);
			}

			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean savelastbgqxdetails(Dto qDto,UserInfoVO userInfoVO) {
		String tablename=qDto.getString("tablename");
		String enuser=qDto.getString("user");
		String cnuser=findcnuser(enuser);
		String last_bgqx_message=qDto.getString("last_jiazhi_message");
		String check_id_=qDto.getString("check_id_");
		String zt_id=qDto.getString("zt_id");
		List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
		//批量修改
		try{
			/*if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String id_=(String)list.get(i).get("id_");
					Dto outDto=Dtos.newDto();
					outDto.put("id_",id_);
					String  _daoqi=bgqxcheckup(outDto);
					String sql="update "+tablename+" set _daoqi='"+_daoqi+"' where id_='"+id_+"'";
					jdbcTemplate.execute(sql);
				}
			}*/
			Dto dto=Dtos.newDto();
			Checkup_topic_logPO checkup_topic_logPO=new Checkup_topic_logPO();
			checkup_topic_logPO.setId_(AOSId.uuid());
			checkup_topic_logPO.setOperate_time(AOSUtils.getDateTimeStr());
			checkup_topic_logPO.setEnuser(enuser);
			checkup_topic_logPO.setCnuser(cnuser);
			checkup_topic_logPO.setDescription(last_bgqx_message);
			checkup_topic_logPO.setRw_id_(check_id_);
			checkup_topic_logMapper.insert(checkup_topic_logPO);
			//修改标记（此时把标记改为2）
			if(qDto.getString("next").equals("1")){
				String sql="update archive_checkup_rw set flag='4',zsr_description='"+last_bgqx_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskBgqxLastYes(qDto,userInfoVO);
			}else if(qDto.getString("next").equals("-1")){
				String sql="update archive_checkup_rw set flag='2',zsr_description='"+last_bgqx_message+"' where id_='"+check_id_+"' ";
				jdbcTemplate.execute(sql);
				notificationService.TaskBgqxLast(qDto,userInfoVO);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public String getNextTrial(Dto qDto) {
		String sql="select operate_description from archive_check_user where pid='"+qDto.getString("aos_rows_")+"' and type='2' order by index_ desc";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			return (String)list.get(0).get("operate_description");
		}else{
			return "";
		}
	}
    public String getLastTrial(Dto qDto) {
        String sql="select operate_description from archive_check_user where pid='"+qDto.getString("aos_rows_")+"'  and type='3' order by index_ desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            return (String)list.get(0).get("operate_description");
        }else{
            return "";
        }
    }
	/**
	 * 开始鉴定
	 *
	 * @author PX
	 * @param dto
	 * @param query
	 *
	 *            2018-11-27
	 * @param request
	 */
	public String bgqxcheckup(Dto dto) {
		// TODO Auto-generated method stub
		// 1.判断是否含有成文日期和保管期限代码字段
		Dto outDto = Dtos.newDto();
		String tablename = dto.getString("tablename");
		String id_=dto.getString("id_");
		String sql = "select FieldEnName  from archive_TableFieldList where tid in ( select id_ from archive_tablename where TableName='"
				+ tablename + "') ";
		int index = 0;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			String[] str = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				str[i] = (String) list.get(i).get("FieldEnName");
			}
			// 判断是否存在保管期限代码和成文日期字段
			if (Arrays.asList(str).contains("cwsj")) {
				if (Arrays.asList(str).contains("bgqxdm")) {
					// 此时进行鉴定
					// 通过查询得到进行鉴定的当前档案条目数据
					String sql2 = "select * from " + tablename + " where 1=1 and id_='"+id_+"'";
					List<Map<String, Object>> datalist = jdbcTemplate
							.queryForList(sql2);
					if (datalist != null && datalist.size() > 0) {
						for (int t = 0; t < datalist.size(); t++) {
							String cwsj = (String) datalist.get(t).get("cwsj");
							String bgqxdm = (String) datalist.get(t).get(
									"bgqxdm");
							if (cwsj == null || cwsj == "" || bgqxdm == null
									|| bgqxdm == "") {
								return "否";
							}
							// 获取当前时间
							String dateNowStr = getData();
							// 获取保管期限值
							int bgqx = getbgqx(bgqxdm);
							if (bgqx == 0) {
								return "否";
							}
							if (bgqx == 10000) {
								return "否";
							}
							if (cwsj.matches("[0-9]+")) {
								if (cwsj.length() != 8) {
									return "否";
								}
								// 加上保管期限时间
								String nd = cwsj.substring(0, 4);
								String gqnd = Integer.valueOf(nd) + bgqx + "";
								String gqsj = gqnd + cwsj.substring(4, 8);
								try {
									// 把年月日转成毫秒值
									long gqtime = new SimpleDateFormat(
											"yyyyMMdd").parse(gqsj).getTime();
									long nowtime = new SimpleDateFormat(
											"yyyyMMdd").parse(dateNowStr)
											.getTime();
									if (gqtime > nowtime) {
										return "否";
									} else {
										//此时到期
										return "是";
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									return "否";
								}
							} else {
								continue;
							}
						}
					}
				} else {
					return "否";
				}
			} else {
				return "否";
			}
		}
		return "否";
	}

	/**
	 * 操作日志查看
	 * @param qDto
	 * @return
	 */
	public List<Map<String, Object>> listlogin(Dto qDto) {
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		String sql="select distinct(index_),\n" +
				" task_number,\n" +
				" task_name,\n" +
				" Datatime,\n" +
				" archive_check_rw.tablename as tablename,\n" +
				" archive_check_rw.tabledesc as tabledesc,\n" +
				" archive_check_rw.createperson as createperson,\n" +
				" archive_check_rw.first_trialperson_cn as first_trialperson_cn,\n" +
				"  archive_check_rw.next_trialperson_cn as next_trialperson_cn,\n" +
				" archive_check_rw.last_trialperson_cn as last_trialperson_cn,\n" +
				" archive_check_rw.description as description,\n" +
				" archive_check_tablenameid.check_type as check_type,\n" +
				"archive_check_user.users as users,\n" +
				"archive_check_user.operate_time as operate_time,\n" +
				" archive_check_user.operate_description as operate_description\n" +
				"  from archive_check_user ,archive_check_rw ,archive_check_tablenameid  where archive_check_user.pid=archive_check_rw.id_ and archive_check_tablenameid.pid=archive_check_rw.id_";
		list=jdbcTemplate.queryForList(sql);
		return list;
	}
	/**
	 * 专题分类
	 * @param qDto
	 * @return
	 */
	public List<Dto> listseminar(Dto qDto) {

		List<Archive_checkup_seminarPO> list = archive_checkup_seminarMapper.list(qDto);
		List<Dto> treeNodes = new ArrayList<Dto>();
		String iconPath = System.getProperty(AOSCons.CXT_KEY) + AOSXmlOptionsHandler.getValue("icon_path");
		for (int i=0;i<list.size();i++) {
			Dto treeNode = Dtos.newDto();
			treeNode.put("id", list.get(i).getId_());
			treeNode.put("text", list.get(i).getName_());
			//自定义
			if(i>=0&&i<=9){
				treeNode.put("cascade_id_", "0.00"+(i+1));
			}else if(i>=10&&i<=99){
				treeNode.put("cascade_id_", "0.0"+(i+1));
			}else if(i>=100&&i<=999){
				treeNode.put("cascade_id_", "0."+(i+1));
			}
			//treeNode.put("icon", iconPath + "folder1.png");
			treeNode.put("leaf", true);
			treeNode.put("expanded", true);
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}
	public Dto getpath(String id_, String topic_id_,String tablename) {
		Dto out=Dtos.newDto();
		Checkup_topicPO archive_topicPO = checkup_topicMapper.selectByKey(topic_id_);
		String sql = "select * from " + tablename + "_path where tid ='" + id_ + "'";
		String filepath = "";
		String urlpath = "";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String path = (String) map.get("_s_path");
				if (path.split("\\.")[1].equals("pdf")) {
					String filename = (String) map.get("dirname");
					// 组合路径
					filepath = filePath  + File.separator+filename
							+ path;
					urlpath = topicAddress  + File.separator+filename
							+ path;
					// 获取当前ip地址
					// 路径批量替换
					// wsda/2e9275c32e6849afab038892747249c2/数字档案馆系统测试办法（档办发[2014]6号).pdf
					filepath = filepath.replace("\\", "/");
					urlpath = urlpath.replace("\\", "/");
					out.put("pdfpath",filepath);
					out.put("urlpath",urlpath);
					break;
				}
			}
		}
		return out;
	}
	public boolean saveseminar(Dto qDto) {
		try{
			Archive_checkup_seminarPO archive_checkup_seminarPO=new Archive_checkup_seminarPO();
			AOSUtils.copyProperties(qDto,archive_checkup_seminarPO);
			archive_checkup_seminarPO.setId_(AOSId.uuid());
			archive_checkup_seminarMapper.insert(archive_checkup_seminarPO);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public Archive_checkup_seminarPO getSeminarById(String id) {
		Archive_checkup_seminarPO archive_checkup_seminarPO=archive_checkup_seminarMapper.selectByKey(id);
		return archive_checkup_seminarPO;
	}
	/**
	 * 修改专题
	 * @param qDto
	 * @return
	 */
	public boolean updateSeminar(Dto qDto) {
		try{
			Archive_checkup_seminarPO archive_checkup_seminarPO=new Archive_checkup_seminarPO();
			AOSUtils.copyProperties(qDto,archive_checkup_seminarPO);
			archive_checkup_seminarMapper.updateByKey(archive_checkup_seminarPO);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteSeminar(Dto qDto) {
		try{
			archive_checkup_seminarMapper.deleteByKey(qDto.getString("id_"));
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**getDataFieldDisplayAll
	 * 功能：转换MapList为数组List
	 *
	 * @param list
	 * @return
	 */
	public static List convertMapListToArrayList(List list) {
		Map map = null;
		return list;
	}
	public Object getData(Dto qDto) {
		String tablename = qDto.getString("tablename");
		String id = qDto.getString("id_");
		String sql = "SELECT * FROM " + tablename + " WHERE id_='" + id + "'";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		if(listDto!=null&&listDto.size()>0){
			return listDto.get(0);
		}
		return "";
	}
	private List<Map<String, Object>> getModule_byid_(String cascade_id_) {
		String sql="select * from aos_sys_module where id_='"+cascade_id_+"'";
		return jdbcTemplate.queryForList(sql);
	}
	public List<Map<String, Object>> getData(Dto inDto, HttpSession session) {
		String tablename="";
		List<Map<String, Object>> listmodule=getModule_byid_(inDto.getString("zt_id"));
		if(listmodule!=null&&listmodule.size()>0){
			tablename=(String)listmodule.get(0).get("tablename");
		}
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(inDto.getString("limit"));
		Integer page = Integer.valueOf(inDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit);
		//根据任务id得到所有的条目id值
		String sql2="select * from "+tablename+" where id_ in(select tablename_id from checkup_topic_tablenameid where pid ='"+inDto.getString("zt_id")+"' order by id_ offset "+pageStart+" rows fetch next "+limit+" rows only)";
		List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql2);
		if(listDto!=null&&listDto.size()>0){
		    for(int i=0;i<listDto.size();i++){
                listDto.get(i).put("tablename",tablename);
            }
		}
		return listDto;
	}
	/**
	 *
	 * 查询记录总数
	 *
	 * @return
	 */
	public int getPageTotal_Data(Dto qDto) {
		String query = queryConditions2(qDto);
		String tablename="";
		List<Map<String, Object>> listmodule=getModule_byid_(qDto.getString("zt_id"));
		if(listmodule!=null&&listmodule.size()>0){
			tablename=(String)listmodule.get(0).get("tablename");
		}
		String sql3="select tablename_id from checkup_topic_tablenameid where pid ='"+qDto.getString("zt_id")+"' order by id_ ";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql3);
		String ids="";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				if(i==0){
					ids="'"+(String)list.get(i).get("tablename_id")+"'";
				}else{
					ids=ids+",'"+(String)list.get(i).get("tablename_id")+"'";
				}
			}
			//查询年度条件是否存在
			String sql2="select count(*) from "+tablename+" where id_ in ("+ids+") "+query;
			return jdbcTemplate.queryForInt(sql2);
		}else{
			return 0;
		}
	}
	/**
	 * 得到鉴定的查询条件
	 * @param qDto
	 * @return
	 */
	public String getQuery(Dto qDto){
		String query=" ";
		String aos_module_id_=qDto.getString("aos_module_id_");
		String jdrwbhmc=getModuleName(aos_module_id_).trim();
		if(jdrwbhmc!=null&&jdrwbhmc!=""||jdrwbhmc.length()>=9){
			if(jdrwbhmc.contains("JDRW")){
				query=" where  task_number='"+jdrwbhmc.substring(0,8)+"' and task_name='"+jdrwbhmc.substring(8)+"'";
			};
		}
		return query;
	}
	public int getQueryCount(Dto qDto){
		String query=getQuery(qDto);
		//得到当前条件下数据个数
		String sql2="select * from archive_checkup_rw  "+query;
		return jdbcTemplate.queryForList(sql2).size();
	}
	public List<Map<String,Object>> listTask(Dto qDto) {
		String checkup_type=qDto.getString("checkup_type");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String query=" ";

		String jdrwbh="";
		String jdrwmc="";
		query=getQuery(qDto);
		if(checkup_type!=""&&checkup_type!=null){
			query+="and checkup_type='"+checkup_type+"'";
		}
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit);
		//得到当前页条目数据
		String sql="select * from archive_checkup_rw  where 1=1  "+query+" order by task_number desc offset "+pageStart+" rows fetch next "+limit+" rows only";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}
	public String getModuleName(String aos_module_id_) {
		String sql="select * from aos_sys_module where id_='"+aos_module_id_+"'";
		String byrwbhmc="";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		if(listDto!=null&&listDto.size()>0){
			byrwbhmc=(String)listDto.get(0).get("name_");
		}
		return byrwbhmc;
	}
	public boolean savetask(Dto qDto) {
		try{
			Archive_checkup_rwPO archive_checkup_rwPO=new Archive_checkup_rwPO();
			AOSUtils.copyProperties(qDto,archive_checkup_rwPO);
			archive_checkup_rwPO.setId_(AOSId.uuid());
			archive_checkup_rwPO.setFlag("1");
			archive_checkup_rwMapper.insert(archive_checkup_rwPO);

			//根据辅助鉴定的类型得到当前操作的tree
			String tree=JianDingTree(archive_checkup_rwPO.getCheckup_type());
			//此时添加到数据compolitation_rw中
			//addTopic_tablenameid(qDto,archive_checkup_rwPO.getId_());
			//目录树添加专题tree，并赋予byadmin权限
			addtree(qDto,tree,archive_checkup_rwPO.getId_());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public Dto return_first(Dto qDto) {

		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		int update = 0;
		for (String id_ : selections) {
			Archive_checkup_rwPO archive_checkup_rwPO=new Archive_checkup_rwPO();
			archive_checkup_rwPO.setId_(id_);
			archive_checkup_rwPO.setFlag("1");
			archive_checkup_rwMapper.updateByKey(archive_checkup_rwPO);
			update++;
		}
		String msg = "操作完成，";
		if (update > 0) {
			msg = AOSUtils.merge(msg + "成功撤销了[{0}]个", update);
		}
		outDto.setAppMsg(msg);
		return outDto;
	}
	public Dto return_next(Dto qDto) {

		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		int update = 0;
		for (String id_ : selections) {
			Archive_checkup_rwPO archive_checkup_rwPO=new Archive_checkup_rwPO();
			archive_checkup_rwPO.setId_(id_);
			archive_checkup_rwPO.setFlag("2");
			archive_checkup_rwMapper.updateByKey(archive_checkup_rwPO);
			update++;
		}
		String msg = "操作完成，";
		if (update > 0) {
			msg = AOSUtils.merge(msg + "成功撤销了[{0}]个", update);
		}
		outDto.setAppMsg(msg);
		return outDto;
	}
	/**
	 * 得到指定id的专题数据
	 * @param zt_id
	 * @return
	 */
	private List<Archive_topic_tablenameidPO> getTopicIds(String zt_id) {
		Archive_topic_tablenameidPO archive_topic_tablenameidPO=new Archive_topic_tablenameidPO();
		Dto out=Dtos.newDto();
		out.put("pic",zt_id);
		return archive_topic_tablenameidMapper.list(out);
	}

	/**
	 * 根据鉴定类型得到tree值
	 * @param checkup_type
	 * @return
	 */
	private String JianDingTree(String checkup_type) {
		if("开放鉴定".equals(checkup_type)){
			return "0.014.012.005.001";
		}else if("密级鉴定".equals(checkup_type)){
			return "0.014.012.006.001";
		}else if("销毁鉴定".equals(checkup_type)){
			return "0.014.012.007.001";
		}else if("价值鉴定".equals(checkup_type)){
			return "0.014.012.008.001";
		}
		return "";
	}

	public String getTablename(String zt_id) {
		String sql="select * from checkup_topic where id_='"+zt_id+"'";
		String tablename="";
		List<Map<String,Object>>  list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			tablename=(String)list.get(0).get("topic_tablename");
		}
		return tablename;
	}
	//创建目录树
	private void addtree(Dto qDto, String cascade_id_,String module_id) {
		//1.得到管理员账号的id值
		Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
		String tablename=qDto.getString("sjkmc");
		aos_sys_module.setId_(module_id);
		//2.得到当前cascade的id值一些信息
		String parent_id_="";
		String parent_name="";
		List<Map<String, Object>> list = getModule(cascade_id_);
		if (list != null && list.size() > 0) {
			parent_id_=(String)list.get(0).get("id_");
			parent_name=(String)list.get(0).get("Name_");
		}
		int i=0;
		//3.查询当前节点的子节点个数
		i=getCountModele_child(parent_id_);
		if(i<9){
			aos_sys_module.setCascade_id_(cascade_id_+".00"+(i+1));
		}else if(i>=9&&i<99){
			aos_sys_module.setCascade_id_(cascade_id_+".0"+(i+1));
		}else if(i>=99){
			aos_sys_module.setCascade_id_(cascade_id_+"."+(i+1));
		}
		aos_sys_module.setName_(qDto.getString("task_number")+qDto.getString("task_name"));
		aos_sys_module.setUrl_("archive/checkup/initData.jhtml");
		aos_sys_module.setParent_id_(parent_id_);
		aos_sys_module.setIs_auto_expand_("0");
		aos_sys_module.setStatus_("1");
		aos_sys_module.setParent_name_(parent_name);
		aos_sys_module.setSort_no_(i+1);
		//aos_sys_module.setTablename(qDto.getString("topic_tablename"));
		aos_sys_moduleMapper.insert(aos_sys_module);
		//父几点变成不是叶子节点
		Aos_sys_modulePO aos_sys_module_parent=new Aos_sys_modulePO();
		aos_sys_module_parent.setId_(parent_id_);
		aos_sys_module_parent.setIs_leaf_("0");
		aos_sys_moduleMapper.updateByKey(aos_sys_module_parent);
		//同时为当前用户添加经办权限
		addpower_handle(aos_sys_module,qDto);
		//同时为当前用户添加管理权限
		addpower_manage(aos_sys_module,qDto);
	}
	//经办权限
	private void addpower_handle(Aos_sys_modulePO aos_sys_module,Dto out) {
		String rootid=getUserId("root");
		UserInfoVO userInfo=out.getUserInfo();
		Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
		aos_sys_module_userPO.setId_(AOSId.uuid());
		aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
		aos_sys_module_userPO.setUser_id_(userInfo.getId_());
		aos_sys_module_userPO.setGrant_type_("1");
		aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
		aos_sys_module_userPO.setOperator_id_(rootid);
		aos_sys_module_userMapper.insert(aos_sys_module_userPO);
	}
	//管理权限
	private void addpower_manage(Aos_sys_modulePO aos_sys_module,Dto out) {
		String rootid=getUserId("root");
		UserInfoVO userInfo=out.getUserInfo();
		Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
		aos_sys_module_userPO.setId_(AOSId.uuid());
		aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
		aos_sys_module_userPO.setUser_id_(userInfo.getId_());
		aos_sys_module_userPO.setGrant_type_("2");
		aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
		aos_sys_module_userPO.setOperator_id_(rootid);
		aos_sys_module_userMapper.insert(aos_sys_module_userPO);
	}
	//得到用户的id值
	private String getUserId(String root) {
		String id_="";
		String sql="select * from aos_sys_user where account_='"+root+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			id_=(String)list.get(0).get("id_");
		}
		return id_;
	}
	private List<Map<String, Object>> getModule(String cascade_id_) {
		String sql="select * from aos_sys_module where cascade_id_='"+cascade_id_+"'";
		return jdbcTemplate.queryForList(sql);
	}
	//查询子节点个数
	private int getCountModele_child(String parent_id_) {
		String sql="select * from aos_sys_module where parent_id_ ='"+parent_id_+"'";
		return jdbcTemplate.queryForList(sql).size();
	}
	private void addTopic_tablenameid(Dto qDto,String id_) {
		String ids=qDto.getString("ids");
		//得到专题id列表
		List<Archive_topic_tablenameidPO> list=getTopicIds(qDto.getString("zt_id"));
		String tablename=qDto.getString("sjkmc");
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Checkup_topic_tablenameidPO checkup_topic_tablenameidPO=new Checkup_topic_tablenameidPO();
				checkup_topic_tablenameidPO.setId_(AOSId.uuid());
				checkup_topic_tablenameidPO.setPid(id_);
				checkup_topic_tablenameidPO.setTablename(list.get(i).getTablename());
				checkup_topic_tablenameidPO.setTablename_id(list.get(i).getTablename_id());
				checkup_topic_tablenameidMapper.insert(checkup_topic_tablenameidPO);
			}
		}
	}
	public boolean updatetask(Dto qDto) {
		try{
			Archive_checkup_rwPO archive_checkup_rwPO=new Archive_checkup_rwPO();
			AOSUtils.copyProperties(qDto,archive_checkup_rwPO);
			archive_checkup_rwMapper.updateByKey(archive_checkup_rwPO);
			//目录树添加专题tree，并赋予byadmin权限
			updatetreename(qDto,"0.014.012.001",archive_checkup_rwPO.getId_());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 修改目录树名称
	 * @param qDto
	 * @param cascade_id_
	 * @param module_id
	 */
	private void updatetreename(Dto qDto, String cascade_id_,String module_id) {
		//1.得到管理员账号的id值
		Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
		aos_sys_module.setId_(module_id);
		aos_sys_module.setName_(qDto.getString("task_number")+qDto.getString("task_name"));
		aos_sys_moduleMapper.updateByKey(aos_sys_module);
	}
	/**
	 *
	 * 删除条目
	 *
	 * @param qDto
	 * @return
	 */
	public Dto deleteTask(Dto qDto){
		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		int del = 0;
		for (String id_ : selections) {
			//删除节点
			//1查询条目的任务名称和任务编号组合组合字段，并查询到节点id值
			delete_tree(id_);

			jdbcTemplate.execute(" delete from archive_checkup_rw where id_='"
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
	public void delete_tree(String rwid) {
		//查询到当前的编号和任务名称组合的节点id值
		String sql="select * from archive_checkup_rw where id_='"+rwid+"'";
		String treename="";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			treename=(String)list.get(0).get("task_number")+(String)list.get(0).get("task_name");
		}
		//根据名称查询到节点id集合
		String sql2="select * from aos_sys_module where name_='"+treename +"'";
		List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
		if(list2!=null&&list2.size()>0){
			for(int i=0;i<list2.size();i++){
				Dto out=Dtos.newDto();
				out.put("aos_rows_",(String)list2.get(i).get("id_"));
				moduleService.deleteModule(out);
			}
		}
	}
	public List<Map<String,Object>> listwrite(Dto qDto, HttpSession session) {
		Dto dto=Dtos.newDto();
		String sql="select * from archive_checkup_rw where  first_compilationperson='"+session.getAttribute("user")+"' and flag='1'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String pid=	(String)list.get(i).get("id_");
				String sql2="select * from archive_check_tablenameid where pid='"+pid+"'";
				List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
				if(list2.size()<=0||list2==null){
					list.remove(list.get(i));
					i--;
				}
			}
		}
		return list;
	}
	public List<Map<String,Object>> listtowrite(Dto qDto, HttpSession session) {
		Dto dto=Dtos.newDto();
		String sql="select * from archive_checkup_rw where  first_compilationperson='"+session.getAttribute("user")+"' and flag='1'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String,Object>> listreadwrite(Dto qDto, HttpSession session) {
		Dto dto=Dtos.newDto();
		String sql="select * from archive_checkup_rw where  first_compilationperson='"+session.getAttribute("user")+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}

	public List<Map<String,Object>> findTablename(Dto qDto) {
		String id_=	qDto.getString("id_");
		String sql="select * from archive_check_tablenameid where pid='"+id_+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}

	public boolean addData(Dto dto) {
		try{
			String tid=dto.getString("id_");
			String tablename=dto.getString("tablename");
			String checkup_type=dto.getString("checkup_type");
			String sql="delete from archive_check_tablenameid where pid='"+tid+"'";
			jdbcTemplate.execute(sql);
			String[] selections = dto.getRows();
			for (String id_ : selections) {
				Archive_check_tablenameidPO archive_check_tablenameidPO=new Archive_check_tablenameidPO();
				archive_check_tablenameidPO.setId_(AOSId.uuid());
				archive_check_tablenameidPO.setPid(tid);
				archive_check_tablenameidPO.setTablename_id(id_);
				archive_check_tablenameidPO.setTablename(tablename);
				archive_check_tablenameidPO.setCheck_type(checkup_type);
				archive_check_tablenameidMapper.insert(archive_check_tablenameidPO);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*public List<Archive_check_userPO> listoperationlogin(Dto qDto) {
		String pid = qDto.getString("pid");
		List<Archive_check_userPO> list = archive_check_userMapper.list(qDto);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String type = (String) list.get(i).getType();
				if ("1".equals(type)) {
					list.get(i).setType("提交");
				} else {
					list.get(i).setType("审核");
				}
			}
		}
		return list;
	}*/
	public void listwritelogin(Dto qDto) {
	}
	public Dto seminar_initData(Dto qDto,HttpSession session) {
		Dto dto=Dtos.newDto();
		String[] f=new String[2^31];
		String[] n=new String[2^31];
		String[] m=new String[2^31];
		int x=0;
		int z=0;
		int y=0;
		String user=(String)session.getAttribute("user");
		String sql="select * from archive_checkup_rw where id_='"+qDto.getString("aos_module_id_")+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String first=(String)list.get(i).get("first_compilationperson");
				if(first!=null&&first.length()>0){
					String[] firsts=first.split(",");
					for(int t=0;t<firsts.length;t++){
						f[x]=firsts[t];
						x++;
					}
				}
				String next=(String)list.get(i).get("next_compilationperson");
				if(next!=null&&next.length()>0){
					String[] nexts=next.split(",");
					for(int t=0;t<nexts.length;t++){
						n[z]=nexts[t];
						z++;
					}
				}
				String last=(String)list.get(i).get("last_compilationperson");
				if(last!=null&&last.length()>0){
					String[] lasts=last.split(",");
					for(int t=0;t<lasts.length;t++){
						m[y]=lasts[t];
						y++;
					}
				}
			}
		}
		if(Arrays.asList(f).contains(user)){
			dto.put("first","1");
		}else{
			dto.put("first","0");
		}
		if(Arrays.asList(n).contains(user)){
			dto.put("next","1");
		}else{
			dto.put("next","0");
		}
		if(Arrays.asList(m).contains(user)){
			dto.put("last","1");
		}else{
			dto.put("last","0");
		}
		return dto;
	}
	public List<Checkup_topic_logPO> listoperationlogin(Dto qDto) {
		Dto out=Dtos.newDto();
		out.put("rw_id_",qDto.getString("pid"));
		return checkup_topic_logMapper.list(out);
	}

	public boolean updatekaifang_lx(Dto qDto) {
		try{
			updateData(qDto);
			String id_=qDto.getString("id_");
			String tablename=qDto.getString("tablename");
			String scjg=qDto.getString("scjg");
			String sfdjcl=qDto.getString("sfdjcl");
			String selectvalue=qDto.getString("selectvalue");
			jdbcTemplate.execute("update "+tablename+" set sfkf='"+selectvalue+"' where id_='"+id_+"'");
			//更新条目信息
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean booleanupdateData_kaifang(Dto qDto) {
		try{
			List list=booleanUpdateData(qDto);
			//更新条目信息
			if(list!=null&&list.size()>=1){
				//此时没有做修改
				return false;
			}
			//此时做修改了
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateAllkaifang(Dto qDto) {
		try{
			String tablename=qDto.getString("tablename");
			String topic_id_=qDto.getString("topic_id_");
			jdbcTemplate.execute("update "+tablename+" set sfkf=scjg  where id_ in (select tablename_id from checkup_topic_tablenameid where pid='"+topic_id_+"' and tablename='"+tablename+"')");
			//更新条目信息
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatekaifang(Dto qDto) {
		try{
			String tablename=qDto.getString("tablename");
			String[] selections = qDto.getRows();
			for (String id_ : selections) {
				jdbcTemplate.execute("update "+tablename+" set sfkf=scjg  where id_='"+id_+"'");
			}

			//更新条目信息
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	//根据当前条件查询
	public List booleanUpdateData(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String columns = "id_";
		String strsql = "";
		Iterator iter = qDto.entrySet().iterator(); // 获取key和value的set
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next(); // 把hashmap转成Iterator再迭代到entry
			Object key = entry.getKey(); // 从entry获取keyObject
			if (key.equals("app") || key.equals("tablename")|| key.equals("selectvalue")
					|| key.equals("_userInfoVO") || key.equals("id")|| key.equals("c1")|| key.equals("c2")|| key.equals("c3")) {
				continue;
			}
			Object val = "'" + entry.getValue() + "'"; // 从entry获取value}
			// strsql=key+"="+val+"";
				strsql = strsql+" and "+ key + "=" + val + "";
		}
		String sql = " select  *  from " + qDto.getString("tablename") +" WHERE 1=1 '" + strsql;
		List list=jdbcTemplate.queryForList(sql);
		return list;
	}
	//修改条目
	public Dto updateData(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String columns = "id_";
		String strsql = "";
		Iterator iter = qDto.entrySet().iterator(); // 获取key和value的set
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next(); // 把hashmap转成Iterator再迭代到entry
			Object key = entry.getKey(); // 从entry获取keyObject
			if (key.equals("app") || key.equals("tablename")|| key.equals("selectvalue")
					|| key.equals("_userInfoVO") || key.equals("id")|| key.equals("c1")|| key.equals("c2")|| key.equals("c3")) {
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
				+ strsql + " WHERE id_='" + qDto.getString("id_") + "'";
		jdbcTemplate.execute(sql);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，保存成功。");
		return outDto;
	}
	public boolean updatemj_lx(Dto qDto) {
		try{
			updateData(qDto);
			String id_=qDto.getString("id_");
			String tablename=qDto.getString("tablename");
			String selectvalue=qDto.getString("selectvalue");
			jdbcTemplate.execute("update "+tablename+" set _mj='"+selectvalue+"' where id_='"+id_+"'");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatejiazhi_lx(Dto qDto) {
		try{
			updateData(qDto);
			String id_=qDto.getString("id_");
			String tablename=qDto.getString("tablename");
			String selectvalue=qDto.getString("selectvalue");
			jdbcTemplate.execute("update "+tablename+" set _bgqx='"+selectvalue+"' where id_='"+id_+"'");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}
	public boolean updatexh_lx(Dto qDto) {
		try{
			updateData(qDto);
			String id_=qDto.getString("id_");
			String tablename=qDto.getString("tablename");
			String selectvalue=qDto.getString("selectvalue");
			jdbcTemplate.execute("update "+tablename+" set _xh='"+selectvalue+"' where id_='"+id_+"'");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}
	public boolean updatekaifang_lx_all(Dto qDto) {
		try{
			String ids_=qDto.getString("aos_rows_");
			String tablenames_=qDto.getString("tablenames");
			String selectvalue=qDto.getString("selectvalue");
			String[] ids=ids_.split(",");
			String[] tablenames=tablenames_.split(",");
			if(ids.length==tablenames.length&&ids.length>0&&tablenames.length>0){
				for(int i=0;i<ids.length;i++){
					jdbcTemplate.execute("update "+tablenames[i]+" set _kaifang='"+selectvalue+"' where id_='"+ids[i]+"'");
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatejiazhi_lx_all(Dto qDto) {
		try{
			String ids_=qDto.getString("aos_rows_");
			String tablenames_=qDto.getString("tablenames");
			String selectvalue=qDto.getString("selectvalue");
			String[] ids=ids_.split(",");
			String[] tablenames=tablenames_.split(",");
			if(ids.length==tablenames.length&&ids.length>0&&tablenames.length>0){
				for(int i=0;i<ids.length;i++){
					jdbcTemplate.execute("update "+tablenames[i]+" set _bgqx='"+selectvalue+"' where id_='"+ids[i]+"'");
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatemj_lx_all(Dto qDto) {
		try{
			String ids_=qDto.getString("aos_rows_");
			String tablenames_=qDto.getString("tablenames");
			String selectvalue=qDto.getString("selectvalue");
			String[] ids=ids_.split(",");
			String[] tablenames=tablenames_.split(",");
			if(ids.length==tablenames.length&&ids.length>0&&tablenames.length>0){
				for(int i=0;i<ids.length;i++){
					jdbcTemplate.execute("update "+tablenames[i]+" set _mj='"+selectvalue+"' where id_='"+ids[i]+"'");
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatexh_lx_all(Dto qDto) {
		try{
			String ids_=qDto.getString("aos_rows_");
			String tablenames_=qDto.getString("tablenames");
			String selectvalue=qDto.getString("selectvalue");
			String[] ids=ids_.split(",");
			String[] tablenames=tablenames_.split(",");
			if(ids.length==tablenames.length&&ids.length>0&&tablenames.length>0){
				for(int i=0;i<ids.length;i++){
					jdbcTemplate.execute("update "+tablenames[i]+" set _xh='"+selectvalue+"' where id_='"+ids[i]+"'");
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public List<Map<String,Object>>   getQueryIds(Dto qDto) {
		// TODO Auto-generated method stub
		String ids="";
		String tablename=qDto.getString("tablename");
		String query=qDto.getString("query");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql="select id_ from "+tablename+" where 1=1 "+query;
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	public String getJDIndex(String lx) {
		String dh = archive_checkup_rwMapper.getJDIndex(lx);
		if (dh != null && dh.length() != 0) {
			String index = dh.substring(dh.length()-3, dh.length());
			int bh = Integer.valueOf(index) + 1;
			return String.format("%3d", bh).replace(" ", "0");
		}else {
			return "001";
		}
	}

	public List<Archive_tablefieldlistPO> getComboboxList(Dto inDto) {
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(inDto.getString("tablename"));
		return list;
	}
}
