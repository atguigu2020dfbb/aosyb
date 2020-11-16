<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String basePath = request.getScheme()
			+ "://" + request.getServerName()
			+ ":"
			+ request.getServerPort()
			+ context
			+ "/";
	System.out.print(basePath);
%>
<head>

    <!-- Basic Page Needs
  ================================================== -->
	<meta charset="utf-8">
	<title>延边州数字档案馆自助查询系统</title>
	<meta name="description" content="Free Responsive Html5 Css3 Templates">
	<meta name="author" content="">
	
    <!-- Mobile Specific Metas
	================================================== -->
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<style>
		/* reset */
		html,body,div,span,applet,object,iframe,h1,h2,h3,h4,h5,h6,p,blockquote,pre,a,abbr,acronym,address,big,cite,code,del,dfn,em,img,ins,kbd,q,s,samp,small,strike,strong,sub,sup,tt,var,b,u,i,dl,dt,dd,ol,nav ul,nav li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td,article,aside,canvas,details,embed,figure,figcaption,footer,header,hgroup,menu,nav,output,ruby,section,summary,time,mark,audio,video{margin:0;padding:0;border:0;font-size:100%;font:inherit;vertical-align:baseline;}
		article, aside, details, figcaption, figure,footer, header, hgroup, menu, nav, section {display: block;}
		ol,ul{list-style:none;margin:0px;padding:0px;}
		blockquote,q{quotes:none;}
		blockquote:before,blockquote:after,q:before,q:after{content:'';content:none;}
		table{border-collapse:collapse;border-spacing:0;}
		/* start editing from here */
		a{text-decoration:none;}
		.txt-rt{text-align:right;}/* text align right */
		.txt-lt{text-align:left;}/* text align left */
		.txt-center{text-align:center;}/* text align center */
		.float-rt{float:right;}/* float right */
		.float-lt{float:left;}/* float left */
		.clear{clear:both;}/* clear float */
		.pos-relative{position:relative;}/* Position Relative */
		.pos-absolute{position:absolute;}/* Position Absolute */
		.vertical-base{	vertical-align:baseline;}/* vertical align baseline */
		.vertical-top{	vertical-align:top;}/* vertical align top */
		nav.vertical ul li{	display:block;}/* vertical menu */
		nav.horizontal ul li{	display: inline-block;}/* horizontal menu */
		img{max-width:100%;}
		/*end reset*/
		body{
			font-family: 'Cabin', sans-serif !important;
			background:url(http://192.168.0.3/aosyb/static/image/banner.jpg) 0px 0px;
		}
		/*-- main --*/
		.main{
			width: 35%;
			margin: 8em auto 0;
			padding: 0em 4em 5em;
		}
		.main h1{
			color: #7e8fad;
			text-align: center;
			font-size: 2.5em;
			margin: 0 0 1.5em;
		}
		.main input[type="text"],.main input[type="password"]{
			outline: none;
			border: 1px solid #0f0f0f;
			background: #f9f2f4;
			border-radius: 5px;
			-webkit-border-radius: 5px;
			-moz-border-radius: 5px;
			-o-border-radius: 5px;
			-ms-border-radius: 5px;
			padding: 15px 10px;
			width: 96%;
			font-size: 1.3em;
			color: #0f0f0f;
			text-align: center;
			font-family: 'Syncopate', sans-serif;
		}
		.main input[type="password"]{
			margin:1.5em 0;
		}
		.main input[type="submit"]{
			outline:none;
			border:1px solid #f2f9fd;
			background:#00a1cb;
			border-radius:5px;
			-webkit-border-radius:5px;
			-moz-border-radius:5px;
			-o-border-radius:5px;
			-ms-border-radius:5px;
			padding:15px 10px;
			width:100%;
			color:#0f0f0f;
			font-size:1.5em;
			font-family: 'Syncopate', sans-serif;
			cursor:pointer;
		}
		.main input[type="submit"]:hover{
			background:#00a1cb;
			color:#0f0f0f;
			border: 1px solid #f2f9fd;
		}
		.footer{
			text-align:center;
			padding:2em 0 0;
		}
		.footer p{
			font-size:1em;
			color:#B47C9F;
			margin:0;
		}
		.footer p a{
			color:#fff;
		}
		.footer p a:hover{
			color:#B47C9F;
		}
		/*-- start-responsive-design --*/
		@media (max-width:1440px){
			.main {
				width: 37%;
			}
			.main input[type="text"], .main input[type="password"] {
				width: 95.8%;
			}
		}
		@media (max-width: 1280px){
			.main {
				width: 40%;
			}
			.main input[type="text"], .main input[type="password"] {
				width: 95.6%;
			}
		}
		@media (max-width: 1024px){
			.main {
				width: 50%;
				margin: 5em auto 0;
			}
		}
		@media (max-width:768px){
			.main {
				width: 60%;
			}
			.main input[type="text"], .main input[type="password"] {
				width: 95.2%;
				font-size: 1.1em;
			}
		}
		@media (max-width:736px){
			.main {
				padding: 0em 4em 2em;
			}
			.footer {
				padding: 2em 0;
			}
		}
		@media (max-width:667px){
			.main {
				width: 65%;
				margin: 3em auto 0;
			}
			.main h1 {
				font-size: 2.3em;
				margin: 0 0 1.2em;
			}
			.main input[type="text"], .main input[type="password"] {
				width: 94.5%;
			}
		}
		@media (max-width:640px){
			.main input[type="submit"] {
				font-size: 1.3em;
			}
		}
		@media (max-width: 600px){
			.main input[type="text"], .main input[type="password"] {
				width: 94.2%;
			}
		}
		@media (max-width:480px){
			.main h1 {
				font-size: 2em;
				margin: 0 0 1em;
			}
			.main {
				padding: 0em 0em 2em;
				width: 85%;
			}
		}
		@media (max-width: 414px){
			.main input[type="text"], .main input[type="password"] {
				width: 93.5%;
			}
			.footer p {
				width: 80%;
				margin: 0 auto;
				line-height: 1.8em;
			}
		}
		@media (max-width:320px){
			.main {
				padding: 0;
				width: 90%;
				margin: 2em auto 0;
			}
			.main h1 {
				font-size: 1.7em;
			}
			.main input[type="text"], .main input[type="password"] {
				padding: 10px;
				font-size: 13px;
				width: 92.5%;
			}
			.main input[type="submit"] {
				font-size: 1em;
				padding: 10px;
			}
			.footer p {
				width: 90%;
				font-size: 15px;
			}
		}
	</style>

</head>

<body class="home-page">
<!-- main -->
<div class="main">
	<h1>
		自助查档系统
	</h1>
	<form>

		<div><label style="float:left;color: #d9331a;font-size: 20px">用户名:</label><input type="text" id="user" style="display:inline-block"  placeholder="例如:张三" onFocus="this.value = '';" onBlur="if (this.value == '') {this.value = '请输入用户名';}"
			   required=""></div>
		<div><label style="float:left;color: #d9331a;font-size: 20px">密  码:</label><input type="text" id="mima" style="display:inline-block" placeholder="密码默认111111" onFocus="this.value = '';" onBlur="if (this.value == '') {this.value = '密码默认111111';}"
			   required=""></div>
		<div><input type="submit" style="margin-top: 20px" value="登录" onclick="fn_kaifang()"></div>
	</form>
</div>
<div class="footer">
</div>

	<script>
	function fn_kaifang(){
		var user=document.getElementById("user").value;
		var mima=document.getElementById("mima").value;
		if(user!=null&&user!=''&&mima!=null&&mima!=''){
			window.open("<%=context%>/utilization/data/zizhu_initData.jhtml?user="+user+"&mima="+mima);
		}else{
			alert("请输入用户名和密码!");
		}

	}
	</script>
	
</body>
</html>