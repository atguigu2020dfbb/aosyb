package cn.osworks.aos.system.modules.controller.compilation;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.Archive_write_topicMapper;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.compilation.CompilationService;
import cn.osworks.aos.system.modules.service.compilation.ExamineService;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;

/**
 * 档案编研
 * 
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="compilation/articles")
public class CompilationsController {
	@Autowired
	private CompilationService compilationService;
	@Autowired
	private DataService dataService;
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private ExamineService examineService;
	/**
	 * 在编课题页面
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * 2018-8-23
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("subject")
	public String subject(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto out=Dtos.newDto(request);
		String first=out.getString("first");
		String second=out.getString("second");
		String three=out.getString("three");
		String four=out.getString("four");
		String cascode_id_=	out.getString("cascode_id_");
		request.setAttribute("cascode_id_",cascode_id_);
		request.setAttribute("byrwbh",out.getString("byrwbh"));
		request.setAttribute("aos_module_id_",out.getString("aos_module_id_"));
		//查询用户当前编研状态
		String  compilation_flag=compilationService.findCompilation_flag(out.getUserInfo());
		//得到当前用户按钮权限
		String bianyan_flag=compilationService.findButton_bianyan(out.getUserInfo());
		//String  checkup_flag=checkupService.findCheckup_flag(out.getUserInfo());
		session.setAttribute("bianyan_flag",bianyan_flag);
		//session.setAttribute("compilation_flag_",compilation_flag);
		//得到当前用户的是再审还是终审
		//根据当前用户名称得到按钮权限
		/*Dto outdto=examineService.seminar_initData(out,session);
		if(outdto.getString("zhuangao").equals("1")){
			request.setAttribute("zhuangao","1");
		}
		if(outdto.getString("first").equals("1")){
			request.setAttribute("first","1");
		}
		if(outdto.getString("next").equals("1")){
			request.setAttribute("next","1");
		}
		if(outdto.getString("last").equals("1")){
			request.setAttribute("last","1");
		}*/
		/*request.setAttribute("one",first);
		request.setAttribute("two",second);
		request.setAttribute("three",three);
		request.setAttribute("four",four);*/
		//上面的注释掉先不用，
		//上面的步骤暂时注释
		if(bianyan_flag!=null&&bianyan_flag.contains("撰稿")){
			request.setAttribute("zhuangao","1");
		}
		if(bianyan_flag!=null&&bianyan_flag.contains("合稿")){
			request.setAttribute("first","1");
		}
		if(bianyan_flag!=null&&bianyan_flag.contains("校对")){
			request.setAttribute("next","1");
		}
		if(bianyan_flag!=null&&bianyan_flag.contains("总编辑")){
			request.setAttribute("last","1");
		}

		//或者直接返回值url携带参数
		 //return "redirect:/compilation/articles/listCompilation？param1=value1&param2=value2";
		return "aos/compilation/subject.jsp?time="+new Date().getTime()+"&cascode_id_="+cascode_id_;
	}

	/**
	 * 加载数据
	 *
	 * @author PX
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * 2018-8-23
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("listCompilation")
	public void listCompilation(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto inDto = Dtos.newDto(request);
		//标题列表
		List<Map<String, Object>> fieldDtos = compilationService.listCompilation(inDto);
		//int pCount = compilationService.getPageAll_compilation(inDto);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos,fieldDtos.size());
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}

	/**
	 * 得到附件
	 * @param request
	 * @param response
	 * @param session
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getfile_fj")
	public void getfile_fj(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		//标题列表
		String fileString= compilationService.getfile_fj(inDto);
		//int pCount = compilationService.getPageAll_compilation(inDto);
		//条目数据
		outDto.setAppMsg(fileString);
		String outString = AOSJson.toJson(outDto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 判断是否存在任务附件
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="booleanfile")
	public void booleanfile(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=compilationService.booleanfile(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 * 得到撰稿人撰稿的内容根据路径查找文件
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "getzhuangaomessage")
	public void getzhuangaomessage(HttpServletRequest request,
								   HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		String zg_message=examineService.getzhuangaomessage(inDto);
		out.setAppMsg(zg_message);
		String outString = AOSJson.toJson(out);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping("listArchive")
	public void listArchive(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表;
		//先得到query条件
		String query = compilationService.queryConditions2(inDto);
		inDto.put("query",query);
		List<Map<String, Object>> fieldDtos = compilationService.listArchive(inDto);
		int pCount = compilationService.getPageAll_archive(inDto);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos,pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping("listArchive_edit")
	public void listArchive_edit(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//标题列表;
		//先得到query条件
		String query = compilationService.queryConditions2(inDto);
		//此时在查询当前任务已经选择的档案
		String query_id_ = compilationService.queryConditions2_noid(inDto);
		inDto.put("query",query+query_id_);
		List<Map<String, Object>> fieldDtos = compilationService.listArchive(inDto);
		int pCount = compilationService.getPageAll_archive(inDto);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos,pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
    @RequestMapping("updatebyrw_data")
	public void updatebyrw_data(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=compilationService.updatebyrw_data(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
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
		inDto.put("id_",inDto.getString("zt_id"));
		//标题列表
		List<Map<String,Object>> fieldDtos =examineService.getData(inDto,session);
		int pCount = examineService.getPageTotal_Data(inDto);
		//条目数据
		String outString = AOSJson.toGridJson(fieldDtos, pCount);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	@RequestMapping(value="updaterwno")
	public void updaterwno(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b= compilationService.updaterwno(dto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成！");
		}else{
			out.setAppMsg("提交失败！");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	@RequestMapping(value="updaterwyes")
	public void updaterwyes(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto dto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b= compilationService.updaterwyes(dto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成！");
		}else{
			out.setAppMsg("提交失败！");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 设计条件
	 *
	 * @author PX
	 * @param request
	 * @param response
	 *
	 * 2018-8-23
	 */
	@RequestMapping(value="getQuery")
	public void getQuery( HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		String query = compilationService.queryConditions2(inDto);
		outDto.put("query",query);
		String outString = AOSJson.toJson(outDto);
		// 就这样返回转换为Json后返回界面就可以了。
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
	@RequestMapping(value="listTablename")
	public void listTablename(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		List<Archive_tablenamePO> list = compilationService.findTablename();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	@RequestMapping(value="listTablename_by")
	public void listTablename_by(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		List<Archive_tablenamePO> list = compilationService.findTablename_by();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	@RequestMapping(value="listzt_by")
	public void listzt_by(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		List<Map<String,Object>> list = compilationService.findzt_by();
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	@RequestMapping(value="listztmc_by")
	public void listztmc_by(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		List<Map<String,Object>> list = compilationService.findztmc_by(qDto);
		String outString =AOSJson.toGridJson(list);
		WebCxt.write(response, outString);
	}
	@RequestMapping(value="addbyrw")
	public void addbyrw( HttpSession session, HttpServletRequest request, HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=compilationService.addbyrw(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}


	@RequestMapping(value="addbyrw_edit")
	public void addbyrw_edit(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=compilationService.addbyrw_edit(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 删除条目
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "delete_rw")
	public void delete_rw(HttpServletRequest request,
								HttpServletResponse response, HttpSession session) {
		Dto dto = Dtos.newDto(request);
		Dto outDto = compilationService.delete_rw(dto);
		//目录树

		WebCxt.write(response, AOSJson.toJson(outDto));
	}


	/**
     *
     * 删除条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "delete_data")
    public void delete_data(HttpServletRequest request,
                          HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = compilationService.delete_data(dto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
	@RequestMapping(value = "updatecompilation")
	public void updatecompilation(HttpServletRequest request,
								  HttpServletResponse response, HttpSession session){
		Dto dto = Dtos.newDto(request);
		UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
		Dto out=Dtos.newDto();
		boolean b= compilationService.updatecompilation(dto,userInfoVO);
		if(b){
			out.setAppMsg("提交完成！");
		}else{
			out.setAppMsg("提交失败！");
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 * 传递用户
	 *
	 * @author PX
	 * @param session
	 * @param request
	 * @param response
	 *
	 * 2018-8-20
	 * @throws ParseException
	 */
	@RequestMapping(value="jymessage")
	public void jymessage(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws ParseException{
		Dto outdto = Dtos.newDto(request);
		Dto dto = compilationService.jymessage(outdto,session);
		WebCxt.write(response, AOSJson.toJson(dto));
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
		List<Archive_tablefieldlistPO> titleDtos = compilationService.getDataFieldListTitle(qDto.getString("tablename"));
		WebCxt.write(response, AOSJson.toGridJson(titleDtos));
	}
	/**
	 *
	 * 查询用户名下拉框
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="queryUsers")
	public void queryUsers(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);

		//1.得到当前用户
		String user=(String)session.getAttribute("user");
		//根据当前用户查询到相关联的列表姓名
		//List<Map<String,Object>> userDtos = compilationService.getUserList(user);
		List<Map<String,Object>> userDtos = compilationService.getUserList2(user);
		WebCxt.write(response, AOSJson.toGridJson(userDtos));
	}


	public List<Map<String, Object>> getDataFieldListTitle(Dto qDto, HttpSession session) {
		// TODO Auto-generated method stub
		String sql="select * from "+qDto.getString("tablename");
		List<Map<String, Object>> listcount = compilationService.getlistString(sql);
		return listcount;
	}

	/**
	 * 得到总数
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("getSumPage")
	public void getSumPage(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		int pagecount = compilationService.getSumPage(qDto);
		Dto newDto=Dtos.newDto();
		newDto.setAppCode(pagecount);
		WebCxt.write(response, AOSJson.toJson(newDto));
	}

	/**
	 * 获取电子文件列表
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("getFiles")
	public void getFiles(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto outDto = Dtos.newDto();
		List<Map<String,Object>> list = compilationService.getFiles(qDto);
		WebCxt.write(response, AOSJson.toJson(list));
	}

	/**
	 * 操作列表
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("listarticles")
	public void listarticles(HttpServletRequest request,
						  HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Archive_topicPO> list =compilationService.listopertion(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 添加分配
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("adddetails")
	public void adddetails(HttpServletRequest request,
						   HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
		boolean b=compilationService.adddetails(qDto,userInfoVO);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 *
	 * 查询数据信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listAccounts2")
	public void listAccounts2(HttpServletRequest request,
							  HttpServletResponse response, HttpSession session) {
		Dto inDto = Dtos.newDto(request);
		inDto.put("tablename", inDto.getString("tablename"));
		List<Map<String,Object>> fieldDtos = compilationService.getDataFieldListDisplayAll2(inDto,session);
		String outString = AOSJson.toGridJson(fieldDtos);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}
	/**
	 * 得到当前条目的电子文件
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
	@RequestMapping("getpath")
	public void getpath(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		//条目数据id值
		String id_=inDto.getString("id_");
		//当前compilation的id值
		String topic_id_=inDto.getString("topic_id_");
		String tablename=inDto.getString("tablename");
		String path=compilationService.getpath(id_,topic_id_,tablename);
		Dto dto = Dtos.newDto();
		dto.put("pdfpath",path);
		//条目数据
		String outString = AOSJson.toJson(dto);
		// 就这样返回转换为Json后返回界面就可以了。
		WebCxt.write(response, outString);
	}




	/**
	 * 操作列表
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping("listwritelogin")
	public void listwritelogin(HttpServletRequest request,
						  HttpServletResponse response, HttpSession session){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Map<String,Object>> list =compilationService.listwritelogin(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}



	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getcompilationmessage")
	public void getcompilationmessage(HttpServletRequest request,
								HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		String message=compilationService.getcompilationmessage(qDto);
		out.setAppMsg(message);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 * 初鉴人操作保存
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getcompilation_message")
	public void getcompilation_message(HttpServletRequest request,
									  HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		String message=compilationService.getcompilation_message(qDto);
		out.setAppMsg(message);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 *
	 *
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "seminar_initData")
	public String seminar_initData(HttpServletRequest request,
									   HttpServletResponse response, HttpSession session){
		return "aos/compilation/topic.jsp?time="+new Date().getTime();
	}
	/**
	 * 查询专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listseminar")
	public void listseminar(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		List<Dto> treeNodes = compilationService.listseminar(qDto);
		String outString = AOSJson.toJson(treeNodes);
		WebCxt.write(response, outString);
	}
	/**
	 * 保存专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "saveseminar")
	public void saveseminar(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = compilationService.saveseminar(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 获取专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getSeminarById")
	public void getSeminarById(HttpServletRequest request, HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		String id=qDto.getString("id_");
		Archive_seminarPO archive_seminarPO=compilationService.getSeminarById(id);
		AOSUtils.copyProperties(archive_seminarPO,out);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 修改专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updateSeminar")
	public void updateSeminar(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = compilationService.updateSeminar(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}

	/**
	 * 删除专题分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deleteSeminar")
	public void deleteSeminar(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = compilationService.deleteSeminar(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 获取任务
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listTask")
	public void listTask(HttpServletRequest request, HttpServletResponse response){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Archive_taskPO> list= compilationService.listTask(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));

	}
	/**
	 * 保存任务
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "savetask")
	public void savetask(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = compilationService.savetask(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 修改任务分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updatetask")
	public void updatetask(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b = compilationService.updatetask(qDto);
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 修改任务分类
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deleteTask")
	public void deleteTask(HttpServletRequest request, HttpServletResponse response) {
		Dto qDto = Dtos.newDto(request);
		Dto out= compilationService.deleteTask(qDto);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 添加撰稿表单
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "addwriteForm")
	public void addwriteForm(HttpServletRequest request, HttpServletResponse response){
		Dto qDto=Dtos.newDto(request);
		Dto out=Dtos.newDto();
		boolean b=compilationService.addwriteForm(qDto);
		//同时再把当前所选择的条目名称全部添加到数据库中
		if(b){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**
	 * 获得撰稿表单
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listwrite")
	public void listwrite(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list= compilationService.listwrite(qDto,session);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 获得撰稿表单(未进行操作过的)
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listwrite2")
	public void listwrite2(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list= compilationService.listwrite2(qDto,session);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 权限预览(用户页面元素权限)。经过权限计算后的总权限。
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "listTaskName")
	public void listTaskName(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto qDto = Dtos.newDto(request);
		List<Map<String,Object>> list= compilationService.listTaskName(qDto,session);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	/**
	 * 计算ID
	 *
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "calcId")
	public void calcId(HttpServletRequest request, HttpServletResponse response) {
		Dto dto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		String id = AOSId.id(dto.getString("name_"));
		out.setAppMsg(id);
		WebCxt.write(response, AOSJson.toJson(out));
	}
	/**listoperationlogin
	 * 在表中添加标记
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "listoperationlogin")
	public void listoperationlogin(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out=Dtos.newDto();
		List<Archive_topic_userPO> list= compilationService.listoperationlogin(qDto);
		WebCxt.write(response, AOSJson.toGridJson(list));
	}
	//判断当前任务这个人是否可以撰稿
	@RequestMapping(value = "check_zhuangao")
	public void check_zhuangao(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		List<Map<String, Object>> list=examineService.check_zhuangao(qDto);
		if(list!=null&&list.size()>0){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	//判断当前任务这个人是否可以合稿
	@RequestMapping(value = "check_hegao")
	public void check_hegao(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		List<Map<String, Object>> list=examineService.check_hegao(qDto);
		if(list!=null&&list.size()>0){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	//判断当前任务这个人是否可以校对
	@RequestMapping(value = "check_jiaodui")
	public void check_jiaodui(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		List<Map<String, Object>> list=examineService.check_jiaodui(qDto);
		if(list!=null&&list.size()>0){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
	//判断当前任务这个人是否可以总编辑
	@RequestMapping(value = "check_zongbianji")
	public void check_zongbianji(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		Dto qDto = Dtos.newDto(request);
		Dto out = Dtos.newDto();
		List<Map<String, Object>> list=examineService.check_zongbianji(qDto);
		if(list!=null&&list.size()>0){
			out.setAppCode(1);
		}else{
			out.setAppCode(-1);
		}
		WebCxt.write(response, AOSJson.toJson(out));
	}
}

