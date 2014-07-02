/**
 * 
 */
package com.shengpay.commons.core.propertyedit;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.shengpay.commons.core.utils.StringUtils;

/**
 * 属性编辑器:将String转换为List<String>
 * @description
 * @usage
 */

public class StrListPropertyEditor extends PropertyEditorSupport {

	/* (non-Javadoc)
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String str) throws IllegalArgumentException {
		if (StringUtils.isBlank(str)) {
			setValue(null);
			return;
		}

		StringTokenizer st=new StringTokenizer(str.trim(), ",");
		List<String> list=new ArrayList<String>();
		while(st.hasMoreTokens()) {
			list.add(st.nextToken().trim());
		}
		setValue(list);
	}

}
