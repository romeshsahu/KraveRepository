package com.ps.utill;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.krave.kraveapp.Activity_Home;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {
	private static Pattern pattern;
	private static Matcher matcher;

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	// public static boolean Emailvalidate(final String hex) {
	// final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	// + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	// pattern = Pattern.compile(EMAIL_PATTERN);
	// matcher = pattern.matcher(hex);
	// return matcher.matches();
	//
	// }

	// public static boolean checkEmailCorrect(String Email) {
	// String pttn = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	// Pattern p = Pattern.compile(pttn);
	// Matcher m = p.matcher(Email);
	//
	// if (m.matches()) {
	// return true;
	// }
	// return false;
	// }

	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	public static String convertServerTimeToLocalTime(String time) {
		long serverTimeInMilliSecound = (Long.valueOf(time)) * (1000L);
		serverTimeInMilliSecound = serverTimeInMilliSecound - (8 * 60 * 1000);
		Date serverDate = new Date(serverTimeInMilliSecound);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		/* UTC LAST ACT DATE */
		TimeZone utcZone = TimeZone.getTimeZone("UTC");
		simpleDateFormat.setTimeZone(utcZone);

		String serverDateString = null;

		serverDateString = simpleDateFormat.format(serverDate);
		Log.d("", "serverDateString=" + serverDateString);
		simpleDateFormat.setTimeZone(TimeZone.getDefault());
		Date localDate = null;
		try {
			localDate = simpleDateFormat.parse(serverDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String localDateString = simpleDateFormat.format(localDate);

		Log.d("", "localDateString=" + localDateString);
		return "" + localDate.getTime();
	}

	public static String convertServerDateTimeToLocalDateTime(
			String dateTimeString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		/* UTC LAST ACT DATE */
		TimeZone utcZone = TimeZone.getTimeZone("UTC");
		simpleDateFormat.setTimeZone(utcZone);
		Date serverDate = null;
		try {
			serverDate = simpleDateFormat.parse(dateTimeString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		simpleDateFormat.setTimeZone(TimeZone.getDefault());
		String serverDateString = simpleDateFormat.format(serverDate); // last
																		// act
																		// date
																		// in
																		// string
		Date localDate = null;
		try {
			localDate = simpleDateFormat.parse(serverDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getTime("" + localDate.getTime());
	}

	public static String getTime(String timeString) {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");

		Date currentDate = new Date(System.currentTimeMillis());
		Date currentDate_seven = new Date(System.currentTimeMillis()
				- (7 * 24 * 60 * 60 * 1000));
		Date msgDate = new Date(Long.valueOf(timeString));
		if (dateFormat1.format(currentDate).equals(dateFormat1.format(msgDate))) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");

			return dateFormat.format(msgDate);
		} else if (msgDate.before(currentDate_seven)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
			return dateFormat.format(msgDate);
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("E");

			return dateFormat.format(msgDate);
		}

	}

	public static void setLocale(Context context, int poss) {
		String lang = null;
		switch (poss) {
		case 1:
			lang = "es";
			break;
		case 2:
			lang = "pt";
			break;

		default:
			lang = "en";
			break;
		}

		Locale myLocale = new Locale(lang);
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);

	}

	public static void sendBadgeCountBrodcast(int badgeno, Context context) {
		Log.d("CHECK", "NotificationService badge count=" + badgeno);

		// The manufacturer of the product/hardware.

		String manufactureStr = Build.MANUFACTURER;

		Log.d("CHECK", "manufacture : " + manufactureStr);

		// int badgeno = new Random().nextInt(100);

		if (manufactureStr != null) {

			boolean bool2 = manufactureStr.toLowerCase(Locale.US).contains(
					"htc");
			boolean bool3 = manufactureStr.toLowerCase(Locale.US).contains(
					"sony");
			boolean bool4 = manufactureStr.toLowerCase(Locale.US).contains(
					"samsung");

			// Sony Ericssion
			if (bool3) {
				try {
					Intent intent = new Intent();
					intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
					intent.putExtra(
							"com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",
							"com.krave.kraveapp.Activity_Splash");
					intent.putExtra(
							"com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",
							true);
					intent.putExtra(
							"com.sonyericsson.home.intent.extra.badge.MESSAGE",
							badgeno);
					intent.putExtra(
							"com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",
							"com.krave.kraveapp");

					context.sendBroadcast(intent);
				} catch (Exception localException) {
					Log.e("CHECK",
							"Sony : " + localException.getLocalizedMessage());
				}
			}

			// HTC
			if (bool2) {
				try {
					Intent localIntent1 = new Intent(
							"com.htc.launcher.action.UPDATE_SHORTCUT");
					localIntent1.putExtra("packagename", "com.krave.kraveapp");
					localIntent1.putExtra("count", badgeno);
					context.sendBroadcast(localIntent1);

					Intent localIntent2 = new Intent(
							"com.htc.launcher.action.SET_NOTIFICATION");
					ComponentName localComponentName = new ComponentName(
							context, "com.krave.kraveapp.Activity_Splash");
					localIntent2.putExtra("com.htc.launcher.extra.COMPONENT",
							localComponentName.flattenToShortString());
					localIntent2.putExtra("com.htc.launcher.extra.COUNT", 10);
					context.sendBroadcast(localIntent2);
				} catch (Exception localException) {
					Log.e("CHECK",
							"HTC : " + localException.getLocalizedMessage());
				}
			}
			if (bool4) {
				// Samsung
				try {
					ContentResolver localContentResolver = context
							.getContentResolver();
					Uri localUri = Uri.parse("content://com.sec.badge/apps");
					ContentValues localContentValues = new ContentValues();
					localContentValues.put("package", "com.krave.kraveapp");
					localContentValues.put("class",
							"com.krave.kraveapp.Activity_Splash");
					localContentValues.put("badgecount",
							Integer.valueOf(badgeno));
					String str = "package=? AND class=?";
					String[] arrayOfString = new String[2];
					arrayOfString[0] = "com.krave.kraveapp";
					arrayOfString[1] = "com.krave.kraveapp.Activity_Splash";

					int update = localContentResolver.update(localUri,
							localContentValues, str, arrayOfString);

					if (update == 0) {
						localContentResolver.insert(localUri,
								localContentValues);
					}

				} catch (IllegalArgumentException localIllegalArgumentException) {
					Log.e("CHECK",
							"Samsung1F : "
									+ localIllegalArgumentException
											.getLocalizedMessage());
				} catch (Exception localException) {
					Log.e("CHECK",
							"Samsung : " + localException.getLocalizedMessage());
				}
			}
		}

	}
}