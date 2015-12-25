/**
 * 
 */
package com.littlemvc.utl;

import android.R.string;

/**
 * @author jiang titeng
 *
 * All right reserve
 */
public abstract class AbNetworkUtl {
    private String baseUrl; 
	
	public void setBaseUrl(String url)
	{
		this.baseUrl  = url;
		
	}
	
	public String getBaseUrl()
	{
		
		return this.baseUrl;
	}
}
