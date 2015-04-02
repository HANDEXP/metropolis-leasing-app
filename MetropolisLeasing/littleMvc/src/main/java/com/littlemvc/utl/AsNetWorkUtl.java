/**
 * 
 */
package com.littlemvc.utl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.CookieStore;

import android.app.Application;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.littlemvc.utl.ILMAsNetworkUtl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author jiang titeng
 * 
 *         All right reserve
 */
public class AsNetWorkUtl extends ILMAsNetworkUtl {

	private static AsNetWorkUtl utl;
	
    public static Application  application;

	private static Map<String, String> headers = new HashMap<String, String>();
	
	private static CookieStore cookieStore = null;


	public static AsNetWorkUtl getAsNetWorkUtl(String url) {
//		if (utl != null) {

			utl = new AsNetWorkUtl(url);

//		}

		return utl;

	}
	
	public AsNetWorkUtl(String url) {
		setBaseUrl(url);

	}
	
	@Override
	public void get(String url, RequestParams param,
			AsyncHttpResponseHandler handler) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setCookieStore(cookieStore);
		//添加头
		addClientHeaders(client, headers);
		
		client.get(getAbsoluteUrl(url), param, handler);
	}

	@Override
	public void post(String url, RequestParams param,
			AsyncHttpResponseHandler handler) {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		client.setCookieStore(cookieStore);

		//添加头
		addClientHeaders(client, headers);

		client.post(getAbsoluteUrl(url), param, handler);

	}
	
	
/**
 * 
 * 	为客户端添加头字段
 */

	private static AsyncHttpClient addClientHeaders(AsyncHttpClient client,
			Map<String, String> headers) {
		Set<String> keys = headers.keySet();
		for (String key : keys) {
			client.addHeader(key, headers.get(key));
		}
		return client;
	}
/**
 * 添加头
 */
	public static void addHeader(String key, String value) {
		headers.put(key, value);
	}


	
	
	public String getAbsoluteUrl(String relativeUrl) {

		return getBaseUrl() + relativeUrl;
	}

/**
 * cookie 相关	
 * @param cookieStore
 */
	public static void setCookieStore(CookieStore cookieStore) {
		AsNetWorkUtl.cookieStore = cookieStore;
	}
	
	public static CookieStore getCookieStore() {
		return cookieStore;
	}

	public static void removeAllCookies() {
		cookieStore = null;
		CookieSyncManager.createInstance(application);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}


}
