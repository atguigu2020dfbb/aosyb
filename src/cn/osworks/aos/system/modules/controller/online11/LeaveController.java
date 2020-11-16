package cn.osworks.aos.system.modules.controller.online11;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.LeaveMapper;
import cn.osworks.aos.system.dao.po.LeavePO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;

@Controller
@RequestMapping(value="online/leave")
public class LeaveController {

	@Autowired
	private LeaveMapper leaveMapper;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@RequestMapping(value="initLeave")
	public String initLeave(HttpServletRequest request,HttpServletResponse response){
		
		return "aos/online/leave.jsp";
	}
	
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto();
		List<LeavePO> list=leaveMapper.list(inDto);
		String outString = AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
		
	}
	@RequestMapping(value="startProcess")
	public void startProcess(HttpServletRequest request,HttpServletResponse respnse){
//			    ProcessEngineConfiguration
//			      .createProcessEngineConfigurationFromResourceDefault()
//			      .setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_CREATE)
//			      .buildProcessEngine();
		// ProcessEngine processEngine =ProcessEngines.getDefaultProcessEngine();
		Dto inDto = Dtos.newDto(request);
		inDto.put("state", "1");
		inDto.put("id_", "5");
		String aa ="444656";
		//String business_key_ = "business_key_" + AOSId.uuid(0);
		LeavePO leavePO1=new LeavePO();
		
		//String key =leavePO1.getClass().getSimpleName();
		
		
		AOSUtils.copyProperties(inDto, leavePO1);
		leaveMapper.updateByKey(leavePO1);
		String key =leavePO1.getClass().getSimpleName();
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("inputUser", "aa");
		
		String business_key_=key+"."+leavePO1.getId_();
		
		variables.put("objId", business_key_);
		runtimeService.startProcessInstanceByKey("process",business_key_,variables);
		
	}
	//启动流程实例
	@RequestMapping(value="startProcessInstance")
	public void startProcessInstance(){
		String key = "process";
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
		
		System.out.println("流程实例ID:"+processInstance.getId());
		System.out.println("流程定义ID:"+processInstance.getId());
		//ProcessInstance pi =processEngine
				//runtimeService.startProcessInstanceByKey("process");
		//System.out.printnln();
		
	}
	//查询个人任务
	@RequestMapping(value="findMyPersonTask")
	public void findMyPersonTask(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		
		UserInfoVO userInfoVO= WebCxt.getUserInfo(session);
		
		String assignee=userInfoVO.getName_();
		List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).list();
		
		List<Dto> outList = new ArrayList<Dto>();
		
		if(list!=null && list.size()>0){
			for(Task task:list){
				Dto outDto = Dtos.newDto();
				outDto.put("id_", task.getId());
				outDto.put("name", task.getName());
				outDto.put("createTime", task.getCreateTime());
				outDto.put("assignee", task.getAssignee());
				System.out.println("任务ID"+task.getId());
				System.out.println("任务名称"+task.getName());
				System.out.println("任务创建时间"+task.getCreateTime());
				System.out.println("任务办理人"+task.getAssignee());
				outList.add(outDto);
			}
		}
		String outString = AOSJson.toGridJson(outList);
		WebCxt.write(response, outString);
	}
	
	//请假列表
	@RequestMapping(value="initTask")
	public String initTask(HttpServletRequest request,HttpServletResponse response){
		
		return "aos/online/taskManeger.jsp";
	}
	//完成个人任务
	@RequestMapping(value="completeMyTask")
	public void completeMyTask(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		taskService.complete(inDto.getString("task_id"));
		
	}
	//保存请假单
	@RequestMapping(value="saveLeave")
	public void saveLeave(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		LeavePO leavePO=new LeavePO();
		AOSUtils.copyProperties(inDto,leavePO);
		leavePO.setId_(AOSId.uuid());
		leaveMapper.insert(leavePO);
		WebCxt.write(response, "添加成功");
	}
}
