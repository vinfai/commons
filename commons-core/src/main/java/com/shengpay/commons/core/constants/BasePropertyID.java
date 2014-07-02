package com.shengpay.commons.core.constants;

/** 
 * 功能: 与数据库表BASE_PROPERTY的ID主键对应  <p> 
 * 用法:
 * @version 1.0
 */ 
public interface BasePropertyID {
	//  select  'Long  ' || replace(upper(bp_name_v),'.','_') || '_ID' || ' = ' || BP_ID_N || 'L;'  || '   // ' ||  BP_DESC_V from BASE_PROPERTY order by bp_id_n
	
	Long  PAYMENT_GATEWAY_URL_ID = 1L;   // 支付网关url
	Long  PAYMENT_GATEWAY_VERSION_ID = 2L;   // 支付网关版本号
	Long  PAYMENT_GATEWAY_MERCHANT_USERID_ID = 3L;   // 商户控台的 用户
	Long  PAYMENT_GATEWAY_MERCHANT_NO_ID = 4L;   // 商户号
	Long  PAYMENT_GATEWAY_PAY_CHANNEL_ID = 5L;   // 支付渠道
	Long  PAYMENT_GATEWAY_DEFAULT_PAY_CHANNEL_ID = 60L;   // 默认支付渠道
	Long  PAYMENT_GATEWAY_POST_BACK_URL_ID = 6L;   // 支付完成后示给终端用户的地址
	Long  PAYMENT_GATEWAY_NOTIFY_URL_ID = 7L;   // 支付完成后服务器端发货通知接口
	Long  PAYMENT_GATEWAY_BACK_URL_ID = 8L;   // 用户取消订单返回或者重新发起订单的地址
	Long  PAYMENT_GATEWAY_CURRENCY_TYPE_ID = 9L;   // 网关支付时候的货币类型
	Long  PAYMENT_GATEWAY_NOTIFY_URL_TYPE_ID = 10L;   // 网关回调的协议类型
	Long  PAYMENT_GATEWAY_SIGN_TYPE_ID = 11L;   // 连接支付网关的协议类型，2表示md5
	Long  PAYMENT_GATEWAY_PUBLIC_KEY_ID = 12L;   // 与支付网关相连的 md5key
	
	/* 邮件发送相关 */
	Long  MAIL_SMTP_USER_ID = 13L;   // 系统邮件的用户名
	Long  MAIL_SMTP_TIMEOUT_ID = 14L;   // 邮件发送的超时时间
	Long  MAIL_SMTP_PORT_ID = 15L;   // 邮件服务器的端口
	Long  MAIL_SMTP_PASSWORD_ID = 16L;   // 邮件服务器密码
	Long  MAIL_SMTP_HOST_ID = 17L;   // 邮件服务器的地址
	Long  MAIL_SMTP_FROM_ID = 18L;   // 系统邮件的发件人
	Long  MAIL_SMTP_CONNECTIONTIMEOUT_ID = 19L;   // 连接邮件服务器的等待时间
	Long  MAIL_SMTP_AUTH_ID = 20L;   // 邮件服务器是否需要认证
	
	/* 短信发送相关 */
	Long  SMS_URL_ID = 21L;   // 短信接口地址
	Long  SMS_PWD_VALUE_ID = 22L;   // 短信接口参数值
	Long  SMS_PWD_NAME_ID = 23L;   // 短信接口指定参数名称
	Long  SMS_PID_VALUE_ID = 24L;   // 短信接口参数值
	Long  SMS_PID_NAME_ID = 25L;   // 短信接口指定参数名称
	Long  SMS_PHONE_NAME_ID = 26L;   // 短信接口指定参数名称
	Long  SMS_MSG_NAME_ID = 27L;   // 短信接口指定参数名称
	Long  SMS_MSG_MAXLENGTH_ID = 28L;   // 短信的长度
	Long  SMS_CPID_VALUE_ID = 29L;   // 短信接口参数值
	Long  SMS_CPID_NAME_ID = 30L;   // 短信接口指定参数名称
	
	/*CAS接入相关*/
	Long CAS_RELATED_CONFIG = 1100L;	// CAS接入相关配置
	Long CAS_LOGIN_FRAME_URL = 1101L;	// CAS frame登录url
	Long CAS_TICKET_VALIDATE_URL = 1102L;	// cas验证ticket url
	Long CAS_TICKET_VALIDATE_SERVICE_URL = 1103L;	// cas验证ticket中service参数 url
	Long CAS_UD_FIND_MAIL_URL = 1104L;	// 查询用户邮箱url
	Long CAS_UD_FIND_PHONE_URL = 1105L;	// 查询用户手机url
	Long CAS_ID_FIND_CONVERT_ID_URL = 1106L;	// loginName查询转换url
	Long CAS_LOGIN_PAGE_URL = 1107L;			// CAS登录页面url
	Long CAS_LOGOUT_URL = 1108L;		// CAS登出url
	Long CAS_REGISTER_URL = 1109L;		// CAS注册url
	Long CAS_REGISTER_ZONE = 1110L;		// CAS注册区域来源
	Long CAS_REGISTER_CSS_ID = 1111L;		// CAS注册样式名称
	
	
	/*退款接入相关*/
	Long REFUND_RELATED_CONFIG = 1200L;	// 退款相关设置值
	Long REFUND_VERSION = 1201L;	// 退款版本号
	Long REFUND_NOTIFY_URL = 1202L;	// 退款成功通知地址
	Long REFUND_NOTIFY_URL_TYPE = 1203L;	// 退款成功通知方式
	Long REFUND_MD5_KEY = 1204L;	// 退款md5key
	Long REFUND_SIGN_TYPE = 1205L;	// 退款验签类型
	Long REFUND_REQUEST_URL = 1206L;	// 退款请求地址
	Long REFUND_RETRY_TIMES = 1207L;	// 退款请求重试次数
	
	Long GATHER_MAX_AMOUNT = 1400l;		//收款人当月最大收款金额
	/*APP ID*/
	Long APP_ID = 8888L;	// 项目APPID
    
	Long  FUNDOUT_APPID = 31L;	// 出款appId
	Long  FUNDOUT_PTID = 32L;	// 出款时付款账号
	Long  FUNDOUT_INTERFACETYPE = 33L;	// 出款接口类型
	
	
	Long  MAIL_SMTP_FROMNAME_ID = 34L;   // 系统邮件发件人显示名称
	Long FUNDOUT_URL = 35L;		//出款url
	Long FUNDOUT_RETRYTIMES = 36L; //出款失败重试次数
	
	/*创建付款商品相关*/
	Long MAIL_GBMANAGE_PAYURL = 37L;//创建收款成功后，生成付款商品URL
	
	Long ORDER_QUERY_URL = 39L;	//订单查询URL
	
	
	/*充值,欧飞相关*/
	Long TOPUP_OF_USERID = 40L;          //商户号
	Long TOPUP_OF_USERPWD = 41L;         //密码
	Long TOPUP_OF_CARDID_CMCU = 42L;     //商品编码(联通移动)
	Long TOPUP_OF_CARDID_CT = 43L;		 //商品编码(电信)
	Long TOPUP_OF_RETURL = 44L;          //充值完成回调URL
	Long TOPUP_OF_VERSION = 45L;         //版本号
	Long TOPUP_OF_KEY = 46L;             //密钥
	Long TOPUP_OF_RECHARGEURL = 47L;     //充值请求URL
	Long TOPUP_OF_QRYURL = 48L;          //查询请求URL
	Long TOPUP_OF_CHECKURL = 49L;        //查询是否可以充值URL
	
	/*批量对账相关*/
	Long RECON_BATCH_QUERY_MAXROW = 50L;          // 批量对账最多返回多少条数据
	Long RECON_REPORT_TO = 51L;          // 批量对账报告收件人
	Long  RECON_TIME_BEGIN_ID = 52L;   // 批量对账从当前时间往前N个小时，作为开始时间
	Long  RECON_TIME_END_ID = 53L;   // 批量对账从当前时间往前N个小时，作为结束时间
	
	/*http连接超时相关*/
	Long HTTP_CONNECT_TIMEOUT = 54L;     //http连接超时(秒为单位)
	Long HTTP_REQ_TIMEOUT = 55L;         //http请求超时(秒为单位)
	
}
