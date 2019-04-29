package sd.cmp.service.provider.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd.cmp.common.resultdeal.ResultVO;
import sd.cmp.common.resultdeal.ResultVOUtil;
import sd.cmp.common.vo.ShopVo;
import sd.cmp.facade.provider.dubbo.MerchantManageService;
import sd.cmp.service.service.TShopService;

@Component
@Slf4j
@Service(version = "1.0.0", interfaceClass = MerchantManageService.class, timeout = 200000)
public class MerchantManageServiceImpl implements MerchantManageService {

	@Autowired
	private TShopService tShopService;
	@Override
	public ResultVO<ShopVo> listShop() {
		return ResultVOUtil.success(tShopService.list());
	}
}
