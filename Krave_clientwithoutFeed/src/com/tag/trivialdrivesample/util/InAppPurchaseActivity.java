/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tag.trivialdrivesample.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.krave.kraveapp.AppManager;
import com.krave.kraveapp.R;
import com.ps.utill.AppConstants;
import com.ps.utill.SessionManager;
import com.tag.trivialdrivesample.util.IabHelper;
import com.tag.trivialdrivesample.util.IabResult;
import com.tag.trivialdrivesample.util.Inventory;
import com.tag.trivialdrivesample.util.Purchase;

/**
 * Example game using in-app billing version 3.
 * 
 * Before attempting to run this sample, please read the README file. It
 * contains important information on how to set up this project.
 * 
 * All the game-specific logic is implemented here in MainActivity, while the
 * general-purpose boilerplate that can be reused in any app is provided in the
 * classes in the util/ subdirectory. When implementing your own application,
 * you can copy over util/*.java to make use of those utility classes.
 * 
 * This game is a simple "driving" game where the player can buy gas and drive.
 * The car has a tank which stores gas. When the player purchases gas, the tank
 * fills up (1/4 tank at a time). When the player drives, the gas in the tank
 * diminishes (also 1/4 tank at a time).
 * 
 * The user can also purchase a "premium upgrade" that gives them a red car
 * instead of the standard blue one (exciting!).
 * 
 * The user can also purchase a subscription ("infinite gas") that allows them
 * to drive without using up any gas while that subscription is active.
 * 
 * It's important to note the consumption mechanics for each item.
 * 
 * PREMIUM: the item is purchased and NEVER consumed. So, after the original
 * purchase, the player will always own that item. The application knows to
 * display the red car instead of the blue one because it queries whether the
 * premium "item" is owned or not.
 * 
 * INFINITE GAS: this is a subscription, and subscriptions can't be consumed.
 * 
 * GAS: when gas is purchased, the "gas" item is then owned. We consume it when
 * we apply that item's effects to our app's world, which to us means filling up
 * 1/4 of the tank. This happens immediately after purchase! It's at this point
 * (and not when the user drives) that the "gas" item is CONSUMED. Consumption
 * should always happen when your game world was safely updated to apply the
 * effect of the purchase. So, in an example scenario:
 * 
 * BEFORE: tank at 1/2 ON PURCHASE: tank at 1/2, "gas" item is owned
 * IMMEDIATELY: "gas" is consumed, tank goes to 3/4 AFTER: tank at 3/4, "gas"
 * item NOT owned any more
 * 
 * Another important point to notice is that it may so happen that the
 * application crashed (or anything else happened) after the user purchased the
 * "gas" item, but before it was consumed. That's why, on startup, we check if
 * we own the "gas" item, and, if so, we have to apply its effects to our world
 * and consume it. This is also very important!
 * 
 * @author Bruno Oliveira (Google)
 */
public class InAppPurchaseActivity extends Activity {
	// Debug tag, for logging
	static final String TAG = "KraveApp";

	// Does the user have the premium upgrade?
	// boolean mIsWeeklyPlanActivated = false;
	boolean mIsMonthlyPlanActivated = false;

	static final int RC_REQUEST = 10001;

	private IabHelper mHelper;
	private AppManager singleton;
	private SessionManager sessionManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		// load game data
		loadData();

		/*
		 * base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY (that
		 * you got from the Google Play developer console). This is not your
		 * developer public key, it's the *app-specific* public key.
		 * 
		 * Instead of just storing the entire literal string here embedded in
		 * the program, construct the key at runtime from pieces or use bit
		 * manipulation (for example, XOR with some other string) to hide the
		 * actual key. The key itself is not secret information, but we don't
		 * want to make it easy for an attacker to replace the public key with
		 * one of their own and then fake messages from the server.
		 */

		// Some sanity checks to see if the developer (that's you!) really
		// followed the
		// instructions to run this sample (don't put these checks on your app!)
		if (InAppPurchaseConstants.base64EncodedPublicKey
				.contains("CONSTRUCT_YOUR")) {
			throw new RuntimeException(
					"Please put your app's public key in MainActivity.java. See README.");
		}
		if (getPackageName().startsWith("com.example")) {
			throw new RuntimeException(
					"Please change the sample's package name! See README.");
		}

		// Create the helper, passing it our context and the public key to
		// verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(this,
				InAppPurchaseConstants.base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;

				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	// Listener that's called when we finish querying the items and
	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Do we have the weekly upgrade?
			// Purchase weeklyPlanPurchase = inventory
			// .getPurchase(InAppPurchaseConstants.SKU_WEEKLY_SUBCRIPTION_PLAN);
			// mIsWeeklyPlanActivated = (weeklyPlanPurchase != null &&
			// verifyDeveloperPayload(weeklyPlanPurchase));
			//
			// Log.d(TAG, "User is "
			// + (mIsWeeklyPlanActivated ? "WEEKLY" : "NOT PREMIUM"));

			// Do we have the monthly upgrade?
			Purchase monthlyPlanPurchase = inventory
					.getPurchase(InAppPurchaseConstants.SKU_MONTHLY_SUBCRIPTION_PLAN);
			mIsMonthlyPlanActivated = (monthlyPlanPurchase != null && verifyDeveloperPayload(monthlyPlanPurchase));
			Log.d(TAG, "User is "
					+ (mIsMonthlyPlanActivated ? "MONTHLY" : "NOT MONTHLY"));

			// Do we have the YEARLY upgrade?
			// Purchase yearlyPlanPurchase = inventory
			// .getPurchase(InAppPurchaseConstants.SKU_YEARLY_SUBCRIPTION_PLAN);
			// mIsYearlyPlanActivated = (yearlyPlanPurchase != null &&
			// verifyDeveloperPayload(yearlyPlanPurchase));
			// Log.d(TAG, "User is "
			// + (mIsYearlyPlanActivated ? "YEARLY" : "NOT YEARLY"));
			// SkuDetails details = inventory
			// .getSkuDetails(InAppPurchaseConstants.SKU_MONTHLY_SUBCRIPTION_PLAN);
			// Log.d(TAG, "details.toString()= " + details.toString());
			// details.toString();

			// // Do we have the infinite gas plan?
			// Purchase infiniteGasPurchase =
			// inventory.getPurchase(SKU_INFINITE_GAS);
			// mSubscribedToInfiniteGas = (infiniteGasPurchase != null &&
			// verifyDeveloperPayload(infiniteGasPurchase));
			// Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" :
			// "DOES NOT HAVE")
			// + " infinite gas subscription.");
			// if (mSubscribedToInfiniteGas) mTank = TANK_MAX;
			//
			// // Check for gas delivery -- if we own gas, we should fill up the
			// tank immediately
			// Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
			// if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
			// Log.d(TAG, "We have gas. Consuming it.");
			// mHelper.consumeAsync(inventory.getPurchase(InAppPurchaseConstants.SKU_WEEKLY_SUBCRIPTION_PLAN),
			// mConsumeFinishedListener);
			// return;
			// }

			updateUi(mIsMonthlyPlanActivated);
			setWaitScreen(false);
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	public void subscribeToPaidAccount() {

		try {
			final Dialog dialog = new Dialog(InAppPurchaseActivity.this,
					android.R.style.Theme_NoTitleBar);

			dialog.setContentView(R.layout.dialog_upgrade_to_paid);

			Button upgradeButton = (Button) dialog
					.findViewById(R.id.upgrade_button);
			// Button ok = (Button) dialog.findViewById(R.id.ok);
			// TextView title = (TextView) dialog.findViewById(R.id.title);
			// Typeface typeface = FontStyle.getFont(InAppPurchaseActivity.this,
			// AppConstants.HelveticaNeueLTStd_Lt);
			// title.setTypeface(typeface);
			// cancle.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// dialog.dismiss();
			// }
			// });
			upgradeButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					buyPlan(InAppPurchaseConstants.SKU_MONTHLY_SUBCRIPTION_PLAN);

				}
			});
			dialog.show();
		} catch (Exception e) {

		}

	}

	// weekly plan clicked
	// public void onWeeklyPlanClicked(View arg0) {
	// buyPlan(InAppPurchaseConstants.SKU_WEEKLY_SUBCRIPTION_PLAN);
	// }

	// yearly plan clicked
	public void onMonthlyPlanClicked(View arg0) {
		buyPlan(InAppPurchaseConstants.SKU_MONTHLY_SUBCRIPTION_PLAN);
	}

	// yearly plan clicked
	// public void onYearlyPlanClicked(View arg0) {
	// buyPlan(InAppPurchaseConstants.SKU_YEARLY_SUBCRIPTION_PLAN);
	// }

	public void buyPlan(String planId) {
		Log.d(TAG, "Buy gas button clicked.");

		// if (mIsWeeklyPlanActivated) {
		// complain("No need! You're subscribed to weekly plan already.");
		// return;
		// }
		if (mIsMonthlyPlanActivated) {
			complain("No need! You're subscribed to monthly plan already.");
			return;
		}
		// if (mIsYearlyPlanActivated) {
		// complain("No need! You're subscribed to yearly plan already.");
		// return;
		// }
		// if (mTank >= TANK_MAX) {
		// complain("Your tank is full. Drive around a bit!");
		// return;
		// }

		// launch the gas purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		setWaitScreen(true);
		Log.d(TAG, "Launching purchase flow for subcription.");

		/*
		 * TODO: for security, generate your payload here for verification. See
		 * the comments on verifyDeveloperPayload() for more info. Since this is
		 * a SAMPLE, we just use an empty string, but on a production app you
		 * should carefully generate this.
		 */
		String payload = "";

		mHelper.launchSubscriptionPurchaseFlow(this, planId, RC_REQUEST,
				mPurchaseFinishedListener, payload);
	}

	// User clicked the "Upgrade to Premium" button.
	// public void onUpgradeAppButtonClicked(View arg0) {
	// Log.d(TAG,
	// "Upgrade button clicked; launching purchase flow for upgrade.");
	// setWaitScreen(true);
	//
	// /* TODO: for security, generate your payload here for verification. See
	// the comments on
	// * verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just
	// use
	// * an empty string, but on a production app you should carefully generate
	// this. */
	// String payload = "";
	//
	// mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
	// mPurchaseFinishedListener, payload);
	// }

	// "Subscribe to infinite gas" button clicked. Explain to user, then start
	// purchase
	// flow for subscription.
	// public void onInfiniteGasButtonClicked(View arg0) {
	// if (!mHelper.subscriptionsSupported()) {
	// complain("Subscriptions not supported on your device yet. Sorry!");
	// return;
	// }
	//
	// /* TODO: for security, generate your payload here for verification. See
	// the comments on
	// * verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just
	// use
	// * an empty string, but on a production app you should carefully generate
	// this. */
	// String payload = "";
	//
	// setWaitScreen(true);
	// Log.d(TAG, "Launching purchase flow for infinite gas subscription.");
	// mHelper.launchPurchaseFlow(this,
	// SKU_INFINITE_GAS, IabHelper.ITEM_TYPE_SUBS,
	// RC_REQUEST, mPurchaseFinishedListener, payload);
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);
		if (mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				// complain("Error purchasing: " + result);
				complain("Purchase cancel");
				setWaitScreen(false);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");
				setWaitScreen(false);
				return;
			}

			Log.d(TAG, "Purchase successful.");

			// if (purchase.getSku().equals(SKU_GAS)) {
			// // bought 1/4 tank of gas. So consume it.
			// Log.d(TAG, "Purchase is gas. Starting gas consumption.");
			// mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			// } else

			// if (purchase.getSku().equals(
			// InAppPurchaseConstants.SKU_WEEKLY_SUBCRIPTION_PLAN)) {
			//
			// Log.d(TAG, "Purchase is upgrading to weekly premium plan!");
			// alert("Thank you for upgrading to weekly premium plan!");
			// mIsWeeklyPlanActivated = true;
			//
			// } else

			if (purchase.getSku().equals(
					InAppPurchaseConstants.SKU_MONTHLY_SUBCRIPTION_PLAN)) {

				Log.d(TAG, "Purchase is upgrading to monthly premium plan!");
				alert("Thank you for upgrading to monthly premium plan!");
				mIsMonthlyPlanActivated = true;
				// mTank = TANK_MAX;

			}
			// else if (purchase.getSku().equals(
			// InAppPurchaseConstants.SKU_YEARLY_SUBCRIPTION_PLAN)) {
			//
			// Log.d(TAG, "Purchase is upgrading to yearly premium plan!");
			// alert("Thank you for upgrading to yearly premium plan!");
			// mIsYearlyPlanActivated = true;
			// // mTank = TANK_MAX;
			//
			// }
			updateUi(mIsMonthlyPlanActivated);
			setWaitScreen(false);
		}
	};

	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	protected void onDestroy() {
		super.onDestroy();

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	public void updateUi(boolean isPurchasedSubcription) {
		isPurchasedSubcription=true;
		singleton.isPaidUser = isPurchasedSubcription;
		sessionManager.setIsPaidUser(isPurchasedSubcription);
		Log.d("", "brodacst notification");
		Intent broadcast = new Intent();
		broadcast.putExtra("come", "in_app_purchase");
		broadcast.setAction(AppConstants.BROADCAST_ACTION);
		sendBroadcast(broadcast);
	}

	// updates UI to reflect model
	// public void updateUi() {
	// // update the car color to reflect premium status or lack thereof
	// // ((ImageView) findViewById(R.id.weekly_plan_button))
	// // .setImageResource(mIsWeeklyPlanActivated ? R.drawable.premium
	// // : R.drawable.free);
	//
	// // "Upgrade" button is only visible if the user is not premium
	// findViewById(R.id.weekly_plan_button).setVisibility(
	// mIsWeeklyPlanActivated ? View.GONE : View.VISIBLE);
	//
	// // "Get infinite gas" button is only visible if the user is not
	// // subscribed yet
	// findViewById(R.id.monthly_plan_button).setVisibility(
	// mIsMonthlyPlanActivated ? View.GONE : View.VISIBLE);
	// findViewById(R.id.yearly_plan_button).setVisibility(
	// mIsYearlyPlanActivated ? View.GONE : View.VISIBLE);
	//
	// // update gas gauge to reflect tank status
	// // if (mSubscribedToInfiniteGas) {
	// // ((ImageView) findViewById(R.id.gas_gauge))
	// // .setImageResource(R.drawable.gas_inf);
	// // } else {
	// // int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1
	// // : mTank;
	// // ((ImageView) findViewById(R.id.gas_gauge))
	// // .setImageResource(TANK_RES_IDS[index]);
	// // }
	// }

	// Enables or disables the "please wait" screen.
	void setWaitScreen(boolean set) {
		// findViewById(R.id.screen_main).setVisibility(
		// set ? View.GONE : View.VISIBLE);
		// findViewById(R.id.screen_wait).setVisibility(
		// set ? View.VISIBLE : View.GONE);
	}

	void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	void loadData() {
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		sessionManager = new SessionManager(InAppPurchaseActivity.this);
		singleton.isPaidUser = sessionManager.isPaidUser();
	}
}
