package com.example.flattomap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceJSONParser {
	 
    /** Receives a JSONObject and returns a list */
	//It Will Return a List of Places 
    public List<HashMap<String,String>> parse(JSONObject jObject){
 
        JSONArray jPlaces = null;
        try {
            // elements in the 'places' array retieve krenge */
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return placesName(jPlaces);
    }
    private List<HashMap<String, String>> placesName(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;
 
        /** Har jagah Lenge aur use Parse Krke dekhnge */
        for(int i=0; i<placesCount;i++){
            try {
               
                place = placeDesc((JSONObject)jPlaces.get(i));
                placesList.add(place);
 
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
 
        return placesList;
    }
   
 
    //Ye Seprate Karegi STring Ko
    private HashMap<String, String> placeDesc(JSONObject jPlace){
 
        HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "";
        String vicinity="";
        String latitude="";
        String longitude="";
 
        try {
            // Yha Place Name AAega Aur check krenge agar wo exist krta h toh
            if(!jPlace.isNull("name")){
                placeName = jPlace.getString("name");
            }
 
            // Extracting Place Vicinity, if available
            if(!jPlace.isNull("vicinity")){
                vicinity = jPlace.getString("vicinity");
            }
 
            latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
            if(placeName==""){
            	placeName="Not Available";
            }
            if(vicinity==""){
            	vicinity="Not Available";
            }
 
            place.put("place_name", placeName);
            place.put("vicinity", vicinity);
            place.put("lat", latitude);
            place.put("lng", longitude);
 
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
  
}
