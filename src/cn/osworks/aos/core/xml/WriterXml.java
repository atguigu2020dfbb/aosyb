package cn.osworks.aos.core.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class WriterXml {

	public  void writerxml(Integer index,Map<String, Object> dhMessage, Map<String, Object> electronicMessage, Map<String, Object> electronicMessage2, Map<String, Object> message, Map<String, Object> basemessage, String user,String tablename, String xmlPath){
		try {
			// 1、创建document对象
			Document document = DocumentHelper.createDocument();
			// 2、创建根节点rss
			Element rss = document.addElement("电子文件封装包");
			// 3、向rss节点添加version属性
			rss.addAttribute("xmlns", "http://www.saac.gov.cn/standards/ERM/encapsulation");
			rss.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			rss.addAttribute("xsi:schemaLocation", "http://www.saac.gov.cn/standards/ERM/encapsulation eep.xsd");
			// 4、生成子节点及子节点内容
			Element channel = rss.addElement("封装包格式描述");
			channel.setText("本EEP根据中华人民共和国档案行业标准DA/T 48-2009《基于XML的电子文件封装规范》生成");
			Element channel2 = rss.addElement("版本");
			channel2.setText("2009");
			Element channel3 = rss.addElement("被签名对象");
			channel3.addAttribute("eep版本", "2009");
			Element channel41 = channel3.addElement("封装包类型");
			channel41.setText("原始型");
			Element channel42 = channel3.addElement("封装包类型描述");
			channel42.setText("本封装包包含电子文件数据及其元数据，原始封装，未经修改");
			Element channel43 = channel3.addElement("封装包创建时间");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = sdf.format(new Date());
			channel43.setText(createTime);
			Element channel44 = channel3.addElement("封装包创建单位");
			channel44.setText("上饶市档案局");
			Element channel45 = channel44.addElement("封装内容");
			Element channel46= channel45.addElement("文件实体块");
			Element channel47= channel46.addElement("文件实体");
			Element channel48= channel47.addElement("聚合层次");
			channel48.setText("文件");
			Element channel49= channel47.addElement("来源");
			Element channel50= channel49.addElement("立档单位名称");
			channel50.setText("上饶市档案局");
			Element channel51= channel47.addElement("电子文件号");
			channel50.setText(index+"");
			Element channel52= channel47.addElement("档号");
			if(dhMessage.size()>0&&dhMessage!=null){
				Iterator<Map.Entry<String,Object>> it=dhMessage.entrySet().iterator();
				int i=0;
				while(it.hasNext()){
					Map.Entry<String,Object> entry=it.next();
					String dhkey=entry.getKey();
					String dhvalue=(String)entry.getValue();
					Element tt= channel52.addElement(dhkey);
					tt.setText(dhvalue);
					i++;
				}
			}
			Element channel58= channel47.addElement("内容描述");
			//内容描述
			if(message.size()>0&&message!=null){
				Iterator<Map.Entry<String,Object>> it=message.entrySet().iterator();
				int i=0;
				while(it.hasNext()){
					Map.Entry<String,Object> entry=it.next();
					String messagekey=entry.getKey()+"";
					String messagevalue=(String)(entry.getValue()+"");
					Element tt= channel58.addElement(messagekey);
					tt.setText(messagevalue+"");
					i++;
				}
			}
			Element channel65= channel47.addElement("形式特征");
			//形式特征
			if(electronicMessage.size()>0&&electronicMessage!=null){
				Iterator<Map.Entry<String,Object>> it=electronicMessage.entrySet().iterator();
				int i=0;
				while(it.hasNext()){
					Map.Entry<String,Object> entry=it.next();
					String electronicMessagekey=entry.getKey()+"";
					String electronicMessagevalue=(String)(entry.getValue()+"");
					Element tt= channel65.addElement(electronicMessagekey);
					tt.setText(electronicMessagevalue);
					i++;
				}
			}
			Element channel68= channel47.addElement("存储位置");
			Element channel69= channel68.addElement("脱机载体编号");
			channel69.setText("0");
			Element channel70= channel47.addElement("权限管理");
			Element channel71= channel70.addElement("控制标识");
			channel71.setText("政务内网开放");
			Element channel72= channel47.addElement("信息系统描述");
			Element channel73= channel47.addElement("附注");
			Element channel74= channel47.addElement("文件数据");
			Element channel75= channel74.addElement("文档");
			Element channel76= channel75.addElement("文档标识符");
			channel76.setText("修改0-文档1");
			Element channel77= channel75.addElement("文档数据");
			channel77.addAttribute("文档数据ID", "修改0-文档1-文档数据1");
			Element channel78= channel77.addElement("编码");
			channel77.addAttribute("编码ID", "修改0-文档1-文档数据1-编码1");
			//电子属性
			Element channel79= channel78.addElement("电子属性");
			if(electronicMessage2.size()>0&&electronicMessage2!=null){
				Iterator<Map.Entry<String,Object>> it=electronicMessage2.entrySet().iterator();
				int i=0;
				while(it.hasNext()){
					Map.Entry<String,Object> entry=it.next();
					String electronicMessage2key=entry.getKey()+"";
					String electronicMessage2value=(String)(entry.getValue()+"");
					Element tt= channel65.addElement(electronicMessage2key);
					tt.setText(electronicMessage2value);
					i++;
				}
			}
			Element channel83= channel78.addElement("数字化属性");
			Element channel84= channel83.addElement("扫描分辨率");
			channel84.setText("0");
			Element channel85= channel83.addElement("扫描色彩模式");
			channel85.setText("黑白二值");
			Element channel86= channel78.addElement("编码描述");
			channel86.setText("编码描述");
			Element channel87= channel78.addElement("反编码关键字");
			channel87.setText("反编码关键字");
			
			if(basemessage.size()>0&&basemessage!=null){
				Element channel88= channel78.addElement("编码数据");
				channel88.attributeValue("编码数据ID","修改0-文档1-文档数据1-编码1编码数据");
				Iterator<Map.Entry<String,Object>> it=basemessage.entrySet().iterator();
				int i=0;
				while(it.hasNext()){
					Map.Entry<String,Object> entry=it.next();
					String basemessagekey=entry.getKey()+"";
					String basemessagevalue=(String)(entry.getValue()+"");
					Element tt= channel88.addElement(basemessagekey);
					tt.setText(basemessagevalue);
					i++;
				}
			}
			Element channel89= channel45.addElement("业务实体块");
			Element channel90= channel89.addElement("业务实体");
			Element channel91= channel90.addElement("业务标识符");
			channel91.setText("85");
			Element channel92= channel90.addElement("文件标识符");
			channel92.setText("204");
			Element channel93= channel90.addElement("业务状态");
			channel93.setText("历史行为");
			Element channel94= channel45.addElement("机构人员实体块");
			Element channel95= channel94.addElement("机构人员实体");
			Element channel96= channel95.addElement("机构人员标识符");
			channel96.setText("602");
			Element channel97= channel95.addElement("机构人员名称");
			channel96.setText("上饶市档案局");
			Element channel98= channel94.addElement("机构人员实体");
			Element channel99= channel94.addElement("机构人员实体关系");
			Element channel20= channel99.addElement("机构人员标识符");
			channel20.setText("602");
			Element channel21= channel99.addElement("被关联机构人员标识符");
			Element channel22= channel99.addElement("关系类型");
			Element channel23 = rss.addElement("电子签名块");
			Element channel24 = channel23.addElement("电子签名");
			Element channel25 = channel24.addElement("签名标识符");
			channel25.setText("1.2.840.113549.1.1.5");
			Element channel26 = channel24.addElement("签名规则");
			channel26.setText("test");
			Element channel27 = channel24.addElement("签名时间");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateNowStr = sdf2.format(new Date());
			channel27.setText(dateNowStr);
			Element channel28 = channel24.addElement("签名人");
			channel28.setText(user);
			Element channel29 = channel24.addElement("签名结果");
			channel29.setText("osrh5yujmMso+QyzcEka0K1P3dU=");
			Element channel30 = channel24.addElement("签名算法标识");
			// 5、设置生成xml的格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置编码格式
			format.setEncoding("UTF-8");
			// 6、生成xml文件（生成到指定的tablename中）	
			//创建路径
			File pathfile=new File(xmlPath+tablename);
			if(!pathfile.isDirectory()){
				pathfile.mkdirs();
			}
			File file = new File(xmlPath+tablename+"//rss"+index+".xml");
			XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
			// 设置是否转义，默认使用转义字符
			writer.setEscapeText(false);
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
