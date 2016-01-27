package com.image.crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.krave.kraveapp.R;
import com.ps.gallery.Custom_Gallery_Activity;

import eu.janmuller.android.simplecropimage.CropImage;

public class Gallery_Activity extends Activity {
	// priyank.deolekar@gmail.com
	Button Gallery, Camera;
	LinearLayout cancleLayout, deleteLayout;
	private String picturePath;
	public static final String CROP_VERSION_SELECTED_KEY = "crop";
	public static final int VERSION_2 = 2;
	public static final int VERSION_1 = 1;
	public static Bitmap pictureObject;
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ELv - April 10, 2015
		// Changed alert dialog to custom dialog
		mDialog = new Dialog(this, R.style.TransparentProgressDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		mDialog.setContentView(R.layout.dialog_gallery_activity);

		Button bTakePicture = (Button) mDialog
				.findViewById(R.id.dialog_button_take_picture);
		Button bFromGallery = (Button) mDialog
				.findViewById(R.id.dialog_button_from_gallery);
		Button bMakeAsProfileImage = (Button) mDialog
				.findViewById(R.id.dialog_button_for_make_as_profile_image);

		Button bDeletePicture = (Button) mDialog
				.findViewById(R.id.dialog_button_delete_picture);
		Button bCancel = (Button) mDialog
				.findViewById(R.id.dialog_button_cancel);

		bTakePicture.setOnClickListener(myDialogButtonListener);
		bFromGallery.setOnClickListener(myDialogButtonListener);
		bMakeAsProfileImage.setOnClickListener(myDialogButtonListener);
		bDeletePicture.setOnClickListener(myDialogButtonListener);
		bCancel.setOnClickListener(myDialogButtonListener);

		// Visibility of Delete Button
		if (getIntent().getExtras().getBoolean("delete")) {
			bDeletePicture.setVisibility(View.VISIBLE);

			bTakePicture.setVisibility(View.GONE);
			bFromGallery.setVisibility(View.GONE);
		} else {
			bDeletePicture.setVisibility(View.GONE);
			bTakePicture.setVisibility(View.VISIBLE);
			bFromGallery.setVisibility(View.VISIBLE);

		}
		if (getIntent().getExtras().getBoolean("profile_image")) {
			bMakeAsProfileImage.setVisibility(View.VISIBLE);
		} else {
			bMakeAsProfileImage.setVisibility(View.GONE);
		}

		mDialog.show();

		// Add onCancelListener to finish activity
		mDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();

			}
		});

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_new_01);
	}

	OnClickListener myDialogButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mDialog.hide();
			if (v.getId() == R.id.dialog_button_take_picture) {
				dispatchTakePictureIntent();
			} else if (v.getId() == R.id.dialog_button_from_gallery) {
				Intent i = new Intent(Gallery_Activity.this,
						Custom_Gallery_Activity.class);
				startActivityForResult(i, 200);
			} else if (v.getId() == R.id.dialog_button_delete_picture) {
				Intent intent = new Intent();
				intent.putExtra("delete", true);
				setResult(RESULT_OK, intent);
				mDialog.cancel();
				finish();
			} else if (v.getId() == R.id.dialog_button_for_make_as_profile_image) {
				Intent intent = new Intent();
				intent.putExtra("profile_image", true);
				setResult(RESULT_OK, intent);
				mDialog.cancel();
				finish();
			} else {
				setResult(RESULT_CANCELED);
				mDialog.cancel();
			}
		}
	};

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				// ...
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, 400);
			}
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"krave/Temp");
		;
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		picturePath = image.getAbsolutePath();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 200 && resultCode == Activity.RESULT_OK
				&& data != null) {
			picturePath = data.getStringExtra("single_path");
			startCropImage();

		} else if (requestCode == 400 && resultCode == RESULT_OK) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;

				imageOreintationValidator(
						BitmapFactory.decodeFile(picturePath, options),
						picturePath);
				startCropImage();
			} catch (Exception e) {
				finish();
			}

		} else

		if (requestCode == 300 && data != null) {
			Intent intent = getIntent();
			intent.putExtra("delete", false);
			intent.putExtra("path", picturePath);
			setResult(RESULT_OK, intent);
			finish();
		} else {
			finish();
		}

	}

	private void startCropImage() {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, picturePath);
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		startActivityForResult(intent, 300);
	}

	private void LoadBitmap(Bitmap bitmap) {

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(picturePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);

			fos.flush();
			fos.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

		ExifInterface ei;
		try {
			ei = new ExifInterface(path);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				bitmap = rotateImage(bitmap, 90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				bitmap = rotateImage(bitmap, 180);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				bitmap = rotateImage(bitmap, 270);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// LoadBitmap(bitmap);
		return bitmap;
	}

	private Bitmap rotateImage(Bitmap source, float angle) {

		Bitmap bitmap = null;
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		try {
			bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), matrix, true);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}

		return bitmap;
	}

}
