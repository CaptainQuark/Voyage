package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends Activity {

    private final String IS_FIRST_RUN = "IS_FIRST_RUN";
    private final int TOTAL_ITEMS_PLAYER = 50, TOTAL_ITEMS_MERCHANT = 20, TOTAL_HEROES_MERCHANT = 3, TOTAL_HEROES_PLAYER = 10;
    private DBheroesAdapter heroesHelper;
    private DBmerchantHeroesAdapter merchantHelper;
    private DBplayerItemsAdapter itemPlayerHelper;
    private DBmerchantItemsAdapter itemMerchantHelper;
    private TextView textViewSlaveMarket, textViewHeroesParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        hideSystemUI();
        heroesHelper = new DBheroesAdapter(this);
        merchantHelper = new DBmerchantHeroesAdapter(this);
        itemPlayerHelper = new DBplayerItemsAdapter(this);
        itemMerchantHelper = new DBmerchantItemsAdapter(this);

        isAppFirstStarted();

        textViewSlaveMarket = (TextView) findViewById(R.id.start_textView_slave_market);
        textViewHeroesParty = (TextView) findViewById(R.id.start_textView_manage_heroes);

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
            Message.message(this, "IS_FIRST_RUN: " + isFirstRun);

            long validation = prepareHeroesDatabaseForGame(TOTAL_HEROES_PLAYER);
            if (validation < 0) {
                Message.message(this, "ERROR @ insertToHeroesDatabase");
            }

            validation = insertToMerchantDatabase(TOTAL_HEROES_MERCHANT);
            if (validation < 0) {
                Message.message(this, "ERROR @ insertToMerchantHeroesDatabase");
            }

            preparePlayersItemDatabase(TOTAL_ITEMS_PLAYER);
            insertToItemMerchantDatabase(TOTAL_ITEMS_MERCHANT);

            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putBoolean(IS_FIRST_RUN, false);
            editor.apply();
        }
    }

    public void setSlaveMarketWindow(){
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

            if(validation < 0){
                Toast.makeText(this, "ERROR @ prepareHeroesDatabaseForGame with index " + i, Toast.LENGTH_SHORT).show();
            }

            if (i == 1 && validation > 0)
                Message.message(this, "9 blank rows in heroes database inserted, 10th underway");
        }

        return validation;
    }

    private void preparePlayersItemDatabase(int rows){

        for (int i = rows; i > 0; i--) {
            long validation = itemPlayerHelper.insertData("NOT_USED", 0, "", "", 0, 0, "");

            if(validation < 0){
                Toast.makeText(this, "ERROR @ preparePlayersItemDatabaseForGame with index " + i, Toast.LENGTH_SHORT).show();
            }else if (i == 1 && validation > 0)
                Message.message(this, "PlayersItemDatabase prepared for game!");
        }
    }

    private void insertToItemMerchantDatabase(int rows){
        Message.message(this, "'insertToItemMerchant' called");

        for(int i = 1; i <= TOTAL_ITEMS_MERCHANT; i++){

            Item item = new Item(this);
            long id = itemMerchantHelper.insertData(
                    item.getStrings("ITEM_NAME"),
                    item.getInts("SKILL_ID"),
                    item.getStrings("DES_MAIN"),
                    item.getStrings("DES_ADD"),
                    item.getInts("BUY_COSTS"),
                    item.getInts("SPELL_COSTS"),
                    item.getStrings("RARITY")
            );

            if(id < 0) Message.message(this, "ERROR@insertToItemMerchantDatabase");
        }
    }

    public long insertToMerchantDatabase(int numberOfInserts) {
        Message.message(this, "'insertToMerchantDatabase'");
        List<Hero> herosList = new ArrayList<>();
        long id = 0;

        for (int i = 0; i < numberOfInserts; i++) {
            herosList.add(new Hero(this));
            herosList.get(i).Initialize("Everywhere");

            // noch Vorgänger-unabhängig -> neue Zeilen werden einfach an Ende angehängt
            id = merchantHelper.insertData(
                    herosList.get(i).getStrings("heroName"),
                    herosList.get(i).getInts("hp"),
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

    public void clickToMerchantInventory(View view){
        Intent i = new Intent(getApplicationContext(), MerchantInventoryActivity.class);
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



