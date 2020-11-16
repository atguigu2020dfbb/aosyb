package cn.osworks.aos.system.modules.service.depot;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Depot_czMapper;
import cn.osworks.aos.system.dao.mapper.Depot_cz_detailMapper;
import cn.osworks.aos.system.dao.mapper.Depot_cz_logMapper;
import cn.osworks.aos.system.dao.mapper.Depot_pdMapper;
import cn.osworks.aos.system.dao.po.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.security.*;
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
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.*;
import java.util.List;

@Service
public class LocationService extends JdbcDaoSupport {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }

    @Autowired
    private Depot_czMapper depot_czMapper;
    @Autowired
    private Depot_cz_logMapper depot_cz_logMapper;
    @Autowired
    private Depot_pdMapper depot_pdMapper;

    @Autowired
    private Depot_cz_detailMapper depot_cz_detailMapper;

    public boolean addlocation(Dto inDto) {
        try {
            String arr = inDto.getString("arr");
            String archive_storehouse = inDto.getString("archive_storehouse");
            String[] at = arr.split(",");
            if (at.length > 0) {
                for (int i = 0; i < at.length; i++) {
                    if (at[i].split("-").length < 7) {
                        continue;
                    }
                    String cz_storehouse = at[i].split("-")[0];
                    Integer cz_liehao = Integer.valueOf(at[i].split("-")[1]);
                    String cz_zuhao = at[i].split("-")[2];
                    String cz_jiehao = at[i].split("-")[4];
                    String cz_gehao = at[i].split("-")[5];
                    String cz_name = at[i].split("-")[6];
                    Depot_czPO depot_czPO = new Depot_czPO();
                    depot_czPO.setArchive_storehouse(cz_storehouse);
                    depot_czPO.setCz_liehao(cz_liehao);
                    depot_czPO.setCz_zuhao(cz_zuhao);
                    depot_czPO.setCz_jiehao(cz_jiehao);
                    depot_czPO.setCz_gehao(cz_gehao);
                    depot_czPO.setCz_name(cz_name);
                    //depot_czPO.setDh(at[i]);
                    depot_czPO.setId_(AOSId.uuid());

                    String sql = "select * from depot_cz where dh='" + at[i] + "'";
                    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
                    if (list != null && list.size() > 0) {
                        depot_czMapper.updateByKey(depot_czPO);
                    } else {
                        depot_czMapper.insert(depot_czPO);
                    }
                }
            }
            getlocation(inDto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getlocation(Dto outDto) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String archive_storehouse = outDto.getString("archive_storehouse");
            String column_number = outDto.getString("column_number");
            String group_number = outDto.getString("group_number");
            String sql = "select * from depot_cz where cz_liehao='" + column_number + "' and cz_zuhao='" + group_number + "' and archive_storehouse='" + archive_storehouse + "'";
            list = jdbcTemplate.queryForList(sql);
            //保存操作日志
            Depot_cz_logPO depot_cz_logPO = new Depot_cz_logPO();
            AOSUtils.copyProperties(outDto, depot_cz_logPO);
            depot_cz_logPO.setId_(AOSId.uuid());
            depot_cz_logMapper.insert(depot_cz_logPO);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    public List<Map<String, Object>> listelog(Dto dto) {
        String sql = "select * from depot_cz_log";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> listlocation(Dto dto) {
        String sql = "select * from depot_cz where archive_storehouse='"+dto.getString("archive_storehouse")+"' order by cz_liehao,cz_jiehao,cz_gehao ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public boolean updatelocation(Dto dto) {
        try {
            //先查询是否存在了
            String id_ = dto.getString("id_");
            String cz_liehao = dto.getString("cz_liehao");
            String cz_zuhao = dto.getString("cz_zuhao");
            String cz_jiehao = dto.getString("cz_jiehao");
            String cz_gehao = dto.getString("cz_gehao");
            String archive_storehouse = dto.getString("archive_storehouse");
            String sql = "select * from depot_cz where cz_liehao='" + cz_liehao + "' and cz_zuhao='" +
                    "" + cz_zuhao + "' and cz_jiehao='" + cz_jiehao + "' and " +
                    "cz_gehao='" + cz_gehao + "' and archive_storehouse='" + archive_storehouse + "' and id_ not like '" + id_ + "'";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            if (list != null && list.size() > 0) {
                return false;
            }
            Depot_czPO depot_czPO = new Depot_czPO();
            AOSUtils.copyProperties(dto, depot_czPO);
            depot_czMapper.updateByKey(depot_czPO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dellocation(Dto dto) {
        try {
            //先查询是否存在了
            String id_ = dto.getString("id_");
            depot_czMapper.deleteByKey(id_);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 页面初始化
     *
     * @return
     */
    public List<Map<String, Object>> getDataFieldListTitle(Dto dto) {
        String sql = "select dh,archive_storehouse,cz_liehao,cz_zuhao,cz_jiehao,cz_gehao,cz_name from depot_cz";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }


    public Dto saveLocation(Dto inDto) {
        Dto outDto = Dtos.newDto();
        //根据存址编号查询是否存在当前存址编号
        String sql = "select * from depot_cz where cz_bh='" + inDto.getString("cz_bh") + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            outDto.setAppCode(AOSCons.ERROR);
            outDto.setAppMsg("添加失败！！！存址编号存在重复！！！");
            return outDto;
        } else {
            Depot_czPO depot_czPO = new Depot_czPO();
            AOSUtils.copyProperties(inDto, depot_czPO);
            depot_czPO.setId_(AOSId.uuid());
            depot_czMapper.insert(depot_czPO);
            outDto.setAppCode(AOSCons.SUCCESS);
            outDto.setAppMsg("操作完成,添加成功！！！");
            return outDto;
        }
    }

    /**
     * 读取Excel标题
     *
     * @param excelPath
     * @return
     */
    public List<Dto> readExcelTitleXlsx(String excelPath) {
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
                dto.put("fieldenname", "fieldenname" + colIx);
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

    public Dto InsertDb(Dto qDto) {
        Dto outDto = Dtos.newDto();
        outDto.setAppCode(1);
        List<Dto> list = qDto.getRows();
        List<Dto> listHeader = AOSJson.fromJson(qDto.getString("aos_row1_"));
        //此时list集合中存放的就是Excel的行数据
        //listHeader存放的就是一一对应的字段列表
        try {
            int count = 0;
            //循环行插入数据库
            for (int rowIdx = 0; rowIdx < list.size(); rowIdx++) {
                String columns = "id_";
                String values = "'" + AOSId.uuid() + "'";
                //循环列拼接字符串
                for (int colIdx = 0; colIdx < listHeader.size(); colIdx++) {
                    if (listHeader.get(colIdx).getString("fieldenname").length() != 0) {
                        values = values + ",'" + list.get(rowIdx).getString("fieldenname" + colIdx) + "'";
                        columns = columns + "," + listHeader.get(colIdx).getString("fieldenname");
                    } else
                        continue;
                }
                //添加_classtree
                //此时把classtree添加到里面
                String sql = " INSERT INTO " + qDto.getString("tablename") + " ("
                        + columns + ",cz_bh ) VALUES (" + values + ",'" + AOSId.id("库房流水号") + "')";
                jdbcTemplate.execute(sql);
                count++;
            }
            String msg = "操作完成，";
            if (count > 0) {
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

    public List<Archive_tablefieldlistPO> listComboBox() {
        List<Archive_tablefieldlistPO> list = new ArrayList<Archive_tablefieldlistPO>();
        Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
        archive_tablefieldlistPO.setFieldenname("cz_bh");
        archive_tablefieldlistPO.setFieldcnname("存址编号");
        list.add(archive_tablefieldlistPO);
        Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
        archive_tablefieldlistPO1.setFieldenname("cz_liehao");
        archive_tablefieldlistPO1.setFieldcnname("存址列号");
        list.add(archive_tablefieldlistPO1);
        Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
        archive_tablefieldlistPO2.setFieldenname("cz_zuhao");
        archive_tablefieldlistPO2.setFieldcnname("存址组号");
        list.add(archive_tablefieldlistPO2);
        Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
        archive_tablefieldlistPO3.setFieldenname("cz_jiehao");
        archive_tablefieldlistPO3.setFieldcnname("存址节号");
        list.add(archive_tablefieldlistPO3);
        Archive_tablefieldlistPO archive_tablefieldlistPO4 = new Archive_tablefieldlistPO();
        archive_tablefieldlistPO4.setFieldenname("cz_gehao");
        archive_tablefieldlistPO4.setFieldcnname("存址格号");
        list.add(archive_tablefieldlistPO4);
        Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
        archive_tablefieldlistPO5.setFieldenname("archive_storehouse");
        archive_tablefieldlistPO5.setFieldcnname("库房名称");
        list.add(archive_tablefieldlistPO5);
        return list;
    }

    /**
     * 读取Excel标题
     *
     * @param excelPath
     * @return
     */
    public List<Dto> readExcelTitleXls(String excelPath) {
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
                dto.put("fieldenname", "fieldenname" + colIx);
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


    public Dto updateLocation(Dto inDto) {
        Dto outDto = Dtos.newDto();
        Depot_czPO depot_czPO = new Depot_czPO();
        AOSUtils.copyProperties(inDto, depot_czPO);
        depot_czMapper.updateByKey(depot_czPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deleteLocation(Dto inDto) {
        String[] selections = inDto.getRows();
        int rows = 0;
        for (String id_ : selections) {
            depot_czMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }


    public Dto saveDetail(Dto inDto) {
        Dto outDto = Dtos.newDto();
        Depot_cz_detailPO depot_cz_detailPO = new Depot_cz_detailPO();
        AOSUtils.copyProperties(inDto, depot_cz_detailPO);
        depot_cz_detailPO.setId_(AOSId.uuid());
        depot_cz_detailMapper.insert(depot_cz_detailPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updateDetail(Dto inDto) {
        Dto outDto = Dtos.newDto();
        Depot_cz_detailPO depot_cz_detailPO = new Depot_cz_detailPO();
        AOSUtils.copyProperties(inDto, depot_cz_detailPO);
        depot_cz_detailMapper.updateByKey(depot_cz_detailPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deleteDetail(Dto inDto) {
        String[] selections = inDto.getRows();
        String[] selection_tablenames = inDto.getString("aos_rows_tablename_").split(",");
        int rows = 0;
        for (int i = 0; i < selections.length; i++) {
            delete_cz_archive(selections[i], selection_tablenames[i]);
            rows++;
        }
        return rows;
    }

    public int deleteDetail_charts(Dto inDto) {
        try {
            String id_ = inDto.getString("id_");
            String tablename = inDto.getString("tablename");
            delete_cz_archive(id_, tablename);
            return 1;
        } catch (Exception e) {
            return 0;
        }

    }

    private void delete_cz_archive(String id_, String selection_tablename) {
        String sql = "update  " + selection_tablename + " set cz_bh = null where id_='" + id_ + "'";
        jdbcTemplate.execute(sql);
    }

    /**
     * 读取excel内容
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<Dto> readXls(String path) {
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
                        dto.put("fieldenname" + colIx, getStringValXls(cell));

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
     * 拼接数据源
     *
     * @param excelpath
     * @param qDto
     * @return
     */
    public List<Dto> getTargetField(String excelpath, Dto qDto) {
        List<Dto> listtitle = new ArrayList<Dto>();
        if (excelpath.endsWith(".xls")) {
            listtitle = readExcelTitleXls(excelpath);
        } else if (excelpath.endsWith(".xlsx")) {
            listtitle = readExcelTitleXlsx(excelpath);
        }
        List<Dto> temlist = new ArrayList<Dto>();
        List<Archive_tablefieldlistPO> list = listComboBox();
        for (Dto inDto : listtitle) {
            Dto strDto = Dtos.newDto();
            String strtagetFieldname = "";
            String strfieldenname = "";
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getFieldcnname().equals(inDto.getString("fieldcnname"))) {
                    strtagetFieldname = list.get(i).getFieldcnname();
                    strfieldenname = list.get(i).getFieldenname();
                }
            }
            strDto.put("sourcefieldname", inDto.getString("fieldcnname"));
            strDto.put("targetfieldname", strtagetFieldname);
            strDto.put("fieldenname", strfieldenname);
            temlist.add(strDto);
        }
        return temlist;
    }

    /**
     * 改造poi默认的toString（）方法如下
     *
     * @param @param  cell
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getStringVal
     * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
     * 2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
     */
    public static String getStringValXls(HSSFCell cell) {
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
     * 读取excel内容
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<Dto> readXlsx(String path) {
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
                        dto.put("fieldenname" + colIx, getStringValXlsx(cell));

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
     *
     * @param @param  cell
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getStringVal
     * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
     * 2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
     */
    public static String getStringValXlsx(XSSFCell cell) {
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

    public String getKfJson_y(String archive_storehouse) {
        String sql = "select distinct(cz_liehao) as cz_liehao from depot_cz where archive_storehouse='" + archive_storehouse + "' order by cz_liehao";
        String json = "[";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String cz_liehao = (String) list.get(i).get("cz_liehao");
                int tt = 0;
                int qq = 0;
                //根据节号列号查询每一个中存在档案的个数
                for (int y = 1; y <= 6; y++) {
                    for (int t = 1; t <= 4; t++) {
                        String sql4 = "select * from depot_cz_detail where cz_bh in( select cz_bh from depot_cz where cz_mian='A' and  cz_liehao='" + cz_liehao + "' and cz_gehao='" + t + "' and cz_jiehao='" + y + "' and archive_storehouse='" + archive_storehouse + "')";
                        List<Map<String, Object>> list4 = jdbcTemplate.queryForList(sql4);
                        if (list4 != null && list4.size() > 0) {
                            tt = tt + 1;
                        }
                    }
                    for (int t = 1; t <= 4; t++) {
                        String sql4 = "select * from depot_cz_detail where cz_bh in( select cz_bh from depot_cz where cz_mian='B' and cz_liehao='" + cz_liehao + "' and cz_gehao='" + t + "' and cz_jiehao='" + y + "' and archive_storehouse='" + archive_storehouse + "')";
                        List<Map<String, Object>> list4 = jdbcTemplate.queryForList(sql4);
                        if (list4 != null && list4.size() > 0) {
                            qq = qq + 1;
                        }
                    }
                }
                if (i == list.size() - 1) {
                    json += "{data1: " + tt + ", name: '" + cz_liehao + "A'},";
                    json += "{data1: " + qq + ", name: '" + cz_liehao + "B'}";
                } else {
                    json += "{data1: " + tt + ", name: '" + cz_liehao + "A'},";
                    json += "{data1: " + qq + ", name: '" + cz_liehao + "B'},";
                }
            }
        }
        json += "]";
        return json;
    }
    public int[] getLieHao(){
      int[] sum=new int[36];
      for(int i=0;i<36;i++){
          sum[i]=i+1;
      }
      return sum;
    }

    //存址管理
    public String getKfJson(String archive_storehouse) {
        String json = "[";

        for (int j = 1; j < 37; j++) {
            int sum=0;
            List<Archive_tablenamePO> list = findTablename_by();
            Set<String>  set = new HashSet<>();
            for (int i = 0; i < list.size(); i++) {
                String sql = "select distinct cz_bh as zs  from " + list.get(i).getTablename() + " where cz_bh like '" + archive_storehouse + "-" + j + "-%'";
                List<Map<String,Object>> listzs = jdbcTemplate.queryForList(sql);
                //int zs = (Integer) jdbcTemplate.queryForList(sql).get(0).get("zs");
                //sum=sum+zs;
                for(int k=0;k<listzs.size();k++){
                    set.add((String)listzs.get(k).get("zs"));
                }
                sum=set.size();
            }
            if(j==36){
                json += "{data1: " + sum + ", name: '" + j  + "'}";
            }else
                json += "{data1: " + sum + ", name: '" + j  + "'},";
        }
        json += "]";
        return json;
    }

    public String getKfJson1(String archive_storehouse) {
        //查询所有列数
        String sql = "select cz_liehao from depot_cz group by cz_liehao having count(*)>1 order by cz_liehao";
        String json = "[";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        int[] sum=getLieHao();
            for (int i = 0; i < sum.length; i++) {
                Object cz_liehao = sum[i];
                int tt = 0;
                //根据节号列号查询每一个中存在档案的个数
                for (int y = 1; y <= 6; y++) {
                    for (int t = 1; t <= 8; t++) {
                        //查询每个格的存址编号，再由存址编号查询档案个数
                        String sql6 = "select cz_bh from depot_cz where    cz_liehao='" + cz_liehao + "' and cz_gehao='" + y + "' and cz_jiehao='" + t + "' and archive_storehouse='" + archive_storehouse + "'";
                        List<Map<String, Object>> list5 = jdbcTemplate.queryForList(sql6);
                        if (list5 != null && list5.size() > 0) {
                            String cz_bh = (String) list5.get(0).get("cz_bh");
                            tt = tt + getCz_bh_archive(cz_bh);
                        }
                    }
                }
                if (i == sum.length - 1) {
                    json += "{data1: " + tt + ", name: '" + cz_liehao  + "'}";
                } else {
                    json += "{data1: " + tt + ", name: '" + cz_liehao  + "'},";
                }
            }
        json += "]";
        return json;
    }

    public int getCz_bh_archive(String cz_bh) {
        List<Map<String, Object>> list4 = new ArrayList<>();
        List<Archive_tablenamePO> list = findTablename_by();
        int k = 0;
        for (int i = 0; i < list.size(); i++) {
            String sql = "select * from  " + list.get(i).getTablename() + " where cz_bh='" + cz_bh + "'";
            List<Map<String, Object>> list5 = jdbcTemplate.queryForList(sql);
            if (list5 != null && list5.size() > 0) {
                k = 1;
                break;
            }
        }
        return k;
    }

    public Map<String, Object> getgeSum2(String archive_storehouse, String liehao, String mian) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 4; j++) {

                String sql = "select * from depot_cz_detail where cz_bh in( select cz_bh from depot_cz where cz_liehao='" + liehao + "' and cz_mian='" + mian + "' and cz_gehao='" + i + "' and cz_jiehao='" + j + "' and archive_storehouse='" + archive_storehouse + "')";
                List<Map<String, Object>> list4 = jdbcTemplate.queryForList(sql);
                if (list4 != null && list4.size() > 0) {
                    map.put(j + "-" + i, "1");
                } else {
                    map.put(j + "-" + i, "0");
                }
            }
        }
        return map;
    }

    public Map<String, Object> getgeSum(String archive_storehouse, String liehao) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 8; j++) {
                String sql = "select cz_bh from depot_cz where cz_liehao='" + liehao + "'  and cz_gehao='" + i + "' and cz_jiehao='" + j + "' and archive_storehouse='" + archive_storehouse + "'";
                List<Map<String, Object>> list5 = jdbcTemplate.queryForList(sql);
                if (list5 != null && list5.size() > 0) {
                    String cz_bh = (String) list5.get(0).get("cz_bh");
                    int flag = getCz_bh_archive(cz_bh);
                    map.put(j + "-" + i, flag);
                } else {
                    map.put(j + "-" + i, "0");
                }
            }
        }
        return map;
    }

    public List<Map<String, Object>> getlocationGrid(Dto qDto) {
        String cz_bh = qDto.getString("cz_bh");
        String cz_liehao = qDto.getString("cz_liehao");
        String cz_mian = qDto.getString("cz_mian");
        String archive_storehouse = qDto.getString("archive_storehouse");
        String cz_jiehaoandgehao = qDto.getString("cz_jiehaoandgehao");
        String cz_jiehao = "";
        String cz_gehao = "";
        if (cz_jiehaoandgehao != null && cz_jiehaoandgehao.length() > 0 && cz_jiehaoandgehao.contains("-")) {
            cz_jiehao = cz_jiehaoandgehao.split("-")[0];
            cz_gehao = cz_jiehaoandgehao.split("-")[1];
        }
        String sql = "select * from depot_cz_detail where cz_bh in( select cz_bh from depot_cz where  archive_storehouse='" + archive_storehouse + "' and cz_liehao='" + cz_liehao + "' and cz_mian='" + cz_mian + "' and cz_jiehao='" + cz_jiehao + "' and cz_gehao='" + cz_gehao + "')";
        List<Map<String, Object>> list4 = jdbcTemplate.queryForList(sql);
        return list4;
    }

    public List<Map<String, Object>> getlocationGrid3(Dto qDto) {
        String cz_bh = qDto.getString("cz_bh");
        String cz_liehao = qDto.getString("cz_liehao");
        String cz_mian = qDto.getString("cz_mian");
        String archive_storehouse = qDto.getString("archive_storehouse");
        String cz_jiehaoandgehao = qDto.getString("cz_jiehaoandgehao");
        String sql = "select * from depot_cz_detail where cz_bh in( select cz_bh from depot_cz where  archive_storehouse='" + archive_storehouse + "' and cz_bh='" + cz_bh + "')";
        List<Map<String, Object>> list4 = jdbcTemplate.queryForList(sql);
        return list4;
    }

    public List<Map<String, Object>> getlocationGrid2(Dto qDto) {


        String cz_bh = qDto.getString("cz_bh");
        List<Map<String, Object>> list4 = new LinkedList<>();
        List<Archive_tablenamePO> list = findTablename_by();
        for (int i = 0; i < list.size(); i++) {
            String sql = "select * from  " + list.get(i).getTablename() + " where cz_bh='" + cz_bh + "'";
            List<Map<String, Object>> list5 = jdbcTemplate.queryForList(sql);
            if (list5 != null && list5.size() > 0) {
                for (int j = 0; j < list5.size(); j++) {
                    list5.get(j).put("tablename", list.get(i).getTablename());
                    list5.get(j).put("tabledesc", list.get(i).getTabledesc());
                    list4.add(list5.get(j));
                }
            }
        }
        //分页取数据
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        //分多少次处理
        Integer requestCount = list4.size() / page;
        Integer fromIndex = (page - 1)* limit;
        //如果总数少于PAGE_SIZE,为了防止数组越界,toIndex直接使用totalCount即可
        int toIndex = Math.min(list4.size(),limit*page);
        List<Map<String,Object>> subList = list4.subList(fromIndex, toIndex);
        //总数不到一页或者刚好等于一页的时候,只需要处理一次就可以退出for循环了
        if (toIndex == list4.size()) {
            return list4;
        }
        return subList;
    }
    public List<Map<String, Object>> getlocationGrid4(Dto qDto) {


        String cz_bh = qDto.getString("cz_bh");
        List<Map<String, Object>> list4 = new LinkedList<>();
        List<Archive_tablenamePO> list = findTablename_by();
        for (int i = 0; i < list.size(); i++) {
            String sql = "select * from  " + list.get(i).getTablename() + " where cz_bh='" + cz_bh + "'";
            List<Map<String, Object>> list5 = jdbcTemplate.queryForList(sql);
            if (list5 != null && list5.size() > 0) {
                for (int j = 0; j < list5.size(); j++) {
                    list5.get(j).put("tablename", list.get(i).getTablename());
                    list5.get(j).put("tabledesc", list.get(i).getTabledesc());
                    list4.add(list5.get(j));
                }
            }
        }
        return list4;
    }
    public String getCz_bh(Dto qDto) {
        String cz_jiehaoandgehao = qDto.getString("cz_jiehaoandgehao");
        String cz_jiehao = "";
        String cz_gehao = "";
        String cz_bh = "";
        if (cz_jiehaoandgehao != null && cz_jiehaoandgehao.length() > 0 && cz_jiehaoandgehao.contains("-")) {
            cz_jiehao = cz_jiehaoandgehao.split("-")[0];
            cz_gehao = cz_jiehaoandgehao.split("-")[1];
        }
        String sql = "select * from depot_cz where cz_liehao='" + qDto.getString("cz_liehao") + "' and cz_jiehao='" + cz_jiehao + "' and cz_gehao='" + cz_gehao + "' and archive_storehouse='" + qDto.getString("archive_storehouse") + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            cz_bh = (String) list.get(0).get("cz_bh");
        }
        return cz_bh;
    }

    //查询存址标号
    public String findCn_bh(Dto qDto) {
        Dto outDto = Dtos.newDto();
        String cz_bh = "";
        Depot_czPO depot_czPO = new Depot_czPO();
        AOSUtils.copyProperties(qDto, depot_czPO);
        List<Depot_czPO> list4 = depot_czMapper.list(qDto);
        if (list4 != null && list4.size() > 0) {
            cz_bh = list4.get(0).getCz_bh();
        }
        return cz_bh;
    }

    public List<Map<String, Object>> listdeport(Dto qDto) {
        String sql = "select cz_bh,cz_liehao,cz_mian,cz_jiehao,cz_gehao from depot_cz ";
        return jdbcTemplate.queryForList(sql);
    }

    /*
     * 查询表名
     */
    public List<Archive_tablenamePO> findTablename_by() {
        // TODO Auto-generated method stub
        List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
        String sql = "SELECT  *  FROM      archive_TableName WHERE   (TableName NOT LIKE '%_total%') AND (TableName NOT LIKE '%_temporary%') AND   (TableName NOT LIKE '%_receive%') AND (TableName NOT LIKE '%depot_cz%')";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String tablename = (String) map.get("TableName");
                String tabledesc = (String) map.get("TableDesc");
                Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
                archive_tablenamePO.setTablename(tablename);
                archive_tablenamePO.setTabledesc(tabledesc);
                alist.add(archive_tablenamePO);
            }
        }
        return alist;
    }

    /**
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions2(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            return query.toString();
        } else
            return "";

    }

    public List<Map<String, Object>> listArchive(Dto qDto) {
        // TODO Auto-generated method stub
        String tablename = qDto.getString("tablename");
        String query = qDto.getString("query");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));

        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql = "select * from " + tablename + " where 1=1 " + query + " order by id_ offset " + pageStart + " rows fetch next " + limit + " rows only";
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }

    /**
     * 查询记录总数
     *
     * @return
     */
    public int getPageAll_archive(Dto qDto) {
        //String query = queryConditions2(qDto);
        String query = qDto.getString("query");
        String tablename = qDto.getString("tablename");
        //查询年度条件是否存在
        String sql = "SELECT COUNT(*) FROM " + tablename + " where 1=1 " + query;
        return jdbcTemplate.queryForInt(sql);
    }

    public boolean addKf_archive(Dto qDto) {
        try {
            String tablename = qDto.getString("tablename");
            String cz_bh = qDto.getString("cz_bh");
            String idss = qDto.getString("ids");
            String[] ids = idss.split(",");
            for (int i = 0; i < ids.length; i++) {
                //1.添加存址档案
                String sql = "update " + tablename + " set cz_bh='" + cz_bh + "' where id_='" + ids[i] + "'";
                jdbcTemplate.execute(sql);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void print(Dto qDto)throws Exception  {

        /*String KEYSTORE="d://test.p12";
        char[] PASSWORD = "123".toCharArray();//keystory密码
        String SRC="D:\\123\\原始\\05公证书译文.pdf" ;//原始pdf
        String DEST="D:\\123\\转换后\\05公证书译文.pdf" ;//签名完成的pdf
        String chapterPath="D:\\123\\公司.gif";//签章图片
        String signername="測試";
        String reason="数据不可更改";
        String location="桃源乡";
        sign(new FileInputStream(SRC), new FileOutputStream(DEST),
                new FileInputStream(KEYSTORE), PASSWORD,
                reason, location, chapterPath);*/
        //Demo("D:\\123\\转换后\\05公证书译文.pdf");
        imageWatermark("D:\\123\\原始\\05公证书译文.pdf","D:\\123\\转换后\\05公证书译文.pdf","D:\\123\\公司.gif","1-2-3-4");
    }
public void Demo(String path)throws Exception{
    Document document = new Document();
    PdfWriter writer = null;
    try {
        writer = PdfWriter.getInstance(document, new
//pdf输出地址
                FileOutputStream(path));
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        BarcodeQRCode barcodeQRCode = new BarcodeQRCode("13251745975", 1000, 1000, null);
        Image codeQrImage = barcodeQRCode.getImage();
        codeQrImage.scaleAbsolute(100, 100);

        document.add(codeQrImage);

        document.close();

        //Graphics g = codeQrImage.createGraphics();
        //g.drawImage(pdfImg, 0, 0, Color.WHITE, null);
        //g.dispose();
    } catch (DocumentException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}
    /**
     * 测试添加2个pdf图片水印
     * @param inputPath
     * @param outputPath
     * @param images
     * @param images2
     * @return
     * @throws IOException
     */
    public  boolean imageWatermark(String inputPath, String outputPath,String images,String dh) throws IOException {

        try{
            Document document = new Document();
            PdfWriter writer =  PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfReader reader = new PdfReader(inputPath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPath));
            PdfGState gs1 = new PdfGState();
            //gs1.setFillOpacity(0.1f);
            Image image = Image.getInstance(images);
            int n = reader.getNumberOfPages();
            PdfContentByte under;
            for (int i = 1; i <= n; i++) {
                PdfContentByte pdfContentByte = stamper.getOverContent(i);
                // 获得PDF最顶层
                under = stamper.getOverContent(i);
                pdfContentByte.setGState(gs1);
                //得到pdf的长和高
                float width=reader.getPageSize(i).getWidth();
                float height= reader.getPageSize(i).getHeight();
                PdfContentByte content = null;
                //此时判断一下当前的pdf是不是最后一页，并添加数字水印
                if(n==1){
                    //此时pdf只有一页，添加二维码并添加数字签章
                    //二维码水印
                    addtwodimensional(pdfContentByte,height,dh);
                    //document.close();
                    //图片水印
                    addimagewatermark(width,height,image,pdfContentByte);
                    //文字水印
                    //addTextAlign(stamper,content,i, width, height, image);
                    break;
                }
                if(i==1){
                    //是在第一页添加二维码
                    addtwodimensional(pdfContentByte,height,dh);
                    //document.close();
                }
                if(i==n){
                    //图片水印
                    addimagewatermark(width,height,image,pdfContentByte);
                    //文字水印
                    //addTextAlign(stamper,content,i, width, height, image);
                }
            }
            stamper.close();
            reader.close();
            //
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 图片水印
     * @param width
     * @param height
     * @param image
     * @param pdfContentByte
     * @throws Exception
     */
    private void addimagewatermark(float width, float height, Image image, PdfContentByte pdfContentByte)throws Exception {
        image.scaleToFit(width/5,height/5);
        image.setRotation(45);
        image.setAbsolutePosition(width*7/10, height*1/10); // set the first background image of the absolute
        pdfContentByte.addImage(image);
    }

    /**
     * 二维码水印
     */
    private void addtwodimensional(PdfContentByte pdfContentByte,float height,String dh) throws Exception {
        Hashtable hints= new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode(dh, 500, 500, hints);
        Image codeQrImage = barcodeQRCode.getImage();
        //image.scaleToFit(width/5,height/5);
        codeQrImage.setRotation(45);
        codeQrImage.setAbsolutePosition(10, height-500);
        pdfContentByte.addImage(codeQrImage);
    }

    /**
     * 文字水印
     */
    public void addTextAlign(PdfStamper stamper,PdfContentByte content,int i,float width,float height,Image image) throws Exception {
        // 设置字体
        BaseFont font = BaseFont.createFont();
        // 水印的起始
        content = stamper.getUnderContent(i);
        // 开始
        content.beginText();
        // 设置颜色 默认为蓝色
        content.setColorFill(BaseColor.RED);
        // content.setColorFill(Color.GRAY);
        // 设置字体及字号
        content.setFontAndSize(font, 2);
        // 设置起始位置
        content.setTextMatrix(width/5,height/5);
        // 开始写入水印
        content.showTextAligned(Element.ALIGN_CENTER, "123456789", width*7/10, height*1/10, 45);

        content.endText();
    }

    public  void sign(InputStream src  //需要签章的pdf文件路径
            , OutputStream dest  // 签完章的pdf文件路径
            , InputStream p12Stream, //p12 路径
                            char[] password
            , String reason  //签名的原因，显示在pdf签名属性中，随便填
            , String location,String chapterPath) //签名的地点，显示在pdf签名属性中，随便填
            throws GeneralSecurityException, IOException, DocumentException {
        //读取keystore ，获得私钥和证书链
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(p12Stream, password);
        String alias = (String)ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
        Certificate[] chain = ks.getCertificateChain(alias);

        //下边的步骤都是固定的，照着写就行了，没啥要解释的
        // Creating the reader and the stamper，开始pdfreader
        PdfReader reader = new PdfReader(src);
        //目标文件输出流
        //创建签章工具PdfStamper ，最后一个boolean参数
        //false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
        //true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
        PdfStamper stamper = PdfStamper.createSignature(reader, dest, '\0', null, false);
        // 获取数字签章属性对象，设定数字签章的属性
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);
        //设置签名的位置，页码，签名域名称，多次追加签名的时候，签名预名称不能一样
        //签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
        //四个参数的分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y
        appearance.setVisibleSignature(new Rectangle(0, 800, 100, 700), 1, "sig1");
        //读取图章图片，这个image是itext包的image
        Image image = Image.getInstance(chapterPath);
        appearance.setSignatureGraphic(image);
        appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
        //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

        // 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
        // 摘要算法
        ExternalDigest digest = new BouncyCastleDigest();
        // 签名算法
        ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA1, null);
        // 调用itext签名方法完成pdf签章CryptoStandard.CMS 签名方式，建议采用这种
        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
    }

    public List<Map<String,Object>>   getQueryIds(Dto qDto) {
        // TODO Auto-generated method stub
        String ids="";
        String tablename=qDto.getString("tablename");
        String query=qDto.getString("query");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql="select id_ from "+tablename+" where 1=1 "+query;
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);

        return listDto;
    }
}