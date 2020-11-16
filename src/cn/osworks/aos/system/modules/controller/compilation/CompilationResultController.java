package cn.osworks.aos.system.modules.controller.compilation;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.compilation.CompilationResultService;
import cn.osworks.aos.system.modules.service.compilation.CompilationService;
import cn.osworks.aos.system.modules.service.compilation.ExamineService;
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

/**
 * 档案编研成果
 *
 * @author PX
 *
 * 2018-8-23
 */
@Controller
@RequestMapping(value="compilation/result")
public class CompilationResultController {
    @Autowired
    private CompilationService compilationService;
    @Autowired
    private DataService dataService;
    @Autowired
    private SqlDao sysDao;
    @Autowired
    private ExamineService examineService;
    @Autowired
    private CompilationResultService compilationResultService;
    /**
     * 在编课题结果页面
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     * @return
     * 2018-8-23
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("subject")
    public String subject(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException{
        Dto out= Dtos.newDto(request);
        String cascode_id_=	out.getString("cascode_id_");
        request.setAttribute("cascode_id_",cascode_id_);
        request.setAttribute("aos_module_id_",out.getString("aos_module_id_"));
        //得到年度
        String nd=compilationResultService.getModuleName(out.getString("aos_module_id_"));
        request.setAttribute("nd",nd);
        return "aos/compilation/subject_result.jsp?time="+new Date().getTime()+"&cascode_id_="+cascode_id_+"&nd="+nd;
    }
    /**
     * 加载数据
     *
     * @author PX
     * @param request
     * @param response
     * @param session
     * @return
     * 2018-8-23
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("listCompilation")
    public void listCompilation(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException{
        Dto inDto = Dtos.newDto(request);
        //标题列表
        List<Map<String, Object>> fieldDtos = compilationResultService.listCompilation(inDto);
        int pCount = compilationResultService.getPageAll(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos,pCount);
        // 就这样返回转换为Json后返回界面就可以了。
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
    @RequestMapping(value="listAccounts")
    public void listAccounts(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Dto inDto = Dtos.newDto(request);
        //标题列表
        List<Map<String,Object>> fieldDtos =compilationResultService.getData(inDto,session);
        int pCount = compilationResultService.getPageTotal_Data(inDto);
        //条目数据
        String outString = AOSJson.toGridJson(fieldDtos, pCount);
        // 就这样返回转换为Json后返回界面就可以了。
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
        String id_=inDto.getString("id_");
        //当前compilation的id值
        String topic_id_=inDto.getString("topic_id_");
        String zt_id=inDto.getString("zt_id");
        Dto dto=compilationResultService.getpath(inDto,id_,zt_id);
        //条目数据
        String outString = AOSJson.toJson(dto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    @RequestMapping("getChengGuopath")
    public void getChengGuopath(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto dto = Dtos.newDto();
        //条目数据id值
        String id_=inDto.getString("id_");
        //当前compilation的id值
        String filename=inDto.getString("filename");
        String filepath=compilationResultService.getChengGuopath(id_,filename);
        //条目数据
        dto.put("pdfpath",filepath);
        String outString = AOSJson.toJson(dto);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     *得到合稿人稿子内容
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "gethegaomessage")
    public void gethegaomessage(HttpServletRequest request,
                                HttpServletResponse response, HttpSession session){
        Dto qDto = Dtos.newDto(request);
        Dto out=Dtos.newDto();
        String message=compilationResultService.gethegaomessage(qDto);
        out.put("hegaomessage",message);
        WebCxt.write(response, AOSJson.toJson(out));
    }
    /**
     *
     *
     * 查询数据信息(合稿)
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "listZg_hegao")
    public void listZg_hegao(HttpServletRequest request,
                             HttpServletResponse response, HttpSession session) {
        Dto inDto = Dtos.newDto(request);
        inDto.put("tablename", inDto.getString("tablename"));
        List<Map<String,Object>> fieldDtos = compilationResultService.listZg_hegao(inDto,session);
        String outString = AOSJson.toGridJson(fieldDtos);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
    /**
     * 得到撰稿人撰稿的内容根据路径查找文件
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "getzhuangaomessage")
    public void getzhuangaomessage(HttpServletRequest request,
                                   HttpServletResponse response, HttpSession session){
        Dto inDto = Dtos.newDto(request);
        Dto out = Dtos.newDto();
        String zg_message=compilationResultService.getzhuangaomessage(inDto);
        out.setAppMsg(zg_message);
        String outString = AOSJson.toJson(out);
        // 就这样返回转换为Json后返回界面就可以了。
        WebCxt.write(response, outString);
    }
}
