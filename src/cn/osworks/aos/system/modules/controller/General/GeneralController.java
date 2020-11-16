package cn.osworks.aos.system.modules.controller.General;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_generalPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.modules.service.General.GeneralService;
import cn.osworks.aos.system.modules.service.compilation.CompilationService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping(value="general/general")
public class GeneralController {

    @Autowired
    private GeneralService generalService;

    /**
     * 页面初始化
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     * @returnlistgenerals
     *
     * 2018-8-23
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("initData")
    public String initData(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {



        return "aos/general/general.jsp?time="+new Date().getTime();
    }
    /**
     * 全宗列表
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
    @RequestMapping("listgenerals")
    public void listgenerals(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException{
        Dto dto=Dtos.newDto();
        List<Map<String,Object>>  list=generalService.listgenerals(dto);
        String outString = AOSJson.toGridJson(list);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    /**
     * 添加
     * @param request
     * @param response
     * @param session
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="addgeneral")
    public void addgeneral(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException{
        Dto dto=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        boolean b=generalService.addgeneral(dto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        String outString = AOSJson.toJson(outDto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    /**
     * 删除
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value="delgeneral")
    public void delgeneral(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto out=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        boolean b=generalService.delgeneral(out);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        String outString = AOSJson.toJson(outDto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     * 修改
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value="updategeneral")
    public void updategeneral(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto out=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        boolean b=generalService.updategeneral(out);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        String outString = AOSJson.toJson(outDto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     * 导出日志操作
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("fillReport")
    public void fillReport(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session){
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("id_", "id值");
        lhm.put("general_number", "全宗编号");
        lhm.put("general_name", "全宗名称");
        lhm.put("create_person", "创建人");
        lhm.put("create_time", "创建时间");
        List<Map<String, Object>> titleDtos = (List)generalService.listgenerals(qDto);
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("全宗信息表");
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
