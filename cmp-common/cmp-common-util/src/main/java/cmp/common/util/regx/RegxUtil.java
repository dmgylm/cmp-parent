package cmp.common.util.regx;

import cmp.common.util.constant.RegxConstant;
import cn.hutool.core.util.ReUtil;

public class RegxUtil {

	public static void main(String[] args) {
		boolean match = ReUtil.isMatch(RegxConstant.OIL_NO, "EO12cD");
		System.out.println(match);
	}
}
