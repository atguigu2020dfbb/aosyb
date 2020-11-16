package cn.osworks.aos.system.modules.controller.archive;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.UploadService;
import cn.osworks.aos.system.modules.service.retrieval.RetrievalService;

/**
 * 
 * @author Archive-SHQ
 *
 * @date 2016-6-14
 */

public class CopyOfUploadController {
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	
	@Autowired
	private RetrievalService retrievalService;
	
	@Autowired
	private DataService dataService;
	
	/**
	 * 
	 * SHQ
	 * 
	 * 电子文件上传
	 * @throws Exception 
	 */
	@RequestMapping(value ="archiveUpload")
	public void archiveUpload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		boolean flag = true;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000);
		// 设置临时文件存储位置
		   Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
	       String tid=request.getParameter("tid");
	       String base= prop.getProperty("filePath")+request.getParameter("tablename");//获取username key对应的值 
		File file = new File(base+"\\"+tid);
		if(!file.exists())
			file.mkdirs();
		factory.setRepository(file);
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(10002400000l);
		// 设置整个request的最大值
		upload.setSizeMax(10002400000l);
		upload.setHeaderEncoding("UTF-8");
	    List<?> items = upload.parseRequest(request);
		FileItem item = null;
		String fileName = null;
		
		
		String tablename=request.getParameter("tablename");
		String _path =null;
		String dirname=null;
		String _s_path =null;
		//String imgurl=null;
		for (int i = 0 ;i < items.size(); i++){
			item = (FileItem) items.get(i);
			// 保存文件
			if (!item.isFormField() && item.getName().length() > 0) {
				String str = item.getName();
				String filetype=AOSId.uuid()+str.substring(str.lastIndexOf("."));
				fileName = base + File.separator+"\\"+tid +"\\"+ filetype;
				item.write(new File(fileName));
				_path=item.getName();
				dirname= tid+"\\";
				//_s_path=item.getName();
				if(SaveUploadSQL(tablename,tid,_path,dirname,filetype,fileName)){
					//添加索引
					//String tm =(String) dataService.selectOne(tid, tablename).get("tm");
					//retrievalService.insertIndex(fileName,tablename,tid,tm);
					flag=true;
				}
				//uploadService.saveUploadInfo("imgurl");
				
			}
		}
		
		if(flag == true){
		//	uploadService.updatePath(tablename, tid);
			WebCxt.write(response, "上传成功！！！");
		}else
		response.setStatus(456);
	}
	@RequestMapping( value ="listDocuments")
	public void listDocuments(HttpServletRequest request, HttpServletResponse response){
		System.out.print("9999999999999999999999999999999999999");
	}
	
	/**
	 * 保存文件SQL
	 * 
	 * @return
	 */
	public boolean SaveUploadSQL(String tablename,String tid,String _path,String dirname,String _s_path,String imgurl){
		boolean out =true;
		try {
			Dto dto=Dtos.newDto();
			dto.put("tablename", tablename);
			String tableinfo = tablename+"_info";
			tablename=tablename+"_path";
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String sdatetime = dateFormat.format(now);
//			int pid=jdbcTemplate.queryForInt("select max(id_) from "+tablename);
//			pid=pid+1;
			String pid=AOSId.uuid();
			
			String sql="INSERT INTO "+tablename+"(id_,tid,_path,dirname,sdatetime,_s_path)" +"VALUES('"+pid+"','"+tid+"','"+_path+"','"+dirname+"','"+sdatetime+"','"+_s_path+"')";
			uploadService.executeSQL(sql);
			//声像档案保存元数据
			
			
			Archive_tablenamePO archive_tablenamePO = archive_tablenameMapper.selectOne(dto);
			
			//System.out.print(archive_tablenamePO.getTablemedia());
			
			if(archive_tablenamePO.getTablemedia()!=null&&archive_tablenamePO.getTablemedia()!=""){
				uploadService.saveUploadInfo(tid,pid,imgurl,tableinfo);
			}
			
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out=false;
		}  
		return out;
	}
}
