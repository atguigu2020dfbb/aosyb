package cn.osworks.aos.system.dao.mapper;

import cn.osworks.aos.core.annotation.Mapper;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.system.dao.po.LYXG_PO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Depot_lyxgMapper {


    List<LYXG_PO> likePage(Dto pDto);

    int insert(LYXG_PO lyxg_po);

    int deleteByKey(@Param(value = "id_") String id_);

    int updateByKey(LYXG_PO lyxg_po);
}
