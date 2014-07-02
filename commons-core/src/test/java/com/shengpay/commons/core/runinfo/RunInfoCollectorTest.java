package com.shengpay.commons.core.runinfo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.shengpay.commons.core.runinfo.RunInfo;
import com.shengpay.commons.core.runinfo.RunInfoCollector;

public class RunInfoCollectorTest {
	
	private RunInfoCollector ric;

	@Before
	public void setUp() throws Exception {
		ric=new RunInfoCollector();
	}

	@Test
	public void test() {
		assertHas(false,false);
		ric.info("info");
		assertHas(false,false);
		ric.warn("warn");
		assertHas(true,false);
		ric.error("error");
		assertHas(true,true);
		ric.add(new RunInfo(new Exception("异常信息232312")));
		ric.add(new RunInfo("异常信息2",new Exception("异常信息877856")));
		
		assertEquals(5,ric.getInfoList().size());
		assertRunInfo(0, "info", RunInfo.LEVEL_INFO);
		assertRunInfo(1, "warn", RunInfo.LEVEL_WARN);
		assertRunInfo(2, "error", RunInfo.LEVEL_ERROR);
		assertRunInfo(3, null, RunInfo.LEVEL_ERROR);
		assertRunInfo(4, "异常信息2", RunInfo.LEVEL_ERROR);
	}

	private void assertRunInfo(int idx, String msg, int level) {
		RunInfo runInfo = ric.getInfoList().get(idx);
		assertEquals(msg,runInfo.getInfo());
		assertEquals(level,runInfo.getLevel());
	}
	
	private void assertHas(boolean hasWarn, boolean hasError) {
		assertEquals(hasWarn,ric.hasWarnInfo());
		assertEquals(hasError,ric.hasErrorInfo());
	}

}
