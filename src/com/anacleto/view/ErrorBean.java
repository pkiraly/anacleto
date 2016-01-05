/*
 * Created on Apr 30, 2005
 *
 */
package com.anacleto.view;

/**
 * Bean to display errors of the application. Errorcodes are labels that will
 * be translated. These labels can contain four arguments. The arguments are
 * prefixed with a '%' sign within the label like:
 * The quick brown %1 jumped.... . When displayed this label %1 will be 
 * replaced by the first argument.
 *   
 * @author robi
 *
 */
public class ErrorBean {

	private String errorCode;
	private String arg1;
	private String arg2;
	private String arg3;
	private String arg4;
	
	
	public ErrorBean(){
	}
	
	public ErrorBean(String errorCode){
		this.errorCode = errorCode;
	}

	public ErrorBean(String errorCode, String arg1){
		this.errorCode = errorCode;
		this.arg1      = arg1;
	}

	public ErrorBean(String errorCode, String arg1, String arg2){
		this.errorCode = errorCode;
		this.arg1      = arg1;
		this.arg2      = arg2;
	}

	public ErrorBean(String errorCode, String arg1, String arg2,
					 String arg3){
		this.errorCode = errorCode;
		this.arg1      = arg1;
		this.arg2      = arg2;
		this.arg3      = arg3;
	}

	public ErrorBean(String errorCode, String arg1, String arg2,
			 String arg3, String arg4){
		this.errorCode = errorCode;
		this.arg1      = arg1;
		this.arg2      = arg2;
		this.arg3      = arg3;
		this.arg4      = arg4;
}
	
	/**
	 * @return Returns the arg1.
	 */
	public String getArg1() {
		return arg1;
	}
	/**
	 * @param arg1 The arg1 to set.
	 */
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}
	/**
	 * @return Returns the arg2.
	 */
	public String getArg2() {
		return arg2;
	}
	/**
	 * @param arg2 The arg2 to set.
	 */
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}
	/**
	 * @return Returns the arg3.
	 */
	public String getArg3() {
		return arg3;
	}
	/**
	 * @param arg3 The arg3 to set.
	 */
	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}
	/**
	 * @return Returns the arg4.
	 */
	public String getArg4() {
		return arg4;
	}
	/**
	 * @param arg4 The arg4 to set.
	 */
	public void setArg4(String arg4) {
		this.arg4 = arg4;
	}
	/**
	 * @return Returns the errorCode.
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode The errorCode to set.
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
