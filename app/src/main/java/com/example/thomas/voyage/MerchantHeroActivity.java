package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MerchantHeroActivity extends Activity {

    DBmerchantHeroesAdapter dBmerchantHeroesAdapter;

    private TextView debugView, buyHeroView, textViewHero_0, textViewHero_1, textViewHero_2;
    private int currentSelectedHeroId;
    private int currentMoneyInPocket;

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

        fillTextViewHeros(maintainDatabaseForThreeRows());

        setDebugText();

    }

    public int maintainDatabaseForThreeRows(){
        int rowsExistent = dBmerchantHeroesAdapter.getTableRowCount();
        Message.message(this, "rowsExistent: " + rowsExistent);

        if(rowsExistent >=0 && rowsExistent <= 2){

            // Differenz zwischen max. Heldenanzahl zum Auswählen und vorhanderer wird als Anzahl der neuen Einfügungen genommen
            insertToMerchantDatabase(3 - rowsExistent);
            Message.message(this, "neu eingefügt: 3 - " + rowsExistent);
            Log.i("1", "insertToMerchantDatabase with " + " 3 - " + rowsExistent);

        } else if(rowsExistent > 3){

            while(rowsExistent > 3){
                Log.i("t", "while-loop wiht rowsExistent at " + rowsExistent);
                dBmerchantHeroesAdapter.deleteRow(rowsExistent);
                rowsExistent = rowsExistent-1;
            }
        } else if(rowsExistent < 0){

            Message.message(this, "rowsExistent in 3. if-statement = " + rowsExistent);
            Log.i("k", "3. else-ausführung = befüllung der datenbank mit 3 Helden");
        }

        return rowsExistent;
    }

    public void fillTextViewHeros(int rowsExistent){

        Log.i("fillText", "fillTextViewHeroes called");

        for(int i = 1; i <= rowsExistent && rowsExistent > 0; i++){

            try {
                if(i == 1){
                    textViewHero_0.setText(dBmerchantHeroesAdapter.getOneHeroRow(i));
                    Log.i("fillText", "textViewHero_0");
                }
                else
                if (i == 2){
                    Log.i("fillText", "textViewHero_0");
                    textViewHero_1.setText(dBmerchantHeroesAdapter.getOneHeroRow(i));
                }
                else
                if(i == 3){
                    Log.i("fillText", "textViewHero_0");
                    textViewHero_2.setText(dBmerchantHeroesAdapter.getOneHeroRow(i));
                }
                else{
                    Message.message(this, "Number of rows in merchant table: " + i);
                }
            }catch (SQLiteException e){
                Message.message(this, "error @ fillTextViewHeroes = " + e);
            }


        }
    }

    public void setDebugText() {

        String totalText = dBmerchantHeroesAdapter.getAllData();

        debugView.setText(totalText);
    }

    public void insertToMerchantDatabase(int numberOfInserts){
        List<Hero> herosList = new ArrayList<>();

        for (int i = 0; i < numberOfInserts; i++) {
            herosList.add(new Hero());
            herosList.get(i).Initialize();

                // noch Vorgänger-unabhängig -> neue Zeilen werden einfach an Ende angehängt
            long id = dBmerchantHeroesAdapter.insertData(
                    herosList.get(i).getStrings("heroName"),
                    herosList.get(i).getInts("hitpoints"),
                    herosList.get(i).getStrings("classPrimary"),
                    herosList.get(i).getStrings("classSecondary"),
                    herosList.get(i).getInts("costs"));

            if(id < 0) Message.message(this, "error@insert of hero " + i + 1);
        }

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
        processSelectedHero(0);
    }

    public void selectedHeroIndex1(View view) {
        processSelectedHero(1);
    }

    public void selectedHeroIndex2(View view) {
        processSelectedHero(2);
    }

    private void processSelectedHero(int index) {

        currentSelectedHeroId = index;

        if (index == 1) {
            buyHeroView.setBackgroundColor(Color.GREEN);
        } else buyHeroView.setBackgroundColor(Color.RED);

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
