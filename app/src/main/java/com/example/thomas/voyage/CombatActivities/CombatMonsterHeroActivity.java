package com.example.thomas.voyage.CombatActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Monster;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatMonsterHeroActivity extends Activity {

    private int tempScore = 0, bonusHealth = 0, bonusBlock = 0, lastSelectedValIndex = -1, bonusScore, scoreMultiplier;
    private long heroDbIndex;
    private String heroClassActive = "", logTopEntry = "";
    private Monster monster;
    private DBheroesAdapter h;
    private CountAndShowThrowsHelper scoreHelper;
    private List<ThrowInputHelper> scoreHelperList;
    private List<String> eventsList;
    private TextView lastSelectedValView, monsterHpView, heroHpView, battleLogView, lastMultiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_monster_hero);
        hideSystemUI();
        iniValues();
        iniViews();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        hideSystemUI();
    }



    /*

    OnClick - Methoden

     */



    public void onClick(View v){
        switch (v.getId()){
            case R.id.cell_com_miss:
                Msg.msg(this, "You missed dat one!");
                break;
        }
    }

    public void onMultiClick(View v){

        switch (v.getId()){
            case R.id.cell_com_multi_single_in:
                scoreMultiplier = 1;
                break;
            case R.id.cell_com_multi_single_out:
                scoreMultiplier = 1;
                break;
            case R.id.cell_com_multi_x_2:
                scoreMultiplier = 2;
                break;
            case R.id.cell_com_multi_x_3:
                scoreMultiplier = 3;
                break;
            case R.id.cell_com_multi_bull:
                scoreMultiplier = 1;
                combat(25);
                break;
            case R.id.cell_com_multi_eye:
                scoreMultiplier = 2;
                combat(25);
                break;
            default:
                Msg.msg(this, "ERROR @ onMultiClick : default called");
        }


        v.setBackgroundColor(Color.DKGRAY);
        if(lastMultiView != null)
            lastMultiView.setBackgroundResource(R.drawable.ripple_soft_grey_to_white);
        lastMultiView = (TextView) v;
    }



    /*

    Funktionen

    */



    private void combat(int scoreField){

        switch(heroClassActive){
            case "Waldläufer":
                if(scoreField == 1){
                    bonusScore += 10 - scoreMultiplier;
                    applyEffects();
                }
                else if(scoreField == 5 && scoreMultiplier == 1){
                    bonusScore += 5;
                    applyEffects();
                }
                break;
            case "Kneipenschläger":
                break;
            case "Monsterjäger":
                break;
            //Secondaries:
            case "Schurke":
                break;
            case "Spion":
                break;
            case "Glaubenskrieger":
                break;
        }

        tempScore += scoreField * scoreMultiplier * monster.resistance - monster.block;

        logTopEntry = h.getHeroName(heroDbIndex) + " attackiert mit " + (tempScore - bonusScore)
                + " und " + bonusScore + " Bonusangriff!";

        if( monster.checkout.equals("double") && checkOutPossible(2, monster.hp) ){
            if( monster.hp == (scoreField * scoreMultiplier) && scoreMultiplier == 2 ){
                logTopEntry = "DOUBLE WIN!";
                monster.hp = 0;
            }

        }else if( monster.checkout.equals("master") && scoreField * scoreMultiplier == monster.hp ){
            logTopEntry = "MASTER WIN!";
            monster.hp = 0;
        }

        monster.hp -= tempScore;
        scoreHelper.addOneThrow(tempScore);
        //tempScoreHistory[throwCount] = tempScore;
        //throwCount++;

        if( monster.hp <= 0 && monster.checkout.equals("default")) {
            logTopEntry = "NORMAL WIN!";

        } else if(monster.hp <= 0) {
            for(int i = 0; i <= 2; i++) {
                monster.hp += scoreHelper.scoreByThrow[i];
                scoreHelper.scoreByThrow[i] = 0;
                scoreHelper.indexNow = 0;
            }
            //Überworfen!
        }

        monsterHpView.setText(monster.hp + "");

        String tempEventsString = logTopEntry + '\n' + '\n';

        for(int i = eventsList.size() - 1; i >= 0; i--){
            tempEventsString += eventsList.get(i) + '\n';
        }

        battleLogView.setText(tempEventsString);
        eventsList.add(logTopEntry);

        resetBonus();
    }

    private Boolean checkOutPossible(int checkOutType, int monsterHealth){
        switch(checkOutType){
            case 1:
                for(int i = 1; i < 21; i++) {
                    for (int p = 1; p < 4; p++) {
                        if ((i*p - monsterHealth) == 0 || 25 - monsterHealth == 0 || 50 - monsterHealth == 0) {
                            return true;
                        }
                    }
                }
                break;
            case 2:
                if( (monsterHealth == 50) || ((monsterHealth <= 40) && (monsterHealth % 2 == 0)) ){
                    return true;
                }
                break;
        }

        return false;
    }

    private void applyEffects(){
        tempScore += bonusScore;
        h.updateHeroHitpoints((int) heroDbIndex, h.getHeroHitpoints(heroDbIndex) + bonusHealth);
        monster.hp -= bonusBlock;
    }

    private void resetBonus(){
        tempScore = 0;
        bonusScore = 0;
        bonusHealth = 0;
        bonusBlock = 0;
    }

    private void updateCharacterInfoViews(){
        heroHpView.setText(String.valueOf(h.getHeroHitpoints(heroDbIndex)));
        monsterHpView.setText(String.valueOf(monster.hp));
    }



    /*

    Klassen

    */



    private class ThrowInputHelper {
        private TextView valView;
        private int scoreValue;
        private boolean isActive = false;


        public ThrowInputHelper(final int index){
            scoreValue = index + 1;
            valView = (TextView) findViewById(getResources().getIdentifier("cell_com_val_" + index, "id", getPackageName()));
            valView.setText(String.valueOf(index + 1));

            valView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Darstellung bearbeiten
                    /*
                    if (!(lastSelectedValView == null) && lastSelectedValIndex != index)
                        lastSelectedValView.setBackground(getResources().getDrawable(R.drawable.ripple_grey_to_black, null));
                    lastSelectedValIndex = index;
                    lastSelectedValView = (TextView) view;
                    view.setBackgroundResource((isActive = !isActive) ? R.drawable.ripple_from_darkgrey_to_black : R.drawable.ripple_grey_to_black);
                    */

                    // Combat
                    combat(scoreValue);

                }
            });
        }
    }

    // Klasse zur Darstellung der Würfe
    // und Speicherung der Wurf-Werte
    private class CountAndShowThrowsHelper {
        private List<TextView> scoreTextViewList;
        private Integer[] scoreByThrow;
        private int indexNow = 0, numAttacks = 0, numRounds = 1;

        public CountAndShowThrowsHelper(){

            scoreTextViewList = new ArrayList<>();
            scoreByThrow = new Integer[3];

            for(int i = 1; i <= 3; i++){
                scoreTextViewList.add( (TextView) findViewById(getResources().getIdentifier("textview_com_throw_" + i, "id", getPackageName())));
                scoreTextViewList.get(i-1).setText("-");
                scoreTextViewList.get(i-1).setTextColor(Color.LTGRAY);
                scoreByThrow[i-1] = 0;
            }
        }

        public int getLatestThrow(){
            return scoreByThrow[(indexNow) % 3];
        }

        public void addOneThrow(int val){
            scoreByThrow[indexNow] = val;

            // Wenn nächster Wurf wieder an 1. Stelle, dann alle Felder ausgrauen
            if(indexNow == 0 && numAttacks > 0){
                numRounds++;
                for(int i = 0; i < 3; i++){
                    scoreTextViewList.get(i).setTextColor(Color.LTGRAY);
                }

                Random random = new Random();
                int monsterDmg = random.nextInt(monster.dmgMax - monster.dmgMin) + monster.dmgMin;
                h.updateHeroHitpoints( (int) heroDbIndex, h.getHeroHitpoints(heroDbIndex) - monsterDmg);
                logTopEntry = monster.name + " erwidert mit " + monsterDmg + ".";
            }

            this.showLatestThrow();
            updateCharacterInfoViews();

            numAttacks++;
            indexNow = ++indexNow % 3;
        }

        public void showLatestThrow(){
            scoreTextViewList.get(indexNow).setText(String.valueOf(scoreByThrow[indexNow]));
            scoreTextViewList.get(indexNow).setTextColor(Color.BLACK);
        }
    }



    /*

    Auslagerung von Initialisierungen

     */



    private void iniValues(){

        monster = new Monster();

        ConstRes c = new ConstRes();
        h = new DBheroesAdapter(this);
        Bundle b = getIntent().getExtras();
        if(b != null){
            long index = b.getLong(c.DATABASE_INDEX, -1);
            if(index > 0){heroDbIndex = index;}
            else{Log.e("iniValues", "dbIndex from Bundle not delievered");}
        }

        // NUR FÜR DEBUGGING!!!
        heroDbIndex = 1;

        scoreHelperList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            scoreHelperList.add(new ThrowInputHelper(i));
        }

        scoreHelper = new CountAndShowThrowsHelper();
        bonusScore = 0;
        scoreMultiplier = 0;

        eventsList = new ArrayList<>();
    }

    private void iniViews(){

        ImageView monsterProfileView = (ImageView) findViewById(R.id.imageview_com_monster_profile);
        ImageView heroProfileView = (ImageView) findViewById(R.id.imageview_com_hero_profile);
        TextView monsterNameView = (TextView) findViewById(R.id.textview_com_monster_name);
        TextView heroNameView = (TextView) findViewById(R.id.textview_com_hero_name);
        monsterHpView = (TextView) findViewById(R.id.textview_com_monster_hp_now);
        heroHpView = (TextView) findViewById(R.id.textview_com_hero_hp_now);

        monsterProfileView.setImageResource(getResources().getIdentifier(monster.getImgRes(), "mipmap", getPackageName()));
        monsterNameView.setText(monster.name);
        monsterHpView.setText(String.valueOf(monster.hp));

        heroProfileView.setImageResource(getResources().getIdentifier(h.getHeroImgRes(heroDbIndex), "mipmap", getPackageName()));
        heroNameView.setText(h.getHeroName(heroDbIndex));
        heroHpView.setText(String.valueOf(h.getHeroHitpoints(heroDbIndex)));

        battleLogView = (TextView) findViewById(R.id.textview_com_battle_log);
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
