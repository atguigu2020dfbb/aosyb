package cn.osworks.aos.system.modules.controller.archive;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.modules.service.archive.BasicSearchService;
import cn.osworks.aos.system.modules.service.checkup.CheckupService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * 档案利用
 * 
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="archive/basicSearch")
public class BasicSearchController {
	@Autowired
	private CheckupService checkupService;
	@Autowired
	private BasicSearchService basicSearchService;
	@Autowired
	private DataService dataService;
	@Autowired
	private SqlDao sysDao;
	/**
	 * 页面初始化
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 *
	 * 2018-8-23
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("initInput")
	public String initInput(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto dto=Dtos.newDto(request);
		String id_=dto.getString("id_");
		String name_=dto.getString("name_");
		String listtablename=basicSearchService.findModule(id_);
		if(listtablename==null||listtablename.equals("")||listtablename.equals("null")){
			if(name_==null||name_=="null"||name_.equals("")){
				name_="请选择档案门类";
			}
			request.setAttribute("id_",id_);
			request.setAttribute("name_",name_);
			return "aos/archive/basicSearch.jsp?time="+new Date().getTime();
		}
		List<Archive_tablefieldlistPO> title = basicSearchService.getDataFieldListTitle(listtablename);
		request.setAttribute("listtablename", listtablename);
		request.setAttribute("fieldDtos", title);
		if(name_==null||name_=="null"||name_.equals("")){
			name_="请选择档案门类";
		}
		request.setAttribute("name_",name_);
		request.setAttribute("id_",id_);
		return "aos/archive/basicSearch.jsp?time="+new Date().getTime();
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
	@RequestMapping(value="listTablename")
	public void listTablename(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		List<Archive_tablenamePO> list = basicSearchService.findTablename();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}

	/**
	 * 
	 * 下拉列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listComboBoxid")
	public void listComboBoxid(HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		List<Dto> list = sysDao.list("Resource.listComboBoxid", dto);
		String outString = AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}

	/**
	 * 
	 * 查询字段下拉框
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="queryFields")
	public void queryFields(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		String id_=qDto.getString("id_");
		String listtablename=basicSearchService.findModule(id_);
		qDto.put("tablename", listtablename);
		List<Archive_tablefieldlistPO> titleDtos = basicSearchService.getDataFieldListTitle(qDto.getString("tablename"));
		String gridJson = AOSJson.toGridJson(titleDtos);
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}
	/**
	 * 显示数据
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		String id_=inDto.getString("id_");
		String listtablename=basicSearchService.findModule(id_);
		if(listtablename.equals("null")||listtablename.equals("")||listtablename==null){
			return;
		}
		inDto.put("tablename", listtablename);
		List<Dto> fieldDtos = basicSearchService.getDataFieldListDisplayAll(inDto,session);
		int pCount = basicSearchService.getPageTotal(inDto);
		String query = inDto.getString("query");
		request.setAttribute("_total", pCount);
		request.setAttribute("query",query);
		session.setAttribute("query",query);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
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
		String id_=qDto.getString("id_");
		qDto.put("query",session.getAttribute("query"));
		String listtablename=basicSearchService.findModule(id_);
		if(listtablename.equals("null")||listtablename.equals("")||listtablename==null){
			return;
		}
		qDto.put("tablename", listtablename);
		List<Dto> list = basicSearchService.exportData(qDto);
		List<Archive_tablefieldlistPO> titleDtos = basicSearchService
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


}
