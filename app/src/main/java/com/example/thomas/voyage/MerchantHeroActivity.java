package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MerchantHeroActivity extends Activity {

    //private final String SHAREDPREF_INSERT = "INSERT";
    //private final String TIME_PREF_FILE = "timefile";
    DBmerchantHeroesAdapter dBmerchantHeroesAdapter;
    ImageView merchantProfile;
    private String MERCHANT_ID = "merchantId";
    private TextView debugView, buyHeroView, textViewHero_0, textViewHero_1, textViewHero_2;
    private int currentSelectedHeroId = 0, currentMoneyInPocket = 0, currentMerchantId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_hero);

        hideSystemUI();
        dBmerchantHeroesAdapter = new DBmerchantHeroesAdapter(this);

        debugView = (TextView) findViewById(R.id.debug_merchant_hero_textView);
        buyHeroView = (TextView) findViewById(R.id.merchant_hero_buy);
        textViewHero_0 = (TextView)findViewById(R.id.textView_merchant_hero_i0);
        textViewHero_1 = (TextView)findViewById(R.id.textView_merchant_hero_i1);
        textViewHero_2 = (TextView)findViewById(R.id.textView_merchant_hero_i2);
        merchantProfile = (ImageView) findViewById(R.id.imageView_merchant_profile);

        //isAppFirstStarted();
        fillTextViewHeros(3);
        calcTimeDiff();

        setDebugText();
    }

    private void calcTimeDiff() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        long finishDate = prefs.getLong("TIME_TO_LEAVE", setNewDate());

        currentMerchantId = prefs.getInt(MERCHANT_ID, 0);
        merchantProfile.setImageResource(getResources().getIdentifier("merchant_" + currentMerchantId, "mipmap", getPackageName()));

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putLong("TIME_TO_LEAVE", finishDate);
        editor.apply();

        Date presentDate = new Date();

        long dateDiff = finishDate - presentDate.getTime();
        long seconds = dateDiff / 1000;
        //long minutes = seconds / 60;

        final TextView merchantTime = (TextView) findViewById(R.id.activity_merchant_textView_time_to_next_merchant);

        new CountDownTimer(dateDiff, 1000 / 60) {

            public void onTick(long millisUntilFinished) {
                merchantTime.setText("" + millisUntilFinished / 1000 / 60);
            }

            public void onFinish() {
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putLong("TIME_TO_LEAVE", setNewDate());
                editor.apply();

                setNewMerchantProfile();

                long validation = updateMerchantsDatabase(3);
                if (validation < 0) {
                    Log.e("ERROR @ ", "updateMerchantsDatabase");
                }

                calcTimeDiff();
            }
        }.start();
    }

    private long setNewDate() {
        Date newExpirationDate = new Date();

        //60*60*1000 = 1 Stunde, *18 = 18 Stunden
        newExpirationDate.setTime(System.currentTimeMillis() + (60 * 60 * 1000 * 18));
        return newExpirationDate.getTime();
    }

    public void setNewMerchantProfile(){
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

        if(currentMerchantId >= 3){
            currentMerchantId = 0;
        } else currentMerchantId++;

        editor.putInt(MERCHANT_ID, currentMerchantId);
        editor.apply();

        merchantProfile.setImageResource(getResources().getIdentifier("merchant_" + currentMerchantId, "mipmap", getPackageName()));
    }

    public long updateMerchantsDatabase(int numberOfInserts) {
        List<Hero> herosList = new ArrayList<>();
        long id = 0;

        for (int i = 0; i < numberOfInserts; i++) {
            herosList.add(new Hero());
            herosList.get(i).Initialize("Everywhere");

            // noch Vorgänger-unabhängig -> neue Zeilen werden einfach an Ende angehängt
            id = dBmerchantHeroesAdapter.updateRowComplete(
                    i + 1,
                    herosList.get(i).getStrings("heroName"),
                    herosList.get(i).getInts("hitpoints"),
                    herosList.get(i).getStrings("classPrimary"),
                    herosList.get(i).getStrings("classSecondary"),
                    herosList.get(i).getInts("costs"));

            if (id < 0) Message.message(this, "error@insert of hero " + i + 1);
        }

        return id;
    }


    /*
    public void isAppFirstStarted() {
        // vor Datenbank-Upgrade durchgeführt -> zuerst letztes 'false' durch 'true' ersetzen
        //  -> App starten -> 'true' wieder auf 'false' & Versionsnummer erhöhen -> starten

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            insertIntoDatabase();
            Message.message(this, "insertIntoDatabase called @ function 'isFirstRun' in 'MerchantHeroActivity'");
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
    }


    public void insertIntoDatabase() {
        long id = insertToMerchantDatabase(3);
        if (id < 0) {
            Message.message(this, "ERROR @ insertToDatabase with " + id + " objects to insert");
        }
    }
    */

    public void fillTextViewHeros(int rowsExistent){
        Log.i("fillText", "fillTextViewHeroes called");

        for(int i = 1; i <= rowsExistent && rowsExistent > 0; i++){
                if(i == 1){
                    if (dBmerchantHeroesAdapter.getHeroName(i).equals("NOT_USED")) {
                        textViewHero_0.setText("ALREADY CLICKED");
                    } else {
                        textViewHero_0.setText(dBmerchantHeroesAdapter.getOneHeroRow(i));
                        Log.i("fillText", "textViewHero_0");
                    }
                }
                else
                if (i == 2){
                    if (dBmerchantHeroesAdapter.getHeroName(i).equals("NOT_USED")) {
                        textViewHero_1.setText("ALREADY CLICKED");
                    } else {
                        textViewHero_1.setText(dBmerchantHeroesAdapter.getOneHeroRow(i));
                        Log.i("fillText", "textViewHero_1");
                    }
                }
                else
                if(i == 3){
                    if (dBmerchantHeroesAdapter.getHeroName(i).equals("NOT_USED")) {
                        textViewHero_2.setText("ALREADY CLICKED");
                    } else {
                        textViewHero_2.setText(dBmerchantHeroesAdapter.getOneHeroRow(i));
                        Log.i("fillText", "textViewHero_2");
                    }
                }
                else{
                    Message.message(this, "Number of rows in merchant table: " + i);
                }
        }
    }

    public void setDebugText() {
        String totalText = dBmerchantHeroesAdapter.getAllData();

        debugView.setText(totalText);
    }

    public long insertToMerchantDatabase(int numberOfInserts) {
        List<Hero> herosList = new ArrayList<>();
        long id = 0;

        for (int i = 0; i < numberOfInserts; i++) {
            herosList.add(new Hero());
            herosList.get(i).Initialize("Everywhere");

                // noch Vorgänger-unabhängig -> neue Zeilen werden einfach an Ende angehängt
            id = dBmerchantHeroesAdapter.insertData(
                    herosList.get(i).getStrings("heroName"),
                    herosList.get(i).getInts("hitpoints"),
                    herosList.get(i).getStrings("classPrimary"),
                    herosList.get(i).getStrings("classSecondary"),
                    herosList.get(i).getInts("costs"));

            if(id < 0) Message.message(this, "error@insert of hero " + i + 1);
        }

        return id;
    }

    public void resetMerchant(View view){
        updateMerchantsDatabase(3);
        setNewMerchantProfile();
        fillTextViewHeros(3);
    }

    public void activityMerchantBackToStart(View view) {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
        finish();
    }

    public void buyHero(View view) {

        debugView.setText("buyHero clicked");
    }

    public void selectedHeroIndex0(View view) {
        processSelectedHero(1);
    }

    public void selectedHeroIndex1(View view) {
        processSelectedHero(2);
    }

    public void selectedHeroIndex2(View view) {
        processSelectedHero(3);
    }

    private void processSelectedHero(int index) {

        currentSelectedHeroId = index;

        try {
            String name = dBmerchantHeroesAdapter.getHeroName(currentSelectedHeroId);
            int hitpoints = dBmerchantHeroesAdapter.getHeroHitpoints(currentSelectedHeroId);
            String classOne = dBmerchantHeroesAdapter.getHeroClassOne(currentSelectedHeroId);
            String classTwo = dBmerchantHeroesAdapter.getHeroClassTwo(currentSelectedHeroId);
            int costs = dBmerchantHeroesAdapter.getHeroCosts(currentSelectedHeroId);

            if (!name.equals(this.getString(R.string.indicator_unused_row))) {
                DBheroesAdapter heroesAdapter = new DBheroesAdapter(this);

                for (int i = 1; i <= 10; i++) {

                    int updateValidation = heroesAdapter.updateRow(i, name, hitpoints, classOne, classTwo, costs);
                    if (updateValidation > 0) {

                        // wenn updateValidation speichert Rückgabewert von '.updateRow' -> wenn -1, dann nicht erfolgreich

                        Message.message(this, "Update in HerosDatabase an Stelle " + i + " erfolgreich.");
                        if (i == 10) {
                            Message.message(this, "This was the last free entry in HeroesDatabase");
                        }
                        i = 11;
                        dBmerchantHeroesAdapter.updateRow(currentSelectedHeroId, "NOT_USED");
                    }
                }

                //dBmerchantHeroesAdapter.updateRow(currentSelectedHeroId, "NOT_USED");
                fillTextViewHeros(3);

            } else {
                Message.message(this, "No Hero to buy");
            }

        } catch (SQLiteException | NullPointerException e) {
            Message.message(this, e + "");
        }

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
