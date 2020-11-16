package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>checkup_topic_log[checkup_topic_log]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author Shq
 * @date 2020-04-02 14:38:37
 */
public class Checkup_topic_logPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * id_
	 */
	private String id_;
	
	/**
	 * rw_id_
	 */
	private String rw_id_;
	
	/**
	 * cnuser
	 */
	private String cnuser;
	
	/**
	 * description
	 */
	private String description;
	
	/**
	 * operate_time
	 */
	private String operate_time;
	
	/**
	 * enuser
	 */
	private String enuser;
	

	/**
	 * id_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * rw_id_
	 * 
	 * @return rw_id_
	 */
	public String getRw_id_() {
		return rw_id_;
	}
	
	/**
	 * cnuser
	 * 
	 * @return cnuser
	 */
	public String getCnuser() {
		return cnuser;
	}
	
	/**
	 * description
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * operate_time
	 * 
	 * @return operate_time
	 */
	public String getOperate_time() {
		return operate_time;
	}
	
	/**
	 * enuser
	 * 
	 * @return enuser
	 */
	public String getEnuser() {
		return enuser;
	}
	

	/**
	 * id_
	 * 
	 * @param id_
	 */
	public void setId_(String id_) {
		this.id_ = id_;
	}
	
	/**
	 * rw_id_
	 * 
	 * @param rw_id_
	 */
	public void setRw_id_(String rw_id_) {
		this.rw_id_ = rw_id_;
	}
	
	/**
	 * cnuser
	 * 
	 * @param cnuser
	 */
	public void setCnuser(String cnuser) {
		this.cnuser = cnuser;
	}
	
	/**
	 * description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * operate_time
	 * 
	 * @param operate_time
	 */
	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}
	
	/**
	 * enuser
	 * 
	 * @param enuser
	 */
	public void setEnuser(String enuser) {
		this.enuser = enuser;
	}
	

}