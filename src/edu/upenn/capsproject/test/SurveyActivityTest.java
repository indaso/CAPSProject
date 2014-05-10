package edu.upenn.capsproject.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.SeekBar;
import edu.upenn.capsproject.R;
import edu.upenn.capsproject.SurveyActivity;

public class SurveyActivityTest extends
		ActivityInstrumentationTestCase2<SurveyActivity> {

	private SurveyActivity mActivity;
	private EditText topic;
	private SeekBar seekBarFeel;
	private SeekBar seekBarFriend;
	public SurveyActivityTest() {
		super(SurveyActivity.class);
	}

	@Override
	protected void setUp() throws Exception{
		super.setUp();

		setActivityInitialTouchMode(false);

		mActivity = getActivity();

		topic = (EditText) mActivity.findViewById(R.id.recipient_talk_answer);
	    seekBarFeel = (SeekBar) mActivity.findViewById(R.id.seekBar_feel);
	    seekBarFriend = (SeekBar) mActivity.findViewById(R.id.seekBar_friend);
	}

	public void testInputFieldsBefore(){
	    TouchUtils.tapView(this, topic);
	    sendKeys("H I");
	    TouchUtils.dragViewToX(this, seekBarFeel, Gravity.CENTER_HORIZONTAL, 300);
	    TouchUtils.dragViewToX(this, seekBarFriend, Gravity.CENTER_HORIZONTAL, 200);

	    assertEquals("hi", mActivity.getTopicText());
	    assertEquals(36, mActivity.getbFeelRating());
	    assertEquals(21, mActivity.getbFriendshipRating());
	}

	public void testInputFieldsAfter(){
		mActivity.setShowBeforeViews(false);

	    TouchUtils.dragViewToX(this, seekBarFeel, Gravity.CENTER_HORIZONTAL, 350);
	    TouchUtils.dragViewToX(this, seekBarFriend, Gravity.CENTER_HORIZONTAL, 100);

	    assertEquals(44, mActivity.getaFeelRating());
	    assertEquals(5, mActivity.getaFriendshipRating());
	}
}
