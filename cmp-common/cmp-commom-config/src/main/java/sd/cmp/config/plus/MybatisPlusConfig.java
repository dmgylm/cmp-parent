package sd.cmp.config.plus;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("sd.cmp.service.dao")
public class MybatisPlusConfig {
	/**
	 * mybatis-plus SQL执行效率插件【生产环境可以关闭】
	 */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		return new PerformanceInterceptor();
	}


//	@Bean
//	public MetaObjectHandler metaObjectHandler(){
//		return new MyMetaObjectHandler();
//	}

	/**
	 * 分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/**
	 * 注入sql注入器
	 */
	@Bean
	public ISqlInjector sqlInjector(){
		return new LogicSqlInjector();
	}


//	@Bean
//	public SqlExplainInterceptor sqlExplainInterceptor(){
//		SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
//		List<ISqlParser> sqlParserList = new ArrayList<>();
//		sqlParserList.add(new BlockAttackSqlParser());
//		sqlExplainInterceptor.setSqlParserList(sqlParserList);
//		return sqlExplainInterceptor;
//	}


}