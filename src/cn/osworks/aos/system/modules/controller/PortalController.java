package cn.osworks.aos.system.modules.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.asset.DicCons;
import cn.osworks.aos.system.dao.mapper.Aos_sys_notificationMapper;
import cn.osworks.aos.system.dao.po.Aos_sys_notificationPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.IndexService;
import cn.osworks.aos.system.modules.service.SystemService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.sf.json.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;

import java.util.*;


/**
 * <b>欢迎页控制器</b>
 * 
 * @author OSWorks-XC
 * @date 2014-10-13
 */
@Controller
@RequestMapping(value = "system/portal")
public class PortalController {
	
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private SystemService systemService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private Aos_sys_notificationMapper aos_sys_notificationMapper;
	@Autowired
	private IndexController indexController;
	/**
	 * 页面初始化
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "init")
	public String init(HttpSession session, HttpServletRequest request) {
		request.setAttribute("curSkin", WebCxt.getCfgByUser(session, "skin_"));
		Dto inDto = Dtos.newDto(request);
		// 构造West区域的系统导航
		//此时只要第一个了
		List<Dto> cardDtos = indexService.getLeftNavCradListxz(inDto);
		String cardString=inDto.getString("cardDtostop");
		request.setAttribute("cardDtostop", cardDtos);
		List<Map<String,Object>> declareds =indexService.getListDeclared();
		request.setAttribute("declareds", declareds);
		// 第一个卡片标识字段
		String first_card_id_ = "";
		if (cardDtos.size() >= 1) {
			first_card_id_ = "0b218509ae63491896289ce7c4173a41";
			request.setAttribute("first_card_id_", first_card_id_);
		}
		//直接写死
		Dto navDto=Dtos.newDto();
		navDto.put("nav_button_style","tight");
		navDto.put("right_button_style","button-small button-pill button-small-neptune");
		navDto.put("is_show_top_nav_",true);
		navDto.put("nav_bar_style","button button-pill button-border-neptune");
		navDto.put("nav_bar_style_visited","button button-pill button-border-neptune button-border-neptune-visited");
		request.setAttribute("navDto", navDto);
		return "aos/portal/portal.jsp";
	}
	@RequestMapping(value = "listPortalNotice")
	public void listPortalNotice(HttpServletResponse response, HttpServletRequest request, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		List<Map<String,Object>> declareds =indexService.listPortalNotice(inDto);
		WebCxt.write(response, AOSJson.toJson(declareds));
	}
	/**
	 * 获取系统导航菜单树
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getModuleTree")
	public void getModuleTree(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
		//超级用户不做任何限制
		if (StringUtils.equals(DicCons.USER_TYPE_SUPER, userInfoVO.getType_())) {
			String jsonString = indexService.getModuleTree(inDto).getStringA();
			WebCxt.write(response, jsonString);
		}else{
			String jsonString = indexService.getModuleTree2(inDto).getStringA();
			WebCxt.write(response, jsonString);
		}



	}
	/**
	 * 获取系统导航菜单树
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listFinish")
	public void listFinish(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = qDto.getUserInfo();
		qDto.put("username",userInfoVO.getAccount_());
		if(userInfoVO.getAccount_()!=null&&"root".equals(userInfoVO.getAccount_())){
			List<Aos_sys_notificationPO> list =  aos_sys_notificationMapper.selectFinish2(qDto);
			String outString = AOSJson.toJson(list);
			WebCxt.write(response,outString);
		}else{
			List<Aos_sys_notificationPO> list =  aos_sys_notificationMapper.selectFinish(qDto);
			String outString = AOSJson.toJson(list);
			WebCxt.write(response,outString);
		}
	}
}
