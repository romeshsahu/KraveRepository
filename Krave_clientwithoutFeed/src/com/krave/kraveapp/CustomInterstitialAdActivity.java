package com.krave.kraveapp;

import com.ps.models.AdDTO;
import com.ps.utill.ClickableWebView;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderForAds;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;

public class CustomInterstitialAdActivity extends Activity {
	public static AdDTO adDTO;
	private ClickableWebView adImageView;
	ImageView cancleImageView;
	private ImageLoaderForAds imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_custom_interstitial_ad);
		try {

			adImageView = (ClickableWebView) findViewById(R.id.adImageView);
			cancleImageView = (ImageView) findViewById(R.id.cancel);

			adImageView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			adImageView.setPadding(0, 0, 0, 0);
			adImageView.setInitialScale(getScale());
			adImageView.getSettings().setJavaScriptEnabled(true);
			adImageView.getSettings().setLoadWithOverviewMode(true);
			adImageView.getSettings().setUseWideViewPort(true);

			adImageView.setHorizontalScrollBarEnabled(false);
			adImageView.setVerticalScrollBarEnabled(false);
			adImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// if (!browserLinkUrl.contains("http://www")) {
						// browserLinkUrl = "http://www."
						// + browserLinkUrl;
						// }
						Intent browserIntent = new Intent(
								CustomInterstitialAdActivity.this,
								Activity_AdBrowser.class);
						browserIntent.putExtra("url", adDTO.getAdBrowserLink());
						startActivity(browserIntent);
						finish();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			adImageView.loadUrl(adDTO.getAdImageLink());

		} catch (Exception e) {
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cancleImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	private int getScale() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();
		Double val = new Double(width) / new Double(315);
		val = val * 100d;
		return val.intValue();
	}
	// @Override
	// public void onClick(View v) {
	// if (v.getId() == R.id.adImageView) {
	// finish();
	// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
	// Uri.parse(adDTO.getAdBrowserLink()));
	// startActivity(browserIntent);
	//
	// } else if (v.getId() == R.id.cancel) {
	// finish();
	// }
	//
	// }

}