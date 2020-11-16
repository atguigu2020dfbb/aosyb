package cn.osworks.aos.system.modules.controller.archive;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.redisUtils.PageData;
import cn.osworks.aos.core.redisUtils.RedisCacheConfig;
import cn.osworks.aos.demo.dao.mapper.Demo_accountMapper;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.service.introduce.ImportTemporaryService;
import cn.osworks.aos.system.modules.service.log.LogService;
import cn.osworks.aos.web.report.AOSPrint;
import com.fasterxml.jackson.databind.util.JSONPObject;
import net.sf.jasperreports.engine.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.text.DocumentException;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.Aos_sys_dic_indexMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tableinputMapper;
import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.archive.UploadService;
import cn.osworks.aos.system.modules.service.dbtable.InputService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

/**
 * 
 * 数据页面控制器
 * 
 * @author shq
 * 
 * @date 2016-9-14
 */
@Controller
@RequestMapping(value = "archive/data")
public class DataController {
	@Autowired
	private DataService dataService;
	@Autowired
	private LogService logService;
	@Autowired
	private Demo_accountMapper demo_accountMapper;
	@Autowired
	private LogMapper logMapper;

	@Autowired
	private DocService docService;
	@Autowired
	private ImportTemporaryService importService;
	@Autowired
	private UploadService uploadService;

	@Autowired
	private InputService inputService;
	@Autowired
	private Archive_tableinputMapper archive_tableinputMapper;
	private static final String DEFAULT_FILE_NAME = "报表数据";

	// 用于支持同一个会话上缓存多个报表的参数名称，由URL传递过来。&aos_report_=会话中存储AOSPrint对象的Key
	private static final String AOSPRINT_PARAM = "aos_report_";
	@Autowired
	private Aos_sys_dic_indexMapper aos_sys_dic_indexMapper;
	@Autowired
	private SqlDao sysDao;
	public static String filePath = "";
	public static String linkAddress = "";
	public static String firstAddress = "";
	// 通过静态代码块读取配置文件
	static {
		try {
			Properties props = new Properties();
			InputStream in = DataService.class
					.getResourceAsStream("/config.properties");
			props.load(in);
			filePath = props.getProperty("filePath");
			linkAddress = props.getProperty("linkAddress");
			firstAddress = props.getProperty("firstAddress");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 页面初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initData")
	public String initData(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		String tree=dataService.findTree(qDto);
		String treenumber="";
		String treename="";
		if(tree!=null&&tree.length()>4){
			treenumber=tree.substring(0,4);
			treename=tree.substring(4);
		}else{
			if("全部".equals(tree)){
				treename="全部";
			}
		}
		//String queryclass = dataService.isAll(qDto);
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		//session.setAttribute("queryclass", queryclass);
		request.setAttribute("aos_module_id_", qDto.getString("aos_module_id_"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);									
		//return "aos/archive/tree.jsp";
		String associations_=qDto.getString("associations_");
		//此时查询数据字典，门类代码
		String mldm=dataService.getDectory_Dm(qDto.getString("tablename"));
		//得到当前目录树的名称和上一级名称
		String dalx=dataService.getTreeName(qDto.getString("aos_module_id_"));

		if(associations_==""||associations_==null||associations_.equals("非关联显示")){
			request.setAttribute("treename", treename);
			request.setAttribute("mldm",mldm);
			request.setAttribute("dalx",dalx);
			request.setAttribute("user", userInfoVO.getAccount_());
			request.setAttribute("treenumber", treenumber);
			request.setAttribute("cascode_id_",qDto.getString("cascode_id_"));
			System.out.println("页面初始化结束"+ AOSUtils.getDateTimeStr());
			if(qDto.getString("tablename").equals("ybrb")||qDto.getString("tablename").equals("ybxw")||qDto.getString("tablename").equals("ybcb")){
				return "aos/archive/datazy.jsp";
			}else{
				return "aos/archive/data.jsp";
			}

		}else if(associations_.equals("关联显示")){
			//将子表的tablename传递过去
			List<Map<String,Object>> list=dataService.getSonTablename(qDto.getString("tablename"));
			//此时查询关联表的名称，根据关联表的名称查询到列名
			List<Archive_tablefieldlistPO> titleDtosson = dataService
					.getDataFieldListTitle(list.get(0).get("t_table").toString());
			request.setAttribute("treename", treename);
			request.setAttribute("mldm",mldm);
			request.setAttribute("dalx",dalx);
			request.setAttribute("user", userInfoVO.getAccount_());
			request.setAttribute("treenumber", treenumber);
			request.setAttribute("cascode_id_",qDto.getString("cascode_id_"));
			request.setAttribute("fieldDtos_son", titleDtosson);
			request.setAttribute("pagesize_son", pagesize);
			request.setAttribute("tablename_son", list.get(0).get("t_table"));
			session.setAttribute("tablename_son", list.get(0).get("t_table"));
			request.setAttribute("queryclass_son","");
			return"aos/archive/upperLowerdata.jsp";
		}else{
			return "aos/archive/windowdata.jsp";
		}
	}
/**
 *
 * 页面初始化
 *
 * @param request
 * @param response
 * @return
 */
	@RequestMapping(value = "zz_initData")
	public String zz_initData(HttpSession session, HttpServletRequest request,
						   HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		String treenumber=dataService.findTree(qDto);
		if(treenumber!=null&&treenumber.length()>4){
			treenumber=treenumber.substring(0,4);
		}
		//String queryclass = dataService.isAll(qDto);
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		//session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		//return "aos/archive/tree.jsp";
		String associations_=qDto.getString("associations_");
		if(associations_==""||associations_==null||associations_.equals("非关联显示")){
			request.setAttribute("treenumber", treenumber);
			return "aos/utilization/zz_data.jsp";
		}else if(associations_.equals("关联显示")){
			//将子表的tablename传递过去
			List<Map<String,Object>> list=dataService.getSonTablename(qDto.getString("tablename"));
			if(list.size()>1){
				return "common/369.jsp";
			}else if(list.size()<1){
				return "common/147.jsp";
			}
			//此时查询关联表的名称，根据关联表的名称查询到列名
			List<Archive_tablefieldlistPO> titleDtosson = dataService
					.getDataFieldListTitle(list.get(0).get("t_table").toString());
			request.setAttribute("fieldDtosson", titleDtosson);
			request.setAttribute("tablenameson", list.get(0).get("t_table"));
			session.setAttribute("tablenameson", list.get(0).get("t_table"));
			request.setAttribute("queryclassson","");
			return"aos/archive/upperLowerdata.jsp";
		}else{
			return "aos/archive/windowdata.jsp";
		}

	}



	/**
			*
			* 页面初始化
 *
		 * @param request
 * @param response
 * @return
		 */
	@RequestMapping(value = "zz_initIndex")
	public String zz_initIndex( HttpServletRequest request,HttpServletResponse response,HttpSession session) {
			//自助查档设定用户登录
		UserInfoVO userInfoVO=new UserInfoVO();
		userInfoVO.setAccount_("root");
		userInfoVO.setName_("超级管理员");
		userInfoVO.setId_("fa04db9dd2f54d61b0c8202a25de2dc6");
		session.setAttribute(AOSCons.USERINFOKEY, userInfoVO);
		saveSessionLog(request, session, userInfoVO);
			return "aos/selfquery/index.jsp";
	}
	/**
	 * 保存会话日志
	 *
	 * @param request
	 * @param session
	 * @param userInfoVO
	 */
	private void saveSessionLog(HttpServletRequest request, HttpSession session, UserInfoVO userInfoVO) {
		try{
			Aos_log_sessionPO aos_log_sessionPO = new Aos_log_sessionPO();
			aos_log_sessionPO.setId_(session.getId());
			aos_log_sessionPO.setUser_id_(userInfoVO.getId_());
			aos_log_sessionPO.setAccount_(userInfoVO.getAccount_());
			aos_log_sessionPO.setUser_name_(userInfoVO.getName_());
			aos_log_sessionPO.setClient_type_(request.getHeader("USER-AGENT"));
			aos_log_sessionPO.setIp_address_(WebCxt.getClientIpAddr(request));
			aos_log_sessionPO.setOwner_(String.valueOf(request.getServerPort()));
			logService.saveSessionLog(aos_log_sessionPO);
		}catch (Exception e){

		}

	}
	/**
	 * 
	 * 页面初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initZpData")
	public String initZpData(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);

		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		request.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		return "aos/archive/zpdata.jsp";
	}

	/**
	 * 
	 * 页面初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initygdData")
	public String initygdData(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);

		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		request.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		return "aos/archive/dataygd.jsp";
	}
	/**
	 *
	 * 获得表单录入项column布局
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getInputColumn")
	public void getInputColumn(HttpServletRequest request,
							   HttpServletResponse response) {
		System.out.println("表单录入项开始"+ AOSUtils.getDateTimeStr());
		Dto inDto = Dtos.newDto(request);
		List<Archive_tableinputPO> list = archive_tableinputMapper.listColumn(inDto);
		System.out.println("表单录入项结束"+ AOSUtils.getDateTimeStr());
		WebCxt.write(response, AOSJson.toGridJson(list));

	}
	/**
	 * 
	 * 页面初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "aa")
	public String aaa(HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		return "aos/archive/bb.jsp";
	}
	/**
	 * 
	 * 查询数据信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listAccounts")
	public void listAccounts(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
			inDto.put("tablename", inDto.getString("tablename"));
			String queryclass = dataService.isAll(inDto);
			inDto.put("queryclass", queryclass);
			request.getSession().setAttribute("querySession",
					dataService.queryConditions(inDto));
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll(inDto,
					userInfoVO.getAccount_());
			int pCount = dataService.getPageTotalAll(inDto);
			String outString = AOSJson.toGridJson(fieldDtos, pCount);
			// 将分类坐标存放到session中
			session.setAttribute("queryclass", queryclass);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
	}
	/**
	 *
	 * 查询数据信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listAccounts_son")
	public void listAccounts_son(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		//String queryclass = dataService.isAll(inDto);
		String queryclass = "";
		inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySession",
				dataService.queryConditions(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll_son(inDto,
				userInfoVO.getAccount_());
		int pCount = dataService.getPageTotalAll_son(inDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 将分类坐标存放到session中
		session.setAttribute("queryclass", queryclass);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 查询子表数据信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "sonlistAccounts")
	public void sonlistAccounts(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablenameson", inDto.getString("tablenameson"));
		String queryclass = dataService.sonisAll(inDto);
		inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySessionson",
				dataService.queryConditions(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = dataService.getDataFieldListDisplayAllSon(inDto,
				userInfoVO.getAccount_());
		int pCount = dataService.getPageTotalSon(inDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 将分类坐标存放到session中
		session.setAttribute("sonqueryclass_son", queryclass);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 
	 * 查询数据信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listygdAccounts")
	public void listygdAccounts(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		if (inDto.getString("appraisal").equals("1")) {
			List<Map<String, Object>> sj = dataService.findSj(inDto);
			// 存入查询到的个数
			int count = sj.size();
			String outString = AOSJson.toGridJson(sj, count);
			// session域中存入鉴定标记
			session.setAttribute("appraisal", "1");
			// 添加日志
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			String tm = inDto.getString("tm");
			LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
					userInfoVO.getAccount_(),
					"鉴定档案[" + inDto.getString("tablename") + "]", tm, "鉴定",
					request.getRemoteAddr(), new Date()+"");
			logMapper.insert(logPO);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		} else {
			inDto.put("tablename", inDto.getString("tablename"));
			String queryclass = dataService.isAll(inDto);
			inDto.put("queryclass", queryclass);
			request.getSession().setAttribute("querySession",
					dataService.queryConditions(inDto));
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll(inDto,
					userInfoVO.getAccount_());
			int pCount = dataService.getPageTotal(inDto);
			String outString = AOSJson.toGridJson(fieldDtos, pCount);
			// 将分类坐标存放到session中
			session.setAttribute("queryclass", queryclass);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}
	}

	/**
	 * 
	 * 查询元数据信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listExifs")
	public void listExifs(HttpServletRequest request,
			HttpServletResponse response) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		// String queryclass = dataService.isAll(inDto);
		// inDto.put("queryclass", queryclass);
		// request.getSession().setAttribute("querySession",
		// dataService.queryConditions(inDto));
		List<Dto> fieldDtos = dataService.getDataExifs(inDto);
		int pCount = dataService.getPageTotal(inDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}

	/**
	 * 
	 * 查询文书档案元数据信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listWsdaExifs")
	public void listWsdaExifs(HttpServletRequest request,
			HttpServletResponse response) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		// String queryclass = dataService.isAll(inDto);
		// inDto.put("queryclass", queryclass);
		// request.getSession().setAttribute("querySession",
		// dataService.queryConditions(inDto));
		List<Dto> fieldDtos = dataService.getDataWsdaExifs(inDto);
		int pCount = dataService.getPageTotal(inDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}

	/**
	 * 
	 * 获得单条信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getData")
	public void getData(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Object outDto = dataService.getData(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 *
	 * 获得关联信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getupperLowerData")
	public void getupperLowerData(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.getupperLowerData(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 获得单条信息(子表)
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getDataSon")
	public void getDataSon(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Object outDto = dataService.getDataSon(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 * 
	 * 获得电子文件信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getPath")
	public void getPath(HttpServletRequest request, HttpServletResponse response) {
		Dto inDto = Dtos.newDto(request);


		List<Dto> pathDtos = dataService.getPath(inDto);

		// request.setAttribute("pathDtos", pathDtos);

		String outString = AOSJson.toGridJson(pathDtos);
		// Dto outDto = printService.getPath(inDto);
		WebCxt.write(response, outString);
	}

	/**
	 * 
	 * 打开电子文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "openSwfFile")
	public String openSwfFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String temp = "";
		Dto inDto = Dtos.newDto(request);
		// Dto outDto = Dtos.newDto();
		String strswf = "";
		int pageCount = 1;
		Properties prop = PropertiesLoaderUtils
				.loadAllProperties("config.properties");
		String linkAddress = prop.getProperty("linkAddress");
		String base = prop.getProperty("filePath")
				+ request.getParameter("tablename");// 获取username key对应的值
		inDto.put("base", base);
		String path1 = dataService.getDocumentPath(inDto);
		// D://dataaos/wsda/4bcd26dbc6fa409ab1d1e6f182d8241a/a0d6df862dbc4abab16d511d0978e02f.jpg
		if (inDto.getString("type").indexOf("jpg") > -1
				|| inDto.getString("type").indexOf("JPG") > -1) {
			// String filepath=prop.getProperty("filePath");
			pageCount = docService.jpeg2swf(base + "/" + path1);
		}
		if (inDto.getString("type").indexOf("pdf") > -1
				|| inDto.getString("type").indexOf("PDF") > -1) {
			pageCount = docService.pdf2swf(path1);
		}
		if (inDto.getString("type").indexOf("png") > -1
				|| inDto.getString("type").indexOf("PNG") > -1) {
			pageCount = docService.png2swf(path1);
		}
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getInfoFieldListTitle(inDto.getString("tablename") + "_info");
		// String count=String.valueOf(pageCount);
		request.setAttribute("tablename", inDto.getString("tablename"));
		request.setAttribute("id", inDto.getString("id"));
		request.setAttribute("strswf",
				(linkAddress + strswf).replaceAll("\\\\", "/"));
		request.setAttribute("tid", inDto.getString("tid"));
		request.setAttribute("fieldDtos", titleDtos);
		// String aa =linkAddress+strswf;
		// String bb=aa.replaceAll("\\\\", "/");
		// String bb=aa;
		return "common/zpdadocumentView.jsp";

	}

	/**
	 *
	 * 打开电子文件
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "openPdfFile")
	public String openPdfFile(HttpServletRequest request,
							  HttpServletResponse response) throws Exception {
		String temp = "";
		Dto inDto = Dtos.newDto(request);
		String tablename=inDto.getString("tablename");
		Properties prop = PropertiesLoaderUtils
				.loadAllProperties("config.properties");
		String linkAddress = prop.getProperty("linkAddress");
		String filePath = prop.getProperty("filePath");
		String path1 = dataService.getDocumentPath(inDto);
		//添加表明
		//String path = linkAddress  +tablename+"//"+ path1;
		//组合本地路径进行判断
		String bendipath=filePath+path1;
		//无需添加
		String path = linkAddress  + path1;
		//initData
		//此时判断路径jpg tif pdf

		if (path1.indexOf("jpg") > -1
				|| path1.indexOf("JPG") > -1) {
			// String filepath=prop.getProperty("filePath");
			path=docService.jpgToPdf(bendipath,"");
			path=linkAddress+"temp/temp.pdf";
		}
		if (path1.indexOf("tif") > -1
				|| path1.indexOf("TIF") > -1) {
			path=docService.tifToPdf(bendipath,"");
			path=linkAddress+"temp/temp.pdf";
		}
		if (path1.indexOf("pdf") > -1
				|| path1.indexOf("PDF") > -1) {

		}

		List<Archive_tablefieldlistPO> titleDtos = dataService.getInfoFieldListTitle(inDto.getString("tablename") + "_info");
		request.setAttribute("tablename", inDto.getString("tablename"));
		request.setAttribute("id", inDto.getString("id"));
		request.setAttribute("strswf", path);
		request.setAttribute("tid", inDto.getString("tid"));
		request.setAttribute("fieldDtos", titleDtos);
		return "common/documentView.jsp";
	}


	@RequestMapping(value = "openFileDbclick")
	public String openFileDbclick(HttpServletRequest request,
								HttpServletResponse response) {
		String temp = "";
		Dto inDto = Dtos.newDto(request);
		int pageCount = 1;
		String strswf = "";
		Properties prop;
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
			String base = prop.getProperty("filePath")
					+ inDto.getString("tablename");// 获取username key对应的值
			inDto.put("base", base);
			String path1 = dataService.getDocumentPath(inDto);
			// D://dataaos/wsda/4bcd26dbc6fa409ab1d1e6f182d8241a/a0d6df862dbc4abab16d511d0978e02f.jpg
			if (inDto.getString("type").indexOf("jpg") > -1
					||inDto.getString("type").indexOf("JPG") > -1) {
				pageCount = docService.jpeg2swf(path1);
			}
			if (inDto.getString("type").indexOf("pdf") > -1
					||inDto.getString("type").indexOf("PDF") > -1) {
				pageCount = docService.pdf2swf(path1);
			}
			if (inDto.getString("type").indexOf("png") > -1
					||inDto.getString("type").indexOf("PNG") > -1) {
				pageCount = docService.png2swf(path1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String aa="[{'pageCount':'"+pageCount+"'}]";
		String outString = "[{'pageCount':'" + pageCount + "'}]";
		WebCxt.write(response, outString);
		request.setAttribute("tablename", inDto.getString("tablename"));
		request.setAttribute("id", inDto.getString("id"));
		request.setAttribute("tid", inDto.getString("tid"));
		return "common/zpdadocumentView.jsp";
	}

	/**
	 * 通过swfupload查看电子文件
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "openFile")
	public String openFile(HttpServletRequest request,
								HttpServletResponse response) {
		String temp = "";
		Dto inDto = Dtos.newDto(request);
		int pageCount = 1;
		String strswf = "";
		Properties prop;
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
			String base = prop.getProperty("filePath");
					//+ inDto.getString("tablename");// 获取username key对应的值
			inDto.put("base", base);
			String path1 = dataService.getDocumentPath(inDto);
			// D://dataaos/wsda/4bcd26dbc6fa409ab1d1e6f182d8241a/a0d6df862dbc4abab16d511d0978e02f.jpg
			if (inDto.getString("type").indexOf("jpg") > -1||inDto.getString("type").indexOf("JPG") > -1) {
				pageCount = docService.jpeg2swf(base+File.separator+path1);
			}
			if (inDto.getString("type").indexOf("pdf") > -1||inDto.getString("type").indexOf("PDF") > -1) {
				pageCount = docService.pdf2swf(base+File.separator+path1);
			}
			if (inDto.getString("type").indexOf("png") > -1||inDto.getString("type").indexOf("PNG") > -1) {
				pageCount = docService.png2swf(base+File.separator+path1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String aa="[{'pageCount':'"+pageCount+"'}]";
		/*String outString = "[{'pageCount':'" + pageCount + "'}]";
		WebCxt.write(response, outString);*/
		request.setAttribute("tablename", inDto.getString("tablename"));
		request.setAttribute("id", inDto.getString("id"));
		request.setAttribute("index", inDto.getString("index"));
		request.setAttribute("tid", inDto.getString("tid"));
		return "common/zpdadocumentView.jsp";
	}
	/**
	 * 通过swfupload查看电子文件
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "openFileSwf")
	public void openFileSwf(HttpServletRequest request,
						   HttpServletResponse response) {
		String temp = "";
		Dto inDto = Dtos.newDto(request);
		int pageCount = 1;
		String strswf = "";
		Properties prop;
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
			String base = prop.getProperty("filePath")
					+ inDto.getString("tablename");// 获取username key对应的值
			inDto.put("base", base);
			String path1 = dataService.getDocumentPath(inDto);
			// D://dataaos/wsda/4bcd26dbc6fa409ab1d1e6f182d8241a/a0d6df862dbc4abab16d511d0978e02f.jpg
			if (inDto.getString("type").indexOf("jpg") > -1) {
				pageCount = docService.jpeg2swf(base+File.separator+path1);
			}
			if (inDto.getString("type").indexOf("pdf") > -1) {
				pageCount = docService.pdf2swf(base+File.separator+path1);
			}
			if (inDto.getString("type").indexOf("png") > -1) {
				pageCount = docService.png2swf(base+File.separator+path1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String aa="[{'pageCount':'"+pageCount+"'}]";
		/*String outString = "[{'pageCount':'" + pageCount + "'}]";
		WebCxt.write(response, outString);*/
		request.setAttribute("tablename", inDto.getString("tablename"));
		request.setAttribute("id", inDto.getString("id"));
		request.setAttribute("index", inDto.getString("index"));
		request.setAttribute("tid", inDto.getString("tid"));
	}
	/**
     *
     * 删除条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "deleteData")
    public void deletePrint(HttpServletRequest request,
                            HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = dataService.deleteData(dto);
        // 添加日志
        String tm = "";
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        if (dto.getString("tm").equals(",")) {
            tm = "";
        } else {
            tm = dto.getString("tm").split(",")[0];
        }
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                userInfoVO.getAccount_(), "删除条目[" + dto.getString("tablename")
                        + "]", tm, "删除条目", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        logMapper.insert(logPO);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     *
     * 彻底删除条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "deleteData_thorough")
    public void deleteData_thorough(HttpServletRequest request,
                            HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = dataService.deleteData_thorough(dto);
        // 添加日志
        String tm = "";
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        if (dto.getString("tm").equals(",")) {
            tm = "";
        } else {
            tm = dto.getString("tm").split(",")[0];
        }
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                userInfoVO.getAccount_(), "删除条目[" + dto.getString("tablename")
                        + "]", tm, "删除条目", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        logMapper.insert(logPO);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
	/**
	 *
	 * 删除条目
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deleteData_back")
	public void deleteData_back(HttpServletRequest request,
							HttpServletResponse response, HttpSession session) {
		Dto dto = Dtos.newDto(request);
		Dto outDto = dataService.deleteData_back(dto);
		// 添加日志
		String tm = "";
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		if (dto.getString("tm").equals(",")) {
			tm = "";
		} else {
			tm = dto.getString("tm").split(",")[0];
		}
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
				userInfoVO.getAccount_(), "删除条目[" + dto.getString("tablename")
						+ "]", tm, "删除条目", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 * 
	 * 删除电子文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deletePath")
	public void deletePath(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto dto = Dtos.newDto(request);
		Dto outDto = dataService.deletePath(dto);
		dataService.refreshPath(dto);
		// 日志
		// 加入日志
		String tm = dto.getString("tm");
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
				userInfoVO.getAccount_(),
				"删除电子文件[" + dto.getString("tablename") + "]", tm, "删除",
				request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);

		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 * 
	 * 删除全部电子文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deletePathAll")
	public void deletePathAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto dto = Dtos.newDto(request);
		Dto outDto = dataService.deletePathAll(dto);
		// 加入日志
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		String tm = dto.getString("tm");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
				userInfoVO.getAccount_(),
				"移除电子文件[" + dto.getString("tablename") + "]", tm, "移除",
				request.getRemoteAddr(), new Date()+"");
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 * 
	 * 获得表单录入项
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getInput")
	public void getInput(HttpServletRequest request,
			HttpServletResponse response) {
		Dto inDto = Dtos.newDto(request);
		List<Archive_tableinputPO> list = archive_tableinputMapper.list(inDto);
		WebCxt.write(response, AOSJson.toGridJson(list));

	}

	/**
	 * 
	 * 加载录入界面字典
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "load_dic_index")
	public void load_dic_index(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		qDto.setOrder("id_ DESC");
		qDto.put("catalog_id_", "520591d0ad114faf8a1a7e8191f35636");
		List<Dto> list = sysDao.list("MasterData.listDicindexInfos", qDto);
		qDto.put("dic_index_id_", list.get(0).getString("id_"));
		List<Aos_sys_dicPO> listdic = sysDao.list("MasterData.listDicInfos",
				qDto);
		System.out.print(AOSJson.toJson(listdic));
		WebCxt.write(response, AOSJson.toGridJson(listdic));
	}
	/**
	 * 加存信息
	 */
	@RequestMapping(value = "saveData")
	public void saveData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		Dto dto = Dtos.newDto(request);
		//加存之前查看是否存在索引
		boolean b=dataService.getIndex(dto);
		if(!b){
			Dto outDto = Dtos.newDto();
			outDto.setAppCode(AOSCons.ERROR);
			outDto.setAppMsg("数值重复");
			WebCxt.write(response, AOSJson.toJson(outDto));
		}else{
			Dto outDto = dataService.saveData(dto);
			String xdfields = inputService.listYnxd(dto);
			outDto.put("xdfields", xdfields);
			// 添加日志
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			// 得到题名
			String tm = dto.getString("tm");
			LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
					userInfoVO.getAccount_(), dto.getString("tablename"), tm,
					"添加条目", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			logMapper.insert(logPO);
			WebCxt.write(response, AOSJson.toJson(outDto));
		}

	}

	/**
	 * 
	 * 保存信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updateData")
	public void updateData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto dto = Dtos.newDto(request);
		//修改保存之前查看是否存在索引和重复值
		boolean b=dataService.getIndex_u(dto);
		if(!b){
			boolean c=dataService.getIndex(dto);
			if(!c){
				Dto outDto = Dtos.newDto();
				outDto.setAppCode(AOSCons.ERROR);
				outDto.setAppMsg("数值重复");
				WebCxt.write(response, AOSJson.toJson(outDto));
			}else{
				Dto outDto = dataService.updateData(dto);
				// 添加日志
				UserInfoVO userInfoVO = (UserInfoVO) session
						.getAttribute("_userInfoVO");
				// 得到题名
				String tm = dto.getString("tm");
				LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
						userInfoVO.getAccount_(), dto.getString("tablename"), tm,
						"修改条目", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				logMapper.insert(logPO);
				WebCxt.write(response, AOSJson.toJson(outDto));
			}
		}else {
			Dto outDto = dataService.updateData(dto);
			// 添加日志
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			// 得到题名
			String tm = dto.getString("tm");
			LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
					userInfoVO.getAccount_(), dto.getString("tablename"), tm,
					"修改条目", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			logMapper.insert(logPO);
			WebCxt.write(response, AOSJson.toJson(outDto));
		}
	}

	/**
	 * 
	 * 查询字段下拉框
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "queryFields")
	public void queryFields(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}
	/**
	 * 
	 * 记录更新
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updateRecord")
	public void updateRecord(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.updateRecord(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 记录更新(子表)
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updateRecord_son")
	public void updateRecord_son(HttpServletRequest request,
							 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.updateRecord(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 
	 * 记录替换
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "replaceRecord")
	public void replaceRecord(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.replaceRecord(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 记录替换(子表)
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "replaceRecord_son")
	public void replaceRecord_son(HttpServletRequest request,
							  HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.replaceRecord(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 
	 * 前后辍
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "suffixRecord")
	public void suffixRecord(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.suffixRecord(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 前后辍（子表）
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "suffixRecord_son")
	public void suffixRecord_son(HttpServletRequest request,
							 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.suffixRecord(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 
	 * 补位
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "repairRecord")
	public void repairRecord(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.repairRecord(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));

	}
	/**
	 *
	 * 补位(子表)
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "repairRecord_son")
	public void repairRecord_son(HttpServletRequest request,
							 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.repairRecord(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));

	}
	/**
	 * 填充报表
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JRException
	 */
	@RequestMapping(value = "fillReport")
	public void fillReport(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		List<Dto> list = dataService.exportData(qDto);
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle2(qDto.getString("tablename"));
		// 组装报表数据模型
		AOSReportModel reportModel = new AOSReportModel();
		reportModel.setFileName("导出信息表");
		// 设置报表集合
		reportModel.setFieldsList(list);
		reportModel.setExcelHeader(titleDtos);
		// 填充报表
		// AOSPrint aosPrint = AOSReport.fillReport(reportModel);
		// aosPrint.setFileName("excel表");
		session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
		WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
	}
	/**
	 * 填充报表
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JRException
	 */
	@RequestMapping(value = "fillReport_backup")
	public void fillReport_backup(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		String query= (String) session.getAttribute("querySession");
		qDto.put("query",query);
		List<Dto> list = dataService.exportData_backup(qDto);
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		// 组装报表数据模型
		AOSReportModel reportModel = new AOSReportModel();
		reportModel.setFileName("导出信息表");
		// 设置报表集合
		reportModel.setFieldsList(list);
		reportModel.setExcelHeader(titleDtos);
		// 填充报表
		// AOSPrint aosPrint = AOSReport.fillReport(reportModel);
		// aosPrint.setFileName("excel表");
		session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
		WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
	}

	/**
	 * 填充报表
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JRException
	 */
	@RequestMapping(value = "fillReportgd")
	public void fillReportgd(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		qDto.put("query", request.getSession().getAttribute("querySession"));
		String enfield = "";
		if (qDto.getString("tablename").equals("wsda")) {
			enfield = "jh,zrz,wjbh,tm,cwsj,ys,dh,qzh,nd,jgwtdm,bgqxdm,mj,rw,fjtm";
		}
		if (qDto.getString("tablename").equals("ctda")) {
			enfield = "";
		}
		qDto.put("enfield", enfield);
		List<Dto> list = dataService.exportDatagd(qDto);

		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		// 组装报表数据模型
		AOSReportModel reportModel = new AOSReportModel();
		reportModel.setFileName("导出信息表");
		// 设置报表集合
		reportModel.setFieldsList(list);
		reportModel.setExcelHeader(titleDtos);
		// 填充报表
		// AOSPrint aosPrint = AOSReport.fillReport(reportModel);
		// aosPrint.setFileName("excel表");
		session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
		WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
	}

	/**
	 *
	 * 数据导入
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initImport")
	public String initImport(HttpServletRequest request,
							 HttpServletResponse response,HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		request.setAttribute("tablename", qDto.get("tablename"));
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
		String localFilename=qDto.getString("file");
		List<Dto> titleDtos =new ArrayList<Dto>();
		if(localFilename.endsWith(".xls")){
			titleDtos =importService.readExcelTitleXls(excelPath+"/"+qDto.getString("file"));
		}else if(localFilename.endsWith(".xlsx")){
			titleDtos =importService.readExcelTitleXlsx(excelPath+"/"+qDto.getString("file"));
		}

		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("localFilename", qDto.getString("file"));
		request.setAttribute("ajtm", qDto.getString("ajtm"));
		request.setAttribute("_classtree", qDto.getString("_classtree"));
		session.setAttribute(AOSCons.USERINFOKEY,qDto.getUserInfo());
		return "aos/archive/import.jsp";
	}
	@RequestMapping(value = "initImport_temporary")
	public String initImport_temporary(HttpServletRequest request,
							 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		request.setAttribute("tablename", qDto.get("tablename"));
		request.setAttribute("pch",qDto.get("pch"));
		request.setAttribute("_classtree", qDto.get("_classtree"));
		return "aos/receive/importTemporary.jsp";
	}

	/**
	 *
	 * 数据导入
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initImport_backup")
	public String initImport_backup(HttpServletRequest request,
							 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		request.setAttribute("tablename", qDto.get("tablename"));
		return "aos/personbase2/import.jsp";

	}

	/**
	 * 
	 * 设置pagesize
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "setPagesize")
	public void setPagesize(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		qDto.put("id_", userInfoVO.getId_());
		Dto outDto = dataService.setPagesize(qDto);
		// request.setAttribute("tablename", qDto.get("tablename"));
		WebCxt.write(response, AOSJson.toJson(outDto));

	}

	/**
	 * 
	 * 重新加载SWFfile
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "loadswfFile")
	public String loadswfFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String temp = "";
		Dto inDto = Dtos.newDto(request);
		int pageCount = 1;
		Properties prop = PropertiesLoaderUtils
				.loadAllProperties("config.properties");
		String base = prop.getProperty("filePath")
				+ request.getParameter("tablename");// 获取username key对应的值
		inDto.put("base", base);
		String path1 = dataService.getDocumentPath(inDto);
		String count = String.valueOf(pageCount);
		try {
			if (inDto.getString("type").indexOf("jpg") > -1) {
				pageCount = docService.jpeg2swf(path1);
			}
			if (inDto.getString("type").indexOf("pdf") > -1) {
				// pageCount = docService.pdf2swf(path1);
			}
			if (inDto.getString("type").indexOf("png") > -1) {
				pageCount = docService.png2swf(path1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("count", count);
		if (inDto.getString("tablename").equals("zpda")) {
			temp = "common/sxdaView.jsp";
		} else {
			temp = "common/wsdaView.jsp";
		}
		return temp;

	}
	@RequestMapping(value = "dataTototal")
	public void dataTototal(HttpServletRequest request,
							  HttpServletResponse response) throws IOException {

		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.dataTotal(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * Excel预览初始跳转页面
	 * 
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "inialExcel")
	public void inialExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto dto = Dtos.newDto(request);
		String tablename = dto.getString("tablename");
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(tablename);
		request.setAttribute("fieldExcels", titleDtos);
	}

	/**
	 * 修改过期销毁档案标记
	 * 
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("removeappraisal")
	public void removeappraisal(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto outDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		// 修改鉴定标记值
		boolean b = dataService.updateappraisal(outDto);
		if (b) {
			out.setAppCode(1);
			// 添加日志
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			String tm = outDto.getString("tm").split(",")[0];
			LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
					userInfoVO.getAccount_(),
					"移动档案[" + outDto.getString("tablename") + "]", tm, "移动",
					request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			logMapper.insert(logPO);
		} else {
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 * 按条件删除
	 * 
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("deleteTermData")
	public void deleteTermData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto outDto = Dtos.newDto(request);
		Dto deleteTermData = dataService.deleteTermData(outDto);
		// 添加日志
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
				userInfoVO.getAccount_(),
				"删除条目[" + outDto.getString("tablename") + "]", "", "批量删除",
				request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(deleteTermData));
	}

	/**
	 * 移交档案
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping("transferData")
	public void transferData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		// 此时进行文件夹的路径选择
		String path = dataService.transferfilepathData(request, response,
				session);
		// 对路径进行判断，是否为空，是否选择了文件路径
		if (path == null || path == "") {
			return;
		}
		Dto outDto = Dtos.newDto(request);
		String flag = outDto.getString("flag");
		if (flag.equals("1")) {
			// 电子文件
			Dto dto = dataService.transferfile(outDto, path, filePath, request,
					response, session);
			WebCxt.write(response, AOSJson.toJson(dto));
		} else if (flag.equals("2")) {
			// 条目
			dataService
					.transferReport(outDto, path, request, response, session);
			Dto dto = Dtos.newOutDto();
			dto.setAppCode(2);
			// 设计电子条目路径
			dto.setAppMsg(path + File.separator + outDto.getString("tablename"));
			WebCxt.write(response, AOSJson.toJson(dto));
		} else {
			// 全部
			// 电子文件
			dataService.transferfile(outDto, path, filePath, request, response,
					session);
			dataService
					.transferReport(outDto, path, request, response, session);
			Dto out = Dtos.newOutDto();
			out.setAppCode(3);
			// 设计电子条目路径
			out.setAppMsg(path + File.separator + outDto.getString("tablename"));
			WebCxt.write(response, AOSJson.toJson(out));
		}
	}

	@RequestMapping(value = "deleteAllData")
	public void deleteAllData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.deleteAllData(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "deleteAllData_back")
	public void deleteAllData_back(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.deleteAllData_back(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "deleteAllData_back_son")
	public void deleteAllData_back_son(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.deleteAllData_back_son(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 销毁操作
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *            2018-9-29
	 */
	@RequestMapping(value = "jdappraisal")
	public void jdappraisal(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		int i = dataService.jdappraisal(qDto);
		Dto outDto = Dtos.newDto();
		outDto.setAppCode(i);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 * 移动到销毁对象中
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2018-9-29
	 */
	@RequestMapping(value = "removeappraisal2")
	public void removeappraisal2(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		int i = dataService.removeappraisal2(qDto);
		if (i == 1) {
			outDto.setAppMsg("移动成功!");
		} else {
			outDto.setAppMsg("移动失败!");
		}
	}

	/**
	 * 档案统计
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2018-10-9
	 */
	@RequestMapping(value = "countarchive")
	public void countarchive(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		List<Dto> countarchive = dataService.countarchive(qDto);
		String outString = AOSJson.toGridJson(countarchive);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}

	/**
	 * 下载电子文件
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("downloadPath")
	public void downloadPath(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		Dto dto = Dtos.newDto(request);
		// aos_rows_=ace1bf5e2b8f4c9a81f9422ed89a5eb6,, tablename=wsgdwj
		dataService.download_electronic_file(filePath, dto, response, request);

	}

	/**
	 * 下载说明书
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value = "downloadbook")
	public void downloadbook(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Dto inDto = Dtos.newDto(request);
		// 得到Excel路径
		String strdir = DocService.class.getResource("/").getFile()
				.replace("%20", " ");
		String excelPath = strdir.substring(1, strdir.lastIndexOf("WEB-INF"))
				+ "/temp/book";
		response.setContentType("application/msword");
		response.setHeader("Content-Disposition", "inline;filename="
				+ URLEncoder.encode("江西省人大档案管理系统说明书.doc", "utf-8"));
		// 好了 ，现在通过IO流来传送数据
		String strpath = excelPath + "//江西省人大档案管理系统说明书.doc";

		File file = new File(strpath);
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			byte[] buff = new byte[1024 * 10];// 可以自己 指定缓冲区的大小
			int len = 0;
			while ((len = input.read(buff)) > -1) {
				outputStream.write(buff, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取分类
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2018-11-20
	 */
	@RequestMapping("queryCatelogys")
	public void queryCatelogys(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto out = Dtos.newDto(request);
		List<Dto> list = dataService.queryCatelogys(out);
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}

	/**
	 * 更新分类
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2018-11-20
	 */
	@RequestMapping("refreshcategory")
	public void refreshcategory(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto out = Dtos.newDto(request);
		out.put("query", request.getSession().getAttribute("querySession"));
		Dto dtoout = dataService.refreshcategory(out);
		WebCxt.write(response, AOSJson.toJson(dtoout));
	}

	/**
	 * 执行排列
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2019-1-3
	 */
	@RequestMapping("toOrder_archive")
	public void toOrder_archive(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto out = Dtos.newDto(request);
		// 删除之前原有的排列条件
		dataService.deleteOrderby(out, session);
		boolean b = dataService.toOrder_archive(out, session, response);
		Dto dto = Dtos.newDto();
		if (b == true) {
			dto.setAppCode(1);
		} else {
			dto.setAppCode(0);
		}
		WebCxt.write(response, AOSJson.toJson(dto));
	}

	/**
	 * 返回上一次查询
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2019-1-18
	 * @throws IOException
	 */
	@RequestMapping("getSelectWhereLast")
	public void getSelectWhereLast(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		Dto out = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		out.put("Username", userInfoVO.getAccount_());
		List<Map<String, Object>> list = dataService.getSelectWhereLast(out);
		// 把list集合传递给前台
		String gridJson = dataService.addLastJson(list, out, response);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.print(gridJson);
	}

	/**
	 * 返回下一次查询
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2019-1-18
	 * @throws IOException
	 */
	@RequestMapping("getSelectWhereNext")
	public void getSelectWhereNext(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		Dto out = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		out.put("Username", userInfoVO.getAccount_());
		List<Map<String, Object>> list = dataService.getSelectWhereNext(out);
		// 把list集合传递给前台
		String gridJson = dataService.addNextJson(list, out, response);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.print(gridJson);
	}

	/**
	 * 清空查询条件缓存
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2019-1-19
	 */
	@RequestMapping("removeDataWhere")
	public void removeDataWhere(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto out = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		out.put("Username", userInfoVO.getAccount_());
		dataService.removDataWhere(out);
	}

	/**
	 * 返回拼接条件
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * 
	 *            2019-1-25
	 */
	@RequestMapping("saveQueryData")
	public void saveQueryData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto out = Dtos.newDto(request);
		String query = dataService.queryConditions2(out);
		Dto outDto = Dtos.newDto();
		outDto.setAppMsg(query);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 导出HTML格式
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value = "downloadHtml")
	public void downloadHtml(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException {
		Dto inDto = Dtos.newDto(request);
		String tablename = inDto.getString("tablename");
		String queryclass = dataService.isAll(inDto);
		inDto.put("queryclass", queryclass);
		List<Archive_tablefieldlistPO> titlelist = dataService
				.getTitleListDisplayAll(tablename);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		// 查询表信息
		List<Map<String, Object>> list = dataService
				.getDataFieldListDisplayAllXml(inDto, tablename,userInfoVO.getAccount_());
		// 查询表头信息
		// 循环迭代，进行返回
		StringBuilder readWriterXml = dataService
				.ReadWriterHtml(titlelist, list);
		
		//response.setContentType("text/html");
		response.setContentType("application/msword");
		response.setHeader("Content-Disposition", "inline;filename="
				+ URLEncoder.encode("html格式数据信息.html", "utf-8"));
		InputStream input = new ByteArrayInputStream(readWriterXml.toString()
				.getBytes());
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			byte[] buff = new byte[1024];// 可以自己 指定缓冲区的大小
			int len = 0;
			while ((len = input.read(buff)) > -1) {
				outputStream.write(buff, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} try {
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 导出txt格式
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value = "downloadTxt")
	public void downloadTxt(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException {
		Dto inDto = Dtos.newDto(request);
		String tablename = inDto.getString("tablename");
		String queryclass = dataService.isAll(inDto);
		inDto.put("queryclass", queryclass);
		List<Archive_tablefieldlistPO> titlelist = dataService
				.getTitleListDisplayAll(tablename);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		// 查询表信息
		List<Map<String, Object>> list = dataService
				.getDataFieldListDisplayAllXml(inDto, tablename,userInfoVO.getAccount_());
		// 查询表头信息
		// 循环迭代，进行返回
		StringBuilder readWriterTxt = dataService
				.ReadWriterTxt(titlelist, list);
		
		//response.setContentType("text/html");
		response.setContentType("application/msword");
		response.setHeader("Content-Disposition", "inline;filename="
				+ URLEncoder.encode("txt格式数据信息.txt", "utf-8"));
		InputStream input = new ByteArrayInputStream(readWriterTxt.toString()
				.getBytes());
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			byte[] buff = new byte[1024];// 可以自己 指定缓冲区的大小
			int len = 0;
			while ((len = input.read(buff)) > -1) {
				outputStream.write(buff, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} try {
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 统计查询
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value = "selectCount")
	public void selectCount(HttpServletRequest request,
			HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		String queryclass = (String)session.getAttribute("queryclass");
		if(queryclass.trim().equals("1=1")){
			inDto.put("queryclass", queryclass);
		}

		List<Map<String, Object>> list = dataService.selectCount(inDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 字段组合更新
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-3-4
	 */
	@RequestMapping("selectjoint")
	public void selectjoint(HttpServletRequest request,
			HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = dataService.selectjoint(inDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 行编辑
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-3-4
	 */
	@RequestMapping("editGrid")
	public void editGrid(HttpServletRequest request,
			HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		boolean b = dataService.editGrid(inDto);
		if(b){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
		
	}
	/**
	 * 得到当前条目的指导意见
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-4-30
	 */
	@RequestMapping("getZDYJ")
	public void getZDYJ(HttpServletRequest request,
			HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Archive_zdyjPO> list = dataService.getZDYJ(inDto);
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 添加指导意见
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-4-30
	 */
	@RequestMapping("addZddescription")
	public void addZddescription(HttpServletRequest request,
			HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		boolean t = dataService.addZddescription(inDto,session);
		if(t){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 生成xml文件
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-4-30
	 */
	@RequestMapping("deriveXML")
	public void deriveXML(HttpServletRequest request,
			HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		dataService.deriveXML(inDto);
	}
	/**
	 * 查询升降序数据字典
	 *
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-4-30
	 */
	@RequestMapping("orderComboBox")
	public void orderComboBox(HttpServletRequest request,
						  HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list =dataService.orderComboBox(inDto);
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 排列方式字段查询
	 *
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-4-30
	 */
	@RequestMapping("addOrder")
	public void addOrder(HttpServletRequest request,
							  HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);

		List<Map<String,Object>> list=dataService.addOrder(inDto);
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 保存记忆
	 *
	 * @author PXopenFile
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-4-30
	 */
	@RequestMapping("saveRemember")
	public void saveRemember(HttpServletRequest request,
								HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
				String user=userInfoVO.getAccount_();
		dataService.saveRemember(inDto,userInfoVO);
	}
	/**
	 * 获取记忆
	 *
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-4-30
	 */
	@RequestMapping("getRemember")
	public void getRemember(HttpServletRequest request,
							 HttpServletResponse response,HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		String user = userInfoVO.getAccount_();
		Map<String,Object> map=dataService.getRemember(inDto, userInfoVO);
		String outString = AOSJson.toJson(map);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
		}
	/**
	 * 接收
	 *
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-4-30
	 */
	@RequestMapping("jieshou")
		public void jieshou(HttpServletRequest request,
							HttpServletResponse response,HttpSession session){
		CloseableHttpClient client = HttpClientBuilder.create().build();

		URIBuilder uri = null;
		CloseableHttpResponse response2 = null;
		try {
			uri = new URIBuilder("http://5d5b453deb366.freetunnel.cc/YHOA/restservices/oarest/SyncentryDataFull/query");
			uri.setCharset(Charset.forName("UTF-8"));
			HttpGet hg = new HttpGet(uri.build());
			// 设置请求的报文头部的编码
			hg.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
			// 设置期望服务端返回的编码
			hg.setHeader(new BasicHeader("Accept", "text/plain;charset=UTF-8"));
			// 请求服务
			response2 = client.execute(hg);
			// 获取响应码
			int statusCode = response2.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				// 获取返回实例entity
				//HttpEntity entity = response.getEntity();
				// 通过EntityUtils的一个工具方法获取返回内容
				String result= EntityUtils.toString(response2.getEntity());
				// json_test.getString("aa");
				JSONObject jsonObject=JSONObject.fromObject(result);
				//提取出status为 success
				String status = jsonObject.getString("status");
				System.out.println("status:" + status);
				JSONArray contents = jsonObject.getJSONArray("content");

				for (int i = 0; i < contents.size(); i++) {
					//提取出currentCity为 青岛
					String flddocfrom = contents.getJSONObject(i).getString("flddocfrom");
					System.out.println("flddocfrom:" + flddocfrom);
					String fldinternalno = contents.getJSONObject(i).getString("fldinternalno");
					System.out.println("fldinternalno:" + flddocfrom);
					String fldtitle = contents.getJSONObject(i).getString("fldtitle");
					System.out.println("fldtitle:" + flddocfrom);
					String fldurgent = contents.getJSONObject(i).getString("fldurgent");
					System.out.println("fldurgent:" + fldurgent);
					String filename_l = contents.getJSONObject(i).getString("filename_l");
					System.out.println("filename_l:" + filename_l);

				}
				//JSONObject json = JSONObject.p
				//  System.out.println(result);
				// JSONObject obj = JSONObject.parseObject(result);
				//此时result是嵌套json数据

				//Map<String, Object> map=parseJSON2Map(result);
				System.out.println("3333");
				// 输出
			} else {
				// 输出
				System.out.println("请求失败,错误码为: " + statusCode);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (client != null) {
					client.close();
				}
				if (response != null) {
					response2.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
	public static Map<String, Object> parseJSON2Map(String jsonStr){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(jsonStr);
		for(Object k : json.keySet()){
			Object v = json.get(k);
			if(v instanceof JSONArray){
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				Iterator it = ((JSONArray)v).iterator();
				while(it.hasNext()){
					Object json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	/**
	 *
	 * 页面初始化(资料库管理)
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "personbase_initData")
	public String personbase_initData(HttpSession session, HttpServletRequest request,
						   HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		String tree=dataService.findTree(qDto);
		String treenumber="";
		String treename="";
		if(tree!=null&&tree.length()>4){
			treenumber=tree.substring(0,4);
			treename=tree.substring(4);
		}
		//String queryclass = dataService.isAll(qDto);
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		//session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		//return "aos/archive/tree.jsp";
		String associations_=qDto.getString("associations_");
		request.setAttribute("treename", treename);
		request.setAttribute("treenumber", treenumber);
		//return "aos/archive/tree.jsp";
			return "aos/personbase2/data.jsp";
	}

	/**
	 *
	 * 页面初始化(资料库管理回收站)
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "backup_initData")
	public String backup_initData(HttpSession session, HttpServletRequest request,
									  HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		String listtablename= qDto.getString("listtablename");
		String tablenamedesc=qDto.getString("tablenamedesc");
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(listtablename);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		String queryclass = dataService.isAll(qDto);
		request.setAttribute("tablename", qDto.getString("listtablename"));
		session.setAttribute("tablename", qDto.getString("listtablename"));
		request.setAttribute("listtablename",listtablename);
		request.setAttribute("tablenamedesc",tablenamedesc);
		session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		//return "aos/archive/tree.jsp";
		return "aos/personbase2/backup_data.jsp";
	}
    /**
     *
     * 页面初始化(资料库管理回收站)
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "export_initData")
    public String export_initData(HttpSession session, HttpServletRequest request,
                                  HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        String listtablename= qDto.getString("listtablename");
		String tablenamedesc=qDto.getString("tablenamedesc");
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(listtablename);
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        String pagesize = userInfoVO.getPagesize() + "";
        if (pagesize == null) {
            pagesize = "20";
        }
        String queryclass = dataService.isAll(qDto);
        request.setAttribute("tablename", qDto.getString("listtablename"));
        session.setAttribute("tablename", qDto.getString("listtablename"));
        request.setAttribute("listtablename",listtablename);
		request.setAttribute("tablenamedesc",tablenamedesc);
        session.setAttribute("queryclass", queryclass);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        //return "aos/archive/tree.jsp";
        return "aos/personbase2/export.jsp";
    }
	/**
	 *
	 * 查询数据信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listAccounts_data")
	public void listAccounts_data(HttpServletRequest request,
									HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		String tablename=inDto.getString("listtablename");
		if(tablename==""||tablename==null||tablename=="null"){

		}else{
			inDto.put("tablename", tablename);
			String queryclass = dataService.isAll(inDto);
			inDto.put("queryclass", queryclass);
			request.getSession().setAttribute("querySession",
					dataService.queryConditions(inDto));
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll_data(inDto,
					userInfoVO.getAccount_());
			int pCount = dataService.getPageTotal_backup(inDto);
			String outString = AOSJson.toGridJson(fieldDtos, pCount);
			// 将分类坐标存放到session中
			session.setAttribute("queryclass", queryclass);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}

	}
	/**
	 *
	 * 查询数据信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listAccounts_backup")
	public void listAccounts_backup(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		String tablename=inDto.getString("listtablename");
		if(tablename==""||tablename==null||tablename=="null"){

		}else{
			inDto.put("tablename", tablename);
			String queryclass = dataService.isAll(inDto);
			inDto.put("queryclass", queryclass);
			request.getSession().setAttribute("querySession",
					dataService.queryConditions(inDto));
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll_backup(inDto,
					userInfoVO.getAccount_());
			int pCount = dataService.getPageTotal_backup(inDto);
			String outString = AOSJson.toGridJson(fieldDtos, pCount);
			// 将分类坐标存放到session中
			session.setAttribute("queryclass", queryclass);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}
	}
    /**
     *
     * 还原条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "return_data")
    public void return_data(HttpServletRequest request,
                            HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = dataService.return_data(dto);

        WebCxt.write(response, AOSJson.toJson(outDto));
    }
	/**
	 * 选择表
	 *
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-20
	 */
	@RequestMapping(value="listZLKTablename")
	public void listZLKTablename(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		List<Archive_tablenamePO> list = dataService.findZLKTablename();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 * 填充报表
	 *
	 * @param request
	 * @param response
	 * @throws JRException
	 */
	@RequestMapping(value = "fillReport_wsda")
	public void fillReport_wsda(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		//组装报表数据模型
		jrxmlTOjasper(request,response);
		Dto out = Dtos.newDto(request);
		AOSReportModel reportModel = new AOSReportModel();
		Dto parameters = Dtos.newDto();
		parameters.put("create_user_", WebCxt.getUserInfo(session).getName_());
		//设置报表参数型
		reportModel.setParametersDto(parameters);
		Dto inDto = Dtos.newDto();
		inDto.setPageStart(0);
		inDto.setPageLimit(25);
		//只获取25条作为报表的演示数据
		List<Map<String,Object>> list=dataService.fillReport(out);
		//List<Demo_accountPO> list = demo_accountMapper.listPage(inDto);
		//设置报表集合
		reportModel.setFieldsList(list);
		//设置报表模版的编译文件
		reportModel.setJasperFile(session.getServletContext().getRealPath("/WEB-INF/template/report_wsda/AccountDetails.jasper"));
		//填充报表
		AOSPrint aosPrint = AOSReport.fillReport(reportModel);
		//这个设置主要是下载报表文件时候使用的缺省文件名
		aosPrint.setFileName("文书档案");
		//设置缺省的AOSPrint对象到会话中。
		session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, aosPrint);
		WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
	}
	//.jrxml转.jasper
	public void jrxmlTOjasper(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String strdir = DocService.class.getResource("/").getFile()
					.replace("%20", " ");
			String path = strdir.substring(1, strdir.lastIndexOf("WEB-INF"))
					+ "/WEB-INF/template/report_wsda/AccountDetails.jrxml";
			File file = new File(path);
			String parentPath = file.getParent();
			String jrxmlDestSourcePath = parentPath + "/AccountDetails.jasper";
			JasperCompileManager.compileReportToFile(path,
					jrxmlDestSourcePath);
			InputStream isRef = new FileInputStream(new File(jrxmlDestSourcePath));
			ServletOutputStream sosRef = response.getOutputStream();
			response.setContentType("application/pdf");
			JasperRunManager.runReportToPdfStream(isRef, sosRef, new HashMap(),
					new JREmptyDataSource());
			sosRef.flush();
			sosRef.close();
		} catch (JRException e) {   // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping("getSensitive")
	public void getSensitive(HttpServletRequest request, HttpServletResponse response){
		Dto out = null;
		try{
			Dto dtos=Dtos.newDto(request);
			out=dataService.getSensitive(dtos);
		}catch (Exception e){
			e.printStackTrace();
			out.setAppCode(1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "listOrderInfos")
	public void listOrderInfos(HttpServletRequest request,
							   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list=dataService.listOrderInfos(qDto);
		String outString = AOSJson.toGridJson(list,list.size());
		WebCxt.write(response, outString);
	}
	/**
	 * 保存字段显示顺序操作
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="updatezdOrder")
	public void updatezdOrder(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto outDto = Dtos.newDto(request);
		boolean b = dataService.updateField_index(outDto);
		if (!b) {
			outDto.setAppCode(-1);
			outDto.setAppMsg("系统出错了!");
		} else {
			outDto.setAppCode(1);
			outDto.setAppMsg("修改成功");
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 填充报表
	 *
	 * @param request
	 * @param response
	 * @throws JRException
	 */
	@RequestMapping(value = "fillReport_gd")
	public void fillReport_gd(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Dto out=Dtos.newDto(request);
		//组装报表数据模型
		AOSReportModel reportModel = new AOSReportModel();
		Dto parameters = Dtos.newDto();
		parameters.put("create_user_", WebCxt.getUserInfo(session).getName_());
		//设计查询条件
		String query=dataService.addQuery(out);
		out.put("query",query);
		List<Map<String,Object>> list=dataService.fillReport_gd(out);
		//添加默认的保管期限
		if(list!=null&&list.size()>0){
			parameters.put("bgqx", list.get(0).get("bgqx"));
		}
		//设置报表参数型
		reportModel.setParametersDto(parameters);
		reportModel.setFieldsList(list);
		//设置报表模版的编译文件
		//reportModel.setJasperFile(session.getServletContext().getRealPath("/WEB-INF/template/report/"+out.getString("tablename")+".jasper"));
		reportModel.setJasperFile(session.getServletContext().getRealPath("/WEB-INF/template/report/"+out.getString("printname")));
		//填充报表
		AOSPrint aosPrint = AOSReport.fillReport(reportModel);
		//这个设置主要是下载报表文件时候使用的缺省文件名
		aosPrint.setFileName("打印报表");
		//设置缺省的AOSPrint对象到会话中。
		session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, aosPrint);
		WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
	}
	/**
	 * 填充报表(预览)
	 *
	 * @param request
	 * @param response
	 * @throws JRException
	 */
	@RequestMapping(value = "fillLook_gd")
	public void fillLook_gd(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Dto out=Dtos.newDto(request);
		//组装报表数据模型
		AOSReportModel reportModel = new AOSReportModel();
		Dto parameters = Dtos.newDto();
		parameters.put("create_user_", WebCxt.getUserInfo(session).getName_());
		//设计查询条件
		String query=dataService.addQuery(out);
		out.put("query",query);
		List<Map<String,Object>> list=dataService.fillLook_gd(out);
		//添加默认的保管期限
		if(list!=null&&list.size()>0){
			parameters.put("bgqx", list.get(0).get("bgqx"));
		}
		//设置报表参数型
		reportModel.setParametersDto(parameters);
		reportModel.setFieldsList(list);
		//设置报表模版的编译文件
		//reportModel.setJasperFile(session.getServletContext().getRealPath("/WEB-INF/template/report/"+out.getString("tablename")+".jasper"));
		reportModel.setJasperFile(session.getServletContext().getRealPath("/WEB-INF/template/report/"+out.getString("printname")));
		//填充报表
		AOSPrint aosPrint = AOSReport.fillReport(reportModel);
		//这个设置主要是下载报表文件时候使用的缺省文件名
		aosPrint.setFileName("打印报表");
		//设置缺省的AOSPrint对象到会话中。
		session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, aosPrint);
		WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
	}

	/**
	 * 获取报表列表
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "reportprint")
	public void reportprint(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		List<Dto> list=dataService.reportprint(request,response,session);
		String outString = AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}

	/**
	 * 测试redis非关系型数据库的写入与读取操作
	 * @param request
	 * @param response
	 * @param session

	@RequestMapping(value = "jedisMethod")
	public void jedisMethod(){
		RedisCacheConfig redisCacheConfig=new RedisCacheConfig();
		JedisConnectionFactory jedisConnecionFactory = redisCacheConfig.getJedisConnecionFactory();
		JedisConnection connection = jedisConnecionFactory.getConnection();
		connection.select(3);
		Jedis jedis = connection.getNativeConnection();
		jedis.set("aaaa","bbbb");
	}
	*
	 * 测试redis非关系型数据库的写入与读取操作

	@RequestMapping(value = "getjedisMethod")
	public void getjedisMethod(){
		RedisCacheConfig redisCacheConfig=new RedisCacheConfig();
		JedisConnectionFactory jedisConnecionFactory = redisCacheConfig.getJedisConnecionFactory();
		JedisConnection connection = jedisConnecionFactory.getConnection();
		connection.select(3);
		Jedis jedis = connection.getNativeConnection();
		System.out.print(jedis.get("aaaa"));
	}*/
	/**
	 * 请讲接口 http://127.0.0.1:/项目名称/archive/data/redisDemo.jhtml
	 * demo展示的在redis存储读取数据的方式，本系统暂时用不到redis，此redis接口可根据实际业务需求选择使用
	 * 具体redis的应用场景->百度下即可
	 */
	@RequestMapping(value="/redisDemo")
	@ResponseBody
	public Object redis(){

		Map<String,Object> map = new HashMap<String,Object>();
		String result = "";

		docService.delete("fh0");											//删除
		docService.delete("fh");											//删除
		docService.delete("fh1");											//删除
		docService.delete("fh2");											//删除

		System.out.println(docService.addString("fh0","opopopo"));		//存储字符串
		System.out.println("获取字符串:"+docService.get("fh0"));			//获取字符串

		result += "获取字符串:"+docService.get("fh0")+",";

		Map<String, String> jmap = new HashMap<String, String>();
		jmap.put("name", "fhadmin");
		jmap.put("age", "22");
		jmap.put("qq", "313596790");
		System.out.println(docService.addMap("fh", jmap));				//存储Map
		System.out.println("获取Map:"+docService.getMap("fh"));			//获取Map

		result += "获取Map:"+docService.getMap("fh")+",";

		List<String> list = new ArrayList<String>();
		list.add("ssss");
		list.add("bbbb");
		list.add("cccc");
		docService.addList("fh1", list);									//存储List
		System.out.println("获取List:"+docService.getList("fh1"));			//获取List

		result += "获取List:"+docService.getList("fh1")+",";

		Set<String> set = new HashSet<String>();
		set.add("wwww");
		set.add("eeee");
		set.add("rrrr");
		docService.addSet("fh2", set);									//存储Set
		System.out.println("获取Set:"+docService.getSet("fh2"));			//获取Set

		result += "获取Set:"+docService.getSet("fh2")+",";

		map.put("result", result);
		PageData pd = new PageData();
		if(pd.containsKey("callback")){
			String callback = pd.get("callback").toString();
			return new JSONPObject(callback, map);
		}else{
			return map;
		}
	}


	@RequestMapping(value="uploadfiles")
	@ResponseBody
	public String uploadfiles(HttpServletRequest request,HttpServletResponse response,@RequestParam MultipartFile file)throws Exception {
		Dto dto = Dtos.newDto(request);
		String tablename = request.getParameter("tablename");//表名
		//死了路径
		String path = "D:/img/";
		String path2 = path+tablename+"/";
		File fileO = new File(path2);
		if (!fileO.exists()) {//判断文件夹是否存在
			fileO.mkdirs();//创建文件夹
		}
		String originalFilename = file.getOriginalFilename();
		File targetFile = new File(path2, originalFilename);
		file.transferTo(targetFile);

		//往path表中写入文件标记
		return "2";
	}
	@RequestMapping(value="openOnlineFile")
	public String openOnlineFile(HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("openOnlineFile table: "+request.getParameter("tablename")+"--id: "+request.getParameter("id"));
		return "aos/archive/onlinepreview.html";
	}
	//查询电子件
	@RequestMapping(value = "/selectAllfilePageByTid",method = RequestMethod.GET)
	@ResponseBody
	public String selectAllfilePageByTid(HttpServletRequest request,HttpServletResponse response) {
		Dto dto=Dtos.newDto(request);
		System.out.println("selectAllfilePageByTid table: "+dto.getString("tablename")+"-id: "+dto.getString("id"));
		List<Map<String,Object>> list = dataService.selectTablePathByTid(dto.getString("tablename"),dto.getString("id"));//table_path查找
		System.out.println("list:"+list.toString());
		String jsonData = list.toString().replace("{", "{\"").replace("}", "\"}").replace("=","\":\"").replace(", ", "\",\"").replace("}\",\"{", "},{");
		System.out.println("jsonData:"+jsonData);
		return "{\"message\":" + jsonData+ "}";
	}
	/**
	 * 获取扫描设置
	 * @param request
	 * @param response
	 * @param tablename
	 */
	@RequestMapping(value = "getSetScan")
	public void getSetScan(HttpServletRequest request,HttpServletResponse response,
						   @RequestParam(value = "tablename",required = false) String tablename) {
		Dto dto = Dtos.newDto(request);
		dto.setStringA(tablename);
		SetscanPO setscanPO = dataService.selectOneSetScan(dto);
		WebCxt.write(response, AOSJson.toJson(setscanPO));
	}

	/**
	 * 扫描设置保存信息
	 */
	@RequestMapping(value = "saveSetScan")
	public void saveSetScan(HttpServletRequest request,HttpServletResponse response) {
		SetscanPO setscanPO = new SetscanPO();
		Dto dto = Dtos.newDto(request);
		Dto outDto ;
		AOSUtils.copyProperties(dto,setscanPO);
		if (setscanPO.getId_() == null || setscanPO.getId_().equals("")){
			outDto = dataService.saveSetScan(setscanPO);

		}else {
			outDto = dataService.updateSetScan(setscanPO);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 *
	 * 添加扫描查询条件
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "addSetscan")
	public void addSetscan(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = dataService.insertSuffix(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
/**
 *
 * 添加扫描查询条件
 *
 * @param request
 * @param response
 */
	/*@RequestMapping(value = "getShuju")
	public void getShuju(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		 dataService.getShuju(qDto);
	}*/
}