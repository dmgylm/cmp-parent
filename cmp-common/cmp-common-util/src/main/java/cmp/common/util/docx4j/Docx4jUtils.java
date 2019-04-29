package cmp.common.util.docx4j;

import org.csource.fastdfs.StorageClient1;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.fastdfs.FastDFSClient;
import cmp.common.util.fastdfs.FastDFSUtil;
import cmp.common.util.string.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * docx4j工具类
 *
 * @author sd
 * @date 2017/09/26
 */
public class Docx4jUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Docx4jUtils.class);

    /**
     * 获取上传合同照片的pdf字节数组
     *
     * @param filePathList
     * @return
     * @throws Exception
     */
    public static byte[] getPdfBytes(List<String> filePathList) throws Exception {
        byte[] bytes = null;
        File pdfFile = getPdfFile2(filePathList);
        if (null != pdfFile) {
            bytes = convertImageToByteArray(pdfFile);
            pdfFile.delete();
        }
        return bytes;
    }

    /**
     * 获取上传合同照片的pdf文档
     *
     * @param filePathList 图片路径集合
     * @return 目标pdf文档
     * @throws Exception
     */
    public static File getPdfFile(List<String> filePathList) throws Exception {
        if (null == filePathList || filePathList.size() == 0) {
            LOGGER.error("获取文件pdf失败！没有传入文件路径！filePathList:{}", filePathList);
            return null;
        }
        WordprocessingMLPackage mlPackage = WordprocessingMLPackage.createPackage();
        //File contractPdf = ResourceUtils.getFile("classpath:csp_contract_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
        //File pdfFile = new File("D:\\csp_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
        File pdfFile = new File("classpath:csp_contract_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
        File tmpDocx = new File("classpath:csp_tmp.docx");
        for (String filePath : filePathList) {
            if (StringUtils.isNotBlank(filePath)) {
                int portIndex = filePath.indexOf("8888");
                String path = filePath.substring(portIndex + 5);
                String groupName = path.substring(0, path.indexOf("/"));
                String remotePath = path.substring(path.indexOf("/") + 1);
                FastDFSClient fastDFSClient = FastDFSUtil.getClientByGroupName(groupName);
                StorageClient1 storageClient1 = fastDFSClient.getStorageClient1();
                if (null == storageClient1) {
                    // 如果第一次获取不到文件服务器连接，则再次尝试获取
                    fastDFSClient = FastDFSUtil.getClientByGroupName(groupName);
                }
                byte[] bytes = fastDFSClient.downloadFileForBytes(groupName, remotePath);
                if (null != bytes && bytes.length > 0) {
                    addImageToPackage(mlPackage, bytes);
                    mlPackage.save(tmpDocx);
                }
            }
        }
        OutputStream outputStream = new FileOutputStream(pdfFile);
        Docx4J.toPDF(mlPackage, outputStream);
        tmpDocx.delete();
        outputStream.flush();
        outputStream.close();
        return pdfFile;
    }

    public static File getPdfFile2(List<String> filePathList) throws Exception {
        if (null == filePathList || filePathList.size() == 0) {
            LOGGER.error("获取文件pdf失败！没有传入文件路径！filePathList:{}", filePathList);
            return null;
        }
        WordprocessingMLPackage mlPackage = WordprocessingMLPackage.createPackage();
        //File contractPdf = ResourceUtils.getFile("classpath:csp_contract_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
        //File pdfFile = new File("D:\\csp_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
        File pdfFile = new File("classpath:csp_contract_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
        File tmpDocx = new File("classpath:csp_tmp.docx");
        for (String filePath : filePathList) {
            if (StringUtils.isNotBlank(filePath)) {
                int portIndex = filePath.indexOf("8888");
                String path = filePath.substring(portIndex + 5);
                String groupName = path.substring(0, path.indexOf("/"));
                String remotePath = path.substring(path.indexOf("/") + 1);
                FastDFSClient fastDFSClient = FastDFSUtil.getClientByGroupName(groupName);
                StorageClient1 storageClient1 = fastDFSClient.getStorageClient1();
                if (null == storageClient1) {
                    // 如果第一次获取不到文件服务器连接，则再次尝试获取
                    fastDFSClient = FastDFSUtil.getClientByGroupName(groupName);
                }
                byte[] bytes = fastDFSClient.downloadFileForBytes(groupName, remotePath);
                if (null != bytes && bytes.length > 0) {
                    addImageToPackage(mlPackage, bytes);
                    mlPackage.save(tmpDocx);
                    //WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new ByteArrayInputStream(bytes));
                    //List<SectionWrapper> sections = wordMLPackage.getDocumentModel().getSections();
                    //for (int i = 0; i < sections.size(); i++) {
                    //    System.out.println("sections size: "+sections.size());
                    //    //wordMLPackage.getDocumentModel().getSections().get(i).getPageDimensions().set
                    //}
                    //
                    //Mapper fontMapper = new IdentityPlusMapper();
                    //PhysicalFont font = PhysicalFonts.getPhysicalFonts().get("Comic Sans MS");
                    //fontMapper.getFontMappings().put("Algerian", font);
                    //
                    //wordMLPackage.setFontMapper(fontMapper);
                    //org.docx4j.convert.out.p

                }
            }
        }


        OutputStream outputStream = new FileOutputStream(pdfFile);

        FOSettings foSettings = Docx4J.createFOSettings();
        foSettings.setWmlPackage(mlPackage);
        Docx4J.toFO(foSettings, outputStream, Docx4J.FLAG_EXPORT_PREFER_XSL);

        //Docx4jProperties.setProperty("com.plutext.converter.URL","http://converter-eval.plutext.com:80/v1/00000000-0000-0000-0000-000000000000/convert");
        //Docx4J.toPDF(mlPackage, outputStream);
        tmpDocx.delete();
        outputStream.flush();
        outputStream.close();
        return pdfFile;
    }


    /**
     * Docx4j拥有一个由字节数组创建图片部件的工具方法, 随后将其添加到给定的包中. 为了能将图片添加
     * 到一个段落中, 我们需要将图片转换成内联对象. 这也有一个方法, 方法需要文件名提示, 替换文本,
     * 两个id标识符和一个是嵌入还是链接到的指示作为参数.
     * 一个id用于文档中绘图对象不可见的属性, 另一个id用于图片本身不可见的绘制属性. 最后我们将内联
     * 对象添加到段落中并将段落添加到包的主文档部件.
     *
     * @param wordMLPackage 要添加图片的包
     * @param bytes         图片对应的字节数组
     * @throws Exception 不幸的createImageInline方法抛出一个异常(没有更多具体的异常类型)
     */
    public static void addImageToPackage(WordprocessingMLPackage wordMLPackage, byte[] bytes) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
        int docPrId = 1;
        int cNvPrId = 2;
        Inline inline = imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, false);
        P paragraph = addInlineImageToParagraph(inline);
        wordMLPackage.getMainDocumentPart().addObject(paragraph);
    }

    /**
     * 创建一个对象工厂并用它创建一个段落和一个可运行块R，然后将可运行块添加到段落中，然后创建一个图画并将其添加到段落中，最后将内联对象添加到图画中并返回段落对象
     *
     * @param inline 包含图片的内联对象
     * @return 包含图片的段落
     */
    public static P addInlineImageToParagraph(Inline inline) {
        // 添加内联对象到一个段落中
        ObjectFactory factory = Context.getWmlObjectFactory();
        P paragraph = factory.createP();
        R run = factory.createR();
        paragraph.getContent().add(run);
        Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return paragraph;
    }

    /**
     * 将图片从文件对象转换成字节数组
     *
     * @param file 将要转换的文件
     * @return 包含图片字节数据的字节数组
     * @throws IOException
     */
    public static byte[] convertImageToByteArray(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            System.out.println("file is too large!");
            LOGGER.error("file is too large!");
            return null;
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            System.out.println("could not completely read file " + file.getName());
            LOGGER.error("could not completely read file: {}", file.getName());
        }
        inputStream.close();
        return bytes;
    }

    public static void main(String[] args) {
        String path = "http://192.168.1.142:8888/group1/M00/00/00/wKgBj1mNRyqAJi39AABR-z3hoYc947.jpg";
        int index = path.indexOf("8888");
        String filePath = path.substring(index + 5);
        String group = filePath.substring(0, filePath.indexOf("/"));
        System.out.println("group: " + group);
        String remotePath = filePath.substring(filePath.indexOf("/") + 1);
        System.out.println("remotePath: " + remotePath);
    }
}
