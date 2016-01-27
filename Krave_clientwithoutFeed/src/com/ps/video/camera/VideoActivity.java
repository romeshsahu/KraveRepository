package com.ps.video.camera;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krave.kraveapp.AppManager;
import com.krave.kraveapp.R;
import com.ps.gallery.Custom_Gallery_Activity;
import com.tag.trivialdrivesample.util.InAppPurchaseActivity;

public class VideoActivity extends InAppPurchaseActivity implements OnClickListener,
		OnTouchListener, OnLongClickListener {

	private CamcorderView videoPreview;
	private Camera mCamera;
	private MediaRecorder recorder;
	private RelativeLayout captureLayout;
	private FrameLayout layout;
	private ProgressBar click;
	private LinearLayout change_camera_view, flash_layout, timerLayout,
			galleryLayout;
	private ImageView done_button;
	private ImageView cancel_button;
	private TextView timerTextView, flashTextView;

	private boolean recording = false;
	private int flag = 0;
	private boolean cameraType = true;
	private boolean loadOnResume = true;
	private boolean isFlashOn = false;
	private int pStatus = 0;
	private WakeLock wakeLock;
	private String path = "";
	private int RESULT_LOAD_IMAGE = 101;
	private AppManager singleton;

	// private Animation startAnimation, endAnimation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.cpature_main);
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		mediaPlayer = new MediaPlayer();
		// startAnimation = AnimationUtils.loadAnimation(this,
		// R.anim.start_scale);
		// endAnimation = AnimationUtils.loadAnimation(this, R.anim.end_scale);
		setLayout();
		setListener();
		setButtonVisibility(true);

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"My Lock");
		wakeLock.acquire();

	}

	private void setListener() {
		click.setOnTouchListener(this);
		click.setOnClickListener(this);

		click.setOnLongClickListener(this);

		change_camera_view.setOnClickListener(this);
		done_button.setOnClickListener(this);
		cancel_button.setOnClickListener(this);
		flash_layout.setOnClickListener(this);
		galleryLayout.setOnClickListener(this);
	}

	private void setLayout() {
		layout = (FrameLayout) findViewById(R.id.view);

		done_button = (ImageView) findViewById(R.id.done_button);

		cancel_button = (ImageView) findViewById(R.id.cancel_button);
		change_camera_view = (LinearLayout) findViewById(R.id.video_camera_view);
		flash_layout = (LinearLayout) findViewById(R.id.flash_light_layout);
		timerLayout = (LinearLayout) findViewById(R.id.timerLayout);
		galleryLayout = (LinearLayout) findViewById(R.id.galleryLayout);
		click = (ProgressBar) findViewById(R.id.captureimg);
		captureLayout = (RelativeLayout) findViewById(R.id.captureLayout);
		timerTextView = (TextView) findViewById(R.id.timerTextView);
		flashTextView = (TextView) findViewById(R.id.flashTextView);
		if (isFlashOn) {
			flashTextView.setText("ON");
		} else {
			flashTextView.setText("OFF");
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		// ReleaseCamera();
		wakeLock.release();

	}

	public void getCamera() {
		try {
			recorder = new MediaRecorder();
			if (cameraType) {
				mCamera = Camera.open(0);
				setCameraDisplayOrientation(VideoActivity.this, 0, mCamera);

				if (isFlashOn
						&& getPackageManager().hasSystemFeature(
								PackageManager.FEATURE_CAMERA_FLASH)) {
					Parameters p = mCamera.getParameters();
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(p);
				}
				recorder.setOrientationHint(90);
			} else {
				mCamera = Camera.open(1);

				setCameraDisplayOrientation(VideoActivity.this, 1, mCamera);
				recorder.setOrientationHint(270);
			}
			// mCamera.setDisplayOrientation(90);

			mCamera.lock();
		} catch (Exception exception) {

		}
	}

	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, Camera camera) {
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	@Override
	protected void onPause() {

		super.onPause();

	}

	@Override
	protected void onResume() {

		super.onResume();
		if (loadOnResume) {
			loadOnResume=true;
			LoadVideoCapture();
			
		}
		
	}

	public void LoadVideoCapture() {

		ReleaseCamera();
		getCamera();
		// mCamera.unlock();
		videoPreview = new CamcorderView(this, mCamera);
		layout.removeAllViews();
		layout.addView(videoPreview);
		flag = 1;
	}

	public void ReleaseCamera() {
		try {
			if (mCamera != null) {

				// if(recorder.)
				// recorder.stop();
				recorder.release();
				recorder = null;
				mCamera.lock();
				mCamera.stopPreview();
				mCamera.release();

				mCamera = null;
			}
			// if (recorder != null) {
			// recorder.stop();
			// recorder.release();
			// recorder = null;
			// }
			// if (mCamera != null) {
			// mCamera.lock();
			// mCamera.stopPreview();
			// mCamera.release();
			// mCamera = null;
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {

		if (click == view) {
			// Toast.makeText(VideoActivity.this, "onClick", Toast.LENGTH_SHORT)
			// .show();

			try {
				mCamera.takePicture(null, null, mPicture);
			} catch (Exception e) {

			}

		} else if (galleryLayout == view) {
			// Toast.makeText(VideoActivity.this, "onClick", Toast.LENGTH_SHORT)
			// .show();
			//
			// mCamera.takePicture(null, null, mPicture);
			openGallery();

		}

		else if (change_camera_view == view) {
			if (!recording && !(cancel_button.getVisibility() == View.VISIBLE)) {
				cameraType = !cameraType;
				LoadVideoCapture();
			}

		} else if (flash_layout == view) {
			if (!recording && !(cancel_button.getVisibility() == View.VISIBLE)) {
				isFlashOn = !isFlashOn;
				if (isFlashOn) {
					flashTextView.setText("ON");
				} else {
					flashTextView.setText("OFF");
				}
				LoadVideoCapture();

			}

		}

		else if (done_button == view) {
			// ReleaseCamera();

			Intent intent = new Intent();

			intent.putExtra("path", path);
			if (path.contains(".mp4")) {
				stopMediaPlayer();
				intent.putExtra("media_type", "video");
			} else {
				if (cameraType) {
					intent.putExtra("cameraType", 90);
				} else {
					intent.putExtra("cameraType", 270);
				}
				intent.putExtra("media_type", "image");
			}
			setResult(RESULT_OK, intent);
			finish();
		}

		else if (cancel_button == view) {
			setButtonVisibility(true);
			if (path.contains(".mp4")) {
				stopMediaPlayer();
			}

			mCamera = null;
			pStatus = 0;

			LoadVideoCapture();
		}
	}

	public void openGallery() {

		Intent i = new Intent(VideoActivity.this, Custom_Gallery_Activity.class);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		
		
		if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {

		
//			Intent intent = new Intent();
//
//			intent.putExtra("path", vImagePath);
//
//			intent.putExtra("cameraType", 0);
//
//			intent.putExtra("media_type", "image");
//
//			setResult(RESULT_OK, intent);
//			finish();
			loadOnResume=false;
			ReleaseCamera();
			setButtonVisibility(false);
			 path = data.getStringExtra("single_path");
			View view = getLayoutInflater().inflate(
					R.layout.gallery_image_layot, null);
			ImageView imageView=(ImageView) view.findViewById(R.id.imageView);
			imageView.setImageBitmap(BitmapFactory.decodeFile(path));
			layout.removeAllViews();
			layout.addView(view);
		}
	}

	private void setButtonVisibility(boolean visible) {
		// capture button visible or not
		if (visible) {
			click.setProgress(0);
			timerTextView.setText("0 sec");

			captureLayout.setVisibility(View.VISIBLE);
			change_camera_view.setVisibility(View.VISIBLE);
			flash_layout.setVisibility(View.VISIBLE);
			done_button.setVisibility(View.GONE);
			cancel_button.setVisibility(View.GONE);

		} else {
			captureLayout.setVisibility(View.INVISIBLE);
			change_camera_view.setVisibility(View.GONE);
			flash_layout.setVisibility(View.GONE);
			done_button.setVisibility(View.VISIBLE);
			cancel_button.setVisibility(View.VISIBLE);

		}

	}

	@Override
	public boolean onLongClick(View v) {
		// Toast.makeText(VideoActivity.this, "onLongClick", Toast.LENGTH_SHORT)
		// .show();
		if (singleton.isPaidUser) {
			isLongClick = true;
			v.setScaleX(1.3f);
			v.setScaleY(1.3f);
			Capture();
			return false;
		} else {
			subscribeToPaidAccount();
			return true;
		}
	}

	boolean isLongClick = false;

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			// Toast.makeText(VideoActivity.this, "ACTION_DOWN",
			// Toast.LENGTH_SHORT).show();
			// v.setScaleX(1.3f);
			// v.setScaleY(1.3f);
			// Capture();
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_CANCEL:
			// Toast.makeText(VideoActivity.this, "ACTION_UP",
			// Toast.LENGTH_SHORT)
			// .show();
			// v.startAnimation(endAnimation);
			if (isLongClick) {
				isLongClick = false;
				v.setScaleX(1f);
				v.setScaleY(1f);
				Capture();
				handler.postDelayed(playerThread, 200);
				return true;
			}

			break;

		}

		return false;

	}

	public void Capture() {
		// Toast.makeText(VideoActivity.this, "Capture", Toast.LENGTH_SHORT)
		// .show();
		if (flag == 1) {

			if (recording) {
				setButtonVisibility(false);
				recording = false;
				pStatus = 100;

			} else {

				pStatus = 0;
				recording = true;
				// change_camera_view.setEnabled(true);
				videoPreview
						.startRecording(getOutputMediaFile(false), recorder);
				// thread = new Thread(myThread);
				// thread.start();
				progressBarThread = new ProgressBarThread();
				progressBarThread.start();
			}
		}
	}

	private Handler handler = new Handler();
	// private Handler handlerPlayer = new Handler();
	private ProgressBarThread progressBarThread;
	private MediaPlayer mediaPlayer;

	class ProgressBarThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (pStatus <= 100) {

				handler.post(new Runnable() {

					@Override
					public void run() {

						click.setProgress(pStatus);
						int time = ((pStatus / 10));
						timerTextView.setText("" + time + " sec");

					}
				});
				try {
					// Sleep for 100 milliseconds.
					// Just to display the progress slowly
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pStatus++;
			}
			handler.post(new Runnable() {

				@Override
				public void run() {
					videoPreview.stopRecording();
					setButtonVisibility(false);
				}
			});

		}

	}

	public void playvideo() {

		// if (mediaPlayer.isPlaying()) {
		// mediaPlayer.reset();
		// }
		FileDescriptor fd = null;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDisplay(videoPreview.holder);

			FileInputStream fis = new FileInputStream(path);
			fd = fis.getFD();
			mediaPlayer.setDataSource(fd);
			mediaPlayer.setLooping(true);
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();

	}

	private void stopMediaPlayer() {
		try {
			handler.removeCallbacks(playerThread);
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	Thread playerThread = new Thread(new Runnable() {
		public void run() {
			// try {
			// Thread.sleep(5000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			playvideo();
		}
	});

	public void onBackPressed() {
		super.onBackPressed();
		stopMediaPlayer();
		ReleaseCamera();
	};

	PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// ReleaseCamera();
			camera.lock();

			camera.release();
			setButtonVisibility(false);
			File pictureFile = getOutputMediaFile(true);
			if (pictureFile == null) {
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}

		}
	};

	private File getOutputMediaFile(boolean isImage) {
		File imageFileFolder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath(), "/krave/");
		imageFileFolder.mkdir();
		FileOutputStream out = null;
		Calendar c = Calendar.getInstance();
		String date = fromInt(c.get(Calendar.MONTH))
				+ fromInt(c.get(Calendar.DAY_OF_MONTH))
				+ fromInt(c.get(Calendar.YEAR))
				+ fromInt(c.get(Calendar.HOUR_OF_DAY))
				+ fromInt(c.get(Calendar.MINUTE))
				+ fromInt(c.get(Calendar.SECOND));
		File file = null;
		if (isImage) {
			file = new File(imageFileFolder, date.toString() + ".jpg");
			// ExifInterface exifInterface;
			// try {
			// exifInterface = new ExifInterface(
			// file.getAbsolutePath());
			// exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "90");
			// exifInterface.saveAttributes();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

		} else {
			file = new File(imageFileFolder, date.toString() + ".mp4");

		}
		path = file.getAbsolutePath();
		return file;
	}

	public String fromInt(int val) {
		return String.valueOf(val);
	}

}