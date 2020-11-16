package cn.osworks.aos.system.modules.service.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_remote_detailMapper;
import cn.osworks.aos.system.dao.po.Archive_remote_detailPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.workflow.WorkflowService;

/***
 * 
 * 归档申请服务
 * 
 * @author Sun
 *
 * 2019-3-26
 */
@Service
public class ApplicationService {

	@Autowired
	private Archive_remote_detailMapper archive_remote_detailMapper;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private RuntimeService runtimeService;
	/**
	 * 
	 * 业务表重组
	 * 
	 * @author Sun
	 * @param inDto
	 * @return
	 *
	 * 2019-4-10
	 */
	public List<Dto> getFormlist(Dto inDto){
		UserInfoVO userInfoVO = inDto.getUserInfo();
		List<Task> list=workflowService.findMyGroupTask(userInfoVO.getName_());
		List<Dto> outList = new ArrayList<Dto>();
		for(Task task:list){
			String formid = workflowService.getTaskID2FormID(task.getId());
			//Archive_remotePO archive_remotePO = archive_remoteMapper.selectByKey(formid);
			Dto outDto = Dtos.newDto();
			outDto.put("taskid", task.getId());
			outDto.put("formid",formid);
			//outDto.put("sqr", StringUtils.isNotBlank(archive_remotePO.getXm())?archive_remotePO.getXm():"");
			outDto.put("name", task.getName());
			outDto.put("assignee", task.getAssignee());
			outList.add(outDto);
			}
		return outList;
	}
	//保存申请信息
	public Dto saveForm (Dto inDto){
		Dto outDto = Dtos.newDto();
		//Archive_remotePO archive_remotePO = new Archive_remotePO();
		//AOSUtils.copyProperties(inDto, archive_remotePO);
		String fieldtid = AOSId.uuid();
		//archive_remotePO.setId_(fieldtid);
		//archive_remoteMapper.insert(archive_remotePO);
		Archive_remote_detailPO archive_remote_detailPO = new Archive_remote_detailPO();
		String ids = inDto.getString("ids");
		String str[] = ids.split(",");
		for (int i = 0; i < str.length; i++) {
			String detailid = AOSId.uuid();
			archive_remote_detailPO.setId_(detailid);
			archive_remote_detailPO.setTid(str[i]);
			archive_remote_detailPO.setFormid(fieldtid);
			archive_remote_detailMapper.insert(archive_remote_detailPO);
		}
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，保存成功！！！");
		return outDto;
	}
	
	//指定的连线名称完成任务
	public Dto completeTask(Dto qDto){
		Dto outDto = Dtos.newDto();
		String outcome = qDto.getString("outcome");
		UserInfoVO userInfo = qDto.getUserInfo();
		workflowService.claim(qDto.getString("taskid"), userInfo.getName_());
		Authentication.setAuthenticatedUserId(userInfo.getName_());
		Task task = taskService.createTaskQuery().taskId(qDto.getString("taskid")).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		taskService.addComment(qDto.getString("taskid"), processInstanceId, qDto.getString("comment"));
		Map<String,Object> variables=new HashMap<String,Object>();
		if(outcome!=null&&!outcome.equals("默认提交")){
			variables.put("outcome", outcome);
		}
		taskService.complete(qDto.getString("taskid"),variables);
		
		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if(pi==null){
			//Archive_remotePO archive_remotePO = new Archive_remotePO();
			//AOSUtils.copyProperties(qDto, archive_remotePO);
			//archive_remotePO.setState(2);
			//archive_remoteMapper.updateByKey(archive_remotePO);
		}
		outDto.setAppMsg("操作完成！！！");
		return outDto;
		
	}
	
	//启动流程
	public Dto startProcess(Dto qDto){
		Dto outDto = Dtos.newDto();
		String key = qDto.getString("key");
		Map<String, Object> variables = new HashMap<String, Object>();
		UserInfoVO userInfoVO = qDto.getUserInfo();
		variables.put("manager", userInfoVO.getName_());
		String business_key_ = key + "." + qDto.getString("id_");
		String proc_ins_id= workflowService.startProcess(key, business_key_,variables);
		Task task = taskService.createTaskQuery().processInstanceId(proc_ins_id).singleResult();
		String taskId=task.getId();
		workflowService.completeTask(taskId);
		//Archive_remotePO archive_remotePO = new Archive_remotePO();
		//AOSUtils.copyProperties(qDto, archive_remotePO);
		//archive_remotePO.setProc_ins_id(proc_ins_id);
		//archive_remoteMapper.updateByKey(archive_remotePO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("提交成功!!!");
		return outDto;
	}
	
	public Dto delete(Dto inDto){
		Dto outDto = Dtos.newDto();
		//archive_remoteMapper.deleteByKey(inDto.getString("id_"));
		outDto.setAppMsg("删除成功！！！");
		return outDto;
	}
	
	public Dto callBack(Dto qDto){
		Dto outDto = Dtos.newDto();
		//Archive_remotePO archie_remotePO = new Archive_remotePO();
		//AOSUtils.copyProperties(qDto, archie_remotePO);
		//archive_remoteMapper.updateByKey(archie_remotePO);
		workflowService.deleteProcessInstance(qDto.getString("proc_ins_id"));
		outDto.setAppMsg("撤回成功!!!");
		return outDto;
	}
	
	public Dto selectBykey(Dto qDto){ 
		Dto outDto = Dtos.newDto();
		//Archive_remotePO archive_remotePO =archive_remoteMapper.selectByKey(qDto.getString("id_"));
		return outDto;
	}
	
	public Dto getRemote(Dto qDto){
		Dto outDto = Dtos.newDto();
		
		return outDto;
		
	}
}
