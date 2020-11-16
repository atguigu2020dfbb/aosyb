package cn.osworks.aos.system.modules.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSCxt;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.system.dao.po.Aos_sys_paramPO;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.dao.asset.DBDialectUtils;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.asset.DicCons;
import cn.osworks.aos.system.dao.po.Aos_sys_modulePO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.web.tag.core.model.TreeBuilder;
import cn.osworks.aos.web.tag.core.model.TreeNode;

import com.google.common.collect.Lists;

import javax.annotation.Resource;

/**
 * 首页服务
 * 
 * @author OSWorks-XC
 * @date 2014-05-13
 */
@Service
public class IndexService extends JdbcDaoSupport {
	
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private SystemService systemService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}


	/**
	 * 获取左侧导航的菜单卡片(现在)
	 *
	 * @return
	 */
	public List<Dto> getLeftNavCradListxz(Dto inDto) {
		UserInfoVO userInfoVO = inDto.getUserInfo();
		List<Dto> cardDtos = Lists.newArrayList();
			//原始偶尔出现错误，暂时注释掉了,(用于获取什么数据库的特殊字符)
			//inDto.put("fnLength", DBDialectUtils.fnLength(sysDao.getDatabaseId()));
			inDto.put("fnLength", "LEN");
			//利用Cache缓存技术,提升性能
			Cache cache = AOSCxt.getCache(AOSCxt.CACHE.AOSSESMENUCACHE);
			Element element = cache.get(AOSCons.CACHE_PREFIX.MENU + userInfoVO.getAccount_());
			if (element != null) {
				cardDtos = (List<Dto>)element.getObjectValue();
			}else {
				cardDtos = sysDao.list("Auth.getLeftNavCradListBasedRoot", inDto);
				for (int i=0; i<cardDtos.size();i++) {
					Dto out=cardDtos.get(i);
					if(i==0){
						out.put("hidden",1);
					}else{
						out.put("hidden",0);
					}
					if(out.getString("is_auto_expand_").equals("1")){
						out.put("boolean_is_auto_expand_",false);
					}
					if(out.getString("is_auto_expand_").equals("0")){
						out.put("boolean_is_auto_expand_",true);
					}
				}
				//加入缓存
				if (cardDtos != null) {
					Element newElement = new Element(AOSCons.CACHE_PREFIX.MENU + userInfoVO.getAccount_(), cardDtos);
					cache.put(newElement);
				}
			}
		return cardDtos;
	}
	/**
	 * 获取左侧导航的菜单卡片
	 *
	 * @return
	 */
	public List<Dto> getLeftNavCradList3(Dto inDto) {
		UserInfoVO userInfoVO = inDto.getUserInfo();
		List<Dto> cardDtos = Lists.newArrayList();
		//超级用户不做任何限制
			inDto.put("user_id_", inDto.getUserInfo().getId_());
			//获取语义节点ID为5的那层菜单作为一级导航  查询卡片标题
			inDto.put("length", "5");
			List<Aos_sys_modulePO> aos_sys_modulePOs = systemService.getBizModulesOfUser(inDto);
			/*for (Aos_sys_modulePO aos_sys_modulePO : aos_sys_modulePOs) {
				cardDtos.add(aos_sys_modulePO.toDto());
			}*/
			for (int i=0; i<aos_sys_modulePOs.size();i++) {
				Aos_sys_modulePO aos_sys_modulePO=aos_sys_modulePOs.get(i);
				if(i==0){
					aos_sys_modulePO.setHidden("1");
				}
				if(aos_sys_modulePO.getIs_auto_expand_().equals("1")){
					aos_sys_modulePO.setBoolean_is_auto_expand_(false);
				}if(aos_sys_modulePO.getIs_auto_expand_().equals("0")){
					aos_sys_modulePO.setBoolean_is_auto_expand_(true);
				}
				cardDtos.add(aos_sys_modulePO.toDto());


			}
		return cardDtos;
	}
	public List<Dto> getLeftNavCradListG(Dto inDto) {
		UserInfoVO userInfoVO = inDto.getUserInfo();
		List<Dto> cardDtos = Lists.newArrayList();
		//超级用户不做任何限制
		inDto.put("user_id_", inDto.getUserInfo().getId_());
		//获取语义节点ID为5的那层菜单作为一级导航  查询卡片标题
		inDto.put("length", "5");
		List<Aos_sys_modulePO> aos_sys_modulePOs = systemService.getBizModulesOfUserG(inDto);
			/*for (Aos_sys_modulePO aos_sys_modulePO : aos_sys_modulePOs) {
				cardDtos.add(aos_sys_modulePO.toDto());
			}*/
		for (int i=0; i<aos_sys_modulePOs.size();i++) {
			Aos_sys_modulePO aos_sys_modulePO=aos_sys_modulePOs.get(i);
			if(i==0){
				aos_sys_modulePO.setHidden("1");
			}
			if(aos_sys_modulePO.getIs_auto_expand_().equals("1")){
				aos_sys_modulePO.setBoolean_is_auto_expand_(false);
			}if(aos_sys_modulePO.getIs_auto_expand_().equals("0")){
				aos_sys_modulePO.setBoolean_is_auto_expand_(true);
			}
			cardDtos.add(aos_sys_modulePO.toDto());


		}
		return cardDtos;
	}
	/**
	 * 获取左侧导航的菜单卡片
	 *
	 * @return
	 */
	public List<Dto> getLeftNavCradList2(Dto inDto) {
		UserInfoVO userInfoVO = inDto.getUserInfo();
		List<Dto> cardDtos = Lists.newArrayList();
		//超级用户不做任何限制
		if (StringUtils.equals(DicCons.USER_TYPE_SUPER, userInfoVO.getType_())) {
			inDto.put("fnLength", DBDialectUtils.fnLength(sysDao.getDatabaseId()));

			String sql="SELECT id_  FROM aos_sys_module WHERE aos_sys_module.status_ = '1'   ORDER BY sort_no_ ASC";
			cardDtos=(List)jdbcTemplate.queryForList(sql);
			//cardDtos = sysDao.list("Auth.getLeftNavCradListBasedRoot2", inDto);
		}else {
			/*inDto.put("user_id_", inDto.getUserInfo().getId_());
			//获取语义节点ID为5的那层菜单作为一级导航  查询卡片标题
			inDto.put("length", "5");
			List<Aos_sys_modulePO> aos_sys_modulePOs = systemService.getBizModulesOfUser(inDto);
			for (Aos_sys_modulePO aos_sys_modulePO : aos_sys_modulePOs) {
				cardDtos.add(aos_sys_modulePO.toDto());
			}*/
			String sql="SELECT id_  FROM aos_sys_module WHERE aos_sys_module.status_ = '1'   ORDER BY sort_no_ ASC";
			cardDtos=(List)jdbcTemplate.queryForList(sql);
		}
		return cardDtos;
	}
	/**
	 * 获取系统导航菜单树
	 * 
	 * @param inDto
	 * @return
	 */
	public Dto getModuleTree(Dto inDto){
		Dto outDto = Dtos.newDto();
		UserInfoVO userInfoVO = inDto.getUserInfo();
		List<Aos_sys_modulePO> aos_sys_modulePOs = Lists.newArrayList();
		if (DicCons.USER_TYPE_SUPER.equals(userInfoVO.getType_())) {
			//超级用户
			//此时先从缓存中获取aos_sys_module，如果获取不到，在进行查询操作,存在不同的目录列表，不能采用缓存
				aos_sys_modulePOs = sysDao.list("Auth.getModuleTreeBasedRoot", inDto);
			//此时要第一个
		}else {
			inDto.put("user_id_", inDto.getUserInfo().getId_());
			//此时先从缓存中获取aos_sys_module，如果获取不到，在进行查询操作
			aos_sys_modulePOs = systemService.getBizModulesOfUser(inDto);
		}
		outDto.setStringA(toTreeModal(aos_sys_modulePOs));
		return outDto;
	}

	public Dto getModuleTree2(Dto inDto){
		Dto outDto = Dtos.newDto();
		UserInfoVO userInfoVO = inDto.getUserInfo();
		List<Aos_sys_modulePO> aos_sys_modulePOs = Lists.newArrayList();
		inDto.put("user_id_", inDto.getUserInfo().getId_());
		aos_sys_modulePOs = systemService.getBizModulesOfUser4(inDto);
		outDto.setStringA(toTreeModal(aos_sys_modulePOs));
		return outDto;
	}
	
	/**
	 * 将后台树结构转换为前端树模型
	 * 
	 * @param inDto
	 * @return
	 */
	private String toTreeModal(List<Aos_sys_modulePO> aos_sys_modulePOs){
		List<TreeNode> treeNodes = Lists.newArrayList();
		for (Aos_sys_modulePO aos_sys_modulePO : aos_sys_modulePOs) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(aos_sys_modulePO.getId_());
			treeNode.setText(aos_sys_modulePO.getName_());
			treeNode.setParentId(aos_sys_modulePO.getParent_id_());
			treeNode.setIcon(aos_sys_modulePO.getIcon_name_());
			treeNode.setA(aos_sys_modulePO.getUrl_());
			treeNode.setC(aos_sys_modulePO.getTablename());
			treeNode.setB(aos_sys_modulePO.getCascade_id_());
			boolean leaf = true;
			if (aos_sys_modulePO.getIs_leaf_().equals("0")) {
				leaf = false;
			}
			treeNode.setLeaf(leaf);
			boolean expanded = true;
			if (aos_sys_modulePO.getIs_auto_expand_().equals("0")) {
				expanded = false;
			}
			treeNode.setExpanded(expanded);
			treeNodes.add(treeNode);
		}
		String jsonString = TreeBuilder.build(treeNodes);
		return jsonString;
	}
	public List<Map<String,Object>> getXT(Dto dto){
		List<Dto> list=new ArrayList<Dto>();
		String parntid="";
		//1.根据用户的id得到它的所有的菜单
		String user_id_=dto.getString("user_id_");
		//得到根目录下的所有数据
		String sql2="select * from aos_sys_module where cascade_id_='0' and parent_id_='p'";
		List<Map<String,Object>> list4=jdbcTemplate.queryForList(sql2);
		if(list4!=null&&list4.size()>0){
			parntid=(String)list4.get(0).get("id_");
		}
		String sql3="select * from aos_sys_module where parent_id_='"+parntid+"' and status_='1' and id_!='7a6d3674e5204937951d01544e18e3aa' order by sort_no_";
		List<Map<String,Object>> list3=jdbcTemplate.queryForList(sql3);
		/*if(list3!=null&&list3.size()>0){
			parntid=(String)list3.get(0).get("id_");
		}
		String sql="select * from aos_sys_module where id_ in(select module_id_ from aos_sys_module_user where user_id_='"+user_id_+"' and module_id_ in ( select id_ from aos_sys_module where parent_id_='"+parntid+"'))";
		List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql);*/

		return list3;
	}
	public List<Map<String,Object>>  getListDeclared(){
		String sql="select * from archive_declared";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	public List<Map<String,Object>> listPortalNotice(Dto inDto) {
		String sql="select * from archive_declared";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}
	//获取当前电子的菜单的子菜单
	public Dto getTreeBy_id(Dto inDto) {
		Dto outDto = Dtos.newDto();
		UserInfoVO userInfoVO = inDto.getUserInfo();
		List<Aos_sys_modulePO> aos_sys_modulePOs = Lists.newArrayList();
		if (DicCons.USER_TYPE_SUPER.equals(userInfoVO.getType_())) {
			//此时需要缓存
			//超级用户不做任何限制
			aos_sys_modulePOs = sysDao.list("Auth.getModuleTreeBasedRoot", inDto);
			//此时要第一个
		}else {
			inDto.put("user_id_", inDto.getUserInfo().getId_());
			aos_sys_modulePOs = systemService.getBizModulesOfUser(inDto);
		}
		if(aos_sys_modulePOs!=null&&aos_sys_modulePOs.size()>0){
			for(int i=0;i<aos_sys_modulePOs.size();i++){
				Aos_sys_modulePO aos_sys_modulePO=aos_sys_modulePOs.get(i);
			}
		}
		outDto.setStringA(toTreeModal(aos_sys_modulePOs));
		return outDto;
	}
}
