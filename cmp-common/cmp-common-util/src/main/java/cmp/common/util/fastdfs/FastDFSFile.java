package cmp.common.util.fastdfs;

import java.util.Arrays;

public class FastDFSFile {

	private String name;

	private String ext;

	private String length;

	private String author;

	private byte[] content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public FastDFSFile() {
		super();
	}

	public FastDFSFile(String ext, byte[] content) {
		super();
		this.ext = ext;
		this.content = content;
	}

	public FastDFSFile(String name, String ext, byte[] content) {
		super();
		this.name = name;
		this.ext = ext;
		this.content = content;
	}

	public FastDFSFile(String name, String ext, String length, String author, byte[] content) {
		super();
		this.name = name;
		this.ext = ext;
		this.length = length;
		this.author = author;
		this.content = content;
	}

	@Override
	public String toString() {
		return "FastDFSFile [name=" + name + ", ext=" + ext + ", length=" + length + ", author=" + author + ", content="
				+ Arrays.toString(content) + "]";
	}

}
