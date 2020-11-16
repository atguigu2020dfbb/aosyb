package cn.osworks.aos.system.modules.service.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.itextpdf.text.log.SysoCounter;

import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;

@Service
public class DynamicService extends JdbcDaoSupport{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}
	/*
	 * 查询表名
	 */
	public List<Archive_tablenamePO> findTablename() {
		// TODO Auto-generated method stub
		List<Archive_tablenamePO> alist=new ArrayList<Archive_tablenamePO>();
		String sql="select * from archive_TableName";
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
	/**
	 * 查询字段条件列表
	 * 
	 * @author PX
	 * @param dto 
	 * @return
	 *
	 * 2018-8-20
	 */
	public List<Archive_tablefieldlistPO> findTableField(String tablename) {
		// TODO Auto-generated method stub
		List<Archive_tablefieldlistPO> alist=new ArrayList<Archive_tablefieldlistPO>();
		String sql="select * from archive_TableFieldList where tid in(select id_ from archive_TableName where tablename='"+tablename+"')";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String, Object> map = list.get(i);
				String FieldEnName=(String)map.get("FieldEnName");
				String FieldCnName=(String)map.get("FieldCnName");
				Archive_tablefieldlistPO archive_tablefieldlistPO=new Archive_tablefieldlistPO();
				archive_tablefieldlistPO.setFieldenname(FieldEnName);
				archive_tablefieldlistPO.setFieldcnname(FieldCnName);
				alist.add(archive_tablefieldlistPO);
			}
		}
		return alist;
	}
	/**
	 * 折线统计图
	 * 
	 * @author PX
	 * @param dto
	 *
	 * 2018-8-20
	 * @return 
	 */
	public String getBrokenpicture(Dto dto) {
		// TODO Auto-generated method stub
		String listTablename = dto.getString("listTablename");
		String listTableField = dto.getString("listTableField");
		String str="{'message':'error!','data' :[";
		String sql="select distinct "+listTableField+" from "+listTablename;
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String string=(String)list.get(i).get(listTableField);
				if(string==null||string.equals("")){
					continue;
				}
					int b=jdbcTemplate.queryForInt("select count(*) from "+listTablename+" where "+listTableField+" ='"+string+"'");
					str+="{name:'"+string+"',total:"+b+"},";
			}
		}
		//去掉一个逗号
		str=str.substring(0, str.length()-1);
		str+="],";
		Integer count = getCount(dto);
		str+="count:'"+count+"'}";
		return str;
	}
	/**
	 * 得到某个字段重复的最大个数
	 * 
	 * @author PX
	 * @param dto
	 *
	 * 2018-8-22
	 */
	public Integer getCount(Dto dto) {
		// TODO Auto-generated method stub
		
		int max=0;
		String listTablename = dto.getString("listTablename");
		String listTableField = dto.getString("listTableField");
		String sql="SELECT  COUNT("+listTableField+") as temp  FROM  "+listTablename+" where "+listTableField+" !='' group by "+listTableField+" order by "+listTableField;
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		int[] str=new int[list.size()];
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				str[i]=(Integer) list.get(i).get("temp");
			}
		}
		
		//得到String[]中的最大值
		for(int t=0;t<str.length;t++){
			if(str[t]>max){
				max=str[t];
			}
		}
		return max;
	}
}
