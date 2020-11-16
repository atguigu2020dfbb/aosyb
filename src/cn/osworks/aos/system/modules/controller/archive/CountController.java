package cn.osworks.aos.system.modules.controller.archive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Aos_sys_orgPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.modules.service.archive.CountService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;

/**
 * 年度档案统计汇总
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "archive/count")
public class CountController extends JdbcDaoSupport {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public CountService countService;
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}
	/**
	 * 汇总页面跳转
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping("initData")
	public String initData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		return "aos/archive/count/math.html";
	}
	/**
	 * 查询制作页面
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("htmlcount")
	public String htmlcount(HttpServletRequest request,
			HttpServletResponse response, HttpSession session){
		return "aos/archive/count/math.html";
	}
	//预览跳传窗口
	@RequestMapping("look")
	public String look(HttpServletRequest request,
			HttpServletResponse response, HttpSession session){
		return "aos/archive/count/math.html";
	}
}
