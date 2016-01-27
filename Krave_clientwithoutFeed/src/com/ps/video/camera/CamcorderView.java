package com.ps.video.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CamcorderView extends SurfaceView implements
		SurfaceHolder.Callback {

	private boolean recording = false;
	// MediaRecorder recorder;
	SurfaceHolder holder;
	// public static String Path = Environment.getExternalStorageDirectory()
	// .getAbsolutePath() + File.separator + "default.mp4";
	private Camera camera;

	Context context;

	// private void calculatePath() {
	// File imageFileFolder = new File(Environment
	// .getExternalStorageDirectory().getAbsolutePath(), "/krave/");
	// imageFileFolder.mkdir();
	// FileOutputStream out = null;
	// Calendar c = Calendar.getInstance();
	// String date = fromInt(c.get(Calendar.MONTH))
	// + fromInt(c.get(Calendar.DAY_OF_MONTH))
	// + fromInt(c.get(Calendar.YEAR))
	// + fromInt(c.get(Calendar.HOUR_OF_DAY))
	// + fromInt(c.get(Calendar.MINUTE))
	// + fromInt(c.get(Calendar.SECOND));
	// Path = new File(imageFileFolder, date.toString() + ".mp4")
	// .getAbsolutePath();
	// }

	public String fromInt(int val) {
		return String.valueOf(val);
	}

	public CamcorderView(Context context, Camera camera) {

		super(context);
		this.context = context;
		this.camera = camera;
		// this.recorder = recorder;
		holder = getHolder();
		holder.addCallback(this);
		// calculatePath();
	}

	public void surfaceCreated(SurfaceHolder holder) {

		try {

			camera.setPreviewDisplay(holder);
			camera.startPreview();
		}

		catch (Exception e1) {

			e1.printStackTrace();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		this.getHolder().removeCallback(this);

	}

	MediaRecorder recorder;

	public void startRecording(File OutputFile, MediaRecorder recorder) {
		this.recorder = recorder;
		camera.unlock();
		recorder.setPreviewDisplay(holder.getSurface());
		recorder.setCamera(camera);

		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

		CamcorderProfile cpHigh = CamcorderProfile
				.get(CamcorderProfile.QUALITY_480P);
		recorder.setProfile(cpHigh);

		recorder.setMaxDuration(60000);

		recorder.setOutputFile(OutputFile.getAbsolutePath());

		if (recorder != null) {

			try {

				camera.stopPreview();
				recorder.prepare();

				recorder.start();
				recording = true;

			}

			catch (IllegalStateException e) {

				Log.e("IllegalStateException", e.toString());
			}

			catch (IOException e) {

				Log.e("IOException", e.toString());
			} catch (Exception e) {

				Log.e("Exception", e.toString());
			}
		}

	}

	public void stopRecording() {
		try {
			recorder.stop();
			recorder.release();
			camera.lock();
			// camera.stopPreview();
			camera.release();

			recording = false;
		} catch (Exception exception) {

		}
	}

}
