/**
 * 
 */
package com.shengpay.commons.web.protocol;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.shengpay.commons.core.cer.CerHandler;
import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.core.utils.CollectionUtils;
import com.shengpay.commons.core.utils.StringUtils;
import com.shengpay.commons.web.util.HttpUtils;
import com.shengpay.commons.web.util.WebClassUtils;

/**
 * 在线支付协议解析服务
 * @description
 * @author Lincoln
 */

public class ProtocolSignServiceImpl implements ProtocolSignService {

	/**
	 * 签名字段参数名称
	 */
	String signParamName;

	/**
	 * 数字证书处理器
	 */
	CerHandler cerHandler;

	/**
	 * 构造登记在线支付请求参数信息
	 * 
	 * @param requestObj
	 * @param keyPassword
	 *            请求扣款客户端私钥密码
	 * @param keyAlias
	 *            请求扣款的客户端名称
	 * @return
	 * @throws BusinessException
	 */
	public String sign(Object requestObj, String keyAlias, String keyPassword, String requestUrl, String charset) throws BusinessException {
		//从URL中取得附加参数信息
		Map<String,String> attachParamMap=HttpUtils.getAttachParamMapByUrl(requestUrl);
		
		//取得签名信息
		Map<String, String> signParamsMap = sign(requestObj, keyAlias, keyPassword,attachParamMap);

		//截取无参数信息的url
		String urlExcludeParams = HttpUtils.getUrlExcludeParams(requestUrl);
		
		// 返回请求映射信息
		return HttpUtils.makeHttpRequestURL(urlExcludeParams, signParamsMap, charset);
	}

	/* (non-Javadoc)
	 * @see com.mypay.commons.protocol.ProtocolSignService#sign(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public Map<String, String> sign(Object requestObj, String keyAlias, String keyPassword) throws BusinessException {
		return sign(requestObj, keyAlias, keyPassword, null);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mypay.commons.protocol.ProtocolSignService#sign(java.lang.Object,
	 * java.lang.String, java.lang.String)
	 */
	public Map<String, String> sign(Object requestObj, String keyAlias, String keyPassword,Map<String,String> attachParamMap) throws BusinessException {
		// 验证参数合法性
		if (requestObj == null) {
			throw new SystemException("参数不合法[makeCheckinOnlinePaymentRequest(Object " + requestObj + ")]");
		}

		// 获取指定对象的《域名：域值》映射
		Map<String, String> paramMap = getFieldMapByObj(requestObj, null, -1);

		//添加附加参数到参数映射中
		addAttachParams(attachParamMap, paramMap);

		// 向参数映射中添加签名信息
		String signParamValue = getSign(paramMap, keyAlias, keyPassword);
		paramMap.put(signParamName, signParamValue);

		// 返回签名映射
		return paramMap;
	}

	/**
	 * 添加附加参数到参数映射中
	 * @param attachParamMap 
	 * @param paramMap
	 */
	private void addAttachParams(Map<String, String> attachParamMap, Map<String, String> paramMap) {
		//空直接返回
		if(attachParamMap==null || attachParamMap.size()==0){
			return;
		}
		
		//检查是否存在重复
		for(String key:attachParamMap.keySet()){
			if(StringUtils.notBlank(paramMap.get(key))){
				throw new SystemException("附加参数【"+attachParamMap+"】中包含了与协议对象【"+paramMap+"】属性名相同的键值对！");
			}
		}
		
		//添加附加参数到参数映射中
		paramMap.putAll(attachParamMap);
	}

	/**
	 * 取得指定参数映射的签名信息
	 * 
	 * @param paramMap
	 *            参数映射
	 * @param keyAlias
	 *            用于签名的私钥的别名
	 * @param keyPassword
	 *            用于签名的私钥的密码
	 * @return
	 * @throws BusinessException
	 */
	private String getSign(Map<String, String> paramMap, String keyAlias, String keyPassword) throws BusinessException {
		// 将编码前的值设置到签名串中
		String strBuf = getSignBeforeStr(paramMap);
		return cerHandler.createCerSign(keyAlias, keyPassword, strBuf);
	}

	/**
	 * 取得参数映射签名前的字符串形式(例如：a=1&b=2)
	 * 
	 * @param paramMap
	 * @return
	 */
	private String getSignBeforeStr(Map<String, String> paramMap) {
		List<String> keyList = new ArrayList<String>(paramMap.keySet());
		Collections.sort(keyList);

		StringBuffer strBuf = new StringBuffer();
		for (String paramName : keyList) {
			// 排除签名字段和字符集类型字段
			if (signParamName.equals(paramName)) {
				continue;
			}

			String paramValue = paramMap.get(paramName);
			strBuf.append((strBuf.length() > 0 ? "&" : "") + paramName + "=" + (StringUtils.notBlank(paramValue) ? paramValue : ""));
		}
		return strBuf.toString();
	}

	/**
	 * 获取指定对象的<属性名:属性值>映射
	 * 
	 * @param requestObj
	 *            被取值的对象
	 * @param fieldNameInParse
	 *            当前对象在父对象中的属性信息
	 * @param indexInList
	 *            当前对象作为父对象列表属性的值时的索引（-1表示不是列表中的值）
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, String> getFieldMapByObj(Object requestObj, Field parseField, int indexInList) throws BusinessException {
		// 循环将请求参数名称及其对应的值存放到映射中
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		for (Field aField : requestObj.getClass().getDeclaredFields()) {
			// 排除常量
			if (Modifier.isFinal(aField.getModifiers()) && Modifier.isStatic(aField.getModifiers())) {
				continue;
			}

			Object fieldObjValue = ClassUtils.getFieldValue(requestObj, aField);
			// 判断域的类型是不是List
			if (fieldObjValue instanceof List<?>) {
				List<?> fieldListValue = (List<?>) fieldObjValue;
				int i = 1;
				for (Iterator<?> iterator = fieldListValue.iterator(); iterator.hasNext(); i++) {
					Object listVal = (Object) iterator.next();
					Map<String, String> clientObjFieldMap = getFieldMapByObj(listVal, aField, i);
					addAttachParams(clientObjFieldMap, paramMap);
				}

				continue;
			}

			// 取得字段名和字段值
			String fieldName = getFieldName(parseField, indexInList, aField);
			String fieldValue = WebClassUtils.convertObject2String(fieldObjValue, aField);

			paramMap.put(fieldName, fieldValue);
		}
		return paramMap;
	}

	/**
	 * 取得作为参数名的属性名
	 * 
	 * @param fieldNameInParse
	 * @param indexInList
	 * @param aField
	 * @return
	 */
	private String getFieldName(Field parseField, int indexInList, Field aField) {
		String fieldName = getFileParamName(aField);
		if (parseField != null) {
			fieldName = getFileParamName(parseField) + "_" + fieldName;
		}

		if (indexInList >= 0) {
			fieldName = fieldName + "_" + indexInList;
		}

		return fieldName;
	}

	/**
	 * 取得指定属性的参数名
	 * 
	 * @param field
	 * @return
	 */
	private String getFileParamName(Field field) {
		ProtocolFieldAnn protocolFieldAnn = field.getAnnotation(ProtocolFieldAnn.class);
		if (protocolFieldAnn != null && StringUtils.notBlank(protocolFieldAnn.paramName())) {
			return protocolFieldAnn.paramName();
		}

		return field.getName();
	}

	/**
	 * 对类的各个属性按照字符顺序进行排序
	 * 
	 * @param declaredFields
	 * @return
	 */
	Collection<Field> sortFields(Field[] declaredFields) {
		TreeMap<String, Field> fieldMap = new TreeMap<String, Field>();
		for (Field aField : declaredFields) {
			fieldMap.put(getFileParamName(aField), aField);
		}
		Collection<Field> sortedFieldList = fieldMap.values();
		return sortedFieldList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mypay.commons.protocol.ProtocolSignService#parse(javax.servlet.http
	 * .HttpServletRequest, java.lang.Class)
	 */
	public <ProtocolClass> ProtocolClass parseByAliasParamName(HttpServletRequest request, Class<ProtocolClass> aimClass, String aliasParamName) throws BusinessException {
		return parseByAliasName(request, aimClass, request.getParameter(aliasParamName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.estock.onlinepayment.protocolparse.OnlinePaymentProtocolParseService
	 * #parseCheckinOnlinePayment(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	public <ProtocolClass> ProtocolClass parseByAliasName(HttpServletRequest request, Class<ProtocolClass> aimClass, String cerAlias) throws BusinessException {
		// 验证参数合法性
		if (request == null) {
			throw new SystemException("参数不合法[parseCheckinOnlinePayment(HttpServletRequest " + request + ")]");
		}

		if (StringUtils.isBlank(cerAlias)) {
			throw new BusinessException("bc.onlinepayment.lincoln.10");
		}

		// 验证数字签名
		Map<String, String> paramMap = CollectionUtils.convertMap(request.getParameterMap());
		String signBeforeStr = getSignBeforeStr(paramMap);
		String signParamValue = request.getParameter(signParamName);
		if (!cerHandler.validateCerSign(cerAlias, signBeforeStr, signParamValue)) {
			throw new BusinessException("bc.onlinepayment.lincoln.1");
		}

		// 构造协议解析目标对象
		ProtocolClass protocolAimObj = getProtocolObj(aimClass);
		for (Field aField : aimClass.getDeclaredFields()) {
			// 排除常量
			if (Modifier.isFinal(aField.getModifiers()) && Modifier.isStatic(aField.getModifiers())) {
				continue;
			}

			// 设置对象各个属性的值
			Class<?> fieldType = aField.getType();
			if (fieldType == List.class) {
				@SuppressWarnings("rawtypes")
				List listFieldValue = getListFieldValue(aField, paramMap);
				ClassUtils.setObjectValueToField(protocolAimObj, aField, listFieldValue);
			} else {
				String fieldValue = paramMap.get(getFileParamName(aField));
				WebClassUtils.setStringValueToField(protocolAimObj, aField, fieldValue);
			}
		}

		// 返回解析结果
		return protocolAimObj;
	}

	/**
	 * 取得指定属性的泛型类型的对象
	 * 
	 * @param aField
	 * @param paramMap
	 * @throws BusinessException
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	private List getListFieldValue(Field aField, Map<String, String> paramMap) throws BusinessException {

		List listFieldVal = new ArrayList();

		// 取得属性的泛型信息
		Class<?> fieldActualType = ClassUtils.getFieldActualType(aField);

		boolean hasValue = true;
		for (int i = 1; hasValue; i++) {

			Object aClientObj = null;
			try {
				aClientObj = fieldActualType.newInstance();
			} catch (Exception e) {
				throw new SystemException("实例化【" + fieldActualType + "】时发生异常：", e);
			}

			hasValue = false;

			for (Field clientField : fieldActualType.getDeclaredFields()) {
				String paramName = getFieldName(aField, i, clientField);

				String paramValue = paramMap.get(paramName);

				if (StringUtils.notBlank(paramValue)) {
					WebClassUtils.setStringValueToField(aClientObj, clientField, paramValue);
					hasValue = true;
				}
			}

			if (hasValue) {
				listFieldVal.add(aClientObj);
			}
		}

		return listFieldVal;
	}

	/**
	 * 构造一个协议对象
	 * 
	 * @param <ProtocolClass>
	 * @param aimClass
	 * @return
	 */
	private <ProtocolClass> ProtocolClass getProtocolObj(Class<ProtocolClass> aimClass) {
		ProtocolClass protocolAimObj = null;
		try {
			protocolAimObj = aimClass.newInstance();
		} catch (Exception e) {
			throw new SystemException("构造协议目标类[" + aimClass + "]的对象时发生异常:", e);
		}
		return protocolAimObj;
	}

	/**
	 * 取得协议类型拥有的参数映射
	 * 
	 * @param <ProtocolClass>
	 * @param aimClass
	 * @return
	 */
	<ProtocolClass> Map<String, Field> getFieldMap(Class<ProtocolClass> aimClass) {
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		Field[] declaredFields = aimClass.getDeclaredFields();
		for (Field field : declaredFields) {
			fieldMap.put(getFileParamName(field), field);
		}
		return fieldMap;
	}

	public String getSignParamName() {
		return signParamName;
	}

	public void setSignParamName(String signParamName) {
		this.signParamName = signParamName;
	}

	public CerHandler getCerHandler() {
		return cerHandler;
	}

	public void setCerHandler(CerHandler cerHandler) {
		this.cerHandler = cerHandler;
	}

	public static void main1(String[] args) {
		String url="http://www.163.com?a=1&b=2&c=3";
		System.out.println(HttpUtils.getAttachParamMapByUrl(url));
	}

	public static void main(String[] args) {
		Set<String> classSetByPackageName = ClassUtils.getClassSetByPackageName("",".properties");
		for (String string : classSetByPackageName) {
			System.out.println(string);
		}
	}

}
