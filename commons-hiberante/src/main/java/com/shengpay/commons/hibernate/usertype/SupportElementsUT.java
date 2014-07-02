/**
 * 
 */
package com.shengpay.commons.hibernate.usertype;

import com.shengpay.commons.core.utils.StringUtils;
import com.shengpay.commons.core.valueobject.SupportElements;
import com.shengpay.commons.core.valueobject.SupportElementsFactory;
import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lindongcheng
 *
 */
public class SupportElementsUT implements UserType {
	

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
		return SupportElements.class;
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
		String string = rs.getString(names[0]);
		if(StringUtils.isBlank(string)) {
			return null;
		}
		
		return SupportElementsFactory.getInstance().makeByExpression(string);
	}


	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		st.setString(index, getValue(value));
	}

	private String getValue(Object value) {
		if(value==null || !(value instanceof SupportElements)) {
			return "";
		}
		
		return SupportElementsFactory.getInstance().getExpression((SupportElements)value);
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

}
