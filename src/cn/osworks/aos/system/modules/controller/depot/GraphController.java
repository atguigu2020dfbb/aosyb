package cn.osworks.aos.system.modules.controller.depot;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Depot_rkMapper;
import cn.osworks.aos.system.modules.service.depot.DepotOutService;
import cn.osworks.aos.system.modules.service.depot.GraphService;
import cn.osworks.aos.system.modules.service.depot.LocationService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="depot/graph")
public class GraphController {

    @Autowired
    private DepotOutService depotOutService;
    @Autowired
    private GraphService graphService;
    @Autowired
    private Depot_rkMapper depot_rkMapper;
    @RequestMapping(value = "initData")
    public String initDepotin(HttpServletRequest request, HttpServletResponse response){
        return "aos/depot/graph_location.jsp";
    }

   /* @RequestMapping(value = "listDepotIn")
    public void listDepotIn(HttpServletResponse response,HttpServletRequest request){
        Dto qDto = Dtos.newDto();
        List<Depot_rkPO> list = depot_rkMapper.list(qDto);
        String outString = AOSJson.toGridJson(list,qDto.getPageTotal());
        WebCxt.write(response,outString);
    }
    @RequestMapping(value = "saveDepotIn")
    public void saveDepotIn(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.saveDepotIn(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    @RequestMapping(value="updateDepotIn")
    public  void updateDepotIn(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.updateDepotIn(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }*/

   @RequestMapping(value="getlocation")
   public void getlocation(HttpServletRequest request, HttpServletResponse response){
       Dto inDto = Dtos.newDto(request);
       List<LinkedHashMap<String,Object>> str=graphService.getlocation(inDto);
       WebCxt.write(response, AOSJson.toJson(str));
   }
}
