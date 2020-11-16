package cn.osworks.aos.system.modules.controller.chulinprint;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 此类供检测Cookie的JSP文件调用
 * 实际使用时，可参照此类进行调用
 * @author Administrator
 *
 */
public class PrintCookieDemo {
	/**
	 * 判断数据表中是否存在，若不存在则新建一条记录，若存在则更新最后访问的时间
	 * @param strCookie
	 */
	private void  UpdateCheckInstall(String strCookie) {
		SqlHelper pSqlHelper = new SqlHelper();
		String strSql = "Select * From CheckInstall Where Cookie = '" + strCookie + "' ";
		boolean bIsExist = pSqlHelper.CheckIsExist(strSql);
		if (bIsExist) { //存在则更新最后访问的时间
			strSql = "Update CheckInstall  Set LastAccessTime = getDate() Where Cookie = '" + strCookie + "' ";
		} else { //不存在则新建一条记录
			strSql = "Insert into CheckInstall(Cookie, LastAccessTime)  Values( '" + strCookie + "' , getDate() )";
		}
		pSqlHelper.Execute(strSql);
		pSqlHelper.Close();
	}

	/**
	 * Ajax检查控件是否安装了，防止那种客户把打印控件卸装了，打印时无法检测到的情况
	 * 判断控件的最后访问时间，若超过了60秒钟，则表示打印控件已经卸装了
	 * @param strCookie
	 * @return
	 */
	private boolean AjaxCheckInstall(String strCookie) {
		SqlHelper pSqlHelper = new SqlHelper();
		String strSql = "Select * From CheckInstall Where Cookie = '" + strCookie + "' ";
		Date dtLastAccessTime = pSqlHelper.GetOneDateTime(strSql, "LastAccessTime");
		pSqlHelper.Close();

		Date dtNow = new Date(System.currentTimeMillis() - 60*1000);
		if( dtLastAccessTime.getTime() < dtNow.getTime() ) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * 回调检查打印控件的Cookie值
	 * @param request
	 */
	public void CheckPrintCookie(HttpServletRequest request, HttpServletResponse response ) {
		String strCurPath = request.getSession().getServletContext().getRealPath("/");
		if( request.getParameter("cookie") != null ) {
			//有控件上传的Cookie值
			String strCookie = request.getParameter("cookie");
			WriteLog(strCurPath, "上传的cookie：" + strCookie);

			//判断数据表中是否存在，若不存在则新建一条记录，若存在则更新最后访问的时间
			UpdateCheckInstall(strCookie);
		} else if( request.getParameter("checkInstall") != null ) {
			//Ajax检查控件是否安装了，防止那种客户把打印控件卸装了，打印时无法检测到的情况
			String strCookie = request.getParameter("checkInstall");
			//读取最后访问的时间
			String strResult = "PrintControlInstall";
			if (!AjaxCheckInstall(strCookie))
			{
				strResult = "NotInstall";
			}

			PrintWriter out;
			try {
				out = response.getWriter();
				out.write(strResult);
			} catch (IOException e) {
				e.printStackTrace();
			}

			WriteLog(strCurPath, "检测安装的值：" + strCookie + "；结果：" + strResult );
		}

	}

	/**
	 * 写入日志文件
	 * @param strPath
	 * @param strParam
	 */
	public void WriteLog(String strPath, String strParam ) {
		if( strParam == null ) {
			strParam = "";
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strLogFile = "Log.txt";
		String fileName = strPath + strLogFile;
		String strValue = sdf.format(date) + " 访问，" + strParam + "\r\n";

		File fileLog = new File(fileName);
		try {
			if ( !fileLog.exists()) {
				fileLog.createNewFile();
			}
			RandomAccessFile pAccessFile = new RandomAccessFile(fileLog, "rw");
			pAccessFile.seek(pAccessFile.length());
			byte[] fileBuf = strValue.getBytes("UTF-8");
			pAccessFile.write(fileBuf, 0, fileBuf.length);
			pAccessFile.close();
		} catch( Exception e ) {
			;
		}
		fileLog = null;

	}
}
