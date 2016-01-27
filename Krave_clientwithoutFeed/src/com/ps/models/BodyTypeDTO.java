package com.ps.models;

public class BodyTypeDTO {
	private String bodyTypeId = "";
	private String bodyTypeName = "";
	private Boolean isSelected = false;

	public String getBodyTypeId() {
		return bodyTypeId;
	}

	public void setBodyTypeId(String bodyTypeId) {
		this.bodyTypeId = bodyTypeId;
	}

	public String getBodyTypeName() {
		return bodyTypeName;
	}

	public void setBodyTypeName(String bodyTypeName) {
		this.bodyTypeName = bodyTypeName;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
}
