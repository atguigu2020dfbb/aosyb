package cn.osworks.aos.system.modules.controller.chulinprint;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;

/**
 * 打印的函数类，供PrintJson类调用
 * 实际使用时，此类需要移植至应用项目中去
 *
 * @author Administrator
 *
 */
public class PrintFunction {

	public PrintFunction() {
	}

	/**
	 * 把ArrayList类转化为XML字符串
	 * @param DtTable
	 * @return
	 */
	public  static  <T> String ListToXml(ArrayList<T> dtTable) {
		StringBuffer XmlStr = new StringBuffer();
		try {
			XmlStr.append("<xml xmlns:s=\"u\" xmlns:dt=\"u\" xmlns:rs=\"u\" xmlns:z=\"#R\">  ");
			XmlStr.append("<s:Schema id=\"RowsetSchema\"> ");
			XmlStr.append("<s:ElementType name=\"row\" content=\"eltOnly\" rs:updatable=\"true\"> ");

			//导出结构
			Field[] fields = dtTable.get(0).getClass().getFields();
			for( int i = 0; i < fields.length; i++ ) {
				Field field = fields[i];
				String strDataType = field.getType().getName();
				String strName = field.getName();

				if (strDataType == "java.lang.String" || strDataType == "java.util.Date"
						|| strDataType == "double" || strDataType == "float"
						|| strDataType == "int"  || strDataType == "byte"
						|| strDataType == "long" || strDataType == "short"  || strDataType == "boolean"
				) {
					XmlStr.append("<s:AttributeType name=\"" + strName + "\" rs:number=\""
							+ String.valueOf(i+1)
							+ "\" rs:nullable=\"true\" rs:maydefer=\"true\" rs:writeunknown=\"true\" "
							+ "   rs:basecolumn=\"" + strName + "\"> ");

					if (strDataType == "java.lang.String") {
						XmlStr.append("<s:datatype dt:type=\"string\" ");
						XmlStr.append(" dt:maxLength=\"8000\" /> ");
					} else if (strDataType == "java.util.Date") {
						XmlStr.append("<s:datatype dt:type=\"datetime\" ");
						XmlStr.append(" dt:maxLength=\"16\" /> ");
					} else if (strDataType == "double") {
						XmlStr.append("<s:datatype dt:type=\"float\" ");
						XmlStr.append(" dt:maxLength=\"8\" /> ");
					} else if (strDataType == "int" || strDataType == "byte"
							|| strDataType == "long" || strDataType == "short" ) {
						XmlStr.append("<s:datatype dt:type=\"int\" ");
						XmlStr.append(" dt:maxLength=\"4\" /> ");
					} else if (strDataType == "boolean") {
						XmlStr.append("<s:datatype dt:type=\"boolean\" ");
						XmlStr.append(" dt:maxLength=\"2\" /> ");
					} else if (strDataType == "float") {
						XmlStr.append("<s:datatype dt:type=\"r4\" ");
						XmlStr.append(" dt:maxLength=\"4\" /> ");
					}
					XmlStr.append("</s:AttributeType> ");
				}
			}
			XmlStr.append("</s:ElementType> ");
			XmlStr.append("</s:Schema> ");

			//导出数据
			XmlStr.append("<rs:data> ");
			for( T pRow : dtTable ) {
				XmlStr.append("<z:row ");
				for( int i = 0; i < fields.length; i++ ) {
					Field field = fields[i];
					String strDataType = field.getType().getName();
					String strName = field.getName();

					if (strDataType == "java.lang.String" || strDataType == "java.util.Date"
							|| strDataType == "double" || strDataType == "float"
							|| strDataType == "int"  || strDataType == "byte"
							|| strDataType == "long" || strDataType == "short"  || strDataType == "boolean"
					) {
						Object objValue  = field.get(pRow);
						if (objValue == null)
							continue;
						String strValue = objValue.toString();

						if (strDataType == "java.util.Date") {
							SimpleDateFormat sdf1= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
							java.util.Date dtValue = sdf1.parse(strValue);
							SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat sdf3= new SimpleDateFormat("HH:mm:ss");
							strValue = sdf2.format(dtValue) + "T" + sdf3.format(dtValue);
						}
						XmlStr.append(strName + "=\"" + strValue + "\" ");
					}
				}
				XmlStr.append(" /> ");
			}
			XmlStr.append("</rs:data> ");
			XmlStr.append("</xml>");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return XmlStr.toString();
	}


	/**
	 * 把数据表转化为XML格式的字符串
	 * @param rs
	 * @return
	 */
	public static String TableToXml(ResultSet rs) {
		StringBuffer XmlStr = new StringBuffer();

		try {
			ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
			XmlStr.append("<xml xmlns:s=\"u\" xmlns:dt=\"u\" xmlns:rs=\"u\" xmlns:z=\"#R\">  ");
			XmlStr.append("<s:Schema id=\"RowsetSchema\"> ");
			XmlStr.append("<s:ElementType name=\"row\" content=\"eltOnly\" rs:updatable=\"true\"> ");
			int FieldCount = rsmd.getColumnCount() + 1;

			for (int Recn = 1; Recn < FieldCount; Recn++) {
				String DataType = rsmd.getColumnClassName(Recn);
				if (DataType == "java.lang.String" || DataType == "java.sql.Timestamp" || DataType == "java.lang.Double"
						|| DataType == "java.lang.Integer" || DataType == "java.lang.Boolean"
						|| DataType == "java.lang.Float") {
					XmlStr.append("<s:AttributeType name=\"" + rsmd.getColumnName(Recn) + "\" rs:number=\""
							+ String.valueOf(Recn)
							+ "\" rs:nullable=\"true\" rs:maydefer=\"true\" rs:writeunknown=\"true\" "
							+ "   rs:basecolumn=\"" + rsmd.getColumnName(Recn) + "\"> ");

					if (DataType == "java.lang.String") {
						XmlStr.append("<s:datatype dt:type=\"string\" ");
						XmlStr.append(" dt:maxLength=\"" + rsmd.getColumnDisplaySize(Recn) + "\" /> ");
					} else if (DataType == "java.sql.Timestamp") {
						XmlStr.append("<s:datatype dt:type=\"datetime\" ");
						XmlStr.append(" dt:maxLength=\"16\" /> ");
					} else if (DataType == "java.lang.Double") {
						XmlStr.append("<s:datatype dt:type=\"float\" ");
						XmlStr.append(" dt:maxLength=\"8\" /> ");
					} else if (DataType == "java.lang.Integer") {
						XmlStr.append("<s:datatype dt:type=\"int\" ");
						XmlStr.append(" dt:maxLength=\"4\" /> ");
					} else if (DataType == "java.lang.Boolean") {
						XmlStr.append("<s:datatype dt:type=\"boolean\" ");
						XmlStr.append(" dt:maxLength=\"2\" /> ");
					} else if (DataType == "java.lang.Float") {
						XmlStr.append("<s:datatype dt:type=\"r4\" ");
						XmlStr.append(" dt:maxLength=\"4\" /> ");
					}

					XmlStr.append("</s:AttributeType> ");
				}
			}
			XmlStr.append("</s:ElementType> ");
			XmlStr.append("</s:Schema> ");

			XmlStr.append("<rs:data> ");
			while (rs.next()) {
				XmlStr.append("<z:row ");
				for (int Recn = 1; Recn < FieldCount; Recn++) {
					String DataType = rsmd.getColumnClassName(Recn);
					if (DataType == "java.lang.String" || DataType == "java.sql.Timestamp"
							|| DataType == "java.lang.Double" || DataType == "java.lang.Integer"
							|| DataType == "java.lang.Boolean" || DataType == "java.lang.Float") {
						String RsValue = rs.getString(Recn);
						if (RsValue == null)
							continue;

						if (DataType == "java.sql.Timestamp") {
							RsValue = RsValue.substring(0, 10) + "T" + RsValue.substring(11, 19);
						}
						XmlStr.append(rsmd.getColumnName(Recn) + "=\"" + RsValue + "\" ");
					}
				}
				XmlStr.append(" /> ");
			}
			XmlStr.append("</rs:data> ");
			XmlStr.append("</xml>");

			return XmlStr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 把文件转化为字符串
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String FileToStr(String fileName) {
		File pFile = new File(fileName);
		if (!pFile.exists() && !pFile.canRead()) {
			return "";
		}

		StringBuffer strFileValue = new StringBuffer();
		int ch = 0;
		int i1, i2;
		char A1, A2;

		InputStream pFileStream;
		try {
			pFileStream = new FileInputStream(pFile);
			while ((ch = pFileStream.read()) != -1) {
				i1 = ch % 16;
				if (i1 > 9)
					i1 = i1 + 55;
				else
					i1 = i1 + 48;
				A1 = (char) i1;

				i2 = ch / 16;
				if (i2 > 9)
					i2 = i2 + 55;
				else
					i2 = i2 + 48;
				A2 = (char) i2;

				strFileValue.append(String.valueOf(A2) + String.valueOf(A1));
			}
			pFileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strFileValue.toString();

	}

	/**
	 * 把字符串转换为Base64
	 * @param strValue
	 * @return
	 */
	public static String encodeBase64(String strValue) {
		byte []result = Base64.encodeBase64(strValue.getBytes());

		return new String(result);
	}
}
