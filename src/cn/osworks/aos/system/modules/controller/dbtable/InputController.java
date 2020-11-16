package cn.osworks.aos.system.modules.controller.dbtable;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonArray;


import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tablefieldlistMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tableinputMapper;
import cn.osworks.aos.system.dao.po.Aos_sys_dicPO;
import cn.osworks.aos.system.dao.po.Aos_sys_dic_indexPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tableinputPO;
import cn.osworks.aos.system.modules.service.dbtable.InputService;


@Controller
@RequestMapping(value="dbtable/input")
public class InputController {
	@Autowired
	private InputService inputService;
	
	
	@Autowired
	private Archive_tableinputMapper archive_tableinputMapper;
	
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	
	
	@Autowired
	private SqlDao sysDao;
	/**
	 * 基本页面初始化
	 * 
	 * @return;
	 */
	@RequestMapping(value="initInput")
	public String initInput(HttpServletRequest request,HttpServletResponse response){
		String tablename = request.getParameter("tablename");
		request.setAttribute("tablename", tablename);
		return "aos/dbtable/input.jsp";
	}
	
	@RequestMapping(value="initInput2")
	public String initInput2(){
		
		return "aos/dbtable/input2.jsp";
	}
	@RequestMapping(value="initInput3")
	public String initInput3(){
		
		return "aos/dbtable/input3.jsp";
	}
	@RequestMapping(value="loadInput")
	public void loadInput(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
//		List<Dto> inputDto = inputService.getInput(inDto);
		List<Archive_tableinputPO> list = archive_tableinputMapper.list(inDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	
	/**
	 * 
	 * 加载下拉框
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="loadFieldsCombo")
	public void loadFieldsCombo(HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		List<Dto> list = inputService.getFieldsCombo(dto.getString("tablename"));
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	
	
	/**
	 * 
	 * 加载表内所有列信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listFieldInfos")
	public void listFieldInfos(HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		List<Dto> list = inputService.listFieldInfos(dto.getString("tablename"));
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	
	/**
	 * 
	 * 定位保存
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="saveInput")
	public void saveInput(HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		Dto outDto =inputService.saveInput(dto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	
	
	/**
	 * 
	 * 重置表单
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="resetInput")
	public void resetInput(HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		inputService.deleteTablefields(dto.getString("tablename"));
		
		List<Archive_tablefieldlistPO> list=archive_tablefieldlistMapper.getFieldsCombo(dto.getString("tablename"));
		WebCxt.write(response, AOSJson.toJson(list));
	}
	
	/**
	 * 
	 * 加载字典
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="loadFieldsDic")
	public void loadFieldsDic(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		qDto.setOrder("id_ DESC");
		qDto.put("catalog_cascade_id_","0.002.008");
		qDto.put("catalog_id_","520591d0ad114faf8a1a7e8191f35636");
		//qDto.set
		List<Aos_sys_dic_indexPO> list = sysDao.list("MasterData.listDicindexInfos", qDto);
		//String outString = AOSJson.toGridJson(list, list.size());
		
		System.out.print(AOSJson.toJson(list));
		WebCxt.write(response, AOSJson.toJson(list));
	}
	
	
	
}
