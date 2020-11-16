package cn.osworks.aos.system.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.osworks.aos.core.annotation.Mapper;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Gdws_temporaryPO;

/**
 * <b>gdws_temporary[gdws_temporary]数据访问接口</b>
 * 
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改
 * </p>
 * 
 * @author Shq
 * @date 2020-09-14 18:48:14
 */
@Mapper
public interface Gdws_temporaryMapper {

	/**
	 * 插入一个数据持久化对象(插入字段为传入PO实体的非空属性)
	 * <p> 防止DB字段缺省值需要程序中再次赋值
	 *
	 * @param gdws_temporaryPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insert(Gdws_temporaryPO gdws_temporaryPO);
	
	/**
	 * 插入一个数据持久化对象(含所有字段)
	 * 
	 * @param gdws_temporaryPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insertAll(Gdws_temporaryPO gdws_temporaryPO);

	/**
	 * 根据主键修改数据持久化对象
	 * 
	 * @param gdws_temporaryPO
	 *            要修改的数据持久化对象
	 * @return int 返回影响行数
	 */
	int updateByKey(Gdws_temporaryPO gdws_temporaryPO);

	/**
	 * 根据主键查询并返回数据持久化对象
	 * 
	 * @return Gdws_temporaryPO
	 */
	Gdws_temporaryPO selectByKey(@Param(value = "id_") String id_);

	/**
	 * 根据唯一组合条件查询并返回数据持久化对象
	 * 
	 * @return Gdws_temporaryPO
	 */
	Gdws_temporaryPO selectOne(Dto pDto);

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Gdws_temporaryPO>
	 */
	List<Gdws_temporaryPO> list(Dto pDto);

	/**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Gdws_temporaryPO>
	 */
	List<Gdws_temporaryPO> listPage(Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Gdws_temporaryPO>
	 */
	List<Gdws_temporaryPO> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Gdws_temporaryPO>
	 */
	List<Gdws_temporaryPO> likePage(Dto pDto);

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
