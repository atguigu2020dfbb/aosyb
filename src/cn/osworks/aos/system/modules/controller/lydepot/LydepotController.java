package cn.osworks.aos.system.modules.controller.lydepot;

import cn.osworks.aos.core.asset.*;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.SystemService;
import cn.osworks.aos.system.modules.service.archive.BasicSearchService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.StatisticsService;
import cn.osworks.aos.system.modules.service.lydepot.LydepotService;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import cn.osworks.aos.system.modules.service.utilization.UtilizationService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.osworks.aos.system.modules.controller.archive.DataController.filePath;

@Controller
@RequestMapping(value="archive/lydepot")
public class LydepotController  extends JdbcDaoSupport {

    @Autowired
    private Archive_remoteMapper archive_remoteMapper;

    @Autowired
    private Archive_zz_applyMapper archive_zz_applyMapper;
    @Autowired
    private UtilizationService utilizationService;
    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }


    @Autowired
    private BasicSearchService basicSearchService;
    @Autowired
    private LydepotService lydepotService;

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
        Dto inDto = Dtos.newDto(request);
        List<Archive_remotePO> list = archive_remoteMapper.likePage(inDto);
        String outString = AOSJson.toGridJson(list,inDto.getPageTotal());
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
        imgtext=imgtext.substring(str1.length(), imgtext.length());
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
     * 在线发布
     * @param request
     * @param response
     */
    @RequestMapping(value = "updateDetails_zxfb")
    public void updateDetails_zxfb(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=lydepotService.updateDetails_zxfb(inDto);
        if(b){
            out.setAppCode(1);
            out.setAppMsg("发布成功!");
        }else{
            out.setAppCode(-1);
            out.setAppMsg("发布失败!");
        }
        WebCxt.write(response, AOSJson.toJson(out));
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
    @RequestMapping("getComboboxList")
    public void getComboboxList(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> list =lydepotService.getComboboxList(inDto);
        String outString = AOSJson.toGridJson(list);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping("getQueryIds")
    public void getQueryIds(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        List<Map<String,Object>> list= lydepotService.getQueryIds(inDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    @RequestMapping("updatehlw_data")
    public void updatehlw_data(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=lydepotService.updatehlw_data(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping("updatesjfb_data")
    public void updatesjfb_data(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=lydepotService.updatesjfb_data(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping("updatejyw_data")
    public void updatejyw_data(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=lydepotService.updatejyw_data(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping("updatezww_data")
    public void updatezww_data(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=lydepotService.updatezww_data(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value="listdata_edit")
    public void listdata_edit(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("null").equals(inDto.getString("tablename"))||"undefined".equals(inDto.getString("tablename"))){
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
            //标题列表
            List<Dto> fieldDtos = lydepotService.getDataFieldListDisplayAll4(inDto,session);
            int pCount = lydepotService.getPageTotal3(inDto);
            String query = inDto.getString("query");
            request.setAttribute("_total", pCount);
            request.setAttribute("query",query);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(fieldDtos, pCount);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }

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
        String query = lydepotService.queryConditions3(out);
        Dto outDto = Dtos.newDto();
        outDto.setAppMsg(query);
        WebCxt.write(response, AOSJson.toJson(outDto));
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
    @RequestMapping("saveQueryData_sjfb")
    public void saveQueryData_sjfb(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session) {
        Dto out = Dtos.newDto(request);
        String query = lydepotService.queryConditions3_sjfb(out);
        Dto outDto = Dtos.newDto();
        outDto.setAppMsg(query);
        WebCxt.write(response, AOSJson.toJson(outDto));
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
    @RequestMapping("saveQueryData_list_sjfb")
    public void saveQueryData_list_sjfb(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session) {
        Dto out = Dtos.newDto(request);
        String query = lydepotService.queryConditions3_list_sjfb(out);
        Dto outDto = Dtos.newDto();
        outDto.setAppMsg(query);
        WebCxt.write(response, AOSJson.toJson(outDto));
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
    @RequestMapping("saveQueryData_hlw")
    public void saveQueryData_hlw(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session) {
        Dto out = Dtos.newDto(request);
        String query = lydepotService.queryConditions_tablenameid_hlw(out);
        Dto outDto = Dtos.newDto();
        outDto.setAppMsg(query);
        WebCxt.write(response, AOSJson.toJson(outDto));
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
    @RequestMapping("saveQueryData_zww")
    public void saveQueryData_zww(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session) {
        Dto out = Dtos.newDto(request);
        String query = lydepotService.queryConditions_tablenameid_zww(out);
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
        if(("null").equals(inDto.getString("tablename"))||"undefined".equals(inDto.getString("tablename"))){
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
            //标题列表
            List<Dto> fieldDtos = lydepotService.getDataFieldListDisplayAll3(inDto,session);
            int pCount = lydepotService.getPageTotal2(inDto);
            String query = inDto.getString("query");
            request.setAttribute("_total", pCount);
            request.setAttribute("query",query);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(fieldDtos, pCount);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }
    }
    @RequestMapping(value="listAccounts_hlw")
    public void listAccounts_hlw(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("null").equals(inDto.getString("tablename"))||"undefined".equals(inDto.getString("tablename"))){
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
            //标题列表
            String tablename=inDto.getString("tablename");
            String query=lydepotService.queryConditions3_list_sjfb(inDto);
            // 排列方式，页码，每页条目数
            Integer limit = Integer.valueOf(inDto.getString("limit"));
            Integer page = Integer.valueOf(inDto.getString("page"));
            // 条目开始索引
            Integer pageStart = (page - 1) * (limit);
//            String sql="select * from "+tablename+" where id_ in (select tablename_id from lysjfb_detail_tablenameid  where 1=1 and pid='"+fbid+"'" +
//                    " and tablename='"+tablename+"' and lx='"+fbwd+"'  order by dh desc offset "+pageStart+" rows fetch next "+limit+" rows only)";
            String sql="select * from "+tablename+" t  left join lysjfb_detail_tablenameid l on t.id_=l.tablename_id left join lysjfb_detail ld on ld.id_=l.pid  where 1=1 and l.hlw='1' "+query+
                    " and l.tablename='"+tablename+"' order by l.dh desc offset "+pageStart+" rows fetch next "+limit+" rows only ";
            List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
            String sql2="select * from "+tablename+" t  left join lysjfb_detail_tablenameid l on t.id_=l.tablename_id left join lysjfb_detail ld on ld.id_=l.pid  where 1=1 and l.hlw='1' "+query+
                    " and l.tablename='"+tablename+"' order by l.dh desc ";
            List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
            request.setAttribute("_total", listAll.size());
            request.setAttribute("query",query);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(listDto, listAll.size());
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }
    }
    @RequestMapping(value="listdataSjfb_edit")
    public void listdataSjfb_edit(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("null").equals(inDto.getString("tablename"))||"undefined".equals(inDto.getString("tablename"))){
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
            String fbwd=inDto.getString("fbwd");
            String tablename=inDto.getString("tablename");
            String fbid=inDto.getString("fbid");
            // 排列方式，页码，每页条目数
            Integer limit = Integer.valueOf(inDto.getString("limit"));
            Integer page = Integer.valueOf(inDto.getString("page"));
            String query=lydepotService.queryConditions3_sjfb(inDto);
            // 条目开始索引
            Integer pageStart = (page - 1) * (limit);
//            String sql="select * from "+tablename+" where id_ not in (select tablename_id from lysjfb_detail_tablenameid  where 1=1 and" +
//                    " pid='"+fbid+"' and tablename='"+tablename+"' and lx='"+fbwd+"' ) "+query+"  " +
//                    "order by dh desc  offset "+pageStart+" rows fetch next "+limit+" rows only";
            String sql="select * from "+tablename+" t   left join"+
                    " lysjfb_detail_tablenameid l on t.id_=l.tablename_id  and" +
                    " l.pid='"+fbid+"' and l.tablename='"+tablename+"' and l.lx='"+fbwd+"' where 1=1 and l.tablename_id is  null   "+query +"order by t.dh desc" +
            "  offset "+pageStart+" rows fetch next "+limit+" rows only ";
            String sql2="select t.id_ from "+tablename+" t   left join"+
                    " lysjfb_detail_tablenameid l on t.id_=l.tablename_id  and" +
                    " l.pid='"+fbid+"' and l.tablename='"+tablename+"' and l.lx='"+fbwd+"' where 1=1 and l.tablename_id is  null "+query +"order by t.dh desc";
            List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);

            List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
            request.setAttribute("_total", listAll.size());
            request.setAttribute("query",query);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(listDto, listAll.size());
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }
    }
    @RequestMapping(value="listAccounts_sjfb")
    public void listAccounts_sjfb(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("null").equals(inDto.getString("tablename"))||"undefined".equals(inDto.getString("tablename"))){
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
            String fbwd=inDto.getString("fbwd");
            String tablename=inDto.getString("tablename");
            String fbid=inDto.getString("fbid");
            String query=lydepotService.queryConditions3_list_sjfb(inDto);
            // 排列方式，页码，每页条目数
            Integer limit = Integer.valueOf(inDto.getString("limit"));
            Integer page = Integer.valueOf(inDto.getString("page"));
            // 条目开始索引
            Integer pageStart = (page - 1) * (limit);
//            String sql="select * from "+tablename+" where id_ in (select tablename_id from lysjfb_detail_tablenameid  where 1=1 and pid='"+fbid+"'" +
//                    " and tablename='"+tablename+"' and lx='"+fbwd+"'  order by dh desc offset "+pageStart+" rows fetch next "+limit+" rows only)";
            String sql="select * from "+tablename+" t  left join lysjfb_detail_tablenameid l on t.id_=l.tablename_id  where 1=1  "+query+
                    " and l.tablename='"+tablename+"' and l.lx='"+fbwd+"' and l.pid='"+fbid+"'  order by l.dh desc offset "+pageStart+" rows fetch next "+limit+" rows only ";
            List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
            String sql2="select * from "+tablename+" t  left join lysjfb_detail_tablenameid l on t.id_=l.tablename_id where 1=1  "+query+
                    " and l.tablename='"+tablename+"' and l.lx='"+fbwd+"' and l.pid='"+fbid+"'";
            List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
            request.setAttribute("_total", listAll.size());
            request.setAttribute("query",query);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(listDto, listAll.size());
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }
    }
    /**
     * 查询数据发布任务列表
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("listDatils.jhtml")
    public void listDatils(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from lysjfb_detail  where 1=1  order by fbbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from lysjfb_detail ";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        //条目数据
        String outString = AOSJson.toGridJson(listDto, listAll.size());
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping(value="listAccounts_jyw")
    public void listAccounts_jyw(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("null").equals(inDto.getString("tablename"))||"undefined".equals(inDto.getString("tablename"))){
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
            //标题列表
            List<Dto> fieldDtos = lydepotService.getDataFieldListDisplayAll3_jyw(inDto,session);
            int pCount = lydepotService.getPageTotal2_jyw(inDto);
            String query = inDto.getString("query");
            request.setAttribute("_total", pCount);
            request.setAttribute("query",query);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(fieldDtos, pCount);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }
    }
    @RequestMapping(value="listAccounts_zww")
    public void listAccounts_zww(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("null").equals(inDto.getString("tablename"))||"undefined".equals(inDto.getString("tablename"))){
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
            //标题列表
            String tablename=inDto.getString("tablename");
            String query=lydepotService.queryConditions3_list_sjfb(inDto);
            // 排列方式，页码，每页条目数
            Integer limit = Integer.valueOf(inDto.getString("limit"));
            Integer page = Integer.valueOf(inDto.getString("page"));
            // 条目开始索引
            Integer pageStart = (page - 1) * (limit);
//            String sql="select * from "+tablename+" where id_ in (select tablename_id from lysjfb_detail_tablenameid  where 1=1 and pid='"+fbid+"'" +
//                    " and tablename='"+tablename+"' and lx='"+fbwd+"'  order by dh desc offset "+pageStart+" rows fetch next "+limit+" rows only)";
            String sql="select * from "+tablename+" t  left join lysjfb_detail_tablenameid l on t.id_=l.tablename_id left join lysjfb_detail ld on ld.id_=l.pid  where 1=1 and l.zww='1' "+query+
                    " and l.tablename='"+tablename+"' order by l.dh desc offset "+pageStart+" rows fetch next "+limit+" rows only ";
            List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
            String sql2="select * from "+tablename+" t  left join lysjfb_detail_tablenameid l on t.id_=l.tablename_id left join lysjfb_detail ld on ld.id_=l.pid  where 1=1 and l.zww='1' "+query+
                    " and l.tablename='"+tablename+"' order by l.dh desc ";
            List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
            request.setAttribute("_total", listAll.size());
            request.setAttribute("query",query);
            request.setAttribute("djbh",inDto.getString("djbh"));
            //条目数据
            String outString = AOSJson.toGridJson(listDto, listAll.size());
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }
    }
    /**
     * 下载电子文件
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("downloadPath")
    public void downloadPath(HttpServletRequest request,
                             HttpServletResponse response, HttpSession session)
            throws IOException {
        Dto dto = Dtos.newDto(request);
        Properties prop = PropertiesLoaderUtils
                .loadAllProperties("config.properties");
        String linkAddress = prop.getProperty("linkAddress");
        String filePath = prop.getProperty("filePath");
        // aos_rows_=ace1bf5e2b8f4c9a81f9422ed89a5eb6,, tablename=wsgdwj
        lydepotService.download_electronic_file(filePath, dto, response, request);

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
    @RequestMapping(value = "updateMakeDd")
    public void updateMakeDd(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateUtilization(inDto);
        String content=null;
        if(inDto.getString("state").equals("1")){
            content="提交申请";
        } if(inDto.getString("state").equals("2")){
            content="转实体库";
        }if(inDto.getString("ckzt").equals("2")){
            content="确认归还";
        }
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
        Dto inDto = Dtos.newDto(request);
        List<Archive_remote_advancePO> list = archive_remote_advanceMapper.listPage(inDto);
        String outString = AOSJson.toGridJson(list,inDto.getPageTotal());
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

    @RequestMapping(value="deleteDetails_hlw")
    public void deleteDetails_hlw(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
        try {
            String tablename=inDto.getString("tablename");
            String query=inDto.getString("query");
            String sql="update lysjfb_detail_tablenameid set hlw=' '  where 1=1  and tablename_id in ( select id_ from "+tablename+" where 1=1 "+query+")";
            jdbcTemplate.execute(sql);
            out.setAppCode(1);
        } catch (DataAccessException e) {
            e.printStackTrace();
            out.setAppCode(-1);
        }
        String outString = AOSJson.toJson(out);
        WebCxt.write(response,outString);

    }
    @RequestMapping(value="deleteDetails_zww")
    public void deleteDetails_zww(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
        try {
            String tablename=inDto.getString("tablename");
            String query=inDto.getString("query");
            String sql="update lysjfb_detail_tablenameid set zww=' '  where 1=1  and tablename_id in ( select id_ from "+tablename+" where 1=1 "+query+")";
            jdbcTemplate.execute(sql);
            out.setAppCode(1);
        } catch (DataAccessException e) {
            e.printStackTrace();
            out.setAppCode(-1);
        }
        String outString = AOSJson.toJson(out);
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
        String waterpath=utilizationService.addWater(inDto,path,filePath);
        String ftpwaterpath=linkAddress  +"//"+"water"+"//"+new File(waterpath).getName();
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

    /**
     *互联网
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="init")
    public String init(HttpServletRequest request,HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        UserInfoVO userInfoVO=dto.getUserInfo();
        String user=userInfoVO.getAccount_();
        String lx=dto.getString("lx");
        request.setAttribute("user",user);
        request.setAttribute("djbh", dto.getString("djbh"));
        request.setAttribute("tablename",dto.getString("tablename"));
        request.setAttribute("tabledesc",dto.getString("tabledesc"));
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(dto.getString("tablename"));
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("recordid", dto.getString("recordid"));
        if("jyw".equals(lx)){
            return "aos/lydepot/jywdata.jsp";
        }else if("zww".equals(lx)){
            return "aos/lydepot/zwwdata.jsp";
        }else if("hlw".equals(lx)){
            return "aos/lydepot/hlwdata.jsp";
        }
        return "aos/lydepot/hlwdata.jsp";
    }
    /**
     *互联网
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="initSjfb")
    public String initSjfb(HttpServletRequest request,HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        UserInfoVO userInfoVO=dto.getUserInfo();
        String user=userInfoVO.getAccount_();
        String lx=dto.getString("lx");
        request.setAttribute("user",user);
        request.setAttribute("djbh", dto.getString("djbh"));
        request.setAttribute("tablename",dto.getString("tablename"));
        request.setAttribute("tabledesc",dto.getString("tabledesc"));
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(dto.getString("tablename"));
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("recordid", dto.getString("recordid"));
        return "aos/lydepot/sjfbdata.jsp";
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
        request.setAttribute("fieldDtos", title);
        request.setAttribute("tablename", tablename);
        request.setAttribute("tabledesc", tabledesc);
        request.setAttribute("djbh", qDto.getString("djbh"));
        request.setAttribute("recordid", qDto.getString("recordid"));
        return "aos/utilization/basicSearch.jsp";
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
        List<Archive_tablenamePO> list = lydepotService.findTablename();
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
    @RequestMapping(value = "deleteAllData_sjfb")
    public void deleteAllData_sjfb(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session) {
        Dto qDto = Dtos.newDto(request);
        Dto outDto = lydepotService.deleteAllData_sjfb(qDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "deleteAllData_jyw")
    public void deleteAllData_jyw(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session) {
        Dto qDto = Dtos.newDto(request);
        Dto outDto = lydepotService.deleteAllData_jyw(qDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "deleteAllData_hlw")
    public void deleteAllData_hlw(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session) {
        Dto qDto = Dtos.newDto(request);
        Dto outDto = lydepotService.deleteAllData_hlw(qDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "deleteAllData_zww")
    public void deleteAllData_zww(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session) {
        Dto qDto = Dtos.newDto(request);
        Dto outDto = lydepotService.deleteAllData_zww(qDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "saveDetail")
    public void saveDetail(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session){
        Dto out = Dtos.newDto(request);
        Dto dto = Dtos.newDto();
        boolean b=lydepotService.saveDetail(out);
        if(b){
            dto.setAppCode(AOSCons.SUCCESS);
            dto.setAppMsg("操作成功!");
        }else{
            dto.setAppCode(AOSCons.ERROR);
            dto.setAppMsg("操作失败!");
        }
        WebCxt.write(response,AOSJson.toJson(dto));
    }
    @RequestMapping(value = "updateDetail")
    public void updateDetail(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session){
        Dto out = Dtos.newDto(request);
        Dto dto = Dtos.newDto();
        boolean b=lydepotService.updateDetail(out);
        if(b){
            dto.setAppCode(AOSCons.SUCCESS);
            dto.setAppMsg("修改成功!");
        }else{
            dto.setAppCode(AOSCons.ERROR);
            dto.setAppMsg("修改失败!");
        }
        WebCxt.write(response,AOSJson.toJson(dto));
    }
    @RequestMapping(value = "deleteDetails")
    public void deleteDetails(HttpServletRequest request,
                             HttpServletResponse response, HttpSession session){
        Dto out = Dtos.newDto(request);
        Dto dto=lydepotService.deleteDetails(out);
        WebCxt.write(response,AOSJson.toJson(dto));
    }
    @RequestMapping(value = "getDetail")
    public void getDetail(HttpServletRequest request,
                          HttpServletResponse response, HttpSession session){
        Dto out = Dtos.newDto(request);
        Object dto = lydepotService.getDetail(out);
        WebCxt.write(response,AOSJson.toJson(dto));
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
        List<Map<String,Object>> list = lydepotService.exportData(qDto);
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle2(qDto.getString("tablename"));
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("导出信息表");
        // 设置报表集合
        reportModel.setFieldsList(list);
        reportModel.setExcelHeader(titleDtos);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }
    @RequestMapping("getfbspath")
    public void getChengGuopath(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto dto = Dtos.newDto();
        //条目数据id值
        String id_=inDto.getString("id_");
        //当前compilation的id值
        String filename=inDto.getString("filename");
        String filepath=lydepotService.getfbspath(id_,filename);
        //条目数据
        dto.put("pdfpath",filepath);
        String outString = AOSJson.toJson(dto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
}
