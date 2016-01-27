package com.ps.models;

public class TransitDTO {
	private String transitId = "";
	private String transitName = "";
	private Boolean isSelected = false;

	public String getTransitId() {
		return transitId;
	}

	public void setTransitId(String transitId) {
		this.transitId = transitId;
	}

	public String getTransitName() {
		return transitName;
	}

	public void setTransitName(String transitName) {
		this.transitName = transitName;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
}
