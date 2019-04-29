package cmp.common.util.sms;

public class SMSCode {

	private String code;

	private long time;

	private String mobile;

	private int count;

	private String ipAddress;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public SMSCode() {
		super();
	}

	public SMSCode(String code, long time) {
		super();
		this.code = code;
		this.time = time;
	}

	public SMSCode(String code, long time, int count) {
		super();
		this.code = code;
		this.time = time;
		this.count = count;
	}

	public SMSCode(String code, long time, String mobile, int count, String ipAddress) {
		super();
		this.code = code;
		this.time = time;
		this.mobile = mobile;
		this.count = count;
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString() {
		return "SMSCode [code=" + code + ", time=" + time + ", mobile=" + mobile + ", count=" + count + ", ipAddress="
				+ ipAddress + "]";
	}
}
