package edu.upenn.capsproject.test;

import java.util.HashMap;

import android.test.ActivityInstrumentationTestCase2;
import edu.upenn.capsproject.GuidedActivity;

public class GuidedActivityTest extends
		ActivityInstrumentationTestCase2<GuidedActivity> {

	private GuidedActivity mActivity;

	public GuidedActivityTest() {
		super(GuidedActivity.class);
	}

	@Override
	protected void setUp() throws Exception{
		super.setUp();

		setActivityInitialTouchMode(false);

		mActivity = new GuidedActivity();
		mActivity.setGiverName("Test");
		mActivity.setReceiverName("TestReceiver");
	}

	public void testParseConversationStageFromLine(){
		String line = "1,Tell [support provider] what's on your mind. Take your "
				+ "time and speak for as long as you like.,Done,2, , ,Listen,,,,";
		mActivity.parseConversationStageFromLine(line);

		HashMap<Integer, GuidedActivity.conversationStage> prompts = mActivity.getPrompts();

		assertEquals(1, prompts.size());
		GuidedActivity.conversationStage cs = prompts.get(1);
		assertEquals("Tell Test what's on your mind. Take your "
				+ "time and speak for as long as you like.", cs.receiverPrompt);
		assertEquals(2, cs.receiverButton1NextStage);
		assertEquals("Listen", cs.giverPrompt);
	}

	public void testParseConversationStageFromLine2(){
		String line = "2,,,,,,Did you understand what [support recipient] said?,Yes,4,No,3";
		mActivity.parseConversationStageFromLine(line);

		HashMap<Integer, GuidedActivity.conversationStage> prompts = mActivity.getPrompts();

		assertEquals(1, prompts.size());
		GuidedActivity.conversationStage cs = prompts.get(2);
		assertEquals("Did you understand what TestReceiver said?", cs.giverPrompt);
		assertEquals(4, cs.giverButton1NextStage);
	}
}
