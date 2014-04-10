package edu.upenn.capsproject;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuidedActivity extends Activity {
	// count to keep track of which stage the conversation is on. Start on stage 1.
	int currentStage = 1;

	// value for which to end the conversation. Could change in the future. 
	final int ENDSTAGE = 28;
	
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
	class conversationStage{
		String receiverPrompt;
		String giverPrompt; 
		String receiverButtonText1;
		String receiverButtonText2;
		String giverButtonText1;
		String giverButtonText2;
		int receiverButton1NextStage;
		int receiverButton2NextStage;
		int giverButton1NextStage;
		int giverButton2NextStage;
		
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
		conversationStage stage1 = new conversationStage("What's on your mind?", "Listen", "Done", 2, "", 0, "", 0, "", 0);
		prompts.put(1, stage1);
		conversationStage stage2 = new conversationStage("", "Did you understand what " + receiverName + " said?", "", 0, "", 0, "Yes", 4, "No", 3);
		prompts.put(2, stage2);
		conversationStage stage3 = new conversationStage("Listen", "Ask a clarifying question", "", 0, "", 0, "Done", 5, "", 0);
		prompts.put(3, stage3);
		conversationStage stage4 = new conversationStage("Listen", "Tell " + receiverName + " what you heard - just say it back", 
																																				"", 0, "", 0, "Done", 6, "", 0);
		prompts.put(4, stage4);
		conversationStage stage5 = new conversationStage("Answer " + giverName + "'s question", "Listen", "", 0, "", 0, "Done", 6, "", 0);
		prompts.put(5, stage5);
		conversationStage stage6 = new conversationStage("Did " + giverName + " understand you?", "", "Yes", 8, "No", 7, "", 0, "", 0);
		prompts.put(6, stage6);
		conversationStage stage7 = new conversationStage("Explain what " + giverName + " did not.", "Listen", "Done", 2, "", 0, "", 0, "", 0);
		prompts.put(7, stage7);
		conversationStage stage8 = new conversationStage("Do you have more to share?", "", "Yes", 9, "No", 10, "", 0, "", 0);
		prompts.put(8, stage8);
		conversationStage stage9 = new conversationStage("Great! Share some more.", "Listen", "Done", 2, "", 0, "", 0, "", 0);
		prompts.put(9, stage9);
		conversationStage stage10 = new conversationStage("Listen", "Briefly summarize everything " + receiverName + " talked about during this conversation", 
																												"", 0, "", 0, "Done", 11, "", 0);
		prompts.put(10, stage10);
		conversationStage stage11 = new conversationStage("Did " + giverName + " understand you?", "", "Yes", 12, "No", 7, "", 0, "", 0);
		prompts.put(11, stage11);
		conversationStage stage12 = new conversationStage("", "Do you have advice, an opinion, or a story to share?", "", 0, "", 0, "Yes", 13, "No", 14);
		prompts.put(12, stage12);
		conversationStage stage13 = new conversationStage("Listen", "Ask " + receiverName + " if it's okay for you to share your advice/opinion/story.", "", 0, "", 0, "Done", 15, "", 0);
		prompts.put(13, stage13);		
		conversationStage stage14 = new conversationStage("", "Would you like to offer reassurance or let " + receiverName + " know that you care?", "", 0, "", 0, "Yes", 22, "No", 25);
		prompts.put(14, stage14);
		conversationStage stage15 = new conversationStage("Answer " + giverName + "'s question", "Listen", "Done", 16, "", 0, "", 0, "", 0);
		prompts.put(15, stage15);		
		conversationStage stage16 = new conversationStage("", "What did " + receiverName + " say help?", "", 0, "", 0, "Yes", 17 , "No", 14);
		prompts.put(16, stage16);
		conversationStage stage17 = new conversationStage("Listen", "Say 'I realize I don't know everything about your situation, so tell me if what I say makes no sense to you.", "", 0, "", 0, "Done", 18, "", 0);
		prompts.put(17, stage17);
		conversationStage stage18 = new conversationStage("Listen", "Share your advice / opinion / story", "", 0, "", 0, "Done", 19, "", 0);
		prompts.put(18, stage18);
		conversationStage stage19 = new conversationStage("How helpful is what " + giverName + " said?", "", "Helpful", 20, "Not Helpful", 21, "", 0, "", 0);
		prompts.put(19, stage19);
		conversationStage stage20 = new conversationStage("Explain to " + giverName + " why you feel this way", "Listen", "Done", 12, "", 0, "", 0, "", 0);
		prompts.put(20, stage20);
		conversationStage stage21 = new conversationStage("Explain to " + giverName + " why you feel this way", "Listen", "Done", 4, "", 0, "", 0, "", 0);
		prompts.put(21, stage21);
		conversationStage stage22 = new conversationStage("Listen", "Offer reassurance or let " + receiverName + " know that you care", "", 0, "", 0, "Done", 23, "", 0);
		prompts.put(22, stage22);
		conversationStage stage23 = new conversationStage("Listen", "Thank " + receiverName + " for sharing", "", 0, "", 0, "Done", 24, "", 0);
		prompts.put(23, stage23);
		conversationStage stage24 = new conversationStage("Thank " + giverName + " for listening to you and offering support", "", "Done", 28, "", 0, "", 0, "", 0);
		prompts.put(24, stage24);
		conversationStage stage25 = new conversationStage("", "Would you like to offer to help " + receiverName + " now or in the future?", "", 0, "", 0, "Yes", 26, "No", 23);
		prompts.put(25, stage25);
		conversationStage stage26 = new conversationStage("Listen", "Ask 'How can I help, now or in the future'", "", 0, "", 0, "Done", 27, "", 0);
		prompts.put(26, stage26);
		conversationStage stage27 = new conversationStage("Answer " + giverName + "'s question", "Listen", "Done", 23, "", 0, "", 0, "", 0);
		prompts.put(27, stage27);


		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guided);
		
		//flip stuff upside down
		supportReceiverButton1 = (Button)findViewById(R.id.support_receiver_button1);
		supportReceiverButton1.setRotation(180);
		supportReceiverButton1.setText(stage1.receiverButtonText1);
		
		supportReceiverButton2 = (Button)findViewById(R.id.support_receiver_button2);
		supportReceiverButton2.setRotation(180);
		supportReceiverButton2.setVisibility(View.INVISIBLE);
				
		TextView supportReceiverName = (TextView) findViewById(R.id.support_receiver_name);
		supportReceiverName.setRotation(180);
		supportReceiverName.setText(receiverName);

		supportReceiverPrompt = (TextView) findViewById(R.id.support_receiver_prompt);
		supportReceiverPrompt.setRotation(180);
		supportReceiverPrompt.setText(stage1.receiverPrompt);

		supportGiverButton1 = (Button)findViewById(R.id.support_giver_button1);
		// hide button since there is no text
		supportGiverButton1.setVisibility(View.INVISIBLE);
		
		supportGiverButton2 = (Button)findViewById(R.id.support_giver_button2);
		supportGiverButton2.setVisibility(View.INVISIBLE);
		
		TextView supportGiverName = (TextView) findViewById(R.id.support_giver_name);
		supportGiverName.setText(giverName);

		supportGiverPrompt = (TextView) findViewById(R.id.support_giver_prompt);
		supportGiverPrompt.setText(stage1.giverPrompt);
		
		//listener for button clicks on the prompt buttons
		supportReceiverButton1.setOnClickListener(new View.OnClickListener() {
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
		if (myStage == ENDSTAGE){
			Intent i = new Intent(GuidedActivity.this, SurveyActivity.class);
			i.putExtra(GUIDED_FINISHED, true);
			setResult(RESULT_OK, i);
			finish();
			return false;
		}
		return true;
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
				supportReceiverButton1.setVisibility(View.INVISIBLE);
			}
			if(!newStage.receiverButtonText2.equals("")){
				supportReceiverButton2.setText(newStage.receiverButtonText2);
				supportReceiverButton2.setVisibility(View.VISIBLE);
			} else {
				supportReceiverButton2.setVisibility(View.INVISIBLE);
			}
			if(!newStage.giverButtonText1.equals("")){
				supportGiverButton1.setText(newStage.giverButtonText1);
				supportGiverButton1.setVisibility(View.VISIBLE);
			} else {
				supportGiverButton1.setVisibility(View.INVISIBLE);
			}
			if(!newStage.giverButtonText2.equals("")){
				supportGiverButton2.setText(newStage.giverButtonText2);
				supportGiverButton2.setVisibility(View.VISIBLE);
			} else {
				supportGiverButton2.setVisibility(View.INVISIBLE);
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guided, menu);
		return true;
	}

}
