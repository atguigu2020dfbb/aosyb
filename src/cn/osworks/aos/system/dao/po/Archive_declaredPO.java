package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>archive_declared[archive_declared]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author Shq
 * @date 2019-12-21 18:40:00
 */
public class Archive_declaredPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * id_
	 */
	private String id_;
	
	/**
	 * name_
	 */
	private String name_;
	
	/**
	 * desc_
	 */
	private String desc_;
	
	/**
	 * declared_list
	 */
	private String declared_list;
	
	/**
	 * create_person
	 */
	private String create_person;
	
	/**
	 * create_time
	 */
	private String create_time;
	

	/**
	 * id_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * name_
	 * 
	 * @return name_
	 */
	public String getName_() {
		return name_;
	}
	
	/**
	 * desc_
	 * 
	 * @return desc_
	 */
	public String getDesc_() {
		return desc_;
	}
	
	/**
	 * declared_list
	 * 
	 * @return declared_list
	 */
	public String getDeclared_list() {
		return declared_list;
	}
	
	/**
	 * create_person
	 * 
	 * @return create_person
	 */
	public String getCreate_person() {
		return create_person;
	}
	
	/**
	 * create_time
	 * 
	 * @return create_time
	 */
	public String getCreate_time() {
		return create_time;
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
	 * name_
	 * 
	 * @param name_
	 */
	public void setName_(String name_) {
		this.name_ = name_;
	}
	
	/**
	 * desc_
	 * 
	 * @param desc_
	 */
	public void setDesc_(String desc_) {
		this.desc_ = desc_;
	}
	
	/**
	 * declared_list
	 * 
	 * @param declared_list
	 */
	public void setDeclared_list(String declared_list) {
		this.declared_list = declared_list;
	}
	
	/**
	 * create_person
	 * 
	 * @param create_person
	 */
	public void setCreate_person(String create_person) {
		this.create_person = create_person;
	}
	
	/**
	 * create_time
	 * 
	 * @param create_time
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	

}