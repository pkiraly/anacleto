package com.anacleto.util;

public class LinkedFloatList {

	private final static int initialCapacity = 1000;
	
	private final static int increment       = 1000;
	
	private float list[];
	private int length = 0;
	
	public LinkedFloatList(){
		list = new float[initialCapacity];
	}

	public int length() {
		return length;
	}

	public void add(float i){
		//check list size, if no place left, allocate
		if (length == list.length){
			float newList[] = new float[list.length + increment];
			System.arraycopy(list, 0, newList, 0, length);
			list = newList;
		}
		list[length] = i;
		length++;
	}
	
	public float get(int n){
		return list[n];
	}
}
