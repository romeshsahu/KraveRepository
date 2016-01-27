package com.krave.kraveapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ps.models.ChatDetailsDTO;
import com.ps.utill.SessionManager;

public class Activity_Show_map extends Activity {
	// static final LatLng location = new LatLng(53.558, 9.927);
	// static final LatLng KIEL = new LatLng(53.551, 9.993);
	public static ChatDetailsDTO chatMessageObj;
	private GoogleMap map;
	private LinearLayout cancelLayout;
	private SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_map);
		// Toast.makeText(getBaseContext(), "" + chatMessageObj.getMessage(),
		// Toast.LENGTH_SHORT).show();
		sessionManager = new SessionManager(getBaseContext());
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		cancelLayout = (LinearLayout) findViewById(R.id.cancleLayout);
		cancelLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
		try {
			double latitude = Double.valueOf(chatMessageObj.getMessage().split(
					"\\|")[0]);
			double longitude = Double.valueOf(chatMessageObj.getMessage()
					.split("\\|")[1]);
			String address = chatMessageObj.getMessage().split("\\|")[2];
			LatLng location = new LatLng(latitude, longitude);
			String name = "";
			if (sessionManager.getUserDetail().getUserID()
					.equals(chatMessageObj.getFromuser())) {
				name = sessionManager.getUserDetail().getFirstName() + " "
						+ sessionManager.getUserDetail().getLastName();
			} else {
				name = FragmentChatMatches.userDTO.getFirstName() + " "
						+ FragmentChatMatches.userDTO.getLastName();
			}
			Marker kiel = map.addMarker(new MarkerOptions().position(location)
					.title(name).snippet(address));
			// .icon(BitmapDescriptorFactory
			// .fromResource(R.drawable.ic_launcher)));

			// Move the camera instantly to hamburg with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 100));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		} catch (Exception exception) {
			Toast.makeText(getBaseContext(),  R.string.toast_location_points_not_available,
					Toast.LENGTH_SHORT).show();
		}

	}
}
