package com.ps.utill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.krave.kraveapp.Activity_AdBrowser;
import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.AppManager;
import com.krave.kraveapp.CustomInterstitialAdActivity;
import com.krave.kraveapp.R;
import com.ps.models.AdDTO;

public class AdMob {

	// private FrameLayout mAdViewFrameLayout;
	private ClickableWebView slidingimage;
	private AdView adView;
	private Activity_Home activity;
	private ImageLoaderForAds imageLoader;
	private AppManager singleton;

	private int delay = 1000; // delay for 1 sec.

	private int period = 10000; // repeat every 4 sec.
	private int currentimageindex = 0;
	String browserLinkUrl = "";
	private Handler mHandler = new Handler();

	private Runnable mUpdateResults = new Runnable() {
		public void run() {

			AnimateandSlideShow();

		}
	};

	private InterstitialAd mInterstitialAd;

	public AdMob(Activity activity, AppManager singleton) {

		// this.mAdViewFrameLayout = mAdView;
		this.activity = (Activity_Home) activity;
		this.singleton = singleton;
		imageLoader = new ImageLoaderForAds(activity);
		// for (int i = 0; i < drawable.length; i++) {
		// AdDTO adDTO = new AdDTO();
		// adDTO.setAdImageLink(drawable[i]);
		// adDTOs.add(adDTO);
		// }

	}

	public void startAdvertising(FrameLayout mAdViewFrameLayout) {
		mAdViewFrameLayout.removeAllViews();
		if (singleton.adsizeAndLocation.equals(AppConstants.ADS_BOTTOM_BANNER)
				|| singleton.adsizeAndLocation
						.equals(AppConstants.ADS_CENTER_BANNER)) {
			if (singleton.isGoogleAd) {

				AdView adView = new AdView(activity);
				adView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				AdRequest adRequest = new AdRequest.Builder()

				// .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				// .addTestDevice("0CD03379730E1850662B69DFA3CE4C5D")
						.build();
				adView.setAdSize(AdSize.SMART_BANNER);
				// adView.setAdUnitId("ca-app-pub-7656986809480646/1283738818");
				// //
				// demo hypnosis
				adView.setAdUnitId(AppConstants.ADMOB_BANNER_UNIIT_ID);

				mAdViewFrameLayout.addView(adView);

				// Start loading the ad in the background.
				adView.loadAd(adRequest);
			} else {

				if (activity.adDTOs.size() != 0) { // if there is no custom
													// admin ads in server or
													// list so we dont display
													// the ads on the admob bar
					slidingimage = new ClickableWebView(activity);
					slidingimage.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
					slidingimage.setPadding(0, 0, 0, 0);
					slidingimage.setInitialScale(getScale());
					slidingimage.getSettings().setJavaScriptEnabled(true);
					slidingimage.getSettings().setLoadWithOverviewMode(true);
					slidingimage.getSettings().setUseWideViewPort(true);

					
					slidingimage.setHorizontalScrollBarEnabled(false);
					slidingimage.setVerticalScrollBarEnabled(false);
					slidingimage.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								// if (!browserLinkUrl.contains("http://www")) {
								// browserLinkUrl = "http://www."
								// + browserLinkUrl;
								// }
								Intent browserIntent = new Intent(activity,
										Activity_AdBrowser.class);
								browserIntent.putExtra("url", browserLinkUrl);
								activity.startActivity(browserIntent);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					mAdViewFrameLayout.addView(slidingimage);
					// slidingimage.setImageResource(R.drawable.rsz_images);
					mHandler.postDelayed(mUpdateResults, delay);
				} else {
					mAdViewFrameLayout.setVisibility(View.GONE);
				}

			}
		} else {
			startInterstitialAds();
		}

	}

	private int getScale() {
		Display display = ((WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();
		Double val = new Double(width) / new Double(315);
		val = val * 100d;
		return val.intValue();
	}

	private void startInterstitialAds() {
		if (singleton.isGoogleAd) {
			mInterstitialAd = new InterstitialAd(activity);
			mInterstitialAd
					.setAdUnitId(AppConstants.ADMOB_INTERSTITIAL_UNIIT_ID);

			mInterstitialAd.setAdListener(new AdListener() {
				@Override
				public void onAdClosed() {
					// requestNewInterstitial();

				}

				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
					if (mInterstitialAd.isLoaded()) {
						mInterstitialAd.show();
					}
				}

			});

			AdRequest adRequest = new AdRequest.Builder().build();
			mInterstitialAd.loadAd(adRequest);

		} else {
			if (activity.adDTOs.size() != 0) {
				CustomInterstitialAdActivity.adDTO = getcurrentAd();
				Intent intent = new Intent(activity,
						CustomInterstitialAdActivity.class);
				activity.startActivity(intent);
			}
		}
	}

	private AdDTO getcurrentAd() {
		Log.d("",
				"adDTOs position=" + currentimageindex % activity.adDTOs.size());
		AdDTO adDTO = activity.adDTOs.get(currentimageindex
				% activity.adDTOs.size());

		if (currentimageindex == activity.adDTOs.size()) {
			currentimageindex = 0;
		}
		currentimageindex++;

		return adDTO;

	}

	// private void requestNewInterstitial() {
	// // AdRequest adRequest = new AdRequest.Builder().addTestDevice(
	// // "0CD03379730E1850662B69DFA3CE4C5D").build();
	//
	// AdRequest adRequest = new AdRequest.Builder().build();
	// mInterstitialAd.loadAd(adRequest);
	// }

	private int getActionBarHeight() {
		final TypedArray styledAttributes = activity.getTheme()
				.obtainStyledAttributes(
						new int[] { android.R.attr.actionBarSize });
		int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();
		return mActionBarSize;
	}

	// public void initializeAdvertising() {
	// new GetAds().execute(WebServiceConstants.GET_ADS);
	// }

	private void AnimateandSlideShow() {

		// slidingimage.setImageResource(adDTOs.get(
		// currentimageindex % adDTOs.size()).getAdImageLink());
		AdDTO adDTO = getcurrentAd();
		String url = adDTO.getAdImageLink();
		browserLinkUrl = adDTO.getAdBrowserLink();
		slidingimage.loadUrl(url);

		// currentimageindex++;

		Animation rotateimage = AnimationUtils.loadAnimation(activity,
				R.anim.fade_in);

		slidingimage.startAnimation(rotateimage);
		mHandler.postDelayed(mUpdateResults, period);
	}

	// private ViewPager viewPager;
	// private Handler h = new Handler();
	// private boolean pagerMoved = false;
	// private static final long ANIM_VIEWPAGER_DELAY = 10000;
	// private Runnable animateViewPager = new Runnable() {
	// @Override
	// public void run() {
	//
	// if (!pagerMoved) {
	// Log.d("",
	// "viewPager.getCurrentItem()="
	// + viewPager.getCurrentItem());
	// Log.d("",
	// "viewPager.getChildCount()="
	// + viewPager.getChildCount());
	// if (viewPager.getCurrentItem() == (adDTOs.size() - 1)) {
	// viewPager.setCurrentItem(0, true);
	// } else {
	// viewPager.setCurrentItem(viewPager.getCurrentItem() + 1,
	// true);
	// }
	//
	// h.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
	// }
	// }
	// };

	// Destroy the AdView.
	public void adViewDestroy() {
		if (adView != null) {
			try {
				adView.destroy();
			} catch (Exception e) {
				Log.e("AdMob", "Error destroy banner: ", e);
			}

		}
		if (mHandler != null) {
			mHandler.removeCallbacks(mUpdateResults);
		}
	}

	// class AdSlideAdapter extends PagerAdapter {
	// private Context mContext;
	// private List<AdDTO> adDTOs;
	//
	// public AdSlideAdapter(Context context, List<AdDTO> adDTOs) {
	// this.mContext = context;
	// this.adDTOs = adDTOs;
	//
	// }
	//
	// @Override
	// public int getCount() {
	// return adDTOs.size();
	// }
	//
	// @Override
	// public boolean isViewFromObject(View view, Object object) {
	// return view == ((View) object);
	// }
	//
	// @Override
	// public Object instantiateItem(ViewGroup container, int position) {
	// // TODO Auto-generated method stub
	// LayoutInflater inflater = (LayoutInflater) mContext
	// .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	// View rootview = inflater.inflate(R.layout.row_ad, container, false);
	// AdDTO adDTO = adDTOs.get(position);
	// ImageView iv = (ImageView) rootview.findViewById(R.id.imageView1);
	//
	// iv.setImageResource(adDTO.getAdImageLink());
	//
	// ((ViewPager) container).addView(rootview, 0);
	//
	// return rootview;
	// }
	//
	// @Override
	// public void destroyItem(ViewGroup container, int position, Object object)
	// {
	// // TODO Auto-generated method stub
	// ((ViewPager) container).removeView((View) object);
	//
	// }
	// }

	// class GetAds extends AsyncTask<String, Void, JSONArray> {
	// String vStatus;
	// TransparentProgressDialog dialog;
	//
	// protected JSONArray doInBackground(String... args) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	//
	// params.add(new BasicNameValuePair("", ""));
	//
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	//
	// Log.d("Ad mob :", "GetAds=" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	//
	// try {
	// adDTOs.clear();
	//
	// JSONObject jsonObject = jsonArray.getJSONObject(0);
	// JSONArray adJsonArray = jsonObject
	// .getJSONArray("advertisement");
	// String status = jsonObject.getString("status");
	// String add_type = jsonObject.getString("add_type");
	// String view = jsonObject.getString("view");
	// // String add_position;
	// // String add_type = "googleadd";
	// if (view.equals("bottom_adds")) {
	// singleton.adsizeAndLocation = AppConstants.ADS_BOTTOM_BANNER;
	// } else if (view.equals("native_adds")) {
	// singleton.adsizeAndLocation = AppConstants.ADS_CENTER_BANNER;
	// } else if (view.equals("interstitial_adds")) {
	// singleton.adsizeAndLocation = AppConstants.ADS_INTERSTITIAL;
	// }
	//
	// if (status.equals("success")) {
	//
	// if (add_type.equals("custom_add")) {
	// singleton.isGoogleAd = false;
	// for (int i = 0; i < adJsonArray.length(); i++) {
	// JSONObject ajsonObject = adJsonArray
	// .getJSONObject(i);
	// AdDTO adDTO = new AdDTO();
	// adDTO.parse(ajsonObject);
	// adDTOs.add(adDTO);
	// }
	// if (adDTOs.size() == 0) {
	// mAdViewFrameLayout.setVisibility(View.GONE);
	// return;
	// }
	// } else {
	// singleton.isGoogleAd = true;
	// }
	// startAdvertising();
	// } else {
	//
	// }
	//
	// } catch (JSONException e) {
	//
	// e.printStackTrace();
	// mAdViewFrameLayout.setVisibility(View.GONE);
	// }
	//
	// }
	// }
}
