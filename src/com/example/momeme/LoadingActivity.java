package com.example.momeme;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.media.AudioManager;

public class LoadingActivity extends Activity {
	
	private Thread loadingThread;
	private SoundPool sp;
	private int soundID;
	boolean loaded = false;
	
	RelativeLayout rl;
	int clickCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		Intent temp = getIntent();
		final String user = temp.getStringExtra("user");

		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		sp.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				// TODO Auto-generated method stub
				loaded = true;
			}
		});
		
		soundID = sp.load(this, R.raw.line_fun, 1);
		
		rl = (RelativeLayout) findViewById(R.id.loading_layout);
		
		rl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//MediaPlayer mp = MediaPlayer.create(LoadingActivity.this, R.raw.line_fun);
				//mp.start();
				
				 AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
				 float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				 float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				 float volume = actualVolume / maxVolume;
				 if (loaded) {
				        sp.play(soundID, volume, volume, 1, 0, 1f);
				        //Log.e("Test", "Played sound");
				      }
				
				clickCount++;
			}
		});
		
		loadingThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(5000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {
                    Intent in = new Intent(LoadingActivity.this, MainActivity.class);
                    in.putExtra("user", user);
                    startActivity(in);
                    finish();
                }
            }
        };
        loadingThread.start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onTouchEvent (MotionEvent evt) {
		if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(loadingThread){
            	loadingThread.notifyAll();
            }
        }
        return true;
	}
}
