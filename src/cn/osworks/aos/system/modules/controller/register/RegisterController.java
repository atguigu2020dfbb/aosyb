package cn.osworks.aos.system.modules.controller.register;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.SharpService;
import cn.osworks.aos.system.modules.service.archive.UploadService;
import cn.osworks.aos.system.modules.service.register.RegisterService;
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
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 保管状况登记
 * 
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="register/register")
public class RegisterController {
	@Autowired
	private SharpService sharpService;
	@Autowired
	private RegisterService registerService;
	@Autowired
	private DataService dataService;

	@Autowired
	private UploadService uploadService;
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
		String tid="";
		String registertype=dto.getString("registertype");
		request.setAttribute("lx",dto.getString("lx"));
		//listtablename="wsda";
		if(registertype==null||registertype.equals("")||registertype.equals("null")){
			return "aos/register/register.jsp?time="+new Date().getTime();
		}
		if("消毒登记".equals(registertype)){
			tid="11111111";
		}
		if("破损登记".equals(registertype)){
			tid="22222222";
		}
		if("修复登记".equals(registertype)){
			tid="33333333";
		}
		List<Archive_tablefieldlistPO> title = registerService.getDataFieldListTitle(tid);
		request.setAttribute("registertype", registertype);
		request.setAttribute("fieldDtos", title);
		return "aos/register/register.jsp?time="+new Date().getTime();
	}
	@RequestMapping("initLeader")
	public String initLeader(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto dto=Dtos.newDto(request);
		String tid="";
		String registertype=dto.getString("registertype");
		//listtablename="wsda";
		return "aos/register/leader.jsp?time="+new Date().getTime();
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
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Map<String,Object>> list= registerService.getDataFieldListDisplayAll(qDto);
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
		List<Archive_tablenamePO> list = sharpService.findTablename();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	/**
	 * 根据id得到借阅信息
	 * 
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-20
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@RequestMapping(value="jymessage")
	public void jymessage(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws ParseException, IOException{
		Dto outdto = Dtos.newDto(request);
		Map<String, Object> jymessage = sharpService.jymessage(outdto);
		WebCxt.write(response, AOSJson.toJson(jymessage));
	}
	/**
	 * 借阅保存
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-29
	 */
	@RequestMapping("savejy")
	public void savejy(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto outDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		try {
			if(outDto.getString("archivestate").equals("未借阅")){
				boolean b = sharpService.savejy(outDto);
				if(!b){
					out.setAppCode(-2);
				}else{
					out.setAppCode(1);
				}
			}else{
				sharpService.updatejy(outDto);
				out.setAppCode(-1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			out.setAppCode(1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 归还操作
	 * 
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-30
	 */
	@RequestMapping("returnjy")
	public void returnjy(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto outDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		int i = sharpService.returnjy(outDto,session);
		if(i==1){
			out.setAppCode(1);
		}else if(i==-1){
			out.setAppCode(-1);
		}else{
			out.setAppCode(-2);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 * 借阅里列表
	 * 
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-30
	 */
	@RequestMapping("listjy")
	public void listjy(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto dto=Dtos.newDto(request);
		List<Map<String,Object>> list = sharpService.listjy(dto);
		String outString="";
		if(list!=null&&list.size()>0){
			outString = AOSJson.toGridJson(list,list.size());
		}
		WebCxt.write(response, outString);
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
		lhm.put("id_", "id_");
		lhm.put("users", "用户");
		lhm.put("jytime", "借阅时间");
		lhm.put("archive_id", "档案号");
		lhm.put("tablename", "操作门类");
		lhm.put("jyday", "借阅天数");
		lhm.put("archivestate", "借阅状态");
		lhm.put("gh", "是否归还");
		List<Map<String, Object>> titleDtos = sharpService.listjy(qDto);
		// 组装报表数据模型
		AOSReportModel reportModel = new AOSReportModel();
		reportModel.setFileName("借阅信息表");
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
	 * 查询字段下拉框
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="queryFields")
	public void queryFields(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = sharpService.getDataFieldListTitle(qDto.getString("tablename"));
		String gridJson = AOSJson.toGridJson(titleDtos);
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}
	/**
	 * 
	 * 查询用户下拉框
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
		List<Map<String,Object>> userDtos = registerService.getUserList(user);
		WebCxt.write(response, AOSJson.toGridJson(userDtos));
	}
	/**
	 * 
	 * 提交申请
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="submit_application")
	public void submit_application(HttpServletRequest request,HttpServletResponse response){
		
		Dto out=Dtos.newDto();
		Dto qDto = Dtos.newDto(request);
		boolean b = sharpService.submit_application(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 
	 * 我的借阅
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="myborrow")
	public void myborrow(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		//得到当前用户
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		
		List<Map<String,Object>> list = sharpService.myborrow(qDto,userInfoVO.getAccount_());
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 得到用户待审核列表
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2018-12-4
	 */
	@RequestMapping("listExamine")
	public void listExamine(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		//得到当前用户
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		List<Map<String, Object>> list= sharpService.listExamine();
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 同意借阅
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2018-12-5
	 */
	@RequestMapping("againExamine")
	public void againExamine(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto=Dtos.newDto();
		//得到id修改这个为已审核
		boolean b = sharpService.againExamine(qDto);
		if(b){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 不同意借阅，并存入原因
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2018-12-5
	 */
	@RequestMapping("unagainExamine")
	public void unagainExamine(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto=Dtos.newDto();
		//得到id修改这个为不通过
		boolean b = sharpService.unagainExamine(qDto);
		if(b){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 打开没通过详情
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2018-12-5
	 */
	@RequestMapping("openmessage")
	public void openmessage(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		String description="";
		List<Map<String,Object>> list = sharpService.openmessage(qDto);
		if(list!=null&&list.size()>0){
			description=(String)list.get(0).get("description");
		}
		outDto.put("description", description);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	@RequestMapping("listuserExamine")
	public void listuserExamine(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		//得到当前用户
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		List<Map<String, Object>> list= sharpService.listuserExamine(userInfoVO.getAccount_());
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 用户看到的详情设为已读
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2018-12-5
	 */
	@RequestMapping("ReadborrowMessage")
	public void ReadborrowMessage(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		boolean b = sharpService.updateReadBorrow(userInfoVO,qDto);
		if(b){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 档案续借
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2018-12-6
	 */
	@RequestMapping("updateRenewalBorrow")
	public void updateRenewalBorrow(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		boolean b = sharpService.updateRenewalBorrow(qDto);
		if(b){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	/**
	 * 归还档案
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 *
	 * 2018-12-6
	 */
	@RequestMapping("returnRenewalBorrow")
	public void returnRenewalBorrow(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		boolean b = sharpService.returnRenewalBorrow(qDto);
		if(b){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
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
		List<Map<String,Object>> list = sharpService.getFiles(qDto);
		WebCxt.write(response, AOSJson.toJson(list));
	}
	/**
	 * 查看电子文件权限
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("booleanBorrowFile")
	public void booleanBorrowFile(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto=Dtos.newDto(request);
		Dto outDto=Dtos.newDto();
		String user=(String)session.getAttribute("user");
		/*boolean b = uploadService.booleanBorrowFile(dto,user);
		if(b){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}*/
		WebCxt.write(response, AOSJson.toJson(outDto));
	}



	/**
	 * 查询当前用户岗位
	 *
	 * @author PX
	 * @param response
	 * @param request
	 * @param session
	 *
	 * 2018-12-7
	 */
	@RequestMapping("getPostUser")
	public void getPostUser(HttpServletResponse response,
							HttpServletRequest request, HttpSession session){
		Dto outDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		String posts = sharpService.getPostUser(outDto);
		out.setAppMsg(posts);
		WebCxt.write(response, AOSJson.toJson(out));
	}
    /**
     * 查询当前用户岗位
     *
     * @author PX
     * @param response
     * @param request
     * @param session
     *
     * 2018-12-7
     */
    @RequestMapping("listxiaoduregister")
	public void listxiaoduregister(HttpServletResponse response,
                                   HttpServletRequest request, HttpSession session){
        Dto outDto = Dtos.newDto(request);
        List<Map<String,Object>> list = registerService.getListxiaoduregister(outDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    @RequestMapping("listposunregister")
    public void listposunregister(HttpServletResponse response,
                                   HttpServletRequest request, HttpSession session){
        Dto outDto = Dtos.newDto(request);
        List<Map<String,Object>> list = registerService.getListposunregister(outDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    @RequestMapping("listxiufuregister")
    public void listxiufuregister(HttpServletResponse response,
                                   HttpServletRequest request, HttpSession session){
        Dto outDto = Dtos.newDto(request);
        List<Map<String,Object>> list = registerService.getListxiufuregister(outDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
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
	@RequestMapping(value = "addxiaodu")
	public void addxiaodu(HttpServletResponse response,
						  HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addxiaodu(dto);
		if(b){
			out.setAppCode(1);
			UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
			//存入日志
			registerService.addLog(AOSId.uuid(),"添加操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"消毒的登记");
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "addposun")
	public void addposun(HttpServletResponse response,
						  HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addposun(dto);
		if(b){
			out.setAppCode(1);
			UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
			//存入日志
			registerService.addLog(AOSId.uuid(),"添加操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"破损的登记");
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "addxiufu")
	public void addxiufu(HttpServletResponse response,
						 HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addxiufu(dto);
		if(b){
			out.setAppCode(1);
			UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
			//存入日志
			registerService.addLog(AOSId.uuid(),"添加操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"修复的登记");
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatexiaodu")
	public void updatexiaodu(HttpServletResponse response,
						  HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.updatexiaodu(dto);
		if(b){
			out.setAppCode(1);
			UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
			//存入日志
			registerService.addLog(AOSId.uuid(),"修改操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"消毒的登记");
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updateposun")
	public void updateposun(HttpServletResponse response,
							 HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.updateposun(dto);
		if(b){
			out.setAppCode(1);
			UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
			//存入日志
			registerService.addLog(AOSId.uuid(),"修改操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"破损的登记");
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "updatexiufu")
	public void updatexiufu(HttpServletResponse response,
							HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.updatexiufu(dto);
		if(b){
			out.setAppCode(1);
			UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
			//存入日志
			registerService.addLog(AOSId.uuid(),"修改操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"修复的登记");
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "deleteXiaodu")
	public void deleteXiaodu(HttpServletResponse response,
							 HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=registerService.deleteXiaodu(dto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
		//存入日志
		registerService.addLog(AOSId.uuid(),"删除操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"消毒的登记");
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "deletePosun")
	public void deletePosun(HttpServletResponse response,
							 HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=registerService.deletePosun(dto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
		//存入日志
		registerService.addLog(AOSId.uuid(),"删除操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"破损的登记");
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "deleteXiufu")
	public void deleteXiufu(HttpServletResponse response,
							HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=registerService.deleteXiufu(dto);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute(AOSCons.USERINFOKEY);
		//存入日志
		registerService.addLog(AOSId.uuid(),"删除操作",dto.getString("task_number"),userInfoVO.getName_(),new Date(),"修复的登记");
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "listlogin")
	public void listlogin(HttpServletResponse response,
						  HttpServletRequest request, HttpSession session){
		Dto dto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.listlogin(dto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}



	@RequestMapping("addXdallocation")
	public void addXdallocation(HttpServletResponse response,
								HttpServletRequest request, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addXdallocation(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("addPsallocation")
	public void addPsallocation(HttpServletResponse response,
								HttpServletRequest request, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addPsallocation(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("addXfallocation")
	public void addXfallocation(HttpServletResponse response,
								HttpServletRequest request, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addXfallocation(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("addRW")
	public void addRW(HttpServletResponse response,
					  HttpServletRequest request, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addRW(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("updateXdallocation")
	public void updateXdallocation(HttpServletResponse response,
								   HttpServletRequest request, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.updateXdallocation(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value = "deleteXdallocation")
	public void deleteXdallocation(HttpServletRequest request,
								HttpServletResponse response, HttpSession session) {
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = registerService.deleteXdallocation(dto);
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
	@RequestMapping(value="listAccounts2")
	public void listAccounts(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> fieldDtos = registerService.listAccounts2(session);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);

	}
	@RequestMapping("getXiaoduMessage")
	public void getXiaoduMessage(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.getXiaoduMessasge(inDto);
		if(list!=null&&list.size()>0){
			Map<String,Object> map=list.get(0);
			String outString = AOSJson.toJson(map);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}else{
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, "");
		}
	}
	@RequestMapping("getPosunMessage")
	public void getPosunMessage(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.getPosunMessasge(inDto);
		if(list!=null&&list.size()>0){
			Map<String,Object> map=list.get(0);
			String outString = AOSJson.toJson(map);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}else{
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, "");
		}
	}
	@RequestMapping("getXiufuMessage")
	public void getXiufuMessage(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.getXiufuMessage(inDto);
		if(list!=null&&list.size()>0){
			Map<String,Object> map=list.get(0);
			String outString = AOSJson.toJson(map);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}else{
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, "");
		}
	}
	@RequestMapping("getXiaoduMessageRw")
	public void getXiaoduMessageRw(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.getXiaoduMessageRw(inDto);
		if(list!=null&&list.size()>0){
			Map<String,Object> map=list.get(0);
			String outString = AOSJson.toJson(map);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}else{
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, "");
		}
	}
	@RequestMapping("getPosunMessageRw")
	public void getPosunMessageRw(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.getPosunMessageRw(inDto);
		if(list!=null&&list.size()>0){
			Map<String,Object> map=list.get(0);
			String outString = AOSJson.toJson(map);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}else{
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, "");
		}
	}
	@RequestMapping("getXiufuMessageRw")
	public void getXiufuMessageRw(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.getXiufuMessageRw(inDto);
		if(list!=null&&list.size()>0){
			Map<String,Object> map=list.get(0);
			String outString = AOSJson.toJson(map);
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, outString);
		}else{
			// 就这样返回转换为Json后返回界面就可以了。
			WebCxt.write(response, "");
		}
	}
	@RequestMapping("addldMessage_ok")
	public void addldMessage_ok(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addldMessage_ok(inDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping("addldMessage_no")
	public void addldMessage_no(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=registerService.addldMessage_no(inDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
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
	@RequestMapping(value = "getRws")
	public void getRws(HttpServletRequest request,
					   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.getRws(inDto,session);
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping(value = "getRws_sh")
	public void getRws_sh(HttpServletRequest request,
					   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> list=registerService.getRws_sh(inDto,session);
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
}
