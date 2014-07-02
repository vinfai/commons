/**
 * 
 */
package com.shengpay.commons.core.runinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息收集器
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-11-22 上午09:57:44
 */

public class RunInfoCollector {

	private static final ThreadLocal<RunInfoCollector> currentThreadInstance = new ThreadLocal<RunInfoCollector>(); 
	
	private List<RunInfo> infoList=new ArrayList<RunInfo>();

	/**
	 * 
	 */
	private boolean hasWarnInfo=false;
	
	/**
	 * 
	 */
	private boolean hasErrorInfo=false;
	
	/**
	 * @return
	 */
	public static RunInfoCollector getCurrentThreadInstance() {
		RunInfoCollector precautionDevice = currentThreadInstance.get();
		if(precautionDevice==null) {
			return resetCurrentThreadInstance();
		}
		return precautionDevice;
	}
	
	/**
	 * @return 
	 * 
	 */
	public static RunInfoCollector resetCurrentThreadInstance() {
		RunInfoCollector ric = new RunInfoCollector();
		currentThreadInstance.set(ric);
		return ric;
	}
	
	/**
	 * @return 
	 * 
	 */
	public static void setCurrentThreadInstance(RunInfoCollector ric) {
		currentThreadInstance.set(ric);
	}

	public void info(String info) {
		gatherInformation(info,RunInfo.LEVEL_INFO);
	}
	
	public void warn(String info) {
		gatherInformation(info,RunInfo.LEVEL_WARN);
	}
	
	public void error(String info) {
		gatherInformation(info,RunInfo.LEVEL_ERROR);
	}
	
	/**
	 * @param precautionStr
	 */
	private void gatherInformation(String info,int level) {
		add(new RunInfo(info,level));
	}

	public void add(RunInfo ri) {
		setHasWarnOrErrorInfoFlag(ri.getLevel());
		infoList.add(ri);
	}

	/**
	 * @param level
	 */
	private void setHasWarnOrErrorInfoFlag(int level) {
		switch(level) {
			case RunInfo.LEVEL_ERROR:
				hasErrorInfo=true;
				break;
			case RunInfo.LEVEL_WARN:
				hasWarnInfo=true;
				break;
		}
	}

	/**
	 * @return
	 */
	public List<RunInfo> getInfoList() {
		return infoList;
	}

	/**
	 * @return
	 */
	public boolean hasWarnInfo() {
		return hasWarnInfo;
	}

	/**
	 * @return
	 */
	public boolean hasErrorInfo() {
		return hasErrorInfo;
	}
	
}
