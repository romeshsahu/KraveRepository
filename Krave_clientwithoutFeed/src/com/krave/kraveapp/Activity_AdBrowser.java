package com.krave.kraveapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class Activity_AdBrowser extends Activity {
	WebView webView;
	private Activity_Home context;
	// TransparentProgressDialog progressDialog;
	private ImageView loadingView;
	private LinearLayout llLoading;
	AppManager singleton;
	private Button backButton;
	private String browserLinkUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ad_browser);
		System.gc();
		browserLinkUrl = getIntent().getExtras().getString("url");
//		if (!browserLinkUrl.contains("http://www")) {
//			browserLinkUrl = "http://www." + browserLinkUrl;
//		}
		AppManager.initInstance();
		singleton = AppManager.getInstance();

		loadingView = (ImageView) findViewById(R.id.loadingView);
		llLoading = (LinearLayout) findViewById(R.id.llLoading);
		singleton.progressLoading(loadingView, llLoading);
		backButton = (Button) findViewById(R.id.back_button);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// // TODO Auto-generated method stub
				// // progressDialog.show();
				// Log.d("", "shouldOverrideUrlLoading=" + url);
				// if (!url.equals("http://kraveapp.net/blog/")) {
				// context.back_button.setVisibility(View.VISIBLE);
				// context.left_button.setVisibility(View.GONE);
				// }

				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				Log.d("", "onPageFinished");
				// progressDialog.dismiss();
				webView.setVisibility(View.VISIBLE);
				singleton.stopLoading(llLoading);
				// if (url.equals("http://kraveapp.net/blog/")) {
				// context.left_button.setVisibility(View.VISIBLE);
				// context.back_button.setVisibility(View.GONE);
				// }
			}

		});

		webView.loadUrl(browserLinkUrl);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}
}