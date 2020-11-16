package cn.osworks.aos.system.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.osworks.aos.core.annotation.Mapper;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.Zlbz_temporaryPO;

/**
 * <b>zlbz_temporary[zlbz_temporary]数据访问接口</b>
 * 
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改
 * </p>
 * 
 * @author Shq
 * @date 2020-09-15 17:03:36
 */
@Mapper
public interface Zlbz_temporaryMapper {

	/**
	 * 插入一个数据持久化对象(插入字段为传入PO实体的非空属性)
	 * <p> 防止DB字段缺省值需要程序中再次赋值
	 *
	 * @param zlbz_temporaryPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insert(Zlbz_temporaryPO zlbz_temporaryPO);
	
	/**
	 * 插入一个数据持久化对象(含所有字段)
	 * 
	 * @param zlbz_temporaryPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insertAll(Zlbz_temporaryPO zlbz_temporaryPO);

	/**
	 * 根据主键修改数据持久化对象
	 * 
	 * @param zlbz_temporaryPO
	 *            要修改的数据持久化对象
	 * @return int 返回影响行数
	 */
	int updateByKey(Zlbz_temporaryPO zlbz_temporaryPO);

	/**
	 * 根据主键查询并返回数据持久化对象
	 * 
	 * @return Zlbz_temporaryPO
	 */
	Zlbz_temporaryPO selectByKey(@Param(value = "id_") String id_);

	/**
	 * 根据唯一组合条件查询并返回数据持久化对象
	 * 
	 * @return Zlbz_temporaryPO
	 */
	Zlbz_temporaryPO selectOne(Dto pDto);

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Zlbz_temporaryPO>
	 */
	List<Zlbz_temporaryPO> list(Dto pDto);

	/**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Zlbz_temporaryPO>
	 */
	List<Zlbz_temporaryPO> listPage(Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Zlbz_temporaryPO>
	 */
	List<Zlbz_temporaryPO> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Zlbz_temporaryPO>
	 */
	List<Zlbz_temporaryPO> likePage(Dto pDto);

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
