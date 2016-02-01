package com.example.thomas.voyage.BasicActivities;

import android.app.Activity;
import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_acitivity);
        hideSystemUI();

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
        Message.message(this, "size bHL: " + brokenHeroList.size());
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
                Message.message(this, "Frame tapped");
                break;
            case R.id.framelayout_hospital_slot_1:
                break;
            case R.id.framelayout_hospital_slot_2:
                break;
            default:
                Message.message(this, "ERROR @ HospitalActivity : onClick : switch : default called");
        }
    }

    private void initializeBrokenHeroes(){
        SharedPreferences prefs = getSharedPreferences("HOSPITAL_SLOT_PREFS", Context.MODE_PRIVATE);

        for(int i = 0, dbIndex; i < 3; i++){
            dbIndex = prefs.getInt("DB_INDEX_BY_SLOT_" + i, -1);

            if(dbIndex == -1)
                slotsList.get(i).showPlaceholder();
            else{
                brokenHeroList.add( new BrokenHero(dbIndex, i) );
                slotsList.get(i).showHero( brokenHeroList.get(i) );
            }
        }
    }

    private void checkForAction(int slotIndex){
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
            brokenHeroList.add( new BrokenHero(slotIndex+1, slotIndex));
            slotsList.get(brokenHeroList.get(slotIndex).getSlotIndex()).showHero(brokenHeroList.get(slotIndex));
            saveNewBrokenHeroInSharedPrefs(slotIndex+1, slotIndex);
        }
    }

    private void saveNewBrokenHeroInSharedPrefs(int dbIndex, int slotIndex){
        SharedPreferences prefs = getSharedPreferences("HOSPITAL_SLOT_PREFS", Context.MODE_PRIVATE);
        prefs.edit().putInt("DB_INDEX_BY_SLOT_" + slotIndex, dbIndex);
        prefs.edit().apply();
    }

    private void removeBrokenHeroFromList(int i){
        slotsList.get(i).showPlaceholder();
        brokenHeroList.remove(i);
        saveNewBrokenHeroInSharedPrefs(-1, i);
    }















    private class Slot{
        private TextView nameView, hpNowView, timeToLeaveView;
        private ImageView profileRessourceView;

        public Slot(int slotIndex){
            profileRessourceView = (ImageView) findViewById(getResources().getIdentifier("imageview_hospital_hero_" + slotIndex, "id", getPackageName()));
            nameView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_name_" + slotIndex,"id",getPackageName()));
            hpNowView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_hp_" + slotIndex,"id",getPackageName()));
            timeToLeaveView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_time_" + slotIndex,"id",getPackageName()));
        }

        private void showHero(BrokenHero hero){
            profileRessourceView.setImageResource(getResources().getIdentifier(hero.getProfileRessource(), "mipmap", getPackageName()));
            nameView.setText(hero.getName());
            hpNowView.setText(hero.getHpNow() + " / " + hero.getHpTotal());
            timeToLeaveView.setText(hero.getTimeToLeave());
        }

        private void showPlaceholder(){
            profileRessourceView.setImageResource(getResources().getIdentifier("journey", "mipmap", getPackageName()));
            nameView.setText("");
            hpNowView.setText("");
            timeToLeaveView.setText("");
        }
    }


    private class BrokenHero{
        private int slotIndex, hpNow, hpTotal = 100, timeToLeave = 24;
        private String name, profileRessource;


        public BrokenHero(int databaseIndex, int si){
            this.slotIndex = si;

            if(databaseIndex >= 1){
                DBheroesAdapter heroesHelper = new DBheroesAdapter(getApplicationContext());
                name = heroesHelper.getHeroName(databaseIndex);
                hpNow = heroesHelper.getHeroHitpoints(databaseIndex);
                profileRessource = heroesHelper.getHeroImgRes(databaseIndex);
            }
        }

        private String getName(){ return name; }
        private String getProfileRessource(){ return profileRessource; }
        private int getHpNow(){ return hpNow; }
        private int getHpTotal(){ return hpTotal; }
        private int getTimeToLeave(){ return timeToLeave; }
        private int getSlotIndex(){ return slotIndex; }
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
