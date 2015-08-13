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
    private DBheroesAdapter heroesHelper;
    private DBmerchantHeroesAdapter merchantHelper;
    private TextView textViewSlaveMarket, textViewHeroesParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        hideSystemUI();
        textViewSlaveMarket = (TextView) findViewById(R.id.start_textView_slave_market);
        textViewHeroesParty = (TextView) findViewById(R.id.start_textView_manage_heroes);

        heroesHelper = new DBheroesAdapter(this);

        isAppFirstStarted();

        setSlaveMarketWindow();
        setHeroesPartyWindow();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        setHeroesPartyWindow();
        setHeroesPartyWindow();
        hideSystemUI();
    }

    public void isAppFirstStarted() {
        // vor Datenbank-Upgrade durchgeführt -> zuerst letztes 'false' durch 'true' ersetzen
        //  -> App starten -> 'true' wieder auf 'false' & Versionsnummer erhöhen -> starten

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean isFirstRun = prefs.getBoolean(IS_FIRST_RUN, true);

        if (isFirstRun) {
            Message.message(this, "'isFirstRun' called");
            long validation = prepareHeroesDatabaseForGame(10);
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

    public void setSlaveMarketWindow(){
        merchantHelper = new DBmerchantHeroesAdapter(this);
        int countNewHeroes = 0;

        for(int i = 1; i <= merchantHelper.getTaskCount(); i++){
            if( !(merchantHelper.getHeroName(i)
                    .equals(getResources().getString(R.string.indicator_unused_row))) ){

                countNewHeroes++;
            }
        }

        if(countNewHeroes == 1) textViewSlaveMarket.setText("1 neuer Held");
        else if(countNewHeroes == 0) textViewSlaveMarket.setText("Nichts anzubieten...");
        else textViewSlaveMarket.setText(countNewHeroes + " neue Helden");
    }

    public void setHeroesPartyWindow(){
        long size = heroesHelper.getTaskCount();
        long count = 0;

        for(int i = 1; i <= size; i++){
            if(!heroesHelper.getHeroName(i).equals(getResources().getString(R.string.indicator_unused_row))){
                count++;
            }
        }

        if(count == size){
            textViewHeroesParty.setText( "Volles Haus!");
        }else{
            textViewHeroesParty.setText( count + " / " + size + " belegt");
        }

    }

    public long prepareHeroesDatabaseForGame(int rows) {

        long validation = 0;

        for (int i = rows; i > 0; i--) {
            validation = heroesHelper.insertData("NOT_USED", 0, "", "", 0, "");

            /*if(validation < 0){
                Toast.makeText(this, "ERROR @ prepareHeroesDatabaseForGame with index " + i, Toast.LENGTH_SHORT).show();
            }*/

            if (i == 1 && validation > 0)
                Message.message(this, "9 blank rows in heroes database inserted, 10th underway");
        }

        return validation;
    }

    public long insertToMerchantDatabase(int numberOfInserts) {
        DBmerchantHeroesAdapter merchantHelper = new DBmerchantHeroesAdapter(this);
        List<Hero> herosList = new ArrayList<>();
        long id = 0;

        for (int i = 0; i < numberOfInserts; i++) {
            herosList.add(new Hero(this));
            herosList.get(i).Initialize("Everywhere");

            // noch Vorgänger-unabhängig -> neue Zeilen werden einfach an Ende angehängt
            id = merchantHelper.insertData(
                    herosList.get(i).getStrings("heroName"),
                    herosList.get(i).getInts("hitpoints"),
                    herosList.get(i).getStrings("classPrimary"),
                    herosList.get(i).getStrings("classSecondary"),
                    herosList.get(i).getInts("costs"),
                    herosList.get(i).getStrings("imageResource"));

            if (id < 0) Message.message(this, "ERROR @ insert of hero " + i + 1);
        }

        return id;
    }

    public void clickToHeroMerchant(View view) {
        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
        startActivity(i);
    }

    public void clickToPrepareCombat(View view) {
        Intent i = new Intent(getApplicationContext(), PrepareCombatActivity.class);
        startActivity(i);
    }

    public void toScreenSlideActivity(View view) {
        Intent i = new Intent(getApplicationContext(), WorldMapQuickCombatActivity.class);
        startActivity(i);
    }

    public void clickToHeroesPartyActivity(View view) {
        Intent i = new Intent(getApplicationContext(), HeroesPartyActivity.class);
        i.putExtra("ORIGIN", "StartActivity");
        startActivity(i);
    }

    public void clickToDirectCombat(View view){
        Intent i = new Intent(getApplicationContext(), CombatActivity.class);
        startActivity(i);
    }

    public boolean leftExpression(List<String> xList) {

        textViewSlaveMarket.setText(xList.get(3));

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



