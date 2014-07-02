/**
 * 
 */
package com.shengpay.commons.web.excel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.shengpay.commons.core.base.PaginationBaseObject;

/**
 * escription
 * @usage
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company SDOCorporation.
 * @author LinDongCheng <lindongcheng@snda.com>
 * @version $Id: ExcelModelAndViewUtil.java,v 1.0 2010-1-31 上午10:57:59 lindc Exp $
 * @create 2010-1-31 上午10:57:59
 */

public class ExcelModelAndViewUtil {

	/**
	 * 输出Excel信息
	 * 
	 * @param fileName(可选)
	 *            文件名
	 * @param headInfo(可选)
	 *            头信息
	 * @param dataList(必填)
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForPage(String fileName, Object headInfo, PaginationBaseObject<?> pageObj) {
		return showExcelForList(fileName, headInfo, pageObj != null ? pageObj.getDataList() : null);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param dataList
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForPage(PaginationBaseObject<?> pageObj) {
		return showExcelForList(null, null, pageObj != null ? pageObj.getDataList() : null);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param headInfo
	 *            头信息
	 * @param dataList
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForPage(Object headInfo, PaginationBaseObject<?> pageObj) {
		return showExcelForList(null, headInfo, pageObj != null ? pageObj.getDataList() : null);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param dataList
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForPage(String fileName, PaginationBaseObject<?> pageObj) {
		return showExcelForList(fileName, null, pageObj != null ? pageObj.getDataList() : null);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param headInfo
	 *            头信息
	 * @param dataList
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForList(String fileName, Object headInfo, List<?> dataList) {
		return showExcel(fileName, headInfo, dataList != null ? dataList.iterator() : null);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param dataList
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForList(List<?> dataList) {
		return showExcel(null, null, dataList != null ? dataList.iterator() : null);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param dataList
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForList(String fileName, List<?> dataList) {
		return showExcel(fileName, null, dataList != null ? dataList.iterator() : null);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param headInfo
	 *            头信息
	 * @param dataList
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForList(Object headInfo, List<?> dataList) {
		return showExcel(null, headInfo, dataList != null ? dataList.iterator() : null);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param headInfo
	 *            头信息
	 * @param dataIte
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForIterator(String fileName, Object headInfo, Iterator<?> dataIte) {
		return showExcel(fileName, headInfo, dataIte);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param dataIte
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForIterator(Iterator<?> dataIte) {
		return showExcel(null, null, dataIte);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param dataIte
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForIterator(String fileName, Iterator<?> dataIte) {
		return showExcel(fileName, null, dataIte);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param headInfo
	 *            头信息
	 * @param dataIte
	 *            数据迭代器
	 * @return
	 */
	public static ModelAndView showExcelForIterator(Object headInfo, Iterator<?> dataIte) {
		return showExcel(null, headInfo, dataIte);
	}

	/**
	 * 输出Excel信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param headInfo
	 *            头信息
	 * @param dataIte
	 *            数据迭代器
	 * @return
	 */
	private static ModelAndView showExcel(String fileName, Object headInfo, Iterator<?> dataIte) {
		Map<String, Object> mode = new HashMap<String, Object>();
		mode.put(ExcelView.ATTRIBUTE_DATE_ITERATOR, dataIte);
		mode.put(ExcelView.ATTRIBUTE_FILE_NAME, fileName);
		mode.put(ExcelView.ATTRIBUTE_HEAD_INFO, headInfo);
		return new ModelAndView("excelView_commons", mode);

	}

	/**
     * 输出Excel信息
     * 
     * @param fileName
     *            文件名
     * @param headInfo
     *            头信息
     * @param dataIte
     *            数据迭代器
     * @return
     */
    public static ModelAndView showExcelForList(String fileName, Object headInfo, Object dataInfo) {
        return showExcel(fileName, headInfo, dataInfo);
    }
    
    /**
     * 输出Excel信息
     * 
     * @param fileName
     *            文件名
     * @param headInfo
     *            头信息
     * @param dataIte
     *            数据迭代器
     * @return
     */
    private static ModelAndView showExcel(String fileName, Object headInfo, Object dataInfo) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ExcelViewForReport.ATTRIBUTE_DATA_INFO, dataInfo);
        model.put(ExcelViewForReport.ATTRIBUTE_FILE_NAME, fileName);
        model.put(ExcelViewForReport.ATTRIBUTE_HEAD_INFO, headInfo);
        return new ModelAndView("excelViewForReport_commons", model);

    }
}
