package cn.osworks.aos.system.modules.service.Synonym;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_censusMapper;
import cn.osworks.aos.system.dao.mapper.Archive_synonymMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tablefieldlistMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;
import cn.osworks.aos.system.dao.po.Archive_censusPO;
import cn.osworks.aos.system.dao.po.Archive_synonymPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SynonymService extends JdbcDaoSupport {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public Archive_synonymMapper archive_synonymMapper;
	@Autowired
	public Archive_tablenameMapper archive_tablenameMapper;
	@Autowired
	public Archive_tablefieldlistMapper archive_tablefieldlistMapper;

	/*@Autowired
	private Archive_compilationMapper archive_compilationMapper;*/
	@Resource
	public void setJd(JdbcTemplate jd) {
		super.setJdbcTemplate(jd);
	}

	/**
     * 全宗列表
	 * @param dto
	 */
	public  List<Map<String,Object>> listgenerals(Dto dto) {
			String sql="select * from archive_synonym";
			List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
			return list;
	}

    /**
     * 添加全宗列表
	 * @param dto
	 */
	public boolean addcensus(Dto dto) {
		    String fieldcnnames=dto.getString("fieldcnnames");
		try{
			Archive_synonymPO archive_synonymPO=new Archive_synonymPO();
			AOSUtils.copyProperties(dto,archive_synonymPO);
			archive_synonymPO.setId_(AOSId.uuid());
			archive_synonymPO.setSynonym_list(fieldcnnames);
			archive_synonymMapper.insert(archive_synonymPO);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

    /**
     * 修改
	 * @param out
	 */
	public boolean updatecensus(Dto out) {
		String fieldcnnames=out.getString("fieldcnnames");
		try{
			Archive_synonymPO archive_synonymPO=new Archive_synonymPO();
			AOSUtils.copyProperties(out,archive_synonymPO);
            archive_synonymPO.setSynonym_list(fieldcnnames);
			archive_synonymMapper.updateByKey(archive_synonymPO);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean delsynonym(Dto outDto) {
		try{
			archive_synonymMapper.deleteByKey(outDto.getString("id_"));
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 得到门类列表
	 * @param inDto
	 */
	public List<Archive_tablefieldlistPO> getQueryTitle(Dto inDto) {
		List<Archive_tablefieldlistPO> list=new ArrayList<Archive_tablefieldlistPO>();
		try{
			Dto outDto=Dtos.newDto();
			outDto.put("tid",inDto.getString("tablename_id"));
			list=archive_tablefieldlistMapper.list(outDto);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return list;
		}
	}
	/**
	 * 得到门类列表
	 * @param inDto
	 */
	public List<Archive_tablefieldlistPO> getQueryTitlename(Dto inDto) {
		List<Archive_tablefieldlistPO> list=new ArrayList<Archive_tablefieldlistPO>();
		try{
			Dto outDto=Dtos.newDto();
			String sql="select id_ from archive_tablename where  tablename='"+inDto.getString("tablename")+"'";
			List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql);
			if(list2!=null&&list2.size()>0){
				outDto.put("tid",list2.get(0).get("id_"));
				list=archive_tablefieldlistMapper.list(outDto);
				return list;
			}else{
				return list;
			}

		}catch(Exception e){
			e.printStackTrace();
			return list;
		}
	}

    public String gettablename(Dto inDto) {
	    String id_=inDto.getString("tablename_id");
	    String sql="select * from archive_tablename where id_='"+id_+"'";
	    List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            return (String)list.get(0).get("tablename");
        }else{
            return "";
        }
	}
}
