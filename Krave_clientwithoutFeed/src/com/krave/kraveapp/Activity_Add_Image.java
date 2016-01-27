package com.krave.kraveapp;
import com.tag.trivialdrivesample.util.InAppPurchaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class Activity_Add_Image extends InAppPurchaseActivity {
	private static Activity_Add_Image activityObject = null;

	public static Activity_Add_Image getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_Add_Image activityObject) {
		Activity_Add_Image.activityObject = activityObject;
	}

	private ImageView imageView;

	private RelativeLayout saveButton;
	private RadioGroup radioGroup;
	private RadioButton privateRadioButton, publicRadioButton;
	private Button backButton;
	private boolean isPublic = true;
	private AppManager singleton;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activityObject = this;
		setContentView(R.layout.activity_add_public_private_image);
		AppManager.initInstance();
		singleton=AppManager.getInstance();
		String picturePath = getIntent().getExtras().getString("path");
		setLayout();
		setTypeFace();
		Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
		imageView.setImageBitmap(bitmap);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;
	}

	private void setTypeFace() {
		// TextView textView1, textView2;
		//
		// textView2 = (TextView) findViewById(R.id.saveTextView);
		//
		// Typeface typeface = FontStyle.getFont(Activity_Add_Image.this,
		// AppConstants.HelveticaNeueLTStd_Lt);
		//
		// textView2.setTypeface(typeface);
		// aboutMe.setTypeface(typeface);

	}

	private void setLayout() {
		imageView = (ImageView) findViewById(R.id.imageView);

		saveButton = (RelativeLayout) findViewById(R.id.upload_layout);

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		privateRadioButton = (RadioButton) findViewById(R.id.privateRadioButton);
		publicRadioButton = (RadioButton) findViewById(R.id.publicRadioButton);
		backButton = (Button) findViewById(R.id.back_button);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.privateRadioButton) {
					if (getIntent().getBooleanExtra("subscribeOrNot", true) && !singleton.isPaidUser) {
						publicRadioButton.setChecked(true);
						subscribeToPaidAccount();
					} else {
						isPublic = false;
					}
				} else if (checkedId == R.id.publicRadioButton) {
					isPublic = true;
				}

			}
		});
		
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.putExtra("isPublic",isPublic);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}
	// public void openDailog() {
	// final Dialog dialog = new Dialog(Activity_Add_Image.this,
	// android.R.style.Theme_Translucent_NoTitleBar);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.dialog_about_me);
	// // Do you realy want to delete your account ?
	// Button cancle = (Button) dialog.findViewById(R.id.cancle);
	// Button ok = (Button) dialog.findViewById(R.id.ok);
	// TextView title = (TextView) dialog.findViewById(R.id.title);
	// title.setText("Do you want to save changes ?");
	// Typeface typeface = FontStyle.getFont(Activity_Add_Image.this,
	// AppConstants.HelveticaNeueLTStd_Lt);
	// title.setTypeface(typeface);
	// cancle.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// finish();
	// }
	// });
	// ok.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// Intent intent = new Intent();
	// intent.putExtra("about", aboutMe.getText().toString());
	// setResult(RESULT_OK, intent);
	// finish();
	//
	// }
	// });
	// dialog.show();
	// }
}