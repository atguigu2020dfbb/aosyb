package cn.osworks.aos.system.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.osworks.aos.core.annotation.Mapper;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Archive_entityPO;

/**
 * <b>archive_entity[archive_entity]数据访问接口</b>
 * 
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改
 * </p>
 * 
 * @author Shq
 * @date 2019-12-27 11:10:05
 */
@Mapper
public interface Archive_entityMapper {

	/**
	 * 插入一个数据持久化对象(插入字段为传入PO实体的非空属性)
	 * <p> 防止DB字段缺省值需要程序中再次赋值
	 *
	 * @param archive_entityPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insert(Archive_entityPO archive_entityPO);
	
	/**
	 * 插入一个数据持久化对象(含所有字段)
	 * 
	 * @param archive_entityPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insertAll(Archive_entityPO archive_entityPO);

	/**
	 * 根据主键修改数据持久化对象
	 * 
	 * @param archive_entityPO
	 *            要修改的数据持久化对象
	 * @return int 返回影响行数
	 */
	int updateByKey(Archive_entityPO archive_entityPO);

	/**
	 * 根据主键查询并返回数据持久化对象
	 * 
	 * @return Archive_entityPO
	 */
	Archive_entityPO selectByKey(@Param(value = "id_") String id_);

	/**
	 * 根据唯一组合条件查询并返回数据持久化对象
	 * 
	 * @return Archive_entityPO
	 */
	Archive_entityPO selectOne(Dto pDto);

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Archive_entityPO>
	 */
	List<Archive_entityPO> list(Dto pDto);

	/**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Archive_entityPO>
	 */
	List<Archive_entityPO> listPage(Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Archive_entityPO>
	 */
	List<Archive_entityPO> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Archive_entityPO>
	 */
	List<Archive_entityPO> likePage(Dto pDto);

	/**
	 * 根据主键删除数据持久化对象
	 *
	 * @return 影响行数
	 */
	int deleteByKey(@Param(value = "id_") String id_);
	
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
	
}
