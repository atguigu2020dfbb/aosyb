package cn.osworks.aos.system.modules.controller.introduce;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.introduce.ImportService;
import cn.osworks.aos.system.modules.service.introduce.ImportTemporaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 导入cotroller
 * 
 * @author Sun
 *
 * @date 2018-8-3
 */
@Controller
@RequestMapping(value ="archive/importTemporary")
public class ImportTemporaryController {

	@Autowired
	private ImportTemporaryService importService;
	
	/**
	 * 加载EXCEL标题到grid
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("loadExcelGrid")
	public String loadExcelGrid(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
		String localFilename=qDto.getString("localFilename");
		List<Dto> titleDtos =new ArrayList<Dto>();
		if(localFilename.endsWith(".xls")){
			titleDtos =importService.readExcelTitleXls(excelPath+"/"+qDto.getString("localFilename"));
		}else if(localFilename.endsWith(".xlsx")){
			titleDtos =importService.readExcelTitleXlsx(excelPath+"/"+qDto.getString("localFilename"));
		}
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("localFilename", qDto.getString("localFilename"));
		request.setAttribute("_classtree", qDto.getString("_classtree"));
		request.setAttribute("pch", qDto.getString("pch"));
		session.setAttribute(AOSCons.USERINFOKEY,qDto.getUserInfo());
		return "aos/receive/importTemporary.jsp";
	}
	/**
	 * 加载EXCEL标题到grid
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("loadExcelGrid_backup")

	public String loadExcelGrid_backup(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		String listtablename=qDto.getString("listtablename");
		String tablenamedesc=qDto.getString("tablenamedesc");
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
		String localFilename=qDto.getString("localFilename");
		List<Dto> titleDtos =new ArrayList<Dto>();
		if(localFilename.endsWith(".xls")){
			titleDtos =importService.readExcelTitleXls(excelPath+"/"+qDto.getString("localFilename"));
		}else if(localFilename.endsWith(".xlsx")){
			titleDtos =importService.readExcelTitleXlsx(excelPath+"/"+qDto.getString("localFilename"));
		}
		request.setAttribute("fieldDtos", titleDtos);
		request.setAttribute("localFilename", qDto.getString("localFilename"));
		request.setAttribute("listtablename", listtablename);
		request.setAttribute("tablenamedesc", tablenamedesc);
		request.setAttribute("_classtree", qDto.getString("_classtree"));
		return "aos/personbase2/import.jsp";
	}


	/**
	 * 
	 * 查询excel数据信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getExcel")
	public void getExcel(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto=Dtos.newDto(request);
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
		String name = excelPath+"/"+qDto.getString("localFilename");
		/*if(qDto.getString("localFilename").equals("")){
			return;
		}*/
		List<Dto> list=new ArrayList<Dto>();
		//判断结尾类型
		if(name.endsWith(".xls")){
			list =importService.readXls(name);
		}else if(name.endsWith(".xlsx")){
			list =importService.readXlsx(name);
		}
		session.setAttribute(AOSCons.USERINFOKEY,qDto.getUserInfo());
		String outString = AOSJson.toGridJson(list);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	
	/**
	 * 
	 * 源字段和目标字段查询
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getSourceField")
	public void getSourceField(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
		String name = excelPath+"/"+qDto.getString("localFilename");
		List<Dto> titleDtos =importService.getTargetField(name,qDto);
		String outString = AOSJson.toGridJson(titleDtos);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	
	/**
	 * 
	 * 下拉列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listComboBox")
	public void listComboBox(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> list = importService.listComboBox(qDto.getString("tablename"));
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	
	/**
	 * 
	 * excel数据导入
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="insertData")
	public void insertData(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = importService.InsertDb(inDto);
		String outString = AOSJson.toJson(outDto);
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * excel数据导入
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="insertData_depot")
	public void insertData_depot(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = importService.InsertDb_depot(inDto);
		String outString = AOSJson.toJson(outDto);
		WebCxt.write(response, outString);
	}
	/**
	 * 选择表
	 *
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-20
	 */
	@RequestMapping(value="listZLKTablename")
	public void listZLKTablename(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		List<Archive_tablenamePO> list = importService.findZLKTablename();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
}