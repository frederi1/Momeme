package com.example.momeme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class Login extends Activity implements OnItemSelectedListener{
	public String username = "Jenny";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Log.d("onCreate", "started");
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kids, android.R.layout.simple_spinner_item);

    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	spinner.setAdapter(adapter);
    	spinner.setOnItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
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
    
    public void enterName (View view) {
    	 Intent intent = new Intent(this, LoadingActivity.class);
    	 //Log.d("Send out", username);
    	 intent.putExtra("user", username);
    	 startActivity(intent);
    }


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d("User Selected", "Who:" + parent.getItemAtPosition(position));
		
		username = (String)parent.getItemAtPosition(position);
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
