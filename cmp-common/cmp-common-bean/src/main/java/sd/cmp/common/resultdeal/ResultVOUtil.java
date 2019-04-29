package sd.cmp.common.resultdeal;

public class ResultVOUtil {

	public static ResultVO success(Object object) {
		ResultVO resultVO = new ResultVO();
		resultVO.setData(object);
		resultVO.setCode(BaseEnum.SUCCESS.getCode());
		resultVO.setMsg(BaseEnum.SUCCESS.getMessage());
		return resultVO;
	}

	public static ResultVO success() {
		return success(null);
	}

	public static ResultVO error(String code,String msg) {
		ResultVO resultVO = new ResultVO();
		resultVO.setCode(code);
		resultVO.setMsg(msg);
		return resultVO;
	}

	public static ResultVO error(BaseEnum settleResultEnum) {
		ResultVO resultVO = new ResultVO();
		resultVO.setCode(settleResultEnum.getCode());
		resultVO.setMsg(settleResultEnum.getMessage());
		return resultVO;
	}

}
