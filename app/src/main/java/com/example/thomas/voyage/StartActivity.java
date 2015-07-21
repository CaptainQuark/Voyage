package com.example.thomas.voyage;

import android.app.Activity;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class StartActivity extends Activity {

    DBheroesAdapter heroesHelper;
        //hier noch vorhanden -> später in passende Datei verschieben

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        View viewLandscape = findViewById(R.id.activity_start);
        viewLandscape.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            // zuerst der Activity eine id geben -> danach direkt ansprechbar
            // in Manifest zur activity ".StartActivity" "android:screenOrientation="landscape"" einfügen -> immer Landschaftsmodus

        heroesHelper = new DBheroesAdapter(this);

        long id = heroesHelper.insertData("Thomas", "100", "eins", "zwei");

        if(id < 0) Message.message(this, "error@insert");
        else Message.message(this, "success@insert");

        String data = heroesHelper.getAllData();
        Message.message(this, data);
    }

}
