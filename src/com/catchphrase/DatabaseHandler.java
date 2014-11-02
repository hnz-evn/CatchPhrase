package com.catchphrase;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "catchphrase";
	private static final String TABLE_WORDS = "Words";
	private static final String KEY_ID = "_id";
	private static final String KEY_WORD = "word";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// create table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createWordsTable = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT)", TABLE_WORDS, KEY_ID, KEY_WORD);
		db.execSQL(createWordsTable);
		
		// on database creation, add words to table
		if (db != null) {
			insertMultiple(db, WordList.list);
		}
	}
	
	// upgrade table
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop old table if already exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
		
		// create table
		onCreate(db);
	}
	
	public String getWord(int id) {
		String word = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_WORDS, new String[] {KEY_ID,  KEY_WORD}, KEY_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null);
		
		// get first (and only) entry
		if (cursor != null) {
			// ensure that cursor fetched something
			if (cursor.moveToFirst()) {
				// ID is stored at (0), word is stored at (1)
				word = cursor.getString(1);
			}
		}
		
		// close cursor
		cursor.close();
		
		return word;
	}
	
	public void insertMultiple(SQLiteDatabase db, List<String> words) {
		ContentValues value = new ContentValues();
		
		try {
			if (db != null) {
				// start transaction to do all or nothing
				db.beginTransaction();
				
				// go through list to add all words into Words table
				for (String word : words) {
					value.put(KEY_WORD, word);
					db.insertOrThrow(TABLE_WORDS, null, value);
					value.clear();
				}
				
				// if whole list was processed without throwing exception, all is well
				db.setTransactionSuccessful();
			}
		}
		finally {
			if (db != null && db.inTransaction()) {
				// complete transaction
				db.endTransaction();
			}
		}
	}
}
