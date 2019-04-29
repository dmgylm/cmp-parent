package cmp.common.util.date;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;

import java.util.Date;

public class DateUtil {


	public static String getCurrentTimeStr(String patten){
		return DateTime.now().toString(patten);
	}


	public static String getDateStr(String date,String patten){
		return DateTime.of(date,patten).toString(patten);
	}


	public static long getDifferenceBetweedTwoDay(Date date1,Date date2){
		return DateTime.of(date2).between(date1,DateUnit.DAY);
	}







	public static void main(String[] args) {

	}
}
