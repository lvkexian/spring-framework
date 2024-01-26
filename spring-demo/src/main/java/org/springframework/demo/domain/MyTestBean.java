package org.springframework.demo.domain;

import org.springframework.stereotype.Component;

/**
 * className MyTestBean
 * description TODO 类描述
 *
 * @author lvkexian
 * @date 2023/7/15
 **/
@Component
public class MyTestBean {
	private String testStr = "testStr";

	public String getTestStr() {
		return testStr;
	}

	public void setTestStr(String testStr) {
		this.testStr = testStr;
	}

}
