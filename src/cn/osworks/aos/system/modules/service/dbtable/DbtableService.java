package cn.osworks.aos.system.modules.service.dbtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tableInfoMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tablefieldlistMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;
import cn.osworks.aos.system.dao.po.Archive_tableInfoPO;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;

/**
 * 
 * 数据表服务
 * 
 * @author shq
 * 
 * @date 2016-9-2
 */
@Service
public class DbtableService {

	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	@Autowired
	private Archive_tableInfoMapper archive_tableInfoMapper;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * 保存数据表信息
	 * 
	 * @param qDto
	 */
	@Transactional
	public Dto saveTable(Dto qDto) {
		Dto outDto = Dtos.newDto();
		Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
		AOSUtils.copyProperties(qDto, archive_tablenamePO);
		if (qDto.getString("tablenametemplate") != null
				&& qDto.getString("tablenametemplate") != "") {
			if (!checkTablename(archive_tablenamePO.getTablename())) {
				outDto.setAppCode(AOSCons.ERROR);
				String msg = AOSUtils.merge("数据表[{0}]已存在, 请重新输入。",
						archive_tablenamePO.getTablename());
				outDto.setAppMsg(msg);
				return outDto;
			}
			String tablenameid=AOSId.uuid();
			//1.创建数据表，并加入字段
			createTable(qDto);
			//3.在tablename表中加入字段信息
			addTableName(qDto,tablenameid);
			//2.在tablefieldlist表中加入字段信息
			addTableFieldlist(qDto,tablenameid);
            //4.创建_path表。
			if (qDto.getString("path").equals("on")) {
				createTablePath(qDto.getString("tablename"));
			}
			//5.创建备份表
			if (qDto.getString("backup").equals("on")) {
				createBackupTable(qDto.getString("tablename"));
			}

			String sql = "insert into archive_TableFieldList  (id_,tid,FieldEnName,FieldCnName,FieldClass,FieldSize,FieldView,indx) "
					+ "values('"
					+ AOSId.uuid()
					+ "','"
					+ tablenameid
					+ "','_lrr','录入人','varchar','20','1','100')"
					+ "insert into archive_TableFieldList  (id_,tid,FieldEnName,FieldCnName,FieldClass,FieldSize,FieldView,indx) "
					+ "values('"
					+ AOSId.uuid()
					+ "','"
					+ tablenameid
					+ "','_lrrq','录入日期','varchar','30','1','101')"
					+ "insert into archive_TableFieldList  (id_,tid,FieldEnName,FieldCnName,FieldClass,FieldSize,FieldView,indx) "
					+ "values('"
					+ AOSId.uuid()
					+ "','"
					+ tablenameid
					+ "','_classtree','目录树','varchar','30','1','103')"
					+ "insert into archive_TableFieldList  (id_,tid,FieldEnName,FieldCnName,FieldClass,FieldSize,FieldView,indx) "
					+ "values('"
					+ AOSId.uuid()
					+ "','"
					+ tablenameid
					+ "','_jd','鉴定','varchar','30','1','104')"
					+ "insert into archive_TableFieldList  (id_,tid,FieldEnName,FieldCnName,FieldClass,FieldSize,FieldView,indx) "
					+ "values('"
					+ AOSId.uuid()
					+ "','"
					+ tablenameid
					+ "','_jy','借阅','varchar','30','1','105')"
					+ "insert into archive_TableFieldList  (id_,tid,FieldEnName,FieldCnName,FieldClass,FieldSize,FieldView,indx) "
					+ "values('"
					+ AOSId.uuid()
					+ "','"
					+ tablenameid
					+ "','_path','附件','int','4','1','102')";
			;
			jdbcExcel(sql);
			outDto.setAppCode(AOSCons.SUCCESS);
			outDto.setAppMsg("操作完成，数据表新增成功。");
			return outDto;
		}else{
			outDto.setAppCode(AOSCons.ERROR);
			outDto.setAppMsg("操作完成，数据表新增失败。");
			return outDto;
		}


	}

	/**
	 * 此时创建备份表
	 * @param tablename
	 */
	private void createBackupTable(String tablename) {

		String sql="select * into "+tablename+"_backup from "+tablename;
		jdbcTemplate.execute(sql);
	}

	/**
	 * 创建的数据表在archive_tableFIELDlist中添加记录
	 * @param qDto
	 * @param tablenameid
	 */
	private void addTableFieldlist(Dto qDto, String tablenameid) {
		//字段英文名称
		String fieldennames=qDto.getString("fieldennames");
		//字段中文名称
		String fieldcnnames=qDto.getString("fieldcnnames");
		//字段类型
		String fieldclasss=qDto.getString("fieldclasss");
		//字段长度
		String fieldsizes=qDto.getString("fieldsizes");
		//显示长度
		String fieldviews=qDto.getString("fieldviews");
		//是否显示
		String fieldmetchs=qDto.getString("fieldmetchs");
		String field="";
		if(fieldennames.split(";").length==fieldclasss.split(";").length&&fieldsizes.split(";").length==fieldclasss.split(";").length){
			for(int i=0;i<fieldennames.split(";").length;i++){
				String fieldenname=fieldennames.split(";")[i];
				String fieldcnname=fieldcnnames.split(";")[i];
				String fieldclass=fieldclasss.split(";")[i];
				String fieldsize=fieldsizes.split(";")[i];
				String fieldview=fieldviews.split(";")[i];
				String fieldmetch=fieldmetchs.split(";")[i];
				//循环箱数据库添加语句
				String sql="insert into archive_TableFieldList (id_,tid,FieldEnName,FieldCnName,FieldClass,FieldSize,FieldView,FieldMetch) values('"+AOSId.uuid()+"','"+tablenameid+"','"+fieldenname+"','"+fieldcnname+"','"+fieldclass+"','"+fieldsize+"','"+fieldview+"','"+fieldmetch+"')";
				jdbcTemplate.execute(sql);
			}
		}
	}

	/**
	 * 添加表明道archive_tablename中
	 * @param qDto
	 * @param tablenameid
	 */
	private boolean addTableName(Dto qDto, String tablenameid) {
		String tablename=	qDto.getString("tablename");
		String tabledesc=	qDto.getString("tabledesc");
		String sql="insert into archive_TableName(id_,TableName,TableDesc) values('"+tablenameid+"','"+tablename+"','"+tabledesc+"')";
		try{
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 根据字段信息，创建数据表
	 * @param qDto
	 * @return
	 */
	private boolean createTable(Dto qDto) {
		String tablename=qDto.getString("tablename");
		//字段英文名称
		String fieldennames=qDto.getString("fieldennames");
		//字段类型
		String fieldclasss=qDto.getString("fieldclasss");
		//字段长度
		String fieldsizes=qDto.getString("fieldsizes");
		String field="";
		if(fieldennames.split(";").length==fieldclasss.split(";").length&&fieldsizes.split(";").length==fieldclasss.split(";").length){
			for(int i=0;i<fieldennames.split(";").length;i++){
				if(fieldclasss.split(";")[i].equals("int")){
					field+=","+fieldennames.split(";")[i]+" "+ fieldclasss.split(";")[i];
				}else{
					field+=","+fieldennames.split(";")[i]+" "+ fieldclasss.split(";")[i]+"("+fieldsizes.split(";")[i]+")";
				}
			}
		}
		String sql="create table "+tablename+"(id_ nvarchar(192) primary key "+field+" ,_lrr varchar(20),_lrrq varchar(30),_classtree varchar(30),_jy varchar(30),_jd varchar(30),_path  int )";
		try{
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 
	 * 修改数据表信息
	 * 
	 * @param qDto
	 * @return
	 */
	@Transactional
	public Dto updateTable(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String tablename = qDto.getString("tablename");
		Archive_tablenamePO check_archive_tablenamePO = archive_tablenameMapper
				.selectByKey(qDto.getString("id_"));
		if (!tablename.equals(check_archive_tablenamePO.getTablename())) {
			if (!checkTablename(tablename)) {
				outDto.setAppCode(AOSCons.ERROR);
				outDto.setAppMsg(AOSUtils.merge("数据表[{0}]已经存在，请重新输入。",
						tablename));
				return outDto;
			}
		}
		String sql = "EXEC  sp_rename '"
				+ check_archive_tablenamePO.getTablename() + "' ,'" + tablename
				+ "'";
		if (!jdbcExcel(sql)) {
			outDto.setAppCode(AOSCons.ERROR);
			outDto.setAppMsg(AOSUtils.merge("数据库中已经存在，请重新输入。", tablename));
			return outDto;
		}
		Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
		AOSUtils.copyProperties(qDto, archive_tablenamePO);
		archive_tablenameMapper.updateByKey(archive_tablenamePO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，数据表修改成功。");
		return outDto;
	}

	/**
	 * 
	 * 检测数据表名的唯一性
	 * 
	 * @param tablename
	 * @return
	 */
	public boolean checkTablename(String tablename) {
		Dto calcDto = Dtos.newCalcDto("count(id_)");
		calcDto.put("tablename", tablename);
		Integer tablenameInteger = Integer.valueOf(archive_tablenameMapper
				.calc(calcDto));
		boolean out = true;
		if (tablenameInteger > 0) {
			out = false;
		}
		return out;
	}

	/**
	 * 
	 * 检测数据表是否创建成功
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean createTable(String tableName) {
		boolean out = true;
		try {

			jdbcTemplate
					.execute("create table "
							+ tableName
							+ " (id_ nvarchar(192) primary key,_lrr nvarchar(192),_lrrq nvarchar(192),_classtree varchar(100),_path int)");

		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out = false;
		}

		return out;
	}

	/**
	 * 
	 * 创建电子附件表
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean createTablePath(String tableName) {
		boolean out = true;

		jdbcTemplate
				.execute("create table "
						+ tableName
						+ "_path"
						+ " (id_ nvarchar(192),tid nvarchar(192),_path varchar(256),dirname varchar(256),sdatetime datetime,_s_path varchar(256),_index int)");
		out = false;

		return out;
	}

	/**
	 * 
	 * 创建元数据表
	 * 
	 * @author Sun
	 * @return
	 * 
	 *         2018-8-10
	 */
	public boolean createTableInfo(Dto qDto) {
		boolean out = true;
		try {
			jdbcTemplate.execute("select *  into "
					+ qDto.getString("tablename")
					+ "_info from archive_infoTemplate");
			Archive_tableInfoPO archive_tableInfoPO = new Archive_tableInfoPO();
			String fieldtid = AOSId.uuid();
			archive_tableInfoPO.setId_(fieldtid);
			archive_tableInfoPO.setTablename(qDto.getString("tablename")
					+ "_info");
			archive_tableInfoPO.setTabledesc(qDto.getString("tabledesc")
					+ "元数据");
			archive_tableInfoMapper.insert(archive_tableInfoPO);
			qDto.put("tid", "467927e3c456465fa1c8fc595f026522");
			List<Archive_tablefieldlistPO> listfield = archive_tablefieldlistMapper
					.list(qDto);
			for (int i = 0; i < listfield.size(); i++) {
				listfield.get(i).setId_(AOSId.uuid());
				listfield.get(i).setTid(fieldtid);
				// listfield.get(i).setFieldview(1);
				archive_tablefieldlistMapper.insert(listfield.get(i));

			}
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out = false;
		}

		return out;
	}

	/**
	 * 
	 * 删除数据表信息
	 * 
	 * @param qDto
	 * @return
	 */
	@SuppressWarnings("finally")
	@Transactional
	public int deleteTable(Dto qDto) {
		String[] selections = qDto.getRows();
		int rows = 0;
		for (String id_ : selections) {
			archive_tablenameMapper.deleteByKey(id_);
			rows++;
		}
		try {
			jdbcTemplate.execute("drop table " + qDto.getString("tablename"));
			jdbcTemplate.execute("drop table " + qDto.getString("tablename")
					+ "_path");
			//备份表删除
			jdbcTemplate.execute("drop table " + qDto.getString("tablename")
					+ "_backup");
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			return rows;
		}

	}

	/**
	 * 
	 * 保存数据表字段信息
	 * 
	 * @param qDto
	 * @return
	 */
	public Dto saveField(Dto qDto) {
		Dto outDto = Dtos.newDto();
		Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
		AOSUtils.copyProperties(qDto, archive_tablefieldlistPO);
		StringBuffer sql = new StringBuffer();
		sql.append("ALTER TABLE " + qDto.getString("tablename") + " add "
				+ qDto.getString("fieldenname") + " "
				+ qDto.getString("fieldclass"));
		if (qDto.getString("fieldclass").equals("varchar")) {
			sql.append(" (" + qDto.getString("fieldsize") + ")");
		}
		if (!jdbcExcel(sql.toString())) {
			outDto.setAppCode(AOSCons.ERROR);
			String msg = AOSUtils.merge("数据库中没有此表或字段[{0}]己存在 ,请重新输入。",
					archive_tablefieldlistPO.getFieldenname());
			outDto.setAppMsg(msg);
			return outDto;
		}
		if (!checkFieldName(archive_tablefieldlistPO.getFieldenname(),
				archive_tablefieldlistPO.getTid())) {
			outDto.setAppCode(AOSCons.ERROR);
			String msg = AOSUtils.merge("字段[{0}]己存在,请重新输入。",
					archive_tablefieldlistPO.getFieldenname());
			outDto.setAppMsg(msg);
			return outDto;
		}
		archive_tablefieldlistPO.setId_(AOSId.uuid());
		archive_tablefieldlistMapper.insert(archive_tablefieldlistPO);

		//此时如果存在备份表在备份表中添加字段
		AddBackupFieldList(qDto);
		String msg = AOSUtils.merge("字段[{0}]添加成功 。",
				archive_tablefieldlistPO.getFieldenname());
		outDto.setAppMsg(msg);
		return outDto;
	}

	/**
	 * 临时表字段添加
	 * @param qDto
	 */
	private boolean AddBackupFieldList(Dto qDto) {
		StringBuffer sql = new StringBuffer();
		sql.append("ALTER TABLE " + qDto.getString("tablename") + "_backup add "
				+ qDto.getString("fieldenname") + " "
				+ qDto.getString("fieldclass"));
		if (qDto.getString("fieldclass").equals("varchar")) {
			sql.append(" (" + qDto.getString("fieldsize") + ")");
		}
		try{
			jdbcTemplate.execute(sql.toString());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 
	 * 修改数据表字段保存
	 * 
	 * @param qDto
	 * @return
	 */
	@Transactional
	public Dto updateField(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String filedenname = qDto.getString("fieldenname");
		Archive_tablefieldlistPO check_archive_tablefieldlistPO = archive_tablefieldlistMapper
				.selectByKey(qDto.getString("id_"));
		if (!filedenname
				.equals(check_archive_tablefieldlistPO.getFieldenname())) {
			if (!checkFieldName(filedenname, qDto.getString("tid"))) {
				outDto.setAppCode(AOSCons.ERROR);
				outDto.setAppMsg(AOSUtils.merge("字段[{0}]己存在,请重新输入。",
						filedenname));
				return outDto;
			}
		}
		// 修改字段长度
		if (!updateFieldSize(qDto)) {
			outDto.setAppCode(AOSCons.ERROR);
			outDto.setAppMsg(AOSUtils.merge("修改数据库字段[{0}]异常,请重新输入。",
					filedenname));
			return outDto;
		}
		String sql = "exec sp_rename '" + qDto.getString("tablename") + "."
				+ check_archive_tablefieldlistPO.getFieldenname() + "','"
				+ filedenname + "', 'COLUMN'";
		if (!jdbcExcel(sql)) {
			outDto.setAppCode(AOSCons.ERROR);
			outDto.setAppMsg(AOSUtils.merge("数据库字段[{0}]不存在或已存在,请重新输入。",
					filedenname));
			return outDto;
		}

		Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
		AOSUtils.copyProperties(qDto, archive_tablefieldlistPO);
		archive_tablefieldlistMapper.updateByKey(archive_tablefieldlistPO);
		//备份表修改字段信息
		updateBackupFieldSize(qDto);

		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，字段修改成功。");
		return outDto;
	}

	/**
	 * 修改备份表字段信息
	 * @param qDto
	 */
	private boolean updateBackupFieldSize(Dto qDto) {
		String sql = "alter table " + qDto.getString("tablename")
				+ "_backup alter column " + qDto.getString("fieldenname")
				+ " varchar(" + qDto.getString("fieldsize") + ")";
		try {
			jdbcTemplate.execute(sql);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 删除数据表字段
	 * 
	 * @param qDto
	 * @return
	 */
	@Transactional
	public int deleteField(Dto qDto) {
		String[] selections = qDto.getRows();
		int rows = 0;
		String sql = " ALTER TABLE " + qDto.getString("tablename")
				+ " drop column " + qDto.getString("fieldenname");
		for (String id_ : selections) {
			archive_tablefieldlistMapper.deleteByKey(id_);
			jdbcExcel(sql);
			rows++;
		}
		//备份表删除字段
		String sql2 = " ALTER TABLE " + qDto.getString("tablename")
				+ "_backup drop column " + qDto.getString("fieldenname");
		for (String id_ : selections) {
			jdbcExcel(sql2);
		}

		return rows;
	}

	/**
	 * 
	 * 检测数据表字段的唯一性
	 * 
	 * @param fieldName
	 * @param tid
	 * @return
	 */
	public boolean checkFieldName(String fieldName, String tid) {
		Dto calcDto = Dtos.newCalcDto("count(id_)");
		calcDto.put("fieldenname", fieldName);
		calcDto.put("tid", tid);
		Integer fieldenanameInteger = Integer
				.valueOf(archive_tablefieldlistMapper.calc(calcDto));
		boolean out = true;
		if (fieldenanameInteger > 0) {
			out = false;
		}
		return out;
	}

	/**
	 * 
	 * 重置（创建数据表）
	 * 
	 * @param qDto
	 * @return
	 */
	public Dto resetTable(Dto qDto) {
		Dto outDto = Dtos.newDto();
		//将要重置表的数据添加到备份表中
		try{
			String sql2="delete from "+qDto.getString("tablename")+"_backup";
			jdbcTemplate.execute(sql2);
			String sql3="insert into "+qDto.getString("tablename")+"_backup select * from "+qDto.getString("tablename");
			jdbcTemplate.execute(sql3);
		}catch(Exception e){
			e.printStackTrace();
		}
		List<Archive_tablenamePO> list = archive_tablenameMapper.list(qDto);
		for (int i = 0; i < list.size(); i++) {
			//删除表数据，在删除表
			deleteDbTableName(list.get(i).getTablename());
			//得到字段信息
			String strfield = creatDbTable(list.get(i).getId_());
			//创建表
			String sql = " create table " + list.get(i).getTablename()
					+ " (id_ nvarchar(192) primary key," + strfield + ")";
			jdbcTemplate.execute(sql);
		}
		outDto.setAppCode(AOSCons.SUCCESS);
		String msg = AOSUtils.merge("数据表重置成功。");
		outDto.setAppMsg(msg);
		return outDto;

	}

	/**
	 * 
	 * 删除数据库己存在的表
	 * 
	 * @param tablename
	 * @return
	 */
	public boolean deleteDbTableName(String tablename) {
		int count = jdbcTemplate
				.queryForInt("SELECT COUNT(*) FROM dbo.sysobjects WHERE name= '"
						+ tablename + "'");
		boolean out = true;
		if (count > 0) {
			String sqlDrop = " drop table " + tablename;
			jdbcTemplate.execute(sqlDrop);
			out = false;
		}
		return out;
	}

	public String creatDbTable(String tid) {
		Dto pDto = Dtos.newDto();
		pDto.put("tid", tid);
		List<Archive_tablefieldlistPO> listfield = archive_tablefieldlistMapper
				.list(pDto);
		String querysql = "";
		String strsql = "";
		for (int j = 0; j < listfield.size(); j++) {
			String fieldenname = listfield.get(j).getFieldenname();
			String fieldclass = listfield.get(j).getFieldclass();
			int fieldsize = listfield.get(j).getFieldsize();

			strsql = fieldenname + " " + fieldclass + "(" + fieldsize + ")";
			if (fieldclass.equals("int")) {
				strsql = fieldenname + " " + fieldclass;
			}
			if (fieldclass.equals("datetime")) {
				strsql = fieldenname + " " + fieldclass;
			}
			if (querysql.equals("")) {
				querysql = strsql;
			} else
				querysql = querysql + "," + strsql;
		}
		return querysql;
	}

	/**
	 * 
	 * JDBC执行语句
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("finally")
	public boolean jdbcExcel(String sql) {
		boolean out = true;
		try {
			jdbcTemplate.execute(sql);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			out = false;
		} finally {
			return out;
		}

	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 
	 * 查询设置顺序信息
	 * 
	 * @param pDto
	 * @return
	 */
	public List<Archive_tablefieldlistPO> listOrderInfos(Dto pDto) {
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getOrderFields(pDto.getString("tid"));
		return list;
	}

	/**
	 * 修改字段顺序的索引值
	 * 
	 * @param dto
	 * @return
	 */
	public boolean updateField_index(Dto dto) {
		// TODO Auto-generated method stub
		String zdid = dto.getString("zdid_");
		String[] zds = zdid.split(",");
		int indx = 0;
		if (zds.length > 0 && zds != null) {
			for (int i = 0; i < zds.length; i++) {
				String sql = "update archive_TableFieldList set indx='" + indx
						+ "' where  id_='" + zds[i] + "'";
				jdbcTemplate.update(sql);
				indx = indx + 10;
			}
			return true;
		}
		return false;
	}

	// 修改字段长度
	public boolean updateFieldSize(Dto qDto) {
		String sql = "alter table " + qDto.getString("tablename")
				+ " alter column " + qDto.getString("fieldenname")
				+ " varchar(" + qDto.getString("fieldsize") + ")";
		try {
			jdbcTemplate.execute(sql);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 得到字段list集合
	 * 
	 * @author PX
	 * @param dto
	 * 
	 *            2019-3-6
	 * @return
	 */
	public String gettablefieldlist(Dto dto) {
		// TODO Auto-generated method stub
		String tablename_id = dto.getString("tablename_id");
		String sql = "select FieldEnName,FieldCnName from archive_TableFieldList where tid ='"
				+ tablename_id + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		String stringenname = "";
		String stringcnname = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String fieldenname = list.get(i).get("FieldEnName") + "";
				String fieldcnname = list.get(i).get("FieldCnName") + "";
				if (i == 0) {
					stringcnname += fieldcnname;
					stringenname += fieldenname;
				} else {
					stringcnname += "," + fieldcnname;
					stringenname += "," + fieldenname;
				}

			}
		}
		String stringnameString = stringenname + ";" + stringcnname;
		return stringnameString;
	}

	/**
	 * 添加关联
	 * 
	 * @author PX
	 * @param dto
	 * 
	 *            2019-3-11
	 * @return
	 */
	public Dto addrelative(Dto dto) {
		// TODO Auto-generated method stub
		Dto outDto = Dtos.newDto();
		// 源表id
		String sourcetablename_id = dto.getString("sourcetablename_id");
		// 目的表id
		String objectivetablename_id = dto.getString("objectivetablename_id");
		// 源表名
		String sourcetablename = "";
		// 目的表名
		String objectivetablename = "";
		// 查询到源表名
		String sql = "select TableName from archive_TableName where id_='"
				+ sourcetablename_id + "'";
		List<Map<String, Object>> sourlist = jdbcTemplate.queryForList(sql);
		if (sourlist != null && sourlist.size() > 0) {
			sourcetablename = sourlist.get(0).get("TableName") + "";
		}
		// 查询到目的表名
		String sql2 = "select TableName from archive_TableName where id_='"
				+ objectivetablename_id + "'";
		List<Map<String, Object>> objectlist = jdbcTemplate.queryForList(sql2);
		if (objectlist != null && objectlist.size() > 0) {
			objectivetablename = objectlist.get(0).get("TableName") + "";
		}
		// 源字段
		String sourcefieldall = dto.getString("sourcefieldall");
		// 目的字段
		String objectfieldall = dto.getString("objectfieldall");
		if (sourcefieldall == null || objectfieldall == null) {

		} else {
			String[] source = sourcefieldall.split(";");
			String[] object = objectfieldall.split(";");
			if (source.length == object.length) {
				for (int i = 0; i < source.length; i++) {
					String sourString = source[i];
					String objectString = object[i];
					String spid = "";
					String opid = "";
					// 根据name名称得到id值
					String sql4 = "select id_ from archive_TableFieldList where tid='"
							+ sourcetablename_id
							+ "' and FieldEnName='"
							+ sourString + "'";
					List<Map<String, Object>> slist = jdbcTemplate
							.queryForList(sql4);
					if (slist != null && slist.size() > 0) {
						for (int k = 0; k < slist.size(); k++) {
							spid = slist.get(0).get("id_") + "";
						}
					}
					// 根据name名称得到id值
					String sql5 = "select id_ from archive_TableFieldList where tid='"
							+ objectivetablename_id
							+ "' and FieldEnName='"
							+ objectString + "'";
					List<Map<String, Object>> olist = jdbcTemplate
							.queryForList(sql5);
					if (olist != null && olist.size() > 0) {
						for (int r = 0; r < olist.size(); r++) {
							opid = olist.get(0).get("id_") + "";
						}
					}
					try {
						String sql3 = "insert into archive_JOIN(s_field,s_table,t_field,t_table) values('"
								+ spid
								+ "','"
								+ sourcetablename
								+ "','"
								+ opid
								+ "','" + objectivetablename + "')";
						jdbcTemplate.execute(sql3);
					} catch (Exception e) {
						// TODO: handle exception
						// 得到错误信息
						e.printStackTrace();
						outDto.setAppMsg(e.getMessage());
						outDto.setAppCode(-1);
						return outDto;
					}
				}
			}
		}
		outDto.setAppMsg("添加成功");
		outDto.setAppCode(1);
		return outDto;
	}

	/**
	 * 得到关联列表
	 * 
	 * @author PX
	 * @param outDto
	 * 
	 *            2019-3-11
	 * @return 
	 */
	public List<Map<String, Object>> listcorrelation(Dto outDto) {
		// TODO Auto-generated method stub
		String sql = "select a.id as id,b.TableDesc as s_table,c.TableDesc as t_table,d.FieldCnName as s_field,e.FieldCnName as t_field  from archive_JOIN  a, archive_TableName  b,  archive_TableName  c,archive_TableFieldList  d,archive_TableFieldList  e  where  a.s_field=d.id_  and a.t_field =e.id_ and a.s_table=b.TableName and a.t_table=c.TableName ";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	public Dto deletecorrelation(Dto dto) {
		Dto outDto=Dtos.newOutDto();
		String ids_=dto.getString("ids_");
		int del=0;
		if(ids_!=null){
			String[] ids=ids_.split(",");
			for(int i=0;i<ids.length;i++){
				try {
					String id=ids[i];
					String sql="delete from archive_JOIN where id='"+id+"'";
					jdbcTemplate.execute(sql);
					del=del+1;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		String msg = "操作完成，";
		if (del > 0) {
			msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
		}
		outDto.setAppMsg(msg);
		return outDto;
	}

	/**
	 * 得到字段名称
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> getFieldname(Dto dto) {
		List<Map<String, Object>> listfieldName=new ArrayList<Map<String, Object>>();
		String tablename=dto.getString("tablename");
		String sql="select FieldCnName,FieldEnName from archive_tableFieldList where tid in(select id_ from archive_tableName where TableName='"+tablename+"')";
		List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("sourcetablefield",list.get(0).get("FieldCnName"));
			    map.put("sourcetablefield_value",list.get(0).get("FieldEnName"));
				map.put("fieldenname",list.get(0).get("FieldEnName"));
			    map.put("fieldcnname",list.get(0).get("FieldCnName"));
				listfieldName.add(map);
		}
		return listfieldName;
	}
	/**
	 * 得到字段名称
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> getFieledlist(Dto dto) {
		List<Map<String, Object>> listfieldName=new ArrayList<Map<String, Object>>();
		String sourcetablename_id=dto.getString("sourcetablename_id");
		String objectivetablename_id=dto.getString("objectivetablename_id");
		String sourcetablenameString=dto.getString("sourcetablenameString");
		String sourcetablefield=dto.getString("sourcetablefield");
		String objectivetablenameString=dto.getString("objectivetablenameString");
		String objectivetablfield=dto.getString("objectivetablfield");
		if(sourcetablenameString.length()>0&&sourcetablefield.length()>0&&objectivetablenameString.length()>0&&objectivetablfield.length()>0){
			int length=sourcetablenameString.split(",").length;
			for (int t=0;t<length;t++){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("sourcetablenameString",sourcetablenameString.split(",")[t]);
				map.put("objectivetablenameString",objectivetablenameString.split(",")[t]);
				map.put("sourcetablefield",sourcetablefield.split(",")[t]);
				map.put("objectivetablfield",objectivetablfield.split(",")[t]);
				map.put("sourcefieldenname",sourcetablefield.split(",")[t]);
				map.put("sourcefieldcnname",sourcetablenameString.split(",")[t]);
				map.put("objectivefieldenname",objectivetablfield.split(",")[t]);
				map.put("objectivefieldcnname",objectivetablenameString.split(",")[t]);
				listfieldName.add(map);
			}
		}
		String sql="select FieldCnName,FieldEnName from archive_tableFieldList   where tid='"+sourcetablename_id+"'";
		String sql2="select FieldCnName,FieldEnName from archive_tableFieldList where tid='"+objectivetablename_id+"'";
		List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
		List<Map<String, Object>> list2= jdbcTemplate.queryForList(sql2);
		if(list!=null&&list.size()>0&&list2!=null&&list2.size()>0){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("sourcetablenameString",list.get(0).get("FieldEnName"));
			map.put("objectivetablenameString",list2.get(0).get("FieldEnName"));
			map.put("sourcetablefield",list.get(0).get("FieldCnName"));
			map.put("objectivetablfield",list2.get(0).get("FieldCnName"));
			map.put("sourcefieldenname",list.get(0).get("FieldEnName"));
			map.put("sourcefieldcnname",list.get(0).get("FieldCnName"));
			map.put("objectivefieldenname",list2.get(0).get("FieldEnName"));
			map.put("objectivefieldcnname",list2.get(0).get("FieldCnName"));
			listfieldName.add(map);
		}
		return listfieldName;
	}
	/**
	 * 得到字段名称
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> getFieldname2(Dto dto) {
		List<Map<String, Object>> listfieldName=new ArrayList<Map<String, Object>>();
		String tablename=dto.getString("tablename");
		String sql="select FieldCnName,FieldEnName from archive_tableFieldList where tid in(select id_ from archive_tableName where TableName='"+tablename+"')";
		List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("sourcetablefield_value",list.get(0).get("FieldEnName"));
			map.put("targetTemplate",list.get(0).get("FieldCnName"));
			listfieldName.add(map);
		}
		return listfieldName;
	}
	public List<Archive_tablefieldlistPO> listComboBox(String tablename){
		return archive_tablefieldlistMapper.getTableFields(tablename);
	}
	/**
	 * 添加索引
	 * @return
	 */
	public Integer saveIndex(Dto outDto) {
		String tablename =outDto.getString("tablename");
		String tablefieldlist =outDto.getString("tablefieldlist");
		String tablefieldlist_cn =outDto.getString("tablefieldlist_cn");
		//1.根据名称查询看看索引是否存在，如果存在了就不让用户添加
		String sql2="select * from archive_INDEX where tablename ='"+tablename+"' and indexdata_en='"+tablefieldlist+"'";
		List<Map<String,Object>> list =jdbcTemplate.queryForList(sql2);
		if(list.size()>0){
			return -1;
		}
		try{
			String sql="insert into archive_INDEX(id,indexdata_en,indexdata_cn,tablename) values('"+AOSId.uuid()+"','"+tablefieldlist+"','"+tablefieldlist_cn+"','"+tablename+"')";
			jdbcTemplate.execute(sql);
			return 1;
		}catch (Exception e){
			e.printStackTrace();
			return -2;
		}
	}
	/**
	 * 获取索引
	 * @param qDto
	 * @return
	 */
	public List<Map<String,Object>> getIndex(Dto qDto) {
		String tablename =qDto.getString("tablename");
		String sql="select * from archive_INDEX where tablename='"+tablename+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		//return archive_tablefieldlistMapper.getTableFields(tablename);
		return list;
	}

	/**
	 * 删除索引
	 * @param qDto
	 * @return
	 */
	public boolean deleteIndex(Dto qDto) {
		String tablename =qDto.getString("tablename");
		String indexdata_en =qDto.getString("indexdata_en");
		String sql="delete from archive_INDEX where tablename='"+tablename+"' and indexdata_en='"+indexdata_en+"'";
		try{
			jdbcTemplate.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据选择的表名的id值，得到字段下拉列表
	 * @param qDto
	 * @return
	 */
	public List<Map<String, Object>> sourcelistComboBox(Dto qDto) {

		String sql="select fieldenname as sourcefieldenname,fieldcnname as sourcefieldcnname from archive_tablefieldlist where tid='"+qDto.getString("tid")+"'";
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 根据选择的表名的id值，得到字段下拉列表
	 * @param qDto
	 * @return
	 */
	public List<Map<String, Object>> objectivelistComboBox(Dto qDto) {
		String sql="select fieldenname as objectivefieldenname,fieldcnname as objectivefieldcnname from archive_tablefieldlist where tid='"+qDto.getString("tid")+"'";
		return jdbcTemplate.queryForList(sql);

	}

	/**
	 * 还原数据表
	 * @param inDto
	 * @return
	 */
	public Dto returnTable(Dto inDto) {
		Dto outDto = Dtos.newDto();
		//将要重置表的数据添加到备份表中
		try{
			String sql2="delete from "+inDto.getString("tablename");
			jdbcTemplate.execute(sql2);
			String sql3="insert into "+inDto.getString("tablename")+" select * from "+inDto.getString("tablename")+"_backup";
			jdbcTemplate.execute(sql3);
			outDto.setAppCode(AOSCons.SUCCESS);
			String msg = AOSUtils.merge("数据表还原成功。");
			outDto.setAppMsg(msg);
			return outDto;
		}catch(Exception e){
			e.printStackTrace();
			outDto.setAppCode(AOSCons.ERROR);
			String msg = AOSUtils.merge("数据表还原失败。");
			outDto.setAppMsg(msg);
			return outDto;
		}
	}
}
