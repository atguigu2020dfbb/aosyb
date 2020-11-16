package cn.osworks.aos.system.modules.service.depot;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.captcha.utils.jdbc.JDBCUtils;
import cn.osworks.aos.core.exception.AOSException;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.controller.Task.WsdTask;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DepotOutService extends JdbcDaoSupport {
    @Autowired
    private Archive_tablenameMapper archive_tablenameMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Depot_ckMapper depot_ckMapper;
    @Autowired
    private Depot_rkMapper depot_rkMapper;
    @Autowired
    private Depot_hjMapper depot_hjMapper;
    @Autowired
    private Depot_aqMapper depot_aqMapper;
    @Autowired
    private Depot_ryMapper depot_ryMapper;
    @Autowired
    private Depot_sbMapper depot_sbMapper;
    @Autowired
    private Depot_pdMapper depot_pdMapper;
    @Autowired
    private WsdMapper wsdMapper;
    @Autowired
    private CrkMapper crkMapper;
    @Autowired
    private LogMapper logMapper;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }

    @Autowired
    private Depot_bgMapper depot_bgMapper;
    public Dto save(Dto inDto){
        Dto outDto = Dtos.newDto();

        Depot_ckPO depotCkPO= new Depot_ckPO();
        AOSUtils.copyProperties(inDto,depotCkPO);
        depotCkPO.setId_(AOSId.uuid());
        depot_ckMapper.insert(depotCkPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，添加成功！！！");
        return outDto;
    }

    public void select(Dto dto){
        String id_ = dto.getString("id_");
        Depot_ckPO depotCkPO=depot_ckMapper.selectByKey(id_);
    }

    public Dto update(Dto inDto){
        Dto outDto=Dtos.newDto();
        Depot_ckPO depotCkPO = new Depot_ckPO();
        AOSUtils.copyProperties(inDto,depotCkPO);
        depot_ckMapper.updateByKey(depotCkPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public Dto saveDepotIn(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_rkPO depot_rkPO=new Depot_rkPO();
        AOSUtils.copyProperties(inDto,depot_rkPO);
        depot_rkPO.setId_(AOSId.uuid());
        depot_rkMapper.insert(depot_rkPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，添加成功！！！");
        return  outDto;
    }

    public Dto updateDepotIn(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_rkPO depot_rkPO=new Depot_rkPO();
        AOSUtils.copyProperties(inDto,depot_rkPO);
        depot_rkMapper.updateByKey(depot_rkPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
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
        List<Archive_tablefieldlistPO> list = listComboBox(qDto.getString("tablename"));
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
    public List<Archive_tablefieldlistPO> listComboBox(String tablename) {
        List<Archive_tablefieldlistPO> list = new ArrayList<Archive_tablefieldlistPO>();
        if("WSD".equals(tablename)){
            Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO1.setFieldenname("timexp");
            archive_tablefieldlistPO1.setFieldcnname("监测日期");
            list.add(archive_tablefieldlistPO1);
            Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO2.setFieldenname("depotid");
            archive_tablefieldlistPO2.setFieldcnname("depotid");
            list.add(archive_tablefieldlistPO2);
            Archive_tablefieldlistPO archive_tablefieldlistPO4 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO4.setFieldenname("cgqname");
            archive_tablefieldlistPO4.setFieldcnname("cgqname");
            list.add(archive_tablefieldlistPO4);
            Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO5.setFieldenname("wd");
            archive_tablefieldlistPO5.setFieldcnname("库房温度");
            list.add(archive_tablefieldlistPO5);
            Archive_tablefieldlistPO archive_tablefieldlistPO6 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO6.setFieldenname("sd");
            archive_tablefieldlistPO6.setFieldcnname("库房湿度");
            list.add(archive_tablefieldlistPO6);
            return list;
        }else if("depot_sb".equals(tablename)){
            Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO.setFieldenname("kfbh");
            archive_tablefieldlistPO.setFieldcnname("库房编号");
            list.add(archive_tablefieldlistPO);
            Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO1.setFieldenname("jcsj");
            archive_tablefieldlistPO1.setFieldcnname("检查时间");
            list.add(archive_tablefieldlistPO1);
            Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO2.setFieldenname("jcbm");
            archive_tablefieldlistPO2.setFieldcnname("检查部门");
            list.add(archive_tablefieldlistPO2);
            Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO3.setFieldenname("jcr");
            archive_tablefieldlistPO3.setFieldcnname("检查人");
            list.add(archive_tablefieldlistPO3);
            Archive_tablefieldlistPO archive_tablefieldlistPO4 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO4.setFieldenname("jclx");
            archive_tablefieldlistPO4.setFieldcnname("检查类型");
            list.add(archive_tablefieldlistPO4);
            Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO5.setFieldenname("sbmc");
            archive_tablefieldlistPO5.setFieldcnname("设备名称");
            list.add(archive_tablefieldlistPO5);
            Archive_tablefieldlistPO archive_tablefieldlistPO6 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO6.setFieldenname("sbxh");
            archive_tablefieldlistPO6.setFieldcnname("设备型号");
            list.add(archive_tablefieldlistPO6);
            Archive_tablefieldlistPO archive_tablefieldlistPO7 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO7.setFieldenname("gzms");
            archive_tablefieldlistPO7.setFieldcnname("故障描述");
            list.add(archive_tablefieldlistPO7);
            Archive_tablefieldlistPO archive_tablefieldlistPO8 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO8.setFieldenname("gzyy");
            archive_tablefieldlistPO8.setFieldcnname("故障原因");
            list.add(archive_tablefieldlistPO8);
            Archive_tablefieldlistPO archive_tablefieldlistPO9 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO9.setFieldenname("gzsfjj");
            archive_tablefieldlistPO9.setFieldcnname("故障是否解决");
            list.add(archive_tablefieldlistPO9);
            Archive_tablefieldlistPO archive_tablefieldlistPO10 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO10.setFieldenname("ywqtwt");
            archive_tablefieldlistPO10.setFieldcnname("有无其他问题");
            list.add(archive_tablefieldlistPO10);
            Archive_tablefieldlistPO archive_tablefieldlistPO11 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO11.setFieldenname("wxbm");
            archive_tablefieldlistPO11.setFieldcnname("维修部门");
            list.add(archive_tablefieldlistPO11);
            Archive_tablefieldlistPO archive_tablefieldlistPO12 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO12.setFieldenname("wxr");
            archive_tablefieldlistPO12.setFieldcnname("维修人");
            list.add(archive_tablefieldlistPO12);
            Archive_tablefieldlistPO archive_tablefieldlistPO13 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO13.setFieldenname("bz1");
            archive_tablefieldlistPO13.setFieldcnname("备注1");
            list.add(archive_tablefieldlistPO13);
            Archive_tablefieldlistPO archive_tablefieldlistPO14 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO14.setFieldenname("bz2");
            archive_tablefieldlistPO14.setFieldcnname("备注2");
            list.add(archive_tablefieldlistPO14);

            return list;
        }else if("depot_ry".equals(tablename)){
            Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO.setFieldenname("kfbh");
            archive_tablefieldlistPO.setFieldcnname("库房编号");
            list.add(archive_tablefieldlistPO);
            Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO1.setFieldenname("ryxm");
            archive_tablefieldlistPO1.setFieldcnname("人员姓名");
            list.add(archive_tablefieldlistPO1);
            Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO2.setFieldenname("rkrs");
            archive_tablefieldlistPO2.setFieldcnname("入库人数");
            list.add(archive_tablefieldlistPO2);
            Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO3.setFieldenname("rksj");
            archive_tablefieldlistPO3.setFieldcnname("入库时间");
            list.add(archive_tablefieldlistPO3);
            Archive_tablefieldlistPO archive_tablefieldlistPO4 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO4.setFieldenname("cksj");
            archive_tablefieldlistPO4.setFieldcnname("出库时间");
            list.add(archive_tablefieldlistPO4);
            Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO5.setFieldenname("ywyc");
            archive_tablefieldlistPO5.setFieldcnname("有无异常");
            list.add(archive_tablefieldlistPO5);
            Archive_tablefieldlistPO archive_tablefieldlistPO6 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO6.setFieldenname("sbxh");
            archive_tablefieldlistPO6.setFieldcnname("设备型号");
            list.add(archive_tablefieldlistPO6);
            Archive_tablefieldlistPO archive_tablefieldlistPO7 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO7.setFieldenname("rkmd");
            archive_tablefieldlistPO7.setFieldcnname("入库目的");
            list.add(archive_tablefieldlistPO7);
            Archive_tablefieldlistPO archive_tablefieldlistPO8 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO8.setFieldenname("rkxq");
            archive_tablefieldlistPO8.setFieldcnname("入库详情");
            list.add(archive_tablefieldlistPO8);
            Archive_tablefieldlistPO archive_tablefieldlistPO9 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO9.setFieldenname("bz");
            archive_tablefieldlistPO9.setFieldcnname("备注");
            list.add(archive_tablefieldlistPO9);
            return list;
        }else if("depot_aq".equals(tablename)){
            Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO.setFieldenname("kfbh");
            archive_tablefieldlistPO.setFieldcnname("库房编号");
            list.add(archive_tablefieldlistPO);
            Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO1.setFieldenname("jcsj");
            archive_tablefieldlistPO1.setFieldcnname("检查时间");
            list.add(archive_tablefieldlistPO1);
            Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO2.setFieldenname("jcbm");
            archive_tablefieldlistPO2.setFieldcnname("检查部门");
            list.add(archive_tablefieldlistPO2);
            Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO3.setFieldenname("jcr");
            archive_tablefieldlistPO3.setFieldcnname("检查人");
            list.add(archive_tablefieldlistPO3);
            Archive_tablefieldlistPO archive_tablefieldlistPO4 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO4.setFieldenname("jclx");
            archive_tablefieldlistPO4.setFieldcnname("检查类型");
            list.add(archive_tablefieldlistPO4);
            Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO5.setFieldenname("dysfgb");
            archive_tablefieldlistPO5.setFieldcnname("电源是否关闭");
            list.add(archive_tablefieldlistPO5);
            Archive_tablefieldlistPO archive_tablefieldlistPO6 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO6.setFieldenname("kcsfgb");
            archive_tablefieldlistPO6.setFieldcnname("口窗是否关闭");
            list.add(archive_tablefieldlistPO6);
            Archive_tablefieldlistPO archive_tablefieldlistPO7 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO7.setFieldenname("nqsfls");
            archive_tablefieldlistPO7.setFieldcnname("暖气是否漏水");
            list.add(archive_tablefieldlistPO7);
            Archive_tablefieldlistPO archive_tablefieldlistPO8 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO8.setFieldenname("sbsfzcsy");
            archive_tablefieldlistPO8.setFieldcnname("设备是否正常");
            list.add(archive_tablefieldlistPO8);
            Archive_tablefieldlistPO archive_tablefieldlistPO9 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO9.setFieldenname("ktsfzc");
            archive_tablefieldlistPO9.setFieldcnname("空调是否正常");
            list.add(archive_tablefieldlistPO9);
            Archive_tablefieldlistPO archive_tablefieldlistPO10 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO10.setFieldenname("ytjsfzc");
            archive_tablefieldlistPO10.setFieldcnname("一体机是否正常");
            list.add(archive_tablefieldlistPO10);
            Archive_tablefieldlistPO archive_tablefieldlistPO11 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO11.setFieldenname("mjsfzc");
            archive_tablefieldlistPO11.setFieldcnname("门禁是否正常");
            list.add(archive_tablefieldlistPO11);
            Archive_tablefieldlistPO archive_tablefieldlistPO12 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO12.setFieldenname("jksfzc");
            archive_tablefieldlistPO12.setFieldcnname("监控是否正常");
            list.add(archive_tablefieldlistPO12);
            Archive_tablefieldlistPO archive_tablefieldlistPO13 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO13.setFieldenname("ywqtwt");
            archive_tablefieldlistPO13.setFieldcnname("有无其他问题");
            list.add(archive_tablefieldlistPO13);
            Archive_tablefieldlistPO archive_tablefieldlistPO14 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO14.setFieldenname("bz");
            archive_tablefieldlistPO14.setFieldcnname("备注");
            list.add(archive_tablefieldlistPO14);
            return list;
        }else if("depot_bg".equals(tablename)){
            Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO.setFieldenname("kfbh");
            archive_tablefieldlistPO.setFieldcnname("库房编号");
            list.add(archive_tablefieldlistPO);
            Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO1.setFieldenname("jcsj");
            archive_tablefieldlistPO1.setFieldcnname("检查时间");
            list.add(archive_tablefieldlistPO1);
            Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO2.setFieldenname("jcbm");
            archive_tablefieldlistPO2.setFieldcnname("检查部门");
            list.add(archive_tablefieldlistPO2);
            Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO3.setFieldenname("jcr");
            archive_tablefieldlistPO3.setFieldcnname("检查人");
            list.add(archive_tablefieldlistPO3);
            Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO5.setFieldenname("daml");
            archive_tablefieldlistPO5.setFieldcnname("档案门类");
            list.add(archive_tablefieldlistPO5);
            Archive_tablefieldlistPO archive_tablefieldlistPO6 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO6.setFieldenname("dh");
            archive_tablefieldlistPO6.setFieldcnname("档号");
            list.add(archive_tablefieldlistPO6);
            Archive_tablefieldlistPO archive_tablefieldlistPO7 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO7.setFieldenname("tm");
            archive_tablefieldlistPO7.setFieldcnname("题名");
            list.add(archive_tablefieldlistPO7);
            Archive_tablefieldlistPO archive_tablefieldlistPO8 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO8.setFieldenname("xcrq");
            archive_tablefieldlistPO8.setFieldcnname("形成日期");
            list.add(archive_tablefieldlistPO8);
            Archive_tablefieldlistPO archive_tablefieldlistPO9 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO9.setFieldenname("bgqx");
            archive_tablefieldlistPO9.setFieldcnname("保管期限");
            list.add(archive_tablefieldlistPO9);
            Archive_tablefieldlistPO archive_tablefieldlistPO10 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO10.setFieldenname("zrz");
            archive_tablefieldlistPO10.setFieldcnname("责任者");
            list.add(archive_tablefieldlistPO10);
            Archive_tablefieldlistPO archive_tablefieldlistPO11 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO11.setFieldenname("ys");
            archive_tablefieldlistPO11.setFieldcnname("页数");
            list.add(archive_tablefieldlistPO11);
            Archive_tablefieldlistPO archive_tablefieldlistPO13 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO13.setFieldenname("bgzk");
            archive_tablefieldlistPO13.setFieldcnname("保管状况");
            list.add(archive_tablefieldlistPO13);
            Archive_tablefieldlistPO archive_tablefieldlistPO14 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO14.setFieldenname("wtys");
            archive_tablefieldlistPO14.setFieldcnname("问题页数");
            list.add(archive_tablefieldlistPO14);
            Archive_tablefieldlistPO archive_tablefieldlistPO15 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO15.setFieldenname("wtms");
            archive_tablefieldlistPO15.setFieldcnname("问题描述");
            list.add(archive_tablefieldlistPO15);
            Archive_tablefieldlistPO archive_tablefieldlistPO16 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO16.setFieldenname("sfcl");
            archive_tablefieldlistPO16.setFieldcnname("是否处理");
            list.add(archive_tablefieldlistPO16);
            Archive_tablefieldlistPO archive_tablefieldlistPO17 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO17.setFieldenname("clr");
            archive_tablefieldlistPO17.setFieldcnname("处理人");
            list.add(archive_tablefieldlistPO17);
            Archive_tablefieldlistPO archive_tablefieldlistPO18 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO18.setFieldenname("clsj");
            archive_tablefieldlistPO18.setFieldcnname("处理时间");
            list.add(archive_tablefieldlistPO18);
            Archive_tablefieldlistPO archive_tablefieldlistPO19 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO19.setFieldenname("bz");
            archive_tablefieldlistPO19.setFieldcnname("备注");
            list.add(archive_tablefieldlistPO19);
            return list;
        }else if("depot_ck".equals(tablename)){
            Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO.setFieldenname("ckdh");
            archive_tablefieldlistPO.setFieldcnname("出库单号");
            list.add(archive_tablefieldlistPO);
            Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO1.setFieldenname("ckr");
            archive_tablefieldlistPO1.setFieldcnname("出库人");
            list.add(archive_tablefieldlistPO1);
            Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO2.setFieldenname("ckyy");
            archive_tablefieldlistPO2.setFieldcnname("出库原因");
            list.add(archive_tablefieldlistPO2);
            Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO3.setFieldenname("cksj");
            archive_tablefieldlistPO3.setFieldcnname("出库时间");
            list.add(archive_tablefieldlistPO3);
            Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO5.setFieldenname("cksl");
            archive_tablefieldlistPO5.setFieldcnname("出库数量");
            list.add(archive_tablefieldlistPO5);
            Archive_tablefieldlistPO archive_tablefieldlistPO6 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO6.setFieldenname("nghsj");
            archive_tablefieldlistPO6.setFieldcnname("拟入库时间");
            list.add(archive_tablefieldlistPO6);
            Archive_tablefieldlistPO archive_tablefieldlistPO7 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO7.setFieldenname("ckzt");
            archive_tablefieldlistPO7.setFieldcnname("出库状态");
            list.add(archive_tablefieldlistPO7);
            Archive_tablefieldlistPO archive_tablefieldlistPO8 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO8.setFieldenname("bz");
            archive_tablefieldlistPO8.setFieldcnname("备注");
            list.add(archive_tablefieldlistPO8);
            return list;
        }else if("depot_rk".equals(tablename)){
            Archive_tablefieldlistPO archive_tablefieldlistPO = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO.setFieldenname("rkdh");
            archive_tablefieldlistPO.setFieldcnname("入库单号");
            list.add(archive_tablefieldlistPO);
            Archive_tablefieldlistPO archive_tablefieldlistPO1 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO1.setFieldenname("rkr");
            archive_tablefieldlistPO1.setFieldcnname("入库人");
            list.add(archive_tablefieldlistPO1);
            Archive_tablefieldlistPO archive_tablefieldlistPO2 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO2.setFieldenname("rkyy");
            archive_tablefieldlistPO2.setFieldcnname("入库原因");
            list.add(archive_tablefieldlistPO2);
            Archive_tablefieldlistPO archive_tablefieldlistPO3 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO3.setFieldenname("yckdh");
            archive_tablefieldlistPO3.setFieldcnname("原出库单号");
            list.add(archive_tablefieldlistPO3);
            Archive_tablefieldlistPO archive_tablefieldlistPO5 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO5.setFieldenname("rksj");
            archive_tablefieldlistPO5.setFieldcnname("入库时间");
            list.add(archive_tablefieldlistPO5);
            Archive_tablefieldlistPO archive_tablefieldlistPO6 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO6.setFieldenname("rksl");
            archive_tablefieldlistPO6.setFieldcnname("入库数量");
            list.add(archive_tablefieldlistPO6);
            Archive_tablefieldlistPO archive_tablefieldlistPO7 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO7.setFieldenname("rkzt");
            archive_tablefieldlistPO7.setFieldcnname("入库状态");
            list.add(archive_tablefieldlistPO7);
            Archive_tablefieldlistPO archive_tablefieldlistPO8 = new Archive_tablefieldlistPO();
            archive_tablefieldlistPO8.setFieldenname("bz");
            archive_tablefieldlistPO8.setFieldcnname("备注");
            list.add(archive_tablefieldlistPO8);
            return list;
        }
        else{
           return list;
        }
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
                        + columns + ",lx ) VALUES (" + values + ",'" + qDto.getString("lx") + "')";
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
    public List<Map<String,Object>> listaq(Dto inDto){
        String sql = "select kfbh,jcsj,jcbm,jcr,jclx,dysfgb,nqsfls,sbsfzcsy,ktsfzc,ytjsfzc,mjsfzc,jksfzc,bz  from depot_aq where lx='"+inDto.getString("lx")+"'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> listry(Dto inDto){
        String sql = "select id_,f_recid ,f_eventtype, f_eventdesc,f_userid ,f_username,f_logdatetime  from CRK  ";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> listwsd(Dto inDto){
        String sql = "select depotid,depotname,fzbm,djr,timexp,wd,sd from WSD where lx='"+inDto.getString("lx")+"'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> listsb(Dto inDto){
        String sql = "select kfbh,jcsj,jcbm,jcr,jclx,sbmc,sbxh,gzms,gzyy,gzsfjj,ywqtwt,wxbm,wxr,bz  from depot_sb where lx='"+inDto.getString("lx")+"'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> listbg(Dto inDto){
        String sql = "select kfbh,jcsj,jcbm,jcr,daml,dh,tm,xcrq,bgqx,zrz,ys,bgzk,wtys,wtms,sfcl,clr,clsj,bz  from depot_bg where lx='"+inDto.getString("lx")+"'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> listck(Dto inDto){
        String sql = "select ckdh,ckr,ckyy,cksj,cksl,nghsj,ckzt,bz  from depot_ck where lx='"+inDto.getString("lx")+"'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> listrk(Dto inDto){
        String sql = "select rkdh,rkr,rkyy,yckdh,rksj,rksl,rkzt,bz  from depot_rk where lx='"+inDto.getString("lx")+"'";
        return jdbcTemplate.queryForList(sql);
    }
    public Dto saveEnvironment(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_hjPO depot_hjPO = new Depot_hjPO();
        AOSUtils.copyProperties(inDto,depot_hjPO);
        depot_hjPO.setId_(AOSId.uuid());
        depot_hjMapper.insert(depot_hjPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updateEnvironment(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_hjPO depot_hjPO = new Depot_hjPO();
        AOSUtils.copyProperties(inDto,depot_hjPO);
        depot_hjMapper.updateByKey(depot_hjPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deleteEnvironment(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            depot_hjMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }
    public Dto saveSafe(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_aqPO depot_aqPO = new Depot_aqPO();
        AOSUtils.copyProperties(inDto,depot_aqPO);
        depot_aqPO.setId_(AOSId.uuid());
        depot_aqMapper.insert(depot_aqPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }
    public Dto saveBg(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_bgPO depot_bgPO = new Depot_bgPO();
        AOSUtils.copyProperties(inDto,depot_bgPO);
        depot_bgPO.setId_(AOSId.uuid());
        depot_bgMapper.insert(depot_bgPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updateSafe(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_aqPO depot_aqPO=new Depot_aqPO();
        AOSUtils.copyProperties(inDto,depot_aqPO);
        depot_aqMapper.updateByKey(depot_aqPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }
    public Dto updateBg(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_bgPO depot_bgPO=new Depot_bgPO();
        AOSUtils.copyProperties(inDto,depot_bgPO);
        depot_bgMapper.updateByKey(depot_bgPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deleteSafe(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            depot_aqMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }
    public int deleteBg(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            depot_bgMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }
    public int deleteDepotout(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            depot_ckMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }
    public int deleteDepotin(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            depot_rkMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }
    public Dto savePersonnel(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_ryPO depot_ryPO = new Depot_ryPO();
        AOSUtils.copyProperties(inDto,depot_ryPO);
        depot_ryPO.setId_(AOSId.uuid());
        depot_ryMapper.insert(depot_ryPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updatePersonnel(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_ryPO depot_ryPO = new Depot_ryPO();
        AOSUtils.copyProperties(inDto,depot_ryPO);
        depot_ryMapper.updateByKey(depot_ryPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deletePersonnel(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            depot_ryMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }


    public Dto saveDevice(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_sbPO depot_sbPO =new Depot_sbPO();
        AOSUtils.copyProperties(inDto,depot_sbPO);
        depot_sbPO.setId_(AOSId.uuid());
        depot_sbMapper.insert(depot_sbPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updateDevice(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_sbPO depot_sbPO=new Depot_sbPO();
        AOSUtils.copyProperties(inDto,depot_sbPO);
        depot_sbMapper.updateByKey(depot_sbPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deleteDevice(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            depot_sbMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    public Dto savePd(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_pdPO depot_pdPO = new Depot_pdPO();
        AOSUtils.copyProperties(inDto,depot_pdPO);
        depot_pdPO.setId_(AOSId.uuid());
        depot_pdMapper.insert(depot_pdPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成,添加成功！！！");
        return outDto;
    }

    public Dto updatePd(Dto inDto){
        Dto outDto = Dtos.newDto();
        Depot_pdPO depot_pdPO=new Depot_pdPO();
        AOSUtils.copyProperties(inDto,depot_pdPO);
        depot_pdMapper.updateByKey(depot_pdPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功！！！");
        return outDto;
    }

    public int deletePd(Dto inDto){
        String[] selections = inDto.getRows();
        int rows=0;
        for(String id_ :selections){
            depot_pdMapper.deleteByKey(id_);
            rows++;
        }
        return rows;
    }

    //****************************************************
    public String getDepotOutIndex(String ckdh){
        String dh = depot_ckMapper.getDepotOutIndex(ckdh);
        if (dh != null && dh.length() != 0) {
            String index = dh.substring(dh.length()-3, dh.length());
            int lx = Integer.valueOf(index) + 1;
            return String.format("%3d", lx).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public String getDepotInIndex(String ckdh){
        String dh = depot_rkMapper.getDepotInIndex(ckdh);
        if (dh != null && dh.length() != 0) {
            String index = dh.substring(dh.length()-3, dh.length());
            int lx = Integer.valueOf(index) + 1;
            return String.format("%3d", lx).replace(" ", "0");
        }else {
            return "001";
        }
    }
    //****************************************************
    public String getDepotOutIndex_nd(String lx,String ckdh){
        String nd="";
        if(ckdh.length()==10&&ckdh!=null){
            nd=ckdh.substring(3,ckdh.length()-3);
        }
        String sql="select  ckdh  from depot_ck where ckdh like '%"+lx+"%' and ckdh like '%"+nd+"%' order by ckdh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
           String dh= (String)list.get(0).get("ckdh");
            String index = dh.substring(dh.length()-3, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%3d", h).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public String getDepotPdIndex_nd(String lx,String lsh){
        String nd="";
        if(lsh.length()==10&&lsh!=null){
            nd=lsh.substring(3,lsh.length()-3);
        }
        String sql="select  lsh  from depot_pd where lsh like '%"+lx+"%' and lsh like '%"+nd+"%' order by lsh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("lsh");
            String index = dh.substring(dh.length()-3, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%3d", h).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public String getDepotBgIndex_nd(String lx,String lsh){
        String nd="";
        if(lsh.length()==10&&lsh!=null){
            nd=lsh.substring(3,lsh.length()-3);
        }
        String sql="select  kfbh  from depot_bg where kfbh like '%"+lx+"%' and kfbh like '%"+nd+"%' order by kfbh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("kfbh");
            String index = dh.substring(dh.length()-3, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%3d", h).replace(" ", "0");
        }else {
            return "001";
        }
    }
    //****************************************************
    public String getDepotInIndex_nd(String lx,String rkdh){
        String nd="";
        if(rkdh.length()==10&&rkdh!=null){
            nd=rkdh.substring(3,rkdh.length()-3);
        }
        String sql="select  rkdh  from depot_rk where rkdh like '%"+lx+"%' and rkdh like '%"+nd+"%' order by rkdh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("rkdh");
            String index = dh.substring(dh.length()-3, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%3d", h).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public String getSafeIndex(String ckdh){
        String dh = depot_sbMapper.getSafeIndex(ckdh);
        if (dh != null && dh.length() != 0) {
            String index = dh.substring(dh.length()-3, dh.length());
            int lx = Integer.valueOf(index) + 1;
            return String.format("%3d", lx).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public String getDeviceIndex(String ckdh){
        String dh = depot_sbMapper.getDeviceIndex(ckdh);
        if (dh != null && dh.length() != 0) {
            String index = dh.substring(dh.length()-3, dh.length());
            int lx = Integer.valueOf(index) + 1;
            return String.format("%3d", lx).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public String getDepotPdIndex(String ckdh){
        String dh = depot_pdMapper.getDepotPdIndex(ckdh);
        if (dh != null && dh.length() != 0) {
            String index = dh.substring(dh.length()-3, dh.length());
            int lx = Integer.valueOf(index) + 1;
            return String.format("%3d", lx).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public String getDepotBgIndex(String ckdh){
        String dh = depot_bgMapper.getDepotBgIndex(ckdh);
        if (dh != null && dh.length() != 0) {
            String index = dh.substring(dh.length()-3, dh.length());
            int lx = Integer.valueOf(index) + 1;
            return String.format("%3d", lx).replace(" ", "0");
        }else {
            return "001";
        }
    }

    public String getPersonIndex(String ckdh){
        String dh = depot_ryMapper.getPersonIndex(ckdh);
        if (dh != null && dh.length() != 0) {
            String index = dh.substring(dh.length()-3, dh.length());
            int lx = Integer.valueOf(index) + 1;
            return String.format("%3d", lx).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public String getDepotAqIndex(String ckdh){
        String dh = depot_aqMapper.getDepotAqIndex(ckdh);
        if (dh != null && dh.length() != 0) {
            String index = dh.substring(dh.length()-3, dh.length());
            int lx = Integer.valueOf(index) + 1;
            return String.format("%3d", lx).replace(" ", "0");
        }else {
            return "001";
        }
    }
    //****************************************************
    public String getDepotHjIndex_nd(String lx,String ckdh){
        String nd="";
        if(ckdh.length()==10&&ckdh!=null){
            nd=ckdh.substring(3,ckdh.length()-3);
        }
        String sql="select  lsh  from depot_hj where lsh like '%"+lx+"%' and lsh like '%"+nd+"%' order by lsh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("lsh");
            String index = dh.substring(dh.length()-3, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%3d", h).replace(" ", "0");
        }else {
            return "001";
        }
    }
    //****************************************************
    public String getDepotAqIndex_nd(String lx,String ckdh){
        String nd="";
        if(ckdh.length()==10&&ckdh!=null){
            nd=ckdh.substring(3,ckdh.length()-3);
        }
        String sql="select  lsh  from depot_aq where lsh like '%"+lx+"%' and lsh like '%"+nd+"%' order by lsh desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() >0) {
            String dh= (String)list.get(0).get("lsh");
            String index = dh.substring(dh.length()-3, dh.length());
            int h = Integer.valueOf(index) + 1;
            return String.format("%3d", h).replace(" ", "0");
        }else {
            return "001";
        }
    }
    public Dto updateEnvironmentAll_lx(Dto inDto){
        Dto outDto = Dtos.newDto();
        try{
            String depotname=inDto.getString("depotname");
            String sql="update depot_hj set jcr_cn='"+inDto.getUserInfo().getName_()+"' , jcr_en='"+inDto.getUserInfo().getAccount_()+"' where DepotName='"+depotname+"'";
            jdbcTemplate.execute(sql);
            outDto.setAppCode(AOSCons.SUCCESS);
        }catch(Exception e){
            e.printStackTrace();
            outDto.setAppCode(AOSCons.ERROR);
        }
        return outDto;
    }


    public Dto get_addEnvironment(Dto inDto) {
        WsdTask wsdTask = new WsdTask();
        String depotname = inDto.getString("depotname");
        Dto dto = executeInternal(inDto);
        return dto;
    }

    public Dto executeInternal(Dto inDto) {
        // TODO Auto-generated method stub
        String depotname=inDto.getString("depotname");
        String depotid="";
        if("301".equals(depotname)){
            depotid="1";
        }else if("302".equals(depotname)){
            depotid="2";
        }else if("401".equals(depotname)){
            depotid="3";
        }else if("402".equals(depotname)){
            depotid="4";
        }else if("501".equals(depotname)){
            depotid="5";
        }else if("708".equals(depotname)){
            depotid="6";
        }else if("502".equals(depotname)){
            depotid="7";
        }
        //获取当前时间，并拆分
        Dto dto = Dtos.newDto();
//        String date = DateFormat.getDateTimeInstance().format(new Date());
        String date=AOSUtils.getDateTimeStr();
        String year = date.substring(0,date.indexOf("-"));
        String month = date.substring(date.indexOf("-")+1,date.lastIndexOf("-"));
        if (month.length() ==1 ){
            month = "0" + month;
        }
        String day = date.substring(date.lastIndexOf("-")+1,date.lastIndexOf(" "));
        if (day.length() ==1 ){
            day = "0" + day;
        }
        String hour = date.substring(date.lastIndexOf(" ")+1,date.indexOf(":"));
        if (Integer.parseInt(hour)<=10 ){
            hour = "0" + (Integer.parseInt(hour)-1);
        }else{
            hour= String.valueOf((Integer.parseInt(hour)-1));
        }
        String last = date.substring(date.indexOf(":"),date.length());

        //String date_WSD = year +"/" + month+"/"+day + " " + hour+last;
        String date_WSDL =year +"/" + month+"/"+day + " "+hour+last;
        String date_WSD =year +"/" + month+"/"+day + " " + (Integer.parseInt(hour)-1)+last;
        String WSD_TableName = "W" + year + month;
        System.out.println("时间:"+date_WSDL);
        Connection conn = JDBCUtils.getMainConnection();
        Connection connWSD = JDBCUtils.getWSDConnection();
        //预编译
        PreparedStatement ps = null;
        String sql0;
        PreparedStatement psWSD = null;
        ResultSet resWSD = null;
        String sql2;
        try {
            sql0 =  "insert into depot_hj  " +
                    "(id_,DepotName,jcrq,wd,sd,jcr_cn) " +
                    "values (?,?,?,?,?,?) ";
            sql2 =  "SELECT timeXP , DepotID , CgqName , wd , sd  " +
                    " FROM  "+ WSD_TableName +
                    " WHERE timeXP < ? and timeXP > ? and DepotID = ? ";
            psWSD = connWSD.prepareStatement(sql2);
//            psWSD.setString(1,WSD_TableName);
            psWSD.setString(1,date_WSDL);
            psWSD.setString(2,date_WSD);
            psWSD.setString(3,depotid);
            resWSD = psWSD.executeQuery();
            // 展开结果集数据库
            while (resWSD.next()){
                // 通过字段检索
                String id_ = AOSId.uuid();
                String DepotName="";
                String timeXP = resWSD.getString("timeXP");
                String DepotID = resWSD.getString("DepotID");
                String CgqName = resWSD.getString("CgqName");
                String wd = resWSD.getString("wd");
                String sd = resWSD.getString("sd");
                if("1".equals(DepotID)){
                    DepotName="301";
                }else if("2".equals(DepotID)){
                    DepotName="302";
                }else if("3".equals(DepotID)){
                    DepotName="401";
                }else if("4".equals(DepotID)){
                    DepotName="402";
                }else if("5".equals(DepotID)){
                    DepotName="501";
                }else if("6".equals(DepotID)){
                    DepotName="708";
                }else if("7".equals(DepotID)){
                    DepotName="502";
                }
                ps = conn.prepareStatement(sql0);
                ps.setString(1,id_);
                ps.setString(2,DepotName);
                ps.setString(3,timeXP);
                ps.setString(4,wd);
                ps.setString(5,sd);
                ps.setString(6,inDto.getUserInfo().getName_());

                int temp  = ps.executeUpdate();
                if (temp > 0 ){
                    System.out.println("添加成功");
                }else {
                    System.out.println("添加失败");
                }
            }
            dto.setAppCode(AOSCons.SUCCESS);
        } catch (SQLException e) {
            e.printStackTrace();
            dto.setAppCode(AOSCons.ERROR);
        }finally {
            //关闭连接
            JDBCUtils.close(conn);
            JDBCUtils.close(ps);
            JDBCUtils.close(resWSD);
            JDBCUtils.close(connWSD);
            JDBCUtils.close(psWSD);
        }
        return dto;
    }

    public void updateDepotOut_time(Archive_remotePO archive_remotePO) {
        try {
            String ckdh=archive_remotePO.getCkdh();
            String sql="update depot_ck set nghsj='"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"' where ckdh='"+ckdh+"'";
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void addDepotIn(Map<String,Object> map,Dto dto) {
        if(map!=null&&map.size()>0){
            String yckdh=(String)map.get("ckdh");
            String rkr=dto.getUserInfo().getName_();
            String rkyy=(String)map.get("ckyy");
            String rksj=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String rksl=(String)map.get("cksl");
            String rkzt="已入库";
            String bz=(String)map.get("bz");
            Depot_rkPO depot_rkPO=new Depot_rkPO();
            depot_rkPO.setId_(AOSId.uuid());
            String index = getDepotInIndex(dto.getString("lx"));
            depot_rkPO.setRkdh(dto.getString("lx")+new Date().getYear()+index);
            depot_rkPO.setRkr(rkr);
            depot_rkPO.setRkyy(rkyy);
            depot_rkPO.setYckdh(yckdh);
            depot_rkPO.setRksj(rksj);
            depot_rkPO.setRksl(rksl);
            depot_rkPO.setLx(dto.getString("lx"));
            depot_rkPO.setBz(bz);
            depot_rkPO.setRkzt("已入库");
            depot_rkMapper.insert(depot_rkPO);
        }
    }

    /**
     * 结合出库单号得到出库信息
     * @param ckdh
     * @return
     */
    public Map<String, Object> getDepotOut(String ckdh) {
        Map<String, Object> maps=new HashMap<String, Object>();
        String sql="select * from depot_ck where ckdh='"+ckdh+"'";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            maps=list.get(0);
        }
        return maps;
    }

    /**
     *
     * 获得电子文件信息
     *
     * @param qDto
     * @return
     */
    public List<Dto> getPath(Dto qDto) {
        String tablename = qDto.getString("tablename") + "_path";
        String tid = qDto.getString("tid");
        String sql = " SELECT id_,tid,_path,dirname,sdatetime,_s_path ,RIGHT(RTRIM(_Path), CHARINDEX('.',REVERSE(RTRIM(_path))) - 1) as filetype FROM "
                + tablename + " WHERE tid ='" + tid + "'";
        List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
                .queryForList(sql));
        return listDto;
    }
    /**
     * 获得记忆
     * @param inDto
     * @param userInfoVO
     */
    public Map<String,Object> getRemember(Dto inDto, UserInfoVO userInfoVO) {
        String tablename=inDto.getString("tablename");
        String username=userInfoVO.getAccount_();
        String type=inDto.getString("type");
        String sql="select flag,name  from archive_remember where tablename='"+tablename+"' and username='"+username+"' and type='"+type+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        Map<String,Object> map=new HashMap<String,Object>();
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                String flag=(String)list.get(i).get("flag");
                String name=(String)list.get(i).get("name");
                map.put(name,flag);
            }
        }
        return map;
    }
    /**getDataFieldDisplayAll
     * 功能：转换MapList为数组List
     *
     * @param list
     * @return
     */
    public static List convertMapListToArrayList(List list) {
        Map map = null;
        /*
         * Dto qDto = Dtos.newDto(); if (list != null) { Iterator iterator =
         * list.iterator(); while (iterator.hasNext()) { map = (Map)
         * iterator.next(); Iterator<?> keyIt = map.keySet().iterator(); while
         * (keyIt.hasNext()) { String key = (String) keyIt.next(); String value
         * = ((Object) map.get(key)) == null ? "" : ((Object)
         * map.get(key)).toString(); qDto.put(key, value); } } }
         */

        return list;
    }
    /**
     * 下载电子文件
     *
     * @param dto
     * @param request
     * @param response
     * @throws IOException
     */
    public void download_electronic_file(String filePath, Dto dto,
                                         HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        // TODO Auto-generated method stub
        String filename = null;
        String filepath = null;
        String _path = null;
        // 查询文件路径信息
        List<Map<String, Object>> list = find_pathmessage(dto.getString("id_"),
                dto.getString("tablename"));
        if (list != null && list.size() > 0) {
            filename = (String) list.get(0).get("dirname");
            filepath = (String) list.get(0).get("_s_path");
            _path = (String) list.get(0).get("_path");
            response.setContentType("application/x-" + filepath.split("\\.")[1]
                    + ";charset=gb2312");
            // 名称点上后缀
            response.addHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(_path, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(
                        filePath

                                + File.separator + filename + filepath));
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = fileInputStream.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                    outputStream.flush();
                }
            } catch (Exception e) {
                throw new AOSException(e);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ex) {

                    }
                }
            }
        }
    }
    /**
     *
     * 删除单个电子文件
     *
     * @param qDto
     * @return
     */
    public Dto deletePath(Dto qDto) {
        Dto outDto = Dtos.newDto();
        Dto dto = Dtos.newDto();
        dto.put("tablename", qDto.getString("tablename"));
        int del = 1;
        String id = qDto.getString("id_");
        String tablename = qDto.getString("tablename") + "_path";
        String tableinfo = qDto.getString("tablename") + "_info";
        jdbcTemplate.execute(" delete from " + tablename + " where id_='" + id
                + "'");
        Archive_tablenamePO archive_tablenamePO = archive_tablenameMapper
                .selectOne(dto);
        if (archive_tablenamePO.getTablemedia() != null
                && archive_tablenamePO.getTablemedia() != "") {
            jdbcTemplate.execute(" delete from " + tableinfo + " where id_='"
                    + id + "'");
        }
        String msg = AOSUtils.merge("操作完成，成功删除信息[{0}]个", del);
        outDto.setAppMsg(msg);
        return outDto;
    }
    /**
     *
     * 更新电子文件个数
     *
     * @param qDto
     * @return
     */
    public void refreshPath(Dto qDto) {
        String tablename = qDto.getString("tablename");
        String tid = qDto.getString("tid");
        String sql = " UPDATE " + tablename
                + " set _path =(select count(id_) from " + tablename
                + "_path where tid='" + tid + "' ) where id_='" + tid + "'";
        jdbcTemplate.execute(sql);
    }
    /**
     * 根据id查询文件详细信息
     *
     * @param id
     * @param tablename
     * @return
     */
    public List<Map<String, Object>> find_pathmessage(String id,
                                                      String tablename) {
        // TODO Auto-generated method stub
        String sql = "select*from " + tablename + "_path " + " where id_='"
                + id + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }
    /**
     *
     * 删除全部电子文件
     *
     * @param qDto
     * @return
     */
    public Dto deletePathAll(Dto qDto) {
        Dto outDto = Dtos.newDto();
        int del = 0;
        String[] selections = qDto.getRows();
        String tablename = qDto.getString("tablename") + "_path";
        String tableinfo = qDto.getString("tablename") + "_info";
        Dto dto = Dtos.newDto();
        dto.put("tablename", qDto.getString("tablename"));
        for (String id : selections) {
            jdbcTemplate.execute(" DELETE FROM " + tablename + " WHERE tid='"
                    + id + "'");
            Archive_tablenamePO archive_tablenamePO = archive_tablenameMapper
                    .selectOne(dto);
            if (archive_tablenamePO.getTablemedia() != null
                    && archive_tablenamePO.getTablemedia() != "") {
                jdbcTemplate.execute(" DELETE FROM " + tableinfo
                        + " WHERE tid='" + id + "'");
            }

            jdbcTemplate.execute(" UPDATE  " + qDto.getString("tablename")
                    + " SET _path='0' WHERE id_='" + id + "'");
            del++;
        }
        String msg = AOSUtils.merge("操作完成，成功删除", del);
        outDto.setAppMsg(msg);
        return outDto;
    }
}
