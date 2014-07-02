package com.sdo.transbutton.common.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sdo.transbutton.common.exception.SystemException;


/**
 * 数组/列表工具类;
 */
public class CollectionUtils {

	/**
	 * 将列表转换为数组;
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] listToArray(List<T> list) {
		if (list == null) {
			return null;
		}

		T[] tArr = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
		return list.toArray(tArr);

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
	 * 截取直接数组
	 * 
	 * @param srcByteArr
	 * @param index
	 * @param length
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] subArr(T[] srcByteArr, int index, int length) {
		//验证参数合法性
		if (srcByteArr == null || index < 0 || length < 0 || srcByteArr.length < index + length) {
			throw new SystemException("参数不合法:subByteArr(T[] " + srcByteArr + ", int " + index + ", int " + length + ")");
		}

		//构造新数据并拷贝
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
		//验证参数合法性
		if (srcByteArr == null || index < 0 || length < 0 || srcByteArr.length < index + length) {
			throw new SystemException("参数不合法:subByteArr(T[] " + srcByteArr + ", int " + index + ", int " + length + ")");
		}

		//构造新数据并拷贝
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
	 * @param str
	 * @param splitChar
	 * @return
	 */
	public static List<String> splitStringToList(String str,String splitChar){
		if(StringUtils.isBlank(str)){
			return null;
		}
		
		return arrayToList(str.split(splitChar));
	}

	public static void main(String[] args) {
		String arrayToString = arrayToString(new String[] { "1", "2", "3" });
		System.out.println(arrayToString);
		Object[] stringToArray = stringToArray(arrayToString);
		for (int i = 0; i < stringToArray.length; i++) {
			System.out.println(stringToArray[i]);
		}
	}

	/**
	 * 判断一个列表是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
		return list==null || list.size()==0;
	}
	
	/**
	 * 将Map<String,String[]>转换为Map<String,String>
	 * @param srcMap
	 * @return
	 */
	public static Map<String,String> convertMap(Map<String,String[]> srcMap){
		Map<String, String> aimMap=new HashMap<String, String>();
		for(Entry<String, String[]> entry : srcMap.entrySet()){
			String[] srcValue = entry.getValue();
			String aimValue=srcValue!=null&&srcValue.length>0?srcValue[0]:null;
			aimMap.put(entry.getKey(), aimValue);
		}
		return aimMap;
	}
}
