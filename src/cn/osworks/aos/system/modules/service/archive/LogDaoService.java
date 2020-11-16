package cn.osworks.aos.system.modules.service.archive;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.LogPO;

@Service
public class LogDaoService extends JdbcDaoSupport{
	@Autowired
	public LogMapper logMapper;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Resource
	public void setJd(JdbcTemplate jd){
		super.setJdbcTemplate(jd);
	}
	/**
	 * 根据id得到详情
	 * @param inDto
	 * @return
	 */
	public LogPO getlogInfo(Dto inDto){
		// TODO Auto-generated method stub
		LogPO logPO = logMapper.selectByKey(inDto.getString("id"));
		if(logPO!=null){
			return logPO;
		}
		return null;
	}
	/**
	 * 根据id删除日志信息
	 * @param inDto
	 */
	public Dto dellogInfo(Dto inDto) {
		// TODO Auto-generated method stub
		Dto outDto = Dtos.newDto();
		int i = logMapper.deleteByKey(inDto.getString("id"));
		String msg = "操作完成，";
		if (i > 0) {
			msg = AOSUtils.merge(msg + "成功删除日志[{0}]个", i);
		}
		outDto.setAppMsg(msg);
		return outDto;
	}
	/**
	 * 清空日志
	 * @param inDto
	 * @return
	 */
	public Dto delAlllogInfo(Dto inDto) {
		// TODO Auto-generated method stub
		Dto outDto = Dtos.newDto();
		String msg="";
		try {
			//清空之前查询总数
			String selectsql="select * from Log";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(selectsql);
			//执行清空操作
			String sql="delete from Log";
			jdbcTemplate.execute(sql);
			msg="操作完成,";
			msg=AOSUtils.merge(msg+"成功清空日志[{0}]个",list.size());
			outDto.setAppMsg(msg);
			return outDto;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		msg="操作失败,系统异常!";
		outDto.setAppMsg(msg);
		return outDto;
	}
	/**
	 * 查询列头数据
	 * @param qDto
	 * @return
	 */
	public List<Dto> exportData(Dto qDto) {
		// TODO Auto-generated method stub
		String sqlString="select name from syscolumns where id=(select max(id) from sysobjects where xtype='u' and name='archive_Log') ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlString);
		return null;
	}
	/**
	 * 页面初始化
	 * @param party 
	 * @param tablename
	 * @return
	 */
	public List<Map<String, Object>> getDataFieldListTitle(Dto dto) {
		String sql="";
		if(dto.getString("party")!=null&&dto.getString("party")!=""){
			sql="select * from Log where party="+dto.getString("party");
		}else{
			sql="select * from Log";
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	public Dto updateshenji(Dto qDto) {
		Dto outDto = Dtos.newDto();
		try {
			LogPO logpo=new LogPO();
			AOSUtils.copyProperties(qDto,logpo);
			logpo.setSjr(qDto.getUserInfo().getAccount_());
			logpo.setSjsj(AOSUtils.getDateTimeStr());
			int i = logMapper.updateByKey(logpo);
			outDto.setAppMsg("操作成功");
			return outDto;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.setAppMsg("操作失败");
			return outDto;
		}
	}
}
