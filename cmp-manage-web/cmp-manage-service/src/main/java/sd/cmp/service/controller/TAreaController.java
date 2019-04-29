package sd.cmp.service.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ResponseBody;
import sd.cmp.common.resultdeal.ResultVO;
import sd.cmp.common.vo.ShopVo;
import sd.cmp.facade.provider.dubbo.MerchantManageService;
/**
 * <p>
 * 区域表 前端控制器
 * </p>
 *
 * @author lm
 * @since 2019-04-26
 */
@Controller
@RequestMapping("/tArea")
public class TAreaController {

	@Reference(version = "1.0.0")
	private MerchantManageService merchantManageService;


	@RequestMapping("testDubbo")
	@ResponseBody
	public ResultVO<ShopVo> testDubbo(){
		return merchantManageService.listShop();
	}


}

