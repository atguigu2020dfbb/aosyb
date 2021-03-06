package cn.osworks.aos.system.modules.controller.declared;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.modules.service.declared.DeclaredService;
import cn.osworks.aos.system.modules.service.sensitive.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="archive/declared")
public class DeclaredController {
    @Autowired
    private DeclaredService declaredService;
    @Autowired
    private SqlDao sysDao;
    /**
     * 页面初始化
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     * @returnlistgenerals
     *
     * 2018-8-23
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("initData")
    public String initData(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {



        return "aos/declared/declared.jsp?time="+new Date().getTime();
    }
    /**
     * 全宗列表
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
    @RequestMapping("listdeclared")
    public void listdeclared(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException{
        Dto dto= Dtos.newDto();
        List<Map<String,Object>> list=declaredService.listdeclared(dto);
        String outString = AOSJson.toGridJson(list);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    /**
     * 添加
     * @param request
     * @param response
     * @param session
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="adddeclared")
    public void adddeclared(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException{
        Dto dto=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        boolean b=declaredService.adddeclared(dto);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        String outString = AOSJson.toJson(outDto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }

    /**
     * 删除
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value="deldeclared")
    public void delsensitive(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto out=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        boolean b=declaredService.deldeclared(out);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        String outString = AOSJson.toJson(outDto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     * 修改
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value="updatedeclared")
    public void updatesensitive(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto out=Dtos.newDto(request);
        Dto outDto=Dtos.newDto();
        boolean b=declaredService.updatedeclared(out);
        if(b){
            outDto.setAppCode(1);
        }else{
            outDto.setAppCode(-1);
        }
        String outString = AOSJson.toJson(outDto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     * 得到列名列表
     *
     * @author PX
     * @param request
     * @param response
     *
     * 2018-8-23
     */
    @RequestMapping(value="getQueryTitle")
    public void getQueryTitle(HttpServletRequest request,HttpServletResponse response,HttpSession session){

        Dto inDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> list=declaredService.getQueryTitle(inDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }

    @RequestMapping(value="gettablename")
    public void gettablename(HttpServletRequest request,HttpServletResponse response,HttpSession session){

        Dto inDto = Dtos.newDto(request);
        String tablename=declaredService.gettablename(inDto);
        Dto out=Dtos.newDto();
        out.setAppMsg(tablename);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     * 得到列名列表
     *
     * @author PX
     * @param request
     * @param response
     *
     * 2018-8-23
     */
    @RequestMapping(value="getQueryTitlename")
    public void getQueryTitlename(HttpServletRequest request,HttpServletResponse response,HttpSession session){

        Dto inDto = Dtos.newDto(request);
        List<Archive_tablefieldlistPO> list=declaredService.getQueryTitlename(inDto);
        WebCxt.write(response, AOSJson.toGridJson(list));
    }
    /**
     * 得到列名列表
     *
     * @author PX
     *
     * 2018-8-23
     */
    @RequestMapping(value="getdeclared")
    public void getdeclared(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
       Map<String,Object> map=declaredService.getdeclared(inDto);
        WebCxt.write(response, AOSJson.toJson(map));
    }
}

