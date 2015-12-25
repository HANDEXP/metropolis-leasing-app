package com.hand.hrms4android.parser;

import com.hand.hrms4android.exception.ParseExpressionException;

public interface ConfigReader {
	/**
	 * 通过表达式取对应的属性
	 * 
	 * @param expression
	 * @return
	 * @throws ParseExpressionException
	 */
	public String getAttr(Expression expression) throws ParseExpressionException;
}
