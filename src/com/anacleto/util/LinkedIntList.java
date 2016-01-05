package com.anacleto.util;

public class LinkedIntList {

	private final static int initialCapacity = 1000;
	
	private final static int increment       = 1000;
	
	private int list[];
	private int length = 0;
	
	public LinkedIntList(){
		list = new int[initialCapacity];
	}

	public int length() {
		return length;
	}

	public void add(int i){
		//check list size, if no place left, allocate
		if (length == list.length){
			int newList[] = new int[list.length + increment];
			System.arraycopy(list, 0, newList, 0, length);
			list = newList;
		}
		list[length] = i;
		length++;
	}
	
	public int get(int n){
		return list[n];
	}
}
