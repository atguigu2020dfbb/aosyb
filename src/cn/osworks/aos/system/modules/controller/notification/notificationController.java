package cn.osworks.aos.system.modules.controller.notification;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Aos_sys_notificationMapper;
import cn.osworks.aos.system.dao.po.Aos_sys_notificationPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "notification")
public class notificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Aos_sys_notificationMapper aos_sys_notificationMapper;


    @RequestMapping(value = "sendMsg")
    public void sendMsg(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        //任务类型0提交 1审核
        Dto outDto = notificationService.getWait(qDto);

        WebCxt.write(response, AOSJson.toJson(outDto));

    }

    @RequestMapping(value = "initFinish")
    public String initFinish(HttpServletRequest request, HttpServletResponse response){

        return "aos/notification/personFinish.jsp";
    }

    @RequestMapping(value = "listFinish")
    public void listFinish(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        /*UserInfoVO userInfoVO = qDto.getUserInfo();
        qDto.put("username",userInfoVO.getAccount_());
        List<Aos_sys_notificationPO> list =  aos_sys_notificationMapper.selectFinish(qDto);
        String outString = AOSJson.toJson(list);
        WebCxt.write(response,outString);*/
        List<Map<String, Object>> list = notificationService.listFinish(qDto);
        String outString = AOSJson.toJson(list);
        WebCxt.write(response,outString);


    }

    @RequestMapping(value = "initWait")
    public String initWait(HttpServletRequest request, HttpServletResponse response){

        return "aos/notification/personWait.jsp";
    }
    @RequestMapping(value = "listWait")
    public void listWait(HttpServletRequest request, HttpServletResponse response){
        Dto qDto = Dtos.newDto(request);
        List<Map<String, Object>> list = notificationService.listWait(qDto);
        String outString = AOSJson.toJson(list);
        WebCxt.write(response,outString);
    }
    @RequestMapping(value = "updateNotification")
    public void updateNotification(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        notificationService.updateNotification(inDto);
    }
}
