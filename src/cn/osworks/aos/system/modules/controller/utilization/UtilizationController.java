package cn.osworks.aos.system.modules.controller.utilization;


import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_remoteMapper;
import cn.osworks.aos.system.dao.mapper.Archive_remote_advanceMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tableinputMapper;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.SystemService;
import cn.osworks.aos.system.modules.service.archive.BasicSearchService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.StatisticsService;
import cn.osworks.aos.system.modules.service.utilization.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "utilization")
public class UtilizationController extends JdbcDaoSupport {

    @Autowired
    private Archive_remoteMapper archive_remoteMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    @Autowired
    private UtilizationService utilizationService;

    @Autowired
    private BasicSearchService basicSearchService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private DataService dataService;

    @Autowired
    private Archive_remote_advanceMapper archive_remote_advanceMapper;
    @Autowired
    private Archive_tableinputMapper archive_tableinputMapper;
    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value="initUtilization")
    public String initUtilization(HttpServletRequest request, HttpServletResponse response){
        return "aos/utilization/initUtilization.jsp";
    }


    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listUtilization")
    public void listUtilization(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        List<Archive_remotePO> list = archive_remoteMapper.likePage(inDto);
        String outString = AOSJson.toGridJson(list,inDto.getPageTotal());
        WebCxt.write(response,outString);

    }

    @RequestMapping(value="saveUtilization")
    public void saveUtilization(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = utilizationService.saveUtilization(qDto);
        WebCxt.write(response,AOSJson.toJson(outDto));

    }

    @RequestMapping(value = "updateUtilization")
    public void updateUtilization(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateUtilization(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "updateUtilizationDd")
    public void updateUtilizationDd(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = utilizationService.updateUtilization(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping( value = "updateUtilizationAgree")
    public void updateUtilizationAgree(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        inDto.put("spr",inDto.getUserInfo().getName_());
        inDto.put("spsj",sd.format(new Date()));
        Dto outDto = utilizationService.updateUtilization(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }


    @RequestMapping(value="deleteUtilization")
    public void deleteUtilization(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = utilizationService.deleteUtilization(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    @RequestMapping(value = "getAPPID")
    public void getAPPID(HttpServletRequest request,HttpServletResponse response){
        Dto outDto = Dtos.newDto();
        String djbh = AOSId.id("登记流水号");
        outDto.put("djbh",djbh);
        WebCxt.write(response,AOSJson.toJson(outDto));

    }

    @RequestMapping("loadData")
    public String initInput(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        Dto dto=Dtos.newDto(request);
        String id_=dto.getString("id_");
        String name_=dto.getString("name_");
        String djbh = dto.getString("djbh");
        String listtablename=basicSearchService.findModule(id_);
        if(listtablename==null||listtablename.equals("")||listtablename.equals("null")){
            if(name_==null||name_=="null"||name_.equals("")){
                name_="请选择档案门类";
            }
            request.setAttribute("id_",id_);
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
        return "aos/utilization/loadData.jsp?time="+new Date().getTime();
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
    @RequestMapping(value="listAccounts")
    public void listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        String id_=inDto.getString("id_");
        String listtablename=basicSearchService.findModule(id_);
        if(listtablename.equals("null")||listtablename.equals("")||listtablename==null){
            return;
        }
        inDto.put("tablename", listtablename);
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
    @RequestMapping(value="listAccounts5")
    public void listAccounts5(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        String tablename=inDto.getString("tablename");
        inDto.put("tablename", tablename);

        if(("null").equals(inDto.getString("tablename"))){
            //标题列表
            List<Dto> fieldDtos = new ArrayList<Dto>();
            int pCount =0;
            String query = inDto.getString("query");
            request.setAttribute("_total", pCount);
            request.setAttribute("query",query);
            request.setAttribute("tablename",tablename);
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
                fieldDtos = basicSearchService.getDataFieldListDisplayAll(inDto,session);
                pCount = basicSearchService.getPageTotal(inDto);
            }else{

            }
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
    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listDjform")
    public void listDjform(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        List<Archive_remotePO> list = archive_remoteMapper.like(inDto);
        String outString = AOSJson.toGridJson(list,inDto.getPageTotal());
        WebCxt.write(response,outString);

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
        String id_=inDto.getString("tablename");
        String listtablename=basicSearchService.findModule(id_);
        inDto.put("tablename",listtablename);
        List<Dto> pathDtos = dataService.getPath(inDto);
        String outString = AOSJson.toGridJson(pathDtos);
        WebCxt.write(response, outString);
    }
    /**
     *
     * 获得电子文件信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getPath2")
    public void getPath2(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        String id_=inDto.getString("tablename");
        inDto.put("tablename",id_);
        List<Dto> pathDtos = dataService.getPath(inDto);
        String outString = AOSJson.toGridJson(pathDtos);
        WebCxt.write(response, outString);
    }
    /**
     *
     * 已查到
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "insertUtilizationDetail")
    public void insertUtilizationDetail(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        Dto outDto =utilizationService.insertUtilizationDetail(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }


    /**
     *
     * 查询登记详情
     *
     * @param request
     * @param response
     */
    public void listUtilizationDetails(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);


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

    @RequestMapping(value = "listDjxq")
    public void listDjxq(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        //String query = "  and id_ in('4fbae7424a5340b1ae2b61ee8e88e6a6')";
        String query = utilizationService.joinQuery(inDto);
        inDto.put("query",query);
        List<Map<String,Object>> fieldDtos = basicSearchService.getDataFieldListDisplayAllCd(inDto,session);
        //上面的状态添加到下面
        for(int i=0;i<fieldDtos.size();i++){
            fieldDtos.get(i).put("tablename",inDto.getString("tablename"));
        }
        int pCount = basicSearchService.getPageTotal(inDto);
        //String query = inDto.getString("query");
        request.setAttribute("_total", pCount);
        request.setAttribute("query",query);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, AOSJson.toGridJson(fieldDtos, pCount));
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
    @RequestMapping(value="initLeader")
    public String initLeader(HttpServletRequest request, HttpServletResponse response){
        return "aos/utilization/initLeader.jsp";
    }

    @RequestMapping(value="initManager")
    public String initManager(HttpServletRequest request, HttpServletResponse response){
        return "aos/utilization/initManager.jsp";
    }

    @RequestMapping(value="initSatisfied")
    public String initSatisfied(HttpServletRequest request, HttpServletResponse response){
        return "aos/utilization/initSatisfied.jsp";
    }

    @RequestMapping(value="initAdvance")
    public String initAdvance(HttpServletRequest request, HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        request.setAttribute("user",inDto.getUserInfo().getName_());
        return "aos/utilization/initAdvance.jsp";
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
     *
     * @param response
     * @param request
     */
    @RequestMapping(value = "yyTojd")
    public void yyTojd(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        boolean b=utilizationService.yyTojd(qDto);
        Dto out = Dtos.newDto();
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        String outString = AOSJson.toJson(out);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
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

    /**
     * 档案详情列表
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("loadData_error")
    public String archive_details(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        String id_=qDto.getString("id_");
        String djbh=qDto.getString("djbh");
        //根据id_
        //String tablename=compilationService.findTablename(id_);
       String tablename= utilizationService.findTablename(id_);
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
        request.setAttribute("djbh",djbh);
        request.setAttribute("pagesize", pagesize);
        request.setAttribute("error_id_", id_);
        return "aos/utilization/errorMessage.jsp";
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
        List<Map<String,Object>> fieldDtos =new ArrayList<Map<String,Object>>();
        String tablename=inDto.getString("tablename");
        if(tablename==null||tablename==""||tablename.equals("")){

        }else{
            fieldDtos = utilizationService.getDataFieldListDisplayAll2(inDto,session);
        }
        String outString = AOSJson.toGridJson(fieldDtos);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    /**
     * 保存错误登记
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("saveErrorRegister")
    public void saveErrorRegister(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=utilizationService.saveErrorRegister(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        String outString = AOSJson.toJson(out);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     * 错误列表
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("list_error")
    public String list_error(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        return "aos/utilization/list_errorMessage.jsp";
    }
    @RequestMapping("updateErrorRegister")
    public void updateErrorRegister(HttpServletRequest request,
                                    HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=  utilizationService.updateErrorRegister(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        String outString = AOSJson.toJson(out);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    /**
     *
     * 查询数据信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listAccounts3")
    public void listAccounts3(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        Dto inDto = Dtos.newDto(request);
        inDto.put("tablename", inDto.getString("tablename"));
        List<Map<String,Object>> fieldDtos = utilizationService.getDataFieldListDisplayAll3(inDto,session);
        String outString = AOSJson.toGridJson(fieldDtos);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping("getData")
    public String getData(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        Dto dto=Dtos.newDto(request);
        String id_=dto.getString("recordid");
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
            return "aos/utilization/getData.jsp?time="+new Date().getTime();
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
        return "aos/utilization/getData.jsp?time="+new Date().getTime();
    }

    @RequestMapping(value="getBasicSearch")
    public String getBasicSearch(HttpServletRequest request,HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        UserInfoVO userInfoVO=dto.getUserInfo();
        String user=userInfoVO.getAccount_();
        request.setAttribute("user",user);
        request.setAttribute("djbh", dto.getString("djbh"));
        request.setAttribute("tablename",dto.getString("tablename"));
        request.setAttribute("recordid", dto.getString("recordid"));
        return "aos/utilization/getbasicSearch.jsp";
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
        List<Archive_tablenamePO> list = statisticsService.findTablename();
        String outString =AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
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
        return "aos/utilization/getbasicSearch.jsp";
    }
    /**
     * 预约编号
     * @param request
     * @param response
     */
    @RequestMapping(value="getAdvanceIndex")
    public void getAdvanceIndex(HttpServletRequest request, HttpServletResponse response){
        Dto outDto = Dtos.newDto(request);
        String nd = outDto.getString("nd");
        String index = utilizationService.getAdvanceIndex(nd);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }


}
