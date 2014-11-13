package com.catchphrase;

import java.io.IOException;

import android.content.Context;
import android.database.SQLException;

public class Words {
	
	private DatabaseHandler db;
	
	// constructor
	public Words(Context context) {
		// set up database handler
		db = new DatabaseHandler(context);
		try {
			db.createDB();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		
		try {
			db.openDB();
		} catch (SQLException sqle) {
			throw new Error("Unable to open database");
		}
	}

	// returns random word in the database, if no words were returned then reset the database
	public String getNextWord() {
		String word = db.getWord();
		
		// if no words were returned reset the 'read' variable in each word
		if (word == null) {
			// TODO: add resetDB method
			// db.resetDB();
		}
		
		return word;
	}
}
