package cn.osworks.aos.system.modules.service.notification;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Aos_sys_notificationMapper;
import cn.osworks.aos.system.dao.mapper.Archive_checkup_rwMapper;
import cn.osworks.aos.system.dao.mapper.Archive_compilation_rwMapper;
import cn.osworks.aos.system.dao.po.Aos_sys_notificationPO;
import cn.osworks.aos.system.dao.po.Archive_checkup_rwPO;
import cn.osworks.aos.system.dao.po.Archive_compilation_rwPO;
import cn.osworks.aos.system.modules.controller.websocket.WSMessageService;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService extends JdbcDaoSupport {
    @Autowired
    private Archive_checkup_rwMapper archive_checkup_rwMapper;
    @Autowired
    private Archive_compilation_rwMapper archive_compilation_rwMapper;
    @Autowired
    private Aos_sys_notificationMapper aos_sys_notificationMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    @Autowired
    private WSMessageService wsMessageService;
    public Dto getWait(Dto qDto){
        Dto outDto = Dtos.newDto();
        UserInfoVO userInfoVO = qDto.getUserInfo();
        qDto.put("sfck","0");
        qDto.put("assignment",userInfoVO.getAccount_());
        List<Aos_sys_notificationPO> list=aos_sys_notificationMapper.like(qDto);
        //int rows=aos_sys_notificationMapper.rows(qDto);
        outDto.put("rows",list.size());
        return  outDto;
    }

    /**
     *
     * 利用提交
     * @param inDto
     * @param content
     * @param person
     */
    public void SubmitLy(Dto inDto, String content, String person){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        UserInfoVO userInfoVO=inDto.getUserInfo();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc(content);
        aos_sys_notificationPO.setRwurl("utilization/initLeader.jhtml");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(person);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToGroupAllTerminal(person,"您有新的消息！！！");
    }
    /**
     *
     * 利用提交
     * @param inDto
     */
    public void SubmitLy_CK(Dto inDto){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        UserInfoVO userInfoVO=inDto.getUserInfo();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("实体出库");
        aos_sys_notificationPO.setRwurl("depot/depotOut/initDepotLy.jhtml");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("qhy,wzw,hgy,gy,zzl,root");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToGroupAllTerminal("qhy,wzw,hgy,gy,zzl,root","您有新的消息！！！");
    }
    /**
     *
     * 临时库接收提交
     * @param inDto
     */
    public void SubmitLinJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("临时库接收提交");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/inittemporaryData.jhtml?nd="+inDto.getString("nd")+"&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //此时一共有4个审核人，分辨是 王兆伟  赵振宇  姜文灿  王欣
        aos_sys_notificationPO.setAssignment("wzw,zzy,jwc,wx");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToGroupAllTerminal("wzw,zzy,jwc,wx","您有新的消息！！！");
    }
    /**
     *
     * 资料征集提交
     * @param inDto
     */
    public void SubmitZLZJ(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        String lx=inDto.getString("lx");
        String zjnd=inDto.getString("zjnd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("资料征集提交");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initziliaoData.jhtml?zjnd="+zjnd+"&lx="+lx+"&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //此时一共有4个审核人，分辨是 王兆伟  赵振宇  姜文灿  王欣
        aos_sys_notificationPO.setAssignment("wzw,zzy,jwc,wx");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToGroupAllTerminal("wzw,zzy,jwc,wx","您有新的消息！！！");
    }
    /**
     *
     * 资源接收提交
     * @param inDto
     */
    public void SubmitZYJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        String lx=inDto.getString("lx");
        String jsnd=inDto.getString("jsnd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("资源接收提交");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initentityData.jhtml?nd="+jsnd+"&lx="+lx+"&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //此时一共有4个审核人，分辨是 王兆伟  赵振宇  姜文灿  王欣
        aos_sys_notificationPO.setAssignment("wzw,zzy,jwc,wx");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToGroupAllTerminal("wzw,zzy,jwc,wx","您有新的消息！！！");
    }
    /**
     *
     * 资料征集提交
     * @param inDto
     */
    public void SubmitZiLiaoJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("资料征集提交");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initziliaoData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("szadmin");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
    }
    /**
     *
     * 资源接收提交
     * @param inDto
     */
    public void SubmitZiYuanJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("资源接收提交");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initziyuanData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("szadmin");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
    }
    /**
     *
     * 档案征集提交
     * @param inDto
     */
    public void SubmitZhengJiJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("档案征集提交");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initzhengjiData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("szadmin");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
    }
    /**
     *
     * 实体接收提交
     * @param inDto
     */
    public void SubmitShiTinJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("实体接收提交");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initshitiData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("szadmin");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
    }
    /**
     *
     * 管理库转总库提交
     * @param inDto
     */
    public void SubmitManageToTotal(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("管理库转总库提交");
        aos_sys_notificationPO.setRwurl("archive/datatotal/initData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("szadmin");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
    }
    /**
     *
     * 总库转Excel提交
     * @param inDto
     */
    public void SubmitTotaltoExcel(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("总库转出提交");
        aos_sys_notificationPO.setRwurl("archive/datatotal/initData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("szadmin");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
    }
    /**
     *
     * 鉴定初鉴提醒
     * @param inDto
     */
    public void SubmitJdFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("鉴定初鉴提醒");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?first=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(inDto.getString("first_fieldennames"));
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(inDto.getString("first_fieldennames"),"您有新的消息！！！");
    }
    /**
     *
     * 开放鉴定初鉴提交
     * @param inDto
     */
    public void SubmitKFFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("开放鉴定初鉴提交");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?next=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getNext_compilationperson());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getNext_compilationperson(),"您有新的消息！！！");
    }



    /**
     *
     * 开放鉴定再审提交(再审同意)
     * @param inDto
     */
    public void SubmitKFNext(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("开放鉴定再审提交");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?last=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getLast_compilationperson());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getLast_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     * 保管期限鉴定初鉴提交
     * @param inDto
     */
    public void SubmitBGQXFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("保管期限鉴定初鉴提交");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?next=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getNext_compilationperson());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getNext_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     * 保管期限鉴定再审提交(再审同意)
     * @param inDto
     */
    public void SubmitBGQXNext(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("保管期限鉴定再审提交");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?last=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getLast_compilationperson());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getLast_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     * 密级鉴定初鉴提交
     * @param inDto
     */
    public void SubmitMJFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("密级鉴定初鉴提交");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?next=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");

        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getNext_compilationperson());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getNext_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     * 密级鉴定再审提交(再审同意)
     * @param inDto
     */
    public void SubmitMJNext(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("密级鉴定再审提交");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?last=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getLast_compilationperson());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getLast_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     * 销毁鉴定初鉴提交
     * @param inDto
     */
    public void SubmitXHFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("销毁鉴定初鉴提交");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?next=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getNext_compilationperson());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getNext_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     * 销毁鉴定再审提交(再审同意)
     * @param inDto
     */
    public void SubmitXHNext(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("销毁鉴定再审提交");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?last=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getLast_compilationperson());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getLast_compilationperson(),"您有新的消息！！！");
    }












    /**
     *
     * 撰稿提交
     * @param inDto
     */
    public void SubmitZhuanGao(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("撰稿提交");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?second=1&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //此时到合稿人了
        //根据rw_id_得到任务的详细信息
        Archive_compilation_rwPO archive_compilation_rwPO=getByRwMessage(inDto.getString("rw_id_"));
        aos_sys_notificationPO.setAssignment(archive_compilation_rwPO.getHgr());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_compilation_rwPO.getHgr(),"您有新的消息！！！");

    }
    /**
     *
     * 合稿提交（合稿提交）
     * @param inDto
     */
    public void SubmitHeGao(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("合稿提交");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?three=1&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //根据rw_id_得到任务的详细信息
        Archive_compilation_rwPO archive_compilation_rwPO=getByRwMessage(inDto.getString("rw_id_"));
        aos_sys_notificationPO.setAssignment(archive_compilation_rwPO.getJdr());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_compilation_rwPO.getJdr(),"您有新的消息！！！");
    }
    /**
     *
     * 校对同意（校对同意）
     * @param inDto
     */
    public void SubmitJiaoDui(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("校对同意");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?four=1&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //根据rw_id_得到任务的详细信息
        Archive_compilation_rwPO archive_compilation_rwPO=getByRwMessage(inDto.getString("rw_id_"));
        aos_sys_notificationPO.setAssignment(archive_compilation_rwPO.getZbj());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_compilation_rwPO.getZbj(),"您有新的消息！！！");
    }

    /**
     *
     * 校对审核不同意（校对审核不同意）
     * @param inDto
     */
    public void TaskJiaoDui(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("校对审核不同意");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?second=1&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //根据rw_id_得到任务的详细信息
        Archive_compilation_rwPO archive_compilation_rwPO=getByRwMessage(inDto.getString("rw_id_"));
        aos_sys_notificationPO.setAssignment(archive_compilation_rwPO.getHgr());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_compilation_rwPO.getHgr(),"您有新的消息！！！");
    }
    /**
     *
     * 总编辑审核同意（总编辑审核同意）
     * @param inDto
     */
    public void SubmitZongBianJian(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("总编辑审核同意");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("byadmin");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
    }
    /**
     *
     * 总编辑审核不同意（总编辑审核不同意）
     * @param inDto
     */
    public void TaskZongBianJian(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("总编辑审核不同意");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?three=1&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        //根据rw_id_得到任务的详细信息
        Archive_compilation_rwPO archive_compilation_rwPO=getByRwMessage(inDto.getString("rw_id_"));
        aos_sys_notificationPO.setAssignment(archive_compilation_rwPO.getHgr());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_compilation_rwPO.getJdr(),"您有新的消息！！！");
    }
    /**
     *
     * 编研任务提交（编研任务提交）
     * @param inDto
     */
    public void SubmitBianYian(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("编研任务提交");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(userInfoVO.getAccount_());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(userInfoVO.getAccount_(),"您有新的消息！！！");
    }
    /**
     *
     * 编研任务审核（编研任务审核）
     * @param inDto
     */
    public void TaskBianYian(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("编研任务审核同意");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(userInfoVO.getAccount_());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(userInfoVO.getAccount_(),"您有新的消息！！！");
    }
    /**
     *
     * 编研任务审核不同意（编研任务审核不同意）
     * @param inDto
     */
    public void TaskBianYianNo(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("编研任务审核不同意");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(userInfoVO.getAccount_());
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(userInfoVO.getAccount_(),"您有新的消息！！！");
    }




















    /**
     *
     *临时库接收审核
     *
     * @param inDto
     */
    public void TaskLinJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("临时库接收审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(inDto.getString("user"));
        aos_sys_notificationPO.setRwurl("archive/datatemporary/inittemporaryData.jhtml?nd="+inDto.getString("nd")+"&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(inDto.getString("user"),"您有新的消息！！！");
    }
    /**
     *
     *临时库接收审核
     *
     * @param inDto
     */
    public void TaskZLZJSH(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        String lx=inDto.getString("lx");
        String zjnd=inDto.getString("zjnd");
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("资料征集审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(inDto.getString("user"));
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initziliaoData.jhtml?zjnd="+zjnd+"&lx="+lx+"&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(inDto.getString("user"),"您有新的消息！！！");
    }
    /**
     *
     *临时库接收审核
     *
     * @param inDto
     */
    public void TaskZYJSSH(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        String lx=inDto.getString("lx");
        String jsnd=inDto.getString("jsnd");
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("资源接收审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(inDto.getString("user"));
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initentityData.jhtml?nd="+jsnd+"&lx="+lx+"&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(inDto.getString("user"),"您有新的消息！！！");
    }
    /**
     *
     *资料征集审核
     *
     * @param inDto
     */
    public void TaskZiLiaoJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("资料征集审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("ky");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initziliaoData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     *资源接收审核
     *
     * @param inDto
     */
    public void TaskZiYuanJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("资源接收审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("ky");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initziyuanData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     *档案征集审核
     *
     * @param inDto
     */
    public void TaskZhengJiJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("档案征集审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("ky");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initzhengjiData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     *实体接收审核
     *
     * @param inDto
     */
    public void TaskShiTiJS(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("实体接收审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("ky");
        aos_sys_notificationPO.setRwurl("archive/datatemporary/initshitiData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     *利用审核
     *
     * @param inDto
     */
    public void TaskLy(Dto inDto){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        UserInfoVO userInfoVO = inDto.getUserInfo();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("接待查档审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(inDto.getString("users"));
        aos_sys_notificationPO.setRwurl("make/initMake.jhtml");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(inDto.getString("users"),"您有新的消息！！！");
    }
    /**
     *
     *利用自助审核
     *
     * @param inDto
     */
    public void TaskZizhuLy(Dto inDto){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        UserInfoVO userInfoVO = inDto.getUserInfo();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjren(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("自助查档审核看电子文件");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("jm,hgc");
        aos_sys_notificationPO.setRwurl("utilization/data/initzizhuLeader.jhtml");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToGroupAllTerminal("jm,hgc","您有新的消息！！！查档用户["+userInfoVO.getName_()+"]申请查看电子文件！！！");
    }
    /**
     *
     *利用自助审核
     *
     * @param inDto
     */
    public void WanZizhuLy(Dto inDto){
        UserInfoVO userInfoVO = inDto.getUserInfo();
        wsMessageService.sendToAllTerminal(userInfoVO.getName_(),"您有新的消息！！！该电子文件"+inDto.getString("message")+"！！！");
    }
    /**
     *
     *管理库转总库审核
     *
     * @param inDto
     */
    public void TaskManageToTotalSH(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("管理库转总库审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("ky");
        aos_sys_notificationPO.setRwurl("archive/datatotal/initData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     *开放鉴定审核（再审不同意）
     *
     * @param inDto
     */
    public void TaskKFFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("开放鉴定审核不同意");
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getFirst_compilationperson());
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?first=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getFirst_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     *开放鉴定终审
     *
     * @param inDto
     */
    public void TaskKFLast(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("开放鉴定终审不同意");
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getNext_compilationperson());
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?next=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getNext_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     *开放鉴定终审同意
     *
     * @param inDto
     */
    public void TaskKFLastYes(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("开放鉴定终审同意");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("dzadmin");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);

    }
    /**
     *
     *保管期限鉴定审核
     *
     * @param inDto
     */
    public void TaskBgqxFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("保管期限鉴定审核不同意");
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getFirst_compilationperson());
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?first=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getFirst_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     *保管期限鉴定终审
     *
     * @param inDto
     */
    public void TaskBgqxLast(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("保管期限鉴定终审不同意");
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getNext_compilationperson());
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?next=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getNext_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     *保管期限鉴定终审同意
     *
     * @param inDto
     */
    public void TaskBgqxLastYes(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("保管期限鉴定终审同意");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("dzadmin");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     *密级鉴定审核
     *
     * @param inDto
     */
    public void TaskMjFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("密级鉴定审核不同意");
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getFirst_compilationperson());
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?first=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getFirst_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     *密级鉴定终审
     *
     * @param inDto
     */
    public void TaskMjLast(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("密级鉴定终审不同意");
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getNext_compilationperson());
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?next=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getNext_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     *密级鉴定终审同意
     *
     * @param inDto
     */
    public void TaskMjLastYes(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("密级鉴定终审同意");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("dzadmin");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     *销毁鉴定审核
     *
     * @param inDto
     */
    public void TaskXHFirst(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("销毁鉴定审核不同意");
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getFirst_compilationperson());
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?first=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getFirst_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     *销毁鉴定终审
     *
     * @param inDto
     */
    public void TaskXHLast(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("销毁鉴定终审不同意");
        aos_sys_notificationPO.setSfck("0");
        //得到动态复核人
        Archive_checkup_rwPO archive_checkup_rwPO=getCheckedMessage(inDto.getString("check_id_"));
        aos_sys_notificationPO.setAssignment(archive_checkup_rwPO.getNext_compilationperson());
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?next=1&tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(archive_checkup_rwPO.getNext_compilationperson(),"您有新的消息！！！");
    }
    /**
     *
     *销毁鉴定终审同意
     *
     * @param inDto
     */
    public void TaskXHLastYes(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("销毁鉴定终审同意");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("dzadmin");
        aos_sys_notificationPO.setRwurl("archive/checkup/initData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     *总库转出Excel审核
     *
     * @param inDto
     */
    public void TaskTotalToExcelSH(Dto inDto,UserInfoVO userInfoVO){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setShren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setShrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setShsj(strtime);
        aos_sys_notificationPO.setRwmc("总库转出Excel审核");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("ky");
        aos_sys_notificationPO.setRwurl("archive/datatotal/initData.jhtml?tablename="+tablename+"&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.updateByFormid(aos_sys_notificationPO);
    }
    /**
     *
     * 更新是否示读
     *
     * @param inDto
     * @return
     */
    public Dto updateNotification(Dto inDto){
        Dto outDto = Dtos.newDto();
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        AOSUtils.copyProperties(inDto,aos_sys_notificationPO);
        aos_sys_notificationMapper.updateByKey(aos_sys_notificationPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }
    /**
     * 根据id得到鉴定任务详细信息
     * @param check_id_
     * @return
     */
    private Archive_checkup_rwPO getCheckedMessage(String check_id_) {
        return archive_checkup_rwMapper.selectByKey(check_id_);
    }

    /**
     * 给撰稿用户一个提醒
     * @param qDto
     * @param userInfoVO
     */
    public void SubmitByFirst(Dto inDto, UserInfoVO userInfoVO) {
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        String aos_module_id_=inDto.getString("aos_module_id_");
        String tablename=inDto.getString("tablename");
        String cascode_id_=inDto.getString("cascode_id_");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjren(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("撰稿人提醒");
        aos_sys_notificationPO.setRwurl("compilation/articles/subject.jhtml?first=1&aos_module_id_="+aos_module_id_+"&cascode_id_="+cascode_id_);
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment(inDto.getString("zgr"));
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToAllTerminal(inDto.getString("zgr"),"您有新的消息！！！");
    }
    public Archive_compilation_rwPO getByRwMessage(String rw_id_){
        return archive_compilation_rwMapper.selectByKey(rw_id_);
    }

    public void wanZizhu_printLy(Dto inDto) {
        UserInfoVO userInfoVO = inDto.getUserInfo();
        wsMessageService.sendToGroupAllTerminal("jm,hgc","用户["+userInfoVO.getName_()+"]请求打印");

    }

    public List<Map<String, Object>> listFinish(Dto qDto) {
        UserInfoVO userInfoVO = qDto.getUserInfo();
        qDto.put("sfck","1");
        qDto.put("assignment",userInfoVO.getAccount_());
        String sql="";
        if(qDto.getUserInfo().getAccount_().equals("root")){
            sql=" select * from aos_sys_notification where sfck='1' order by tjsj desc";
        }else{
            sql="select * from aos_sys_notification where sfck='1' and shren='"+ qDto.getUserInfo().getAccount_()+"'  order by tjsj desc";
        }
        List<Map<String, Object>> list =jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> listWait(Dto qDto) {
        UserInfoVO userInfoVO = qDto.getUserInfo();
        qDto.put("sfck","0");
        qDto.put("assignment",userInfoVO.getAccount_());
        String sql="";
        if(qDto.getUserInfo().getAccount_().equals("root")){
            sql=" select * from aos_sys_notification where sfck='0' order by tjsj desc";
        }else{
            sql="select * from aos_sys_notification where sfck='0' and shren='"+ qDto.getUserInfo().getAccount_()+"'  order by tjsj desc";
        }
        List<Map<String, Object>> list =jdbcTemplate.queryForList(sql);
        return list;
    }
    /**
     *
     *利用触摸屏
     *
     * @param inDto
     */
    public void TaskChuMoLy(Dto inDto){
        Aos_sys_notificationPO aos_sys_notificationPO=new Aos_sys_notificationPO();
        UserInfoVO userInfoVO = inDto.getUserInfo();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtime = df.format(new Date());
        aos_sys_notificationPO.setId_(AOSId.uuid());
        aos_sys_notificationPO.setTjrcn(userInfoVO.getAccount_());
        aos_sys_notificationPO.setTjren(userInfoVO.getName_());
        aos_sys_notificationPO.setTjsj(strtime);
        aos_sys_notificationPO.setRwmc("触摸屏查档申请");
        aos_sys_notificationPO.setSfck("0");
        aos_sys_notificationPO.setAssignment("zc,hgc,root");
        aos_sys_notificationPO.setRwurl("make/initLeader.jhtml");
        aos_sys_notificationPO.setFormid(inDto.getString("id_"));
        aos_sys_notificationMapper.insert(aos_sys_notificationPO);
        wsMessageService.sendToGroupAllTerminal("zc,hgc,root","您有新的消息！！！查档用户["+userInfoVO.getName_()+"]申请查看电子文件！！！");
    }
}
