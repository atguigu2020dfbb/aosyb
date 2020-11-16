package cn.osworks.aos.system.modules.controller.archive;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.Aos_sys_dic_indexMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tableinputMapper;
import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.DatatemporaryService;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.archive.UploadService;
import cn.osworks.aos.system.modules.service.dbtable.InputService;
import cn.osworks.aos.system.modules.service.introduce.ImportTemporaryService;
import cn.osworks.aos.web.report.AOSPrint;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import com.itextpdf.text.DocumentException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 数据页面控制器
 *
 * @author shq
 *
 * @date 2016-9-14
 */
@Controller
@RequestMapping(value = "archive/datatemporary")
public class DataTemporaryController {
	@Autowired
	private DatatemporaryService datatemporaryService;
	@Autowired
	private LogMapper logMapper;
	@Autowired
	private DocService docService;
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
    private ImportTemporaryService importService;
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
	@RequestMapping(value = "listReceive")
	public void listReceive(HttpSession session, HttpServletRequest request,
								 HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Map<String, Object>> fieldDtos=datatemporaryService.getReceive(inDto);
		String outString = AOSJson.toGridJson(fieldDtos);
		// 将分类坐标存放到session中
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping(value = "listReceive_zpda")
	public void listReceive_zpda(HttpSession session, HttpServletRequest request,
							HttpServletResponse response){
				Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Map<String, Object>> fieldDtos=datatemporaryService.getReceive_zpda(inDto);
		String outString = AOSJson.toGridJson(fieldDtos);
		// 将分类坐标存放到session中
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping(value = "listReceive_spda")
	public void listReceive_spda(HttpSession session, HttpServletRequest request,
								 HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Map<String, Object>> fieldDtos=datatemporaryService.getReceive_spda(inDto);
		String outString = AOSJson.toGridJson(fieldDtos);
		// 将分类坐标存放到session中
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping(value = "listReceive_ypda")
	public void listReceive_ypda(HttpSession session, HttpServletRequest request,
								 HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Map<String, Object>> fieldDtos=datatemporaryService.getReceive_ypda(inDto);
		String outString = AOSJson.toGridJson(fieldDtos);
		// 将分类坐标存放到session中
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 页面初始化
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "inittemporaryData")
	public String initData(HttpSession session, HttpServletRequest request,
						   HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		//查询当前用户的每页显示数
		Object pagesize =datatemporaryService.getPageSizeFromUser(userInfoVO.getAccount_());
		//Object pagesize = userInfoVO.getPagesize();
		if(pagesize == null||pagesize==""){
			pagesize = "20";
		}
		String treename=datatemporaryService.findTree(qDto);
		//String nd=datatemporaryService.findParentNd(qDto);
		String nd=qDto.getString("nd");
        //String nd=datatemporaryService.findNd(qDto);
		String queryclass = datatemporaryService.isAll(qDto);
		//得到当前用户的接收按钮权限
		String jieshou_module=datatemporaryService.findButton_jieshou(userInfoVO);
		//String  compilation_flag=datatemporaryService.findCompilation_flag(userInfoVO);
		session.setAttribute("jieshou_module",jieshou_module);
		//session.setAttribute("compilation_flag_",compilation_flag);
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("treename",treename);
		request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
        session.setAttribute("ndd",nd);
        request.setAttribute("flag",1);
		session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		request.setAttribute("user",qDto.getUserInfo().getName_());
		request.setAttribute("user_cn",qDto.getUserInfo().getName_());
		if("spda_temporary".equals(qDto.getString("tablename"))){
			return "aos/receive/datatemporary_spda.jsp";
		}else if("ypda_temporary".equals(qDto.getString("tablename"))){
			return "aos/receive/datatemporary_ypda.jsp";
		}else if("zpda_temporary".equals(qDto.getString("tablename"))){
			return "aos/receive/datatemporary_zpda.jsp";
		}
		//return "aos/archive/tree.jsp";
		return "aos/receive/datatemporary.jsp";
	}
	/**
	 *
	 * 页面初始化实体
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initshitiData")
	public String initshitiData(HttpSession session, HttpServletRequest request,
								 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		//查询当前用户的每页显示数
		Object pagesize =datatemporaryService.getPageSizeFromUser(userInfoVO.getAccount_());
		if(pagesize == null){
			pagesize = "20";
		}
		String treename=datatemporaryService.findTree(qDto);
		String nd=datatemporaryService.findNd(qDto);
		String queryclass = datatemporaryService.isAll(qDto);
		String  compilation_flag=datatemporaryService.findCompilation_flag(userInfoVO);
		session.setAttribute("compilation_flag_",compilation_flag);
		request.setAttribute("user_en", userInfoVO.getName_());
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
		request.setAttribute("treename",treename);
		request.setAttribute("nd",nd);
		session.setAttribute("accountname",userInfoVO.getAccount_());
		request.setAttribute("flag",2);
		session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		//return "aos/archive/tree.jsp";
		return "aos/receive/dataentity.jsp";
	}
	/**
	 *
	 * 页面初始化征集
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initzhengjiData")
	public String initziyuanData(HttpSession session, HttpServletRequest request,
								 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		//查询当前用户的每页显示数
		Object pagesize =datatemporaryService.getPageSizeFromUser(userInfoVO.getAccount_());
		if(pagesize == null){
			pagesize = "20";
		}
		String treename=datatemporaryService.findTree(qDto);
		String nd=datatemporaryService.findNd(qDto);
		String queryclass = datatemporaryService.isAll(qDto);
		String  compilation_flag=datatemporaryService.findCompilation_flag(userInfoVO);
		session.setAttribute("compilation_flag_",compilation_flag);
		request.setAttribute("user_en", userInfoVO.getName_());
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
		request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("treename",treename);
		request.setAttribute("nd",nd);
		request.setAttribute("flag",3);
		session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		//return "aos/archive/tree.jsp";
		return "aos/receive/datazhengji.jsp";
	}
	/**
	 *
	 * 页面初始化资料
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initziliaoData")
	public String initziliaoData(HttpSession session, HttpServletRequest request,
								 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		//查询当前用户的每页显示数
		Object pagesize =datatemporaryService.getPageSizeFromUser(userInfoVO.getAccount_());
		if(pagesize == null){
			pagesize = "20";
		}
		String treename=datatemporaryService.findTree(qDto);
		String queryclass = datatemporaryService.isAll(qDto);
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
		request.setAttribute("zjnd",qDto.getString("zjnd"));
		request.setAttribute("lx",qDto.getString("lx"));
		request.setAttribute("treename",treename);
		request.setAttribute("nd",qDto.getString("zjnd"));
		request.setAttribute("flag",5);
		session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
        request.setAttribute("user", qDto.getUserInfo().getName_());
		//return "aos/archive/tree.jsp";
		return "aos/receive/dataziliao.jsp";
	}
	@RequestMapping("stjslsh")
	public void stjslsh(HttpSession session, HttpServletRequest request,
						HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		String departmentindex=datatemporaryService.getSHiTiIndex(inDto);
		Dto out =Dtos.newDto();
		out.put("index",departmentindex);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("dazjlsh")
	public void dazjlsh(HttpSession session, HttpServletRequest request,
						HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		String departmentindex=datatemporaryService.getDangAnIndex(inDto);
		Dto out =Dtos.newDto();
		out.put("index",departmentindex);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("zyjslsh")
	public void zyjslsh(HttpSession session, HttpServletRequest request,
						HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		String departmentindex=datatemporaryService.getZiYuanIndex(inDto);
		Dto out =Dtos.newDto();
		out.put("index",departmentindex);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("zlzjlsh")
	public void zlzjlsh(HttpSession session, HttpServletRequest request,
						HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		String departmentindex=datatemporaryService.getZiLiaoIndex(inDto);
		Dto out =Dtos.newDto();
		out.put("index",departmentindex);
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 *
	 * 页面初始化资源
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "initentityData")
	public String initentityData(HttpSession session, HttpServletRequest request,
						   HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		//查询当前用户的每页显示数
		Object pagesize =datatemporaryService.getPageSizeFromUser(userInfoVO.getAccount_());
		if(pagesize == null){
			pagesize = "20";
		}
		String treename=datatemporaryService.findTree(qDto);
		String nd=datatemporaryService.findNd(qDto);
		String queryclass = datatemporaryService.isAll(qDto);
		String  compilation_flag=datatemporaryService.findCompilation_flag(userInfoVO);
		session.setAttribute("compilation_flag_",compilation_flag);
		request.setAttribute("user_en", userInfoVO.getName_());
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
		request.setAttribute("treename",treename);
		request.setAttribute("user",userInfoVO.getAccount_());
		request.setAttribute("nd",nd);
		request.setAttribute("jsnd",qDto.getString("nd"));
		request.setAttribute("lx",qDto.getString("lx"));
		request.setAttribute("flag",4);
		session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		//return "aos/archive/tree.jsp";
		return "aos/receive/dataziyuan.jsp";
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
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);

		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
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
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
		request.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		return "aos/archive/dataygd.jsp";
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
	@RequestMapping(value = "listAccounts_zhengji")
	public void listAccounts_zhengji(HttpServletRequest request,
									HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		String queryclass = datatemporaryService.isAll(inDto);
		queryclass=queryclass+" and zjsj='"+inDto.getString("zjsj")+"'";
		inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySession",
				datatemporaryService.queryConditions(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_zhengji(inDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal_zhengji(inDto);
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
	@RequestMapping(value = "listAccounts_entity")
	public void listAccounts_entity(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		String queryclass = datatemporaryService.isAll(inDto);
		queryclass=queryclass+" and jsnd='"+inDto.getString("jsnd")+"'";
		inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySession",
				datatemporaryService.queryConditions(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_entity(inDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal_entity(inDto);
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
	@RequestMapping(value = "listAccounts_temporary")
	public void listAccounts_temporary(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		String queryclass = datatemporaryService.isAll(inDto);
		inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySession",
				datatemporaryService.queryConditions_temporary(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll(inDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal_ziliao(inDto);
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
	@RequestMapping(value = "listAccounts")
	public void listAccounts(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		String queryclass = datatemporaryService.isAll(inDto);
		inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySession",
				datatemporaryService.queryConditions(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll(inDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal_ziliao(inDto);
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
		String queryclass = datatemporaryService.sonisAll(inDto);
		inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySessionson",
				datatemporaryService.queryConditions(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAllSon(inDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotalSon(inDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 将分类坐标存放到session中
		session.setAttribute("sonqueryclass", queryclass);
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
			List<Map<String, Object>> sj = datatemporaryService.findSj(inDto);
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
			String queryclass = datatemporaryService.isAll(inDto);
			inDto.put("queryclass", queryclass);
			request.getSession().setAttribute("querySession",
					datatemporaryService.queryConditions(inDto));
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll(inDto,
					userInfoVO.getAccount_());
			int pCount = datatemporaryService.getPageTotal(inDto);
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
		// String queryclass = datatemporaryService.isAll(inDto);
		// inDto.put("queryclass", queryclass);
		// request.getSession().setAttribute("querySession",
		// datatemporaryService.queryConditions(inDto));
		List<Dto> fieldDtos = datatemporaryService.getDataExifs(inDto);
		int pCount = datatemporaryService.getPageTotal(inDto);
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
		// String queryclass = datatemporaryService.isAll(inDto);
		// inDto.put("queryclass", queryclass);
		// request.getSession().setAttribute("querySession",
		// datatemporaryService.queryConditions(inDto));
		List<Dto> fieldDtos = datatemporaryService.getDataWsdaExifs(inDto);
		int pCount = datatemporaryService.getPageTotal(inDto);
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
		Object outDto = datatemporaryService.getData(qDto);
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
		Dto outDto = datatemporaryService.getupperLowerData(qDto);
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
		Object outDto = datatemporaryService.getDataSon(qDto);
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


		List<Dto> pathDtos = datatemporaryService.getPath(inDto);

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
		String path1 = datatemporaryService.getDocumentPath(inDto);
		// D://dataaos/wsda/4bcd26dbc6fa409ab1d1e6f182d8241a/a0d6df862dbc4abab16d511d0978e02f.jpg
		if (inDto.getString("type").indexOf("jpg") > -1
				|| inDto.getString("type").indexOf("JPG") > -1) {
			// String filepath=prop.getProperty("filePath");
			pageCount = docService.jpeg2swf(base + "/" + path1);
		}
		if (inDto.getString("type").indexOf("pdf") > -1) {
			pageCount = docService.pdf2swf(path1);
		}
		if (inDto.getString("type").indexOf("png") > -1) {
			pageCount = docService.png2swf(path1);
		}
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
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
		// Dto outDto = Dtos.newDto();
		Properties prop = PropertiesLoaderUtils
				.loadAllProperties("config.properties");
		String linkAddress = prop.getProperty("linkAddress");
		String path1 = datatemporaryService.getDocumentPath(inDto);
		String path = linkAddress  + path1;
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService.getInfoFieldListTitle(inDto.getString("tablename") + "_info");
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
			String path1 = datatemporaryService.getDocumentPath(inDto);
			// D://dataaos/wsda/4bcd26dbc6fa409ab1d1e6f182d8241a/a0d6df862dbc4abab16d511d0978e02f.jpg
			if (inDto.getString("type").indexOf("jpg") > -1) {
				pageCount = docService.jpeg2swf(path1);
			}
			if (inDto.getString("type").indexOf("pdf") > -1) {
				pageCount = docService.pdf2swf(path1);
			}
			if (inDto.getString("type").indexOf("png") > -1) {
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
			String path1 = datatemporaryService.getDocumentPath(inDto);
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
			String path1 = datatemporaryService.getDocumentPath(inDto);
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
	 * 获得表单录入项column布局
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getInputColumn")
	public void getInputColumn(HttpServletRequest request,
							   HttpServletResponse response) {
		Dto inDto = Dtos.newDto(request);
		List<Archive_tableinputPO> list = archive_tableinputMapper.listColumn(inDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
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
		Dto outDto = datatemporaryService.deleteData(dto);
		// 添加日志
		String tm = "";
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 删除批次任务
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deleteRw_pici")
	public void deleteRw_pici(HttpServletRequest request,
							HttpServletResponse response, HttpSession session) {
		Dto dto = Dtos.newDto(request);
		Dto outDto = datatemporaryService.deleteRw_pici(dto);
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
		Dto outDto = datatemporaryService.deleteData_thorough(dto);
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
		Dto outDto = datatemporaryService.deleteData_back(dto);
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
		Dto outDto = datatemporaryService.deletePath(dto);
		datatemporaryService.refreshPath(dto);
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
		Dto outDto = datatemporaryService.deletePathAll(dto);
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
		boolean b=datatemporaryService.getIndex(dto);
		if(!b){
			Dto outDto = Dtos.newDto();
			outDto.setAppCode(AOSCons.ERROR);
			outDto.setAppMsg("数值重复");
			WebCxt.write(response, AOSJson.toJson(outDto));
		}else{
			Dto outDto = datatemporaryService.saveData(dto);
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
		boolean b=datatemporaryService.getIndex_u(dto);
		if(!b){
			boolean c=datatemporaryService.getIndex(dto);
			if(!c){
				Dto outDto = Dtos.newDto();
				outDto.setAppCode(AOSCons.ERROR);
				outDto.setAppMsg("数值重复");
				WebCxt.write(response, AOSJson.toJson(outDto));
			}else{
				Dto outDto = datatemporaryService.updateData(dto);
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
			Dto outDto = datatemporaryService.updateData(dto);
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
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
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
		Dto outDto = datatemporaryService.updateRecord(qDto);
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
		Dto outDto = datatemporaryService.replaceRecord(qDto);
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
		Dto outDto = datatemporaryService.suffixRecord(qDto);
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
		Dto outDto = datatemporaryService.repairRecord(qDto);
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
		List<Dto> list = datatemporaryService.exportData(qDto);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
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
	@RequestMapping(value = "fillReport_backup")
	public void fillReport_backup(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		String query= (String) session.getAttribute("querySession");
		qDto.put("query",query);
		List<Dto> list = datatemporaryService.exportData_backup(qDto);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
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
		List<Dto> list = datatemporaryService.exportDatagd(qDto);

		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
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
							 HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		request.setAttribute("tablename", qDto.get("tablename"));
		request.setAttribute("pch",qDto.get("pch"));
		request.setAttribute("_classtree", qDto.get("_classtree"));
		return "aos/archive/importTemporary.jsp";

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
		Dto outDto = datatemporaryService.setPagesize(qDto);
		// request.setAttribute("tablename", qDto.get("tablename"));
		WebCxt.write(response, AOSJson.toJson(outDto));

	}
	/**
	 * 得到附件
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getfile_fj")
	public void getfile_fj(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		//标题列表
		String fileString= datatemporaryService.getfile_fj_zlzj(inDto);
		//int pCount = compilationService.getPageAll_compilation(inDto);
		//条目数据
		outDto.setAppMsg(fileString);
		String outString = AOSJson.toJson(outDto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 得到附件
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getfile_temporary")
	public void getfile_temporary(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		//标题列表getfile_entity
		String fileString= datatemporaryService.getfile_fj_temporary(inDto);
		//int pCount = compilationService.getPageAll_compilation(inDto);
		//条目数据
		outDto.setAppMsg(fileString);
		String outString = AOSJson.toJson(outDto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 得到附件
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getfile_ziyuan")
	public void getfile_ziyuan(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		//标题列表getfile_entity
		String fileString= datatemporaryService.getfile_fj_ziyuan(inDto);
		//int pCount = compilationService.getPageAll_compilation(inDto);
		//条目数据
		outDto.setAppMsg(fileString);
		String outString = AOSJson.toJson(outDto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 得到附件
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getfile_entity")
	public void getfile_entity(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		//标题列表
		String fileString= datatemporaryService.getfile_fj_entity(inDto);
		//int pCount = compilationService.getPageAll_compilation(inDto);
		//条目数据
		outDto.setAppMsg(fileString);
		String outString = AOSJson.toJson(outDto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 得到附件
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getfile_fj_entity")
	public void getfile_fj_entity(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		//标题列表
		String fileString= datatemporaryService.getfile_fj_entity(inDto);
		//int pCount = compilationService.getPageAll_compilation(inDto);
		//条目数据
		outDto.setAppMsg(fileString);
		String outString = AOSJson.toJson(outDto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
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
		String path1 = datatemporaryService.getDocumentPath(inDto);
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
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
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
		boolean b = datatemporaryService.updateappraisal(outDto);
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
		Dto deleteTermData = datatemporaryService.deleteTermData(outDto);
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
		String path = datatemporaryService.transferfilepathData(request, response,
				session);
		// 对路径进行判断，是否为空，是否选择了文件路径
		if (path == null || path == "") {
			return;
		}
		Dto outDto = Dtos.newDto(request);
		String flag = outDto.getString("flag");
		if (flag.equals("1")) {
			// 电子文件
			Dto dto = datatemporaryService.transferfile(outDto, path, filePath, request,
					response, session);
			WebCxt.write(response, AOSJson.toJson(dto));
		} else if (flag.equals("2")) {
			// 条目
			datatemporaryService
					.transferReport(outDto, path, request, response, session);
			Dto dto = Dtos.newOutDto();
			dto.setAppCode(2);
			// 设计电子条目路径
			dto.setAppMsg(path + File.separator + outDto.getString("tablename"));
			WebCxt.write(response, AOSJson.toJson(dto));
		} else {
			// 全部
			// 电子文件
			datatemporaryService.transferfile(outDto, path, filePath, request, response,
					session);
			datatemporaryService
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
		Dto outDto = datatemporaryService.deleteAllData(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "deleteAllData_back")
	public void deleteAllData_back(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		Dto outDto = datatemporaryService.deleteAllData_back(qDto);
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
		int i = datatemporaryService.jdappraisal(qDto);
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
		int i = datatemporaryService.removeappraisal2(qDto);
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
		List<Dto> countarchive = datatemporaryService.countarchive(qDto);
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
		datatemporaryService.download_electronic_file(filePath, dto, response, request);

	}

	/**
	 * 下载说明书
	 *
	 * @param request
	 * @param response
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
		List<Dto> list = datatemporaryService.queryCatelogys(out);
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
		Dto dtoout = datatemporaryService.refreshcategory(out);
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
		datatemporaryService.deleteOrderby(out, session);
		boolean b = datatemporaryService.toOrder_archive(out, session, response);
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
		List<Map<String, Object>> list = datatemporaryService.getSelectWhereLast(out);
		// 把list集合传递给前台
		String gridJson = datatemporaryService.addLastJson(list, out, response);
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
		List<Map<String, Object>> list = datatemporaryService.getSelectWhereNext(out);
		// 把list集合传递给前台
		String gridJson = datatemporaryService.addNextJson(list, out, response);
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
		datatemporaryService.removDataWhere(out);
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
		String query = datatemporaryService.queryConditions2(out);
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
		String queryclass = datatemporaryService.isAll(inDto);
		inDto.put("queryclass", queryclass);
		List<Archive_tablefieldlistPO> titlelist = datatemporaryService
				.getTitleListDisplayAll(tablename);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		// 查询表信息
		List<Map<String, Object>> list = datatemporaryService
				.getDataFieldListDisplayAllXml(inDto, tablename,userInfoVO.getAccount_());
		// 查询表头信息
		// 循环迭代，进行返回
		StringBuilder readWriterXml = datatemporaryService
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
		String queryclass = datatemporaryService.isAll(inDto);
		inDto.put("queryclass", queryclass);
		List<Archive_tablefieldlistPO> titlelist = datatemporaryService
				.getTitleListDisplayAll(tablename);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		// 查询表信息
		List<Map<String, Object>> list = datatemporaryService
				.getDataFieldListDisplayAllXml(inDto, tablename,userInfoVO.getAccount_());
		// 查询表头信息
		// 循环迭代，进行返回
		StringBuilder readWriterTxt = datatemporaryService
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

		List<Map<String, Object>> list = datatemporaryService.selectCount(inDto);
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
		Dto outDto = datatemporaryService.selectjoint(inDto);
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
		boolean b = datatemporaryService.editGrid(inDto);
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
		List<Archive_zdyjPO> list = datatemporaryService.getZDYJ(inDto);
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
		boolean t = datatemporaryService.addZddescription(inDto,session);
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
		datatemporaryService.deriveXML(inDto);
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
		List<Map<String,Object>> list =datatemporaryService.orderComboBox(inDto);
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

		List<Map<String,Object>> list=datatemporaryService.addOrder(inDto);
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
		datatemporaryService.saveRemember(inDto,userInfoVO);
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
		Map<String,Object> map=datatemporaryService.getRemember(inDto, userInfoVO);
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
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		String queryclass = datatemporaryService.isAll(qDto);
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
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
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(listtablename);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		String queryclass = datatemporaryService.isAll(qDto);
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
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
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(listtablename);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		String queryclass = datatemporaryService.isAll(qDto);
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
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
			String queryclass = datatemporaryService.isAll(inDto);
			inDto.put("queryclass", queryclass);
			request.getSession().setAttribute("querySession",
					datatemporaryService.queryConditions(inDto));
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_data(inDto,
					userInfoVO.getAccount_());
			int pCount = datatemporaryService.getPageTotal_backup(inDto);
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
			String queryclass = datatemporaryService.isAll(inDto);
			inDto.put("queryclass", queryclass);
			request.getSession().setAttribute("querySession",
					datatemporaryService.queryConditions(inDto));
			UserInfoVO userInfoVO = (UserInfoVO) session
					.getAttribute("_userInfoVO");
			List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_backup(inDto,
					userInfoVO.getAccount_());
			int pCount = datatemporaryService.getPageTotal_backup(inDto);
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
		Dto outDto = datatemporaryService.return_data(dto);

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
		List<Archive_tablenamePO> list = datatemporaryService.findZLKTablename();
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
		List<Map<String,Object>> list=datatemporaryService.fillReport(out);
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
			out=datatemporaryService.getSensitive(dtos);
		}catch (Exception e){
			e.printStackTrace();
			out.setAppCode(1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("savereceive")
	public void savereceive(HttpServletRequest request, HttpServletResponse response){
		Dto out = Dtos.newDto();
		Dto dtos=Dtos.newDto(request);
		boolean b=datatemporaryService.savereceive(dtos);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("listreceive")
	public void listreceive(HttpServletRequest request, HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		List<Map<String, Object>> fieldDtos=datatemporaryService.listreceive(inDto);
		String outString = AOSJson.toGridJson(fieldDtos);
		// 将分类坐标存放到session中
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping("listzlzj")
	public void listzlzj(HttpServletRequest request, HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		List<Map<String, Object>> fieldDtos=datatemporaryService.listzlzj(inDto);
		String outString = AOSJson.toGridJson(fieldDtos);
		// 将分类坐标存放到session中
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping("listziyuan")
	public void listziyuan(HttpServletRequest request, HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		List<Map<String, Object>> fieldDtos=datatemporaryService.listziyuan(inDto);
		String outString = AOSJson.toGridJson(fieldDtos);
		// 将分类坐标存放到session中
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping("initreceiveexamine")
	public String initreceiveexamine(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		//return "aos/archive/tree.jsp";
		return "aos/receive/receive_examine.jsp";
	}
	/**
	 * 档案详情列表
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("archive_details")
	public String archive_details(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String pch=qDto.getString("pch");
		//根据id_
		//String tablename=compilationService.findTablename(id_);
		String tablename=qDto.getString("tablename");
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(tablename);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		request.setAttribute("tablename", tablename);
		session.setAttribute("tablename", tablename);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		request.setAttribute("pch", pch);
		return "aos/receive/detailsMessage.jsp";
	}
	/**
	 *
	 * 查询数据信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listAccounts2")
	public void listAccounts2(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		//String queryclass = datatemporaryService.isAll(inDto);
		//inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySession",
				datatemporaryService.queryConditions(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_temporary(inDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal(inDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 将分类坐标存放到session中
		//session.setAttribute("queryclass", queryclass);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	//临时库到接收库
	@RequestMapping(value = "temporaryToreceiveData")
	public void temporaryToreceiveData(HttpServletRequest request,
									   HttpServletResponse response, HttpSession session)throws Exception{
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out=datatemporaryService.temporaryToreceiveData(inDto,userInfoVO);
		String outString =AOSJson.toJson(out);
		WebCxt.write(response, outString);
	}
	//临时库到管理库
	@RequestMapping(value = "temporaryTodataData")
	public void temporaryTodataData(HttpServletRequest request,
									   HttpServletResponse response, HttpSession session)throws Exception{
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out=datatemporaryService.temporaryTodataData(inDto,userInfoVO);
		String outString =AOSJson.toJson(out);
		WebCxt.write(response, outString);
	}
	//接收库到管理库
	@RequestMapping(value = "receiveTodata")
	public void receiveTodata(HttpServletRequest request,
									HttpServletResponse response, HttpSession session)throws Exception{
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out=datatemporaryService.receiveTodata(inDto,userInfoVO);
		String outString =AOSJson.toJson(out);
		WebCxt.write(response, outString);
	}
	@RequestMapping(value = "resourceToreceiveData")
	public void resourceToreceiveData(HttpServletRequest request,
									   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.resourceToreceiveData(inDto,userInfoVO);

		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "entityToreceiveData")
	public void entityToreceiveData(HttpServletRequest request,
									   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.entityToreceiveData(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "collectToreceiveData")
	public void collectToreceiveData(HttpServletRequest request,
									HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.collectToreceiveData(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceive")
	public void updatereceive(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.updatereceive(inDto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	@RequestMapping(value = "updateTemporary")
	public void updateTemporary(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.updateTemporary(inDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "update_ziliao")
	public void update_ziliao(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.update_ziliao(inDto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "update_ziyuan")
	public void update_ziyuan(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.update_ziyuan(inDto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceive_ziliao")
	public void updatereceive_ziliao(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.updatereceive_ziliao(inDto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceive_ziyuan")
	public void updatereceive_ziyuan(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.updatereceive_ziyuan(inDto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceive_zhengji")
	public void updatereceive_zhengji(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.updatereceive_zhengji(inDto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceive_shiti")
	public void updatereceive_shiti(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.updatereceive_shiti(inDto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatecollect")
	public void updatecollect(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.updatecollect(inDto);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updateresource")
	public void updateresource(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.updateresource(inDto);
		if(b){
			out.setAppMsg("提交完成!");
		}else{
			out.setAppMsg("提交失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceiveyes")
	public void updatereceiveyes(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatereceiveyes(inDto,userInfoVO);

		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatezhengjiyes")
	public void updatezhengjiyes(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatezhengjiyes(inDto,userInfoVO);

		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updateziyuanyes")
	public void updateziyuanyes(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updateziyuanyes(inDto,userInfoVO);

		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceiveyes_ziliao")
	public void updatereceiveyes_ziliao(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatereceiveyes_ziliao(inDto,userInfoVO);

		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceiveyes_ziyuan")
	public void updatereceiveyes_ziyuan(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatereceiveyes_ziyuan(inDto,userInfoVO);

		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceiveyes_zhengji")
	public void updatereceiveyes_zhengji(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatereceiveyes_zhengji(inDto,userInfoVO);

		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceiveyes_shiti")
	public void updatereceiveyes_shiti(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatereceiveyes_shiti(inDto,userInfoVO);

		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updateresourceyes")
	public void updateresourceyes(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updateresourceyes(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatecollectno")
	public void updatecollectno(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatecollectno(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updateresourceno")
	public void updateresourceno(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updateresourceno(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatecollectyes")
	public void updatecollectyes(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatecollectyes(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceiveno")
	public void updatereceiveno(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatereceiveno(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatezhengjino")
	public void updatezhengjino(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatezhengjino(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updateziyuanno")
	public void updateziyuanno(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updateziyuanno(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatereceiveno_shiti")
	public void updatereceiveno_shiti(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updatereceiveno_shiti(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "calcId")
	public void calcId(HttpServletRequest request,
					   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		String dictionary=datatemporaryService.getDictionary(inDto,"department");
		String departmentindex=datatemporaryService.getDeprtmentindex(inDto);
		Dto out =Dtos.newDto();
		out.put("dictionary",dictionary);
		out.put("index",departmentindex);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("addpici")
	public void addpici(HttpServletRequest request,
						HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.addpici(inDto);
		if(b){
			out.setAppMsg("操作完成!");
			out.setAppCode(1);
		}else{
			out.setAppMsg("操作失败!");
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("entityinit")
	public String entityinit(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		return "aos/receive/dataentity.jsp";
	}
	@RequestMapping("collectinit")
	public String collectinit(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		return "aos/receive/dataarchivecollect.jsp";
	}
	@RequestMapping("resourceinit")
	public String resourceinit(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		return "aos/receive/dataresource.jsp";
	}
	@RequestMapping("listentity")
	public void listentity(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Map<String,Object>> fieldDtos=datatemporaryService.getDataListEntiry(inDto,userInfoVO.getAccount_());
		//List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll(inDto,userInfoVO.getAccount_());
		String outString = AOSJson.toGridJson(fieldDtos);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping("listresource")
	public void listresource(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Map<String,Object>> fieldDtos=datatemporaryService.getDataListResource(inDto,userInfoVO.getAccount_());
		//List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll(inDto,userInfoVO.getAccount_());
		String outString = AOSJson.toGridJson(fieldDtos);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping("listcollect")
	public void listcollect(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Map<String,Object>> fieldDtos=datatemporaryService.getDataListCollect(inDto,userInfoVO.getAccount_());
		//List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll(inDto,userInfoVO.getAccount_());
		String outString = AOSJson.toGridJson(fieldDtos);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping("addentity")
	public void addentity(HttpServletRequest request,
						  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.addentity(inDto);
		if(b){
			out.setAppMsg("操作完成!");
			out.setAppCode(1);
		}else{
			out.setAppMsg("操作失败!");
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("addcollect")
	public void addcollect(HttpServletRequest request,
						  HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.addcollect(inDto);
		if(b){
			out.setAppMsg("操作完成!");
			out.setAppCode(1);
		}else{
			out.setAppMsg("操作失败!");
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("addresource")
	public void addreource(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		boolean b=datatemporaryService.addresource(inDto);
		if(b){
			out.setAppMsg("操作完成!");
			out.setAppCode(1);
		}else{
			out.setAppMsg("操作失败!");
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("updateentity")
	public void updateentity(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updateentity(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updateentityyes")
	public void updateentityyes(HttpServletRequest request,
								 HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updateentityyes(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updateentityno")
	public void updateentityno(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out =Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=datatemporaryService.updateentityno(inDto,userInfoVO);
		if(b){
			out.setAppMsg("操作完成!");
		}else{
			out.setAppMsg("操作失败!");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "querybmmcs")
	public void querybmmcs(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> bmmcDtos = datatemporaryService
				.getDatabmmcsList();
		WebCxt.write(response, AOSJson.toGridJson(bmmcDtos));
	}
	@RequestMapping(value = "listOrderInfos")
	public void listOrderInfos(HttpServletRequest request,
							   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list=datatemporaryService.listOrderInfos(qDto);
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
		boolean b = datatemporaryService.updateField_index(outDto);
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
	 *
	 * 四性检测
	 *temporaryToreceiveData
	 * @param request
	 * @param response
*/
	@RequestMapping(value="safeTest")
	public void safeTest(HttpServletRequest request,HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		String tablename=qDto.getString("tablename");
		if("jnws_temporary"==tablename){
            int row=datatemporaryService.testEffectiveTemp(qDto);
            String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
            WebCxt.write(response,outString);
        }else if("gdws_temporary"==tablename){
            int row=datatemporaryService.testEffectiveTemp_gdws(qDto);
            String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
            WebCxt.write(response,outString);
        }else if("xgdws_temporary"==tablename){
			int row=datatemporaryService.testEffectiveTemp_xgdws(qDto);
			String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
			WebCxt.write(response,outString);
        }else{
            int row=datatemporaryService.testEffectiveTemp(qDto);
            String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
            WebCxt.write(response,outString);
        }

	}

/**
 *
 * 四性检测配置
 *
 * @param request
 * @param response
 */
	@RequestMapping(value="getsxjc_properties")
	public void getsxjc_properties(HttpServletRequest request,HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Archive_sxjcPO archive_sxjcPO=datatemporaryService.getsxjc_properties(qDto);
		WebCxt.write(response, AOSJson.toJson(archive_sxjcPO));
	}
	@RequestMapping(value = "saveEntity")
	public void saveEntity(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto =datatemporaryService.saveEntity(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "updateEntity")
	public void updateEntity(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = datatemporaryService.updateEntity(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value="deleteEntity")
	public void deleteEnvironment(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		int row = datatemporaryService.deleteEntity(inDto);
		String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
		WebCxt.write(response,outString);
	}
	@RequestMapping(value = "saveZiliao")
	public void saveZiliao(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto =datatemporaryService.saveZiliao(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "updateZiliao")
	public void updateZiliao(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = datatemporaryService.updateZiliao(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value="deleteZiliao")
	public void deleteZiliao(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		int row = datatemporaryService.deleteZiliao(inDto);
		String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
		WebCxt.write(response,outString);
	}
	@RequestMapping(value = "saveArchivezhengji")
	public void saveArchivezhengji(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto =datatemporaryService.saveArchivezhengji(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "updateArchivezhengji")
	public void updateArchivezhengji(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = datatemporaryService.updateArchivezhengji(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value="deleteArchivezhengji")
	public void deleteArchivezhengji(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		int row = datatemporaryService.deleteArchivezhengji(inDto);
		String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
		WebCxt.write(response,outString);
	}
	@RequestMapping(value = "saveZiyuan")
	public void saveZiyuan(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto =datatemporaryService.saveZiyuan(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "saveData_ziyuan")
	public void saveData_ziyuan(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto =datatemporaryService.saveData_ziyuan(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "updateZiyuan")
	public void updateZiyuan(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = datatemporaryService.updateZiyuan(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "updateData_ziyuan")
	public void updateData_ziyuan(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = datatemporaryService.updateData_ziyuan(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value="deleteZiyuan")
	public void deleteZiyuan(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		int row = datatemporaryService.deleteZiyuan(inDto);
		String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
		WebCxt.write(response,outString);
	}
	//deleteAllData_temporary
	@RequestMapping(value="deleteAllData_temporary")
	public void deleteAllData_temporary(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		Integer row = datatemporaryService.deleteAllData_temporary(inDto);
		String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
		WebCxt.write(response,outString);
	}
	/**
	 * 保存四性检测配置
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="savesxjc_properties")
	public void savesxjc_properties(HttpServletRequest request,HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto outDto=Dtos.newDto();
		boolean b=datatemporaryService.savesxjc_properties(qDto);
		if (!b) {
			outDto.setAppCode(-1);
			outDto.setAppMsg("保存失败!");
		} else {
			outDto.setAppCode(1);
			outDto.setAppMsg("保存成功");
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 四性检测配置获取
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getSxjc")
	public void getSxjc(HttpServletRequest request,HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto outDto=Dtos.newDto();
		List<Map<String,Object>> list=datatemporaryService.getSxjc(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
    /**
     * 四性检测操作
     * @param request
     * @param response
     */
    @RequestMapping(value="operator_sxjc")
	public void operator_sxjc(HttpServletRequest request,HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
		String tablename=qDto.getString("tablename");
		if("jnws_temporary".equals(tablename)){
			int row=datatemporaryService.testEffectiveTemp(qDto);
			String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
			WebCxt.write(response,outString);
		}else if("gdws_temporary".equals(tablename)){
			int row=datatemporaryService.testEffectiveTemp_gdws(qDto);
			String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
			WebCxt.write(response,outString);
		}else if("xgdws_temporary".equals(tablename)){
			int row=datatemporaryService.testEffectiveTemp_xgdws(qDto);
			String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
			WebCxt.write(response,outString);
		}
		/*else if("zpda_temporary".equals(tablename)){
			int row=datatemporaryService.testEffectiveTemp_zpda(qDto);
			String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
			WebCxt.write(response,outString);
		}*/
		/*else if("spda_temporary".equals(tablename)){
			int row=datatemporaryService.testEffectiveTemp_spda(qDto);
			String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
			WebCxt.write(response,outString);
		}*/
		else{
			int row=datatemporaryService.testEffectiveTemp(qDto);
			String outString= AOSUtils.merge("操作完成，成功检测[{0}]个数据",row);
			WebCxt.write(response,outString);
		}
    }
	/**
	 * 四性检测报告
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="sxjc_report")
    public String sxjc_report(HttpServletRequest request,HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		String tablename=qDto.getString("tablename");
		Map<String,Object> map=datatemporaryService.sxjc_report(qDto);
		if(map!=null&&map.size()>=1){
			Object dh_equalssum=map.get("dh_equalssum");
			Object file_booleansum=map.get("file_booleansum");
			Object qzh_sum=map.get("qzh_sum");
			Object qzh_null=map.get("qzh_null");
			Object mlh_sum=map.get("mlh_sum");
			Object mlh_null=map.get("mlh_null");
			Object nd_sum=map.get("nd_sum");
			Object nd_null=map.get("nd_null");
			Object jh_sum=map.get("jh_sum");
			Object jh_null=map.get("jh_null");
			Object pch=map.get("pch");
			request.setAttribute("dh_equalssum",dh_equalssum);
			request.setAttribute("file_booleansum",file_booleansum);
			request.setAttribute("pch",pch);
			request.setAttribute("qzh_sum",qzh_sum);
			request.setAttribute("qzh_null",qzh_null);
			request.setAttribute("mlh_sum",mlh_sum);
			request.setAttribute("mlh_null",mlh_null);
			request.setAttribute("nd_sum",nd_sum);
			request.setAttribute("nd_null",nd_null);
			request.setAttribute("jh_sum",jh_sum);
			request.setAttribute("jh_null",jh_null);
			request.setAttribute("tablename",tablename);

		}
		String tabledesc=datatemporaryService.getTabledesc(tablename);
		request.setAttribute("tabledesc",tabledesc);
		return "aos/archive/count/math.jsp";
	}
	/**
	 * 接收报告
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="receive_report")
	public String receive_report(HttpServletRequest request,HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		String tablename=qDto.getString("tablename");
		Map<String,Object> map=datatemporaryService.receive_report(qDto);
		if(map!=null&&map.size()>=1){
			Object pch=map.get("pch");
			Object qzh_number=map.get("qzh_number");
			Object qzh_name=map.get("qzh_name");
			Object nd=map.get("nd");
			Object total_number=map.get("total_number");
			Object file_number=map.get("file_number");
			Object jsrq=map.get("jsrq");
			request.setAttribute("pch",pch);
			request.setAttribute("qzh_number",qzh_number);
			request.setAttribute("qzh_name",qzh_name);
			request.setAttribute("nd",nd);
			request.setAttribute("total_number",total_number);
			request.setAttribute("file_number",file_number);
			request.setAttribute("jsrq",jsrq);
		}
		String tabledesc=datatemporaryService.getTabledesc(tablename);
		request.setAttribute("tabledesc",tabledesc);
		request.setAttribute("table",tabledesc.split("_")[0]);
		return "aos/receive/count/receive_math.jsp";
	}
	/**
	 * 接收报告
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="sxjc_error")
	public String sxjc_error(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Object pagesize = userInfoVO.getPagesize();
		if(pagesize == null||pagesize==""){
			pagesize = "20";
		}
		String treename=datatemporaryService.findTree(qDto);
		String nd=datatemporaryService.findNd(qDto);
		String queryclass = datatemporaryService.isAll(qDto);
		request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("treename",treename);
		request.setAttribute("sxjczt",qDto.getString("sxjczt"));
		request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
		request.setAttribute("nd",nd);
		session.setAttribute("queryclass", queryclass);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		//return "aos/archive/tree.jsp";
		return "aos/receive/dataerror.jsp";
		/*List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_error(qDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal(qDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 将分类坐标存放到session中
		session.setAttribute("queryclass", queryclass);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);*/
	}
	/**
	 * 错误数据2
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="sxjc_error2")
	public String sxjc_error2(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = datatemporaryService
				.getDataFieldListTitle(qDto.getString("tablename"));
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Object pagesize = userInfoVO.getPagesize();
		if(pagesize == null||pagesize==""){
			pagesize = "20";
		}
		String treename=datatemporaryService.findTree(qDto);
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		request.setAttribute("treename",treename);
		request.setAttribute("pch",qDto.getString("pch"));
		request.setAttribute("sxjczt",qDto.getString("text"));
		request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		//return "aos/archive/tree.jsp";
		return "aos/receive/dataerror2.jsp";
		/*List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_error(qDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal(qDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 将分类坐标存放到session中
		session.setAttribute("queryclass", queryclass);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);*/
	}
	/**
	 *
	 * 查询数据信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listAccounts_error")
	public void listAccounts_error(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		String queryclass = datatemporaryService.isAll(inDto);
		inDto.put("queryclass", queryclass);
		request.getSession().setAttribute("querySession",
				datatemporaryService.queryConditions(inDto));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_error(inDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal(inDto);
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
	@RequestMapping(value = "listAccounts_error2")
	public void listAccounts_error2(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = datatemporaryService.getDataFieldListDisplayAll_error2(inDto,
				userInfoVO.getAccount_());
		int pCount = datatemporaryService.getPageTotal2(inDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 将分类坐标存放到session中
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 判断是否存在任务附件
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="booleanfile")
	public void booleanfile(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=datatemporaryService.booleanfile(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="booleanfile_ziliao")
	public void booleanfile_ziliao(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=datatemporaryService.booleanfile_ziliao(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="booleanfile_ziyuan")
	public void booleanfile_ziyuan(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=datatemporaryService.booleanfile_ziyuan(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="booleanfile_temporary")
	public void booleanfile_temporary(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=datatemporaryService.booleanfile_temporary(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="booleanfile_entity")
	public void booleanfile_entity(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=datatemporaryService.booleanfile_entity(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	@RequestMapping(value = "initImport_temporary")
	public String initImport_temporary(HttpServletRequest request,
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
        request.setAttribute("_classtree", qDto.getString("_classtree"));
        request.setAttribute("pch", qDto.getString("pch"));
        session.setAttribute(AOSCons.USERINFOKEY,qDto.getUserInfo());
		return "aos/receive/importTemporary.jsp";
	}
	@RequestMapping(value = "initImport_depot")
	public String initImport_depot(HttpServletRequest request,
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
		request.setAttribute("_classtree", qDto.getString("_classtree"));
		request.setAttribute("pch", qDto.getString("pch"));
		session.setAttribute(AOSCons.USERINFOKEY,qDto.getUserInfo());
		return "aos/depot/importdepot.jsp";
	}
	@RequestMapping(value = "datatemporaryDjIndex")
	public void datatemporaryDjIndex(HttpServletRequest request,
									 HttpServletResponse response,HttpSession session){
		Dto out=Dtos.newDto(request);

		String index=datatemporaryService.datatemporaryDjIndex(out);
		Dto outDto =Dtos.newDto();
		outDto.put("index",index);
		WebCxt.write(response, AOSJson.toJson(outDto));

	}
}