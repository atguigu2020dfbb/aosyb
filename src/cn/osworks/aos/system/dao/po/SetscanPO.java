package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>setscan[setscan]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author Shq
 * @date 2020-08-18 13:52:27
 */
public class SetscanPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * id_
	 */
	private String id_;
	
	/**
	 * caption
	 */
	private String caption;
	
	/**
	 * tablename
	 */
	private String tablename;
	
	/**
	 * tm
	 */
	private String tm;
	
	/**
	 * capname
	 */
	private String capname;
	

	/**
	 * id_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * caption
	 * 
	 * @return caption
	 */
	public String getCaption() {
		return caption;
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
	 * tm
	 * 
	 * @return tm
	 */
	public String getTm() {
		return tm;
	}
	
	/**
	 * capname
	 * 
	 * @return capname
	 */
	public String getCapname() {
		return capname;
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
	 * caption
	 * 
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * tablename
	 * 
	 * @param tablename
	 */
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	
	/**
	 * tm
	 * 
	 * @param tm
	 */
	public void setTm(String tm) {
		this.tm = tm;
	}
	
	/**
	 * capname
	 * 
	 * @param capname
	 */
	public void setCapname(String capname) {
		this.capname = capname;
	}
	

}