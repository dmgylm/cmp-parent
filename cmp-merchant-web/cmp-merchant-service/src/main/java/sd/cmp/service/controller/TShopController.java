package sd.cmp.service.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import sd.cmp.facade.model.TShop;
import sd.cmp.service.service.TShopService;

/**
 * <p>
 * 商户信息表 前端控制器
 * </p>
 *
 * @author lm
 * @since 2019-04-26
 */
@Controller
@RequestMapping("/tShop")
@Slf4j
public class TShopController {

	@Autowired
	private TShopService tShopService;

	@Value("${pool.validationQuery}")
	private String validationQuery;


	@RequestMapping("test")
	@ResponseBody
	public IPage<TShop> listShow(){
		log.info("info test ------");
		log.error("error test ------");
		log.debug("info debug ------");
		return tShopService.page(new Page<>(0,10));
	}


	@ResponseBody
	@RequestMapping("testApolloDynamic")
	public String testApolloDynamic(){
		return validationQuery;
	}


}

