package cn.osworks.aos.system.modules.controller.dbtable;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
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
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.dbtable.MaintainService;

@Controller
@RequestMapping(value="system/maintain")
public class MaintainController {
	
	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private SqlDao sysDao;
	
	@Autowired
	private MaintainService maintainService;
	
	@RequestMapping(value="initMaintain")
	public String initMaintain(){
		
		return "aos/dbtable/maintain.jsp"; 
	}
	
	/**
	 * 
	 * 查询表信息
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2019-5-3
	 */
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablenamePO> list = archive_tablenameMapper.listPage(qDto);
		String outString = AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 * 
	 * 
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2019-5-3
	 */
	@RequestMapping(value = "listData")
	public void listData(HttpServletRequest request,HttpServletResponse response,HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		qDto.put("queryclass", "1=1");
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll(qDto,userInfoVO.getAccount_());
		int pCount = dataService.getPageTotal(qDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		WebCxt.write(response, outString);
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2019-5-3
	 */
	@RequestMapping(value="getTableTitle")
	public void getTableTitle(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = dataService.getDataFieldListTitle(qDto.getString("tablename"));
		request.setAttribute("fieldDtos2",titleDtos);
		String outString = AOSJson.toJson(titleDtos);
		WebCxt.write(response, outString);
		
	}
	
	
	/**
	 * 
	 * 源字段和目标字段查询
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2019-5-4
	 */
	@RequestMapping(value="getSourceField")
	public void getSourceField(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Dto> list = maintainService.getTargetField(qDto);
		String outString = AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
		
	}
	
	
	/**
	 * 
	 * 下拉列表主键
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
	 * 下拉列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listComboBox")
	public void listComboBox(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper.getTableFields(qDto.getString("targetTablename"));
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	
	/**
	 * 
	 * 导入
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2019-5-4
	 */
	@RequestMapping(value="insertData")
	public void insertData(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = maintainService.InsertDb(inDto);
		String outString = AOSJson.toJson(outDto);
		WebCxt.write(response, outString);
	}
	
	/**
	 * 
	 * 
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2019-5-5
	 */
	@RequestMapping(value="deleteData")
	public void deleteData(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = maintainService.clearTable(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
		
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
		List<Archive_tablefieldlistPO> titleDtos = archive_tablefieldlistMapper
				.getDataFieldDisplayAll(qDto.getString("tablename"));
		String outString = AOSJson.toGridJson(titleDtos);
		WebCxt.write(response, outString);
	}
}
