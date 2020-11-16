package cn.osworks.aos.system.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.osworks.aos.core.annotation.Mapper;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.YbrbPO;

/**
 * <b>ybrb[ybrb]数据访问接口</b>
 * 
 * <p>
 * 注意:此文件由AOS平台自动生成-禁止手工修改
 * </p>
 * 
 * @author Shq
 * @date 2020-08-12 16:40:41
 */
@Mapper
public interface YbrbMapper {

	/**
	 * 插入一个数据持久化对象(插入字段为传入PO实体的非空属性)
	 * <p> 防止DB字段缺省值需要程序中再次赋值
	 *
	 * @param ybrbPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insert(YbrbPO ybrbPO);
	
	/**
	 * 插入一个数据持久化对象(含所有字段)
	 * 
	 * @param ybrbPO
	 *            要插入的数据持久化对象
	 * @return 返回影响行数
	 */
	int insertAll(YbrbPO ybrbPO);

	/**
	 * 根据主键修改数据持久化对象
	 * 
	 * @param ybrbPO
	 *            要修改的数据持久化对象
	 * @return int 返回影响行数
	 */
	int updateByKey(YbrbPO ybrbPO);

	/**
	 * 根据主键查询并返回数据持久化对象
	 * 
	 * @return YbrbPO
	 */
	YbrbPO selectByKey(@Param(value = "id_") String id_);

	/**
	 * 根据唯一组合条件查询并返回数据持久化对象
	 * 
	 * @return YbrbPO
	 */
	YbrbPO selectOne(Dto pDto);

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<YbrbPO>
	 */
	List<YbrbPO> list(Dto pDto);

	/**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<YbrbPO>
	 */
	List<YbrbPO> listPage(Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<YbrbPO>
	 */
	List<YbrbPO> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<YbrbPO>
	 */
	List<YbrbPO> likePage(Dto pDto);

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
