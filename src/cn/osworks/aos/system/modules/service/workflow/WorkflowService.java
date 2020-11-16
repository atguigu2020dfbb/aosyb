package cn.osworks.aos.system.modules.service.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Aos_sys_userMapper;
import cn.osworks.aos.system.dao.po.Aos_sys_userPO;

/**
 * 工作流服务
 * 
 * @author OSWorks-XC
 * @date 2014-12-16
 */
@Service
public class WorkflowService {

	@Autowired
	private SqlDao sysDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private Aos_sys_userMapper aos_sys_userMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 签收任务
	 * 
	 * @param task_id_
	 *            任务ID
	 * @param user_id_
	 *            用户ID
	 */
	public void claim(String task_id_, String user_id_) {
		taskService.claim(task_id_, user_id_);
	}

	/**
	 * 将个人回退到组任务，前提，之前一定是个组任务
	 * 
	 * @param id_
	 *            任务ID
	 * @return
	 */
	@Transactional
	public void unclaim(String taskid) {
		taskService.setAssignee(taskid, null);
		//sysDao.update("Workflow.update_claim_time_when_unclaim", id_);
	}

	public void deleteModel(String id) {

		repositoryService.deleteModel(id);

		// ActivitiPlugin.buildProcessEngine().getRepositoryService().deleteModel(id);
	}

	/***
	 * 
	 * 启动流程并返回实例ID
	 * 
	 * @author Sun
	 * @param key
	 * @param business_key_
	 * @param variables
	 * @return
	 * 
	 *         2019-3-25
	 */
	@Transactional
	public String startProcess(String key, String business_key_,
			Map<String, Object> variables) {

		ProcessInstance proIns = runtimeService.startProcessInstanceByKey(key,
				business_key_, variables);

		return proIns.getId();
	}

	
	
	public String startProcess(String key, String business_key_) {

		ProcessInstance proIns = runtimeService.startProcessInstanceByKey(key,
				business_key_);
		return proIns.getId();
	}
	/***
	 * 
	 * 完成任务
	 * 
	 * @author Sun
	 * @param taskId
	 * @param variables
	 * 
	 *            2019-3-26
	 */
	@Transactional
	public void completeTask(String taskId, Map<String, Object> variables) {

		taskService.complete(taskId, variables);

	}
	
	
	/***
	 * 
	 * 完成任务
	 * 
	 * @author Sun
	 * @param taskId
	 * @param variables
	 * 
	 *            2019-3-26
	 */
	@Transactional
	public void completeTask(String taskId) {

		taskService.complete(taskId);

	}
	/**
	 * 
	 * 获取流转历史
	 * 
	 * @author Sun
	 * @param procesInstanceId
	 * @return
	 *
	 * 2019-4-9
	 */
	
	public List<Dto> getHisTaskList(String procesInstanceId) {
		List<Dto> list = (List) jdbcTemplate
				.queryForList("select  t.NAME_ taskName,t.ASSIGNEE_ assignee,t.EXECUTION_ID_ exeId,t.ID_ taskId,t.END_TIME_ endTime,c.MESSAGE_ message from act_hi_taskinst t LEFT JOIN act_hi_comment c  ON c.TASK_ID_=t.ID_ where t.proc_inst_id_ = '"
						+ procesInstanceId + "' ORDER BY t.end_time_ DESC");
		for(Map<String,Object> dto : list){
			String assignee=String.valueOf(dto.get("assignee"));
			if(StringUtils.isBlank(assignee)){
				//Dto d = Db.findFirst("select * from act_hi_varinst v where v.EXECUTION_ID_='"+r.getStr("exeId")+"' and v.NAME_!='loopCounter'");
				//assignee = v.getStr("TEXT_");
			}
			Dto qDto = Dtos.newDto();
			qDto.put("name_", assignee);
			Aos_sys_userPO aos_sys_userPO = aos_sys_userMapper.selectOne(qDto);
			if(aos_sys_userPO!=null){
				dto.put("assignee", aos_sys_userPO.getName_());
				//dto.put(key, );
			}else{
				dto.put("assignee", "已无此人["+assignee+"]");
			}
			
			String endtime = String.valueOf(dto.get("endTime"));
			
			dto.put("endTime", isObjectNotEmpty(dto.get("endTime"))?endtime.substring(0,endtime.indexOf(".")):"");
		}
		return list;
	}
	/**
	     * 判断Object对象为空或空字符串
	     * @param obj
	     * @return
	     */
	public static Boolean isObjectNotEmpty(Object obj) {
		
		String str = ObjectUtils.toString(obj, "");
		return StringUtils.isNotBlank(str);
	}
	
	/**
	 * 
	 * 通过任务ID查询业务ID
	 * 
	 * @author Sun
	 * @param taskid
	 * @return
	 *
	 * 2019-4-9
	 */
	public String getTaskID2FormID(String taskid){
		Task task = taskService.createTaskQuery()
								.taskId(taskid)
								.singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
										.processInstanceId(processInstanceId)
										.singleResult();
		String business_key = pi.getBusinessKey();
		String id ="";
		if(StringUtils.isNotBlank(business_key)){
			
			id=business_key.split("\\.")[1];
		}
		return id;
	}
	
	public void deleteProcessInstance(String proceIntanceId){
		runtimeService.deleteProcessInstance(proceIntanceId,"删除流程实例");
		historyService.deleteHistoricProcessInstance(proceIntanceId);
	}
	
	/**
	 * 
	 * 已知任务ID，获取任务完成之后的连线名称，并放置到List<String>
	 * 
	 * @author Sun
	 * @param taskid
	 * @return
	 *
	 * 2019-3-5
	 */
	public List<String> findOutComeListByTaskId(String taskid){
		List<String> list = new ArrayList<String>();
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity=(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String activityId = pi.getActivityId();
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		List<PvmTransition> pvmList=activityImpl.getOutgoingTransitions();
		if(pvmList != null && pvmList.size()>0){
			for(PvmTransition pvm:pvmList){
				String name = (String)pvm.getProperty("name");
				if(StringUtils.isNotBlank(name)){
					list.add(name);
				}else{
					list.add("默认提交");
				}
			}
		}
		
		return list;
	}
	
	
	/** 查找当前人的组任务*/
	public List<Task> findMyGroupTask(String candidateUser){
		List<Task> taskList = taskService.createTaskQuery()
				.taskCandidateUser(candidateUser).orderByTaskCreateTime().asc()
				.list();
		return taskList;
	}
	
	/**查询正在执行的任务办理人（候选人）*/
	
	public void findRunPersonTask(){
		String taskid = "";
		List<IdentityLink> list = taskService.getIdentityLinksForTask(taskid);
	}
}
