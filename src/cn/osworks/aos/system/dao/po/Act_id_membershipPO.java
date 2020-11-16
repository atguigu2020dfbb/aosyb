package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>ACT_ID_MEMBERSHIP[act_id_membership]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-18 15:48:29
 */
public class Act_id_membershipPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * USER_ID_
	 */
	private String user_id_;
	
	/**
	 * GROUP_ID_
	 */
	private String group_id_;
	

	/**
	 * USER_ID_
	 * 
	 * @return user_id_
	 */
	public String getUser_id_() {
		return user_id_;
	}
	
	/**
	 * GROUP_ID_
	 * 
	 * @return group_id_
	 */
	public String getGroup_id_() {
		return group_id_;
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
	 * GROUP_ID_
	 * 
	 * @param group_id_
	 */
	public void setGroup_id_(String group_id_) {
		this.group_id_ = group_id_;
	}
	

}