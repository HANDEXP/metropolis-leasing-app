/**
 * 
 */
package com.littlemvc.model;

/**
 * @author jiang titeng
 *
 * All right reserve
 */
public interface LMModelDelegate {
	
	public void modelDidFinishLoad(LMModel model);
	
	public void modelDidStartLoad(LMModel model); 
	
	public void modelDidFailedLoadWithError(LMModel model);
		
	
}
