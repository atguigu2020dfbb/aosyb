package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;
import java.util.Date;

/**
 * <b>archive_LOG_Register[archive_log_register]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author Shq
 * @date 2019-11-02 10:25:57
 */
public class Archive_log_registerPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;
	
	/**
	 * _Log
	 */
	private String _log;
	
	/**
	 * _bh
	 */
	private String _bh;
	
	/**
	 * _USER
	 */
	private String _user;
	
	/**
	 * _Date
	 */
	private Date _date;
	
	/**
	 * _ModeName
	 */
	private String _modename;
	

	/**
	 * id
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * _Log
	 * 
	 * @return _log
	 */
	public String get_log() {
		return _log;
	}
	
	/**
	 * _bh
	 * 
	 * @return _bh
	 */
	public String get_bh() {
		return _bh;
	}
	
	/**
	 * _USER
	 * 
	 * @return _user
	 */
	public String get_user() {
		return _user;
	}
	
	/**
	 * _Date
	 * 
	 * @return _date
	 */
	public Date get_date() {
		return _date;
	}
	
	/**
	 * _ModeName
	 * 
	 * @return _modename
	 */
	public String get_modename() {
		return _modename;
	}
	

	/**
	 * id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * _Log
	 * 
	 * @param _log
	 */
	public void set_log(String _log) {
		this._log = _log;
	}
	
	/**
	 * _bh
	 * 
	 * @param _bh
	 */
	public void set_bh(String _bh) {
		this._bh = _bh;
	}
	
	/**
	 * _USER
	 * 
	 * @param _user
	 */
	public void set_user(String _user) {
		this._user = _user;
	}
	
	/**
	 * _Date
	 * 
	 * @param _date
	 */
	public void set_date(Date _date) {
		this._date = _date;
	}
	
	/**
	 * _ModeName
	 * 
	 * @param _modename
	 */
	public void set_modename(String _modename) {
		this._modename = _modename;
	}
	

}