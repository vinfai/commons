package com.shengpay.commons.springtools.proxy.facade;

public class Interface2Impl implements Interface2 {

	@Override
	public String sayHello2(String name) {
		return "2 hello "+name;
	}

}
