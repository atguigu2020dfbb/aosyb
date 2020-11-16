package cn.osworks.aos.system.modules.service.depot;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.mapper.Depot_czMapper;
import cn.osworks.aos.system.dao.mapper.Depot_cz_logMapper;
import cn.osworks.aos.system.dao.po.Depot_czPO;
import cn.osworks.aos.system.dao.po.Depot_cz_logPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class GraphService extends JdbcDaoSupport {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    @Autowired
    private Depot_czMapper depot_czMapper;
    @Autowired
    private Depot_cz_logMapper depot_cz_logMapper;

    public List<LinkedHashMap<String,Object>>  getlocation(Dto outDto){
        List<LinkedHashMap<String,Object>> list=new  LinkedList<LinkedHashMap<String,Object>>();
        try{

            String archive_storehouse=outDto.getString("archive_storehouse");
            String column_number=outDto.getString("column_number");
            String group_number=outDto.getString("group_number");
            int k=1;
            for(int i=1;i<=6;i++){
                for(int j=1;j<=8;j++){
                    if(j<=4){
                        String sql="select * from depot_cz where cz_liehao='"+column_number+"' and cz_zuhao='"+group_number+"' and archive_storehouse='"+archive_storehouse+"' and cz_mian='A' and cz_jiehao='"+j+"' and cz_gehao='"+i+"'";
                        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql);
                        if(list2!=null&&list2.size()>0){
                            LinkedHashMap<String,Object> map=new LinkedHashMap<String,Object>();
                            map.put("cz_name",list2.get(0).get("cz_name"));
                            list.add(map);
                        }else{
                            LinkedHashMap<String,Object> map=new LinkedHashMap<String,Object>();
                            map.put("cz_name","");
                            list.add(map);
                        }
                    }else{
                        String sql="select * from depot_cz where cz_liehao='"+column_number+"' and cz_zuhao='"+group_number+"' and archive_storehouse='"+archive_storehouse+"' and cz_mian='B' and cz_jiehao='"+j+"' and cz_gehao='"+i+"'";
                        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql);
                        if(list2!=null&&list2.size()>0){
                            LinkedHashMap<String,Object> map=new LinkedHashMap<String,Object>();
                            map.put("cz_name",list2.get(0).get("cz_name"));
                            list.add(map);
                        }else{
                            LinkedHashMap<String,Object> map=new LinkedHashMap<String,Object>();
                            map.put("cz_name","");
                            list.add(map);
                        }
                    }
                }
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
            return list;
        }
    }
}