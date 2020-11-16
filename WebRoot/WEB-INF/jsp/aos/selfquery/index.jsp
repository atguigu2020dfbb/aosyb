<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7 ]><html class="ie ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!--><html lang="en"> <!--<![endif]-->
<head>

    <!-- Basic Page Needs
  ================================================== -->
	<meta charset="utf-8">
	<title style="margin-left: 400px">延边州数字档案馆自助查询系统</title>
	<meta name="description" content="Free Responsive Html5 Css3 Templates">
	<meta name="author" content="">

    <!-- Mobile Specific Metas
	================================================== -->
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- CSS
	================================================== -->
  	<link rel="stylesheet" href="../../static/self/css/zerogrid.css">
	<link rel="stylesheet" href="../../static/self/css/style.css">

	<!-- Custom Fonts -->
    <link href="../../static/self/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">


	<link rel="stylesheet" href="../../static/self/css/menu.css">
	<!-- jQuery Core Javascript -->
	<script src="../../static/self/js/jquery.min.js"></script>
	<script src="../../static/self/js/script.js"></script>

	<!-- Owl Stylesheets -->
    <link rel="stylesheet" href="../../static/self/owlcarousel/assets/owl.carousel.min.css">

	<!--[if lt IE 8]>
       <div style=' clear: both; text-align:center; position: relative;'>
         <a href="http://windows.microsoft.com/en-US/internet-explorer/Items/ie/home?ocid=ie6_countdown_bannercode">
           <img src="http://storage.ie6countdown.com/assets/100/images/banners/warning_bar_0000_us.jpg" border="0" height="42" width="820" alt="You are using an outdated browser. For a faster, safer browsing experience, upgrade for free today." />
        </a>
      </div>
    <![endif]-->
    <!--[if lt IE 9]>
		<script src="../../static/self/js/html5.js"></script>
		<script src="../../static/self/js/css3-mediaqueries.js"></script>
	<![endif]-->

	<%
		String context = request.getContextPath();
		System.out.print(context);
	%>
</head>

<body class="home-page">
	<div class="wrap-body" style="background-size:100% 100%;height: 100%" >
		<header class="main-header">
			<div class="zerogrid">
				<div class="row">
						<div class="box-item-detail" style="margin:0 auto; width:1000px;">
							<h2 class="title" style="margin-left: 20px"> 延边州数字档案馆民生档案自助查档系统</h2>
						</div>
				</div>
			</div>
		</header>

		<!--////////////////////////////////////Container-->
		<section id="container" class="zerogrid">
			<div class="wrap-container">
				<!-----------------content-box-2-------------------->
				<section class="content-box box-2">
					<div class="wrap-box"><!--Start Box-->
						<div class="row">
							<div class="col-2-5 box-item">
								<a class="box-item-inner" href="#" onclick="fn_grtpda()">
									<div class="box-item-image gradient-1" style="background-image: url('../../static/self/images/1.jpg')"></div>
									<h3 class="sub-title">工人调配档案</h3>
								</a>
							</div>
							<div class="col-3-5 box-item">
								<a class="box-item-inner" href="single.html" onclick="fn_fzt()">
									<div class="box-item-image gradient-2" style="background-image: url('../../static/self/images/2.jpg')"></div>
									<h3 class="sub-title">复转退军人档案</h3>
								</a>
							</div>
							<div class="col-3-5 box-item">
								<a class="box-item-inner" href="single.html" onclick="fn_swhy()">
									<div class="box-item-image gradient-3" style="background-image: url('../../static/self/images/3.jpg')"></div>
									<h3 class="sub-title">涉外婚姻档案</h3>

								</a>
							</div>
							<div class="col-2-5 box-item">
								<a class="box-item-inner" href="single.html" onclick="fn_xjda()">
									<div class="box-item-image gradient-4" style="background-image: url('../../static/self/images/4.jpg')"></div>
									<h3 class="sub-title">学籍档案</h3>

								</a>
							</div>

						</div>
					</div>
				</section>



		<!--////////////////////////////////////Footer-->
		<footer>
			<div class="zerogrid">
				<div class="wrap-footer">
					<div class="row">
						<div class="col-1-3 col-footer-1">
							<div class="wrap-col">
								<h3 class="widget-title">地址：延吉市建工街明河胡同197号</h3>
							</div>
						</div>

						<div class="col-1-3 col-footer-2">
							<div class="wrap-col">
								<h3 class="widget-title">查阅时间 周一至周五: 9:00-16:30 </h3>
							</div>
						</div>


						<div class="col-1-3 col-footer-3">
							<div class="wrap-col">
								<h3 class="widget-title">电话 0432-2852442</h3>

							</div>
						</div>
					</div>
				</div>

			</div>
		</footer>

			</div>
		</section>

	</div>
	<!-- Owl Carusel JavaScript -->
	<script src="../../static/self/owlcarousel/owl.carousel.js"></script>
	<script>
	$(document).ready(function() {
	  $("#owl-travel").owlCarousel({
		autoplay:true,
		autoplayTimeout:3000,
		loop:true,
		items : 1,
		nav:true,
		navText: ['<i class="fa fa-chevron-left fa-2x"></i>', '<i class="fa fa-chevron-right fa-2x"></i>'],
		pagination:false
	  });
	});

	function fn_grtpda(){
	    window.open("<%=context%>/utilization/data/zz_initData.jhtml?tablename=grtpda");
	}
	function fn_fzt(){
		window.open("<%=context%>/utilization/data/zz_initData.jhtml?tablename=fztjrda");
	}
	function fn_swhy(){
		window.open("<%=context%>/utilization/data/zz_initData.jhtml?tablename=swhyda");
	}
	function fn_xjda(){
		window.open("<%=context%>/utilization/data/zz_initData.jhtml?tablename=xjda");
	}
	</script>
	
</body>
</html>