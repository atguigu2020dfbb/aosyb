package cn.osworks.aos.system.modules.controller.recovery;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_recoveryMapper;
import cn.osworks.aos.system.dao.po.Archive_recoveryPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.recovery.RecoveryService;

/**
 * 
 * 回收站
 * 
 * @author shq
 *
 * @date 2016-9-14
 */
@Controller
@RequestMapping(value="recovery/recovery")
public class RecoveryController {
	
	@Autowired
	private Archive_recoveryMapper archive_recoveryMapper;
	@Autowired
	private RecoveryService recoveryService;
	@Autowired
	private DataService dataService;
	
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
		
		return "aos/recovery/recovery.jsp";
	}
	/**
	 * 查询回收站表，把信息展示出来
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_recoveryPO> list = archive_recoveryMapper.listPage(qDto);
		String outString = AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 * 查询列明
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getTableTitle")
	public void getTableTitle(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService.getDataFieldListTitle_recovery(qDto.getString("tablename"));
		String outString = AOSJson.toJson(titleDtos);
		WebCxt.write(response, outString);
	}
	/**
	 * 得到每一行数据信息
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listData")
	public void listData(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		qDto.put("tablename", qDto.getString("tablename"));
		qDto.put("queryclass", "1=1");
		String querySession = (String) request.getAttribute("querySession");
		request.getSession().setAttribute("querySession", dataService.queryConditions(qDto));
		List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll_Recovery(qDto);
		int pCount = dataService.getPageTotal(qDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		WebCxt.write(response, outString);
	}
	/**
	 * 
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getQueryTitle")
	public void getQueryTitle(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService.getDataFieldListTitle_recovery(qDto.getString("tablename"));
		String outString = AOSJson.toGridJson(titleDtos);
		WebCxt.write(response, outString);
	}
	/**
	 * 清除当前条件下的数据
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deleteData")
	public void deleteData(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		qDto.put("tablename", qDto.getString("tablename"));
		qDto.put("queryclass", "1=1");
		String querySession = request.getSession().getAttribute("querySession")+"";
		qDto.put("querySession", querySession);
		boolean b = recoveryService.deleteData(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 清除数据表中的数据
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deleteAllData")
	public void deleteAllData(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		qDto.put("tablename", qDto.getString("tablename"));
		qDto.put("queryclass", "1=1");
		boolean b = recoveryService.deleteAllData(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 还原部分数据
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="returnData")
	public void returnData(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		qDto.put("tablename", qDto.getString("tablename"));
		qDto.put("queryclass", "1=1");
		String querySession = request.getSession().getAttribute("querySession")+"";
		qDto.put("querySession", querySession);
		boolean b = recoveryService.returnData(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 还原数据表所有数据
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="returnAllData")
	public void returnAllData(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		qDto.put("tablename", qDto.getString("tablename"));
		boolean b = recoveryService.returnAllData(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
}