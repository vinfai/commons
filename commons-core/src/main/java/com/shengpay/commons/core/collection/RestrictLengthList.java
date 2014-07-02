package com.shengpay.commons.core.collection;

import java.util.ArrayList;
import java.util.Collection;


/**
 * 
 * @description
 * @usage
 */
@SuppressWarnings("serial")
public class RestrictLengthList<T> extends ArrayList<T> {

	/**
	 * 列表的最大长度
	 */
	private final int maxLength;

	/**
	 * @param maxLength 列表最大长度
	 */
	public RestrictLengthList(int maxLength) {
		this.maxLength = maxLength;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(T o) {
		boolean addSucc = super.add(o);
		validateLength();
		return addSucc;
	}

	/**
	 * 
	 */
	private void validateLength() {
		for (int i = size(); i > maxLength; i--) {
			remove(0);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, T element) {
		super.add(index, element);
		validateLength();
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean addSucc = super.addAll(c);
		validateLength();
		return addSucc;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		boolean addSucc = super.addAll(index, c);
		validateLength();
		return addSucc;
	}
	
}
