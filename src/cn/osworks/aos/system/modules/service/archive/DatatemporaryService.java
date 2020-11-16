package cn.osworks.aos.system.modules.service.archive;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.AOSXmlOptionsHandler;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.exception.AOSException;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 数据服务
 *
 * @author shq
 *
 * @date 2016-9-14
 */
@Service
public class DatatemporaryService extends JdbcDaoSupport {

	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private Archive_tableinputMapper archive_tableinputMapper;
	@Autowired
	private Aos_sys_moduleMapper aos_sys_moduleMapper;
	@Autowired
	private Archive_sxjc_dataMapper archive_sxjc_dataMapper;
	@Autowired
	private Aos_sys_userMapper aos_sys_userMapper;
	@Autowired
	private Jnws_temporaryMapper jnws_temporaryMapper;
	@Autowired
	private Zlbz_receiveMapper zlbz_receiveMapper;
	@Autowired
	private Zlqf_receiveMapper zlqf_receiveMapper;
	@Autowired
	private Zlts_receiveMapper zlts_receiveMapper;
	@Autowired
	private Archive_zdyjMapper archive_zdyjMapper;
	@Autowired
	private Aos_sys_dicMapper aos_sys_dicMapper;
	@Autowired
	private Aos_sys_dic_indexMapper aos_sys_dic_indexMapper;
	@Autowired
	private Stjs_nMapper stjs_nMapper;
	@Autowired
	private Dazj_nMapper dazj_nMapper;
	@Autowired
	private Zyjs_nMapper zyjs_nMapper;
	@Autowired
	private Zlzj_nMapper zlzj_nMapper;
	@Autowired
	private YbrbMapper ybrbMapper;
	@Autowired
	private YbxwMapper ybxwMapper;
	@Autowired
	private Ybrb_receiveMapper ybrb_receiveMapper;
	@Autowired
	private Ybxw_receiveMapper ybxw_receiveMapper;
	@Autowired
	private Ybcb_receiveMapper ybcb_receiveMapper;
	@Autowired
	private Gdws_temporaryMapper gdws_temporaryMapper;
	@Autowired
	private Xgdws_temporaryMapper xgdws_temporaryMapper;
	/*@Autowired
	private Zpda_temporaryMapper zpda_temporaryMapper;*/
	//@Autowired
	//private Spda_temporaryMapper spda_temporaryMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	public static String url = "";
	@Autowired
	public static String username = "";
	@Autowired
	public static String password = "";
	@Autowired
	public static String filePath = "";
	@Autowired
	public static Connection connection = null;
	@Autowired
	public static PreparedStatement ps;
	@Autowired
	private LogMapper logMapper;
	@Autowired
	private Archive_receive_resetMapper archive_receive_resetMapper;
	@Autowired
	private Archive_receiveMapper archive_receiveMapper;
	@Autowired
	private Archive_entityMapper archive_entityMapper;
	@Autowired
	private Archive_sxjcMapper archive_sxjcMapper;
	@Autowired
	private Archive_sxjc_resultMapper archive_sxjc_resultMapper;
	@Autowired
	private Archive_receive_reportMapper archive_receive_reportMapper;
	@Autowired
	private NotificationService notificationService;
	// 加载配置文件
	static {
		try {
			Properties props = new Properties();
			InputStream in = DatatemporaryService.class
					.getResourceAsStream("/jdbc.properties");
			props.load(in);
			url = props.getProperty("jdbc.url");
			username = props.getProperty("jdbc.username");
			password = props.getProperty("jdbc.password");
			filePath = props.getProperty("jdbc.filePath");

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
		String query = queryConditions2(qDto);
		String pch=qDto.getString("pch");
		if(pch!=null&&pch!=""){
				query+=" and pch='"+pch+"'";
		}
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
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
		/*Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}*/
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
					+ " WHERE 1=1 "
					//+ qDto.getString("queryclass")
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
					+ qDto.getString("tablename")+"" +
					""
					+ " WHERE 1=1 "
					//+ qDto.getString("queryclass")
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
	public List<Dto> getDataFieldListDisplayAll_entity(Dto qDto, String username) {
		String query = queryConditions2(qDto);
		String pch=qDto.getString("pch");
		if(pch!=null&&pch!=""){
			query+=" and pch='"+pch+"'";
		}
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
		String sql = "";
		String term = "";
		String fieldName;
		String enfield = "id_";
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
		// 到这一步进行全宗筛选
		/*Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}*/
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
					+ " WHERE 1=1 "
					+ qDto.getString("queryclass")
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
					+ qDto.getString("tablename")+"" +
					""
					+ " WHERE 1=1 "
					+ qDto.getString("queryclass")
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
	public List<Dto> getDataFieldListDisplayAll_zhengji(Dto qDto, String username) {
		String query = queryConditions2(qDto);
		String pch=qDto.getString("pch");
		if(pch!=null&&pch!=""){
			query+=" and pch='"+pch+"'";
		}
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
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
		/*Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}*/
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
					+ " WHERE 1=1 "
					+ qDto.getString("queryclass")
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
					+ qDto.getString("tablename")+"" +
					""
					+ " WHERE 1=1 "
					+ qDto.getString("queryclass")
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
	public List<Dto> getDataFieldListDisplayAll_error2(Dto qDto, String username) {
		String query = queryConditions2(qDto);
		String pch=qDto.getString("pch");
		String sxjczt=qDto.getString("sxjczt");
		if(pch!=null&&pch!=""){
			query+=" and pch='"+pch+"'";
		}
		//得到当前选中的错误类型的查询条件
		query+=getQueryError(qDto);

		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
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
					+ " WHERE  1=1 "
					+ qDto.getString("queryclass")
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
					+ qDto.getString("tablename")+"" +
					""
					+ " WHERE 1=1  "
					+ qDto.getString("queryclass")
					+ query
					+ ") "
					+ "SELECT * FROM aos_query_  ORDER BY aos_rn_";
		}
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}

	private String getQueryError(Dto qDto) {
		String sxjc_id_="";
		String tablename=qDto.getString("tablename");
		String text=qDto.getString("sxjczt");
		String pch=qDto.getString("pch");
		String query="";
		String idss="";
		List<Archive_sxjc_dataPO> datalist=null;
		String sql="select * from archive_sxjc_result where pch='"+pch+"'  order by index_ desc";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			sxjc_id_=(String)list.get(0).get("id_");
			Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
			archive_sxjc_dataPO.setTid(sxjc_id_);
			archive_sxjc_dataPO.setTablename(tablename);
			archive_sxjc_dataPO.setText(text);
			Dto out=Dtos.newDto();
			AOSUtils.copyProperties(archive_sxjc_dataPO,out);
			datalist=archive_sxjc_dataMapper.list(out);
			if(datalist!=null&&datalist.size()>0){
				idss="( ";
				for(int k=0;k<datalist.size();k++){
					if(k==0){
						idss=idss+" '"+datalist.get(k).getTablename_id_()+"'";
					}else{
						idss=idss+",'"+datalist.get(k).getTablename_id_()+"'";
					}
				}
				idss=idss+" )";
				query=" and  id_ in "+idss;
			}
		}

		return query;
	}

	/**
	 *
	 * 显示表头
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getDataFieldListDisplayAll_error(Dto qDto, String username) {
		String query = queryConditions2(qDto);
		String pch=qDto.getString("pch");
		String sxjczt=qDto.getString("sxjczt");
		if(pch!=null&&pch!=""){
			query+=" and pch='"+pch+"'";
		}
		if(sxjczt!=null&&sxjczt!=""){
			query+=" and sxjczt='"+sxjczt+"'";
		}
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
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
					+ " WHERE "
					+ qDto.getString("queryclass")
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
					+ qDto.getString("tablename")+"" +
					""
					+ " WHERE "
					+ qDto.getString("queryclass")
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
	public List<Dto> getDataFieldListDisplayAll_temporary(Dto qDto, String username) {
		String query = queryConditions2(qDto);
		String pch=qDto.getString("pch");
		if(pch!=null&&pch!=""){
			query+=" and pch='"+pch+"'";
		}
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
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
		/*Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}*/
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
		String query = queryConditions2(qDto);
		String queryclass=qDto.getString("queryclass");
		String sxjczt=qDto.getString("sxjczt");
		if(queryclass!=null&&queryclass!=""){
			query=" "+queryclass+" "+query;
		}
		if(sxjczt!=null&&sxjczt!=""){
			query=" "+queryclass+" "+query+" and sxjczt='"+sxjczt+"'";
		}
		String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
				+ " WHERE 1=1 " + query;
		return jdbcTemplate.queryForInt(sql);
	}
	public int getPageTotal_ziliao(Dto qDto) {
		String query = queryConditions2(qDto);
		String pch=qDto.getString("pch");
		if(pch!=null&&pch!=""){
			query+=" and pch='"+pch+"'";
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
	public int getPageTotal_entity(Dto qDto) {
		String query = queryConditions2(qDto);
		String queryclass=qDto.getString("queryclass");
		String sxjczt=qDto.getString("sxjczt");
		if(queryclass!=null&&queryclass!=""){
			query=" "+queryclass+" "+query;
		}
		if(sxjczt!=null&&sxjczt!=""){
			query=" "+queryclass+" "+query+" and sxjczt='"+sxjczt+"'";
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
	public int getPageTotal_zhengji(Dto qDto) {
		String query = queryConditions2(qDto);
		String queryclass=qDto.getString("queryclass");
		String sxjczt=qDto.getString("sxjczt");
		if(queryclass!=null&&queryclass!=""){
			query=" "+queryclass+" "+query;
		}
		if(sxjczt!=null&&sxjczt!=""){
			query=" "+queryclass+" "+query+" and sxjczt='"+sxjczt+"'";
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
	public int getPageTotal2(Dto qDto) {
		String query = queryConditions2(qDto);
		String pch=qDto.getString("pch");
		String sxjczt=qDto.getString("sxjczt");
		if(pch!=null&&pch!=""){
			query+=" and pch='"+pch+"'";
		}
		//得到当前选中的错误类型的查询条件
		query+=getQueryError(qDto);
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
	public Dto deleteRw_pici(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		int del = 0;
		for (String id_ : selections) {
			jdbcTemplate.execute(" delete from  archive_receive  where id_='"
					+ id_ + "'");
			del++;
		}
		String msg = "操作完成，";
		if (del > 0) {
			msg = AOSUtils.merge(msg + "成功删除任务[{0}]个", del);
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
						filePath+ filename+filepath));
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
//此时查询哪些字段是自动加1
		getFieldYnSum(outDto,qDto);
		// List<Archive_tableinputPO> list = archive_tableinputMapper.

		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，加存成功。");
		return outDto;
	}
	public Dto getFieldYnSum(Dto outDto,Dto qDto){
		//此时更新下标签是否为空
		Dto dto = Dtos.newDto();
		dto.put("tablename",qDto.getString("tablename"));
		List<Archive_tableinputPO> alist=archive_tableinputMapper.list(dto);
		for(int i=0;i<alist.size();i++){
			String fieldname=alist.get(i).getFieldname();
			//是否自动加1
			String ynnum=alist.get(i).getYnnum();
			//是否补位
			String ynpw=alist.get(i).getYnpw();
			//最大长度
			String edtmax=alist.get(i).getEdtmax();
			if("1".equals(ynnum)){
				String name=fieldname.substring(0,fieldname.length()-1);
				String fieldvalue=qDto.getString(name);
				try{
					Integer newvalue=Integer.valueOf(fieldvalue)+1;
					if("1".equals(ynpw)){
						outDto.put(fieldname.substring(0,fieldname.length()-1),String.format("%0"+edtmax+"d", newvalue));
					}else{
						outDto.put(fieldname.substring(0,fieldname.length()-1),newvalue);
					}
				}catch(Exception e){
					outDto.put(fieldname.substring(0,fieldname.length()-1),0);
				}
			}
		}
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
	public String queryConditions_temporary(Dto qDto) {

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

			return query.toString()+" and pch='"+qDto.getString("pch")+"'";
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
			queryclass = " and  _classtree='" + pDto.getString("cascode_id_") + "'";
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
	/**
	 * 根据任务编号得到附件内容
	 * @param inDto
	 * @return
	 */
	public String getfile_fj(Dto qDto) {
		String message="";
		String htmlpath="";
		String rw_id_;
		String yjssc="";
		String pch=qDto.getString("pch");
		String sql="select * from archive_receive where pch='"+pch+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return "";
		}
		// Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(rw_id_);
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return message;
		}
		File file=new File(filePath+"//"+"temporary"+"//"+rw_id_+"//"+yjssc);
		if(!file.exists()){
			return message;
		}
		try {
			htmlpath=wordtohtml(filePath+"//"+"temporary"+"//"+rw_id_+"//"+yjssc);
			return  htmlpath;
		}catch(Exception e){
			e.printStackTrace();
			message="";
		}
		return message;
	}
	public String getfile_fj_zlzj(Dto qDto) {
		String message="";
		String htmlpath="";
		String rw_id_;
		String yjssc="";
		String pch=qDto.getString("pch");
		String sql="select * from zlzj_n where pch='"+pch+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return "";
		}
		// Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(rw_id_);
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return message;
		}
		/*File file=new File(filePath+"//"+"zlzj"+"//"+rw_id_+"//"+yjssc);
		if(!file.exists()){
			return message;
		}*/


		/*try {
			htmlpath=wordtohtml(filePath+"//"+"zlzj"+"//"+rw_id_+"//"+yjssc);
			return  htmlpath;
		}catch(Exception e){
			e.printStackTrace();
			message="";
		}*/
		//转base64码观看
		byte[] data = null;
		try {
			InputStream in = new FileInputStream(filePath+"/"+"zlzj_n"+"/"+rw_id_+"/"+yjssc);
			System.out.println("文件大小（字节）="+in.available());
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组进行Base64编码，得到Base64编码的字符串
		BASE64Encoder encoder = new BASE64Encoder();
		String base64Str = encoder.encode(data);
		return base64Str;
	}
	public String getfile_fj_temporary(Dto qDto) {
		String message="";
		String htmlpath="";
		String rw_id_;
		String yjssc="";
		String pch=qDto.getString("pch");
		String sql="select * from archive_receive where pch='"+pch+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return "";
		}
		// Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(rw_id_);
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return message;
		}
		//转base64码观看
		byte[] data = null;
		try {
			InputStream in = new FileInputStream(filePath+"/"+"archive_receive"+"/"+rw_id_+"/"+yjssc);
			System.out.println("文件大小（字节）="+in.available());
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组进行Base64编码，得到Base64编码的字符串
		BASE64Encoder encoder = new BASE64Encoder();
		String base64Str = encoder.encode(data);
		return base64Str;
	}
	public String getfile_fj_ziyuan(Dto qDto) {
		String message="";
		String htmlpath="";
		String rw_id_;
		String yjssc="";
		String pch=qDto.getString("pch");
		String sql="select * from zyjs_n where jsbh='"+pch+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return "";
		}
		// Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(rw_id_);
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return message;
		}
		//转base64码观看
		byte[] data = null;
		try {
			InputStream in = new FileInputStream(filePath+"/"+"zyjs_n"+"/"+rw_id_+"/"+yjssc);
			System.out.println("文件大小（字节）="+in.available());
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组进行Base64编码，得到Base64编码的字符串
		BASE64Encoder encoder = new BASE64Encoder();
		String base64Str = encoder.encode(data);
		return base64Str;
	}
	/**
	 * 根据任务编号得到附件内容
	 * @param inDto
	 * @return
	 */
	public String getfile_fj_entity(Dto qDto) {
		String message="";
		String htmlpath="";
		String rw_id_;
		String yjssc="";
		String pch=qDto.getString("pch");
		String sql="select * from stjs_n where jsbh='"+pch+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return "";
		}
		// Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(rw_id_);
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return message;
		}
		File file=new File(filePath+"//"+"stjs_n"+"//"+rw_id_+"//"+yjssc);
		if(!file.exists()){
			return message;
		}
		try {
			//此时把图片转成base64
			String newyjspath=convertFileToBase64(filePath+"/"+"stjs_n"+"/"+rw_id_+"/"+yjssc);
			return  newyjspath;
		}catch(Exception e){
			e.printStackTrace();
			message="";
		}
		return message;
	}
	public  String convertFileToBase64(String imgPath) {
		byte[] data = null;
		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgPath);
			System.out.println("文件大小（字节）="+in.available());
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组进行Base64编码，得到Base64编码的字符串
		BASE64Encoder encoder = new BASE64Encoder();
		String base64Str = encoder.encode(data);
		return base64Str;
	}
	 private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
		inputChannel = new FileInputStream(source).getChannel();
		outputChannel = new FileOutputStream(dest).getChannel();
		outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
		inputChannel.close();
		outputChannel.close();
		}
	}

	public boolean booleanfile(Dto qDto) {
		String byrwbh=qDto.getString("pch");
		String yjssc="";
		String rw_id_="";
		String sql="select * from archive_receive where pch='"+byrwbh+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return false;
		}
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return false;
		}
		File file=new File(filePath
				+"//"+"temporary"+"//"+rw_id_+"//"+yjssc);

		if(!file.exists()){
			return false;
		}
		return true;
	}
	public boolean booleanfile_ziliao(Dto qDto) {
		String byrwbh=qDto.getString("pch");
		String yjssc="";
		String rw_id_="";
		String sql="select * from zlzj_n where pch='"+byrwbh+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return false;
		}
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return false;
		}
		File file=new File(filePath
				+"//"+"zlzj_n"+"//"+rw_id_+"//"+yjssc);

		if(!file.exists()){
			return false;
		}
		return true;
	}
	public boolean booleanfile_temporary(Dto qDto) {
		String byrwbh=qDto.getString("pch");
		String yjssc="";
		String rw_id_="";
		String sql="select * from archive_receive where pch='"+byrwbh+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return false;
		}
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return false;
		}
		File file=new File(filePath
				+"//"+"archive_receive"+"//"+rw_id_+"//"+yjssc);

		if(!file.exists()){
			return false;
		}
		return true;
	}
	public boolean booleanfile_ziyuan(Dto qDto) {
		String byrwbh=qDto.getString("pch");
		String yjssc="";
		String rw_id_="";
		String sql="select * from zyjs_n where jsbh='"+byrwbh+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return false;
		}
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return false;
		}
		File file=new File(filePath
				+"zyjs_n"+"/"+rw_id_+"/"+yjssc);

		if(!file.exists()){
			return false;
		}
		return true;
	}
	public boolean booleanfile_entity(Dto qDto) {
		String byrwbh=qDto.getString("pch");
		String yjssc="";
		String rw_id_="";
		String sql="select * from stjs_n where jsbh='"+byrwbh+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			rw_id_=(String)list.get(0).get("id_");
			yjssc=(String)list.get(0).get("yjssc");
		}else{
			return false;
		}
		//设置全路径名称
		if(yjssc==null||yjssc.length()==0){
			return false;
		}
		File file=new File(filePath
				+"//"+"stjs_n"+"//"+rw_id_+"//"+yjssc);

		if(!file.exists()){
			return false;
		}
		return true;
	}
	private String wordtohtml(String docpath) {
		File outputFolder = null;
		File outputPictureFolder = null;
		try {
			//转换后HTML文件存放位置
			outputFolder = new File(docpath.substring(0,docpath.lastIndexOf("/")+1));
			if (null != outputFolder) {
				//转换后原word中图片存放位置
				String outputPictureFolderPath = docpath.substring(0,docpath.lastIndexOf("/")+1);
				outputPictureFolder = new File(outputPictureFolderPath);
				outputPictureFolder.mkdir();
			}
		} catch (Exception e1) {

		}
		try {
			//被转换的word文件
			File convertedWordFile = new File(docpath);
			return convert2Html(convertedWordFile, outputFolder, outputPictureFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}


	public  String convert2Html(File wordFile, File outputFolder,
								final File outputPictureFolder) throws TransformerException,
			IOException, ParserConfigurationException {
		//创建被转换的word HWPFDocument对象
		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(wordFile));
		//创建word转换器，并设置对于图片如何处理
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType,
									  String suggestedName, float widthInches, float heightInches) {
				return outputPictureFolder.getName() + File.separator
						+ suggestedName;
			}
		});
		//开始转换word为HTML
		wordToHtmlConverter.processDocument(wordDocument);
		//开始转换word中图片到图片存放目录
		List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = (Picture) pics.get(i);
				try {
					pic.writeImageContent(new FileOutputStream(
							outputPictureFolder.getAbsolutePath()
									+ File.separator
									+ pic.suggestFullFileName()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		//将word转换为HTML，输出到指定目录
		Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		out.close();
		return new String(out.toByteArray(),"UTF-8");
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

    public List<Map<String, Object>> getReceive(Dto inDto) {

		String sql="select * from archive_receive where  nd='"+inDto.getString("nd")+"' and tablename='"+inDto.getString("tablename")+"' and flag='"+inDto.getString("flag")+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
    }
	public List<Map<String, Object>> getReceive_zpda(Dto inDto) {

		String sql = "select * from archive_receive where   tablename='" + inDto.getString("tablename") + "' and flag='" + inDto.getString("flag") + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String, Object>> getReceive_ypda(Dto inDto) {

		String sql = "select * from archive_receive where   tablename='" + inDto.getString("tablename") + "' and flag='" + inDto.getString("flag") + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String, Object>> getReceive_spda(Dto inDto) {

		String sql = "select * from archive_receive where   tablename='" + inDto.getString("tablename") + "' and flag='" + inDto.getString("flag") + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String, Object>> listreceive(Dto qDto) {
// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit);
		//得到当前页条目数据
		String sql="select * from archive_receive_reset  order by pch desc offset "+pageStart+" rows fetch next "+limit+" rows only";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String, Object>> listzlzj(Dto qDto) {
// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		String lx=qDto.getString("lx");
		String zjnd=qDto.getString("zjnd");
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit);
		//得到当前页条目数据
		String sql="select * from zlzj_n where lx='"+lx+"' and zjnd='"+zjnd+"' order by pch desc offset "+pageStart+" rows fetch next "+limit+" rows only";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String, Object>> listziyuan(Dto qDto) {
// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		String lx=qDto.getString("lx");
		String jsnd=qDto.getString("jsnd");
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit);
		//得到当前页条目数据
		String sql="select * from zyjs_n where lx='"+lx+"' and nd='"+jsnd+"' order by jsbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	public boolean savereceive(Dto dtos) {
		try{
			Archive_receive_resetPO archive_receive_resetPO=new Archive_receive_resetPO();
			AOSUtils.copyProperties(dtos,archive_receive_resetPO);
			archive_receive_resetPO.setId_(AOSId.uuid());
			archive_receive_resetMapper.insert(archive_receive_resetPO);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean entityToreceiveData(Dto inDto,UserInfoVO userInfoVO) {
		try{
			Dto outDto=Dtos.newDto();
			String id_=inDto.getString("id_");
			String sql="update archive_entity set jszt='已接收'" +"where id_='"+id_+"'";
			jdbcTemplate.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean collectToreceiveData(Dto inDto,UserInfoVO userInfoVO) {
		try{
			Dto outDto=Dtos.newDto();
			String id_=inDto.getString("id_");
			String sql="update archive_collect set jszt='已接收'" +"where id_='"+id_+"'";
			jdbcTemplate.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean resourceToreceiveData(Dto inDto,UserInfoVO userInfoVO) {
		try{
			Dto outDto=Dtos.newDto();
			String id_=inDto.getString("id_");
			String sql="update archive_resource set jszt='已接收'" +"where id_='"+id_+"'";
			jdbcTemplate.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	//临时库---------》》》》》接收库
	public Dto temporaryToreceiveData(Dto inDto,UserInfoVO userInfoVO)throws Exception {
		Dto outDto=Dtos.newDto();
		String tablename=inDto.getString("tablename");
		String id_=inDto.getString("id_");
		//根据id得到当前选中的临时库的数据表数据的id值
		String pch="";
		String bmbh=inDto.getString("bmbh");
		String _classtree_f=inDto.getString("_classtree_f");
		_classtree_f=_classtree_f.replaceAll("0.027.016.001","0.027.016.002");
		String sql="select pch from archive_receive where id_='"+id_+"'";
		//去除tablename的后缀
		String tablename_receive=tablename.substring(0,tablename.lastIndexOf("_"))+"_receive";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0&&list!=null){
			pch=(String)list.get(0).get("pch");
		}
		String sql10="select * from "+tablename+" where pch='"+pch+"'";
		List<Map<String,Object>> list10=jdbcTemplate.queryForList(sql10);
		if(list10!=null&&list10.size()>0) {
			for (int j= 0; j< list10.size(); j++) {
					String temporary_id_=(String)list10.get(j).get("id_");
				String sql4="select * from "+tablename_receive +" where id_ ='"+temporary_id_+"'";
				List<Map<String,Object>> list3=jdbcTemplate.queryForList(sql4);
				if(list3.size()>0&&list3!=null){

				}else{
					String sql6="select * from "+tablename +" where id_ ='"+temporary_id_+"'";
					List<Map<String,Object>> list7=jdbcTemplate.queryForList(sql6);
					if(list7!=null&&list7.size()>0){
						String qzh=(String)list7.get(0).get("qzh");
						/*if(!bmbh.equals(qzh)){
							continue;
						}*/
					}
					//数据单条迁移,暂时不采用删除的方式了
					String sql3="insert into "+tablename_receive+" select * from "+tablename+" where pch='"+pch+"' and id_='"+temporary_id_+"'";
					jdbcTemplate.execute(sql3);
					//修改classtree的值
					String sql15="update "+tablename_receive+" set _classtree='"+_classtree_f+"' where id_ in " +
							"( select id_ from "+tablename+" where pch='"+pch+"' and id_='"+temporary_id_+"')";
					jdbcTemplate.execute(sql15);
					//同时也把当前temporary_id_中的电子文件也上传一份到receive_path中
					temporaryToPathreceive(filePath,tablename,tablename_receive,pch,temporary_id_);
					//此时在删除临时库操作完的条目数据
					String sql7="delete from "+tablename+" where id_='"+temporary_id_+"'";
					jdbcTemplate.execute(sql7);
					//删除path表
					String sql8="delete from "+tablename+"_path where tid='"+temporary_id_+"'";
					jdbcTemplate.execute(sql8);
					//更新_path的个数值
					String sql5="update "+tablename_receive+" set _path=( select count(*) from "+tablename_receive+"_path where tid='"+temporary_id_+"')";
					jdbcTemplate.execute(sql5);
				}
			}
		}
		//更新接收人 接收时间
		String user=userInfoVO.getAccount_();
		long nowtime = System.currentTimeMillis();
		SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = dfDateFormat.format(new Date());
		jdbcTemplate.execute("update archive_receive set jsr='"+user+"',jssj='"+time+"',jszt='已接收' where id_='"+id_+"'");
		//数据库存入导入的报表
		saveReportReceive(inDto,list10);
		String msg = "操作完成";
		outDto.setAppMsg(msg);
		return outDto;
	}

	//接收库---------》》》》》管理库,只针对条目进行操作
	public Dto receiveTodata(Dto inDto,UserInfoVO userInfoVO)throws Exception {
		Dto outDto=Dtos.newDto();
		String _classtree="";
		String tablename=inDto.getString("tablename");
		String rw_tablename=inDto.getString("rw_tablename");
		String id_=inDto.getString("id_");
		//根据id得到当前选中的临时库的数据表数据的id值
		String pch="";
		String lx_bh=inDto.getString("lx_bh");
		String sql="select "+lx_bh+" from "+rw_tablename+" where id_='"+id_+"'";
		//去除tablename的后缀
		String tablename_data=tablename.substring(0,tablename.lastIndexOf("_"));
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0&&list!=null){
			pch=(String)list.get(0).get(lx_bh);
		}
		String sql10="select * from "+tablename+" where pch='"+pch+"'";
		List<Map<String,Object>> list10=jdbcTemplate.queryForList(sql10);
		if(list10!=null&&list10.size()>0) {
			for (int j= 0; j< list10.size(); j++) {
				String temporary_id_=(String)list10.get(j).get("id_");
				String sql4="select * from "+tablename_data +" where id_ ='"+temporary_id_+"'";
				List<Map<String,Object>> list3=jdbcTemplate.queryForList(sql4);
				if(list3.size()>0&&list3!=null){
				}else{
					//数据单条迁移,暂时不采用删除的方式了
					String sql3="insert into "+tablename_data+" select * from "+tablename+" where pch='"+pch+"' and id_='"+temporary_id_+"'";
					jdbcTemplate.execute(sql3);
                    //更新lx值和classtree值
					Dto dout=Dtos.newDto();
					dout.put("key_","_classtree");
					dout.put("code_",tablename_data);
					List<Aos_sys_dicPO> aos_sys_dicPOS1 = aos_sys_dicMapper.dicBydesc(dout);
					if(aos_sys_dicPOS1!=null||aos_sys_dicPOS1.size()>0){
						_classtree=aos_sys_dicPOS1.get(0).getDesc_();
					}
					String sql5="update "+tablename_data+" set flag='0',_classtree='"+_classtree+"' where id_='"+temporary_id_+"'";
                    jdbcTemplate.execute(sql5);
				}
			}
		}
		//更新接收人 接收时间
		String user=userInfoVO.getAccount_();
		long nowtime = System.currentTimeMillis();
		SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = dfDateFormat.format(new Date());
		jdbcTemplate.execute("update "+rw_tablename+" set jsr='"+user+"',jssj='"+time+"',jszt='已接收' where id_='"+id_+"'");
		//数据库存入导入的报表
		saveReportReceive(inDto,list10);
		String msg = "操作完成";
		outDto.setAppMsg(msg);
		return outDto;
	}
	//临时库---------》》》》》管理库
	public Dto temporaryTodataData(Dto inDto,UserInfoVO userInfoVO)throws Exception {
		Dto outDto=Dtos.newDto();
		String tablename=inDto.getString("tablename");
		String id_=inDto.getString("id_");
		//根据id得到当前选中的临时库的数据表数据的id值
		String pch="";
		String zjbh=inDto.getString("zjbh");
		String sql="select pch from zlzj_n where id_='"+id_+"'";
		//去除tablename的后缀
		String tablename_data=tablename.substring(0,tablename.lastIndexOf("_"));
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0&&list!=null){
			pch=(String)list.get(0).get("pch");
		}
		String sql10="select * from "+tablename+" where pch='"+pch+"'";
		List<Map<String,Object>> list10=jdbcTemplate.queryForList(sql10);
		if(list10!=null&&list10.size()>0) {
			for (int j= 0; j< list10.size(); j++) {
				String temporary_id_=(String)list10.get(j).get("id_");
				String sql4="select * from "+tablename_data +" where id_ ='"+temporary_id_+"'";
				List<Map<String,Object>> list3=jdbcTemplate.queryForList(sql4);
				if(list3.size()>0&&list3!=null){

				}else{
					//数据单条迁移,暂时不采用删除的方式了
					String sql3="insert into "+tablename_data+" select * from "+tablename+" where pch='"+pch+"' and id_='"+temporary_id_+"'";
					jdbcTemplate.execute(sql3);
					//同时也把当前temporary_id_中的电子文件也上传一份到receive_path中
					temporaryToPathreceive(filePath,tablename,tablename_data,pch,temporary_id_);
					//更新_path的个数值
					String sql5="update "+tablename_data+" set _path=( select count(*) from "+tablename_data+"_path where tid='"+temporary_id_+"')";
					jdbcTemplate.execute(sql5);
				}
			}
		}
		//更新接收人 接收时间
		String user=userInfoVO.getAccount_();
		long nowtime = System.currentTimeMillis();
		SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = dfDateFormat.format(new Date());
		jdbcTemplate.execute("update zlzj_n set jsr='"+user+"',jssj='"+time+"',jszt='已接收' where id_='"+id_+"'");
		//数据库存入导入的报表
		//saveReportReceive(inDto,list10);
		String msg = "操作完成";
		outDto.setAppMsg(msg);
		return outDto;
	}
	private void saveReportReceive(Dto inDto, List<Map<String, Object>> list10) {
		String tablename=inDto.getString("tablename");
		Archive_receive_reportPO archive_receive_reportPO=new Archive_receive_reportPO();
		archive_receive_reportPO.setId_(AOSId.uuid());
		String pch=inDto.getString("pch");
		int file_number=getFileNumber(pch,tablename);
		archive_receive_reportPO.setFile_number(file_number+"");
		archive_receive_reportPO.setJsrq(AOSUtils.getDateTimeStr());
		archive_receive_reportPO.setNd(inDto.getString("nd"));
		archive_receive_reportPO.setPch(pch);
		archive_receive_reportPO.setQzh_name(inDto.getString("bmmc"));
		archive_receive_reportPO.setQzh_number(inDto.getString("bmbh"));
		archive_receive_reportPO.setTotal_number(list10.size()+"");
		//接收报告保存到数据库中
		archive_receive_reportMapper.insert(archive_receive_reportPO);
	}

	private int getFileNumber(String pch,String tablename) {
		String sql="select * from "+tablename+" where pch='"+pch+"'and  _path>=1";
		return jdbcTemplate.queryForList(sql).size();
	}

	private void temporaryToPathreceive(String filePath, String tablename, String tablename_receive, String pch, String temporary_id_) throws Exception{
		//1.根据temporary_id和tablename得到_path列表
		String sql="select * from "+tablename+"_path where tid ='"+temporary_id_+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String _path=(String)list.get(i).get("_path");
				String dirname=(String)list.get(i).get("dirname");
				String id_=(String)list.get(i).get("id_");
				//判断当前的id_是否在tablename _PATHk中存在
				String sql2="select * from "+tablename_receive+"_path where id_='"+id_+"'";
				List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
				if(list2!=null&&list2.size()>0){
						continue;
				}
				//文件拷贝，_path表添加
				//原路径文件全名
				/*String oldpathfile=filePath+tablename+dirname+_path;
				String newpathfile=filePath+tablename_receive+dirname+_path;
				String newpath=filePath+tablename_receive+dirname;
				boolean b=readTowrite(oldpathfile,newpathfile,newpath);*/
				//_path拷贝
				String sql3="insert into "+tablename_receive+"_path select * from "+tablename+"_path where  id_='"+id_+"'";
				jdbcTemplate.execute(sql3);
			}
		}
	}

	private boolean readTowrite(String oldpathfile, String newpathfile,String newpath)throws Exception{
		File oldfile=new File(oldpathfile);
		if(!oldfile.exists()){
			return false;
		}

		File news =new File(newpath);
		if(!news.exists()  && !news.isDirectory()){
			news.mkdirs();
		}
		File newfile=new File(newpathfile);
		if(!newfile.exists()){
			newfile.createNewFile();
		}
		//读取文件(缓存字节流)
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(oldfile));
		//写入相应的文件
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newfile));
		//读取数据
		//一次性取多少字节
		byte[] bytes = new byte[2048];
		//接受读取的内容(n就代表的相关数据，只不过是数字的形式)
		int n = -1;
		//循环取出数据
		while ((n = in.read(bytes,0,bytes.length)) != -1) {
			//转换成字符串
			String str = new String(bytes,0,n,"GBK");
			System.out.println(str);
			//写入相关文件
			out.write(bytes, 0, n);
		}
		//清楚缓存
		out.flush();
		//关闭流
		in.close();
		out.close();
		return true;
	}

	/**
	 *目录树
	 * @param qDto
	 * @return
	 */
	public String findTree(Dto qDto) {
		String name_="";
		String sql="select * from aos_sys_module where id_='"+qDto.getString("aos_module_id_")+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			name_=(String)list.get(0).get("name_");
		}
		return name_;
	}
	/**
	 *目录树
	 * @param qDto
	 * @return
	 */
	public String findNd(Dto qDto) {
		String nd="";
		String sql="select * from aos_sys_module where id_='"+qDto.getString("aos_module_id_")+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			nd=(String)list.get(0).get("name_");
		}
		return nd;
	}
	/**
	 *父节点目录树
	 * @param qDto
	 * @return
	 */
	public String findParentNd(Dto qDto) {
		String nd="";
		String sql="select * from aos_sys_module where id_ in (select parent_id_ from aos_sys_module where id_='"+qDto.getString("aos_module_id_")+"')";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			nd=(String)list.get(0).get("name_");
		}
		return nd;
	}
	public boolean updatereceive(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String sql = "update archive_receive set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			//提交任务添加进去
			notificationService.SubmitLinJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateTemporary(Dto inDto,UserInfoVO userInfoVO) {
		try{
			Archive_receivePO archive_receivePO=new Archive_receivePO();
			AOSUtils.copyProperties(inDto,archive_receivePO);
			archive_receiveMapper.updateByKey(archive_receivePO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean update_ziliao(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String sql = "update zlzj_n set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			//提交任务添加进去
			notificationService.SubmitZLZJ(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean update_ziyuan(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String sql = "update zyjs_n set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			//提交任务添加进去
			notificationService.SubmitZYJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceive_ziliao(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String sql = "update archive_receive set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			//提交任务添加进去
			notificationService.SubmitZiLiaoJS(inDto,userInfoVO);

			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceive_ziyuan(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String sql = "update archive_receive set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			//提交任务添加进去
			notificationService.SubmitZiYuanJS(inDto,userInfoVO);

			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceive_zhengji(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String sql = "update archive_receive set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			//提交任务添加进去
			notificationService.SubmitZhengJiJS(inDto,userInfoVO);

			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceive_shiti(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String sql = "update archive_receive set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			//提交任务添加进去
			notificationService.SubmitShiTinJS(inDto,userInfoVO);

			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveyes(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());

			String sql = "update archive_receive set flag_examine='已通过',shenhr='"+user+"',sfck='1',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskLinJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveyes_ziliao(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='已通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZiLiaoJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveyes_ziyuan(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='已通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZiYuanJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveyes_zhengji(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='已通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZhengJiJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveyes_shiti(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='已通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskShiTiJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateresourceyes(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_resource set flag_examine='已通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveno(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='未通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskLinJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatezhengjiyes(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update zlzj_n set flag_examine='已通过',shperson='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZLZJSH(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateziyuanyes(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update zyjs_n set flag_examine='已通过',shperson='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZYJSSH(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatezhengjino(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update zlzj_n set flag_examine='未通过',shperson='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZLZJSH(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateziyuanno(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update zyjs_n set flag_examine='未通过',shperson='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZYJSSH(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveno_ziliao(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='未通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZiLiaoJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveno_ziyuan(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='未通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZiYuanJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveno_zhengji(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='未通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskZhengJiJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatereceiveno_shiti(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_receive set flag_examine='未通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			notificationService.TaskShiTiJS(inDto,userInfoVO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateentityyes(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_entity set flag_examine='已通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatecollectyes(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_collect set flag_examine='已通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateentityno(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_entity set flag_examine='未通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatecollectno(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_collect set flag_examine='未通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateresourceno(Dto inDto,UserInfoVO userInfoVO) {
		try {
			//更新接收人 接收时间
			String next_kf_message=inDto.getString("next_kf_message");
			//更新审核人 审核时间
			String user=userInfoVO.getAccount_();
			long nowtime = System.currentTimeMillis();
			SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dfDateFormat.format(new Date());
			String sql = "update archive_resource set flag_examine='未通过',shr='"+user+"',shsj='"+time+"',shyj='"+next_kf_message+"'  where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateentity(Dto inDto,UserInfoVO userInfoVO) {
		try {
			String sql = "update archive_entity set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updatecollect(Dto inDto) {
		try {
			String sql = "update archive_collect set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateresource(Dto inDto) {
		try {
			String sql = "update archive_resource set flag_submit='已提交' where id_='" + inDto.getString("id_") + "'";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
    public String getDictionary(Dto inDto, String department) {
	    String dictionary="";
	    try{
            List<Map<String,Object>> list=jdbcTemplate.queryForList("select * from aos_sys_dic where dic_index_id_ in (select id_ from aos_sys_dic_index where key_='"+department+"') and code_='"+inDto.getString("treename")+"'");
            if(list!=null&&list.size()>0){
                dictionary=(String)list.get(0).get("desc_");
                return dictionary;
            }else{
                return dictionary;
            }
	    }catch(Exception e){
            e.printStackTrace();
            return dictionary;
        }
    }
	public String getDeprtmentindex(Dto inDto) {
		String index = "";
		String str = "";
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from archive_department_index where name_='" + inDto.getString("treename") + "' order by index_ desc");
			if (list != null && list.size() > 0) {
				index = (String) list.get(0).get("index_");
				//进行加一,并存到数据库中
				if (index == "" || index == null) {
					str = "0001";
				}
				int newindex = Integer.valueOf(index) + 1;
				str = String.format("%4d", newindex).replace(" ", "0");

			} else {
				str = "001";
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "001";
		}
		//存入到数据库中
		jdbcTemplate.execute("insert into archive_department_index(id_,name_,index_) values('"+AOSId.uuid()+"','"+inDto.getString("treename")+"','"+str+"')");
		return str;
	}
	public boolean addpici(Dto inDto) {
		try{
			Archive_receivePO archive_receivePO=new Archive_receivePO();
			AOSUtils.copyProperties(inDto,archive_receivePO);
			archive_receivePO.setId_(AOSId.uuid());
			archive_receivePO.setFlag_submit("未提交");
			archive_receivePO.setFlag_examine("未通过");
			archive_receiveMapper.insert(archive_receivePO);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean addentity(Dto inDto) {
		try{
			jdbcTemplate.execute("insert into archive_entity(id_,stmc,djms,djsj,djr,jszt,bmmc,bmbh,flag_submit,flag_examine) " +
					"values('"+AOSId.uuid()+"','"+inDto.getString("stmc")+"','"+inDto.getString("djms")+"','"+inDto.getString("djsj")+"','"+inDto.getString("djr")+"" +
					"','未接收','"+inDto.getString("bmmc")+"','"+inDto.getString("bmbh")+"','未提交','未通过')");
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean addcollect(Dto inDto) {
		try{
			jdbcTemplate.execute("insert into archive_collect(id_,zjmc,zjms,zjsj,zjr,jszt,flag_submit,flag_examine) " +
					"values('"+AOSId.uuid()+"','"+inDto.getString("zjmc")+"','"+inDto.getString("zjms")+"','"+inDto.getString("zjsj")+"','"+inDto.getString("zjr")+"" +
					"','未接收','未提交','未通过')");
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean addresource(Dto inDto) {
		try{
			jdbcTemplate.execute("insert into archive_resource(id_,zymc,zyms,zysj,jsr,jszt,flag_submit,flag_examine) " +
					"values('"+AOSId.uuid()+"','"+inDto.getString("zymc")+"','"+inDto.getString("zyms")+"','"+inDto.getString("zysj")+"','"+inDto.getString("jsr")+"" +
					"','未接收','未提交','未通过')");
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public void selectmath() {
		Archive_receivePO archive_receivePO=archive_receiveMapper.selectByKey("14785632941");
		System.out.print(archive_receivePO);
	}

	public List<Map<String, Object>> getDataListEntiry(Dto inDto, String account_) {
		String sql="select * from archive_entity ";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String, Object>> getDataListCollect(Dto inDto, String account_) {
		String sql="select * from archive_collect ";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String, Object>> getDataListResource(Dto inDto, String account_) {
		String sql="select * from archive_resource ";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}

    public List<Map<String, Object>> getDatabmmcsList() {
		String sql="select * from aos_sys_dic where dic_index_id_ in(select id_ from aos_sys_dic_index where key_='qzmc')";
		List<Map<String,Object>> listbmmc=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=new HashMap<>();
				map.put("bmbh",list.get(i).get("code_"));
				map.put("bmmc",list.get(i).get("desc_"));
				listbmmc.add(map);
			}
		}
		return listbmmc;
    }
	/**
	 *
	 * 查询设置顺序信息
	 *
	 * @param pDto
	 * @return
	 */
	public List<Map<String,Object>> listOrderInfos(Dto pDto) {
		String sql="select id_,FieldEnName,FieldCnName,indx,FieldView from archive_TableFieldList where tid in( select id_ from archive_TableName where TableName='"+pDto.getString("tablename")+"') order by indx";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		List<Map<String,Object>> list2=new ArrayList<Map<String,Object>>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=new HashMap<String,Object>();
				Integer fieldview=(Integer)list.get(i).get("FieldView");
				map.put("id_",list.get(i).get("id_"));
				map.put("fieldenname",list.get(i).get("FieldEnName"));
				map.put("fieldcnname",list.get(i).get("FieldCnName"));
				map.put("indx",list.get(i).get("indx"));
				if(fieldview==1){
					map.put("fieldview",true);
				}else{
					map.put("fieldview",false);
				}
				list2.add(map);
			}
		}
		return  list;
	}
	/**
	 * 修改字段顺序的索引值
	 *
	 * @param dto
	 * @return
	 */
	public boolean updateField_index(Dto dto) {
		// TODO Auto-generated method stub
		String zdid = dto.getString("zdid_");
		String[] zds = zdid.split(",");
		String view = dto.getString("view");
		String[] views = view.split(",");
		int indx = 0;
		if (zds.length > 0 && zds != null&&views.length > 0 && views != null&&zds.length==views.length) {
			for (int i = 0; i < zds.length; i++) {
				if("1".equals(views[i])||"true".equals(views[i])){
					String sql = "update archive_TableFieldList set indx='" + indx
							+ "', FieldView='1' where  id_='" + zds[i] + "'";
					jdbcTemplate.update(sql);
					indx = indx + 10;
				}else{
					String sql = "update archive_TableFieldList set indx='" + indx
							+ "', FieldView='0' where  id_='" + zds[i] + "'";
					jdbcTemplate.update(sql);
					indx = indx + 10;
				}


			}
			return true;
		}
		return false;
	}
	//视频档案
	/*public int testEffectiveTemp_spda(Dto qDto){
		String pch=qDto.getString("pch");
		String aos_rows_=qDto.getString("aos_rows_");
		String[] ids_=aos_rows_.split(",");
		aos_rows_.split(",");
		int dh_equalssum=0;
		int file_booleansum=0;
		int qzh_sum=0;
		int qzh_null=0;
		int jh_sum=0;
		int jh_null=0;
		int nd_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Spda_temporaryPO> list=getDatabypch_spda(pch,tablename);
		//此时从数据库获取当前用户的四性检测分类
		Archive_sxjcPO archive_sxjcPO=getsxjc_properties(qDto);
		if(archive_sxjcPO!=null){
			String dh_equals=archive_sxjcPO.getDh_equals();
			String file_boolean=archive_sxjcPO.getFile_boolean();
			String jh_=archive_sxjcPO.getJh();
			//String nd_=archive_sxjcPO.getNd();
			String qzh_=archive_sxjcPO.getQzh();
			int rows=0;
			for(int i=0;i<list.size();i++){
				//是否为空
				String strqzh="";
				String state="1";
				Spda_temporaryPO spda_temporaryPO= list.get(i);
				if(Arrays.asList(ids_).contains("9")){
					if(!isNumeric(spda_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有非法字符或不足四位。";
						state="2";
						qzh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("10")){
					if(!isNull(spda_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有空。";
						state="2";
						qzh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("15")){
					if(!isNumeric(spda_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位。";
						state="2";
						jh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("16")){
					if(!isNull(spda_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有空。";
						state="2";
						jh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("19")) {
					if (!testPDF(spda_temporaryPO.get_path(),spda_temporaryPO.getId_())) {
						strqzh = strqzh + "没有电子文件或文件已损坏。";
						state = "2";
						file_booleansum++;
					}
				}
				if(Arrays.asList(ids_).contains("7")) {
					if (!isEqualsNull(spda_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复。";
						state = "2";
						dh_equalssum++;
					}
				}
				//四性检测结果
				//1无问题2有问题 其他未检测
				//四性检测状态
				spda_temporaryPO.setSxjcjg(strqzh);
				spda_temporaryPO.setSxjczt(state);
				// manage_wsdaPO
				spda_temporaryMapper.updateByKey(spda_temporaryPO);
			}
		}
		//存到数据库中
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}*/
	//照片档案
	/*public int testEffectiveTemp_zpda(Dto qDto){
		String pch=qDto.getString("pch");
		String aos_rows_=qDto.getString("aos_rows_");
		String[] ids_=aos_rows_.split(",");
		aos_rows_.split(",");
		int dh_equalssum=0;
		int file_booleansum=0;
		int qzh_sum=0;
		int qzh_null=0;
		int jh_sum=0;
		int jh_null=0;
		int nd_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Zpda_temporaryPO> list=getDatabypch_zpda(pch,tablename);
		//此时从数据库获取当前用户的四性检测分类
		Archive_sxjcPO archive_sxjcPO=getsxjc_properties(qDto);
		if(archive_sxjcPO!=null){
			String dh_equals=archive_sxjcPO.getDh_equals();
			String file_boolean=archive_sxjcPO.getFile_boolean();
			String jh_=archive_sxjcPO.getJh();
			//String nd_=archive_sxjcPO.getNd();
			String qzh_=archive_sxjcPO.getQzh();
			int rows=0;
			for(int i=0;i<list.size();i++){
				//是否为空
				String strqzh="";
				String state="1";
				Zpda_temporaryPO zpda_temporaryPO= list.get(i);
				if(Arrays.asList(ids_).contains("9")){
					if(!isNumeric(zpda_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有非法字符或不足四位。";
						state="2";
						qzh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("10")){
					if(!isNull(zpda_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有空。";
						state="2";
						qzh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("15")){
					if(!isNumeric(zpda_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位。";
						state="2";
						jh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("16")){
					if(!isNull(zpda_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有空。";
						state="2";
						jh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("19")) {
					if (!testPDF(zpda_temporaryPO.get_path(),zpda_temporaryPO.getId_())) {
						strqzh = strqzh + "没有电子文件或文件已损坏。";
						state = "2";
						file_booleansum++;
					}
				}
				if(Arrays.asList(ids_).contains("7")) {
					if (!isEqualsNull(zpda_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复。";
						state = "2";
						dh_equalssum++;
					}
				}
				//四性检测结果
				//1无问题2有问题 其他未检测
				//四性检测状态
				zpda_temporaryPO.setSxjcjg(strqzh);
				zpda_temporaryPO.setSxjczt(state);
				// manage_wsdaPO
				zpda_temporaryMapper.updateByKey(zpda_temporaryPO);
			}
		}
		//存到数据库中
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}*/
	//新归档文书
	public int testEffectiveTemp_xgdws(Dto qDto){
		String pch=qDto.getString("pch");
		String aos_rows_=qDto.getString("aos_rows_");
		String[] ids_={"7","9","10","15","16","19"};
		aos_rows_.split(",");
		int dh_equalssum=0;
		int file_booleansum=0;
		int qzh_sum=0;
		int qzh_null=0;
		int jh_sum=0;
		int jh_null=0;
		int nd_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Xgdws_temporaryPO> list=getDatabypch_xgdws(pch,tablename);
		//此时从数据库获取当前用户的四性检测分类
		Archive_sxjcPO archive_sxjcPO=getsxjc_properties(qDto);
		if(archive_sxjcPO!=null){
			String dh_equals=archive_sxjcPO.getDh_equals();
			String file_boolean=archive_sxjcPO.getFile_boolean();
			String jh_=archive_sxjcPO.getJh();
			//String nd_=archive_sxjcPO.getNd();
			String qzh_=archive_sxjcPO.getQzh();
			int rows=0;
			for(int i=0;i<list.size();i++){
				//是否为空
				String strqzh="";
				String state="1";
				Xgdws_temporaryPO xgdws_temporaryPO= list.get(i);
				if(Arrays.asList(ids_).contains("9")){
					if(!isNumeric(xgdws_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有非法字符或不足四位。";
						state="2";
						qzh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("10")){
					if(!isNull(xgdws_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有空。";
						state="2";
						qzh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("15")){
					if(!isNumeric(xgdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位。";
						state="2";
						jh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("16")){
					if(!isNull(xgdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有空。";
						state="2";
						jh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("19")) {
					if (!testPDF(xgdws_temporaryPO.get_path(),xgdws_temporaryPO.getId_())) {
						strqzh = strqzh + "没有电子文件或文件已损坏。";
						state = "2";
						file_booleansum++;
					}
				}
				if(Arrays.asList(ids_).contains("7")) {
					if (!isEqualsNull(xgdws_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复。";
						state = "2";
						dh_equalssum++;
					}
				}
				//四性检测结果
				//1无问题2有问题 其他未检测
				//四性检测状态
				xgdws_temporaryPO.setSxjcjg(strqzh);
				xgdws_temporaryPO.setSxjczt(state);
				// manage_wsdaPO
				xgdws_temporaryMapper.updateByKey(xgdws_temporaryPO);
			}
		}
		//存到数据库中
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}
	//原归档文书
	public int testEffectiveTemp_gdws(Dto qDto){
		String pch=qDto.getString("pch");
		String aos_rows_=qDto.getString("aos_rows_");
		String[] ids_={"7","9","10","15","16","19"};
		aos_rows_.split(",");
		int dh_equalssum=0;
		int file_booleansum=0;
		int qzh_sum=0;
		int jh_sum=0;
		int qzh_null=0;
		int jh_null=0;
		int nd_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Gdws_temporaryPO> list=getDatabypch_gdws(pch,tablename);
		//此时从数据库获取当前用户的四性检测分类
		Archive_sxjcPO archive_sxjcPO=getsxjc_properties(qDto);
		if(archive_sxjcPO!=null){
			String dh_equals=archive_sxjcPO.getDh_equals();
			String file_boolean=archive_sxjcPO.getFile_boolean();
			String jh_=archive_sxjcPO.getJh();
			//String nd_=archive_sxjcPO.getNd();
			String qzh_=archive_sxjcPO.getQzh();
			int rows=0;
			for(int i=0;i<list.size();i++){
				//是否为空
				String strqzh="";
				String state="1";
				Gdws_temporaryPO gdws_temporaryPO= list.get(i);
				if(Arrays.asList(ids_).contains("9")){
					if(!isNumeric(gdws_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有非法字符或不足四位。";
						state="2";
						qzh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("10")){
					if(!isNull(gdws_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有空。";
						state="2";
						qzh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("15")){
					if(!isNumeric(gdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位。";
						state="2";
						jh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("16")){
					if(!isNull(gdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有空。";
						state="2";
						jh_null++;
					}
				}
				/*if("需要".equals(nd_)){
					if(!isNumeric(jnws_temporaryPO.getNd())){
						strqzh=strqzh+"年度有非法字符或不足四位。";
						state="2";
						nd_sum++;
					}
				}*/
				if(Arrays.asList(ids_).contains("19")) {
					if (!testPDF(gdws_temporaryPO.get_path(),gdws_temporaryPO.getId_())) {
						strqzh = strqzh + "没有电子文件或文件已损坏。";
						state = "2";
						file_booleansum++;
					}
				}
				if(Arrays.asList(ids_).contains("7")) {
					if (!isEqualsNull(gdws_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复。";
						state = "2";
						dh_equalssum++;
					}
				}
				//四性检测结果
				//1无问题2有问题 其他未检测
				//四性检测状态
				gdws_temporaryPO.setSxjcjg(strqzh);
				gdws_temporaryPO.setSxjczt(state);
				// manage_wsdaPO
				gdws_temporaryMapper.updateByKey(gdws_temporaryPO);
			}
		}
		//存到数据库中
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}
	//资料报纸
	public int testEffectiveTemp_zlbz(Dto qDto){
		String pch=qDto.getString("pch");
		String aos_rows_=qDto.getString("aos_rows_");
		String[] ids_=aos_rows_.split(",");
		aos_rows_.split(",");
		int dh_equalssum=0;
		int file_booleansum=0;
		int qzh_sum=0;
		int qzh_null=0;
		int jh_sum=0;
		int jh_null=0;
		int nd_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Zlbz_receivePO> list=getDatabypch_zlbz(pch,tablename);
		//此时从数据库获取当前用户的四性检测分类
		Archive_sxjcPO archive_sxjcPO=getsxjc_properties(qDto);
		if(archive_sxjcPO!=null){
			String dh_equals=archive_sxjcPO.getDh_equals();
			String file_boolean=archive_sxjcPO.getFile_boolean();
			String jh_=archive_sxjcPO.getJh();
			//String nd_=archive_sxjcPO.getNd();
			String qzh_=archive_sxjcPO.getQzh();
			int rows=0;
			for(int i=0;i<list.size();i++){
				//是否为空
				String strqzh="";
				String state="1";
				Zlbz_receivePO zlbz_receivePO= list.get(i);
				/*if(Arrays.asList(ids_).contains("9")){
					if(!isNumeric(zlbz_receivePO.getQzh())){
						strqzh=strqzh+"全宗号有非法字符或不足四位。";
						state="2";
						qzh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("10")){
					if(!isNull(zlbz_receivePO.getQzh())){
						strqzh=strqzh+"全宗号有空。";
						state="2";
						qzh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("15")){
					if(!isNumeric(xgdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位。";
						state="2";
						jh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("16")){
					if(!isNull(xgdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有空。";
						state="2";
						jh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("19")) {
					if (!testPDF(xgdws_temporaryPO.get_path(),xgdws_temporaryPO.getId_())) {
						strqzh = strqzh + "没有电子文件或文件已损坏。";
						state = "2";
						file_booleansum++;
					}
				}
				if(Arrays.asList(ids_).contains("7")) {
					if (!isEqualsNull(xgdws_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复。";
						state = "2";
						dh_equalssum++;
					}
				}*/
				//四性检测结果
				//1无问题2有问题 其他未检测
				//四性检测状态

				zlbz_receivePO.setSxjcjg(strqzh);
				zlbz_receivePO.setSxjczt(state);
				// manage_wsdaPO
				zlbz_receiveMapper.updateByKey(zlbz_receivePO);
			}
		}
		//存到数据库中
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}
	//资料图书
	public int testEffectiveTemp_zlts(Dto qDto){
		String pch=qDto.getString("pch");
		String aos_rows_=qDto.getString("aos_rows_");
		String[] ids_=aos_rows_.split(",");
		aos_rows_.split(",");
		int dh_equalssum=0;
		int file_booleansum=0;
		int qzh_sum=0;
		int qzh_null=0;
		int jh_sum=0;
		int jh_null=0;
		int nd_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Zlts_receivePO> list=getDatabypch_zlts(pch,tablename);
		//此时从数据库获取当前用户的四性检测分类
		Archive_sxjcPO archive_sxjcPO=getsxjc_properties(qDto);
		if(archive_sxjcPO!=null){
			String dh_equals=archive_sxjcPO.getDh_equals();
			String file_boolean=archive_sxjcPO.getFile_boolean();
			String jh_=archive_sxjcPO.getJh();
			//String nd_=archive_sxjcPO.getNd();
			String qzh_=archive_sxjcPO.getQzh();
			int rows=0;
			for(int i=0;i<list.size();i++){
				//是否为空
				String strqzh="";
				String state="1";
				Zlts_receivePO zlts_receivePO= list.get(i);
				/*if(Arrays.asList(ids_).contains("9")){
					if(!isNumeric(zlbz_receivePO.getQzh())){
						strqzh=strqzh+"全宗号有非法字符或不足四位。";
						state="2";
						qzh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("10")){
					if(!isNull(zlbz_receivePO.getQzh())){
						strqzh=strqzh+"全宗号有空。";
						state="2";
						qzh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("15")){
					if(!isNumeric(xgdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位。";
						state="2";
						jh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("16")){
					if(!isNull(xgdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有空。";
						state="2";
						jh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("19")) {
					if (!testPDF(xgdws_temporaryPO.get_path(),xgdws_temporaryPO.getId_())) {
						strqzh = strqzh + "没有电子文件或文件已损坏。";
						state = "2";
						file_booleansum++;
					}
				}
				if(Arrays.asList(ids_).contains("7")) {
					if (!isEqualsNull(xgdws_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复。";
						state = "2";
						dh_equalssum++;
					}
				}*/
				//四性检测结果
				//1无问题2有问题 其他未检测
				//四性检测状态

				zlts_receivePO.setSxjcjg(strqzh);
				zlts_receivePO.setSxjczt(state);
				// manage_wsdaPO
				zlts_receiveMapper.updateByKey(zlts_receivePO);
			}
		}
		//存到数据库中
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}
	//资料期刊
	public int testEffectiveTemp_zlqf(Dto qDto){
		String pch=qDto.getString("pch");
		String aos_rows_=qDto.getString("aos_rows_");
		String[] ids_=aos_rows_.split(",");
		aos_rows_.split(",");
		int dh_equalssum=0;
		int file_booleansum=0;
		int qzh_sum=0;
		int qzh_null=0;
		int jh_sum=0;
		int jh_null=0;
		int nd_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Zlqf_receivePO> list=getDatabypch_zlqf(pch,tablename);
		//此时从数据库获取当前用户的四性检测分类
		Archive_sxjcPO archive_sxjcPO=getsxjc_properties(qDto);
		if(archive_sxjcPO!=null){
			String dh_equals=archive_sxjcPO.getDh_equals();
			String file_boolean=archive_sxjcPO.getFile_boolean();
			String jh_=archive_sxjcPO.getJh();
			//String nd_=archive_sxjcPO.getNd();
			String qzh_=archive_sxjcPO.getQzh();
			int rows=0;
			for(int i=0;i<list.size();i++){
				//是否为空
				String strqzh="";
				String state="1";
				Zlqf_receivePO zlqf_receivePO= list.get(i);
				/*if(Arrays.asList(ids_).contains("9")){
					if(!isNumeric(zlbz_receivePO.getQzh())){
						strqzh=strqzh+"全宗号有非法字符或不足四位。";
						state="2";
						qzh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("10")){
					if(!isNull(zlbz_receivePO.getQzh())){
						strqzh=strqzh+"全宗号有空。";
						state="2";
						qzh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("15")){
					if(!isNumeric(xgdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位。";
						state="2";
						jh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("16")){
					if(!isNull(xgdws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有空。";
						state="2";
						jh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("19")) {
					if (!testPDF(xgdws_temporaryPO.get_path(),xgdws_temporaryPO.getId_())) {
						strqzh = strqzh + "没有电子文件或文件已损坏。";
						state = "2";
						file_booleansum++;
					}
				}
				if(Arrays.asList(ids_).contains("7")) {
					if (!isEqualsNull(xgdws_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复。";
						state = "2";
						dh_equalssum++;
					}
				}*/
				//四性检测结果
				//1无问题2有问题 其他未检测
				//四性检测状态

				zlqf_receivePO.setSxjcjg(strqzh);
				zlqf_receivePO.setSxjczt(state);
				// manage_wsdaPO
				zlqf_receiveMapper.updateByKey(zlqf_receivePO);
			}
		}
		//存到数据库中
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}

	public int testEffectiveTemp(Dto qDto){
		String pch=qDto.getString("pch");
		String aos_rows_=qDto.getString("aos_rows_");
		String[] ids_={"7","9","10","15","16","19"};
		aos_rows_.split(",");
		int dh_equalssum=0;
		int file_booleansum=0;
		int qzh_sum=0;
		int qzh_null=0;
		int jh_sum=0;
		int jh_null=0;
		int nd_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Jnws_temporaryPO> list=getDatabypch(pch,tablename);
			int rows=0;
			for(int i=0;i<list.size();i++){
				//是否为空
				String strqzh="";
				String state="1";
				Jnws_temporaryPO jnws_temporaryPO= list.get(i);
				if(Arrays.asList(ids_).contains("9")){
					if(!isNumeric(jnws_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有非法字符或不足四位。";
						state="2";
						qzh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("10")){
					if(!isNull(jnws_temporaryPO.getQzh())){
						strqzh=strqzh+"全宗号有空。";
						state="2";
						qzh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("15")){
					if(!isNumeric(jnws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位。";
						state="2";
						jh_sum++;
					}
				}
				if(Arrays.asList(ids_).contains("16")){
					if(!isNull(jnws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有空。";
						state="2";
						jh_null++;
					}
				}
				if(Arrays.asList(ids_).contains("19")){
					if (!testPDF(jnws_temporaryPO.get_path(),jnws_temporaryPO.getId_())) {
						strqzh = strqzh + "没有电子文件或文件已损坏。";
						state = "2";
						file_booleansum++;
					}
				}
				if(Arrays.asList(ids_).contains("7")){
					if (!isEqualsNull(jnws_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复。";
						state = "2";
						dh_equalssum++;
					}
				}
				//四性检测结果
				//1无问题2有问题 其他未检测
				//四性检测状态
				jnws_temporaryPO.setSxjcjg(strqzh);
				jnws_temporaryPO.setSxjczt(state);
				// manage_wsdaPO
				jnws_temporaryMapper.updateByKey(jnws_temporaryPO);
		}
		//存到数据库中
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}

	private boolean isEqualsNull(String dh, String tablename) {
			if(dh.length()==0){
				return false;
			}
			//判断档号是否重复
			List<Map<String,Object>> list=jdbcTemplate.queryForList("select * from "+tablename+" where dh='"+dh+"'");
			if(list.size()>=2){
				return false;
			}
			return true;
	}
	private List<Jnws_temporaryPO> getDatabypch(String pch,String tablename) {
		Dto out=Dtos.newDto();
		out.put("pch",pch);
		return jnws_temporaryMapper.list(out);
	}
	private List<Gdws_temporaryPO> getDatabypch_gdws(String pch,String tablename) {
		Dto out=Dtos.newDto();
		out.put("pch",pch);
		return gdws_temporaryMapper.list(out);
	}
	private List<Xgdws_temporaryPO> getDatabypch_xgdws(String pch,String tablename) {
		Dto out=Dtos.newDto();
		out.put("pch",pch);
		return xgdws_temporaryMapper.list(out);
	}
	/*private List<Spda_temporaryPO> getDatabypch_spda(String pch,String tablename) {
		Dto out=Dtos.newDto();
		out.put("pch",pch);
		return spda_temporaryMapper.list(out);
	}*/
	/*private List<Zpda_temporaryPO> getDatabypch_zpda(String pch,String tablename) {
		Dto out=Dtos.newDto();
		out.put("pch",pch);
		return zpda_temporaryMapper.list(out);
	}*/
	private List<Zlbz_receivePO> getDatabypch_zlbz(String pch,String tablename) {
		Dto out=Dtos.newDto();
		out.put("pch",pch);
		return zlbz_receiveMapper.list(out);
	}
	private List<Zlts_receivePO> getDatabypch_zlts(String pch,String tablename) {
		Dto out=Dtos.newDto();
		out.put("pch",pch);
		return zlts_receiveMapper.list(out);
	}
	private List<Zlqf_receivePO> getDatabypch_zlqf(String pch,String tablename) {
		Dto out=Dtos.newDto();
		out.put("pch",pch);
		return zlqf_receiveMapper.list(out);
	}
	/**
	 *
	 *
	 * @param str
	 * @return
	*/
	public boolean isNumeric(String str) {
		Dto outDto = Dtos.newDto();
		if(StringUtils.isEmpty(str)){
			return false;
		}
		//Pattern pattern = Pattern.compile("^-?[0-9]+"); //这个也行 "[1-9]\\d{3}"
		Pattern pattern = Pattern.compile("\\d{4}");//这个也行
		Matcher isNum = pattern.matcher(str);

		if (!isNum.matches()) {
			return false;

		}
		return true;
	}
	/**
	 * 检测电子文件真实性（是否存在，是否为0KB）
	 */
	public boolean testPDF(Integer _path,String id_){

		if(_path==0){
			return false;
		}else {
			String sqls = "select * from jnws_temporary_path where tid ='" + id_+"'" +
					"";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqls);
			if(list!=null&&list.size()>0){

				list.get(0).get("dirname");
				String path=filePath+list.get(0).get("dirname")+list.get(0).get("_s_path");
				File file=new File(path);
				if(!file.exists()){
					return false;
				}
				//判断是不是0字节
				long filelength=file.length();
				if(filelength<=0){
					return false;
				}
				return true;
			}
		}
		return false;

	}
	/**
	 * 检测字段是否为空
	 */
	public boolean isNull(String zd){
		if(zd==null||zd.length()<=0){
			return false;
		}else
			return true;
	}
    public Archive_sxjcPO getsxjc_properties(Dto qDto) {
		String user=qDto.getString("user");
		Dto out=Dtos.newDto();
		out.put("users",user);
		return  archive_sxjcMapper.selectOne(out);
    }
	public boolean savesxjc_properties(Dto qDto) {
		try{
			String user=qDto.getString("user");
			Archive_sxjcPO archive_sxjcPO2=new Archive_sxjcPO();
			Dto out=Dtos.newDto();
			out.put("users",user);
			Archive_sxjcPO archive_sxjcPO=archive_sxjcMapper.selectOne(out);
			AOSUtils.copyProperties(qDto,archive_sxjcPO2);
			if(archive_sxjcPO==null){
				archive_sxjcPO2.setId_(AOSId.uuid());
				archive_sxjcMapper.insert(archive_sxjcPO2);
			}else{
				archive_sxjcMapper.updateByKey(archive_sxjcPO2);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*public List<Map<String, Object>> getSxjc(Dto qDto) {
		String sql="select * from aos_sys_dic where dic_index_id_ in ( select id_ from aos_sys_dic_index where key_='sxjc') order by code_ asc";
		return jdbcTemplate.queryForList(sql);
	}*/
	public List<Map<String, Object>> getSxjc(Dto qDto) {
		String sql="select * from archive_sxjcxm where lx  like  '%"+qDto.getString("lx")+"%' order by bh asc";
		return jdbcTemplate.queryForList(sql);
	}
	public int operator_sxjc(Dto qDto){
		String pch=qDto.getString("pch");
		Archive_sxjc_resultPO archive_sxjc_resultPO=new Archive_sxjc_resultPO();
		archive_sxjc_resultPO.setId_(AOSId.uuid());
		//1-7（档号重复问题）
		int dh_equalssum=0;
		//全宗号长度
		int qzh_sum=0;
		//全宗号是否为空
		int qzh_null=0;
		//目录号长度
		int mlh_sum=0;
		//目录号是否为空
		int mlh_null=0;
		//件号长度
		int jh_sum=0;
		//件号是否为空
		int jh_null=0;
		//年度长度
		int nd_sum=0;
		//年度是否为空
		int nd_null=0;
		//1-9(全宗号 目录号长度)
		int post_sum=0;
		//3-4（电子文件是否存在并且可以打开吗）
		int file_booleansum=0;
		//2-4（全宗号、目录号、件号、年度是否为空）
		int null_sum=0;
		String tablename=qDto.getString("tablename");
		//得到当前批次的档案
		List<Jnws_temporaryPO> list=getDatabypch(pch,tablename);
		String[] selections = qDto.getRows();
		//得到数据字典的检测类型
		List<Aos_sys_dicPO> dictorarylist=getDictorary("sxjc");

		//删除之前的错误记录条目数据
		Dto out=Dtos.newDto();
		out.put("tid",archive_sxjc_resultPO.getId_());
		out.put("tablename",tablename);
		archive_sxjc_dataMapper.deleteByTid(out);
		for(int i=0;i<list.size();i++){
			//是否为空
			String strqzh="";
			String state="1";
			Jnws_temporaryPO jnws_temporaryPO= list.get(i);
			for (int k=0;k<dictorarylist.size();k++) {
				String code_=dictorarylist.get(k).getCode_();
				if("7".equals(code_)){
					if (!isEqualsNull(jnws_temporaryPO.getDh(),tablename)) {
						strqzh = strqzh + "没有档号或档号重复\r\n";
						state = "2";
						dh_equalssum++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("dh_equalssum");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}
				}
				if("9".equals(code_)) {
					if (!isNumeric(jnws_temporaryPO.getQzh())) {
						strqzh = strqzh + "全宗号有非法字符或不足四位\r\n";
						state = "2";
						qzh_sum++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("qzh_sum");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}
				}
				if("10".equals(code_)) {
					if (!isNull(jnws_temporaryPO.getQzh())) {
						strqzh = strqzh + "全宗号为空\r\n";
						state = "2";
						qzh_null++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("qzh_null");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}
				}
				if("11".equals(code_)) {
					if (!isNumeric(jnws_temporaryPO.getMlh())) {
						strqzh = strqzh + "目录号有非法字符或不足四位\r\n";
						state = "2";
						mlh_sum++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("mlh_sum");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}
				}
				if("12".equals(code_)) {
					if (!isNull(jnws_temporaryPO.getMlh())) {
						strqzh = strqzh + "目录号为空\r\n";
						state = "2";
						mlh_null++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("mlh_null");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}
				}
				if("15".equals(code_)) {
					if(!isNumeric(jnws_temporaryPO.getSxh())){
						strqzh=strqzh+"件号有非法字符或不足四位\r\n";
						state="2";
						jh_sum++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("jh_sum");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}
				}
				if("16".equals(code_)) {
					if (!isNull(jnws_temporaryPO.getSxh())) {
						strqzh = strqzh + "件号为空\r\n";
						state = "2";
						jh_null++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("jh_null");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}
				}
				if("13".equals(code_)) {
					/*if (!isNumeric(jnws_temporaryPO.getNd())) {
						strqzh = strqzh + "年度有非法字符或不足四位\r\n";
						state = "2";
						nd_sum++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("nd_sum");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}*/
				}
				if("14".equals(code_)) {
					/*if (!isNull(jnws_temporaryPO.getNd())) {
						strqzh = strqzh + "年度为空\r\n";
						state = "2";
						nd_null++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("nd_null");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}*/
				}
				if("19".equals(code_)){
					if (!testPDF(jnws_temporaryPO.get_path(),jnws_temporaryPO.getId_())){
						strqzh = strqzh + "没有找到电子文件或文件已损坏\r\n";
						state = "2";
						file_booleansum++;
						Archive_sxjc_dataPO archive_sxjc_dataPO=new Archive_sxjc_dataPO();
						archive_sxjc_dataPO.setId_(AOSId.uuid());
						archive_sxjc_dataPO.setTablename(tablename);
						archive_sxjc_dataPO.setTablename_id_(jnws_temporaryPO.getId_());
						archive_sxjc_dataPO.setText("file_booleansum");
						archive_sxjc_dataPO.setTid(archive_sxjc_resultPO.getId_());
						archive_sxjc_dataMapper.insert(archive_sxjc_dataPO);
					}
				}
				/*if("2-4".equals(code_)) {*/
					/*if (!isNull(jnws_temporaryPO.getQzh())||!isNull(jnws_temporaryPO.getMlh())||!isNull(jnws_temporaryPO.getNd())||!isNull(jnws_temporaryPO.getJh())) {
						strqzh = strqzh + "全宗号为空或目录号为空或年度为空或件号为空。";
						state = "2";
						null_sum++;
					}*/
			/*}*/
			}
			//四性检测结果
			//1无问题2有问题 其他未检测
			//四性检测状态
			jnws_temporaryPO.setSxjcjg(strqzh);
			jnws_temporaryPO.setSxjczt(state);
			// manage_wsdaPO
			jnws_temporaryMapper.updateByKey(jnws_temporaryPO);
		}
		//检测结果存放到数据库中
		//存到数据库中
		archive_sxjc_resultPO.setDh_equalssum(dh_equalssum);
		archive_sxjc_resultPO.setFile_booleansum(file_booleansum);
		archive_sxjc_resultPO.setNull_sum(null_sum);
		archive_sxjc_resultPO.setPost_sum(post_sum);
		archive_sxjc_resultPO.setPch(pch);
		archive_sxjc_resultPO.setQzh_sum(qzh_sum);
		archive_sxjc_resultPO.setQzh_null(qzh_null);
		archive_sxjc_resultPO.setMlh_sum(mlh_sum);
		archive_sxjc_resultPO.setMlh_null(mlh_null);
		archive_sxjc_resultPO.setNd_sum(nd_sum);
		archive_sxjc_resultPO.setNd_null(nd_null);
		archive_sxjc_resultPO.setJh_sum(jh_sum);
		archive_sxjc_resultPO.setJh_null(jh_null);
		archive_sxjc_resultMapper.insert(archive_sxjc_resultPO);
		return list.size();
	}
	/*
	得到四性检测数据字典
	 */
	private List<Aos_sys_dicPO> getDictorary(String sxjc) {
		Dto out=Dtos.newDto();
		out.put("key_",sxjc);
		List<Aos_sys_dicPO> listdic=aos_sys_dicMapper.listDicBydesc(out);
		return listdic;
	}

	public Map<String,Object> sxjc_report(Dto qDto) {
		Map<String,Object> map=new HashMap<String,Object>();
		String pch=qDto.getString("pch");
		String sql="select * from archive_sxjc_result where pch='"+pch+"' order by index_ desc";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			map=list.get(0);
		}
		return  map;
	}
	public Map<String,Object> receive_report(Dto qDto) {
		Map<String,Object> map=new HashMap<String,Object>();
		String pch=qDto.getString("pch");
		String sql="select * from archive_receive_report where pch='"+pch+"' order by index_ desc";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			map=list.get(0);
		}
		return  map;
	}

	public String getTabledesc(String tablename) {
		Dto out=Dtos.newDto();
		String tabledesc="";
		out.put("tablename",tablename);
		Archive_tablenamePO archive_tablenamePO=archive_tablenameMapper.selectOne(out);
		if(archive_tablenamePO!=null){
			tabledesc=archive_tablenamePO.getTabledesc();
		}
		return tabledesc;
	}

	/**
	 * 查询当前用户编研状态
	 * @param userInfo
	 * @return
	 */
	public String findCompilation_flag(UserInfoVO userInfo) {
		String compilation_flag="";
		String sql="select * from aos_sys_user where account_='"+userInfo.getAccount_()+"'";
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0) {
			compilation_flag=(String)list.get(0).get("compilation_flag_");
		}
		return compilation_flag;
	}

	public Object getPageSizeFromUser(String account_) {
		Dto out=Dtos.newDto();
		out.put("account_",account_);
		Aos_sys_userPO aos_sys_userPO = aos_sys_userMapper.selectOne(out);
		if(aos_sys_userPO!=null){
			return aos_sys_userPO.getPagesize();
		}else{
			return "";
		}
	}

	public Dto saveEntity(Dto inDto) {
		Dto outDto = Dtos.newDto();
		Stjs_nPO stjs_nPO = new Stjs_nPO();
		//集合接收编号查询是否存在重复的
		boolean b=add_getEquals_jsbh(inDto);
		if(b){
			AOSUtils.copyProperties(inDto,stjs_nPO);
			stjs_nPO.setId_(AOSId.uuid());
			stjs_nMapper.insert(stjs_nPO);
			outDto.setAppCode(AOSCons.SUCCESS);
			outDto.setAppMsg("操作完成,添加成功！！！");
			return outDto;
		}else{
			outDto.setAppCode(AOSCons.ERROR);
			outDto.setAppMsg("征集编号存在重复！！！");
			return outDto;
		}
	}




	private boolean add_getEquals_jsbh(Dto inDto) {
		Dto outDto=Dtos.newDto();
		outDto.put("jsbh",inDto.getString("jsbh"));
		List<Stjs_nPO> list = stjs_nMapper.list(outDto);
		if(list!=null&&list.size()>0){
			return false;
		}
		return true;
	}

	public Dto saveArchivezhengji(Dto inDto) {
		Dto outDto = Dtos.newDto();
		Dazj_nPO dazj_nPO = new Dazj_nPO();
		AOSUtils.copyProperties(inDto,dazj_nPO);
		dazj_nPO.setId_(AOSId.uuid());
		dazj_nMapper.insert(dazj_nPO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成,添加成功！！！");
		return outDto;
	}
	public Dto saveZiyuan(Dto inDto) {
		Dto outDto = Dtos.newDto();
		Zyjs_nPO zyjs_nPO = new Zyjs_nPO();
		AOSUtils.copyProperties(inDto,zyjs_nPO);
		zyjs_nPO.setId_(AOSId.uuid());
		zyjs_nMapper.insert(zyjs_nPO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成,添加成功！！！");
		return outDto;
	}
	public Dto saveData_ziyuan(Dto inDto) {
	    String tablename=inDto.getString("tablename");
		if("ybrb_receive".equals(tablename)){
            Dto outDto = Dtos.newDto();
            Ybrb_receivePO ybrb = new Ybrb_receivePO();
            AOSUtils.copyProperties(inDto,ybrb);
            ybrb.setId_(AOSId.uuid());
            ybrb_receiveMapper.insert(ybrb);
            outDto.setAppCode(AOSCons.SUCCESS);
            outDto.setAppMsg("操作完成,添加成功！！！");
            return outDto;
        }else if("ybxw_receive".equals(tablename)){
			Dto outDto = Dtos.newDto();
			Ybxw_receivePO ybxw = new Ybxw_receivePO();
			AOSUtils.copyProperties(inDto,ybxw);
			ybxw.setId_(AOSId.uuid());
			ybxw_receiveMapper.insert(ybxw);
			outDto.setAppCode(AOSCons.SUCCESS);
			outDto.setAppMsg("操作完成,添加成功！！！");
			return outDto;
		}else{
            Dto outDto = Dtos.newDto();
            Ybcb_receivePO ybcb = new Ybcb_receivePO();
            AOSUtils.copyProperties(inDto,ybcb);
			ybcb.setId_(AOSId.uuid());
            ybcb_receiveMapper.insert(ybcb);
            outDto.setAppCode(AOSCons.SUCCESS);
            outDto.setAppMsg("操作完成,添加成功！！！");
            return outDto;
        }
	}
	public Dto saveZiliao(Dto inDto) {
		Dto outDto = Dtos.newDto();
		Zlzj_nPO zlzj_nPO = new Zlzj_nPO();
		AOSUtils.copyProperties(inDto,zlzj_nPO);
		zlzj_nPO.setId_(AOSId.uuid());
		zlzj_nMapper.insert(zlzj_nPO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成,添加成功！！！");
		return outDto;
	}
	public Dto updateEntity(Dto inDto) {
		Dto outDto = Dtos.newDto();
		Stjs_nPO stjs_nPO = new Stjs_nPO();
		boolean b=update_getEquals_jsbh(inDto);
		if(b){
			AOSUtils.copyProperties(inDto,stjs_nPO);
			stjs_nMapper.updateByKey(stjs_nPO);
			outDto.setAppCode(AOSCons.SUCCESS);
			outDto.setAppMsg("操作完成，保存成功！！！");
			return outDto;
		}else{
			outDto.setAppCode(AOSCons.ERROR);
			outDto.setAppMsg("征集编号存在重复！！！");
			return outDto;
		}
	}

	private boolean update_getEquals_jsbh(Dto inDto) {
		Dto outDto=Dtos.newDto();
		String sql="select * from stjs_n where jsbh='"+inDto.getString("jsbh")+"' and id_ <>'"+inDto.getString("id_")+"'";
		List<Map<String,Object>> list =jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			return false;
		}
		return true;
	}
	public Dto updateArchivezhengji(Dto inDto) {
		Dto outDto = Dtos.newDto();
		Dazj_nPO dazj_nPO = new Dazj_nPO();
		AOSUtils.copyProperties(inDto,dazj_nPO);
		dazj_nMapper.updateByKey(dazj_nPO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，保存成功！！！");
		return outDto;
	}
	public Dto updateZiyuan(Dto inDto) {
		Dto outDto = Dtos.newDto();
		Zyjs_nPO zyjs_nPO = new Zyjs_nPO();
		AOSUtils.copyProperties(inDto,zyjs_nPO);
		zyjs_nMapper.updateByKey(zyjs_nPO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，保存成功！！！");
		return outDto;
	}
    public Dto updateData_ziyuan(Dto inDto) {
       String tablename=inDto.getString("tablename");
       if("ybrb_receive".equals(tablename)){
           Dto outDto = Dtos.newDto();
           Ybrb_receivePO ybrbPO = new Ybrb_receivePO();
           AOSUtils.copyProperties(inDto,ybrbPO);
           ybrb_receiveMapper.updateByKey(ybrbPO);
           outDto.setAppCode(AOSCons.SUCCESS);
           outDto.setAppMsg("操作完成，保存成功！！！");
           return outDto;
       }else if("ybxw_receive".equals(tablename)){
           Dto outDto = Dtos.newDto();
		   Ybxw_receivePO ybxw = new Ybxw_receivePO();
		   AOSUtils.copyProperties(inDto,ybxw);
		   ybxw_receiveMapper.updateByKey(ybxw);
           outDto.setAppCode(AOSCons.SUCCESS);
           outDto.setAppMsg("操作完成，保存成功！！！");
           return outDto;
       }else{
		   Dto outDto = Dtos.newDto();
		   Ybcb_receivePO ybcb = new Ybcb_receivePO();
		   AOSUtils.copyProperties(inDto,ybcb);
		   ybcb_receiveMapper.updateByKey(ybcb);
		   outDto.setAppCode(AOSCons.SUCCESS);
		   outDto.setAppMsg("操作完成，保存成功！！！");
		   return outDto;
	   }
    }
	public Dto updateZiliao(Dto inDto) {
		Dto outDto = Dtos.newDto();
		Zlzj_nPO zlzj_nPO = new Zlzj_nPO();
		AOSUtils.copyProperties(inDto,zlzj_nPO);
		zlzj_nMapper.updateByKey(zlzj_nPO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，保存成功！！！");
		return outDto;
	}
    public int deleteEntity(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            stjs_nMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }
	public int deleteArchivezhengji(Dto inDto){
		String[] selections = inDto.getRows();
		int rows=0;
		for(String id_ :selections){
			dazj_nMapper.deleteByKey(id_);
			rows++;
		}
		return rows;
	}
	public int deleteZiyuan(Dto inDto){
		String[] selections = inDto.getRows();
		int rows=0;
		for(String id_ :selections){
			zyjs_nMapper.deleteByKey(id_);
			rows++;
		}
		return rows;
	}
	public Integer deleteAllData_temporary(Dto inDto){
		try {
			Integer count=0;
			String query=inDto.getString("query");
			String pch=inDto.getString("pch");
			String tablename=inDto.getString("tablename");
			String sql="select count(id_) as ids from "+tablename+" where 1=1 "+ query+" and pch='"+pch+"'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if(list!=null&&list.size()>0){
				count=Integer.valueOf(list.get(0).get("ids")+"");
			}
			String delsql="delete  from "+tablename+" where 1=1 "+ query+" and pch='"+pch+"'";
			jdbcTemplate.execute(delsql);
			return count;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public int deleteZiliao(Dto inDto){
		String[] selections = inDto.getRows();
		int rows=0;
		for(String id_ :selections){
			zlzj_nMapper.deleteByKey(id_);
			rows++;
		}
		return rows;
	}

	public String findButton_jieshou(UserInfoVO userInfoVO) {
		String compilation_flag="";
		String sql="select * from aos_sys_user where account_='"+userInfoVO.getAccount_()+"'";
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0) {
			compilation_flag=(String)list.get(0).get("jieshou_module");
		}
		return compilation_flag;
	}

	public String getSHiTiIndex(Dto inDto) {
		String jsbh = "";
		String str = "";
		Integer index=0;
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select jsbh  from stjs_n where jsnd='" + inDto.getString("jsnd")+"' order by jsbh desc ");
			if (list != null && list.size() > 0) {
				jsbh =(String) list.get(0).get("jsbh");
				//进行加一,并存到数据库中
				index=Integer.valueOf(jsbh.substring(jsbh.length()-3));
				int newindex = index + 1;
				str = String.format("%3d", newindex).replace(" ", "0");
			} else {
				str = "001";
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "001";
		}
		return str;
	}
	public String getDangAnIndex(Dto inDto) {
		String zjbh = "";
		String str = "";
		Integer index=0;
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select zjbh  from dazj_n where zjsj='" + inDto.getString("zjnd")+"' order by zjbh desc ");
			if (list != null && list.size() > 0) {
				zjbh =(String) list.get(0).get("zjbh");
				//进行加一,并存到数据库中
				index=Integer.valueOf(zjbh.substring(zjbh.length()-3));
				int newindex = index + 1;
				str = String.format("%3d", newindex).replace(" ", "0");
			} else {
				str = "001";
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "001";
		}
		return str;
	}
	public String getZiYuanIndex(Dto inDto) {
		String jsbh = "";
		String str = "";
		Integer index=0;
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select jsbh  from zyjs_n where nd='" + inDto.getString("zynd")+"' order by jsbh desc ");
			if (list != null && list.size() > 0) {
				jsbh =(String) list.get(0).get("jsbh");
				//进行加一,并存到数据库中
				index=Integer.valueOf(jsbh.substring(jsbh.length()-3));
				int newindex = index + 1;
				str = String.format("%3d", newindex).replace(" ", "0");
			} else {
				str = "001";
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "001";
		}
		return str;
	}
	public String getZiLiaoIndex(Dto inDto) {
		String pch = "";
		String str = "";
		Integer index=0;
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select pch  from zlzj_n where zjnd='" + inDto.getString("zjnd")+"' and lx='"+inDto.getString("lx")+"' order by pch desc ");
			if (list != null && list.size() > 0) {
				pch =(String) list.get(0).get("pch");
				//进行加一,并存到数据库中
				index=Integer.valueOf(pch.substring(pch.length()-3));
				int newindex = index + 1;
				str = String.format("%3d", newindex).replace(" ", "0");
			} else {
				str = "001";
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "001";
		}
		return str;
	}

	public String datatemporaryDjIndex(Dto out) {
		String lx=out.getString("lx");
		String nd=out.getString("nd");
		String pch="";
		String str = "";
		Integer index=0;
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select  pch from archive_receive where 1=1 and pch like '"+lx+"%' and pch like '%"+nd+"%' order by pch desc");
			if (list != null && list.size() > 0) {
				pch =(String) list.get(0).get("pch");
				//进行加一,并存到数据库中
				index=Integer.valueOf(pch.substring(pch.length()-3));
				int newindex = index + 1;
				str = String.format("%3d", newindex).replace(" ", "0");
			} else {
				str = "001";
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "001";
		}
		return str;

	}
}