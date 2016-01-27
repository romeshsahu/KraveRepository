package com.ps.models;

import java.util.List;

public class SettingDTO {
	private String userID;//="-1";
	private String minAge;//="10";
	private String maxAge;//="75";
	private String searchRadius;//="500";
	private String isNotificationEnable;//="1";
	private String interest[];
	private String love_hookup;
	private String body_type;
	private String chatHistoryPeriod="0";
	private String findDudesColumnCoun="0";
	private String language="0";
	
	private List<InterestsDTO> selectedInterestList;
	private List<RoleDTO> selectedRoleList;
	private List<BodyTypeDTO> selectedBodyTypeList;
	private List<WhatAreYouDataDTO> whatAreYouDataDTOs ;
	private TransitDTO transitDTO;
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public List<RoleDTO> getSelectedRoleList() {
		return selectedRoleList;
	}

	public void setSelectedRoleList(List<RoleDTO> selectedRoleList) {
		this.selectedRoleList = selectedRoleList;
	}
	
	public List<BodyTypeDTO> getSelectedBodyTypeList() {
		return selectedBodyTypeList;
	}

	public void setSelectedBodyTypeList(List<BodyTypeDTO> selectedBodyTypeList) {
		this.selectedBodyTypeList = selectedBodyTypeList;
	}
	
	public String getLoveHookup() {
		return love_hookup;
	}
	
	public void setLoveHookup(String love_hookup) {
		this.love_hookup = love_hookup;
	}
	
	public String getBodyType() {
		return body_type;
	}
	
	public void setBodyType(String body_type) {
		this.body_type = body_type;
	}
	
	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

	public String getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(String searchRadius) {
		this.searchRadius = searchRadius;
	}

	public String getIsNotificationEnable() {
		return isNotificationEnable;
	}

	public void setIsNotificationEnable(String isNotificationEnable) {
		this.isNotificationEnable = isNotificationEnable;
	}

	public String[] getInterest() {
		return interest;
	}

	public void setInterest(String interest[]) {
		this.interest = interest;
	}

	public List<InterestsDTO> getSelectedInterestList() {
		return selectedInterestList;
	}

	public void setSelectedInterestList(List<InterestsDTO> selectedInterestList) {
		this.selectedInterestList = selectedInterestList;
	}

	public List<WhatAreYouDataDTO> getWhatAreYouDataDTOs() {
		return whatAreYouDataDTOs;
	}

	public void setWhatAreYouDataDTOs(List<WhatAreYouDataDTO> whatAreYouDataDTOs) {
		this.whatAreYouDataDTOs = whatAreYouDataDTOs;
	}

	public void setTransitDTO(TransitDTO transitDTO){
		this.transitDTO = transitDTO;
	}
	
	public TransitDTO getTransitDTO(){
		return this.transitDTO;
	}

	public String getChatHistoryPeriod() {
		return chatHistoryPeriod;
	}

	public void setChatHistoryPeriod(String chatHistoryPeriod) {
		this.chatHistoryPeriod = chatHistoryPeriod;
	}

	public String getFindDudesColumnCoun() {
		return findDudesColumnCoun;
	}

	public void setFindDudesColumnCoun(String findDudesColumnCoun) {
		this.findDudesColumnCoun = findDudesColumnCoun;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}
