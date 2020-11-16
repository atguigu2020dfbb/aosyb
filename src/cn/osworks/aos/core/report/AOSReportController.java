package cn.osworks.aos.core.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.exception.AOSException;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.web.report.AOSPrint;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;

/**
 * 报表导出通用控制器
 * 
 * @author OSWorks-XC
 */
@Controller
@RequestMapping(value = "report")
public class AOSReportController {

	private static final String DEFAULT_FILE_NAME = "报表数据";

	// 用于支持同一个会话上缓存多个报表的参数名称，由URL传递过来。&aos_report_=会话中存储AOSPrint对象的Key
	private static final String AOSPRINT_PARAM = "aos_report_";

	/**
	 * 获取AOSPrint对象
	 * 
	 * @param inDto
	 * @param session
	 * @return
	 */
	private AOSPrint getAosPrintFromSession(Dto inDto, HttpSession session) {
		String Key = AOSReport.DEFAULT_AOSPRINT_KEY;
		if (AOSUtils.isNotEmpty(inDto.getString(AOSPRINT_PARAM))) {
			Key = inDto.getString(AOSPRINT_PARAM);
		}
		//AOSPrint bb = (AOSPrint)session.getAttribute(Key);
		AOSPrint aosPrint = (AOSPrint) session.getAttribute(Key);
		if (aosPrint == null) {
			throw new AOSException(9, Key);
		}
		return aosPrint;
	}

	/**
	 * 以PDF格式输出打印对象
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "pdf")
	public void pdf(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("application/pdf");
		AOSPrint aosPrint = getAosPrintFromSession(Dtos.newDto(request), session);
		String fileName = AOSUtils.isEmpty(aosPrint.getFileName()) ? DEFAULT_FILE_NAME : aosPrint.getFileName();
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName + ".pdf", "utf-8"));
		OutputStream outputStream = response.getOutputStream();
		JRPdfExporter exporter = new JRPdfExporter(DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(new SimpleExporterInput(aosPrint.getJasperPrint()));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		try {
			exporter.exportReport();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	/**
	 * 以DOCX格式输出打印对象
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "docx")
	public void docx(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		AOSPrint aosPrint = getAosPrintFromSession(Dtos.newDto(request), session);
		String fileName = AOSUtils.isEmpty(aosPrint.getFileName()) ? DEFAULT_FILE_NAME : aosPrint.getFileName();
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName + ".docx", "utf-8"));
		OutputStream outputStream = response.getOutputStream();
		JRDocxExporter exporter = new JRDocxExporter(DefaultJasperReportsContext.getInstance());
//		exporter.setExporterInput(new SimpleExporterInput(aosPrint.getJasperPrint()));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		try {
			exporter.exportReport();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	/**
	 * 以PPTX格式输出打印对象
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "pptx")
	public void pptx(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
		AOSPrint aosPrint = getAosPrintFromSession(Dtos.newDto(request), session);
		String fileName = AOSUtils.isEmpty(aosPrint.getFileName()) ? DEFAULT_FILE_NAME : aosPrint.getFileName();
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName + ".pptx", "utf-8"));
		OutputStream outputStream = response.getOutputStream();
		JRPptxExporter exporter = new JRPptxExporter(DefaultJasperReportsContext.getInstance());
//		exporter.setExporterInput(new SimpleExporterInput(aosPrint.getJasperPrint()));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		try {
			exporter.exportReport();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e2) {
				}
			}
		}
	}


	/**
	 * 以HTML格式输出打印对象
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "html")
	public void html(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("text/html");
		JasperPrint jasperPrint = getAosPrintFromSession(Dtos.newDto(request), session).getJasperPrint();
		// Servlet环境下使用不需要关闭。response会帮你关闭，别自找麻烦！
		// 仅限PrintWriter哦!!
		PrintWriter out = response.getWriter();
		try {
			HtmlExporter exporter = new HtmlExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(out);
			exporter.setExporterOutput(output);
			exporter.exportReport();
		} catch (JRException e) {
			throw new AOSException("报表输出为HTML格式时发生异常。", e);
		}
	}

	/**
	 * 以XLS格式输出打印对象
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "xls")
	public void xls(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("application/xls");
		AOSReportModel aosReport = getAosPrintFromSession2(Dtos.newDto(request), session);
		String fileName = AOSUtils.isEmpty(aosReport.getFileName()) ? DEFAULT_FILE_NAME : aosReport.getFileName();
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName + ".xls", "utf-8"));
		List<?> fieldsList = aosReport.getFieldsList();
		List<Archive_tablefieldlistPO> excelHeader=aosReport.getExcelHeader();
		HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("Sheet1");  
//        HSSFRow row = sheet.createRow(0);  
        HSSFCellStyle style = wb.createCellStyle();  
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
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
        
        HSSFRow row = createRow(sheet, 0);  
        createCell(row,excelHeader);  
//            aosPrint.setWb(wb);
		
//		HSSFWorkbook wb = aosPrint.getWb();
		OutputStream outputStream = response.getOutputStream();
		wb.write(outputStream);  
		try {
			outputStream.flush();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	/**
	 * 以XLS格式输出打印对象
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "xls_qzj")
	public void xls_qzj(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("application/xls");
		AOSReportModel aosReport = getAosPrintFromSession2(Dtos.newDto(request), session);
		String fileName = AOSUtils.isEmpty(aosReport.getFileName()) ? DEFAULT_FILE_NAME : aosReport.getFileName();
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName + ".xls", "utf-8"));
		List<Map<String,Object>> fieldsList = aosReport.getLogsList();
		LinkedHashMap<String,Object> excelHeader=aosReport.getLogHeader();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Sheet1");
//        HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		//style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
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

		HSSFRow row = createRow(sheet, 0);
		createCell_qzj(row,excelHeader);
//            aosPrint.setWb(wb);

//		HSSFWorkbook wb = aosPrint.getWb();
		OutputStream outputStream = response.getOutputStream();
		wb.write(outputStream);
		try {
			outputStream.flush();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	/**
	 * 以XLS格式移交电子条目
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "transferxls")
	public void transferxls(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		String path=request.getParameter("path");
		path=URLDecoder.decode(path,"utf-8");
		AOSReportModel aosReport = getAosPrintFromSession2(Dtos.newDto(request), session);
		String fileName = AOSUtils.isEmpty(aosReport.getFileName()) ? DEFAULT_FILE_NAME : aosReport.getFileName();
		List<?> fieldsList = aosReport.getFieldsList();
		List<Archive_tablefieldlistPO> excelHeader=aosReport.getExcelHeader();
		HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("Sheet1");  
//        HSSFRow row = sheet.createRow(0);  
        HSSFCellStyle style = wb.createCellStyle();  
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
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
        
        HSSFRow row = createRow(sheet, 0);  
        createCell(row,excelHeader);  
//            aosPrint.setWb(wb);
		
//		HSSFWorkbook wb = aosPrint.getWb();
        //输出到制定路径中
        //OutputStream outputStream = response.getOutputStream();
        File f=new File(path);
        if(!f.exists()){
        	f.mkdirs();
        }
		OutputStream outputStream = new FileOutputStream(new File(path+File.separator+fileName + ".xls"));
		wb.write(outputStream);  
		try {
			outputStream.flush();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	
	/**
	 * 以XLSX格式输出打印对象
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "xlsx")
	public void xlsx(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("application/xls");
		AOSReportModel aosReport = getAosPrintFromSession2(Dtos.newDto(request), session);
		String fileName = AOSUtils.isEmpty(aosReport.getFileName()) ? DEFAULT_FILE_NAME : aosReport.getFileName();
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName + ".xlsx", "utf-8"));
		List<?> fieldsList = aosReport.getFieldsList();
		List<Archive_tablefieldlistPO> excelHeader=aosReport.getExcelHeader();
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sheet1");  
//        HSSFRow row = sheet.createRow(0);  
        XSSFCellStyle style = wb.createCellStyle();  
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
        //给单元格赋值
        for(int i=0;i<fieldsList.size();i++){
        	XSSFRow row = sheet.createRow(i);
        	int j=0;
        	 for (String k : ((Map<String, Object>) fieldsList.get(i)).keySet()) 
             { 
             	row.createCell(j).setCellValue(((Map<String, Object>) fieldsList.get(i)).get(k) == null ? ""
     					: ((Map<String, Object>) fieldsList.get(i)).get(k).toString());
             	j++;
             } 
        }
        XSSFRow row = createRowXLSX(sheet, 0);  
        createCellXLSX(row,excelHeader);  
		OutputStream outputStream = response.getOutputStream();
		wb.write(outputStream);  
		try {
			outputStream.flush();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	/**
	 * 以XLSX格式输出打印对象
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "xlsx_qzj")
	public void xlsx_qzj(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("application/xls");
		AOSReportModel aosReport = getAosPrintFromSession2(Dtos.newDto(request), session);
		String fileName = AOSUtils.isEmpty(aosReport.getFileName()) ? DEFAULT_FILE_NAME : aosReport.getFileName();
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName + ".xlsx", "utf-8"));
		List<Map<String,Object>> fieldsList = aosReport.getLogsList();
		LinkedHashMap<String,Object> excelHeader=aosReport.getLogHeader();
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Sheet1");
//        HSSFRow row = sheet.createRow(0);
		XSSFCellStyle style = wb.createCellStyle();
		//style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
		//给单元格赋值
		for(int i=0;i<fieldsList.size();i++){
			XSSFRow row = sheet.createRow(i);
			int j=0;
			for (String k : ((Map<String, Object>) fieldsList.get(i)).keySet())
			{
				row.createCell(j).setCellValue(((Map<String, Object>) fieldsList.get(i)).get(k) == null ? ""
						: ((Map<String, Object>) fieldsList.get(i)).get(k).toString());
				j++;
			}
		}
		XSSFRow row = createRowXLSX(sheet, 0);
		createCellXLSX_qzj(row,excelHeader);
		OutputStream outputStream = response.getOutputStream();
		wb.write(outputStream);
		try {
			outputStream.flush();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	/**
	 * 以XLS2格式输出打印对象
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "xls2")
	public void xls2(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("application/xls");
		AOSReportModel aosReport = getAosPrintFromSession2(Dtos.newDto(request), session);
		//导出数据表
		String fileName = AOSUtils.isEmpty(aosReport.getFileName()) ? DEFAULT_FILE_NAME : aosReport.getFileName();
		//设置响应头
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName + ".xls", "utf-8"));
		//得到存到集合中的4个数据
		List<Map<String, Object>> linkList = aosReport.getLogsList();
		//得到第一行列头数据
		LinkedHashMap<String, Object> excelHeader=aosReport.getLogHeader();
		HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("Sheet1");  
//        HSSFRow row = sheet.createRow(0);  
        HSSFCellStyle style = wb.createCellStyle();  
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //style.setAlignment(HorizontalAlignment.CENTER);
        //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
        Iterator<Map<String, Object>> iterator = linkList.iterator();
        //给单元格赋值
        for(int i=0;i<linkList.size();i++){
        	//循环一次创建一次行
        	HSSFRow row = sheet.createRow(i);
        	int j=0;
        	Map<String, Object> map = linkList.get(i);
        	Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        	while(it.hasNext()){
        		Map.Entry<String, Object> entry = it.next();
        		String key = entry.getKey();
        		if(key.equals("id")){
        			continue;
        		}
        		row.createCell(j).setCellValue(entry.getValue()== null ? ""
     					: entry.getValue().toString());
        		j++;
        	}
        }
        //第一行单独写入列明
        HSSFRow row = createRow(sheet, 0);  
        HSSFCell cell = createCell2(row,excelHeader);  
//            aosPrint.setWb(wb);
//		HSSFWorkbook wb = aosPrint.getWb();
		OutputStream outputStream = response.getOutputStream();
		wb.write(outputStream);  
		try {
			outputStream.flush();
		} catch (Exception e) {
			throw new AOSException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	/**
	 * 导出zip格式数据
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "zip")
	public void zip(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		Dto newDto = Dtos.newDto(request);
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode("Xml数据文件打包.zip", "utf-8"));
		String strpath = newDto.getString("zipfilename");

		File file = new File(strpath);
		FileInputStream input=null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			byte[] buff=new byte[1024*10];//可以自己 指定缓冲区的大小
	        int len=0;
	        while((len=input.read(buff))>-1)
	        {
	        	outputStream.write(buff,0,len);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				outputStream.flush();
				outputStream.close();
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
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
		/**
		 * 创建要出入的行中单元格
		 * @param row
		 * @return
		 */
		private static HSSFCell createCell_qzj(HSSFRow row,LinkedHashMap<String,Object> list) {
			HSSFCell cell = row.createCell(0);
			int i=0;
			//打印出迭代的顺序
			for (Map.Entry<String, Object> entry : list.entrySet()) {
				row.createCell(i).setCellValue(entry.getValue()+"");
				i++;
			}
	//	        cell.setCellValue(999999);
	//	        row.createCell(1).setCellValue(1.2);
	//	        row.createCell(2).setCellValue("This is a string cell");
			return cell;
		}
	/**
		    * 找到需要插入的行数，并新建一个POI的row对象 
		    * @param sheet 
		    * @param rowIndex 
		    * @return 
		    */  
		    private static XSSFRow createRowXLSX(XSSFSheet sheet, Integer rowIndex) {  
		        XSSFRow row = null;  
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
		    private static XSSFCell createCellXLSX(XSSFRow row,List<Archive_tablefieldlistPO> list) {
		        XSSFCell cell = row.createCell(0);
		        for(int i=0;i<list.size();i++){
		        	row.createCell(i).setCellValue(list.get(i).getFieldcnname());
		        }
//		        cell.setCellValue(999999);
//		        row.createCell(1).setCellValue(1.2);
//		        row.createCell(2).setCellValue("This is a string cell");
		        return cell;
		    }
			/**
			 * 创建要出入的行中单元格
			 * @param row
			 * @return
			 */
			private static XSSFCell createCellXLSX_qzj(XSSFRow row,LinkedHashMap<String,Object> list) {
				XSSFCell cell = row.createCell(0);
				Iterator it = list.entrySet().iterator();
				int i=0;
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					row.createCell(i).setCellValue(entry.getValue()+"");
					i++;
				}
		//		        cell.setCellValue(999999);
		//		        row.createCell(1).setCellValue(1.2);
		//		        row.createCell(2).setCellValue("This is a string cell");
				return cell;
			}
		    /**
			 * 获取AOSPrint对象
			 * 
			 * @param inDto
			 * @param session
			 * @return
			 */
			public static AOSReportModel getAosPrintFromSession2(Dto inDto, HttpSession session) {
				String Key = AOSReport.DEFAULT_AOSPRINT_KEY;
				if (AOSUtils.isNotEmpty(inDto.getString(AOSPRINT_PARAM))) {
					Key = inDto.getString(AOSPRINT_PARAM);
				}
				AOSReportModel aosreport = (AOSReportModel) session.getAttribute(Key);
				if (aosreport == null) {
					throw new AOSException(9, Key);
				}
				return aosreport;
			}
			/** 
		     * 创建要出入的行中单元格 (日志)
		     * @param row 
		     * @return 
		     */  
		    private static HSSFCell createCell2(HSSFRow row,LinkedHashMap<String, Object> excelHeader) {  
		        HSSFCell cell = row.createCell(0);
		        Set<Entry<String,Object>> set = excelHeader.entrySet();
		        Iterator<Entry<String, Object>> it = set.iterator();
		        int i=0;
		        while(it.hasNext()){
		        	Entry<String, Object> entry = it.next();
		        	entry.getValue();
		        	row.createCell(i).setCellValue(entry.getValue().toString());
		        	i++;
		        }
//		        cell.setCellValue(999999);  
//		        row.createCell(1).setCellValue(1.2);  
//		        row.createCell(2).setCellValue("This is a string cell");  
		        return cell;  
		    }  

	    
}
