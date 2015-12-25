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
	
	protected void didFinshLoad(LMModel model) {
		modelDelegate.modelDidFinshLoad(model);
	}
	
	protected void didFinshLoadWithError (LMModel model) {
		modelDelegate.modelDidFaildLoadWithError(model);
	}
	
	
}
