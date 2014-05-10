package edu.upenn.capsproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class InformationActivity extends Activity {

    protected String support_recipient;
    protected String support_provider;

    protected EditText mRecipientEText;
    protected EditText mProviderEText;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mRecipientEText = (EditText) findViewById(R.id.enter_receiver_name);
        mProviderEText = (EditText) findViewById(R.id.enter_giver_name);

        mRecipientEText.addTextChangedListener(new TextWatcher() {

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
                support_recipient = s.toString();
            }

        });

        mProviderEText.addTextChangedListener(new TextWatcher() {

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
                support_provider = s.toString();
            }

        });

    }

    // Continue to Pre-conversation Survey
    public void onNextButtonClick(View v) {
        Intent i = new Intent(this, SurveyActivity.class);
        i.putExtra("supportRecipient", this.support_recipient);
        i.putExtra("supportProvider", this.support_provider);
        startActivityForResult(i, 0);
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	finish();
    }

    public String getSupportRecipient(){
    	return support_recipient;
    }
    public String getSupportProvider(){
    	return support_provider;
    }

}
