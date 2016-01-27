package com.ps.models;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

public class AdDTO {

	private String adId;
	private String adName;
	private String adImageLink;
	private String adBrowserLink;

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getAdImageLink() {
		return adImageLink;
	}

	public void setAdImageLink(String adImageLink) {
		this.adImageLink = adImageLink;
	}

	public String getAdBrowserLink() {
		return adBrowserLink;
	}

	public void setAdBrowserLink(String adBrowserLink) {
		this.adBrowserLink = adBrowserLink;
	}

	public void parse(JSONObject jsonObject) throws JSONException {

		adId = jsonObject.getString("add_id");
		adName = jsonObject.getString("name");
		adImageLink = jsonObject.getString("image");
		adBrowserLink = jsonObject.getString("url");

	}
}
