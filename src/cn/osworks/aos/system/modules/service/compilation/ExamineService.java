package cn.osworks.aos.system.modules.service.compilation;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import cn.osworks.aos.system.modules.service.resource.ModuleService;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExamineService extends JdbcDaoSupport {
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    public DataService dataService;
    @Autowired
    private SqlDao sysDao;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private Aos_sys_orgMapper aos_sys_orgMapper;
    @Autowired
    private Aos_sys_moduleMapper aos_sys_moduleMapper;
    @Autowired
    private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
    @Autowired
    private Aos_sys_userMapper aos_sys_userMapper;
    @Autowired
    private Archive_topicMapper archive_topicMapper;
    @Autowired
    private Archive_write_topicMapper archive_write_topicMapper;
    @Autowired
    private Archive_topic_tablenameidMapper archive_topic_tablenameidMapper;
    @Autowired
    private Archive_topic_userMapper archive_topic_userMapper;
    @Autowired
    private Archive_seminarMapper archive_seminarMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Archive_compilation_rwMapper archive_compilation_rwMapper;
    @Autowired
    private Archive_compilation_zgMapper archive_compilation_zgMapper;
    @Autowired
    private Archive_compilation_hgMapper archive_compilation_hgMapper;
    @Autowired
    private Compilation_topicMapper compilation_topicMapper;
    @Autowired
    private Archive_taskMapper archive_taskMapper;
    @Autowired
    private Aos_sys_module_userMapper aos_sys_module_userMapper;
    @Resource
    public void setJd(JdbcTemplate jd) {
        super.setJdbcTemplate(jd);
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
    public List<Map<String,Object>> listfirsttrial(Dto qDto) {
        String user=qDto.getString("user");
        List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
        String sql="select * from archive_task  where first_compilationperson like '%"+user+"%' and flag='1'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Object persons=list.get(i).get("first_compilationperson");
                if(persons!=null&&persons!=""){
                    String personss=(String)persons;
                    String[] person=personss.split(",");
                    for(int k=0;k<person.length;k++){
                        if(user.equals(person[k])){
                            listt.add(list.get(i));
                        }
                    }
                }
            }
        }
        return listt;
    }
    public List<Map<String,Object>> listnexttrial(Dto qDto) {
        String user=qDto.getString("user");
        List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
        String sql="select * from archive_task  where next_compilationperson like '%"+user+"%' and flag='2'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Object persons=list.get(i).get("next_compilationperson");
                if(persons!=null&&persons!=""){
                    String personss=(String)persons;
                    String[] person=personss.split(",");
                    for(int k=0;k<person.length;k++){
                        if(user.equals(person[k])){
                            listt.add(list.get(i));
                        }
                    }
                }
            }
        }
        return listt;
    }
    public List<Map<String,Object>> listlasttrial(Dto qDto) {
        String user=qDto.getString("user");
        List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
        String sql="select * from archive_task  where last_compilationperson like '%"+user+"%' and flag='3'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Object persons=list.get(i).get("last_compilationperson");
                if(persons!=null&&persons!=""){
                    String personss=(String)persons;
                    String[] person=personss.split(",");
                    for(int k=0;k<person.length;k++){
                        if(user.equals(person[k])){
                            listt.add(list.get(i));
                        }
                    }
                }
            }
        }
        return listt;
    }
    public List<Map<String,Object>> first_details_all(Dto qDto) {
        String user=qDto.getString("user");
        List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
        String sql="select * from archive_task  where first_compilationperson like '%"+user+"%'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Object persons=list.get(i).get("first_compilationperson");
                if(persons!=null&&persons!=""){
                    String personss=(String)persons;
                    String[] person=personss.split(",");
                    for(int k=0;k<person.length;k++){
                        if(user.equals(person[k])){
                            listt.add(list.get(i));
                        }
                    }
                }
            }
        }
        return listt;
    }

    public Dto seminar_initData(Dto qDto, HttpSession session) {
        Dto dto=Dtos.newDto();
        String[] b=new String[2^31];
        String[] n=new String[2^31];
        String[] m=new String[2^31];
        String[] v=new String[2^31];
        int x=0;
        int z=0;
        int y=0;
        int w=0;
        String user=(String)session.getAttribute("user");
        String sql="select * from archive_compilation_rw  where id_='"+qDto.getString("aos_module_id_")+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                String zgr=(String)list.get(i).get("zgr");
                if(zgr!=null&&zgr.length()>0){
                    String[] zgrs=zgr.split(",");
                    for(int t=0;t<zgrs.length;t++){
                        v[w]=zgrs[t];
                        w++;
                    }
                }
                String first=(String)list.get(i).get("hgr");
                if(first!=null&&first.length()>0){
                    String[] firsts=first.split(",");
                    for(int t=0;t<firsts.length;t++){
                        b[x]=firsts[t];
                        x++;
                    }
                }
                String next=(String)list.get(i).get("jdr");
                if(next!=null&&next.length()>0){
                    String[] nexts=next.split(",");
                    for(int t=0;t<nexts.length;t++){
                        n[z]=nexts[t];
                        z++;
                    }
                }
                String last=(String)list.get(i).get("zbj");
                if(last!=null&&last.length()>0){
                    String[] lasts=last.split(",");
                    for(int t=0;t<lasts.length;t++){
                        m[y]=lasts[t];
                        y++;
                    }
                }
            }
        }
        if(Arrays.asList(v).contains(user)){
            dto.put("zhuangao","1");
        }else{
            dto.put("zhuangao","0");
        }
        if(Arrays.asList(b).contains(user)){
            dto.put("first","1");
        }else{
            dto.put("first","0");
        }
        if(Arrays.asList(n).contains(user)){
            dto.put("next","1");
        }else{
            dto.put("next","0");
        }
        if(Arrays.asList(m).contains(user)){
            dto.put("last","1");
        }else{
            dto.put("last","0");
        }
        return dto;
    }
    public Dto seminar_by_initData(Dto qDto, HttpSession session) {
        Dto dto=Dtos.newDto();
        String sjkmc=qDto.getString("sjkmc");
        String byrwbh=qDto.getString("byrwbh");
        String byrwmc=qDto.getString("byrwmc");

        String[] b=new String[2^31];
        String[] n=new String[2^31];
        String[] m=new String[2^31];
        String[] v=new String[2^31];
        int c=0;
        int x=0;
        int z=0;
        int y=0;
        String user=(String)session.getAttribute("user");
        String sql="select * from archive_compilation_rw where  byrwbh='"+byrwbh+"' and byrwmc='"+byrwmc+"' and  sjkmc='"+sjkmc+"' ";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                String zhuangao=(String)list.get(i).get("zgr");
                if(zhuangao!=null&&zhuangao.length()>0){
                    String[] zhuangaos=zhuangao.split(",");
                    for(int t=0;t<zhuangaos.length;t++){
                        v[c]=zhuangaos[t];
                        c++;
                    }
                }
                String first=(String)list.get(i).get("hgr");
                if(first!=null&&first.length()>0){
                    String[] firsts=first.split(",");
                    for(int t=0;t<firsts.length;t++){
                        b[x]=firsts[t];
                        x++;
                    }
                }

                String next=(String)list.get(i).get("jdr");
                if(next!=null&&next.length()>0){
                    String[] nexts=next.split(",");
                    for(int t=0;t<nexts.length;t++){
                        n[z]=nexts[t];
                        z++;
                    }
                }

                String last=(String)list.get(i).get("zbj");
                if(last!=null&&last.length()>0){
                    String[] lasts=last.split(",");
                    for(int t=0;t<lasts.length;t++){
                        m[y]=lasts[t];
                        y++;
                    }
                }
            }
        }
        if(Arrays.asList(v).contains(user)){
            dto.put("zhuangao","1");
        }else{
            dto.put("zhuangao","0");
        }
        if(Arrays.asList(b).contains(user)){
            dto.put("first","1");
        }else{
            dto.put("first","0");
        }
        if(Arrays.asList(n).contains(user)){
            dto.put("next","1");
        }else{
            dto.put("next","0");
        }
        if(Arrays.asList(m).contains(user)){
            dto.put("last","1");
        }else{
            dto.put("last","0");
        }
        return dto;
    }
    public boolean savefirstcompilationdetails2(Dto qDto) {
        String tablename=qDto.getString("tablename");
        String first_compilation_message=qDto.getString("first_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
        //批量修改
        try{
            Dto dto= Dtos.newDto();
            int index=archive_topic_userMapper.rows(dto);
            //将鉴定信息保存
            Archive_topic_userPO archive_topic_userPO=new Archive_topic_userPO();
            archive_topic_userPO.setId_(AOSId.uuid());
            archive_topic_userPO.setPid(topic_id_);
            archive_topic_userPO.setUsers(qDto.getString("user"));
            archive_topic_userPO.setOperate_description(first_compilation_message);
            archive_topic_userPO.setOperate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            archive_topic_userPO.setIndex_(index+1);
            archive_topic_userPO.setCompilation_message(qDto.getString("compilation_message"));
            archive_topic_userPO.setType("2");
            archive_topic_userMapper.insert(archive_topic_userPO);
            //修改标记（此时把标记改为2）
           // String sql="update archive_write_topic set write_flag='3' where id_='"+topic_id_+"' ";
           // jdbcTemplate.execute(sql);
            //archive——taskxiugai修改标记，并存入合并信息在task表中
            String sql2="update archive_task set flag='2',description='"+qDto.getString("compilation_message")+"' where id_='"+topic_id_+"' ";
            jdbcTemplate.execute(sql2);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean savefirstcompilationdetails(Dto qDto,UserInfoVO userInfoVO) {
        String tablename=qDto.getString("tablename");
        String first_compilation_message=qDto.getString("first_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        String compilation_message=qDto.getString("compilation_message");
        String user=userInfoVO.getAccount_();
        List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
        //批量修改
        try{
            Dto dto= Dtos.newDto();
            //2.把合稿的文稿存放到记事本txt文件中去，记事本的路径名称组合为D：dataaos/表明/任务编号/hegao/合稿人.txt
            String hgpath=filePath+tablename+File.separator+rw_id_+File.separator+"hegao";
            String hgname=user+".txt";
            addpath(hgpath,hgname,compilation_message);
            //3.生成撰稿条目保存到撰稿数据表中以便于查看。（如果存在就更新）
            addupdateHg(qDto,hgpath,hgname,first_compilation_message);
            //2.
            int index=archive_topic_userMapper.rows(dto);
            //将鉴定信息保存
            Archive_topic_userPO archive_topic_userPO=new Archive_topic_userPO();
            archive_topic_userPO.setId_(AOSId.uuid());
            archive_topic_userPO.setPid(rw_id_);
            archive_topic_userPO.setUsers(qDto.getString("user"));
            archive_topic_userPO.setOperate_description(first_compilation_message);
            archive_topic_userPO.setOperate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            archive_topic_userPO.setIndex_(index+1);
            archive_topic_userPO.setCompilation_message(hgpath+File.separator+hgname);
            archive_topic_userPO.setType("2");
            archive_topic_userMapper.insert(archive_topic_userPO);
            //修改标记（此时把标记改为2）
            // String sql="update archive_write_topic set write_flag='3' where id_='"+topic_id_+"' ";
            // jdbcTemplate.execute(sql);
            //修改标记（此时把标记改为2）
            String sql="update archive_compilation_rw set flag='已合稿',hg_time='"+archive_topic_userPO.getOperate_time()+"',hg_message='"+(hgpath+File.separator+hgname)+"' where id_='"+rw_id_+"' ";
            jdbcTemplate.execute(sql);
            notificationService.SubmitHeGao(qDto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean savenextcompilationdetails(Dto qDto, UserInfoVO userInfoVO) {
        String tablename=qDto.getString("tablename");
        String next_compilation_message=qDto.getString("next_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        String compilation_message=qDto.getString("compilation_message");
        String user=userInfoVO.getAccount_();
        String hguser=qDto.getString("hguser");
        List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
        //批量修改
        try{
            Dto dto= Dtos.newDto();
            //2.把合稿的文稿存放到记事本txt文件中去，记事本的路径名称组合为D：dataaos/表明/任务编号/hegao/合稿人.txt
            String hgpath=filePath+tablename+File.separator+rw_id_+File.separator+"hegao";
            String hgname=hguser+".txt";
            addpath(hgpath,hgname,compilation_message);

            //3.生成撰稿条目保存到撰稿数据表中以便于查看。（如果存在就更新）
            updateHg(qDto,hgpath,hgname,next_compilation_message);

            int index=archive_topic_userMapper.rows(dto);
            //将鉴定信息保存
            Archive_topic_userPO archive_topic_userPO=new Archive_topic_userPO();
            archive_topic_userPO.setId_(AOSId.uuid());
            archive_topic_userPO.setPid(rw_id_);
            archive_topic_userPO.setUsers(qDto.getString("user"));
            archive_topic_userPO.setOperate_description(next_compilation_message);
            archive_topic_userPO.setOperate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            archive_topic_userPO.setIndex_(index+1);
            archive_topic_userPO.setCompilation_message(hgpath+File.separator+hgname);
            archive_topic_userPO.setType("3");
            archive_topic_userMapper.insert(archive_topic_userPO);
            //修改标记（此时把标记改为2）
            // String sql="update archive_write_topic set write_flag='3' where id_='"+topic_id_+"' ";
            // jdbcTemplate.execute(sql);
            //archive——taskxiugai修改标记，并存入合并信息在task表中
            String sql="update archive_compilation_rw set flag='已校对',jd_time='"+archive_topic_userPO.getOperate_time()+"',hg_message='"+(hgpath+File.separator+hgname)+"' where id_='"+rw_id_+"' ";
            //此时把任务进行更新一下
            jdbcTemplate.execute(sql);
            notificationService.SubmitJiaoDui(qDto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean nosavenextcompilationdetails(Dto qDto) {
        String tablename=qDto.getString("tablename");
        String next_compilation_message=qDto.getString("next_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        String compilation_message=qDto.getString("compilation_message");
        UserInfoVO userInfoVO=qDto.getUserInfo();
        String user=userInfoVO.getAccount_();
        String hguser=qDto.getString("hguser");
        List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
        //批量修改
        try{
            Dto dto= Dtos.newDto();
            //2.把合稿的文稿存放到记事本txt文件中去，记事本的路径名称组合为D：dataaos/表明/任务编号/hegao/合稿人.txt
            String hgpath=filePath+tablename+File.separator+rw_id_+File.separator+"hegao";
            String hgname=hguser+".txt";
            addpath(hgpath,hgname,compilation_message);

            //3.生成撰稿条目保存到撰稿数据表中以便于查看。（如果存在就更新）
            updateHg(qDto,hgpath,hgname,next_compilation_message);
            int index=archive_topic_userMapper.rows(dto);
            //将鉴定信息保存
            Archive_topic_userPO archive_topic_userPO=new Archive_topic_userPO();
            archive_topic_userPO.setId_(AOSId.uuid());
            archive_topic_userPO.setPid(rw_id_);
            archive_topic_userPO.setUsers(qDto.getString("user"));
            archive_topic_userPO.setOperate_description(next_compilation_message);
            archive_topic_userPO.setOperate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            archive_topic_userPO.setIndex_(index+1);
            archive_topic_userPO.setCompilation_message(hgpath+File.separator+hgname);
            archive_topic_userPO.setType("3");
            archive_topic_userMapper.insert(archive_topic_userPO);
            //修改标记（此时把标记改为2）
            // String sql="update archive_write_topic set write_flag='3' where id_='"+topic_id_+"' ";
            // jdbcTemplate.execute(sql);
            //archive——taskxiugai修改标记，并存入合并信息在task表中
            String sql="update archive_compilation_rw set flag='已撰稿',hg_message='"+(hgpath+File.separator+hgname)+"' where id_='"+rw_id_+"' ";
            jdbcTemplate.execute(sql);
            notificationService.TaskJiaoDui(qDto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean savelastcompilationdetails(Dto qDto) {
        String tablename=qDto.getString("tablename");
        String last_compilation_message=qDto.getString("last_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        String compilation_message=qDto.getString("compilation_message");
        UserInfoVO userInfoVO=qDto.getUserInfo();
        String user=userInfoVO.getAccount_();
        String hguser=qDto.getString("hguser");
        List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
        //批量修改
        try{
            Dto dto= Dtos.newDto();
            int index=archive_topic_userMapper.rows(dto);
            //2.把合稿的文稿存放到记事本txt文件中去，记事本的路径名称组合为D：dataaos/表明/任务编号/hegao/合稿人.txt
            String hgpath=filePath+tablename+File.separator+rw_id_+File.separator+"hegao";
            String hgname=hguser+".txt";
            addpath(hgpath,hgname,compilation_message);

            //3.生成撰稿条目保存到撰稿数据表中以便于查看。（如果存在就更新）
            updatezs(qDto,hgpath,hgname,last_compilation_message);
            //将鉴定信息保存
            Archive_topic_userPO archive_topic_userPO=new Archive_topic_userPO();
            archive_topic_userPO.setId_(AOSId.uuid());
            archive_topic_userPO.setPid(rw_id_);
            archive_topic_userPO.setUsers(qDto.getString("user"));
            archive_topic_userPO.setOperate_description(last_compilation_message);
            archive_topic_userPO.setOperate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            archive_topic_userPO.setIndex_(index+1);
            archive_topic_userPO.setCompilation_message(hgpath+File.separator+hgname);
            archive_topic_userPO.setType("4");
            archive_topic_userMapper.insert(archive_topic_userPO);
            //修改标记（此时把标记改为2）
            // String sql="update archive_write_topic set write_flag='3' where id_='"+topic_id_+"' ";
            // jdbcTemplate.execute(sql);
            //archive——taskxiugai修改标记，并存入合并信息在task表中
            String sql="update archive_compilation_rw set flag='已完成',zbj_time='"+archive_topic_userPO.getOperate_time()+"',hg_message='"+(hgpath+File.separator+hgname)+"' where id_='"+rw_id_+"' ";
            jdbcTemplate.execute(sql);
            notificationService.SubmitZongBianJian(dto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean nosavelastcompilationdetails(Dto qDto) {
        String tablename=qDto.getString("tablename");
        String last_compilation_message=qDto.getString("last_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
        String compilation_message=qDto.getString("compilation_message");
        UserInfoVO userInfoVO=qDto.getUserInfo();
        String user=userInfoVO.getAccount_();
        String hguser=qDto.getString("hguser");
        //批量修改
        try{
            Dto dto= Dtos.newDto();
            int index=archive_topic_userMapper.rows(dto);
            //2.把合稿的文稿存放到记事本txt文件中去，记事本的路径名称组合为D：dataaos/表明/任务编号/hegao/合稿人.txt
            String hgpath=filePath+tablename+File.separator+rw_id_+File.separator+"hegao";
            String hgname=hguser+".txt";
            addpath(hgpath,hgname,compilation_message);

            //3.生成撰稿条目保存到撰稿数据表中以便于查看。（如果存在就更新）
            updatezs(qDto,hgpath,hgname,last_compilation_message);
            //将鉴定信息保存
            Archive_topic_userPO archive_topic_userPO=new Archive_topic_userPO();
            archive_topic_userPO.setId_(AOSId.uuid());
            archive_topic_userPO.setPid(rw_id_);
            archive_topic_userPO.setUsers(qDto.getString("user"));
            archive_topic_userPO.setOperate_description(last_compilation_message);
            archive_topic_userPO.setOperate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            archive_topic_userPO.setIndex_(index+1);
            archive_topic_userPO.setCompilation_message(hgpath+File.separator+hgname);
            archive_topic_userPO.setType("4");
            archive_topic_userMapper.insert(archive_topic_userPO);
            //修改标记（此时把标记改为2）
            // String sql="update archive_write_topic set write_flag='3' where id_='"+topic_id_+"' ";
            // jdbcTemplate.execute(sql);
            //archive——taskxiugai修改标记，并存入合并信息在task表中
            String sql="update archive_compilation_rw set flag='已合稿', hg_message='"+(hgpath+File.separator+hgname)+"' where id_='"+rw_id_+"' ";
            jdbcTemplate.execute(sql);
            notificationService.TaskZongBianJian(qDto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Map<String,Object>> listexaminedescription(Dto qDto){
        String topic_id_=qDto.getString("topic_id_");
        String sql="select archive_write_topic.tid as tid ," +
                "archive_write_topic.id_ as id_," +
                "archive_write_topic.task_number as task_number," +
                " archive_write_topic.write_number as write_number," +
                "archive_write_topic.write_datatime as write_datatime," +
                "archive_write_topic.tablename as tablename," +
                "archive_write_topic.write_createperson as write_createperson," +
                "archive_write_topic.write_description as write_description," +
                "archive_write_topic.task_name as task_name," +
                "archive_write_topic.write_name as write_name," +
                "archive_topic_user.operate_description as operate_description," +
                "archive_topic_user.compilation_message as compilation_message" +
                " from archive_write_topic,archive_topic_user where tid='"+topic_id_+"' and archive_write_topic.id_=archive_topic_user.pid";
            List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
            return list;
    }
    public List<Map<String, Object>> listoperationlogin(Dto qDto) {
        String sql="select * from archive_topic_user";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    public  List<Map<String,Object>> getcompilationmessage(Dto qDto) {
        String topic_id_=qDto.getString("topic_id_");
        String sql="select description from archive_task where id_='"+topic_id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    public  List<Map<String,Object>> getcompilation_zhuagao_message(Dto qDto) {
        String topic_id_ = qDto.getString("topic_id_");
        String type = qDto.getString("type");
        String sql = "select compilation_message from archive_topic_user where pid='" + topic_id_ + "' and type='"+type+"' order by index_ desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;

    }
        public String getLastTrial(Dto qDto) {
        String sql="select operate_description from archive_topic_user where pid='"+qDto.getString("aos_rows_")+"'  and type='4' order by index_ desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            return (String)list.get(0).get("operate_description");
        }else{
            return "";
        }
    }
    public String getNextTrial(Dto qDto) {
        String sql="select operate_description from archive_topic_user where pid='"+qDto.getString("aos_rows_")+"' and type='3' order by index_ desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            return (String)list.get(0).get("operate_description");
        }else{
            return "";
        }
    }
    public List<Map<String,Object>> listfirsttrial(Dto qDto, HttpSession session) {
        List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
        String sql="select * from archive_task  where first_compilationperson like '%"+session.getAttribute("user")+"%' and flag ='1'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Object persons=list.get(i).get("first_compilationperson");
                if(persons!=null&&persons!=""){
                    String personss=(String)persons;
                    String[] person=personss.split(",");
                    for(int k=0;k<person.length;k++){
                        if(session.getAttribute("user").equals(person[k])){
                            listt.add(list.get(i));
                        }
                    }
                }
            }
        }
        return listt;
    }

    public List<Map<String,Object>> listnexttrial(Dto qDto, HttpSession session) {
        List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
        String sql="select * from archive_task  where next_compilationperson like '%"+session.getAttribute("user")+"%' and flag ='2'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Object persons=list.get(i).get("next_compilationperson");
                if(persons!=null&&persons!=""){
                    String personss=(String)persons;
                    String[] person=personss.split(",");
                    for(int k=0;k<person.length;k++){
                        if(session.getAttribute("user").equals(person[k])){
                            listt.add(list.get(i));
                        }
                    }
                }
            }
        }
        return listt;
    }
    public List<Map<String,Object>> listlasttrial(Dto qDto,HttpSession session) {
        List<Map<String,Object>> listt=new ArrayList<Map<String,Object>>();
        String sql="select * from archive_task  where last_compilationperson like '%"+session.getAttribute("user")+"%' and flag ='3'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Object persons=list.get(i).get("last_compilationperson");
                if(persons!=null&&persons!=""){
                    String personss=(String)persons;
                    String[] person=personss.split(",");
                    for(int k=0;k<person.length;k++){
                        if(session.getAttribute("user").equals(person[k])){
                            listt.add(list.get(i));
                        }
                    }
                }
            }
        }
        return listt;
    }


    public void getmodel(Dto qDto, String cascode_id_) {
        String sjkmc="";
        String byrwbhmc="";
        String parent_id_="";
        String sql="select * from aos_sys_module where cascade_id_='"+cascode_id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            sjkmc=(String)list.get(0).get("tablename");
            parent_id_=(String)list.get(0).get("parent_id_");

        }
        String sql2="select * from aos_sys_module where id_='"+parent_id_+"'";
        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
        if(list2!=null&&list2.size()>0){
            byrwbhmc=(String)list2.get(0).get("name_");
        }
        qDto.put("sjkmc",sjkmc);
    }

    public List<Map<String, Object>> listCompilation_examine(Dto qDto) {
        String byrwmc=qDto.getString("byrwmc");
        String byrwbh=qDto.getString("byrwbh");
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_compilation_rw where byrwmc='"+byrwmc+"' and byrwbh='"+byrwbh+"' order by byrwbh offset "+pageStart+" rows fetch next "+limit+" rows only";
        return jdbcTemplate.queryForList(sql);
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageAll(Dto qDto) {
        //查询年度条件是否存在
        String byrwbh=qDto.getString("byrwbh");
        String byrwmc=qDto.getString("byrwmc");
        String sql = "SELECT COUNT(*) FROM archive_compilation_rw where byrwbh='"+byrwbh+"' and byrwmc='"+byrwmc+"'";
        return jdbcTemplate.queryForInt(sql);
    }

    public List<Map<String, Object>> check_zhuangao(Dto qDto) {
        String id_=qDto.getString("id_");
        String user=qDto.getString("user");
        String sql="select * from archive_compilation_rw where zgr like '%"+user+"%' and id_='"+id_+"' and flag='未撰稿'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String, Object>> check_hegao(Dto qDto) {
        String id_=qDto.getString("id_");
        String user=qDto.getString("user");
        String sql="select * from archive_compilation_rw where hgr like '%"+user+"%' and id_='"+id_+"' and flag <>'已合稿' and flag <>'已校对' and flag <>'已完成'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String, Object>> check_jiaodui(Dto qDto) {
        String id_=qDto.getString("id_");
        String user=qDto.getString("user");
        String sql="select * from archive_compilation_rw where jdr like '%"+user+"%' and id_='"+id_+"' and flag='已合稿'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String, Object>> check_zongbianji(Dto qDto) {
        String id_=qDto.getString("id_");
        String user=qDto.getString("user");
        String sql="select * from archive_compilation_rw where zbj like '%"+user+"%' and id_='"+id_+"' and flag='已校对'";
        return jdbcTemplate.queryForList(sql);
    }
    /*
     * 查询表名
     */
    public String findTablename(String id_) {
        // TODO Auto-generated method stub
        String sql="select tablename from archive_compilation_tablenameid where pid='"+id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            return (String)list.get(0).get("tablename");
        }else{
            return "";
        }
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Map<String,Object>> getDataFieldListDisplayAll2(Dto qDto, HttpSession session) {
        String sql="select * from "+qDto.getString("tablename")+" where id_ in (select tablename_id from archive_topic_tablenameid where pid='"+qDto.getString("topic_id_")+"')";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    public Dto getpath(String id_, String topic_id_,String tablename) {
        Dto out=Dtos.newDto();
        Archive_topicPO archive_topicPO = archive_topicMapper.selectByKey(topic_id_);
        String sql = "select * from " + tablename + "_path where tid ='" + id_ + "'";
        String filepath = "";
        String urlpath = "";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String path = (String) map.get("_s_path");
                if (path.split("\\.")[1].equals("pdf")) {
                    String filename = (String) map.get("dirname");
                    // 组合路径
                    filepath = filePath  + File.separator+filename
                            + path;
                    urlpath = topicAddress  + File.separator+filename
                            + path;
                    // 获取当前ip地址
                    // 路径批量替换
                    // wsda/2e9275c32e6849afab038892747249c2/数字档案馆系统测试办法（档办发[2014]6号).pdf
                    filepath = filepath.replace("\\", "/");
                    urlpath = urlpath.replace("\\", "/");
                    out.put("pdfpath",filepath);
                    out.put("urlpath",urlpath);
                    break;
                }
            }
        }
        return out;
    }
    public String booleanPath(String filepath){
        URL httpurl = null;
        try {
            httpurl = new URL(new URI(filepath).toASCIIString());
            URLConnection urlConnection = httpurl.openConnection();
            // urlConnection.getInputStream();
            Long TotalSize=Long.parseLong(urlConnection.getHeaderField("Content-Length"));
            if (TotalSize <= 0){
                return "";
            }
            return filepath;
        } catch (Exception e) {
            logger.debug(httpurl + "文件不存在");
            return "";
        }
    }
    public boolean savewritecompilationdetails(Dto qDto,UserInfoVO userInfoVO) {
        String tablename=qDto.getString("tablename");
        String write_compilation_message=qDto.getString("write_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        String user=qDto.getString("user");
        String compilation_message=qDto.getString("compilation_message");
        List<Dto> list= AOSJson.fromJson(qDto.getString("aos_rows_"));
        //批量修改
        try{
            Dto dto=Dtos.newDto();
            //2.把撰稿的文稿存放到记事本txt文件中去，记事本的路径名称组合为D：dataaos/表明/任务编号/撰稿/撰稿人.txt
            String zgpath=filePath+tablename+File.separator+topic_id_+File.separator+"zhuangao";
            String zgname=user+".txt";
            addpath(zgpath,zgname,compilation_message);
            //3.生成撰稿条目保存到撰稿数据表中以便于查看。（如果存在就更新）
            addupdateZg(qDto,zgpath,zgname,compilation_message);
            //4.将鉴定信息保存
            int index=archive_topic_userMapper.rows(dto);
            Archive_topic_userPO archive_topic_userPO=new Archive_topic_userPO();
            archive_topic_userPO.setId_(AOSId.uuid());
            archive_topic_userPO.setPid(rw_id_);
            archive_topic_userPO.setUsers(qDto.getString("user"));
            archive_topic_userPO.setOperate_description(write_compilation_message);
            archive_topic_userPO.setOperate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            archive_topic_userPO.setIndex_(index+1);
            archive_topic_userPO.setCompilation_message(zgpath+File.separator+zgname);
            archive_topic_userPO.setType("1");
            archive_topic_userMapper.insert(archive_topic_userPO);
            notificationService.SubmitZhuanGao(qDto,userInfoVO);
            //修改标记（此时把标记改为2）
           // String sql="update archive_compilation_rw set flag='已撰稿',zg_time='"+archive_topic_userPO.getOperate_time()+"',zg_message='"+qDto.getString("compilation_message")+"' where id_='"+topic_id_+"' ";
            //jdbcTemplate.execute(sql);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新或添加撰稿详情
     * @param Dto
     * @param zgpath
     * @param zgname
     * @param compilation_message
     */
    private void addupdateZg(Dto qDto, String zgpath, String zgname, String compilation_message) {
        Dto out=Dtos.newDto();
        String tablename=qDto.getString("tablename");
        String write_compilation_message=qDto.getString("write_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        String user=qDto.getString("user");

        Archive_compilation_zgPO archive_compilation_zgPO=new Archive_compilation_zgPO();
        archive_compilation_zgPO.setRw_id_(rw_id_);
        archive_compilation_zgPO.setTablename(tablename);
        archive_compilation_zgPO.setUsername(qDto.getString("user"));
        AOSUtils.copyProperties(archive_compilation_zgPO,out);
        List<Archive_compilation_zgPO> list=archive_compilation_zgMapper.list(out);
        if(list!=null&&list.size()>0){
            //修改
            Archive_compilation_zgPO archive_compilation_zgPO_update=new Archive_compilation_zgPO();
            archive_compilation_zgPO_update.setId_(list.get(0).getId_());
            archive_compilation_zgPO_update.setZg_description(write_compilation_message);
            archive_compilation_zgPO_update.setOperate_time(AOSUtils.getDateTimeStr());
            archive_compilation_zgMapper.updateByKey(archive_compilation_zgPO_update);
        }else{
            //添加
            archive_compilation_zgPO.setId_(AOSId.uuid());
            archive_compilation_zgPO.setZg_description(write_compilation_message);
            archive_compilation_zgPO.setZg_path(zgpath+File.separator+zgname);
            archive_compilation_zgPO.setOperate_time(AOSUtils.getDateTimeStr());
            archive_compilation_zgMapper.insert(archive_compilation_zgPO);
        }

    }
    /**
     * 更新或添加撰稿详情
     * @param Dto
     * @param zgpath
     * @param zgname
     * @param compilation_message
     */
    private void addupdateHg(Dto qDto, String hgpath, String hgname, String compilation_message) {
        Dto out=Dtos.newDto();
        String tablename=qDto.getString("tablename");
        String write_compilation_message=qDto.getString("write_compilation_message");
        String rw_id_=qDto.getString("rw_id_");
        String user=qDto.getString("user");

        Archive_compilation_hgPO archive_compilation_hgPO=new Archive_compilation_hgPO();
        archive_compilation_hgPO.setRw_id_(rw_id_);
        archive_compilation_hgPO.setTablename(tablename);
        archive_compilation_hgPO.setUsername(qDto.getString("user"));
        AOSUtils.copyProperties(archive_compilation_hgPO,out);
        List<Archive_compilation_hgPO> list=archive_compilation_hgMapper.list(out);
        if(list!=null&&list.size()>0){
            //修改
            Archive_compilation_hgPO archive_compilation_hgPO_update=new Archive_compilation_hgPO();
            archive_compilation_hgPO_update.setId_(list.get(0).getId_());
            archive_compilation_hgPO_update.setHg_description(compilation_message);
            archive_compilation_hgPO_update.setOperate_time(AOSUtils.getDateTimeStr());
            archive_compilation_hgMapper.updateByKey(archive_compilation_hgPO_update);
        }else{
            //添加
            archive_compilation_hgPO.setId_(AOSId.uuid());
            archive_compilation_hgPO.setHg_description(compilation_message);
            archive_compilation_hgPO.setHg_path(hgpath+File.separator+hgname);
            archive_compilation_hgPO.setOperate_time(AOSUtils.getDateTimeStr());
            archive_compilation_hgMapper.insert(archive_compilation_hgPO);
        }
    }
    /**
     * 更新撰稿详情(校对人)
     * @param Dto
     * @param zgpath
     * @param zgname
     * @param compilation_message
     */
    private void updateHg(Dto qDto, String hgpath, String hgname, String compilation_message) {
        Dto out=Dtos.newDto();
        String tablename=qDto.getString("tablename");
        String write_compilation_message=qDto.getString("write_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        String user=qDto.getString("user");

        Archive_compilation_hgPO archive_compilation_hgPO=new Archive_compilation_hgPO();
        archive_compilation_hgPO.setRw_id_(rw_id_);
        archive_compilation_hgPO.setTablename(tablename);
        archive_compilation_hgPO.setUsername(qDto.getString("byuser"));
        AOSUtils.copyProperties(archive_compilation_hgPO,out);
        List<Archive_compilation_hgPO> list=archive_compilation_hgMapper.list(out);
        if(list!=null&&list.size()>0){
            //修改
            Archive_compilation_hgPO archive_compilation_hgPO_update=new Archive_compilation_hgPO();
            archive_compilation_hgPO_update.setId_(list.get(0).getId_());
            archive_compilation_hgPO_update.setJd_description(compilation_message);
            archive_compilation_hgPO_update.setOperate_time(AOSUtils.getDateTimeStr());
            archive_compilation_hgMapper.updateByKey(archive_compilation_hgPO_update);
        }
    }
    /**
     * 更新撰稿详情(总编辑人)
     * @param Dto
     * @param zgpath
     * @param zgname
     * @param compilation_message
     */
    private void updatezs(Dto qDto, String hgpath, String hgname, String compilation_message) {
        Dto out=Dtos.newDto();
        String tablename=qDto.getString("tablename");
        String write_compilation_message=qDto.getString("write_compilation_message");
        String topic_id_=qDto.getString("topic_id_");
        String user=qDto.getString("user");

        Archive_compilation_hgPO archive_compilation_hgPO=new Archive_compilation_hgPO();
        archive_compilation_hgPO.setRw_id_(topic_id_);
        archive_compilation_hgPO.setTablename(tablename);
        archive_compilation_hgPO.setUsername(qDto.getString("byuser"));
        AOSUtils.copyProperties(archive_compilation_hgPO,out);
        List<Archive_compilation_hgPO> list=archive_compilation_hgMapper.list(out);
        if(list!=null&&list.size()>0){
            //修改
            Archive_compilation_hgPO archive_compilation_hgPO_update=new Archive_compilation_hgPO();
            archive_compilation_hgPO_update.setId_(list.get(0).getId_());
            archive_compilation_hgPO_update.setZbj_description(compilation_message);
            archive_compilation_hgPO_update.setOperate_time(AOSUtils.getDateTimeStr());
            archive_compilation_hgMapper.updateByKey(archive_compilation_hgPO_update);
        }
    }
    /**
     * 在指定路径中写入文本(撰稿人合稿人)
     * @param zgpath
     * @param compilation_message
     */
    private void addpath(String zgpath,String zgname, String compilation_message)throws  Exception {
        File file=new File(zgpath);
        if(!file.exists()){
            file.mkdirs();
        }
        File fileall=new File(zgpath+File.separator+zgname);
        if(!fileall.exists()){
            fileall.createNewFile();
        }
        PrintStream ps = new PrintStream(new FileOutputStream(fileall));
        ps.println(compilation_message);// 往文件里写入字符串
        ps.close();

    }

    public String gethegaomessage(Dto qDto) {
        String id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        String user=qDto.getString("user");
        String hg_path="";
        String sql="select hg_path from archive_compilation_hg where rw_id_='"+rw_id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            hg_path=(String)list.get(0).get("hg_path");
        }
        String hg_message=gethgmessage(hg_path);
        return hg_message;
    }

    public String findCompilation_RwName(Dto out) {
        String id_=out.getString("id_");
        String rwname="";
        Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(id_);
        if(archive_compilation_rwPO!=null){
            rwname=archive_compilation_rwPO.getByrwbh();
        }else{
            rwname="文件名";
        }
        return rwname;
    }

    /**
     * 撰稿和合稿组合查询结果（此时是校对人和总编辑的展开方式）
     * @param inDto
     * @param session
     * @return
     */
    public List<Map<String, Object>> listZg(Dto inDto, HttpSession session) {
        String sql="select zg.id_ as id_,zg.rw_id_ as rw_id_,zg.username as username,zg.tablename as tablename,zg.zg_path as zg_path,zg.zg_description as zg_description,zg.operate_time as operate_time," +
                " hg.hg_description as hg_description,hg.jd_description as jd_description,hg.zbj_description as zbj_description from archive_compilation_zg as zg,archive_compilation_rw as rw,archive_compilation_hg as hg where rw.id_=zg.rw_id_ and rw.id_=hg.rw_id_ and  rw.id_='"+inDto.getString("rw_id_")+"'";
        return jdbcTemplate.queryForList(sql);
    }
    /**
     * 撰稿和合稿组合查询结果（此时是合稿的展开方式）
     * @param inDto
     * @param session
     * @return
     */
    public List<Map<String, Object>> listZg_hegao(Dto inDto, HttpSession session) {
        //此时做一下判断如果合稿操作过就执行三表联合查询语句
        String sql="select * from archive_compilation_hg where rw_id_='"+inDto.getString("rw_id_")+"'" ;
        String sql2="";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            sql2="select zg.id_ as id_,zg.rw_id_ as rw_id_,zg.username as username,zg.tablename as tablename,zg.zg_path as zg_path,zg.zg_description as zg_description,zg.operate_time as operate_time," +
                    " hg.hg_description as hg_description,hg.jd_description as jd_description,hg.zbj_description as zbj_description from archive_compilation_zg as zg,archive_compilation_rw as rw,archive_compilation_hg as hg where rw.id_=zg.rw_id_ and rw.id_=hg.rw_id_ and  rw.id_='"+inDto.getString("rw_id_")+"'";

        }else{
            sql2="select zg.id_ as id_,zg.rw_id_ as rw_id_,zg.username as username,zg.tablename as tablename,zg.zg_path as zg_path,zg.zg_description as zg_description,zg.operate_time as operate_time" +
                    "  from archive_compilation_zg as zg,archive_compilation_rw as rw where rw.id_=zg.rw_id_ and   rw.id_='"+inDto.getString("rw_id_")+"'";
        }

        return jdbcTemplate.queryForList(sql2);
    }

    public String getzhuangaomessage(Dto inDto) {
        String zg_path=inDto.getString("zg_path");
        StringBuilder result = new StringBuilder();
        try {
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(zg_path)), "GBK"));
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
    public String gethgmessage(String hg_path) {
        StringBuilder result = new StringBuilder();
        try {
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(hg_path)), "GBK"));
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
     * 根据任务id得到archive_compilation_hg的用户
     * @param id_
     * @return
     */
    public String findHgUser(String id_) {
        String user="";
        String sql="select * from archive_compilation_hg where rw_id_='"+id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
                user=(String)list.get(0).get("username");
        }
        return user;
    }

    public boolean addtopic(Dto qDto) {
        try{
            Compilation_topicPO compilation_topicPO=new Compilation_topicPO();
            AOSUtils.copyProperties(qDto,compilation_topicPO);
            compilation_topicPO.setId_(AOSId.uuid());
            compilation_topicMapper.insert(compilation_topicPO);
            //此时添加到数据compolitation_rw中
            addTopic_tablenameid(qDto,compilation_topicPO.getId_());
            //目录树添加专题tree，并赋予byadmin权限
            addtree(qDto,"0.024.008",compilation_topicPO.getId_());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * 删除条目
     *
     * @param qDto
     * @return
     */
    public Dto delete_topic(Dto dto) {
        Dto outDto = Dtos.newDto();
        String[] selections = dto.getRows();
        int del = 0;
        for (String id_ : selections) {
            //删除节点
            //1查询条目的任务名称和任务编号组合组合字段，并查询到节点id值
            delete_tree(id_);
            //删除专题数据的目录树
            //删除任务
            jdbcTemplate.execute(" delete from compilation_topic where id_='"
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

    /**
     * 检索专题数据
     * @param inDto
     * @param session
     * @return
     */
    public List<Map<String, Object>> getData(Dto inDto, HttpSession session) {
        String tablename=inDto.getString("tablename");
        String id_=inDto.getString("id_");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(inDto.getString("limit"));
        Integer page = Integer.valueOf(inDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_topic_tablenameid where pid='"+id_+"'";
        List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql);
        if(listDto!=null&&listDto.size()>0){
            for(int i=0;i<listDto.size();i++){
                getDatafromtopic(list,inDto,limit,page,pageStart,listDto.get(i));
            }
        }
        //此时对list集合分页操作
        list=getPageList(list,page,limit);
        return list;
        //根据任务id得到所有的条目id值
        //String sql2="select * from "+tablename+" where id_ in(select tablename_id from archive_topic_tablenameid where pid ='"+inDto.getString("id_")+"' order by id_ offset "+pageStart+" rows fetch next "+limit+" rows only)";
       // List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql2);

    }

    private List<Map<String, Object>> getPageList(List<Map<String, Object>> list, Integer page, Integer limit) {
        int fromIndex = (page - 1) * limit;
        if (fromIndex >= list.size()) {
            return null;//空数组
        }
        if(fromIndex<0){
            return null;//空数组
        }
        int toIndex = page * limit;
        if (toIndex >= list.size()) {
            toIndex = list.size();
        }
        return list.subList(fromIndex, toIndex);
    }

    /**
     * 查询到一条数据进行新的集合添加
     * @param list
     * @param inDto
     * @param limit
     * @param page
     * @param pageStart
     * @param stringObjectMap
     */
    private void getDatafromtopic(List<Map<String, Object>> list, Dto inDto, Integer limit, Integer page, Integer pageStart, Map<String, Object> stringObjectMap) {
        String sql2="select * from "+stringObjectMap.get("tablename")+" where id_ in(select tablename_id from archive_topic_tablenameid where id_='"+stringObjectMap.get("id_")+"')";
        List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql2);
        if(listDto!=null&&listDto.size()>0){
            listDto.get(0).put("tablename",stringObjectMap.get("tablename"));
            list.add(listDto.get(0));
        }
    }

    /**
     *
     * 查询专题数据总数
     *
     * @return
     */
    public int getPageTotal_Data(Dto inDto) {
        String tablename=inDto.getString("tablename");
        String id_=inDto.getString("id_");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(inDto.getString("limit"));
        Integer page = Integer.valueOf(inDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_topic_tablenameid where pid='"+id_+"'";
        List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql);
        if(listDto!=null&&listDto.size()>0){
            for(int i=0;i<listDto.size();i++){
                getDatafromtopic(list,inDto,limit,page,pageStart,listDto.get(i));
            }
        }
        return list.size();
    }

    /**
     * 删除目录树
     * @param id_
     */
    public void delete_tree(String id_) {
        //查询到当前的编号和任务名称组合的节点id值
        //根据名称查询到节点id集合
        String sql2="select * from aos_sys_module where id_='"+id_ +"'";
        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
        if(list2!=null&&list2.size()>0){
            for(int i=0;i<list2.size();i++){
                Dto out=Dtos.newDto();
                out.put("aos_rows_",(String)list2.get(i).get("id_"));
                moduleService.deleteModule(out);
            }
        }
    }

    /**
     * 修改专题
     * @param qDto
     * @return
     */
    public boolean edittopic(Dto qDto) {
        try{
            Compilation_topicPO compilation_topicPO=new Compilation_topicPO();
            AOSUtils.copyProperties(qDto,compilation_topicPO);
            compilation_topicMapper.updateByKey(compilation_topicPO);
            //目录树添加专题tree，并赋予byadmin权限
            updatetreename(qDto,"0.024.008",compilation_topicPO.getId_());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改目录树名称
     * @param qDto
     * @param cascade_id_
     * @param module_id
     */
    private void updatetreename(Dto qDto, String cascade_id_,String module_id) {
        //1.得到管理员账号的id值
        Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
        String tablename=qDto.getString("sjkmc");
        aos_sys_module.setId_(module_id);
        aos_sys_module.setName_(qDto.getString("topic_name"));
        aos_sys_moduleMapper.updateByKey(aos_sys_module);
    }

    //创建目录树
    private void addtree(Dto qDto, String cascade_id_,String module_id) {
        //1.得到管理员账号的id值
        Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
        String tablename=qDto.getString("sjkmc");
        aos_sys_module.setId_(module_id);
        //2.得到当前cascade的id值一些信息
        String parent_id_="";
        String parent_name="";
        List<Map<String, Object>> list = getModule(cascade_id_);
        if (list != null && list.size() > 0) {
            parent_id_=(String)list.get(0).get("id_");
            parent_name=(String)list.get(0).get("Name_");
        }
        int i=0;
        //3.查询当前节点的子节点个数
        i=getCountModele_child(parent_id_);
        if(i<9){
            aos_sys_module.setCascade_id_(cascade_id_+".00"+(i+1));
        }else if(i>=9&&i<99){
            aos_sys_module.setCascade_id_(cascade_id_+".0"+(i+1));
        }else if(i>=99){
            aos_sys_module.setCascade_id_(cascade_id_+"."+(i+1));
        }
        aos_sys_module.setName_(qDto.getString("topic_name"));
        aos_sys_module.setUrl_("compilation/examine/initData.jhtml");
        aos_sys_module.setParent_id_(parent_id_);
        aos_sys_module.setIs_auto_expand_("0");
        aos_sys_module.setStatus_("1");
        aos_sys_module.setParent_name_(parent_name);
        aos_sys_module.setSort_no_(i+1);
        aos_sys_module.setTablename(qDto.getString("topic_tablename"));
        aos_sys_moduleMapper.insert(aos_sys_module);
        //父几点变成不是叶子节点
        Aos_sys_modulePO aos_sys_module_parent=new Aos_sys_modulePO();
        aos_sys_module_parent.setId_(parent_id_);
        aos_sys_module_parent.setIs_leaf_("0");
        aos_sys_moduleMapper.updateByKey(aos_sys_module_parent);
        //同时为当前用户添加经办权限
        addpower_handle(aos_sys_module,qDto,"档案编研");
        //同时为当前用户添加管理权限
        addpower_manage(aos_sys_module,qDto,"档案编研");
    }

    //查询子节点个数
    private int getCountModele_child(String parent_id_) {
        String sql="select * from aos_sys_module where parent_id_ ='"+parent_id_+"'";
        return jdbcTemplate.queryForList(sql).size();
    }
    private List<Map<String, Object>> getModule(String cascade_id_) {
        String sql="select * from aos_sys_module where cascade_id_='"+cascade_id_+"'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String, Object>> getModule_byid_(String cascade_id_) {
        String sql="select * from aos_sys_module where id_='"+cascade_id_+"'";
        return jdbcTemplate.queryForList(sql);
    }
    //经办权限
    private void addpower_handle(Aos_sys_modulePO aos_sys_module,Dto out,String byname) {
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        //此时如果是档案编研授权操作，需要把编研管理员和分管进行授权
        if("档案编研".equals(byname)){
            //String sql="select * from aos_sys_user where compilation_flag_='编研管理员' or compilation_flag_='编研分管'";

            String sql="select * from aos_sys_user where account_='"+out.getUserInfo().getAccount_()+"'";
            list=jdbcTemplate.queryForList(sql);
            if(list!=null&&list.size()>0){
                for(int i=0;i<list.size();i++){
                    String rootid=getUserId("root");
                    UserInfoVO userInfo=out.getUserInfo();
                    Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
                    aos_sys_module_userPO.setId_(AOSId.uuid());
                    aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
                    aos_sys_module_userPO.setUser_id_((String)list.get(i).get("id_"));
                    aos_sys_module_userPO.setGrant_type_("1");
                    aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
                    aos_sys_module_userPO.setOperator_id_(rootid);
                    aos_sys_module_userMapper.insert(aos_sys_module_userPO);
                }
            }
        }else {
            String rootid = getUserId("root");
            UserInfoVO userInfo = out.getUserInfo();
            Aos_sys_module_userPO aos_sys_module_userPO = new Aos_sys_module_userPO();
            aos_sys_module_userPO.setId_(AOSId.uuid());
            aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
            aos_sys_module_userPO.setUser_id_(userInfo.getId_());
            aos_sys_module_userPO.setGrant_type_("1");
            aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
            aos_sys_module_userPO.setOperator_id_(rootid);
            aos_sys_module_userMapper.insert(aos_sys_module_userPO);
        }
    }
    //管理权限
    private void addpower_manage(Aos_sys_modulePO aos_sys_module,Dto out,String byname) {
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        //此时如果是档案编研授权操作，需要把编研管理员和分管进行授权
        if("档案编研".equals(byname)){
            String sql="select * from aos_sys_user where account_='"+out.getUserInfo().getAccount_()+"'";
            list=jdbcTemplate.queryForList(sql);
            if(list!=null&&list.size()>0){
                for(int i=0;i<list.size();i++){
                    String rootid=getUserId("root");
                    UserInfoVO userInfo=out.getUserInfo();
                    Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
                    aos_sys_module_userPO.setId_(AOSId.uuid());
                    aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
                    aos_sys_module_userPO.setUser_id_((String)list.get(i).get("id_"));
                    aos_sys_module_userPO.setGrant_type_("2");
                    aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
                    aos_sys_module_userPO.setOperator_id_(rootid);
                    aos_sys_module_userMapper.insert(aos_sys_module_userPO);
                }
            }
        }else {
            String rootid = getUserId("root");
            UserInfoVO userInfo = out.getUserInfo();
            Aos_sys_module_userPO aos_sys_module_userPO = new Aos_sys_module_userPO();
            aos_sys_module_userPO.setId_(AOSId.uuid());
            aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
            aos_sys_module_userPO.setUser_id_(userInfo.getId_());
            aos_sys_module_userPO.setGrant_type_("2");
            aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
            aos_sys_module_userPO.setOperator_id_(rootid);
            aos_sys_module_userMapper.insert(aos_sys_module_userPO);
        }
    }
    //得到用户的id值
    private String getUserId(String root) {
        String id_="";
        String sql="select * from aos_sys_user where account_='"+root+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            id_=(String)list.get(0).get("id_");
        }
        return id_;
    }
    private void addTopic_tablenameid(Dto qDto,String id_) throws SQLException {
        String ids=qDto.getString("ids");
        String tablename=qDto.getString("sjkmc");
        if(ids!=null&&ids!=""){
            String[] id=ids.split(",");
            Statement state = jdbcTemplate.getDataSource().getConnection().createStatement();
            for(int i=0;i<id.length;i++){
                Archive_topic_tablenameidPO archive_topic_tablenameidPO=new Archive_topic_tablenameidPO();
                archive_topic_tablenameidPO.setId_(AOSId.uuid());
                archive_topic_tablenameidPO.setPid(id_);
                archive_topic_tablenameidPO.setTablename(tablename);
                archive_topic_tablenameidPO.setTablename_id(id[i]);
                archive_topic_tablenameidMapper.insert(archive_topic_tablenameidPO);
               // String sql="insert into archive_topic_tablenameid (id_,pid,tablename,tablename_id ) value('"+AOSId.uuid()+"','"+id_+"','"+tablename+"','"+id[i]+"')";
               // state.addBatch(sql);
            }
           // state.executeBatch();
        }
    }
    public List<Compilation_topicPO> listtopic(Dto qDto) {
       return compilation_topicMapper.listPage(qDto);
    }

    public boolean addtopic_edit(Dto qDto) {
        try{
            Compilation_topicPO compilation_topicPO=new Compilation_topicPO();
            AOSUtils.copyProperties(qDto,compilation_topicPO);
            compilation_topicMapper.updateByKey(compilation_topicPO);
            //此时添加到数据compolitation_rw中
            //此时修改对应的目录树名称(待添加)


            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
            return query.toString();
        } else
            return "";
    }
    public String queryConditions2_noid(Dto inDto) {
        String tablename=inDto.getString("tablename");
        /*List<Map<String, Object>> listmodule=getModule_byid_(inDto.getString("id_"));
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }*/
        String id_=inDto.getString("id_");
        String query_ids="";
        String sql="select tablename_id from archive_topic_tablenameid where pid='"+id_+"' and tablename='"+tablename+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        String ids="";
        if(list!=null&&list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    ids = "'" + (String) list.get(i).get("tablename_id") + "'";
                } else {
                    ids = ids + ",'" + (String) list.get(i).get("tablename_id") + "'";
                }
            }
            query_ids=" and id_ not in ("+ids+")";
        }
        return query_ids;
    }
    public List<Map<String, Object>> listArchive(Dto qDto) {
        // TODO Auto-generated method stub
        String tablename=qDto.getString("tablename");
        String query=qDto.getString("query");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from "+tablename+" where 1=1 "+query+" order by id_ offset "+pageStart+" rows fetch next "+limit+" rows only";
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;

    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageAll_archive(Dto qDto) {
        //String query = queryConditions2(qDto);
        String query= qDto.getString("query");
        String tablename=qDto.getString("tablename");
        //查询年度条件是否存在
        String sql = "SELECT COUNT(*) FROM " +tablename +" where 1=1 "+query;
        return jdbcTemplate.queryForInt(sql);
    }

    public boolean updatetopic_data(Dto inDto) {
        try{
            String tablename=inDto.getString("tablename");
            String ids=inDto.getString("ids");
            if(ids!=null&&ids!=""){
                String[] id=ids.split(",");
                for(int i=0;i<id.length;i++){
                    Archive_topic_tablenameidPO archive_topic_tablenameidPO=new Archive_topic_tablenameidPO();
                    archive_topic_tablenameidPO.setId_(AOSId.uuid());
                    archive_topic_tablenameidPO.setPid(inDto.getString("id_"));
                    archive_topic_tablenameidPO.setTablename(tablename);
                    archive_topic_tablenameidPO.setTablename_id(id[i]);
                    archive_topic_tablenameidMapper.insert(archive_topic_tablenameidPO);
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
     * 删除条目
     *
     * @param qDto
     * @return
     */
    public Dto deletetopic_data(Dto dto) {
        Dto outDto = Dtos.newDto();
        String tablename="";
        List<Map<String, Object>> listmodule=getModule_byid_(dto.getString("pid"));
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }
        String pid=dto.getString("pid");
        String[] selections = dto.getRows();
        int del = 0;
        for (String id_ : selections) {

            jdbcTemplate.execute(" delete from archive_topic_tablenameid where pid='"
                    + pid + "' and tablename_id='"+id_+"'");
            del++;
        }
        String msg = "操作完成，";
        if (del > 0) {
            msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
        }
        outDto.setAppMsg(msg);
        return outDto;

    }

    public List<Archive_tablefieldlistPO> getComboboxList(Dto inDto) {
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAll(inDto.getString("tablename"));
        return list;
    }
}
