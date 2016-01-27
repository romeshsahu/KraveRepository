package com.krave.kraveapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class FragmentNightlifeFinder extends Fragment {
	WebView webView;
	private Activity_Home context;
	// TransparentProgressDialog progressDialog;
	private ImageView loadingView;
	private LinearLayout llLoading;
	AppManager singleton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_daily_krave_with_web_view, container, false);
		System.gc();
		context = (Activity_Home) getActivity();
		// progressDialog = new TransparentProgressDialog(context);
		// progressDialog.show();
		/* av singleton */
		AppManager.initInstance();
		singleton = AppManager.getInstance();

		loadingView = (ImageView) view.findViewById(R.id.loadingView);
		llLoading = (LinearLayout) view.findViewById(R.id.llLoading);
		singleton.progressLoading(loadingView, llLoading);

		webView = (WebView) view.findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// progressDialog.show();
				Log.d("", "shouldOverrideUrlLoading=" + url);
				if (!url.equals("http://kraveapp.net/night-life/")) {
					context.back_button.setVisibility(View.VISIBLE);
					context.left_button.setVisibility(View.GONE);
				}

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
				if (url.equals("http://kraveapp.net/night-life/")) {
					context.left_button.setVisibility(View.VISIBLE);
					context.back_button.setVisibility(View.GONE);
				}
			}

		});
		context.back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (webView != null) {
					if (webView.canGoBack()) {
						// progressDialog.show();
						webView.goBack();
						Log.d("", "canGoBack");
					}
					// if (!webView.canGoBack()) {
					// Log.d("", "canGoBacknot");
					// context.left_button.setVisibility(View.VISIBLE);
					// context.back_button.setVisibility(View.GONE);
					// }
				}
			}
		});
		webView.loadUrl("http://www.kraveapp.net/night-life/");

		return view;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		context.back_button.setVisibility(View.GONE);
		context.left_button.setVisibility(View.VISIBLE);
	}
}