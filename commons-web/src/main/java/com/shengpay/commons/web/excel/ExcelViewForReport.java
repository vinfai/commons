package com.shengpay.commons.web.excel;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.web.servlet.view.document.AbstractJExcelView;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * 查询套件导出使用的excel实现
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		lindongchengonlindongchengng <lindongcheng@SSDOom>
 * @version		$Id: ExcelViewForReport.java,v 1.2 2010-lindongcheng 下午04:10:41 lindongcheng Exp $
 * @create		2010-5-12 下午04:10:41
 */
public class ExcelViewForReport extends AbstractJExcelView {
    /**
     * 属性名称：数据迭代器
     */
    public static final String ATTRIBUTE_HEAD_INFO = "ATTRIBUTE_HEAD_INFO";

    /**
     * 属性名称：数据迭代器
     */
    public static final String ATTRIBUTE_DATA_INFO = "ATTRIBUTE_DATA_INFO";

    /**
     * 属性名称：文件名称
     */
    public static final String ATTRIBUTE_FILE_NAME = "ATTRIBUTE_FILE_NAME";

    /**
     * Excel表名称
     */
    private String sheetName = "Sheet";

    /**
     * 单个Excel工作区最大行数
     */
    private long maxRowIndex = 60000;

    /**
     * 默认文件名
     */
    private String defaultFileName = "数据文件";

    @SuppressWarnings("unchecked")
    @Override
    protected void buildExcelDocument(Map model, WritableWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 取得文件名
        String fileName = (String) model.get(ATTRIBUTE_FILE_NAME);
        if (StringUtils.isBlank(fileName)) {
            fileName = defaultFileName;
        }

        // 取得文件数据信息
        Object dataInfo = model.get(ATTRIBUTE_DATA_INFO);
        if (dataInfo == null) {
            throw new SystemException("未找到查询到的相应数据信息");
        }
        if (!dataInfo.getClass().getName().equals("java.util.ArrayList")) {
            throw new SystemException("[" + dataInfo + "]不是list类型变量");
        }
        //
        Object headInfo = model.get(ATTRIBUTE_HEAD_INFO);
        if (!headInfo.getClass().getName().equals("java.util.ArrayList")) {
            throw new SystemException("[" + headInfo + "]不是list类型变量");
        }

        // 设置文件响应信息
        String showFileName = URLEncoder.encode(fileName + ".xls", "UTF-8");
        showFileName = new String(showFileName.getBytes("iso8859-1"), "gb2312");
        response.setContentType("application/msexcel");// 定义输出类型
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-disposition", "attachment; filename=" + new String(showFileName.getBytes("gb2312"), "iso8859-1"));

        // 初始化参数
        int sheetIndex = 1;// 工作区索引
        int rowIndex = 0;// 行索引
        WritableSheet sheet = workbook.createSheet(sheetName + sheetIndex, sheetIndex++);

        // 输出Excel头信息
        WritableFont wfTitle = new WritableFont(WritableFont.ARIAL, 12);
        WritableCellFormat contentFormat = new WritableCellFormat(wfTitle);
        contentFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        List<String> headInfoList = (List) headInfo;
        int headColIndex = 0;
        for (String head : headInfoList) {
            sheet.addCell(new Label(headColIndex++, rowIndex, head, contentFormat));
        }
        rowIndex++;

        List dataInfoList = (List) dataInfo;
        Iterator dataIterator = dataInfoList.iterator();
        while (dataIterator.hasNext()) {
            // 若已输出行数大于等于excel单工作区最大行数,则创建新的工作区
            if (rowIndex >= maxRowIndex) {
                rowIndex = 0;
                sheet = workbook.createSheet(sheetName + sheetIndex, sheetIndex++);

                // 输出标题栏
                outTitleRow(sheet, rowIndex, headInfoList);
            }

            // 输出行信息
            int colIndex = 0;
            rowIndex++;
            List<String> rowData = (List) dataIterator.next();
            for (String data : rowData) {
                sheet.addCell(new Label(colIndex++, rowIndex, data, contentFormat));
            }
        }
    }


    /**
     * 输出标题栏
     * 
     * @param sheet 标题栏输出到的excel工作区
     * @param rowIndex 标题栏输入的行索引
     * @param colInfoList 列信息列表
     * @throws WriteException
     * @throws RowsExceededException
     */
    private void outTitleRow(WritableSheet sheet, int rowIndex, List<String> headInfoList) throws WriteException, RowsExceededException {
        // 标题栏格式
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false);
        WritableCellFormat titleFormat = new WritableCellFormat(wf);

        int colIndex = 0;

        for (String head : headInfoList) {
            sheet.addCell(new Label(colIndex++, rowIndex, head, titleFormat));
        }
    }
}