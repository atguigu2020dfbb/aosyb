package cn.osworks.aos.web.report;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * 报表打印对象
 * 
 * @author OSWorks-XC
 */
public class AOSPrint {
	
	/**
	 * 原始打印对象
	 */
	private JasperPrint jasperPrint;
	
	/**
	 * 报表集合对象
	 */
	private List<Archive_tablefieldlistPO> excelHeader;
	
	/**
	 * 报表集合对象
	 */
	private List<?> fieldsList;
	
	/**
	 * 原始打印对象
	 */
	private HSSFWorkbook wb;
	
    /**
     * 文件下载或另存为时的缺省文件名
     */
	private String fileName;
	
	/**
	 * 导出XLS时的配置对象
	 */
	private SimpleXlsReportConfiguration xlsConfiguration;
	
	/**
	 * 导出XLSX时的配置对象
	 */
	private SimpleXlsxReportConfiguration xlsxConfiguration;

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public SimpleXlsReportConfiguration getXlsConfiguration() {
		return xlsConfiguration;
	}

	public void setXlsConfiguration(SimpleXlsReportConfiguration xlsConfiguration) {
		this.xlsConfiguration = xlsConfiguration;
	}

	public SimpleXlsxReportConfiguration getXlsxConfiguration() {
		return xlsxConfiguration;
	}

	public void setXlsxConfiguration(SimpleXlsxReportConfiguration xlsxConfiguration) {
		this.xlsxConfiguration = xlsxConfiguration;
	}

	public HSSFWorkbook getWb() {
		return wb;
	}

	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

	public List<Archive_tablefieldlistPO> getExcelHeader() {
		return excelHeader;
	}

	public void setExcelHeader(List<Archive_tablefieldlistPO> excelHeader) {
		this.excelHeader = excelHeader;
	}

	public List<?> getFieldsList() {
		return fieldsList;
	}

	public void setFieldsList(List<?> fieldsList) {
		this.fieldsList = fieldsList;
	}
	
}
