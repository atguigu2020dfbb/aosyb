package cn.osworks.aos.system.dao.po;

import cn.osworks.aos.core.typewrap.PO;

/**
 * <b>setWatermark[setwatermark]数据持久化对象</b>
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改。
 * </p>
 * 
 * @author Shq
 * @date 2020-08-28 15:22:29
 */
public class SetwatermarkPO extends PO {

	private static final long serialVersionUID = 1L;

	/**
	 * id_
	 */
	private Integer id_;
	
	/**
	 * txtword
	 */
	private String txtword;
	
	/**
	 * wordSize
	 */
	private String wordsize;
	
	/**
	 * font
	 */
	private String wordfont;
	
	/**
	 * rad
	 */
	private String wordrad;
	
	/**
	 * opacity
	 */
	private String wordopacity;
	
	private String density;

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	/**
	 * id_
	 * 
	 * @return id_
	 */
	public Integer getId_() {
		return id_;
	}
	
	/**
	 * txtword
	 * 
	 * @return txtword
	 */
	public String getTxtword() {
		return txtword;
	}
	
	/**
	 * wordSize
	 * 
	 * @return wordsize
	 */
	public String getWordsize() {
		return wordsize;
	}
	
	/**
	 * font
	 * 
	 * @return font
	 */
	public String getWordfont() {
		return wordfont;
	}
	
	/**
	 * rad
	 * 
	 * @return rad
	 */
	public String getWordrad() {
		return wordrad;
	}
	
	/**
	 * opacity
	 * 
	 * @return opacity
	 */
	public String getWordopacity() {
		return wordopacity;
	}
	

	/**
	 * id_
	 * 
	 * @param id_
	 */
	public void setId_(Integer id_) {
		this.id_ = id_;
	}
	
	/**
	 * txtword
	 * 
	 * @param txtword
	 */
	public void setTxtword(String txtword) {
		this.txtword = txtword;
	}
	
	/**
	 * wordSize
	 * 
	 * @param wordsize
	 */
	public void setWordsize(String wordsize) {
		this.wordsize = wordsize;
	}
	
	/**
	 * font
	 * 
	 * @param wordfont
	 */
	public void setWordfont(String wordfont) {
		this.wordfont = wordfont;
	}
	
	/**
	 * rad
	 * 
	 * @param wordrad
	 */
	public void setWordrad(String wordrad) {
		this.wordrad = wordrad;
	}
	
	/**
	 * opacity
	 * 
	 * @param wordopacity
	 */
	public void setWordopacity(String wordopacity) {
		this.wordopacity = wordopacity;
	}
	

}