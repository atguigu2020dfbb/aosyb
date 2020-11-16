package cn.osworks.aos.system.modules.controller.count;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.modules.service.count.Statistics_personjianciService;
import cn.osworks.aos.system.modules.service.count.Statistics_receiveService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 
 * 档案统计
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "count/statistics_personjianci")
public class Statistics_PersonJianciController {

	@Autowired
	private Statistics_personjianciService statisticsService;
	/**
	 * 
	 * 页面初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="initData")
	public String initData(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		request.setAttribute("type",qDto.getString("type"));
		request.setAttribute("aos_module_id_",Dtos.newDto(request).getString("aos_module_id_"));
		return "aos/count/statistics_personjian.jsp";
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
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablenamePO> list = statisticsService.findTablename2(qDto);
		String outString =AOSJson.toGridJson(list);
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
	@RequestMapping(value="initData2")
	public String initData2(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		request.setAttribute("type",qDto.getString("type"));
		request.setAttribute("aos_module_id_",Dtos.newDto(request).getString("aos_module_id_"));
		return "aos/archive/statistics2.jsp";
	}
	/**listTablename
	 *
	 * 页面初始化
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="initData3")
	public String initData3(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);

		request.setAttribute("aos_module_id_",Dtos.newDto(request).getString("aos_module_id_"));
		return "aos/archive/statistics3.jsp";
	}
	/**listTablename
	 *
	 * 页面初始化
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="initData4")
	public String initData4(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);

		request.setAttribute("aos_module_id_",Dtos.newDto(request).getString("aos_module_id_"));
		return "aos/archive/statistics4.jsp";
	}
	/**
	 * 统计档案数据
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="liststatistics")
	public void liststatistics(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		
		List<Dto> listcount = statisticsService.getCount(dto);
		session.setAttribute("listcount", listcount);
		int count=listcount.size();
		WebCxt.write(response, AOSJson.toGridJson(listcount,count));
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
		lhm.put("category", "档案门类");
		lhm.put("ajs", "案卷数");
		lhm.put("wjs", "文件数");
		lhm.put("ajys", "案卷页数");
		List<Dto> titleDtos = statisticsService.getDataFieldListTitle(qDto,session);
		//Dto转map、
		List<Map<String, Object>> list = DtotoMaplist(titleDtos);
		// 组装报表数据模型
		AOSReportModel reportModel = new AOSReportModel();
		reportModel.setFileName("档案统计表");
		
		
		// 设置报表集合
		reportModel.setLogsList(list);
		reportModel.setLogHeader(lhm);
		// 填充报表
		// AOSPrint aosPrint = AOSReport.fillReport(reportModel);
		// aosPrint.setFileName("excel表");
		session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
		WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
	}
	/**
	 * Dto转map集合
	 * 
	 * @author PX
	 * @param titleDtos
	 *
	 * 2018-9-19
	 * @return 
	 */
	private List<Map<String, Object>> DtotoMaplist(List<Dto> titleDtos) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		if(titleDtos!=null&&titleDtos.size()>0){
			for(int t=0;t<titleDtos.size();t++){
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("category", titleDtos.get(t).get("category"));
				map.put("ajs", titleDtos.get(t).get("ajs"));
				map.put("wjs", titleDtos.get(t).get("wjs"));
				map.put("ajys", titleDtos.get(t).get("ajys"));
				list.add(map);
			}
		}
		return list;
	}
	/**
	 * 统计查询操作
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("countMath")
	private void countMath(HttpServletRequest request,HttpServletResponse response, HttpSession session){
		Dto dto=Dtos.newDto(request);
		List<Map<String,Object>> list=statisticsService.countMath(dto);
		WebCxt.write(response, AOSJson.toGridJson(list));

	}
	/**
	 * 统计查询操作2
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("countMath2")
	private void countMath2(HttpServletRequest request,HttpServletResponse response, HttpSession session){
		Dto dto=Dtos.newDto(request);
		List<Map<String,Object>> list=statisticsService.countMath2(dto);
		WebCxt.write(response, AOSJson.toGridJson(list));

	}
	//第一个统计图
	@RequestMapping(value="broken1picture")
	public void brokenpicture(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws IOException {
		Dto dto=Dtos.newDto(request);
		String gridJson = statisticsService.getBroken1picture(dto);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.print(gridJson);
	}
	//第二个统计图
	@RequestMapping(value="broken2picture")
	public void broken2picture(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws IOException {
		Dto dto=Dtos.newDto(request);
		String gridJson = statisticsService.getBroken2picture(dto);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.print(gridJson);
	}

}
