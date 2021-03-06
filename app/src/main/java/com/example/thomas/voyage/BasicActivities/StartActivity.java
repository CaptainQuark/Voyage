package com.example.thomas.voyage.BasicActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.voyage.CombatActivities.PrepareCombatActivity;
import com.example.thomas.voyage.CombatActivities.QuickCombatActivity;
import com.example.thomas.voyage.CombatActivities.WorldMapQuickCombatActivity;
import com.example.thomas.voyage.ContainerClasses.HelperSharedPrefs;
import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Item;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantHeroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantItemsAdapter;
import com.example.thomas.voyage.Databases.DBplayerItemsAdapter;
import com.example.thomas.voyage.Databases.DBscorefieldAndMultiAmountAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class StartActivity extends Activity {

    private DBheroesAdapter h;
    private DBmerchantHeroesAdapter m;
    private DBplayerItemsAdapter itemPlayerHelper;
    private DBmerchantItemsAdapter itemMerchantHelper;
    private TextView textViewSlaveMarket, textViewHeroesParty, textViewItemMarket, textViewHospital;
    private ConstRes c;
    private HelperSharedPrefs prefs = new HelperSharedPrefs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        hideSystemUI();
        iniValues();
        iniViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        setSlaveMarketWindow();
        setHeroCampWindow();
        setItemMarketWindow();
        setHospitalWindows();
        hideSystemUI();
    }



    /*

    onClick-Methoden

     */



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

    public void clickToStats(View view) {
        //Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
        //startActivity(i);
    }

    public void clickToMerchantInventory(View view){
        Intent i = new Intent(getApplicationContext(), MerchantInventoryActivity.class);
        startActivity(i);
    }

    public void clickToCamp(View view){
        Intent i = new Intent(getApplicationContext(), HeroCampActivity.class);
        i.putExtra(c.ORIGIN, "StartActivity");
        startActivity(i);
    }

    public void onClickHospital(View view){
        Intent i = new Intent(getApplicationContext(), HospitalActivity.class);
        i.putExtra(c.ORIGIN, "StartActivity");
        startActivity(i);
    }

    public void clickToHeroMerchant(View view) {
        Intent i = new Intent(getApplicationContext(), MerchantSlaveActivity.class);
        startActivity(i);
    }



    /*

    Funktionen

     */



    public void isAppFirstStarted() {
        // vor Datenbank-Upgrade durchgeführt -> zuerst letztes 'false' durch 'true' ersetzen
        //  -> App starten -> 'true' wieder auf 'false' & Versionsnummer erhöhen -> starten

        //SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        //Boolean isFirstRun = prefs.getBoolean(c.IS_FIRST_RUN, true);
        Boolean isFirstRun = prefs.getIsFirstStarted(this, new ConstRes());

        if (isFirstRun) {
            Msg.msg(this, "IS_FIRST_RUN: " + isFirstRun);

            prefs.addToCurrentMoneyAndGetNewVal(5000, this, new ConstRes());

            long validation = prepareHeroesDatabaseForGame(c.TOTAL_HEROES_PLAYER);
            if (validation < 0) {
                Msg.msg(this, "ERROR @ insertToHeroesDatabase");
            }

            // Datenbankplätze für HeroMerchant reservieren
            validation = insertToMerchantDatabase(c.TOTAL_HEROES_MERCHANT);
            if (validation < 0) {
                Msg.msg(this, "ERROR @ insertToMerchantHeroesDatabase");
            }

            preparePlayersItemDatabase(c.TOTAL_ITEMS_PLAYER_LV1);
            insertToItemMerchantDatabase(c.TOTAL_ITEMS_MERCHANT_LV1);

            //SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            //editor.putBoolean(c.IS_FIRST_RUN, false);
            //editor.apply();
            HelperSharedPrefs.setIsFirstStarted(false, this, new ConstRes());
        }
    }

    private void isQuickCombatfirstStarted(){
        c = new ConstRes();
        //SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        if(prefs.getQuickCombatFirstStarted(this, new ConstRes())){
            Msg.msg(this, "SCOREFIELDS AND MULTIS DB CREATED");
            DBscorefieldAndMultiAmountAdapter scoreHelper = new DBscorefieldAndMultiAmountAdapter(this);

            long validation = 0;
            for(int i = 0; i <= 20; i++){
                validation = scoreHelper.insertData(i, 0, 0, 0);
            }

            scoreHelper.insertData(25, 0, 0, 0);
            scoreHelper.insertData(50, 0, 0, 0);

            if(validation < 0) Msg.msg(this, "ERROR @ insert for 20 values in scoreDatabase");

            //SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            //editor.putBoolean(c.QUICK_COMBAT_FIRST_STARTED, false);
            //editor.apply();
            HelperSharedPrefs.setQuickCombatFirstStarted(false, this, new ConstRes());
        }
    }

    private void setSlaveMarketWindow(){
        int countNewHeroes = 0;

        // Überprüfe, ob neuer Händler
        // -> wenn ja, aktualisiere Datenbank
        long timeToShow = getTimeToShow();

        for(int i = 1; i <= m.getTaskCount(); i++){
            if( !(m.getHeroName(i)
                    .equals(getResources().getString(R.string.indicator_unused_row))) ){

                countNewHeroes++;
            }
        }

        Random rand = new Random();
        if(rand.nextInt(2) == 0){
            if(timeToShow < 60) textViewSlaveMarket.setText("Händler geht in weniger als einer Stunde...");
            else if(timeToShow < 20) textViewSlaveMarket.setText("Händler geht in wenigen Minuten...");
            else if(timeToShow < 5) textViewSlaveMarket.setText("Schnell! Er verlässt bereits den Markt!");
            else textViewSlaveMarket.setText("Händler geht in ca. " + timeToShow/60 + " Stunden");

        }else{
            if(countNewHeroes == 1) textViewSlaveMarket.setText("1 neuer Held");
            else if(countNewHeroes == 0) textViewSlaveMarket.setText("Nichts anzubieten...");
            else textViewSlaveMarket.setText(countNewHeroes + " neue Helden");
        }
    }

    private void setHeroCampWindow(){
        long size = h.getTaskCount();
        long count = 0;

        for(int i = 1; i <= size; i++){
            if(!h.getHeroName(i).equals(c.NOT_USED)){
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

    private void setHospitalWindows(){
        Random r = new Random();
        int sum = checkForHeroConditionsAndReturnNumOfHeroesInMed();

        if(sum == 0) textViewHospital.setText("Niemand in Behandlung...");
        else if(sum == 1 && r.nextInt(2) == 1){
            for(int i = 1; i <= h.getTaskCount(); i++){
                if(h.getMedSlotIndex(i) != -1){
                    textViewHospital.setText(h.getHeroName(i) + " schreit entsetzlich...");
                    i = (int) h.getTaskCount();
                }
            }
        }
        else if(sum == 1) textViewHospital.setText("Ein Held in Behandlung");
        else textViewHospital.setText(sum + " Helden in Behandlung");
    }

    private long prepareHeroesDatabaseForGame(int rows) {

        long validation = 0;

        for (int i = rows; i > 0; i--) {
            validation = h.insertData(c.NOT_USED, 0, "", "", 0, "");

            if(validation < 0){
                Toast.makeText(this, "ERROR @ prepareHeroesDatabaseForGame with index " + i, Toast.LENGTH_SHORT).show();
            }

            if (i == 1 && validation > 0)
                Msg.msg(this, "9 blank rows in heroes database inserted, 10th under way");
        }

        return validation;
    }

    private void preparePlayersItemDatabase(int rows){

        for (int i = rows; i > 0; i--) {
            long validation = itemPlayerHelper.insertData(c.NOT_USED, 0, "", "", 0, 0, "");

            if(validation < 0){
                Toast.makeText(this, "ERROR @ preparePlayersItemDatabaseForGame with index " + i, Toast.LENGTH_SHORT).show();
            }else if (i == 1 && validation > 0)
                Msg.msg(this, "PlayersItemDatabase prepared for game!");
        }
    }

    private void insertToItemMerchantDatabase(int rows){
        Msg.msg(this, "'insertToItemMerchant' called");

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

            if(id < 0) Msg.msg(this, "ERROR@insertToItemMerchantDatabase");
        }
    }

    private long insertToMerchantDatabase(int numberOfInserts) {
        Msg.msg(this, "'insertToMerchantDatabase'");
        List<Hero> heroList = new ArrayList<>();
        long id = 0;

        for (int i = 0; i < numberOfInserts; i++) {
            heroList.add(new Hero(this));
            heroList.get(i).Initialize("Everywhere");

            id = m.insertData(
                    heroList.get(i).getHeroName(),
                    heroList.get(i).getHp(),
                    heroList.get(i).getClassPrimary(),
                    heroList.get(i).getClassSecondary(),
                    heroList.get(i).getCosts(),
                    heroList.get(i).getImageResource(),
                    heroList.get(i).getEvasion(),
                    heroList.get(i).getHpTotal(),
                    heroList.get(i).getBonusNumber());

            if (id < 0) Msg.msg(this, "ERROR @ insert of hero " + i + 1);

        }

        return id;
    }

    private void refillMerchSlaveDatabase(){
        Log.v("UNEW MERCHANT", "updateMerchantDatabase, inserts: " + c.TOTAL_HEROES_MERCHANT);
        List<Hero> heroList = new ArrayList<>();

        for (int i = 0, id; i < c.TOTAL_HEROES_MERCHANT; i++) {
            heroList.add(new Hero(this));
            heroList.get(i).Initialize("Everywhere");

            id = m.updateRowComplete(
                    i + 1,
                    heroList.get(i).getHeroName(),
                    heroList.get(i).getHp(),
                    heroList.get(i).getClassPrimary(),
                    heroList.get(i).getClassSecondary(),
                    heroList.get(i).getCosts(),
                    heroList.get(i).getImageResource(),
                    heroList.get(i).getEvasion(),
                    heroList.get(i).getHpTotal(),
                    heroList.get(i).getBonusNumber());

            if (id < 0) Msg.msg(this, "error@insert of hero " + i + 1);
        }
    }

    private void setNewMerchant(){
        Log.v("NEW MERCHANT", "setNewMerchant");
        prefs.incrementMerchantSlaveId(this, new ConstRes());

        refillMerchSlaveDatabase();
    }

    private long getNowInSeconds(){
        Log.v("Merch Time", "getNowInSeconds");
        return (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) * 60 *60
                + Calendar.getInstance().get(Calendar.MINUTE) * 60
                + Calendar.getInstance().get(Calendar.SECOND);
    }

    private long getNewMerchLeaveDaytime(){
        Log.v("Merch Time", "getNewMerchLeaveDaytime");

        // Wenn jetzt nach Mittag, dann Mitternacht neuer Merchant, sonst zu Mittag
        return ((Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) < 12) ? 12*60*60 : 24*60*60;
    }

    private long getNewMerchChangeDate(){
        Log.v("Merch Time", "getNewMerchChangeDate");
        long newFinishDate = getNewMerchLeaveDaytime();

        long todayInSeconds = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) * 60 *60
                + Calendar.getInstance().get(Calendar.MINUTE) * 60
                + Calendar.getInstance().get(Calendar.SECOND);

        // neuer Abreise-Zeitpunkt des Händlers
        return System.currentTimeMillis() + (newFinishDate - todayInSeconds)*1000;
    }

    private long getTimeToShow() {
        long timeToShow, merchToLeaveDaytime = prefs.getMerchSlaveDaytime(this, new ConstRes()),
                merchChangeDate = prefs.getMerchSlaveChangeDate(this, new ConstRes());

        Log.v("Merch Time" , "merchToLeaveDaytime : " + merchToLeaveDaytime);
        Log.v("Merch Time" , "merchChangeDate : " + merchChangeDate);
        Log.v("Merch Time" , ".currentTimeMillis : " + System.currentTimeMillis());


        if(merchToLeaveDaytime == -1) merchToLeaveDaytime = getNewMerchLeaveDaytime();
        //if(merchChangeDate == -1) merchChangeDate = getNewMerchChangeDate();


        // Wenn der gegenwärtige Zeitpunkt aus mehr Millisekunden
        // besteht als das Ablaufdatum des Händlers, dann wechsle
        if(System.currentTimeMillis() >= merchChangeDate){
            Log.v("Merch Time", "new merchant will is arriving");
            timeToShow = (getNewMerchLeaveDaytime() - getNowInSeconds()) * 1000;
            //prefs.edit().putLong("merchChangeDate", getNewMerchChangeDate()).apply();
            //prefs.edit().putLong("merchToLeaveDaytime", getNewMerchLeaveDaytime()).apply();

            prefs.setNewMerchSlaveDaytime(getNewMerchLeaveDaytime(), this, new ConstRes());
            prefs.setNewMerchChangeDate(getNewMerchChangeDate(), this, new ConstRes());

            setNewMerchant();

            return timeToShow/1000/60;

        }else{
            timeToShow = (merchToLeaveDaytime - getNowInSeconds()) * 1000;
            Log.v("Merch Time", "timeToShow didn't change: " + timeToShow);
            return timeToShow/1000/60;
        }
    }

    private int checkForHeroConditionsAndReturnNumOfHeroesInMed(){
        int sum = 0;

        for(int i = 1; i <= h.getTaskCount(); i++){
            if(h.getMedSlotIndex(i) != -1){
                if(System.currentTimeMillis() >= h.getTimeToLeave(i)){
                    h.updateHeroHitpoints(i, h.getHeroHitpointsTotal(i));
                    h.updateMedSlotIndex(i, -1);
                    h.updateTimeToLeave(i, 0);

                }else{
                    sum++;
                }
            }
        }

        return sum;
    }



    /*

    Funktionen zur Auslagerung von Initialisierungen

     */



    private void iniValues(){

        // Datenbankobjekte initialisieren
        h = new DBheroesAdapter(this);
        m = new DBmerchantHeroesAdapter(this);
        itemPlayerHelper = new DBplayerItemsAdapter(this);
        itemMerchantHelper = new DBmerchantItemsAdapter(this);
        c = new ConstRes();

        isAppFirstStarted();
        isQuickCombatfirstStarted();
    }

    private void iniViews(){

        textViewSlaveMarket = (TextView) findViewById(R.id.start_textView_slave_market);
        textViewHeroesParty = (TextView) findViewById(R.id.start_textView_manage_heroes);
        textViewItemMarket = (TextView) findViewById(R.id.start_textView_inventory_merchant);
        textViewHospital = (TextView) findViewById(R.id.start_textView_hospital);

        setSlaveMarketWindow();
        setHeroCampWindow();
        setItemMarketWindow();
        setHospitalWindows();
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