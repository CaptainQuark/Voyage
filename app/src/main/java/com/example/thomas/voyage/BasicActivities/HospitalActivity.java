package com.example.thomas.voyage.BasicActivities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;

public class HospitalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_acitivity);
        hideSystemUI();

        //BrokenHero hero = new BrokenHero(1,0);
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
            default:
                Message.message(this, "ERROR @ HospitalActivity : onClick : switch : default called");
        }
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

    private class BrokenHero{
        private int slotIndex, hpNow, hpTotal = 100, timeToLeave;
        private String name, profileRessource;
        private TextView nameView, hpNowView, timeToLeaveView;
        private ImageView profileRessourceView;

        public BrokenHero(int databaseIndex, int slotIndex){
            DBheroesAdapter heroesHelper = new DBheroesAdapter(getApplicationContext());
            name = heroesHelper.getHeroName(databaseIndex);
            hpNow = heroesHelper.getHeroHitpoints(databaseIndex);
            profileRessource = heroesHelper.getHeroImgRes(databaseIndex);
            //hpTotal = heroesHelper.get

            this.slotIndex = slotIndex;
            if(slotIndex <= 3 && slotIndex >= 0){
                profileRessourceView = (ImageView) findViewById(getResources().getIdentifier("imageview_hospital_hero_" + slotIndex, "id", getPackageName()));
                nameView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_name_" + slotIndex,"id",getPackageName()));
                hpNowView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_hp_" + slotIndex,"id",getPackageName()));
                timeToLeaveView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_time_" + slotIndex,"id",getPackageName()));

            }else{
                Message.message(getApplicationContext(), "slotIndex out of border");
            }

            this.setSlotAppearence();
        }

        private void setSlotAppearence(){
            profileRessourceView.setImageResource(getResources().getIdentifier(profileRessource, "mipmap", getPackageName()));
            nameView.setText(name);
            hpNowView.setText(hpNow + " / " + hpTotal);
        }
    }
}
