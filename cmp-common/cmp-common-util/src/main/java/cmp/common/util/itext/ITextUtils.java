package cmp.common.util.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.csource.fastdfs.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.fastdfs.FastDFSClient;
import cmp.common.util.fastdfs.FastDFSUtil;
import cmp.common.util.string.StringUtils;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * iText工具类
 *
 * @author sd
 * @date 2017/12/14
 */
public class ITextUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ITextUtils.class);

	/**
	 * 根据图片的存储路径通过FastDFS服务器下载该文件，将图片合成pdf文件，返回该pdf文件的字节数组
	 *
	 * @param filePathList
	 *            文件的存储路径
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] getPdfFileByFastDFS(List<String> filePathList) throws Exception {
		byte[] buffer = null;
		try {
			if (null == filePathList || filePathList.size() == 0) {
				LOGGER.error("获取文件pdf失败！没有传入文件路径！filePathList:{}", filePathList);
				return null;
			}
			File tempFile = File.createTempFile(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), ".pdf");
			// File tempFile = new File("D:\\tmp\\" + new
			// SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
			// 创建文件
			Document document = new Document();
			// 建立一个书写器
			PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			// 打开文件
			document.open();
			for (int i = 0; i < filePathList.size(); i++) {
				String filePath = filePathList.get(i);
				if (StringUtils.isNotBlank(filePath)) {
					// 创建表
					PdfPTable pdfPTable = new PdfPTable(1);
					pdfPTable.setWidthPercentage(80f);
					pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);
					pdfPTable.getDefaultCell().setBorderWidth(1f);
					// 根据图片路径获取groupName、remoteFileName
					String groupName = filePath.substring(filePath.indexOf("g")).substring(0, 6);
					String fileId = filePath.substring(filePath.indexOf("g"));
					LOGGER.info("fileId: " + fileId);
					String remoteFileName = fileId.substring(fileId.indexOf("/") + 1);
					LOGGER.info("remoteFileName: " + remoteFileName);
					FastDFSClient fastDFSClient;
					if (StringUtils.isBlank(groupName)) {
						fastDFSClient = FastDFSUtil.getDefaultGroupClient();
					} else {
						fastDFSClient = FastDFSUtil.getClientByGroupName(groupName);
					}
					StorageClient storageClient = fastDFSClient.getStorageClient();
					if (null == storageClient) {
						// 如果第一次获取不到文件服务器连接，则再次尝试获取
						if (StringUtils.isBlank(groupName)) {
							fastDFSClient = FastDFSUtil.getDefaultGroupClient();
						} else {
							fastDFSClient = FastDFSUtil.getClientByGroupName(groupName);
						}
					}
					LOGGER.info("开始获取fileId为:{} 的文件!", fileId);
					byte[] bytes = fastDFSClient.downloadFileForBytes(groupName, remoteFileName);
					if (bytes != null && bytes.length > 0) {
						LOGGER.info("成功获取fileId为:{} 的文件!", fileId);
						Image image = Image.getInstance(bytes);
						// String url =
						// "http://150.242.239.250:8131/group1/M00/00/0C/wKhkDVplbtuAIKtqAAJDXccVIIM012.jpg";
						// Image image = Image.getInstance(url);
						image.setScaleToFitHeight(true);
						pdfPTable.addCell(image);
						document.add(pdfPTable);
					} else {
						LOGGER.error("获取fileId为:{} 的文件失败! 现在开始重试5次获取该文件!", fileId);
						for (int j = 0; j < 5; j++) {
							bytes = fastDFSClient.downloadFileForBytes(groupName, remoteFileName);
							if (null != bytes && bytes.length > 0) {
								LOGGER.info("重试第:{} 次获取fileId为:{} 的文件成功!", j, fileId);
								Image image = Image.getInstance(bytes);
								image.setScaleToFitHeight(true);
								pdfPTable.addCell(image);
								document.add(pdfPTable);
								break;
							} else {
								LOGGER.error("重试第:{} 次获取fileId为:{} 的文件失败! 现在开始重试第:{} 次获取该文件!", j, fileId, j + 1);
							}
						}
					}
				}
			}
			document.close();
			instance.close();
			FileInputStream fis = new FileInputStream(tempFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			bos.close();
			fis.close();
			// 删除临时文件
			if (null != tempFile) {
				tempFile.deleteOnExit();
			}
			buffer = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("根据图片合成pdf文件异常！异常信息为:{}, e:{}!", e.getMessage(), e);
		} catch (DocumentException e) {
			e.printStackTrace();
			LOGGER.error("合成pdf文件异常！异常信息为:{}, e:{}!", e.getMessage(), e);
		}
		return buffer;
	}

	/**
	 * 根据图片的存储路径将图片合成pdf文件，返回该pdf文件的字节数组
	 *
	 * @param filePathList
	 *            文件的存储路径
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] getPdfFile(List<String> filePathList) throws Exception {
		byte[] buffer = null;
		try {
			if (null == filePathList || filePathList.size() == 0) {
				LOGGER.error("获取文件pdf失败！没有传入文件路径！filePathList:{}", filePathList);
				return null;
			}
			File tempFile = File.createTempFile(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), ".pdf");
			// File tempFile = new File("D:\\tmp\\" + new
			// SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
			// 创建文件
			Document document = new Document();
			// 建立一个书写器
			PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			// 打开文件
			document.open();
			LOGGER.info("开始将图片合成pdf!总共有：{} 张图片!", filePathList.size());
			for (int i = 0; i < filePathList.size(); i++) {
				String filePath = filePathList.get(i);
				if (StringUtils.isNotBlank(filePath)) {
					// 创建表
					PdfPTable pdfPTable = new PdfPTable(1);
					pdfPTable.setWidthPercentage(80f);
					pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);
					pdfPTable.getDefaultCell().setBorderWidth(1f);

					Image image = null;
					try {
						image = Image.getInstance(new URL(filePath));
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.error("获取第：{} 张图片失败! filePath为:{} 的文件!现在开始从备份文件【17】服务器上取该文件!", i + 1, filePath);
						String bakPath = "http://150.242.239.250:8171/" + filePath.substring(filePath.indexOf("g"));
						image = Image.getInstance(new URL(bakPath));
						LOGGER.info("成功从备份文件【17】服务器上获取第：{} 张图片! bakPath:{} 的文件!", i + 1, bakPath);
					}
					if (image != null) {
						image.setScaleToFitHeight(true);
						pdfPTable.addCell(image);
						document.add(pdfPTable);
						LOGGER.info("成功获取第：{} 张图片! filePath为:{} 的文件!", i + 1, filePath);
					} else {
						LOGGER.error("获取第：{} 张图片失败! filePath为:{} 的文件!", i + 1, filePath);
					}
				}
			}
			document.close();
			instance.close();
			FileInputStream fis = new FileInputStream(tempFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			bos.close();
			fis.close();
			// 删除临时文件
			if (null != tempFile) {
				tempFile.deleteOnExit();
			}
			buffer = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("根据图片合成pdf文件异常！异常信息为:{}, e:{}!", e.getMessage(), e);
			throw e;
		} catch (DocumentException e) {
			e.printStackTrace();
			LOGGER.error("合成pdf文件异常！异常信息为:{}, e:{}!", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("合成pdf文件异常！异常信息为:{}, e:{}!", e.getMessage(), e);
			throw e;
		}
		return buffer;
	}

	/**
	 * 将2个pdf文件合并成1个，并返回字节数组
	 * 
	 * @param pdfPathA
	 *            第1个pdf的路径
	 * @param pdfPathB
	 *            第2个pdf的路径
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] mergePdfFile(String pdfPathA, String pdfPathB) throws Exception {
		byte[] buffer = null;
		try {
			// 获取2个pdf文件
			PdfReader reader1 = new PdfReader(pdfPathA);
			PdfReader reader2 = new PdfReader(pdfPathB);
			// 生成临时文件
			File tempFile = File.createTempFile(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), ".pdf");
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			java.util.List<PdfReader> readers = new ArrayList<PdfReader>();
			readers.add(reader1);
			readers.add(reader2);
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);
				}
				pageOfCurrentReaderPDF = 0;
			}
			document.close();
			FileInputStream fis = new FileInputStream(tempFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			bos.close();
			fis.close();
			// 删除临时文件
			if (null != tempFile) {
				tempFile.deleteOnExit();
			}
			buffer = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("合并pdf文件异常！异常信息为:{}, e:{}!", e.getMessage(), e);
			throw e;
		}
		return buffer;
	}
}
