package com.ps.models;

public class ChatHistoryDTO {
	private String chatHistoryId = "";
	private String chatHistoryName = "";
	private Boolean isSelected = false;

	public String getChatHistoryId() {
		return chatHistoryId;
	}

	public void setChatHistoryId(String chatHistoryId) {
		this.chatHistoryId = chatHistoryId;
	}

	public String getChatHistoryName() {
		return chatHistoryName;
	}

	public void setChatHistoryName(String chatHistoryName) {
		this.chatHistoryName = chatHistoryName;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
}
