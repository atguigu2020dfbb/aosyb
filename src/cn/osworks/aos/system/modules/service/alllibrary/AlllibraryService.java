package cn.osworks.aos.system.modules.service.alllibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;

@Service
public class AlllibraryService extends JdbcDaoSupport {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}

	/**
	 * 查询不同数据表的字段汇总
	 * 
	 * @return
	 */
	public List<Archive_tablefieldlistPO> getDataFieldListTitle() {
		// TODO Auto-generated method stub
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		List<Archive_tablefieldlistPO> titlelist = new ArrayList<Archive_tablefieldlistPO>();
		List<Map<String, Object>> list = findTablename();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String tableName = (String) map.get("TableName");
				String tableDesc = (String) map.get("TableDesc");
				String id_ = (String) map.get("id_");
				List<Map<String, Object>> fieldList = findTableFieldList(id_);
				if (fieldList != null && fieldList.size() > 0) {
					for (int t = 0; t < fieldList.size(); t++) {
						Map<String, Object> fieldmap = fieldList.get(t);
						String FieldEnName = (String) fieldmap
								.get("FieldEnName");
						String FieldCnName = (String) fieldmap
								.get("FieldCnName");
						hashmap.put(FieldEnName, FieldCnName);
					}
				}
			}
			// 此时hashmap中存入的就是完整的字段英文名称,存放到list中
			if (hashmap != null && hashmap.size() > 0) {
				for (Entry<String, Object> entry : hashmap.entrySet()) {
					String key = entry.getKey();
					String value = (String) entry.getValue();
					Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
					archive_tablefieldlistPO.setFieldenname(key);
					archive_tablefieldlistPO.setFieldcnname(value);
					titlelist.add(archive_tablefieldlistPO);
				}
			}
		}
		return titlelist;
	}

	/**
	 * 数据表所有数据展示
	 * 
	 * @param dto
	 */
	public List<Map<String, Object>> getDataSjbList(Dto dto) {
		// TODO Auto-generated method stub
		String content = dto.getString("content");
		String sql = "";
		List<Map<String, Object>> alllist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = findTablename();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String tableName = (String) map.get("TableName");
				String tableDesc = (String) map.get("TableDesc");
				String id_ = (String) map.get("id_");
				List<Map<String, Object>> fieldList = findTableFieldList(id_);
				if (content != "" || content != null) {
					StringBuffer sb = new StringBuffer();
					String kk="";
					for (int z = 0; z < fieldList.size(); z++) {
						if(!isNumeric(content)){
							if(fieldList.get(z).get("FieldClass").equals("int")){
								continue;
							}
						}
							sb.append("or "+fieldList.get(z).get("fieldenname")
									+ " = '" + content + "' ");
					}
					if(sb.toString().startsWith("or")){
						//去掉第一个or
						kk=sb.substring(2);
					}
					kk=" and "+kk;
					sql = "select * from " + tableName + " where 1=1  "
							+ kk.toString();
					List<Map<String, Object>> datalist = jdbcTemplate
							.queryForList(sql);
					if (datalist != null && datalist.size() > 0) {
						for (int t = 0; t < datalist.size(); t++) {
							//插入一列所属表名，在前端隐藏起来
							datalist.get(t).put("tablename", tableName);
							alllist.add(datalist.get(t));
						}
					}
				} else {
					sql = "select * from " + tableName;
					List<Map<String, Object>> datalist = jdbcTemplate
							.queryForList(sql);
					if (datalist != null && datalist.size() > 0) {
						for (int t = 0; t < datalist.size(); t++) {
							alllist.add(datalist.get(t));
						}
					}
				}
			}
		}
		return alllist;
	}
		public static boolean isNumeric(String str) {
			//Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");//这个有问题，一位的整数不能通过
			Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");//这个是对的
			Matcher isNum = pattern.matcher(str);
			if (!isNum.matches()) {
				return false;
			}
			return true;
	}
	/**
	 * 查询所有表名
	 * 
	 * @return
	 */
	public List<Map<String, Object>> findTablename() {
		String sqltablename = "select * from archive_TableName";
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sqltablename);
		return list;
	}

	/**
	 * 根据id查询到字段列表集合
	 */
	public List<Map<String, Object>> findTableFieldList(String id_) {
		String sqlTableFieldList = "select * from archive_TableFieldList where tid='"
				+ id_ + "'";
		List<Map<String, Object>> fieldList = jdbcTemplate
				.queryForList(sqlTableFieldList);
		return fieldList;
	}
	/**
	 * 得到电子文件列表
	 * @param dto
	 * @return 
	 */
	public List<Map<String, Object>> getPath(Dto dto) {
		// TODO Auto-generated method stub
		String tid=dto.getString("tid");
		String tablename=dto.getString("tablename");
		String sql="select * from "+tablename+"_path where tid='"+tid+"'";
		List<Map<String, Object>> pathList = jdbcTemplate
				.queryForList(sql);
		return pathList;
	}
}
