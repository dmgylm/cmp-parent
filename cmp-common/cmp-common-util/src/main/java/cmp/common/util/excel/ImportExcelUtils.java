package cmp.common.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.constant.ConfigConsts;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入Excel工具类
 *
 * @date 2017年8月28日
 */
public class ImportExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExcelUtils.class);

    /**
     * 2003版本的excel
     */
    private static final String EXCEL_2003 = ".xls";

    /**
     * 2007版本的excel
     */
    private static final String EXCEL_2007 = ".xlsx";

    /**
     * 获取IO流中的数据，组装成List<List<Object>>对象
     *
     * @param inputStream
     * @param fileName
     * @return
     * @throws Exception
     */
    public static List<List<Object>> getDataList(InputStream inputStream, String fileName) throws Exception {
        List<List<Object>> dataList = null;
        // 创建Excel工作薄
        Workbook workbook = getWorkBook(inputStream, fileName);
        if (null == workbook) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        dataList = new ArrayList<List<Object>>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(i);
            if (null == sheet) {
                continue;
            }
            // 遍历当前sheet中的所有行
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            for (int j = firstRowNum; j < lastRowNum + 1; j++) {
                row = sheet.getRow(j);
                if (null == row || row.getFirstCellNum() == j) {
                    continue;
                }
                // 遍历所有的列
                List<Object> list = new ArrayList<Object>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    list.add(getCellValue(cell));
                }
                dataList.add(list);
            }
        }
        //workbook.close();
        return dataList;
    }

    /**
     * 根据上传文件后缀，自适应上传文件的版本
     *
     * @param inputStream
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkBook(InputStream inputStream, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (EXCEL_2003.equals(fileType)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (EXCEL_2007.equals(fileType)) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            LOGGER.error("导入的文件名为：{} 的文件格式解析错误！该文件不是常规的Excel文件！", fileName);
            throw new Exception("导入文件格式解析错误");
        }
        return workbook;
    }

    /**
     * 对表格中的数值进行格式化
     *
     * @param cell
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Object getCellValue(Cell cell) {
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");
        DecimalFormat df2 = new DecimalFormat("0.00");
        SimpleDateFormat sdf = new SimpleDateFormat(ConfigConsts.DATE_TIME_FORMAT);
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }
}
