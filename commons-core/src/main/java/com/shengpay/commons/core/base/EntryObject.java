/**
 * 
 */
package com.shengpay.commons.core.base;

/**
 * @author lindongcheng
 *
 */
public class EntryObject<KeyType,ValueType> {
	/**
	 * 
	 */
	private KeyType key;
	
	private ValueType value;
	
	public EntryObject(KeyType key,ValueType value){
		this.key=key;
		this.value=value;
	}

	public KeyType getKey() {
		return key;
	}

	public void setKey(KeyType key) {
		this.key = key;
	}

	public ValueType getValue() {
		return value;
	}

	public void setValue(ValueType value) {
		this.value = value;
	}
}
