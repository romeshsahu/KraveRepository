package com.ps.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.krave.kraveapp.Activity_ShowImage;
import com.krave.kraveapp.Activity_ShowVideo;
import com.krave.kraveapp.Activity_Show_map;
import com.krave.kraveapp.FragmentChatMatches;
import com.krave.kraveapp.R;
import com.ps.models.ChatDetailsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class ChatListViewAdapter extends ArrayAdapter {

	// private TextView chatText;
	private List<ChatDetailsDTO> chatMessageList = new ArrayList();
	// private LinearLayout singleMessageContainer;
	private Context context;
	private SessionManager sessionManager;
	ImageLoaderCircle imageLoaderCircle;
	String previousDate = "";
	ImageLoader imageLoader;

	// @Override
	// public void add(ChatDetailsDTO object) {
	// chatMessageList.add(object);
	// super.add(object);
	// }

	public ChatListViewAdapter(Context context, ArrayList<ChatDetailsDTO> list) {
		super(context, 0);
		this.chatMessageList = list;
		this.context = context;
		sessionManager = new SessionManager(context);
		imageLoaderCircle = new ImageLoaderCircle(context);
		imageLoader = new ImageLoader(context);

	}

	public int getCount() {
		return this.chatMessageList.size();
	}

	public ChatDetailsDTO getItem(int index) {
		return this.chatMessageList.get(index);
	}

	class ViewHolder {
		public TextView chatText, dateTime, chatText0;
		public CircleImageView profilePick;
		public ImageView onlineImage, sendImageView, tempImage, videoPlayIcon;
		public LinearLayout singleMessageContainer;
		public ProgressBar progressBar;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder = null;
		final ChatDetailsDTO chatMessageObj = getItem(position);
		Log.d("", "chat message id=" + chatMessageObj.getId());
		// if (position != 0) {
		// if (getItem(position - 1).getId().equals("-1")) {
		// row = null;
		// }
		// }
		if (!chatMessageObj.getId().equals("-1")) {
			// if (row == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_chat_item, parent, false);
			viewHolder.singleMessageContainer = (LinearLayout) row
					.findViewById(R.id.singleMessageContainer);
			viewHolder.chatText = (TextView) row.findViewById(R.id.message);
			viewHolder.chatText0 = (TextView) row.findViewById(R.id.message0);
			viewHolder.profilePick = (CircleImageView) row
					.findViewById(R.id.userimage);
			viewHolder.onlineImage = (ImageView) row
					.findViewById(R.id.onlineImage);
			viewHolder.dateTime = (TextView) row.findViewById(R.id.dateTime);
			viewHolder.sendImageView = (ImageView) row
					.findViewById(R.id.imageTransfer);
			viewHolder.tempImage = (ImageView) row.findViewById(R.id.tempImage);
			viewHolder.videoPlayIcon = (ImageView) row
					.findViewById(R.id.videoPlayIcon);
			viewHolder.progressBar = (ProgressBar) row
					.findViewById(R.id.progressBar);
			Typeface typeface = FontStyle.getFont(context,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.chatText.setTypeface(typeface);
			viewHolder.dateTime.setTypeface(typeface);
			viewHolder.chatText0.setTypeface(typeface);

			row.setTag(viewHolder);
			// } else {
			// viewHolder = (ViewHolder) row.getTag();
			// }

			// SimpleDateFormat dateFormat = new
			// SimpleDateFormat("dd-MMM-yyyy hh:mm");
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
			Date date = new Date(Long.valueOf(chatMessageObj.getTime()));

			viewHolder.dateTime.setText(dateFormat.format(date));
			/* check Messge type */
			if (chatMessageObj.getMeassageType().equals(
					AppConstants.MESSAGE_TYPE_TEXT)) {
				viewHolder.chatText.setVisibility(View.VISIBLE);

				viewHolder.chatText0.setVisibility(View.VISIBLE);

				// edited revision 581
				viewHolder.dateTime.setVisibility(View.VISIBLE);
				viewHolder.sendImageView.setVisibility(View.GONE);
				viewHolder.chatText.setText(chatMessageObj.getMessage());
				viewHolder.chatText0.setText(chatMessageObj.getMessage());
			} else if (chatMessageObj.getMeassageType().equals(
					AppConstants.MESSAGE_TYPE_LOCATION)) {

				viewHolder.chatText.setVisibility(View.GONE);
				viewHolder.chatText0.setVisibility(View.GONE);
				// revision 581
				viewHolder.dateTime.setVisibility(View.VISIBLE);
				viewHolder.sendImageView.setVisibility(View.VISIBLE);
				viewHolder.sendImageView
						.setImageResource(R.drawable.location_map_icon);
			} else if (chatMessageObj.getMeassageType().equals(
					AppConstants.MESSAGE_TYPE_IMAGE)) {
				viewHolder.chatText.setVisibility(View.GONE);
				viewHolder.chatText0.setVisibility(View.GONE);
				// revision 581
				viewHolder.dateTime.setVisibility(View.VISIBLE);
				viewHolder.sendImageView.setVisibility(View.VISIBLE);

				if (isLoginUserMsg(chatMessageObj)
						|| isUnlimitedSnapChatMessage(chatMessageObj)) {
					imageLoader.DisplayImage(chatMessageObj.getMessage(),
							viewHolder.sendImageView);
				} else {
					// viewHolder.sendImageView
					// .setImageResource(R.drawable.loading);
					imageLoader.DisplayImage(chatMessageObj.getMessage(),
							viewHolder.tempImage);
					chatMessageObj.setView(viewHolder.sendImageView);

					if (chatMessageObj.getImageSnapChatLeftTime() == -10) {
						viewHolder.progressBar.setVisibility(View.VISIBLE);
						chatMessageObj.setProgressBar(viewHolder.progressBar);
						new CheckChatPostImage().execute(chatMessageObj);
					} else {

						int snapTime = Integer.valueOf(chatMessageObj
								.getMessage().split("=")[1]);

						if (chatMessageObj.getImageSnapChatLeftTime() == 0) {
							viewHolder.sendImageView
									.setImageResource(R.drawable.snap_chat_padlock);
						} else {
							setSnapChatImage(snapTime, viewHolder.sendImageView);
						}

					}
				}
			} else {
				viewHolder.chatText.setVisibility(View.GONE);
				viewHolder.chatText0.setVisibility(View.GONE);
				viewHolder.videoPlayIcon.setVisibility(View.VISIBLE);
				viewHolder.dateTime.setVisibility(View.VISIBLE);
				viewHolder.sendImageView.setVisibility(View.VISIBLE);

				if (isLoginUserMsg(chatMessageObj)
						|| isUnlimitedSnapChatMessage(chatMessageObj)) {
					imageLoader.DisplayImage(
							chatMessageObj.getMessage().split("\\|")[0],
							viewHolder.sendImageView);
				} else {

					imageLoader.DisplayImage(chatMessageObj.getMessage(),
							viewHolder.tempImage);
					chatMessageObj.setView(viewHolder.sendImageView);

					if (chatMessageObj.getImageSnapChatLeftTime() == -10) {
						viewHolder.progressBar.setVisibility(View.VISIBLE);
						chatMessageObj.setProgressBar(viewHolder.progressBar);
						new CheckChatPostImage().execute(chatMessageObj);
					} else {

						int snapTime = Integer.valueOf(chatMessageObj
								.getMessage().split("=")[1]);

						if (chatMessageObj.getImageSnapChatLeftTime() == 0) {
							viewHolder.sendImageView
									.setImageResource(R.drawable.snap_chat_padlock);
						} else {
							setSnapChatImage(snapTime, viewHolder.sendImageView);
						}

					}
				}
			}
			// viewHolder.sendImageView.setOnClickListener(new OnClickListener()
			// {
			//
			// @Override
			// public void onClick(View v) {
			// if (chatMessageObj.getMeassageType().equals(
			// AppConstants.MESSAGE_TYPE_LOCATION)) {
			// Activity_Show_map.chatMessageObj = chatMessageObj;
			// Intent intent = new Intent(context,
			// Activity_Show_map.class);
			// context.startActivity(intent);
			// } else {
			// if ((chatMessageObj.getImageSnapChatLeftTime() != 0 &&
			// chatMessageObj
			// .getImageSnapChatLeftTime() != -10)
			// || sessionManager.getUserDetail().getUserID()
			// .equals(chatMessageObj.getFromuser())) {
			// Activity_ShowImage.chatMessageObj = chatMessageObj;
			// Intent intent = new Intent(context,
			// Activity_ShowImage.class);
			// context.startActivity(intent);
			// // Toast.makeText(context, "onclick",
			// // Toast.LENGTH_SHORT).show();
			// }
			// }
			//
			// }
			// });
			viewHolder.sendImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			viewHolder.sendImageView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						if (chatMessageObj.getMeassageType().equals(
								AppConstants.MESSAGE_TYPE_LOCATION)) {
							Activity_Show_map.chatMessageObj = chatMessageObj;
							Intent intent = new Intent(context,
									Activity_Show_map.class);
							context.startActivity(intent);
						} else if (chatMessageObj.getMeassageType().equals(
								AppConstants.MESSAGE_TYPE_VIDEO)) {

							Activity_ShowVideo.chatMessageObj = chatMessageObj;
							Intent intent = new Intent(context,
									Activity_ShowVideo.class);
							context.startActivity(intent);
						}

						else {
							if ((chatMessageObj.getImageSnapChatLeftTime() != 0 && chatMessageObj
									.getImageSnapChatLeftTime() != -10)
									|| isUnlimitedSnapChatMessage(chatMessageObj)
									|| sessionManager
											.getUserDetail()
											.getUserID()
											.equals(chatMessageObj
													.getFromuser())) {
								Activity_ShowImage.chatMessageObj = chatMessageObj;
								Intent intent = new Intent(context,
										Activity_ShowImage.class);
								context.startActivity(intent);
								// Toast.makeText(context, "onclick",
								// Toast.LENGTH_SHORT).show();
							}
						}

						break;

					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_OUTSIDE:
					case MotionEvent.ACTION_CANCEL:

//						if (Activity_ShowImage.getActivityObject() != null)
//							Activity_ShowImage.getActivityObject().finish();

						break;

					}

					return false;
				}
			});
			/* set message row */
			if (sessionManager.getUserDetail().getUserID()
					.equals(chatMessageObj.getFromuser())) {

				if (chatMessageObj.getMeassageType().equals(
						AppConstants.MESSAGE_TYPE_TEXT)) {
					viewHolder.chatText0.setVisibility(View.VISIBLE);
					// revision 581
					viewHolder.dateTime.setVisibility(View.VISIBLE);
				}

				viewHolder.chatText.setVisibility(View.GONE);
				// viewHolder.chatText
				// .setBackgroundResource(R.drawable.chat_small1);
				viewHolder.sendImageView
						.setBackgroundResource(R.drawable.chat_small1);
				viewHolder.singleMessageContainer.setGravity(Gravity.RIGHT);
				viewHolder.profilePick.setVisibility(View.GONE);
				viewHolder.onlineImage.setVisibility(View.GONE);
			} else {
				viewHolder.profilePick.setVisibility(View.VISIBLE);
				if (FragmentChatMatches.isOnline) {
					viewHolder.onlineImage.setVisibility(View.VISIBLE);
				} else {
					viewHolder.onlineImage.setVisibility(View.GONE);
				}
				try {
					if (FragmentChatMatches.userDTO != null
							&& FragmentChatMatches.userDTO
									.getUserProfileImageDTOs().size() > 0
							&& FragmentChatMatches.userDTO
									.getUserProfileImageDTOs().get(0)
									.getIsImageActive()) {
						imageLoaderCircle
								.DisplayImage(FragmentChatMatches.userDTO
										.getUserProfileImageDTOs().get(0)
										.getImagePath(), viewHolder.profilePick);
					} else {
						imageLoaderCircle.DisplayImage(
								FragmentChatMatches.userDTO.getProfileImage(),
								viewHolder.profilePick);
					}
				} catch (Exception e) {

				}
				viewHolder.chatText0.setVisibility(View.GONE);
				if (chatMessageObj.getMeassageType().equals(
						AppConstants.MESSAGE_TYPE_TEXT)) {
					viewHolder.chatText.setVisibility(View.VISIBLE);
				}
				// viewHolder.chatText
				// .setBackgroundResource(R.drawable.chat_small2);
				viewHolder.sendImageView
						.setBackgroundResource(R.drawable.chat_small2);
				viewHolder.singleMessageContainer.setGravity(Gravity.LEFT);

			}

			//((ListView) parent).setSelection(position);  // edited on 16/05/2015
		} 
		
		else {

			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_chat_date_header_item, parent,
					false);
			TextView dateTime = (TextView) row.findViewById(R.id.dateTime);
			Typeface typeface = FontStyle.getFont(context,
					AppConstants.HelveticaNeueLTStd_Bd);

			dateTime.setTypeface(typeface);
			dateTime.setText(chatMessageObj.getDate());

		}

		return row;
	}

	private int getSnapChatRemainingTime(ChatDetailsDTO chatDetailsDTO) {
		String msg = chatDetailsDTO.getMessage();
		int value = 1;
		if (msg.contains(AppConstants.SNAP_CHAT_CONSTANTS)) {
			value = Integer.valueOf(msg.split("=")[1]);
		}

		return value;

	}

	private boolean isUnlimitedSnapChatMessage(ChatDetailsDTO chatDetailsDTO) {

		return !chatDetailsDTO.getMessage().contains(
				AppConstants.SNAP_CHAT_CONSTANTS);

	}

	private boolean isLoginUserMsg(ChatDetailsDTO chatDetailsDTO) {

		return sessionManager.getUserDetail().getUserID()
				.equals(chatDetailsDTO.getFromuser());

	}

	public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth,
			int bitmapHeight) {
		return Bitmap
				.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
	}

	// public Bitmap getThumbnail(ContentResolver cr, String path)
	// throws Exception {
	//
	// Cursor ca = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	// new String[] { MediaStore.MediaColumns._ID },
	// MediaStore.MediaColumns.DATA + "=?", new String[] { path },
	// null);
	// if (ca != null && ca.moveToFirst()) {
	// int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
	// ca.close();
	// return MediaStore.Images.Thumbnails.getThumbnail(cr, id,
	// MediaStore.Images.Thumbnails.MICRO_KIND, null);
	// }
	//
	// ca.close();
	// return null;
	//
	// }
	//
	// public Bitmap decodeToBitmap(byte[] decodedByte) {
	// return BitmapFactory
	// .decodeByteArray(decodedByte, 0, decodedByte.length);
	// }

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();

	}

	// private void setMeassge(ChatDetailsDTO chatMessageObj, ViewHolder
	// viewHolder) {
	// if (chatMessageObj.getMeassageType().equals(
	// AppConstants.MESSAGE_TYPE_TEXT)) {
	// viewHolder.chatText.setText(chatMessageObj.getMessage());
	// viewHolder.chatText.setBackgroundResource(R.drawable.chat_small1);
	// viewHolder.sendImageView.setVisibility(View.GONE);
	// } else {
	// Bitmap myBitmap = BitmapFactory.decodeFile(chatMessageObj
	// .getMessage());
	//
	// viewHolder.sendImageView.setImageBitmap(myBitmap);
	// viewHolder.chatText.setVisibility(View.GONE);
	// }
	// }
	private class ImageGetter implements Html.ImageGetter {

		public Drawable getDrawable(String source) {
			int id;

			if (source.equals("gps_orange.png")) {
				id = R.drawable.gps_orange;
			} else if (source.equals("gps_white.png")) {
				id = R.drawable.gps_white;
			} else {
				return null;
			}

			Drawable d = context.getResources().getDrawable(id);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			return d;
		}
	};

	class CheckChatPostImage extends
			AsyncTask<ChatDetailsDTO, Void, JSONObject> {

		ChatDetailsDTO chatDetailsDTO = null;
		int snapTime;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new TransparentProgressDialog(context);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONObject doInBackground(ChatDetailsDTO... args) {
			chatDetailsDTO = args[0];
			// http://54.219.211.237/KraveAPI/api_calls/checkChatPostImage.php?user_id=770&image=1432104507_.png?seconds=11
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String imageUrl = chatDetailsDTO.getMessage();
			snapTime = Integer.valueOf(imageUrl.split("=")[1]);
			params.add(new BasicNameValuePair("user_id", ""
					+ chatDetailsDTO.getFromuser()));
			params.add(new BasicNameValuePair("image", ""
					+ imageUrl.substring((imageUrl.lastIndexOf('/') + 1),
							(imageUrl.lastIndexOf('?')))));

			JSONObject json = new JSONParser().makeHttpRequest2(
					WebServiceConstants.CHECK_SNAP_CHAT_MSG_TIME_LEFT, "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			// dialog.dismiss();
			// {"MSG":"45","S":200}
			try {
				if (jsonObject.getInt("S") == AppConstants.RESPONSE_SUCCESS) {
					ImageView image = (ImageView) chatDetailsDTO.getView();
					int leftTime = Integer.valueOf(jsonObject.getString("MSG"));
					String url = chatDetailsDTO.getMessage().split("=")[0]
							+ leftTime;
					if (leftTime == 0) {
						image.setImageResource(R.drawable.snap_chat_padlock);
						chatDetailsDTO.setImageSnapChatLeftTime(leftTime);
						chatDetailsDTO.getProgressBar()
								.setVisibility(View.GONE);
					} else if (leftTime == -1) {
						new AddUpdateSnapChatTime().execute(chatDetailsDTO);
					} else {

						setSnapChatImage(snapTime, image);
						chatDetailsDTO.getProgressBar()
								.setVisibility(View.GONE);
						chatDetailsDTO.setImageSnapChatLeftTime(leftTime);
					}

					// switch (leftTime) {
					// case -1:
					// image.setImageResource(R.drawable.av_new_interest_transgender_down);
					// break;
					// case 0:
					// image.setImageResource(R.drawable.av_new_interest_transgender_up);
					// break;
					//
					// default:
					// image.setImageResource(R.drawable.av_new_interest_transgender_down);
					// chatDetailsDTO.setMessage(url);
					// break;
					// }
				} else {

				}

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}

	private void setSnapChatImage(int snapTime, ImageView image) {
		switch (snapTime) {
		case 3:
			image.setImageResource(R.drawable.snap_chat_3_seconds);
			break;
		case 7:
			image.setImageResource(R.drawable.snap_chat_7_seconds);
			break;
		case 11:
			image.setImageResource(R.drawable.snap_chat_11_seconds);
			break;
		}

	}

	class AddUpdateSnapChatTime extends
			AsyncTask<ChatDetailsDTO, Void, JSONObject> {

		ChatDetailsDTO chatDetailsDTO = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new TransparentProgressDialog(context);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONObject doInBackground(ChatDetailsDTO... args) {
			chatDetailsDTO = args[0];
			// http://54.219.211.237/KraveAPI/api_calls/isImageSeen.php?user_id=770&image=1432104507_.png?seconds=11&time=11
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String imageUrl = chatDetailsDTO.getMessage();
			String imageId = imageUrl.substring(
					(imageUrl.lastIndexOf('/') + 1),
					(imageUrl.lastIndexOf('?')));
			String time = imageUrl.split("=")[1];

			params.add(new BasicNameValuePair("user_id", ""
					+ chatDetailsDTO.getFromuser()));
			params.add(new BasicNameValuePair("image", imageId));
			params.add(new BasicNameValuePair("time", time));
			Log.d("ADD_UPDATE_SNAP_CHAT_MSG_TIME", ""
					+ WebServiceConstants.ADD_UPDATE_SNAP_CHAT_MSG_TIME
					+ params);
			JSONObject json = new JSONParser().makeHttpRequest2(
					WebServiceConstants.ADD_UPDATE_SNAP_CHAT_MSG_TIME, "POST",
					params);
			Log.d("ADD_UPDATE_SNAP_CHAT_MSG_TIME", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			// dialog.dismiss();
			// {"MSG":"45","S":200}
			try {
				if (jsonObject.getInt("S") == AppConstants.RESPONSE_SUCCESS) {
					int snapTime = Integer.valueOf(chatDetailsDTO.getMessage()
							.split("=")[1]);
					ImageView image = (ImageView) chatDetailsDTO.getView();
					setSnapChatImage(snapTime, image);
					chatDetailsDTO.getProgressBar().setVisibility(View.GONE);
					chatDetailsDTO.setImageSnapChatLeftTime(snapTime);

				} else {

				}

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}

}
