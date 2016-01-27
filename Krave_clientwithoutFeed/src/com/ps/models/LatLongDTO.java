package com.ps.models;

public class LatLongDTO extends AddressDTO {
	String userID;
	String latitude;
	String longitude;
	String distance;

	public String getUser_id() {
		return userID;
	}

	public void setUser_id(String user_id) {
		userID = user_id;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
