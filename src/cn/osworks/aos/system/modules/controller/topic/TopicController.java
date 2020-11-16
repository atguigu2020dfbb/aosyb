package cn.osworks.aos.system.modules.controller.topic;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_topicPO;
import cn.osworks.aos.system.modules.service.checkup.CheckupService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.topic.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 档案利用
 * 
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="topic/topic")
public class TopicController {
	@Autowired
	private CheckupService checkupService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private DataService dataService;
	@Autowired
	private SqlDao sysDao;
	/**
	 * 页面初始化
	 * 
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 *
	 * 2018-8-23
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("initData")
	public String initData(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto dto=Dtos.newDto(request);
		String id_=dto.getString("id_");
		String name_=dto.getString("name_");
		String tablename="";
		List<Map<String,Object>> list=topicService.findTablename(id_);
		if(list!=null&&list.size()>0){
			tablename=(String)list.get(0).get("tablename");
		}
		if(tablename==null||tablename.equals("null")||tablename==""){
			request.setAttribute("id_",id_);
		}else{
			List<Archive_tablefieldlistPO> titleDtos = topicService.getDataFieldListTitle(tablename);
			request.setAttribute("fieldDtos",titleDtos);
			request.setAttribute("id_",id_);
		}

		return "aos/compilation/topic.jsp?time="+new Date().getTime();
	}
	/**
	 * 
	 * 下拉列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listComboBoxid")
	public void listComboBoxid(HttpServletRequest request,HttpServletResponse response){
		Dto dto = Dtos.newDto(request);
		List<Dto> list = sysDao.list("Resource.listComboBoxid", dto);
		String outString = AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}

	/**
	 * 
	 * 查询字段下拉框
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="queryFields")
	public void queryFields(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> titleDtos = topicService.getDataFieldListTitle(qDto.getString("tablename"));
		String gridJson = AOSJson.toGridJson(titleDtos);
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}
	/**
	 * 显示数据
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		String id_=inDto.getString("id_");
		String tablename="";
		String query="";
		String queryquery="";
		List<Map<String,Object>> list=topicService.findTablename(id_);
		if(list.size()>0){
			tablename=(String)list.get(0).get("tablename");
			query=(String)list.get(0).get("query");
			queryquery=inDto.getString("queryquery");
			List<Dto> fieldDtos =new ArrayList<Dto>();
			int pCount =0;
			if(tablename==null||tablename.equals("null")||tablename==""){
			}else{
				if(("true").equals(queryquery)){
					//桥套查询
					String query2 = topicService.queryConditions2(inDto);
					inDto.put("query",query+" "+query2);
					inDto.put("tablename",tablename);
					fieldDtos = topicService.getDataFieldListDisplayAll(inDto,session);
					pCount = topicService.getPageTotal2(inDto);
					request.setAttribute("_total", pCount);
				}else{
					inDto.put("query",query);
					inDto.put("tablename",tablename);
					fieldDtos = topicService.getDataFieldListDisplayAll(inDto,session);
					pCount = topicService.getPageTotal(inDto);
					request.setAttribute("_total", pCount);
				}
				//条目数据
				String outString = AOSJson.toGridJson(fieldDtos, pCount);
				// 就这样返回转换为Json后返回界面就可以了。
				WebCxt.write(response, outString);
			}
		}
	}
	/**
	 * 显示数据
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="listAccounts2")
	public void listAccounts2(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		String id_=inDto.getString("id_");
		String tablename=topicService.getTablename(id_);
		inDto.put("tablename",tablename);
		List<Archive_tablefieldlistPO> titleDtos = topicService.getDataFieldListTitle(tablename);
		List<Dto> fieldDtos = topicService.getDataFieldListDisplayAll2(inDto,session);
		int pCount = topicService.getPageTotal(inDto);
		request.setAttribute("_total", pCount);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 得到当前的查询套件
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="getQuery")
	public void getQuery(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		String query = topicService.queryConditions2(inDto);
		Dto dto=Dtos.newDto();
		dto.setAppMsg(query);
		WebCxt.write(response, AOSJson.toJson(dto));
	}
	/**
	 * 得到当前的查询条件的条件
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="getQueryQuery")
	public void getQueryQuery(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		String query = topicService.queryConditions2(inDto);
		Dto dto=Dtos.newDto();
		dto.setAppMsg(query);
		WebCxt.write(response, AOSJson.toJson(dto));
	}
	/**
	 * 得到当前的查询套件
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="addTopicTableFieldList")
	public void addTopicTableFieldList(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		boolean b=topicService.addTopicTableFieldList(inDto);
		Dto dto=Dtos.newDto();
		if(b){
			dto.setAppCode(1);
		}else{
			dto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(dto));


	}

	/**
	 * 通过id获得专题
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="getTopicById")
	public void getTopicById(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		Archive_topicPO dto=topicService.getTopicById(inDto.getString("id_"));
		WebCxt.write(response, AOSJson.toJson(dto));
	}
	/**
	 * 通过id获得专题
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="updateTopic")
	public void updateTopic(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		boolean b=topicService.updateTopic(inDto);
		Dto dto=Dtos.newDto();
		if(b){
			dto.setAppCode(1);
		}else{
			dto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(dto));
	}

	/**
	 * 通过id删除专题
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value="deleteTopic")
	public void deleteTopic(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表
		boolean b=topicService.deleteTopic(inDto.getString("id_"));
		Dto dto=Dtos.newDto();
		if(b){
			dto.setAppCode(1);
		}else{
			dto.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(dto));
	}
	/**
	 *
	 *
	 *
	 * @author Sun
	 * @param request
	 * @param response
	 *
	 * 2019-5-3
	 */
	@RequestMapping(value="getTableTitle")
	public String getTableTitle(HttpServletRequest request,HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		String id_=qDto.getString("id_");
		String topic_id_=qDto.getString("topic_id_");
		String tablename=topicService.getTablename(id_);
		List<Archive_tablefieldlistPO> titleDtos = topicService.getDataFieldListTitle(tablename);
		request.setAttribute("fieldDtos",titleDtos);
		request.setAttribute("topic_id_",topic_id_);
		request.setAttribute("id_",id_);
		request.setAttribute("tablename",tablename);
		return "aos/compilation/topic_archive.jsp?time="+new Date().getTime();

	}
	/**
	 * 保存专题
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="savetopic")
	public void savetopic(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto dto=Dtos.newDto();
		int b=	topicService.savetopic(inDto);
		if(b==1){
			dto.setAppCode(1);
		}else if(b==-1){
			dto.setAppCode(-1);
		}else if(b==-2){
			dto.setAppCode(-2);
		}
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, AOSJson.toJson(dto));
	}
	/**
	 * 得到列名列表
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="getQueryTitle")
	public void getQueryTitle(HttpServletRequest request,HttpServletResponse response,HttpSession session){

		Dto inDto = Dtos.newDto(request);
		List<Archive_tablefieldlistPO> list=topicService.getQueryTitle(inDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 得到门类名称和描述
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="gettable")
	public void gettable(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto dto=topicService.gettable(inDto);
		WebCxt.write(response, AOSJson.toJson(dto));
	}
	/**
	 * 得到门类名称和描述
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="compilationForm")
	public void compilationForm(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		String b=topicService.compilationForm(inDto);
		if(b.length()==0||b==""){
			out.setAppCode(-1);
			out.setAppMsg(b);
		}else{
			out.setAppCode(1);
			out.setAppMsg(b);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
}
