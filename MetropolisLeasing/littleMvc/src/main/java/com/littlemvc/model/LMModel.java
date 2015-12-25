/**
 * 
 */
package com.littlemvc.model;

/**
 * @author jiang titeng
 *
 * All right reserve
 */
public abstract class LMModel {
	public LMModelDelegate modelDelegate;
	public Object tag; 
	
	
	protected void didStartLoad(LMModel model) {
		modelDelegate.modelDidStartLoad(model);
	}
	
	protected void didFinishLoad(LMModel model) {
		modelDelegate.modelDidFinishLoad(model);
	}
	
	protected void didFinishLoadWithError (LMModel model) {
		modelDelegate.modelDidFailedLoadWithError(model);
	}
	
	
}
