package com.example.thomas.voyage.BasicActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.HelperSharedPrefs;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;

public class HospitalActivity extends Activity {

    private DBheroesAdapter h;
    private ConstRes c;
    private HelperSharedPrefs prefs = new HelperSharedPrefs();
    private List<BrokenHero> brokenHeroList = new ArrayList<>();
    private List<Slot> slotsList = new ArrayList<>();
    private TextView freeSlotsView, fortuneView, abortMedicationView, boostMedicationView;
    private int selectedSlotIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        hideSystemUI();
        prepareActivity();

        /*

        USAGE:

        Um Helden heilen zu lassen, wird dieser über

         -> freie Hospital-Fläche antippen
         -> 'HeroCampActivity' starten und angetippten slotIndex mitgeben
         -> Helden in 'HeroCampActivity' auswählen - "MedSlotIndex"-Eintrag wird angepasst
         -> zurück zu 'HospitalActivity'
         -> Held wird angezeigt

        eingetragen.

        */
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }



    /*

     onClick-Methoden

     */



    public void onClick(View v){
        switch(v.getId()){
            case R.id.imagebutton_hospital_back_button:
                super.onBackPressed();
                finish();
                break;
            case R.id.framelayout_hospital_slot_0:
                checkForActionAfterTapOnCard(0);
                break;
            case R.id.framelayout_hospital_slot_1:
                checkForActionAfterTapOnCard(1);
                break;
            case R.id.framelayout_hospital_slot_2:
                checkForActionAfterTapOnCard(2);
                break;
            case R.id.textview_hospital_abort_medication:
                abortMedication();
                break;
            case R.id.textview_hospital_boost_healing:
                boostMedication();
                break;
            default:
                Msg.msg(this, "ERROR @ HospitalActivity : onClick : switch : default called");
        }
    }



    /*

     Funktionen

     */



    private void initializeBrokenHeroes(){
        try {
            long sizeDatabase = h.getTaskCount();

            for(int i = 1; i <= sizeDatabase; i++){
                Log.v("iniBrokenHeroes", "time to leave from db @ index " + i + ": " + h.getTimeToLeave(i));
            }

            List<Integer> placeholderList = new ArrayList<>();
            for(int i = 0; i < 3; i++) placeholderList.add(i);

            for(int i = 1; i <= sizeDatabase; i++){
                int medSlot = h.getMedSlotIndex(i);

                if(medSlot != -1){

                    //Msg.msg(getApplicationContext(), "medSlot != -1 @ index " + i);

                    brokenHeroList.add( new BrokenHero(i, medSlot));
                    slotsList.get(h.getMedSlotIndex(i)).showHero(brokenHeroList.get(brokenHeroList.size() - 1));

                    for(int j = 0; j < placeholderList.size(); j++){
                        if(placeholderList.get(j) == medSlot) placeholderList.remove(j);
                    }
                }
            }

            for(int i = 0; i < brokenHeroList.size(); i++){
                slotsList.get( brokenHeroList.get(i).getSlotIndex()).showHero(brokenHeroList.get(i));
            }

            for(int i = 0; i < placeholderList.size(); i++){
                slotsList.get( placeholderList.get(i)).showPlaceholder();
            }

            refreshToolbarViews();

        }catch (IndexOutOfBoundsException e){
            Msg.msg(this, e + "");
        }
    }

    private void checkForActionAfterTapOnCard(int slotIndex){
        boolean isUsed = false;
        //selectedSlotIndex = slotIndex;
        selectedSlotIndex = (slotIndex == selectedSlotIndex) ? -1 : slotIndex;

        for(int i = 0; i < brokenHeroList.size(); i++){
            if( brokenHeroList.get(i).getSlotIndex() == slotIndex ){isUsed = true;}
        }

        if(!isUsed){

            Intent i = new Intent(getApplicationContext(), HeroCampActivity.class);
            i.putExtra(c.ORIGIN, "HospitalActivity");
            i.putExtra(c.SLOT_INDEX, slotIndex);
            startActivity(i);
            finish();

        }else{
            for(int i = 0; i < slotsList.size(); i++){
                boolean used = false;
                for(int j = 0; j < brokenHeroList.size(); j++){
                    if(brokenHeroList.get(j).getSlotIndex() == i){
                        used = true;
                        slotsList.get(i).showHero(brokenHeroList.get(j));
                        j = brokenHeroList.size();
                    }

                    if(!used) slotsList.get(i).showPlaceholder();
                }
            }

            refreshToolbarViews();
        }
    }

    private void releaseBrokenHero(int slotIndex){

        for(int index = 0; index < brokenHeroList.size(); index++){

            if(brokenHeroList.get(index).getSlotIndex() == slotIndex){

                slotsList.get(slotIndex).showPlaceholder();
                //brokenHeroList.get(index).setHeroHitpoints(brokenHeroList.get(index).getHpNow());
                if(!brokenHeroList.get(index).setHeroMedSlotIndex(-1)){ Log.e("releaseBrokenHero", "setHeroMedSlotIndex(-1)"); }
                if(!brokenHeroList.get(index).setTimeToLeave(0)){ Log.e("releaseBrokenHero", "setTimeToLeave(-1)"); }
                brokenHeroList.remove(index);

                index = 3;

            }else if(index == 2) Msg.msg(this, "ERROR @ releaseBrokenHero : no matching index");
        }
    }

    private void refreshToolbarViews(){
        if(selectedSlotIndex != -1){
            abortMedicationView.setTextColor(Color.BLACK);
            boostMedicationView.setTextColor(Color.BLACK);

        }else{
            abortMedicationView.setTextColor(getColor(R.color.button_text_inactive));
            boostMedicationView.setTextColor(getColor(R.color.button_text_inactive));
        }

        freeSlotsView.setText(brokenHeroList.size() + " / " + slotsList.size());
        fortuneView.setText("$ " + prefs.getCurrentMoney(this, new ConstRes()));
    }

    private void abortMedication(){

        /*

        'abortMedicaton' sollte keine Kosten verursachen,
        um zu vermeiden, dass dem Spieler alle Helden fehlen

         */

        if(selectedSlotIndex == -1){
            Msg.msg(this, "No hero yet choosen!");

        }else{
            Log.v("releaseBrokenHero", "brokenHeroList.size : " + brokenHeroList.size());
            Log.v("releaseBrokenHero", "slotList.size : " + slotsList.size());
            for(int i = 0; i < brokenHeroList.size(); i++){
                if( brokenHeroList.get(i).getSlotIndex() == selectedSlotIndex){

                    releaseBrokenHero(selectedSlotIndex);
                    i = brokenHeroList.size();
                    Msg.msg(this, "No more medication");
                }
            }

            selectedSlotIndex = -1;
            refreshToolbarViews();
        }
    }

    private void boostMedication(){
        if(selectedSlotIndex == -1){
            Msg.msg(this, "No hero yet choosen!");

        }else{
            Log.v("boostMedication", "brokenHeroList.size : " + brokenHeroList.size());
            Log.v("boostMedication", "slotList.size : " + slotsList.size());
            for(int i = 0; i < brokenHeroList.size(); i++){
                if( brokenHeroList.get(i).getSlotIndex() == selectedSlotIndex){

                    long money = prefs.getCurrentMoney(this, new ConstRes());
                    int hoursToLeave = (int) ((brokenHeroList.get(i).getTimeToLeave() - System.currentTimeMillis()) / 1000 / 60 / 60);

                    if(money - hoursToLeave * 150 >= 0){
                        // pro fehlendem Hitpoint werden 150 Kosten als Penality verrecnet
                        prefs.removeFromCurrentMoneyAndGetNewVal(hoursToLeave * 150, this, new ConstRes());

                        if(!brokenHeroList.get(i).setHeroHitpoints(brokenHeroList.get(i).getHpTotal()))
                            Msg.msg(this, "ERROR @ setHeroHitpoints");

                        // zum Beenden der Schleife
                        i = brokenHeroList.size();
                        Msg.msg(this, "Heal-A-Hero!");
                        releaseBrokenHero(selectedSlotIndex);

                    }else{
                        Msg.msg(this, "No money, boy!");
                    }

                }else{
                    Log.v("BOOST", "no match at indices");
                }
            }

            selectedSlotIndex = -1;
            refreshToolbarViews();
        }
    }



    /*

    Klassen

     */



    private class Slot{
        private FrameLayout containerLayout;
        private TextView nameView, hpNowView, timeToLeaveView, staticHpView, staticTimeView;
        private ImageView profileResourceView;
        private int slotIndex;

        public Slot(int slotIndex){
            this.slotIndex = slotIndex;
            profileResourceView = (ImageView) findViewById(getResources().getIdentifier("imageview_hospital_hero_" + slotIndex, "id", getPackageName()));
            nameView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_name_" + slotIndex,"id",getPackageName()));
            hpNowView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_hp_" + slotIndex,"id",getPackageName()));
            timeToLeaveView = (TextView) findViewById(getResources().getIdentifier("textview_hospital_hero_time_" + slotIndex,"id",getPackageName()));
            staticHpView = (TextView) findViewById(getResources().getIdentifier("static_textview_hospital_hp_" + slotIndex,"id",getPackageName()));
            staticTimeView = (TextView) findViewById(getResources().getIdentifier("static_textview_hospital_time_" + slotIndex,"id",getPackageName()));
            containerLayout = (FrameLayout) findViewById(getResources().getIdentifier("framelayout_hospital_slot_" + slotIndex, "id", getPackageName()));
        }

        public void showHero(BrokenHero hero){
            profileResourceView.setImageResource(getResources().getIdentifier(hero.getProfileResource(), "mipmap", getPackageName()));
            nameView.setText(String.valueOf(hero.getName() + ""));
            hpNowView.setText(String.valueOf(hero.getHpNow() + " / " + hero.getHpTotal()));
            staticHpView.setVisibility(View.VISIBLE);
            staticTimeView.setText(String.valueOf("abreise in min"));
            timeToLeaveView.setText((String.valueOf((hero.getTimeToLeave() - System.currentTimeMillis()) / 1000 / 60)));

            if(slotIndex == selectedSlotIndex || selectedSlotIndex == -1) containerLayout.setForeground(null);
            else containerLayout.setForeground(new ColorDrawable(Color.parseColor("#ae000000")));
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
        private int slotIndex, dbIndex, hpNow, hpTotal;
        private long timeToLeave;
        private String name, profileResource;

        public BrokenHero(int dbIndex, int slotIndex){
            this.slotIndex = slotIndex;
            this.dbIndex = dbIndex;

            if(dbIndex > 0){
                name = h.getHeroName(dbIndex);
                hpNow = h.getHeroHitpoints(dbIndex);
                profileResource = h.getHeroImgRes(dbIndex);
                hpTotal = h.getHeroHitpointsTotal(dbIndex);

                timeToLeave = h.getTimeToLeave(dbIndex);

                // Überprüfe, ob Held geheilt ist, also ob Zeitpunkt der Heilung
                // bereits in der Vergangenheit liegt
                if(timeToLeave - System.currentTimeMillis() > 0){
                    hpNow = hpTotal - 1 - (int) ((timeToLeave - System.currentTimeMillis()) / 1000 / 60 / c.MIN_TO_HEAL_PER_HP);
                    this.setHeroHitpoints(hpNow);

                }else{
                    releaseBrokenHero(this.slotIndex);
                }
            }
        }

        public boolean setHeroHitpoints(int hpNew){
            return h.updateHeroHitpoints(dbIndex, hpNew) != -1;
        }

        public boolean setHeroMedSlotIndex(int slotIndex){
            this.slotIndex = slotIndex;
            return h.updateMedSlotIndex(dbIndex, slotIndex);
        }

        public boolean setTimeToLeave(long time) {
            Log.v("setTimeToLeave",  "setTimeToLeave called with time: " + time);
            return h.updateTimeToLeave(dbIndex, time);
        }

        public String getName(){ return name; }
        public String getProfileResource(){ return profileResource; }
        public int getHpNow(){ return hpNow; }
        public int getHpTotal(){ return hpTotal; }
        public long getTimeToLeave(){ return timeToLeave; }
        public int getSlotIndex(){ return slotIndex; }
    }



    /*

    Funktionen zur Auslagerung von Initialisierungen

     */



    private void prepareActivity(){
        freeSlotsView = (TextView) findViewById(R.id.textview_hospital_slots);
        fortuneView = (TextView) findViewById(R.id.textview_hospital_fortune);
        abortMedicationView = (TextView) findViewById(R.id.textview_hospital_abort_medication);
        boostMedicationView = (TextView) findViewById(R.id.textview_hospital_boost_healing);

        h = new DBheroesAdapter(getApplicationContext());
        c = new ConstRes();

        for(int i = 0; i < 3; i++) slotsList.add( new Slot(i) );
        initializeBrokenHeroes();
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