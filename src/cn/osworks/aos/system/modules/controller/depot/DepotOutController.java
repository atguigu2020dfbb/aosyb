package cn.osworks.aos.system.modules.controller.depot;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.Archive_remoteMapper;
import cn.osworks.aos.system.dao.mapper.Depot_ckMapper;
import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.dao.po.Archive_remotePO;
import cn.osworks.aos.system.dao.po.Depot_ckPO;
import cn.osworks.aos.system.dao.po.LogPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.BasicSearchService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.depot.DepotOutService;
import cn.osworks.aos.system.modules.service.utilization.UtilizationService;
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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value="depot/depotOut")
public class DepotOutController extends JdbcDaoSupport {


    @Autowired
    private Depot_ckMapper depot_ckMapper;

    @Autowired
    private DepotOutService depotOutService;
    @Autowired
    private BasicSearchService basicSearchService;
    @Autowired
    private Archive_remoteMapper archive_remoteMapper;

    @Autowired
    private UtilizationService utilizationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LogMapper logMapper;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    public static String filePath = "";
    public static String linkAddress = "";
    public static String firstAddress = "";
    // 通过静态代码块读取配置文件
    static {
        try {
            Properties props = new Properties();
            InputStream in = DataService.class
                    .getResourceAsStream("/config.properties");
            props.load(in);
            filePath = props.getProperty("filePath");
            linkAddress = props.getProperty("linkAddress");
            firstAddress = props.getProperty("firstAddress");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     *
     * 出库管理
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="initDepotOut")
    public String initDepot(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);

        request.setAttribute("lx",qDto.getString("lx"));
        request.setAttribute("user",qDto.getUserInfo().getName_());
        return "aos/depot/depotOut.jsp";
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
        String ckdh= AOSId.id(cz_name);
        outDto.put("index",ckdh);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    /**
     *
     * 利用登记出库管理
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="initDepotLy")
    public String initDepotLy(HttpServletRequest request, HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        request.setAttribute("user",inDto.getUserInfo().getAccount_());

        return "aos/depot/initDepotLy.jsp";
    }


    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listDepotLy")
    public void listDepotLy(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        List<Map<String,Object>> list = utilizationService.listDepotLy(inDto);
        int count=utilizationService.listDepotLy_count(inDto);
        String outString = AOSJson.toGridJson(list,count);
        WebCxt.write(response,outString);

    }

    @RequestMapping( value = "updateUtilizationCk")
    public void updateUtilizationCk(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        inDto.put("ckr",inDto.getUserInfo().getName_());
        inDto.put("cksj",sd.format(new Date()));
        Dto outDto = utilizationService.updateUtilization(inDto);
        //此时在出库管理中添加一个出库单据
        utilizationService.saveDepotout(inDto);
        utilizationService.insertRemoteLog(inDto.getString("id_"),inDto.getUserInfo().getName_(),"确认出库");
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    /**
     *
     * 查询表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value ="listDepotout")
    public void listDepotout(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from depot_ck  where lx='"+qDto.getString("lx")+"'  order by ckdh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        String sql2="select * from depot_ck  where lx='"+qDto.getString("lx")+"'";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        String outString = AOSJson.toGridJson(listDto,listAll.size());

        WebCxt.write(response,outString);
    }

    @RequestMapping(value="saveDepotout")
    public void saveDepotout(HttpServletResponse response,HttpServletRequest request){
       Dto dto = Dtos.newDto(request);
       Dto outDto = depotOutService.save(dto);
       WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="updateDepotout")
    public void updateDepotout(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = depotOutService.update(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "getAPPID")
    public void getAPPID(HttpServletRequest request,HttpServletResponse response){
        Dto outDto = Dtos.newDto();
        String ckdh = AOSId.id("库房流水号");
        outDto.put("ckdh",ckdh);
        WebCxt.write(response,AOSJson.toJson(outDto));

    }
    @RequestMapping(value="deleteDepotout")
    public void deleteDepotout(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = depotOutService.deleteDepotout(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }
    /**
     * 填充报表
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping(value = "fillReport_ck")
    public void fillReport_wsd(HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        // inDto包装了全部的请求参数哦
        Dto qDto = Dtos.newDto(request);
        //设置表头
        LinkedHashMap<String,Object> lhm=new LinkedHashMap<String, Object>();
        lhm.put("ckdh", "出库单号");
        lhm.put("ckr", "出库人");
        lhm.put("ckyy", "出库原因");
        lhm.put("cksj", "出库时间");
        lhm.put("cksl", "出库数量");
        lhm.put("nrksj", "拟入库时间");
        lhm.put("ckzt", "出库状态");
        lhm.put("bz", "备注");
        List<Map<String,Object>> titleDtos = depotOutService.listck(qDto);
        //Dto转map、
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("出库登记表");
        // 设置报表集合
        reportModel.setLogsList(titleDtos);
        reportModel.setLogHeader(lhm);
        // 填充报表
        // AOSPrint aosPrint = AOSReport.fillReport(reportModel);
        // aosPrint.setFileName("excel表");
        session.setAttribute(AOSReport.DEFAULT_AOSPRINT_KEY, reportModel);
        WebCxt.write(response, AOSJson.toJson(Dtos.newOutDto()));
    }

    //*****************************************************************

    /**
     * 出库管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotOutIndex")
    public void getDepotOutIndex(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String index = depotOutService.getDepotOutIndex(lx);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     * 出库管理 单号 index
     * @param request
     * @param response
     */
    @RequestMapping(value = "getDepotOutIndex_nd")
    public void getDepotOutIndex_nd(HttpServletRequest request, HttpServletResponse response) {
        Dto outDto = Dtos.newDto(request);
        String lx = outDto.getString("lx");
        String ckdh = outDto.getString("ckdh");
        String index = depotOutService.getDepotOutIndex_nd(lx,ckdh);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
    /**
     * 删除利用出库信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "deleteDepotLy")
    public void deleteDepotLy(HttpServletRequest request, HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        int row =  utilizationService.deleteDepotLy(dto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }
    /**
     * 出库完成后，入库管理添加入库信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "addDepotIn_ly")
    public void addDepotIn_ly(HttpServletRequest request, HttpServletResponse response){
        Dto out=Dtos.newDto();
        try {
            Dto dto = Dtos.newDto(request);
            //1.获取利用任务信息，并修改任务额出库状态已入库
            Archive_remotePO archive_remotePO = utilizationService.updateArchive_Remote_rk(dto.getString("id_"));
            //2.出库管理，需要将出库管理中的入库时间写成当前时间
            depotOutService.updateDepotOut_time(archive_remotePO);
            //3.得到出库的信息
            Map<String,Object> map=depotOutService.getDepotOut(archive_remotePO.getCkdh());
            //4.入库管理，需要在入库管理中添加条目，并且把原出库单号传递过去
            depotOutService.addDepotIn(map,dto);
            out.setAppCode(1);
            WebCxt.write(response,AOSJson.toJson(out));
        } catch (Exception e) {
            e.printStackTrace();
            out.setAppCode(-1);
            WebCxt.write(response,AOSJson.toJson(out));
        }
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
        Map<String,Object> map=depotOutService.getRemember(inDto, userInfoVO);
        String outString = AOSJson.toJson(map);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
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
        List<Dto> pathDtos = depotOutService.getPath(inDto);
        // request.setAttribute("pathDtos", pathDtos);
        String outString = AOSJson.toGridJson(pathDtos);
        // Dto outDto = printService.getPath(inDto);
        WebCxt.write(response, outString);
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
        // aos_rows_=ace1bf5e2b8f4c9a81f9422ed89a5eb6,, tablename=wsgdwj
        depotOutService.download_electronic_file(filePath, dto, response, request);
    }
    /**
     *
     * 删除电子文件
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "deletePath")
    public void deletePath(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = depotOutService.deletePath(dto);
        depotOutService.refreshPath(dto);
        // 日志
        // 加入日志
        String tm = dto.getString("tm");
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                userInfoVO.getAccount_(),
                "删除电子文件[" + dto.getString("tablename") + "]", tm, "删除",
                request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        logMapper.insert(logPO);

        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     *
     * 删除全部电子文件
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "deletePathAll")
    public void deletePathAll(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = depotOutService.deletePathAll(dto);
        // 加入日志
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        String tm = dto.getString("tm");
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                userInfoVO.getAccount_(),
                "移除电子文件[" + dto.getString("tablename") + "]", tm, "移除",
                request.getRemoteAddr(), new Date()+"");
        logMapper.insert(logPO);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

}
