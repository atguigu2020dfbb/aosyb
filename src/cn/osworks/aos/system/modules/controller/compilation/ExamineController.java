package cn.osworks.aos.system.modules.controller.compilation;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_compilation_rwMapper;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.dao.po.Compilation_topicPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.compilation.CompilationService;
import cn.osworks.aos.system.modules.service.compilation.ExamineService;


import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 编研审核
 *
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="compilation/examine")
public class ExamineController {
    @Autowired
    private CompilationService compilationService;
    @Autowired
    private DataService dataService;
    @Autowired
    private SqlDao sysDao;
    @Autowired
    private ExamineService examineService;
    /**
     * 页面初始化
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     * @return
     * 2018-8-23
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "examine_initData")
    public String initInput(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException{
        Dto qDto = Dtos.newDto(request);
        //得到当前用户的是再审还是终审
        //根据当前用户名称得到按钮权限
        Dto out=examineService.seminar_initData(qDto,session);
        request.setAttribute("first",out.getString("first"));
        request.setAttribute("next",out.getString("next"));
        request.setAttribute("last",out.getString("last"));
        if(out.getString("first").equals("1")){
            request.setAttribute("flag",1);
        }
        if(out.getString("next").equals("1")){
            request.setAttribute("flag",2);
        }
        if(out.getString("last").equals("1")){
            request.setAttribute("flag",3);
        }
        return "aos/compilation/examine.jsp?time="+new Date().getTime();
    }
    @RequestMapping(value="listTablename_by")
    public void listTablename_by(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        List<Archive_tablenamePO> list = compilationService.findTablename_by();
        String outString =AOSJson.toGridJson(list);
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
        String outString = AOSJson.toGridJson(fieldDtos,pCount,query);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping("getQueryIds")
    public void getQueryIds(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        List<Map<String,Object>> list= compilationService.getQueryIds(inDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }





    /**
     * 返回拼接条件
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     *            2019-1-25
     */
    @RequestMapping("saveQueryData")
    public void saveQueryData(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        Dto out = Dtos.newDto(request);
        String query = compilationService.queryConditions2(out);
        Dto outDto = Dtos.newDto();
        outDto.put("query",query);
        WebCxt.write(response, AOSJson.toJson(outDto));

    }

    /**
     *
     * 删除条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "delete_topic")
    public void delete_topic(HttpServletRequest request,
                          HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = examineService.delete_topic(dto);
        //目录树

        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    @RequestMapping(value="addtopic")
    public void addtopic( HttpSession session, HttpServletRequest request, HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=examineService.addtopic(qDto);
        //同时再把当前所选择的条目名称全部添加到数据库中
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value="edittopic")
    public void edittopic( HttpSession session, HttpServletRequest request, HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=examineService.edittopic(qDto);
        //同时再把当前所选择的条目名称全部添加到数据库中
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 专题列表界面
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "topic_initData")
    public String topic_initData(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        return "aos/compilation/topicList.jsp";
    }

    @RequestMapping(value = "initData")
    public String initData(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto out=Dtos.newDto(request);
        request.setAttribute("aos_module_id_",out.getString("aos_module_id_"));
        return "aos/compilation/data.jsp";
    }

    /**
     * 获取专题数据列表
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "listtopic")
    public void listtopic(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        List<Compilation_topicPO> list=examineService.listtopic(qDto);
        WebCxt.write(response, AOSJson.toGridJson(list,list.size()));

    }
    /**
     * 页面初始化
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     * @return
     * 2018-8-23
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "examine_by_initData")
    public String by_initInput(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException{
        Dto qDto = Dtos.newDto(request);
        //得到当前用户的是再审还是终审
        //根据当前用户名称得到按钮权限
        //根据cascode_id_得到当前用户的菜单数据信息

        String cascode_id_=qDto.getString("cascode_id_");
        examineService.getmodel(qDto,cascode_id_);
        Dto out=examineService.seminar_by_initData(qDto,session);
        request.setAttribute("zhuangao",out.getString("zhuangao"));
        request.setAttribute("first",out.getString("first"));
        request.setAttribute("next",out.getString("next"));
        request.setAttribute("last",out.getString("last"));
        if(out.getString("zhuangao").equals("1")){
            request.setAttribute("flag",1);
        }
        if(out.getString("first").equals("1")){
            request.setAttribute("flag",2);
        }
        if(out.getString("next").equals("1")){
            request.setAttribute("flag",3);
        }
        if(out.getString("last").equals("1")){
            request.setAttribute("flag",4);
        }
        request.setAttribute("sjkmc",qDto.getString("sjkmc"));
        request.setAttribute("byrwmc",qDto.getString("byrwmc"));
        request.setAttribute("byrwbh",qDto.getString("byrwbh"));
        request.setAttribute("cascode_id_",qDto.getString("cascode_id_"));
        return "aos/compilation/edit_examine.jsp?time="+new Date().getTime();

    }

    @RequestMapping(value="addtopic_edit")
    public void addtopic_edit(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=examineService.addtopic_edit(qDto);
        //同时再把当前所选择的条目名称全部添加到数据库中
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 判断是否存在任务附件
     * @param session
     * @param request
     * @param response
     */
    @RequestMapping(value="booleanpath")
    public void booleanpath(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=compilationService.booleanpath(qDto);
        //同时再把当前所选择的条目名称全部添加到数据库中
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
               WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 得到任务附件
     * @param session
     * @param request
     * @param response
     */
    @RequestMapping(value="getRwpath")
    public void getRwpath(HttpSession session, HttpServletRequest request, HttpServletResponse response)throws Exception{
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        String message=compilationService.getRwpath(qDto);
        //同时再把当前所选择的条目名称全部添加到数据库中
        out.setAppMsg(message);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 合稿人操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listCompilation_examine")
    public void listCompilation_examine(HttpServletRequest request,
                                        HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        List<Map<String,Object>> list =examineService.listCompilation_examine(qDto);
        int pCount = examineService.getPageAll(qDto);
        //条目数据
        String outString = AOSJson.toGridJson(list,pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     *
     * 合稿人操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "savefirstcompilationdetails")
    public void savefirstcompilationdetails(HttpServletRequest request,
                                            HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
        Dto out=Dtos.newDto();
        boolean b=examineService.savefirstcompilationdetails(qDto,userInfoVO);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }

    /**
     *得到合稿人稿子内容
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "gethegaomessage")
    public void gethegaomessage(HttpServletRequest request,
                                HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        String message=examineService.gethegaomessage(qDto);
        out.put("hegaomessage",message);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 校对人同意操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "savenextcompilationdetails")
    public void savenextcompilationdetails(HttpServletRequest request,
                                           HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
        Dto out=Dtos.newDto();
        boolean b=examineService.savenextcompilationdetails(qDto,userInfoVO);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 总编辑人同意操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "savelastcompilationdetails")
    public void savelastcompilationdetails(HttpServletRequest request,
                                           HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=examineService.savelastcompilationdetails(qDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 校对人不同意操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "nosavenextcompilationdetails")
    public void nosavenextcompilationdetails(HttpServletRequest request,
                                             HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=examineService.nosavenextcompilationdetails(qDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 总编辑不同意操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "nosavelastcompilationdetails")
    public void nosavelastcompilationdetails(HttpServletRequest request,
                                             HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=examineService.nosavelastcompilationdetails(qDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }

    @RequestMapping(value = "getcompilationmessage")
    public void getcompilationmessage(HttpServletRequest request,
                                      HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        List<Map<String,Object>> list =examineService.getcompilationmessage(qDto);
        if(list!=null&&list.size()>0){
            out.setAppMsg((String)list.get(0).get("description"));
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value = "getcompilation_zhuagao_message")
    public void getcompilation_zhuagao_message(HttpServletRequest request,
                                      HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        List<Map<String,Object>> list =examineService.getcompilation_zhuagao_message(qDto);
        if(list!=null&&list.size()>0){
            out.setAppMsg((String)list.get(0).get("compilation_message"));
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }

    /**
     *
     * 撰稿人操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getcompilation_zhuangao_message")
    public void getcompilation_zhuangao_message(HttpServletRequest request,
                                      HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        String message=compilationService.getcompilation_zhuangao_message(qDto);
        out.setAppMsg(message);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 合稿人稿件信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getcompilation_hegao_message")
    public void getcompilation_hegao_message(HttpServletRequest request,
                                                HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        String message=compilationService.getcompilation_hegao_message(qDto);
        out.setAppMsg(message);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 校对人稿件信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getcompilation_jiaodui_message")
    public void getcompilation_jiaodui_message(HttpServletRequest request,
                                             HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        String message=compilationService.getcompilation_jiaodui_message(qDto);
        out.setAppMsg(message);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 页面初始化
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     * @return
     * 2018-8-23
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("listfirsttrial")
    public void listfirsttrial(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto=Dtos.newDto(request);
        List<Map<String,Object>> list =examineService.listfirsttrial(qDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    @RequestMapping("listnexttrial")
    public void listnexttrial(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto=Dtos.newDto(request);
        List<Map<String,Object>> list =examineService.listnexttrial(qDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    @RequestMapping("listlasttrial")
    public void listlasttrial(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto=Dtos.newDto(request);
        List<Map<String,Object>> list =examineService.listlasttrial(qDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    //得到编研档案合稿操作信息
    @RequestMapping("first_details_all")
    public String first_details_all(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto=Dtos.newDto(request);
        request.setAttribute("topic_id_",qDto.getString("id_"));
        return "aos/compilation/first_compilation.jsp";
    }
    //得到编研档案校对操作信息
    @RequestMapping("next_details_all")
    public String next_details_all(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto=Dtos.newDto(request);
        request.setAttribute("topic_id_",qDto.getString("id_"));
        return "aos/compilation/next_compilation.jsp";
    }
    /**
     * 操作列表
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("listoperationlogin")
    public void listoperationlogin(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session){
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        List<Map<String,Object>> list =examineService.listoperationlogin(qDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    //得到编研档案校对操作信息
    @RequestMapping("last_details_all")
    public String last_details_all(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto=Dtos.newDto(request);
        request.setAttribute("topic_id_",qDto.getString("id_"));
        return "aos/compilation/last_compilation.jsp";
    }
    @RequestMapping("listexaminedescription")
    public void listexaminedescription(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto=Dtos.newDto(request);
        List<Map<String,Object>> list =examineService.listexaminedescription(qDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    /**
     *
     * 初鉴人的反馈终审信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getLastTrial")
    public void getLastTrial(HttpServletRequest request,
                             HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        String operator_description=examineService.getLastTrial(qDto);
        out.setAppMsg(operator_description);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 初鉴人的反馈再审信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getNextTrial")
    public void getNextTrial(HttpServletRequest request,
                             HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        String operator_description=examineService.getNextTrial(qDto);
        out.setAppMsg(operator_description);
        WebCxt.write(response, AOSJson.toJson(out));
    }

    /**
     * 获得表单
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listtrial")
    public void listtrial(HttpServletRequest request, HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        String flag=qDto.getString("flag");
        if("1".equals(flag)){
            list= examineService.listfirsttrial(qDto,session);
            WebCxt.write(response, AOSJson.toGridJson(list));
        }else if("2".equals(flag)){
            list= examineService.listnexttrial(qDto,session);
            WebCxt.write(response, AOSJson.toGridJson(list));
        }else if("3".equals(flag)){
            list= examineService.listlasttrial(qDto,session);
            WebCxt.write(response, AOSJson.toGridJson(list));
        }else{
            WebCxt.write(response, AOSJson.toGridJson(list));
        }
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
    @RequestMapping(value = "archive_details_all_message")
    public String archive_details_all_message(HttpServletRequest request,
                                              HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        String id_=qDto.getString("id_");
        String zt_id=qDto.getString("zt_id");
        String flag=qDto.getString("flag");
        String cascode_id_=	qDto.getString("cascode_id_");
        request.setAttribute("cascode_id_",cascode_id_);
        request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
        //根据id_
        String tablename="";
        List<Map<String, Object>> listmodule=examineService.getModule_byid_(zt_id);
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(tablename);
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        String pagesize = userInfoVO.getPagesize() + "";
        if (pagesize == null) {
            pagesize = "20";
        }
        request.setAttribute("tablename", tablename);
        session.setAttribute("tablename", tablename);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        request.setAttribute("zt_id", zt_id);
        request.setAttribute("rw_id_", id_);
        //根据当前id判断是初鉴 再审  终审操作
        return "aos/compilation/zhuangao_compilation_message.jsp";
    }
    @RequestMapping(value = "archive_details_first_message")
    public String archive_details_first_message(HttpServletRequest request,
                                              HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        String id_=qDto.getString("id_");
        String zt_id=qDto.getString("zt_id");
        String flag=qDto.getString("flag");
        String cascode_id_=	qDto.getString("cascode_id_");
        request.setAttribute("cascode_id_",cascode_id_);
        request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
        //根据id_
        String tablename="";
        List<Map<String, Object>> listmodule=examineService.getModule_byid_(zt_id);
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(tablename);
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        String pagesize = userInfoVO.getPagesize() + "";
        if (pagesize == null||pagesize==""||"null".equals(pagesize)) {
            pagesize = "20";
        }
        request.setAttribute("tablename", tablename);
        session.setAttribute("tablename", tablename);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        request.setAttribute("zt_id", zt_id);
        request.setAttribute("rw_id_", id_);
        request.setAttribute("byrwbh",qDto.getString("byrwbh"));
        //根据当前id判断是初鉴 再审  终审操作
        return "aos/compilation/hegao_compilation.jsp";
    }
    @RequestMapping(value = "archive_details_next_message")
    public String archive_details_next_message(HttpServletRequest request,
                                                HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        String id_=qDto.getString("id_");
        String zt_id=qDto.getString("zt_id");
        String flag=qDto.getString("flag");
        String cascode_id_=	qDto.getString("cascode_id_");
        request.setAttribute("cascode_id_",cascode_id_);
        request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
        //根据id_
        String tablename="";
        List<Map<String, Object>> listmodule=examineService.getModule_byid_(zt_id);
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }
        String hguser= examineService.findHgUser(id_);
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(tablename);
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        String pagesize = userInfoVO.getPagesize() + "";
        if (pagesize == null) {
            pagesize = "20";
        }
        request.setAttribute("tablename", tablename);
        session.setAttribute("tablename", tablename);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        request.setAttribute("zt_id", zt_id);
        request.setAttribute("rw_id_", id_);
        request.setAttribute("hguser", hguser);
        //根据当前id判断是初鉴 再审  终审操作
        return "aos/compilation/jiaodui_compilation.jsp";
    }
    @RequestMapping(value = "archive_details_last_message")
    public String archive_details_last_message(HttpServletRequest request,
                                               HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        String id_=qDto.getString("id_");
        String zt_id=qDto.getString("zt_id");
        String flag=qDto.getString("flag");
        String cascode_id_=	qDto.getString("cascode_id_");
        request.setAttribute("cascode_id_",cascode_id_);
        request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
        //根据id_
        String tablename="";
        List<Map<String, Object>> listmodule=examineService.getModule_byid_(zt_id);
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }
        String hguser= examineService.findHgUser(id_);
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(tablename);
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        String pagesize = userInfoVO.getPagesize() + "";
        if (pagesize == null) {
            pagesize = "20";
        }
        request.setAttribute("tablename", tablename);
        session.setAttribute("tablename", tablename);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        request.setAttribute("zt_id", zt_id);
        request.setAttribute("rw_id_", id_);
        request.setAttribute("hguser", hguser);
        //根据当前id判断是初鉴 再审  终审操作
        return "aos/compilation/zongbianji_compilation.jsp";
    }
    @RequestMapping(value = "getOCR_compilation")
    public void getOCR_compilation(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session) throws IOException {
        Dto qDto = Dtos.newDto(request);
        String _path=qDto.getString("_path");
        File pdfFile = new File(_path);
        //File pdfFile = new File(path);
       // PDFParser pdfParser=new PDFParser(new org.apache.pdfbox.io.RandomAccessFile(pdfFile,"r"));
        //pdfParser.parse();
       // PDDocument pddocument = new PDDocument(pdfParser.getDocument());
        PDFTextStripper stripper = new PDFTextStripper();
        PDDocument pddocument2 =PDDocument.load(pdfFile);
        String body = stripper.getText(pddocument2);
       // String body=getTextFromPDF(_path);
        //body=ocrpdf(_path);
      Dto out = Dtos.newDto();
        out.setAppMsg(body);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    public  String ocrpdf (String path) {
        File file = new File(path);
        FileInputStream in = null;
        String test="";
        try {
            in =  new FileInputStream(file);
            RandomAccessRead randomAccessRead = new RandomAccessBufferedFileInputStream(in);
            PDFParser parser = new PDFParser(randomAccessRead);
            parser.parse();
            PDDocument pdDocument = parser.getPDDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            test = stripper.getText(pdDocument);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return test;
    }


    /**
     *
     * 撰稿人操作保存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "savewritecompilationdetails")
    public void savewritecompilationdetails(HttpServletRequest request,
                                            HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        UserInfoVO userInfoVO=WebCxt.getUserInfo(session);
        Dto out=Dtos.newDto();
        boolean b=examineService.savewritecompilationdetails(qDto,userInfoVO);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }

    /**
     *
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
        inDto.put("id_",inDto.getString("topic_id_"));
        //标题列表
        List<Map<String,Object>> fieldDtos =examineService.getData(inDto,session);
        int pCount = examineService.getPageTotal_Data(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos, pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
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
    /**
     * 显示数据
     *
     * @author PX
     * @param request
     * @param response
     *
     * 2018-8-23
     */
    @RequestMapping(value="listAccounts_topic")
    public void listAccounts_topic(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        List<Map<String,Object>> fieldDtos =examineService.getData(inDto,session);
        int pCount = examineService.getPageTotal_Data(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos, pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping("listdata_edit2")
    public void listdata_edit2(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        String query = examineService.queryConditions2(inDto);
        //此时在查询当前任务已经选择的档案
        String query_id_ = examineService.queryConditions2_noid(inDto);
        inDto.put("query",query+query_id_);
        List<Map<String, Object>> fieldDtos = examineService.listArchive(inDto);
        int pCount = examineService.getPageAll_archive(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos,pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    @RequestMapping("listdata_edit")
    public void listdata_edit(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        String query = examineService.queryConditions2(inDto);
        //此时在查询当前任务已经选择的档案
        String query_id_ = examineService.queryConditions2_noid(inDto);
        inDto.put("query",query+query_id_);
        List<Map<String, Object>> fieldDtos = examineService.listArchive(inDto);
        int pCount = examineService.getPageAll_archive(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos,pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }



    @RequestMapping("updatetopic_data")
    public void updatetopic_data(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=examineService.updatetopic_data(inDto);
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
    @RequestMapping(value = "deletetopic_data")
    public void deletetopic_data(HttpServletRequest request,
                            HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = examineService.deletetopic_data(dto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     *
     *
     * 查询数据信息(校对和总编辑)
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listZg")
    public void listZg(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        Dto inDto = Dtos.newDto(request);
        inDto.put("tablename", inDto.getString("tablename"));
        List<Map<String,Object>> fieldDtos = examineService.listZg(inDto,session);
        String outString = AOSJson.toGridJson(fieldDtos);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     *
     *
     * 查询数据信息(合稿)
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listZg_hegao")
    public void listZg_hegao(HttpServletRequest request,
                       HttpServletResponse response, HttpSession session) {
        Dto inDto = Dtos.newDto(request);
        inDto.put("tablename", inDto.getString("tablename"));
        List<Map<String,Object>> fieldDtos = examineService.listZg_hegao(inDto,session);
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
        Dto dto=examineService.getpath(id_,topic_id_,tablename);
        //条目数据
        String outString = AOSJson.toJson(dto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     * 填充报表
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping(value = "fillReport")
    public void fillReport(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session) {
        Dto qDto = Dtos.newDto(request);
        String compilation_message=qDto.getString("compilation_message");
        session.setAttribute("compilation_message", compilation_message);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }
    @RequestMapping(value = "downloadcompilation")
    public void downloadcompilation(HttpServletRequest request,
                                    HttpServletResponse response, HttpSession session) throws IOException {
        Dto out=Dtos.newDto(request);
        String rwname=examineService.findCompilation_RwName(out);
        StringBuffer sbf = new StringBuffer();
        sbf.append("<html><body>");//缺失的首标签
        String content=(String)session.getAttribute("compilation_message");
        sbf.append(content); //富文本内容
        sbf.append("</body></html>");//缺失的尾标签

        byte b[] = sbf.toString().getBytes("GBK");
        ByteArrayInputStream bais = new ByteArrayInputStream(b);//将字节数组包装到流中
/* * 关键地方
生成word格式*/
        POIFSFileSystem poifs = new POIFSFileSystem();

        DirectoryEntry directory = poifs.getRoot();
        DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
//输出文件
        //String fileName="文件名";
        response.reset();
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/msword");//导出word格式
        response.addHeader("Content-Disposition", "attachment;filename=" +
                new String(rwname.getBytes("GB2312"),"iso8859-1") + ".doc");
        OutputStream ostream = response.getOutputStream();
        poifs.writeFilesystem(ostream);
        ostream.flush();
        ostream.close();
        bais.close();

    }
    @RequestMapping("getComboboxList")
    public void getComboboxList(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> list =examineService.getComboboxList(inDto);
        String outString = AOSJson.toGridJson(list);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }


}
