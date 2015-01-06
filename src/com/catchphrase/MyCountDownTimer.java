package com.catchphrase;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.AudioManager;

public class MyCountDownTimer extends CountDownTimer
{
	int timeRemaining;
	int vibrateStart;
	Vibrator vibe;
	Context context;
	MediaPlayer mPlayer;
	Activity current;
	
	
	public MyCountDownTimer(long length, int tickInterval, Activity current, Vibrator vibe, int vibrateStart)
	{
		super(length, tickInterval);
		this.context = current.getApplicationContext();
		mPlayer = MediaPlayer.create(context, R.raw.horn);
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		this.vibe = vibe;
		this.current = current;
		this.vibrateStart = vibrateStart;
	}
	
	public void onTick(long millisUntilFinished)
	{
		if(millisUntilFinished < vibrateStart && (((millisUntilFinished/1000) * 1000 % 1000)) == 0)
			vibrate(100, (int)millisUntilFinished);
		timeRemaining = (int)millisUntilFinished;
	}
	
	public void onFinish()
	{
		//mTimerTextView.setText("0s");
		Toast toast = Toast.makeText(context.getApplicationContext(), "Time's up!", Toast.LENGTH_LONG);
		toast.show();
		mPlayer.start();
		vibe.vibrate(1000);
		current.finish();
	}
	
	public void vibrate(int duration, int millisUntilFinished)
	{
		//TODO: fix this, make it shorter and look prettier
		long[] pattern;
		if(millisUntilFinished < vibrateStart/10)
		{
			pattern = new long[] {0,100,100,100,100,100,100,100,100,100};
			vibe.vibrate(pattern, 1);
		}
		else if(millisUntilFinished < vibrateStart/2)
		{
			pattern = new long[] {0,100,400,100,400};
			vibe.vibrate(pattern, 1);
		}
		else
		{
			vibe.vibrate(duration);
		}
	}
	
	public int getTimeRemaining()
	{
		return timeRemaining;
	}
}