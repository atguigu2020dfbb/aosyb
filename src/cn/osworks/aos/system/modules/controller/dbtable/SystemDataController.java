package cn.osworks.aos.system.modules.controller.dbtable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.Archive_tablefieldlistMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;
import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.dao.po.LogPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.dbtable.DbtableService;
/**
 *
 * 数据表配置
 *
 * @author shq
 *
 * @date 2016-9-15
 */
@Controller
@RequestMapping(value = "system/data")
public class SystemDataController {

	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	@Autowired
	private LogMapper logMapper;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private DbtableService dbtableService;
	@RequestMapping(value="initData")
	public String initPrint(){
		return "aos/dbtable/dbtable.jsp";
	}
	@RequestMapping(value="initData_archive")
	public String initData_archive(){
		return "aos/dbtable/dbtable_archive.jsp";
	}
	/**
	 *
	 * 查询表信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		List<Archive_tablenamePO> list = archive_tablenameMapper.listPage(inDto);
		String outString =AOSJson.toGridJson(list,inDto.getPageTotal());
		WebCxt.write(response, outString);
	}

	/**
	 *
	 * 查询表列信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listFieldInfos")
	public void listFieldInfos(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> list=sysDao.list("dbtable.listTablefield", qDto);
		String outString = AOSJson.toGridJson(list,list.size());
		WebCxt.write(response, outString);
	}

	/**
	 *
	 * 保存数据表信息
	 *
	 * @param request
	 */
	@RequestMapping(value="saveTable")
	public void saveTable(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto outDto =dbtableService.saveTable(dto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(), userInfoVO.getAccount_(), "添加"+dto.getString("tablename"), "", "添加数据表", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 修改数据表信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="updateTable")
	public void updateTable(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto =Dtos.newDto(request);
		Dto outDto = dbtableService.updateTable(dto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(), userInfoVO.getAccount_(), "修改"+dto.getString("tablename"), "", "修改数据表", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 保存数据表字段信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="saveField")
	public void saveField(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto outDto = dbtableService.saveField(dto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(), userInfoVO.getAccount_(), "保存"+dto.getString("fieldenname"), "", "保存字段", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	/**
	 *
	 * 修改数据表字段保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="updateField")
	public void updateField(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto =Dtos.newDto(request);
		Dto outDto = dbtableService.updateField(dto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),userInfoVO.getAccount_(), "修改"+dto.getString("fieldenname"), "", "修改字段", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 删除数据表信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deleteTable")
	public void deleteTable(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		int row = dbtableService.deleteTable(dto);
		String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。", row);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(), userInfoVO.getAccount_(), "删除数据表"+dto.getString("tablename"), "", "删除数据表", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, outString);
	}

	/**
	 *
	 * 删除数据表字段
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deleteField")
	public void deleteField(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		int row=dbtableService.deleteField(dto);
		String outString=AOSUtils.merge("操作完成，成功删除[{0}]条数据", row);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),userInfoVO.getAccount_(), "删除字段"+dto.getString("fieldenname"), "", "删除字段", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 重置（创建数据库表）
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="resetTable")
	public void resetTable(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = dbtableService.resetTable(inDto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),userInfoVO.getAccount_(), "重置数据表"+inDto.getString("tablename"), "", "重置数据表", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 *
	 * 还原数据表（创建数据库表）
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="returnTable")
	public void returnTable(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = dbtableService.returnTable(inDto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),userInfoVO.getAccount_(), "还原数据表"+inDto.getString("tablename"), "", "还原数据表", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logMapper.insert(logPO);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}


	/**
	 *
	 * 下拉列表
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listComboBoxData")
	public void listComboBoxData(HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		List<Dto> list = sysDao.list("Resource.listComboBoxData", dto);
		WebCxt.write(response, AOSJson.toGridJson(list));
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
	 * 查询设置顺序信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listOrderInfos")
	public void listOrderInfos(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> list=dbtableService.listOrderInfos(qDto);
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
		boolean b = dbtableService.updateField_index(outDto);
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
	 * 得到关联列表
	 *
	 * @author PX
	 *
	 * 2019-3-11
	 */
	@RequestMapping("listcorrelation")
	public void listcorrelation(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto outDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		List<Map<String,Object>> list = dbtableService.listcorrelation(outDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}

	/**
	 * 根据tablename的id值查询到字段集合
	 *
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-3-6
	 */
	@RequestMapping("gettablefieldlist")
	public void gettablefieldlist(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto outdto = Dtos.newDto();
		String stringname = dbtableService.gettablefieldlist(dto);
		outdto.put("stringname", stringname);
		//将list集合迭代返回大的string
		WebCxt.write(response, AOSJson.toJson(outdto));
	}
	/**
	 * 添加关联
	 *
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2019-3-11
	 */
	@RequestMapping("addrelative")
	public void addrelative(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto=Dtos.newDto(request);
		Dto outDto = dbtableService.addrelative(dto);
		int i = outDto.getAppCode();
		if(i==-1){
			WebCxt.write(response, AOSJson.toJson(outDto));
		}else{
			outDto.setAppMsg("添加成功");
			WebCxt.write(response, AOSJson.toJson(outDto));
		}
	}
	/**
	 * 删除关联
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("deletecorrelation")
	public void deletecorrelation(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto=Dtos.newDto(request);
		Dto outDto=dbtableService.deletecorrelation(dto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 查询字段
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("getFieldname")
	public void getFieldname(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto=Dtos.newDto(request);
		List<Map<String,Object>> list=dbtableService.getFieldname(dto);
		WebCxt.write(response, AOSJson.toJson(list));
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
		List<Archive_tablefieldlistPO> list = dbtableService.listComboBox(qDto.getString("tablename"));
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 添加索引
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="saveIndex")
	public void saveIndex(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		int  b=dbtableService.saveIndex(qDto);
		if(b==1){
			outDto.setAppMsg("添加成功!");
		}else if(b==-1){
			outDto.setAppMsg("添加失败,索引重复!");
		}else if(b==-2){
			outDto.setAppMsg("添加失败!");
		}
		String outString =AOSJson.toJson(outDto);
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 删除索引
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deleteIndex")
	public void deleteIndex(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		boolean b=dbtableService.deleteIndex(qDto);
		if(b){
			outDto.setAppMsg("删除成功!");
		}else{
			outDto.setAppMsg("删除失败!");
		}
		String outString =AOSJson.toJson(outDto);
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 获得索引
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getIndex")
	public void getIndex(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list=dbtableService.getIndex(qDto);
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 根据选择的表名的id值，得到字段下拉列表
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="sourcelistComboBox")
	public void sourcelistComboBox(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list=dbtableService.sourcelistComboBox(qDto);
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}

	/**
	 *
	 * 根据选择的表名的id值，得到字段下拉列表
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="objectivelistComboBox")
	public void objectivelistComboBox(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list=dbtableService.objectivelistComboBox(qDto);
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 查询字段
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getFieledlist")
	public void getFieledlist(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list=dbtableService.getFieledlist(qDto);
		String json=AOSJson.toJson(list);
		WebCxt.write(response, AOSJson.toJson(list));
	}
	/**
	 *
	 * 临时字段保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="saveTemporaryFieldName")
	public void saveTemporaryFieldName(HttpServletRequest request,HttpServletResponse response){

	}
}
