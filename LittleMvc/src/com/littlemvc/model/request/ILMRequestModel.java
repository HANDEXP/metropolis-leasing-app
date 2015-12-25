/**
 * 
 */
package com.littlemvc.model.request;

/**
 * @author jiang titeng
 *
 * All right reserve
 */
public interface ILMRequestModel {
	
	public void requestDidStartLoad(LMRequestModel model);
	 
	public void requestDidFinishLoad(LMRequestModel model);
	
	public void requestDidFailLoadWithError(LMRequestModel model);
	
	
	
}
