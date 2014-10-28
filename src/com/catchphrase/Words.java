package com.catchphrase;

public class Words {
	
	private static int index;
	private String[] words;
	
	// constructor
	public Words() {
		index = 0;
		words = new String[]{"Landon", "smells", "like", "dookie"};
	}
	
	// returns next word in the array, resets index once it reaches end of string array
	public String getNextWord() {
		String next = words[index];
		index = ++index % words.length;
		return next;
	}
}
