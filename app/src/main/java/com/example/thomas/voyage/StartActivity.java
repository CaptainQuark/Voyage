package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class StartActivity extends Activity {

    private final String TIME_PREF_FILE = "timefile";
    private DBheroesAdapter heroesHelper;
    private DBmerchantHeroesAdapter merchantHelper;
    private int seconds, minutes, hours, day, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        hideSystemUI();

        heroesHelper = new DBheroesAdapter(this);
        merchantHelper = new DBmerchantHeroesAdapter(this);

    }

    public void clickToHeroMerchant(View view) {
        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
        startActivity(i);
    }

    public void toScreenSlideActivity(View view) {
        Intent i = new Intent(getApplicationContext(), BeginJourney.class);
        startActivity(i);
    }

    public void calcTimeDiff(View view) {
        Intent i = new Intent(getApplicationContext(), HeroesPartyActivity.class);
        startActivity(i);
        /*
        Calendar calendar = Calendar.getInstance();

        int Nseconds = calendar.get(Calendar.SECOND);
        int Nminutes = calendar.get(Calendar.MINUTE);
        int Nhours = calendar.get(Calendar.HOUR_OF_DAY);
        int Nday = calendar.get(Calendar.DAY_OF_YEAR);
        int Nyear = calendar.get(Calendar.YEAR);

        Nseconds -= seconds;
        Nhours -= hours;
        Nminutes -= minutes;
        Nday -= day;
        Nyear -= year;

            // statt TIME_PREF_FILE -> getString(R.id.'...')
        SharedPreferences sharedPreferences = this.getSharedPreferences(TIME_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("SECONDS", seconds);
        editor.putInt("MINUTES", seconds);
        editor.putInt("HOURS", seconds);
        editor.putInt("DAYS", seconds);
        editor.putInt("YEARS", seconds);
        editor.apply();

        int loadedSecond = sharedPreferences.getInt("SECONDS", 0);
        Message.message(this, "loadedSecond: " + loadedSecond);
        */
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



