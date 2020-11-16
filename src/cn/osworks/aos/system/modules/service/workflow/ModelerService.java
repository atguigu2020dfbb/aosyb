package cn.osworks.aos.system.modules.service.workflow;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.asset.DicCons;


/**
 * 流程模型服务
 *
 * @author OSWorks-XC
 * @date 2014-12-16
 */
@Service
public class ModelerService {
	
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private WorkflowService workflowService;

    /**
     * 修改模型
     *
     * @throws UnsupportedEncodingException
     */
    @Transactional
    public void updateModel(Dto inDto) throws UnsupportedEncodingException {
        String modelId = inDto.getString("id");
        //更新大对象
        repositoryService.addModelEditorSource(modelId, inDto.getString("json_xml").getBytes("utf-8"));
        //更新re_model表
        Model model = repositoryService.getModel(modelId);
        model.setName(inDto.getString("name"));
        repositoryService.saveModel(model);
    }
    
    /**
     * 保存Modeler模型数据
     * @throws UnsupportedEncodingException 
     *
     */
    @Transactional
    public Dto saveModel(Dto inDto) throws UnsupportedEncodingException{
        String id = inDto.getString("id");
        String modelId = "";
        if (StringUtils.equals(id, "-1")) {
            //接收新增模型设计的请求
            String _aos_modelID = inDto.getString("_aos_modelID");
            if (AOSUtils.isEmpty(_aos_modelID)) {
                // 模型新增
                inDto.put("create_type_", DicCons.CREATE_TYPE_DESIGN);
                // 模型新增
                Model model = repositoryService.newModel();
                model.setName(inDto.getString("name"));
                repositoryService.saveModel(model);
                //插入大对象
                repositoryService.addModelEditorSource(model.getId(), inDto.getString("json_xml").getBytes("utf-8"));
                modelId = model.getId();
            } else {
                //防止重复新增->转为修改
                inDto.put("id", _aos_modelID);
                updateModel(inDto);
                modelId = _aos_modelID;
            }
        } else {
            //接收修改模型设计的请求
            updateModel(inDto);
        }
        Dto outDto = Dtos.newDto();
        outDto.put("_aos_modelID", modelId);
        outDto.setAppMsg("模型数据保存成功。");
        outDto.put(AOSCons.REQUEST_SUCCESS, true);
        return outDto;
    }
    
    /**
	 * 创建新模型
	 * @throws UnsupportedEncodingException 
	 * */
    @Transactional
	public Dto createModel(Dto inDto) throws UnsupportedEncodingException{
    	String name=inDto.getString("name_");
    	String key = inDto.getString("key_");
    	//String version = inDto.getString("version_");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        Model modelData = repositoryService.newModel();

        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        String description = StringUtils.defaultString("模型描述信息");
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelData.setMetaInfo(modelObjectNode.toString());
        modelData.setName(name);
        modelData.setKey(key);
        modelData.setVersion(1);

        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        Dto outDto = Dtos.newDto();
        //outDto.put("_aos_modelID", modelId);
        outDto.setAppMsg("模型数据保存成功。");
        outDto.put(AOSCons.REQUEST_SUCCESS, true);
        return outDto;
	}
    
    /**
     * 
     * 删除模型
     * 
     * @author Sun
     * @param inDto
     *
     * 2018-12-4
     */
    @Transactional
    public Dto delModel(Dto inDto){
    	repositoryService.deleteModel(inDto.getString("id"));
    	Dto outDto = Dtos.newDto();
    	outDto.setAppMsg("删除成功!");
    	outDto.put(AOSCons.REQUEST_SUCCESS, true);
    	return outDto;
    }
    
    /**
     * 
     * 部署模型
     * 
     * @author Sun
     * @param inDto
     * @return
     *
     * 2018-12-4
     */
    @Transactional
	public Dto deploy(Dto inDto) {
		String message = "";
		Dto outDto = Dtos.newDto();
		try {
			//Model modelData = repositoryService.newModel();
			Model modelData = repositoryService.getModel(inDto.getString("id"));
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel,"ISO-8859-1");
			
			String processName = modelData.getName();
			if (!StringUtils.endsWith(processName, ".bpmn20.xml")){
				processName += ".bpmn20.xml";
			}
//			System.out.println("========="+processName+"============"+modelData.getName());
			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.addInputStream(processName, in).deploy();
//					.addString(processName, new String(bpmnBytes)).deploy();
			
			// 设置流程分类
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			for (ProcessDefinition processDefinition : list) {
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), modelData.getCategory());
				message = "部署成功";
			}
			if (list.size() == 0){
				message = "部署失败，没有流程。";
			}
		} catch (Exception e) {
			throw new ActivitiException("设计模型图不正确，检查模型正确性", e);
		}
		outDto.setAppMsg(message);
		//outDto.put(AOSCons.REQUEST_SUCCESS, true);
		return outDto;
	}
}
