package cn.osworks.aos.web.asset;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import cn.osworks.aos.system.dao.po.Aos_log_sessionPO;
import cn.osworks.aos.system.modules.service.log.LogService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * <b>请求过滤器</b>
 * 
 * @author OSWorks-XC
 */
public class HttpRequestFilter implements Filter{
	
	private Log log = LogFactory.getLog(HttpRequestFilter.class);
	private Boolean enabled; //过滤器开关
	private String[] excludes; //忽略过滤配置


	/**
	 * 初始化
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String enabledCfg = filterConfig.getInitParameter("enabled");
		enabledCfg = AOSUtils.isEmpty(enabledCfg) ? "true" : enabledCfg;
		enabled = BooleanUtils.toBoolean(enabledCfg);
		String excludesCfg = filterConfig.getInitParameter("excludes");
		excludes = StringUtils.split(excludesCfg, ",");
	}

	/**
	 * 过滤
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestUri = httpRequest.getRequestURI();
		if (log.isInfoEnabled()) {
			log.info(AOSCons.CONSOLE_FLAG3 + "HTTP请求: " + requestUri + " >> 参数列表: " + WebCxt.getParamAsDto(httpRequest));
		}
		if (!enabled) {
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		if (isExclude(requestUri)) {
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		String ctxPath = httpRequest.getContextPath();
		//因为特殊情况 做下判断，民生档案的页面即使没登录也放行
		if(requestUri.contains("/aosyb/utilization/data/zz_initIndex.jhtml")||"/aosyb/archive/data/zz_initIndex.jhtml".equals(requestUri)||requestUri.contains("openGpy.jhtml")||requestUri.contains("uploadExcel")
				||requestUri.contains("loadExcelGrid")||requestUri.contains("zizhu_initData")||requestUri.contains("zizhu_initIndex")
		){

		}else{
			UserInfoVO userInfoVO = WebCxt.getUserInfo(httpRequest.getSession());
			if (AOSUtils.isEmpty(userInfoVO)) {
				httpResponse.getWriter().write("<script type=\"text/javascript\">parent.location.href='" + ctxPath
						+ "/login.jhtml'</script>");
				httpResponse.getWriter().flush();
				httpResponse.getWriter().close();
				return;
			}
		}
		chain.doFilter(httpRequest, httpResponse);
	}

	
	/**
	 * 检查URI例外
	 * 
	 * @param uri 待检查的URI
	 * @return true: 例外
	 */
	private boolean isExclude(String uri){
		boolean out = false;
		for (String exclude : excludes) {
			if (StringUtils.indexOfIgnoreCase(uri, exclude) != -1) {
				out = true;
				break;
			}
		}
		return out;
	}

	/**
	 * 释放资源
	 */
	@Override
	public void destroy() {
		enabled = null;
		excludes = null;
	}

}
