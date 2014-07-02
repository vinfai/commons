package com.shengpay.commons.springtools.aop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.springtools.util.AopClassUtils;

/**
 * 在线程中执行被调用方法的拦截器
 * @author Lin
 */
public class InThreadExecuteInterceptor implements MethodInterceptor {
	/**
	 * 系统日志输出句柄
	 */
	private final Logger log = Logger.getLogger(InThreadExecuteInterceptor.class);

	/**
	 * 线程池
	 */
	public final static ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(false);//设计执行任务的线程为非守护线程;
			return t;
		}
	});

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(final MethodInvocation mi) throws Throwable {
		//输出日志
		final String callInfo = AopClassUtils.getMethodCallInfo(mi);
		final long currentTimeMillis = System.currentTimeMillis();

		//构建执行接口
		Runnable proceedRunnable = new Runnable() {
			public void run() {
				try {
					mi.proceed();
				} catch (Throwable e) {
					log.error("在现场内执行【" + callInfo + "】【编号:" + currentTimeMillis + "】时发生异常:", e);
				}
			}
		};

		//在线程池内执行操作
		cachedThreadPool.execute(proceedRunnable);

		//返回空信息
		return null;
	}

}
