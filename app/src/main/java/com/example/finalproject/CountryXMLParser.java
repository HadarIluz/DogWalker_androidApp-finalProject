package com.example.finalproject;


import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CountryXMLParser {
	final static String KEY_COUNTRY="country";
	final static String KEY_NAME="name";
	final static String KEY_FLAG="flag";
	final static String KEY_ANTHEM="anthem";
	final static String KEY_SHORT="short";
	final static String KEY_DETAILS="details";


	public static ArrayList<com.example.finalproject.Country> parseCountries(Context context){
		ArrayList<com.example.finalproject.Country> data = null;
		InputStream in = openCountriesFile(context);
		XmlPullParserFactory xmlFactoryObject;
		try {
				xmlFactoryObject = XmlPullParserFactory.newInstance();
				XmlPullParser parser = xmlFactoryObject.newPullParser();
		
				parser.setInput(in, null);
		        int eventType = parser.getEventType();
		        com.example.finalproject.Country currentCountry = null;
		        String inTag = "";
		        String strTagText = null;
		
		        while (eventType != XmlPullParser.END_DOCUMENT){
		        	inTag = parser.getName();
		            switch (eventType){
		                case XmlPullParser.START_DOCUMENT:
		                	data = new ArrayList<com.example.finalproject.Country>();
		                    break;
		                case XmlPullParser.START_TAG:
		                	if (inTag.equalsIgnoreCase(KEY_COUNTRY))
		                    	currentCountry = new com.example.finalproject.Country();
		                    break;
		                case XmlPullParser.TEXT:
		                	strTagText = parser.getText();
		                	break;
		                case XmlPullParser.END_TAG:
		                	if (inTag.equalsIgnoreCase(KEY_COUNTRY))
		                    	data.add(currentCountry);
		                	else if (inTag.equalsIgnoreCase(KEY_NAME))
		                    	currentCountry.name = strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_SHORT))
		                    	currentCountry.shorty =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_FLAG))
		                    	currentCountry.flag =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_ANTHEM))
		                    	currentCountry.anthem =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_DETAILS))
		                    	currentCountry.setDetails(strTagText);
		            		inTag ="";
		                	break;	                    
		            }//switch
		            eventType = parser.next();
		        }//while
			} catch (Exception e) {e.printStackTrace();}
		return data;
	}

	private static InputStream openCountriesFile(Context context){
		AssetManager assetManager = context.getAssets();
		InputStream in =null;
		try {
			in = assetManager.open("countries.xml");
		} catch (IOException e) {e.printStackTrace();}
		return in;
	}
}
