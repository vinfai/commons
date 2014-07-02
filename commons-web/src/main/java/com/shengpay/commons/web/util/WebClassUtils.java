package com.shengpay.commons.web.util;

import java.lang.reflect.Field;
import java.util.Date;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.core.utils.DateTimeUtils;
import com.shengpay.commons.core.utils.StringUtils;
import com.shengpay.commons.web.protocol.ProtocolFieldAnn;

public class WebClassUtils {

	/**
	 * 转化Object类型为String类型
	 * 
	 * @param objValue
	 * @param field
	 * @return
	 * @throws BusinessException
	 */
	public static String convertObject2String(Object objValue, Field field) throws BusinessException {
		if (objValue == null) {
			return "";
		}

		if (objValue instanceof Date) {
			ProtocolFieldAnn dateFormatAnn = field.getAnnotation(ProtocolFieldAnn.class);
			if (dateFormatAnn == null) {
				throw new SystemException("转换字符串为日期类型时无法取得日期格式信息！");
			}

			String dateFormat = dateFormatAnn.dateFormat();
			return DateTimeUtils.formateDate2Str((Date) objValue, dateFormat);

		}

		return String.valueOf(objValue);
	}

	/**
	 * 将字符串值转换为指定域所属类型
	 * 
	 * @param strValue
	 * @param field
	 * @return
	 * @throws BusinessException
	 */
	public static Object convertString2Obj(String strValue, Field field) throws BusinessException {
		if (StringUtils.isBlank(strValue)) {
			return null;
		}

		Class<?> fieldType = field.getType();
		if (fieldType == Date.class) {
			ProtocolFieldAnn dateFormatAnn = field.getAnnotation(ProtocolFieldAnn.class);
			if (dateFormatAnn == null) {
				throw new SystemException("转换字符串为日期类型时无法取得日期格式信息！");
			}

			String dateFormat = dateFormatAnn.dateFormat();
			return DateTimeUtils.parseDateByString(strValue, dateFormat);
		}

		return ClassUtils.convertString2Obj(strValue, fieldType);
	}

	/**
	 * 将字符串值设置到特定对象的特定属性中
	 * 
	 * @param aimObj
	 *            目标对象
	 * @param field
	 *            目标属性
	 * @param fieldValue
	 *            被设置的值
	 * @throws BusinessException
	 */
	public static void setStringValueToField(Object aimObj, Field field, String fieldValue) throws BusinessException {
		Object filedValue = convertString2Obj(fieldValue, field);
		ClassUtils.setObjectValueToField(aimObj, field, filedValue);
	}

}
