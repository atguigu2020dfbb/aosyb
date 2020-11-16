package cn.osworks.aos.system.modules.controller.depot;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Depot_hjMapper;
import cn.osworks.aos.system.dao.mapper.WsdMapper;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Depot_hjPO;
import cn.osworks.aos.system.dao.po.WsdPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.depot.DepotOutService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="depot/environment")
public class EnvironmentController extends JdbcDaoSupport

    {

    @Autowired
    private Depot_hjMapper depot_hjMapper;
    @Autowired
    private WsdMapper wsdMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
            super.setJdbcTemplate(jb);
        }

    @Autowired
    private DepotOutService depotOutService;
    @RequestMapping(value="initEnvironment")
    public String initEnvironment(HttpServletResponse response, HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("depotname",qDto.getString("DepotName"));
        UserInfoVO userInfo = qDto.getUserInfo();
        request.setAttribute("jcr_cn",userInfo.getName_());
        request.setAttribute("jcr_en",userInfo.getAccount_());
        return "aos/depot/environment.jsp";
    }
    @RequestMapping(value = "calcId")
    public void calcId(HttpServletRequest request,
                       HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        String id = AOSId.id(inDto.getString("name_"));
        Dto out =Dtos.newDto();
        out.put("index",id);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value = "listEnvironment")
    public void listEnvironment(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from depot_hj  where DepotName='"+qDto.getString("depotname")+"' order by jcrq desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from depot_hj  where DepotName='"+qDto.getString("depotname")+"'";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());
        WebCxt.write(response,outString);
    }
    /**
     *
     * 数据导入
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "initImport")
    public String initImport(HttpServletRequest request,
                             HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("tablename", qDto.get("tablename"));
        request.setAttribute("_classtree", qDto.get("_classtree"));
        return "aos/depot/djimport.jsp";
    }
    /**
     * 加载EXCEL标题到grid
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("loadExcelGrid")
    public String loadExcelGrid(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
        String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
        String localFilename=qDto.getString("localFilename");
        List<Dto> titleDtos =new ArrayList<Dto>();
        if(localFilename.endsWith(".xls")){
            titleDtos =depotOutService.readExcelTitleXls(excelPath+"/"+qDto.getString("localFilename"));
        }else if(localFilename.endsWith(".xlsx")){
            titleDtos =depotOutService.readExcelTitleXlsx(excelPath+"/"+qDto.getString("localFilename"));
        }
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("localFilename", qDto.getString("localFilename"));
        request.setAttribute("tablename", qDto.getString("tablename"));
        request.setAttribute("lx", qDto.getString("lx"));
        return "aos/depot/djimport.jsp";
    }
    /**
     *
     * 查询excel数据信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="getExcel")
    public void getExcel(HttpServletRequest request,HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
        String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
        String name = excelPath+"/"+qDto.getString("localFilename");
        List<Dto> list=new ArrayList<Dto>();
        //判断结尾类型
        if(name.endsWith(".xls")){
            list =depotOutService.readXls(name);
        }else if(name.endsWith(".xlsx")){
            list =depotOutService.readXlsx(name);
        }
        String outString = AOSJson.toGridJson(list);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     *
     * 源字段和目标字段查询
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="getSourceField")
    public void getSourceField(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
        String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/excel";
        String name = excelPath+"/"+qDto.getString("localFilename");
        List<Dto> titleDtos =depotOutService.getTargetField(name,qDto);
        String outString = AOSJson.toGridJson(titleDtos);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     *
     * 数据导入
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "initWSDimport")
    public String initWSDimport(HttpServletRequest request,
                             HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("tablename", qDto.get("tablename"));
        request.setAttribute("kfbh",qDto.getString("kfbh"));
        request.setAttribute("_classtree", qDto.get("_classtree"));
        return "aos/depot/djimport.jsp";
    }
    /**
     *
     * 下拉列表
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="listComboBox")
    public void listComboBox(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> list = depotOutService.listComboBox(qDto.getString("tablename"));
        String outString =AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
    }
    /**
     *
     * excel数据导入
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="insertData")
    public void insertData(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.InsertDb(inDto);
        String outString = AOSJson.toJson(outDto);
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
    @RequestMapping(value = "fillReport_wsd")
    public void fillReport_wsd(HttpServletRequest request,
                                 HttpServletResponse response, HttpSession session) {
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("depotid", "库房编号");
        lhm.put("depotname", "库房名称");
        lhm.put("fzbm", "负责部门");
        lhm.put("djr", "登记人");
        lhm.put("timexp", "监测时间");
        lhm.put("wd", "库房温度");
        lhm.put("sd", "库房湿度");
        List<Map<String,Object>> titleDtos = depotOutService.listwsd(qDto);
        //Dto转map、
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("温湿度登记表");
        // 设置报表集合
        reportModel.setLogsList(titleDtos);
        reportModel.setLogHeader(lhm);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }

    @RequestMapping(value = "saveEnvironment")
    public void saveEnvironment(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =depotOutService.saveEnvironment(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "updateEnvironment")
    public void updateEnvironment(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.updateEnvironment(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deleteEnvironment")
    public void deleteEnvironment(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = depotOutService.deleteEnvironment(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }
    @RequestMapping(value = "updateEnvironmentAll_lx")
    public void updateEnvironmentAll_lx(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.updateEnvironmentAll_lx(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping("get_addEnvironment")
    public void get_addEnvironment(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.get_addEnvironment(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

}
