package cn.osworks.aos.system.modules.controller.checkup;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.dao.po.Checkup_topicPO;
import cn.osworks.aos.system.dao.po.Compilation_topicPO;
import cn.osworks.aos.system.modules.service.checkup.CheckupTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="archive/topic")
public class CheckupTopicController {

    @Autowired
    private CheckupTopicService checkupTopicService;
    @Autowired
    private SqlDao sysDao;
    /**
     * 专题列表界面
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "topic_initData")
    public String topic_initData(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto out=Dtos.newDto(request);
        request.setAttribute("user_en",out.getUserInfo().getName_());
        return "aos/details/detailsList.jsp";
    }
    @RequestMapping(value = "initData")
    public String initData(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto out=Dtos.newDto(request);
        request.setAttribute("aos_module_id_",out.getString("aos_module_id_"));
        return "aos/details/data.jsp";
    }

    @RequestMapping(value="listTablename_by")
    public void listTablename_by(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        List<Archive_tablenamePO> list = checkupTopicService.findTablename_by();
        String outString =AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
    }
    /**
     * 显示数据
     *
     * @author PX
     * @param request
     * @param response
     *
     * 2018-8-23
     */
    @RequestMapping(value="listAccounts_topic")
    public void listAccounts_topic(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        List<Map<String,Object>> fieldDtos =checkupTopicService.getData(inDto,session);
        int pCount = checkupTopicService.getPageTotal_Data(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos, pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     *
     * 删除条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "deletetopic_data")
    public void deletetopic_data(HttpServletRequest request,
                                 HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = checkupTopicService.deletetopic_data(dto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    @RequestMapping("updatetopic_data")
    public void updatetopic_data(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=checkupTopicService.updatetopic_data(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping("listdata_edit")
    public void listdata_edit(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        String query = checkupTopicService.queryConditions2(inDto);
        //此时在查询当前任务已经选择的档案
        String query_id_ = checkupTopicService.queryConditions2_noid(inDto);
        inDto.put("query",query+query_id_);
        List<Map<String, Object>> fieldDtos = checkupTopicService.listArchive(inDto);
        int pCount = checkupTopicService.getPageAll_archive(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos,pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
        //条目数据


    }
    @RequestMapping(value="edittopic")
    public void edittopic( HttpSession session, HttpServletRequest request, HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=checkupTopicService.edittopic(qDto);
        //同时再把当前所选择的条目名称全部添加到数据库中
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 获取专题数据列表
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "listtopic")
    public void listtopic(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        List<Checkup_topicPO> list=checkupTopicService.listtopic(qDto);
        WebCxt.write(response, AOSJson.toGridJson(list,list.size()));

    }

    @RequestMapping(value="addtopic")
    public void addtopic( HttpSession session, HttpServletRequest request, HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=checkupTopicService.addtopic(qDto);
        //同时再把当前所选择的条目名称全部添加到数据库中
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value="addtopic_edit")
    public void addtopic_edit(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto qDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b=checkupTopicService.addtopic_edit(qDto);
        //同时再把当前所选择的条目名称全部添加到数据库中
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 删除条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "delete_topic")
    public void delete_topic(HttpServletRequest request,
                             HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = checkupTopicService.delete_topic(dto);
        //目录树

        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping("listArchive")
    public void listArchive(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        String query = checkupTopicService.queryConditions2(inDto);

        inDto.put("query",query+inDto.getString("query_old"));
        List<Map<String, Object>> fieldDtos = checkupTopicService.listArchive(inDto);
        int pCount = checkupTopicService.getPageAll_archive(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos,pCount);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping("getComboboxList")
    public void getComboboxList(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> list =checkupTopicService.getComboboxList(inDto);
        String outString = AOSJson.toGridJson(list);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping("getQueryIds")
    public void getQueryIds(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表;
        //先得到query条件
        List<Map<String,Object>> list= checkupTopicService.getQueryIds(inDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
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
        String query = checkupTopicService.queryConditions2(out);

        Dto outDto = Dtos.newDto();
        outDto.setAppMsg(query);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }


}
