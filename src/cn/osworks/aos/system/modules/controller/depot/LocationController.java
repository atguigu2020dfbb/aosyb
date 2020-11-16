package cn.osworks.aos.system.modules.controller.depot;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Depot_czMapper;
import cn.osworks.aos.system.dao.mapper.Depot_cz_detailMapper;
import cn.osworks.aos.system.dao.mapper.Depot_rkMapper;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.depot.DepotOutService;
import cn.osworks.aos.system.modules.service.depot.LocationService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import com.google.common.collect.Lists;
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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="depot/location")
public class LocationController extends JdbcDaoSupport {

    @Autowired
    private DepotOutService depotOutService;
    @Autowired
    private LocationService locationService;

    @Autowired
    private Depot_rkMapper depot_rkMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    @Autowired
    private Depot_czMapper depot_czMapper;

    @Autowired
    private Depot_cz_detailMapper depot_cz_detailMapper;

    @RequestMapping(value = "initData")
    public String initDepotin(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        String archive_storehouse=qDto.getString("archive_storehouse");
        UserInfoVO userInfoVO=qDto.getUserInfo();
        request.setAttribute("djr_cn",userInfoVO.getName_());
        request.setAttribute("djr_en",userInfoVO.getAccount_());
        request.setAttribute("archive_storehouse", archive_storehouse);
        return "aos/depot/initLocation.jsp";
    }

    @RequestMapping(value="listCatalogs")
    public void listCatalogs(HttpServletRequest request, HttpServletResponse response){
        List<Dto> outlist = Lists.newArrayList();
        Dto dto = Dtos.newDto();
        dto.put("key","11111");
        dto.put("id_","1");
        outlist.add(dto);
        String outString = AOSJson.toGridJson(outlist);
        WebCxt.write(response,outString);
    }

    @RequestMapping(value="listLocation")
    public void listLocation(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from depot_cz  where archive_storehouse='"+qDto.getString("archive_storehouse")+"' order by convert(int,cz_liehao),cz_jiehao,cz_gehao offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from depot_cz  where archive_storehouse='"+qDto.getString("archive_storehouse")+"'";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());
        WebCxt.write(response,outString);
    }

    @RequestMapping(value = "saveLocation")
    public void saveLocation(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =locationService.saveLocation(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "updateLocation")
    public void updateLocation(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = locationService.updateLocation(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deleteLocation")
    public void deleteLocation(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = locationService.deleteLocation(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }


    @RequestMapping(value="listDetail2")
    public void listDetail2(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        List<Map<String,Object>> list4=locationService.getlocationGrid2(qDto);
        List<Map<String,Object>> listall=locationService.getlocationGrid4(qDto);
        String outString = AOSJson.toGridJson(list4,listall.size());
        WebCxt.write(response,outString);
    }
    @RequestMapping(value="listDetail")
    public void listDetail(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        //查询存址编号
        qDto.put("cz_bh",locationService.getCz_bh(qDto));
        List<Map<String,Object>> list4=locationService.getlocationGrid2(qDto);
        List<Map<String,Object>> list5=locationService.getlocationGrid4(qDto);
        String outString = AOSJson.toGridJson(list4,list5.size());
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
        String query = locationService.queryConditions2(out);
        Dto outDto = Dtos.newDto();
        outDto.setAppMsg(query);
        WebCxt.write(response, AOSJson.toJson(outDto));

    }
    @RequestMapping("getQueryIds")
    public void getQueryIds(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        List<Map<String,Object>> list= locationService.getQueryIds(inDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }


    @RequestMapping(value = "saveDetail")
    public void saveDetail(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =locationService.saveDetail(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "saveDetail_charts")
    public void saveDetail_charts(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        //查询存址编号
        String cz_bh= locationService.findCn_bh(inDto);
        inDto.put("cz_bh",cz_bh);
        Dto outDto =locationService.saveDetail(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }



    @RequestMapping(value = "updateDetail")
    public void updateDetail(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = locationService.updateDetail(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deleteDetail")
    public void deleteDetail(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = locationService.deleteDetail(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    @RequestMapping(value="deleteDetail_charts")
    public void deleteDetail_charts(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        inDto.put("cz_bh",locationService.getCz_bh(inDto));
        int row=locationService.deleteDetail_charts(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
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
   @RequestMapping(value = "addlocation")
   public void addlocation(HttpServletRequest request, HttpServletResponse response){
       Dto inDto = Dtos.newDto(request);
       boolean b=locationService.addlocation(inDto);
       Dto outDto=Dtos.newDto();
       if(b){
           outDto.setAppCode(1);
       }else{
           outDto.setAppCode(-1);
       }
       WebCxt.write(response, AOSJson.toJson(outDto));
   }
   @RequestMapping(value="getlocation")
   public void getlocation(HttpServletRequest request, HttpServletResponse response){
       Dto inDto = Dtos.newDto(request);
       List<Map<String,Object>> list=locationService.getlocation(inDto);
       WebCxt.write(response, AOSJson.toGridJson(list));
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
        String cz_bh= AOSId.id(cz_name);
        outDto.put("cz_bh",cz_bh);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    /**
     * 获取操作日志
     * @param request
     * @param response
     */
    @RequestMapping(value = "listelog")
    public void listelog(HttpServletRequest request, HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        List<Map<String,Object>> list=locationService.listelog(dto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }

    /**
     * 存址列表跳转
     * @param request
     * @param response
     */
    @RequestMapping("initlistData")
    public String initlistData(HttpServletRequest request, HttpServletResponse response){

        return "aos/depot/locationlist.jsp";

    }

    /**
     * 存址修改
     * @param request
     * @param response
     */
   /* @RequestMapping("updatelocation")
    public void updatelocation(HttpServletRequest request, HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        boolean b=locationService.updatelocation(dto);
        Dto outDto=Dtos.newDto();
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     * 存址删除
     * @param request
     * @param response
     */
  /*  @RequestMapping("dellocation")
    public void dellocation(HttpServletRequest request, HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        boolean b=locationService.dellocation(dto);
        Dto outDto=Dtos.newDto();
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
    }*/
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
        lhm.put("dh", "档号");
        lhm.put("archive_storehouse", "库房名称");
        lhm.put("cz_liehao", "存址列号");
        lhm.put("cz_zuhao", "存址组号");
        lhm.put("cz_mian", "存址AB面");
        lhm.put("cz_jiehao", "存址节号");
        lhm.put("cz_gehao", "存址隔号");
        lhm.put("cz_name", "名称");
        List<Map<String, Object>> titleDtos = locationService
                .getDataFieldListTitle(qDto);
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("存址列表");
        // 设置报表集合
        reportModel.setLogsList(titleDtos);
        reportModel.setLogHeader(lhm);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }
    @RequestMapping(value = "initCharts")
    public String initCharts(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        String archive_storehouse=qDto.getString("archive_storehouse");
        request.setAttribute("archive_storehouse", archive_storehouse);
        return "aos/depot/charts.jsp";
    }
    @RequestMapping(value = "getKfJson")
    public void getKfJson(HttpServletRequest request, HttpServletResponse response)throws Exception{
        Dto qDto = Dtos.newDto(request);
        String archive_storehouse=qDto.getString("archive_storehouse");
        String json=locationService.getKfJson(archive_storehouse);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.print(json);
    }
    @RequestMapping(value="getgeSum")
    public void getgeSum(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        String archive_storehouse=qDto.getString("archive_storehouse");
        String liehao=qDto.getString("liehao");
        Map<String,Object> map=locationService.getgeSum(archive_storehouse,liehao);
        WebCxt.write(response, AOSJson.toJson(map));

    }
    @RequestMapping(value="print")
    public void print(HttpServletRequest request, HttpServletResponse response)throws Exception{
        Dto qDto = Dtos.newDto(request);
        locationService.print(qDto);
    }
    /**
     * 填充报表
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping(value = "fillReport_depot")
    public void fillReport_depot(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session) {
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("cz_bh", "存址编号");
        lhm.put("cz_liehao", "列号");
        lhm.put("cz_mian", "面");
        lhm.put("cz_jiehao", "节号");
        lhm.put("cz_gehao", "格号");
        List<Map<String,Object>> titleDtos = locationService.listdeport(qDto);
        //Dto转map、
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("存址表");
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
    @RequestMapping(value = "initImport")
    public String initImport(HttpServletRequest request,
                             HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("tablename", qDto.get("tablename"));
        request.setAttribute("_classtree", qDto.get("_classtree"));
        return "aos/depot/import.jsp";
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
            titleDtos =locationService.readExcelTitleXls(excelPath+"/"+qDto.getString("localFilename"));
        }else if(localFilename.endsWith(".xlsx")){
            titleDtos =locationService.readExcelTitleXlsx(excelPath+"/"+qDto.getString("localFilename"));
        }
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("localFilename", qDto.getString("localFilename"));
        request.setAttribute("tablename", qDto.getString("tablename"));
        return "aos/depot/import.jsp";
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
        List<Archive_tablefieldlistPO> list = locationService.listComboBox();
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
        Dto outDto = locationService.InsertDb(inDto);
        String outString = AOSJson.toJson(outDto);
        WebCxt.write(response, outString);
    }
    @RequestMapping(value="listTablename_by")
    public void listTablename_by(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        List<Archive_tablenamePO> list = locationService.findTablename_by();
        String outString =AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
    }
    @RequestMapping("listArchive")
    public void listArchive(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        String query = locationService.queryConditions2(inDto);
        //查询数据不包含库房名称的数据
        query+=" and (cz_bh='' or cz_bh is NULL)";
        inDto.put("query",query);
        List<Map<String, Object>> fieldDtos = locationService.listArchive(inDto);
        int pCount = locationService.getPageAll_archive(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos,pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    /**
     * 向指定存址编号添加数据
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("addKf_archive")
    public void addKf_archive(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        boolean b= locationService.addKf_archive(qDto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));

    }
    /**
     * 向指定存址编号添加数据
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("addKf_archive_charts")
    public void addKf_archive_charts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        qDto.put("cz_jiehaoandgehao",qDto.getString("cz_jiehao")+"-"+qDto.getString("cz_gehao"));
        String cz_bh=locationService.getCz_bh(qDto);
        if(cz_bh==null||cz_bh==""){
            outDto.setAppCode(-1);
        }else{
            qDto.put("cz_bh",locationService.getCz_bh(qDto));
            boolean b= locationService.addKf_archive(qDto);
            if(b){
                outDto.setAppCode(1);
            }else{
                outDto.setAppCode(-1);
            }
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
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
        if(qDto.getString("localFilename").equals("")){
            return;
        }
        List<Dto> list=new ArrayList<Dto>();
        //判断结尾类型
        if(name.endsWith(".xls")){
            list =locationService.readXls(name);
        }else if(name.endsWith(".xlsx")){
            list =locationService.readXlsx(name);
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
        List<Dto> titleDtos =locationService.getTargetField(name,qDto);
        String outString = AOSJson.toGridJson(titleDtos);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }


}
