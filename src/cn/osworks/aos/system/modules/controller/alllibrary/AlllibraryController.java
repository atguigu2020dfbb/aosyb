package cn.osworks.aos.system.modules.controller.alllibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.modules.service.alllibrary.AlllibraryService;
/**
 * 全库检索
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="alllibrary/alllibrary")
public class AlllibraryController {
	@Autowired
	private AlllibraryService alllibraryService;
	
	/**
	 * 
	 * 页面初始化
	 * 
	 * @return
	 */
	@RequestMapping(value="initData")
	public String initData(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		//数据表所有字段检索，并且不要重复的,把不重复的列表展示出来
		List<Archive_tablefieldlistPO> titleDtos =alllibraryService.getDataFieldListTitle();
		request.setAttribute("fieldDtos", titleDtos);
		return "aos/alllibrary/alllibrary.jsp";
	}
	/**
	 * 
	 * 查询数据列表
	 * 
	 * @return
	 */
	@RequestMapping(value="getDataList")
	public void getDataList(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		String limit=dto.getString("limit");
		String page=dto.getString("page");
		List<Map<String,Object>> list = alllibraryService.getDataSjbList(dto);
		int pCount = list.size();

		list=GetRange(list,limit,page,pCount);

		String outString = AOSJson.toGridJson(list, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}

	private List<Map<String, Object>> GetRange(List<Map<String, Object>> fieldDtos, String limit, String page, int pCount) {
		//计算出索引位置
		Integer startindex=(Integer.valueOf(page)-1)*Integer.valueOf(limit);
		Integer endindex=0;
		//计算查询范围内最有一个坐标索引
		if(pCount-startindex<=Integer.valueOf(limit)){
			//证明数据不足下一次查询的最大索引值
			endindex=pCount-startindex+startindex;
		}else if(pCount-startindex>Integer.valueOf(limit)){
			endindex=Integer.valueOf(limit)+startindex;
		}
		List<Map<String, Object>> fieldDtos2=new ArrayList<Map<String, Object>>();
		for(int i=startindex;i<endindex;i++){
			fieldDtos2.add(fieldDtos.get(i));
		}
		return fieldDtos2;
	}
	/**
	 * 得到电子文件列表
	 * @param request
	 * @param session
	 * @param response
	 */
	@RequestMapping(value="getPath")
	public void getPath(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		List<Map<String,Object>> list = alllibraryService.getPath(dto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
}
