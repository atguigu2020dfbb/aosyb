package cn.osworks.aos.system.modules.service.properties;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
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

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SxjcPerpertiesService extends JdbcDaoSupport {

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
    private Archive_sxjcxmMapper archive_sxjcxmMapper;
    @Autowired
    private WsdMapper wsdMapper;
    @Autowired
    private CrkMapper crkMapper;
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
        Archive_sxjcxmPO archive_sxjcxmPO=new Archive_sxjcxmPO();
        AOSUtils.copyProperties(inDto,archive_sxjcxmPO);
        archive_sxjcxmPO.setId_(AOSId.uuid());
        archive_sxjcxmMapper.insert(archive_sxjcxmPO);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成！！！");
        return  outDto;
    }

    public Dto updateDepotIn(Dto inDto){
        Dto outDto = Dtos.newDto();
        Archive_sxjcxmPO archive_sxjcxmPO=new Archive_sxjcxmPO();
        AOSUtils.copyProperties(inDto,archive_sxjcxmPO);
        archive_sxjcxmMapper.updateByKey(archive_sxjcxmPO);
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
            archive_sxjcxmMapper.deleteByKey(id_);
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

    public List<Map<String, Object>> list() {
        String sql="select * from archive_sxjcxm order by bh asc";
        return jdbcTemplate.queryForList(sql);
    }
}
