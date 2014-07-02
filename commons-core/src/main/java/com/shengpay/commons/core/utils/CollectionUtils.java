package com.shengpay.commons.core.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.shengpay.commons.core.exception.SystemException;

/**
 * 数组/列表工具类;
 */
public class CollectionUtils {

	public static interface GetKey<T> {
		String getKey(T obj);
	}

	/**
	 * 将列表转换为数组;
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] listToArray(List<T> list) {
		if (list == null || list.size() == 0) {
			return null;
		}

		T[] tArr = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
		return list.toArray(tArr);

	}

	public static <S, T> List<T> map(List<S> list, Maper<S, T> maper) {
		List<T> maped = new ArrayList<T>();
		for (S s : list) {
			maped.add(maper.map(s));
		}
		return maped;
	}

	public static <T> List<T> filter(List<T> list, Filter<T> filter) {
		for (Iterator<T> iterator = list.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			if (!filter.filter(t))
				iterator.remove();
		}
		return list;
	}


	/**
	 * 将列表转换为数组;
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<T> arrayToList(T[] array) {
		if (array == null) {
			return null;
		}

		return Arrays.asList(array);
	}

	/**
	 * 将列表转换为数组;
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> Set<T> arrayToSet(T[] array) {
		if (array == null) {
			return new HashSet<T>();
		}
		Set<T> set = new HashSet<T>();
		for (T t : array) {
			set.add(t);
		}
		return set;
	}

	public static <T> List<List<T>> splitList(List<T> srcList, int maxSize) {
		List<List<T>> splitList = new ArrayList<List<T>>();
		if (srcList == null || srcList.size() == 0) {
			return splitList;
		}

		int listSize = srcList.size();
		int splitListSize = (listSize / maxSize) + (listSize % maxSize > 0 ? 1 : 0);
		for (int i = 0; i < splitListSize; i++) {
			int beginIndex = i * maxSize;
			int endIndex = (i + 1) * maxSize;
			endIndex = endIndex > listSize ? listSize : endIndex;
			splitList.add(srcList.subList(beginIndex, endIndex));
		}
		return splitList;
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= 131; i++) {
			list.add(i);
		}

		List<List<Integer>> splitList = splitList(list, 7);
		int i = 1;
		for (List<?> list2 : splitList) {
			System.out.print(i + ":\t");
			for (Object object : list2) {
				System.out.print(object + "\t");
			}
			System.out.println();
			i++;
		}
	}

	/**
	 * 截取直接数组
	 * 
	 * @param srcByteArr
	 * @param index
	 * @param length
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] subArr(T[] srcByteArr, int index, int length) {
		// 验证参数合法性
		if (srcByteArr == null || index < 0 || length < 0 || srcByteArr.length < index + length) {
			throw new SystemException("参数不合法:subByteArr(T[] " + srcByteArr + ", int " + index + ", int " + length + ")");
		}

		// 构造新数据并拷贝
		T[] tArr = (T[]) Array.newInstance(srcByteArr[0].getClass(), length);
		System.arraycopy(srcByteArr, index, tArr, 0, length);
		return tArr;
	}

	/**
	 * 截取直接数组
	 * 
	 * @param srcByteArr
	 * @param index
	 * @param length
	 * @return
	 */
	public static byte[] subArr(byte[] srcByteArr, int index, int length) {
		// 验证参数合法性
		if (srcByteArr == null || index < 0 || length < 0 || srcByteArr.length < index + length) {
			throw new SystemException("参数不合法:subByteArr(T[] " + srcByteArr + ", int " + index + ", int " + length + ")");
		}

		// 构造新数据并拷贝
		byte[] tArr = new byte[length];
		System.arraycopy(srcByteArr, index, tArr, 0, length);
		return tArr;
	}

	/**
	 * 转换数组:String[] -> Long[]
	 * 
	 * @param strArr
	 * @return
	 */
	public static Long[] convertArray(String[] strArr) {
		if (strArr == null) {
			return null;
		}

		Long[] longArr = new Long[strArr.length];
		for (int i = 0; i < longArr.length; i++) {
			if (!NumberUtils.isLong(strArr[i])) {
				throw new SystemException("执行字符串数组到长整型数组转换过程中,发现字符串数组存在非长整型字符串[索引:" + i + " 值:" + strArr[i] + "]");
			}

			longArr[i] = Long.parseLong(strArr[i]);
		}
		return longArr;
	}

	/**
	 * 将数组转换为逗号分隔的字符串
	 * 
	 * @param objArr
	 * @return
	 */
	public static String arrayToString(Object[] objArr) {
		if (objArr == null || objArr.length == 0) {
			return null;
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < objArr.length; i++) {
			buf.append((i > 0 ? "," : "") + objArr[i]);
		}
		return buf.toString();
	}

	/**
	 * 将逗号分隔的字符串转换为数组
	 * 
	 * @param str
	 * @return
	 */
	public static String[] stringToArray(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}

		String[] split = str.split(",");
		return split;
	}

	/**
	 * 将一个字符串使用分隔符分割后转换为数组
	 * 
	 * @param str
	 * @param splitChar
	 * @return
	 */
	public static List<String> splitStringToList(String str, String splitChar) {
		if (StringUtils.isBlank(str)) {
			return null;
		}

		return arrayToList(str.split(splitChar));
	}

	/**
	 * 将一个字符串使用分隔符分割后转换为数组
	 * 
	 * @param str
	 * @param splitChar
	 * @return
	 */
	public static List<Integer> splitStringToIntegerList(String str, String splitChar) {
		if (StringUtils.isBlank(str)) {
			return null;
		}

		String[] split = str.split(splitChar);
		List<Integer> list = new ArrayList<Integer>();
		for (String string : split) {
			list.add(Integer.valueOf(string));
		}
		return list;
	}

	/**
	 * 判断一个列表是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
		return list == null || list.size() == 0;
	}

	/**
	 * 判断一个列表是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(String[] list) {
		return list == null || list.length == 0;
	}

	/**
	 * 将Map<String,String[]>转换为Map<String,String>
	 * 
	 * @param srcMap
	 * @return
	 */
	public static Map<String, String> convertMap(Map<String, String[]> srcMap) {
		Map<String, String> aimMap = new HashMap<String, String>();
		for (Entry<String, String[]> entry : srcMap.entrySet()) {
			String[] srcValue = entry.getValue();
			String aimValue = srcValue != null && srcValue.length > 0 ? srcValue[0] : null;
			aimMap.put(entry.getKey(), aimValue);
		}
		return aimMap;
	}

	/**
	 * 转换properties为map
	 * 
	 * @param properties
	 * @return
	 */
	public static Map<String, String> convertProperties2Map(Properties properties) {
		if (properties == null) {
			return null;
		}

		Map<String, String> map = new LinkedHashMap<String, String>();
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}

		return map;
	}

	/**
	 * 判断两个集合是否包含重复项
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static boolean isOverlap(Collection<?> list1, Collection<?> list2) {
		if (list1 == list2) {
			return true;
		}

		if (list1 == null || list2 == null) {
			return false;
		}

		for (Object element : list1) {
			if (list2.contains(element)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 将字符串列表内容都改为小写
	 * 
	 * @param srcList
	 * @return
	 */
	public static List<String> toLowerCase(List<String> srcList) {
		if (srcList == null) {
			return null;
		}

		List<String> aimList = new ArrayList<String>();
		for (String string : srcList) {
			aimList.add(StringUtils.toLowerCase(string));
		}
		return aimList;
	}

	/**
	 * 将字符串列表内容都改为小写
	 * 
	 * @param srcList
	 * @return
	 */
	public static Set<String> toLowerCase(Set<String> srcList) {
		if (srcList == null) {
			return null;
		}

		Set<String> aimList = new HashSet<String>();
		for (String string : srcList) {
			aimList.add(StringUtils.toLowerCase(string));
		}
		return aimList;
	}

	public static <T> Map<String, List<T>> convert2Map(List<T> valueList, GetKey<T> getKey) {
		Map<String, List<T>> map = new HashMap<String, List<T>>();
		for (T value : valueList) {
			String key = getKey.getKey(value);
			List<T> mapedValueList = map.get(key);
			if (mapedValueList == null) {
				mapedValueList = new ArrayList<T>();
				map.put(key, mapedValueList);
			}
			mapedValueList.add(value);
		}
		return map;
	}

}
