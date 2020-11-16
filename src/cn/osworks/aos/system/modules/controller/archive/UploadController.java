package cn.osworks.aos.system.modules.controller.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.archive.UploadService;
import cn.osworks.aos.system.modules.service.retrieval.RetrievalService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Archive-SHQ
 *
 * @date 2016-6-14
 */
@Controller
@RequestMapping(value ="archive/upload")
public class UploadController {
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
		Dto qDto = Dtos.newDto(request);
		boolean flag = true;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000);
		// 设置临时文件存储位置
		   Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
	       String tid=request.getParameter("tid");
	       String base= prop.getProperty("filePath")+File.separator+request.getParameter("tablename");//获取username key对应的值
		File file = new File(base+"\\"+tid);
		if(!file.exists()){
			file.mkdirs();
		}
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
				String markPath = base + File.separator+"\\"+tid;
				qDto.put("fileName",fileName );
				qDto.put("markPath", markPath);
				qDto.put("filetype", filetype);
				item.write(new File(fileName));
				_path=item.getName();
				dirname= "/"+tablename+"/"+tid+"/";
				String pathid=AOSId.uuid();
					qDto.put("pathid", pathid);
				if(SaveUploadSQL(pathid,tablename,tid,_path,dirname,filetype,fileName)){
					//添加索引
					if(qDto.getString("ocr").equals("true")){
						String tm =(String) dataService.selectOne(tid, tablename).get("tm");
						retrievalService.insertIndex(qDto);
					}
					flag=true;
				}
			}
		}
		if(flag == true){
			uploadService.updatePath(tablename, tid,dirname);
			WebCxt.write(response, "上传成功！！！");
		}else{
		response.setStatus(456);
		}
	}
	@RequestMapping(value ="uploadTemporary_fj")
	public void uploadTemporary_fj(HttpServletRequest request, HttpServletResponse response,@RequestParam MultipartFile file)throws Exception{

		boolean flag = true;
		Dto dto = Dtos.newDto(request);
		String tablename = "archive_receive";
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String id_=request.getParameter("id_");
		String path= prop.getProperty("filePath")+"//"+tablename+"//"+id_;//获取username key对应的值
		File fileO = new File(path);
		if (!fileO.exists()) {//判断文件夹是否存在
			fileO.mkdirs();//创建文件夹
		}
		String originalFilename = file.getOriginalFilename();
		File targetFile = new File(path, originalFilename);
		file.transferTo(targetFile);
		//往任务表中写入文件标记
		uploadService.updateTemporaryYjs(tablename,id_,originalFilename);
		// 设置内存缓冲区，超过后写入临时文件

		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else {
			WebCxt.write(response, "上传失败！！！");
		}
	}
	@RequestMapping(value="uploadTemporary_zl")
	@ResponseBody
	public void uploadTemporary_zl(HttpServletRequest request,HttpServletResponse response,@RequestParam MultipartFile file)throws Exception {
		boolean flag = true;
		Dto dto = Dtos.newDto(request);
		String tablename = "zlzj_n";//表名
		//死了路径
		// 设置临时文件存储位置
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String id_=request.getParameter("id_");
		//String smzph = uploadService.selectOne(tid, request.getParameter("tablename"));
		// String strzph = smzph.substring(0,smzph.lastIndexOf('-'));
		//base上传附件的文件路径
		String path= prop.getProperty("filePath")+"//"+tablename+"//"+id_;//获取username key对应的值
		File fileO = new File(path);
		if (!fileO.exists()) {//判断文件夹是否存在
			fileO.mkdirs();//创建文件夹
		}
		String originalFilename = file.getOriginalFilename();
		File targetFile = new File(path, originalFilename);
		file.transferTo(targetFile);
		//往任务表中写入文件标记
		uploadService.updateZiLiaoYjs(tablename,id_,originalFilename);
		// 设置内存缓冲区，超过后写入临时文件

		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else
			WebCxt.write(response, "上传失败！！！");
	}

	@RequestMapping(value="uploadTemporary_ziyuan")
	@ResponseBody
	public void uploadTemporary_ziyuan(HttpServletRequest request,HttpServletResponse response,@RequestParam MultipartFile file)throws Exception {
		boolean flag = true;
		Dto dto = Dtos.newDto(request);
		String tablename = "zyjs_n";//表名
		//死了路径
		// 设置临时文件存储位置
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String id_=request.getParameter("id_");
		//String smzph = uploadService.selectOne(tid, request.getParameter("tablename"));
		// String strzph = smzph.substring(0,smzph.lastIndexOf('-'));
		//base上传附件的文件路径
		String path= prop.getProperty("filePath")+"//"+tablename+"//"+id_;//获取username key对应的值
		File fileO = new File(path);
		if (!fileO.exists()) {//判断文件夹是否存在
			fileO.mkdirs();//创建文件夹
		}
		String originalFilename = file.getOriginalFilename();
		File targetFile = new File(path, originalFilename);
		file.transferTo(targetFile);
		//往任务表中写入文件标记
		uploadService.updateZiYuanJieShouYjs(tablename,id_,originalFilename);
		// 设置内存缓冲区，超过后写入临时文件

		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else
			WebCxt.write(response, "上传失败！！！");
	}

	@RequestMapping(value="uploadTemporary_entity")
	@ResponseBody
	public void uploadTemporary_entity(HttpServletRequest request,HttpServletResponse response,@RequestParam MultipartFile file)throws Exception {
		boolean flag = true;
		Dto dto = Dtos.newDto(request);
		String tablename = "stjs_n";//表名
		//死了路径
		// 设置临时文件存储位置
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String id_=request.getParameter("id_");
		//String smzph = uploadService.selectOne(tid, request.getParameter("tablename"));
		// String strzph = smzph.substring(0,smzph.lastIndexOf('-'));
		//base上传附件的文件路径
		String path= prop.getProperty("filePath")+"//"+tablename+"//"+id_;//获取username key对应的值
		File fileO = new File(path);
		if (!fileO.exists()) {//判断文件夹是否存在
			fileO.mkdirs();//创建文件夹
		}
		String originalFilename = file.getOriginalFilename();
		File targetFile = new File(path, originalFilename);
		file.transferTo(targetFile);
		//往任务表中写入文件标记
		uploadService.updateSiTiJieShouYjs(tablename,id_,originalFilename);
		// 设置内存缓冲区，超过后写入临时文件

		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else
			WebCxt.write(response, "上传失败！！！");
	}

	@RequestMapping(value ="uploadSubject_fj")
	public void uploadSubject_fj(HttpServletRequest request, HttpServletResponse response)throws Exception{

		Dto qDto = Dtos.newDto(request);
		boolean flag = true;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000);
		// 设置临时文件存储位置
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String id_=request.getParameter("id_");
		//String smzph = uploadService.selectOne(tid, request.getParameter("tablename"));
		// String strzph = smzph.substring(0,smzph.lastIndexOf('-'));
		//base上传编研附件的文件路径
		String base= prop.getProperty("filePath")+"//"+"byrw"+"//"+id_;//获取username key对应的值
		File file = new File(base);
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
		String _path =null;
		String dirname=null;
		String _s_path =null;
		String realName=null;
		//String imgurl=null;
		for (int i = 0 ;i < items.size(); i++){
			item = (FileItem) items.get(i);
			// 保存文件
			if (!item.isFormField() && item.getName().length() > 0) {
				String str = item.getName();
				item.write(new File(base+"//"+str));
				_path=item.getName();
				//添加任务附件路径
				uploadService.updateByrw(id_,str);
				flag=true;
				//uploadService.saveUploadInfo("imgurl");
			}
		}
		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else
		WebCxt.write(response, "上传失败！！！");
	}
	@RequestMapping(value ="uploadSubject_cg")
	public void uploadSubject_cg(HttpServletRequest request, HttpServletResponse response,@RequestParam MultipartFile file)throws Exception{
		// 设置临时文件存储位置
        Dto dto = Dtos.newDto(request);
        boolean flag = true;
        String filename=request.getParameter("fileId");
        String id_=request.getParameter("id_");
        Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
        //base上传编研附件的文件路径
        String base= prop.getProperty("filePath")+"/"+"bycg"+"/"+id_;//获取username key对应的值
        //base上传附件的文件路径
        File fileo = new File(base);
        if (!fileo.exists()) {//判断文件夹是否存在
            fileo.mkdirs();//创建文件夹
        }
        //保存文件
        if (!file.isEmpty()){
            String originalFilename = file.getOriginalFilename();
            File targetFile = new File(base, originalFilename);
            file.transferTo(targetFile);
        }
        //添加任务附件路径
        uploadService.updateBycg(id_,file.getOriginalFilename());
        if(flag == true){
            WebCxt.write(response, "上传成功！！！");
        }else {
            WebCxt.write(response, "上传失败！！！");
        }


    }
	@RequestMapping(value ="uploadSjfb_fbs")
	public void uploadSjfb_fbs(HttpServletRequest request, HttpServletResponse response,@RequestParam MultipartFile file)throws Exception{
		// 设置临时文件存储位置
		Dto dto = Dtos.newDto(request);
		boolean flag = true;
		String filename=request.getParameter("fileId");
		String id_=request.getParameter("id_");
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		//base上传编研附件的文件路径
		String base= prop.getProperty("filePath")+"/"+"sjfb"+"/"+id_;//获取username key对应的值
		//base上传附件的文件路径
		File fileo = new File(base);
		if (!fileo.exists()) {//判断文件夹是否存在
			fileo.mkdirs();//创建文件夹
		}
		//保存文件
		if (!file.isEmpty()){
			String originalFilename = file.getOriginalFilename();
			File targetFile = new File(base, originalFilename);
			file.transferTo(targetFile);
		}
		//添加任务附件路径
		uploadService.updateSjfb(id_,file.getOriginalFilename());
		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else {
			WebCxt.write(response, "上传失败！！！");
		}


	}
	/**
	 *
	 * SHQ
	 *
	 * 照片档案电子文件上传
	 * @throws Exception
	 */
	@RequestMapping(value ="archiveZpUpload")
	public void archiveZpUpload(HttpServletRequest request, HttpServletResponse response) throws Exception{

		Dto qDto = Dtos.newDto(request);
		System.out.print("111111111111111111111");
		boolean flag = true;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000);
		// 设置临时文件存储位置
		   Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
	       String tid=request.getParameter("tid");
	       //String smzph = uploadService.selectOne(tid, request.getParameter("tablename"));
	      // String strzph = smzph.substring(0,smzph.lastIndexOf('-'));
	       String base= prop.getProperty("filePath")+request.getParameter("tablename");//获取username key对应的值
		File file = new File(base+"\\mark");
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
		String realName=null;
		//String imgurl=null;
		for (int i = 0 ;i < items.size(); i++){
			item = (FileItem) items.get(i);
			// 保存文件
			if (!item.isFormField() && item.getName().length() > 0) {
				String str = item.getName();
				realName =item.getName();
				String filetype=AOSId.uuid()+str.substring(str.lastIndexOf("."));
				String strtype = str.substring(str.lastIndexOf('.')+1);
				fileName = base + File.separator +"\\"+ filetype;
				String strrq=realName.substring(0,8);
				String strtm = realName.substring(8,realName.lastIndexOf("-"));
				String strsyz = realName.substring(realName.lastIndexOf("-")+1,realName.lastIndexOf("."));
				//String filetype=AOSId.uuid()+str.substring(str.lastIndexOf("."));
				/*fileName = base + File.separator+"\\"+strzph +"\\"+ filetype;
				if(strtype.equals("doc")||strtype.equals("docx")){
					fileName=base + File.separator+"\\"+strzph+"\\"+str;
				}*/
				String markPath = base + File.separator+"\\";
				qDto.put("fileName",fileName );
				qDto.put("markPath", markPath);
				qDto.put("filetype", filetype);
				item.write(new File(fileName));
				_path=item.getName();
				dirname= "mark/";
				String pathid=AOSId.uuid();
				if(SaveUploadSQL(pathid,tablename,tid,_path,dirname,filetype,fileName)){
					//添加索引

					String tm =(String) dataService.selectOne(tid, tablename).get("tm");
					retrievalService.insertIndex(qDto);
					if(qDto.getString("mark").equals("true")){
						uploadService.addJpgWaterMark(qDto);
						//uploadService.markImageByText();
					}
					flag=true;
				}
				//uploadService.saveUploadInfo("imgurl");

			}
		}

		if(flag == true){
			uploadService.updateZpPath(tablename,tid,realName);
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
	public boolean SaveUploadSQL(String pathid,String tablename,String tid,String _path,String dirname,String _s_path,String imgurl){
		boolean out =true;
		try {
			Dto dto=Dtos.newDto();
			dto.put("tablename", tablename);
			String tableinfo = tablename+"_info";
			//电子文件赋予1，证明有电子文件
			String sql2="update "+tablename+" set _path='1' where id_='"+tid+"'";
			//uploadService.executeSQL(sql2);
			tablename=tablename+"_path";
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String sdatetime = dateFormat.format(now);
//			int pid=jdbcTemplate.queryForInt("select max(id_) from "+tablename);
//			pid=pid+1;
			
			
			String sql="INSERT INTO "+tablename+"(id_,tid,_path,dirname,sdatetime,_s_path)" +"VALUES('"+pathid+"','"+tid+"','"+_path+"','"+dirname+"','"+sdatetime+"','"+_s_path+"')";
			uploadService.executeSQL(sql);
			//声像档案保存元数据
			
			
			Archive_tablenamePO archive_tablenamePO = archive_tablenameMapper.selectOne(dto);


			//System.out.print(archive_tablenamePO.getTablemedia());
			
			/*if(archive_tablenamePO.getTablemedia()!=null&&archive_tablenamePO.getTablemedia()!=""){
				//uploadService.saveUploadInfo(tid,pathid,imgurl,tableinfo);
			}*/
			
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out=false;
		}  
		return out;
	}
	/**
	 * 
	 * SHQ
	 * 
	 * Excel文件上传
	 * @throws Exception 
	 */
	@RequestMapping(value ="uploadExcel")
	public void uploadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		boolean flag = true;
		
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000);
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(10002400000l);
		// 设置整个request的最大值
		upload.setSizeMax(10002400000l);
		upload.setHeaderEncoding("UTF-8");
	    List<?> items = upload.parseRequest(request);
		FileItem item = null;
		String strname = null;
		for (int i = 0 ;i < items.size(); i++){
			item = (FileItem) items.get(i);
			// 保存文件
			//fileName="D://dataaos/1.xls";
			if (!item.isFormField() && item.getName().length() > 0) {
				 strname=item.getName();
				item.write(new File(excelPath+"/"+strname));
				
			}
		}
		//String strpath = excelPath+"/"+strname;
		//List<Dto> titleDtos=readXlsTitle(strpath);
		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else
		response.setStatus(456);
	}
	/**
	 *
	 * SHQ
	 *
	 * 数据库备份文件上传
	 * @throws Exception
	 */
	@RequestMapping(value ="uploadRecord")
	public void uploadRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{

		boolean flag = true;

		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/record";
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000);
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(10002400000l);
		// 设置整个request的最大值
		upload.setSizeMax(10002400000l);
		upload.setHeaderEncoding("UTF-8");
		List<?> items = upload.parseRequest(request);
		FileItem item = null;
		String strname = null;
		for (int i = 0 ;i < items.size(); i++){
			item = (FileItem) items.get(i);
			// 保存文件
			//fileName="D://dataaos/1.xls";
			if (!item.isFormField() && item.getName().length() > 0) {
				strname=item.getName();
				item.write(new File(excelPath+"/"+strname));

			}
		}
		uploadService.saveReturnMessageData(request,response,session);
		//String strpath = excelPath+"/"+strname;
		//List<Dto> titleDtos=readXlsTitle(strpath);
		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else
			response.setStatus(456);
	}

	/**
	 * 上传条目数据
	 * @param request
	 * @param response
	 * @param file
	 * @throws Exception
	 */
	@RequestMapping(value="uploadfiles_data")
	@ResponseBody
	public void uploadfiles_data(HttpServletRequest request,HttpServletResponse response,@RequestParam MultipartFile file)throws Exception {
		boolean flag = true;
		Dto dto = Dtos.newDto(request);
		String tablename = dto.getString("tablename");
		//死了路径
		// 设置临时文件存储位置
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String id_=request.getParameter("id_");
		//String smzph = uploadService.selectOne(tid, request.getParameter("tablename"));
		// String strzph = smzph.substring(0,smzph.lastIndexOf('-'));
		//base上传附件的文件路径
		String path= prop.getProperty("filePath")+"//"+tablename+"//"+id_;//获取username key对应的值
		File fileO = new File(path);
		if (!fileO.exists()) {//判断文件夹是否存在
			fileO.mkdirs();//创建文件夹
		}
		//保存文件
		if (!file.isEmpty()){
			String originalFilename = file.getOriginalFilename();
			File targetFile = new File(path, originalFilename);
			file.transferTo(targetFile);
			//在_path表中写入信息
			uploadService.addData_path(originalFilename,tablename,id_);
		}
		uploadService.updateData_path(tablename,id_,"1");
		//往任务表中写入文件标记
		//uploadService.updateZiLiaoYjs(tablename,id_,originalFilename);
		// 设置内存缓冲区，超过后写入临时文件

		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else
			WebCxt.write(response, "上传失败！！！");
	}
	/**
	 * 上传条目数据针对当前任务的上传文件
	 * @param request
	 * @param response
	 * @param file
	 * @throws Exception
	 */
	@RequestMapping(value="uploadImport_excel")
	@ResponseBody
	public void uploadImport_excel(HttpServletRequest request,HttpServletResponse response,@RequestParam MultipartFile file)throws Exception {
		boolean flag = true;
		Dto dto = Dtos.newDto(request);
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
		//死了路径
		// 设置临时文件存储位置
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String id_=request.getParameter("id_");
		//String smzph = uploadService.selectOne(tid, request.getParameter("tablename"));
		// String strzph = smzph.substring(0,smzph.lastIndexOf('-'));
		//base上传附件的文件路径
		File fileO = new File(excelPath);
		if (!fileO.exists()) {//判断文件夹是否存在
			fileO.mkdirs();//创建文件夹
		}
		//保存文件
		if (!file.isEmpty()){
			String originalFilename = file.getOriginalFilename();
			File targetFile = new File(excelPath, originalFilename);
			file.transferTo(targetFile);
		}
		if(flag == true){
			WebCxt.write(response, "上传成功！！！");
		}else
			WebCxt.write(response, "上传失败！！！");
	}
	
	
}
