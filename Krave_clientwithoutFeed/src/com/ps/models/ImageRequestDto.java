package com.ps.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;

import com.krave.kraveapp.FragmentChatMatches;
import com.krave.kraveapp.R;
import com.ps.utill.Utils;

public class ImageRequestDto {

	// final String alert = jsonObject.getString("message");
	//
	// final String userid = jsonObject.getString("user_id");
	// final String ownerId = jsonObject.getString("owner_id");
	// final String imageId = jsonObject.getString("image_id");
	// final String fName = jsonObject.getString("user_fname");
	// final String lName = jsonObject.getString("user_lname");
	private String notificationId;
	private String alert;
	private String notificationTime;
	private String userid;
	private String ownerId;
	private String imageId;
	private String fName;
	private String lName;
	private String profileImage;
	private String jsonString;

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public void setJsonObject(JSONObject jsonObject , Context context) {
		// [{"status":"success","notification":[{"notification_id":"20","user_id":"360",
		// "owner_id":"15282","image_id":"22931","user_fname":"danny","user_lname":"",
		// "message":"You have an image request","response":"0"}]}]

		try {
			notificationId = jsonObject.getString("notification_id");
			notificationTime = Utils
					.convertServerDateTimeToLocalDateTime(jsonObject
							.getString("created")); // 2015-07-27 13:00:29 date
													// format
			userid = jsonObject.getString("user_id");
			ownerId = jsonObject.getString("owner_id");
			imageId = jsonObject.getString("image_id");
			fName = jsonObject.getString("user_fname");
			lName = jsonObject.getString("user_lname");
			profileImage = jsonObject.getString("user_image");
			String message = jsonObject.getString("message");

			if (message.contains("You have an image request")) {

				alert = context.getResources().getString(
						R.string.notification_you_have_an_image_request_);

			} else if (message.contains("You have an image response")) {

				if (jsonObject.getString("response").equals("1")) {// approved
																	// photo
																	// request
					alert = context.getResources().getString(
							R.string.notification_your_request_is_accepted_);
				} else {// reject photo request
					alert = context.getResources().getString(
							R.string.notification_your_request_is_declined_);
				}

			}

			jsonString = jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String jsonString() {
		return jsonString;

	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getNotificationTime() {
		return notificationTime;
	}

	public void setNotificationTime(String notificationTime) {
		this.notificationTime = notificationTime;
	}
}
