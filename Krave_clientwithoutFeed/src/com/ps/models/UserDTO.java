package com.ps.models;

import java.util.ArrayList;
import java.util.List;

import com.ps.utill.AppConstants;

public class UserDTO extends AddressDTO {
	private String language = "0";
	private String userID = "";
	private String profileImage = "";
	private String firstName = "";
	private String lastName = "";
	private String email = "";
	private String mobile = "";
	private String aboutMe = "";
	private boolean isFacebookLikeEnable=false;
	private String isFirstTime = "";
	private String password = "";
	private String confirmPassword = "";
	private String unreadMsg = "0";
	private String userLastMsg = "";
	private String userLastMsgTime = "";
	private String userLastMsgFromOrTo = "0";
	private String commonFriends="";
	private String isOnline = AppConstants.OFFLINE;
	private String isFavorite;
	private String BodyTypeId = "";
	private String roleId = "";
	private String loveHookup = "";
	private String lastActiveDate = "";
	private boolean isSelected = false;
	private boolean isLoginUser = false;
	private WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
	private LatLongDTO latLongDTO = new LatLongDTO();
	private List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
	private List<InterestsDTO> interestList = new ArrayList<InterestsDTO>();
	private List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
	private List<UserProfileImageDTO> userPublicProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
	public String getUserID() {
		System.out.println(userID);
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(String isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	public WhatAreYouDTO getWhatAreYouDTO() {
		return whatAreYouDTO;
	}

	public void setWhatAreYouDTO(WhatAreYouDTO whatAreYouDTO) {
		this.whatAreYouDTO = whatAreYouDTO;
	}

	public List<InterestsDTO> getInterestList() {
		return interestList;
	}

	public void setInterestList(List<InterestsDTO> interestList) {
		this.interestList = interestList;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UserProfileImageDTO> getUserProfileImageDTOs() {
		return userProfileImageDTOs;
	}

	public void setUserProfileImageDTOs(
			List<UserProfileImageDTO> userProfileImageDTOs) {
		this.userProfileImageDTOs = userProfileImageDTOs;
	}

	public LatLongDTO getLatLongDTO() {
		return latLongDTO;
	}

	public void setLatLongDTO(LatLongDTO latLongDTO) {
		this.latLongDTO = latLongDTO;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getCommonFriends() {
		return commonFriends;
	}

	public void setCommonFriends(String commonFriends) {
		this.commonFriends = commonFriends;
	}

	public List<FacebookLikesDTO> getFacebookLikesDTOs() {
		return facebookLikesDTOs;
	}

	public void setFacebookLikesDTOs(List<FacebookLikesDTO> facebookLikesDTOs) {
		this.facebookLikesDTOs = facebookLikesDTOs;
	}

	public boolean isFacebookLikeEnable() {
		return isFacebookLikeEnable;
	}

	public void setFacebookLikeEnable(boolean isFacebookLikeEnable) {
		this.isFacebookLikeEnable = isFacebookLikeEnable;
	}

	public String isFavorite() {
		return isFavorite;
	}
	
	public void setFavorite(String isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	public String getUnreadMsg() {
		return unreadMsg;
	}

	public void setUnreadMsg(String unreadMsg) {
		this.unreadMsg = unreadMsg;
	}

	public String getUserLastMsg() {
		return userLastMsg;
	}

	public void setUserLastMsg(String userLastMsg) {
		this.userLastMsg = userLastMsg;
	}

	public String getBodyTypeId(){
		return BodyTypeId;
	}
	
	public void setBodyType(String BodyTypeId){
		this.BodyTypeId = BodyTypeId;
	}
	
	public String getLoveHookup(){
		return loveHookup;
	}
	
	public void setLoveHookup(String loveHookup) {
		this.loveHookup = loveHookup;
	}
	
	public String getRoleId(){
		return roleId;
	}
	
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getUserLastMsgTime() {
		return userLastMsgTime;
	}

	public void setUserLastMsgTime(String userLastMsgTime) {
		this.userLastMsgTime = userLastMsgTime;
	}

	public String getUserLastMsgFromOrTo() {
		return userLastMsgFromOrTo;
	}

	public void setUserLastMsgFromOrTo(String userLastMsgFromOrTo) {
		this.userLastMsgFromOrTo = userLastMsgFromOrTo;
	}

	public String getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(String lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public List<UserProfileImageDTO> getUserPublicProfileImageDTOs() {
		return userPublicProfileImageDTOs;
	}

	public void setUserPublicProfileImageDTOs(
			List<UserProfileImageDTO> userPublicProfileImageDTOs) {
		this.userPublicProfileImageDTOs = userPublicProfileImageDTOs;
	}

	public boolean isLoginUser() {
		return isLoginUser;
	}

	public void setLoginUser(boolean isLoginUser) {
		this.isLoginUser = isLoginUser;
	}
}
