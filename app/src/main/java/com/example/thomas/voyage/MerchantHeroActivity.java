package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MerchantHeroActivity extends Activity {

    //private final String SHAREDPREF_INSERT = "INSERT";
    //private final String TIME_PREF_FILE = "timefile";
    DBmerchantHeroesAdapter merchantHelper;
    ImageView merchantProfile;
    private String MERCHANT_ID = "merchantId";
    private String CURRENT_MONEY_FILE = "currentMoneyLong";
    private String origin = "";
    private ImageView textViewHero_0, textViewHero_1, textViewHero_2, marketView;
    private LinearLayout heroDataLayout, containerLayoutMiddle;
    private TextView textView_current_money, textView_available_slots, buyHeroView,tag1, tag2, tag3, nameView, hitpointsView, costsView, primView, secView;
    private int currentSelectedHeroId = 0, currentMerchantId = 0;
    private long currentMoneyInPocket = 0, slotsInHeroesDatabase = 0;
    private boolean availableToBuy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_hero);
        merchantHelper = new DBmerchantHeroesAdapter(this);

        iniViews();
        hideSystemUI();

        Bundle b = getIntent().getExtras();
        if(b != null){
            origin = b.getString("ORIGIN", "StartActivity");
        }

        fillTextViewHeroes(3);
        showExpirationDate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        setSlotsViewAppearence();
        hideSystemUI();
    }

    private void setSlotsViewAppearence(){
        textView_available_slots.setText(Long.toString(getUsedSlotsInHeroesDatabase()) + " / " + slotsInHeroesDatabase);

        if(getUsedSlotsInHeroesDatabase() < slotsInHeroesDatabase){
            textView_available_slots.setBackgroundColor(getResources().getColor(R.color.active_field));

        }else{
            textView_available_slots.setBackgroundColor(getResources().getColor(R.color.inactive_field));
        }
    }

    private long getCurrentMoney() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        currentMoneyInPocket = prefs.getLong(CURRENT_MONEY_FILE, 4500);

        return currentMoneyInPocket;
    }

    private void setCurrentMoney(long money) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putLong(CURRENT_MONEY_FILE, money);
        editor.apply();
    }

    private long getUsedSlotsInHeroesDatabase() {
        DBheroesAdapter helper = new DBheroesAdapter(this);

        long countUsed = 0;
        slotsInHeroesDatabase = helper.getTaskCount();

        for (long i = 0; i < slotsInHeroesDatabase; i++) {
            if (!(helper.getHeroName(i + 1).equals(getResources().getString(R.string.indicator_unused_row)))) {
                countUsed++;
            }
        }

        return countUsed;
    }

    public void clickToHeroesPartyActivity(View view) {
        Intent i = new Intent(getApplicationContext(), HeroesPartyActivity.class);
        startActivity(i);
    }

    private void showExpirationDate() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        long finishDate = prefs.getLong("TIME_TO_LEAVE", setNewDate());

        currentMerchantId = prefs.getInt(MERCHANT_ID, 0);
        merchantProfile.setImageResource(ImgRes.res(this, "merch", currentMerchantId + ""));

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

                showExpirationDate();
            }
        }.start();
    }

    private long setNewDate() {
       SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        long finishDate = prefs.getLong("TIME_TO_LEAVE", 0);

        Date newExpirationDate = new Date();

        //60*60*1000 = 1 Stunde, *18 = 18 Stunden
        newExpirationDate.setTime(System.currentTimeMillis() - finishDate + (60 * 60 * 1000 * 12));

        return newExpirationDate.getTime();
    }

    public void setNewMerchantProfile(){
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

        if(currentMerchantId >= 3){
            currentMerchantId = 0;
        } else currentMerchantId++;

        editor.putInt(MERCHANT_ID, currentMerchantId);
        editor.apply();

        merchantProfile.setImageResource(ImgRes.res(this, "merch", Integer.toString(currentMerchantId)));
    }

    public long updateMerchantsDatabase(int numberOfInserts) {
        List<Hero> herosList = new ArrayList<>();
        long id = 0;

        for (int i = 0; i < numberOfInserts; i++) {
            herosList.add(new Hero(this));
            herosList.get(i).Initialize("Everywhere");

            // noch Vorgänger-unabhängig -> neue Zeilen werden einfach an Ende angehängt
            id = merchantHelper.updateRowComplete(
                    i + 1,
                    herosList.get(i).getStrings("heroName"),
                    herosList.get(i).getInts("hitpoints"),
                    herosList.get(i).getStrings("classPrimary"),
                    herosList.get(i).getStrings("classSecondary"),
                    herosList.get(i).getInts("costs"),
                    herosList.get(i).getStrings("imageResource"));

            //merchantHelper.updateImageResource(i + 1, "hero_dummy_" + (i));

            if (id < 0) Message.message(this, "error@insert of hero " + i + 1);
        }

        return id;
    }

    public void fillTextViewHeroes(int rowsExistent){
        Log.i("fillText", "fillTextViewHeroes called");

        textView_current_money.setText("$ " + getCurrentMoney());
        setSlotsViewAppearence();

        int countUnused = 0;

        for(int i = 1; i <= rowsExistent && rowsExistent > 0; i++){
                if(i == 1){
                    if (merchantHelper.getHeroName(i).equals("NOT_USED")) {
                        textViewHero_0.setImageResource(R.color.standard_background);
                        tag1.setBackgroundColor(getResources().getColor(R.color.standard_background));
                        countUnused++;
                    } else {
                        //merchantProfile.setImageResource(getResources().getIdentifier("merchant_" + currentMerchantId, "mipmap", getPackageName()));
                        //getResources().getIdentifier(merchantHelper.getHeroImgRes(i), "mipmap", getPackageName())
                        textViewHero_0.setImageResource(ImgRes.res(this, "hero", merchantHelper.getHeroImgRes(i)));
                        tag1.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                    }
                }
                else
                if (i == 2){
                    if (merchantHelper.getHeroName(i).equals("NOT_USED")) {
                        textViewHero_1.setImageResource(R.color.standard_background);
                        tag2.setBackgroundColor(getResources().getColor(R.color.standard_background));
                        countUnused++;
                    } else {
                        textViewHero_1.setImageResource(ImgRes.res(this, "hero", merchantHelper.getHeroImgRes(i)));
                        tag2.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                    }
                }
                else
                if(i == 3){
                    if (merchantHelper.getHeroName(i).equals("NOT_USED")) {
                        textViewHero_2.setImageResource(R.color.standard_background);
                        tag3.setBackgroundColor(getResources().getColor(R.color.standard_background));
                        countUnused++;
                    } else {
                        textViewHero_2.setImageResource(ImgRes.res(this, "hero", merchantHelper.getHeroImgRes(i)));
                        tag3.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                    }
                }
                else{
                    Message.message(this, "Number of rows in merchant table: " + i);
                }
        }

        if(countUnused == rowsExistent){
            marketView.setImageResource(R.mipmap.market_dummy_0);
            marketView.setVisibility(View.VISIBLE);
            merchantProfile.setColorFilter(getResources().getColor(R.color.tint_inactive), android.graphics.PorterDuff.Mode.MULTIPLY);
            containerLayoutMiddle.setVisibility(View.INVISIBLE);
        } else {
            merchantProfile.setColorFilter(getResources().getColor(R.color.tint_transparent), android.graphics.PorterDuff.Mode.ADD);
        }
    }

    public void resetMerchant(View view){
        heroDataLayout.setVisibility(View.INVISIBLE);
        updateMerchantsDatabase(3);
        setNewMerchantProfile();
        fillTextViewHeroes(3);

        if(marketView.getVisibility() == View.VISIBLE){
            marketView.setVisibility(View.GONE);
            containerLayoutMiddle.setVisibility(View.VISIBLE);
        }
    }

    public void addMoneyToCurrentMoney(View view){
        setCurrentMoney(getCurrentMoney() + 2500);
        textView_current_money.setText("$ " + getCurrentMoney());
    }

    public void merchantHerosBackbuttonPressed(View view) {
        if(origin.equals("HeroesPartyActivity")){
            Intent i = new Intent(getApplicationContext(), HeroesPartyActivity.class);
            startActivity(i);
            finish();

        }else {
            onBackPressed();
            finish();
        }
    }

    public void goFromMerchantToHeroesParty(View view) {
        Intent i = new Intent(getApplicationContext(), HeroesPartyActivity.class);
        startActivity(i);
    }

    public void buyHero(View view) {

        if (availableToBuy) {
            String name = merchantHelper.getHeroName(currentSelectedHeroId);
            int hitpoints = merchantHelper.getHeroHitpoints(currentSelectedHeroId);
            String classOne = merchantHelper.getHeroClassOne(currentSelectedHeroId);
            String classTwo = merchantHelper.getHeroClassTwo(currentSelectedHeroId);
            int costs = merchantHelper.getHeroCosts(currentSelectedHeroId);
            String imageResource = merchantHelper.getHeroImgRes(currentSelectedHeroId);

            DBheroesAdapter heroesAdapter = new DBheroesAdapter(this);

            for (int i = 1; i <= 10; i++) {

                int updateValidation = heroesAdapter.updateRowWithHeroData(i, name, hitpoints, classOne, classTwo, costs, imageResource);

                if (updateValidation > 0) {

                    // wenn updateValidation speichert Rückgabewert von '.updateRowWithHeroData' -> wenn -1, dann nicht erfolgreich
                    //Message.message(this, "Update in HerosDatabase an Stelle " + i + " erfolgreich.");

                    i = 11;
                    merchantHelper.updateRow(currentSelectedHeroId, "NOT_USED");
                }
            }

            fillTextViewHeroes(3);
            setSlotsViewAppearence();
            currentMoneyInPocket = getCurrentMoney() - costs;
            setCurrentMoney(currentMoneyInPocket);
            textView_current_money.setText("$ " + currentMoneyInPocket);
            buyHeroView.setText("...");
            buyHeroView.setBackgroundColor(getResources().getColor(R.color.inactive_field));
            textView_available_slots.setText(getUsedSlotsInHeroesDatabase() + " / " + slotsInHeroesDatabase);
            heroDataLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void heroSelected(View view){

        switch (view.getId()){
            case R.id.merch_hero_0:
                tag1.setBackgroundColor(getResources().getColor(R.color.standard_background));
                tag2.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                tag3.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                processSelectedHero(1);
                break;

            case R.id.merch_hero_1:
                tag2.setBackgroundColor(getResources().getColor(R.color.standard_background));
                tag1.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                tag3.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                processSelectedHero(2);
                break;

            case R.id.merch_hero_2:
                tag3.setBackgroundColor(getResources().getColor(R.color.standard_background));
                tag2.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                tag1.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_name_tag_under_profile_picture));
                processSelectedHero(3);
                break;

            default:
                Message.message(this, "ERROR @ 'heroSelected'");
        }
    }

    private void processSelectedHero(int index) {
        currentSelectedHeroId = index;

        try {
            String name = merchantHelper.getHeroName(currentSelectedHeroId);
            int hitpoints = merchantHelper.getHeroHitpoints(currentSelectedHeroId);
            String classOne = merchantHelper.getHeroClassOne(currentSelectedHeroId);
            String classTwo = merchantHelper.getHeroClassTwo(currentSelectedHeroId);
            int costs = merchantHelper.getHeroCosts(currentSelectedHeroId);

            if (!name.equals(getResources().getString(R.string.indicator_unused_row))) {
                if (getUsedSlotsInHeroesDatabase() < slotsInHeroesDatabase) {

                    if (currentMoneyInPocket >= costs) {
                        buyHeroView.setText("$ " + costs);
                        buyHeroView.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_permission_to_buy));
                        availableToBuy = true;
                    } else {
                        buyHeroView.setText("$ " + (currentMoneyInPocket - costs));
                        buyHeroView.setBackgroundColor(getResources().getColor(R.color.merchant_heroes_denial_to_buy));
                        availableToBuy = false;
                    }


                } else {
                    buyHeroView.setText("X");
                }

                heroDataLayout.setVisibility(View.VISIBLE);

                nameView.setText(name);
                costsView.setText(costs + "");
                primView.setText(classOne);
                secView.setText(classTwo);
                hitpointsView.setText(hitpoints + "");

            } else {
                Message.message(this, "No Hero to buy");
            }


        } catch (SQLiteException | NullPointerException e) {
            Message.message(this, e + "");
        }

    }


    public void iniViews(){
        heroDataLayout = (LinearLayout)findViewById(R.id.merchant_linearLayout_hero_data);
        nameView = (TextView) findViewById(R.id.merchant_hero_name);
        hitpointsView = (TextView) findViewById(R.id.merchant_hero_hitpoints);
        costsView = (TextView) findViewById(R.id.merchant_hero_costs);
        primView = (TextView) findViewById(R.id.merchant_hero_prim_class);
        secView = (TextView) findViewById(R.id.merchant_hero_sec_class);

        textViewHero_0 = (ImageView) findViewById(R.id.merch_hero_0);
        textViewHero_1 = (ImageView) findViewById(R.id.merch_hero_1);
        textViewHero_2 = (ImageView) findViewById(R.id.merch_hero_2);
        textView_current_money = (TextView) findViewById(R.id.merchant_hero_current_money);
        textView_available_slots = (TextView) findViewById(R.id.merchant_hero_free_slots);
        buyHeroView = (TextView) findViewById(R.id.merchant_hero_buy);
        tag1 = (TextView) findViewById(R.id.merchant_hero_textView_chosen_hero_index0);
        tag2 = (TextView) findViewById(R.id.merchant_hero_textView_chosen_hero_index1);
        tag3 = (TextView) findViewById(R.id.merchant_hero_textView_chosen_hero_index2);
        merchantProfile = (ImageView) findViewById(R.id.imageView_merchant_profile);
        marketView = (ImageView)findViewById(R.id.merchant_market);
        containerLayoutMiddle = (LinearLayout)findViewById(R.id.merchant_linearLayout_middle);
    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
    }

}




/*
        // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
        private void showSystemUI() {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
 */
