package edu.upenn.capsproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.github.sendgrid.SendGrid;

public class SurveyActivity extends Activity {

    // actual names of recipient and provider
    protected String supportRecipient;
    protected String supportProvider;

    // Conversation topic
    protected String topicText;

    // before survey slider ratings
    protected int bFeelRating;
    protected int bFriendshipRating;

    // after survey slider ratings
    protected int aFeelRating;
    protected int aFriendshipRating;
    protected int aSupportRating;

    protected TextView recipientNotification;

    // Topic questions
    protected TextView recipientAskTopic;
    protected EditText topic;

    protected TextView recipientFeelings;
    protected TextView recipientFriendship;
    protected TextView recipientSupport;

    // Horizontal sliders
    protected SeekBar seekBarFeel;
    protected SeekBar seekBarFriend;
    protected SeekBar seekBarSupport;

    // TextViews for ratings
    protected TextView seekBarFeelView2;
    protected TextView seekBarFriendView;
    protected TextView seekBarSupportView;

    // TextView for Survey Type
    protected TextView surveyType;

    // Button to start conversation
    protected Button startConversation;

    // Hide actionBar
    protected ActionBar actionBar;

    // Timestamps
    protected Date dBefore;
    protected Date dAfter;

    protected String sTimeBefore;
    protected String sTimeAfter = "";

    protected String mFormatString = "yyyy-MM-dd hh:mm:ss";

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.supportRecipient = extras.getString("supportRecipient");
            this.supportProvider = extras.getString("supportProvider");
        }

        surveyType = (TextView) findViewById(R.id.survey_title);
        surveyType.setText("Pre-conversation Questionnaire");

        recipientNotification = (TextView) findViewById(R.id.receiver_notification);
        recipientNotification.setText("The following questions are for "
                + supportRecipient + ":");

        recipientAskTopic = (TextView) findViewById(R.id.recipient_talk_question);
        recipientAskTopic.setText(supportRecipient
                + ", what are you going to talk about?");

        topic = (EditText) findViewById(R.id.recipient_talk_answer);

        topic.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                topicText = s.toString();
            }

        });

        // Feel seekbar
        recipientFeelings = (TextView) findViewById(R.id.recipient_feel_question);
        recipientFeelings
                .setText(supportRecipient
                        + ", how good or bad do you feel right now?\n(Very Bad to Very Good)");

        seekBarFeelView2 = (TextView) findViewById(R.id.seekbar_feel_output);
        seekBarFeelView2
                .setText("Very Bad                      Neutral                      Very Good");

        seekBarFeel = (SeekBar) findViewById(R.id.seekBar_feel);
        seekBarFeel.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                if (showBeforeViews) {
                    bFeelRating = progress;
                } else {
                    aFeelRating = progress;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        // Support Seekbar
        recipientSupport = (TextView) findViewById(R.id.recipient_support_question);
        recipientSupport
                .setText("How good or bad was the support you received from "
                        + supportRecipient + " during this conversation?\n"
                        + "(Very Bad to Very Good)");

        seekBarSupportView = (TextView) findViewById(R.id.seekBar_support_output);
        seekBarSupportView
                .setText("Very Bad                      Neutral                      Very Good");

        seekBarSupport = (SeekBar) findViewById(R.id.seekBar_support);
        seekBarSupport
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                            int progress, boolean fromUser) {
                        aSupportRating = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                });

        recipientSupport.setVisibility(View.GONE);
        seekBarSupportView.setVisibility(View.GONE);
        seekBarSupport.setVisibility(View.GONE);

        // Friendship Seekbar
        recipientFriendship = (TextView) findViewById(R.id.recipient_friendship_question);
        recipientFriendship.setText(supportRecipient
                + ", how close is your friendship with " + supportProvider
                + "?\n(Not Close to Very Close)");

        seekBarFriendView = (TextView) findViewById(R.id.seekBar_friend_output);
        seekBarFriendView
                .setText("Not Close                    Neutral                    Very Close");

        seekBarFriend = (SeekBar) findViewById(R.id.seekBar_friend);
        seekBarFriend.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                if (showBeforeViews) {
                    bFriendshipRating = progress;
                } else {
                    aFriendshipRating = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        // Button to start conversation
        startConversation = (Button) findViewById(R.id.guided_conversation);

        // When clicked, start new Activity
        startConversation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dBefore = new Date();
                sTimeBefore = DateFormat.format(mFormatString, dBefore).toString();

                Intent i = new Intent(SurveyActivity.this, GuidedActivity.class);
                i.putExtra(RECIPIENT_NAME, supportRecipient);
                i.putExtra(PROVIDER_NAME, supportProvider);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // name will be from GuidedActivity string similar to recipient name
        if (resultCode == RESULT_OK) {
            showBeforeViews = data.getBooleanExtra(
                    GuidedActivity.GUIDED_FINISHED, true);
            dAfter = new Date();
            surveyType.setText("Post-conversation Questionnaire");
            startConversation.setText("End Conversation");
            startConversation.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            recipientNotification.setVisibility(View.GONE);
            recipientAskTopic.setVisibility(View.GONE);
            topic.setVisibility(View.GONE);

            recipientSupport.setVisibility(View.VISIBLE);
            seekBarSupport.setVisibility(View.VISIBLE);
            seekBarSupportView.setVisibility(View.VISIBLE);

            // Formatted date string
            sTimeAfter = DateFormat.format(mFormatString, dAfter).toString();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Logger.createLogger(new File(getFilesDir(), "data.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("onDestroy", sTimeBefore);
        Log.d("onDestroy", sTimeAfter);
        String[] headers = { "Recipient", "Topic", "Start Time", "End Time",
                "Feeling Before", "Friendship Before",
                "Feeling After", "Friendship After", "Support Rating"};
        String[] info1 = { supportRecipient, topicText, sTimeBefore, sTimeAfter,
                String.valueOf(bFeelRating), String.valueOf(bFriendshipRating),
                String.valueOf(aFeelRating), String.valueOf(aFriendshipRating),
                String.valueOf(aSupportRating) };
        ArrayList<String[]> s1 = new ArrayList<String[]>();
        s1.add(headers);
        s1.add(info1);
        try {
            Logger.getLogger().write(s1);
            Logger.getLogger().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
        	Thread thread = new Thread(new Runnable(){
	        	@Override
	        	public void run() {
	        		try {
	        			// get the email from website
	        			HttpClient httpClient = new DefaultHttpClient();
	        			HttpContext localContext = new BasicHttpContext();
	        			String uri = "https://capsscriptfiles.s3.amazonaws.com/uploads/email.txt";
	        			HttpGet httpGet = new HttpGet(uri);
	        			HttpResponse response = httpClient.execute(httpGet, localContext);
	        			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        			String to = reader.readLine();
	        			if (to == null){
	        				to = "eranm@upenn.edu";
	        			}
	        			new SendEmailWithSendGrid().execute(to);
	        		}
	        		catch (Exception e) {
	        			e.printStackTrace();
	        		}
				}
        	});
        	thread.start();
			thread.join();
        } catch (Exception e) {
            Log.d("onDestroy", "email failed");
            e.printStackTrace();
        }
    }

    private class SendEmailWithSendGrid extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            SendGrid sendgrid = new SendGrid("app19013461@heroku.com",
                    "d1buyrvv");
            sendgrid.addTo(params[0]);
            sendgrid.setFrom(params[0]);
            sendgrid.setSubject("Conversation Data");
            sendgrid.setText("CSV File for conversation");
            try {
                sendgrid.addFile(new File(getFilesDir(), "data.csv"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String response = sendgrid.send();
            return response;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.survey, menu);
        return true;
    }
}

