package com.example.momeme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

public class LoadingActivity extends Activity {
	
	private Thread loadingThread;
	//public final String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		Intent temp = getIntent();
		final String user = temp.getStringExtra("user");
		Log.d("Got from Login", user);
		
		loadingThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);  //Delay of 10 seconds
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
