package cmp.common.util.coordinate;

/**
 * 坐标
 * 
 * @author lyh
 *
 */
public class CoordinateVO {

	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * 维度
	 */
	private double lantitude;

	public double getLongitude() {
		return longitude;
	}

	public double getLantitude() {
		return lantitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLantitude(double lantitude) {
		this.lantitude = lantitude;
	}

	public CoordinateVO() {
		super();
	}

	public CoordinateVO(double longitude, double lantitude) {
		super();
		this.longitude = longitude;
		this.lantitude = lantitude;
	}

	@Override
	public String toString() {
		return "CoordinateVO [longitude=" + longitude + ", lantitude=" + lantitude + "]";
	}

}
