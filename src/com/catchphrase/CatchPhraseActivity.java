package com.catchphrase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CatchPhraseActivity extends Activity {

	private Button mNextButton;
	private Button mSkipButton;
	private TextView mCatchphraseTextView;
	private Words words;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catch_phrase);
		words = new Words();
		
		mCatchphraseTextView = (TextView) findViewById(R.id.catchphrase_display);
		mCatchphraseTextView.setText(words.getNextWord());
		
		// initialize buttons and set onclick events
		mNextButton = (Button) findViewById(R.id.next_button);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCatchphraseTextView.setText(words.getNextWord());
			}
		});
		
		mSkipButton = (Button) findViewById(R.id.skip_button);
		mSkipButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCatchphraseTextView.setText(words.getNextWord());
			}
		});
	}
}
