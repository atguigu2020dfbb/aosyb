package cn.osworks.aos.system.modules.service.archive;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class Data_backService  extends JdbcDaoSupport {
    @Autowired
    private Archive_tablenameMapper archive_tablenameMapper;
    @Autowired
    private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
    @Autowired
    private Aos_sys_moduleMapper aos_sys_moduleMapper;
    @Autowired
    private Aos_sys_userMapper aos_sys_userMapper;
    @Autowired
    private Archive_zdyjMapper archive_zdyjMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Dto> getDataFieldListDisplayAll(Dto qDto, String username) {
        String query = queryConditions2(qDto);
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 将条件存入到一个数据表中，方便与缓存查询
        String selectmark = qDto.getString("selectmark");
        if (selectmark.equals("1")) {
            // 存入缓存表中
            addSearchData(qDto, username);
        }
        String sql = "";
        String term = "";
        String fieldName;
        String enfield = "id_";
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAll(qDto.getString("tablename"));
        String orderenfield = "";
        for (int i = 0; i < list.size(); i++) {
            fieldName = list.get(i).getFieldenname();
            enfield = enfield + "," + fieldName;
            if (i == 3) {
                orderenfield = enfield;
            }
        }
        orderenfield = orderenfield.substring(4);
        // 到这一步进行全宗筛选
		/*Map<String, Object> map = findGeneral(username);
		if (map != null && map.size() > 0) {
			String qzh = (String) map.get("generalnumber");
			term = " and qzh='" + qzh + "'";
		} else {
			term = term;
		}*/
        // 排列条件传递过去
        String orderBy = getOrderBy(qDto, username);
        if (orderenfield != null) {
            if (orderBy != null){
                orderBy = "( ORDER BY "+orderBy+")";
            }else{
                orderBy = "( ORDER BY "+orderenfield+")";
            }

        } else {
            orderBy = "( ORDER BY "+orderenfield+")";
        }
        if (qDto.getString("page") != null && qDto.getString("page") != "") {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
                    //+ orderenfield
                    + orderBy
                    + " AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")+"_backup"
                    + " WHERE 1=1 "
                    + qDto.getString("queryclass")
                    + query
                    + term
                    + ") "
                    + "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                    + (limit * (page - 1) + 1)
                    + " AND "
                    + qDto.getPageLimit()
                    * Integer.valueOf(qDto.getString("page"))
                    + " ORDER BY aos_rn_";
        } else {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
                    //+ orderenfield
                    + orderBy
                    + " AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")+"_backup"
                    + " WHERE 1=1 "
                    + qDto.getString("queryclass")
                    + query
                    + term
                    + ") "
                    + "SELECT * FROM aos_query_  ORDER BY aos_rn_";
        }
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     * 存入查询缓存
     *
     * @author PX
     * @param qDto
     * @param username2
     *
     *            2019-1-14
     */
    private void addSearchData(Dto qDto, String username) {
        // TODO Auto-generated method stub
        // 得到当前时间毫秒
        // 查询用户中文名
        String chinese = getUserChinese(username);
        long nowtime = System.currentTimeMillis();
        SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String bh = dfDateFormat.format(new Date());
        for (int q = 1; q <= 8; q++) {
            String filedname = qDto.getString("filedname" + q);
            String filedcnname = qDto.getString("filedcnname" + q);
            String and = qDto.getString("and" + q);
            String content = qDto.getString("content" + q);
            String condition = qDto.getString("condition" + q);
            String sql = "insert into archive_Reseach_Data(bh,gx,zdmc,nr,tj,tablename,mk,cxr,mk_en) values ('"
                    + bh
                    + "','"
                    + and
                    + "','"
                    + filedname
                    + "','"
                    + content
                    + "','"
                    + condition
                    + "','"
                    + qDto.getString("tablename")
                    + "','"
                    + chinese
                    + "','"
                    + filedcnname
                    + "','"
                    + username
                    + "')";
            // 存入到数据库中
            jdbcTemplate.execute(sql);
        }
    }
    /**
     * 得到中文登录名
     *
     * @author PX
     * @param username2
     *
     *            2019-1-14
     * @return
     */
    private String getUserChinese(String username2) {
        // TODO Auto-generated method stub
        String sql = "select * from aos_sys_user where account_='" + username2
                + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            String name_ = (String) list.get(0).get("name_");
            return name_;
        }
        return null;
    }
    /**
     *
     * 获取排列条件(根据表名和用户名)
     *
     * @author PX
     * @param qDto
     *
     *            2019-1-7
     * @param username
     * @return
     */
    private String getOrderBy(Dto qDto, String username) {
        // TODO Auto-generated method stub
        String sql = "select * from archive_Sort_Data where tablename='"
                + qDto.getString("tablename") + "' and cxr='" + username + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() > 0) {
            return list.get(0).get("nr").toString();
        } else {
            return null;
        }
    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append(" and " + qDto.getString("filedname" + i));
                    }
                    if (qDto.getString("or" + i).equals("on")) {
                        query.append(" or " + qDto.getString("filedname" + i)
                                + " " + qDto.getString("condition" + i));
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
                    if (qDto.getString("condition" + i).equals("<>")) {
                        query.append(" <>'" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" >'" + qDto.getString("content" + i)+ "'");
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" <'" + qDto.getString("content" + i)+ "'");
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <='" + qDto.getString("content" + i)+ "'");
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >='" + qDto.getString("content" + i)+ "'");
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
    /*
     * 查询表名
     */
    public List<Archive_tablenamePO> findTablename() {
        // TODO Auto-generated method stub
        List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
        String sql = "select * from archive_TableName WHERE TableName not like '%_total' and TableName not like '%_temporary' and TableName not like '%_receive'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String tablename = (String) map.get("TableName");
                String tabledesc = (String) map.get("TableDesc");
                Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
                archive_tablenamePO.setTablename(tablename);
                archive_tablenamePO.setTabledesc(tabledesc);
                alist.add(archive_tablenamePO);
            }
        }
        return alist;
    }
    /**
     *
     * 删除条目
     *
     * @param qDto
     * @return
     */
    public Dto deleteData(Dto qDto) {
        Dto outDto = Dtos.newDto();
        String[] selections = qDto.getRows();
        String tablename = qDto.getString("tablename");
        int del = 0;
        for (String id_ : selections) {
            //String sql3="insert into "+qDto.getString("tablename")+" select * from "+qDto.getString("tablename")+"_backup where id_='"+id_+"'";
            //jdbcTemplate.execute(sql3);
            jdbcTemplate.execute(" delete from " + tablename + "_backup where id_='"
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
     *
     * 删除条目
     *
     * @param qDto
     * @return
     */
    public Dto returnData(Dto qDto) {
        Dto outDto = Dtos.newDto();
        String[] selections = qDto.getRows();
        String tablename = qDto.getString("tablename");
        int del = 0;
        for (String id_ : selections) {
            String sql3="insert into "+qDto.getString("tablename")+" select * from "+qDto.getString("tablename")+"_backup where id_='"+id_+"'";
            jdbcTemplate.execute(sql3);
            jdbcTemplate.execute(" delete from " + tablename + "_backup where id_='"
                    + id_ + "'");
            del++;
        }
        String msg = "操作完成，";
        if (del > 0) {
            msg = AOSUtils.merge(msg + "成功还原信息[{0}]个", del);
        }
        outDto.setAppMsg(msg);
        return outDto;

    }
    public Dto deleteAllData(Dto qDto) {
        // TODO Auto-generated method stub
        Dto outDto = Dtos.newDto();
        String tablename = qDto.getString("tablename");
        // 查询条目数
        List<Map<String, Object>> list = jdbcTemplate
                .queryForList("select * from " + tablename + "_backup where 1=1 "
                        + qDto.getString("query"));
        try {
            jdbcTemplate.execute(" delete from " + tablename + "_backup where 1=1  "
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
    public Dto returnAllData(Dto qDto) {
        // TODO Auto-generated method stub
        Dto outDto = Dtos.newDto();
        String tablename = qDto.getString("tablename");
        // 查询条目数
        List<Map<String, Object>> list = jdbcTemplate
                .queryForList("select * from " + tablename + "_backup where 1=1 "
                        + qDto.getString("query"));
        try {
            String sql3="insert into "+qDto.getString("tablename")+" select * from "+qDto.getString("tablename")+"_backup where 1=1  "+qDto.getString("query");
            jdbcTemplate.execute(sql3);
            jdbcTemplate.execute(" delete from " + tablename + "_backup where 1=1  "
                    + qDto.getString("query"));
            String msg = "操作完成,";
            msg = AOSUtils.merge(msg + "成功还原信息[{0}]个", list.size());
            outDto.setAppMsg(msg);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            outDto.setAppMsg("还原失败!");
        }
        return outDto;
    }

    /**
     *
     * 页面初始化
     *
     * @param tablename
     * @return
     */
    public List<Archive_tablefieldlistPO> getDataFieldListTitle(String tablename) {
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAll(tablename);
        return list;
    }
}
