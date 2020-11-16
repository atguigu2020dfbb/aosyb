package cn.osworks.aos.system.modules.service.dbtable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tablefieldlistMapper;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;

@Service
public class MaintainService {
	
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
     * 
     * 查询表字段
     * 
     * @param excelpath
     * @param qDto
     * @return
     */
    public List<Dto> getTargetField(Dto qDto){
    	//List<Dto>  listtitle=readExcelTitle(excelpath);
    	List<Dto> temlist = new ArrayList<Dto>();
		List<Archive_tablefieldlistPO> sourcelist=archive_tablefieldlistMapper.getTableFields(qDto.getString("sourceTablename"));
		List<Archive_tablefieldlistPO> targetlist=archive_tablefieldlistMapper.getTableFields(qDto.getString("targetTablename"));
		for(Archive_tablefieldlistPO archive_tablefieldlistPO :sourcelist){
			Dto strDto = Dtos.newDto();
			String strtagetFieldname="";
			String strfieldenname="";
			for(int i=0;i<targetlist.size();i++){
				if(targetlist.get(i).getFieldcnname().equals(archive_tablefieldlistPO.getFieldcnname())){
					strtagetFieldname=targetlist.get(i).getFieldcnname();
					strfieldenname=targetlist.get(i).getFieldenname();
				}
			}
			strDto.put("sourcefieldname", archive_tablefieldlistPO.getFieldcnname());
			strDto.put("targetfieldname", strtagetFieldname);
			strDto.put("sourcefieldenname", archive_tablefieldlistPO.getFieldenname());
			strDto.put("targetfieldenname", strfieldenname);
			temlist.add(strDto);
		}
    	return temlist;
    }
    
    /**
     * 
     * 
     */
    public Dto InsertDb (Dto qDto){
    	Dto outDto = Dtos.newDto();
    	List<Dto> list = qDto.getRows();
    	StringBuffer sourceColumn = new StringBuffer("id_,");
    	StringBuffer targetColumn = new StringBuffer("id_,");
    	for(Dto d : list){
    		if(!d.getString("targetfieldenname").isEmpty()){
    			targetColumn.append(d.getString("targetfieldenname")).append(",");
    			sourceColumn.append(d.getString("sourcefieldenname")).append(",");
    		}
    	}
    	sourceColumn.deleteCharAt(sourceColumn.length() - 1).toString();
		targetColumn.deleteCharAt(targetColumn.length() - 1).toString();
    	String sql="INSERT INTO "+qDto.getString("targetTablename")+" ("+targetColumn+") SELECT "+sourceColumn+" from "+qDto.getString("sourceTablename");
		jdbcTemplate.execute(sql);
		if(qDto.getString("path").equals("on")){
			String pathcolumn = "id_,tid,_path,dirname,sdatetime,_s_path,_index";
			String strsql=" INSERT INTO "+qDto.getString("targetTablename")+"_PATH ("+pathcolumn+") SELECT "+pathcolumn+" FROM "+qDto.getString("sourceTablename")+"_path";
			jdbcTemplate.execute(strsql);
		}
    	String msg = "操作完成.";
    	outDto.setAppMsg(msg);
		return outDto;
    }
    
    public Dto clearTable(Dto qDto){
    	Dto outDto = Dtos.newDto();
    	String sql = "delete from "+qDto.getString("tablename");
    	jdbcTemplate.execute(sql);
    	clearPath(qDto.getString("tablename"));
    	String msg="操作完成.";
    	outDto.setAppMsg(msg);
    	return outDto;
    	
    }
    
    public void clearPath(String tablename){
    	String sql = "delete from "+tablename +"_path"; 
    	jdbcTemplate.execute(sql);
    }
}
