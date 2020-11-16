package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>ACT_HI_PROCINST[act_hi_procinst]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-18 15:48:28
 */
public class Act_hi_procinstPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_
	 */
	private String id_;
	
	/**
	 * PROC_INST_ID_
	 */
	private String proc_inst_id_;
	
	/**
	 * BUSINESS_KEY_
	 */
	private String business_key_;
	
	/**
	 * PROC_DEF_ID_
	 */
	private String proc_def_id_;
	
	/**
	 * START_TIME_
	 */
	private Date start_time_;
	
	/**
	 * END_TIME_
	 */
	private Date end_time_;
	
	/**
	 * DURATION_
	 */
	private BigDecimal duration_;
	
	/**
	 * START_USER_ID_
	 */
	private String start_user_id_;
	
	/**
	 * START_ACT_ID_
	 */
	private String start_act_id_;
	
	/**
	 * END_ACT_ID_
	 */
	private String end_act_id_;
	
	/**
	 * SUPER_PROCESS_INSTANCE_ID_
	 */
	private String super_process_instance_id_;
	
	/**
	 * DELETE_REASON_
	 */
	private String delete_reason_;
	
	/**
	 * TENANT_ID_
	 */
	private String tenant_id_;
	
	/**
	 * NAME_
	 */
	private String name_;
	

	/**
	 * ID_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
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
	 * BUSINESS_KEY_
	 * 
	 * @return business_key_
	 */
	public String getBusiness_key_() {
		return business_key_;
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
	 * START_TIME_
	 * 
	 * @return start_time_
	 */
	public Date getStart_time_() {
		return start_time_;
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
	 * START_USER_ID_
	 * 
	 * @return start_user_id_
	 */
	public String getStart_user_id_() {
		return start_user_id_;
	}
	
	/**
	 * START_ACT_ID_
	 * 
	 * @return start_act_id_
	 */
	public String getStart_act_id_() {
		return start_act_id_;
	}
	
	/**
	 * END_ACT_ID_
	 * 
	 * @return end_act_id_
	 */
	public String getEnd_act_id_() {
		return end_act_id_;
	}
	
	/**
	 * SUPER_PROCESS_INSTANCE_ID_
	 * 
	 * @return super_process_instance_id_
	 */
	public String getSuper_process_instance_id_() {
		return super_process_instance_id_;
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
	 * TENANT_ID_
	 * 
	 * @return tenant_id_
	 */
	public String getTenant_id_() {
		return tenant_id_;
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
	 * ID_
	 * 
	 * @param id_
	 */
	public void setId_(String id_) {
		this.id_ = id_;
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
	 * BUSINESS_KEY_
	 * 
	 * @param business_key_
	 */
	public void setBusiness_key_(String business_key_) {
		this.business_key_ = business_key_;
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
	 * START_TIME_
	 * 
	 * @param start_time_
	 */
	public void setStart_time_(Date start_time_) {
		this.start_time_ = start_time_;
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
	 * START_USER_ID_
	 * 
	 * @param start_user_id_
	 */
	public void setStart_user_id_(String start_user_id_) {
		this.start_user_id_ = start_user_id_;
	}
	
	/**
	 * START_ACT_ID_
	 * 
	 * @param start_act_id_
	 */
	public void setStart_act_id_(String start_act_id_) {
		this.start_act_id_ = start_act_id_;
	}
	
	/**
	 * END_ACT_ID_
	 * 
	 * @param end_act_id_
	 */
	public void setEnd_act_id_(String end_act_id_) {
		this.end_act_id_ = end_act_id_;
	}
	
	/**
	 * SUPER_PROCESS_INSTANCE_ID_
	 * 
	 * @param super_process_instance_id_
	 */
	public void setSuper_process_instance_id_(String super_process_instance_id_) {
		this.super_process_instance_id_ = super_process_instance_id_;
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
	 * TENANT_ID_
	 * 
	 * @param tenant_id_
	 */
	public void setTenant_id_(String tenant_id_) {
		this.tenant_id_ = tenant_id_;
	}
	
	/**
	 * NAME_
	 * 
	 * @param name_
	 */
	public void setName_(String name_) {
		this.name_ = name_;
	}
	

}