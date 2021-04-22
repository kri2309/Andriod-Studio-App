package com.example.covid_kristina_borg_olivier;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.provider.Settings.System.getString;


public class GetCountry extends AsyncTask<String, Void, String[]> {


        @Override
        protected String[] doInBackground(String... name) {

            HttpClient client = new HttpClient();

            String[] NameCountry = new String[249];//the api returned 249 countries

            String readNames = client.GetData("https://api.covid19api.com/countries" ); // actually getting the names here from api
            JSONObject tempCountry = null;


            try {
                //retrieving country names here into array
                JSONArray tempArr = new JSONArray(readNames);
                for( int i = 1; i <= tempArr.length(); i++ ){
                    tempCountry = tempArr.getJSONObject(i-1);
                    NameCountry[i] = tempCountry.getString("Country");


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            NameCountry[0] = "Choose Country :)"; // this is so the first option doesnt bug out on the spinner

            return NameCountry;
        }
    }

