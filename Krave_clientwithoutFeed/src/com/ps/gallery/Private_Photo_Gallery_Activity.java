package com.ps.gallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.krave.kraveapp.R;
import com.ps.models.UserProfileImageDTO;
import com.ps.utill.ImageLoader;
import com.ps.utill.SessionManager;

public class Private_Photo_Gallery_Activity extends Activity {
	GridView gridGallery;
	ImageButton back;
	Handler handler;
	GalleryAdapter adapter;
	private ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery);
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		init();
	}

	private void init() {

		handler = new Handler();
		gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(Private_Photo_Gallery_Activity.this,
				new ImageLoader(Private_Photo_Gallery_Activity.this),
				getPrivatePhotos());
		gridGallery.setAdapter(adapter);
		gridGallery.setOnItemClickListener(mItemSingleClickListener);

	}

	AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			CustomGallery item = adapter.getItem(position);
			String path = LoadBitmap(new ImageLoader(
					Private_Photo_Gallery_Activity.this)
					.getBitmap(item.sdcardPath));
			Intent data = new Intent().putExtra("single_path", path);
			setResult(RESULT_OK, data);
			finish();
		}
	};

	private String LoadBitmap(Bitmap bitmap) {
		File outputDir = this.getCacheDir(); // context being the Activity
												// pointer
		File outputFile = null;
		try {
			outputFile = File.createTempFile("prefix", ".jpg", outputDir);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// String strMyImagePath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/test.png";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

			fos.flush();
			fos.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return outputFile.getAbsolutePath();
	}

	private ArrayList<CustomGallery> getPrivatePhotos() {
		ArrayList<CustomGallery> galleryList = new ArrayList<CustomGallery>();

		try {
			List<UserProfileImageDTO> list = new SessionManager(
					Private_Photo_Gallery_Activity.this).getUserDetail()
					.getUserProfileImageDTOs();

			if (list != null && list.size() > 0) {

				for (UserProfileImageDTO userProfileImageDTO : list) {
					if (!userProfileImageDTO.isPublic()) {
						CustomGallery item = new CustomGallery();

						item.sdcardPath = userProfileImageDTO.getImagePath();

						galleryList.add(item);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return galleryList;
	}

	public class GalleryAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater infalter;
		private ArrayList<CustomGallery> data;
		ImageLoader imageLoader;

		public GalleryAdapter(Context c, ImageLoader imageLoader,
				ArrayList<CustomGallery> list) {
			infalter = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mContext = c;
			this.imageLoader = imageLoader;
			this.data = list;
			// clearCache();
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public CustomGallery getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {

				convertView = infalter.inflate(R.layout.gallery_item, null);
				holder = new ViewHolder();
				holder.imgQueue = (ImageView) convertView
						.findViewById(R.id.imgQueue);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imgQueue.setTag(position);

			try {

				imageLoader.DisplayImage(data.get(position).sdcardPath,
						holder.imgQueue);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}

		public class ViewHolder {
			ImageView imgQueue;
		}
	}

}
