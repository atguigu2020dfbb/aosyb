package cn.osworks.aos.system.modules.controller.record;
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
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.record.RecordsService;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.service.archive.DataService;

/**
 * 
 * 数据备份与恢复
 * 
 * @author shq
 * 
 * @date 2016-9-14
 */
@Controller
@RequestMapping(value = "archive/record")
public class RecordController extends JdbcDaoSupport{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	public RecordsService recordsService;
	//盘符路径
	public static String url="";
	public static final JFileChooser fc = new JFileChooser();
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}
	//静态代码块读取配置文件
	static {
		try {
			Properties props = new Properties();
			InputStream in = DataService.class
					.getResourceAsStream("/jdbc.properties");
			props.load(in);
			url = props.getProperty("jdbc.url");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * 
 * 查档系统设置页面初始化
 * 
 * @param request
 * @param response
 * @return
 * @throws UnknownHostException 
 */
@RequestMapping(value = "initData")
public String initData(HttpServletRequest request,
		HttpServletResponse response, HttpSession session) throws Exception {
		//String baddr = request.getRemoteAddr();
		//String faddr = request.getLocalAddr();
		//此时如果值相同证明是服务器机器操作，可以正常向下执行，并跳转页面
		return "aos/record/record.jsp";
	}
	/**
	 *
	 * 查档系统设置页面初始化
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "backup_initData")
	public String backup_initData(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session) throws Exception {
		//String baddr = request.getRemoteAddr();
		//String faddr = request.getLocalAddr();
		//此时如果值相同证明是服务器机器操作，可以正常向下执行，并跳转页面
		return "aos/record/backup_record.jsp";
	}
	/**
	 *
	 * 查档系统设置页面初始化
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "return_initData")
	public String return_initData(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session) throws Exception {
		//String baddr = request.getRemoteAddr();
		//String faddr = request.getLocalAddr();
		//此时如果值相同证明是服务器机器操作，可以正常向下执行，并跳转页面
		return "aos/record/return_record.jsp";
	}
/**
 * 数据库备份
 * @param request
 * @param response
 * @param session
 */
@RequestMapping(value = "backupData")
public void backupData11(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto out=Dtos.newDto();
		String filename =  new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date().getTime());
		Properties prop=null;
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String base= prop.getProperty("filePath")+"/backup";
		//System.out.println(time);
			//当前时间毫秒值
			//long filename=System.currentTimeMillis();
			String database=url.split("=")[1];
	        String selectsql = "backup database " + database + " to disk='" + base+"/"+ filename+"' with format,name='123'";
	        jdbcTemplate.execute(selectsql);
	        out.setAppCode(1);
	        out.put("filepath", base+"/"+ filename);
	        out.put("name", filename);
	    saveBackupMessageData(request,response,session);
		WebCxt.write(response, AOSJson.toJson(out));
}
	@RequestMapping(value = "listbackup")
	public void listbackup(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		String sql="select * from  archive_backup_login";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	@RequestMapping(value = "listreturn")
	public void listreturn(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		String sql="select * from  archive_return_login";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
//保存操作信息
@RequestMapping(value = "saveBackupMessageData")
public boolean saveBackupMessageData(HttpServletRequest request,HttpServletResponse response,HttpSession session){
	Dto inDto = Dtos.newDto(request);
	try{
		UserInfoVO userInfoVO=(UserInfoVO)session.getAttribute("_userInfoVO");
		Date date=new Date();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql="insert into archive_backup_login(id_,operator_time,operator_en_person,operator_cn_person,operator_name) values('"+ AOSId.uuid()+"','"+simpleDateFormat.format(date)+"','"+userInfoVO.getName_()+"','"+userInfoVO.getAccount_()+"','备份数据库')";
		jdbcTemplate.execute(sql);
		return true;
	}catch (Exception e){
		e.printStackTrace();
		return false;
	}
}
@RequestMapping("deleteBack")
public void deleteBack(HttpServletRequest request,HttpServletResponse response){
	Dto qDto = Dtos.newDto(request);
	Dto outDto = Dtos.newDto();
	String[] selections = qDto.getRows();
	int del = 0;
	for (String id_ : selections) {
		jdbcTemplate.execute(" delete from archive_backup_login where id_='"
				+ id_ + "'");
		del++;
	}
	String msg = "操作完成，";
	if (del > 0) {
		msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
	}
	outDto.setAppMsg(msg);
	WebCxt.write(response, AOSJson.toJson(outDto));

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
	response.setContentType("application/xls");
	response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(inDto.getString("name"), "utf-8"));
	//好了 ，现在通过IO流来传送数据
	String strpath = inDto.getString("filepath");

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
		byte[]buff=new byte[1024*10];//可以自己 指定缓冲区的大小
        int len=0;
        while((len=input.read(buff))>-1)
        {
        	outputStream.write(buff,0,len);
        }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		outputStream.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		outputStream.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	@RequestMapping("loadRecordGrid")
	public void loadRecordGrid(HttpServletRequest request,
							   HttpServletResponse response, HttpSession session){
		Dto dto=Dtos.newDto(request);
		Dto out = recordsService.restoreData2(dto,response,request);
		WebCxt.write(response, AOSJson.toJson(out));
	}
}