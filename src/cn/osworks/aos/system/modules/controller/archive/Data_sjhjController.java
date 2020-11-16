package cn.osworks.aos.system.modules.controller.archive;


import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tableinputMapper;
import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.dao.po.Aos_sys_dicPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tableinputPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.Data_sjhjService;
import cn.osworks.aos.system.modules.service.archive.DatatemporaryService;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.archive.UploadService;
import cn.osworks.aos.system.modules.service.dbtable.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 数据核检
 */
@Controller
@RequestMapping(value = "archive/data_sjhj")
public class Data_sjhjController {
    @Autowired
    private Data_sjhjService data_sjhjService;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private DocService docService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private InputService inputService;
    @Autowired
    private Archive_tableinputMapper archive_tableinputMapper;
    @Autowired
    private SqlDao sysDao;
    /**
     *
     * 页面初始化
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "initData_sjhj")
    public String initData_sjhj(HttpSession session, HttpServletRequest request,
                           HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> titleDtos = data_sjhjService
                .getDataFieldListTitle(qDto.getString("tablename"));
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        Object pagesize = userInfoVO.getPagesize();
        if(pagesize == null||pagesize==""){
            pagesize = "20";
        }
        String tree=data_sjhjService.findTree(qDto);
        String treenumber="";
        String treename="";
        if(tree!=null&&tree.length()>4){
            treenumber=tree.substring(0,4);
            treename=tree.substring(4);
        }else{
            if("全部".equals(tree)){
                treename="全部";
            }
        }
        String nd=data_sjhjService.findNd(qDto);
        String queryclass = data_sjhjService.isAll(qDto);
        request.setAttribute("cascode_id_", qDto.getString("cascode_id_"));
        request.setAttribute("tablename", qDto.getString("tablename"));
        session.setAttribute("tablename", qDto.getString("tablename"));
        request.setAttribute("treenumber",treenumber);
        request.setAttribute("treename",treename);
        request.setAttribute("aos_module_id_",qDto.getString("aos_module_id_"));
        request.setAttribute("aos_page_id_",qDto.getString("aos_module_id_"));
        request.setAttribute("nd",nd);
        session.setAttribute("queryclass", queryclass);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        //return "aos/archive/tree.jsp";
        return "aos/receive/data_sjhj.jsp";
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
        String id_=inDto.getString("id_");
        //当前compilation的id值
        String tablename=inDto.getString("tablename");
        Dto dto=data_sjhjService.getpath(id_,tablename);
        //条目数据
        String outString = AOSJson.toJson(dto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    /**
     *
     * 获得表单录入项
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getInput")
    public void getInput(HttpServletRequest request,
                         HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        List<Archive_tableinputPO> list = archive_tableinputMapper.list(inDto);
        WebCxt.write(response, AOSJson.toGridJson(list));

    }
    /**
     *
     * 加载录入界面字典
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "load_dic_index")
    public void load_dic_index(HttpServletRequest request,
                               HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        qDto.setOrder("id_ DESC");
        qDto.put("catalog_id_", "520591d0ad114faf8a1a7e8191f35636");
        List<Dto> list = sysDao.list("MasterData.listDicindexInfos", qDto);
        qDto.put("dic_index_id_", list.get(0).getString("id_"));
        List<Aos_sys_dicPO> listdic = sysDao.list("MasterData.listDicInfos",
                qDto);
        System.out.print(AOSJson.toJson(listdic));
        WebCxt.write(response, AOSJson.toGridJson(listdic));
    }
    @RequestMapping(value="updateData")
    public void updateData(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        Dto out=data_sjhjService.updateData(qDto);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 获得单条信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getData")
    public void getData(HttpServletRequest request, HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        Object outDto = data_sjhjService.getData(qDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     *
     * 查询数据信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listAccounts2")
    public void listAccounts2(HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        Dto inDto = Dtos.newDto(request);
        inDto.put("tablename", inDto.getString("tablename"));
        String queryclass = data_sjhjService.isAll(inDto);
        inDto.put("queryclass", queryclass);
        request.getSession().setAttribute("querySession",
                data_sjhjService.queryConditions(inDto));
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        List<Dto> fieldDtos = data_sjhjService.getDataFieldListDisplayAll(inDto,
                userInfoVO.getAccount_());
        int pCount = data_sjhjService.getPageTotal(inDto);
        String outString = AOSJson.toGridJson(fieldDtos, pCount);
        // 将分类坐标存放到session中
        session.setAttribute("queryclass", queryclass);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
}
