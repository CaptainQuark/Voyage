package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends Activity {

    private final String IS_FIRST_RUN = "IS_FIRST_RUN";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        hideSystemUI();
        isAppFirstStarted();

        tv = (TextView) findViewById(R.id.start_textView_slave_market);

        List<String> xList = new ArrayList<>();
        xList.add("IF");
        xList.add("2.");
        xList.add("==");
        xList.add("10");
        xList.add("->");
        xList.add("2.");
        xList.add("*2");
    }

    public void isAppFirstStarted() {


        // vor Datenbank-Upgrade durchgeführt -> zuerst letztes 'false' durch 'true' ersetzen
        //  -> App starten -> 'true' wieder auf 'false' & Versionsnummer erhöhen -> starten

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean isFirstRun = prefs.getBoolean(IS_FIRST_RUN, true);

        if (isFirstRun) {
            Message.message(this, "'isFirstRun' called");
            long validation = freshInsertToHeroesDatabase(10);
            if (validation < 0) {
                Message.message(this, "ERROR @ insertToHeroesDatabase");
            }

            long id = insertToMerchantDatabase(3);
            if (id < 0) {
                Message.message(this, "ERROR @ insertToMerchantHeroesDatabase");
            }

            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putBoolean(IS_FIRST_RUN, false);
            editor.apply();
        }
    }

    public long freshInsertToHeroesDatabase(int rows) {
        DBheroesAdapter dBheroesAdapter = new DBheroesAdapter(this);

        long validation = 0;

        for (int i = rows; i > 0; i--) {
            validation = dBheroesAdapter.insertData(this.getString(R.string.indicator_unused_row), 0, "", "", 0);
            if (i == 1)
                Message.message(this, "9 blank rows in heroes database inserted, 10th underway");
        }

        return validation;
    }

    public long insertToMerchantDatabase(int numberOfInserts) {
        DBmerchantHeroesAdapter merchantHelper = new DBmerchantHeroesAdapter(this);
        List<Hero> herosList = new ArrayList<>();
        long id = 0;

        for (int i = 0; i < numberOfInserts; i++) {
            herosList.add(new Hero());
            herosList.get(i).Initialize("Everywhere");

            // noch Vorgänger-unabhängig -> neue Zeilen werden einfach an Ende angehängt
            id = merchantHelper.insertData(
                    herosList.get(i).getStrings("heroName"),
                    herosList.get(i).getInts("hitpoints"),
                    herosList.get(i).getStrings("classPrimary"),
                    herosList.get(i).getStrings("classSecondary"),
                    herosList.get(i).getInts("costs"));

            if (id < 0) Message.message(this, "ERROR @ insert of hero " + i + 1);
        }

        return id;
    }

    public void clickToHeroMerchant(View view) {
        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
        startActivity(i);
    }

    public void clickToCombat(View view) {
        Intent i = new Intent(getApplicationContext(), CombatActivity.class);
        startActivity(i);
    }

    public void toScreenSlideActivity(View view) {
        Intent i = new Intent(getApplicationContext(), WorldMapQuickCombatActivity.class);
        startActivity(i);
    }

    public void clickToHeroesPartyActivity(View view) {
        Intent i = new Intent(getApplicationContext(), HeroesPartyActivity.class);
        startActivity(i);
    }

    public boolean leftExpression(List<String> xList) {

        tv.setText(xList.get(3));

        return false;
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



