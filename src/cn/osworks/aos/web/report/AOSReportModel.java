package cn.osworks.aos.web.report;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;

/**
 * 报表数据模型
 * 
 * @author OSWorks-XC
 */
public class AOSReportModel {

	/**
	 * 报表参数对象
	 */
	private Dto parametersDto;

	/**
	 * 报表集合对象
	 */
	private List<Archive_tablefieldlistPO> excelHeader;
	
	/**
	 * 报表集合对象
	 */
	private List<?> fieldsList;
	
	/**
	 * 报表编译文件
	 */
	private String jasperFile;
	/**
	 * 报表集合对象（日志）
	 */
	private List<Map<String,Object>> logsList;
	/***
	 * 
	 */
	private LinkedHashMap<String,Object> logHeader;
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 报表源文件
	 */
	private String jrxmlFile;
	
		
	public AOSReportModel() {}
	
	/**
	 * 构造函数
	 * 
	 * @param parametersDto 报表参数集
	 * @param fieldsList 报表字段列表集合
	 */
	public AOSReportModel(Dto parametersDto, List<?> fieldsList) {
		this.parametersDto = parametersDto;
		this.fieldsList = fieldsList;
	}

	public Dto getParametersDto() {
		return parametersDto;
	}

	public void setParametersDto(Dto parametersDto) {
		this.parametersDto = parametersDto;
	}

	public List<?> getFieldsList() {
		return fieldsList;
	}

	public void setFieldsList(List<?> fieldsList) {
		this.fieldsList = fieldsList;
	}

	public String getJasperFile() {
		return jasperFile;
	}

	public void setJasperFile(String jasperFile) {
		this.jasperFile = jasperFile;
	}

	public String getJrxmlFile() {
		return jrxmlFile;
	}

	public void setJrxmlFile(String jrxmlFile) {
		this.jrxmlFile = jrxmlFile;
	}

	public List<Archive_tablefieldlistPO> getExcelHeader() {
		return excelHeader;
	}

	public void setExcelHeader(List<Archive_tablefieldlistPO> excelHeader) {
		this.excelHeader = excelHeader;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public LinkedHashMap<String, Object> getLogHeader(){
		return logHeader;
	}
	public void setLogHeader(LinkedHashMap<String,Object> logHeader){
		this.logHeader=logHeader;
	}
	public List<Map<String,Object>> getLogsList(){
		return logsList;
	}
	public void setLogsList(List<Map<String,Object>> logsList){
		this.logsList=logsList;
	}

}
