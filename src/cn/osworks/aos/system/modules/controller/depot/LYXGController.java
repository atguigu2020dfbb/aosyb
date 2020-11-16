package cn.osworks.aos.system.modules.controller.depot;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Depot_lyxgMapper;
import cn.osworks.aos.system.dao.po.Depot_rkPO;
import cn.osworks.aos.system.dao.po.LYXG_PO;
import cn.osworks.aos.system.modules.service.depot.LYXGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//利用效果Controller
@Controller
@RequestMapping(value = "depot/lyxg")
public class LYXGController {

    @Autowired
    private Depot_lyxgMapper depot_lyxgMapper;

    @Autowired
    private LYXGService lyxgServicel;

    //跳转利用效果界面并携带年度
    @RequestMapping(value = "initLYXG")
    public String initLYXG(HttpServletRequest request, HttpServletResponse response) {
        Dto qDto = Dtos.newDto(request);
        request.setAttribute("nd", qDto.getString("nd"));
        request.setAttribute("user", qDto.getUserInfo().getName_());
        return "aos/depot/lyxg.jsp";
    }

    //查询利用效果表数据
    @RequestMapping(value = "listLYXGIn")
    public void listLYXGIn(HttpServletResponse response, HttpServletRequest request) {
        Dto qDto = Dtos.newDto(request);
        List<LYXG_PO> list = depot_lyxgMapper.likePage(qDto);
        String outString = AOSJson.toGridJson(list, qDto.getPageTotal());
        WebCxt.write(response, outString);
    }

    //新增利用效果信息
    @RequestMapping(value = "saveDepotIn")
    public void saveDepotIn(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        Dto outDto = lyxgServicel.saveDepotIn(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }

    //删除利用效果信息
    @RequestMapping(value = "deleteDepotIn")
    public void deleteDepotIn(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        int row = lyxgServicel.deleteDepotin(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。", row);
        WebCxt.write(response, outString);
    }

    //修改利用效果信息
    @RequestMapping(value = "updateDepotIn")
    public void updateDepotIn(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = Dtos.newDto(request);
        Dto outDto = lyxgServicel.updateDepotIn(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    @RequestMapping("getLyxgIndex")
    public void getLyxgIndex(HttpServletRequest request, HttpServletResponse response){
        Dto outDto = Dtos.newDto(request);
        String index=lyxgServicel.getLyxgIndex(outDto);
        outDto.put("index",index);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }
}
