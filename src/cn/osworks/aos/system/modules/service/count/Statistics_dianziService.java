package cn.osworks.aos.system.modules.service.count;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
@Service
public class Statistics_dianziService extends JdbcDaoSupport {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}
	/**
	 * 统计数据
	 * 
	 * @author PX
	 *
	 * 2018-9-19
	 * @param dto 
	 * @return 
	 */
	public List<Dto> getCount(Dto dto) {
		// TODO Auto-generated method stub
		//得到条件：
		String query = queryConditions2(dto);
		List<Dto> l = new ArrayList<Dto>();
		//统计条目总数
		int c=0;
		//统计电子文件数
		int d=0;
		//统计页数
		int y=0;
		//统计盒数
		int h=0;
		//统计30年
		int bgqx30=0;
		//统计10年
		int bgqx10=0;
		//统计永久
		int bgqxy=0;
		String sql="select * from archive_TableName";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Dto outDto=Dtos.newDto();
				String tablename=(String)list.get(i).get("TableName");
				String tabledesc=(String)list.get(i).get("TableDesc");
				outDto.put("category", tabledesc);
				int count = count(outDto,tablename,query);
				int countys = countys(outDto,tablename,query);
				int counth = counth(outDto,tablename,query);
				int countpath = countpath(outDto,tablename,query);

				int count10 = count10(outDto,tablename,query);
				int count30 = count30(outDto,tablename,query);
				int county = county(outDto,tablename,query);
				c=c+count;
				d=d+countpath;
				y=y+countys;
				h=h+counth;
				bgqx10=bgqx10+count10;
				bgqx30=bgqx30+count30;
				bgqxy=bgqxy+county;
				l.add(outDto);
			}
		}
		Dto out=Dtos.newDto();
		out.put("category","总计");
		out.put("ajs",c);
		out.put("wjs",d);
		out.put("ajys",y);
		out.put("hs",h);
		out.put("bgqx10",bgqx10);
		out.put("bgqx30",bgqx30);
		out.put("bgqxy",bgqxy);
		l.add(out);
		return l;
	}
	/**
	 * 
	 * 查询条件拼接
	 * 
	 * @param qDto
	 * @return
	 */
	public String queryConditions2(Dto qDto) {
			StringBuffer query = new StringBuffer();
			for (int i = 1; i <= 3; i++) {
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

	}
	private int countpath(Dto outDto, String tablename, String query) {
		// TODO Auto-generated method stub
		int countpath =0;
		try {
			String sql4="select count(*) from "+tablename+"_path where 1=1 "+query;
			countpath = jdbcTemplate.queryForInt(sql4);
			outDto.put("wjs", countpath);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.put("wjs", countpath);
		}
		return countpath;
		
	}
	//操作页数
	private int countys(Dto outDto, String tablename, String query) {
		// TODO Auto-generated method stub
		String sql4="select * from archive_TableFieldList where tid in (select id_ from archive_TableName where TableName='"+tablename+"') and FieldEnName='ys' "+query;
		int countys =0;
		try {
			List<Map<String,Object>> forList = jdbcTemplate.queryForList(sql4);
			if(forList!=null&&forList.size()>0){
				String lx=(String)forList.get(0).get("FieldClass");
				if(lx.equals("int")&&lx!=null&&lx!=""){
					String sql3="select sum(ys) from "+tablename;
					countys = jdbcTemplate.queryForInt(sql3);
					outDto.put("ajys", countys);
				}else{
					try {
						String sql3="select sum(cast(ys as numeric(12,0))) from "+tablename  +" where 1=1 "+query;
						countys = jdbcTemplate.queryForInt(sql3);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						countys=0;
					}
					outDto.put("ajys", countys);
				}
			}else{
				outDto.put("ajys", 0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.put("ajys", 0);
		}
		return countys;
	}
	//操作盒数
	private int counth(Dto outDto, String tablename, String query) {
		// TODO Auto-generated method stub
		String sql4="select * from archive_TableFieldList where tid in (select id_ from archive_TableName where TableName='"+tablename+"') and FieldEnName='hs' "+query;
		int counth =0;
		try {
			List<Map<String,Object>> forList = jdbcTemplate.queryForList(sql4);
			if(forList!=null&&forList.size()>0){
				String lx=(String)forList.get(0).get("FieldClass");
				if(lx.equals("int")&&lx!=null&&lx!=""){
					String sql3="select sum(hs) from "+tablename;
					counth = jdbcTemplate.queryForInt(sql3);
					outDto.put("hs", counth);
				}else{
					try {
						String sql3="select sum(cast(hs as numeric(12,0))) from "+tablename  +" where 1=1 "+query;
						counth = jdbcTemplate.queryForInt(sql3);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						counth=0;
					}
					outDto.put("hs", counth);
				}
			}else{
				outDto.put("hs", 0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.put("hs", 0);
		}
		return counth;
	}
	private int count(Dto outDto, String tablename, String query) {
		// TODO Auto-generated method stub
		int count =0;
		try {
			String sql2="select count(*) from "+tablename+" where 1=1 "+query;
			count= jdbcTemplate.queryForInt(sql2);
			outDto.put("ajs",count);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.put("ajs",0);
		}
		return count;
	}
	private int count10(Dto outDto, String tablename, String query) {
		// TODO Auto-generated method stub
		int count =0;
		try {
			String sql2="select count(*) from "+tablename+" where 1=1 and bgqx='D10' "+query;
			count= jdbcTemplate.queryForInt(sql2);
			outDto.put("bgqx10",count);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.put("bgqx10",0);
		}
		return count;
	}
	private int count30(Dto outDto, String tablename, String query) {
		// TODO Auto-generated method stub
		int count =0;
		try {
			String sql2="select count(*) from "+tablename+" where 1=1 and bgqx='D30' "+query;
			count= jdbcTemplate.queryForInt(sql2);
			outDto.put("bgqx30",count);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.put("bgqx30",0);
		}
		return count;
	}
	private int county(Dto outDto, String tablename, String query) {
		// TODO Auto-generated method stub
		int count =0;
		try {
			String sql2="select count(*) from "+tablename+" where 1=1 and bgqx='Y' "+query;
			count= jdbcTemplate.queryForInt(sql2);
			outDto.put("bgqxy",count);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.put("bgqxy",0);
		}
		return count;
	}
	public List<Dto> getDataFieldListTitle(Dto qDto, HttpSession session) {
		// TODO Auto-generated method stub
		List<Dto> listcount=(List<Dto>)session.getAttribute("listcount");
		return listcount;
	}
}
