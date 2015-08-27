package com.example.thomas.voyage.BasicActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.voyage.CombatActivities.PrepareCombatActivity;
import com.example.thomas.voyage.CombatActivities.QuickCombatActivity;
import com.example.thomas.voyage.CombatActivities.QuickCombatClassicActivity;
import com.example.thomas.voyage.CombatActivities.WorldMapQuickCombatActivity;
import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Item;
import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantHeroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantItemsAdapter;
import com.example.thomas.voyage.Databases.DBplayerItemsAdapter;
import com.example.thomas.voyage.Databases.DBscorefieldAndMultiAmountAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends Activity {

    private DBheroesAdapter heroesHelper;
    private DBmerchantHeroesAdapter merchantHelper;
    private DBplayerItemsAdapter itemPlayerHelper;
    private DBmerchantItemsAdapter itemMerchantHelper;
    private TextView textViewSlaveMarket, textViewHeroesParty, textViewItemMarket;
    private ConstRes c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        hideSystemUI();

        heroesHelper = new DBheroesAdapter(this);
        merchantHelper = new DBmerchantHeroesAdapter(this);
        itemPlayerHelper = new DBplayerItemsAdapter(this);
        itemMerchantHelper = new DBmerchantItemsAdapter(this);
        c = new ConstRes();

        isAppFirstStarted();
        isQuickCombatfirstStarted();

        textViewSlaveMarket = (TextView) findViewById(R.id.start_textView_slave_market);
        textViewHeroesParty = (TextView) findViewById(R.id.start_textView_manage_heroes);
        textViewItemMarket = (TextView) findViewById(R.id.start_textView_inventory_merchant);

        setSlaveMarketWindow();
        setHeroesPartyWindow();
        setItemMarketWindow();
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
        Boolean isFirstRun = prefs.getBoolean(c.IS_FIRST_RUN, true);

        if (isFirstRun) {
            Message.message(this, "IS_FIRST_RUN: " + isFirstRun);

            long validation = prepareHeroesDatabaseForGame(c.TOTAL_HEROES_PLAYER);
            if (validation < 0) {
                Message.message(this, "ERROR @ insertToHeroesDatabase");
            }

            validation = insertToMerchantDatabase(c.TOTAL_HEROES_MERCHANT);
            if (validation < 0) {
                Message.message(this, "ERROR @ insertToMerchantHeroesDatabase");
            }

            preparePlayersItemDatabase(c.TOTAL_ITEMS_PLAYER_LV1);
            insertToItemMerchantDatabase(c.TOTAL_ITEMS_MERCHANT_LV1);

            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putBoolean(c.IS_FIRST_RUN, false);
            editor.apply();
        }
    }

    private void isQuickCombatfirstStarted(){
        c = new ConstRes();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        if(prefs.getBoolean(c.QUICK_COMBAT_FIRST_STARTED, true)){
            Message.message(this, "SCOREFIELDS AND MULTIS DB CREATED");
            DBscorefieldAndMultiAmountAdapter scoreHelper = new DBscorefieldAndMultiAmountAdapter(this);

            long validation = 0;
            for(int i = 0; i <= 20; i++){
                validation = scoreHelper.insertData(i, 0, 0, 0);
            }

            scoreHelper.insertData(25, 0, 0, 0);
            scoreHelper.insertData(50, 0, 0, 0);

            if(validation < 0) Message.message(this, "ERROR @ insert for 20 values in scoreDatabase");

            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putBoolean(c.QUICK_COMBAT_FIRST_STARTED, false);
            editor.apply();
        }
    }

    private void setSlaveMarketWindow(){
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

    private void setHeroesPartyWindow(){
        long size = heroesHelper.getTaskCount();
        long count = 0;

        for(int i = 1; i <= size; i++){
            if(!heroesHelper.getHeroName(i).equals(c.NOT_USED)){
                count++;
            }
        }

        if(count == size){
            textViewHeroesParty.setText( "Volles Haus!");
        }else{
            textViewHeroesParty.setText( count + " / " + size + " belegt");
        }

    }

    private void setItemMarketWindow(){
        int countUsed = 0;

        for(int i = 1; i <= itemMerchantHelper.getTaskCount(); i++){
            if(! itemMerchantHelper.getItemName(i).equals(c.NOT_USED) ){
                countUsed++;
            }
        }

        if(countUsed > 0) textViewItemMarket.setText(countUsed + " Waren zu kaufen");
        else if(countUsed == 0) textViewItemMarket.setText("Nichts mehr zu kaufen...");
    }

    private long prepareHeroesDatabaseForGame(int rows) {

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

        for(int i = 1; i <= rows; i++){

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

    private long insertToMerchantDatabase(int numberOfInserts) {
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

    public void startWearTest(View view){

    }

    public void clickToHeroMerchant(View view) {
        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
        startActivity(i);
    }

    public void clickToPrepareCombat(View view) {
        Intent i = new Intent(getApplicationContext(), PrepareCombatActivity.class);
        startActivity(i);
    }

    public void clickToQuickCombat(View view){
        Intent i = new Intent(getApplicationContext(), QuickCombatActivity.class);
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

    public void clickToClassic(View view){
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



