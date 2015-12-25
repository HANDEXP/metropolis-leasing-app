package com.hand.hrms4android.parser;

public class Expression {
	private String expression;
	private String attName;

	public Expression() {
		this(null, null);
	}

	public Expression(String expression, String attName) {
		this.expression = expression;
		this.attName = attName;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

}
