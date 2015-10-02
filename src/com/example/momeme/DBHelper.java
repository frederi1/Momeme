package com.example.momeme;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "MOM_QUOTES" ;
	public static final String DATABASE_TABLE = "userInfo";
	public static final String DATABASE_MINITABLE = "minigame";
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_TO_MOM = "to_mom";
	public static final String KEY_FROM_MOM = "from_mom";
	public static final String KEY_SOURCE = "source";
	
	public static final String KEY_CLICKS = "clicks";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME , null, 1);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"create table userInfo " +
				"(_id integer primary key, name text not null, to_mom text, from_mom text, source text not null);"
				);
		db.execSQL("create table minigame (_id integer primary key, name text not null, clicks integer");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP table if exists userInfo");
		db.execSQL("Drop table if exists minigame");
		onCreate(db);
	}
	
	public boolean insertQuote (Map<String, String> map) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		cv.put("name", map.get("name"));
		cv.put("to_mom", map.get("toMom"));
		cv.put("from_mom", map.get("fromMom"));
		cv.put("source", map.get("source"));
		
		
		db.insert("userInfo", null, cv);
		return true;
	}
	
	public Cursor getData(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from userInfo where name='" + name + "'" , null);
		return res;
	}
	
	public int getNumberQuotes() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numQuotes = (int) DatabaseUtils.queryNumEntries(db, DATABASE_TABLE);
		return numQuotes;
	}
	
	public ArrayList<String> getAllQuote(){
		ArrayList<String> alQuote = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res =  db.rawQuery( "select * from userInfo", null );
		res.moveToFirst();
	    while(res.isAfterLast() == false){
	      alQuote.add(res.getString(res.getColumnIndex(KEY_FROM_MOM)));
	      res.moveToNext();
	    }
	    
		return alQuote;
	}

}
