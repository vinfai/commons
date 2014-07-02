/**
 * 
 */
package com.shengpay.commons.springtools.methodcallinfo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.shengpay.commons.core.threadlocal.ThreadLocalUtils;

/**
 * 调用堆栈信息
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-9-11 上午10:56:37
 */

public class MethodCallInfo{
	
	/**
	 * 当前线程信息存储关键字：父方法调用信息
	 */
	private static final String	PARENT_METHOD_CALL	= "PARENT_METHOD_CALL";
	
	/**
	 * 方法调用信息
	 */
	private MethodInvocation mi;
	
	/**
	 * 调用层级
	 */
	private int callLevel;

	/**
	 * 方法调用信息 
	 */
	private String callInfo;
	
	/**
	 * 调用开始时间
	 */
	private long beginTime;
	
	/**
	 * 调用开始时间
	 */
	private long endTime;
	
	/**
	 * 父方法调用
	 */
	private MethodCallInfo parseMethodCallInfo;
	
	/**
	 * 内部方法调用列表
	 */
	private List<MethodCallInfo> interCallList=new ArrayList<MethodCallInfo>();
	
	/**
	 * 判断当前方法是否是顶级调用
	 * @return
	 */
	private boolean isTopMethodCall() {
		return parseMethodCallInfo==null;
	}

	/**
	 * 从当前线程中取得父方法调用信息
	 * @return
	 */
	private MethodCallInfo getCurrentParentMethodCall() {
		return (MethodCallInfo) ThreadLocalUtils.getValue(PARENT_METHOD_CALL);
	}
	
	/**
	 * 将当前调用信息作为父调用方法存放到当前线程中
	 * @return
	 */
	private void setCurrentParentMethodCall4this() {
		ThreadLocalUtils.setValue(PARENT_METHOD_CALL,this);
	}
	
	/**
	 * 将当前调用信息作为父调用方法存放到当前线程中
	 * @return
	 */
	private void setCurrentParentMethodCall4parse() {
		ThreadLocalUtils.setValue(PARENT_METHOD_CALL,parseMethodCallInfo);
	}
	
	/**
	 * 获取方法（及其内部方法）调用耗时堆栈信息
	 */
	private String getMethodExpendStack() {
		StringBuffer buf=new StringBuffer();
		buf.append(getCallLevelTabString()+getCallLevelInfo()+getFullMethodExpendMillInfo() + "\r\n");
		for (MethodCallInfo interMethodCall : interCallList) {
			buf.append(interMethodCall.getMethodExpendStack());
		}
		
		return buf.toString();
	}
	
	/**
	 * @return
	 */
	private String getFullMethodExpendMillInfo() {
		return "【用时:" + getMethodExpendMillInfo() + "毫秒】"+callInfo;
	}

	private List<MethodCallInfo> getExpendTopsList(){
		List<MethodCallInfo> list=new ArrayList<MethodCallInfo>();
		list.add(this);
		addInterCall2List(list);
		
		//排序
		Collections.sort(list, new Comparator<MethodCallInfo>() {
			@Override
			public int compare(MethodCallInfo o1, MethodCallInfo o2) {
				return (int) (o2.calcMethodSelfExpendMill()-o1.calcMethodSelfExpendMill());
			}
		});
		
		return list;
		
	}
	
	/**
	 * 获取方法耗时最长排序列表
	 * @return
	 */
	private void addInterCall2List(List<MethodCallInfo> list) {
		list.addAll(this.interCallList);
		for (MethodCallInfo methodCallInfo : interCallList) {
			methodCallInfo.addInterCall2List(list);
		}
		
	}

	/**
	 * @return
	 */
	private String getCallLevelInfo() {
		return "【"+callLevel+"】";
	}

	/**
	 * @return
	 */
	private String getCallLevelTabString() {
		StringBuffer buf=new StringBuffer();
		for (int i = 1; i < callLevel; i++) {
			buf.append("\t");
		}
		return buf.toString();
	}

	/**
	 * @param callInfo	方法信息
	 * @param parentMethodCallInfo 父方法调用信息
	 */
	public MethodCallInfo(MethodInvocation mi,String callInfo) {
		this.callInfo = callInfo;
		this.mi=mi;
		
		initParseMethodCallInfo();
		initCallLevel();
		setCurrentParentMethodCall4this();
	}

	/**
	 * 初始化父方法调用信息
	 */
	private void initParseMethodCallInfo() {
		MethodCallInfo parentMethodCallInfo=getCurrentParentMethodCall();
		if(parentMethodCallInfo!=null) {
			this.parseMethodCallInfo=parentMethodCallInfo;
			parentMethodCallInfo.interCallList.add(this);
		}
	}

	/**
	 * 初始化调用级别信息
	 */
	private void initCallLevel() {
		if(parseMethodCallInfo!=null) {
			callLevel=parseMethodCallInfo.callLevel+1;
		}else {
			callLevel=1;
		}
	}

	/**
	 * 清理计时信息
	 */
	private void cleanReckonByTimwTime() {
		beginTime=-1;
		endTime=-1;
	}
	
	/**
	 * 开始方法调用计时
	 */
	private void startRockonByTimwTime() {
		cleanReckonByTimwTime();
		beginTime=System.currentTimeMillis();
	}
	
	/**
	 * 结束方法调用计时
	 */
	private void stopRockonByTimwTime() {
		assert endTime==-1:"计时已结束，不能重复结束！";
		endTime=System.currentTimeMillis();
		
		setCurrentParentMethodCall4parse();
	}
	
	/**
	 * 计算方法总耗时（包含内部方法调用时间）（单位：毫秒）
	 * @return
	 */
	public long calcMethodTotalExpendMill() {
		assert beginTime!=-1 && endTime!=-1:"服务未正常启动和停止计时，不能计算晓浩时间！";
		return endTime-beginTime;
	}
	
	/**
	 * 计算方法自身耗时（不包含内部方法调用时间）（单位：毫秒）
	 * @return
	 */
	public long calcMethodSelfExpendMill() {
		return calcMethodTotalExpendMill()-calcInterMethodCallTotalExpendMill();
	}
	
	public String getMethodExpendMillInfo() {
		return calcMethodSelfExpendMill() + "/" + calcMethodTotalExpendMill();
	}
	
	/**
	 * 计算内部方法调用总耗时
	 * @return
	 */
	private long calcInterMethodCallTotalExpendMill() {
		long interCallMethodTotalExpend=0;
		for (MethodCallInfo interCallMethod : interCallList) {
			interCallMethodTotalExpend+=interCallMethod.calcMethodTotalExpendMill();
		}
		return interCallMethodTotalExpend;
	}

	/**
	 * @return
	 * @throws Throwable
	 * @see org.aopalliance.intercept.Joinpoint#proceed()
	 */
	public Object proceed() throws Throwable {
		startRockonByTimwTime();
		try {
			return mi.proceed();
		}finally {
			stopRockonByTimwTime();
		}
	}
	
	/**
	 * 打印方法堆栈信息
	 * @param out 
	 */
	public void printMethodExpentStack(PrintStream out) {
		if(isTopMethodCall()) {
			out.println(getMethodExpendStack());
		}
	}
	
	/**
	 * 打印方法调用耗时列表（按时长逆序排序）
	 */
	public void printMethodExpentTops(PrintStream out) {
		if(isTopMethodCall()) {
			List<MethodCallInfo> expendTops = getExpendTopsList();
			for (MethodCallInfo methodCallInfo : expendTops) {
				out.println(methodCallInfo.getFullMethodExpendMillInfo());
			}
		}
		
	}
}
