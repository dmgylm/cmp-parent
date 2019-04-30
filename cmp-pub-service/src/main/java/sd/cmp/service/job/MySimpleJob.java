package sd.cmp.service.job;


import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
@Slf4j
@ElasticJobConf(name = "MySimpleJob", cron = "0/10 * * * * ?",
		shardingItemParameters = "0=0", description = "简单任务", eventTraceRdbDataSource = "dataSource")
public class MySimpleJob implements SimpleJob {

	@Override
	public void execute(ShardingContext context) {
		log.info("job 启动了");
	}

}
