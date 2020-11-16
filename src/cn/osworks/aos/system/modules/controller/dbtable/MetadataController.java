package cn.osworks.aos.system.modules.controller.dbtable;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tableInfoMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;
import cn.osworks.aos.system.dao.po.Archive_tableInfoPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.modules.service.dbtable.DbtableService;



/**
 * 
 * 元数据信息设定
 * 
 * @author Sun
 *
 * 2018-8-10
 */
@Controller
@RequestMapping(value = "system/metadata")
public class MetadataController {
	
	@Autowired
	private Archive_tableInfoMapper archive_tableInfoMapper;
	
	@Autowired
	private DbtableService dbtableService;
	@Autowired
	private SqlDao sysDao;
	/**
	 * 
	 * 
	 * @author Sun
	 * @return
	 *
	 * 2018-8-10
	 */
	@RequestMapping(value="initMetadata")
	public String initMetadata(){
		
		return "aos/dbtable/metadata.jsp";
	}
	
	/**
	 * 
	 * 查询元数据表信息
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2018-8-10
	 */
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		List<Archive_tableInfoPO> list = archive_tableInfoMapper.listPage(inDto);
		String outString =AOSJson.toGridJson(list,inDto.getPageTotal());
		WebCxt.write(response, outString);
	}
	/**
	 * 
	 * 查询设置顺序信息
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2018-8-10
	 */
	@RequestMapping(value="listOrderInfos")
	public void listOrderInfos(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> list=dbtableService.listOrderInfos(qDto);
		String outString = AOSJson.toGridJson(list,list.size());
		WebCxt.write(response, outString);
	}
	
	/**
	 * 
	 * 查询元数据表信息
	 * 
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2018-8-10
	 */
	@RequestMapping(value="listFieldInfos")
	public void listFieldInfos(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> list=sysDao.list("dbtable.listTablefield", qDto);
		String outString = AOSJson.toGridJson(list,list.size());
		WebCxt.write(response, outString);
	}
}
