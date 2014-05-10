package edu.upenn.capsproject.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.EditText;
import edu.upenn.capsproject.InformationActivity;
import edu.upenn.capsproject.R;

public class InformationActivityTest extends
		ActivityInstrumentationTestCase2<InformationActivity> {

	private InformationActivity mActivity;
	private EditText mRecipientEText;
	private EditText mProviderEText;

	public InformationActivityTest() {
		super(InformationActivity.class);
	}

	@Override
	protected void setUp() throws Exception{
		super.setUp();

		setActivityInitialTouchMode(false);

		mActivity = getActivity();

        mRecipientEText = (EditText) mActivity.findViewById(R.id.enter_receiver_name);
        mProviderEText = (EditText) mActivity.findViewById(R.id.enter_giver_name);
	}

	public void testInputFields(){
	    TouchUtils.tapView(this, mRecipientEText);
	    sendKeys("J O H N");
	    TouchUtils.tapView(this, mProviderEText);
	    sendKeys("J A N E");

	    assertEquals("john", mActivity.getSupportRecipient());
	    assertEquals("jane", mActivity.getSupportProvider());
	}

}
