package com.example.covid_kristina_borg_olivier;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetStats extends AsyncTask<String, Void, JSONObject[]> {
    // since the country is always changing we have to piece together the url
    private static String start_bit ="https://api.covid19api.com/live/country/";
    private static String end_bit =  "/status/confirmed";

    @Override
    protected JSONObject[] doInBackground(String... countryName) {
        String link = start_bit + countryName[0] + end_bit; // country is inserted here
        HttpClient client = new HttpClient();

        String readNames = client.GetData(link);

        JSONObject[] country = new JSONObject[2];//for getting the current day and day before


        try {
            JSONArray tempArr = new JSONArray(readNames);
            //getting last two days
            country[0] = tempArr.getJSONObject(tempArr.length()-1);
            country[1] = tempArr.getJSONObject(tempArr.length()-2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return country;
    }
}

