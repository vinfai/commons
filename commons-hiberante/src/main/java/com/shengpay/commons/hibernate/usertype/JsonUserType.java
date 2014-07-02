/**
 * 
 */
package com.shengpay.commons.hibernate.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * @author lindongcheng
 *
 */
public class JsonUserType implements UserType,ParameterizedType {
	
	private static final String PARAM_STATUS_TYPE = "type";
	private Class<?> type;

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	@Override
	public int[] sqlTypes() {
		return new int[] {StandardBasicTypes.STRING.sqlType()};
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	@Override
	public Class<?> returnedClass() {
		return type;
	}
	
	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if(x==y) return true;
		if(x==null || y==null) return false;
		return x.equals(y);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		return JsonUtils.fromJson(type,rs.getString(names[0]));
	}

//	protected abstract SwitchStatusFactory getSwitchStatusFactory();

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		st.setString(index, JsonUtils.toJson(value));
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	@Override
	public boolean isMutable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
	 */
	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public void setParameterValues(Properties parameters) {
		String className = parameters.getProperty(PARAM_STATUS_TYPE);
		if(StringUtils.isBlank(className)) {
			throw new SystemException("请为【"+JsonUserType.class+"】设置参数【type】的值为目标状态类型！");
		}
		
		try {
			type=Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new SystemException("无法找到状态类型信息【"+className+"】",e);
		}
	}

}
