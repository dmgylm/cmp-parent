package cmp.common.util.excel;

import javax.persistence.Column;
import java.util.Map;

public class TestVO {

	@Column(name="shop_code")
	private String shopCode;

	@Column(name="shop_name")
	private String shopName;


	private Map<String,String> map ;

	public String getShopName() {
		return shopName;
	}

	public String getShopCode() {
		return shopCode;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return "TestVO{" +
				"shopName='" + shopName + '\'' +
				", shopCode='" + shopCode + '\'' +
				", map=" + map +
				'}';
	}
}
