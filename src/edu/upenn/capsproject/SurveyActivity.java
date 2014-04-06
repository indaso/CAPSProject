package edu.upenn.capsproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SurveyActivity extends Activity {
	// before survey slider ratings
	protected int bFeelRating;
	protected int bFriendshipRating;

	// after survey slider ratings
	protected int aFeelRating;
	protected int aFriendshipRating;

	// entering names in EditText
	protected EditText mProviderEText;
	protected EditText mRecipientEText;

	// actual names of recipient and provider
	protected String support_recipient;
	protected String support_provider;

	// Recipient topic questions
	protected EditText topic;
	protected String topicText;
	protected TextView recipientNotification;
	protected TextView recipientAskTopic;
	protected TextView recipientFeelings;
	protected TextView recipientFriendship;

	// Horizontal sliders
	protected SeekBar seekBarFeel;
	protected SeekBar seekBarFriend;

	// TextViews for ratings
	protected TextView seekBarFeelView;
	protected TextView seekBarFriendView;

	// Button to start conversation
	protected Button startConversation;

	// Hide actionBar
	protected ActionBar actionBar;

	// Strings to pass in extras for the guided activity
	public static final String RECIPIENT_NAME = "edu.upenn.capsproject.recipient_name";
	public static final String PROVIDER_NAME = "edu.upenn.capsproject.provider_name";

	/**
	 * Variable on whether to show various buttons and views. When false, some
	 * views in this activity will not be shown for the Post Survey.
	 */
	protected static boolean showBeforeViews = true;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_survey);

		mProviderEText = (EditText) findViewById(R.id.enter_giver_name);
		mRecipientEText = (EditText) findViewById(R.id.enter_receiver_name);

		mProviderEText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				support_provider = s.toString();
			}

		});

		mProviderEText.setText(support_provider);

		mRecipientEText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				recipientNotification = (TextView) findViewById(R.id.receiver_notification);
				recipientNotification
						.setText("The following questions are for "
								+ support_recipient + ".");

				recipientAskTopic = (TextView) findViewById(R.id.recipient_talk_question);
				recipientAskTopic.setText(support_recipient
						+ ", what are you going to talk about?");

				recipientFeelings = (TextView) findViewById(R.id.recipient_feel_question);
				recipientFeelings
						.setText(support_recipient
								+ ", how good or bad do you feel right now?\n(Very Bad to Very Good)");

				recipientFriendship = (TextView) findViewById(R.id.recipient_friendship_question);
				recipientFriendship.setText(support_recipient
						+ ", how close is your friendship with "
						+ support_provider + "?\n Not Close to Very Close");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				support_recipient = s.toString();
			}

		});

		// seekBar actions
		seekBarFeel = (SeekBar) findViewById(R.id.seekBar_feel);
		seekBarFriend = (SeekBar) findViewById(R.id.seekBar_friend);
		seekBarFeelView = (TextView) findViewById(R.id.seekbar_feel_output);
		seekBarFriendView = (TextView) findViewById(R.id.seekBar_friend_output);

		seekBarFeel.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				bFeelRating = progress;
				String sFeel = String.valueOf(bFeelRating);
				seekBarFeelView.setText("Feeling rating: " + sFeel);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

		});

		seekBarFriend.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				bFriendshipRating = progress;
				String sFriend = String.valueOf(bFriendshipRating);
				seekBarFriendView.setText("Friendship Rating: " + sFriend);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

		});

		topic = (EditText) findViewById(R.id.recipient_talk_answer);

		topic.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				topicText = s.toString();
			}

		});

		// Button to start conversation
		startConversation = (Button) findViewById(R.id.guided_conversation);

		// When clicked, start new Activity
		startConversation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SurveyActivity.this, GuidedActivity.class);
				i.putExtra(RECIPIENT_NAME, support_recipient);
				i.putExtra(PROVIDER_NAME, support_provider);
				startActivityForResult(i, 0);
			}
		});

	}

	/*
	 * Eventually will be used to determine whether to use Pre Survey or Post
	 * Survey by setting showBeforeViews variable to false. if Intent has an
	 * extra that returns a boolean that is true, change showBeforeViews to
	 * false
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// name will be from GuidedActivity string similar to recipient name
		 showBeforeViews = data.getBooleanExtra(GuidedActivity.GUIDED_FINISHED, true);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		Log.d("destroy method", "got called");
		try {
			Logger.createLogger(getFilesDir());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String mFormatString = "EEEE, MMMMM d, yyyy";
		Date d = new Date();
		String[] info1 = {DateFormat.format(mFormatString, d).toString(),
				support_recipient, String.valueOf(bFeelRating), 
				String.valueOf(aFeelRating), String.valueOf(bFriendshipRating),
				String.valueOf(aFriendshipRating)};
		ArrayList<String[]> s1 = new ArrayList<String[]>();
		s1.add(info1);
		try {
			Logger.getLogger().write(s1);
			Logger.getLogger().close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.survey, menu);
		return true;
	}

}
