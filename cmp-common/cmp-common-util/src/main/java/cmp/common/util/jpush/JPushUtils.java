package cmp.common.util.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.WinphoneNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 使用极光推送平台JPush进行推送消息的工具类
 * 
 */
public class JPushUtils {

	protected static final Logger LOG = LoggerFactory.getLogger(JPushUtils.class);

	private static final Integer ErrorCode = 500;// 错误code

	/**
	 * 通过别名推送通知
	 * 
	 * @param masterSecret
	 * @param appKey
	 * @param alias
	 *            别名
	 * @param alert
	 *            通知的内容
	 * @param fromWhere
	 *            用于表示通过什么发出的推送
	 */
	public static JPushResult sendAlertByAlias(String masterSecret, String appKey, String alias, String alert,
											   String fromWhere) {

		JPushResult jPushResult = new JPushResult();

		ClientConfig clientConfig = ClientConfig.getInstance();

		clientConfig.setApnsProduction(false);// 生产环境

		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);

		// 构建推送对象
		PushPayload payload = buildPushObject_all_alias_alert(alias, alert, fromWhere);

		try {
			// 调用极光平台进行推送
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);
			jPushResult.setPushResult(result);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
			jPushResult.setErrorCode(ErrorCode);
			jPushResult.setErrorMessage(e.getMessage());
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
			jPushResult.setErrorCode(e.getErrorCode());
			jPushResult.setErrorMessage(e.getErrorMessage());
		} catch (Exception e) {
			LOG.info("The function sendAlertByAlias() call error!(通过别名推送通知失败！) ", e.getMessage());
			jPushResult.setErrorCode(ErrorCode);
			jPushResult.setErrorMessage(e.getMessage());
		}
		return jPushResult;
	}

	/**
	 * 通过别名推送通知
	 * 
	 * @param masterSecret
	 * @param appKey
	 * @param alias
	 * @param alert
	 * @param title
	 * @return
	 */
	public static JPushResult sendAlertByAlias(String masterSecret, String appKey, String alias, String alert,
											   String title, String fromWhere) {

		JPushResult jPushResult = new JPushResult();

		ClientConfig clientConfig = ClientConfig.getInstance();

		clientConfig.setApnsProduction(false);// 生产环境

		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);

		// 构建推送对象
		PushPayload payload = buildPushObject_all_alias_alert(alias, alert, title, fromWhere);

		try {
			// 调用极光平台进行推送
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);
			jPushResult.setPushResult(result);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
			jPushResult.setErrorCode(ErrorCode);
			jPushResult.setErrorMessage(e.getMessage());
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
			jPushResult.setErrorCode(e.getErrorCode());
			jPushResult.setErrorMessage(e.getErrorMessage());
		} catch (Exception e) {
			LOG.info("The function sendAlertByAlias() call error!(通过别名推送通知失败！) ", e.getMessage());
			jPushResult.setErrorCode(ErrorCode);
			jPushResult.setErrorMessage(e.getMessage());
		}
		return jPushResult;
	}

	/**
	 * 通过别名推送通知
	 * 
	 * @param masterSecret
	 * @param appKey
	 * @param alias
	 * @param message
	 * @param title
	 * @return
	 */
	public static JPushResult sendMessageByAlias(String masterSecret, String appKey, String alias, String message,
												 String title) {

		JPushResult jPushResult = new JPushResult();

		ClientConfig clientConfig = ClientConfig.getInstance();

		clientConfig.setApnsProduction(false);// 生产环境

		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);

		// 构建推送对象
		PushPayload payload = buildPushObject_all_alias_message(alias, message, title);

		try {
			// 调用极光平台进行推送
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);
			jPushResult.setPushResult(result);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
			jPushResult.setErrorCode(ErrorCode);
			jPushResult.setErrorMessage(e.getMessage());
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
			jPushResult.setErrorCode(e.getErrorCode());
			jPushResult.setErrorMessage(e.getErrorMessage());
		} catch (Exception e) {
			LOG.info("The function sendAlertByAlias() call error!(通过别名推送消息失败！) ", e.getMessage());
			jPushResult.setErrorCode(ErrorCode);
			jPushResult.setErrorMessage(e.getMessage());
		}
		return jPushResult;
	}

	/**
	 * 通过别名（集合）的方式推送消息
	 * 
	 * @param masterSecret
	 * @param appKey
	 * @param aliasList
	 *            别名(集合)
	 * @param alert
	 *            内容
	 * @param fromWhere
	 *            用于表示通过什么发出的推送
	 * @return
	 */
	public static JPushResult sendPushByAliasCollections(String masterSecret, String appKey, List<String> aliasList,
														 String alert, String fromWhere) {

		JPushResult jPushResult = new JPushResult();

		ClientConfig clientConfig = ClientConfig.getInstance();

		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);

		// 构建推送对象
		PushPayload payload = buildPushObject_all_aliasCollections_alert(aliasList, alert, fromWhere);

		try {
			// 调用极光平台进行推送
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);
			jPushResult.setPushResult(result);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
			jPushResult.setErrorCode(500);
			jPushResult.setErrorMessage(e.getMessage());
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
			jPushResult.setErrorCode(e.getErrorCode());
			jPushResult.setErrorMessage(e.getErrorMessage());
		}
		return jPushResult;
	}

	/**
	 * 构建推送对象：推送到所有平台、别名为 "alias"的设备
	 * 
	 * @param alert
	 *            通知的内容
	 * @param alias
	 *            设备的别名
	 * @param fromWhere
	 *            用于表示通过什么发出的推送
	 * @return
	 */
	private static PushPayload buildPushObject_all_alias_alert(String alias, String alert, String fromWhere) {

		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
				.setNotification(Notification.newBuilder().setAlert(alert)
						.addPlatformNotification(AndroidNotification.newBuilder().addExtra("from", fromWhere).build())
						.addPlatformNotification(IosNotification.newBuilder().setSound("happy")
								.setContentAvailable(true).addExtra("from", fromWhere).build())
						.addPlatformNotification(WinphoneNotification.newBuilder().build()).build())
				.setOptions(Options.newBuilder()// apns_production: true
												// 表示推送生产环境;false表示要推送开发环境;如果不指定则为推送生产环境。JPush官方默认设置为推送“开发环境”
						.setApnsProduction(false).build())
				.build();
	}

	/**
	 * 构建推送对象：推送到所有平台、别名为 "alias"的设备
	 * 
	 * @param alias
	 *            通知的内容
	 * @param alert
	 *            设备的别名
	 * @param title
	 *            标题
	 * @param fromWhere
	 *            用于表示通过什么发出的推送
	 * @return
	 */
	private static PushPayload buildPushObject_all_alias_alert(String alias, String alert, String title,
															   String fromWhere) {

		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
				.setNotification(Notification.newBuilder().setAlert(alert)
						.addPlatformNotification(AndroidNotification.newBuilder().addExtra("from", fromWhere).build())
						.addPlatformNotification(IosNotification.newBuilder().setSound("happy")
								.setContentAvailable(true).addExtra("from", fromWhere).build())
						.addPlatformNotification(WinphoneNotification.newBuilder().build()).build())
				// .setNotification(Notification.android(alert, title, null))
				.setOptions(Options.newBuilder()// apns_production: true
												// 表示推送生产环境;
						// false表示要推送开发环境;如果不指定则为推送生产环境。JPush官方默认设置为推送“开发环境”
						.setApnsProduction(false).build())
				.build();
	}

	/**
	 * 构建推送对象：推送到所有平台、别名为 "alias"的设备
	 * 
	 * @param alias
	 *            设备的别名
	 * @param message
	 *            消息的内容
	 * @param title
	 *            消息的标题
	 * @return
	 */
	private static PushPayload buildPushObject_all_alias_message(String alias, String message, String title) {

		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
				.setMessage(Message.newBuilder().setMsgContent(message).setTitle(title).build())
				.setOptions(Options.newBuilder()// apns_production: true
												// 表示推送生产环境;
						// false表示要推送开发环境;如果不指定则为推送生产环境。JPush官方默认设置为推送“开发环境”
						.setApnsProduction(false).build())
				.build();
	}

	/**
	 * 构建推送对象：推送到所有平台、别名为 "alias(集合)"的设备
	 * 
	 * @param aliasList
	 *            别名(集合)
	 * @param alert
	 *            内容
	 * @param fromWhere
	 *            用于表示通过什么发出的推送
	 * @return
	 */
	private static PushPayload buildPushObject_all_aliasCollections_alert(List<String> aliasList, String alert,
																		  String fromWhere) {

		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(aliasList))
				.setNotification(Notification.newBuilder().setAlert(alert)
						.addPlatformNotification(AndroidNotification.newBuilder().addExtra("from", fromWhere).build())
						.addPlatformNotification(IosNotification.newBuilder().setSound("happy")
								.setContentAvailable(true).addExtra("from", fromWhere).build())
						.addPlatformNotification(WinphoneNotification.newBuilder().build()).build())
				.build();
	}

}
