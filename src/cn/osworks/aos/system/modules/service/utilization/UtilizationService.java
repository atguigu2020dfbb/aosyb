package cn.osworks.aos.system.modules.service.utilization;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.LogUtils;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.backups.BackupsService;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.google.zxing.client.j2se.MatrixToImageConfig.BLACK;
import static com.google.zxing.client.j2se.MatrixToImageConfig.WHITE;
import static org.apache.log4j.spi.Configurator.NULL;

@Service
public class UtilizationService extends JdbcDaoSupport {

    @Autowired
    private Archive_remoteMapper archive_remoteMapper;

    @Autowired
    private Archive_remote_detailMapper archive_remote_detailMapper;
    @Autowired
    private Archive_zzremote_detailMapper archive_zzremote_detailMapper;
    @Autowired
    private Archive_zzremoteMapper archive_zzremoteMapper;
    @Autowired
    private Archive_zz_applyMapper archive_zz_applyMapper;
    @Autowired
    private Archive_remote_jcMapper archive_remote_jcMapper;
    @Autowired
    private LogMapper logMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Archive_remote_advanceMapper archive_remote_advanceMapper;
    @Autowired
    private Archive_error_registerMapper archive_error_registerMapper;

    @Autowired
    private Archive_remote_logMapper archive_remote_logMapper;
    @Autowired
    private Archive_zizhu_applyMapper archive_zizhu_applyMapper;
    @Autowired
    private Archive_zizhuremote_detailMapper archive_zizhuremote_detailMapper;
    @Autowired
    private BackupsService backupsService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Depot_ckMapper depot_ckMapper;

    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    public static String filePath = "";
    public static String linkAddress = "";
    public static String topicAddress = "";
    // 通过静态代码块读取配置文件
    static {
        try {
            Properties props = new Properties();
            InputStream in = DataService.class
                    .getResourceAsStream("/config.properties");
            props.load(in);
            filePath = props.getProperty("filePath");
            linkAddress = props.getProperty("linkAddress");
            topicAddress = props.getProperty("topicAddress");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public Dto saveUtilization(Dto inDto){
        Dto outDto = Dtos.newDto();
        Dto dto=Dtos.newDto();
        //2.把撰稿的文稿存放到记事本txt文件中去，记事本的路径名称组合为D：dataaos/表明/任务编号/撰稿/撰稿人.txt
        String djpath=filePath+File.separator+"ly"+File.separator+inDto.getString("djbh");
        String djname=inDto.getString("xm")+".txt";
        //将信息保存到记事本中
        saveTxtFromDj(djpath,djname,inDto.getString("imagesrc"));
        Archive_remotePO archive_remotePO = new Archive_remotePO();
        AOSUtils.copyProperties(inDto,archive_remotePO);
        String id_=AOSId.uuid();
        archive_remotePO.setId_(id_);
        archive_remotePO.setImg(djpath+File.separator+djname);
        archive_remoteMapper.insert(archive_remotePO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，添加成功！！！");
        outDto.put("id_",id_);
        return  outDto;
    }
    private void saveTxtFromDj(String djpath, String djname, String imagesrc) {
        try{
            File file=new File(djpath);
            if(!file.exists()){
                file.mkdirs();
            }
            File fileall=new File(djpath+File.separator+djname);
            if(!fileall.exists()){
                fileall.createNewFile();
            }
            PrintStream ps = new PrintStream(new FileOutputStream(fileall));
            ps.println(imagesrc);// 往文件里写入字符串
            ps.close();
        }catch (Exception e){

        }
    }

    public Dto saveApply(Dto inDto){
        Dto outDto = Dtos.newDto();
        Archive_zz_applyPO archive_zz_applyPO = new Archive_zz_applyPO();
        AOSUtils.copyProperties(inDto,archive_zz_applyPO);
        String id_=AOSId.uuid();
        archive_zz_applyPO.setId_(id_);
        archive_zz_applyMapper.insert(archive_zz_applyPO);
        //添加档案信息到archive_remote_detail中
        String[] selections = inDto.getRows();
        String tablename = inDto.getString("tablename");
        int del = 0;
        for (String dataid_ : selections) {
            Archive_remote_detailPO archive_remote_detailPO=new Archive_remote_detailPO();
            archive_remote_detailPO.setFormid(id_);
            archive_remote_detailPO.setTid(dataid_);
            archive_remote_detailPO.setId_(AOSId.uuid());
            archive_remote_detailPO.setTablename(tablename);
            archive_remote_detailMapper.insert(archive_remote_detailPO);
        }
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，添加成功！！！");
        outDto.put("id_",id_);
        return  outDto;
    }
    public Dto updateUtilization(Dto inDto){
        Dto outDto = Dtos.newDto();
        Archive_remotePO archive_remotePO = new Archive_remotePO();
        AOSUtils.copyProperties(inDto,archive_remotePO);
        archive_remoteMapper.updateByKey(archive_remotePO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }
    public Dto updateSatisfied(Dto inDto){
        Dto outDto = Dtos.newDto();
        String id_=inDto.getString("id_");
        //满意度结果字符串
        String satisfied=mathToString(parseJSONWithJSONObject(inDto.getString("data")));
        Archive_remotePO archive_remotePO = new Archive_remotePO();
        archive_remotePO.setId_(id_);
        archive_remotePO.setLyrc(inDto.getString("lyrc"));
        archive_remotePO.setLyjac(inDto.getString("lyjac"));
        archive_remotePO.setLyjc(inDto.getString("lyjc"));
        archive_remotePO.setLylbb(inDto.getString("lylbb"));
        archive_remotePO.setMyd(satisfied);
        archive_remoteMapper.updateByKey(archive_remotePO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }
    //json字符串转换
    private String  parseJSONWithJSONObject(String JsonData) {
        try {
            org.activiti.engine.impl.util.json.JSONObject jsonArray = new org.activiti.engine.impl.util.json.JSONObject(JsonData);
           return jsonArray.getString("data");

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    //满意度数字汉子转换
    public String mathToString(String satisfied){
        if("1".equals(satisfied)){
            return "非常满意";
        }else if("2".equals(satisfied)){
            return "满意";
        }else if("3".equals(satisfied)){
            return "基本满意";
        }else if("4".equals(satisfied)){
            return "不满意";
        }else{
            return "非常满意";
        }
    }

    public Dto updateApplyzation(Dto inDto){
        Dto outDto = Dtos.newDto();
        Archive_zz_applyPO archive_zz_applyPO = new Archive_zz_applyPO();
        AOSUtils.copyProperties(inDto,archive_zz_applyPO);
        archive_zz_applyMapper.updateByKey(archive_zz_applyPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }
    public Dto updatezz_Applyzation(Dto inDto){
        Dto outDto = Dtos.newDto();
        Archive_zz_applyPO archive_zz_applyPO = new Archive_zz_applyPO();
        AOSUtils.copyProperties(inDto,archive_zz_applyPO);
        archive_zz_applyMapper.updateByKey(archive_zz_applyPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        //同时添加日志
        addRemote_log(inDto);
        return outDto;
    }
    private void addRemote_log(Dto inDto) {
        String id_=inDto.getString("id_");
        Archive_remote_logPO archive_remote_logPO=new Archive_remote_logPO();
        archive_remote_logPO.setId_(AOSId.uuid());
        archive_remote_logPO.setTid(id_);
        archive_remote_logPO.setCzr(inDto.getUserInfo().getName_());
        archive_remote_logPO.setCzsj(AOSUtils.getDateTimeStr());
        if("1".equals(inDto.getString("shzt"))){
            archive_remote_logPO.setCznr("审核完成");
        }else if("0".equals(inDto.getString("shzt"))){
            archive_remote_logPO.setCznr("审核完成");
        }
        archive_remote_logMapper.insert(archive_remote_logPO);
        String tm = "";
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                inDto.getUserInfo().getAccount_(),"审核完成" , tm, "利用", "", new Date()+"");
        logMapper.insert(logPO);

    }

    public int deleteUtilization(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            archive_remoteMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }
    public int updateMake_spzt(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            jdbcTemplate.execute("update archive_remote set state = NULL where id_='"+id_+"'");
            rows++;
        }
        return rows;
    }

    public Dto insertUtilizationDetail(Dto qDto){
        Dto outDto = Dtos.newDto();
        try {
            String[] selections = qDto.getRows();
            int del = 0;
            for (String id_ : selections) {
                Archive_remote_detailPO archive_remote_detailPO = new Archive_remote_detailPO();
                AOSUtils.copyProperties(qDto,archive_remote_detailPO);
                archive_remote_detailPO.setId_(AOSId.uuid());
                archive_remote_detailPO.setTid(id_);
                archive_remote_detailMapper.insert(archive_remote_detailPO);
                Archive_remotePO archive_remotePO = new Archive_remotePO();
                archive_remotePO.setId_(qDto.getString("formid"));
                archive_remotePO.setCxjg(qDto.getString("cxjg"));
                archive_remoteMapper.updateByKey(archive_remotePO);
                //此时集合archive_remote_jc中的记录数据
                String sql ="select * from archive_remote_jc where data_id='"+id_+"' and tablename='"+qDto.getString("tablename")+"'";
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
                if(list!=null&&list.size()>0){
                    Integer cishu = Integer.valueOf(list.get(0).get("cishu").toString());
                    String sqlupdate="update archive_remote_jc set cishu='"+(cishu+1)+"' where data_id='"+id_+"' and tablename='"+qDto.getString("tablename")+"'";
                    jdbcTemplate.execute(sqlupdate);
                }else{
                    String sqldata="select * from "+qDto.getString("tablename")+" where id_='"+id_+"'";
                    List<Map<String, Object>> listdata = jdbcTemplate.queryForList(sqldata);
                    if(listdata!=null&&listdata.size()>0){
                        Archive_remote_jcPO archive_remote_jcPO=new Archive_remote_jcPO();
                        archive_remote_jcPO.setId_(AOSId.uuid());
                        archive_remote_jcPO.setTablename(qDto.getString("tablename"));
                        archive_remote_jcPO.setData_id(id_);
                        archive_remote_jcPO.setDh(listdata.get(0).get("dh")+"");
                        archive_remote_jcPO.setQzdw(listdata.get(0).get("qzdw")+"");
                        archive_remote_jcPO.setDaml(qDto.getString("tablename_cn")+"");
                        archive_remote_jcPO.setTm(qDto.getString("tm")+"");
                        archive_remote_jcPO.setCwrq(qDto.getString("cwrq")+"");
                        archive_remote_jcPO.setBz(qDto.getString("bz")+"");
                        archive_remote_jcPO.setCishu(1);
                        archive_remote_jcMapper.insert(archive_remote_jcPO);
                    }else{
                        String sqladd="insert into archive_remote_jc ( id_,tablename,data_id,dh,qzdw,daml,tm,cwrq,bz,cishu)" +
                                " values('"+AOSId.uuid()+"','"+qDto.getString("tablename")+"','"+id_+"','','','','','','','1')";
                        jdbcTemplate.execute(sqladd);



                    }

                }
            }
            String msg = "操作完成";
            outDto.setAppMsg(msg);
            return outDto;
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return outDto;
    }
    public Dto insertUtilizationApply(Dto qDto){
        Dto outDto = Dtos.newDto();
        Archive_zz_applyPO archive_zz_applyPO = new Archive_zz_applyPO();
        archive_zz_applyPO.setId_(qDto.getString("formid"));
        archive_zz_applyPO.setCxjg(qDto.getString("cxjg"));
        archive_zz_applyMapper.updateByKey(archive_zz_applyPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成。");
        return outDto;
    }
    public Dto insertUtilizationMake(Dto qDto){
        Dto outDto = Dtos.newDto();
        Archive_zizhu_applyPO archive_zizhu_applyPO = new Archive_zizhu_applyPO();
        archive_zizhu_applyPO.setId_(qDto.getString("formid"));
        archive_zizhu_applyPO.setCxjg(qDto.getString("cxjg"));
        archive_zizhu_applyMapper.updateByKey(archive_zizhu_applyPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成。");
        return outDto;
    }
    public String selecttablename(Dto qDto){
        Dto inDto =  Dtos.newDto();
        inDto.put("formid",qDto.getString("formid"));
        List<Archive_remote_detailPO> list= archive_remote_detailMapper.list(inDto);
        return list.get(0).getTablename();
    }
    public String joinQuery(Dto qDto){
        List<Archive_remote_detailPO> list= archive_remote_detailMapper.list(qDto);
        String strtid =null;
        for(Archive_remote_detailPO archive_remote_detailPO :list){
            //strtid=archive_remote_detailPO.getTid();
            if(strtid!=null){
                strtid=strtid+",'"+archive_remote_detailPO.getTid()+"'";
            }else
                strtid="'"+archive_remote_detailPO.getTid()+"'";
        }

        return " and id_ in ("+strtid+")";
    }

    public String getTablename(Dto qDto){
        List<Archive_remote_detailPO> list= archive_remote_detailMapper.list(qDto);
        if(list.isEmpty()){
            return "";
        }
        return list.get(0).getTablename();
    }


    public Dto saveAdvance(Dto inDto){
        Dto outDto = Dtos.newDto();
        Archive_remote_advancePO archive_remote_advancePO = new Archive_remote_advancePO();
        AOSUtils.copyProperties(inDto,archive_remote_advancePO);
        archive_remote_advancePO.setId_(AOSId.uuid());
        archive_remote_advanceMapper.insert(archive_remote_advancePO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updateAdvance(Dto inDto){
        Dto outDto = Dtos.newDto();
        Archive_remote_advancePO archive_remote_advancePO = new Archive_remote_advancePO();
        AOSUtils.copyProperties(inDto,archive_remote_advancePO);
        archive_remote_advanceMapper.updateByKey(archive_remote_advancePO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deleteAdvance(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            archive_remote_advanceMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    public String findTablename(String id_) {
        String tablename="";
        String sql ="select tablename from archive_remote_detail where formid='"+id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
        tablename=(String)list.get(0).get("tablename");
        }
        return tablename;
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Map<String,Object>> getDataFieldListDisplayAll2(Dto qDto, HttpSession session) {
        String sql="select * from "+qDto.getString("tablename")+" where id_ in (select tid from archive_remote_detail where formid='"+qDto.getString("error_id_")+"' and tablename='"+qDto.getString("tablename")+"')";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Map<String,Object>> getDataFieldListDisplayAll3(Dto qDto, HttpSession session) {
        String sql="select * from archive_error_register";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }

    public boolean saveErrorRegister(Dto inDto) {
        try{
        Archive_error_registerPO archive_error_registerPO=new Archive_error_registerPO();
        AOSUtils.copyProperties(inDto,archive_error_registerPO);
        archive_error_registerPO.setFlag(0);
        archive_error_registerPO.setId_(AOSId.uuid());
        archive_error_registerMapper.insert(archive_error_registerPO);

        return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateErrorRegister(Dto inDto) {
        try{
            String id_= inDto.getString("id_");
            String flag= inDto.getString("flag");
            Archive_error_registerPO archive_error_registerPO=new Archive_error_registerPO();
            archive_error_registerPO.setId_(id_);
            archive_error_registerPO.setFlag(Integer.valueOf(flag));
            archive_error_registerMapper.updateByKey(archive_error_registerPO);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public void insertRemoteLog2(String tid,String username,String content,String person,String person_cn){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Archive_remote_logPO archive_remote_logPO = new Archive_remote_logPO();
        archive_remote_logPO.setId_(AOSId.uuid());
        archive_remote_logPO.setTid(tid);
        archive_remote_logPO.setCzsj(simpleDateFormat.format(date));
        archive_remote_logPO.setCzr(username);
        archive_remote_logPO.setCznr(content);
        archive_remote_logMapper.insert(archive_remote_logPO);
        String tm = "";
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                username, content, tm, "利用", "", new Date()+"");
        logMapper.insert(logPO);
    }
    public void insertRemoteLog(String tid,String username,String content){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Archive_remote_logPO archive_remote_logPO = new Archive_remote_logPO();
        archive_remote_logPO.setId_(AOSId.uuid());
        archive_remote_logPO.setTid(tid);
        archive_remote_logPO.setCzsj(simpleDateFormat.format(date));
        archive_remote_logPO.setCzr(username);
        archive_remote_logPO.setCznr(content);
        archive_remote_logMapper.insert(archive_remote_logPO);
        String tm = "";
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                username, content, tm, "利用", "", new Date()+"");
        logMapper.insert(logPO);
    }

    public List<Map<String, Object>> getFiles(Dto qDto) {
        // TODO Auto-generated method stub
        Dto out=Dtos.newDto();
        String id_=qDto.getString("id_");
        String tablename=qDto.getString("tablename");
        String sql="select * from "+tablename+"_path where tid='"+id_+"' order by _index";
        try {
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;

        }
    }























    /**
     * 预约查档转接待查档
     * @param qDto
     * @return
     */
    public boolean yyTojd(Dto qDto) {
        try {
            Dto outDto = Dtos.newDto();
            String[] selections = qDto.getRows();
            for (String id_ : selections) {
                //在接待库添加数据
                Archive_remote_advancePO archive_remote_advancePO=  archive_remote_advanceMapper.selectByKey(id_);
                if("已预约".equals(archive_remote_advancePO.getFlag())){
                    continue;
                }
                Archive_remotePO archive_remotePO=new Archive_remotePO();
                archive_remotePO.setYybh(archive_remote_advancePO.getYybh());
                archive_remotePO.setCddw(archive_remote_advancePO.getYydw());
                archive_remotePO.setXm(archive_remote_advancePO.getYyr());
                archive_remotePO.setSfzh(archive_remote_advancePO.getSfzh());
                archive_remotePO.setCyfs(archive_remote_advancePO.getYyfs());
                archive_remotePO.setLymd(archive_remote_advancePO.getCdmd());
                archive_remotePO.setCdnr(archive_remote_advancePO.getCdnr());
                archive_remotePO.setBz(archive_remote_advancePO.getBz());
                archive_remotePO.setJdr(qDto.getUserInfo().getName_());
                archive_remotePO.setId_(AOSId.uuid());
                archive_remotePO.setDjbh("CD"+getYear()+getMakeIndex("CD"+getYear()));
                archive_remoteMapper.insert(archive_remotePO);
                //修改状态
                archive_remote_advancePO.setFlag("已预约");
                archive_remote_advanceMapper.updateByKey(archive_remote_advancePO);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 当前年月日
     */
    public String getDate(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String createTime = dateFormat.format(now);
        return createTime;
    }
    /**
     * 当前年
     */
    public String getYear(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//设置日期格式
        String createTime = dateFormat.format(now);
        return createTime;
    }
    /**
     * 根据序列号得到当前的值
     */
    public String getSerializable(String name){
        String id = AOSId.id(name);
        return id;
    }

    /**
     * 图片水印
     * @param width
     * @param height
     * @param image
     * @param pdfContentByte
     * @throws Exception
     */
    public void addimagewatermark(float width, float height, Image image, PdfContentByte pdfContentByte)throws Exception {
        image.scaleToFit(width/5,height/5);
        image.setRotation(45);
        image.setAbsolutePosition(width*7/10, height*1/10); // set the first background image of the absolute
        pdfContentByte.addImage(image);
    }
    /**
     * 二维码水印
     * @param width
     * @param height
     * @param image
     * @param pdfContentByte
     * @throws Exception
     */
    public void addtwodimensional(float width, float height, Image image, PdfContentByte pdfContentByte)throws Exception {
        image.scaleToFit(width/10,height/10);
        image.setRotation(45);
        image.setAbsolutePosition(0, (int)height*9/10); // set the first background image of the absolute
        pdfContentByte.addImage(image);
    }

    /**
     * 二维码水印(2)
     */
    public void addtwodimensional2(PdfContentByte pdfContentByte,float width,float height,String dh) throws Exception {
        Hashtable hints= new Hashtable();
        String codetext="档号:"+dh+"\r\n"+"时间:"+AOSUtils.getDateTimeStr();
        /*BarcodeQRCode barcodeQRCode = new BarcodeQRCode(codetext, (int)width*1/5, (int)height*1/8, hints);
        Image codeQrImage = barcodeQRCode.getImage();

        //image.scaleToFit(width/5,height/5);
        codeQrImage.setRotation(45);
        codeQrImage.setAbsolutePosition(10, (int)height*9/10);
        pdfContentByte.addImage(codeQrImage);*/
    }
    /**
     * 二维码水印()
     */
    public void addtwodimensional(PdfContentByte pdfContentByte,float width,float height,String dh) throws Exception {
        Hashtable hints= new Hashtable();
        /*BarcodeQRCode barcodeQRCode = new BarcodeQRCode(codetext, (int)width*1/5, (int)height*1/8, hints);
        Image codeQrImage = barcodeQRCode.getImage();

        //image.scaleToFit(width/5,height/5);
        codeQrImage.setRotation(45);
        codeQrImage.setAbsolutePosition(10, (int)height*9/10);
        pdfContentByte.addImage(codeQrImage);*/
    }


    private String imageWatermark( String pathname, String erweimapathname, String s,String s2, String dh) {
        try{
            PdfReader reader = new PdfReader(pathname);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(erweimapathname));
            PdfGState gs1 = new PdfGState();
            //gs1.setFillOpacity(0.1f);
            Image image = Image.getInstance(s);
            Image image2 = Image.getInstance(s2);
            int n = reader.getNumberOfPages();
            PdfContentByte under;
            for (int i = 1; i <= n; i++) {
                PdfContentByte pdfContentByte = stamper.getOverContent(i);
                // 获得PDF最顶层
                under = stamper.getOverContent(i);
                pdfContentByte.setGState(gs1);
                //得到pdf的长和高
                float width=reader.getPageSize(i).getWidth();
                float height= reader.getPageSize(i).getHeight();
                PdfContentByte content = null;
                //此时判断一下当前的pdf是不是最后一页，并添加数字水印
                if(n==1){
                    //此时pdf只有一页，添加二维码并添加数字签章
                    //二维码水印
                    addtwodimensional(width,height,image2,pdfContentByte);
                    //document.close();
                    //图片水印（暂时注释掉，不要图片签章）
                    //addimagewatermark(width,height,image,pdfContentByte);
                    //文字水印
                    //addTextAlign(stamper,content,i, width, height, image);
                    break;
                }
                if(i==1){
                    //是在第一页添加二维码
                    addtwodimensional(width,height,image2,pdfContentByte);
                    //document.close();
                }
                if(i==n){
                    //图片水印（暂时注释掉，不要图片签章）
                    //addimagewatermark(width,height,image,pdfContentByte);
                    //文字水印
                    //addTextAlign(stamper,content,i, width, height, image);
                }
            }
            stamper.close();
            reader.close();
            //
            return erweimapathname;
        }catch (Exception e){
            e.printStackTrace();
            return erweimapathname;
        }
    }


    public void main2(String dh){
        try{
          String text2= "档号:"+dh+"\r\n"+"时间:"+AOSUtils.getDateTimeStr();

            //text2=new String(text2.getBytes("ISO-8859-1"),"GBK");

            text2=new String(text2.getBytes("UTF-8"),"ISO-8859-1");
            System.out.println(text2);
          int width = 100;
            int height = 100;
            String format = "png";
            Hashtable hints= new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text2, BarcodeFormat.QR_CODE, width, height,hints);
            bitMatrix = deleteWhite(bitMatrix);
            File outputFile = new File("F://dataaos/tempjpg/nnew.png");
            if(outputFile.exists()){
                outputFile.delete();
               outputFile.createNewFile();
            }
            MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
        }catch(Exception e){
            System.out.println("eeeeeeeeeeeee:"+e);
            e.printStackTrace();
        }
    }
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }

        return image;
    }
    /**
     * 添加水印，并返回水印文件全路径名称
     * @param inDto
     * @param path
     * @return
     */
    public String addWater(Dto inDto, String path, String filepath)throws Exception {
        //查询当前条目信息
        String tablename=inDto.getString("tablename");
        String id_=inDto.getString("id");
        String dh=inDto.getString("dh");

        //根据路径得到文件名称
        File tempFile =new File( path.trim());
        String pathname = tempFile.getName();
        //设计新文件名
        //String waterpathname=pathname.split("\\.")[0]+"-water"+"."+pathname.split("\\.")[1];
        String waterpathname="water"+"."+pathname.split("\\.")[1];
        //水印全路径名称
        String waterpath=filepath+"water"+"/"+waterpathname;
        //pdf路径拷贝到waterpdf路径文件
        copyfile(path,waterpath);
        //生成水印
        main2(dh);

        //******************************
        /*Ricardo*/
        String QRshow = inDto.getString("QRshow");
        String waterShow = inDto.getString("waterShow");
        //设置生成二维码的pdf路径
        String erweima_pdfpath="F://dataaos/temp/erweima.pdf";
        //
        //是否显示二维码
        if (Boolean.valueOf(QRshow)){
            path=imageWatermark(path,erweima_pdfpath,"F://dataaos/tempjpg/sign.gif","F://dataaos/tempjpg/nnew.png",dh);
        }
        //是否显示文字水印
        if (Boolean.valueOf(waterShow)) {
            path = addwater(path,waterpath);
        }
        return path;

    }
    /**
     * 添加水印，并返回水印文件全路径名称
     * @param inDto
     * @param path
     * @return
     */
    public String addWater2(Dto inDto, String path, String filepath)throws Exception {
        //查询当前条目信息
        String tablename=inDto.getString("tablename");
        String id_=inDto.getString("id");
        String dh=inDto.getString("dh");

        //根据路径得到文件名称
        File tempFile =new File( path.trim());
        String pathname = tempFile.getName();
        //设计新文件名
        //String waterpathname=pathname.split("\\.")[0]+"-water"+"."+pathname.split("\\.")[1];
        String waterpathname="water-yulan"+"."+pathname.split("\\.")[1];
        //水印全路径名称
        String waterpath=filepath+"/"+"water-yulan"+"/"+waterpathname;
        //pdf路径拷贝到waterpdf路径文件
        copyfile(path,waterpath);
        //生成水印
        //main2(dh);

        //******************************
        /*Ricardo*/
        String QRshow = inDto.getString("QRshow");
        String waterShow = inDto.getString("waterShow");


        String water_shuiyin = waterpath;
        water_shuiyin = addwater2(path);
        return water_shuiyin;

    }

    /**
     * 生成水印
     * @param waterpath
     * @return
     */
    public String addwater(String input,String output)throws Exception {
        // 从数据库获取
        Dto dto = Dtos.newDto();
        dto.setStringA("setWatermark");
        SetwatermarkPO setwatermarkPO = backupsService.selectOnegetSetwatermark(dto);
        String txtWord = setwatermarkPO.getTxtword(); //文字内容
        String wordSize = setwatermarkPO.getWordsize();//字号
        String wordFont = setwatermarkPO.getWordfont();//字体
        String wordRad = setwatermarkPO.getWordrad();//倾斜角度
        String wordOpacity = setwatermarkPO.getWordopacity();//透明度
        String density = setwatermarkPO.getDensity();//密度
        String waterMarkName =txtWord;
        //输出的文件路径
        //输出的文件
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(output)));
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, bos);

        // 获取总页数 +1, 下面从1开始遍历
        int total = reader.getNumberOfPages() + 1;
        BaseFont base = null;
        try {
            base = BaseFont.createFont("C:/WINDOWS/Fonts/"+wordFont, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            // 日志处理
            e.printStackTrace();
        }

        // 距左侧起始距离
        int interval = -15;
        // 获取水印文字的高度和宽度
        int textH = 0, textW = 0;
        JLabel label = new JLabel();
        label.setText(waterMarkName);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        textH = metrics.getHeight();
        textW = metrics.stringWidth(label.getText());
        System.out.println("textH: " + textH);
        System.out.println("textW: " + textW);

        // 设置水印透明度
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(Float.parseFloat(wordOpacity));
        gs.setStrokeOpacity(Float.parseFloat(wordOpacity));

        Rectangle pageSizeWithRotation = null;
        PdfContentByte content = null;
        for (int i = 1; i < total; i++) {
            // 在内容上方加水印
            content = stamper.getOverContent(i);
            // 在内容下方加水印
            // content = stamper.getUnderContent(i);
            content.saveState();
            content.setGState(gs);

            // 设置字体和字体大小
            content.beginText();
            content.setFontAndSize(base, Float.parseFloat(wordSize));

            // 获取每一页的高度、宽度
            pageSizeWithRotation = reader.getPageSizeWithRotation(i);
            float pageHeight = pageSizeWithRotation.getHeight();
            float pageWidth = pageSizeWithRotation.getWidth();

            // 根据纸张大小多次添加， 水印文字成30度角倾斜
            for (int height = interval + textH; height < pageHeight; height = height + textH *6+Integer.parseInt(density)+100) {
                for (int width = interval + textW; width < pageWidth + textW; width = width + textW * 6+Integer.parseInt(density)+100) {
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, width - textW, height - textH, Float.parseFloat(wordRad));
                }
            }
            content.endText();
        }

        // 关流
        stamper.close();
        reader.close();
        return output;


    }
    /**
     * 生成水印
     * @param waterpath
     * @return
     */
    public String addwater2(String input)throws Exception  {
        // 从数据库获取
        Dto dto = Dtos.newDto();
        dto.setStringA("setWatermark");
        SetwatermarkPO setwatermarkPO = backupsService.selectOnegetSetwatermark(dto);
        String txtWord = setwatermarkPO.getTxtword(); //文字内容
        String wordSize = setwatermarkPO.getWordsize();//字号
        String wordFont = setwatermarkPO.getWordfont();//字体
        String wordRad = setwatermarkPO.getWordrad();//倾斜角度
        String wordOpacity = setwatermarkPO.getWordopacity();//透明度
        String density = setwatermarkPO.getDensity();//密度
        String waterMarkName =txtWord;
        //输出的文件路径
        String output = input.substring(0, input.lastIndexOf("/"))+"/" + "water_yulan.pdf";
        //输出的文件
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(output)));
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, bos);

        // 获取总页数 +1, 下面从1开始遍历
        int total = reader.getNumberOfPages() + 1;
        BaseFont base = null;
        try {
            base = BaseFont.createFont("C:/WINDOWS/Fonts/"+wordFont, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            // 日志处理
            e.printStackTrace();
        }

        // 距左侧起始距离
        int interval = -15;
        // 获取水印文字的高度和宽度
        int textH = 0, textW = 0;
        JLabel label = new JLabel();
        label.setText(waterMarkName);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        textH = metrics.getHeight();
        textW = metrics.stringWidth(label.getText());
        System.out.println("textH: " + textH);
        System.out.println("textW: " + textW);

        // 设置水印透明度
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(Float.parseFloat(wordOpacity));
        gs.setStrokeOpacity(Float.parseFloat(wordOpacity));

        Rectangle pageSizeWithRotation = null;
        PdfContentByte content = null;
        for (int i = 1; i < total; i++) {
            // 在内容上方加水印
            content = stamper.getOverContent(i);
            // 在内容下方加水印
            // content = stamper.getUnderContent(i);
            content.saveState();
            content.setGState(gs);

            // 设置字体和字体大小
            content.beginText();
            content.setFontAndSize(base, Float.parseFloat(wordSize));

            // 获取每一页的高度、宽度
            pageSizeWithRotation = reader.getPageSizeWithRotation(i);
            float pageHeight = pageSizeWithRotation.getHeight();
            float pageWidth = pageSizeWithRotation.getWidth();

            // 根据纸张大小多次添加， 水印文字成30度角倾斜
            for (int height = interval + textH; height < pageHeight; height = height + textH *6+Integer.parseInt(density)+100) {
                for (int width = interval + textW; width < pageWidth + textW; width = width + textW * 6+Integer.parseInt(density)+100) {
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, width - textW, height - textH, Float.parseFloat(wordRad));
                }
            }

            content.endText();
        }

        // 关流
        stamper.close();
        reader.close();
        return output;
    }
    private void copyfile(String path, String waterpath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File file=new File(waterpath);
            if(!file.exists()){
                file.createNewFile();
            }else{
                file.delete();
            }
            fis = new FileInputStream(path);
            fos = new FileOutputStream(waterpath);
            byte[] arr = new byte[1024];
            int length;
            while((length = fis.read(arr)) != -1) {
                fos.write(arr,0,length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                fis.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     *
     * 电子文件路径
     *
     * @param qDto
     * @return
     */
    public String getDocumentPath(Dto qDto) {
        // dirname+_s_path 文件路径
        String tablename = qDto.getString("tablename") + "_path";
        String pid = qDto.getString("id");
        String tid = qDto.getString("tid");
        String sql = " SELECT dirname+_s_path as path From " + tablename
                + " WHERE id_='" + pid + "' and tid='"+tid+"'";

        List<Map<String, Object>> listDto = jdbcTemplate.queryForList(sql);
        String path = listDto.get(0).get("path").toString();
        // String path=" ";
        return path;
    }


    public String getImg(Dto qDto) {
        String imgpath=qDto.getString("imgpath");
        StringBuilder result = new StringBuilder();
        try {
            if(!new File(imgpath).exists()){
                return "";
            }
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(imgpath)), "GBK"));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
                result.append(lineTxt).append("\n");
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    public String getJsx(Dto qDto) {
        String id_=qDto.getString("id_");
        Archive_remotePO archive_remotePO=archive_remoteMapper.selectByKey(id_);
        StringBuilder result = new StringBuilder();
        try {
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File("F:\\dataaos\\jsx\\"+archive_remotePO.getJsx())), "GBK"));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
                result.append(lineTxt).append("\n");
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    /**
     * 修改用户等级介绍信
     * @param id_
     * @param s
     */
    public Dto updateDjMessage(String id_, String pdfpath) {
        Dto outDto=Dtos.newDto();
       try{
           Archive_remotePO archive_remotePO=new Archive_remotePO();
           archive_remotePO.setId_(id_);
           archive_remotePO.setJsx(pdfpath);
           archive_remoteMapper.updateByKey(archive_remotePO);
           outDto.setAppCode(AOSCons.SUCCESS);
       }catch(Exception e){
           e.printStackTrace();
           outDto.setAppCode(AOSCons.ERROR);
       }
        return  outDto;
    }

    public boolean delArchive_make(Dto qDto) {
        try{
            String sql="delete  from archive_remote_detail where tid='"+qDto.getString("archive_id")+"' and formid='"+qDto.getString("rw_id")+"'" +
                    " and tablename='"+qDto.getString("tablename")+"'";
            jdbcTemplate.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getMakeIndex(String nd) {
        String sql="select  djbh  from archive_remote where djbh like '%"+nd+"%'  order by djbh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("djbh");
            String index = dh.substring(dh.length()-4, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%4d", h).replace(" ", "0");
        }else {
            return "0001";
        }
    }

    public String getAdvanceIndex(String nd) {
        String sql="select  yybh  from archive_remote_advance where yybh like '%"+nd+"%'  order by yybh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("yybh");
            String index = dh.substring(dh.length()-4, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%4d", h).replace(" ", "0");
        }else {
            return "0001";
        }
    }
    public Dto addSFXX(Dto qDto){
        Dto outDto = Dtos.newDto();
        Archive_zzremotePO archive_zzremotePO=new Archive_zzremotePO();
        AOSUtils.copyProperties(qDto,archive_zzremotePO);
        String id=AOSId.uuid();
        archive_zzremotePO.setId_(id);
        archive_zzremoteMapper.insert(archive_zzremotePO);
        outDto.put("id",id);
        return outDto;
    }
    public Dto addzz_Dtail(Dto qDto){
        Dto outDto = Dtos.newDto();
        Archive_zzremote_detailPO archive_zzremote_detailPO = new Archive_zzremote_detailPO();
        AOSUtils.copyProperties(qDto,archive_zzremote_detailPO);
        String id=AOSId.uuid();
        archive_zzremote_detailPO.setId_(id);
        archive_zzremote_detailMapper.insert(archive_zzremote_detailPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("已提交，请等待。。。");
        return outDto;
    }

    public void updateZzremote(Dto qDto){
        Archive_zzremotePO archive_zzremotePO=new Archive_zzremotePO();
        archive_zzremotePO.setId_(qDto.getString("formid"));
        archive_zzremotePO.setState("2");
        archive_zzremoteMapper.updateByKey(archive_zzremotePO);
    }

    public String joinQueryShq(Dto qDto) {
        List<Archive_zzremote_detailPO> list = archive_zzremote_detailMapper.list(qDto);
        String strtid = null;
        for (Archive_zzremote_detailPO archive_zzremote_detailPO : list) {
            //strtid=archive_remote_detailPO.getTid();
            if (strtid != null) {
                strtid = strtid + ",'" + archive_zzremote_detailPO.getTid() + "'";
            } else
                strtid = "'" + archive_zzremote_detailPO.getTid() + "'";
        }
        return " and id_ in ("+strtid+")";
    }

    public List<Map<String, Object>> getDocumentPathShq(Dto qDto) {
        // dirname+_s_path 文件路径
        String tablename = qDto.getString("tablename") + "_path";
        String tid = qDto.getString("tid");
        String sql = " SELECT id_,_path,tid, dirname+_s_path as relpath From " + tablename
                + " WHERE  tid='"+tid+"'";

        List<Map<String, Object>> listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    public String getpath(String tid,String id_,String tablename) {
        String sql = "select * from " + tablename + "_path where id_ ='" + id_ + "'";
        String filepath = "";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String path = (String) map.get("_s_path");
                if (path.split("\\.")[1].equals("pdf")) {
                    String filename = (String) map.get("dirname");
                    // 组合路径
                    filepath = topicAddress  + File.separator+filename
                            + path;
                    // 获取当前ip地址
                    // 路径批量替换
                    // wsda/2e9275c32e6849afab038892747249c2/数字档案馆系统测试办法（档办发[2014]6号).pdf
                    filepath = filepath.replace("\\", "/");
                    break;
                }
            }
        }
        return filepath;
    }

    public Dto shenqing_print(Dto inDto) {
        Dto out=Dtos.newDto();
        try {
            notificationService.wanZizhu_printLy(inDto);
            //保存日志
            String id_=inDto.getString("tid");
            Archive_remote_logPO archive_remote_logPO=new Archive_remote_logPO();
            archive_remote_logPO.setId_(AOSId.uuid());
            archive_remote_logPO.setTid(id_);
            archive_remote_logPO.setCzr(inDto.getUserInfo().getName_());
            archive_remote_logPO.setCzsj(AOSUtils.getDateTimeStr());
            archive_remote_logPO.setCznr("申请打印");
            archive_remote_logMapper.insert(archive_remote_logPO);
            out.setAppCode(AOSCons.SUCCESS);
            String tm = "";
            LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                    inDto.getUserInfo().getAccount_(),"申请打印" , tm, "利用", "", new Date()+"");
            logMapper.insert(logPO);
        } catch (Exception e) {
            e.printStackTrace();
            out.setAppCode(AOSCons.ERROR);
        }
        return out;
    }
    public Dto updateZiZhuUtilization(Dto inDto){
        Dto outDto = Dtos.newDto();
        Archive_zizhu_applyPO archive_zizhu_applyPO = new Archive_zizhu_applyPO();
        AOSUtils.copyProperties(inDto,archive_zizhu_applyPO);
        archive_zizhu_applyMapper.updateByKey(archive_zizhu_applyPO);
        String nftg=inDto.getString("nftg");
        if("可以提供".equals(nftg)){
            outDto.put("message","[可以提供]");
        }else if("不可提供".equals(nftg)){
            outDto.put("message","[不可提供],"+outDto.getString("wtgyy"));
        }
        //同时给管理员用户发送审核标记信息
        notificationService.WanZizhuLy(inDto);
        String id_=archive_zizhu_applyPO.getId_();
        Archive_remote_logPO archive_remote_logPO=new Archive_remote_logPO();
        archive_remote_logPO.setId_(AOSId.uuid());
        archive_remote_logPO.setTid(id_);
        archive_remote_logPO.setCzr(inDto.getUserInfo().getName_());
        archive_remote_logPO.setCzsj(AOSUtils.getDateTimeStr());
        archive_remote_logPO.setCznr("审核");
        archive_remote_logMapper.insert(archive_remote_logPO);

        String tm = "";
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                inDto.getUserInfo().getAccount_(), "审核", tm, "利用", "", new Date()+"");
        logMapper.insert(logPO);

        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    /**
     * 申请看电子文件
     * @param inDto
     * @return
     */
    public Dto shenqing_file(Dto inDto) {
        Dto outDto = Dtos.newDto();
        Archive_zizhuremote_detailPO archive_zizhuremote_detailPO=new Archive_zizhuremote_detailPO();
        archive_zizhuremote_detailPO.setFormid(inDto.getString("formid"));
        archive_zizhuremote_detailPO.setTablename(inDto.getString("tablename"));
        archive_zizhuremote_detailPO.setTid(inDto.getString("tid"));
        archive_zizhuremote_detailPO.setId_(AOSId.uuid());
        archive_zizhuremote_detailMapper.insert(archive_zizhuremote_detailPO);
        //保存日志
        Archive_remote_logPO archive_remote_logPO=new Archive_remote_logPO();
        archive_remote_logPO.setId_(AOSId.uuid());
        archive_remote_logPO.setTid(inDto.getString("tid"));
        archive_remote_logPO.setCzr(inDto.getUserInfo().getName_());
        archive_remote_logPO.setCzsj(AOSUtils.getDateTimeStr());
        archive_remote_logPO.setCznr("申请查看");
        archive_remote_logMapper.insert(archive_remote_logPO);
        String tm = "";
        LogPO logPO = LogUtils.InsertLog(AOSId.uuid(),
                inDto.getUserInfo().getAccount_(), "申请查看", tm, "利用", "", new Date()+"");
        logMapper.insert(logPO);
        //申请看电子文件
        return outDto;
    }

    public String zizhucalcId(Dto dto) {
        String bh="";

        String sql="select zzbh from archive_zizhu_apply order by zzbh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            String zzbh = (String)list.get(0).get("zzbh");
            if (zzbh != null && zzbh.length() != 0) {
                String index = zzbh.substring(zzbh.length()-4, zzbh.length());
                int lx = Integer.valueOf(index) + 1;
                bh=String.format("%4d", lx).replace(" ", "0");
            }else {
                bh= "0001";
            }
        }else{
            bh= "0001";
        }
        return bh;
    }
    public boolean addZiZhuMessage(Dto inDto) {
        try {
            Dto outDto = Dtos.newDto();
            Archive_zizhu_applyPO archive_zizhu_applyPO=new Archive_zizhu_applyPO();
            AOSUtils.copyProperties(inDto,archive_zizhu_applyPO);
            archive_zizhu_applyPO.setId_(AOSId.uuid());
            archive_zizhu_applyMapper.insert(archive_zizhu_applyPO);
            //添加日志
            String id_=archive_zizhu_applyPO.getId_();
            Archive_remote_logPO archive_remote_logPO=new Archive_remote_logPO();
            archive_remote_logPO.setId_(AOSId.uuid());
            archive_remote_logPO.setTid(id_);
            archive_remote_logPO.setCzr(inDto.getUserInfo().getName_());
            archive_remote_logPO.setCzsj(AOSUtils.getDateTimeStr());
            archive_remote_logPO.setCznr("用户登录");
            archive_remote_logMapper.insert(archive_remote_logPO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int deleteZiZhuTask(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            archive_zz_applyMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    public int deleteZiZhuMake(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            archive_zizhu_applyMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    public List<Map<String,Object>> listDepotLy(Dto qDto) {
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_remote  where ckzt = '0' or ckzt='1' or ckzt='2'  order by djbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }

    public int listDepotLy_count(Dto qDto) {
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_remote  where ckzt = '0' or ckzt='1' or ckzt='2'";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        return listDto.size();
    }
    public void saveDepotout(Dto inDto) {
        UserInfoVO userInfo = inDto.getUserInfo();
        String username = userInfo.getName_();
        String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Depot_ckPO depot_ckPO=new Depot_ckPO();
        depot_ckPO.setId_(AOSId.uuid());
        depot_ckPO.setCkdh(inDto.getString("ckdh"));
        depot_ckPO.setCkr(username);
        depot_ckPO.setCkyy(inDto.getString("ckyy"));
        depot_ckPO.setLx(inDto.getString("lx"));
        depot_ckPO.setCksj(date);
        depot_ckPO.setCksl(inDto.getString("cksl"));
        depot_ckPO.setCkzt("已出库");
        depot_ckPO.setBz(inDto.getString("ckbz"));
        depot_ckMapper.insert(depot_ckPO);
    }
    public int deleteDepotLy(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            archive_remoteMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    public Archive_remotePO updateArchive_Remote_rk(String id_) {
        Archive_remotePO archive_remotePO=new Archive_remotePO();
        archive_remotePO.setId_(id_);
        archive_remotePO.setCkzt("2");
        archive_remoteMapper.updateByKey(archive_remotePO);
        Archive_remotePO archive_remotePO1 = archive_remoteMapper.selectByKey(id_);
        return archive_remotePO1;
    }

    public void addChumo(Dto inDto) {
        Archive_zz_applyPO archive_zz_apply=new Archive_zz_applyPO();
        AOSUtils.copyProperties(inDto,archive_zz_apply);
        archive_zz_apply.setId_(AOSId.uuid());
        archive_zz_apply.setState(1);
        archive_zz_applyMapper.insert(archive_zz_apply);
        notificationService.TaskChuMoLy(inDto);
    }
}
