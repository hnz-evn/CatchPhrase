package com.catchphrase;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CatchPhraseActivity extends Activity {

	private Button mNextButton;
	private Button mSkipButton;
	private TextView mCatchphraseTextView;
	private TextView mTimerTextView;
	private Words mWords;
	private CountDownTimer mTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catch_phrase);
		
		mWords = new Words();
		
		// put first word into phrase textview
		mCatchphraseTextView = (TextView) findViewById(R.id.catchphrase_display);
		mCatchphraseTextView.setText(mWords.getNextWord());
		
		// set up timer and tie it to timer textview
		mTimerTextView = (TextView) findViewById(R.id.timer);
		mTimer = new CountDownTimer(15000, 1000) {
			public void onTick(long millisUntilFinished) {
				mTimerTextView.setText(String.valueOf(millisUntilFinished / 1000) + "s");
			}
			public void onFinish() {
				mTimerTextView.setText("0s");
				Toast toast = Toast.makeText(getApplicationContext(), "Time's up!", Toast.LENGTH_LONG);
				toast.show();
			}
		};
		mTimer.start();
		
		// initialize buttons and set onclick events
		mNextButton = (Button) findViewById(R.id.next_button);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCatchphraseTextView.setText(mWords.getNextWord());
			}
		});
		
		mSkipButton = (Button) findViewById(R.id.skip_button);
		mSkipButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCatchphraseTextView.setText(mWords.getNextWord());
			}
		});
	}
}
