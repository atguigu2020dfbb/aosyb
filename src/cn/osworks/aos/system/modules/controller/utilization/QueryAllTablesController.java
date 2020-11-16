package cn.osworks.aos.system.modules.controller.utilization;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.service.archive.BasicSearchService;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.utilization.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="queryAlltables")
public class QueryAllTablesController extends JdbcDaoSupport {
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Resource
    public void setJd(JdbcTemplate jd) {
        super.setJdbcTemplate(jd);
    }
    @Autowired
    private BasicSearchService basicSearchService;
    @Autowired
    private DataService dataService;
    @Autowired
    private UtilizationService utilizationService;

    @RequestMapping(value="listAccounts_all")
    public void listAccounts_wsda(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        if(("0").equals(inDto.getString("flag"))||("").equals(inDto.getString("flag"))){
            //标题列表
            List<Map<String,Object>> fieldDtos = new ArrayList<Map<String,Object>>();
            int pCount =0;
            String query = inDto.getString("query");
            request.setAttribute("_total", 0);
            request.setAttribute("query",query);
            //条目数据
            String outString = AOSJson.toGridJson(fieldDtos, pCount);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }else{
            String flag=inDto.getString("flag");
            List<Map<String,Object>> linkedList =null;
            int pCount=0;
            if("1".equals(flag)){
                //标题列表
                linkedList = basicSearchService.getDataFieldListDisplayAll_all(inDto,session);
                pCount=linkedList.size();
                // 排列方式，页码，每页条目数
                Integer limit = Integer.valueOf(inDto.getString("limit"));
                Integer page = Integer.valueOf(inDto.getString("page"));
                // 条目开始索引
                Integer pageStart = (page - 1) * (limit) + 1;
                linkedList=basicSearchService.startPage(linkedList,page,limit);
            }
            String query = inDto.getString("query");
            request.setAttribute("_total", pCount);
            request.setAttribute("query",query);
            //条目数据
            String outString = AOSJson.toGridJson(linkedList, pCount);
            // 就这样返回转换为Json后返回界面就可以了。
            WebCxt.write(response, outString);
        }
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
}
