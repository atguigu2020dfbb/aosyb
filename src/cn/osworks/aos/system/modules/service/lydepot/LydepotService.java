package cn.osworks.aos.system.modules.service.lydepot;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.exception.AOSException;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.service.archive.DataService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.zxing.client.j2se.MatrixToImageConfig.BLACK;
import static com.google.zxing.client.j2se.MatrixToImageConfig.WHITE;

@Service
public class LydepotService extends JdbcDaoSupport {

    @Autowired
    private Archive_remoteMapper archive_remoteMapper;

    @Autowired
    private Archive_remote_detailMapper archive_remote_detailMapper;
    @Autowired
    private Archive_zz_applyMapper archive_zz_applyMapper;
    @Autowired
    private DataService dataService;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Lysjfb_detailMapper lysjfb_detailMapper;
    @Autowired
    private Archive_remote_advanceMapper archive_remote_advanceMapper;
    @Autowired
    private Archive_error_registerMapper archive_error_registerMapper;
    @Autowired
    private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
    @Autowired
    private Lysjfb_detail_tablenameidMapper lysjfb_detail_tablenameidMapper;

    @Autowired
    private Archive_remote_logMapper archive_remote_logMapper;

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

    public Dto insertUtilizationDetail(Dto qDto){
        Dto outDto = Dtos.newDto();
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
        }
        String msg = "操作完成";
        outDto.setAppMsg(msg);
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
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions2(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            query.append(" and hlw='1'");
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions2_hlw(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            query.append(" and hlw='1'");
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions2_jyw(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            query.append(" and jyw='1'");
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions2_zww(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            query.append(" and zww='1'");
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions3(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            query.append(" and  ( hlw<>'1' or hlw='' or hlw is NULL )");
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions3_hlw(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            query.append(" and  hlw='1' ");
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions3_sjfb(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {
                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filednames" + i));
                    } else {
                        query.append("or " + qDto.getString("filednames" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("not like")) {
                        query.append(" not like '%" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions3_list_sjfb(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {
                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions3_jyw(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            query.append(" and  jyw='1' ");
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions_tablenameid_hlw(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions_tablenameid_zww(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions3_zww(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            if(qDto.getString("queryclass")!=null&&qDto.getString("queryclass")!=""){
                query.append(" and  ").append(qDto.getString("queryclass"));
            }
            query.append(" and  zww='1' ");
            return query.toString();
        } else
            return "";

    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Dto> getDataFieldListDisplayAll3(Dto qDto, HttpSession session) {
        String sql = "";
        String query = queryConditions2(qDto);
        query=query+" and hlw='1'";
        String fieldName;
        String enfield = "id_";
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit) + 1;
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAllAll(qDto.getString("tablename"));
        String orderenfield = "";
        for (int i = 0; i < list.size(); i++) {
            fieldName = list.get(i).getFieldenname();
            enfield = enfield + "," + fieldName;
            if (i == 3) {
                orderenfield = enfield;
            }
        }
        orderenfield = orderenfield.substring(4);
        if (qDto.getString("page") != null && qDto.getString("page") != "") {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                    + pageStart
                    + " AND "
                    + limit
                    * page
                    * Integer.valueOf(qDto.getString("page"))
                    + " ORDER BY aos_rn_";
        } else {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
        }
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Dto> getDataFieldListDisplayAll3_hlw(Dto qDto, HttpSession session) {
        String sql = "";
        String query = queryConditions2_hlw(qDto);
        query=query+" and hlw='1'";
        String fieldName;
        String enfield = "id_";
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit) + 1;
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAllAll(qDto.getString("tablename"));
        String orderenfield = "";
        for (int i = 0; i < list.size(); i++) {
            fieldName = list.get(i).getFieldenname();
            enfield = enfield + "," + fieldName;
            if (i == 3) {
                orderenfield = enfield;
            }
        }
        orderenfield = orderenfield.substring(4);
        if (qDto.getString("page") != null && qDto.getString("page") != "") {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                    + pageStart
                    + " AND "
                    + limit
                    * page
                    * Integer.valueOf(qDto.getString("page"))
                    + " ORDER BY aos_rn_";
        } else {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
        }
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Dto> getDataFieldListDisplayAll4(Dto qDto, HttpSession session) {
        String sql = "";
        String query = queryConditions3(qDto);
        query=query+"  and ( hlw<>'1' or hlw ='' or hlw is NULL)";
        String fieldName;
        String enfield = "id_";
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit) + 1;
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAllAll(qDto.getString("tablename"));
        String orderenfield = "";
        for (int i = 0; i < list.size(); i++) {
            fieldName = list.get(i).getFieldenname();
            enfield = enfield + "," + fieldName;
            if (i == 3) {
                orderenfield = enfield;
            }
        }
        orderenfield = orderenfield.substring(4);
        if (qDto.getString("page") != null && qDto.getString("page") != "") {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                    + pageStart
                    + " AND "
                    + limit
                    * page
                    * Integer.valueOf(qDto.getString("page"))
                    + " ORDER BY aos_rn_";
        } else {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
        }
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Dto> getDataFieldListDisplayAll3_jyw(Dto qDto, HttpSession session) {
        String sql = "";
        String query = queryConditions2_jyw(qDto);
        query=query+" and jyw='1'";
        String fieldName;
        String enfield = "id_";
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit) + 1;
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAllAll(qDto.getString("tablename"));
        String orderenfield = "";
        for (int i = 0; i < list.size(); i++) {
            fieldName = list.get(i).getFieldenname();
            enfield = enfield + "," + fieldName;
            if (i == 3) {
                orderenfield = enfield;
            }
        }
        orderenfield = orderenfield.substring(4);
        if (qDto.getString("page") != null && qDto.getString("page") != "") {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                    + pageStart
                    + " AND "
                    + limit
                    * page
                    * Integer.valueOf(qDto.getString("page"))
                    + " ORDER BY aos_rn_";
        } else {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
        }
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Dto> getDataFieldListDisplayAll3_zww(Dto qDto, HttpSession session) {
        String sql = "";
        String query = queryConditions2_zww(qDto);
        query=query+" and zww='1'";
        String fieldName;
        String enfield = "id_";
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit) + 1;
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAllAll(qDto.getString("tablename"));
        String orderenfield = "";
        for (int i = 0; i < list.size(); i++) {
            fieldName = list.get(i).getFieldenname();
            enfield = enfield + "," + fieldName;
            if (i == 3) {
                orderenfield = enfield;
            }
        }
        orderenfield = orderenfield.substring(4);
        if (qDto.getString("page") != null && qDto.getString("page") != "") {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                    + pageStart
                    + " AND "
                    + limit
                    * page
                    * Integer.valueOf(qDto.getString("page"))
                    + " ORDER BY aos_rn_";
        } else {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + ") SELECT * FROM aos_query_  ORDER BY aos_rn_";
        }
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
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
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageTotal2(Dto qDto) {
        String query = queryConditions2(qDto);
        query=query+" and hlw='1'";
        String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
                + " WHERE 1=1 " + query ;
        return jdbcTemplate.queryForInt(sql);
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageTotal2_hlw(Dto qDto) {
        String query = queryConditions2(qDto);
        query=query+" and hlw='1'";
        String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
                + " WHERE 1=1 " + query ;
        return jdbcTemplate.queryForInt(sql);
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageTotal2_sjfb(Dto qDto) {
        String query = queryConditions2(qDto);
        query=query+" and hlw='1'";
        String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
                + " WHERE 1=1 " + query ;
        return jdbcTemplate.queryForInt(sql);
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageTotal2_jyw(Dto qDto) {
        String query = queryConditions2(qDto);
        query=query+" and jyw='1'";
        String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
                + " WHERE 1=1 " + query ;
        return jdbcTemplate.queryForInt(sql);
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageTotal2_zww(Dto qDto) {
        String query = queryConditions2(qDto);
        query=query+" and zww='1'";
        String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
                + " WHERE 1=1 " + query ;
        return jdbcTemplate.queryForInt(sql);
    }
    public List<Archive_tablefieldlistPO> getComboboxList(Dto inDto) {
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAll(inDto.getString("tablename"));
        return list;
    }
    public List<Map<String,Object>>   getQueryIds(Dto qDto) {
        // TODO Auto-generated method stub
        String ids="";
        String tablename=qDto.getString("tablename");
        String query=qDto.getString("query");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql="select id_,dh from "+tablename+" where 1=1 "+query;
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);

        return listDto;
    }
    public boolean updatehlw_data(Dto inDto) {
        try{
            String tablename=inDto.getString("tablename");
            String ids=inDto.getString("ids");
            String dhs=inDto.getString("dhs");
            String fbid=inDto.getString("fbid");
            String fbwd=inDto.getString("fbwd");
            if(ids!=null&&ids!=""){
                String[] id=ids.split(",");
                String[] dh=dhs.split(",");
                for(int i=0;i<id.length;i++){
                    Lysjfb_detail_tablenameidPO lysjfb_detail_tablenameidPO=new Lysjfb_detail_tablenameidPO();
                    lysjfb_detail_tablenameidPO.setId_(AOSId.uuid());
                    lysjfb_detail_tablenameidPO.setPid(fbid);
                    lysjfb_detail_tablenameidPO.setTablename_id(id[i]);
                    lysjfb_detail_tablenameidPO.setTablename(tablename);
                    lysjfb_detail_tablenameidPO.setDh(dh[i]);
                    lysjfb_detail_tablenameidPO.setLx(fbwd);
                    lysjfb_detail_tablenameidMapper.insert(lysjfb_detail_tablenameidPO);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updatesjfb_data(Dto inDto) {
        try{
            String tablename=inDto.getString("tablename");
            String ids=inDto.getString("ids");
            String dhs=inDto.getString("dhs");
            String fbid=inDto.getString("fbid");
            String fbwd=inDto.getString("fbwd");
            if(ids!=null&&ids!=""){
                String[] id=ids.split(",");
                String[] dh=dhs.split(",");
                for(int i=0;i<id.length;i++){
                    Lysjfb_detail_tablenameidPO lysjfb_detail_tablenameidPO=new Lysjfb_detail_tablenameidPO();
                    lysjfb_detail_tablenameidPO.setId_(AOSId.uuid());
                    lysjfb_detail_tablenameidPO.setPid(fbid);
                    lysjfb_detail_tablenameidPO.setTablename_id(id[i]);
                    lysjfb_detail_tablenameidPO.setTablename(tablename);
                    lysjfb_detail_tablenameidPO.setDh(dh[i]);
                    lysjfb_detail_tablenameidPO.setLx(fbwd);
                    lysjfb_detail_tablenameidMapper.insert(lysjfb_detail_tablenameidPO);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updatejyw_data(Dto inDto) {
        try{
            String tablename=inDto.getString("tablename");
            String ids=inDto.getString("ids");
            if(ids!=null&&ids!=""){
                String[] id=ids.split(",");
                for(int i=0;i<id.length;i++){
                    String sql="update "+tablename+" set jyw='1' where id_ = '"+id[i]+"'";
                    jdbcTemplate.execute(sql);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updatezww_data(Dto inDto) {
        try{
            String tablename=inDto.getString("tablename");
            String ids=inDto.getString("ids");
            if(ids!=null&&ids!=""){
                String[] id=ids.split(",");
                for(int i=0;i<id.length;i++){
                    String sql="update "+tablename+" set zww='1' where id_ = '"+id[i]+"'";
                    jdbcTemplate.execute(sql);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageTotal3(Dto qDto) {
        String query = queryConditions3(qDto);
        query=query+"  and ( hlw<>'1' or hlw ='' or hlw is NULL)";
        String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
                + " WHERE 1=1 " + query ;
        return jdbcTemplate.queryForInt(sql);
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
                archive_remotePO.setXm(archive_remote_advancePO.getYyr());
                archive_remotePO.setSfzh(archive_remote_advancePO.getSfzh());
                archive_remotePO.setLymd(archive_remote_advancePO.getCdmd());
                archive_remotePO.setCdnr(archive_remote_advancePO.getCdnr());
                archive_remotePO.setBz(archive_remote_advancePO.getBz());
                archive_remotePO.setId_(AOSId.uuid());
                archive_remotePO.setDjbh("JD"+getDate()+getSerializable("接待流水号"));
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


    private boolean imageWatermark( String pathname, String waterpathname, String s,String s2, String dh) {
        try{
            PdfReader reader = new PdfReader(pathname);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(waterpathname));
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
                    //图片水印
                    addimagewatermark(width,height,image,pdfContentByte);
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
                    //图片水印
                    addimagewatermark(width,height,image,pdfContentByte);
                    //文字水印
                    //addTextAlign(stamper,content,i, width, height, image);
                }
            }
            stamper.close();
            reader.close();
            //
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public void main2(String dh){
        try{
          String text2= "档号:"+dh+"\r\n"+"时间:"+AOSUtils.getDateTimeStr();

            //text2=new String(text2.getBytes("ISO-8859-1"),"GBK");
            text2=new String(text2.getBytes("UTF-8"),"ISO-8859-1");
          int width = 100;
            int height = 100;
            String format = "png";
            Hashtable hints= new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text2, BarcodeFormat.QR_CODE, width, height,hints);
            bitMatrix = deleteWhite(bitMatrix);
            File outputFile = new File("F:\\dataaos\\tempjpg\\nnew.png");
            if(outputFile.exists()){
                outputFile.delete();
               outputFile.createNewFile();
            }
            MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
        }catch(Exception e){
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
    public String addWater(Dto inDto, String path, String filepath) {
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
        String waterpath=filepath+"/"+"water"+"/"+waterpathname;
        //pdf路径拷贝到waterpdf路径文件
        copyfile(path,waterpath);
        //生成水印
        main2(dh);
        imageWatermark(path,waterpath,"F:\\dataaos\\tempjpg\\sign.gif","F:\\dataaos\\tempjpg\\nnew.png",dh);
        String water_shuiyin = addwater(waterpath);
        return water_shuiyin;

    }
   public String addwater(String waterpath) {
       String srcFile = null;
       try {
           String waterlujing = waterpath.substring(0, waterpath.lastIndexOf("/"));
           srcFile = waterlujing + "/" + "water_shuiyin.pdf";
           // 加完水印后的文件
           File file = new File(srcFile);
           if (!file.exists())
               file.createNewFile();
           String text = "延边朝鲜族自治州档案馆";
           int textWidth = 100;
           int textHeight = 100;
           PdfReader reader = new PdfReader(waterpath);
           // 加完水印的文件
           PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                   srcFile));
           int total = reader.getNumberOfPages() + 1;
           PdfContentByte content;
           // 设置字体
           BaseFont font = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);
           // 循环对每页插入水印
           for (int i = 1; i < total; i++) {
               // 水印的起始
               content = stamper.getOverContent(i);
               // 开始
               content.beginText();
               // 设置颜色 默认为蓝色
               content.setColorFill(BaseColor.GRAY);
               // 设置字体及字号
               content.setFontAndSize(font, 38);
               PdfGState gs = new PdfGState();
               gs.setFillOpacity(0.8f);// 设置透明度为0.8
               content.setGState(gs);
               // 开始写入水印
               content.showTextAligned(Element.ALIGN_LEFT, text, 100, 400, 30);
               content.endText();
           }
           stamper.close();
           reader.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return srcFile;

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

    public List<Archive_tablenamePO> findTablename() {
        List<Archive_tablenamePO> alist=new ArrayList<Archive_tablenamePO>();
        String sql="select * from archive_TableName where tablename ='jnws' or tablename='gdws'";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Map<String, Object> map = list.get(i);
                String tablename=(String)map.get("TableName");
                String tabledesc=(String)map.get("TableDesc");
                Archive_tablenamePO archive_tablenamePO=new Archive_tablenamePO();
                archive_tablenamePO.setTablename(tablename);
                archive_tablenamePO.setTabledesc(tabledesc);
                alist.add(archive_tablenamePO);
            }
        }
        return alist;
    }
    public Dto deleteAllData_sjfb(Dto qDto) {
        // TODO Auto-generated method stub
        Dto outDto = Dtos.newDto();
        String tablename = qDto.getString("tablename");
        String fbwd = qDto.getString("fbwd");
        String fbid = qDto.getString("fbid");
        String query=qDto.getString("query");
        // 查询条目数
        List<Map<String, Object>> list = jdbcTemplate
                .queryForList("select * from "+tablename+" t  left join lysjfb_detail_tablenameid l on t.id_=l.tablename_id where 1=1  "+query+
                        " and l.tablename='"+tablename+"' and l.lx='"+fbwd+"'");
        try {
            //
            String sql3="delete from lysjfb_detail_tablenameid where tablename_id in (select id_ from " + tablename +" where 1=1 "+query +" ) " +
                    "and tablename='"+tablename+"' and lx='"+fbwd+"' and pid='"+fbid+"'";
            jdbcTemplate.execute(sql3);
            String msg = "操作完成,";
            msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", list.size());
            outDto.setAppMsg(msg);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            outDto.setAppMsg("删除失败!");
        }
        return outDto;
    }
    public Dto deleteAllData_jyw(Dto qDto) {
        // TODO Auto-generated method stub
        Dto outDto = Dtos.newDto();
        String tablename = qDto.getString("tablename");
        // 查询条目数
        List<Map<String, Object>> list = jdbcTemplate
                .queryForList("select id_ from " + tablename + " where   1=1 and jyw='1' "
                        + qDto.getString("query"));
        try {
            //
            jdbcTemplate.execute(" update " + tablename + " set jyw='0' where 1=1  "
                    + qDto.getString("query"));
            String msg = "操作完成,";
            msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", list.size());
            outDto.setAppMsg(msg);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            outDto.setAppMsg("删除失败!");
        }
        return outDto;
    }
    public Dto deleteAllData_hlw(Dto qDto) {
        // TODO Auto-generated method stub
        Dto outDto = Dtos.newDto();
        String tablename = qDto.getString("tablename");
        // 查询条目数
        List<Map<String, Object>> list = jdbcTemplate
                .queryForList("select id_ from " + tablename + " where   1=1 and hlw='1' "
                        + qDto.getString("query"));
        try {
            //

            jdbcTemplate.execute(" update " + tablename + " set hlw='0' where 1=1  "
                    + qDto.getString("query"));
            String msg = "操作完成,";
            msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", list.size());
            outDto.setAppMsg(msg);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            outDto.setAppMsg("删除失败!");
        }
        return outDto;
    }
    public Dto deleteAllData_zww(Dto qDto) {
        // TODO Auto-generated method stub
        Dto outDto = Dtos.newDto();
        String tablename = qDto.getString("tablename");
        // 查询条目数
        List<Map<String, Object>> list = jdbcTemplate
                .queryForList("select * from " + tablename + " where   1=1 and zww='1' "
                        + qDto.getString("query"));
        try {
            //

            jdbcTemplate.execute(" update " + tablename + " set zww='0' where 1=1  "
                    + qDto.getString("query"));
            String msg = "操作完成,";
            msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", list.size());
            outDto.setAppMsg(msg);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            outDto.setAppMsg("删除失败!");
        }
        return outDto;
    }

    public boolean saveDetail(Dto out) {
        try {
            Lysjfb_detailPO lysjfb_detailPO=new Lysjfb_detailPO();
            AOSUtils.copyProperties(out,lysjfb_detailPO);
            lysjfb_detailPO.setId_(AOSId.uuid());
            lysjfb_detailMapper.insert(lysjfb_detailPO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateDetail(Dto out) {
        try {
            Lysjfb_detailPO lysjfb_detailPO=new Lysjfb_detailPO();
            AOSUtils.copyProperties(out,lysjfb_detailPO);
            lysjfb_detailMapper.updateByKey(lysjfb_detailPO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public Dto deleteDetails(Dto qDto) {
        Dto outDto = Dtos.newDto();
        String[] selections = qDto.getRows();
        int del = 0;
        for (String id_ : selections) {
            jdbcTemplate.execute(" delete from lysjfb_detail where id_='"
                    + id_ + "'");
            del++;
        }
        String msg = "操作完成，";
        if (del > 0) {
            msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
        }
        outDto.setAppMsg(msg);
        return outDto;
    }

    public Object getDetail(Dto out) {
        Lysjfb_detailPO lysjfb_detailPO = lysjfb_detailMapper.selectByKey(out.getString("id_"));
        String sql="select * from lysjfb_detail where id_='"+out.getString("id_")+"'";
        List<Dto> list = convertMapListToArrayList(jdbcTemplate
                .queryForList(sql));
        if(list.size()>0&&list!=null){
            return list.get(0);
        }
        return null;
    }
    /**getDataFieldDisplayAll
     * 功能：转换MapList为数组List
     *
     * @param list
     * @return
     */
    public static List convertMapListToArrayList(List list) {
        Map map = null;
        /*
         * Dto qDto = Dtos.newDto(); if (list != null) { Iterator iterator =
         * list.iterator(); while (iterator.hasNext()) { map = (Map)
         * iterator.next(); Iterator<?> keyIt = map.keySet().iterator(); while
         * (keyIt.hasNext()) { String key = (String) keyIt.next(); String value
         * = ((Object) map.get(key)) == null ? "" : ((Object)
         * map.get(key)).toString(); qDto.put(key, value); } } }
         */

        return list;
    }

    public boolean updateDetails_zxfb(Dto inDto) {
        try {
            String zww=inDto.getString("zww");
            String hlw=inDto.getString("hlw");
            if("1".equals(zww)){
                String sql="update lysjfb_detail_tablenameid set zww='1' where pid='"+inDto.getString("id_")+"' ";
                jdbcTemplate.execute(sql);
            }else if("1".equals(hlw)){
                String sql="update lysjfb_detail_tablenameid set hlw='1' where pid='"+inDto.getString("id_")+"' ";
                jdbcTemplate.execute(sql);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 下载电子文件
     *
     * @param dto
     * @param request
     * @param response
     * @throws IOException
     */
    public void download_electronic_file(String filePath, Dto dto,
                                         HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        // TODO Auto-generated method stub
        String filename = dto.getString("fbs");
        String id_ = dto.getString("id_");
        String path = filePath+"/"+"sjfb"+"/"+id_+"/"+filename;
        response.setContentType("application/x-" + filename.split("\\.")[1]
                + ";charset=gb2312");
        // 名称点上后缀
        response.addHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(
                    path));
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            throw new AOSException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {

                }
            }
        }
    }

    public List<Map<String,Object>> exportData(Dto qDto) {
        List<Archive_tablefieldlistPO> titleDtos = dataService
                .getDataFieldListTitle2(qDto.getString("tablename"));
        String ennames="";
        if(titleDtos!=null&&titleDtos.size()>0){
            for(int i=0;i<titleDtos.size();i++){
                if(i==0){
                    ennames=titleDtos.get(i).getFieldenname();
                }else{
                    ennames+=","+titleDtos.get(i).getFieldenname();
                }
            }
        }
        String tablename=qDto.getString("tablename");
        String id_=qDto.getString("id_");
        String sql2="select "+ennames+" from "+tablename+" where id_ in (select t.id_ from "+tablename+" t  left join lysjfb_detail_tablenameid l on t.id_=l.tablename_id where 1=1  and l.tablename='"+tablename+"' and l.pid='"+id_+"')";
        List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
        return listAll;
    }
    public String getfbspath(String id_, String filename) {
        String path=linkAddress+"sjfb"+"/"+id_+"/"+filename;
        return path;
    }
}
