package com.catchphrase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final String DB_PATH = "/data/data/com.catchphrase/databases/";
	private static final String DB_NAME = "catchphrase";
	private static final int DB_VERSION = 1;
	private static final String TABLE_WORDS = "Words";
	private static final String KEY_ID = "_id";
	private static final String KEY_WORD = "word";
	private static final String KEY_READ = "read";
	private final Context context;
	private SQLiteDatabase database;
	
	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}
	
	public String getWord() {
		SQLiteDatabase db = this.getReadableDatabase();
		String word = null;
		String id = null;
		ContentValues args = new ContentValues();
		
		// fetch a random word from the Words table where the word has not been read yet by the user
		Cursor cursor = db.query(TABLE_WORDS, new String[] {KEY_ID,  KEY_WORD}, KEY_READ + " = 0", null, null, null, "RANDOM()", "1");
				
		// get first (and only) entry
		if (cursor != null) {
			// ensure that cursor fetched something
			if (cursor.moveToFirst()) {
				// set ID of word to read
				// ID is stored at (0), word is stored at (1)
				id = cursor.getString(0);
				args.put(KEY_READ, 1);
				db.update(TABLE_WORDS, args, KEY_ID + " = ?", new String[] { id });
				
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
	
	// create empty database and rewrite it with prepopulated database file in assets folder
	public void createDB() throws IOException {
		boolean dbExists = checkDB();
		
		if (!dbExists) {
			// use superclass method to create empty database
			this.getReadableDatabase();
			
			try {
				copyDB();
			} catch (IOException e) {
				throw e;
			}
		}
	}
	
	// checks to see if DB exists to avoid re-copying the file each time application is opened
	private boolean checkDB() {
		SQLiteDatabase checkDB = null;
		
		try {
			String path = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLException e) {
			// db doesn't exist yet
		}
		
		// close db if exists
		if (checkDB != null) {
			checkDB.close();
		}
		
		return checkDB != null ? true : false;
	}
	
	// copies database from local assets folder into newly created empty database
	// through a transferring bytestream
	private void copyDB() throws IOException {
		// open local db
		InputStream input = context.getAssets().open(DB_NAME);
		
		// path to the newly created db
		String outputFile = DB_PATH + DB_NAME;
		
		// open the empty db as output stream
		OutputStream output = new FileOutputStream(outputFile);
		
		// transfer bytes from input db to output db
		byte[] buffer = new byte[1024];
		int length;
		
		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		
		// close objects
		output.flush();
		output.close();
		input.close();		
	}
	
	// opens database
	public void openDB() throws SQLiteException {
		String path = DB_PATH + DB_NAME;
		database = SQLiteDatabase.openDatabase(path,  null, SQLiteDatabase.OPEN_READONLY);
	}
	
	// create table
	@Override
	public void onCreate(SQLiteDatabase db) {
//		String createWordsTable = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT)", TABLE_WORDS, KEY_ID, KEY_WORD);
//		db.execSQL(createWordsTable);
//		
//		// on database creation, add words to table
//		if (db != null) {
//			insertMultiple(db, WordList.list);
//		}
	}
	
	// upgrade table
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// drop old table if already exists
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
//		
//		// create table
//		onCreate(db);
	}
}
