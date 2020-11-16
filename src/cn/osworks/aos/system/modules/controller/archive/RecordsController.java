package cn.osworks.aos.system.modules.controller.archive;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import cn.osworks.aos.system.modules.service.archive.Records1Service;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.service.archive.DocService;

/**
 * 
 * 数据备份与恢复
 * 
 * @author shq
 * 
 * @date 2016-9-14
 */
@Controller
@RequestMapping("archive/records")
public class RecordsController{
	@Autowired
	public Records1Service recordsService;
/**
 * 
 * 查档系统设置页面初始化
 * 
 * @param request
 * @param response
 * @return
 */
@RequestMapping(value = "initData")
public String initData(HttpServletRequest request,
		HttpServletResponse response, HttpSession session) throws Exception {
		//String baddr = request.getRemoteAddr();
		//String faddr = request.getLocalAddr();
		//清空指定路径里面的备份文件
	try{
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/record";
		//判断这个路径是否有内容，有的话就删除
		File myFilePath = new File(excelPath);
		//文件或目录是否存在
		 if(myFilePath.exists()){
			   deleteFile(myFilePath);
		}
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
		//此时如果值相同证明是服务器机器操作，可以正常向下执行，并跳转页面
	request.setAttribute("aos_module_id_",Dtos.newDto(request).getString("aos_module_id_"));
		return "aos/record/record.jsp";
	}
private void deleteFile(File myFilePath) {
	// TODO Auto-generated method stub
	 if(myFilePath.isDirectory()){
		           File[] files = myFilePath.listFiles();
		            for(int i=0; i<files.length; i++){
		                new File(files[i].toString()).delete();
		            }
		       }
	 //myFilePath.delete();
}
/**
 * 数据库备份
 * @param request
 * @param response
 * @param session
 */
@RequestMapping(value = "backupData")
public void backupData11(HttpServletRequest request,HttpServletResponse response){
	Dto dto = recordsService.backupData11();	
	WebCxt.write(response, AOSJson.toJson(dto));
}
/**
 * 数据库备份
 * @param request
 * @param response
 * @param session
 * @throws UnsupportedEncodingException 
 * @throws IOException 
 */
@RequestMapping(value = "download")
public void backupData(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
	Dto inDto = Dtos.newDto(request);
	recordsService.backupData(inDto,response);
}

/**
 * 数据库还原
 * @param request
 * @param response
 * @param session
 */
@RequestMapping("restoreData")
public void restoreData(HttpServletRequest request,
		HttpServletResponse response, HttpSession session){
	Dto out = recordsService.restoreData(response,request,session);
	WebCxt.write(response, AOSJson.toJson(out));
}
@RequestMapping("loadRecordGrid")
public void loadRecordGrid(HttpServletRequest request,
		HttpServletResponse response, HttpSession session){
	Dto dto=Dtos.newDto(request);
	Dto out = recordsService.restoreData2(dto,response,request);
	WebCxt.write(response, AOSJson.toJson(out));
	}
}