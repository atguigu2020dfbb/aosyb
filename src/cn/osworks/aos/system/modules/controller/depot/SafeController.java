package cn.osworks.aos.system.modules.controller.depot;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Depot_aqMapper;
import cn.osworks.aos.system.dao.mapper.Depot_bgMapper;
import cn.osworks.aos.system.dao.mapper.Depot_pdMapper;
import cn.osworks.aos.system.dao.mapper.Depot_sbMapper;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "depot/safe")
public class SafeController extends JdbcDaoSupport {
    @Autowired
    private Depot_aqMapper depot_aqMapper;

    @Autowired
    private Depot_pdMapper depot_pdMapper;

    @Autowired
    private Depot_sbMapper depot_sbMapper;
    @Autowired
    private Depot_bgMapper depot_bgMapper;
    @Autowired
    private DepotOutService depotOutService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }

    /**
     * 填充报表
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping(value = "fillReport_bg")
    public void fillReport_wsd(HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("kfbh", "库房编号");
        lhm.put("jcsj", "检查时间");
        lhm.put("jcbm", "检查部门");
        lhm.put("jcr", "检查人");
        lhm.put("daml", "档案门类");
        lhm.put("dh", "档号");
        lhm.put("tm", "题名");
        lhm.put("xcrq", "形成日期");
        lhm.put("bgqx", "保管期限");
        lhm.put("zrz", "责任者");
        lhm.put("ys", "页数");
        lhm.put("bgzk", "保管状况");
        lhm.put("wtys", "问题页数");
        lhm.put("wtms", "问题描述");
        lhm.put("sfcl", "是否处理");
        lhm.put("clr", "处理人");
        lhm.put("clsj", "处理时间");
        lhm.put("bz", "备注");
        List<Map<String,Object>> titleDtos = depotOutService.listbg(qDto);
        //Dto转map、
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("保管状况登记表");
        // 设置报表集合
        reportModel.setLogsList(titleDtos);
        reportModel.setLogHeader(lhm);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }
    /**
     * 填充报表
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping(value = "fillReport_sb")
    public void fillReport_sb(HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("kfbh", "库房编号");
        lhm.put("jcsj", "检查时间");
        lhm.put("jcbm", "检查部门");
        lhm.put("jcr", "检查人");
        lhm.put("jclx", "检查类型");
        lhm.put("sbmc", "设备名称");
        lhm.put("sbxh", "设备型号");
        lhm.put("gzms", "故障描述");
        lhm.put("gzyy", "故障原因");
        lhm.put("gzsfjj", "故障是否解决");
        lhm.put("ywqtwt", "有无其他问题");
        lhm.put("wxbm", "维修部门");
        lhm.put("wxr", "维修人");
        lhm.put("bz", "备注");
        List<Map<String,Object>> titleDtos = depotOutService.listsb(qDto);
        //Dto转map、
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("设备管理登记表");
        // 设置报表集合
        reportModel.setLogsList(titleDtos);
        reportModel.setLogHeader(lhm);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }
    /**
     * 填充报表
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping(value = "fillReport_aq")
    public void fillReport_aq(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("kfbh", "库房编号");
        lhm.put("jcsj", "检查时间");
        lhm.put("jcbm", "检查部门");
        lhm.put("jcr", "检查人");
        lhm.put("jclx", "检查类型");
        lhm.put("dysfgb", "电源是否关闭");
        lhm.put("kcsfgb", "口窗是否关闭");
        lhm.put("nqsfls", "暖气是否漏水");
        lhm.put("sbsfzcsy", "设备是否正常");
        lhm.put("ktsfzc", "空调是否正常");
        lhm.put("ytjsfzc", "一体机是否正常");
        lhm.put("mjsfzc", "门禁是否正常");
        lhm.put("jksfzc", "监控是否正常");
        lhm.put("bz", "备注");
        List<Map<String,Object>> titleDtos = depotOutService.listaq(qDto);
        //Dto转map、
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("安全检查登记表");
        // 设置报表集合
        reportModel.setLogsList(titleDtos);
        reportModel.setLogHeader(lhm);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }
    /**
     *
     * 数据导入
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "initimport")
    public String initWSDimport(HttpServletRequest request,
                                HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("tablename", qDto.get("tablename"));
        request.setAttribute("lx",qDto.get("lx"));
        request.setAttribute("_classtree", qDto.get("_classtree"));
        return "aos/depot/djimport.jsp";
    }
    @RequestMapping(value="initsafe")
    public String initsafe(HttpServletResponse response, HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("lx",qDto.getString("lx"));
        request.setAttribute("jcr",qDto.getUserInfo().getName_());
        return "aos/depot/iniSafe.jsp";
    }
    @RequestMapping(value="initBg")
    public String initBg(HttpServletResponse response, HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("lx",qDto.getString("lx"));
        request.setAttribute("user",qDto.getUserInfo().getName_());
        return "aos/depot/initBg.jsp";
    }
    @RequestMapping(value = "listSafe")
    public void listSafe(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from depot_aq  where lx='"+qDto.getString("lx")+"'  order by lsh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from depot_aq  where lx='"+qDto.getString("lx")+"'";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());
        WebCxt.write(response,outString);
    }
    @RequestMapping(value = "listBg")
    public void listBg(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from depot_bg  where lx='"+qDto.getString("lx")+"'  order by kfbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from depot_bg  where lx='"+qDto.getString("lx")+"'";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());
        WebCxt.write(response,outString);
    }

    @RequestMapping(value = "saveSafe")
    public void saveSafe(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =depotOutService.saveSafe(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "saveBg")
    public void saveBg(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =depotOutService.saveBg(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
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
    /**
     * 出库管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getSafeIndex")
    public void getDepotOutIndex(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String index = depotOutService.getSafeIndex(lx);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "updateSafe")
    public void updateSafe(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.updateSafe(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "updateBg")
    public void updateBg(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.updateBg(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value="deleteSafe")
    public void deleteSafe(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = depotOutService.deleteSafe(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }
    @RequestMapping(value="deleteBg")
    public void deleteBg(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = depotOutService.deleteBg(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }
    @RequestMapping(value="initPd")
    public String initPd(HttpServletResponse response, HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("lx",qDto.getString("lx"));
        UserInfoVO userInfo = qDto.getUserInfo();
        request.setAttribute("pdr_cn",userInfo.getName_());
        request.setAttribute("pdr_en",userInfo.getAccount_());
        request.setAttribute("user",qDto.getUserInfo().getAccount_());
        return "aos/depot/initPd.jsp";
    }
    /**
     * 盘点管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotPdIndex_nd")
    public void getDepotPdIndex_nd(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String lsh = outDto.getString("lsh");
        String index = depotOutService.getDepotPdIndex_nd(lx,lsh);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     * 保管状况管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotBgIndex_nd")
    public void getDepotBgIndex_nd(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String lsh = outDto.getString("kfbh");
        String index = depotOutService.getDepotBgIndex_nd(lx,lsh);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }



    @RequestMapping(value = "listPd")
    public void listpPd(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from depot_pd  where lx='"+qDto.getString("lx")+"'  order by lsh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from depot_pd  where lx='"+qDto.getString("lx")+"'";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());
        WebCxt.write(response,outString);
    }

    @RequestMapping(value = "savePd")
    public void savePd(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =depotOutService.savePd(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "updatePd")
    public void updatePd(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.updatePd(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deletePd")
    public void deletePd(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = depotOutService.deletePd(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    @RequestMapping(value="initDevice")
    public String initDevice(HttpServletResponse response, HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("lx",qDto.getString("lx"));
        return "aos/depot/initDevice.jsp";
    }

    @RequestMapping(value = "listDevice")
    public void listDevice(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        List<Depot_sbPO> list = depot_sbMapper.list(qDto);
        String outString = AOSJson.toGridJson(list,qDto.getPageTotal());
        WebCxt.write(response,outString);
    }

    @RequestMapping(value = "saveDevice")
    public void saveDevice(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =depotOutService.saveDevice(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "updateDevice")
    public void updateDevice(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.updateDevice(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deleteDevice")
    public void deleteDevice(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = depotOutService.deleteDevice(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    //******************************************************************************************
    /**
     * 设备管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDeviceIndex")
    public void getDeviceIndex(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("depotname");
        String index = depotOutService.getDeviceIndex(lx);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     * 出库管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotPdIndex")
    public void getDepotPdIndex(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String index = depotOutService.getDepotPdIndex(lx);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     * 出库管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotAqIndex")
    public void getDepotAqIndex(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String index = depotOutService.getDepotAqIndex(lx);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "getDepotBgIndex")
    public void getDepotBgIndex(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String index = depotOutService.getDepotBgIndex(lx);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    /**
     * 安全管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotAqIndex_nd")
    public void getDepotAqIndex_nd(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String ckdh = outDto.getString("lsh");
        String index = depotOutService.getDepotAqIndex_nd(lx,ckdh);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

}
