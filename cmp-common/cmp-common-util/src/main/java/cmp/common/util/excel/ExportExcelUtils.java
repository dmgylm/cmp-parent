package cmp.common.util.excel;

import cmp.common.util.constant.ConfigConsts;
import cmp.common.util.string.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 导出Excel工具类
 *
 * @date 2017年8月28日
 */
public class ExportExcelUtils<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelUtils.class);

    /**
     * 导出订单管理
     *
     * @param title
     * @param headers
     * @param dataset
     * @return
     */
    @SuppressWarnings("deprecation")
    public Workbook exportOrderListExcel(String title, String[] headers, List<T> dataList) {
        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        int _ix = 1;
        workbook = new SXSSFWorkbook(500);
        sheet = workbook.createSheet(title);
        // 创建excel头部
        row = sheet.createRow(0);
        Cell cell = null;
        CellStyle top = workbook.createCellStyle();
        top.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        row = sheet.createRow(1);
        CellRangeAddress ca0 = new CellRangeAddress(0, 0, 0, 9);
        sheet.addMergedRegion(ca0);
        if (headers != null && headers.length > 0) {
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i);
                RichTextString text = new XSSFRichTextString(headers[i]);
                cell.setCellValue(text);
                Font headfont = workbook.createFont();
                headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
                headfont.setFontHeightInPoints((short) 10);
                CellStyle style = workbook.createCellStyle();
                // 并设置值表头 设置表头颜色
                style.setFont(headfont);
                style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                style.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
                style.setAlignment(CellStyle.ALIGN_CENTER); // 创建一个居中格式
                cell.setCellStyle(style);
            }
        }
        sheet.setDefaultColumnWidth((short) 20); // 默认宽度为20
        // 遍历数据
        Iterator<T> it = dataList.iterator();
        while (it.hasNext()) {
            _ix++;
            row = sheet.createRow(_ix);
            T t = (T) it.next();
            Field[] fields = t.getClass().getDeclaredFields();
            CellStyle setBorder = workbook.createCellStyle();
            setBorder.setAlignment(CellStyle.ALIGN_CENTER); // 居中 Font font =
            // workbook.createFont();
            setBorder.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            if (fields != null && fields.length > 0) {
                for (int j = 0; j < fields.length; j++) {
                    sheet.autoSizeColumn(j, true);// 自适应列宽
                    Field field = fields[j];
                    String fieldName = field.getName();
                    if (!fieldName.equals("cid")) {
                        cell = row.createCell(j);
                        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Class<? extends Object> tCls = t.getClass();
                        try {
                            Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                            try {
                                Object value = getMethod.invoke(t, new Object[]{});
                                String textValue = String.valueOf(value);
                                if (textValue != null) {
                                    if (textValue == "" || textValue == "null") {
                                        textValue = "0";
                                        cell.setCellValue(textValue);
                                        cell.setCellStyle(setBorder);
                                    } else {
                                        RichTextString richString = new XSSFRichTextString(textValue);
                                        cell.setCellValue(richString);
                                        cell.setCellStyle(setBorder);
                                    }
                                } else {
                                    cell.setCellValue("0");
                                    cell.setCellStyle(setBorder);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // 合并单元格
        int currnetRow = 2;
        int lastRow = sheet.getLastRowNum();
        for (int p = currnetRow; p < lastRow; p++) {// totalRow 总行数
            Cell currentCell = sheet.getRow(p).getCell(0);
            String current = currentCell.getStringCellValue();
            Cell nextCell = null;
            String next = "";
            if (p < lastRow + 1) {
                Row nowRow = sheet.getRow(p + 1);
                if (nowRow != null) {
                    nextCell = nowRow.getCell(0);
                    next = nextCell.getStringCellValue();
                } else {
                    next = "";
                }
            } else {
                next = "";
            }

            if (current.equals(next) && p != lastRow - 1) {// 比对是否相同
                currentCell.setCellValue("");
                continue;
            } else if (current.equals(next) && p == lastRow - 1) {
                sheet.addMergedRegion(new CellRangeAddress(currnetRow, p + 1, 0, 0));// 合并单元格
                Cell nowCell = sheet.getRow(currnetRow).getCell(0);
                nowCell.setCellValue(current);
                break;
            } else {
                sheet.addMergedRegion(new CellRangeAddress(currnetRow, p, 0, 0));// 合并单元格
                Cell nowCell = sheet.getRow(currnetRow).getCell(0);
                nowCell.setCellValue(current);
                currnetRow = p + 1;
            }
        }
        return workbook;
    }

    /**
     * 生成导出内容信息
     *
     * @param sheet
     * @param contextStyle 内容样式
     * @param list         待导出的列表信息
     * @param fieldNames   属性名称
     * @param isHaveSerial 是否添加序列号
     */
    public static <T> void addContextByList(HSSFSheet sheet, HSSFCellStyle contextStyle, List<T> list,
											String[] fieldNames, boolean isHaveSerial) {

        HSSFRow row = null;
        HSSFCell cell = null;
        sheet.createFreezePane(0, 1, 0, 1);

        if (null != list && list.size() > 0) {
            T t = null;
            String value = "";
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i);
                for (int j = 0; j < fieldNames.length; j++) {
                    t = list.get(i);
                    value = objectToString(getFieldValueByName(fieldNames[j], t));
                    if (isHaveSerial) {
                        // 首列加序列号
                        if (null != row.getCell(0) && null != row.getCell(0).getStringCellValue()) {
                            cell = row.createCell(0);
                            cell.setCellValue(i + "");
                        }
                        cell = row.createCell(j + 1);
                        cell.setCellValue(value);
                    } else {
                        cell = row.createCell(j);
                        cell.setCellValue(value);
                    }
                    cell.setCellStyle(contextStyle);
                }
            }
            for (int j = 0; j < fieldNames.length; j++) {
                sheet.autoSizeColumn(j, true);// 自适应列宽度
            }
        } else {
            row = sheet.createRow(2);
            cell = row.createCell(0);
        }

    }

    /**
     * 将Object转成String类型，便于填充单元格
     *
     * @param object
     * @return
     */
    public static String objectToString(Object object) {
        String str = "";
        if (null == object) {
        } else if (object instanceof Date) {
            DateFormat format = new SimpleDateFormat(ConfigConsts.DATE_TIME_FORMAT);
            Date date = (Date) object;
            str = format.format(date);
        } else if (object instanceof Short) {
            str = Short.toString((Short) object);
        } else if (object instanceof String) {
            str = (String) object;
        } else if (object instanceof Integer) {
            str = ((Integer) object).intValue() + "";
        } else if (object instanceof Long) {
            str = Long.toString(((Long) object).longValue());
        } else if (object instanceof Float) {
            str = Float.toHexString(((Float) object).floatValue());
        } else if (object instanceof Double) {
            str = ((Double) object).doubleValue() + "";
        } else if (object instanceof Boolean) {
            str = Boolean.toString((Boolean) object);
        }
        return str;
    }

    /**
     * 添加标题（第一行）与表头（第二行）
     *
     * @param sheet
     * @param headerStyle  标题样式
     * @param contextStyle 表头样式
     * @param assetTitle   表头数组
     * @param titleName    标题
     */
    public static void addTitle(HSSFSheet sheet, HSSFCellStyle headerStyle, HSSFCellStyle contextStyle,
								String[] assetTitle, String titleName) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, assetTitle.length - 1));
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(titleName);
        cell.setCellStyle(headerStyle);
        row = sheet.createRow(1);
        for (int i = 0; i < assetTitle.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(assetTitle[i]);
            cell.setCellStyle(contextStyle);
        }
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName 属性名
     * @param object    实体对象
     * @return
     */
    @SuppressWarnings("all")
    public static Object getFieldValueByName(String fieldName, Object object) {
        Object fieldVaule = null;
        try {
            if (StringUtils.isNotBlank(fieldName) && null != object) {
                String firstLetter = "";// 首字母
                String getter = "";// get方法
                Method method = null;
                String extraKey = null;
                if (fieldName.indexOf(".") > 0) {
                    String[] extra = fieldName.split(".");
                    fieldName = extra[0];
                    extraKey = extra[1];
                }
                firstLetter = fieldName.substring(0, 1).toUpperCase();
                getter = "get" + firstLetter + fieldName.substring(1);
                method = object.getClass().getMethod(getter, new Class[]{});
                fieldVaule = method.invoke(object, new Class[]{});
                if (null != extraKey) {
                    Map<String, Object> map = (Map<String, Object>) fieldVaule;
                    fieldVaule = map == null ? "" : map.get(extraKey);
                }
            }
            return fieldVaule;
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            LOGGER.error("根据属性名获取属性值出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LOGGER.error("根据属性名获取属性值出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LOGGER.error("根据属性名获取属性值出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            LOGGER.error("根据属性名获取属性值出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取标题样式
     *
     * @param workbook
     * @return
     */
    @SuppressWarnings("deprecation")
    public static HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);
        return headerStyle;
    }

    /**
     * 内容样式
     *
     * @param workbook
     * @return
     */
    @SuppressWarnings("deprecation")
    public static HSSFCellStyle getContextStyle(HSSFWorkbook workbook) {
        HSSFCellStyle contextStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        contextStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        contextStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        contextStyle.setFont(font);
        return contextStyle;
    }

    /**
     * 输出导出的Excel
     *
     * @param workbook
     * @param outPath
     * @throws IOException
     */
    public static void outputExcel(Workbook workbook, String outPath) throws IOException {
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(outPath);
        workbook.write(outputStream);
        outputStream.close();
    }

    /**
     * 导出Excel数据
     *
     * @param sheetName
     * @param headers
     * @param dataList
     * @return
     */
    public Workbook exportExcel(String sheetName, String[] headers, List<T> dataList) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        int index = 0;
        workbook = new SXSSFWorkbook(100);
        sheet = workbook.createSheet(sheetName);
        sheet.createFreezePane(0, 1, 0, 1);
        // sheet.setDefaultColumnWidth((short) 20);// 默认宽度：20
        // 1、创建Excel头部
        row = sheet.createRow(0);
        if (null != headers && headers.length > 0) {
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i];
                Cell cell = row.createCell(i);
                RichTextString text = new XSSFRichTextString(headers[i]);
                cell.setCellValue(text);
                // 1.1、设置字体
//				Font headfont = workbook.createFont();
//				headfont.setBold(true);
                // headfont.setFontHeightInPoints((short) 10);
                CellStyle style = workbook.createCellStyle();
//				style.setFont(headfont);
//				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // style.setFillForegroundColor(HSSFColorPredefined.GREEN.getIndex());
//				style.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, header.getBytes().length * 2 * 256);
            }
        }
        sheet.setDefaultColumnWidth(50);
        // 2、遍历数据
        CellStyle borderStyle = workbook.createCellStyle();
//		borderStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
//		borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        Iterator<T> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) iterator.next();
            Field[] fields = t.getClass().getDeclaredFields();
            if (null != fields && fields.length > 0) {
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    String fieldName = field.getName();
                    // 过滤对serialVersionUID属性字段的解析
                    if (StringUtils.isNotBlank(fieldName) && "serialVersionUID".equals(fieldName)) {
                        continue;
                    }
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Cell cell = row.createCell(j);
                    Class<? extends Object> clazz = t.getClass();
                    try {
                        Method method = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = method.invoke(t, new Object[]{});
                        String textValue;
                        if(value instanceof Date){
                            textValue = df.format(value);
                        }else {
                            textValue = String.valueOf(value);
                        }
                        if (StringUtils.isNotBlank(textValue)) {
                        	if("null".equals(textValue)){
                        		textValue = "";
                        	}
                            Pattern pattern = Pattern.compile("^//d+(//.//d+)?$");
                            Matcher matcher = pattern.matcher(textValue);
                            if (matcher.matches()) {// 数字当做double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                                cell.setCellStyle(borderStyle);
                            } else {
                                RichTextString richTextString = new XSSFRichTextString(textValue);
                                cell.setCellValue(richTextString);
                                cell.setCellStyle(borderStyle);
                            }
                        } else {
                            cell.setCellValue("");
                            cell.setCellStyle(borderStyle);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // sheet.trackAllColumnsForAutoSizing();
        // for (int i = 0; i < headers.length; i++) {
        // sheet.autoSizeColumn(i, true);
        // }
        return workbook;
    }

    /**
     * 导出Excel数据
     *
     * @param sheetName
     * @param headers
     * @param dataList
     * @return
     */
    public SXSSFWorkbook exportExcelByNoUID(String sheetName, String[] headers, List<T> dataList) {
        SXSSFWorkbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        int index = 0;
        workbook = new SXSSFWorkbook(100);
        sheet = workbook.createSheet(sheetName);
        sheet.createFreezePane(0, 1, 0, 1);
        // sheet.setDefaultColumnWidth((short) 20);// 默认宽度：20
        // 1、创建Excel头部
        row = sheet.createRow(0);
        if (null != headers && headers.length > 0) {
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i];
                Cell cell = row.createCell(i);
                RichTextString text = new XSSFRichTextString(headers[i]);
                cell.setCellValue(text);
                // 1.1、设置字体
//				Font headfont = workbook.createFont();
//				headfont.setBold(true);
                // headfont.setFontHeightInPoints((short) 10);
                CellStyle style = workbook.createCellStyle();
//				style.setFont(headfont);
//				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // style.setFillForegroundColor(HSSFColorPredefined.GREEN.getIndex());
                //style.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(style);
                // 自适应列宽
                sheet.setColumnWidth(i, header.getBytes().length * 2 * 256);
            }
        }
        //sheet.setDefaultColumnWidth(50);
        // 2、遍历数据
        CellStyle borderStyle = workbook.createCellStyle();
//		borderStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
//		borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        Iterator<T> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) iterator.next();
            Field[] fields = t.getClass().getFields();
            if (null != fields && fields.length > 0) {
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    String fieldName = field.getName();
                    // 过滤对serialVersionUID属性字段的解析
                    if (StringUtils.isNotBlank(fieldName) && "serialVersionUID".equals(fieldName)) {
                        continue;
                    }
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Cell cell = row.createCell(j);
                    Class<? extends Object> clazz = t.getClass();
                    try {
                        Method method = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = method.invoke(t, new Object[]{});
                        String textValue = String.valueOf(value);
                        if (StringUtils.isNotBlank(textValue)) {
                            Pattern pattern = Pattern.compile("^//d+(//.//d+)?$");
                            Matcher matcher = pattern.matcher(textValue);
                            if (matcher.matches()) {// 数字当做double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                                cell.setCellStyle(borderStyle);
                            } else {
                                RichTextString richTextString = new XSSFRichTextString(textValue);
                                cell.setCellValue(richTextString);
                                cell.setCellStyle(borderStyle);
                            }
                        } else {
                            cell.setCellValue("");
                            cell.setCellStyle(borderStyle);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // sheet.trackAllColumnsForAutoSizing();
        // for (int i = 0; i < headers.length; i++) {
        // sheet.autoSizeColumn(i, true);
        // }
        return workbook;
    }

    /**
     * 导出订单激活列表的Excel数据
     *
     * @param headers
     * @param dataList
     * @return
     */
    public SXSSFWorkbook exportActiveOrderExcel(String[] headers, List<T> dataList) {
        SXSSFWorkbook workbook = null;
        try {
            Sheet sheet = null;
            Row row = null;
            workbook = new SXSSFWorkbook(100);
            //sheet.setDefaultColumnWidth(50);
            // 2、遍历数据
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
            borderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            Iterator<T> iterator = dataList.iterator();
            while (iterator.hasNext()) {
                T t = (T) iterator.next();
                Class<? extends Object> clazz = t.getClass();
                Field[] fields = t.getClass().getFields();

                String sheetName = "";
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if ("serviceName".equals(fieldName)) {
                        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Method method = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = method.invoke(t, new Object[]{});
                        if (value != null) {
                            sheetName = (String) value;
                        }
                        break;
                    }
                }
                sheet = getSheetByServiceName(workbook, headers, sheetName);
                int lastRowNum = sheet.getLastRowNum();
                row = sheet.createRow(lastRowNum + 1);
                if (null != fields && fields.length > 0) {
                    for (int j = 0; j < fields.length; j++) {
                        Field field = fields[j];
                        String fieldName = field.getName();
                        // 过滤对serialVersionUID属性字段的解析
                        if (StringUtils.isNotBlank(fieldName) && "serialVersionUID".equals(fieldName)) {
                            continue;
                        }
                        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Cell cell = row.createCell(j);
                        Method method = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = method.invoke(t, new Object[]{});
                        String textValue = String.valueOf(value);
                        if (StringUtils.isNotBlank(textValue)) {
                            Pattern pattern = Pattern.compile("^//d+(//.//d+)?$");
                            Matcher matcher = pattern.matcher(textValue);
                            if (matcher.matches()) {// 数字当做double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                            } else {
                                RichTextString richTextString = new XSSFRichTextString(textValue);
                                cell.setCellValue(richTextString);
                            }
                            cell.setCellStyle(borderStyle);
                        } else {
                            cell.setCellValue("");
                            cell.setCellStyle(borderStyle);
                        }

                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            LOGGER.error("导出订单激活列表的Excel数据异常！异常信息为：{}！", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LOGGER.error("导出订单激活列表的Excel数据异常！异常信息为：{}！", e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            LOGGER.error("导出订单激活列表的Excel数据异常！异常信息为：{}！", e);
        }
        return workbook;
    }

    /**
     *@Description:  * 要求不计入excel的属性放在serialVersionUID属性之后
     * @param sheetName
     * @param headers
     * @param dataList
     *@Date: 下午7:20 2018/11/19
     *@Return:  * @return : org.apache.poi.ss.usermodel.Workbook
     **/
    public Workbook exportActiveExcel(String sheetName, String[] headers, List<T> dataList) throws Exception {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        int index = 0;
        workbook = new SXSSFWorkbook(100);
        sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(50);
        sheet.createFreezePane(0, 1, 0, 1);
        // 1、创建Excel头部
        row = sheet.createRow(0);
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i];
                Cell cell = row.createCell(i);
                RichTextString text = new XSSFRichTextString(headers[i]);
                cell.setCellValue(text);
                sheet.setColumnWidth(i, header.getBytes().length * 2 * 256);
            }
        }
        // 2、遍历插入数据
        Iterator<T> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) iterator.next();
            Field[] fields = t.getClass().getDeclaredFields();
            if (null != fields && fields.length > 0) {
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    String fieldName = field.getName();
                    // 遇见serialVersionUID则跳出本层循环
                    if ("serialVersionUID".equals(fieldName)) {
                        break;
                    }
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Cell cell = row.createCell(j);
                    Class<? extends Object> clazz = t.getClass();
                    Method method = clazz.getMethod(getMethodName, new Class[]{});
                    Object value = method.invoke(t, new Object[]{});
                    if(value instanceof Date){
                        //转换date类型至string
                        cell.setCellValue(df.format(value));
                    }else if(value instanceof Double){
                        cell.setCellValue((Double)value);
                    }else if(value instanceof String){
                        if("null".equals(value)){
                            cell.setCellValue(new XSSFRichTextString(""));
                        }else {
                            cell.setCellValue(new XSSFRichTextString((String)value));
                        }
                    }else {
                        cell.setCellValue(new XSSFRichTextString(""));
                    }
                }
            }
        }
        return workbook;
    }

    /**
     * 根据服务名称创建sheet
     *
     * @param workbook  工作薄空间
     * @param headers   头部字段
     * @param sheetName sheet名称(服务名称)
     * @return 服务名sheet
     */
    private Sheet getSheetByServiceName(Workbook workbook, String[] headers, String sheetName) {
        if (workbook.getSheet(sheetName) != null) {
            return workbook.getSheet(sheetName);
        }
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.createFreezePane(2, 1, 2, 1);

        // sheet.setDefaultColumnWidth((short) 20);// 默认宽度：20
        // 1、创建Excel头部
        Row row = sheet.createRow(0);
        if (null != headers && headers.length > 0) {
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i];
                Cell cell = row.createCell(i);
                RichTextString text = new XSSFRichTextString(headers[i]);
                cell.setCellValue(text);
                // 1.1、设置字体
                Font headfont = workbook.createFont();
                headfont.setBold(true);
                headfont.setFontHeightInPoints((short) 10);
                CellStyle style = workbook.createCellStyle();
                style.setFont(headfont);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cell.setCellStyle(style);
                // 自适应列宽
                sheet.setColumnWidth(i, header.getBytes().length * 2 * 256);
                //sheet.autoSizeColumn(i, true);
            }
        }
        return sheet;
    }
    
    private void setExportExcelHeader(Workbook workbook,Sheet sheet,String[] headers) {
    	sheet.createFreezePane(0, 1, 0, 1);
        Row row = sheet.createRow(0);
        if (null != headers && headers.length > 0) {
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i];
                Cell cell = row.createCell(i);
                RichTextString text = new XSSFRichTextString(headers[i]);
                cell.setCellValue(text);
                CellStyle style = workbook.createCellStyle();
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, header.getBytes().length * 2 * 256);
            }
        }
    }


    // #########lyh

    public static void main(String[] args) throws Exception {
      ExportExcelUtils<TestVoV2> exportExcelUtils=new ExportExcelUtils<>();
        String sheetName = "支出待结算记录列表";
        String[] headers = new String[]{"结算记录编号",
                "备注"};
        List<TestVoV2> data=new ArrayList<>();
        TestVoV2  testVoV2=new TestVoV2();
        testVoV2.setText("lm");
        testVoV2.setStrs(new String[]{"A","B","C","D"});
        data.add(testVoV2);
        Workbook hssfWorkbook = exportExcelUtils.exportExcelByNoUIDPullDown(sheetName, headers, data);
        FileOutputStream output = new FileOutputStream(new File("D://test2.xlsx"));
        hssfWorkbook.write(output);
    }



    public static <T> XSSFWorkbook exportExcelNew(String sheetName, Map<String,String> headerMap, List<T> dataList){
        XSSFWorkbook sfwb =new XSSFWorkbook();

        //拿到excel中的第一个sheet
        XSSFSheet sheet = sfwb.createSheet(sheetName);
        //首行冻结
        sheet.createFreezePane(0, 1, 0, 1);

        List<String> sequenceList = new ArrayList<>();
        //列头赋值
        setHeaderValue(sheet, headerMap,sequenceList);

        sheet.setDefaultColumnWidth(50);

        //列内容赋值
        setContentValue(sheet, sequenceList, dataList);

        return sfwb;
    }


    /**
     * 列头赋值
     * @param sheet
     * @param headerList
     */
    public static void setHeaderValue(XSSFSheet sheet, Map<String,String> headerMap , List<String> sequenceList ){
        //设置列头
        XSSFRow rowOne =sheet.createRow(0);

        int i = 0;
        for (Map.Entry<String,String> entry : headerMap.entrySet()) {
            String headerKey = entry.getKey();
            String headerValue = entry.getValue();
            //顺序
            sequenceList.add(headerKey);

            XSSFCell cell =rowOne.createCell(i);
            cell.setCellValue(new XSSFRichTextString(headerValue));
            sheet.setColumnWidth(i, headerValue.getBytes().length * 2 * 256);

            i++;
        }
    }


    /**
     * 列内容赋值
     * @param sheet
     * @param headerList
     * @param dataList
     */
    public static <T> void setContentValue(XSSFSheet sheet, List<String> sequenceList, List<T> dataList){

        int rowNum = 0;

        Iterator<T> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            rowNum++;
            Row row = sheet.createRow(rowNum);
            T t = iterator.next();
            Field[] fields = t.getClass().getDeclaredFields();
            if (null != fields && fields.length > 0) {

                int dynamicSeq = 0;
                int ceilNum = 0;

                for (int j = 0; j < fields.length; j++) {

                    String fieldName = fields[j].getName();

                    // 过滤对serialVersionUID属性字段的解析
                    if ( "serialVersionUID".equals(fieldName)) {
                        continue;
                    }

                    Object value = null;

                    try {
                        Method method = t.getClass().getMethod("get" + org.apache.commons.lang.StringUtils.capitalize(fieldName));
                        value = method.invoke(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(value instanceof Map){

                        Map<String,String> valueMap =(Map<String,String>) value;

                        for (int n = dynamicSeq; n < sequenceList.size(); n++) {
                            Cell cell = row.createCell(ceilNum);
                            cell.setCellValue(formatValue(valueMap.get(sequenceList.get(n))));
                            ceilNum++;
                        }

                    }else{
                        Cell cell = row.createCell(ceilNum);
                        cell.setCellValue(formatValue(value));
                        ceilNum++;
                        dynamicSeq++;
                    }

                }
            }
        }
    }


    public static String formatValue(Object value){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String textValue ;

        if(value instanceof Date){
            textValue = df.format(value);
        }else {
            textValue = String.valueOf(value);
        }

        if(textValue == null || "null".equals(textValue)){
            textValue = "";
        }


//
//        if (StringUtils.isNotBlank(textValue)) {
//            Pattern pattern = Pattern.compile("^//d+(//.//d+)?$");
//            Matcher matcher = pattern.matcher(textValue);
//            if (matcher.matches()) {// 数字当做double处理
//
//                cell.setCellValue(Double.parseDouble(textValue));
//            } else {
//                RichTextString richTextString = new XSSFRichTextString(textValue);
//                cell.setCellValue(richTextString);
//            }
//        }

        return textValue;

    }



    /**
     * 导出Excel数据
     *
     * @param sheetName
     * @param headers
     * @param dataList
     * @return
     */
    public Workbook exportExcelByNoUIDPullDown(String sheetName, String[] headers, List<T> dataList) {
        Workbook  workbook = null;
        HSSFSheet sheet = null;
        HSSFRow row = null;
        int index = 0;
        workbook = new HSSFWorkbook();
        sheet = ((HSSFWorkbook) workbook).createSheet();
        sheet.createFreezePane(0, 1, 0, 1);
        // sheet.setDefaultColumnWidth((short) 20);// 默认宽度：20
        // 1、创建Excel头部
        row = sheet.createRow(0);
        if (null != headers && headers.length > 0) {
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i];
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
                // 1.1、设置字体
//				Font headfont = workbook.createFont();
//				headfont.setBold(true);
                // headfont.setFontHeightInPoints((short) 10);
                HSSFCellStyle style = (HSSFCellStyle) workbook.createCellStyle();
//				style.setFont(headfont);
//				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // style.setFillForegroundColor(HSSFColorPredefined.GREEN.getIndex());
                //style.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(style);
                // 自适应列宽
                sheet.setColumnWidth(i, header.getBytes().length * 2 * 256);
            }
        }
        //sheet.setDefaultColumnWidth(50);
        // 2、遍历数据
        HSSFCellStyle borderStyle = (HSSFCellStyle) workbook.createCellStyle();
//		borderStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
//		borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        Iterator<T> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) iterator.next();
            Field[] fields = t.getClass().getFields();
            if (null != fields && fields.length > 0) {
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    String fieldName = field.getName();
                    // 过滤对serialVersionUID属性字段的解析
                    if (StringUtils.isNotBlank(fieldName) && "serialVersionUID".equals(fieldName)) {
                        continue;
                    }
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    HSSFCell cell = row.createCell(j);
                    Class<? extends Object> clazz = t.getClass();
                    try {
                        Method method = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = method.invoke(t, new Object[]{});
                        if(value instanceof String[]){
                            String[] valueCopy= (String[]) value;
                            RichTextString richTextString = new HSSFRichTextString(valueCopy[0]);
                            cell.setCellValue(richTextString);
                            cell.setCellStyle(borderStyle);
                            sheet.addValidationData(setHSSFDataValidation(valueCopy,row.getRowNum(),row.getRowNum(),cell.getColumnIndex(),cell.getColumnIndex()+valueCopy.length));
                            continue;
                        }
                        String textValue = String.valueOf(value);
                        if (StringUtils.isNotBlank(textValue)) {
                            Pattern pattern = Pattern.compile("^//d+(//.//d+)?$");
                            Matcher matcher = pattern.matcher(textValue);
                            if (matcher.matches()) {// 数字当做double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                                cell.setCellStyle(borderStyle);
                            } else {
                                RichTextString richTextString = new HSSFRichTextString(textValue);
                                cell.setCellValue(richTextString);
                                cell.setCellStyle(borderStyle);
                            }
                        } else {
                            cell.setCellValue("");
                            cell.setCellStyle(borderStyle);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // sheet.trackAllColumnsForAutoSizing();
        // for (int i = 0; i < headers.length; i++) {
        // sheet.autoSizeColumn(i, true);
        // }
        return workbook;
    }

    public static HSSFDataValidation  setHSSFDataValidation(String[] strs, int firstRow, int lastRow, int firstCol, int lastCol){
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        // 创建下拉列表数据
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(strs);
        // 绑定
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        return dataValidation;
    }

}
