package cn.osworks.aos.system.modules.controller.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "web")
public class MessageController {
    //websocket服务层调用类
    private Logger logger = LoggerFactory.getLogger(WSMessageService.class);
    @Autowired
    private WSMessageService wsMessageService;

    //请求入口
    @RequestMapping(value="TestWS",method= RequestMethod.GET)
    @ResponseBody
    public String TestWS(@RequestParam(value="userId",required=true) String userId,
                         @RequestParam(value="message",required=true) String message){
        logger.debug("收到发送请求，向用户{}的消息：{}",userId,message);
        if(wsMessageService.sendToAllTerminal(userId, message)){
            return "发送成功";
        }else{
            return "发送失败";
        }
    }
}
