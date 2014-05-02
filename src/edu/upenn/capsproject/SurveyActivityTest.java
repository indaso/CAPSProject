package edu.upenn.capsproject;

import junit.framework.TestCase;

public class SurveyActivityTest extends TestCase {
	SurveyActivity s;
	protected void setUp() throws Exception {
		super.setUp();
		
		s = new SurveyActivity();
	}
	
	
	public void testProgressBar() {
		s.seekBarFriend.setProgress(30);
		assertEquals(s.seekBarFriendView.getText(), "30");
	}
}
