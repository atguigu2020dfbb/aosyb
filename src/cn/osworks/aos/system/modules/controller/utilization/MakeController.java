package cn.osworks.aos.system.modules.controller.utilization;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.SystemService;
import cn.osworks.aos.system.modules.service.archive.BasicSearchService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.archive.StatisticsService;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import cn.osworks.aos.system.modules.service.utilization.UtilizationService;
import com.lowagie.text.*;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.lowagie.text.pdf.PdfWriter;

@Controller
@RequestMapping(value="make")
public class MakeController extends JdbcDaoSupport {

    @Autowired
    private Archive_remoteMapper archive_remoteMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    @Autowired
    private Archive_zz_applyMapper archive_zz_applyMapper;
    @Autowired
    private UtilizationService utilizationService;
    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DocService docService;

    @Autowired
    private BasicSearchService basicSearchService;

    @Autowired
    private SystemService systemService;
    @Autowired
    private DataService dataService;

    @Autowired
    private Archive_remote_advanceMapper archive_remote_advanceMapper;

    @Autowired
    private Archive_remote_logMapper archive_remote_logMapper;
    @Autowired
    private Archive_tableinputMapper archive_tableinputMapper;

    @RequestMapping(value="initMake")
    public String initUtilization(HttpServletRequest request, HttpServletResponse response){
       Dto dto= Dtos.newDto(request);
       request.setAttribute("user",dto.getUserInfo().getName_());
        return "aos/utilization/initMake.jsp";
    }

    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listMake")
    public void listMake(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_remote    order by djbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from archive_remote ";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());

        WebCxt.write(response,outString);

    }
    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listMake_ldhp")
    public void listMake_ldhp(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_remote where 1=1 and  state='"+qDto.getString("state")+"'   order by djbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from archive_remote where 1=1 and  state='"+qDto.getString("state")+"'";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());

        WebCxt.write(response,outString);

    }
    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listApply")
    public void listApply(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        List<Archive_zz_applyPO> list = archive_zz_applyMapper.likePage(inDto);
        String outString = AOSJson.toGridJson(list,inDto.getPageTotal());
        WebCxt.write(response,outString);
    }

    @RequestMapping(value="deleteZiZhuTask")
    public void deleteZiZhuTask(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = utilizationService.deleteZiZhuTask(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listMakedetail")
    public void listMakedetail(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        String tablename=utilizationService.getTablename(inDto);
        if(tablename.isEmpty()){
            return;
        }
        String queryy = utilizationService.joinQuery(inDto);
        inDto.put("query",queryy);
        inDto.put("tablename",tablename);
        List<Map<String,Object>> fieldDtos = basicSearchService.getDataFieldListDisplayAllCd(inDto,session);
        //上面的状态添加到下面
        for(int i=0;i<fieldDtos.size();i++){
            fieldDtos.get(i).put("ckzt",inDto.getString("ckzt"));
            fieldDtos.get(i).put("yngh",inDto.getString("yngh"));
            fieldDtos.get(i).put("tablename",tablename);
        }
        //fieldDtos.get();
        int pCount = basicSearchService.getPageTotal3(inDto);
        request.setAttribute("_total", pCount);
        request.setAttribute("query",queryy);
        //WebCxt.write(response,AOSJson.toGridJson(aa));
        WebCxt.write(response,AOSJson.toGridJson(fieldDtos,pCount));
        // 就这样返回转换为Json后返回界面就可以了。
        //WebCxt.write(response, AOSJson.toGridJson(fieldDtos, pCount));
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
        List<Map<String,Object>> list = utilizationService.getFiles(qDto);
        WebCxt.write(response, AOSJson.toJson(list));
    }
    @RequestMapping(value = "getAPPID")
    public void getAPPID(HttpServletRequest request,HttpServletResponse response){
        Dto outDto = Dtos.newDto();
        String djbh = AOSId.id("登记流水号");
        outDto.put("djbh",djbh);
        WebCxt.write(response,AOSJson.toJson(outDto));

    }

    @RequestMapping(value="saveMake")
    public void saveUtilization(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = utilizationService.saveUtilization(qDto);
        utilizationService.insertRemoteLog(outDto.getString("id_"),qDto.getUserInfo().getName_(),"登记");
        WebCxt.write(response,AOSJson.toJson(outDto));

    }

    /**
     * 根据路径得到内容
     * @param response
     * @param request
     */
    @RequestMapping(value="getImg")
    public void getImg(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = Dtos.newDto();
        String imgtext=utilizationService.getImg(qDto);
        String str1="data:image/jpeg;base64,";
        if(imgtext.length()<=str1.length()){

        }else{
            imgtext=imgtext.substring(str1.length(), imgtext.length());
        }
        outDto.setAppMsg(imgtext);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     * 根据路径得到介绍信
     * @param response
     * @param request
     */
    @RequestMapping(value="getJsx")
    public void getJsx(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = Dtos.newDto();
        String imgtext=utilizationService.getJsx(qDto);
        String str1="data:image/jpeg;base64,";
        imgtext=imgtext.substring(str1.length(), imgtext.length());
        outDto.setAppMsg(imgtext);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }


    @RequestMapping(value="saveApply")
    public void saveApply(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = utilizationService.saveApply(qDto);
        utilizationService.insertRemoteLog(outDto.getString("id_"),qDto.getUserInfo().getName_(),"登记");
        WebCxt.write(response,AOSJson.toJson(outDto));

    }
    @RequestMapping(value="initLeader")
    public String initLeader(HttpServletRequest request, HttpServletResponse response){
        return "aos/utilization/zz_initLeader.jsp";
    }

    @RequestMapping("loadData")
    public String initInput(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        Dto dto=Dtos.newDto(request);
        String id_=dto.getString("id_");
        String recordid = dto.getString("recordid");
        String name_=dto.getString("name_");
        String djbh = dto.getString("djbh");
        String listtablename=basicSearchService.findModule(id_);
        if(listtablename==null||listtablename.equals("")||listtablename.equals("null")){
            if(name_==null||name_=="null"||name_.equals("")){
                name_="请选择档案门类";
            }
            request.setAttribute("id_",id_);
            request.setAttribute("recordid",recordid);
            request.setAttribute("tablename_",listtablename);
            request.setAttribute("djbh",djbh);
            request.setAttribute("bj",dto.getString("bj"));
            return "aos/utilization/loadData.jsp?time="+new Date().getTime();
        }
        List<Archive_tablefieldlistPO> title = basicSearchService.getDataFieldListTitle(listtablename);
        request.setAttribute("tablename", listtablename);
        request.setAttribute("fieldDtos", title);
        if(name_==null||name_=="null"||name_.equals("")){
            name_="请选择档案门类";
        }
        request.setAttribute("name_",name_);
        request.setAttribute("id_",id_);
        request.setAttribute("djbh",djbh);
        request.setAttribute("bj",dto.getString("bj"));
        request.setAttribute("recordid",recordid);
        return "aos/utilization/loadData.jsp?time="+new Date().getTime();
    }

    @RequestMapping(value = "updateMake")
    public void updateUtilization(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateUtilization(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "updatezz_Apply")
    public void updatezz_Apply(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updatezz_Applyzation(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "updateApply")
    public void updateApply(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateApplyzation(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value="deleteMake")
    public void deleteUtilization(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = utilizationService.deleteUtilization(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }
    @RequestMapping(value="updateMake_spzt")
    public void updateMake_spzt(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = utilizationService.updateMake_spzt(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    /**
     *
     * 已查到
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "insertMakeDetail")
    public void insertUtilizationDetail(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        Dto outDto =utilizationService.insertUtilizationDetail(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     *
     * 已查到
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "insertApplyDetail")
    public void insertApplyDetail(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        Dto outDto =utilizationService.insertUtilizationApply(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    /**
     * 查询分类科目树
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listData")
    public void listcategory(HttpServletRequest request, HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        List<Dto> treeNodes = systemService.listcategory(qDto);
        String outString = AOSJson.toJson(treeNodes);
        WebCxt.write(response, outString);
    }

    /**
     *
     * 获得电子文件信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getPath")
    public void getPath(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
       /* String id_=inDto.getString("tablename");
        String listtablename=basicSearchService.findModule(id_);
        inDto.put("tablename",listtablename);*/
        List<Dto> pathDtos = dataService.getPath(inDto);
        String outString = AOSJson.toGridJson(pathDtos);
        WebCxt.write(response, outString);
    }
    @RequestMapping(value="zz_listAccounts")
    public void zz_listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        List<Dto> fieldDtos = basicSearchService.getDataFieldListDisplayAll(inDto,session);
        int pCount = basicSearchService.getPageTotal(inDto);
        String query = inDto.getString("query");
        request.setAttribute("_total", pCount);
        request.setAttribute("query",query);
        session.setAttribute("query",query);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos, pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
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
        String query = dataService.queryConditions2(out);
        Dto outDto = Dtos.newDto();
        outDto.setAppMsg(query);
        WebCxt.write(response, AOSJson.toJson(outDto));

    }
    /**
     * 返回上一次查询
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     *            2019-1-18
     * @throws IOException
     */
    @RequestMapping("getSelectWhereLast")
    public void getSelectWhereLast(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session)
            throws IOException {
        Dto out = Dtos.newDto(request);
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        out.put("Username", userInfoVO.getAccount_());
        List<Map<String, Object>> list = dataService.getSelectWhereLast(out);
        // 把list集合传递给前台
        String gridJson = dataService.addLastJson(list, out, response);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.print(gridJson);
    }

    /**
     * 返回下一次查询
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     *            2019-1-18
     * @throws IOException
     */
    @RequestMapping("getSelectWhereNext")
    public void getSelectWhereNext(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session)
            throws IOException {
        Dto out = Dtos.newDto(request);
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        out.put("Username", userInfoVO.getAccount_());
        List<Map<String, Object>> list = dataService.getSelectWhereNext(out);
        // 把list集合传递给前台
        String gridJson = dataService.addNextJson(list, out, response);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.print(gridJson);
    }

    /**
     * 清空查询条件缓存
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     *            2019-1-19
     */
    @RequestMapping("removeDataWhere")
    public void removeDataWhere(HttpServletRequest request,
                                HttpServletResponse response, HttpSession session) {
        Dto out = Dtos.newDto(request);
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        out.put("Username", userInfoVO.getAccount_());
        dataService.removDataWhere(out);
    }

    @RequestMapping(value="listAccounts")
    public void listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("null").equals(inDto.getString("tablename"))){
            //标题列表
            List<Dto> fieldDtos = new ArrayList<Dto>();
            int pCount =0;
            String query = inDto.getString("query");
            request.setAttribute("_total", pCount);
            request.setAttribute("query",query);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(fieldDtos, pCount);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }else{
            String flag=inDto.getString("flag");
            List<Dto> fieldDtos =null;
            int pCount=0;
            if("1".equals(flag)){
                //标题列表
                fieldDtos = basicSearchService.getDataFieldListDisplayAll3(inDto,session);
                 pCount= basicSearchService.getPageTotal5(inDto);
            }else{

            }
            request.setAttribute("_total", pCount);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(fieldDtos, pCount);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }

    }

    /**
     *
     * 查询字段下拉框
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "queryFields")
    public void queryFields(HttpServletRequest request,
                            HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(qDto.getString("tablename"));
        WebCxt.write(response, AOSJson.toGridJson(titleDtos));
    }
    /**
     *
     * 查询字段下拉框(附加表获取)
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "queryFields_children")
    public void queryFields_children(HttpServletRequest request,
                            HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        List<Archive_tablefieldlist_childrenPO> titleDtos = dataService
                .getDataFieldListTitle_children(qDto.getString("tablename"));
        WebCxt.write(response, AOSJson.toGridJson(titleDtos));
    }
    @RequestMapping(value = "updateMakeDd")
    public void updateMakeDd(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateUtilization(inDto);
        String content=null;
        String person=null;
        String person_cn=null;
        if(inDto.getString("state").equals("1")){
            content="提交申请";
        } if(inDto.getString("state").equals("2")){
            content="转实体库";
        }if(inDto.getString("ckzt").equals("2")){
            content="确认归还";
        }
        person=inDto.getString("spr_en");
        person_cn=inDto.getString("spr_cn");
        utilizationService.insertRemoteLog2(inDto.getString("id_"),inDto.getUserInfo().getName_(),content,person,person_cn);
        notificationService.SubmitLy(inDto,content,person);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "updateMakeStck")
    public void updateMakeStck(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateUtilization(inDto);
        String content=null;
        String person=null;
        if(inDto.getString("state").equals("1")){
            content="提交申请";
        } if(inDto.getString("state").equals("2")){
            content="转实体库";
        }if(inDto.getString("ckzt").equals("2")){
            content="确认归还";
        }
        person=inDto.getString("person");
        utilizationService.insertRemoteLog(inDto.getString("id_"),inDto.getUserInfo().getName_(),content);
        notificationService.SubmitLy_CK(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping( value = "updateMakeAgree")
    public void updateMakeAgree(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        inDto.put("spr",inDto.getUserInfo().getName_());
        inDto.put("spsj",sd.format(new Date()));
        Dto outDto = utilizationService.updateUtilization(inDto);
        String content=null;
        if(inDto.getString("spzt").equals("1")){
            content="同意";
        }if(inDto.getString("spzt").equals("0")){
            content="拒绝";
        }
        utilizationService.insertRemoteLog(inDto.getString("id_"),inDto.getUserInfo().getName_(),content);
        notificationService.TaskLy(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping( value = "updateSatisfied")
    public void updateSatisfied(HttpServletResponse response,HttpServletRequest request){
        Dto inDto=Dtos.newDto(request);
        Dto outDto = utilizationService.updateSatisfied(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }


    @RequestMapping( value = "updateApplyAgree")
    public void updateApplyAgree(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        inDto.put("spr",inDto.getUserInfo().getName_());
        inDto.put("spsj",sd.format(new Date()));
        Dto outDto = utilizationService.updateApplyzation(inDto);
        String content=null;
        if(inDto.getString("spzt").equals("1")){
            content="同意";
        }if(inDto.getString("spzt").equals("0")){
            content="拒绝";
        }
        utilizationService.insertRemoteLog(inDto.getString("id_"),inDto.getUserInfo().getName_(),content);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listAdvance")
    public void listAdvance(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_remote_advance   order by yybh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from archive_remote_advance ";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());
        WebCxt.write(response,outString);

    }

    @RequestMapping(value="saveAdvance")
    public void saveAdvance(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = utilizationService.saveAdvance(qDto);
        WebCxt.write(response,AOSJson.toJson(outDto));

    }

    @RequestMapping(value = "updateAdvance")
    public void updateAdvance(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateAdvance(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deleteAdvance")
    public void deleteAdvance(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = utilizationService.deleteAdvance(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }


    @RequestMapping(value = "initData")
    public String initData(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        String tablename = utilizationService.selecttablename(qDto);
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(tablename);
        request.setAttribute("tablename", tablename);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("formid", qDto.getString("formid"));
        return "aos/utilization/djxq.jsp";
    }

    @RequestMapping(value = "listMakeLog")
    public void listMakeLog(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        qDto.setOrder("czsj DESC");
        List<Archive_remote_logPO> list = archive_remote_logMapper.list(qDto);
        String outString = AOSJson.toGridJson(list,qDto.getPageTotal());
        WebCxt.write(response,outString);
    }
    /**
     *
     * 打开电子文件
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "openPdfFile")
    public String openPdfFile(HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        //执行生成水印pdf
        String temp = "";
        Dto inDto = Dtos.newDto(request);
        String tid=inDto.getString("tid");
        String tablename=inDto.getString("tablename");
        Properties prop = PropertiesLoaderUtils
                .loadAllProperties("config.properties");
        String linkAddress = prop.getProperty("linkAddress");
        String filePath = prop.getProperty("filePath");
        String path1 = utilizationService.getDocumentPath(inDto);
        String path = filePath  + path1;
        //******************************************
        /*Ricardo*/
        if (path1.indexOf("tif")>0||path1.indexOf("TIF")>0){
            path = docService.tifToPdf(path,"");
            path1 = path.substring(11);
        }
        else if (path1.indexOf("jpg")>0||path1.indexOf("JPG")>0){
            path = docService.jpgToPdf(path,"");
            path1 = path.substring(11);
        }

        /*Ricardo*/
        String QRshow = inDto.getString("QRshow");
        String waterShow = inDto.getString("waterShow");
        String ftpwaterpath = linkAddress+path1;

        //是否显示二维码或水印
        if (Boolean.valueOf(QRshow)||Boolean.valueOf(waterShow)){
            String waterpath=utilizationService.addWater(inDto,path,filePath);
            ftpwaterpath=linkAddress  +waterpath.substring(11,waterpath.length());
           // ftpwaterpath=linkAddress  +"//"+"water"+"//"+new File(waterpath).getName();
        }
        System.out.println(ftpwaterpath);
        //*************************************
        List<Archive_tablefieldlistPO> titleDtos = dataService.getInfoFieldListTitle(inDto.getString("tablename") + "_info");
        request.setAttribute("tablename", inDto.getString("tablename"));
        request.setAttribute("id", inDto.getString("id"));
        request.setAttribute("strswf", ftpwaterpath);
        request.setAttribute("tid", inDto.getString("tid"));
        request.setAttribute("fieldDtos", titleDtos);
        return "common/documentView.jsp";

    }

    @RequestMapping(value="initFullText")
    public String initFullText(HttpServletRequest request,HttpServletResponse response){
        return "aos/utilization/fullText.jsp";
    }

    @RequestMapping(value="initBasicSearch")
    public String initBasicSearch(HttpServletRequest request,HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        UserInfoVO userInfoVO=dto.getUserInfo();
        String user=userInfoVO.getAccount_();
        request.setAttribute("user",user);
        request.setAttribute("djbh", dto.getString("djbh"));
        request.setAttribute("tablename",dto.getString("tablename"));
        request.setAttribute("recordid", dto.getString("recordid"));
        request.setAttribute("bj",dto.getString("bj"));
        return "aos/utilization/basicSearch.jsp";
    }

    @RequestMapping(value="initCrossSearch")
    public String initCrossSearch(HttpServletRequest request,HttpServletResponse response){
        return "aos/utilization/crossSearch.jsp";
    }
    @RequestMapping(value="basicSearchTable")
    public String basicSearchTable(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        UserInfoVO userInfoVO=qDto.getUserInfo();
        String user=userInfoVO.getAccount_();
        request.setAttribute("user",user);
        String tablename = qDto.getString("tablename");
        String tabledesc = qDto.getString("tabledesc");
        List<Archive_tablefieldlistPO> title = basicSearchService.getDataFieldListTitle(tablename);
        List<Archive_tablefieldlist_childrenPO> titleDtos = dataService
                .getDataFieldListTitle_children(qDto.getString("tablename"));
        request.setAttribute("fieldDtos", title);
        request.setAttribute("field_childrenDtos", titleDtos);
        request.setAttribute("tablename", tablename);
        request.setAttribute("tabledesc", tabledesc);
        request.setAttribute("djbh", qDto.getString("djbh"));
        request.setAttribute("recordid", qDto.getString("recordid"));
        return "aos/utilization/basicSearch.jsp";
    }
    @RequestMapping(value="basicSearchTable2")
    public void basicSearchTable2(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        UserInfoVO userInfoVO=qDto.getUserInfo();
        String user=userInfoVO.getAccount_();
        request.setAttribute("user",user);
        String tablename = qDto.getString("tablename");
        List<Archive_tablefieldlistPO> title = basicSearchService.getDataFieldListTitle(tablename);
        request.setAttribute("fieldDtos", title);
        request.setAttribute("tablename", tablename);
        request.setAttribute("djbh", qDto.getString("djbh"));
        request.setAttribute("recordid", qDto.getString("recordid"));
        String outString =AOSJson.toGridJson(title);
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
        List<Archive_tablenamePO> list = statisticsService.findTablename_children();
        String outString =AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
    }
    @RequestMapping(value="openGpy")
    public String openGpy(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        String id_ = qDto.getString("id");
        String djbh = qDto.getString("djbh");
        request.setAttribute("id_", id_);
        request.setAttribute("djbh", djbh);
        //return "aos/utilization/indexx(1).html";
       return "aos/utilization/PrintCookie.jsp";

    }
    @RequestMapping(value="upload")
    public void upload(HttpSession session, HttpServletRequest request, HttpServletResponse response, String[] base64)throws Exception{
        Dto qDto = Dtos.newDto(request);
        String id_ = qDto.getString("id_");
        String djbh = qDto.getString("djbh");
        Base64ToTXT(base64[0]+","+base64[1],"F:\\dataaos\\jsx\\"+djbh+".txt");
        //此时修改条目信息，把介绍信添加进去
       Dto outDto= utilizationService.updateDjMessage(id_,djbh+".txt");
        response.setHeader("ContentType","text/json");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject(); //创建Json对象
        jsonObject.put("appcode", 1);
        response.getWriter().print(jsonObject.toString());
    }
    private void Base64ToTXT( String imagesrc,String djpath) {
        try{
            File fileall=new File(djpath);
            if(!fileall.exists()){
                fileall.createNewFile();
            }
            PrintStream ps = new PrintStream(new FileOutputStream(fileall));
            ps.println(imagesrc);// 往文件里写入字符串
            ps.close();
        }catch (Exception e){
        }
    }


    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
        /**
         * base64字符串转换成图片 (对字节数组字符串进行Base64解码并生成图片)
         * @param imgStr		base64字符串
         * @param imgFilePath	指定图片存放路径  （注意：带文件名）
         * @return
         */
    public static boolean Base64ToImage(String imgStr,String imgFilePath) {
        if (StringUtils.isEmpty(imgStr)) // 图像数据为空
            return false;
        try {
            //imgStr=imgStr.substring(23,imgStr.length());
            //判断文件是否存在，不存在就创建
            File file=new File(imgFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // Base64解码
            byte[] b = Base64Utils.decodeFromString(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     *
     * @param imageFolderPath
     *            图片文件夹地址
     * @param pdfPath
     *            PDF文件保存地址
     *
     */
    public static void toPdf(String imageFolderPath, String pdfPath) {
        try {
            //判断pdf是否存在不存在创建
            //判断文件是否存在，不存在就创建
            File pdffile=new File(pdfPath);
            if (!pdffile.exists()) {
                pdffile.createNewFile();
            }
            // 图片文件夹地址
            // String imageFolderPath = "D:/Demo/ceshi/";
            // 图片地址
            String imagePath = null;
            // PDF文件保存地址
            // String pdfPath = "D:/Demo/ceshi/hebing.pdf";
            // 输入流
            FileOutputStream fos = new FileOutputStream(pdfPath);
            // 创建文档
            Document doc = new Document(null, 0, 0, 0, 0);
            //doc.open();
            // 写入PDF文档
            PdfWriter.getInstance(doc, fos);
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;
            // 获取图片文件夹对象
            File file = new File(imageFolderPath);
            File[] files = file.listFiles();
            // 循环获取图片文件夹内的图片
            for (File file1 : files) {
                if (file1.getName().endsWith(".jpg")) {
                    // System.out.println(file1.getName());
                    imagePath = imageFolderPath + file1.getName();
                    System.out.println(file1.getName());
                    // 读取图片流
                    img = ImageIO.read(new File(imagePath));
                    // 根据图片大小设置文档大小
                    doc.setPageSize(new Rectangle(img.getWidth(), img
                            .getHeight()));
                    // 实例化图片
                    image = Image.getInstance(imagePath);
                    // 添加图片到文档
                    doc.open();
                    doc.add(image);
                }
            }
            // 关闭文档
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * 获得表单录入项column布局
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getInputColumn")
    public void getInputColumn(HttpServletRequest request,
                               HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        List<Archive_tableinputPO> list=new ArrayList<Archive_tableinputPO>();
        try{
            list = archive_tableinputMapper.listColumn(inDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        WebCxt.write(response, AOSJson.toGridJson(list));

    }

    /**
     *
     * 获得单条信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getData")
    public void getData(HttpServletRequest request, HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        Object outDto = dataService.getData(qDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    /**
     * 删除当前任务的档案
     * @param request
     * @param response
     */
    @RequestMapping(value="delArchive_make")
    public void delArchive_make(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = Dtos.newDto();
        boolean b = utilizationService.delArchive_make(qDto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     * 查档编号
     * @param request
     * @param response
     */
    @RequestMapping(value="getMakeIndex")
    public void getMakeIndex(HttpServletRequest request, HttpServletResponse response){
        Dto outDto = Dtos.newDto(request);
        String nd = outDto.getString("nd");
        String index = utilizationService.getMakeIndex(nd);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value="initMakeLeader")
    public String initMakeLeader(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        return "aos/utilization/initZzLeader.jsp";

    }

    @RequestMapping("addSFXX")
    public void addSFXX(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto dto = Dtos.newDto(request);
        Dto outDto = basicSearchService.addSFXX(dto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping("updateMakeDetail_select")
    public void updateMakeDetail_select(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto dto = Dtos.newDto(request);
        Dto outDto = basicSearchService.updateMakeDetail_select(dto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
}
