package sd.cmp.service;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDubboConfiguration
@EnableTransactionManagement
@ComponentScan(value = {"sd.cmp.service","sd.cmp.config.cat","sd.cmp.config.redis",
		"sd.cmp.config.datasource","sd.cmp.config.plus"})
@EnableApolloConfig({"application","CGJ.mysql","CGJ.dubbo","CGJ.redis.springboot"})
public class ClientWebApp {
	public static void main(String[] args) {
		SpringApplication.run(ClientWebApp.class, args);
	}
}
