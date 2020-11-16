package cn.osworks.aos.system.modules.controller.qzj;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.Aos_sys_moduleMapper;
import cn.osworks.aos.system.dao.mapper.Aos_sys_qzdwMapper;
import cn.osworks.aos.system.dao.mapper.Aos_sys_qzjMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tableinputMapper;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.PageVO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.introduce.ImportTemporaryService;
import cn.osworks.aos.system.modules.service.qzj.QzjService;
import cn.osworks.aos.system.modules.service.resource.ModuleService;
import cn.osworks.aos.system.modules.service.qzj.QzjService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 全宗卷管理
 *
 * @author OSWorks-XC
 * @date 2014-07-09
 */
@Controller
@RequestMapping(value = "archive/qzj")
public class QzjController extends JdbcDaoSupport {

	@Autowired
	private Aos_sys_moduleMapper aos_sys_moduleMapper;
	@Autowired
	private QzjService qzjService;
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private ImportTemporaryService importService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private Aos_sys_qzjMapper aos_sys_qzjMapper;
	@Autowired
	private Archive_tableinputMapper archive_tableinputMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}

	@Autowired
	private Aos_sys_qzdwMapper aos_sys_qzdwMapper;
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
	 * 页面初始化
	 *
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "init")
	public String init(HttpSession session, HttpServletRequest request) {
		Dto qDto=Dtos.newDto(request);
		Aos_sys_qzjPO aos_sys_qzjPO = aos_sys_qzjMapper.selectByKey(AOSCons.MODULE_ROOT_ID);
		request.setAttribute("_root", aos_sys_qzjPO);
		List<Archive_tablefieldlistPO> list=qzjService.getDataFieldDisplayAll("Aos_sys_qzj_catalog");
		request.setAttribute("fieldDtos", list);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		//查询当前用户的每页显示数
		Object pagesize =qzjService.getPageSizeFromUser(userInfoVO.getAccount_());
		if (pagesize == null) {
			pagesize = "20";
		}
		request.setAttribute("user",userInfoVO.getName_());
		request.setAttribute("pagesize", pagesize);
		request.setAttribute("listtablename", "Aos_sys_qzj_catalog");
		return "aos/qzj/qzj.jsp";
	}
	@RequestMapping(value="listMlh")
	public void listMlh(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		String tabledesc=inDto.getString("tabledesc");
		String qzh=inDto.getString("qzh");
		String tablename="";
		String sql45="select * from archive_tablename where tabledesc='"+tabledesc+"'";
		List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql45);
		if(maps!=null&&maps.size()>0){
			tablename=(String)maps.get(0).get("tablename");
		}
		if(tablename!=null&&tablename!=""){
			List<Map<String,Object>> listAll =new ArrayList<>();
			try {
				String sql2= "select distinct(mlh) as mlhenname from "+tablename+" where qzh='"+qzh+"'";
				listAll = jdbcTemplate.queryForList(sql2);
				String outString = AOSJson.toGridJson(listAll);
				// 就这样返回转换为Json后返回界面就可以了。
				WebCxt.write(response, outString);
			} catch (DataAccessException e) {
				e.printStackTrace();
				WebCxt.write(response, "");
			}

		}

	}
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		String outString=qzjService.listAccounts(inDto,request);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping(value="getTablename")
	public void getTablename(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		String tablename=qzjService.getTablename(inDto,request);
		// 就这样返回转换为Json后返回界面就可以了。
		Dto out=Dtos.newDto();
		out.put("tablename",tablename);
		String outString = AOSJson.toJson(out);
		WebCxt.write(response, outString);
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
		List<Archive_tablefieldlistPO> titleDtos = qzjService
				.getDataFieldDisplayAll("aos_sys_qzj");
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}

	/**
	 * 查询功能模块列表
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listModules")
	public void listModules(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		qDto.setOrder("sort_no_");

		List<Aos_sys_qzjPO> aos_sys_qzjPOs = sysDao.list("Resource.listAos_sys_qzjPOs", qDto);

		String outString = AOSJson.toGridJson(aos_sys_qzjPOs, aos_sys_qzjPOs.size());
		WebCxt.write(response, outString);
	}

	/**
	 * 查询菜单树列表
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listModuleTree")
	public void listModuleTree(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		qDto.setOrder("sort_no_");
		List<Dto> list = qzjService.getModulesTree(qDto);
		String jsonString = AOSJson.toJson(list);
		WebCxt.write(response, jsonString);
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
		String query = qzjService.queryConditions(out);
		Dto outDto = Dtos.newDto();
		outDto.setAppMsg(query);
		WebCxt.write(response, AOSJson.toJson(outDto));

	}
	/**
	 * 保存功能模块信息
	 *
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "saveModule")
	public void saveModule(HttpServletRequest request, HttpServletResponse response) {
		Dto dto = Dtos.newDto(request);
		qzjService.saveModule(dto);
		WebCxt.write(response, "操作完成，功能模块菜单新增成功。");
	}

	/**
	 * 修改功能模块信息
	 *
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateModule")
	public void updateModule(HttpServletRequest request, HttpServletResponse response) {
		Dto dto = Dtos.newDto(request);
		qzjService.updateModule(dto);
		WebCxt.write(response, "操作完成，功能模块菜单修改成功。");
	}

	/**
	 * 删除功能模块信息
	 *
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "deleteModule")
	public void deleteModule(HttpServletRequest request, HttpServletResponse response) {
		Dto dto = Dtos.newDto(request);
		Dto outDto = qzjService.deleteModule(dto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 *
	 * 下拉列表数据
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listComboBoxData")
	public void listComboBoxData(HttpServletRequest request, HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		List<Dto> list = sysDao.list("Resource.listComboBoxData", dto);
		WebCxt.write(response, AOSJson.toGridJson(list));
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
		Dto outDto = qzjService.deletePath(dto);
		qzjService.refreshPath(dto);
		// 日志
		// 加入日志
		String tm = dto.getString("tm");
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
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
		Dto outDto = qzjService.deletePathAll(dto);
		// 加入日志
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		WebCxt.write(response, AOSJson.toJson(outDto));
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
		qzjService.download_electronic_file(filePath, dto, response, request);

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


		List<Dto> pathDtos = qzjService.getPath(inDto);

		// request.setAttribute("pathDtos", pathDtos);

		String outString = AOSJson.toGridJson(pathDtos);
		// Dto outDto = printService.getPath(inDto);
		WebCxt.write(response, outString);
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
		Map<String,Object> map=qzjService.getRemember(inDto, userInfoVO);
		String outString = AOSJson.toJson(map);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 得到全宗目录
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listCatalogs")
	public void listCatalogs(HttpServletRequest request, HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		String query=qzjService.queryConditions(dto);
		request.getSession().setAttribute("querySession",query);
		List<Dto> list =qzjService.listCatalogs(dto,query);
		int pCount = qzjService.getPageTotal(dto);
		String outString = AOSJson.toGridJson(list, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}

	/**
	 *
	 * 新增全宗目录
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="saveCatalog")
	public void saveCatalog(HttpServletRequest request, HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		Dto outdto = Dtos.newDto();
		boolean b=qzjService.saveCatalog(dto);
		if(b){
			outdto.setAppCode(1);
		}else{
			outdto.setAppCode(-1);
		}

		WebCxt.write(response, AOSJson.toJson(outdto));
	}
	/**
	 *
	 * 新增全宗目录
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="updateCatalog")
	public void updateCatalog(HttpServletRequest request, HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		Dto outdto = Dtos.newDto();
		boolean b=qzjService.updateCatalog(dto);
		if(b){
			outdto.setAppCode(1);
		}else{
			outdto.setAppCode(-1);
		}

		WebCxt.write(response, AOSJson.toJson(outdto));
	}
	/**
	 * 删除页面元素信息
	 *
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "deleteCatalog")
	public void deleteCatalog(HttpServletRequest request, HttpServletResponse response) {
		Dto dto = Dtos.newDto(request);
		Dto outDto = qzjService.deleteCatalog(dto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 信息导入
	 *
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "initImport")
	public String initImport(HttpServletRequest request, HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		request.setAttribute("tablename", qDto.get("tablename"));
		request.setAttribute("tid", qDto.get("tid"));
		return "aos/qzj/import.jsp";
	}
	/**
	 * 导出日志操作
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("fillReport")
	public void fillReport(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		// inDto包装了全部的请求参数哦
		Dto qDto = Dtos.newDto(request);
		//设置表头
		LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
		lhm.put("qzh", "全宗号");
		lhm.put("tm", "题名");
		lhm.put("nd", "年度");
		lhm.put("jh", "顺序号");
		lhm.put("zrz", "责任者");
		lhm.put("ys", "页数");
		lhm.put("lb", "类别");
		lhm.put("rq", "日期");
		lhm.put("bz", "备注");
		List<Map<String, Object>> titleDtos = qzjService
				.getDataFieldListTitle(qDto);
		// 组装报表数据模型
		AOSReportModel reportModel = new AOSReportModel();
		reportModel.setFileName("全宗目录");
		// 设置报表集合
		reportModel.setLogsList(titleDtos);
		reportModel.setLogHeader(lhm);
		// 填充报表
		// AOSPrint aosPrint = AOSReport.fillReport(reportModel);
		// aosPrint.setFileName("excel表");
		session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
		WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
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
		Object outDto = qzjService.getData(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
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
		Dto outDto = qzjService.setPagesize(qDto);
		// request.setAttribute("tablename", qDto.get("tablename"));
		WebCxt.write(response, AOSJson.toJson(outDto));

	}

	/**
	 * 全宗卷接收编号
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "qzjjsbh")
	public void qzjjsbh(HttpSession session, HttpServletRequest request,
						HttpServletResponse response){
		String index=qzjService.qzjjsbh();
		Dto out =Dtos.newDto();
		out.put("index",index);
		WebCxt.write(response, AOSJson.toJson(out));

	}
	/**
	 * 页面初始化
	 *
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "initQzdw")
	public String initQzdw(HttpSession session, HttpServletRequest request) {
		Dto dto = Dtos.newDto(request);
		request.setAttribute("user",dto.getUserInfo().getAccount_());
		return "aos/qzj/qzdw.jsp";
	}

	@RequestMapping(value = "listQzdw")
	public void listQzdw(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);

		/*List<Aos_sys_qzdwPO> list= aos_sys_qzdwMapper.likePage(qDto);
		String outString = AOSJson.toGridJson(list,qDto.getPageTotal());*/

		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit);
		String sql="select * from aos_sys_qzdw  where 1=1  order by qzxh offset "+pageStart+" rows fetch next "+limit+" rows only";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		String sql2="select * from aos_sys_qzdw  where 1=1";
		List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
		String outString = AOSJson.toGridJson(listDto,listAll.size());

		WebCxt.write(response,outString);
	}

	@RequestMapping(value = "saveQzdw")
	public void saveQzdw(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto =qzjService.saveQzdw(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}
	@RequestMapping(value = "updateQzdw")
	public void updateQzdw(HttpServletResponse response,HttpServletRequest request){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = qzjService.updateQzdw(inDto);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}

	@RequestMapping(value="deleteQzdw")
	public void deleteQzdw(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		int row = qzjService.deleteQzdw(inDto);
		String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
		WebCxt.write(response,outString);
	}

	@RequestMapping(value="updateFiled")
	public void updateFiled(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		int row = qzjService.updateFiled(inDto);
		String outString = AOSUtils.merge("操作完成，成功更新数据。",row);
		WebCxt.write(response,outString);
	}
	@RequestMapping(value = "initImport_qzdw")
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
		request.setAttribute("_classtree", qDto.getString("_classtree"));
		request.setAttribute("pch", qDto.getString("pch"));
		session.setAttribute(AOSCons.USERINFOKEY,qDto.getUserInfo());
		return "aos/qzj/import.jsp";
	}
	@RequestMapping(value="querytables")
	public void querytables(HttpServletRequest request,HttpServletResponse response){
		Dto out=Dtos.newDto(request);
		List<Map<String,Object>> list=qzjService.querytables(out);
		WebCxt.write(response, AOSJson.toGridJson(list));
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
}
