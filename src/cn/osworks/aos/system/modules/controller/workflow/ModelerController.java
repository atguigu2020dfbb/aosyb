package cn.osworks.aos.system.modules.controller.workflow;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.core.typewrap.impl.HashDto;
import cn.osworks.aos.system.dao.mapper.Act_re_modelMapper;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;
import cn.osworks.aos.system.dao.po.Act_re_modelPO;
import cn.osworks.aos.system.modules.service.workflow.ModelerService;


/**
 * 流程模型设计器控制器
 *
 * @author OSWorks-XC
 * @date 2014-10-16
 */
@Controller
@RequestMapping(value = "workflow/modeler")
public class ModelerController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ModelerService modelerService;
    
    @Autowired
	private Act_re_modelMapper act_re_modelMapper;

    /**
     * 新建模型页面初始化
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "init")
    public String init(HttpServletRequest request) {
        // ID参数将会被modeler内部的Ajax再次携带使用
        return "aos/workflow/modeler/modeler.jsp";
    }


    /**
     * 弹出模型保存窗口之前请求的方法
     * <p></p>
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "retrieveBeforeSave")
    public void retrieveBeforeSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Dto inDto = Dtos.newDto(request);
        Dto modelDto = Dtos.newDto();
        String name = "", description = ""; //保存模型窗口的表单属性
        String modelId = inDto.getString("id");
        modelDto.put("name", name);
        modelDto.put("description", description);
        modelDto.put("revision", 1);
        modelDto.put("modelId", modelId);
        modelDto.put("model", getEmptyModelNode());
        WebCxt.writeRaw(response, AOSJson.toJson(modelDto));
    }

    /**
     * Modeler模型数据初始化方法
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "initModel")
    public void initModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Dto inDto = Dtos.newDto(request);
        String id = inDto.getString("id");
        Dto modelDto = Dtos.newDto();
        modelDto.put("modelId", id);
        modelDto.put("revision", 1);
        modelDto.put("name", "name");
        modelDto.put("description", "description");
        if (StringUtils.equals(id, "-1")) {
            //新建模型
            modelDto.put("model", getEmptyModelNode());
        } else {
            //修改模型
            byte[] sourceByte = repositoryService.getModelEditorSource(id);
            if (sourceByte == null || sourceByte.length == 0) {
                modelDto.put("model", getEmptyModelNode());
            } else {
                String modelEditorSource = new String(sourceByte, "utf-8");
                Dto modelNode = AOSJson.fromJson(modelEditorSource, HashDto.class);
                modelDto.put("model", modelNode);
            }

        }
        WebCxt.writeRaw(response, AOSJson.toJson(modelDto));
    }
    /**
     * 生成一个空的模型节点
     *
     * @return
     */
    private Dto getEmptyModelNode() {
        Dto modelNode = Dtos.newDto();
        String uuid = AOSId.uuid(4); //这个值还没发现有啥用处
        modelNode.put("id", uuid);
        modelNode.put("resourceId", uuid);
        Dto stencilSetNode = Dtos.newDto();
        stencilSetNode.put("namespace",
                "http://b3mn.org/stencilset/bpmn2.0#");
        modelNode.put("stencilset", stencilSetNode);
        return modelNode;
    }

    /**
     * 保存Modeler模型数据
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "saveModel")
    public void saveModel(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        Dto inDto = Dtos.newDto(request);
        Dto outDto = modelerService.saveModel(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    
    /**
     * 
     * 查询模型列表
     * 
     * @author Sun
     * @param request
     * @param response
     *
     * 2018-11-30
     */
    @RequestMapping(value = "listModelers")
    public void listModelers(HttpServletRequest request,HttpServletResponse response){
    	Dto inDto = Dtos.newDto(request);
    	List<Act_re_modelPO> list = act_re_modelMapper.listPage(inDto);
    	
    	String outString =AOSJson.toGridJson(list,inDto.getPageTotal());
		WebCxt.write(response, outString);
    	
    }
    /**
     * 保存Modeler模型数据
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "crateModeler")
    public void crateModeler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Dto inDto = Dtos.newDto(request);
        Dto outDto = modelerService.createModel(inDto);
        
      //  Dto outDto = modelerService.saveModel(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    
    /**
     * 删除Modeler模型数据
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "delModeler")
    public void delModeler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Dto inDto = Dtos.newDto(request);
        Dto outDto = modelerService.delModel(inDto);
        WebCxt.write(response, AOSJson.toJson(outDto));
    }
    
    /**
     * 创建模型
     */
    @RequestMapping("deployModeler")
    public void deployModeler(HttpServletRequest request, HttpServletResponse response) {
    	Dto inDto = Dtos.newDto(request);
    	Dto outDto = modelerService.deploy(inDto);
    	WebCxt.write(response, AOSJson.toJson(outDto));
    }
}
