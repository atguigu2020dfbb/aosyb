<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="/aosyb/static/js/qwebchannel.js"></script>
	<script type="text/javascript" src="/aosyb/static/js/jquery-1.10.1.min.js"></script>
	<link rel="stylesheet" href="/aosyb/static/weblib/bootstrap/css/bootstrap.min.css">
	<script src="/aosyb/static/weblib/bootstrap/js/bootstrap.min.js"></script>

	<style type="text/css">
		html {
			height: 100%;
			width: 100%;
		}
		#input {
			width: 400px;
			margin: 0 10px 0 0;
		}
		#output {
			width: 500px;
			height: 200px;
			margin: 0 10px 0 0;
		}

	</style>
<title>Insert title here</title>
</head>
<body>
<tr>
		<div style="float:left;"
		>
			<img id="bigPriDev" width="640" height="480"  /></img>
		</div>
<div class="col-sm-4" style="float:left;" >
	<div class="panel panel-default space">
		<div class="panel-heading">
			<label id="lab1" class=" control-label">设备1</label>
		</div>

		<div class="panel-body">



			<div class="form-group">
				<label id="lab5" class="col-sm-5 control-label">图片类型</label>
				<div class="col-sm-7">
					<select id="priModelList" class="form-control"></select>
				</div>
			</div>

			<div class="form-group">
				<label id="lab6" class="col-sm-5 control-label">分 辨 率</label>
				<div class="col-sm-7">
					<select id="priResolutionList" class="form-control">
					</select>
				</div>
			</div>

			<div class="form-group">
				<label  id="lab7" class="col-sm-5 control-label">原始尺寸</label>
				<div class="col-sm-7">
					<select id="setScanSize" class="form-control">
					</select>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-3">
					<button id="openPriVideo" type="button" class="btn btn-primary btn-sm">
						<span class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span>打开
					</button>
				</div>
				<div class="col-sm-3">
					<button id="closePriVideo" type="button" class="btn btn-default btn-sm btn-danger">
						<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>关闭
					</button>
				</div>
				<div class="col-sm-3">
					<button id="photographPri" type="button" class="btn btn-default btn-sm btn-success">
						<span class="glyphicon glyphicon-camera" aria-hidden="true"></span>拍照
					</button>
				</div>
				<div class="col-sm-3">
					<button  id="upload" onclick="upload('${requestScope.id_}','${requestScope.djbh}')" type="button" class="btn btn-default btn-success btn-sm">
						<span class="glyphicon glyphicon-open" aria-hidden="true"></span>上传
					</button>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-6">
					<center><button id="deleteFile" type="button" class="btn btn-default btn-sm btn-info">
					<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除文件
				</button>
					</center>
			</div>
				<div class="form-group">
					<div class="col-sm-6">
						<center><button id="clearFile" type="button" class="btn btn-default btn-sm btn-info">
							<span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>清空文件
						</button></center>
					</div>
			</div>
			<div class="form-group">
				<div class="col-sm-12">
			<tr>
				<div id="container" > </div></td>
			</tr>

				</div>

			</div>



		</div>
	</div>
</div>
	<img id="bigSubDev" width="640" height="480"  hidden="true"/></img>
	<img id="devPhoto" width="360" height="270"  hidden="true"/></img>
	<img id="devPhoto2" width="360" height="270"  hidden="true"/></img>
	<textarea id="output" hidden="true"></textarea>
	<input id="input" hidden="true" style="width:666px"/><input id="sendOrder" hidden="true" type="button" value="发送命令" />
	<input id="openHtml" hidden="true" type="button" value="初始化" />
	<input id="closeHtml" hidden="true" type="button" value="反初始化" />
	<input id="base64" value="" hidden="true"/>
	<input id="Zbase64" value="" hidden="true"/>
	<input id="imageBlend" hidden="true" type="button" value="合成拍照" />
	<label id="lab2" hidden="true"></label>
	<select id="subModelList" hidden="true" style="width: 120px" ></select>
	<select id="subResolutionList" hidden="true" style="width: 120px" ></select>
	<input id="openSubVideo" hidden="true" type="button" value="打开副视频" />
	<input id="closeSubVideo" hidden="true" type="button" value="关闭副视频"  />
	<input id="photographSub" hidden="true" type="button" value="副头拍照"  />
	<input id="rotateLeft" hidden="true" type="button" value="左转" />
	<input id="rotateRight" hidden="true" type="button" value="右转" />
	<input id="showProperty" hidden="true" type="button" value="属性设置" />
	<input id="startIDCard" type="button" hidden="true" value="启动二代证阅读" />
	<input id="stopIDCard" type="button" hidden="true" value="停止" />
	<input id="singleReadIDCard" type="button" hidden="true" value="单次读取二代证" />
	<input id="verifyFaceDetect" type="button" hidden="true" value="人证对比" />
	<input id="imageMatching" type="button" hidden="true" value="图片对比" />
	<input id="getIdcardImage" type="button" hidden="true" value="获取二代证图像" />
	<input id="startLive" type="button" hidden="true" value="开启活体检测" />
	<input id="stopLive" type="button" hidden="true" value="停止活体检测" />
	<input id="priRecord" type="button" hidden="true" value="主头录像" />
	<input id="stopPriRecord" type="button" hidden="true" value="停止主头录像" />
	<input id="subRecord" type="button" hidden="true" value="副头录像" />
	<input id="stopSubRecord" type="button" hidden="true" value="停止副头录像" />
	<input id="composePDF" type="button" hidden="true" value="合成PDF" />
	<input id="getFileBase64" type="button" hidden="true" value="获取文件base64" />
	<input id="discernOcr" type="button" hidden="true" value="OCR" />
	<input id="DiscernOCRTempl" type="button" hidden="true" value="OCR模板识别" />
	<input id="singleReadBarcode" type="button" hidden="true" value="条码阅读" />
	<input id="setScanSizeSub" type="button" hidden="true" value="设置副头拍照尺寸" />
	<input id="setdeskew" type="checkbox" hidden="true" value="" />
	<input id="SetMoveDetec" type="checkbox" hidden="true" value=""/>
	<input id="enableDate" type="checkbox" hidden="true" value="" />
	<input id="enableWord" type="checkbox" hidden="true" value="" />
	<input id="setgray" type="checkbox" hidden="true" value="" />
	<input id="setthreshold" type="checkbox" hidden="true" value="" />
	<input id="delbkcolor" type="checkbox" hidden="true" value=""  />
	<input id="setreverse" type="checkbox" hidden="true" value=""  />
	<input id="InitBiokey" type="button" hidden="true" value="初始化指纹仪" />
	<input id="GetBiokeyFeature" type="button"  hidden="true" value="获取指纹特征"/>
	<img   id="BiokeyImg1" alt="指纹图像1" hidden="true" width=35 height=30/>
	<input id="StopBiokeyFeature"  type="button" hidden="true" value="停止获取指纹特征"/>
	<input id="GetBiokeyTemplate" type="button" hidden="true" value="获取指纹模板" />
	<img   id="BiokeyImg2" alt="指纹图像2" hidden="true" width=35 height=30/>
	<input id="StopBiokeyTemplate" type="button" hidden="true" value="停止获取指纹模板" />
	<input id="BiokeyVerify" type="button" hidden="true" value="指纹对比" />
	<input id="DeinitBiokey" type="button" hidden="true" value="反初始化指纹仪"/>

	<br />
</tr>



</body>
<script type="text/javascript">
	//BEGIN SETUP
	//拍照数据（base64）
	var imageIndex;
	var baseUrl;
	var socket;
	//储存图片路径的变量和数组
	var imgPath = "";
	var imgPathArray = new Array();
	var base64Array=new Array();
	//预览图片
	function openImage(imageItem)
	{
		var url = imageItem.parentNode.id;
		window.showModalDialog(url, null, "dialogHeight:800px; dialogWidth:1000px; resizable:no");
	}

	//增加图片缩略图
	function addImgDiv(){
		var container =document.getElementById('container');
		var newchild = document.createElement("div");
		newchild.setAttribute("style", "float:left");
		//imgPath = "file:///" + imgPath ;
		newchild.setAttribute("id", imgPath);
		imgPathArray.push(imgPath);////增加缩略图时默认把路径加入图片数组

		var Zbase64=document.getElementById("Zbase64").value;

		newchild.innerHTML = "<img width='105' height='85' src='"+Zbase64+"' onclick='openImage(this)' /></img>" +
				"<input name='checkimg' id='"+Zbase64+"' type='checkbox'  value='"+imgPath+"' checked='checked' onchange='checkboxClicked(this)' />";
		container.appendChild(newchild);
	}
	//清空缩略图
	function removeAll(){
		document.getElementById('container').innerHTML = "";
		imgPathArray = [];
	}
	//处理缩略图CheckBox点击事件
	function checkboxClicked(checkboxItem)
	{
		if(imgPathArray.indexOf(checkboxItem.parentNode.id) < 0)
		{
			imgPathArray.push(checkboxItem.parentNode.id);

		} else{
			var index = imgPathArray.indexOf(checkboxItem.parentNode.id);
			//暂时数组中不去掉
			//imgPathArray.splice(index, 1);
		}
	}

	function openSocket() {
		socket = new WebSocket(baseUrl);
		socket.onclose = function()
		{
			console.error("web channel closed");
		};
		socket.onerror = function(error)
		{
			console.error("web channel error: " + error);
		};
		socket.onopen = function()
		{
			output("WebSocket connected");
			new QWebChannel(socket, function(channel) {
				//获取注册的对象
				window.dialog = channel.objects.dialog;
				//网页关闭函数
				window.onbeforeunload = function() {
					dialog.get_actionType("closeSignal");
				}
				window.onunload = function() {
					dialog.get_actionType("closeSignal");
				}
				//反初始化
				document.getElementById("closeHtml").onclick = function() {
					dialog.get_actionType("closeSignal");
					var element = document.getElementById("bigPriDev");
					element.src = "";
					var element1 = document.getElementById("bigSubDev");
					element1.src = "";
				};
				//初始化
				document.getElementById("openHtml").onclick = function() {
					dialog.html_loaded("two");
				};
				/***********设备状态更改（区分摄像头）*****************/
				//网页控件事件，模式列表点击
				document.getElementById("priModelList").onchange = function() {
					//清除展示信息
					var resolutionList = document.getElementById("priResolutionList");
					resolutionList.options.length = 0;
					var select = document.getElementById("priModelList");
					dialog.devChanged("primaryDev_:" + select.value);
				};
				//网页控件事件，分辨率列表点击
				document.getElementById("priResolutionList").onchange = function() {
					//清除展示信息
					var select = document.getElementById("priResolutionList");
					dialog.devChanged("primaryDev_:" + select.value);
				};
				//网页控件事件，模式列表点击
				document.getElementById("subModelList").onchange = function() {
					//清除展示信息
					var resolutionList = document.getElementById("subResolutionList");
					resolutionList.options.length = 0;
					var select = document.getElementById("subModelList");
					dialog.devChanged("subDev_:" + select.value);
				};
				//网页控件事件，分辨率列表点击
				document.getElementById("subResolutionList").onchange = function() {
					//清除展示信息
					var select = document.getElementById("subResolutionList");
					dialog.devChanged("subDev_:" + select.value);
				};
				//设置尺寸列表点击，只有主头有设置尺寸
				document.getElementById("setScanSize").onchange = function() {
					var select = document.getElementById("setScanSize");
					if(select.value == "原始尺寸")
					{
						dialog.get_actionType("setScanSize_ori");
					}
					else if(select.value == "A5")
					{
						dialog.get_actionType("setScanSize_A5");
					}
					else if(select.value == "A4")
					{
						dialog.get_actionType("setScanSize_A4");
					}
					else if(select.value == "卡片")
					{
						dialog.get_actionType("setScanSize_card");
					}
				};
				//设置副摄像头拍照尺寸
				document.getElementById("setScanSizeSub").onclick = function() {
					//第四个参数如果为空则会设置指定的长宽；如果不为空则把视频大小改为正常大小
					dialog.get_functionTypes("setScanSizeSub", "800", "1024", "");
					//dialog.get_functionTypes("setScanSizeSub", "800", "1024", "ori");

				};

				//打开主视频
				document.getElementById("openPriVideo").onclick = function() {
					var resolutionList = document.getElementById("priResolutionList");
					resolutionList.options.length = 0;
					var modelList = document.getElementById("priModelList");
					modelList.options.length = 0;
					var label1 = document.getElementById("lab1").innerHTML;
					dialog.devChanged("primaryDev_:" + label1);
				};
				//关闭主视频
				document.getElementById("closePriVideo").onclick = function() {
					dialog.get_actionType("closePriVideo");
					var element = document.getElementById("bigPriDev");
					element.src = "";
				};
				//打开副视频
				document.getElementById("openSubVideo").onclick = function() {
					var resolutionList = document.getElementById("subResolutionList");
					resolutionList.options.length = 0;
					var modelList = document.getElementById("subModelList");
					modelList.options.length = 0;
					var label1 = document.getElementById("lab2").innerHTML;
					dialog.devChanged("subDev_:" + label1);
				};
				//关闭副视频
				document.getElementById("closeSubVideo").onclick = function() {
					dialog.get_actionType("closeSubVideo");
					var element = document.getElementById("bigSubDev");
					element.src = "";
				};
				//主摄像头拍照按钮点击
				document.getElementById("photographPri").onclick = function() {
					dialog.photoBtnClicked("primaryDev_");
					dialog.get_actionType("savePhotoPriDev");
				};
				//副摄像头拍照按钮点击
				document.getElementById("photographSub").onclick = function() {

					dialog.photoBtnClicked("subDev_");
					dialog.get_actionType("savePhotoSubDev");
				};
				//左转
				document.getElementById("rotateLeft").onclick = function() {
					dialog.get_actionType("rotateLeft");
				};
				//右转
				document.getElementById("rotateRight").onclick = function() {
					dialog.get_actionType("rotateRight");
				};
				//属性设置
				document.getElementById("showProperty").onclick = function() {
					dialog.get_actionType("showProperty");
				};
				//纠偏裁边
				document.getElementById("setdeskew").onclick = function() {
					dialog.get_actionType("setdeskew");
				};
				//二代证阅读
				document.getElementById("startIDCard").onclick = function() {
					dialog.get_actionType("startIDCard");
				};
				//停止二代证阅读
				document.getElementById("stopIDCard").onclick = function() {
					dialog.get_actionType("stopIDCard");
				};
				//单次二代证阅读
				document.getElementById("singleReadIDCard").onclick = function() {
					dialog.get_actionType("singleReadIDCard");
				};
				//人证比对
				document.getElementById("verifyFaceDetect").onclick = function() {
					dialog.get_actionType("verifyFaceDetect");
				};
				/*********************活体功能*******************************************/
				//启动活体识别
				document.getElementById("startLive").onclick = function() {
					dialog.get_actionType("startLive");
				};
				//关闭活体识别
				document.getElementById("stopLive").onclick = function() {
					dialog.get_actionType("stopLive");

				};

				//图片对比
				document.getElementById("imageMatching").onclick = function() {
					//第一种调用格式适用于本地文件比较，图片文件里面需要带人脸
					//dialog.get_functionTypes("imageMatching", "c:/eloamFile/1.jpg", "c:/eloamFile/2.jpg", "");
					//第二种调用格式适用于主副头图像比较；先使用摄像头各拍一张照片，再点击按钮比较
					dialog.get_functionTypes("imageMatching", "", "", "");
				};

				//获取二代证图像
				//Flag 1表示头像，2表示正面，3表示反面，4表示正反垂直合成，
				//5表示反正垂直合成，6表示正反水平合成，7表示反正水平合成
				document.getElementById("getIdcardImage").onclick = function() {
					dialog.get_functionTypes("getIdcardImage", "5", "", "");
				};

				/**********录像************/
				//主头录像
				document.getElementById("priRecord").onclick = function() {
					//第2个参数为保存文件地址,第3个麦克风索引（默认没有麦）,第4个为空
					dialog.get_functionTypes("priRecord", "C:\\priRecord.mp4", "-1", "");
				};
				//停止主头录像
				document.getElementById("stopPriRecord").onclick = function() {
					dialog.get_actionType("stopPriRecord");
				};
				//副头录像
				document.getElementById("subRecord").onclick = function() {
					//第2个参数为保存文件地址,第3个麦克风索引（默认没有麦）,第4个为空
					dialog.get_functionTypes("subRecord", "C:\\subRecord.mp4", "-1", "");
				};
				//停止副头录像
				document.getElementById("stopSubRecord").onclick = function() {
					dialog.get_actionType("stopSubRecord");
				};

				//图像合成
				document.getElementById("imageBlend").onclick = function() {
					dialog.get_actionType("imageBlend");
				};

				//获取文件base64
				document.getElementById("getFileBase64").onclick = function() {


					dialog.get_functionTypes("getFileBase64",imgPath , "","");
					//dialog.get_functionTypes("getFileBase64","C:/1234.pdf" , "","");
				};
				//图片合成pdf
				document.getElementById("composePDF").onclick = function() {
					if(imgPathArray.length > 0)
					{
						for (var i = 0; i < imgPathArray.length; i++)
						{
							//发送合成pdf图片的路径，第2个参数：图片文件路径，第3，第4个参数为空
							var path = imgPathArray[i];
							if(path.indexOf("file:///") >= 0)
							{
								path = path.substr(8);
							}
							dialog.get_functionTypes("sendPDFImgPath", path, "","");
						}
						//发送合成pdf命令
						//第2个参数：保存路径，如果该路径不存在则保存在.exe文件下的eloamFile文件夹
						//第3个参数：保存文件名称，如果为空则按照当前时间命名，
						//第4个参数为图片质量1-100的十进制整数x（以x:1保存）100最好
						//dialog.get_functionTypes("composePDF", "C:" , "1234", "75");
						//dialog.get_functionTypes("composePDF", "C:" , "12345650", "50");
						dialog.get_functionTypes("composePDF", "C:" , "12345100", "75");
					}
				};
				//删除本地文件
				document.getElementById("deleteFile").onclick = function() {
					//dialog.get_functionTypes("deleteFile", "C:\\Users\\Administrator\\Desktop\\eloamPhoto\\20180903-200102046.jpg", "", "");
					//dialog.get_functionTypes("deleteFile", "C:/Users/Administrator/Desktop/eloamPhoto/eeee.jpg", "", "");

					var doms = document.getElementsByName("checkimg");
					var container = document.getElementById("container");
					var str = '';                        //定义拼接字符变量
					for(var i=0; i<doms.length; i++){
						if(doms[i].checked==true){
							//此时执行删除操作
							dialog.get_functionTypes("deleteFile", doms[i].value, "", "");
							var base64id=doms[i].id;
							var index = base64Array.indexOf(base64id);
							base64Array.splice(index, 1);
							//此时imgPathArray也需要删除
							//imgPathArray.splice(doms[i].value,1);
							container.removeChild(document.getElementById(doms[i].value));
							//imgPathArray.remove(doms[i].value);
							//此时从base64数组中移除
							i--;
						}
					}

					//removeAll();
					/*for (var i = 0; i < imgPathArray.length; i++)
                    {
                        //删除文件，第2个参数：图片文件路径，第3，第4个参数为空
                        var path = imgPathArray[i];
                        if(path.indexOf("file:///") >= 0)
                        {
                            path = path.substr(8);
                        }
                        dialog.get_functionTypes("deleteFile", path, "", "");
                    }*/
					//removeAll();
					//imgPathArray = [];
				};



				document.getElementById("clearFile").onclick = function() {
					var doms = document.getElementsByName("checkimg");
					var container = document.getElementById("container");
					var str = '';                        //定义拼接字符变量
					for(var i=0; i<doms.length; i++){
						//此时执行删除操作
						dialog.get_functionTypes("deleteFile", doms[i].value, "", "");
						var base64id=doms[i].id;
						var index = base64Array.indexOf(base64id);
						base64Array.splice(index, 1);
						container.removeChild(document.getElementById(doms[i].value));
						i--;
					}
					removeAll();
				}


				//网页发送命令按钮
				document.getElementById("sendOrder").onclick = function() {
					var order = document.getElementById("input").value;
					if(!order)
						return;
					dialog.set_configValue(order);
				}
				//ocr
				document.getElementById("discernOcr").onclick = function() {
					dialog.get_actionType("discernOcr");
				};
				//ocr模板识别
				document.getElementById("DiscernOCRTempl").onclick = function() {
					dialog.get_functionTypes("DiscernOCRTempl", "C:/idCard.xml", "", "");
				};

				/*********************指纹功能*******************************************/
				//初始化指纹仪
				document.getElementById("InitBiokey").onclick = function() {
					dialog.get_actionType("InitBiokey");

				};
				//获取指纹特征
				document.getElementById("GetBiokeyFeature").onclick = function() {
					dialog.get_actionType("GetBiokeyFeature");

				};
				//停止获取指纹特征
				document.getElementById("StopBiokeyFeature").onclick = function() {
					dialog.get_actionType("StopBiokeyFeature");

				};
				//获取指纹模板
				document.getElementById("GetBiokeyTemplate").onclick = function() {
					dialog.get_actionType("GetBiokeyTemplate");

				};
				//停止获取指纹模板
				document.getElementById("StopBiokeyTemplate").onclick = function() {
					dialog.get_actionType("StopBiokeyTemplate");

				};
				//指纹比对
				document.getElementById("BiokeyVerify").onclick = function() {
					dialog.get_actionType("BiokeyVerify");

				};
				//反初始化指纹
				document.getElementById("DeinitBiokey").onclick = function() {
					dialog.get_actionType("DeinitBiokey");
				};
				//条码阅读
				document.getElementById("singleReadBarcode").onclick = function() {
					dialog.get_actionType("singleReadBarcode");
				};
				//设置/取消自动连拍
				document.getElementById("SetMoveDetec").onclick = function() {
					dialog.get_actionType("SetMoveDetec");
				};
				//设置日期
				document.getElementById("enableDate").onclick = function() {
					//发送设置日期
					//第2个参数：top，bottom 代表上下，后面的1，2，3代表左中右
					//第3个参数：10进制字体颜色 16711680
					dialog.get_functionTypes("setFontProperty", "20" , "", "");
					dialog.get_functionTypes("enableDate", "bottom2" , "13467442", "");
				};
				//设置文字水印
				document.getElementById("enableWord").onclick = function() {
					//发送设置日期
					//第2个参数：top，bottom 代表上下，后面的1，2，3代表左中右
					//第3个参数：10进制字体颜色 16711680
					//第4个参数：水印内容

					//修改水印文字属性第二个参数为文字大小
					dialog.get_functionTypes("setFontProperty", "40" , "", "");
					dialog.get_functionTypes("enableWord", "top1" , "13467442", "汪汪汪汪汪汪汪汪汪汪汪");
				};
				//灰度化
				document.getElementById("setgray").onclick = function() {
					dialog.get_actionType("setgray");
				};
				//二值化
				document.getElementById("setthreshold").onclick = function() {
					dialog.get_actionType("setthreshold");
				};
				//去底色
				document.getElementById("delbkcolor").onclick = function() {
					dialog.get_actionType("delbkcolor");
				};
				//设置反色
				document.getElementById("setreverse").onclick = function() {
					dialog.get_actionType("setreverse");
				};

				//服务器返回消息
				dialog.sendPrintInfo.connect(function(message) {
					//读取二代证头像返回信息
					if(message.indexOf("idFaceInfo:") >= 0)
					{
						var value = message.substr(11);
						var element = document.getElementById("devPhoto2");
						element.src = "data:image/jpg;base64," + value;
						return;
					}
					//获取文件base64返回值
					else if(message.indexOf("fileBase64:") >= 0)
					{
						var value = message.substr(11);
						//此时得到base64内容
						//alert(value);
						//此时的base64的值应该存放到数组中

						return;
					}
					//output(message);

					/********主头设备信息***********/
					//设备名
					if(message.indexOf("priDevName:") >= 0)
					{
						message = message.substr(11);
						var label = document.getElementById("lab1");
						label.innerHTML = message;
					}
					//主头设备出图格式
					else if(message.indexOf("priModel:") >= 0)
					{
						message = message.substr(9);
						var select = document.getElementById("priModelList");
						if(message.indexOf("MJPG") >= 0)
						{
							select.add(new Option(message), 0);
						} else{
							select.add(new Option(message));
						}
						select.selectedIndex=0;
					}
					//主头设备分辨率
					else if(message.indexOf("priResolution:") >= 0)
					{
						message = message.substr(14);
						var select = document.getElementById("priResolutionList");
						select.add(new Option(message));
						if(select.options.length > 3)
						{
							select.selectedIndex = 2;
						}
					}
					/********副头设备信息***********/
					//设备名
					else if(message.indexOf("subDevName:") >= 0)
					{
						message = message.substr(11);
						var label = document.getElementById("lab2");
						label.innerHTML = message;
					}
					//副头设备出图格式
					else if(message.indexOf("subModel:") >= 0)
					{
						message = message.substr(9);
						var select = document.getElementById("subModelList");
						if(message.indexOf("MJPG") >= 0)
						{
							select.add(new Option(message), 0);
						} else{
							select.add(new Option(message));
						}
						select.selectedIndex=0;
					}
					//副头设备分辨率
					else if(message.indexOf("subResolution:") >= 0)
					{
						message = message.substr(14);
						var select = document.getElementById("subResolutionList");
						select.add(new Option(message));
						if(select.options.length > 2)
						{
							select.selectedIndex = 1;
						}
					}
					//后台程序返回设备序列号
					else if(message.indexOf("SerialNumber:") >= 0)
					{
						var SerialNumber = message.substr(13);
					}
					//后台程序条形码、二维码识别结果
					else if(message.indexOf("BarcodeInfo:") >= 0)
					{
						//格式为 "BarcodeInfo:" + "个数\n" + "识别结果\n"。每个每个结果后有换行符
						var BarcodeInfo = message.substr(12);
					}
					//图片保存后返回路径关键字savePhoto_success:
					else if(message.indexOf("savePhoto_success:") >= 0)
					{
						imgPath = message.substr(18);
						addImgDiv();
					}
					//合成pdf成功，返回路径关键字composePDF_success:
					else if(message.indexOf("composePDF_success:") >= 0)
					{
						var path = message.substr(19);
						removeAll();
						//alert(path);
					}
					//指纹特征数据返回
					if(message.indexOf("GetBiokeyFeature_success!") >= 0)
					{
						var img = document.getElementById("BiokeyImg1");
						var name = message.substr(25);
						//img.src = "file:///d:\\BiokeyImg1.jpg" +'?'+ Math.random();
						img.src = name +'?'+ Math.random();
					}
					//指纹模板数据返回
					if(message.indexOf("GetBiokeyTemplate_success!") >= 0)
					{
						var img = document.getElementById("BiokeyImg2");
						var name = message.substr(26);
						//img.src = "file:///d:\\BiokeyImg2.jpg" +'?'+ Math.random();
						img.src = name +'?'+ Math.random();

					}

				});
				//接收图片流用来展示，多个，较小的base64，主头数据
				dialog.send_priImgData.connect(function(message) {
					var element = document.getElementById("bigPriDev");
					element.src = "data:image/jpg;base64," + message;
				});
				//接收图片流用来展示，多个，较小的base64，副头数据
				dialog.send_subImgData.connect(function(message) {
					var element = document.getElementById("bigSubDev");
					element.src = "data:image/jpg;base64," + message;
				});

				//接收拍照base64，主头数据
				dialog.send_priPhotoData.connect(function(message) {

					var element = document.getElementById("devPhoto");
					element.src = "data:image/jpeg;base64," + message;
					var Zbase64=document.getElementById("Zbase64");
					Zbase64.value = "data:image/jpeg;base64," + message;

					base64Array.push("data:image/jpeg;base64," + message);
					document.getElementById("Zbase64").value="data:image/jpeg;base64," + message;

				});
				//接收拍照base64，副头数据
				dialog.send_subPhotoData.connect(function(message) {
					var element = document.getElementById("devPhoto2");
					element.src = "data:image/jpg;base64," + message;
				});

				output("ready to send/receive messages!");
				//网页加载完成信号
				//dialog.html_loaded("two");
				dialog.html_loaded("faceDetect_two");

				//设置图片dpi，第二个参数是dpi大小
				dialog.get_functionTypes("setImageProperty", "300", "", "");
			});
		}
	}
	//输出信息
	function output(message){
		var output = document.getElementById("output");
		output.innerHTML = output.innerHTML + message + "\n";
		output.scrollTop = output.scrollHeight;
	}
	//网页初始化函数
	window.onload = function() {
		baseUrl = "ws://127.0.0.1:12345";
		//output("Connecting at " + baseUrl + ".");
		openSocket();
		var scanSize = document.getElementById('setScanSize');
		scanSize.add(new Option("原始尺寸"));
		scanSize.add(new Option("A5"));
		scanSize.add(new Option("A4"));
		scanSize.add(new Option("卡片"));
		scanSize.selectIndex = 0;

		imageIndex = -1;
	}
	function removeFile(){
		for (var i = 0; i < imgPathArray.length; i++)
		{
			//删除文件，第2个参数：图片文件路径，第3，第4个参数为空
			var path = imgPathArray[i];
			if(path.indexOf("file:///") >= 0)
			{
				path = path.substr(8);
			}
			dialog.get_functionTypes("deleteFile", path, "", "");
		}
	}
	function test(){
		//创建FileSystemObject对象fso
		var fso = new ActiveXObject("Scripting.FileSystemObject");
		//path 删除文件的路径(文件夹)
		var path = "c:\\eloamFile";
		//如果指定的文件夹存在则返回 True ；否则返回 False
		if(fso.FolderExists(path))  {
			try{
				//删除除文件夹以外的所有文件
				fso.DeleteFile(path+"\\*");
				//删除所有文件夹
				fso.DeleteFolder(path+"\\*");
				alert("删除成功！");
			}catch(e){
				alert("*.*" + e.message);
			}
		}else
		{
			alert("不存在这个目录："+path);
		}
	}
	function upload(id_,djbh){
		if(base64Array.length>1||base64Array.length<1){
			alert("仅支持上传一张介绍信");
			return;
		};
		removeFile();
		$.ajax({
			url:'upload.jhtml',
			type: 'POST',
			dataType: 'text',
			cache: false,
			data: {
				'id_':id_,
				'djbh':djbh,
				base64:base64Array[0]
			},
			success: function(data){

				data=JSON.parse(data);
				if(data.appcode==1){
					removeAll();

					alert("上传成功!");
					window.opener = null;
					window.open('', '_self');
					window.close();
				}else{
					alert("上传失败!");
				}
			},
			error:function(data){
				alert("出现错误!");
			}
		});
	}
	//END SETUP
</script>
</html>