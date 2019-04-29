package cmp.common.util.radom;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

public class RandomCharUtil {

	private static final char[] prefix = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	/**
	 * 随机产生最大为18位的long型数据(long型数据的最大值是9223372036854775807,共有19位)
	 * 
	 * @param digit
	 *            用户指定随机数据的位数
	 */
	public static String randomChar(int digit) {
		if (digit >= 19 || digit <= 0)
			throw new IllegalArgumentException("digit should between 1 and 18(1<=digit<=18)");
		String s = RandomStringUtils.randomNumeric(digit - 4);
		StringBuffer sb = new StringBuffer();
		sb.append(getPrefix());
		sb.append(getPrefix());
		sb.append(getPrefix());
		sb.append(getPrefix());
		sb.append(s);
		return sb.toString();
	}

	/**
	 * 随机产生在指定位数之间的long型数据,位数包括两边的值,minDigit<=maxDigit
	 * 
	 * @param minDigit
	 *            用户指定随机数据的最小位数 minDigit>=1
	 * @param maxDigit
	 *            用户指定随机数据的最大位数 maxDigit<=18
	 */
	public static String randomLong(int minDigit, int maxDigit) {
		if (minDigit > maxDigit) {
			throw new IllegalArgumentException("minDigit > maxDigit");
		}
		if (minDigit <= 0 || maxDigit >= 19) {
			throw new IllegalArgumentException("minDigit <=0 || maxDigit>=19");
		}
		return randomChar(minDigit + getDigit(maxDigit - minDigit));
	}

	/**
	 * 随机产生指定的位数的字符串
	 *
	 * @param count 随机字符串的位数
	 * @return 随机字符串
	 */
	public static String randomString(int count) {
		if (count >= 19 || count <= 0) {
			throw new IllegalArgumentException("digit should between 1 and 18(1<=digit<=18)");
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(getPrefix());
		}
		return sb.toString();
	}


	private static int getDigit(int max) {
		return RandomUtils.nextInt(max + 1);
	}

	/**
	 * 前四位为字母
	 */
	private static String getPrefix() {
		return prefix[RandomUtils.nextInt(9)] + "";
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			System.out.println(randomLong(8, 8));
		}
	}
}