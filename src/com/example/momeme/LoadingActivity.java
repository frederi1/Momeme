package com.example.momeme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class LoadingActivity extends Activity {
	
	private Thread loadingThread;
	//public final String username;
	
	RelativeLayout rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		Intent temp = getIntent();
		final String user = temp.getStringExtra("user");
		Log.d("Got from Login", user);
		
		rl = (RelativeLayout) findViewById(R.id.loading_layout);
		
		rl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MediaPlayer mp = MediaPlayer.create(LoadingActivity.this, R.raw.line_fun);
				mp.start();
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
                	Log.d("Within thread", user);
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
