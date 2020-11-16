package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>ACT_PROCDEF_INFO[act_procdef_info]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author AHei
 * @date 2018-12-18 15:48:29
 */
public class Act_procdef_infoPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_
	 */
	private String id_;
	
	/**
	 * PROC_DEF_ID_
	 */
	private String proc_def_id_;
	
	/**
	 * REV_
	 */
	private Integer rev_;
	
	/**
	 * INFO_JSON_ID_
	 */
	private String info_json_id_;
	

	/**
	 * ID_
	 * 
	 * @return id_
	 */
	public String getId_() {
		return id_;
	}
	
	/**
	 * PROC_DEF_ID_
	 * 
	 * @return proc_def_id_
	 */
	public String getProc_def_id_() {
		return proc_def_id_;
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
	 * INFO_JSON_ID_
	 * 
	 * @return info_json_id_
	 */
	public String getInfo_json_id_() {
		return info_json_id_;
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
	 * PROC_DEF_ID_
	 * 
	 * @param proc_def_id_
	 */
	public void setProc_def_id_(String proc_def_id_) {
		this.proc_def_id_ = proc_def_id_;
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
	 * INFO_JSON_ID_
	 * 
	 * @param info_json_id_
	 */
	public void setInfo_json_id_(String info_json_id_) {
		this.info_json_id_ = info_json_id_;
	}
	

}