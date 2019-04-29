package sd.cmp.config.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * druid 连接池配置类
 * Created by Fant.J.
 * 但是后来我找不到这个原作者的那篇文章，如果有人知道麻烦告诉我一声
 * by slience
 */
@Configuration
public class DruidConfiguration {

	@Value("${jdbc.url}")
	private String dbUrl;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;
	@Value("${jdbc.driver}")
	private String driverClassName;
	@Value("${pool.initialSize}")
	private int initialSize;
	@Value("${pool.minIdle}")
	private int minIdle;
	@Value("${pool.maxActive}")
	private int maxActive;
	@Value("${pool.maxWait}")
	private int maxWait;
	@Value("${pool.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;
	@Value("${pool.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;
	@Value("${pool.validationQuery}")
	private String validationQuery;
	@Value("${pool.testWhileIdle}")
	private boolean testWhileIdle;
	@Value("${pool.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${pool.testOnReturn}")
	private boolean testOnReturn;
	@Value("${pool.poolPreparedStatements}")
	private boolean poolPreparedStatements;
	@Value("${pool.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;
	@Value("${pool.filters}")
	private String filters;
	@Value("{pool.connectionProperties}")
	private String connectionProperties;

	@Bean   //声明其为Bean实例
	@Primary //在同样的DataSource中，首先使用被标注的DataSource
	public DataSource dataSource(){
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(this.dbUrl);
		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setDriverClassName(driverClassName);

		//configuration
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			System.err.println("druid configuration initialization filter: "+ e);
		}
		datasource.setConnectionProperties(connectionProperties);
		return datasource;
	}
	@Bean
	public ServletRegistrationBean statViewServlet(){
		//创建servlet注册实体
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
		//设置ip白名单
		//servletRegistrationBean.addInitParameter("allow","127.0.0.1");
		//设置ip黑名单
		//servletRegistrationBean.addInitParameter("deny","192.168.0.2");
		//设置控制台管理用户__登录用户名和密码
		servletRegistrationBean.addInitParameter("loginUsername","druid");
		servletRegistrationBean.addInitParameter("loginPassword","123456");
		//是否可以重置数据
		servletRegistrationBean.addInitParameter("resetEnable","false");
		return servletRegistrationBean;
	}
	@Bean
	public FilterRegistrationBean statFilter(){
		//创建过滤器
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		//设置过滤器过滤路径
		filterRegistrationBean.addUrlPatterns("/*");
		//忽略过滤的形式
		filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
}
