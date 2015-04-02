package com.hand.hrms4android.exception;

/**
 * 适用于aurora的错误，也就是通讯过程没有问题，但业务逻辑报错
 * 
 * @author emerson
 * 
 */
public class AuroraServerFailure extends Exception {

	/**
     * 
     */
	private static final long serialVersionUID = 7840602976477688026L;

	public AuroraServerFailure() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuroraServerFailure(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public AuroraServerFailure(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public AuroraServerFailure(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
