package cn.osworks.aos.system.modules.controller.dynamic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tablefieldlistMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.dynamic.DynamicService;

/**
 * 动态分析
 * 
 * @author PX
 *
 * 2018-8-20
 */
@Controller
@RequestMapping(value="dynamic")
public class DynamicController {
	@Autowired
	private DynamicService dynamicService;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	
	/**
	 * 
	 * 页面初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="dynamicindata")
	public String dynamicindata(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		return "aos/dynamic/dynamic.jsp";
	}
	/**
	 * 选择字段
	 * 
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-20
	 */
	@RequestMapping(value="listTableField")
	public void listTableField(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		String attribute = (String)session.getAttribute("tablename");
		List<Archive_tablefieldlistPO> list = dynamicService.findTableField(attribute);
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 * 选择框刷新进入
	 * 
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-20
	 */
	@RequestMapping(value="flushtablename")
	public String flushtablename(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		request.setAttribute("tablename", qDto.getString("tablename"));
		session.setAttribute("tablename", qDto.getString("tablename"));
		return "aos/dynamic/dynamic.jsp";
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
		List<Archive_tablenamePO> list = dynamicService.findTablename();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 * 折线统计图
	 * 
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-20
	 * @throws IOException 
	 */
	@RequestMapping(value="brokenpicture")
	public void brokenpicture(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Dto dto=Dtos.newDto(request);
		String gridJson = dynamicService.getBrokenpicture(dto);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.print(gridJson);

	}
}
