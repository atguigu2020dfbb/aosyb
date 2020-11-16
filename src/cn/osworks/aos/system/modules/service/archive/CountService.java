package cn.osworks.aos.system.modules.service.archive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.typewrap.Dto;
@Service
public class CountService extends JdbcDaoSupport {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Resource
	public void setJd(JdbcTemplate jd) {
		super.setJdbcTemplate(jd);
	}
	/**
	 * 查询数据
	 * @param outDtos
	 */
	public StringBuffer htmlcount(Dto outDtos) {
		// TODO Auto-generated method stub
		String year =outDtos.getString("year");
		String security =outDtos.getString("security");
		String total =outDtos.getString("total");
		StringBuffer count=new StringBuffer();
		//year=, security=, total=
		String sql1 ="select count(*) from wsda where 1=1 and nd='"+year+"' and mj='"+security+"' and qzh='"+total+"'";
		String sql2 ="select count(*) from wsda where 1=1 and nd='"+year+"' and mj='"+security+"' and qzh='"+total+"'";
		String sql3 ="select count(*) from wsda where 1=1 and nd='"+year+"' and mj='"+security+"' and qzh='"+total+"'";
		List<Map<String,Object>> list1 = jdbcTemplate.queryForList(sql1);
		List<Map<String,Object>> list2 = jdbcTemplate.queryForList(sql2);
		List<Map<String,Object>> list3 = jdbcTemplate.queryForList(sql3);
		count.append(list1.size()+",");
		count.append(list2.size()+",");
		count.append(list3.size()+"");
		return count;
	}
	
}
