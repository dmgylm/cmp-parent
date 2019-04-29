package sd.cmp.facade.provider.dubbo;

import sd.cmp.common.resultdeal.ResultVO;
import sd.cmp.common.vo.ShopVo;

public interface MerchantManageService {

	public ResultVO<ShopVo> listShop();

}
