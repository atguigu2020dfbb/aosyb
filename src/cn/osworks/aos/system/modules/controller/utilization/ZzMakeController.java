package cn.osworks.aos.system.modules.controller.utilization;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_remote_logMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tableinputMapper;
import cn.osworks.aos.system.dao.mapper.Archive_zzremoteMapper;
import cn.osworks.aos.system.dao.mapper.Archive_zzremote_detailMapper;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.BasicSearchService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.log.LogService;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import cn.osworks.aos.system.modules.service.utilization.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping(value = "utilization/data")
public class ZzMakeController {

    @Autowired
    private LogService logService;

    @Autowired
    private DataService dataService;

    @Autowired
    private DocService docService;

    @Autowired
    private BasicSearchService basicSearchService;

    @Autowired
    private UtilizationService utilizationService;

    @Autowired
    private Archive_zzremoteMapper archive_zzremoteMapper;
    @Autowired
    private Archive_tableinputMapper archive_tableinputMapper;

    @Autowired
    private Archive_zzremote_detailMapper archive_zzremote_detailMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Archive_remote_logMapper archive_remote_logMapper;

    /**
     *
     * 初始化
     *
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "zz_initIndex")
    public String zz_initIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        //自助查档设定用户登录
        UserInfoVO userInfoVO=new UserInfoVO();
        userInfoVO.setAccount_("root");
        userInfoVO.setName_("超级管理员");
        userInfoVO.setId_("fa04db9dd2f54d61b0c8202a25de2dc6");
        session.setAttribute(AOSCons.USERINFOKEY, userInfoVO);
        saveSessionLog(request, session, userInfoVO);
        return "aos/selfquery/index.jsp";
    }
    /**
     *
     * 初始化
     *
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "zizhu_initIndex")
    public String zizhu_initIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        //自助查档设定用户登录

        return "aos/selfquery/zizhu_index.jsp";
    }
    /**
     * 保存会话日志
     *
     * @param request
     * @param session
     * @param userInfoVO
     */
    private void saveSessionLog(HttpServletRequest request, HttpSession session, UserInfoVO userInfoVO) {
        try{
            Aos_log_sessionPO aos_log_sessionPO = new Aos_log_sessionPO();
            aos_log_sessionPO.setId_(session.getId());
            aos_log_sessionPO.setUser_id_(userInfoVO.getId_());
            aos_log_sessionPO.setAccount_(userInfoVO.getAccount_());
            aos_log_sessionPO.setUser_name_(userInfoVO.getName_());
            aos_log_sessionPO.setClient_type_(request.getHeader("USER-AGENT"));
            aos_log_sessionPO.setIp_address_(WebCxt.getClientIpAddr(request));
            aos_log_sessionPO.setOwner_(String.valueOf(request.getServerPort()));
            logService.saveSessionLog(aos_log_sessionPO);
        }catch (Exception e){

        }

    }
    /**
     *
     * 页面初始化
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "zz_initData")
    public String zz_initData(HttpSession session, HttpServletRequest request,
                              HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(qDto.getString("tablename"));
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        String pagesize = userInfoVO.getPagesize() + "";
        if (pagesize == null) {
            pagesize = "20";
        }
        request.setAttribute("tablename", qDto.getString("tablename"));
        session.setAttribute("tablename", qDto.getString("tablename"));
        //session.setAttribute("queryclass", queryclass);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        return "aos/selfquery/initData.jsp";
    }
    /**
     *
     * 自助查档页面初始化
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "zizhu_initData")
    public String zizhu_initData(HttpSession session, HttpServletRequest request,
                              HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        String user=qDto.getString("user");
        String mima=qDto.getString("mima");
        String id_=qDto.getString("zz_id_");
        if(user!=null&&user!=""&&mima!=null&&mima!=""){
            UserInfoVO userInfoVO=new UserInfoVO();
            userInfoVO.setName_(user);
            userInfoVO.setAccount_(user);
            userInfoVO.setId_("21312342543546");
            session.setAttribute(AOSCons.USERINFOKEY, userInfoVO);
            saveSessionLog(request, session, userInfoVO);
            //再把用户登录信息存到数据表中
           id_= basicSearchService.addzizhuMessage(userInfoVO);
        }
        String tablename = qDto.getString("tablename");
        String tabledesc = qDto.getString("tabledesc");
        List<Archive_tablefieldlistPO> title = basicSearchService.getDataFieldListTitle(tablename);
        List<Archive_tablefieldlist_childrenPO> titleDtos = dataService
                .getDataFieldListTitle_children(qDto.getString("tablename"));
        request.setAttribute("fieldDtos", title);
        request.setAttribute("user",user);
        request.setAttribute("zz_id_",id_);
        request.setAttribute("tablename",qDto.getString("tablename"));
        request.setAttribute("tabledesc",qDto.getString("tabledesc"));
        return "aos/selfquery/zizhuinitData.jsp";
    }
    /**
     *
     * 保存自助查档用户登录信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "saveZiZhuMessage")
    public void saveZiZhuMessage(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
        boolean b=utilizationService.addZiZhuMessage(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value="zz_listAccounts")
    public void zz_listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){

        Dto inDto = Dtos.newDto(request);
        String flag=inDto.getString("flag");
        if("0".equals(flag)){
            //标题列表
            List<Dto> fieldDtos = new ArrayList<Dto>();
            int pCount =0;
            String query = inDto.getString("query");
            request.setAttribute("_total", pCount);
            request.setAttribute("query",query);
            session.setAttribute("query",query);
            //条目数据
            String outString = AOSJson.toGridJson(fieldDtos, 0);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }else{
            //标题列表
            List<Dto> fieldDtos = basicSearchService.getDataFieldListDisplayAll_zz(inDto,session);
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

    }
    /**
     * 计算ID
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "zizhucalcId")
    public void zizhucalcId(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
        String id = utilizationService.zizhucalcId(dto);
        out.put("index",id);
        WebCxt.write(response, AOSJson.toJson(out));
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

    @RequestMapping("addSFXX")
    public void addSFXX(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto dto = Dtos.newDto(request);
        Dto outDto = utilizationService.addSFXX(dto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listConsult")
    public void listConsult(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        inDto.put("state","2");
        List<Archive_zzremotePO> list = archive_zzremoteMapper.likePage(inDto);
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
    @RequestMapping(value ="TjsqConsult")
    public void TjsqConsult(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        utilizationService.updateZzremote(inDto);
        Dto outDto = utilizationService.addzz_Dtail(inDto);
        utilizationService.addChumo(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));

    }


    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="contrast")
    public String contrast(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);

        Dto inDto = Dtos.newDto();
        inDto.put("formid",qDto.getString("id_"));
        Archive_zzremote_detailPO archive_zzremote_detailPO= archive_zzremote_detailMapper.selectOne(inDto);
        //同时修改审核标记
        Archive_zzremotePO archive_zzremotePO=new Archive_zzremotePO();
        archive_zzremotePO.setId_(qDto.getString("id_"));
        archive_zzremotePO.setState("3");
        archive_zzremoteMapper.updateByKey(archive_zzremotePO);
        String tablename = archive_zzremote_detailPO.getTablename();
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(tablename);
        request.setAttribute("tablename", tablename);
        //session.setAttribute("queryclass", queryclass);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("formid", qDto.getString("id_"));
        return "aos/selfquery/initContrast.jsp";
    }

    @RequestMapping(value = "listAccounts")
    public void listAccounts(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        //String query = "  and id_ in('4fbae7424a5340b1ae2b61ee8e88e6a6')";
        String query = utilizationService.joinQueryShq(inDto);
        inDto.put("query",query);
        List<Map<String,Object>> fieldDtos = basicSearchService.getDataFieldListDisplayAllCd(inDto,session);
        //上面的状态添加到下面
        for(int i=0;i<fieldDtos.size();i++){
            fieldDtos.get(i).put("tablename",inDto.getString("tablename"));
        }
        //int pCount = basicSearchService.getPageTotal(inDto);
        //String query = inDto.getString("query");
        request.setAttribute("_total", fieldDtos.size());
        request.setAttribute("query",query);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, AOSJson.toGridJson(fieldDtos, fieldDtos.size()));
    }

    @RequestMapping(value = "listDocument")
    public void listDocument(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        List<Map<String, Object>> list=utilizationService.getDocumentPathShq(inDto);
        WebCxt.write(response,AOSJson.toGridJson(list, list.size()));
    }
    @RequestMapping(value = "getDocumentPath")
    public void getDocumentPath(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        String path=utilizationService.getDocumentPath(inDto);
        path = "F:/dataaos/"+path;
       // path = path;
        String pdfPath=null;
        Dto outDto=Dtos.newDto();
        try {
            pdfPath=docService.tifToPdfShq(path,"");
        } catch (Exception e) {
            e.printStackTrace();
            outDto.setAppCode(AOSCons.ERROR);
            return;
        }
        outDto.setAppCode(AOSCons.SUCCESS);
        pdfPath=pdfPath.substring(11);
        outDto.put("pdfpath",pdfPath);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "getform")
    public void getform(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        Archive_zzremotePO archive_zzremotePO = archive_zzremoteMapper.selectByKey(inDto.getString("id_"));
        WebCxt.write(response,AOSJson.toJson(archive_zzremotePO));
    }
    @RequestMapping(value="listAccounts_wsda")
    public void listAccounts_wsda(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("null").equals(inDto.getString("tablename"))||("").equals(inDto.getString("tablename"))){
            //标题列表
            List<Map<String,Object>> fieldDtos = new ArrayList<Map<String,Object>>();
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
            List<Map<String,Object>> fieldDtos =null;
            int pCount=0;
            if("1".equals(flag)){
                //标题列表
                fieldDtos = basicSearchService.getDataFieldListDisplayAll_zizhu(inDto,session);
                pCount= basicSearchService.getPageTotal4(inDto);
            }

            String query = inDto.getString("query");
            request.setAttribute("_total", pCount);
            request.setAttribute("query",query);
            //条目数据
            String outString = AOSJson.toGridJson(fieldDtos, pCount);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }
    }

    @RequestMapping(value="listZiZhuMake")
    public void listZiZhuMake(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //更新自助查档表
        List<Map<String,Object>> list=basicSearchService.listZiZhuMake(inDto);
        int pCount=basicSearchService.getPageTotal_ZizhuMake(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(list, pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping(value="getPictureLook")
    public void getPictureLook(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
        //结合用户选择的数据信息在任务表查询是否存在并且可以访问。
        //更新自助查档表
       // basicSearchService.updateZizhuMessage(inDto);

        boolean b=basicSearchService.getZizhuMessage(inDto);
        //条目数据
        if(b){
            out.setAppCode(AOSCons.SUCCESS);
        }else{
            out.setAppCode(AOSCons.ERROR);
        }
        // 就这样返回转换为Json后返回界面就可以了。
        String outString = AOSJson.toJson(out);
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
        List<Archive_tablenamePO> list = basicSearchService.findTablename_zizhu();
        String outString =AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
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
    /**
     * 获取记忆
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     * 2019-4-30
     */
    @RequestMapping("getRemember")
    public void getRemember(HttpServletRequest request,
                            HttpServletResponse response,HttpSession session) {
        Dto inDto = Dtos.newDto(request);
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        String user = userInfoVO.getAccount_();
        Map<String,Object> map=dataService.getRemember(inDto, userInfoVO);
        String outString = AOSJson.toJson(map);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
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
        String temp = "";
        Dto inDto = Dtos.newDto(request);
        String tablename=inDto.getString("tablename");
        Properties prop = PropertiesLoaderUtils
                .loadAllProperties("config.properties");
        String linkAddress = prop.getProperty("linkAddress");
        String filePath = prop.getProperty("filePath");
        String path1 = dataService.getDocumentPath(inDto);
        //添加表明
        //String path = linkAddress  +tablename+"//"+ path1;
        //组合本地路径进行判断
        String bendipath=filePath+path1;
        //无需添加
        String path = linkAddress  + path1;
        //initData
        //此时判断路径jpg tif pdf

        if (path1.indexOf("jpg") > -1
                || path1.indexOf("JPG") > -1) {
            // String filepath=prop.getProperty("filePath");
            path=docService.jpgToPdf(bendipath,"");
            path=linkAddress+"temp/temp.pdf";
        }
        if (path1.indexOf("tif") > -1
                || path1.indexOf("TIF") > -1) {
            path=docService.tifToPdf(bendipath,"");
            path=linkAddress+"temp/temp.pdf";
        }
        if (path1.indexOf("pdf") > -1
                || path1.indexOf("PDF") > -1) {

        }

        List<Archive_tablefieldlistPO> titleDtos = dataService.getInfoFieldListTitle(inDto.getString("tablename") + "_info");
        request.setAttribute("tablename", inDto.getString("tablename"));
        request.setAttribute("id", inDto.getString("id"));
        request.setAttribute("strswf", path);
        request.setAttribute("tid", inDto.getString("tid"));
        request.setAttribute("fieldDtos", titleDtos);
        return "common/documentView.jsp";
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
        String tid=inDto.getString("tid");
        //当前compilation的id值
        String id_=inDto.getString("id_");
        String tablename=inDto.getString("tablename");
        String path=utilizationService.getpath(tid,id_,tablename);
        Dto dto = Dtos.newDto();
        dto.put("pdfpath",path);
        //条目数据
        String outString = AOSJson.toJson(dto);
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
    @RequestMapping("shenqing_print")
    public void shenqing_print(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto dto=utilizationService.shenqing_print(inDto);
        //条目数据
        String outString = AOSJson.toJson(dto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listZiZhuMakedetail")
    public void listZiZhuMakedetail(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        List<Map<String,Object>> fieldDtos = basicSearchService.getDataFieldListDisplay_ZiZhu(inDto);
        //fieldDtos.get();
        int pCount = basicSearchService.getPageTotalData_zizhu(inDto);
        request.setAttribute("_total", pCount);
        WebCxt.write(response,AOSJson.toGridJson(fieldDtos,pCount));
    }
    /**
     *
     * 查询表数
     */
    @RequestMapping(value ="initzizhuLeader")
    public String initzizhuLeader(HttpSession session,HttpServletRequest request,HttpServletResponse response){
       return "aos/selfquery/initzizhuLeader.jsp";
    }
    /**
     *
     * 修改提供状态
     */
    @RequestMapping(value ="updateZiZhuMake")
    public void updateZiZhuMake(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateZiZhuUtilization(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     *
     * 申请看电子文件
     */
    @RequestMapping(value ="shenqing_file")
    public void shenqing_file(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.shenqing_file(inDto);
        //同时给管理员用户发送审核标记信息
        notificationService.TaskZizhuLy(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     *
     * 已查到
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "insertMakeDetail")
    public void insertMakeDetail(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        Dto outDto =utilizationService.insertUtilizationMake(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping(value="deleteZiZhuMake")
    public void deleteZiZhuMake(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = utilizationService.deleteZiZhuMake(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }
    @RequestMapping(value = "listMakeLog_make")
    public void listMakeLog_make(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        qDto.setOrder("czsj DESC");
        List<Archive_remote_logPO> list = archive_remote_logMapper.list(qDto);
        String outString = AOSJson.toGridJson(list,qDto.getPageTotal());
        WebCxt.write(response,outString);
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

}
