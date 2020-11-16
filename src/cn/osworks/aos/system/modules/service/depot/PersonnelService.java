package cn.osworks.aos.system.modules.service.depot;

import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class PersonnelService  extends JdbcDaoSupport {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Depot_ckMapper depot_ckMapper;
    @Autowired
    private Depot_rkMapper depot_rkMapper;
    @Autowired
    private Depot_hjMapper depot_hjMapper;
    @Autowired
    private Depot_aqMapper depot_aqMapper;
    @Autowired
    private Depot_ryMapper depot_ryMapper;
    @Autowired
    private Depot_sbMapper depot_sbMapper;
    @Autowired
    private Depot_pdMapper depot_pdMapper;
    @Autowired
    private WsdMapper wsdMapper;
    @Autowired
    private CrkMapper crkMapper;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
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
                        query.append(" and " + qDto.getString("filedname" + i));
                    } else {
                        query.append(" or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("<>")) {
                        query.append(" <> '" + qDto.getString("content" + i)
                                + "'");
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
    public List<Map<String,Object>> getDataFieldListDisplayAll(Dto qDto) {
        String query = queryConditions2(qDto);
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from depot_ry where 1=1 and lx='"+qDto.getString("lx")+"'"+query+" order by f_logdatetime desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }

    public int getPageTotal(Dto qDto) {
        String query = queryConditions2(qDto);
        String sql="select * from depot_ry where 1=1 "+query;
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list.size();
    }
    //****************************************************
    public String getDepotRyIndex_nd(String lx,String rkdh){
        String nd="";
        if(rkdh.length()==10&&rkdh!=null){
            nd=rkdh.substring(3,rkdh.length()-3);
        }
        String sql="select  lsh  from depot_ry where lsh like '%"+lx+"%' and lsh like '%"+nd+"%' order by lsh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("lsh");
            String index = dh.substring(dh.length()-3, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%3d", h).replace(" ", "0");
        }else {
            return "001";
        }
    }
}
