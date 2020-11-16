package cn.osworks.aos.system.modules.controller.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_remote_detailMapper;
import cn.osworks.aos.system.dao.po.Archive_remote_detailPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_zdyjPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.online.ApplicationService;
import cn.osworks.aos.system.modules.service.workflow.WorkflowService;

@Controller
@RequestMapping(value = "preprocessing/application")
public class ApplicationController {

	@Autowired
	private DataService dataService;

	//@Autowired
	//private Archive_remoteMapper archive_remoteMapper;

	@Autowired
	private Archive_remote_detailMapper archive_remote_detailMapper;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@RequestMapping(value = "initApplication")
	public String initApplication(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		qDto.put("tablename", "wsda2");
		UserInfoVO userInfoVO= qDto.getUserInfo();
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("userEnName", userInfoVO.getAccount_());
		return "aos/preprocessing/application.jsp";
		// return "aos/preprocessing/formlist.jsp";
	}

	@RequestMapping(value = "init")
	public String init() {

		return "aos/preprocessing/formlist.jsp";
	}

	/**
	 * 
	 * 查询信息
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 * 
	 *            2019-2-13
	 */
	@RequestMapping(value = "listAccounts")
	public void listAccounts(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = qDto.getUserInfo();
		qDto.put("userenname", userInfoVO.getAccount_());
		//List<Archive_remotePO> list = archive_remoteMapper.list(qDto);
		//String outString = AOSJson.toGridJson(list,qDto.getPageTotal());
		//WebCxt.write(response, outString);
	}

	/**
	 * 
	 * 查询信息
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 * 
	 *            2019-2-13
	 */
	@RequestMapping(value = "listData")
	public void listData(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		qDto.put("tablename", "wsda2");
		qDto.put("queryclass", "1=1");
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll(qDto,userInfoVO.getAccount_());
		int pCount = dataService.getPageTotal(qDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// String outString
		// ="{'_rows':[{'aos_rn_':1,'id_':'3401a0e312ad4455907e559b17513972','dh':'123123213','qzh':'','damldm':'','nd':'','jgwt':'','jgwtdm':'','bgqx':'','bgqxdm':'','jh':'','hh':'','tm':'','wjbh':'','cwsj':'','szhsj':'','fjtm':'','rw':'','ztc':'','mj':'','bmqx':'','dzdam':'','fz':'','zrz':'','_path':1,'_lrr':'超级用户','_lrrq':'2018-10-11 13:56:41'},{'aos_rn_':2,'id_':'3401a0e312ad4455907e559b17513972','dh':'123123213','qzh':'','damldm':'','nd':'','jgwt':'','jgwtdm':'','bgqx':'','bgqxdm':'','jh':'','hh':'','tm':'','wjbh':'','cwsj':'','szhsj':'','fjtm':'','rw':'','ztc':'','mj':'','bmqx':'','dzdam':'','fz':'','zrz':'','_path':1,'_lrr':'超级用户','_lrrq':'2018-10-11 13:56:41'},{'aos_rn_':3,'id_':'26ee7856273047818758baeb689009ef','ys':'','dh':'1','qzh':'','damldm':'','nd':'','jgwt':'','jgwtdm':'','bgqx':'','bgqxdm':'','jh':'','hh':'','tm':'','wjbh':'','cwsj':'','szhsj':'','fjtm':'','rw':'','ztc':'','mj':'','bmqx':'','dzdam':'','fz':'','zrz':'','zzjjg':'','_jy':'','_path':0,'_lrr':'超级用户','_lrrq':'2019-02-12 14:00:42'},{'aos_rn_':4,'id_':'2861024a65e24cb9abf7b1e9c930b967','ys':'','dh':'2','qzh':'','damldm':'','nd':'','jgwt':'','jgwtdm':'','bgqx':'','bgqxdm':'','jh':'','hh':'','tm':'','wjbh':'','cwsj':'','szhsj':'','fjtm':'','rw':'','ztc':'','mj':'','bmqx':'','dzdam':'','fz':'','zrz':'','zzjjg':'','_jy':'','_path':0,'_lrr':'超级用户','_lrrq':'2019-02-12 14:00:44'},{'aos_rn_':5,'id_':'c4acded5d63e48f583b3649fa650a26b','ys':'','dh':'3','qzh':'','damldm':'','nd':'','jgwt':'','jgwtdm':'','bgqx':'','bgqxdm':'','jh':'','hh':'','tm':'','wjbh':'','cwsj':'','szhsj':'','fjtm':'','rw':'','ztc':'','mj':'','bmqx':'','dzdam':'','fz':'','zrz':'','zzjjg':'','_jy':'','_path':0,'_lrr':'超级用户','_lrrq':'2019-02-12 14:00:46'},{'aos_rn_':6,'id_':'889dca8a1e174431a6f8a016691f0a6f','ys':'','dh':'4','qzh':'','damldm':'','nd':'','jgwt':'','jgwtdm':'','bgqx':'','bgqxdm':'','jh':'','hh':'','tm':'','wjbh':'','cwsj':'','szhsj':'','fjtm':'','rw':'','ztc':'','mj':'','bmqx':'','dzdam':'','fz':'','zrz':'','zzjjg':'','_jy':'','_path':0,'_lrr':'超级用户','_lrrq':'2019-02-12 14:00:49'}],'_total':6}";
		WebCxt.write(response, outString);
	}

	/**
	 * 
	 * 
	 * @author Sun
	 * 
	 *         2019-2-18
	 */
	// 保存申请登记
	@RequestMapping(value = "formsave")
	public void formsave(HttpServletRequest request,
			HttpServletResponse response) {
		Dto inDto = Dtos.newDto(request);
		Dto outDto = applicationService.saveForm(inDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
		
	}

	// 提交表单审核 启动流程
	@RequestMapping(value = "startInstance")
	public void startInstance(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) {
		Dto qDto = Dtos.newDto(request);
		qDto.put("key", "register");
		Dto outDto = applicationService.startProcess(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}

	// 查询个人任务
	@RequestMapping(value = "listPersonTask")
	public void listPersonTask(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		List<Dto> outlist=applicationService.getFormlist(inDto);		
		String outString = AOSJson.toGridJson(outlist);
		WebCxt.write(response, outString);
	}

	@RequestMapping(value = "initPersonTaskList")
	public String initPersonTaskList() {

		return "aos/preprocessing/personTaskList.jsp";
	}

	// 办理任务

	@RequestMapping(value = "setTaskManager")
	public String setTaskManager(HttpServletRequest request,
			HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = qDto.getUserInfo();
		//workflowService.claim(qDto.getString("taskid"), userInfoVO.getName_());
		List<String> list = workflowService.findOutComeListByTaskId(qDto.getString("taskid"));
		qDto.put("tablename", "wsda2");
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("taskid", qDto.getString("taskid"));
		request.setAttribute("list", list);
		
		return "aos/preprocessing/taskManage.jsp";
	}
	
	
	@RequestMapping(value="getFormList")
	public void getFormList(HttpServletRequest request,
			HttpServletResponse response){
		Dto outDto = Dtos.newDto(request);
		String taskid = outDto.getString("taskid");
		String id = workflowService.getTaskID2FormID(taskid);
		//Archive_remotePO archive_remote=archive_remoteMapper.selectByKey(id);
		//WebCxt.write(response, AOSJson.toJson(archive_remote));
		
	}

	@RequestMapping(value = "aa")
	public void aa(HttpServletRequest request, HttpServletResponse response) {
		System.out.print("11111111");
	}
	@RequestMapping(value="listTaskAccounts")
	public void listTaskAccounts(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String businessID = workflowService.getTaskID2FormID(qDto.getString("taskid"));
		 //Archive_remotePO archive_remote =archive_remoteMapper.selectByKey(businessID);
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		//String formid = archive_remote.getId_();
		qDto.put("tablename", "wsda2");
		//qDto.put("queryclass", "id_ in (select tid from archive_remote_detail where formid='"+formid+"')");
		List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll(qDto,userInfoVO.getAccount_());
		int pCount = dataService.getPageTotal(qDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		WebCxt.write(response, outString);
	}
	/***
	 * 
	 * 指定的连线名称完成任务
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2019-3-7
	 */
	@RequestMapping(value="saveSubmitTask")
	public void saveSubmitTask(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto=applicationService.completeTask(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	
	//查询历史活动
	@RequestMapping(value ="getFlowHistory")
	public void getFlowHistory(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Dto> outList = workflowService.getHisTaskList(qDto.getString("proc_ins_id"));
		
		WebCxt.write(response, AOSJson.toGridJson(outList));
		
	}
	
	@RequestMapping( value="deleteForm")
	public void deleteForm(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		Dto outDto=applicationService.delete(inDto);
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
	
	@RequestMapping(value = "callBack")
	public void callBack(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = applicationService.callBack(qDto);
		WebCxt.write(response, AOSJson.toJson(outDto)); 
	}
	
	
	//查看业务数据
	@RequestMapping(value="applicationList")
	public String applicationList(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		String id = qDto.getString("id");
		qDto.put("tablename", "wsda2");
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("formid", id);
		return "aos/preprocessing/applicationList.jsp";
	}
	
	//编辑业务数据
	@RequestMapping(value="applicationEdit")
	public String applicationEdit(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		String id = qDto.getString("id");
		qDto.put("tablename", "wsda2");
		List<Archive_tablefieldlistPO> titleDtos = dataService
				.getDataFieldListTitle(qDto.getString("tablename"));
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("formid", id);
		return "aos/preprocessing/applicationEdit.jsp";
	}	

	
	//查看获取表单数据
	@RequestMapping(value="getRemote")
	public void getRemote(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		//Archive_remotePO archive_remotePO = archive_remoteMapper.selectByKey(qDto.getString("formid"));
		//WebCxt.write(response, AOSJson.toJson(archive_remotePO.toDto()));
	}
	//查看获取grid数据
	@RequestMapping(value="listGridAccounts")
	public void listGridAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String formid = qDto.getString("formid");
		qDto.put("tablename", "wsda2");
		qDto.put("queryclass", "id_ in (select tid from archive_remote_detail where formid='"+formid+"')");
		UserInfoVO userInfoVO = (UserInfoVO) session
				.getAttribute("_userInfoVO");
		List<Dto> fieldDtos = dataService.getDataFieldListDisplayAll(qDto,userInfoVO.getAccount_());
		int pCount = dataService.getPageTotal(qDto);
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		WebCxt.write(response, outString);
		
	}
	
	//查询指导意见
	@RequestMapping("getZDYJ")
	public void getZDYJ(HttpServletRequest request,
			HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Archive_zdyjPO> list = dataService.getZDYJ(inDto);
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	
	@RequestMapping("addZddescription")
	public void addZddescription(HttpServletRequest request,
			HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		boolean t = dataService.addZddescription(inDto,session);
		if(t){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(outDto));
	}
}
