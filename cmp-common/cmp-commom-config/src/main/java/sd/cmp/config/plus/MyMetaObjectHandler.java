package sd.cmp.config.plus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		final Object createTime = getFieldValByName("createTime", metaObject);
		final Object modifyTime = getFieldValByName("updateTime", metaObject);
		//final Object isDeleted = getFieldValByName("isDeleted", metaObject);

		if (null==createTime){
			metaObject.setValue("createTime",LocalDateTime.now());
		}

		if (null==modifyTime){
			metaObject.setValue("updateTime",LocalDateTime.now());
		}

	}

	@Override
	public void updateFill(MetaObject metaObject) {
			setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
	}
}
