/**
 * 
 */
package com.shengpay.commons.core.valueobject;

import java.util.ArrayList;
import java.util.List;

import com.shengpay.commons.core.exception.SystemException;

/**
 * @author lindongcheng
 * 
 */
public class RandomNumberBuilder {

	private List<Long> valueList;

	public RandomNumberBuilder(long maxValue) {
		this(0, maxValue);
	}

	public RandomNumberBuilder(long minValue, long maxValue) {
		// 判断参数条件
		if (maxValue < minValue) {
			throw new SystemException("取随机数时，结束值【" + maxValue + "】不能小于开始值【" + minValue + "】！");
		}

		valueList = makeValueList(minValue, maxValue);
	}

	private List<Long> makeValueList(long minValue, long maxValue) {
		List<Long> valueList = new ArrayList<Long>();

		for (long i = minValue; i <= maxValue; i++) {
			valueList.add(i);
		}

		return valueList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.bgw.commons.valueobject.RandomNumberBuilder#getRandomNumber
	 * (int, int, java.util.List)
	 */
	public Long nextRandomNumber() {
		int size = valueList.size();
		if (size == 0) {
			return null;
		}

		// 基于随机数长度生成一个随机索引
		int index = getRandomIndex(size);

		// 返回指定索引的可选值
		Long integer = valueList.get(index);
		valueList.remove(index);
		return integer;
	}

	private int getRandomIndex(int arrLength) {
		return (int) (Math.random() * arrLength);
	}

}
