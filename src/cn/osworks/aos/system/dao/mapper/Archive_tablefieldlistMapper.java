package cn.osworks.aos.system.dao.mapper;

import java.util.List;

import cn.osworks.aos.core.annotation.Mapper;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;

/**
 * <b>archive_TableFieldList[archive_tablefieldlist]数据访问接口</b>
 * 
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改
 * </p>
 * 
 * @author AHei
 * @date 2016-08-25 21:00:04
 */
@Mapper
public interface Archive_tablefieldlistMapper {

	/**
	 * 插入一个数据持久化对象(插入字段为传入PO实体的非空属性)
	 * <p> 防止DB字段缺省值需要程序中再次赋值
	 *
	 * @param archive_tablefieldlistPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insert(Archive_tablefieldlistPO archive_tablefieldlistPO);
	
	/**
	 * 插入一个数据持久化对象(含所有字段)
	 * 
	 * @param archive_tablefieldlistPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insertAll(Archive_tablefieldlistPO archive_tablefieldlistPO);

	/**
	 * 根据主键修改数据持久化对象
	 * 
	 * @param archive_tablefieldlistPO
	 *            要修改的数据持久化对象
	 * @return int 返回影响行数
	 */
	int updateByKey(Archive_tablefieldlistPO archive_tablefieldlistPO);

	/**
	 * 根据主键查询并返回数据持久化对象
	 * 
	 * @return Archive_tablefieldlistPO
	 */
	Archive_tablefieldlistPO selectByKey(String tid);

	/**
	 * 根据唯一组合条件查询并返回数据持久化对象
	 * 
	 * @return Archive_tablefieldlistPO
	 */
	Archive_tablefieldlistPO selectOne(Dto pDto);

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Archive_tablefieldlistPO>
	 */
	List<Archive_tablefieldlistPO> list(Dto pDto);

	/**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Archive_tablefieldlistPO>
	 */
	List<Archive_tablefieldlistPO> listPage(Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Archive_tablefieldlistPO>
	 */
	List<Archive_tablefieldlistPO> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Archive_tablefieldlistPO>
	 */
	List<Archive_tablefieldlistPO> likePage(Dto pDto);

	/**
	 * 根据主键删除数据持久化对象
	 *
	 * @return 影响行数
	 */
	int deleteByKey(String id_);
	
	/**
	 * 根据多条件删除数据持久化对象
	 *
	 * @return 影响行数
	 */
	int deleteBy(Dto pDto);
	
	/**
	 * 根据Dto统计行数
	 * 
	 * @param pDto
	 * @return
	 */
	int rows(Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	String calc(Dto pDto);
	
	
	/**
	 * 
	 * 显示表头
	 * 
	 * @param tablename
	 * @return List<Archive_tablefieldlistPO>
	 */
	List<Archive_tablefieldlistPO> getDataFieldDisplayAll(String tablename);


	/**
	 *
	 * 显示所有表字段
	 *
	 * @param tablename
	 * @return List<Archive_tablefieldlistPO>
	 */
	List<Archive_tablefieldlistPO> getDataFieldDisplayAllAll(String tablename);
	/**
	 * 
	 * 显示表头(回收站)
	 * 
	 * @param tablename
	 * @return List<Archive_tablefieldlistPO>
	 */
	List<Archive_tablefieldlistPO> getDataFieldDisplayAll_Recovery(String tablename);
	
	
	/**
	 * 
	 * 显示元数据表头
	 * 
	 * @author Sun
	 * @param tablename
	 * @return
	 *
	 * 2018-8-14
	 */
	List<Archive_tablefieldlistPO> getInfoFieldDisplayAll(String tablename);
	
	/**
	 * 
	 * 
	 * 数据源下拉框
	 * 
	 * @param tablename
	 * @return
	 */
	List<Archive_tablefieldlistPO> getFieldsCombo(String tablename);
	
	
	/**
	 * 
	 * 重置字段
	 * 
	 * @param tablename
	 * @return
	 */
	List<Archive_tablefieldlistPO> getTableFields(String tablename);
	
	
	/**
	 * 
	 * 设置顺序字段
	 * 
	 * 
	 * @param tablename
	 * @return
	 */
	List<Archive_tablefieldlistPO> getOrderFields(String tid);
	
	
}
