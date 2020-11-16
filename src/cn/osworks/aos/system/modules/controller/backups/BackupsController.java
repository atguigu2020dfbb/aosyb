package cn.osworks.aos.system.modules.controller.backups;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.service.backups.BackupsService;
import cn.osworks.aos.system.modules.service.utilization.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping( value = "backups/strategy")
public class BackupsController {
    @Autowired
    private Backups_strategyMapper backups_strategyMapper;

    @Autowired
    private Backups_carrierMapper backups_carrierMapper;

    @Autowired
    private Archive_tablenameMapper archive_tablenameMapper;

    @Autowired
    private Backups_detailMapper backups_detailMapper;

    @Autowired
    private Backups_logMapper backups_logMapper;

    @Autowired
    private BackupsService backupsService;
    @Autowired
    private UtilizationService utilizationService;

   @RequestMapping(value="initStrategy")
    public String initStrategy(HttpServletResponse response, HttpServletRequest request){
        return "aos/backups/initStrategy.jsp";
    }

    @RequestMapping(value = "listStrategy")
    public void listStrategy(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto();
        List<Backups_strategyPO> list = backups_strategyMapper.list(qDto);
        String outString = AOSJson.toGridJson(list,qDto.getPageTotal());
        WebCxt.write(response,outString);
    }

    @RequestMapping(value = "saveStrategy")
    public void saveStrategy(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =backupsService.saveStrategy(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "updateStrategy")
    public void updateStrategy(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = backupsService.updateStrategy(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deleteStrategy")
    public void deleteStrategy(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = backupsService.deleteStrategy(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    @RequestMapping(value="initCarrier")
    public String initCarrier(HttpServletResponse response, HttpServletRequest request){
        return "aos/backups/initCarrier.jsp";
    }

    @RequestMapping(value = "listCarrier")
    public void listCarrier(HttpServletRequest request,HttpServletResponse response){
        Dto qDto = Dtos.newDto();
        List<Backups_carrierPO> list = backups_carrierMapper.list(qDto);
        String outString = AOSJson.toGridJson(list,qDto.getPageTotal());
        WebCxt.write(response,outString);
    }

    @RequestMapping(value = "saveCarrier")
    public void saveCarrier(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto =backupsService.saveCarrier(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value = "updateCarrier")
    public void updateCarrier(HttpServletResponse response,HttpServletRequest request){
        Dto inDto = Dtos.newDto(request);
        Dto outDto = backupsService.updateCarrier(inDto);
        WebCxt.write(response,AOSJson.toJson(outDto));
    }

    @RequestMapping(value="deleteCarrier")
    public void deleteCarrier(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = backupsService.deleteCarrier(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    @RequestMapping(value="initBackups")
    public String initBackups(HttpServletRequest request,HttpServletResponse response){
       return "aos/backups/initBackups.jsp";
    }



    /**
     *
     * 查询表信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="listAccounts")
    public void listAccounts(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        List<Archive_tablenamePO> list = archive_tablenameMapper.listPage(inDto);
        String outString =AOSJson.toGridJson(list,inDto.getPageTotal());
        WebCxt.write(response, outString);
    }

    /**
     *
     * 查询备份记录信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value="listBackups")
    public void listBackups(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        List<Backups_detailPO> list = backups_detailMapper.listPage(inDto);
        String outString =AOSJson.toGridJson(list,inDto.getPageTotal());
        WebCxt.write(response, outString);
    }

    @RequestMapping(value="saveBackups")
    public void saveBackups(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String bfmc=inDto.getString("bfbm")+df.format(new Date());
        inDto.put("bfsj",df.format(new Date()));
        inDto.put("bflx","单表备份");
        inDto.put("bfr",inDto.getUserInfo().getName_());
        inDto.put("bfmc",bfmc);
        backupsService.selectinto(inDto);
        Dto outDto = backupsService.saveBackups(inDto);

        WebCxt.write(response,AOSJson.toJson(outDto));
    }


    @RequestMapping(value="deleteBackups")
    public void deleteBackups(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        int row = backupsService.deleteBackups(inDto);
        String outString = AOSUtils.merge("操作完成，成功删除[{0}]条数据。",row);
        WebCxt.write(response,outString);
    }

    @RequestMapping(value="returnTable")
    public void returnTable(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);
        backupsService.deleteTable(inDto.getString("bfbm"));
        backupsService.returnTable(inDto);
        backupsService.insertLog(inDto);
        String outString = AOSUtils.merge("操作完成，已成功还原数据。");
        WebCxt.write(response,outString);
    }

    @RequestMapping(value="initLog")
    public String initLog(HttpServletRequest request,HttpServletResponse response){
        return "aos/backups/initLog.jsp";
    }

    @RequestMapping(value="listLog")
    public void listLog(HttpServletRequest request,HttpServletResponse response){
        Dto inDto = Dtos.newDto(request);

        List<Backups_logPO> list =  backups_logMapper.likePage(inDto);

        String outString = AOSJson.toGridJson(list,inDto.getPageTotal());
        WebCxt.write(response, outString);

    }

    /**
     * 获取设置
     * @param request
     * @param response
     * @param tablename
     */
    @RequestMapping(value = "getSetwatermark")
    public void getSetwatermark(HttpServletRequest request,HttpServletResponse response,
                                @RequestParam(value = "tablename",required = false) String tablename) {
        Dto dto = Dtos.newDto(request);
        dto.setStringA(tablename);
        SetwatermarkPO setwatermarkPO = backupsService.selectOnegetSetwatermark(dto);
        WebCxt.write(response, AOSJson.toJson(setwatermarkPO));
    }

    /**
     * 保存设置
     * @param request
     * @param response
     */
    @RequestMapping(value = "saveData")
    public void saveData(HttpServletRequest request,HttpServletResponse response){
        SetwatermarkPO setwatermarkPO = new SetwatermarkPO();
        Dto dto = Dtos.newDto(request);
        Dto outDto ;
        AOSUtils.copyProperties(dto,setwatermarkPO);
        if (setwatermarkPO.getId_() == null){
            //新增
            outDto =backupsService.saveSetWatermark(setwatermarkPO);

        }else {
            //修改
            outDto = backupsService.updateSetWatermark(setwatermarkPO);
        }

        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    /**
     * 水印设置页面
     * @return
     */
    @RequestMapping(value = "setWatermark")
    public String setWatermark() {
        //return "aos/backups/setWatermark.jsp";
        return "aos/backups/setWatermarkView.jsp";
    }
    @RequestMapping(value = "getPreview")
    public void getPreview(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Dto inDto = Dtos.newDto(request);
        Properties prop = PropertiesLoaderUtils
                .loadAllProperties("config.properties");
        String linkAddress = prop.getProperty("linkAddress");
        String filePath = prop.getProperty("filePath");
        String path = filePath  + "water-yulan/1/water.pdf";

        String waterpath=utilizationService.addWater2(inDto,path,filePath);
        String ftpwaterpath=linkAddress  +"/water-yulan/1/"+new File(waterpath).getName();


        WebCxt.write(response, ftpwaterpath);
    }
    /**
     * 数据库备份
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "backupData")
    public void backupData11(HttpServletRequest request,HttpServletResponse response){
        Dto dto = backupsService.backupData11();
        WebCxt.write(response, AOSJson.toJson(dto));
    }
    /**
     * 数据库备份
     * @param request
     * @param response
     * @param session
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @RequestMapping(value = "download")
    public void backupData(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{

        Dto inDto = Dtos.newDto(request);
        response.setContentType("application/xls");
        response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(inDto.getString("name"), "utf-8"));
        //好了 ，现在通过IO流来传送数据
        String strpath = inDto.getString("filepath");

        File file = new File(strpath);
        FileInputStream input=null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[]buff=new byte[1024*10];//可以自己 指定缓冲区的大小
            int len=0;
            while((len=input.read(buff))>-1)
            {
                outputStream.write(buff,0,len);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            outputStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
