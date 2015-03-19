/**
 * 
 */
package com.littlemvc.model.request;

import com.littlemvc.model.LMModel;

/**
 * @author jiang titeng
 *
 * All right reserve
 */
public class LMRequestModel extends LMModel implements ILMRequestModel{


	@Override
	public void requestDidStartLoad(LMRequestModel model) {
		 super.didStartLoad(model);
		
	}


	@Override
	public void requestDidFinishLoad(LMRequestModel model) {
		super.didFinshLoad(model);
		// TODO Auto-generated method stub 	
		
	}


	@Override
	public void requestDidFailLoadWithError(LMRequestModel model) {
		super.didFinishLoadWithError(model);
		// TODO Auto-generated method stub
		
	}
	
	
}
