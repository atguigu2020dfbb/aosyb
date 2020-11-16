package cn.osworks.aos.system.modules.controller.archive;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Aos_sys_userPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.AdvancedSearchService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.SharpService;
import cn.osworks.aos.system.modules.service.archive.UploadService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 档案利用
 * 
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="archive/advancedSearch")
public class AdvancedSearchController {
	@Autowired
	private AdvancedSearchService advancedSearchService;
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
		request.setAttribute("aos_module_id_",Dtos.newDto(request).getString("aos_module_id_"));
		String listtablename=dto.getString("listtablename");
		//listtablename="wsda";
		String tablenamedesc=dto.getString("tablenamedesc");
		tablenamedesc=URLDecoder.decode(tablenamedesc,"utf-8");
		if(listtablename==null||listtablename.equals("")||listtablename.equals("null")){
			return "aos/archive/AdvancedSearch.jsp?time="+new Date().getTime();
		}
		List<Archive_tablefieldlistPO> title = advancedSearchService.getDataFieldListTitle(listtablename);
		request.setAttribute("listtablename", listtablename);
		request.setAttribute("tablenamedesc", tablenamedesc);
		request.setAttribute("fieldDtos", title);
		return "aos/archive/AdvancedSearch.jsp?time="+new Date().getTime();
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
	public void listAccounts(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		String listtablename=inDto.getString("listtablename");
		if(listtablename.equals("null")||listtablename.equals("")||listtablename==null){
			return;
		}
		inDto.put("tablename", inDto.getString("listtablename"));
		List<Dto> fieldDtos = advancedSearchService.getDataFieldListDisplayAll(inDto);
		int pCount = dataService.getPageTotal(inDto);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
		
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
		List<Archive_tablenamePO> list = advancedSearchService.findTablename();
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
		List<Archive_tablefieldlistPO> titleDtos = advancedSearchService.getDataFieldListTitle(qDto.getString("tablename"));
		String gridJson = AOSJson.toGridJson(titleDtos);
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}
}
