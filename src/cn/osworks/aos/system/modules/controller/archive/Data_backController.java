package cn.osworks.aos.system.modules.controller.archive;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.dao.po.LogPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.Data_backService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "archive/data_back")
public class Data_backController {
    @Autowired
    public Data_backService data_backService;
    @Autowired
    public DataService dataService;
    /**
     *
     * 页面初始化
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "initData")
    public String initData(HttpSession session, HttpServletRequest request,
                           HttpServletResponse response) throws UnsupportedEncodingException {
        Dto qDto = Dtos.newDto(request);
        //后台解码
        String listtabledesc=qDto.getString("tablenamedesc");
        listtabledesc=URLDecoder.decode(listtabledesc, "UTF-8");

        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(qDto.getString("listtablename"));
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        String pagesize = userInfoVO.getPagesize() + "";
        if (pagesize == null) {
            pagesize = "20";
        }
        String queryclass = dataService.isAll(qDto);
        request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
        request.setAttribute("tablename", qDto.getString("listtablename"));
        session.setAttribute("tablename", qDto.getString("listtablename"));
        session.setAttribute("queryclass", queryclass);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        request.setAttribute("listtablename", qDto.getString("listtablename"));
        request.setAttribute("tablenamedesc", listtabledesc);
        //return "aos/archive/tree.jsp";
        String associations_=qDto.getString("associations_");
         return "aos/archive/data_back.jsp";

    }
    /**
     *
     * 查询数据信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listAccounts")
    public void listAccounts2(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        Dto inDto = Dtos.newDto(request);
        String tablename=inDto.getString("listtablename");
        inDto.put("tablename", tablename);
        request.getSession().setAttribute("querySession",
                data_backService.queryConditions(inDto));
        List<Dto> fieldDtos = data_backService.getDataFieldListDisplayAll(inDto,"user");
        String outString = AOSJson.toGridJson(fieldDtos);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     * 选择表
     *
     * @author PX
     * @param session
     * @param request
     * @param response
     *
     * 2018-8-20
     */
    @RequestMapping(value="listTablename")
    public void listTablename(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        List<Archive_tablenamePO> list = data_backService.findTablename();
        String outString =AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
    }
    /**
     *
     * 删除条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "deleteData")
    public void deleteData(HttpServletRequest request,
                                HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = data_backService.deleteData(dto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     *
     * 删除条目
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "returnData")
    public void returnData(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = data_backService.returnData(dto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "deleteAllData")
    public void deleteAllData(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        Dto qDto = Dtos.newDto(request);
        Dto outDto = data_backService.deleteAllData(qDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping(value = "returnAllData")
    public void returnAllData(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        Dto qDto = Dtos.newDto(request);
        Dto outDto = data_backService.returnAllData(qDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    /**
     *
     * 查询字段下拉框
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "queryFields")
    public void queryFields(HttpServletRequest request,
                            HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> titleDtos = data_backService
                .getDataFieldListTitle(qDto.getString("tablename"));
        WebCxt.write(response, AOSJson.toGridJson(titleDtos));
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
        String query = dataService.queryConditions2(out);
        Dto outDto = Dtos.newDto();
        outDto.setAppMsg(query);
        WebCxt.write(response, AOSJson.toJson(outDto));

    }
}
