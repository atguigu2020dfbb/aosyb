package cn.osworks.aos.system.modules.controller.register;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Aos_sys_userPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.UploadService;
import cn.osworks.aos.system.modules.service.register.TransferService;
import cn.osworks.aos.web.report.AOSReport;
import cn.osworks.aos.web.report.AOSReportModel;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 档案利用登记调档
 *
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="transfer/transfer")
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private DataService dataService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private SqlDao sysDao;
    /**
     * 页面初始化
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
    @RequestMapping("initInput")
    public String initInput(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
        Dto dto=Dtos.newDto(request);
        request.setAttribute("aos_module_id_",Dtos.newDto(request).getString("aos_module_id_"));
        String listtablename=dto.getString("listtablename");
        //listtablename="wsda";
        String tablenamedesc=dto.getString("tablenamedesc");
        tablenamedesc=URLDecoder.decode(tablenamedesc,"utf-8");
        if(listtablename==null||listtablename.equals("")||listtablename.equals("null")){
            return "aos/register/transfer.jsp?time="+new Date().getTime();
        }
        List<Archive_tablefieldlistPO> title = transferService.getDataFieldListTitle(listtablename);
        request.setAttribute("listtablename", listtablename);
        request.setAttribute("tablenamedesc", tablenamedesc);
        request.setAttribute("fieldDtos", title);
        return "aos/register/transfer.jsp?time="+new Date().getTime();
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
    @RequestMapping(value="listAccounts")
    public void listAccounts(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        List<Map<String,Object>> fieldDtos = transferService.listAccounts(session);
        //条目数据
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
        List<Archive_tablenamePO> list = transferService.findTablename();
        String outString =AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
    }
    /**
     * 根据id得到借阅信息
     *
     * @author PX
     * @param session
     * @param request
     * @param response
     *
     * 2018-8-20
     * @throws ParseException
     * @throws IOException
     */
    @RequestMapping(value="jymessage")
    public void jymessage(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws ParseException, IOException{
        Dto outdto = Dtos.newDto(request);
        Map<String, Object> jymessage = transferService.jymessage(outdto);
        WebCxt.write(response, AOSJson.toJson(jymessage));
    }
    /**
     * 借阅保存
     * @author PX
     * @param session
     * @param request
     * @param response
     *
     * 2018-8-29
     */
    @RequestMapping("savejy")
    public void savejy(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto outDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        try {
            if(outDto.getString("archivestate").equals("未借阅")){
                boolean b = transferService.savejy(outDto);
                if(!b){
                    out.setAppCode(-2);
                }else{
                    out.setAppCode(1);
                }
            }else{
                transferService.updatejy(outDto);
                out.setAppCode(-1);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            out.setAppCode(1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 归还操作
     *
     * @author PX
     * @param session
     * @param request
     * @param response
     *
     * 2018-8-30
     */
    @RequestMapping("returnjy")
    public void returnjy(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto outDto=Dtos.newDto(request);
        Dto out=Dtos.newDto();
        int i = transferService.returnjy(outDto,session);
        if(i==1){
            out.setAppCode(1);
        }else if(i==-1){
            out.setAppCode(-1);
        }else{
            out.setAppCode(-2);
        }
        WebCxt.write(response, AOSJson.toJson(out));
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
        String outString = AOSJson.toGridJson(list);
        WebCxt.write(response, outString);
    }
    /**
     * 借阅里列表
     *
     * @author PX
     * @param session
     * @param request
     * @param response
     *
     * 2018-8-30
     */
    @RequestMapping("listjy")
    public void listjy(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Dto dto=Dtos.newDto(request);
        List<Map<String,Object>> list = transferService.listjy(dto);
        String outString="";
        if(list!=null&&list.size()>0){
            outString = AOSJson.toGridJson(list,list.size());
        }
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
        lhm.put("id_", "id_");
        lhm.put("users", "用户");
        lhm.put("jytime", "借阅时间");
        lhm.put("archive_id", "档案号");
        lhm.put("tablename", "操作门类");
        lhm.put("jyday", "借阅天数");
        lhm.put("archivestate", "借阅状态");
        lhm.put("gh", "是否归还");
        List<Map<String, Object>> titleDtos = transferService.listjy(qDto);
        // 组装报表数据模型
        AOSReportModel reportModel = new AOSReportModel();
        reportModel.setFileName("借阅信息表");
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
     * 查询字段下拉框
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="queryFields")
    public void queryFields(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> titleDtos = transferService.getDataFieldListTitle(qDto.getString("tablename"));
        String gridJson = AOSJson.toGridJson(titleDtos);
        WebCxt.write(response, AOSJson.toGridJson(titleDtos));
    }
    /**
     *
     * 查询用户下拉框
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="queryUsers")
    public void queryUsers(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        List<Aos_sys_userPO> titleDtos = transferService.queryUsers();
        WebCxt.write(response, AOSJson.toGridJson(titleDtos));
    }
    /**
     *
     * 提交申请
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="submit_application")
    public void submit_application(HttpServletRequest request,HttpServletResponse response){

        Dto out=Dtos.newDto();
        Dto qDto = Dtos.newDto(request);
        boolean b = transferService.submit_application(qDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     * 我的借阅
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="myborrow")
    public void myborrow(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        //得到当前用户
        UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");

        List<Map<String,Object>> list = transferService.myborrow(qDto,userInfoVO.getAccount_());
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    /**
     * 得到用户待审核列表
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     * 2018-12-4
     */
    @RequestMapping("listExamine")
    public void listExamine(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        //得到当前用户
        UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
        List<Map<String, Object>> list= transferService.listExamine();
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    /**
     * 同意借阅
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     * 2018-12-5
     */
    @RequestMapping("againExamine")
    public void againExamine(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        //得到id修改这个为已审核
        boolean b = transferService.againExamine(qDto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     * 不同意借阅，并存入原因
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     * 2018-12-5
     */
    @RequestMapping("unagainExamine")
    public void unagainExamine(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        //得到id修改这个为不通过
        boolean b = transferService.unagainExamine(qDto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     * 打开没通过详情
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     * 2018-12-5
     */
    @RequestMapping("openmessage")
    public void openmessage(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = Dtos.newDto();
        String description="";
        List<Map<String,Object>> list = transferService.openmessage(qDto);
        if(list!=null&&list.size()>0){
            description=(String)list.get(0).get("description");
        }
        outDto.put("description", description);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping("listuserExamine")
    public void listuserExamine(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        //得到当前用户
        UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
        List<Map<String, Object>> list= transferService.listuserExamine(userInfoVO.getAccount_());
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    /**
     * 用户看到的详情设为已读
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     * 2018-12-5
     */
    @RequestMapping("ReadborrowMessage")
    public void ReadborrowMessage(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = Dtos.newDto();
        UserInfoVO userInfoVO=(UserInfoVO) session.getAttribute("_userInfoVO");
        boolean b = transferService.updateReadBorrow(userInfoVO,qDto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     * 档案续借
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     * 2018-12-6
     */
    @RequestMapping("updateRenewalBorrow")
    public void updateRenewalBorrow(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = Dtos.newDto();
        boolean b = transferService.updateRenewalBorrow(qDto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     * 归还档案
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     *
     * 2018-12-6
     */
    @RequestMapping("returnRenewalBorrow")
    public void returnRenewalBorrow(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = Dtos.newDto();
        boolean b = transferService.returnRenewalBorrow(qDto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     * 获取电子文件列表
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("getFiles")
    public void getFiles(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto outDto = Dtos.newDto();
        List<Map<String,Object>> list = transferService.getFiles(qDto);
        WebCxt.write(response, AOSJson.toJson(list));
    }
    /**
     * 查看电子文件权限
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("booleanBorrowFile")
    public void booleanBorrowFile(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto dto=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        String user=(String)session.getAttribute("user");
		/*boolean b = uploadService.booleanBorrowFile(dto,user);
		if(b){
			outDto.setAppCode(1);
		}else{
			outDto.setAppCode(-1);
		}*/
        WebCxt.write(response, AOSJson.toJson(outDto));
    }



    /**
     * 查询当前用户岗位
     *
     * @author PX
     * @param response
     * @param request
     * @param session
     *
     * 2018-12-7
     */
    @RequestMapping("getPostUser")
    public void getPostUser(HttpServletResponse response,
                            HttpServletRequest request, HttpSession session){
        Dto outDto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
        String posts = transferService.getPostUser(outDto);
        out.setAppMsg(posts);
        WebCxt.write(response, AOSJson.toJson(out));
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
        Dto dto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
        String id = AOSId.id(dto.getString("name_"));
        out.setAppMsg(id);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 保存调档信息
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "saveTransfer")
    public void saveTransfer(HttpServletResponse response,
                             HttpServletRequest request, HttpSession session){
        Dto dto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
         boolean b=transferService.saveTransfer(dto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 保存调档信息
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "getTransfer")
    public void getTransfer(HttpServletResponse response,
                            HttpServletRequest request, HttpSession session){
        Dto dto = Dtos.newDto(request);
        List<Map<String,Object>> list=transferService.getTransfer(dto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    /**
     * 档案详情列表
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping("archive_details")
    public String archive_details(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        String id_=qDto.getString("id_");
        //根据id_
        //String tablename=compilationService.findTablename(id_);
        String tablename=qDto.getString("tablename");
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle(tablename);
        UserInfoVO userInfoVO = WebCxt.getUserInfo(session);
        String pagesize = userInfoVO.getPagesize() + "";
        if (pagesize == null) {
            pagesize = "20";
        }
        request.setAttribute("tablename", tablename);
        session.setAttribute("tablename", tablename);
        request.setAttribute("fieldDtos", titleDtos);
        request.setAttribute("pagesize", pagesize);
        request.setAttribute("transfer_id_", id_);
        return "aos/register/detailsMessage.jsp";
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
        List<Map<String,Object>> fieldDtos = transferService.getDataFieldListDisplayAll2(inDto,session);
        String outString = AOSJson.toGridJson(fieldDtos);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     *
     * 删除标记
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "deleteTransferData")
    public void deleteTransferData(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
       Dto out= transferService.deleteTransferData(inDto);
        String outString = AOSJson.toJson(out);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    @RequestMapping(value = "addXiaoduDengji")
    public void addXiaoduDengji(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b= transferService.addXiaoduDengji(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value = "addPosunDengji")
    public void addPosunDengji(HttpServletRequest request,
                                HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b= transferService.addPosunDengji(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value = "addXiufuDengji")
    public void addXiufuDengji(HttpServletRequest request,
                               HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        boolean b= transferService.addXiufuDengji(inDto);
        if(b){
            out.setAppCode(1);
        }else{
            out.setAppCode(-1);
        }
        WebCxt.write(response, AOSJson.toJson(out));
    }
    @RequestMapping(value = "getRws")
    public void getRws(HttpServletRequest request,
                       HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        List<Map<String,Object>> list=transferService.getRws(inDto,session);
        String outString = AOSJson.toGridJson(list);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
}
