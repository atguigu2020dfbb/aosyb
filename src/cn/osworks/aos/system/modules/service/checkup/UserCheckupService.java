package cn.osworks.aos.system.modules.service.checkup;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.asset.DicCons;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.Aos_sys_modulePO;
import cn.osworks.aos.system.dao.po.Aos_sys_module_userPO;
import cn.osworks.aos.system.dao.po.Archive_checkupPO;
import cn.osworks.aos.system.dao.po.Archive_taskPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.SystemService;
import cn.osworks.aos.system.modules.service.auth.PostService;
import cn.osworks.aos.system.modules.service.resource.ModuleService;
import cn.osworks.aos.web.tag.core.model.TreeBuilder;
import cn.osworks.aos.web.tag.core.model.TreeNode;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserCheckupService {
    @Autowired
    private SqlDao sysDao;
    @Autowired
    private Aos_sys_orgMapper aos_sys_orgMapper;
    @Autowired
    private Aos_sys_userMapper aos_sys_userMapper;
    @Autowired
    private Aos_sys_user_cfgMapper aos_sys_user_cfgMapper;
    @Autowired
    private Aos_sys_module_userMapper aos_sys_module_userMapper;
    @Autowired
    private Aos_sys_moduleMapper aos_sys_moduleMapper;
    @Autowired
    private Aos_sys_user_postMapper aos_sys_user_postMapper;
    @Autowired
    private Aos_sys_user_roleMapper aos_sys_user_roleMapper;
    @Autowired
    private SystemService systemService;
    @Autowired
    private PostService postService;
    @Autowired
    private Aos_sys_user_extMapper aos_sys_user_extMapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private Aos_sys_pageMapper aos_sys_pageMapper;
    @Autowired
    private Archive_checkupMapper archive_checkupMapper;
    /**
     * 获取菜单树(一次加载全部节点) 查看已授权
     *
     * @param inDto
     * @return
     */
    public Dto getModuleTree4Selected(Dto inDto) {
        Dto outDto = Dtos.newDto();
        inDto.put("status_", DicCons.ENABLED_YES);
        List<Aos_sys_modulePO> aos_sys_modulePOs = sysDao.list("Auth.listUserModuleSelected", inDto);
        List<TreeNode> treeNodes = moduleService.toTreeModal(aos_sys_modulePOs);
        String jsonString = TreeBuilder.build(treeNodes);
        outDto.setStringA(jsonString);
        return outDto;
    }
    /**
     * 获取菜单树(一次加载全部节点) 授权选择 (一次性渲染全部节点出来才能完美复选)
     *
     * @param inDto
     * @return
     */
    public String getModuleTree4Select(Dto inDto) {
        UserInfoVO userInfoVO = inDto.getUserInfo();
        List<Aos_sys_modulePO> aos_sys_modulePOs = Lists.newArrayList();
        // 超级用户获取所有菜单
        if (DicCons.USER_TYPE_SUPER.equals(userInfoVO.getType_())) {
            inDto.setOrder("sort_no_ ASC");
            inDto.put("status_", DicCons.ENABLED_YES);
            aos_sys_modulePOs = aos_sys_moduleMapper.list(inDto);
        } else {
            // 权限用户 根据管理权限获取可再授权菜单
            Dto grantDto = Dtos.newDto();
            grantDto.putAll(inDto);
            grantDto.put("user_id_", userInfoVO.getId_()); // 当前登录用户
            aos_sys_modulePOs = systemService.getAdminModulesOfUser(grantDto);
        }
        return toTreeModal(aos_sys_modulePOs, inDto);
    }
    /**
     * 转换为树模型
     *
     * @param inDto
     * @return
     */
    private String toTreeModal(List<Aos_sys_modulePO> aos_sys_modulePOs, Dto inDto) {
        List<TreeNode> treeNodes = Lists.newArrayList();
        Dto aos_sys_module_userDto = Dtos.newDto();
        aos_sys_module_userDto.put("user_id_", inDto.getString("user_id_")); // 界面选中的用户
        aos_sys_module_userDto.put("grant_type_", inDto.getString("grant_type_"));
        List<Aos_sys_module_userPO> aos_sys_module_userPOs = aos_sys_module_userMapper.list(aos_sys_module_userDto);
        for (Aos_sys_modulePO aos_sys_modulePO : aos_sys_modulePOs) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(aos_sys_modulePO.getId_());
            treeNode.setText(aos_sys_modulePO.getName_());
            treeNode.setParentId(aos_sys_modulePO.getParent_id_());
            treeNode.setIcon(aos_sys_modulePO.getIcon_name_());
            //此时为了保留之前有的选择选项，进行数据库查询之前是否选中了，为其赋予历史标记
             String cascade_id_=aos_sys_modulePO.getCascade_id_();
             if(!cascade_id_.startsWith("0.024")){
                 //此时添加隐藏属性
                 treeNode.setHide(false);
             }else{
                 treeNode.setHide(true);
             }
            boolean checked = false;
            // 可以改成AOSList的写法
            for (Aos_sys_module_userPO aos_sys_module_userPO : aos_sys_module_userPOs) {
                if (aos_sys_module_userPO.getModule_id_().equals(aos_sys_modulePO.getId_())) {
                    checked = true;
                    break;
                }
            }
            treeNode.setChecked(checked);
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
        return TreeBuilder.build(treeNodes);
    }
    /**
     * 保存用户-功能模块授权信息
     *
     * @param pDto
     */
    @Transactional
    public void saveUserModuleGrantInfo(Dto pDto) {
        UserInfoVO userInfoVO = pDto.getUserInfo();
        Dto delDto = Dtos.newDto();
        delDto.put("user_id_", pDto.getString("user_id_"));
        delDto.put("grant_type_", pDto.getString("grant_type_"));
        // 每次授权都将历史数据清零
        sysDao.delete("Auth.deleteAos_sys_module_userByDto", delDto);
        String[] selections = pDto.getRows();
        Aos_sys_module_userPO aos_sys_module_userPO = new Aos_sys_module_userPO();
        for (String module_id_ : selections) {
            aos_sys_module_userPO.setId_(AOSId.uuid());
            aos_sys_module_userPO.setModule_id_(module_id_);
            aos_sys_module_userPO.setUser_id_(pDto.getString("user_id_"));
            aos_sys_module_userPO.setGrant_type_(pDto.getString("grant_type_"));
            aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
            aos_sys_module_userPO.setOperator_id_(userInfoVO.getId_());
            aos_sys_module_userMapper.insert(aos_sys_module_userPO);
        }
        //此时将方案添加用户名称
       /* Archive_checkupPO archive_checkupPO=new Archive_checkupPO();
        archive_checkupPO.setId_(pDto.getString("task_id_"));
        archive_checkupPO.setFirst_compilationperson(pDto.getString("user"));
        archive_checkupPO.setFirst_compilationperson_cn(pDto.getString("username"));
        archive_checkupMapper.updateByKey(archive_checkupPO);*/
    }
}
