package cn.osworks.aos.system.modules.service.depot;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Depot_lyxgMapper;
import cn.osworks.aos.system.dao.po.LYXG_PO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

//利用效果逻辑(service)层
@Service
public class LYXGService  extends JdbcDaoSupport {


    @Autowired
    private Depot_lyxgMapper depot_lyxgMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    //新增方法
    public Dto saveDepotIn(Dto inDto) {
        Dto outDto = Dtos.newDto();
        LYXG_PO lyxg_po = new LYXG_PO();
        AOSUtils.copyProperties(inDto, lyxg_po);
        lyxg_po.setId_(AOSId.uuid());
        depot_lyxgMapper.insert(lyxg_po);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，添加成功！！！");
        return outDto;
    }

    //删除方法
    public int deleteDepotin(Dto inDto) {
        String[] selections = inDto.getRows();
        int rows = 0;
        for (String id_ : selections) {
            depot_lyxgMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    //修改方法
    public Dto updateDepotIn(Dto inDto) {
        Dto outDto = Dtos.newDto();
        LYXG_PO lyxg_po = new LYXG_PO();
        AOSUtils.copyProperties(inDto, lyxg_po);
        depot_lyxgMapper.updateByKey(lyxg_po);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public String getLyxgIndex(Dto outDto) {
        String nd=outDto.getString("nd");
        String sql="select sxh from lyxg where 1=1 and nd='"+nd+"' and sxh like 'XG%' order by sxh";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("sxh");
            String index = dh.substring(dh.length()-3, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%3d", h).replace(" ", "0");
        }else {
            return "001";
        }
    }
}
