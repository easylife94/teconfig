package cn.teracloud.plugin.teconfig.exception;

public class ValidateException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4409131978870325443L;
	
	public ValidateException(String message) {
		super("validate exception:"+message);
	}

}
