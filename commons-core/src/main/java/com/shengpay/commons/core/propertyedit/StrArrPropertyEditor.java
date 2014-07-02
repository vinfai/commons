/**
 * 
 */
package com.shengpay.commons.core.propertyedit;

import java.beans.PropertyEditorSupport;

import com.shengpay.commons.core.utils.StringUtils;

/**
 * 属性编辑器:将String转换为String数组
 * @description
 * @usage
 */

public class StrArrPropertyEditor extends PropertyEditorSupport {

	/* (non-Javadoc)
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String str) throws IllegalArgumentException {
		if (StringUtils.isBlank(str)) {
			setValue(null);
			return;
		}

		setValue(str.split(","));
	}

}
