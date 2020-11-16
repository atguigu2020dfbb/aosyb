package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>leave[leave]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-14 09:34:19
 */
public class LeavePO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * id_
	 */
	private String id_;
	
	/**
	 * title
	 */
	private String title;
	
	/**
	 * start_time
	 */
	private String start_time;
	
	/**
	 * end_time
	 */
	private String end_time;
	
	/**
	 * day
	 */
	private String day;
	
	/**
	 * reason
	 */
	private String reason;
	
	/**
	 * state
	 */
	private Integer state;
	
	/**
	 * userid
	 */
	private String userid;
	

	/**
	 * id_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * title
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * start_time
	 * 
	 * @return start_time
	 */
	public String getStart_time() {
		return start_time;
	}
	
	/**
	 * end_time
	 * 
	 * @return end_time
	 */
	public String getEnd_time() {
		return end_time;
	}
	
	/**
	 * day
	 * 
	 * @return day
	 */
	public String getDay() {
		return day;
	}
	
	/**
	 * reason
	 * 
	 * @return reason
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * state
	 * 
	 * @return state
	 */
	public Integer getState() {
		return state;
	}
	
	/**
	 * userid
	 * 
	 * @return userid
	 */
	public String getUserid() {
		return userid;
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
	 * title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * start_time
	 * 
	 * @param start_time
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	
	/**
	 * end_time
	 * 
	 * @param end_time
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	/**
	 * day
	 * 
	 * @param day
	 */
	public void setDay(String day) {
		this.day = day;
	}
	
	/**
	 * reason
	 * 
	 * @param reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/**
	 * state
	 * 
	 * @param state
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	
	/**
	 * userid
	 * 
	 * @param userid
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	

}