package cn.osworks.aos.web.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.exception.AOSException;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;

/**
 * 报表处理类
 * 
 * @author OSWorks-XC
 */
public class AOSReport {
	
	//缺省的AOSPrint对象在会话中的标识
	public static final String DEFAULT_AOSPRINT_KEY = "DEFAULT_AOSPRINT_KEY";
	
	/**
	 * 填充报表数据，生成报表打印对象
	 * @param reportModel
	 * @return
	 */
	public static AOSPrint fillReport(AOSReportModel reportModel){
		AOSPrint aosPrint = new AOSPrint();
		JasperPrint jasperPrint = null;
		Dto paramsDto = reportModel.getParametersDto();
		List<?> fieldsList = reportModel.getFieldsList();
		String jasperFile = reportModel.getJasperFile();
		if (AOSUtils.isNotEmpty(jasperFile)) {
			if (!new File(jasperFile).exists()){
				throw new AOSException(8, jasperFile);
			}
			try {
				//JasperReport jasperReport = (JasperReport)JRLoader.loadObjectFromFile(jasperFile);
				if (AOSUtils.isEmpty(fieldsList)) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, paramsDto, new JREmptyDataSource());
				}else {
					jasperPrint = JasperFillManager.fillReport(jasperFile, paramsDto, new JRBeanCollectionDataSource(fieldsList));
				}
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		aosPrint.setJasperPrint(jasperPrint);
		return aosPrint;
	}
	
	
	/**
	 * 填充报表数据，生成报表打印对象
	 * @param reportModel
	 * @return
	 */
	public static AOSPrint fillReport44(AOSReportModel reportModel){
		AOSPrint aosPrint = new AOSPrint();
		Dto paramsDto = reportModel.getParametersDto();
		List<?> fieldsList = reportModel.getFieldsList();
		List<Archive_tablefieldlistPO> excelHeader=reportModel.getExcelHeader();
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("Sheet1");  
//        HSSFRow row = sheet.createRow(0);  
        HSSFCellStyle style = wb.createCellStyle();  
       // style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
        //给单元格赋值
        for(int i=0;i<fieldsList.size();i++){
        	HSSFRow row = sheet.createRow(i);
        	int j=0;
        	 for (String k : ((Map<String, Object>) fieldsList.get(i)).keySet()) 
             { 
             	row.createCell(j).setCellValue(((Map<String, Object>) fieldsList.get(i)).get(k) == null ? ""
     					: ((Map<String, Object>) fieldsList.get(i)).get(k).toString());
             	j++;
             } 
        }
//        
        HSSFRow row = createRow(sheet, 0);  
        createCell(row,excelHeader);  
            aosPrint.setWb(wb);
		return aosPrint;
	}
	/** 
	    * 找到需要插入的行数，并新建一个POI的row对象 
	    * @param sheet 
	    * @param rowIndex 
	    * @return 
	    */  
	    private static HSSFRow createRow(HSSFSheet sheet, Integer rowIndex) {  
	        HSSFRow row = null;  
	        if (sheet.getRow(rowIndex) != null) {  
	            int lastRowNo = sheet.getLastRowNum();  
	            sheet.shiftRows(rowIndex, lastRowNo, 1);  
	        }  
	        row = sheet.createRow(rowIndex);  
	        return row;  
	    }  
	    
	    /** 
	     * 创建要出入的行中单元格 
	     * @param row 
	     * @return 
	     */  
	    private static HSSFCell createCell(HSSFRow row,List<Archive_tablefieldlistPO> list) {  
	        HSSFCell cell = row.createCell(0);  
	        for(int i=0;i<list.size();i++){
	        	row.createCell(i).setCellValue(list.get(i).getFieldcnname());
	        }
//	        cell.setCellValue(999999);  
//	        row.createCell(1).setCellValue(1.2);  
//	        row.createCell(2).setCellValue("This is a string cell");  
	        return cell;  
	    }  
	    
	
}
