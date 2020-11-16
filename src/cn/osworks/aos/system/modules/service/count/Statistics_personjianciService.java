package cn.osworks.aos.system.modules.service.count;

import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Statistics_personjianciService extends JdbcDaoSupport {
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
				int countpath = countpath(outDto,tablename,query);
				c=c+count;
				d=d+countpath;
				y=y+countys;
				l.add(outDto);
			}
		}
		Dto out=Dtos.newDto();
		out.put("category","总计");
		out.put("ajs",c);
		out.put("wjs",d);
		out.put("ajys",y);
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
	public List<Dto> getDataFieldListTitle(Dto qDto, HttpSession session) {
		// TODO Auto-generated method stub
		List<Dto> listcount=(List<Dto>)session.getAttribute("listcount");
		return listcount;
	}

	public List<Archive_tablenamePO> findTablename() {
		// TODO Auto-generated method stub
		List<Archive_tablenamePO> alist=new ArrayList<Archive_tablenamePO>();
		String sql="select * from archive_TableName where tablename not like '%_receive' and tablename not like '%_temporary' and tablename not like '%_total'";
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


	public List<Archive_tablenamePO> findTablename2(Dto qDto) {
		// TODO Auto-generated method stub
		List<Archive_tablenamePO> alist=new ArrayList<Archive_tablenamePO>();
		String type=qDto.getString("type");
		String sql="";
		if("1".equals(type)){
			sql="select * from archive_TableName where  tablename  like '%_temporary' ";
		}else if("2".equals(type)){
			sql="select * from archive_TableName where tablename  like '%_receive' ";
		}else if("3".equals(type)){
			sql="select * from archive_TableName where tablename not like '%_receive' and tablename not like '%_temporary' and tablename not like '%_total' ";
		}else if("4".equals(type)){
			sql="select * from archive_TableName where tablename  like 'zl%' and tablename not like '%_receive' and tablename not like '%_temporary' and tablename not like '%_total' ";
		}else if("5".equals(type)){
			sql="select * from archive_TableName where tablename like '%_total'";
		}else{
			return alist;
		}
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
	 * 得到保管期限中的各个数量
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> countMath(Dto dto) {
		String shijian=dto.getString("shijian");
		shijian=shijian.replaceAll("-","");
		String tablenames=dto.getString("tablenames");
		String qzmc=dto.getString("qzmc");
		//创建list集合，并存储到集合中
		List<Map<String,Object>> countlist=new ArrayList<Map<String,Object>>();
			//设计sql语句查询10年，30年，永久的各个数量并组成集合
			String sql2="select count(distinct(sfzh)) as rc from archive_remote where  djbh like '%"+shijian+"%'";
			List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
			String sql3="select count(*) as jc  from archive_remote_detail where formid in ( select id_ from archive_remote where  djbh like '%"+shijian+"%' ) and tablename in ("+tablenames+")";
			List<Map<String,Object>> list3=jdbcTemplate.queryForList(sql3);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("shijian",shijian);
			if(list2.get(0).get("rc")==null){
				map.put("rc",0);
			}else{
				map.put("rc",list2.get(0).get("rc"));
			}
			if(list3.get(0).get("jc")==null){
				map.put("jc",0);
			}else{
				map.put("jc",list3.get(0).get("jc"));
			}
			countlist.add(map);
		return countlist;
	}
	/**
	 * 得到保管期限中的各个数量2
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> countMath2(Dto dto) {
		String start_nd=dto.getString("start_listnd");
		String end_nd=dto.getString("end_listnd");
		String tablename=dto.getString("tablename");
		String qzmc=dto.getString("qzmc");
		//创建list集合，并存储到集合中
		List<Map<String,Object>> countlist=new ArrayList<Map<String,Object>>();
		String sql="select distinct(nd) as nd from "+tablename+" where nd>="+start_nd+" and nd<="+end_nd;
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		for(int i=0;i<list.size();i++){
			//设计sql语句查询10年，30年，永久的各个数量并组成集合
			String sql2="select sum(syjs) as sumsyjs,sum(ssjs) as sumssjs,sum(ysys) as sumysys,sum(ssys) as sumssys,sum(ssrl) as ssrl from "+tablename+" where nd='"+list.get(i).get("nd")+"' and bgqx='D10' and qzmc='"+qzmc+"'";
			List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
			String sql3="select sum(syjs) as sumsyjs,sum(ssjs) as sumssjs,sum(ysys) as sumysys,sum(ssys) as sumssys,sum(ssrl) as ssrl from "+tablename+" where nd='"+list.get(i).get("nd")+"' and bgqx='D30' and qzmc='"+qzmc+"'";
			List<Map<String,Object>> list3=jdbcTemplate.queryForList(sql3);
			String sql4="select sum(syjs) as sumsyjs,sum(ssjs) as sumssjs,sum(ysys) as sumysys,sum(ssys) as sumssys,sum(ssrl) as ssrl from "+tablename+" where nd='"+list.get(i).get("nd")+"' and bgqx='Y' and qzmc='"+qzmc+"'";
			List<Map<String,Object>> list4=jdbcTemplate.queryForList(sql4);
			String sql5="select sum(syjs) as sumsyjs,sum(ssjs) as sumssjs,sum(ysys) as sumysys from "+tablename+" where nd='"+list.get(i).get("nd")+"' and qzmc='"+qzmc+"'";
			List<Map<String,Object>> list5=jdbcTemplate.queryForList(sql5);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("qzmc",qzmc);
			map.put("nd",list.get(i).get("nd"));

			if(list2.get(0).get("sumsyjs")==null){
				map.put("syjs_10",0);
			}else{
				map.put("syjs_10",list2.get(0).get("sumsyjs"));
			}
			if(list2.get(0).get("sumssjs")==null){
				map.put("ssjs_10",0);
			}else{
				map.put("ssjs_10",list2.get(0).get("sumssjs"));
			}
			if(list2.get(0).get("sumysys")==null){
				map.put("ysys_10",0);
			}else{
				map.put("ysys_10",list2.get(0).get("sumysys"));
			}
			if(list2.get(0).get("sumssys")==null){
				map.put("ssys_10",0);
			}else{
				map.put("ssys_10",list2.get(0).get("sumssys"));
			}
			if(list2.get(0).get("sumyssrl")==null){
				map.put("ssrl_10",0);
			}else{
				map.put("ssrl_10",list2.get(0).get("sumyssrl"));
			}



			if(list3.get(0).get("sumsyjs")==null){
				map.put("syjs_30",0);
			}else{
				map.put("syjs_30",list3.get(0).get("sumsyjs"));
			}
			if(list3.get(0).get("sumssjs")==null){
				map.put("ssjs_30",0);
			}else{
				map.put("ssjs_30",list3.get(0).get("sumssjs"));
			}
			if(list3.get(0).get("sumysys")==null){
				map.put("ysys_30",0);
			}else{
				map.put("ysys_30",list3.get(0).get("sumysys"));
			}
			if(list3.get(0).get("sumssys")==null){
				map.put("ssys_30",0);
			}else{
				map.put("ssys_30",list3.get(0).get("sumssys"));
			}
			if(list3.get(0).get("sumyssrl")==null){
				map.put("ssrl_30",0);
			}else{
				map.put("ssrl_30",list3.get(0).get("sumyssrl"));
			}


			if(list4.get(0).get("sumsyjs")==null){
				map.put("syjs_y",0);
			}else{
				map.put("syjs_y",list4.get(0).get("sumsyjs"));
			}
			if(list4.get(0).get("sumssjs")==null){
				map.put("ssjs_y",0);
			}else{
				map.put("ssjs_y",list4.get(0).get("sumssjs"));
			}
			if(list4.get(0).get("sumysys")==null){
				map.put("ysys_y",0);
			}else{
				map.put("ysys_y",list4.get(0).get("sumysys"));
			}
			if(list4.get(0).get("sumssys")==null){
				map.put("ssys_y",0);
			}else{
				map.put("ssys_y",list4.get(0).get("sumssys"));
			}
			if(list4.get(0).get("sumyssrl")==null){
				map.put("ssrl_y",0);
			}else{
				map.put("ssrl_y",list4.get(0).get("sumyssrl"));
			}


			if(list5.get(0).get("sumsyjs")==null){
				map.put("syjs_z",0);
			}else{
				map.put("syjs_z",list5.get(0).get("sumsyjs"));
			}
			if(list5.get(0).get("sumssjs")==null){
				map.put("ssjs_z",0);
			}else{
				map.put("ssjs_z",list5.get(0).get("sumssjs"));
			}
			if(list5.get(0).get("sumysys")==null){
				map.put("ysys_z",0);
			}else{
				map.put("ysys_z",list5.get(0).get("sumysys"));
			}
			countlist.add(map);
		}
		return countlist;
	}

	/**
	 * 第一个折线统计图
	 * @param dto
	 * @return
	 */
	public String getBroken1picture(Dto dto) {
		String tablename = dto.getString("tablename");
		String flag = dto.getString("flag");
		String start_listnd = dto.getString("start_listnd");
		String end_listnd = dto.getString("end_listnd");
		String qzmc=dto.getString("qzmc");
		if(flag.equals("1")) {
			try {
			String str = "{'message':'error!','data' :[";
			String sql = "select sum(js) as js from " + tablename + " where nd >="+start_listnd+" and nd<="+end_listnd +" and qzmc='"+qzmc+"'";
			List<Map<String,Object>> list1=jdbcTemplate.queryForList(sql);
			int jssum=0;
			if(list1!=null&&list1.size()>0){
				jssum= Integer.valueOf(list1.get(0).get("js").toString());
			}else{
				jssum=0;
			}
			str += "{name:'件数',total:" + jssum + "},";
			String sql2 = "select sum(hs) as hs from " + tablename + " where nd >="+start_listnd+" and nd<="+end_listnd+" and qzmc='"+qzmc+"'";
			List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
			int hssum=0;
			if(list2!=null&&list2.size()>0){
				hssum= Integer.valueOf(list2.get(0).get("hs").toString());
			}else{
				hssum=0;
			}
			str += "{name:'盒数',total:" + hssum + "},";
			String sql3 = "select sum(ys) as ys from " + tablename + " where nd >="+start_listnd+" and nd<="+end_listnd+" and qzmc='"+qzmc+"'";
			List<Map<String,Object>> list3=jdbcTemplate.queryForList(sql3);
			int yssum=0;
			if(list3!=null&&list3.size()>0){
				yssum= Integer.valueOf(list3.get(0).get("ys").toString());
			}else{
				yssum=0;
			}
				str += "{name:'页数',total:" + yssum + "},";
			int intArray2 [] = new int[]{jssum,hssum,yssum};

//				str += "{name:'" + string + "',total:" + b + "},";
				//去掉一个逗号
				str = str.substring(0, str.length() - 1);
				str += "],";

				Integer count = getSum(intArray2);
				str += "count:'" + count + "'}";
				return str;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}
	/**
	 * 第一个折线统计图
	 * @param dto
	 * @return
	 */
	public String getBroken2picture(Dto dto) {
		String tablename = dto.getString("tablename");
		String flag = dto.getString("flag");
		String start_listnd = dto.getString("start_listnd");
		String end_listnd = dto.getString("end_listnd");
		String qzmc=dto.getString("qzmc");
		if(flag.equals("2")) {
			try {
				String str = "{'message':'error!','data' :[";
				String sql = "select sum(syjs) as syjs from " + tablename + " where nd >="+start_listnd+" and nd<="+end_listnd +" and qzmc='"+qzmc+"'";
				List<Map<String,Object>> list1=jdbcTemplate.queryForList(sql);
				int syjssum=0;
				if(list1!=null&&list1.size()>0){
					syjssum= Integer.valueOf(list1.get(0).get("syjs").toString());
				}else{
					syjssum=0;
				}
				str += "{name:'实有件数',total:" + syjssum + "},";
				String sql2 = "select sum(ssjs) as ssjs from " + tablename + " where nd >="+start_listnd+" and nd<="+end_listnd+" and qzmc='"+qzmc+"'";
				List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
				int ssjssum=0;
				if(list2!=null&&list2.size()>0){
					ssjssum= Integer.valueOf(list2.get(0).get("ssjs").toString());
				}else{
					ssjssum=0;
				}
				str += "{name:'实扫件数',total:" + ssjssum + "},";
				String sql3 = "select sum(ysys) as ysys from " + tablename + " where nd >="+start_listnd+" and nd<="+end_listnd+" and qzmc='"+qzmc+"'";
				List<Map<String,Object>> list3=jdbcTemplate.queryForList(sql3);
				int ysyssum=0;
				if(list3!=null&&list3.size()>0){
					ysyssum= Integer.valueOf(list3.get(0).get("ysys").toString());
				}else{
					ysyssum=0;
				}
				str += "{name:'应扫页数',total:" + ysyssum + "},";
				int intArray2 [] = new int[]{syjssum,ssjssum,ysyssum};

//				str += "{name:'" + string + "',total:" + b + "},";
				//去掉一个逗号
				str = str.substring(0, str.length() - 1);
				str += "],";

				Integer count = getSum(intArray2);
				str += "count:'" + count + "'}";
				return str;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}
	private Integer getSum(int[] intArray2) {
		int sum = intArray2[0];//假设第一个元素是最大值
		//for循环遍历数组中元素，每次循环跟数组索引为0的元素比较大小
		for (int i = 0; i < intArray2.length; i++){
			if (sum < intArray2[i]){//数组中的元素跟sum比较，比sum大就把它赋值给sum作为新的比较值
				sum = intArray2[i];
			}
		}
		return sum;
	}

	/**
	 * 得到某个字段重复的最大个数
	 *
	 * @author PX
	 * @param dto
	 *
	 * 2018-8-22
	 */
	public Integer getCount(Dto dto,String ste) {
		// TODO Auto-generated method stub
		int max=0;
		String listTablename = dto.getString("listTablename");
		String listTableField = dto.getString("listTableField");
		String listnd = dto.getString("listnd");
		if(listnd.equals("全部")){
			String sql="SELECT  COUNT(nd) as temp  FROM  "+listTablename+" where nd !='' group by nd order by nd";
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
		}else{
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
}
