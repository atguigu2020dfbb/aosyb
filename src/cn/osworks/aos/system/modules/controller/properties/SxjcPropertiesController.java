package cn.osworks.aos.system.modules.controller.properties;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_sxjcxmMapper;
import cn.osworks.aos.system.dao.mapper.Depot_rkMapper;
import cn.osworks.aos.system.dao.po.Archive_sxjcxmPO;
import cn.osworks.aos.system.dao.po.Depot_rkPO;
import cn.osworks.aos.system.modules.service.depot.DepotOutService;
import cn.osworks.aos.system.modules.service.properties.SxjcPerpertiesService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="properties/sxjc")
public class SxjcPropertiesController {

    @Autowired
    private SxjcPerpertiesService sxjcPerpertiesService;
    @Autowired
    private DepotOutService depotOutService;

    @Autowired
    private Archive_sxjcxmMapper archive_sxjcxmMapper;
    @Autowired
    private Depot_rkMapper depot_rkMapper;
    @RequestMapping(value = "initSxjc")
    public String initDepotin(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        return "aos/sxjc_properties/sxjc_properties.jsp";
    }
    /**
     * 入库管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotInIndex_nd")
    public void getDepotOutIndex_nd(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String rkdh = outDto.getString("rkdh");
        String index = depotOutService.getDepotInIndex_nd(lx,rkdh);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     * 出库管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotInIndex")
    public void getDepotInIndex(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String index = depotOutService.getDepotInIndex(lx);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }


    @RequestMapping(value = "listSxjc")
    public void listDepotIn(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto(request);
        List<Map<String,Object>> list =sxjcPerpertiesService.list();
        String outString = AOSJson.toGridJson(list,qDto.getPageTotal());
        WebCxt.write(response,outString);
    }

    @RequestMapping(value = "saveDepotIn")
    public void saveDepotIn(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = sxjcPerpertiesService.saveDepotIn(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping(value="updateDepotIn")
    public  void updateDepotIn(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = sxjcPerpertiesService.updateDepotIn(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value="deleteDepotIn")
    public void deleteDepotIn(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = sxjcPerpertiesService.deleteDepotin(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
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
        Dto outDto = Dtos.newDto(request);
        String cz_name=outDto.getString("name_");
        String rkdh= AOSId.id(cz_name);
        outDto.put("index",rkdh);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     * 填充报表
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping(value = "fillReport_rk")
    public void fillReport_wsd(HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("rkdh", "入库单号");
        lhm.put("rkr", "入库人");
        lhm.put("rkyy", "入库原因");
        lhm.put("yckdh", "原出库单号");
        lhm.put("rksj", "入库时间");
        lhm.put("rksl", "入库数量");
        lhm.put("rkzt", "入库状态");
        lhm.put("bz", "备注");
        List<Map<String,Object>> titleDtos = depotOutService.listrk(qDto);
        //Dto转map、
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("入库登记表");
        // 设置报表集合
        reportModel.setLogsList(titleDtos);
        reportModel.setLogHeader(lhm);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }
}
