package cn.osworks.aos.system.modules.controller.archive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.dao.po.Aos_sys_orgPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.LogPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.AppraisalService;
import cn.osworks.aos.system.modules.service.archive.LogDaoService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;

/**
 * 日志页面控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "appraisal")
public class AppraisalController extends JdbcDaoSupport {
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    private SqlDao sysDao;
    @Autowired
    public LogMapper logMapper;
    @Autowired
    private AppraisalService appraisalservice;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    /**
     * 页面初始化
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("initData")
    public String initData(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session) {
        //字段列表
        List<Archive_tablefieldlistPO> titleDtos = appraisalservice.getDataFieldListTitle("swda");
        request.setAttribute("fieldDtos", titleDtos);
        return "aos/archive/appraisal.jsp";
    }
    /**
     * 删除档案
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("deleteData")
    public void deleteData(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session) {
        Dto dto = Dtos.newDto(request);
        Dto outDto = appraisalservice.deleteData(dto);
        //日志
        UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
        String tm=dto.getString("tm").split(",")[0];
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(), userInfoVO.getAccount_(), "删除档案["+dto.getString("tablename")+"]",tm,"删除档案", request.getRemoteAddr(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        logMapper.insert(logPO);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     *
     * 下拉列表
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="listComboBoxid")
    public void listComboBoxid(HttpServletRequest request,HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        List<Dto> list = sysDao.list("Resource.listComboBoxid", dto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    /**
     *
     * 页面初始化得到字段列表
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="selectappraisal")
    public void selectappraisal(HttpServletRequest request,HttpServletResponse response){
        Dto dto = Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        String tableTemplate=dto.getString("tableTemplate");
        //条目列表
        List<Map<String,Object>> selectappraisal = appraisalservice.selectappraisal(tableTemplate);
        if(selectappraisal==null){
            outDto.setAppCode(-1);
            outDto.setAppMsg("不存在过期或销毁数据");
            WebCxt.write(response, AOSJson.toJson(outDto));
        }
        WebCxt.write(response, AOSJson.toGridJson(selectappraisal));
    }
}
