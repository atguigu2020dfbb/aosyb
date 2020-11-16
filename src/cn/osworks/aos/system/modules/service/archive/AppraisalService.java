package cn.osworks.aos.system.modules.service.archive;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.net.aso.i;

import org.apache.poi.util.TempFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tablefieldlistMapper;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;

@Service
public class AppraisalService extends JdbcDaoSupport {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Resource
	public void setJd(JdbcTemplate jd) {
		super.setJdbcTemplate(jd);
	}

	/**
	 * 查询鉴定过期列表
	 * 
	 * @param tableTemplate
	 */
	public List<Map<String, Object>> selectappraisal(String tableTemplate) {
		// TODO Auto-generated method stub
		// 1.判断当前数据表是否存在保管期限，成文日期字段,最终鉴定结果字段
		String sql = "select * from archive_TableFieldList where tid in(select id_ from archive_Tablename where TableName='"
				+ tableTemplate
				+ "') and FieldEnName='cwrq' or FieldEnName='bgqx' or FieldEnName='zzjjg'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			if(list.size()<2){
				return null;
			}
		}
		//2.查询移动的鉴定档案
		String sql2 = "select * from "+tableTemplate+" where zzjjg='1'";
		List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
		return list2;
	}

	public List<Archive_tablefieldlistPO> getDataFieldListTitle(String string) {
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper.getDataFieldDisplayAll(string);
		return list;
	}
	/**
	 * 删除档案
	 * @param dto
	 */
	public Dto deleteData(Dto dto) {
		// TODO Auto-generated method stub
		Dto outDto=Dtos.newDto();
		int update = 0;
		String[] idstring = dto.getString("aos_rows_").split(",");
		for (int i=0;i<idstring.length;i++) {
			jdbcTemplate.execute("update "+dto.getString("tablename")+"  set zzjjg = NULL where id_='"+idstring[i]+"'");
			update++;
		}
		String msg = "操作完成,";
		if (update > 0) {
			msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", update);
		}
		outDto.setAppMsg(msg);
		return outDto;
	}
}
