/**
 * 
 */
package com.shengpay.commons.web.excel;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.web.servlet.view.document.AbstractJExcelView;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.format.DataFormater;
import com.shengpay.commons.core.format.DataFormaterForBigDecimal;
import com.shengpay.commons.core.format.DataFormaterUtil;
import com.shengpay.commons.core.utils.StringUtils;


/**
 * 将列表转换为excel格式并允许下载
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: ListForExcelView.java,v 1.0 2010-1-28 下午05:23:39 lindc Exp $
 * @create		2010-1-28 下午05:23:39
 */
public class ExcelView extends AbstractJExcelView {
	
	/**
	 * 属性名称：数据迭代器
	 */
	public static final String 	ATTRIBUTE_HEAD_INFO="ATTRIBUTE_HEAD_INFO";
	
	/**
	 * 属性名称：数据迭代器
	 */
	public static final String 	ATTRIBUTE_DATE_ITERATOR="ATTRIBUTE_DATE_ITERATOR";
	
	/**
	 * 属性名称：文件名称
	 */
	public static final String 	ATTRIBUTE_FILE_NAME="ATTRIBUTE_FILE_NAME";

	/**
	 * Excel表名称
	 */
	private String sheetName="Sheet";
	
	/**
	 * 单个Excel工作区最大行数
	 */
	private long maxRowIndex=60000;

	/**
	 * 默认文件名
	 */
	private String defaultFileName="数据文件";

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map mode, WritableWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//取得交易信息
		Iterator dataIterator = (Iterator) mode.get(ATTRIBUTE_DATE_ITERATOR);
		if (dataIterator == null) {
			throw new SystemException("未找到查询到的交易列表信息");
		}

		//取得文件名
		String fileName = (String) mode.get(ATTRIBUTE_FILE_NAME);
		if(StringUtils.isBlank(fileName)){
			fileName=defaultFileName;
		}
		
		//设置文件响应信息
		String showFileName = URLEncoder.encode(fileName + ".xls", "UTF-8");
		showFileName = new String(showFileName.getBytes("iso8859-1"), "gb2312");
		response.setContentType("application/msexcel");// 定义输出类型
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=30");
		response.setHeader("Content-disposition", "attachment; filename=" + new String(showFileName.getBytes("gb2312"), "iso8859-1"));

		//初始化参数
		int sheetIndex=1;//工作区索引
		int rowIndex = 0;//行索引
		WritableSheet sheet = workbook.createSheet(sheetName+sheetIndex, sheetIndex++);

		//输出Excel头信息
		WritableFont wfTitle = new WritableFont(WritableFont.ARIAL, 12);
		WritableCellFormat contentFormat = new WritableCellFormat(wfTitle);
		contentFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		Object headInfoVO = mode.get(ATTRIBUTE_HEAD_INFO);
		if (headInfoVO != null) {
			List<ColMetaInfo> headColInfoList = getExcelColInfoListByClass(headInfoVO.getClass());
			for (ColMetaInfo excelColInfo : headColInfoList) {
				String headInfoVOTitle = excelColInfo.colTitle;

				sheet.addCell(new Label(0, rowIndex, headInfoVOTitle, contentFormat));
				addCell(sheet, rowIndex++, 1, headInfoVO, excelColInfo);
			}
			rowIndex++;
		}

		//取得第一行数据,若无数据,则直接退出
		if(!dataIterator.hasNext()){
			return;
		}
		Object rowData=dataIterator.next();
		
		//输出标题栏信息
		List<ColMetaInfo> excelColInfoList = getExcelColInfoListByClass(rowData.getClass());
		outTitleRow(sheet, rowIndex,excelColInfoList );

		//输出内容体
		while (true) {
			//若已输出行数大于等于excel单工作区最大行数,则创建新的工作区
			if(rowIndex>=maxRowIndex){
				rowIndex=0;
				sheet=workbook.createSheet(sheetName+sheetIndex, sheetIndex++);
				
				//输出标题栏
				outTitleRow(sheet, rowIndex, excelColInfoList);
			}
			
			//输出行信息
			int colIndex=0;
			rowIndex++;
			for (ColMetaInfo colMetaInfo : excelColInfoList) {
				addCell(sheet,rowIndex,colIndex++,rowData,colMetaInfo);
			}
			
			//如果不在有数据,则跳出循环
			if(dataIterator.hasNext()){
				rowData=dataIterator.next();
			}else{
				break;
			}
		}
	}

	/**
	 * 向EXCEL表添加一个单元格信息
	 * @param sheet	excel表
	 * @param rowIndex 行索引
	 * @param colIndex 列索引
	 * @param dataObj 数据对象 
	 * @param excelColInfo 被输出的列信息
	 * @throws IllegalAccessException
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	@SuppressWarnings("unchecked")
	private void addCell(WritableSheet sheet, int rowIndex, int colIndex, Object dataObj, ColMetaInfo excelColInfo) throws IllegalAccessException, WriteException, RowsExceededException {
		Object propertyValue = excelColInfo.colField.get(dataObj);
		String value = excelColInfo.dataFormater.format(propertyValue);
		WritableCellFormat contentFormat = excelColInfo.contentFormat;
		if(excelColInfo.dataFormater instanceof DataFormaterForBigDecimal){
			sheet.addCell(new Number(colIndex, rowIndex , Double.parseDouble(value) , contentFormat));
		}else{
			sheet.addCell(new Label(colIndex, rowIndex, value , contentFormat));
		}
	}

	/**
	 * 输出标题栏
	 * @param sheet 标题栏输出到的excel工作区
	 * @param rowIndex 标题栏输入的行索引
	 * @param colInfoList 列信息列表
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private void outTitleRow(WritableSheet sheet, int rowIndex, List<ColMetaInfo> colInfoList) throws WriteException, RowsExceededException {
		//标题栏格式
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false);
		WritableCellFormat titleFormat = new WritableCellFormat(wf);
		
		int colIndex=0;
		
		for (ColMetaInfo excelColInfo : colInfoList) {
			sheet.addCell(new Label(colIndex++, rowIndex, excelColInfo.colTitle, titleFormat));
		}
	}
	
	/**
	 * 取得类型信息中包含的EXCEL列信息
	 * @param dataClass 数据类型
	 * @return
	 */
	private List<ColMetaInfo> getExcelColInfoListByClass(Class<?> dataClass){
		Field[] fields = dataClass.getDeclaredFields();//返回的是某个类里的所有类型的变量，不包括继承父类的
		if(fields==null || fields.length==0){
			throw new SystemException("在["+dataClass+"]未找到类型变量");
		}
		List<ColMetaInfo> colInfoList=new ArrayList<ColMetaInfo>();
		for (int i = 0; i < fields.length; i++) {
			Field colField = fields[i];
			ExcelColTag excelColTag = colField.getAnnotation(ExcelColTag.class);
			if(excelColTag==null){
				continue;
			}
			
			Class<?> fieldClass = colField.getType();
			colField.setAccessible(true);
			String colTitle = excelColTag.getTitle();
			int colOrder = excelColTag.getOrder();
			WritableCellFormat contentFormat=getFormatByClass(fieldClass);
			DataFormater<?> dataFormat=DataFormaterUtil.getInstanceByClass(fieldClass);
			colInfoList.add(new ColMetaInfo(colField,colTitle, colOrder,contentFormat,dataFormat));
		}

		Collections.sort(colInfoList);
		return colInfoList;
	}
	
	/**
	 * 根据列类型取得列格式
	 * @param fieldClass
	 * @return
	 */
	private WritableCellFormat getFormatByClass(Class<?> fieldClass) {
		WritableFont wfTitle = new WritableFont(WritableFont.ARIAL, 12);
		WritableCellFormat contentFormat = new WritableCellFormat(wfTitle);
		if(fieldClass.equals(BigDecimal.class)){
			NumberFormat nf = new jxl.write.NumberFormat("#0.00");
			contentFormat = new WritableCellFormat(nf);
		}
		return contentFormat;
	}

	/**
	 * 列的元信息 		
	 * @description	
	 * @usage		
	 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
	 * @company		SSDOorporation.
	 * @author		LinDongCheng <lindongcheng@snda.com>
	 * @version		$Id: ExcelView.java,v 1.0 2010-1-29 下午06:43:46 lindc Exp $
	 * @create		2010-1-29 下午06:43:46
	 */
	class ColMetaInfo implements Comparable<ColMetaInfo>{
		/**
		 * 列所属域信息
		 */
		Field colField;
		
		/**
		 * 列标题
		 */
		String colTitle;

		/**
		 * 列排序
		 */
		int order;
		
		/**
		 * 列格式
		 */
		WritableCellFormat contentFormat;
		
		/**
		 * 数据格式化接口 
		 */
		@SuppressWarnings("unchecked")
		DataFormater dataFormater;
		
		@SuppressWarnings("unchecked")
		public ColMetaInfo(Field colField, String colTitle, int order, WritableCellFormat contentFormat, DataFormater dataFormater) {
			this.colField = colField;
			this.colTitle = colTitle;
			this.order = order;
			this.contentFormat = contentFormat;
			this.dataFormater = dataFormater;
		}

		@Override
		public int compareTo(ColMetaInfo o) {
			return order-o.order;
		}
		
	}
}
