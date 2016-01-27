package com.ps.models;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ProgressBar;

public class ChatDetailsDTO {
	private String message;
	private String date;
	private String fromuser;
	private String touser;
	private String id;
	private String time;
	private String pin;
	private String meassageType;
	private View view;
	private ProgressBar progressBar;
	private int imageSnapChatLeftTime=-10;
	private byte[] image;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFromuser() {
		return fromuser;
	}

	public void setFromuser(String fromuser) {
		this.fromuser = fromuser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getMeassageType() {
		return meassageType;
	}

	public void setMeassageType(String meassageType) {
		this.meassageType = meassageType;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public int getImageSnapChatLeftTime() {
		return imageSnapChatLeftTime;
	}

	public void setImageSnapChatLeftTime(int imageSnapChatLeftTime) {
		this.imageSnapChatLeftTime = imageSnapChatLeftTime;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

}
