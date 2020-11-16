package cn.osworks.aos.system.modules.controller.chulinprint;
import java.io.*;
import org.json.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;;

/**
 * 公司版特有功能：
 * 1、不需要预览，直接导出报表内容至PDF文件
 * 2、在线编辑报表，可以直接保存至服务器
 * 3、在线编辑报表，可以自定义数据集的名称
 * 4、报表预览窗口，可以定制哪些按钮不显示出来
 * 5、能通过程序设置报表的页边距，主要用于针式打印机的页边距调整
 */



/**
 * 产生打印所需要的JSON文件
 * 实际使用时，此类需要移植至应用项目中去
 *
 */
public class PrintJson {
	/**
	 *  打印的JSON数据
	 */
	private JSONObject jPrintValue;
	/**
	 *  打印的参数，每个数组包括两个值：Key, Value
	 */
	private JSONArray jaryPrintParam;
	/**
	 * 打印预览时的按钮是否显示
	 */
	private JSONObject jButtonHide;
	/**
	 * 设置页边距
	 */
	private JSONObject jMargin;
	/**
	 * 设置数据集的名称
	 */
	private JSONObject jDataSetName;
	/**
	 *  打印的单个图片的数组，包含3个元素：报表图片对象名、图片文件名、 图片文件内容
	 */
	private JSONArray jaryPictureSingle;
	/**
	 *  打印的数据集中的图片的内容的数组，包含2个元素：图片文件名称、图片文件内容
	 */
	private JSONArray jaryPictureTableValue;
	/**
	 *  打印的数据集中的图片的设置的数组，包含3个元素：报表图片对象名、数据集序号、字段名称
	 */
	private JSONArray jaryPictureTableSet;
	/**
	 *  打印的数据内容的数组
	 */
	private JSONArray jaryPrintData;
	/**
	 *  控件注册的姓名或公司名称
	 */
	private String strRegName;
	/**
	 *  控件的注册码
	 */
	private String strRegValue;
	/**
	 *  报表文件名称，包括绝对路径
	 */
	private String strReportFileName;
	/**
	 *  报表临时文件所在的文件夹，服务器的报表JSON数据将临时存储在此文件夹，此文件夹要求能通过URL访问，控件通过文件名下载报表打印所需要的数据
	 */
	private String strReportTempDirectory;
	/**
	 *  存储打印控件时初始化时，所传入的Cookie值、回传服务端的URL
	 */
	private JSONObject jCookieUrl;


	/**
	 *  打印初始化类
	 * @param _strReportTempDirectory 报表临时文件所在的文件夹，服务器的报表JSON数据将临时存储在此文件夹，此文件夹要求能通过URL访问，控件通过文件名下载报表打印所需要的数据
	 * @param _strReportFileName 报表文件名称，包括绝对路径
	 */
	public PrintJson(String _strReportTempDirectory, String _strReportFileName) {
		jPrintValue = new JSONObject();
		jaryPrintParam = new JSONArray();
		jButtonHide = new JSONObject();
		jMargin = new JSONObject();
		jDataSetName = new JSONObject();
		jaryPictureSingle = new JSONArray();
		jaryPictureTableValue = new JSONArray();
		jaryPictureTableSet = new JSONArray();
		jaryPrintData = new JSONArray();
		jCookieUrl = new JSONObject();

		this.strReportTempDirectory = _strReportTempDirectory;
		File pDir = new File(strReportTempDirectory);
		if (!pDir.exists()) {
			pDir.mkdir();
		}
		pDir = null;
		this.strReportFileName = _strReportFileName;
	}

	/**
	 *  输入控件注册的信息，检查注册
	 * @param _strRegName 控件注册的姓名或公司名称
	 * @param _strRegValue 控件的注册码
	 */
	public void CheckRegister(String _strRegName, String _strRegValue) {
		this.strRegName = _strRegName;
		this.strRegValue = _strRegValue;
	}

	/**
	 *  设置控件初始化后，需要上传的Cookie，以及回传的服务器URL
	 *  服务端调用控件之前，判断是否有 Cookie（此Cookie为GuId值，保证唯一性，此Cookie的保存时间为10年以上），若有Cookie则在服务端的数据表中查找，判断是否有记录，若有记录，则表示此电脑已经安装过。 若没有记录，或者没有Cookie，则表示未安装过。若没有Cookie则建立一个Cookie
	 *  调用控件时，把此Cookie，还有Cookie回传的服务端的URL传给控件。控件在运行时，把此cookie值回传服务器，服务器把此Cookie写入数据表中，并且更新最后一次回调的时间
	 *  浏览器在调用控件之后，可以再启动一个延时（比如延时3至15秒）函数，以Ajax去访问服务端（参数：Cookie值），服务端去读取数据表的此Cookie的记录，若不存在记录，或最后访问的时间超过了规定时间，则判断为客户机没有安装，提示下载安装。
	 * @param strCookie 此Cookie值由服务器生成，最好是GUID，保证唯一性
	 * @param strServerGetUrl 回传的服务器URL，是Get方式
	 */
	public void SetCookieAndURL(String strCookie, String strServerGetUrl) {
		if (strCookie != "" && strServerGetUrl.toLowerCase().contains("http")) {
			try {
				jCookieUrl.put("Cookie", strCookie);
				jCookieUrl.put("ServerUrl", strServerGetUrl);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *  增加打印的参数
	 * @param strKey  参数名
	 * @param strValue  参数值
	 */
	public void AddPrintParam(String strKey, String strValue) {
		JSONObject jParam = new JSONObject();
		try {
			jParam.put("Key", strKey);
			jParam.put("Value", strValue);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jaryPrintParam.put(jParam);
	}

	/**
	 *  在线编辑报表时，设置报表的文件名
	 * @param strReportFileName 报表的文件名
	 */
	public void SetReportFileName(String strReportFileName) {
		try {
			jPrintValue.put("ReportFileName", strReportFileName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  在线编辑报表时，设置Web服务器接收保存报表数据的URL
	 * @param strPostUrl Web服务器接收保存报表数据的URL，为完整的URL
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetPostUrl(String strPostUrl) {
		try {
			jPrintValue.put("PostUrl", strPostUrl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  在报表预览时，点击页面设置按钮，所设置的页面设置信息可通过此URL上传至Web服务器保存
	 * @param strPageSetUrl
	 */
	public void SetPageSetUrl(String strPageSetUrl) {
		try {
			jPrintValue.put("PageSetUrl", strPageSetUrl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  在报表打印时，在弹出的选择打印机的窗口后点击“确定”按钮时，通过Http的Post方式直接提交到所设置的URL页面，用户可以在URL处理用户已打印的份数
	 * @param strPrintUrl
	 */
	public void SetPrintUrl(String strPrintUrl) {
		try {
			jPrintValue.put("PrintUrl", strPrintUrl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定打印时的打印机名称
	 * @param strPrinter
	 */
	public void SetPrinter(String strPrinter) {
		try {
			jPrintValue.put("Printer", strPrinter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：打印
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonPrint() {
		try {
			jButtonHide.put("pbPrint", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 报表打印预览窗口隐藏按钮：打开文件
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButt() {
		try {
			jButtonHide.put("pbLoad", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：保存
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonSave() {
		try {
			jButtonHide.put("pbSave", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：导出
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonExport() {
		try {
			jButtonHide.put("pbExport", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：放大缩小
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonZoom() {
		try {
			jButtonHide.put("pbZoom", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：查找
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonFind() {
		try {
			jButtonHide.put("pbFind", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：大纲显示
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonOutline() {
		try {
			jButtonHide.put("pbOutline", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：页面设置
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonPageSetup() {
		try {
			jButtonHide.put("pbPageSetup", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：页面导航
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonNavigator() {
		try {
			jButtonHide.put("pbNavigator", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  报表打印预览窗口隐藏按钮：导出
	 *  公司版本才有此功能，个人版本无此功能
	 */
	public void IsHideButtonExportQuick() {
		try {
			jButtonHide.put("pbExportQuick", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置页面设置的左边距，单位为毫米
	 * @param dbLeftMargin
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetLeftMargin(double dbLeftMargin) {
		try {
			jMargin.put("LeftMargin", dbLeftMargin);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置页面设置的右边距，单位为毫米
	 * @param dbRightMargin
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetRightMargin(int dbRightMargin) {
		try {
			jMargin.put("RightMargin", dbRightMargin);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置页面设置的上边距，单位为毫米
	 * @param dbTopMargin
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetTopMargin(int dbTopMargin) {
		try {
			jMargin.put("TopMargin", dbTopMargin);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置页面设置的下边距，单位为毫米
	 * @param dbBottomMargin
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetBottomMargin(int dbBottomMargin) {
		try {
			jMargin.put("BottomMargin", dbBottomMargin);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置数据集1的名称为指定名称（可以是中文）
	 * @param strDataSetName
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetDataSetName1(String strDataSetName) {
		try {
			jDataSetName.put("DataSetName1", strDataSetName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置数据集2的名称为指定名称（可以是中文）
	 * @param strDataSetName
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetDataSetName2(String strDataSetName) {
		try {
			jDataSetName.put("DataSetName2", strDataSetName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置数据集3的名称为指定名称（可以是中文）
	 * @param strDataSetName
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetDataSetName3(String strDataSetName) {
		try {
			jDataSetName.put("DataSetName3", strDataSetName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置数据集4的名称为指定名称（可以是中文）
	 * @param strDataSetName
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetDataSetName4(String strDataSetName) {
		try {
			jDataSetName.put("DataSetName4", strDataSetName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置数据集5的名称为指定名称（可以是中文）
	 * @param strDataSetName
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetDataSetName5(String strDataSetName) {
		try {
			jDataSetName.put("DataSetName5", strDataSetName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置数据集6的名称为指定名称（可以是中文）
	 * @param strDataSetName
	 * 公司版本才有此功能，个人版本无此功能
	 */
	public void SetDataSetName6(String strDataSetName) {
		try {
			jDataSetName.put("DataSetName6", strDataSetName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  建立数据集的主从关系
	 * @param iMasterDataSeq  主数据集序号，通常为1
	 * @param strFieldName    关联字段名为两个数据集共同的字段名
	 * @param bIsNumber       关联的字段是否为数值型
	 */
	public void MasterOptions(int iMasterDataSeq, String strFieldName, Boolean bIsNumber) {
		JSONObject jMasterOptions = new JSONObject();
		try {
			jMasterOptions.put("MasterDataSeq", iMasterDataSeq);
			jMasterOptions.put("FieldName", strFieldName);
			jMasterOptions.put("IsNumber", bIsNumber);
			jPrintValue.put("MasterOptions", jMasterOptions);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  增加单张图片打印
	 * @param strReportObjectName  在报表机模板中的图片对象名称
	 * @param strPictureFileName   图片文件的绝对路径，包括最后的斜线或反斜线
	 * @param strPictureFilePath   图片文件名称
	 */
	public void AddPictureSinglePrint(String strReportObjectName, String strPictureFileName,
									  String strPictureFilePath) {
		try {
			String strFileValue =cn.osworks.aos.system.modules.controller.chulinprint.PrintFunction.FileToStr(strPictureFilePath + strPictureFileName);
			if (strFileValue != "") {
				JSONObject jPicture = new JSONObject();
				jPicture.put("ReportObjectName", strReportObjectName);
				jPicture.put("PictureFileName", strPictureFileName);
				jPicture.put("PictureFileValue", strFileValue);
				jaryPictureSingle.put(jPicture);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加DataTable中图片字段所对应的图片打印
	 * @param strReportObjectName 在报表模板文件中的图片对象名称
	 * @param dtTable             数据内容
	 * @param iDataSetIndex       该table中所有数据集的序号，从1开始
	 * @param strFieldName        图片所在的字段名
	 * @param strFilePath         图片在服务器上的绝对路径，包括最后的斜线和反斜线
	 */
	public void AddPictureTableToPrint(String strReportObjectName, ResultSet dtTable, int iDataSetIndex,
									   String strFieldName, String strFilePath) {
		try {
			// 增加图片打印的设置信息
			JSONObject jPictureSet = new JSONObject();
			jPictureSet.put("ReportObjectName", strReportObjectName);
			jPictureSet.put("DataSetIndex", iDataSetIndex);
			jPictureSet.put("FieldName", strFieldName);
			jaryPictureTableSet.put(jPictureSet);

			while (dtTable.next()) {
				String strPictureFileName = dtTable.getString(strFieldName);
				if( strPictureFileName == null ) {
					continue;
				}
				if ( strPictureFileName.trim() == "" ) {
					continue;
				}

				String strPictureValue = cn.osworks.aos.system.modules.controller.chulinprint.PrintFunction.FileToStr(strFilePath + strPictureFileName);
				if (strPictureValue != "") {
					JSONObject jPicture = new JSONObject();
					jPicture.put("PictureFileName", strPictureFileName);
					jPicture.put("PictureFileValue", strPictureValue);
					jaryPictureTableValue.put(jPicture);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  增加打印的数据集，数据类型：Table
	 * @param dtTable  数据内容
	 */
	public void AddPrintDataTable(ResultSet dtTable) {
		String strPrintValue = cn.osworks.aos.system.modules.controller.chulinprint.PrintFunction.TableToXml(dtTable);
		if (strPrintValue != "") {
			jaryPrintData.put(strPrintValue);
		}
	}

	/**
	 * 增加打印的数据集，数据类型：ArrayList
	 * @param listData
	 */
	public <T> void  AddPrintDataList( ArrayList<T> listData ) {
		String strPrintValue = cn.osworks.aos.system.modules.controller.chulinprint.PrintFunction.ListToXml(listData);
		if (strPrintValue != "") {
			jaryPrintData.put(strPrintValue);
		}
	}

	/**
	 *  预览报表，在打印之前先调用了 AddPrintDataIList 或 AddPrintDataTable 增加好了数据集
	 * @return 所产生的JSON文件名称
	 */
	public String ShowReport() {
		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  预览报表：1个List数据
	 * @param listData
	 * @return 所产生的JSON文件名称
	 */
	public <T> String ShowReport(ArrayList<T> listData) {
		AddPrintDataList(listData);

		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 * 预览报表：2个ListData数据
	 * @param listData1
	 * @param listData2
	 * @return
	 */
	public <T, S> String ShowReport(ArrayList<T> listData1, ArrayList<S> listData2) {
		AddPrintDataList(listData1);
		AddPrintDataList(listData2);

		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}


	/**
	 *  预览报表：1个Table数据
	 * @param dtTable1  数据内容
	 * @return 所产生的JSON文件名称
	 */
	public String ShowReport(ResultSet dtTable1) {
		AddPrintDataTable(dtTable1);

		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}


	/**
	 *  预览报表：2个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @return  所产生的JSON文件名称
	 */
	public String ShowReport(ResultSet dtTable1, ResultSet dtTable2) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);

		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  预览报表：3个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @return 所产生的JSON文件名称
	 */
	public String ShowReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);

		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  预览报表：4个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @return 所产生的JSON文件名称
	 */
	public String ShowReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);

		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  预览报表：5个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @param dtTable5
	 * @return 所产生的JSON文件名称
	 */
	public String ShowReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4,
							 ResultSet dtTable5) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);
		AddPrintDataTable(dtTable5);

		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  预览报表：6个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @param dtTable5
	 * @param dtTable6
	 * @return 所产生的JSON文件名称
	 */
	public String ShowReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4,
							 ResultSet dtTable5, ResultSet dtTable6) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);
		AddPrintDataTable(dtTable5);
		AddPrintDataTable(dtTable6);

		try {
			jPrintValue.put("Method", "ShowReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  直接打印报表，在打印之前先调用了 AddPrintDataIList 或 AddPrintDataTable 增加好了数据集
	 * @return 所产生的JSON文件名称
	 */
	public String PrintReport() {
		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  直接打印报表：1个List数据
	 * @param listData
	 * @return 所产生的JSON文件名称
	 */
	public <T> String PrintReport(ArrayList<T> listData) {
		AddPrintDataList(listData);

		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 * 直接打印报表：2个ListData数据
	 * @param listData1
	 * @param listData2
	 * @return
	 */
	public <T, S> String PrintReport(ArrayList<T> listData1, ArrayList<S> listData2) {
		AddPrintDataList(listData1);
		AddPrintDataList(listData2);

		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}


	/**
	 *  直接打印报表：1个Table数据
	 * @param dtTable1
	 * @return 所产生的JSON文件名称
	 */
	public String PrintReport(ResultSet dtTable1) {
		AddPrintDataTable(dtTable1);

		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return CallPrint();
	}


	/**
	 *  直接打印报表：2个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @return 所产生的JSON文件名称
	 */
	public String PrintReport(ResultSet dtTable1, ResultSet dtTable2) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);

		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  直接打印报表：3个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @return 所产生的JSON文件名称
	 */
	public String PrintReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);

		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  直接打印报表：4个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @return 所产生的JSON文件名称
	 */
	public String PrintReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);

		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  直接打印报表：5个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @param dtTable5
	 * @return 所产生的JSON文件名称
	 */
	public String PrintReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4,
							  ResultSet dtTable5) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);
		AddPrintDataTable(dtTable5);

		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  直接打印报表：6个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @param dtTable5
	 * @param dtTable6
	 * @return 所产生的JSON文件名称
	 */
	public String PrintReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4,
							  ResultSet dtTable5, ResultSet dtTable6) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);
		AddPrintDataTable(dtTable5);
		AddPrintDataTable(dtTable6);

		try {
			jPrintValue.put("Method", "PrintReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  导出报表至Pdf文件，在打印之前先调用了 AddPrintDataIList 或 AddPrintDataTable 增加好了数据集
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @return 所产生的JSON文件名称
	 */
	public String ExportReportPdf(String strExportPdfFileName)
	{
		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  导出报表至Pdf文件：1个List数据
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @param listData
	 * @return 所产生的JSON文件名称
	 */
	public <T> String ExportReportPdf(ArrayList<T> listData, String strExportPdfFileName) {
		AddPrintDataList(listData);

		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 * 导出报表至Pdf文件：2个ListData数据
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @param listData1
	 * @param listData2
	 * @return
	 */
	public <T, S> String ExportReportPdf(ArrayList<T> listData1, ArrayList<S> listData2, String strExportPdfFileName) {
		AddPrintDataList(listData1);
		AddPrintDataList(listData2);

		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}


	/**
	 *  导出报表至Pdf文件：1个Table数据
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @param dtTable1
	 * @return 所产生的JSON文件名称
	 */
	public String ExportReportPdf(ResultSet dtTable1, String strExportPdfFileName) {
		AddPrintDataTable(dtTable1);

		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  导出报表至Pdf文件：2个Table数据
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @param dtTable1
	 * @param dtTable2
	 * @return 所产生的JSON文件名称
	 */
	public String ExportReportPdf(ResultSet dtTable1, ResultSet dtTable2, String strExportPdfFileName) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);

		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  导出报表至Pdf文件：3个Table数据
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @return 所产生的JSON文件名称
	 */
	public String ExportReportPdf(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, String strExportPdfFileName) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);

		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  导出报表至Pdf文件：4个Table数据
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @return 所产生的JSON文件名称
	 */
	public String ExportReportPdf(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4, String strExportPdfFileName) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);

		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  导出报表至Pdf文件：5个Table数据
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @param dtTable5
	 * @return 所产生的JSON文件名称
	 */
	public String ExportReportPdf(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4,
								  ResultSet dtTable5, String strExportPdfFileName) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);
		AddPrintDataTable(dtTable5);

		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  导出报表至Pdf文件：6个Table数据
	 * @param strExportPdfFileName 所导出的Pdf文件的绝对路径及文件名；如果为空，则会弹出打开文件的对话框
	 * 公司版本才有此功能，个人版本无此功能
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @param dtTable5
	 * @param dtTable6
	 * @return
	 */
	public String ExportReportPdf(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4,
								  ResultSet dtTable5, ResultSet dtTable6, String strExportPdfFileName) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);
		AddPrintDataTable(dtTable5);
		AddPrintDataTable(dtTable6);

		try {
			jPrintValue.put("ExportPdfFileName", strExportPdfFileName);
			jPrintValue.put("Method", "ExportReportPdf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}



	/**
	 *  在线设计报表，在打印之前先调用了 AddPrintDataIList 或 AddPrintDataTable 增加好了数据集
	 * @return 所产生的JSON文件名称
	 */
	public String DesignReport() {
		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  在线设计报表：1个List数据
	 * @param listData
	 * @return 所产生的JSON文件名称
	 */
	public <T> String DesignReport(ArrayList<T> listData) {
		AddPrintDataList(listData);

		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 * 在线设计报表：2个ListData数据
	 * @param listData1
	 * @param listData2
	 * @return
	 */
	public <T, S> String DesignReport(ArrayList<T> listData1, ArrayList<S> listData2) {
		AddPrintDataList(listData1);
		AddPrintDataList(listData2);

		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}


	/**
	 *  在线设计报表：1个Table数据
	 * @param dtTable1
	 * @return 所产生的JSON文件名称
	 */
	public String DesignReport(ResultSet dtTable1) {
		AddPrintDataTable(dtTable1);

		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  在线设计报表：2个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @return 所产生的JSON文件名称
	 */
	public String DesignReport(ResultSet dtTable1, ResultSet dtTable2) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);

		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  在线设计报表：3个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @return 所产生的JSON文件名称
	 */
	public String DesignReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);

		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  在线设计报表：4个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @return 所产生的JSON文件名称
	 */
	public String DesignReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);

		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  在线设计报表：5个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @param dtTable5
	 * @return 所产生的JSON文件名称
	 */
	public String DesignReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4,
							   ResultSet dtTable5) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);
		AddPrintDataTable(dtTable5);

		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  在线设计报表：6个Table数据
	 * @param dtTable1
	 * @param dtTable2
	 * @param dtTable3
	 * @param dtTable4
	 * @param dtTable5
	 * @param dtTable6
	 * @return
	 */
	public String DesignReport(ResultSet dtTable1, ResultSet dtTable2, ResultSet dtTable3, ResultSet dtTable4,
							   ResultSet dtTable5, ResultSet dtTable6) {
		AddPrintDataTable(dtTable1);
		AddPrintDataTable(dtTable2);
		AddPrintDataTable(dtTable3);
		AddPrintDataTable(dtTable4);
		AddPrintDataTable(dtTable5);
		AddPrintDataTable(dtTable6);

		try {
			jPrintValue.put("Method", "DesignReport");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return CallPrint();
	}

	/**
	 *  组织好参数，调用控件进行操作
	 * @return 所产生的JSON文件名称
	 */
	private String CallPrint() throws NullPointerException {
		String strPrintFileName = "";
		if( jaryPrintData.length() == 0 ) { //没有定义数据集数据，抛出异常
			throw new NullPointerException();
		}

		try {

			// 取报表文件的内容
			String strReportFileValue = cn.osworks.aos.system.modules.controller.chulinprint.PrintFunction.FileToStr(this.strReportFileName);
			if (strReportFileValue == "") { // 报表文件内容为空则忽略
				return strPrintFileName;
			}
			jPrintValue.put("ReportFileValue", strReportFileValue);

			// 检查注册信息
			JSONObject jRegInfo = new JSONObject();
			jRegInfo.put("RegName", this.strRegName);
			jRegInfo.put("RegValue", this.strRegValue);
			jPrintValue.put("RegInfo", jRegInfo);

			// 报表打印的各参数
			jPrintValue.put("PrintParam", jaryPrintParam);
			jPrintValue.put("ButtonHide", jButtonHide);
			jPrintValue.put("Margin", jMargin);
			jPrintValue.put("DataSetName", jDataSetName);
			jPrintValue.put("PictureSingle", jaryPictureSingle);
			jPrintValue.put("PictureTableValue", jaryPictureTableValue);
			jPrintValue.put("PictureTableSet", jaryPictureTableSet);
			jPrintValue.put("PrintData", jaryPrintData);
			jPrintValue.put("CookieUrl", jCookieUrl);

			// 把JSON内容写入文件，返回文件名
			String strPrintValue = jPrintValue.toString();
			Random r = new Random();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

			strPrintFileName = "PrintTemp" + sdf.format(date) + r.nextInt(999999) + ".txt";
			String fileName = this.strReportTempDirectory + strPrintFileName;

			File fileDbf = new File(fileName);
			if (fileDbf.exists()) {
				fileDbf.delete();
			}

			try {
				fileDbf.createNewFile();
			} catch (IOException e) {
				fileDbf = null;
				return "";
			}

			RandomAccessFile struHandle;
			try {
				struHandle = new RandomAccessFile(fileDbf, "rw");
			} catch (FileNotFoundException e) {
				fileDbf = null;
				struHandle = null;
				return "";
			}

			byte[] fileBuf = strPrintValue.getBytes("UTF-8");
			try {
				struHandle.write(fileBuf, 0, fileBuf.length);
			} catch (IOException e) {
				fileDbf = null;
				struHandle.close();
				return "";
			}
			fileBuf = null;

			try {
				struHandle.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			strPrintFileName = "";
		}

		return strPrintFileName;
	}
}
