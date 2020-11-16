package cn.osworks.aos.system.dao.mapper;

import java.util.List;


import cn.osworks.aos.core.annotation.Mapper;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Archive_tableinputPO;

/**
 * <b>archive_TableInput[archive_tableinput]数据访问接口</b>
 * 
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改
 * </p>
 * 
 * @author AHei
 * @date 2017-07-31 10:28:15
 */
@Mapper
public interface Archive_tableinputMapper {

	/**
	 * 插入一个数据持久化对象(插入字段为传入PO实体的非空属性)
	 * <p> 防止DB字段缺省值需要程序中再次赋值
	 *
	 * @param archive_tableinputPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insert(Archive_tableinputPO archive_tableinputPO);
	
	/**
	 * 插入一个数据持久化对象(含所有字段)
	 * 
	 * @param archive_tableinputPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insertAll(Archive_tableinputPO archive_tableinputPO);

	/**
	 * 根据主键修改数据持久化对象
	 * 
	 * @param archive_tableinputPO
	 *            要修改的数据持久化对象
	 * @return int 返回影响行数
	 */
	int updateByKey(Archive_tableinputPO archive_tableinputPO);

	/**
	 * 根据主键查询并返回数据持久化对象
	 * 
	 * @return Archive_tableinputPO
	 */
	Archive_tableinputPO selectByKey();

	/**
	 * 根据唯一组合条件查询并返回数据持久化对象
	 * 
	 * @return Archive_tableinputPO
	 */
	Archive_tableinputPO selectOne(Dto pDto);

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Archive_tableinputPO>
	 */
	List<Archive_tableinputPO> list(Dto pDto);

	/**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Archive_tableinputPO>
	 */
	List<Archive_tableinputPO> listPage(Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Archive_tableinputPO>
	 */
	List<Archive_tableinputPO> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Archive_tableinputPO>
	 */
	List<Archive_tableinputPO> likePage(Dto pDto);

	/**
	 * 根据主键删除数据持久化对象
	 *
	 * @return 影响行数
	 */
	int deleteByKey();
	
	
	/**
	 * 根据主键删除数据持久化对象
	 *
	 * @return 影响行数
	 */
	int deleteByFieldname(String fieldname);
	
	/**
	 * 根据Dto统计行数
	 * 
	 * @param pDto
	 * @return
	 */
	int rows(Dto pDto);
	
	
	/**
	 * 查询表返回ID最大值
	 * 
	 * @param pDto
	 * @return
	 */
	int getMaxId(Dto pDto);
	
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	String calc(Dto pDto);
	
	
	/**
	 * 
	 * 
	 * 数据源下拉框
	 * 
	 * @param tablename
	 * @return
	 */
	List<Archive_tableinputPO> getFieldsCombo(String tablename);

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 *
	 * @return List<Archive_tableinputPO>
	 */
	List<Archive_tableinputPO> listColumn(Dto pDto);
}
