package com.ps.models;

public class UserProfileImageDTO {
	private String imageId = "";
	private String imagePath = "";
	private String imagePosition = "";
	private boolean isImageActive = false;
	private boolean isPublic = false;
	private Boolean isPaidImage = false;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImagePosition() {
		return imagePosition;
	}

	public void setImagePosition(String imagePosition) {
		this.imagePosition = imagePosition;
	}

	public boolean getIsImageActive() {
		return isImageActive;
	}

	public void setIsImageActive(boolean isImageActive) {
		this.isImageActive = isImageActive;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public boolean isPaidImage() {
		return isPaidImage;
	}

	public void setPaidImage(boolean isPaidImage) {
		this.isPaidImage = isPaidImage;
	}

}
