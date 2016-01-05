package com.anacleto.view;

public class OptionBean {
	
	String optionValue;
	
	String optionLabel;
	
	public OptionBean(String optionValue, String optionLabel) {
		this.optionValue = optionValue;
		this.optionLabel = optionLabel;
	}

	public String getOptionLabel() {
		return optionLabel;
	}

	public void setText(String optionLabel) {
		this.optionLabel = optionLabel;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	
}
