package cmp.common.util.fastdfs;

import java.io.Serializable;

/**
 * 使用FastDFS上传文件返回的结果集
 * 
 * @author al.wang
 * @date 2017年7月28日
 */
public class FastDFSUploadResult implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7356346279880449845L;

	/**
	 * 组名
	 */
	private String groupName;

	/**
	 * 文件名
	 */
	private String remoteFileName;

	/**
	 * 文件访问的绝对路径
	 */
	private String fileAbsolutePath;

	/**
	 * 返回的状态码
	 */
	private String code;

	/**
	 * 返回的信息
	 */
	private String msg;

	/**
	 * 原始的文件名
	 */
	private String originalFileName;

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemoteFileName() {
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}

	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public FastDFSUploadResult() {
		super();
	}

	public FastDFSUploadResult(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public FastDFSUploadResult(String groupName, String remoteFileName, String fileAbsolutePath, String code,
							   String msg) {
		super();
		this.groupName = groupName;
		this.remoteFileName = remoteFileName;
		this.fileAbsolutePath = fileAbsolutePath;
		this.code = code;
		this.msg = msg;
	}

	public FastDFSUploadResult(String groupName, String remoteFileName, String fileAbsolutePath, String code,
							   String msg, String originalFileName) {
		super();
		this.groupName = groupName;
		this.remoteFileName = remoteFileName;
		this.fileAbsolutePath = fileAbsolutePath;
		this.code = code;
		this.msg = msg;
		this.originalFileName = originalFileName;
	}
	@Override
	public String toString() {
		return "FastDFSUploadResult [groupName=" + groupName + ", remoteFileName=" + remoteFileName
				+ ", fileAbsolutePath=" + fileAbsolutePath + ", code=" + code + ", msg=" + msg + ", originalFileName="
				+ originalFileName + "]";
	}

}
