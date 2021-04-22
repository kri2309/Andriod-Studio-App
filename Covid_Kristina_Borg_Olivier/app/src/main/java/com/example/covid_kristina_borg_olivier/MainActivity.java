package com.example.covid_kristina_borg_olivier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter = null;

    Spinner countrySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCountry(); //without this nothing will run
    }

    public void getCountry() {
        //Getiing countries from other class
        countrySpinner = (Spinner) findViewById(R.id.spnCountries);
        GetCountry gc = new GetCountry();
        String[] names = null;
        try {
            names = gc.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //filling in the spinner with country names
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, names);
        countrySpinner.setAdapter(adapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //if position is not 0
                    if (position > 0) {

                        String name = parent.getItemAtPosition((int) id).toString();// name of the country that i am getting :) V I P
                        String saveStat = parent.getItemAtPosition((int) id).toString(); //saving for save prefs
                        name = ToSlug(name); //getting slug so that spaces and certian letters also work
                        Log.i("! ! ! ! ", name); // debug, if doesn't show up, no country name is being transferred
                        GetStats gs = new GetStats(); // getting the data that corrisponds with the country
                        JSONObject[] countryStats = gs.execute(name).get();

                        //shared prefs saving of country
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Country", countryStats.toString());
                    editor.commit();


                        Log.i("uwu", countryStats.toString());//debug checking for stats

                        Intent i = new Intent(getApplicationContext(), CountryDetails.class);// being trnasferred to other activity with all data


                        //These two lines of code are for the new cases, so that we minus them in the stats
                        i.putExtra("statsCurry", countryStats[0].toString());
                        i.putExtra("statsBefore", countryStats[1].toString());
                        //starting Country Details
                        startActivity(i);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //This is code for slug mentioned before; found code on stack overflow
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    //Without this code a lot of countries with spaces in their names would not give stats
    public static String ToSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        Log.i("SLUGGED VERSION", slug.toLowerCase(Locale.ENGLISH));
        return slug.toLowerCase(Locale.ENGLISH);
    }

     }



