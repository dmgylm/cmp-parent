package cmp.common.util.picture;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.csource.common.MyException;
import org.springframework.stereotype.Component;
import cmp.common.util.fastdfs.FastDFSClient;
import cmp.common.util.fastdfs.FastDFSUploadResult;
import cmp.common.util.fastdfs.FastDFSUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 生成缩略图
 * @author chenyezhou
 *
 */
@Component
public class NarrowPicture {

	FastDFSClient fastDFSClient = FastDFSUtil.getClientByGroupName("group1");

	/**
	 * 生成缩略图
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public byte[] narrowThePicture(InputStream input) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(input);
		//bufferedImage = ImageIO.read(new File("C:\\Users\\user\\Documents\\Tencent Files\\458967438\\FileRecv\\testJPG.jpg"));
		int newWidth = 300;
		if (null == bufferedImage) 
            return null;
		int originalWidth = bufferedImage.getWidth();
	    int originalHeight = bufferedImage.getHeight();
	    double scale = (double)originalWidth / (double)newWidth;    // 缩放的比例
	    int newHeight =  (int)(originalHeight / scale);
	    byte[] byteAfter = zoomImageUtils( bufferedImage, newWidth, newHeight);
		return byteAfter;
	}
	
	/**
	 * 缩略图片
	 * @param bufferedImage
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
	 private static byte[] zoomImageUtils(BufferedImage bufferedImage, int width, int height)
	            throws IOException {
	     byte[] bytes = null;  
		 try{
		        BufferedImage newImage = new BufferedImage(width, height, bufferedImage.getType());
		        Graphics g = newImage.getGraphics();
		        g.drawImage(bufferedImage, 0, 0, width, height, null);
		        g.dispose();
		       // newImage.getGraphics().drawImage(bufferedImage.getScaledInstance(width, height,  Image.SCALE_DEFAULT), 0, 0,  null);   
		        //将缩略图写入output管道中输出
		        ByteArrayOutputStream output = new ByteArrayOutputStream();
		        ImageIO.write(newImage, "jpg", output);
		        //将缩略图写入字节数组
		         bytes = output.toByteArray();
	       } catch(Exception e){
	        	e.printStackTrace();
	       }
        return bytes;
	}
	 
	 /**
	  * 根据传入的路径，下载并缩小图片然后传上fastDFS服务器，返回地址
	  * @param path
	  * @return
	 * @throws MyException 
	 * @throws IOException
	  */
	 public String NarrowAPicture(String filePath) throws IOException, MyException{
		 if(filePath== null ){
				return null;
			}
		 String[] filePathArray = filePath.split("/");
			String fileName = filePathArray[filePathArray.length-1];
			StringBuilder sb = new StringBuilder();
			for(int i=3; i<filePathArray.length; i++){
				sb.append(filePathArray[i]).append("/");
			}
			sb.deleteCharAt(sb.length()-1);
			String fileId = sb.toString();
			byte[] bytes = null;
			try{
				bytes = fastDFSClient.downloadFileForBytes(fileId);
			} catch(Exception e) {
				bytes = fastDFSClient.downloadFileForBytes(fileId);
			}
	        if(null == bytes) {
	        	return null;
	        }
	        InputStream in = new ByteArrayInputStream(bytes);
		//1.05 生成缩略图
	        byte[] byteAfter = narrowThePicture(in);
	    //1.06 上传该图片，获得Path
	        FastDFSUploadResult fastDFSUploadResult = fastDFSClient.uploadFile(byteAfter, fileName, null);
	        return fastDFSUploadResult.getFileAbsolutePath();
	 }
	 
	 /**
	  * File转byte[]
	  * @param filePath
	  * @return
	  */
	 public static byte[] File2byte(String filePath)
	    {
	        byte[] buffer = null;
	        try
	        {
	            File file = new File(filePath);
	            FileInputStream fis = new FileInputStream(file);
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            byte[] b = new byte[1024];
	            int n;
	            while ((n = fis.read(b)) != -1)
	            {
	                bos.write(b, 0, n);
	            }
	            fis.close();
	            bos.close();
	            buffer = bos.toByteArray();
	        }
	        catch (FileNotFoundException e)
	        {
	            e.printStackTrace();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        return buffer;
	    }

	 
}
