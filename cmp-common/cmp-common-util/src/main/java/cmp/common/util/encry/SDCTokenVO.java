package cmp.common.util.encry;

/**
 * 盛大C端token对象
 * 
 * @author lyh
 *
 */
public class SDCTokenVO {

	/**
	 * token
	 */
	private String accessToken;
	/**
	 * token过期时间
	 */
	private String tokenDeadLine;

	public String getAccessToken() {
		return accessToken;
	}

	public String getTokenDeadLine() {
		return tokenDeadLine;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setTokenDeadLine(String tokenDeadLine) {
		this.tokenDeadLine = tokenDeadLine;
	}

	@Override
	public String toString() {
		return "SDCTokenVO [accessToken=" + accessToken + ", tokenDeadLine=" + tokenDeadLine + "]";
	}

}
