package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;
import java.util.Date;

/**
 * <b>ACT_EVT_LOG[act_evt_log]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-18 15:48:28
 */
public class Act_evt_logPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * LOG_NR_
	 */
	private String log_nr_;
	
	/**
	 * TYPE_
	 */
	private String type_;
	
	/**
	 * PROC_DEF_ID_
	 */
	private String proc_def_id_;
	
	/**
	 * PROC_INST_ID_
	 */
	private String proc_inst_id_;
	
	/**
	 * EXECUTION_ID_
	 */
	private String execution_id_;
	
	/**
	 * TASK_ID_
	 */
	private String task_id_;
	
	/**
	 * TIME_STAMP_
	 */
	private Date time_stamp_;
	
	/**
	 * USER_ID_
	 */
	private String user_id_;
	
	/**
	 * DATA_
	 */
	private byte[] data_;
	
	/**
	 * LOCK_OWNER_
	 */
	private String lock_owner_;
	
	/**
	 * LOCK_TIME_
	 */
	private Date lock_time_;
	
	/**
	 * IS_PROCESSED_
	 */
	private Integer is_processed_;
	

	/**
	 * LOG_NR_
	 * 
	 * @return log_nr_
	 */
	public String getLog_nr_() {
		return log_nr_;
	}
	
	/**
	 * TYPE_
	 * 
	 * @return type_
	 */
	public String getType_() {
		return type_;
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
	 * TASK_ID_
	 * 
	 * @return task_id_
	 */
	public String getTask_id_() {
		return task_id_;
	}
	
	/**
	 * TIME_STAMP_
	 * 
	 * @return time_stamp_
	 */
	public Date getTime_stamp_() {
		return time_stamp_;
	}
	
	/**
	 * USER_ID_
	 * 
	 * @return user_id_
	 */
	public String getUser_id_() {
		return user_id_;
	}
	
	/**
	 * DATA_
	 * 
	 * @return data_
	 */
	public byte[] getData_() {
		return data_;
	}
	
	/**
	 * LOCK_OWNER_
	 * 
	 * @return lock_owner_
	 */
	public String getLock_owner_() {
		return lock_owner_;
	}
	
	/**
	 * LOCK_TIME_
	 * 
	 * @return lock_time_
	 */
	public Date getLock_time_() {
		return lock_time_;
	}
	
	/**
	 * IS_PROCESSED_
	 * 
	 * @return is_processed_
	 */
	public Integer getIs_processed_() {
		return is_processed_;
	}
	

	/**
	 * LOG_NR_
	 * 
	 * @param log_nr_
	 */
	public void setLog_nr_(String log_nr_) {
		this.log_nr_ = log_nr_;
	}
	
	/**
	 * TYPE_
	 * 
	 * @param type_
	 */
	public void setType_(String type_) {
		this.type_ = type_;
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
	 * TASK_ID_
	 * 
	 * @param task_id_
	 */
	public void setTask_id_(String task_id_) {
		this.task_id_ = task_id_;
	}
	
	/**
	 * TIME_STAMP_
	 * 
	 * @param time_stamp_
	 */
	public void setTime_stamp_(Date time_stamp_) {
		this.time_stamp_ = time_stamp_;
	}
	
	/**
	 * USER_ID_
	 * 
	 * @param user_id_
	 */
	public void setUser_id_(String user_id_) {
		this.user_id_ = user_id_;
	}
	
	/**
	 * DATA_
	 * 
	 * @param data_
	 */
	public void setData_(byte[] data_) {
		this.data_ = data_;
	}
	
	/**
	 * LOCK_OWNER_
	 * 
	 * @param lock_owner_
	 */
	public void setLock_owner_(String lock_owner_) {
		this.lock_owner_ = lock_owner_;
	}
	
	/**
	 * LOCK_TIME_
	 * 
	 * @param lock_time_
	 */
	public void setLock_time_(Date lock_time_) {
		this.lock_time_ = lock_time_;
	}
	
	/**
	 * IS_PROCESSED_
	 * 
	 * @param is_processed_
	 */
	public void setIs_processed_(Integer is_processed_) {
		this.is_processed_ = is_processed_;
	}
	

}