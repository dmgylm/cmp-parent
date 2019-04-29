package cmp.common.util.radom;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import cmp.common.util.constant.ConfigConsts;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 验证码生成工具类
 */
public abstract class RandomCodeUtils {

	private static Logger logger = LoggerFactory.getLogger(RandomCodeUtils.class);

	private static final String RANDOM_CODE_KEY = "random"; // 弹出框中的验证码放在session中的key

	private static final String RANDOM_LOGIN_CODE_KEY = "random_login"; // 点击"登录"时校验的验证码放在session中的key

	private static final int CODE_NUM = 4; // 验证码字符个数

	// 设置图形验证码中字符串的字体和大小
	private static Font myFont = new Font("Arial", Font.BOLD, 16);

	// 随机字符数组
	private static char[] charSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

	private static Random random = new Random();

	/**
	 * 生成随机验证码
	 *
	 * @param request
	 * @param response
	 */
	public static void createRandomCode(HttpServletRequest request, HttpServletResponse response) {
		// 阻止生成的页面内容被缓存，保证每次重新生成随机验证码
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 指定图形验证码图片的大小
		int width = 80, height = 25;
		// 生成一张新图片
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		//取得画笔
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(156,162,166));
        g.fillRect(0, 0, width, height );
         g=image.createGraphics();
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(myFont);
		// 该变量用来保存系统生成的随机字符串
		StringBuilder sRand = new StringBuilder(CODE_NUM);
		for (int i = 0; i < CODE_NUM; i++) {
			// 取得一个随机字符
			String tmp = getRandomChar();
			sRand.append(tmp);
			// 将系统生成的随机字符添加到图形验证码图片上
			g.setColor(Color.WHITE);
			g.drawString(tmp, 15 * i + 10, 20);
		}
		// 取得用户Session
		HttpSession session = request.getSession(true);
		// 将系统生成的图形验证码添加
		session.setAttribute(RANDOM_CODE_KEY, sRand.toString());
		// System.out.println(sRand.toString());
		g.dispose();
		// 输出图形验证码图片
		try {
			ImageIO.write(image, "PNG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 生成随机验证码
	 *
	 * @param request
	 * @param response
	 */
	public static void createRandomCode(HttpServletRequest request, HttpServletResponse response, String type) {
		// 阻止生成的页面内容被缓存，保证每次重新生成随机验证码
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 指定图形验证码图片的大小
		int width = 80, height = 25;
		// 生成一张新图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 在图片中绘制内容
		Graphics g = image.getGraphics();
		g.setColor(getRandomColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(myFont);
		// 随机生成线条，让图片看起来更加杂乱
		g.setColor(getRandomColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width - 1);// 起点的x坐标
			int y = random.nextInt(height - 1);// 起点的y坐标
			int x1 = random.nextInt(6) + 1;// x轴偏移量
			int y1 = random.nextInt(12) + 1;// y轴偏移量
			g.drawLine(x, y, x + x1, y + y1);
		}
		// 随机生成线条，让图片看起来更加杂乱
		for (int i = 0; i < 70; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(12) + 1;
			int y1 = random.nextInt(6) + 1;
			g.drawLine(x, y, x - x1, y - y1);
		}

		// 该变量用来保存系统生成的随机字符串
		StringBuilder sRand = new StringBuilder(CODE_NUM);
		for (int i = 0; i < CODE_NUM; i++) {
			// 取得一个随机字符
			String tmp = getRandomChar();
			sRand.append(tmp);
			// 将系统生成的随机字符添加到图形验证码图片上
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(tmp, 15 * i + 10, 20);
		}

		// 取得用户Session
		HttpSession session = request.getSession(true);
		// 将系统生成的图形验证码添加
		if (type.equals("RANDOM_LOGIN")) {
			session.setAttribute(RANDOM_LOGIN_CODE_KEY, sRand.toString());
		} else {
			session.setAttribute(RANDOM_CODE_KEY, sRand.toString());
		}

		System.out.println(sRand.toString());
		g.dispose();
		// 输出图形验证码图片
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 检查用户输入的验证码是否正确
	 *
	 * @param request
	 * @param inputCode
	 * @return true: 正确, false:不正确
	 */
	public static boolean checkRandomCode(HttpServletRequest request, String inputCode) {
		HttpSession session = request.getSession(false);
		if (session != null && StringUtils.hasLength(inputCode)) {
			String code = (String) session.getAttribute(RANDOM_CODE_KEY);
			logger.info("inputCode:" + inputCode.trim() + ",code:" + code);
			return inputCode.trim().equalsIgnoreCase(code);
		}
		return false;
	}

	/**
	 * 检查用户输入的验证码是否正确
	 *
	 * @param request
	 * @param inputCode
	 * @return true: 正确, false:不正确
	 */
	public static boolean checkRandomCode(HttpServletRequest request, String inputCode, String random) {
		HttpSession session = request.getSession(false);
		if (session != null && StringUtils.hasLength(inputCode)) {
			String code;
			if (random.equals("RANDOM_LOGIN")) {
				code = (String) session.getAttribute(RANDOM_LOGIN_CODE_KEY);
			} else {
				code = (String) session.getAttribute(RANDOM_CODE_KEY);
			}
			logger.info("inputCode:" + inputCode.trim() + ",code:" + code);
			return inputCode.trim().equalsIgnoreCase(code);
		}
		return false;
	}

	/**
	 * 移除验证码
	 *
	 * @param request
	 * @param inputCode
	 */
	public static void removeRandomCode(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(RANDOM_CODE_KEY);
		}
	}

	// 生成随机颜色
	private static Color getRandomColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	// 随机生成一个字符
	private static String getRandomChar() {
		int index = random.nextInt(charSequence.length);
		return String.valueOf(charSequence[index]);
	}

    /**
     * 生成结算记录中的结算编码
     *
     * @return 结算编码
     */
    public static String getSettleNo() {
        StringBuffer settleNo = new StringBuffer();
        settleNo.append(new SimpleDateFormat(ConfigConsts.TIMESTAMP_FORMAT).format(new Date()));
        settleNo.append(RandomStringUtils.randomNumeric(5));
        return settleNo.toString();
    }





	/**
	 * 根据字符串生成随机验证码
	 *
	 * @param request
	 * @param response
	 */
	public static void createRandomCodeByStr(HttpServletRequest request, HttpServletResponse response, String codestr)  {

		// 阻止生成的页面内容被缓存，保证每次重新生成随机验证码
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 指定图形验证码图片的大小
		int width = 80, height = 25;
		// 生成一张新图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 在图片中绘制内容
		Graphics g = image.getGraphics();
		g.setColor(getRandomColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(myFont);
		// 随机生成线条，让图片看起来更加杂乱
		g.setColor(getRandomColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width - 1);// 起点的x坐标
			int y = random.nextInt(height - 1);// 起点的y坐标
			int x1 = random.nextInt(6) + 1;// x轴偏移量
			int y1 = random.nextInt(12) + 1;// y轴偏移量
			g.drawLine(x, y, x + x1, y + y1);
		}
		// 随机生成线条，让图片看起来更加杂乱
		for (int i = 0; i < 70; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(12) + 1;
			int y1 = random.nextInt(6) + 1;
			g.drawLine(x, y, x - x1, y - y1);
		}

		// 该变量用来保存系统生成的随机字符串

		for (int i = 0; i < codestr.length(); i++) {
			// 取得一个随机字符
			String tmp = codestr.charAt(i)+"";
			// 将系统生成的随机字符添加到图形验证码图片上
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(tmp, 15 * i + 10, 20);
		}

		// 取得用户Session
		HttpSession session = request.getSession(true);
		// 将系统生成的图形验证码添加
		g.dispose();
		// 输出图形验证码图片
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}