package cn.osworks.aos.system.modules.controller.chulinprint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * 此类供打印控件在线编辑时POST方式上传保存报表模板文件的JSP文件调用
 * 实际使用时，可参照此类进行调用
 * @author Administrator
 *
 */
public class PostSaveDemo {
	/**
	 * 上传POST方式保存的文件
	 * @param request
	 * @param response
	 */
	public void PostSaveReportFile(HttpServletRequest request, HttpServletResponse response ) {
        String strFileName = request.getParameter("FileName"); //GET参数中所带的文件名称
        String strFileValue = request.getParameter("ReportFileValue"); //POST方式上传的文件内容
        
        WriteReportFile(request, strFileName, strFileValue);
     }

     private void WriteReportFile(HttpServletRequest request, String strFileName, String strFileValue) {
         if (strFileName == null || strFileName == "" || strFileValue == null || strFileValue == "")
             return;

 		String strCurPath = request.getSession().getServletContext().getRealPath("/"); 
 		String fileName = strCurPath + "Frp/" + strFileName;
 		byte[] fileBuf = hexStringToBytes(strFileValue);
 		
		File fileReport = new File(fileName);
 		if ( fileReport.exists()) {
 			fileReport.delete(); //文件存在则先删除
  		}		
 		try {
 			fileReport.createNewFile();
 		    RandomAccessFile pAccessFile = new RandomAccessFile(fileReport, "rw");
 		    pAccessFile.write(fileBuf, 0, fileBuf.length);
 		    pAccessFile.close();
 		} catch( Exception e ) {
 			;
 		}
 		fileReport = null;
     }
     
     /** 
      * Convert hex string to byte[] 
      * @param hexString the hex string 
      * @return byte[] 
      */  
     public  byte[] hexStringToBytes(String hexString) {  
         if (hexString == null || hexString.equals("")) {  
             return null;  
         }  
         hexString = hexString.toUpperCase();  
         int length = hexString.length() / 2;  
         char[] hexChars = hexString.toCharArray();  
         byte[] d = new byte[length];  
         for (int i = 0; i < length; i++) {  
             int pos = i * 2;  
             d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
         }  
         return d;  
     } 
     
     /** 
      * Convert char to byte 
      * @param c char 
      * @return byte 
      */  
      private byte charToByte(char c) {  
         return (byte) "0123456789ABCDEF".indexOf(c);  
     }    

}
