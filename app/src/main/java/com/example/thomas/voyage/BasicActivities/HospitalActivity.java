package com.example.thomas.voyage.BasicActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.List;

public class HospitalActivity extends Activity {

    private List<BrokenHero> brokenHeroList = new ArrayList<>();
    private List<Slot> slotsList = new ArrayList<>();
    private TextView freeSlotsView, fortuneView;
    private int lastSelectedSlotIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_acitivity);
        hideSystemUI();
        iniGeneralViews();

        /*

        USAGE:

        Um Helden heilen zu lassen, wird dieser über

         -> freie Hospital-Fläche antippen
         -> Helden in 'HeroCampActivity' auswählen
         -> Datenbank-Index wird an 'HospitalActivity' zurückgereicht
         -> Held wird angelegt

        eingetragen.

        */

        for(int i = 0; i < 3; i++) slotsList.add( new Slot(i) );
        initializeBrokenHeroes();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.imagebutton_hospital_back_button:
                super.onBackPressed();
                finish();
                break;
            case R.id.framelayout_hospital_slot_0:
                checkForAction(0);
                break;
            case R.id.framelayout_hospital_slot_1:
                checkForAction(1);
                break;
            case R.id.framelayout_hospital_slot_2:
                checkForAction(2);
                break;
            case R.id.textview_hospital_abort_medication:
                sellTheMan();
                break;
            case R.id.textview_hospital_back_to_camp:
                Intent i = new Intent(getApplicationContext(), HeroCampActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                Message.message(this, "ERROR @ HospitalActivity : onClick : switch : default called");
        }
    }

    private void initializeBrokenHeroes(){
        try {
            SharedPreferences prefs = getSharedPreferences("HOSPITAL_SLOT_PREFS", Context.MODE_PRIVATE);

            for(int i = 0, dbIndex; i < 3; i++){
                dbIndex = prefs.getInt("DB_INDEX_BY_SLOT_" + i, -1);

                if(dbIndex == -1)
                    slotsList.get(i).showPlaceholder();
                else{
                    brokenHeroList.add(new BrokenHero(dbIndex, i));
                    slotsList.get(i).showHero( brokenHeroList.get( brokenHeroList.size()-1) );
                }
            }

            setFreeSlotsView();
            setFortuneView();
        }catch (IndexOutOfBoundsException e){
            Message.message(this, e + "");
        }
    }

    private void checkForAction(int slotIndex){
        lastSelectedSlotIndex = slotIndex;
        boolean isUsed = false;

        for(int i = 0; i < brokenHeroList.size(); i++){
            if( brokenHeroList.get(i).getSlotIndex() == slotIndex ){
                i = brokenHeroList.size();
                Message.message(this, "Heal-A-Hero!");
                removeBrokenHeroFromList(slotIndex);
                isUsed = true;
            }
        }

        if(!isUsed){
            brokenHeroList.add(new BrokenHero(slotIndex + 1, slotIndex));
            slotsList.get( slotIndex ).showHero(brokenHeroList.get(brokenHeroList.size() - 1));
        }

        setFreeSlotsView();
    }

    private void removeBrokenHeroFromList(int i){

        for(int index = 0; index < 3; index++){
            if(brokenHeroList.get(index).getSlotIndex() == i){
                slotsList.get(i).showPlaceholder();
                removeSharedPreference(i);
                brokenHeroList.remove(index);
                index = 3;

            }else if(index == 2) Message.message(this, "ERROR @ removeBrokenHeroFromList : no matching index");
        }
    }

    private void removeSharedPreference(int i){
        SharedPreferences prefs = getSharedPreferences("HOSPITAL_SLOT_PREFS", Context.MODE_PRIVATE);
        prefs.edit().remove("DB_INDEX_BY_SLOT_" + i).apply();
    }

    private void setFreeSlotsView(){
        freeSlotsView.setText(brokenHeroList.size() + " / " + slotsList.size());
    }

    private void setFortuneView(){
        SharedPreferences prefs = getSharedPreferences("CURRENT_MONEY_PREF", Context.MODE_PRIVATE);
        long money = prefs.getLong("currentMoneyLong", -1);

        fortuneView.setText("$ " + money);
    }

    private void sellTheMan(){
        if(lastSelectedSlotIndex == -1){
            Message.message(this, "No hero yet choosen!");

        }else{
            Message.message(this, "No selling for slot " + lastSelectedSlotIndex + " yet implemented");
            lastSelectedSlotIndex = -1;
        }
    }














    private class Slot{
        private TextView nameView, hpNowView, timeToLeaveView, staticHpView, staticTimeView;
        private ImageView profileResourceView;

        public Slot(int slotIndex){
            profileResourceView = (ImageView) findViewById(getResources().getIdentifier("imageview_hospital_hero_" + slotIndex, "id", getPackageName()));
            nameView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_name_" + slotIndex,"id",getPackageName()));
            hpNowView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_hp_" + slotIndex,"id",getPackageName()));
            timeToLeaveView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_time_" + slotIndex,"id",getPackageName()));
            staticHpView = (TextView) findViewById(getResources().getIdentifier("static_textview_hospital_hp_" + slotIndex,"id",getPackageName()));
            staticTimeView = (TextView) findViewById(getResources().getIdentifier("static_textview_hospital_time_" + slotIndex,"id",getPackageName()));
        }

        public void showHero(BrokenHero hero){
            profileResourceView.setImageResource(getResources().getIdentifier(hero.getProfileResource(), "mipmap", getPackageName()));
            nameView.setText(hero.getName() + "");
            hpNowView.setText(hero.getHpNow() + " / " + hero.getHpTotal());
            timeToLeaveView.setText(hero.getTimeToLeave() + "");
            staticHpView.setVisibility(View.VISIBLE);
            staticTimeView.setText("abreise in min");

        }

        public void showPlaceholder(){
            profileResourceView.setImageResource(getResources().getIdentifier("journey_b3", "mipmap", getPackageName()));
            nameView.setText("");
            hpNowView.setText("");
            timeToLeaveView.setText("");
            staticHpView.setVisibility(View.INVISIBLE);
            staticTimeView.setText("hinzufügen");
        }
    }


    private class BrokenHero{
        private int slotIndex, hpNow, hpTotal = 100, timeToLeave = 24;
        private String name, profileResource;


        public BrokenHero(int databaseIndex, int si){
            this.slotIndex = si;

            SharedPreferences prefs = getSharedPreferences("HOSPITAL_SLOT_PREFS", Context.MODE_PRIVATE);
            prefs.edit().putInt("DB_INDEX_BY_SLOT_" + si, databaseIndex).apply();

            if(databaseIndex >= 1){
                DBheroesAdapter heroesHelper = new DBheroesAdapter(getApplicationContext());
                name = heroesHelper.getHeroName(databaseIndex);
                hpNow = heroesHelper.getHeroHitpoints(databaseIndex);
                profileResource = heroesHelper.getHeroImgRes(databaseIndex);
            }
        }

        public String getName(){ return name; }
        public String getProfileResource(){ return profileResource; }
        public int getHpNow(){ return hpNow; }
        public int getHpTotal(){ return hpTotal; }
        public int getTimeToLeave(){ return timeToLeave; }
        public int getSlotIndex(){ return slotIndex; }
    }













    private void iniGeneralViews(){
        freeSlotsView = (TextView) findViewById(R.id.textview_hospital_slots);
        fortuneView = (TextView) findViewById(R.id.textview_hospital_fortune);
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
