package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.util.Log;
import android.view.View;

import java.util.Calendar;


public class StartActivity extends Activity {

    DBheroesAdapter heroesHelper;
        //hier noch vorhanden -> später in passende Datei verschieben

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        hideSystemUI();

        /*
        heroesHelper = new DBheroesAdapter(this);

        long id = heroesHelper.insertData("Thomas", 100, "eins", "zwei");

        if (id < 0) Message.message(this, "error@insert");
        else Message.message(this, "success@insert");

        String data = heroesHelper.getAllData();
        Message.message(this, data);
        */

    }

    public void clickToHeroMerchant(View view) {
        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
        startActivity(i);
    }



































    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}



/* CODE SNIPPETS 2 LEARN

        //View viewLandscape = findViewById(R.id.activity_start);
        //viewLandscape.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // zuerst der Activity eine id geben -> danach direkt ansprechbar
        // in Manifest zur activity ".StartActivity" "android:screenOrientation="landscape"" einfügen -> immer Landschaftsmodus


        heroesHelper = new DBheroesAdapter(this);

        long id = heroesHelper.insertData("Thomas", 100, "eins", "zwei");

        if (id < 0) Message.message(this, "error@insert");
        else Message.message(this, "success@insert");

        String data = heroesHelper.getAllData();
        Message.message(this, data);
        */



