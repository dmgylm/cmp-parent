package cmp.common.util.excel;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil<T> {


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
					if (StrUtil.isNotBlank(fieldName) && "serialVersionUID".equals(fieldName)) {
						continue;
					}
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Cell cell = row.createCell(j);
					Class<? extends Object> clazz = t.getClass();
					try {
						Method method = clazz.getMethod(getMethodName, new Class[]{});
						Object value = method.invoke(t, new Object[]{});
						String textValue = String.valueOf(value);
						if (StrUtil.isNotBlank(textValue)) {
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


}
