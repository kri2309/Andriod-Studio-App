package com.example.covid_kristina_borg_olivier;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class CountryDetails extends AppCompatActivity {
    String savedCountry = null;
   // JSONObject[] savedStats = null;
    SharedPreferences pref;
    public static final String mypreference = "mSaveStats";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_details);

        //declaring my shared prefs
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        try {
            savedCountry = sharedPreferences.getString("Country", null);
        }catch(NullPointerException e){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        if(savedCountry !=null){


        }
        //filling in the data on the xml file
        try {
            //get stats back from other activity for last 2 days
           JSONObject country = new JSONObject(getIntent().getStringExtra("statsCurry"));
            JSONObject countryBefore = new JSONObject(getIntent().getStringExtra("statsBefore"));

            TextView txtCountry = findViewById(R.id.txtCountry);
            TextView txtCode = findViewById(R.id.txtCode);
            TextView txtConfirmed = findViewById(R.id.txtConfirmed);
            TextView txtDeaths = findViewById(R.id.txtDeaths);
            TextView txtRecovered = findViewById(R.id.txtRecovered);
            TextView txtActive = findViewById(R.id.txtActive);
            TextView txtNewCases = findViewById(R.id.txtNewCases);

            txtCountry.setText(country.getString("Country" ));
            txtCode.setText(":" + country.getString("CountryCode"));
            txtConfirmed.setText("Confirmed: " + country.getString("Confirmed"));
            txtDeaths.setText("Deaths: " + country.getString("Deaths"));
            txtRecovered.setText("Recovered: " + country.getString("Recovered"));
            txtActive.setText("Active: " + country.getString("Active"));

            // working out the new cases
            int Cases = 0;
            Cases = Integer.parseInt(country.getString("Confirmed")) - Integer.parseInt(countryBefore.getString("Confirmed"));

            txtNewCases.setText("NewCases: " + String.valueOf(Cases));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //here i am attempting to edit the xml according to the save prefs
    //this did not work but i left the code here just in case
     public void Implement(JSONObject t) {

         try {
             JSONObject coutnries = t;

             TextView txtCountry = findViewById(R.id.txtCountry);
             TextView txtCode = findViewById(R.id.txtCode);
             TextView txtConfirmed = findViewById(R.id.txtConfirmed);
             TextView txtDeaths = findViewById(R.id.txtDeaths);
             TextView txtRecovered = findViewById(R.id.txtRecovered);
             TextView txtActive = findViewById(R.id.txtActive);
             TextView txtNewCases = findViewById(R.id.txtNewCases);

             txtCountry.setText(coutnries.getString("Country"));
             txtCode.setText(":" + coutnries.getString("CountryCode"));
             txtConfirmed.setText("Confirmed: " + coutnries.getString("Confirmed"));
             txtDeaths.setText("Deaths: " + coutnries.getString("Deaths"));
             txtRecovered.setText("Recovered: " + coutnries.getString("Recovered"));
             txtActive.setText("Active: " + coutnries.getString("Active"));
         } catch (JSONException e) {
             e.printStackTrace();
         }


     }

     // trying to load the prefs here
    //sadly did not work
    //left it here just in case
    public void Saved(){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("Country", 0);
        SharedPreferences.Editor editor = pref.edit();


        String s = pref.getString("Country", null);

        if (pref.getBoolean("SaveDone", false) == false) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else {
            //JSONObject[] savedCountry = null;

            try {
                Implement(new JSONObject(s));

                //Log.i("owo", savedCountry.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject countrySave = null;
            try {
                countrySave = new JSONObject(pref.getString("Country", null));

                CountryDetails details = new CountryDetails();
            } catch (JSONException  e) {
                e.printStackTrace();
            }
            Implement(countrySave);
        }
    }
    //trying to get the stats to save here
    public void Resource(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Save", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
        editor.putString("Country", savedCountry);
        editor.putBoolean("SaveDone", true);
        editor.commit();

    }
//this doesn't rly effect the app
    @Override
    public void onResume(){
        super.onResume();
        Resource();

    }
    //here the saved needed to be commented out as it would crash
    @Override
    public void onStart(){
        super.onStart();
        //Saved();
           }

    //this doesn't rly effect the app
    @Override
    public void onStop() {
        super.onStop();
        Resource();

    }
//works
    protected void onPause() {
        super.onPause();

        SharedPreferences savedStats = getSharedPreferences(mypreference, 0);
        SharedPreferences.Editor editor = savedStats.edit();
        // Necessary to clear first if we save preferences
         editor.clear();
        editor.putString("Country", savedCountry);
        editor.commit();
    }
// adding the back button on the options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }


    }

}
