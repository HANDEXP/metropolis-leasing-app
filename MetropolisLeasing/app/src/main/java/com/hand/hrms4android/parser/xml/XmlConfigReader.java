package com.hand.hrms4android.parser.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.hand.mas.metropolisleasing.application.MSApplication;
import org.hand.mas.utl.ConstantUtl;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.content.Context;


import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.ConfigReader;
import com.hand.hrms4android.parser.Expression;

import static org.hand.mas.utl.ConstantUtl.*;


public class XmlConfigReader implements ConfigReader {

	/**
	 * xml
	 */
	private InputSource xmlInputSource;

	private static XmlConfigReader configReader;

	/**
	 * 
	 */

	public XmlConfigReader(File xmlFile) throws FileNotFoundException {
		xmlInputSource = new InputSource(new FileInputStream(xmlFile));
	}

	public XmlConfigReader(InputStream inputStream) throws FileNotFoundException {
		xmlInputSource = new InputSource(inputStream);
	}

	public static XmlConfigReader getInstance() {
		if (configReader == null) {

			File dir = MSApplication.getApplication().getDir(SYS_PREFRENCES_CONFIG_FILE_DIR_NAME,
			        Context.MODE_PRIVATE);
			try {
				configReader = new XmlConfigReader(new File(dir, configFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return configReader;
	}

	@Override
	public String getAttr(Expression expression) throws ParseExpressionException {
		Element element = getElement(expression.getExpression());
		return element.getAttribute(expression.getAttName());
	}

	public Element getElement(String expression) throws ParseExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		InputStream configFileInputStream = null;
		try {

			File dir = MSApplication.getApplication().getDir(ConstantUtl.SYS_PREFRENCES_CONFIG_FILE_DIR_NAME,
			        Context.MODE_PRIVATE);
			File configFile = new File(dir, ConstantUtl.configFile);
			FileInputStream inputStream = new FileInputStream(configFile);
			xmlInputSource = new InputSource(inputStream);
			Object result = xpath.evaluate(expression, xmlInputSource, XPathConstants.NODE);
			if (result != null && result instanceof Element) {
				return (Element) result;
			} else {
				throw new ParseExpressionException(
				        "Can't find the node or the result is not an instance of Element ,expression:" + expression);
			}
		} catch (XPathExpressionException e) {
			throw new ParseExpressionException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ParseExpressionException(e);
		} finally {
			if (configFileInputStream != null) {
				try {
					configFileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
