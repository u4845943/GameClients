package com.golddigger.util;

import java.util.Arrays;

public class ArrayTools {

	public static <T> T[] copyOf(T[] original, int newLength, int startAtIndex){
		T[] newArray = Arrays.copyOf(original, newLength);
		if (startAtIndex > 0) {
			for (int i = 0; i < newArray.length; i++){
				if (i < startAtIndex) newArray[i] = null;
				else newArray[i] = original[i-startAtIndex];
			}
		}
		return newArray;
	}

	public static <T> String toString(T[] array){
		if (array.length == 0) return "[]";
		else {
			String str = "[";
			for (T element : array)
				if (element == null) str += "null,";
				else str += element.toString()+",";
			return str.substring(0,str.length()-1)+"]";
		}
	}
	
	public static <T> String toString(T[][] array){
		if (array.length == 0) return "[]";
		else {
			String str = "[";
			for (T[] element : array)
				if (element == null) str += "null,";
				else str += toString(element)+",";
			return str.substring(0,str.length()-1)+"]";
		}
	}
}
