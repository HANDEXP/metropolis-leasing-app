/**
 * 
 */
package com.littlemvc.utl;

import java.util.Map;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author jiang titeng
 *
 * All right reserve
 */
public abstract class ILMAsNetworkUtl  extends AbNetworkUtl{
	

	
	public abstract   void get(String url,RequestParams param ,AsyncHttpResponseHandler handler);

	public abstract   void post(String url,RequestParams param,AsyncHttpResponseHandler handler);
	
	
}
