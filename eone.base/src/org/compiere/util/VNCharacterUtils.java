package org.compiere.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class VNCharacterUtils {
	
    
    public static String removeAccents(String s) {
    	  
    	String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
    	Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    	return pattern.matcher(temp).replaceAll("");
	}
    
    public static void main (String[] args)
	{
    	String s = removeAccents("Cộng hòa xã hội chủ nghĩa Việt Nam");
    	System.out.println(s);
	}
}
