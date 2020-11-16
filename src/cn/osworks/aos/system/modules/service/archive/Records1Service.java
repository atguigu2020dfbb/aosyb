package cn.osworks.aos.system.modules.service.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JFileChooser;

import cn.osworks.aos.system.modules.service.archive.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
@Service
public class Records1Service extends JdbcDaoSupport{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}
	
	public Dto backupData11() {
		// TODO Auto-generated method stub
		Dto out=Dtos.newDto();
		String filename =  new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date().getTime());
		Properties prop=null;
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties prop2=null;
		try {
			prop2 = PropertiesLoaderUtils.loadAllProperties("sjb.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String base= prop.getProperty("filePath")+"/backup";
		//判断路径是否存在。不存在创建一个
		File file=new File(base);
		if (file.exists()) {
	            if (file.isDirectory()) {
	            } else {
	             }
	         } else {
	             file.mkdir();
	        }
		//System.out.println(time);
			//当前时间毫秒值
			//long filename=System.currentTimeMillis();
			String database=prop2.getProperty("sjk");
	        String selectsql = "backup database " + database + " to disk='" + base+"/"+ filename+"' with format,name='123'";
	        jdbcTemplate.execute(selectsql);
	        out.setAppCode(1);
	        out.put("filepath", base+"/"+ filename);
	        out.put("name", filename);
	        return out;
	}

	public void backupData(Dto inDto, HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
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
			byte[] buff=new byte[1024*10];//可以自己 指定缓冲区的大小
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

	public Dto restoreData(HttpServletResponse response, HttpServletRequest request,HttpSession session) {
		// TODO Auto-generated method stub
		Dto out=Dtos.newDto();
		String baddr = request.getRemoteAddr();
		String faddr = request.getLocalAddr();
		//判断是不是服务器机器还原数据库
		if(!baddr.equals(faddr)){
			out.setAppCode(-2);
			WebCxt.write(response, AOSJson.toJson(out));
			return out;
		}
		/*UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		String account_ = userInfoVO.getAccount_();
		if(!account_.equals("jxsrd")){
			out.setAppCode(-2);
			WebCxt.write(response, AOSJson.toJson(out));
			return out;
		}*/
		Properties prop=null;
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String restorefile = getFile();
			if(restorefile==null||restorefile==""){
				return out;
			}
			Properties prop2=null;
			try {
				prop2 = PropertiesLoaderUtils.loadAllProperties("sjb.properties");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String database=prop2.getProperty("sjk");
			String sql2 = "ALTER DATABASE " + database + "  SET OFFLINE  WITH ROLLBACK IMMEDIATE";
		    String sql = "use master restore database " + database + " from disk='" + restorefile+ "' with replace";
		    String sql3 = "ALTER DATABASE " + database + "  SET ONLINE WITH ROLLBACK IMMEDIATE";
		    jdbcTemplate.execute(sql2);
		    jdbcTemplate.execute(sql);
		    jdbcTemplate.execute(sql3);
		    out.setAppCode(1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			out.setAppCode(-1);
		}
		return out;
	}
	public Dto restoreData2(Dto dto, HttpServletResponse response, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Dto out=Dtos.newDto();
		String baddr = request.getRemoteAddr();
		String faddr = request.getLocalAddr();
		//判断是不是服务器机器还原数据库
		if(!baddr.equals(faddr)){
			out.setAppCode(-2);
			WebCxt.write(response, AOSJson.toJson(out));
			return out;
		}
		Properties prop=null;
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
			String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/record";
			
			String restorefile = excelPath+File.separator+dto.getString("localFilename");
			if(restorefile==null||restorefile==""){
				return out;
			}
			Properties prop2=null;
			try {
				prop2 = PropertiesLoaderUtils.loadAllProperties("sjb.properties");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String database=prop2.getProperty("sjk");
			String sql2 = "ALTER DATABASE " + database + "  SET OFFLINE  WITH ROLLBACK IMMEDIATE";
		    String sql = "use master restore database " + database + " from disk='" + restorefile+ "' with replace";
		    String sql3 = "ALTER DATABASE " + database + "  SET ONLINE WITH ROLLBACK IMMEDIATE";
		    jdbcTemplate.execute(sql2);
		    jdbcTemplate.execute(sql);
		    jdbcTemplate.execute(sql3);
		    out.setAppCode(1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			out.setAppCode(-1);
		}
		return out;
	}
	/**
	 * 选择文件
	 * @return
	 */
	public String getFile(){
		JFileChooser fileChooser = new JFileChooser("D:\\");
		int returnVal=fileChooser.showOpenDialog(fileChooser);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		       return fileChooser.getSelectedFile().getAbsolutePath();
		    }
		return null;
	}
	/**
	 * 选择文件夹路径
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public String backupfilepathData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session){
		JFileChooser fileChooser = new JFileChooser("D:\\"); 

		 fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 

		 int returnVal = fileChooser.showOpenDialog(fileChooser); 

		 if(returnVal == JFileChooser.APPROVE_OPTION)

		{ String filePath= fileChooser.getSelectedFile().getAbsolutePath();//这个就是你选择的文件夹的
			return filePath;
		}
		 return null;
	}

}
