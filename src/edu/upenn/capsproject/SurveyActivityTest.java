package edu.upenn.capsproject;

import junit.framework.TestCase;

public class SurveyActivityTest extends TestCase {
	OldSurveyActivity s;
	protected void setUp() throws Exception {
		super.setUp();
		
		s = new OldSurveyActivity();
	}
	
	
	public void testProgressBar() {
		s.seekBarFriend.setProgress(30);
		assertEquals(s.seekBarFriendView.getText(), "30");
	}
}
