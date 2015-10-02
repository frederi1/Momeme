package com.example.momeme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnItemSelectedListener {
	private TextToSpeech ttobj;
	DBHelper mydb;
	EditText toMomET, fromMomET;
	Button submitBut, addQuoteBut, randQuoteBut, cancelBut, delBut;
	public String currentUser;
	public static String quoteAbout = "Jenny";
	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		
		String user = intent.getStringExtra("user");
		
		Log.d("user: ", user);
		
		currentUser = user;
		
		mydb = new DBHelper(this);
		
		setContentView(R.layout.activity_main);
		
		toMomET = (EditText) findViewById(R.id.editText1);
		fromMomET = (EditText) findViewById(R.id.editText2);
		submitBut = (Button) findViewById(R.id.button3);
		randQuoteBut = (Button) findViewById(R.id.button1);
		addQuoteBut = (Button) findViewById(R.id.button2);
		cancelBut = (Button) findViewById(R.id.button4);
		delBut = (Button) findViewById(R.id.button5);
		
		toMomET.setVisibility(View.INVISIBLE);
		fromMomET.setVisibility(View.INVISIBLE);
		submitBut.setVisibility(View.INVISIBLE);
		cancelBut.setVisibility(View.INVISIBLE);
		
		ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			
			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kids, android.R.layout.simple_spinner_item);

	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(this);
	}
	
	public void onPause(){
	      if(ttobj !=null){
	    	  ttobj.stop();
	    	  ttobj.shutdown();
	      }
	      super.onPause();
	   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d("Quote about Selected", "Who:" + parent.getItemAtPosition(position));
		
		quoteAbout = (String)parent.getItemAtPosition(position);
	}
	
	public void randQuote(View view) {
		addQuoteBut.setVisibility(View.INVISIBLE);
		randQuoteBut.setVisibility(View.INVISIBLE);
		spinner.setVisibility(View.INVISIBLE);
		
		
		cancelBut.setText("Go Back");
		cancelBut.setVisibility(View.VISIBLE);
		
		//Retrieve data from db
		ArrayList<String> quotes = new ArrayList<String>();
		Cursor cur;
		Log.d("Obtain data for: ", quoteAbout);
		cur = mydb.getData(quoteAbout);
		if(cur.moveToFirst()){
			do{
				quotes.add(cur.getString(cur.getColumnIndex("from_mom")));
			}while(cur.moveToNext());
			
			Random rand = new Random();
			int val = rand.nextInt(quotes.size());
			
			ttobj.speak(quotes.get(val), TextToSpeech.QUEUE_FLUSH, null);
		}
		else{
			ttobj.speak("No quote was found for " + quoteAbout, TextToSpeech.QUEUE_FLUSH, null);
		}
		return;
	}
	
	
	public void addQuote(View view) {
		addQuoteBut.setVisibility(View.INVISIBLE);
		randQuoteBut.setVisibility(View.INVISIBLE);
		spinner.setVisibility(View.INVISIBLE);
		
		toMomET.setHint("What did " + quoteAbout + " say?");
		
		toMomET.setVisibility(View.VISIBLE);
		fromMomET.setVisibility(View.VISIBLE);
		submitBut.setVisibility(View.VISIBLE);
		
		cancelBut.setText("Cancel");
		cancelBut.setVisibility(View.VISIBLE);
		
	}
	
	private void displayDB() {
		ArrayList<String> array_list = new ArrayList<String>();
		array_list = mydb.getAllQuote();
		
		Log.d("number of quotes: ", Integer.toString(mydb.getNumberQuotes()));
		
		for(int i = 0; i < array_list.size(); i++) {
			Log.d("Quote " + i + ":", array_list.get(i));
		}
		return;
	}
	
	public void submit(View view) {
		String storeFromMom, storeToMom;
		Map<String, String> map = new HashMap<String, String>();
		
		storeFromMom = fromMomET.getText().toString();
		storeToMom = toMomET.getText().toString();
		
		map.put("name", quoteAbout);
		map.put("fromMom", storeFromMom);
		map.put("toMom", storeToMom);
		map.put("source", currentUser);
		mydb.insertQuote(map);
		
		toMomET.setVisibility(View.INVISIBLE);
		fromMomET.setVisibility(View.INVISIBLE);
		submitBut.setVisibility(View.INVISIBLE);
		cancelBut.setVisibility(View.INVISIBLE);
		displayDB();
		
		addQuoteBut.setVisibility(View.VISIBLE);
		randQuoteBut.setVisibility(View.VISIBLE);
		spinner.setVisibility(View.VISIBLE);
	}
	
	public void cancelChoice(View view) {
		addQuoteBut.setVisibility(View.VISIBLE);
		randQuoteBut.setVisibility(View.VISIBLE);
		spinner.setVisibility(View.VISIBLE);
		
		toMomET.setText("");
		fromMomET.setText("");
		
		toMomET.setVisibility(View.INVISIBLE);
		fromMomET.setVisibility(View.INVISIBLE);
		submitBut.setVisibility(View.INVISIBLE);
		
		cancelBut.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	public void deleteDB(View view) {
		this.deleteDatabase(DBHelper.DATABASE_NAME);
		mydb = new DBHelper(this);
		return;
	}
}
