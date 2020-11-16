package cn.osworks.aos.system.modules.service.General;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.Archive_compilationPO;
import cn.osworks.aos.system.dao.po.Archive_generalPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class GeneralService extends JdbcDaoSupport {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public Archive_generalMapper archive_generalMapper;

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
			String sql="select * from archive_general";
			List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
			return list;
	}

	/**
	 * 添加全宗列表
	 * @param dto
	 */
	public boolean addgeneral(Dto dto) {
		try{
			Archive_generalPO archive_generalPO=new Archive_generalPO();
			AOSUtils.copyProperties(dto,archive_generalPO);
			archive_generalPO.setId_(AOSId.uuid());
			archive_generalMapper.insert(archive_generalPO);
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
	public boolean updategeneral(Dto out) {
		try{
			Archive_generalPO archive_generalPO=new Archive_generalPO();
			AOSUtils.copyProperties(out,archive_generalPO);
			archive_generalMapper.updateByKey(archive_generalPO);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean delgeneral(Dto outDto) {
		try{
			archive_generalMapper.deleteByKey(outDto.getString("id_"));
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
