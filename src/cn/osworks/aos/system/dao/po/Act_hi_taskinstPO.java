package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>ACT_HI_TASKINST[act_hi_taskinst]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-18 15:48:29
 */
public class Act_hi_taskinstPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_
	 */
	private String id_;
	
	/**
	 * PROC_DEF_ID_
	 */
	private String proc_def_id_;
	
	/**
	 * TASK_DEF_KEY_
	 */
	private String task_def_key_;
	
	/**
	 * PROC_INST_ID_
	 */
	private String proc_inst_id_;
	
	/**
	 * EXECUTION_ID_
	 */
	private String execution_id_;
	
	/**
	 * NAME_
	 */
	private String name_;
	
	/**
	 * PARENT_TASK_ID_
	 */
	private String parent_task_id_;
	
	/**
	 * DESCRIPTION_
	 */
	private String description_;
	
	/**
	 * OWNER_
	 */
	private String owner_;
	
	/**
	 * ASSIGNEE_
	 */
	private String assignee_;
	
	/**
	 * START_TIME_
	 */
	private Date start_time_;
	
	/**
	 * CLAIM_TIME_
	 */
	private Date claim_time_;
	
	/**
	 * END_TIME_
	 */
	private Date end_time_;
	
	/**
	 * DURATION_
	 */
	private BigDecimal duration_;
	
	/**
	 * DELETE_REASON_
	 */
	private String delete_reason_;
	
	/**
	 * PRIORITY_
	 */
	private Integer priority_;
	
	/**
	 * DUE_DATE_
	 */
	private Date due_date_;
	
	/**
	 * FORM_KEY_
	 */
	private String form_key_;
	
	/**
	 * CATEGORY_
	 */
	private String category_;
	
	/**
	 * TENANT_ID_
	 */
	private String tenant_id_;
	

	/**
	 * ID_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * PROC_DEF_ID_
	 * 
	 * @return proc_def_id_
	 */
	public String getProc_def_id_() {
		return proc_def_id_;
	}
	
	/**
	 * TASK_DEF_KEY_
	 * 
	 * @return task_def_key_
	 */
	public String getTask_def_key_() {
		return task_def_key_;
	}
	
	/**
	 * PROC_INST_ID_
	 * 
	 * @return proc_inst_id_
	 */
	public String getProc_inst_id_() {
		return proc_inst_id_;
	}
	
	/**
	 * EXECUTION_ID_
	 * 
	 * @return execution_id_
	 */
	public String getExecution_id_() {
		return execution_id_;
	}
	
	/**
	 * NAME_
	 * 
	 * @return name_
	 */
	public String getName_() {
		return name_;
	}
	
	/**
	 * PARENT_TASK_ID_
	 * 
	 * @return parent_task_id_
	 */
	public String getParent_task_id_() {
		return parent_task_id_;
	}
	
	/**
	 * DESCRIPTION_
	 * 
	 * @return description_
	 */
	public String getDescription_() {
		return description_;
	}
	
	/**
	 * OWNER_
	 * 
	 * @return owner_
	 */
	public String getOwner_() {
		return owner_;
	}
	
	/**
	 * ASSIGNEE_
	 * 
	 * @return assignee_
	 */
	public String getAssignee_() {
		return assignee_;
	}
	
	/**
	 * START_TIME_
	 * 
	 * @return start_time_
	 */
	public Date getStart_time_() {
		return start_time_;
	}
	
	/**
	 * CLAIM_TIME_
	 * 
	 * @return claim_time_
	 */
	public Date getClaim_time_() {
		return claim_time_;
	}
	
	/**
	 * END_TIME_
	 * 
	 * @return end_time_
	 */
	public Date getEnd_time_() {
		return end_time_;
	}
	
	/**
	 * DURATION_
	 * 
	 * @return duration_
	 */
	public BigDecimal getDuration_() {
		return duration_;
	}
	
	/**
	 * DELETE_REASON_
	 * 
	 * @return delete_reason_
	 */
	public String getDelete_reason_() {
		return delete_reason_;
	}
	
	/**
	 * PRIORITY_
	 * 
	 * @return priority_
	 */
	public Integer getPriority_() {
		return priority_;
	}
	
	/**
	 * DUE_DATE_
	 * 
	 * @return due_date_
	 */
	public Date getDue_date_() {
		return due_date_;
	}
	
	/**
	 * FORM_KEY_
	 * 
	 * @return form_key_
	 */
	public String getForm_key_() {
		return form_key_;
	}
	
	/**
	 * CATEGORY_
	 * 
	 * @return category_
	 */
	public String getCategory_() {
		return category_;
	}
	
	/**
	 * TENANT_ID_
	 * 
	 * @return tenant_id_
	 */
	public String getTenant_id_() {
		return tenant_id_;
	}
	

	/**
	 * ID_
	 * 
	 * @param id_
	 */
	public void setId_(String id_) {
		this.id_ = id_;
	}
	
	/**
	 * PROC_DEF_ID_
	 * 
	 * @param proc_def_id_
	 */
	public void setProc_def_id_(String proc_def_id_) {
		this.proc_def_id_ = proc_def_id_;
	}
	
	/**
	 * TASK_DEF_KEY_
	 * 
	 * @param task_def_key_
	 */
	public void setTask_def_key_(String task_def_key_) {
		this.task_def_key_ = task_def_key_;
	}
	
	/**
	 * PROC_INST_ID_
	 * 
	 * @param proc_inst_id_
	 */
	public void setProc_inst_id_(String proc_inst_id_) {
		this.proc_inst_id_ = proc_inst_id_;
	}
	
	/**
	 * EXECUTION_ID_
	 * 
	 * @param execution_id_
	 */
	public void setExecution_id_(String execution_id_) {
		this.execution_id_ = execution_id_;
	}
	
	/**
	 * NAME_
	 * 
	 * @param name_
	 */
	public void setName_(String name_) {
		this.name_ = name_;
	}
	
	/**
	 * PARENT_TASK_ID_
	 * 
	 * @param parent_task_id_
	 */
	public void setParent_task_id_(String parent_task_id_) {
		this.parent_task_id_ = parent_task_id_;
	}
	
	/**
	 * DESCRIPTION_
	 * 
	 * @param description_
	 */
	public void setDescription_(String description_) {
		this.description_ = description_;
	}
	
	/**
	 * OWNER_
	 * 
	 * @param owner_
	 */
	public void setOwner_(String owner_) {
		this.owner_ = owner_;
	}
	
	/**
	 * ASSIGNEE_
	 * 
	 * @param assignee_
	 */
	public void setAssignee_(String assignee_) {
		this.assignee_ = assignee_;
	}
	
	/**
	 * START_TIME_
	 * 
	 * @param start_time_
	 */
	public void setStart_time_(Date start_time_) {
		this.start_time_ = start_time_;
	}
	
	/**
	 * CLAIM_TIME_
	 * 
	 * @param claim_time_
	 */
	public void setClaim_time_(Date claim_time_) {
		this.claim_time_ = claim_time_;
	}
	
	/**
	 * END_TIME_
	 * 
	 * @param end_time_
	 */
	public void setEnd_time_(Date end_time_) {
		this.end_time_ = end_time_;
	}
	
	/**
	 * DURATION_
	 * 
	 * @param duration_
	 */
	public void setDuration_(BigDecimal duration_) {
		this.duration_ = duration_;
	}
	
	/**
	 * DELETE_REASON_
	 * 
	 * @param delete_reason_
	 */
	public void setDelete_reason_(String delete_reason_) {
		this.delete_reason_ = delete_reason_;
	}
	
	/**
	 * PRIORITY_
	 * 
	 * @param priority_
	 */
	public void setPriority_(Integer priority_) {
		this.priority_ = priority_;
	}
	
	/**
	 * DUE_DATE_
	 * 
	 * @param due_date_
	 */
	public void setDue_date_(Date due_date_) {
		this.due_date_ = due_date_;
	}
	
	/**
	 * FORM_KEY_
	 * 
	 * @param form_key_
	 */
	public void setForm_key_(String form_key_) {
		this.form_key_ = form_key_;
	}
	
	/**
	 * CATEGORY_
	 * 
	 * @param category_
	 */
	public void setCategory_(String category_) {
		this.category_ = category_;
	}
	
	/**
	 * TENANT_ID_
	 * 
	 * @param tenant_id_
	 */
	public void setTenant_id_(String tenant_id_) {
		this.tenant_id_ = tenant_id_;
	}
	

}