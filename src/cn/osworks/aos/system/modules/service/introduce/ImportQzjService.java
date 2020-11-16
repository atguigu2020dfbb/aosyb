package cn.osworks.aos.system.modules.service.introduce;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tablefieldlistMapper;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 导入服务
 * 
 * @author Sun
 *
 * @date 2018-8-3
 */
@Service
public class ImportQzjService {
	
	@Autowired
	private SqlDao sysDao;
	@Autowired
	private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 *
	 * 读取Excel标题
	 *
	 * @param excelPath
	 * @return
	 */
	public List<Dto> readExcelTitleXlsx(String excelPath){
		List<Dto> titleDtos = new ArrayList<Dto>();
		try {
			InputStream is = new FileInputStream(excelPath);
			XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
			XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 处理当前页，循环读取每一行
			XSSFRow titleRow = hssfSheet.getRow(0);
			int minColIx = titleRow.getFirstCellNum();
			int maxColIx = titleRow.getLastCellNum();
			// 遍历改行，获取处理每个cell元素

			for (int colIx = minColIx; colIx < maxColIx; colIx++) {
				// HSSFCell 表示单元格
				XSSFCell cellTitle = titleRow.getCell(colIx);
				Dto dto = Dtos.newDto();
				dto.put("fieldenname", "fieldenname"+colIx);
				dto.put("fieldcnname", getStringValXlsx(cellTitle));
				titleDtos.add(dto);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return titleDtos;
	}
	/**
	 * 改造poi默认的toString（）方法如下
	 * @Title: getStringVal
	 * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
	 *               2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
	 * @param @param cell
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static String getStringValXlsx(XSSFCell cell) {
		if(cell==null){
			return "";
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case Cell.CELL_TYPE_NUMERIC:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			default:
				return "";
		}
	}
	/**
	 *
	 * 读取Excel标题
	 *
	 * @param excelPath
	 * @return
	 */
	public List<Dto> readExcelTitleXls(String excelPath){
		List<Dto> titleDtos = new ArrayList<Dto>();
		try {
			InputStream is = new FileInputStream(excelPath);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 处理当前页，循环读取每一行
			HSSFRow titleRow = hssfSheet.getRow(0);
			int minColIx = titleRow.getFirstCellNum();
			int maxColIx = titleRow.getLastCellNum();
			// 遍历改行，获取处理每个cell元素

			for (int colIx = minColIx; colIx < maxColIx; colIx++) {
				// HSSFCell 表示单元格
				HSSFCell cellTitle = titleRow.getCell(colIx);
				Dto dto = Dtos.newDto();
				dto.put("fieldenname", "fieldenname"+colIx);
				dto.put("fieldcnname", getStringValXls(cellTitle));
				titleDtos.add(dto);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return titleDtos;
	}
	
	
	/**
	 * 
	 * 读取excel内容
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
    public List<Dto> readXls(String path){
		List<Dto> result = new ArrayList<Dto>();
		try {
			InputStream is = new FileInputStream(path);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			int size = hssfWorkbook.getNumberOfSheets();
			// 循环每一页，并处理当前循环页
			for (int numSheet = 0; numSheet < size; numSheet++) {
				// HSSFSheet 标识某一页
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 处理当前页，循环读取每一行
				for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					// HSSFRow表示行
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					// HSSFRow titleRow = hssfSheet.getRow(0);
					int minColIx = hssfRow.getFirstCellNum();
					int maxColIx = hssfRow.getLastCellNum();
					//List<String> rowList = new ArrayList<String>();
					Dto dto = Dtos.newDto();
					// 遍历改行，获取处理每个cell元素
					for (int colIx = minColIx; colIx < maxColIx; colIx++) {
						// HSSFCell 表示单元格
						HSSFCell cell = hssfRow.getCell(colIx);
						// HSSFCell titleCell = titleRow.getCell(colIx);
						//dto.set
						dto.put("aos_rn_", rowNum);
						dto.put("fieldenname"+colIx, getStringValXls(cell));

						if (cell == null) {
							continue;
						}
						// rowList.add(getStringVal(cell));
					}
					result.add(dto);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// HSSFWorkbook 标识整个excel
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
    }
	/**
	 *
	 * 读取excel内容
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public List<Dto> readXlsx(String path){
		List<Dto> result = new ArrayList<Dto>();
		try {
			InputStream is = new FileInputStream(path);
			XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
			int size = hssfWorkbook.getNumberOfSheets();
			// 循环每一页，并处理当前循环页
			for (int numSheet = 0; numSheet < size; numSheet++) {
				// HSSFSheet 标识某一页
				XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 处理当前页，循环读取每一行
				for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					// HSSFRow表示行
					XSSFRow hssfRow = hssfSheet.getRow(rowNum);
					// HSSFRow titleRow = hssfSheet.getRow(0);
					int minColIx = hssfRow.getFirstCellNum();
					int maxColIx = hssfRow.getLastCellNum();
					//List<String> rowList = new ArrayList<String>();
					Dto dto = Dtos.newDto();
					// 遍历改行，获取处理每个cell元素
					for (int colIx = minColIx; colIx < maxColIx; colIx++) {
						// HSSFCell 表示单元格
						XSSFCell cell = hssfRow.getCell(colIx);
						// HSSFCell titleCell = titleRow.getCell(colIx);
						//dto.set
						dto.put("aos_rn_", rowNum);
						dto.put("fieldenname"+colIx, getStringValXlsx(cell));

						if (cell == null) {
							continue;
						}
						// rowList.add(getStringVal(cell));
					}
					result.add(dto);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// HSSFWorkbook 标识整个excel
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 改造poi默认的toString（）方法如下
	 * @Title: getStringVal
	 * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
	 *               2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
	 * @param @param cell
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static String getStringValXls(HSSFCell cell) {
		if(cell==null){
			return "";
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case Cell.CELL_TYPE_NUMERIC:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			default:
				return "";
		}
	}
	/**
	 *
	 * 拼接数据源
	 *
	 * @param excelpath
	 * @param qDto
	 * @return
	 */
	public List<Dto> getTargetField(String excelpath,Dto qDto){
		List<Dto>  listtitle=new ArrayList<Dto>();
		if(excelpath.endsWith(".xls")){
			listtitle=readExcelTitleXls(excelpath);
		}else	if(excelpath.endsWith(".xlsx")){
			listtitle=readExcelTitleXlsx(excelpath);
		}
		List<Dto> temlist = new ArrayList<Dto>();
		//List<Archive_tablefieldlistPO> list=archive_tablefieldlistMapper.getTableFields(qDto.getString("tablename"));
		List<Archive_tablefieldlistPO> list =listComboBox2(qDto.getString("tablename"));


		for(Dto inDto :listtitle){
			Dto strDto = Dtos.newDto();
			String strtagetFieldname="";
			String strfieldenname="";
			for(int i=0;i<list.size();i++){
				if(list.get(i).getFieldcnname().equals(inDto.getString("fieldcnname"))){
					strtagetFieldname=list.get(i).getFieldcnname();
					strfieldenname=list.get(i).getFieldenname();
				}
			}
			strDto.put("sourcefieldname", inDto.getString("fieldcnname"));
			strDto.put("targetfieldname", strtagetFieldname);
			strDto.put("fieldenname", strfieldenname);
			temlist.add(strDto);
		}
		return temlist;
	}
    
    public List<Archive_tablefieldlistPO> listComboBox(String tablename){
		List<Archive_tablefieldlistPO> list = new ArrayList<Archive_tablefieldlistPO>();
		Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO.setFieldenname("jh");
		archive_tablefieldlistPO.setFieldcnname("件号");
		list.add(archive_tablefieldlistPO);
		Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO1.setFieldenname("zrz");
		archive_tablefieldlistPO1.setFieldcnname("责任者");
		list.add(archive_tablefieldlistPO1);
		Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO2.setFieldenname("tm");
		archive_tablefieldlistPO2.setFieldcnname("题名");
		list.add(archive_tablefieldlistPO2);
		Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO3.setFieldenname("lb");
		archive_tablefieldlistPO3.setFieldcnname("类别");
		list.add(archive_tablefieldlistPO3);
		Archive_tablefieldlistPO archive_tablefieldlistPO4 = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO4.setFieldenname("rq");
		archive_tablefieldlistPO4.setFieldcnname("日期");
		list.add(archive_tablefieldlistPO4);
		Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO5.setFieldenname("bz");
		archive_tablefieldlistPO5.setFieldcnname("备注");
		list.add(archive_tablefieldlistPO5);
		return list;
    }
	public List<Archive_tablefieldlistPO> listComboBox2(String tablename){
		List<Archive_tablefieldlistPO> list = new ArrayList<Archive_tablefieldlistPO>();
		Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO.setFieldenname("qzh");
		archive_tablefieldlistPO.setFieldcnname("全宗号");
		list.add(archive_tablefieldlistPO);
		Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO1.setFieldenname("qzdw");
		archive_tablefieldlistPO1.setFieldcnname("全宗单位");
		list.add(archive_tablefieldlistPO1);
		Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO2.setFieldenname("qzlb");
		archive_tablefieldlistPO2.setFieldcnname("类别");
		list.add(archive_tablefieldlistPO2);
		Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
		archive_tablefieldlistPO3.setFieldenname("bz");
		archive_tablefieldlistPO3.setFieldcnname("备注");
		list.add(archive_tablefieldlistPO3);
		return list;
	}

	public Dto InsertDb (Dto qDto){
		Dto outDto = Dtos.newDto();
		outDto.setAppCode(1);
		List<Dto> list = qDto.getRows();
		List<Dto> listHeader=AOSJson.fromJson(qDto.getString("aos_row1_"));
		String tid=qDto.getString("tid");
		//此时list集合中存放的就是Excel的行数据
		//listHeader存放的就是一一对应的字段列表
		try {
			int count=0;
			//循环行插入数据库
			for(int rowIdx=0;rowIdx<list.size();rowIdx++){
				String columns="id_";
				String values="'"+AOSId.uuid()+"'";
				//循环列拼接字符串
				for(int colIdx=0;colIdx<listHeader.size();colIdx++){
					if(listHeader.get(colIdx).getString("fieldenname").length()!=0){
						values=values+",'"+list.get(rowIdx).getString("fieldenname"+colIdx)+"'";
						columns=columns+","+listHeader.get(colIdx).getString("fieldenname");
					}else
						continue;
				}
				//添加_classtree
					//此时把classtree添加到里面
					columns=columns+",tid";
					values=values+",'"+tid+"'";
				String sql = " INSERT INTO " + qDto.getString("tablename") + " ("
						+ columns+" ) VALUES ("+values+")";
				jdbcTemplate.execute(sql);
				count++;
			}
			String msg = "操作完成，";
			if(count>0){
				msg = AOSUtils.merge(msg + "成功导入[{0}]个", count);
			}
			outDto.setAppMsg(msg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.setAppMsg(e.getMessage());
			outDto.setAppCode(-1);
		}
		return outDto;
	}

	/**
	 * 全宗单位导入Excel
	 * @param qDto
	 * @return
	 */
	public Dto InsertDb_qzdw (Dto qDto){
		Dto outDto = Dtos.newDto();
		outDto.setAppCode(1);
		List<Dto> list = qDto.getRows();
		List<Dto> listHeader=AOSJson.fromJson(qDto.getString("aos_row1_"));
		String tid=qDto.getString("tid");
		//此时list集合中存放的就是Excel的行数据
		//listHeader存放的就是一一对应的字段列表
		try {
			int count=0;
			//循环行插入数据库
			for(int rowIdx=0;rowIdx<list.size();rowIdx++){
				String columns="id_";
				String values="'"+AOSId.uuid()+"'";
				//循环列拼接字符串
				for(int colIdx=0;colIdx<listHeader.size();colIdx++){
					if(listHeader.get(colIdx).getString("fieldenname").length()!=0){
						values=values+",'"+list.get(rowIdx).getString("fieldenname"+colIdx)+"'";
						columns=columns+","+listHeader.get(colIdx).getString("fieldenname");
					}else
						continue;
				}
				String sql = " INSERT INTO " + qDto.getString("tablename") + " ("
						+ columns+" ) VALUES ("+values+")";
				jdbcTemplate.execute(sql);
				count++;
			}
			String msg = "操作完成，";
			if(count>0){
				msg = AOSUtils.merge(msg + "成功导入[{0}]个", count);
			}
			outDto.setAppMsg(msg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outDto.setAppMsg(e.getMessage());
			outDto.setAppCode(-1);
		}
		return outDto;
	}
	/**
     * 改造poi默认的toString（）方法如下
    * @Title: getStringVal 
    * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
    *               2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
    * @param @param cell
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String getStringVal(HSSFCell cell) {
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        case Cell.CELL_TYPE_NUMERIC:
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue();
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();
        default:
            return "";
        }
    }
	/*
	 * 查询表名
	 */
	public List<Archive_tablenamePO> findZLKTablename() {
		// TODO Auto-generated method stub
		List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
		String sql = "select * from aos_sys_dic where  dic_index_id_ in( select id_ from aos_sys_dic_index where key_='zlk')";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String tablename = (String) map.get("code_");
				String tabledesc = (String) map.get("desc_");
				Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
				archive_tablenamePO.setTablename(tablename);
				archive_tablenamePO.setTabledesc(tabledesc);
				alist.add(archive_tablenamePO);
			}
		}
		return alist;
	}
}
