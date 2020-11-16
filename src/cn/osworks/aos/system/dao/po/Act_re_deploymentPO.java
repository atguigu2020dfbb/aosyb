package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;
import java.util.Date;

/**
 * <b>ACT_RE_DEPLOYMENT[act_re_deployment]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-18 15:48:29
 */
public class Act_re_deploymentPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_
	 */
	private String id_;
	
	/**
	 * NAME_
	 */
	private String name_;
	
	/**
	 * CATEGORY_
	 */
	private String category_;
	
	/**
	 * TENANT_ID_
	 */
	private String tenant_id_;
	
	/**
	 * DEPLOY_TIME_
	 */
	private Date deploy_time_;
	

	/**
	 * ID_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
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
	 * DEPLOY_TIME_
	 * 
	 * @return deploy_time_
	 */
	public Date getDeploy_time_() {
		return deploy_time_;
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
	 * NAME_
	 * 
	 * @param name_
	 */
	public void setName_(String name_) {
		this.name_ = name_;
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
	
	/**
	 * DEPLOY_TIME_
	 * 
	 * @param deploy_time_
	 */
	public void setDeploy_time_(Date deploy_time_) {
		this.deploy_time_ = deploy_time_;
	}
	

}