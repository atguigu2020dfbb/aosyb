package cn.osworks.aos.system.modules.controller.depot;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.CrkMapper;
import cn.osworks.aos.system.dao.mapper.Depot_ryMapper;
import cn.osworks.aos.system.dao.po.CrkPO;
import cn.osworks.aos.system.dao.po.Depot_aqPO;
import cn.osworks.aos.system.dao.po.Depot_ryPO;
import cn.osworks.aos.system.modules.service.depot.DepotOutService;
import cn.osworks.aos.system.modules.service.depot.PersonnelService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "depot/personnel")
public class PersonnelController extends JdbcDaoSupport {

    @Autowired
    private Depot_ryMapper depot_ryMapper;
    @Autowired
    private CrkMapper crkMapper;
    @Autowired
    private DepotOutService depotOutService;
    @Autowired
    private PersonnelService personnelService;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
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
     * 填充报表
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping(value = "fillReport_ry")
    public void fillReport_wsd(HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("f_recid", "f_recid");
        lhm.put("f_eventtype", "f_eventtype");
        lhm.put("f_eventdesc", "出入信息");
        lhm.put("f_userid", "用户id");
        lhm.put("f_username", "用户名");
        lhm.put("f_logdatetime", "出入时间");
        List<Map<String,Object>> titleDtos = depotOutService.listry(qDto);
        //Dto转map、
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("人员出入库登记表");
        // 设置报表集合
        reportModel.setLogsList(titleDtos);
        reportModel.setLogHeader(lhm);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }
    @RequestMapping(value="initPersonnel")
    public String initPersonnel(HttpServletResponse response, HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("xm",qDto.getUserInfo().getName_());
        request.setAttribute("lx",qDto.getString("lx"));
        return "aos/depot/personnel.jsp";
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
    @RequestMapping(value = "listPersonnel")
    public void listPersonnel(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from depot_ry  where lx='"+qDto.getString("lx")+"'  order by lsh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from depot_ry  where lx='"+qDto.getString("lx")+"'";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());
        WebCxt.write(response,outString);
    }
    @RequestMapping(value = "savePersonnel")
    public void savePersonnel(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =depotOutService.savePersonnel(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "updatePersonnel")
    public void updatePersonnel(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.updatePersonnel(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deletePersonnel")
    public void deletePersonnel(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = depotOutService.deletePersonnel(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    /**
     * 出库管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getPersonIndex")
    public void getPersonIndex(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String index = depotOutService.getPersonIndex(lx);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    /**
     * 人员管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotRyIndex_nd")
    public void getDepotRyIndex_nd(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String lsh = outDto.getString("lsh");
        String index = personnelService.getDepotRyIndex_nd(lx,lsh);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

}
