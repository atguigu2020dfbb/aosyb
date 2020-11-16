package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>archive_compilation_tablenameid[archive_compilation_tablenameid]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author Shq
 * @date 2020-02-13 12:14:59
 */
public class Archive_compilation_tablenameidPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * id_
	 */
	private String id_;
	
	/**
	 * pid
	 */
	private String pid;
	
	/**
	 * tablename_id
	 */
	private String tablename_id;
	
	/**
	 * tablename
	 */
	private String tablename;
	

	/**
	 * id_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * pid
	 * 
	 * @return pid
	 */
	public String getPid() {
		return pid;
	}
	
	/**
	 * tablename_id
	 * 
	 * @return tablename_id
	 */
	public String getTablename_id() {
		return tablename_id;
	}
	
	/**
	 * tablename
	 * 
	 * @return tablename
	 */
	public String getTablename() {
		return tablename;
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
	 * pid
	 * 
	 * @param pid
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	/**
	 * tablename_id
	 * 
	 * @param tablename_id
	 */
	public void setTablename_id(String tablename_id) {
		this.tablename_id = tablename_id;
	}
	
	/**
	 * tablename
	 * 
	 * @param tablename
	 */
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	

}