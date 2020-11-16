package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>archive_leader[archive_leader]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author Shq
 * @date 2019-11-02 14:05:01
 */
public class Archive_leaderPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * id_
	 */
	private String id_;
	
	/**
	 * tid
	 */
	private String tid;
	
	/**
	 * leader_person
	 */
	private String leader_person;
	
	/**
	 * leader_time
	 */
	private String leader_time;
	
	/**
	 * leader_message
	 */
	private String leader_message;
	

	/**
	 * id_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * tid
	 * 
	 * @return tid
	 */
	public String getTid() {
		return tid;
	}
	
	/**
	 * leader_person
	 * 
	 * @return leader_person
	 */
	public String getLeader_person() {
		return leader_person;
	}
	
	/**
	 * leader_time
	 * 
	 * @return leader_time
	 */
	public String getLeader_time() {
		return leader_time;
	}
	
	/**
	 * leader_message
	 * 
	 * @return leader_message
	 */
	public String getLeader_message() {
		return leader_message;
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
	 * tid
	 * 
	 * @param tid
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	/**
	 * leader_person
	 * 
	 * @param leader_person
	 */
	public void setLeader_person(String leader_person) {
		this.leader_person = leader_person;
	}
	
	/**
	 * leader_time
	 * 
	 * @param leader_time
	 */
	public void setLeader_time(String leader_time) {
		this.leader_time = leader_time;
	}
	
	/**
	 * leader_message
	 * 
	 * @param leader_message
	 */
	public void setLeader_message(String leader_message) {
		this.leader_message = leader_message;
	}
	

}