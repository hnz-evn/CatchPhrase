package com.catchphrase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Vibrator;
import android.media.MediaPlayer;
import android.media.AudioManager;
import java.util.Random;


public class CatchPhraseActivity extends Activity {

	private static final String KEY_TIMER = "Timer";
	private static final int MIN_TIMER_LENGTH = 30;
	private static final int MAX_TIMER_LENGTH = 45;
	private static final int MIN_VIBRATE_TIME = 10;
	private static Vibrator vibe;
	private static int vibrateStart;
	private static int timeRemaining;
	
	private Button mNextButton;
	private Button mSkipButton;
	private TextView mCatchphraseTextView;
	private TextView mTimerTextView;
	private Words mWords;
	private MyCountDownTimer mTimer;
	private MediaPlayer mPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int timerLength;
		Random rand = new Random();

		timerLength = 1000 * (rand.nextInt((MAX_TIMER_LENGTH - MIN_TIMER_LENGTH) + 1) + MIN_TIMER_LENGTH);
		vibrateStart = 1000 * (rand.nextInt(MIN_TIMER_LENGTH + 1) + MIN_VIBRATE_TIME);
		vibe = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

		setContentView(R.layout.activity_catch_phrase);
		
		//create media player
		mPlayer = MediaPlayer.create(this, R.raw.horn);
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		// initialize Words class with CatchPhraseActivity context
		mWords = new Words(this);
		
		// put first word into phrase text view
		mCatchphraseTextView = (TextView) findViewById(R.id.catchphrase_display);
		mCatchphraseTextView.setText(mWords.getNextWord());
		
		// set up timer and tie it to timer text view
		mTimerTextView = (TextView) findViewById(R.id.timer);

		//TODO: create wrapper class MyCountDownTimer so we can resume with same time left...
		mTimer = new MyCountDownTimer(timerLength, 1000, this, vibe, vibrateStart);
		mTimer.start();
		
		// initialize buttons and set onclick events
		mNextButton = (Button) findViewById(R.id.next_button);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCatchphraseTextView.setText(mWords.getNextWord());
				vibe.vibrate(50);
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
	
	//TODO: add all these methods to handle going in/out of focus

	@Override
	protected void onPause()
	{
		super.onPause();
		timeRemaining = mTimer.getTimeRemaining();
		mTimer.cancel();
		mTimer = null;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();

		if (mTimer == null) {
			mTimer = new MyCountDownTimer(timeRemaining, 1000, this, vibe, vibrateStart);
			mTimer.start();
		}
	}
	
//	@Override
//	public void onSaveInstanceState(Bundle savedInstanceState){
//		super.onSaveInstanceState(savedInstanceState);
//		
//		timeRemaining = mTimer.getTimeRemaining();
//		savedInstanceState.putInt(KEY_TIMER, timeRemaining);
//	}
	

//	public void vibrate(int duration, int millisUntilFinished)
//	{
//		//TODO: fix this, make it shorter and look prettier
//		long[] pattern;
//		if(millisUntilFinished < vibrateStart/10)
//		{
//			pattern = new long[] {0,100,100,100,100,100,100,100,100,100};
//			vibe.vibrate(pattern, 1);
//		}
//		else if(millisUntilFinished < vibrateStart/2)
//		{
//			pattern = new long[] {0,100,400,100,400};
//			vibe.vibrate(pattern, 1);
//		}
//		else
//		{
//			vibe.vibrate(duration);
//		}
//	}
}
