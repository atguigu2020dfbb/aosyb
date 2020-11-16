package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>ACT_GE_PROPERTY[act_ge_property]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-18 15:48:28
 */
public class Act_ge_propertyPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * NAME_
	 */
	private String name_;
	
	/**
	 * VALUE_
	 */
	private String value_;
	
	/**
	 * REV_
	 */
	private Integer rev_;
	

	/**
	 * NAME_
	 * 
	 * @return name_
	 */
	public String getName_() {
		return name_;
	}
	
	/**
	 * VALUE_
	 * 
	 * @return value_
	 */
	public String getValue_() {
		return value_;
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
	 * @param name_
	 */
	public void setName_(String name_) {
		this.name_ = name_;
	}
	
	/**
	 * VALUE_
	 * 
	 * @param value_
	 */
	public void setValue_(String value_) {
		this.value_ = value_;
	}
	
	/**
	 * REV_
	 * 
	 * @param rev_
	 */
	public void setRev_(Integer rev_) {
		this.rev_ = rev_;
	}
	

}