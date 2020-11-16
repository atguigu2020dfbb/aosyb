package cn.osworks.aos.system.modules.service.qzj;

import cn.osworks.aos.core.asset.*;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.exception.AOSException;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.asset.DicCons;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.PageVO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.SystemService;
import cn.osworks.aos.system.modules.service.resource.ModuleService;
import cn.osworks.aos.web.tag.core.model.TreeBuilder;
import cn.osworks.aos.web.tag.core.model.TreeNode;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 页面组件服务
 * 
 * @author OSWorks-XC
 * @date 2014-07-13
 */
@Service
public class QzjService extends JdbcDaoSupport {

	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;
	@Resource
	public void setJd(JdbcTemplate jd) {
		super.setJdbcTemplate(jd);
	}
	@Autowired
	private Aos_sys_moduleMapper aos_sys_moduleMapper;
	@Autowired
	private Aos_sys_qzjMapper aos_sys_qzjMapper;
	@Autowired
	private Aos_sys_qzdwMapper aos_sys_qzdwMapper;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	public Aos_sys_module_userMapper aos_sys_module_userMapper;
	@Autowired
	public Aos_sys_qzj_catalogMapper aos_sys_qzj_catalogMapper;
	@Autowired
	private Aos_sys_userMapper aos_sys_userMapper;
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private Aos_sys_page_elMapper aos_sys_page_elMapper;

	/**
	 * 保存功能模块菜单信息
	 *
	 * @param inDto
	 */
	@Transactional
	public void saveModule(Dto inDto) {
		Aos_sys_qzjPO aos_sys_qzjPO = new Aos_sys_qzjPO();
		AOSUtils.copyProperties(inDto, aos_sys_qzjPO);
		aos_sys_qzjPO.setId_(AOSId.uuid());
		// 生成语义ID
		Dto calcDto = Dtos.newCalcDto("MAX(cascade_id_)");
		calcDto.put("parent_id_", aos_sys_qzjPO.getParent_id_());
		String maxAos_sys_modulea1 = aos_sys_qzjMapper.calc(calcDto);
		Aos_sys_qzjPO parentAos_sys_qzjPO = aos_sys_qzjMapper.selectByKey(aos_sys_qzjPO.getParent_id_());
		if (AOSUtils.isEmpty(maxAos_sys_modulea1)) {
			String temp = "0";
			if (AOSUtils.isNotEmpty(parentAos_sys_qzjPO)) {
				temp = parentAos_sys_qzjPO.getCascade_id_();
			}
			maxAos_sys_modulea1 = temp + ".000";
		}
		String cascade_id_ = AOSId.treeId(maxAos_sys_modulea1, 999);
		aos_sys_qzjPO.setCascade_id_(cascade_id_);
		String aos_sys_moduleb1 = "root";
		if (AOSUtils.isNotEmpty(parentAos_sys_qzjPO)) {
			aos_sys_moduleb1 = parentAos_sys_qzjPO.getName_();
		}
		aos_sys_qzjPO.setIs_leaf_(AOSCons.YES);
		aos_sys_qzjPO.setParent_name_(aos_sys_moduleb1);
		aos_sys_qzjMapper.insert(aos_sys_qzjPO);
		// 更新父节点是否叶子节点属性
		parentAos_sys_qzjPO.setIs_leaf_(AOSCons.NO);
		aos_sys_qzjMapper.updateByKey(parentAos_sys_qzjPO);
		//此时判断下是否自动生成数据表菜单
		//by_tablename(inDto,aos_sys_modulePO);
	}
	//批量生成固定的数据表菜单
	/*private void by_tablename(Dto inDto, Aos_sys_modulePO aos_sys_modulePO) {
		String edit_tablename_url=inDto.getString("edit_tablename_url-inputEl");
		if(edit_tablename_url=="1"||edit_tablename_url.equals("1")){
			//批量生成子节点
			//1.得到数据表名称列表
			List<Map<String,Object>> list=getTablename();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
					String tablename=(String)list.get(i).get("TableName");
					String tabledesc=(String)list.get(i).get("TableDesc");
					aos_sys_module.setId_(AOSId.uuid());
					if(i<9){
						aos_sys_module.setCascade_id_(aos_sys_modulePO.getCascade_id_()+".00"+(i+1));
					}else if(i>=9&&i<99){
						aos_sys_module.setCascade_id_(aos_sys_modulePO.getCascade_id_()+".0"+(i+1));
					}else if(i>=99){
						aos_sys_module.setCascade_id_(aos_sys_modulePO.getCascade_id_()+"."+(i+1));
					}
					aos_sys_module.setName_(tabledesc);
					aos_sys_module.setUrl_("compilation/examine/examine_by_initData.jhtml");
					aos_sys_module.setParent_id_(aos_sys_modulePO.getId_());
					aos_sys_module.setIs_auto_expand_("0");
					aos_sys_module.setStatus_("1");
					aos_sys_module.setParent_name_(aos_sys_modulePO.getName_());
					aos_sys_module.setSort_no_(i+1);
					aos_sys_module.setTablename(tablename);
					aos_sys_moduleMapper.insert(aos_sys_module);
					//菜单经办权限
					addUserModule(inDto,aos_sys_module,"1");
					//菜单管理权限
					addUserModule(inDto,aos_sys_module,"2");
				}
				//此时修改下父节点的叶子节点标识
				aos_sys_modulePO.setIs_leaf_(AOSCons.NO);
				aos_sys_moduleMapper.updateByKey(aos_sys_modulePO);
				//父菜单经办权限
				addUserModule(inDto,aos_sys_modulePO,"1");
				//父菜单管理权限
				addUserModule(inDto,aos_sys_modulePO,"2");
			}
		}
	}*/
	//跳过管理员直接赋予菜单到用户的权限
	private void addUserModule(Dto out,Aos_sys_modulePO aos_sys_modulePO,String flag) {
		//1.得到管理员账号的id值
		String rootid=getUserId("root");
		UserInfoVO userInfo=out.getUserInfo();
		Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
		aos_sys_module_userPO.setId_(AOSId.uuid());
		aos_sys_module_userPO.setModule_id_(aos_sys_modulePO.getId_());
		aos_sys_module_userPO.setUser_id_(userInfo.getId_());
		aos_sys_module_userPO.setGrant_type_(flag);
		aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
		aos_sys_module_userPO.setOperator_id_(rootid);
		aos_sys_module_userMapper.insert(aos_sys_module_userPO);
	}
	//得到用户的id值
	private String getUserId(String root) {
		String id_="";
		String sql="select * from aos_sys_user where account_='"+root+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			id_=(String)list.get(0).get("id_");
		}
		return id_;
	}

	private List<Map<String,Object>> getTablename() {
		String sql="select * from  archive_tablename where TableName not like '%_total' and TableName not like '%_receive' and TableName not like '%_temporary'";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 修改功能模块菜单信息
	 *
	 * @param inDto
	 */
	@Transactional
	public Dto updateModule(Dto inDto) {
		Dto outDto = Dtos.newDto();
		// 修改属性后的节点
		Aos_sys_qzjPO updatePO = new Aos_sys_qzjPO();
		AOSUtils.copyProperties(inDto, updatePO);
		// 修改属性前的节点
		Aos_sys_qzjPO aos_sys_qzjPO = aos_sys_qzjMapper.selectByKey(updatePO.getId_());
		if (!updatePO.getName_().equals(aos_sys_qzjPO.getName_())) {
			// 修改了菜单名称，则更新以此节点作为父节点名称的冗余字段
			Dto dto = Dtos.newDto();
			dto.put("parent_id_", updatePO.getId_());
			dto.put("parent_name_", updatePO.getName_());
			sysDao.update("Resource.updateqzjByParent_id_", dto);
		}
		if (!updatePO.getParent_id_().equals(aos_sys_qzjPO.getParent_id_())) {
			//outDto.setAppCode(1); // 是否更新了上级菜单
			// 如果修改了上级菜单，则做相应的处理。
			// 修改属性后的节点的父节点
			Aos_sys_qzjPO pPO = aos_sys_qzjMapper.selectByKey(updatePO.getParent_id_());
			Aos_sys_qzjPO updatePO2 = new Aos_sys_qzjPO();
			updatePO2.setId_(updatePO.getId_());
			updatePO2.setParent_name_(pPO.getName_());
			// 更新当前节点变更后的新父节点的名称
			aos_sys_qzjMapper.updateByKey(updatePO2);
			// 更新节点、循环处理当前节点及其所有子孙节点的语义节点ID属性
			updateAos_sys_module(aos_sys_qzjPO, updatePO);
			// 更新当前节点修改之前的父节点的是否叶子节点属性
			updateIsLeafNode(aos_sys_qzjPO.getParent_id_());
			// 更新当前节点修改之后的父节点的是否叶子节点属性
			updateIsLeafNode(updatePO.getParent_id_());
		} else {
			// 更新节点
			aos_sys_qzjMapper.updateByKey(updatePO);
		}
		return outDto;
	}

	/**
	 * 更新节点、循环处理当前节点及其所有子孙节点的语义节点ID属性
	 *
	 * @param aos_sys_qzjPO
	 *            原节点
	 * @param updatePO
	 *            新节点
	 */
	@Transactional
	public void updateAos_sys_module(Aos_sys_qzjPO aos_sys_qzjPO, Aos_sys_qzjPO updatePO) {
		Dto calcDto = Dtos.newCalcDto("MAX(cascade_id_)");
		calcDto.put("parent_id_", updatePO.getParent_id_());
		String maxAos_sys_modulea1 = aos_sys_qzjMapper.calc(calcDto);
		if (AOSUtils.isEmpty(maxAos_sys_modulea1)) {
			Aos_sys_qzjPO aos_sys_qzjPO2 = aos_sys_qzjMapper.selectByKey(updatePO.getParent_id_());
			maxAos_sys_modulea1 = aos_sys_qzjPO2.getCascade_id_() + ".000";
		}
		String new_cascade_id_ = AOSId.treeId(maxAos_sys_modulea1, 999);
		updatePO.setCascade_id_(new_cascade_id_);
		Dto qDto = Dtos.newDto();
		String old_cascade_id_ = aos_sys_qzjPO.getCascade_id_();
		qDto.put("cascade_id_", old_cascade_id_);
		List<Aos_sys_qzjPO> subNodeList = aos_sys_qzjMapper.like(qDto);
		for (Aos_sys_qzjPO po : subNodeList) {
			String cur_cascade_id_ = po.getCascade_id_();
			if (cur_cascade_id_.equals(old_cascade_id_)) {
				continue;
			}
			String cascade_id_ = StringUtils.replace(cur_cascade_id_, old_cascade_id_, new_cascade_id_);
			po.setCascade_id_(cascade_id_);
			// 更新子节点的语义ID
			aos_sys_qzjMapper.updateByKey(po);
		}
		// 更新节点
		aos_sys_qzjMapper.updateByKey(updatePO);
	}

	/**
	 * 更新该节点的是否叶子节点属性
	 *
	 * @param nodeid
	 *            节点ID
	 */
	private void updateIsLeafNode(String nodeid) {
		Dto calcDto = Dtos.newCalcDto("COUNT(id_)");
		calcDto.put("parent_id_", nodeid);
		String count = aos_sys_qzjMapper.calc(calcDto);
		Aos_sys_qzjPO aos_sys_qzjPO = new Aos_sys_qzjPO();
		aos_sys_qzjPO.setId_(nodeid);
		if (Integer.valueOf(count) > 0) {
			aos_sys_qzjPO.setIs_leaf_(AOSCons.NO);
		} else {
			aos_sys_qzjPO.setIs_leaf_(AOSCons.YES);
		}
		aos_sys_qzjMapper.updateByKey(aos_sys_qzjPO);
	}

	/**
	 * 获取功能模块树节点集合
	 *
	 * @param qDto
	 */
	public List<Dto> getModulesTree(Dto qDto) {
		List<Aos_sys_qzjPO> list = aos_sys_qzjMapper.list(qDto);
		return toTreeDto(list);
	}

	/**
	 * 删除功能模块菜单
	 *
	 * @param qDto
	 */
	@Transactional
	public Dto deleteModule(Dto qDto) {
		Dto outDto = Dtos.newDto();
		String[] selections = qDto.getRows();
		List<String> selList = Arrays.asList(selections);
		for (String id_ : selList) {
			if (id_.equals("0")) {
				outDto.setAppCode(AOSCons.ERROR);
				outDto.setAppMsg("根节点不能删除，请重新选择。");
				return outDto;
			}
		}
		int length = 100; // 语义节点ID长度最短值
		String parent_id_ = "";
		for (String id_ : selList) {
			Aos_sys_qzjPO aos_sys_qzjPO = aos_sys_qzjMapper.selectByKey(id_);
			if (aos_sys_qzjPO.getCascade_id_().length() < length) {
				parent_id_ = aos_sys_qzjPO.getParent_id_();
				length = aos_sys_qzjPO.getCascade_id_().length();
			}
		}

		for (String id_ : selList) {
			Aos_sys_qzjPO aos_sys_qzjPO = aos_sys_qzjMapper.selectByKey(id_);
			if (AOSUtils.isEmpty(aos_sys_qzjPO)) {
				continue;
			}
			Dto dto = Dtos.newDto();
			dto.put("cascade_id_", aos_sys_qzjPO.getCascade_id_());
			List<Aos_sys_qzjPO> list = aos_sys_qzjMapper.like(dto);
			for (Aos_sys_qzjPO aos_sys_qzjPO2 : list) {
				aos_sys_qzjMapper.deleteByKey(aos_sys_qzjPO2.getId_());
				updateIsLeafNode(aos_sys_qzjPO2.getParent_id_());
			}
		}
		//计算前端菜单树刷新节点ID
		Aos_sys_qzjPO aos_sys_qzjPO = aos_sys_qzjMapper.selectByKey(parent_id_);
		Dto calcDto = Dtos.newCalcDto("COUNT(id_)");
		calcDto.put("parent_id_", parent_id_);
		String count = aos_sys_qzjMapper.calc(calcDto);
		if (Integer.valueOf(count) > 0) {
			outDto.put("nodeid", aos_sys_qzjPO.getId_());
		} else {
			Aos_sys_qzjPO aos_sys_qzjPO2 = aos_sys_qzjMapper.selectByKey(aos_sys_qzjPO.getParent_id_());
			if (aos_sys_qzjPO.getId_().equals("0")) {
				outDto.put("nodeid", "0");
			}else {
				outDto.put("nodeid", aos_sys_qzjPO2.getId_());
			}
		}
		outDto.setAppMsg("操作完成，成功删除数据。");
		return outDto;
	}

	/**
	 * 将模型集合转换为模型树
	 *
	 * @param aos_sys_qzjPOs
	 * @return
	 */
	public List<Dto>toTreeDto(List<Aos_sys_qzjPO> aos_sys_qzjPOs){
		List<Dto> treeNodes = new ArrayList<Dto>();
		String iconPath = System.getProperty(AOSCons.CXT_KEY) + AOSXmlOptionsHandler.getValue("icon_path");
		for (Aos_sys_qzjPO aos_sys_qzjPO : aos_sys_qzjPOs) {
			Dto treeNode = Dtos.newDto();
			treeNode.put("id", aos_sys_qzjPO.getId_());
			treeNode.put("text", aos_sys_qzjPO.getName_());
			treeNode.put("cascade_id_", aos_sys_qzjPO.getCascade_id_());
			String icon_ = aos_sys_qzjPO.getIcon_name_();
			if (AOSUtils.isNotEmpty(icon_)) {
				treeNode.put("icon", iconPath + icon_);
			}
			String is_leaf_ = aos_sys_qzjPO.getIs_leaf_();
			if (AOSCons.YES.equals(is_leaf_)) {
				treeNode.put("leaf", true);
			} else {
				treeNode.put("leaf", false);
			}
			String is_auto_expand_ = aos_sys_qzjPO.getIs_auto_expand_();
			if (AOSCons.YES.equals(is_auto_expand_)) {
				treeNode.put("expanded", true);
			} else {
				treeNode.put("expanded", false);
			}
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

	/**
	 * 将模型集合转换为模型树
	 *
	 * @param aos_sys_qzjPOs
	 * @return
	 */
	public List<TreeNode> toTreeModal(List<Aos_sys_qzjPO> aos_sys_qzjPOs){
		List<TreeNode> treeNodes = Lists.newArrayList();
		for (Aos_sys_qzjPO aos_sys_qzjPO : aos_sys_qzjPOs) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(aos_sys_qzjPO.getId_());
			treeNode.setText(aos_sys_qzjPO.getName_());
			treeNode.setParentId(aos_sys_qzjPO.getParent_id_());
			treeNode.setIcon(aos_sys_qzjPO.getIcon_name_());
			treeNode.setA(aos_sys_qzjPO.getUrl_());
			boolean leaf = true;
			if (aos_sys_qzjPO.getIs_leaf_().equals(AOSCons.NO)) {
				leaf = false;
			}
			treeNode.setLeaf(leaf);
			boolean expanded = true;
			if (aos_sys_qzjPO.getIs_auto_expand_().equals(AOSCons.NO)) {
				expanded = false;
			}
			treeNode.setExpanded(expanded);
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

	public List<Dto> listCatalogs(Dto qDto,String query) {
		aos_sys_qzj_catalogMapper.listPage(qDto);
		//out.put("qzh","0003");
		String tid=qDto.getString("tid");
		if(tid!=null&&tid!=""){
			query=query+" and tid='"+tid+"'";
		}
		qDto.put("query",query);
		Integer limit = Integer.valueOf(qDto.getString("limit"));
		Integer page = Integer.valueOf(qDto.getString("page"));
		// 将条件存入到一个数据表中，方便与缓存查询
		String sql = "";
		String term = "";
		String fieldName;
		String enfield = "id_";
		List<Archive_tablefieldlistPO> list =getDataFieldDisplayAll("Aos_sys_qzj_catalog");
		String orderenfield = "";
		for (int i = 0; i < list.size(); i++) {
			fieldName = list.get(i).getFieldenname();
			enfield = enfield + "," + fieldName;
			if (i == 3) {
				orderenfield = enfield;
			}
		}
		orderenfield = orderenfield.substring(4);
			sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
					//+ orderenfield
					+"(ORDER BY CURRENT_TIMESTAMP)"
					+ " AS aos_rn_,"
					+ enfield
					+ " FROM "
					+ "Aos_sys_qzj_catalog"
					+ " WHERE 1=1 "
					+ query
					+ term
					+ ") "
					+ "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
					+ (limit * (page - 1) + 1)
					+ " AND "
					+ qDto.getPageLimit()
					* Integer.valueOf(qDto.getString("page"))
					+ " ORDER BY aos_rn_";
		List listDto = jdbcTemplate.queryForList(sql);
		return listDto;
	}
	/**
	 *
	 * 查询条件拼接
	 *
	 * @param qDto
	 * @return
	 */
	public String queryConditions(Dto qDto) {

		if (qDto.getString("columnnum") != "") {
			int contentLength = Integer.parseInt(qDto.getString("columnnum"));
			StringBuffer query = new StringBuffer();
			for (int i = 1; i <= contentLength + 1; i++) {
				if (qDto.getString("content" + i) != null
						&& qDto.getString("content" + i) != "") {

					if (qDto.getString("and" + i).equals("on")) {
						query.append(" and " + qDto.getString("filedname" + i));
					}
					if (qDto.getString("or" + i).equals("on")) {
						query.append(" or " + qDto.getString("filedname" + i)
								+ " " + qDto.getString("condition" + i));
					}
					if (qDto.getString("condition" + i).equals("like")) {
						query.append(" " + qDto.getString("condition" + i)
								+ " '%" + qDto.getString("content" + i) + "%'");
					}
					if (qDto.getString("condition" + i).equals("left")) {
						query.append(" like '%" + qDto.getString("content" + i)
								+ "'");
					}
					if (qDto.getString("condition" + i).equals("right")) {
						query.append(" like '" + qDto.getString("content" + i)
								+ "%'");
					}
					if (qDto.getString("condition" + i).equals("<>")) {
						query.append(" <>'" + qDto.getString("content" + i)
								+ "'");
					}
					if (qDto.getString("condition" + i).equals(">")) {
						query.append(" >'" + qDto.getString("content" + i)+ "'");
					}
					if (qDto.getString("condition" + i).equals("<")) {
						query.append(" <'" + qDto.getString("content" + i)+ "'");
					}
					if (qDto.getString("condition" + i).equals("<=")) {
						query.append(" <='" + qDto.getString("content" + i)+ "'");
					}
					if (qDto.getString("condition" + i).equals(">=")) {
						query.append(" >='" + qDto.getString("content" + i)+ "'");
					}
					if (qDto.getString("condition" + i).equals("=")) {
						query.append(" = '" + qDto.getString("content" + i)
								+ "'");
					}
				}
			}
			return query.toString();
		} else
			return "";
	}
	public boolean saveCatalog(Dto dto) {
		try{
			Aos_sys_qzj_catalogPO aos_sys_qzj_catalogPO=new Aos_sys_qzj_catalogPO();
			AOSUtils.copyProperties(dto,aos_sys_qzj_catalogPO);
			aos_sys_qzj_catalogPO.setId_(AOSId.uuid());
			aos_sys_qzj_catalogMapper.insert(aos_sys_qzj_catalogPO);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateCatalog(Dto dto) {
		try{
			Aos_sys_qzj_catalogPO aos_sys_qzj_catalogPO=new Aos_sys_qzj_catalogPO();
			AOSUtils.copyProperties(dto,aos_sys_qzj_catalogPO);
			aos_sys_qzj_catalogMapper.updateByKey(aos_sys_qzj_catalogPO);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 删除全宗目录
	 *
	 * @param qDto
	 */
	@Transactional
	public Dto deleteCatalog(Dto qDto) {
		Dto outDto = Dtos.newOutDto();
		String[] selections = qDto.getRows();
		for (String id_ : selections) {
			aos_sys_qzj_catalogMapper.deleteByKey(id_);
			// 清除授权资源
			//sysDao.delete("Resource.deleteAos_sys_page_el_grant", id_);
		}
		outDto.setAppMsg(AOSUtils.merge("操作成功，删除{0}条全宗记录。", selections.length));
		return outDto;
	}
	/**
	 * 页面初始化
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDataFieldListTitle(Dto dto) {
		String sql = "select qzh,tm,nd,jh,zrz,ys,lb,rq,bz from "+dto.getString("tablename") +" where tid='"+dto.getString("tid")+"' "+dto.getString("query");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	public List<Archive_tablefieldlistPO> getDataFieldDisplayAll(String aos_sys_qzj) {
		List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
				.getDataFieldDisplayAllAll("Aos_sys_qzj_catalog");
		return list;
	}
	/**
	 *
	 * 设置显示行数
	 *
	 * @param inDto
	 * @return
	 */
	public Dto setPagesize(Dto inDto) {
		Dto outDto = Dtos.newDto();

		Aos_sys_userPO aos_sys_user = new Aos_sys_userPO();

		AOSUtils.copyProperties(inDto, aos_sys_user);

		aos_sys_userMapper.updateByKey(aos_sys_user);

		String msg = AOSUtils.merge("操作完成，请重新登录！！！");
		outDto.setAppMsg(msg);
		return outDto;
	}
	/**
	 *
	 * 查询记录总数
	 *
	 * @param qDto
	 * @return
	 */
	public int getPageTotal(Dto qDto) {
		String query = queryConditions(qDto);
		String tid=qDto.getString("tid");
		if(tid!=null&&tid!=""){
			query=query+" and tid='"+tid+"'";
		}
		String sql = "SELECT COUNT(*) FROM " + "Aos_sys_qzj_catalog"
				+ " WHERE 1=1 " + query;
		return jdbcTemplate.queryForInt(sql);
	}
	public Object getPageSizeFromUser(String account_) {
		Dto out=Dtos.newDto();
		out.put("account_",account_);
		Aos_sys_userPO aos_sys_userPO = aos_sys_userMapper.selectOne(out);
		if(aos_sys_userPO!=null){
			return aos_sys_userPO.getPagesize();
		}else{
			return "";
		}
	}
	/**
	 *
	 * 删除单个电子文件
	 *
	 * @param qDto
	 * @return
	 */
	public Dto deletePath(Dto qDto) {
		Dto outDto = Dtos.newDto();
		Dto dto = Dtos.newDto();
		dto.put("tablename", qDto.getString("tablename"));
		int del = 1;
		String id = qDto.getString("id_");
		String tablename = qDto.getString("tablename") + "_path";
		String tableinfo = qDto.getString("tablename") + "_info";
		jdbcTemplate.execute(" delete from " + tablename + " where id_='" + id
				+ "'");
		Archive_tablenamePO archive_tablenamePO = archive_tablenameMapper
				.selectOne(dto);
		if (archive_tablenamePO.getTablemedia() != null
				&& archive_tablenamePO.getTablemedia() != "") {
			jdbcTemplate.execute(" delete from " + tableinfo + " where id_='"
					+ id + "'");
		}
		String msg = AOSUtils.merge("操作完成，成功删除信息[{0}]个", del);
		outDto.setAppMsg(msg);
		return outDto;
	}
	/**
	 *
	 * 删除全部电子文件
	 *
	 * @param qDto
	 * @return
	 */
	public Dto deletePathAll(Dto qDto) {
		Dto outDto = Dtos.newDto();
		int del = 0;
		String[] selections = qDto.getRows();
		String tablename = qDto.getString("tablename") + "_path";
		String tableinfo = qDto.getString("tablename") + "_info";
		Dto dto = Dtos.newDto();
		dto.put("tablename", qDto.getString("tablename"));
		for (String id : selections) {
			jdbcTemplate.execute(" DELETE FROM " + tablename + " WHERE tid='"
					+ id + "'");
			Archive_tablenamePO archive_tablenamePO = archive_tablenameMapper
					.selectOne(dto);
			if (archive_tablenamePO.getTablemedia() != null
					&& archive_tablenamePO.getTablemedia() != "") {
				jdbcTemplate.execute(" DELETE FROM " + tableinfo
						+ " WHERE tid='" + id + "'");
			}

			jdbcTemplate.execute(" UPDATE  " + qDto.getString("tablename")
					+ " SET _path='0' WHERE id_='" + id + "'");
			del++;
		}
		String msg = AOSUtils.merge("操作完成，成功删除", del);
		outDto.setAppMsg(msg);
		return outDto;
	}
	/**
	 * 下载电子文件
	 *
	 * @param dto
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void download_electronic_file(String filePath, Dto dto,
										 HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		// TODO Auto-generated method stub
		String filename = null;
		String filepath = null;
		String _path = null;
		// 查询文件路径信息
		List<Map<String, Object>> list = find_pathmessage(dto.getString("id_"),
				dto.getString("tablename"));
		if (list != null && list.size() > 0) {
			filename = (String) list.get(0).get("dirname");
			filepath = (String) list.get(0).get("_s_path");
			_path = (String) list.get(0).get("_path");
			response.setContentType("application/x-" + filepath.split("\\.")[1]
					+ ";charset=gb2312");
			// 名称点上后缀
			response.addHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(_path, "utf-8"));
			OutputStream outputStream = response.getOutputStream();
			try {
				FileInputStream fileInputStream = new FileInputStream(new File(
						filePath

								+ File.separator + filename + filepath));
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = fileInputStream.read(b)) != -1) {
					outputStream.write(b, 0, len);
					outputStream.flush();
				}
			} catch (Exception e) {
				throw new AOSException(e);
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException ex) {

					}
				}
			}
		}
	}
	/**
	 * 根据id查询文件详细信息
	 *
	 * @param id
	 * @param tablename
	 * @return
	 */
	public List<Map<String, Object>> find_pathmessage(String id,
													  String tablename) {
		// TODO Auto-generated method stub
		String sql = "select*from " + tablename + "_path " + " where id_='"
				+ id + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	/**
	 *
	 * 获得电子文件信息
	 *
	 * @param qDto
	 * @return
	 */
	public List<Dto> getPath(Dto qDto) {
		String tablename = qDto.getString("tablename") + "_path";
		String tid = qDto.getString("tid");
		String sql = " SELECT id_,tid,_path,dirname,sdatetime,_s_path ,RIGHT(RTRIM(_Path), CHARINDEX('.',REVERSE(RTRIM(_path))) - 1) as filetype FROM "
				+ tablename + " WHERE tid ='" + tid + "'";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto;
	}
	/**getDataFieldDisplayAll
	 * 功能：转换MapList为数组List
	 *
	 * @param list
	 * @return
	 */
	public static List convertMapListToArrayList(List list) {
		Map map = null;
		return list;
	}
	/**
	 * 获得记忆
	 * @param inDto
	 * @param userInfoVO
	 */
	public Map<String,Object> getRemember(Dto inDto, UserInfoVO userInfoVO) {
		String tablename=inDto.getString("tablename");
		String username=userInfoVO.getAccount_();
		String type=inDto.getString("type");
		String sql="select flag,name  from archive_remember where tablename='"+tablename+"' and username='"+username+"' and type='"+type+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		Map<String,Object> map=new HashMap<String,Object>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String flag=(String)list.get(i).get("flag");
				String name=(String)list.get(i).get("name");
				map.put(name,flag);
			}
		}
		return map;
	}
	/**
	 *
	 * 更新电子文件个数
	 *
	 * @param qDto
	 * @return
	 */
	public void refreshPath(Dto qDto) {
		String tablename = qDto.getString("tablename");
		String tid = qDto.getString("tid");
		String sql = " UPDATE " + tablename
				+ " set _path =(select count(id_) from " + tablename
				+ "_path where tid='" + tid + "' ) where id_='" + tid + "'";
		jdbcTemplate.execute(sql);
	}

	public String qzjjsbh() {
		String jsbh = "";
		String str = "";
		Integer index=0;
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select jsbh  from Aos_sys_qzj  order by jsbh desc ");
			if (list != null && list.size() > 0) {
				jsbh =(String) list.get(0).get("jsbh");
				//进行加一,并存到数据库中
				index=Integer.valueOf(jsbh.substring(jsbh.length()-3));
				int newindex = index + 1;
				str = String.format("%3d", newindex).replace(" ", "0");
			} else {
				str = "001";
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "001";
		}
		return str;
	}

	public Dto saveQzdw(Dto inDto){
		Dto outDto = Dtos.newDto();
		Aos_sys_qzdwPO aos_sys_qzdwPO = new Aos_sys_qzdwPO();
		AOSUtils.copyProperties(inDto,aos_sys_qzdwPO);
		aos_sys_qzdwPO.setId_(AOSId.uuid());
		aos_sys_qzdwPO.setQzlb(inDto.getString("qzlb_cn"));
		aos_sys_qzdwMapper.insert(aos_sys_qzdwPO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成,添加成功！！！");
		return outDto;
	}

	public Dto updateQzdw(Dto inDto){
		Dto outDto = Dtos.newDto();
		Aos_sys_qzdwPO aos_sys_qzdwPO = new Aos_sys_qzdwPO();
		AOSUtils.copyProperties(inDto,aos_sys_qzdwPO);
		aos_sys_qzdwPO.setQzlb(inDto.getString("qzlb_cn"));
		aos_sys_qzdwMapper.updateByKey(aos_sys_qzdwPO);
		outDto.setAppCode(AOSCons.SUCCESS);
		outDto.setAppMsg("操作完成，保存成功！！！");
		return outDto;
	}

	public int deleteQzdw(Dto inDto){
		String[] selections = inDto.getRows();
		int rows=0;
		for(String id_ :selections){
			aos_sys_qzdwMapper.deleteByKey(id_);
			rows++;
		}
		return rows;
	}
	public int updateFiled(Dto inDto){
		String[] selections = inDto.getRows();
		int rows=0;
		for(String id_ :selections){

			Aos_sys_qzdwPO aos_sys_qzdwPO=aos_sys_qzdwMapper.selectByKey(id_);
			String sql2=" select code_ from aos_sys_dic where desc_ ='"+aos_sys_qzdwPO.getQzlb()+"'  and dic_index_id_ in (SELECT  id_ FROM aos_sys_dic_index  WHERE   key_ = 'qzlb')";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
			if(list!=null&&list.size()>0){
				String sql="update "+list.get(0).get("code_")+" set qzdw='"+aos_sys_qzdwPO.getQzdw()+"' where qzh='"+aos_sys_qzdwPO.getQzh()+"'";
				jdbcTemplate.execute(sql);
				rows++;
			}
		}
		return rows;
	}

	public List<Map<String, Object>> querytables(Dto out) {
		String sql="select * from aos_sys_dic where dic_index_id_ in ( select id_ from aos_sys_dic_index where key_='"+out.getString("qzlb")+"')";
		return jdbcTemplate.queryForList(sql);
	}

    public String listAccounts(Dto inDto,HttpServletRequest request) {
		//标题列表
		String query="";
		String tabledesc=inDto.getString("tabledesc");
		String qzh=inDto.getString("qzh");
		String mlh=inDto.getString("mlh");
		String bgqx=inDto.getString("bgqx");
		String sfkf=inDto.getString("sfkf");
		if(qzh!=null&&qzh!=""){
			query=query+" and qzh='"+qzh+"'";
		}
		if(mlh!=null&&mlh!=""){
			query=query+" and mlh='"+mlh+"'";
		}
		if(bgqx!=null&&bgqx!=""){
			query=query+" and bgqx='"+bgqx+"'";
		}
		if(sfkf!=null&&sfkf!=""){
			query=query+" and sfkf='"+sfkf+"'";
		}
		String tablename="";
		String sql45="select * from archive_tablename where tabledesc='"+tabledesc+"'";
		List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql45);
		if(maps!=null&&maps.size()>0){
			tablename=(String)maps.get(0).get("tablename");
		}
		// 排列方式，页码，每页条目数
		Integer limit = Integer.valueOf(inDto.getString("limit"));
		Integer page = Integer.valueOf(inDto.getString("page"));
		// 条目开始索引
		Integer pageStart = (page - 1) * (limit);
//            String sql="select * from "+tablename+" where id_ in (select tablename_id from lysjfb_detail_tablenameid  where 1=1 and pid='"+fbid+"'" +
//                    " and tablename='"+tablename+"' and lx='"+fbwd+"'  order by dh desc offset "+pageStart+" rows fetch next "+limit+" rows only)";
		String sql="select * from "+tablename+" t where 1=1 "+query+"   order by t.dh desc offset "+pageStart+" rows fetch next "+limit+" rows only ";
		List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
		String sql2="select id_ from "+tablename+" t  where 1=1 "+query+"  order by t.dh desc ";
		List<Map<String,Object>> listAll = jdbcTemplate.queryForList(sql2);
		request.setAttribute("_total", listAll.size());
		//request.setAttribute("query",query);
		//条目数据
		String outString = AOSJson.toGridJson(listDto, listAll.size());
		return outString;
    }
	public String getTablename(Dto inDto,HttpServletRequest request) {
		//标题列表
		String query="";
		String tabledesc=inDto.getString("tabledesc");
		String tablename="";
		String sql45="select * from archive_tablename where tabledesc='"+tabledesc+"'";
		List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql45);
		if(maps!=null&&maps.size()>0){
			tablename=(String)maps.get(0).get("tablename");
		}
		return tablename;
	}
	public Object getData(Dto qDto) {
		String tablename = qDto.getString("tablename");
		String id = qDto.getString("id_");
		String sql = "SELECT * FROM " + tablename + " WHERE id_='" + id + "'";
		List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
				.queryForList(sql));
		return listDto.get(0);
	}
}
