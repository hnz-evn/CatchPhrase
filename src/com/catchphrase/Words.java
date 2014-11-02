package com.catchphrase;

import java.util.List;

import android.content.Context;

public class Words {
	
	private static int index;
	private DatabaseHandler db;
	
	// constructor
	public Words(Context context) {
		index = 1;
		db = new DatabaseHandler(context);
	}

	// returns next word in the array, resets index once it reaches end of string array
	public String getNextWord() {
		String word = db.getWord(index);
		
		// if no words were returned, reached end of word list (might change this, looks janky)
		if (word == null) {
			index = 1;
			word = getNextWord();
		}
		else {
			index++;
		}
		
		return word;
	}
}
