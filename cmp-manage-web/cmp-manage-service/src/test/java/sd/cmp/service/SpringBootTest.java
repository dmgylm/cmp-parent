package sd.cmp.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import sd.cmp.config.redis.RedisUtils;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
@Slf4j
public class SpringBootTest {

	@Autowired
	private RedisUtils redisUtils;

	private String testRedisKey="testRedis";

	@Test
	public void testRedis(){
		redisUtils.set(testRedisKey,"lll");
		log.info(redisUtils.get(testRedisKey));
		redisUtils.delete(testRedisKey);
	}
}
