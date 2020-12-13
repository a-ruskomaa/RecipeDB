package jyu.ties4560.demo3_4_recipedb.domain;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * Class for error messages.
 */
@XmlRootElement
public class ErrorMessage {
	public ErrorMessage() {}
	
	private String errorMessage;
	private int errorCode;
	
	public ErrorMessage(String errorMessage, int errorCode) {
		super();
		this.setErrorMessage(errorMessage);
		this.setErrorCode(errorCode);
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
