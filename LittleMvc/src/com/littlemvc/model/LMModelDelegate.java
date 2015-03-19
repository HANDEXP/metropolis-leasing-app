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
	
	public void modelDidFinshLoad(LMModel model);
	
	public void modelDidStartLoad(LMModel model); 
	
	public void modelDidFaildLoadWithError(LMModel model);
		
	
}
