package sd.cmp.config.elasticJob;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyElasticJobListener implements ElasticJobListener {

	private long beginTime = 0;
	@Override
	public void beforeJobExecuted(ShardingContexts shardingContexts) {
		beginTime = System.currentTimeMillis();

		log.info("===>{} JOB BEGIN TIME: {} <===",shardingContexts.getJobName(), beginTime);
	}

	@Override
	public void afterJobExecuted(ShardingContexts shardingContexts) {
		long endTime = System.currentTimeMillis();
		log.info("===>{} JOB END TIME: {},TOTAL CAST: {} <===",shardingContexts.getJobName(), endTime, endTime - beginTime);
	}
}
