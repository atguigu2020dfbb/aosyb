package cn.osworks.aos.system.modules.service.backups;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Service
public class BackupsService extends JdbcDaoSupport {
    
    @Autowired
    private Backups_strategyMapper backups_strategyMapper;

    @Autowired
    private Backups_carrierMapper backups_carrierMapper;

    @Autowired
    private Backups_detailMapper backups_detailMapper;

    @Autowired
    private Backups_logMapper backups_logMapper;
    @Autowired
    private SetwatermarkMapper setwatermarkMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    
    public Dto saveStrategy(Dto inDto){
        Dto outDto = Dtos.newDto();
        Backups_strategyPO backups_strategyPO = new Backups_strategyPO();
        AOSUtils.copyProperties(inDto,backups_strategyPO);
        backups_strategyPO.setId_(AOSId.uuid());
        backups_strategyMapper.insert(backups_strategyPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updateStrategy(Dto inDto){
        Dto outDto = Dtos.newDto();
        Backups_strategyPO backups_strategyPO = new Backups_strategyPO();
        AOSUtils.copyProperties(inDto,backups_strategyPO);
        backups_strategyMapper.updateByKey(backups_strategyPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deleteStrategy(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            backups_strategyMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }
    
    /** 备份载体 **/

    public Dto saveCarrier(Dto inDto){
        Dto outDto = Dtos.newDto();
        Backups_carrierPO backups_carrierPO = new Backups_carrierPO();
        AOSUtils.copyProperties(inDto,backups_carrierPO);
        backups_carrierPO.setId_(AOSId.uuid());
        backups_carrierMapper.insert(backups_carrierPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updateCarrier(Dto inDto){
        Dto outDto = Dtos.newDto();
        Backups_carrierPO backups_carrierPO = new Backups_carrierPO();
        AOSUtils.copyProperties(inDto,backups_carrierPO);
        backups_carrierMapper.updateByKey(backups_carrierPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deleteCarrier(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            backups_carrierMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    /** 备份载体 **/

    public Dto saveBackups(Dto inDto){
        Dto outDto = Dtos.newDto();
        Backups_detailPO backups_detailPO = new Backups_detailPO();
        AOSUtils.copyProperties(inDto,backups_detailPO);
        backups_detailPO.setId_(AOSId.uuid());
        backups_detailMapper.insert(backups_detailPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }


    public void selectinto(Dto inDto){
        try{
            backups_detailMapper.selectinto(inDto);
        }catch (Exception ex){
            System.out.print(ex);
        }


    }


    public int deleteBackups(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            backups_detailMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    public void returnTable(Dto inDto){

        backups_detailMapper.returntable(inDto);
    }

    public void deleteTable(String tablename){
        backups_detailMapper.deleteTable(tablename);
    }

    public void insertLog(Dto inDto){

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Backups_logPO backups_logPO = new Backups_logPO();
        //AOSUtils.copyProperties(backups_logPO,inDto);
        backups_logPO.setId_(AOSId.uuid());
        backups_logPO.setHyb(inDto.getString("bfbm"));
        backups_logPO.setHywj(inDto.getString("bfmc"));
        backups_logPO.setHysj(df.format(new Date()));
        backups_logPO.setHyr(inDto.getUserInfo().getName_());
        backups_logMapper.insert(backups_logPO);
    }
    /**
     *  水印设置加存
     * @param setwatermarkPO
     * @return
     */
    public Dto saveSetWatermark(SetwatermarkPO setwatermarkPO){
        Dto outDto = Dtos.newDto();
        String msg = null;
        int temp  = setwatermarkMapper.insert(setwatermarkPO);
        if (temp==1){
            msg = AOSUtils.merge("保存成功");
        }else {
            msg = AOSUtils.merge("保存失败");
        }

        outDto.setAppMsg(msg);
        return outDto;
    }

    /**
     * 水印设置修改
     * @param setwatermarkPO
     * @return
     */
    public Dto updateSetWatermark(SetwatermarkPO setwatermarkPO){
        Dto outDto = Dtos.newDto();
        String msg = null;
        int temp  = setwatermarkMapper.updateByKey(setwatermarkPO);
        if (temp==1){
            msg = AOSUtils.merge("保存成功");
        }else {
            msg = AOSUtils.merge("保存失败");
        }

        outDto.setAppMsg(msg);
        return outDto;
    }

    public SetwatermarkPO selectOnegetSetwatermark(Dto dto) {
        return setwatermarkMapper.selectOne(dto);
    }

    public Dto backupData11() {
        // TODO Auto-generated method stub
        Dto out=Dtos.newDto();
        String filename =  new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date().getTime());
        Properties prop=null;
        try {
            prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Properties prop2=null;
        try {
            prop2 = PropertiesLoaderUtils.loadAllProperties("sjb.properties");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String base= prop.getProperty("filePath")+"/backup";
        //判断路径是否存在。不存在创建一个
        File file=new File(base);
        if (file.exists()) {
            if (file.isDirectory()) {
            } else {
            }
        } else {
            file.mkdir();
        }
        //System.out.println(time);
        //当前时间毫秒值
        //long filename=System.currentTimeMillis();
        String database=prop2.getProperty("sjk");
        String selectsql = "backup database " + database + " to disk='" + base+"/"+ filename+"' with format,name='123'";
        jdbcTemplate.execute(selectsql);
        out.setAppCode(1);
        out.put("filepath", base+"/"+ filename);
        out.put("name", filename);
        return out;
    }
}
