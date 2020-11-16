function fileQueueError(file, errorCode, message) {

}

/**
 * 当文件选择对话框关闭消失时，如果选择的文件成功加入上传队列，
 * 那么针对每个成功加入的文件都会触发一次该事件（N个文件成功加入队列，就触发N次此事件）。
 * @param {} file
 * id : string,			    // SWFUpload控制的文件的id,通过指定该id可启动此文件的上传、退出上传等
 * index : number,			// 文件在选定文件队列（包括出错、退出、排队的文件）中的索引，getFile可使用此索引
 * name : string,			// 文件名，不包括文件的路径。
 * size : number,			// 文件字节数
 * type : string,			// 客户端操作系统设置的文件类型
 * creationdate : Date,		// 文件的创建时间
 * modificationdate : Date,	// 文件的最后修改时间
 * filestatus : number		// 文件的当前状态，对应的状态代码可查看SWFUpload.FILE_STATUS }
 */
function fileQueued(file){
//这个是否是把文件的名称传递给了隐藏域
    //我们还可以把文件的大小也传给隐藏域
    //此时判断选择的文件类型是否符合要求，对于符合要求的电子文件进行队列的添加以及隐藏grid中的添加工作
    //文件类型
    var wjlx = Ext.getCmp('wjlx').getChecked()[0].boxLabel;
    try {
        var suffix=file.type.split(".")[file.type.split(".").length-1];
        var t=coverString(suffix,wjlx);
        Ext.getCmp("_g_data").getStore().add({
            filename : file.name,
            size : file.size,
            id:file.id
        });
        if(t){
            //再把上传文件文本框中赋予选择的文件值
            if (Ext.getCmp("biz_code_").getValue() == "") {
                Ext.getCmp("biz_code_").setValue(file.name);
            } else {
                Ext.getCmp("biz_code_").setValue(
                    Ext.getCmp("biz_code_").getValue() + "," + file.name);
            }
        }
    }catch(e){
        AOS.err(e.message);
    }
}
function  coverString(subStr,str){
    var reg = eval("/"+subStr+"/ig");
    return reg.test(str);
}
//选择文件窗口打开时触发
function fileDialogStart() {
    Ext.getCmp("_g_data").getStore().removeAll();

};
function fileDialogComplete(numFilesSelected, numFilesQueued) {


}

function uploadProgress(file, bytesLoaded) {
    fileDialogComplete

}
function uploadStart(){


}
function uploadSuccess(file, serverData) {
        //alert("上传成功!"+file.name);
        //得到总文件数
    var nofinishcount=this.getStats().files_queued;
    var countAll=Ext.getCmp("_g_data").getStore().getCount();
    //this.getStats().files_queued表示剩余电子文件数
    //alert(this.getStats().files_queued);
    var message='<%=request.getAttribute("message")%>';

        var progressBar=Ext.Msg.show({
            title:"上传进度",
            msg:"加载中......",
            progress:true,
            width:300
        });

    //得到当前挂接文件数
    var bartext="";
    var curnum=0;
    var count=countAll-nofinishcount;
    Ext.TaskManager.start({
        run:function () {
            if (count>=countAll) {
                progressBar.hide();
                AOS.tip("挂接完成!");
                //挂接完毕后把选择框中的文件名称清除
                Ext.getCmp("biz_code_").setValue("");
            }
            var remarkValue=Ext.getCmp("remark_").getValue();
            //获取response中的值
            //document.getElementById("remark_").innerHTML=message;
            Ext.getCmp("remark_").setValue(remarkValue+serverData);
            curnum=count/countAll;
            bartext=Math.round(curnum*100)+"%";
            progressBar.updateProgress(curnum,bartext);
        },
        interval:1000000
    });









}


function cancelUpload(){
    var infoTable = document.getElementById("infoTable");
    var rows = infoTable.rows;
    var ids = new Array();
    var row;
    if(rows==null){
        return false;
    }
    for(var i=0;i<rows.length;i++){
        ids[i] = rows[i].id;
    }
    for(var i=0;i<ids.length;i++){
        deleteFile(ids[i]);
    }
}

function deleteFile(fileId){
    //用表格显示
    var infoTable = document.getElementById("infoTable");
    var row = document.getElementById(fileId);
    infoTable.deleteRow(row.rowIndex);
    swfu.cancelUpload(fileId,false);
}

function uploadComplete(file) {
    try {
        /*  I want the next upload to continue automatically so I'll call startUpload here */
        if (this.getStats().files_queued > 0) {
            this.startUpload();
        } else {
            var progress = new FileProgress(file,  this.customSettings.upload_target);
            progress.setComplete();
            progress.setStatus("<font color='red'>所有文件上传完毕!</b></font>");
            progress.toggleCancel(false);
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadError(file, errorCode, message) {
    var imageName =  "<font color='red'>文件上传出错!</font>";
    var progress;
    try {
        switch (errorCode) {
            case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                try {
                    progress = new FileProgress(file,  this.customSettings.upload_target);
                    progress.setCancelled();
                    progress.setStatus("<font color='red'>取消上传!</font>");
                    progress.toggleCancel(false);
                }
                catch (ex1) {
                    this.debug(ex1);
                }
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                try {
                    progress = new FileProgress(file,  this.customSettings.upload_target);
                    progress.setCancelled();
                    progress.setStatus("<font color='red'>停止上传!</font>");
                    progress.toggleCancel(true);
                }
                catch (ex2) {
                    this.debug(ex2);
                }
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                imageName = "<font color='red'>文件大小超过限制!</font>";
                break;
            default:
                //alert(message);
                break;
        }
        addFileInfo(file.id,imageName);
    } catch (ex3) {
        this.debug(ex3);
    }

}


function addImage(src) {
    var newImg = document.createElement("img");
    newImg.style.margin = "5px";

    document.getElementById("thumbnails").appendChild(newImg);
    if (newImg.filters) {
        try {
            newImg.filters.item("DXImageTransform.Microsoft.Alpha").opacity = 0;
        } catch (e) {
            // If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
            newImg.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + 0 + ')';
        }
    } else {
        newImg.style.opacity = 0;
    }

    newImg.onload = function () {
        fadeIn(newImg, 0);
    };
    newImg.src = src;
}

function fadeIn(element, opacity) {
    var reduceOpacityBy = 5;
    var rate = 30;	// 15 fps


    if (opacity < 100) {
        opacity += reduceOpacityBy;
        if (opacity > 100) {
            opacity = 100;
        }

        if (element.filters) {
            try {
                element.filters.item("DXImageTransform.Microsoft.Alpha").opacity = opacity;
            } catch (e) {
                // If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
                element.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + opacity + ')';
            }
        } else {
            element.style.opacity = opacity / 100;
        }
    }

    if (opacity < 100) {
        setTimeout(function () {
            fadeIn(element, opacity);
        }, rate);
    }
}



/* ******************************************
 *	FileProgress Object
 *	Control object for displaying file info
 * ****************************************** */

function FileProgress(file, targetID) {
    this.fileProgressID = "divFileProgress";

    this.fileProgressWrapper = document.getElementById(this.fileProgressID);
    if (!this.fileProgressWrapper) {
        this.fileProgressWrapper = document.createElement("div");
        this.fileProgressWrapper.className = "progressWrapper";
        this.fileProgressWrapper.id = this.fileProgressID;

        this.fileProgressElement = document.createElement("div");
        this.fileProgressElement.className = "progressContainer";

        var progressCancel = document.createElement("a");
        progressCancel.className = "progressCancel";
        progressCancel.href = "#";
        progressCancel.style.visibility = "hidden";
        progressCancel.appendChild(document.createTextNode(" "));

        var progressText = document.createElement("div");
        progressText.className = "progressName";
        progressText.appendChild(document.createTextNode("上传文件: "+file.name));

        var progressBar = document.createElement("div");
        progressBar.className = "progressBarInProgress";

        var progressStatus = document.createElement("div");
        progressStatus.className = "progressBarStatus";
        progressStatus.innerHTML = "&nbsp;";

        this.fileProgressElement.appendChild(progressCancel);
        this.fileProgressElement.appendChild(progressText);
        this.fileProgressElement.appendChild(progressStatus);
        this.fileProgressElement.appendChild(progressBar);

        this.fileProgressWrapper.appendChild(this.fileProgressElement);
        document.getElementById(targetID).style.height = "75px";
        document.getElementById(targetID).appendChild(this.fileProgressWrapper);
        fadeIn(this.fileProgressWrapper, 0);

    } else {
        this.fileProgressElement = this.fileProgressWrapper.firstChild;
        this.fileProgressElement.childNodes[1].firstChild.nodeValue = "上传文件: "+file.name;
    }

    this.height = this.fileProgressWrapper.offsetHeight;

}
FileProgress.prototype.setProgress = function (percentage) {
    this.fileProgressElement.className = "progressContainer green";
    this.fileProgressElement.childNodes[3].className = "progressBarInProgress";
    this.fileProgressElement.childNodes[3].style.width = percentage + "%";
};
FileProgress.prototype.setComplete = function () {
    this.fileProgressElement.className = "progressContainer blue";
    this.fileProgressElement.childNodes[3].className = "progressBarComplete";
    this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setError = function () {
    this.fileProgressElement.className = "progressContainer red";
    this.fileProgressElement.childNodes[3].className = "progressBarError";
    this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setCancelled = function () {
    this.fileProgressElement.className = "progressContainer";
    this.fileProgressElement.childNodes[3].className = "progressBarError";
    this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setStatus = function (status) {
    this.fileProgressElement.childNodes[2].innerHTML = status;
};

FileProgress.prototype.toggleCancel = function (show, swfuploadInstance) {
    this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
    if (swfuploadInstance) {
        var fileID = this.fileProgressID;
        this.fileProgressElement.childNodes[0].onclick = function () {
            swfuploadInstance.cancelUpload(fileID);
            return false;
        };
    }
};
