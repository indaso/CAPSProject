package edu.upenn.capsproject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuidedActivity extends Activity {
	// count to keep track of which stage the conversation is on. Start on stage 1.
	int currentStage = 1;

	// value for which to end the conversation. Could change in the future.
	int endstage = 0;

	// map to keep track of which prompts to use. First string array entry is giver text
	// and second array entry is receiver text
	HashMap<Integer, conversationStage> prompts = new HashMap<Integer, conversationStage>();

	// support giver and receiver names
	String receiverName;
	String giverName;

	// keep track of buttons and prompts
	Button supportReceiverButton1, supportReceiverButton2, supportGiverButton1, supportGiverButton2;
	TextView supportReceiverPrompt, supportGiverPrompt;

	// string to return back to survey activity after guided activity is finished
	public static final String GUIDED_FINISHED = "edu.upenn.capsproject.guided_finished";

	// datatype to hold prompts, button texts, and what the next stage should be
	public class conversationStage{
		public String receiverPrompt;
		public String giverPrompt;
		public String receiverButtonText1;
		public String receiverButtonText2;
		public String giverButtonText1;
		public String giverButtonText2;
		public int receiverButton1NextStage;
		public int receiverButton2NextStage;
		public int giverButton1NextStage;
		public int giverButton2NextStage;

		public conversationStage(String receiverPrompt, String giverPrompt, String
									receiverButtonText1, int receiverButton1NextStage,
									String receiverButtonText2, int receiverButton2NextStage,
									String giverButtonText1, int giverButton1NextStage,
									String giverButtonText2, int giverButton2NextStage){
			this.receiverPrompt = receiverPrompt;
			this.giverPrompt = giverPrompt;
			this.receiverButtonText1 = receiverButtonText1;
			this.receiverButtonText2 = receiverButtonText2;
			this.giverButtonText1 = giverButtonText1;
			this.giverButtonText2 = giverButtonText2;
			this.receiverButton1NextStage = receiverButton1NextStage;
			this.receiverButton2NextStage = receiverButton2NextStage;
			this.giverButton1NextStage = giverButton1NextStage;
			this.giverButton2NextStage = giverButton2NextStage;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		giverName = getIntent().getStringExtra(SurveyActivity.PROVIDER_NAME);
		receiverName = getIntent().getStringExtra(SurveyActivity.RECIPIENT_NAME);
		// create all the stages of the conversation
		try {
			createConversationFromCsv();
		} catch (IOException e) {
			System.out.println("could not read conversations from csv");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guided);

		//flip stuff upside down
		supportReceiverButton1 = (Button)findViewById(R.id.support_receiver_button1);
		supportReceiverButton1.setRotation(180);
		supportReceiverButton1.setText(prompts.get(1).receiverButtonText1);

		supportReceiverButton2 = (Button)findViewById(R.id.support_receiver_button2);
		supportReceiverButton2.setRotation(180);
		supportReceiverButton2.setVisibility(View.GONE);

		TextView supportReceiverName = (TextView) findViewById(R.id.support_receiver_name);
		supportReceiverName.setRotation(180);
		supportReceiverName.setText(receiverName);

		supportReceiverPrompt = (TextView) findViewById(R.id.support_receiver_prompt);
		supportReceiverPrompt.setRotation(180);
		supportReceiverPrompt.setText(prompts.get(1).receiverPrompt);

		supportGiverButton1 = (Button)findViewById(R.id.support_giver_button1);
		// hide button since there is no text
		supportGiverButton1.setVisibility(View.GONE);

		supportGiverButton2 = (Button)findViewById(R.id.support_giver_button2);
		supportGiverButton2.setVisibility(View.GONE);

		TextView supportGiverName = (TextView) findViewById(R.id.support_giver_name);
		supportGiverName.setText(giverName);

		supportGiverPrompt = (TextView) findViewById(R.id.support_giver_prompt);
		supportGiverPrompt.setText(prompts.get(1).giverPrompt);

		//listener for button clicks on the prompt buttons
		supportReceiverButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// switch to the next conversation stage associated with this button
				int nextStage = prompts.get(currentStage).receiverButton1NextStage;
				System.out.println(nextStage);

				// if next stage is end stage then we switch to end survey
				boolean conversationContinue = checkIfEndStage(nextStage);

				// or if it is not then switch to next stage
				if (conversationContinue) {
					conversationStageSwitch(prompts.get(nextStage));
				}
				currentStage = nextStage;
			}
		});

		supportReceiverButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int nextStage = prompts.get(currentStage).receiverButton2NextStage;
				System.out.println(nextStage);

				boolean conversationContinue = checkIfEndStage(nextStage);
				if (conversationContinue) {
					conversationStageSwitch(prompts.get(nextStage));
				}
				currentStage = nextStage;
			}
		});

		supportGiverButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int nextStage = prompts.get(currentStage).giverButton1NextStage;
				System.out.println(nextStage);

				boolean conversationContinue = checkIfEndStage(nextStage);
				if (conversationContinue){
					conversationStageSwitch(prompts.get(nextStage));
				}
				currentStage = nextStage;
			}
		});

		supportGiverButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int nextStage = prompts.get(currentStage).giverButton2NextStage;
				System.out.println(nextStage);

				boolean conversationContinue = checkIfEndStage(nextStage);
				if (conversationContinue) {
					conversationStageSwitch(prompts.get(nextStage));
				}
				currentStage = nextStage;
			}
		});
	}

    // method to check if we should switch to end survey
	public boolean checkIfEndStage(int myStage){
		if (myStage == endstage){
			Intent i = new Intent(GuidedActivity.this, SurveyActivity.class);
			i.putExtra(GUIDED_FINISHED, false);
			setResult(RESULT_OK, i);
			finish();
			return false;
		}
		return true;
	}

	// create the conversation stages from the csv
	public void createConversationFromCsv() throws IOException, InterruptedException{
		if (isOnline()){
			Thread thread = new Thread(new Runnable(){
			    @Override
				public void run() {
			        try {
			        	HttpClient httpClient = new DefaultHttpClient();
			            HttpContext localContext = new BasicHttpContext();
			            String uri = "https://capsscriptfiles.s3.amazonaws.com/uploads/script.csv";
			            HttpGet httpGet = new HttpGet(uri);
			            HttpResponse response = httpClient.execute(httpGet, localContext);
			    		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			    		String line;

			    		// write to internal storage
			    		String FILENAME = "script.csv";
			    		String stringToWrite = "";
			    		deleteFile(FILENAME);
			    		FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			    		while ((line = reader.readLine()) != null) {
			    			parseConversationStageFromLine(line);
			    			stringToWrite = line + "\n";
				    		fos.write(stringToWrite.getBytes());
			    		}
			    		fos.close();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }
			});
			thread.start();
			thread.join();
		} else {
			// if not online look in the local file
			// look in the default csv

			String FILENAME = "script.csv";
			FileInputStream fis = null;
			try {
				fis = openFileInput(FILENAME);
				InputStreamReader isr = new InputStreamReader(fis);
	               BufferedReader bufferedReader = new BufferedReader(isr);
	               String line;
	               try {
	                while ((line = bufferedReader.readLine()) != null) {
	                       parseConversationStageFromLine(line);
	                   }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
			} catch (FileNotFoundException e1) {
			    InputStream inputStream = getResources().openRawResource(R.raw.script);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                parseConversationStageFromLine(line);
	            }
	            reader.close();
			}
		}

	}

	public void parseConversationStageFromLine(String line){
		System.out.println(line);
		// do not skip empty csv values
		String[] values = line.split(",", -1);
		int stageLevel = Integer.parseInt(values[0]);

		// the last stage should be the stage at which the conversation ends
		endstage = stageLevel;

		// replace [support provider] place holder with actual name
		String receiverTemp1 = values[1];
		String receiverTemp2 = receiverTemp1.replace("[support provider]", giverName);
		String receiverPrompt = receiverTemp2.replace("[support recipient]", receiverName);

		String receiverButton1Text = values[2];
		int receiverButton1Dst = 1;
		if (!(values[3]).trim().equals("")){
			receiverButton1Dst = Integer.parseInt(values[3]);
		}
		String receiverButton2Text = values[4];
		int receiverButton2Dst = 1;
		if (!(values[5]).trim().equals("")){
			System.out.println("value[5] is " + values[5]);
			receiverButton2Dst = Integer.parseInt(values[5]);
		}
		String giverTemp1 = values[6];
		String giverTemp2 = giverTemp1.replace("[support provider]", giverName);
		String giverPrompt = giverTemp2.replace("[support recipient]", receiverName);

		String giverButton1Text = values[7];
		int giverButton1Dst = 1;
		if (!(values[8]).trim().equals("")){
			giverButton1Dst = Integer.parseInt(values[8]);
		}
		String giverButton2Text = values[9];
		int giverButton2Dst = 1;
		if (!(values[10]).trim().equals("")){
			giverButton2Dst = Integer.parseInt(values[10]);
		}

		// construct the conversation stage
		conversationStage myConvo = new conversationStage(receiverPrompt, giverPrompt, receiverButton1Text, receiverButton1Dst,
															receiverButton2Text, receiverButton2Dst, giverButton1Text,
															giverButton1Dst, giverButton2Text, giverButton2Dst);
		prompts.put(stageLevel, myConvo);
	}

    // method for switching between the conversation stages
	public void conversationStageSwitch(conversationStage newStage){
			if (newStage.giverPrompt != null){
				supportGiverPrompt.setText(newStage.giverPrompt);
			}
			if (newStage.receiverPrompt != null){
				supportReceiverPrompt.setText(newStage.receiverPrompt);
			}
			// if the button has no text then we hide it
			if(!newStage.receiverButtonText1.equals("")){
				supportReceiverButton1.setText(newStage.receiverButtonText1);
				supportReceiverButton1.setVisibility(View.VISIBLE);
			} else {
				supportReceiverButton1.setVisibility(View.GONE);
			}
			if(!newStage.receiverButtonText2.equals("")){
				supportReceiverButton2.setText(newStage.receiverButtonText2);
				supportReceiverButton2.setVisibility(View.VISIBLE);
			} else {
				supportReceiverButton2.setVisibility(View.GONE);
			}
			if(!newStage.giverButtonText1.equals("")){
				supportGiverButton1.setText(newStage.giverButtonText1);
				supportGiverButton1.setVisibility(View.VISIBLE);
			} else {
				supportGiverButton1.setVisibility(View.GONE);
			}
			if(!newStage.giverButtonText2.equals("")){
				supportGiverButton2.setText(newStage.giverButtonText2);
				supportGiverButton2.setVisibility(View.VISIBLE);
			} else {
				supportGiverButton2.setVisibility(View.GONE);
			}
	}

	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guided, menu);
		return true;
	}

	public HashMap<Integer, GuidedActivity.conversationStage> getPrompts(){
		return prompts;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public void setGiverName(String giverName) {
		this.giverName = giverName;
	}


}
