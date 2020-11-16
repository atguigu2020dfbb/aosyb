var PATH = "http://localhost:8080/Data/";
console.log("-----------"+getArgs('id'));
console.log("-----------"+getArgs('tablename'));
var id = getArgs('id');
var tablename = getArgs('tablename');
$(function() {// 自动加载
    filelist();
});
function getArgs(strParame) {//获取参数
    var args = new Object( );
    var query = location.search.substring(1);
    var pairs = query.split("&"); // Break at ampersand
    for(var i = 0; i < pairs.length; i++) {
        var pos = pairs[i].indexOf('=');
        if (pos == -1) continue;
        var argname = pairs[i].substring(0,pos);
        var value = pairs[i].substring(pos+1);
        value = decodeURIComponent(value);
        args[argname] = value;
    }
    return args[strParame];
}
function filelist() {//查询文件
    console.log("调用");
    $.ajax({
        url : "http://localhost/aosyb/archive/data/selectAllfilePageByTid.jhtml",
        type : "GET",
        dataType : 'json',
        cache : false,
        data : {
            id : id,
            tablename : tablename
        },
        success : function(result) {
            console.log("返回值: "+result.message[0].id_);
            table(result);// 1、解析并显示教师数据
            //info(result);// 2、解析并显示分页信息
            //nav(result);// 3、解析显示分页条数据
        }
    });
}
function table(result) {//显示文件列表-左侧
    $("#tablemessage").empty();
    var message = result.message;
    $.each(message,function(index, item) {
        var picture = $("<td></td>").append(item._path);
        var showBtn = $("<button></button>").addClass("btn btn-primary btn-xs show_btn").append("预览");
        showBtn.attr("show-id", item.id_).attr("dirname", item.dirname).attr("_path", item._path);
        var btnTd = $("<td></td>").append(showBtn);
        // append方法执行完成以后还是返回原来的元素
        $("<tr></tr>").append(picture).append(btnTd).appendTo("#tablemessage");
    });
}

// 预览按钮
$(document).on("click", ".show_btn", function() {
    console.log("调用1");
    var dirname = $(this).attr("dirname");
    var _path = $(this).attr("_path");
    console.log(_path);
    console.log(PATH+dirname+_path);
    var div = document.getElementById("filess");
    div.innerHTML = "";
    if(_path.indexOf(".jpg") != -1){
        var image = $("<img>").attr("style","width: 80%;height: auto; margin-left: 10%;margin-top: 3%;").attr("src",PATH+dirname+_path);
        $("#filess").append(image);
    }else if (_path.indexOf(".pdf") != -1){
        PDFObject.embed(PATH+dirname+_path, '#filess');
    }else if(_path.indexOf(".TIF") != -1){
        console.log("tiff文件");
        tiffImage(PATH+dirname+_path);
    }
});

function tiffImage(filepath){
    var xhr = new XMLHttpRequest();
    xhr.responseType = 'arraybuffer';
    xhr.open('GET', filepath);
    xhr.onload = function (e) {
        var tiff = new Tiff({buffer: xhr.response});
        var canvas = tiff.toCanvas();
        $("#filess").append(canvas);
    };
    xhr.send();
}

















// 解析显示分页信息
function info(result) {
    $("#page_info_area").empty();
    $("#page_info_area").append(
        "当前" + result.extend.list.pageNum + "页,总"
        + result.extend.list.pages + "页,总"
        + result.extend.list.total + "条记录");
    totalRecord = result.extend.list.total;
    currentPage = result.extend.list.pageNum;
}
// 解析显示分页条，点击分页要能去下一页....
function nav(result) {
    // page_nav_area
    $("#page_nav_area").empty();
    var ul = $("<ul></ul>").addClass("pagination");
    // 构建元素
    var firstPageLi = $("<li></li>").append(
        $("<a></a>").append("首页").attr("href", "#"));
    var prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
    if (result.extend.list.hasPreviousPage == false) {
        firstPageLi.addClass("disabled");
        prePageLi.addClass("disabled");
    } else {
        // 为元素添加点击翻页的事件
        firstPageLi.click(function() {
            to_page(1);
        });
        prePageLi.click(function() {
            to_page(result.extend.list.pageNum - 1);
        });
    }
    var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
    var lastPageLi = $("<li></li>").append(
        $("<a></a>").append("末页").attr("href", "#"));
    if (result.extend.list.hasNextPage == false) {
        nextPageLi.addClass("disabled");
        lastPageLi.addClass("disabled");
    } else {
        nextPageLi.click(function() {
            to_page(result.extend.list.pageNum + 1);
        });
        lastPageLi.click(function() {
            to_page(result.extend.list.pages);
        });
    }
    // 添加首页和前一页 的提示
    ul.append(firstPageLi).append(prePageLi);
    // 1,2，3遍历给ul中添加页码提示
    $.each(result.extend.list.navigatepageNums, function(index, item) {

        var numLi = $("<li></li>").append($("<a></a>").append(item));
        if (result.extend.list.pageNum == item) {
            numLi.addClass("active");
        }
        numLi.click(function() {
            to_page(item);
        });
        ul.append(numLi);
    });
    // 添加下一页和末页 的提示
    ul.append(nextPageLi).append(lastPageLi);
    // 把ul加入到nav
    var navEle = $("<nav></nav>").append(ul);
    navEle.appendTo("#page_nav_area");
}