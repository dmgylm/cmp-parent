package sd.cmp.config.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Component
@ConfigurationProperties
@Slf4j
public class DynamicConfig {


	@ApolloConfig
	private Config config;


	@ApolloConfigChangeListener
	private void someOnChange(ConfigChangeEvent changeEvent) {
		changeEvent.changedKeys().forEach(key ->{
			ConfigChange change = changeEvent.getChange(key);
			log.info(String.format("Found JavaConfigSample change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
		});
	}
}
