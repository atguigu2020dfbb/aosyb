package cn.osworks.aos.system.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.osworks.aos.core.annotation.Mapper;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Checkup_topic_tablenameidPO;

/**
 * <b>checkup_topic_tablenameid[checkup_topic_tablenameid]数据访问接口</b>
 * 
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改
 * </p>
 * 
 * @author Shq
 * @date 2020-08-21 14:03:11
 */
@Mapper
public interface Checkup_topic_tablenameidMapper {

	/**
	 * 插入一个数据持久化对象(插入字段为传入PO实体的非空属性)
	 * <p> 防止DB字段缺省值需要程序中再次赋值
	 *
	 * @param checkup_topic_tablenameidPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insert(Checkup_topic_tablenameidPO checkup_topic_tablenameidPO);
	
	/**
	 * 插入一个数据持久化对象(含所有字段)
	 * 
	 * @param checkup_topic_tablenameidPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insertAll(Checkup_topic_tablenameidPO checkup_topic_tablenameidPO);

	/**
	 * 根据主键修改数据持久化对象
	 * 
	 * @param checkup_topic_tablenameidPO
	 *            要修改的数据持久化对象
	 * @return int 返回影响行数
	 */
	int updateByKey(Checkup_topic_tablenameidPO checkup_topic_tablenameidPO);

	/**
	 * 根据主键查询并返回数据持久化对象
	 * 
	 * @return Checkup_topic_tablenameidPO
	 */
	Checkup_topic_tablenameidPO selectByKey(@Param(value = "id_") String id_);

	/**
	 * 根据唯一组合条件查询并返回数据持久化对象
	 * 
	 * @return Checkup_topic_tablenameidPO
	 */
	Checkup_topic_tablenameidPO selectOne(Dto pDto);

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Checkup_topic_tablenameidPO>
	 */
	List<Checkup_topic_tablenameidPO> list(Dto pDto);

	/**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Checkup_topic_tablenameidPO>
	 */
	List<Checkup_topic_tablenameidPO> listPage(Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Checkup_topic_tablenameidPO>
	 */
	List<Checkup_topic_tablenameidPO> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Checkup_topic_tablenameidPO>
	 */
	List<Checkup_topic_tablenameidPO> likePage(Dto pDto);

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

	void deleteTopic_tablenameid(String id_);
}
