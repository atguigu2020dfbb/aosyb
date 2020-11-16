package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>ACT_GE_BYTEARRAY[act_ge_bytearray]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-18 15:48:28
 */
public class Act_ge_bytearrayPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_
	 */
	private String id_;
	
	/**
	 * REV_
	 */
	private Integer rev_;
	
	/**
	 * NAME_
	 */
	private String name_;
	
	/**
	 * DEPLOYMENT_ID_
	 */
	private String deployment_id_;
	
	/**
	 * BYTES_
	 */
	private byte[] bytes_;
	
	/**
	 * GENERATED_
	 */
	private Integer generated_;
	

	/**
	 * ID_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * REV_
	 * 
	 * @return rev_
	 */
	public Integer getRev_() {
		return rev_;
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
	 * DEPLOYMENT_ID_
	 * 
	 * @return deployment_id_
	 */
	public String getDeployment_id_() {
		return deployment_id_;
	}
	
	/**
	 * BYTES_
	 * 
	 * @return bytes_
	 */
	public byte[] getBytes_() {
		return bytes_;
	}
	
	/**
	 * GENERATED_
	 * 
	 * @return generated_
	 */
	public Integer getGenerated_() {
		return generated_;
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
	 * REV_
	 * 
	 * @param rev_
	 */
	public void setRev_(Integer rev_) {
		this.rev_ = rev_;
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
	 * DEPLOYMENT_ID_
	 * 
	 * @param deployment_id_
	 */
	public void setDeployment_id_(String deployment_id_) {
		this.deployment_id_ = deployment_id_;
	}
	
	/**
	 * BYTES_
	 * 
	 * @param bytes_
	 */
	public void setBytes_(byte[] bytes_) {
		this.bytes_ = bytes_;
	}
	
	/**
	 * GENERATED_
	 * 
	 * @param generated_
	 */
	public void setGenerated_(Integer generated_) {
		this.generated_ = generated_;
	}
	

}