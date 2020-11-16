package cn.osworks.aos.system.modules.controller.checkup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.checkup.CheckupTopicService;
import cn.osworks.aos.system.modules.service.checkup.DetailsService;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.service.checkup.CheckupService;
import cn.osworks.aos.system.modules.service.archive.DataService;

/**
 * 档案鉴定
 * 
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="archive/checkup")
public class CheckupController {
	@Autowired
	private CheckupService checkupService;
	@Autowired
	private CheckupTopicService checkupTopicService;
	@Autowired
	private DataService dataService;
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private DetailsService detailsService;
	/**
	 * 页面初始化
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * 2018-8-23
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("initData")
	public String initInput(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto out=Dtos.newDto(request);
		String lx=out.getString("lx");
		if("kfjd".equals(lx)){
			lx="开放鉴定";
		}else if("mjjd".equals(lx)){
			lx="密级鉴定";
		}else if("xhjd".equals(lx)){
			lx="销毁鉴定";
		}else if("jzjd".equals(lx)){
			lx="价值鉴定";
		}
		request.setAttribute("topic",lx);
		String first=out.getString("first");
		String next=out.getString("next");
		String last=out.getString("last");
		String cascode_id_=	out.getString("cascode_id_");
		request.setAttribute("cascode_id_",cascode_id_);
		request.setAttribute("byrwbh",out.getString("byrwbh"));
		request.setAttribute("aos_module_id_",out.getString("aos_module_id_"));
		//查询用户当前鉴定状态
		request.setAttribute("checkup_type",lx);
		//得到当前用户的接收按钮权限
		String checkup_flag=checkupService.findButton_jianding(out.getUserInfo());
		//String  checkup_flag=checkupService.findCheckup_flag(out.getUserInfo());
		session.setAttribute("checkup_flag",checkup_flag);
		request.setAttribute("user_en",out.getUserInfo().getName_());
		//得到当前用户的是再审还是终审
		//根据当前用户名称得到按钮权限
		/*Dto outdto=checkupService.seminar_initData(out,session);
		if(outdto.getString("first").equals("1")){
			request.setAttribute("first","1");
		}
		if(outdto.getString("next").equals("1")){
			request.setAttribute("next","1");
		}
		if(outdto.getString("last").equals("1")){
			request.setAttribute("last","1");
		}
		if("1".equals(first)){
			request.setAttribute("first","1");
		}
		if("1".equals(next)){
			request.setAttribute("next","1");
		}
		if("1".equals(last)){
			request.setAttribute("last","1");
		}*/
		//上面的步骤暂时注释
		if(checkup_flag!=null&&checkup_flag.contains("初鉴")){
			request.setAttribute("first","1");
		}
		if(checkup_flag!=null&&checkup_flag.contains("复核")){
			request.setAttribute("next","1");
		}
		if(checkup_flag!=null&&checkup_flag.contains("终审")){
			request.setAttribute("last","1");
		}
		return "aos/details/topic.jsp?time="+new Date().getTime()+"&cascode_id_="+cascode_id_;
	}

	/**
	 * 查询专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listseminar")
	public void listseminar(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Dto> treeNodes = checkupService.listseminar(qDto);
		String outString = AOSJson.toJson(treeNodes);
		WebCxt.write(response, outString);
	}
	/**
	 * 得到当前条目的电子文件
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
	@RequestMapping("getpath")
	public void getpath(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//条目数据id值
		String id_=inDto.getString("id_");
		//当前compilation的id值
		String topic_id_=inDto.getString("topic_id_");
		String tablename=inDto.getString("tablename");
		Dto dto=checkupService.getpath(id_,topic_id_,tablename);
		//条目数据
		String outString = AOSJson.toJson(dto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 保存专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "saveseminar")
	public void saveseminar(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = checkupService.saveseminar(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 获取专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getSeminarById")
	public void getSeminarById(HttpServletRequest request, HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		String id=qDto.getString("id_");
		Archive_checkup_seminarPO archive_checkup_seminarPO=checkupService.getSeminarById(id);
		AOSUtils.copyProperties(archive_checkup_seminarPO,out);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 修改专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updateSeminar")
	public void updateSeminar(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = checkupService.updateSeminar(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 * 删除专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deleteSeminar")
	public void deleteSeminar(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = checkupService.deleteSeminar(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
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
	/*@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		String listtablename=inDto.getString("tablename");
		if(listtablename.equals("null")||listtablename.equals("")||listtablename==null){
			return;
		}
		inDto.put("tablename", inDto.getString("tablename"));
		List<Dto> fieldDtos = checkupService.getDataFieldListDisplayAll(inDto,session);
		int pCount = checkupService.getPageTotal(inDto);
		String query = checkupService.queryConditions2(inDto);
		request.setAttribute("_total", pCount);

		request.setAttribute("query",query);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}*/
	/**
	 * 设计条件
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="getQuery")
	public void getQuery(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		String query = checkupService.queryConditions2(inDto);
		outDto.put("query",query);
		String outString = AOSJson.toJson(outDto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 获得撰稿表单
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listwrite")
	public void listwrite(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list= checkupService.listwrite(qDto,session);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 获得撰稿表单
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listtowrite")
	public void listtowrite(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list= checkupService.listtowrite(qDto,session);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 获得撰稿表单
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listreadwrite")
	public void listreadwrite(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list= checkupService.listreadwrite(qDto,session);
		WebCxt.write(response, AOSJson.toGridJson(list));
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
		List<Archive_tablenamePO> list = checkupService.findTablename();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 * 传递用户
	 * 
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-20
	 * @throws ParseException 
	 */
	@RequestMapping(value="jymessage")
	public void jymessage(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws ParseException{
		Dto outdto = Dtos.newDto(request);
		Dto dto = checkupService.jymessage(outdto,session);
		WebCxt.write(response, AOSJson.toJson(dto));
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
	 * 得到待销毁列表数据
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2018-11-28
	 */
	@RequestMapping(value="listwaitdestroy")
	public void listwaitdestroy(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		List<Map<String,Object>> list = checkupService.listwaitdestroy(dto);
		
		
		String outString = AOSJson.toGridJson(list);
		
		WebCxt.write(response, outString);
	}
	/**
	 * 开始鉴定
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-11-27
	 */
	@RequestMapping(value="startcheckup")
	public void startcheckup(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		String query = (String)session.getAttribute("query");
		Dto dtoOut = checkupService.startcheckup(dto,query,request);
		if(dtoOut.getAppCode()==-1){
			dtoOut.setAppMsg("不存在成文时间字段，不能实现鉴定操作!");
			WebCxt.write(response, AOSJson.toJson(dtoOut));
		}else if(dtoOut.getAppCode()==-2){
			dtoOut.setAppMsg("不存在保管期限代码字段，不能实现鉴定操作!");
			WebCxt.write(response, AOSJson.toJson(dtoOut));
		}else if(dtoOut.getAppCode()>=0){
			String msg = AOSUtils.merge("操作完成，过期档案[{0}]条记录", dtoOut.getAppCode());
			dtoOut.setAppMsg(msg);
			WebCxt.write(response, AOSJson.toJson(dtoOut));
		}		
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
		List<Archive_tablefieldlistPO> titleDtos = checkupService.getDataFieldListTitle(qDto.getString("tablename"));
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}
	/**
	 *
	 * 查询用户名下拉框
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="queryUsers")
	public void queryUsers(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);

		//1.得到当前用户
		String user=(String)session.getAttribute("user");
		//根据当前用户查询到相关联的列表姓名
		//List<Map<String,Object>> userDtos = checkupService.getUserList(user);
		//直接他妈查所有用户
		List<Map<String,Object>> userDtos = checkupService.getUserAllList(user);
		WebCxt.write(response, AOSJson.toGridJson(userDtos));
	}
	/**
	 * 
	 * 销毁档案
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deleteData")
	public void deleteData(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto OutqDto = Dtos.newDto();
		boolean b = checkupService.deleteData(qDto);
		if(b){
			OutqDto.setAppCode(1);
		}else{
			OutqDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(OutqDto));
	}
	/**
	 * 
	 * 还原档案
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="returnData")
	public void returnData(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto OutqDto = Dtos.newDto();
		boolean b = checkupService.returnData(qDto);
		if(b){
			OutqDto.setAppCode(1);
		}else{
			OutqDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(OutqDto));
	}
	/**
	 * 
	 * 鉴定记录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listappraisedestroy")
	public void listappraisedestroy(HttpServletRequest request,HttpServletResponse response){
		List<Map<String, Object>> titleDtos = checkupService.listappraisedestroy();
		
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}
	public List<Map<String, Object>> getDataFieldListTitle(Dto qDto, HttpSession session) {
		// TODO Auto-generated method stub
		String sql="select * from "+qDto.getString("tablename");
		List<Map<String, Object>> listcount = checkupService.getlistString(sql);
		return listcount;
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
	 * 得到总数
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("getSumPage")
	public void getSumPage(HttpServletRequest request,
			HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		int pagecount = checkupService.getSumPage(qDto);
		Dto newDto=Dtos.newDto();
		newDto.setAppCode(pagecount);
		WebCxt.write(response, AOSJson.toJson(newDto));
	}

	/**
	 * 开放档案鉴定保存表单
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("addbloomForm")
	public void addbloomForm(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.addForm(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 保管期限档案鉴定保存表单
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("addbgqxForm")
	public void addbgqxForm(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.addForm(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 密级档案鉴定保存表单
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("addmjForm")
	public void addmjForm(HttpServletRequest request,
							HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.addForm(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 销毁档案鉴定保存表单
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("addxhForm")
	public void addxhForm(HttpServletRequest request,
						  HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.addForm(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 操作列表
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("listcheckup")
	public void listcheckup(HttpServletRequest request,
							HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Archive_checkup_rwPO> list =checkupService.listopertion(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 添加分配
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("adddetails")
	public void adddetails(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=checkupService.adddetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 * 删除方案
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("deleteexamine")
	public void deleteexamine(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.deleteexamine(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
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
		String id_=qDto.getString("id_");
		//根据id_
		List<Map<String,Object>> list=checkupService.findTablename(qDto);
		if(list==null||list.size()<=0){
			return "aos/details/detailsMessage.jsp";
		}
		//根据id_
		//String tablename=compilationService.findTablename(id_);
		String tablename=(String)list.get(0).get("tablename");

		List<Archive_tablefieldlistPO> titleDtos = dataService
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
		request.setAttribute("check_id_", id_);
		return "aos/details/detailsMessage.jsp";
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
		inDto.put("id_",inDto.getString("topic_id_"));
		//标题列表
		List<Map<String,Object>> fieldDtos =checkupTopicService.getData(inDto,session);
		int pCount = checkupTopicService.getPageTotal_Data(inDto);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 查询初鉴人对应的数据条目信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listfirsttrial")
	public void listfirsttrial(HttpServletRequest request,
							   HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Map<String,Object>> list =checkupService.listfirsttrial(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}


	/**
	 *
	 * 查询初鉴人对应的数据条目信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "first_archive_details")
	public String first_archive_details(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String id_=qDto.getString("id_");
		String zt_id=qDto.getString("zt_id");
		String tablename=checkupService.getTablename(zt_id);
		/*List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(tablename);*/
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		List<Archive_tablefieldlistPO> titleDtos = checkupService
				.getDataFieldListTitle(tablename);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		request.setAttribute("tablename", tablename);
		session.setAttribute("tablename", tablename);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		request.setAttribute("check_id_", id_);
		request.setAttribute("zt_id", zt_id);

		request.setAttribute("_classtree", qDto.getString("_classtree"));
		request.setAttribute("aos_module_id_", qDto.getString("aos_module_id_"));
		//判断是初鉴 再审  终审操作
		String type=qDto.getString("checkup_type");
		if(type!=null){
			if("开放鉴定".equals(type)){
				return "aos/details/first_kf_detailsMessage.jsp";
			}else if("价值鉴定".equals(type)){
				return "aos/details/first_bgqx_detailsMessage.jsp";
			}else if("密级鉴定".equals(type)){
				return "aos/details/first_mj_detailsMessage.jsp";
			}else if("销毁鉴定".equals(type)){
				return "aos/details/first_xh_detailsMessage.jsp";
			}
			return "aos/details/detailsMessage.jsp";
		}
		return "aos/details/detailsMessage.jsp";
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
		Object outDto = checkupService.getData(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}


	/**
	 *
	 * 查询初鉴人对应的数据条目信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "next_archive_details")
	public String next_archive_details(HttpServletRequest request,
									   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String id_=qDto.getString("id_");
		String zt_id=qDto.getString("zt_id");
		//根据id_
		String tablename=checkupService.getTablename(zt_id);
		List<Archive_tablefieldlistPO> titleDtos = checkupService
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
		request.setAttribute("check_id_", id_);
		request.setAttribute("zt_id", zt_id);
		request.setAttribute("_classtree", qDto.getString("_classtree"));
		request.setAttribute("aos_module_id_", qDto.getString("aos_module_id_"));
		//根据当前id判断是初鉴 再审  终审操作
		Archive_checkup_rwPO archive_checkup_rwPO=checkupService.findCheck(id_);
		//判断是初鉴 再审  终审操作
		String type=qDto.getString("checkup_type");
		if(type!=null){
			if("开放鉴定".equals(type)){
				return "aos/details/next_kf_detailsMessage.jsp";
			}else if("价值鉴定".equals(type)){
				return "aos/details/next_bgqx_detailsMessage.jsp";
			}else if("密级鉴定".equals(type)){
				return "aos/details/next_mj_detailsMessage.jsp";
			}else if("销毁鉴定".equals(type)){
				return "aos/details/next_xh_detailsMessage.jsp";
			}
			return "aos/details/detailsMessage.jsp";
		}
		return "aos/details/detailsMessage.jsp";
	}
	/**
	 *
	 * 查询初鉴人对应的数据条目信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "last_archive_details")
	public String last_archive_details(HttpServletRequest request,
									   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String id_=qDto.getString("id_");
		String zt_id=qDto.getString("zt_id");
		//根据id_
		String tablename=checkupService.getTablename(zt_id);
		List<Archive_tablefieldlistPO> titleDtos = checkupService.getDataFieldListTitle(tablename);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		String pagesize = userInfoVO.getPagesize() + "";
		if (pagesize == null) {
			pagesize = "20";
		}
		request.setAttribute("tablename", tablename);
		session.setAttribute("tablename", tablename);
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("pagesize", pagesize);
		request.setAttribute("check_id_", id_);
		request.setAttribute("zt_id", zt_id);
		request.setAttribute("_classtree", qDto.getString("_classtree"));
		request.setAttribute("aos_module_id_", qDto.getString("aos_module_id_"));
		//根据当前id判断是初鉴 再审  终审操作
		Archive_checkup_rwPO archive_checkup_rwPO=checkupService.findCheck(id_);
		//判断是初鉴 再审  终审操作
		String type=qDto.getString("checkup_type");
		if(type!=null){
			if("开放鉴定".equals(type)){
				return "aos/details/last_kf_detailsMessage.jsp";
			}else if("价值鉴定".equals(type)){
				return "aos/details/last_bgqx_detailsMessage.jsp";
			}else if("密级鉴定".equals(type)){
				return "aos/details/last_mj_detailsMessage.jsp";
			}else if("销毁鉴定".equals(type)){
				return "aos/details/last_xh_detailsMessage.jsp";
			}
			return "aos/details/detailsMessage.jsp";
		}
		return "aos/details/detailsMessage.jsp";
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savefirstkfdetails")
	public void savefirstkfdetails(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savefirstkfdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savefirstmjdetails")
	public void savefirstmjdetails(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savefirstmjdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
/**
 *
 * 初鉴人操作保存
 *
 * @param request
 * @param response
 */
	@RequestMapping(value = "savefirstxhdetails")
	public void savefirstxhdetails(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfo=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savefirstxhdetails(qDto,userInfo);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savefirstbgqxdetails")
	public void savefirstbgqxdetails(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savefirstbgqxdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savenextbgqxdetails")
	public void savenextbgqxdetails(HttpServletRequest request,
									 HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savenextbgqxdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savelastbgqxdetails")
	public void savelastbgqxdetails(HttpServletRequest request,
									HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savelastbgqxdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
    /**
     *
     * 初鉴人操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "savenextkfdetails")
    public void savenextkfdetails(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
        boolean b=checkupService.savenextkfdetails(qDto,userInfoVO);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savelastkfdetails")
	public void savelastkfdetails(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savelastkfdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savenextxhdetails")
	public void savenextxhdetails(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savenextxhdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savelastxhdetails")
	public void savelastxhdetails(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savelastxhdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 获取电子文件列表
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("getFiles")
	public void getFiles(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		List<Map<String,Object>> list = checkupService.getFiles(qDto);
		WebCxt.write(response, AOSJson.toJson(list));
	}
    /**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savenextmjdetails")
	public void savenextmjdetails(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savenextmjdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savelastmjdetails")
	public void savelastmjdetails(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b=checkupService.savelastmjdetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人的反馈再审信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getNextTrial")
	public void getNextTrial(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		String operator_description=checkupService.getNextTrial(qDto);
		out.setAppMsg(operator_description);
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 * 操作列表
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("listlogin")
	public void listlogin(HttpServletRequest request,
							HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Map<String,Object>> list =checkupService.listlogin(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 *
	 *
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "seminar_initData")
	public String seminar_initData(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session){

		return "aos/details/examine.jsp?time="+new Date().getTime();
	}
	/**
	 * 获取任务
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listTask")
	public void listTask(HttpServletRequest request, HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Map<String,Object>> list= checkupService.listTask(qDto);
		int count=checkupService.getQueryCount(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list,count));
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
		inDto.put("id_",inDto.getString("zt_id"));
		//标题列表
		List<Map<String,Object>> fieldDtos =checkupTopicService.getData(inDto,session);
		int pCount = checkupTopicService.getPageTotal_Data(inDto);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 计算ID
	 *
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "calcId")
	public void calcId(HttpServletRequest request, HttpServletResponse response) {
		Dto dto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		String id = AOSId.id(dto.getString("name_"));
		out.setAppMsg(id);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 保存任务
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savetask")
	public void savetask(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = checkupService.savetask(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 撤销再审
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "return_first")
	public void return_first(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out= checkupService.return_first(qDto);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 撤销终审
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "return_next")
	public void return_next(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out= checkupService.return_next(qDto);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 修改任务分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updatetask")
	public void updatetask(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = checkupService.updatetask(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 修改任务分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deleteTask")
	public void deleteTask(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out= checkupService.deleteTask(qDto);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 修改任务分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "checkup_initData")
	public String checkup_initData(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		Dto dto=Dtos.newDto(request);
		String nd=dto.getString("nd");
		String tablename=dto.getString("tablename");
		//listtablename="wsda";
		if(tablename==null||tablename.equals("")||tablename.equals("null")){
			return "aos/details/data.jsp?time="+new Date().getTime();
		}
		List<Archive_tablefieldlistPO> title = checkupService.getDataFieldListTitle(tablename);
		request.setAttribute("tablename", tablename);
		request.setAttribute("fieldDtos", title);
		UserInfoVO userInfoVO=(UserInfoVO)session.getAttribute("_userInfoVO");
		request.setAttribute("user",userInfoVO.getAccount_());
		request.setAttribute("aos_module_id_",Dtos.newDto(request).getString("aos_module_id_"));
		if(nd==null||nd.equals("")||nd.equals("null")){
			return "aos/details/data.jsp?time="+new Date().getTime();
		}
		session.setAttribute("nd",nd);
		return "aos/details/data.jsp?time="+new Date().getTime();
	}

	/**
	 * 在表中添加标记
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "addData")
	public void addData(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto dto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		//先删除  再添加
		boolean b=checkupService.addData(dto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**listoperationlogin
	 * 在表中添加标记
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "listoperationlogin")
	public void listoperationlogin(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Checkup_topic_logPO> list= checkupService.listoperationlogin(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	@RequestMapping(value = "examine_initData")
	public String examine_initData(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		//得到当前用户的是再审还是终审
		//根据当前用户名称得到按钮权限
		Dto out=checkupService.seminar_initData(qDto,session);
		request.setAttribute("next",out.getString("next"));
		request.setAttribute("last",out.getString("last"));
		if(out.getString("next").equals("1")){
			request.setAttribute("flag",1);
		}
		if(out.getString("last").equals("1")){
			request.setAttribute("flag",2);
		}
		return "aos/details/examine.jsp?time="+new Date().getTime();
	}
	/**
	 * 获得表单
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listtrial")
	public void listtrial(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		String flag=qDto.getString("flag");
		if("1".equals(flag)){
			 list= detailsService.listnexttrial(qDto,session);
			WebCxt.write(response, AOSJson.toGridJson(list));
		}else if("2".equals(flag)){
			list= detailsService.listlasttrial(qDto,session);
			WebCxt.write(response, AOSJson.toGridJson(list));
		}else{
			WebCxt.write(response, AOSJson.toGridJson(list));
		}
	}

	@RequestMapping(value = "next_details_all")
	public String next_details_all(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String id_=qDto.getString("id_");
		//查询tablename
		String tablename="";
		List<Map<String,Object>> list= detailsService.findTablename(id_);
		if(list==null||list.size()<=0){
			return null;
		}
		tablename=(String)list.get(0).get("tablename");
		//把当前界面选择的档案存放到数据库中
		//根据id_
		//String tablename=checkupService.findTablename(id_);
		List<Archive_tablefieldlistPO> titleDtos = dataService
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
		request.setAttribute("check_id_", id_);
		//判断是初鉴 再审  终审操作
		String type=qDto.getString("checkup_type");
		if(type!=null){
			if("开放鉴定".equals(type)){
				return "aos/details/next_kf_detailsMessage.jsp";
			}else if("价值鉴定".equals(type)){
				return "aos/details/next_bgqx_detailsMessage.jsp";
			}else if("密级鉴定".equals(type)){
				return "aos/details/next_mj_detailsMessage.jsp";
			}else if("销毁鉴定".equals(type)){
				return "aos/details/next_xh_detailsMessage.jsp";
			}
			return "aos/details/detailsMessage.jsp";
		}
		return "aos/details/detailsMessage.jsp";
	}
	@RequestMapping(value = "last_details_all")
	public String last_details_all(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String id_=qDto.getString("id_");
		//查询tablename
		String tablename="";
		List<Map<String,Object>> list= detailsService.findTablename(id_);
		if(list==null||list.size()<=0){
			return null;
		}
		tablename=(String)list.get(0).get("tablename");
		//把当前界面选择的档案存放到数据库中
		//根据id_
		//String tablename=checkupService.findTablename(id_);
		List<Archive_tablefieldlistPO> titleDtos = dataService
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
		request.setAttribute("check_id_", id_);
		//判断是初鉴 再审  终审操作
		String type=qDto.getString("checkup_type");
		if(type!=null){
			if("开放鉴定".equals(type)){
				return "aos/details/last_kf_detailsMessage.jsp";
			}else if("价值鉴定".equals(type)){
				return "aos/details/last_bgqx_detailsMessage.jsp";
			}else if("密级鉴定".equals(type)){
				return "aos/details/last_mj_detailsMessage.jsp";
			}else if("销毁鉴定".equals(type)){
				return "aos/details/last_xh_detailsMessage.jsp";
			}
			return "aos/details/detailsMessage.jsp";
		}
		return "aos/details/detailsMessage.jsp";
	}
	@RequestMapping("listlasttrial")
	public void listlasttrial(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		List<Map<String,Object>> list =detailsService.listlasttrial(qDto,session);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 *
	 * 初鉴人的反馈终审信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getLastTrial")
	public void getLastTrial(HttpServletRequest request,
							 HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		String operator_description=detailsService.getLastTrial(qDto);
		out.setAppMsg(operator_description);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="listzt_jd")
	public void listzt_by(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		List<Map<String,Object>> list = detailsService.findzt_jd();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	@RequestMapping(value="updatekaifang_lx")
	public void updatekaifang_lx(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.updatekaifang_lx(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="booleanupdateData_kaifang")
	public void booleanupdateData_kaifang(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.booleanupdateData_kaifang(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="updatekaifang")
	public void updatekaifang(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.updatekaifang(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="updateAllkaifang")
	public void updateAllkaifang(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.updateAllkaifang(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("getQueryIds")
	public void getQueryIds(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表;
		//先得到query条件
		String query = checkupService.queryConditions2(inDto);
		inDto.put("query",query);
		List<Map<String,Object>> list= checkupService.getQueryIds(inDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
    @RequestMapping(value="updatemj_lx")
    public void updatemj_lx(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=checkupService.updatemj_lx(qDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }

	@RequestMapping(value="updatejiazhi_lx")
	public void updatejiazhi_lx(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.updatejiazhi_lx(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="updatexh_lx")
	public void updatexh_lx(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.updatexh_lx(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="updatekaifang_lx_all")
	public void updatekaifang_lx_all(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.updatekaifang_lx_all(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
    @RequestMapping(value="updatejiazhi_lx_all")
    public void updatejiazhi_lx_all(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=checkupService.updatejiazhi_lx_all(qDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
	@RequestMapping(value="updatemj_lx_all")
	public void updatemj_lx_all(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.updatemj_lx_all(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="updatexh_lx_all")
	public void updatexh_lx_all(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=checkupService.updatexh_lx_all(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 鉴定任务 单号 index
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getJDIndex")
	public void getJDIndex(HttpServletRequest request, HttpServletResponse response) {
		Dto outDto = Dtos.newDto(request);
		String lx = outDto.getString("lx");
		String index = checkupService.getJDIndex(lx);
		outDto.put("index",index);
		WebCxt.write(response,AOSJson.toJson(outDto));
	}

	@RequestMapping("getComboboxList")
	public void getComboboxList(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> list =checkupService.getComboboxList(inDto);
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
}
