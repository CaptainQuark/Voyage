package com.example.thomas.voyage.CombatActivities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.thomas.voyage.BasicActivities.StartActivity;
import com.example.thomas.voyage.ContainerClasses.HelperCSV;
import com.example.thomas.voyage.ContainerClasses.PassParametersHelper;
import com.example.thomas.voyage.ContainerClasses.Item;
import com.example.thomas.voyage.ContainerClasses.Monster;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Databases.DBplayerItemsAdapter;
import com.example.thomas.voyage.Fragments.HeroAllDataCardFragment;
import com.example.thomas.voyage.Fragments.MonsterAllDataFragment;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatMonsterHeroActivity extends Activity implements HeroAllDataCardFragment.onHeroAllDataCardListener,
        MonsterAllDataFragment.OnFragmentInteractionListener{

    private int tempScore = 0, bonusHealth = 0, bonusDamage = 0, turnsBetweenRetreat = -1,
            currentMonsterCounter, bountyTotal, monsterDmg = -1, heroCritChanceP = -1, heroCritChanceS = -1,
            heroCritChanceActive = -1, bonusBounty = 0, bonusScore = 0, bonusEvasion = 0, throwCount = 0,
            bonusMonsterEvasion = 0;
    private Integer[] tempScoreHistory;
    private double heroCritMultiplierP = 1, heroCritMultiplierS = 1,  heroCritMultiplierActive = -1, bonusCritMultiplier = 1, heroResistance = 1, scoreMultiplier, bonusArmorTear = 1;
    private int heroDbIndex;
    private String heroClassActive = "", logTopEntry = "", levelOfMonsters = "", currentBiome;
    private Monster monster;
    private DBheroesAdapter h;
    private DBplayerItemsAdapter itemHelper;
    private CountAndShowThrowsHelper scoreHelper;
    private HeroAllDataCardFragment heroAllDataCardFragment;
    private MonsterAllDataFragment monsterAllDataFragment;
    private ImageView monsterProfileView;
    private TextView monsterNameView;
    private List<ThrowInputHelper> scoreHelperList;
    private List<Item> playerItemList;
    private List<String> logList;
    private LinearLayout mainLayout;
    private TextView monsterHpView, heroHpView, battleLogView, lastMultiView, lastClassView, defaultMultiView, lastSelectedShowBattleView;
    private GridView playerItemGridView;
    private ScrollView battleLogScrollView;

    //Monster-Fähigkeiten
    private boolean monsterScalingDamage = false;

    Integer[] intArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_monster_hero);
        hideSystemUI();

        //try {


            iniValues();
            iniViews();

        monsterProfileView = (ImageView) findViewById(R.id.imageview_com_monster_profile);
        monsterProfileView.setImageResource(getResources().getIdentifier(monster.imgRes, "mipmap", getPackageName()));
        monsterNameView = (TextView) findViewById(R.id.textview_com_monster_name);
        monsterNameView.setText(monster.name);

        /*}catch(Exception e){
            Msg.msg(this, String.valueOf(e));
            Log.e("ERROR@Null", String.valueOf(e));
        }

    */
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
                double tempMulti = scoreMultiplier;
                scoreMultiplier = 1;
                combat(0);
                scoreMultiplier = tempMulti;
                break;

            case R.id.imageview_com_hero_profile:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                heroAllDataCardFragment = new HeroAllDataCardFragment();
                Bundle b = new Bundle();
                b.putInt("DB_INDEX_PLAYER", heroDbIndex);

                heroAllDataCardFragment.setArguments(b);
                fragmentTransaction.add(R.id.layout_com_main, heroAllDataCardFragment);
                fragmentTransaction.commit();
                break;

            case R.id.cell_com_undo:
                Msg.msgShort(this, "No undo yet implemented...");
                break;

            case R.id.cell_com_primary_attack:
                if(throwCount == 0) {
                    heroClassActive = h.getHeroPrimaryClass(heroDbIndex);
                    heroCritChanceActive = heroCritChanceP;
                    heroCritMultiplierActive = heroCritMultiplierP;
                    TextView primTempView = (TextView) v;
                    primTempView.setBackground(getDrawable(R.drawable.ripple_from_darkgrey_to_black));
                    if (lastClassView != null)
                        lastClassView.setBackground(getDrawable(R.drawable.ripple_grey_to_black));
                    lastClassView = primTempView;
                } else {
                    Msg.msgShort(this, "Die aktive Klasse kann nur zwischen den Würfen gewählt werden!");
                }
                break;

            case R.id.cell_com_secondary_attack:
                if(throwCount == 0) {
                    heroClassActive = h.getHeroSecondaryClass(heroDbIndex);
                    heroCritChanceActive = heroCritChanceS;
                    heroCritMultiplierActive = heroCritMultiplierS;
                    TextView secTempView = (TextView) v;
                    secTempView.setBackground(getDrawable(R.drawable.ripple_from_darkgrey_to_black));
                    if (lastClassView != null)
                        lastClassView.setBackground(getDrawable(R.drawable.ripple_grey_to_black));
                    lastClassView = secTempView;
                } else {
                    Msg.msgShort(this, "Die aktive Klasse kann nur zwischen den Würfen gewählt werden!");
                }
                break;

            case R.id.imageview_com_monster_profile:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                monsterAllDataFragment = new MonsterAllDataFragment();
                monsterAllDataFragment.setArguments(PassParametersHelper.toMonsterAllDataFragment(new ConstRes(), monster));
                ft.add(R.id.layout_com_main, monsterAllDataFragment);
                ft.commit();
                break;

            default:
                Msg.msgShort(this, "ERROR @ onClick : switch : default called");
        }
    }

    public void onScoreMulti(View v){
        TextView tv = (TextView) v;

        switch (tv.getId()){
            case R.id.cell_com_multi_single_in:
                scoreMultiplier = 1.1;
                tv.setBackgroundColor(getColor(R.color.combat_multi_field_active));
                tv.setTextColor(Color.BLACK);
                break;
            case R.id.cell_com_multi_single_out:
                scoreMultiplier = 1.2;
                tv.setBackgroundColor(getColor(R.color.combat_multi_field_active));
                tv.setTextColor(Color.BLACK);
                break;
            case R.id.cell_com_multi_x_2:
                scoreMultiplier = 2;
                tv.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark, null));
                break;
            case R.id.cell_com_multi_x_3:
                scoreMultiplier = 3;
                tv.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark, null));
                break;
            case R.id.cell_com_multi_bull:
                scoreMultiplier = 1;
                combat(25);
                scoreMultiplier = 1;
                break;
            case R.id.cell_com_multi_eye:
                scoreMultiplier = 2;
                combat(25);
                scoreMultiplier = 1;
                break;
            default:
                Msg.msg(this, "ERROR @ onScoreMulti : default called");
        }

        if(lastMultiView != tv){
            lastMultiView.setTextColor(Color.WHITE);
            // Elemente ohne break lassen switch einfach zu
            // nächstem Element weiterlaufe -> solange, bis
            // ein break erreicht wird -> man spart sich if( x || y || z)
            switch(lastMultiView.getId()){
                case R.id.cell_com_multi_eye:
                    lastMultiView.setBackgroundResource(R.drawable.ripple_soft_grey_to_red);
                    break;
                case R.id.cell_com_multi_bull:
                case R.id.cell_com_multi_x_3:
                case R.id.cell_com_multi_x_2:
                    lastMultiView.setBackgroundResource(R.drawable.ripple_soft_grey_to_green);
                    break;
                default:
                    lastMultiView.setBackgroundResource(R.drawable.ripple_soft_grey_to_white);
            }

            // Wenn Bulls oder Bullseye getroffen werden, dann wird
            // wieder SingleOut (default) gewählt
            if(tv.getId() == R.id.cell_com_multi_eye || tv.getId() == R.id.cell_com_multi_bull){
                lastMultiView = defaultMultiView;
                defaultMultiView.setBackgroundColor(getColor(R.color.combat_multi_field_active));
                defaultMultiView.setTextColor(Color.BLACK);
            } else
                lastMultiView = tv;
        }
    }

    public void onActionSceneToolbar(View v){
        TextView tv = (TextView) v;

        tv.setTextColor(Color.WHITE);
        lastSelectedShowBattleView.setTextColor(getColor(R.color.grey_7000));
        lastSelectedShowBattleView = tv;

        switch(v.getId()){
            case R.id.textview_com_show_battle_log:
                tv.setTextColor(Color.WHITE);
                playerItemGridView.setVisibility(View.GONE);
                battleLogScrollView.setVisibility(View.VISIBLE);
                break;

            case R.id.textview_com_show_inventory:
                tv.setTextColor(Color.WHITE);
                playerItemGridView.setVisibility(View.VISIBLE);
                battleLogScrollView.setVisibility(View.GONE);
                break;

            default:
                Msg.msg(this, "ERROR @ onActionSceneToolbar : switch : default called");
        }
    }



    /*

    Funktionen

    */


    private void combat(int scoreField) {

        switch (heroClassActive) {
            case "Waldläufer":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex)) {
                    bonusDamage += 7;
                }
                break;
            case "Barbar":
                int v = 0;
                for (int i = 0; i <= 2; i++) {
                    v += tempScoreHistory[i];
                }
                if (v > 64 && throwCount == 2) {
                    bonusDamage += 25;
                    heroCritChanceActive -= 10;
                    heroCritChanceP -= 10;
                    heroCritChanceS -= 10;
                }
                break;
            case "Kleriker":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex)) {
                    bonusHealth += 2 * (int) scoreMultiplier;
                } else if (scoreField == h.getHeroBonusNumber(heroDbIndex) + 2 || scoreField == h.getHeroBonusNumber(heroDbIndex) - 2) {
                    if (!monster.checkout.equals("double") && monster.hp - 3 == 0) combatVictory();
                    if (monster.checkout.equals("default") || monster.hp - 3 > 1)
                        monster.hp -= 3;
                }
                break;
            case "Kneipenschläger":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex)) {
                    heroResistance -= 0.15;
                }
                break;
            case "Musketier":
                if (scoreField == 25) bonusScore += 10;
                break;
            case "Schwertmeister":
                if (scoreField == 1) {
                    bonusDamage += 7 - (int) scoreMultiplier;
                } else if (scoreField == 5 && (int) scoreMultiplier == 1) {
                    bonusDamage += 2;
                }
                break;
            case "Freischärler":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex)) {
                    switch ((int) scoreMultiplier) {
                        case 1:
                            bonusDamage = 30 - scoreField * (int) scoreMultiplier;
                            break;
                        case 2:
                            bonusDamage = 55 - scoreField * (int) scoreMultiplier;
                            break;
                        case 3:
                            bonusDamage = 80 - scoreField * (int) scoreMultiplier;
                            break;
                    }
                    bonusBounty -= 9;
                }
                break;
            case "Plänkler":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex)) bonusEvasion += 100;
                break;

            //Secondaries:
            case "Attentäter":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex) && (int) scoreMultiplier == 1)
                    bonusArmorTear = 0;
                break;
            case "Dieb":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex)) bonusBounty += 5;
                break;
            case "Scharfschütze":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex) || scoreField == 25) {
                    bonusMonsterEvasion -= 1000;
                }
                break;
            case "Plünderer":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex)) {
                    if ((monster.checkout.equals("double") && scoreField * 2 == monster.hp)
                            || (!monster.checkout.equals("double") && scoreField * (int) scoreMultiplier == monster.hp - monster.armor)) {
                        Msg.msgShort(this, "Wertvoller Plunder wurde gefunden!");
                        if (scoreMultiplier == 3) bonusBounty += monster.bounty / 4;
                        else if (scoreMultiplier == 2) bonusBounty += monster.bounty / 7;
                        else bonusBounty += monster.bounty / 14;
                    }
                }
                break;
            case "Gladiator":
                if (scoreField == 0) bonusEvasion += 150;
                break;
            case "Kopfgeldjäger":
                if (scoreField == h.getHeroBonusNumber(heroDbIndex))
                    bonusDamage += monster.bounty / 50;
                break;
            case "Okkultist":
                if (scoreMultiplier == 1.1) {
                    bonusHealth += scoreField;
                } else bonusHealth -= scoreField * (int) scoreMultiplier;
                break;
            default:
                Log.e("COMBAT: ", "Error@switch: heroClassActive invalid");
        }

        applyEffects();

        //Crit-Probe nur möglich falls kein Checkout oder HP außerhalb des Finish-Bereichs
        if ((int) (Math.random() * 1000) > heroCritChanceActive){
            if(monster.checkout.equals("default") || monster.hp > 170){
                bonusCritMultiplier += heroCritMultiplierActive - 1;
                battleLogHandler("heroCrit");
                Log.i("CRITSUCCESS: ", "bonusCritMultiplier: " + bonusCritMultiplier);
            }
        }

        tempScore += ((scoreField + bonusScore) * (int) scoreMultiplier + bonusDamage) * bonusCritMultiplier * monster.resistance - (monster.armor * bonusArmorTear);
        if (tempScore < 0) tempScore = 0;

        //Weicht das Monster aus? Ist auch kein Checkout möglich (könnte dies sonst zsammhaun!)?
        if ((int) (Math.random() * 1000) <= monster.evasion - bonusMonsterEvasion ||
                (monster.hp <= 170 && monster.checkout.equals("master")) ||
                (monster.hp <= 170 && monster.checkout.equals("double"))) {

            //Checkout-Probe mit raw-Score und dem tempScore mit Boni
            if (monster.checkout.equals("double") && checkOutPossible(2)) {
                if ((monster.hp == (scoreField * (int) scoreMultiplier) && scoreMultiplier == 2)
                        || (monster.hp == tempScore && scoreMultiplier == 2)) {
                    combatVictory();
                    monster.hp = 0;
                }

            } else if (monster.checkout.equals("master")) {
                if (scoreField * (int) scoreMultiplier == monster.hp || monster.hp == tempScore) {
                combatVictory();
                monster.hp = 0;
            }
            }

            battleLogHandler("heroAttack");
            monster.hp -= tempScore;

            if (monster.hp <= 0 && monster.checkout.equals("default")) {
                Log.v("CombatMonster", "before combatVictory");
                combatVictory();
                Log.v("CombatMonster", "after combatVictory");
            } else if (monster.hp <= 0 || (monster.hp == 1 && monster.checkout.equals("double"))) {
                //Überworfen!
                calculateMonsterDamage();
                scoreHelper.bustReset();
            } else if (scoreHelper.addOneThrow(tempScore)) {
                //Schau ob 3. Wurf, falls ja: Berechne MonsterDmg und Spezialattacken
                heroSpecialEffects();
                calculateMonsterDamage();
                resetBonusAfterTurn();
            }

        } else {
            battleLogHandler("monsterEvasion");
            if (scoreHelper.addOneThrow(0)) {
                calculateMonsterDamage();
            }
        }

        updateCharacterInfoViews();

        if (h.getHeroHitpoints(heroDbIndex) <= 0) {
            h.markOneRowAsUnused(heroDbIndex);
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
            finish();
        }
        resetBonusAfterThrow();
    }

    private void heroSpecialEffects(){
    }

    private void calculateMonsterDamage(){
        Random random = new Random();

        //Trifft das Monster?
        if (random.nextInt(1000) < monster.accuracy) {

            //Kann der Held ausweichen?
            if (random.nextInt(1000) < h.getHeroEvasion(heroDbIndex) - bonusEvasion) {
                monsterDmg = random.nextInt(monster.dmgMax - monster.dmgMin) + monster.dmgMin;

                //Krittet das Monster?
                if((Math.random() * 1000) > monster.critChance) {
                    monsterDmg *= monster.critMultiplier;
                    battleLogHandler("monsterCrit");
                }

                //Monster-Fähigkeiten
                if(monsterScalingDamage){
                    Log.i("CALCULATEMONSTERDAMAGE:", "Original damage: " + monsterDmg);
                    if (monster.hp < monster.hpTotal / 3) monsterDmg = monsterDmg / 3;
                    else if (monster.hp < monster.hpTotal / 3 * 2) monsterDmg = (monsterDmg / 3) * 2;

                    Log.i("CALCULATEMONSTERDAMAGE:", "Scaled Damage: " + monsterDmg);
                }
                Log.i("HERORESISTANCE:", "DMG ohne Res: " + monsterDmg + " DMG + RES " + monsterDmg * heroResistance + " RES: " + heroResistance);
                h.updateHeroHitpoints( heroDbIndex, (int) (h.getHeroHitpoints(heroDbIndex) - monsterDmg * heroResistance));
                battleLogHandler("monsterAttack");
            }
            else{
                battleLogHandler("heroEvasion");
            }
        }
        else{
            battleLogHandler("monsterMiss");
        }
        updateCharacterInfoViews();
    }

    private void battleLogHandler(String s){
        switch(s){
            case "heroAttack":
                if(tempScore == 0){
                    logTopEntry = "Der Angriff hat keine Auswirkungen";
                } else if(bonusDamage == 0 && bonusScore == 0){
                    logTopEntry = h.getHeroName(heroDbIndex) + " attackiert mit " + (tempScore) + ". ";
                } else if(bonusScore == 0){
                    logTopEntry = h.getHeroName(heroDbIndex) + " attackiert mit " + (tempScore - bonusDamage)
                            + " und " + bonusDamage + " Bonusschaden!";
                } else if(bonusDamage == 0){
                    logTopEntry = h.getHeroName(heroDbIndex) + " attackiert mit " + (tempScore - bonusScore * scoreMultiplier)
                            + " und " + bonusScore * scoreMultiplier + " Bonusangriff!";
                } else{
                    logTopEntry = h.getHeroName(heroDbIndex) + " attackiert mit " + (tempScore - bonusDamage - bonusScore * scoreMultiplier)
                            + " und " + bonusDamage + " Bonusschaden sowie " + bonusScore * scoreMultiplier + " Bonusscore!";
                }
                break;
            case "heroBust":
                logTopEntry = "Du hast überworfen! ";
                break;
            case "monsterEvasion":
                logTopEntry = monster.name + " weicht der Attacke aus! ";
                Msg.msgShort(this, "Der Angriff schlägt fehl!");
                break;
            case "monsterAttack":
                logTopEntry = '\n' + monster.name + " erwidert mit " + monsterDmg + "." + '\n';
                break;
            case "heroEvasion":
                logTopEntry = h.getHeroName(heroDbIndex) + " kann dem Angriff ausweichen. ";
                break;
            case "monsterMiss":
                logTopEntry = '\n' + monster.name + " schlägt daneben. " + '\n';
                break;
            case "heroCrit":
                logTopEntry = h.getHeroName(heroDbIndex) + " landet einen kritischen Treffer! ";
                break;
            case "monsterCrit":
                logTopEntry = '\n' + monster.name + " attackiert mit voller Wucht! " + '\n';
                break;
            default:
                logTopEntry = "FEHLER @BATTLELOGHANDLER! ";
        }

        String tempEventsString = logTopEntry + '\n' + '\n';

        for(int i = logList.size() - 1; i >= 0; i--){
            tempEventsString += logList.get(i) + '\n';
        }

        battleLogView.setText(tempEventsString);
        logList.add(logTopEntry);
    }

    private void combatVictory(){

        Bundle b = getIntent().getExtras();
        ConstRes c = new ConstRes();

        // wenn noch nicht letztes Monster erreicht, eröffne neuen Kampf
        if(b.getInt(c.COMBAT_LENGTH, 1) > 1){

            // Mit 'PassParametersHelper' werden Werte-Übergaben an Intent
            // standardisiert - keine Kopierfehler, weniger Code, leichter zu verstehen
            startActivity(PassParametersHelper.toCombatSplash(
                    this, new ConstRes(), heroDbIndex,
                    b.getString(c.CURRENT_BIOME, "Forest"),
                    b.getString(c.COMBAT_LEVEL_OF_MONSTERS, "Easy"),
                    b.getInt(c.COMBAT_LENGTH, 1),
                    b.getInt(c.COMBAT_MONSTER_COUNTER) + 1,
                    //TODO: Bounty randomness einfügen
                    b.getInt(c.COMBAT_BOUNTY_TOTAL) + (monster.bounty + (monster.bounty / 100 * (currentMonsterCounter + turnsBetweenRetreat)) + bonusBounty)));
            Log.i("BOUNTY", "monsterBounty : " + monster.bounty + (monster.bounty / 100 * (currentMonsterCounter + turnsBetweenRetreat)) +
                    " / raw bounty: " + monster.bounty
                    + " / new bountyTotal: " + (bountyTotal + (monster.bounty + (monster.bounty / 100 * (currentMonsterCounter + turnsBetweenRetreat)))));
            finish();

        }else{
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
            finish();
        }
    }

    private Boolean checkOutPossible(int checkOutType){
        switch(checkOutType){
            case 1:
                for(int i = 1; i < 21; i++) {
                    for (int p = 1; p < 4; p++) {
                        if ((i*p - monster.hp) == 0 || monster.hp == 25 || monster.hp == 50) {
                            return true;
                        }
                    }
                }
                break;
            case 2:
                if( (monster.hp == 50) || ((monster.hp <= 40) && (monster.hp % 2 == 0)) ){
                    return true;
                }
                break;
        }

        return false;
    }

    private void applyEffects(){
        h.updateHeroHitpoints( heroDbIndex, h.getHeroHitpoints(heroDbIndex) + bonusHealth);
    }

    private void resetBonusAfterThrow(){
        tempScore = 0;
        bonusCritMultiplier = 1;
        bonusDamage = 0;
        bonusHealth = 0;
        bonusScore = 0;
        bonusArmorTear = 1;
        bonusMonsterEvasion = 0;
    }

    private void resetBonusAfterTurn(){
        heroResistance = 1;
        bonusEvasion = 0;
    }

    private void updateCharacterInfoViews(){
        heroHpView.setText(String.valueOf(h.getHeroHitpoints(heroDbIndex)) + " / " + String.valueOf(h.getHeroHitpointsTotal(heroDbIndex)));
        monsterHpView.setText(String.valueOf(monster.hp) + " / " + String.valueOf(monster.hpTotal));
    }



    /*

    Interaction-Listener für Fragmente

     */



    @Override
    public void putFragmentToSleep() {
        if(heroAllDataCardFragment != null){
            getFragmentManager().beginTransaction().remove(heroAllDataCardFragment).commit();
            heroAllDataCardFragment = null;
        }
    }

    @Override
    public void putMonsterAllDataFragToSleep() {
        if(monsterAllDataFragment != null){
            getFragmentManager().beginTransaction().remove(monsterAllDataFragment).commit();
            monsterAllDataFragment = null;
        }
    }



    /*

    Klassen

    */



    private class ThrowInputHelper {
        private TextView valView;
        private int scoreValue;

        public ThrowInputHelper(final int index){
            scoreValue = index + 1;
            valView = (TextView) findViewById(getResources().getIdentifier("cell_com_val_" + index, "id", getPackageName()));
            valView.setText(String.valueOf(index + 1));

            valView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    combat(scoreValue);
                }
            });
        }
    }

    // Klasse zur Darstellung der Würfe
    // und Speicherung der Wurf-Werte
    private class CountAndShowThrowsHelper {
        private List<TextView> scoreTextViewList;
        private int numAttacks = 0;

        public CountAndShowThrowsHelper(){

            scoreTextViewList = new ArrayList<>();
            tempScoreHistory = new Integer[3];

            for(int i = 1; i <= 3; i++){
                scoreTextViewList.add( (TextView) findViewById(getResources().getIdentifier("textview_com_throw_" + i, "id", getPackageName())));
            }
        }

        //Aktualisiert Score-Display und returned 'true' falls 3. Wurf
        public boolean addOneThrow(int val){
            tempScoreHistory[throwCount] = val;

            // Wenn nächster Wurf wieder an 1. Stelle, dann alle Felder ausgrauen
            if(throwCount == 0 && numAttacks > 0) {
                for (int i = 0; i <= 2; i++) {
                    scoreTextViewList.get(i).setTextColor(Color.DKGRAY);
                }
            }

            scoreTextViewList.get(throwCount).setText(String.valueOf(tempScoreHistory[throwCount]));
            if (throwCount == 1) {
                scoreTextViewList.get(throwCount).setText(String.valueOf(tempScoreHistory[throwCount] + tempScoreHistory[0]));
            } else if (throwCount == 2) {
                scoreTextViewList.get(throwCount).setText(String.valueOf(tempScoreHistory[throwCount] + tempScoreHistory[0] + tempScoreHistory[1]));
            }
            scoreTextViewList.get(throwCount).setTextColor(Color.WHITE);

            throwCount++;
            numAttacks++;

            if(throwCount == 3){
                for (int i = 0; i <= 2; i++) {
                    tempScoreHistory[i] = 0;
                    throwCount = 0;
                }
                return true;
            }
            else return false;
        }

        //Überworfen!
        public void bustReset(){
            tempScoreHistory[throwCount] = tempScore;
            for (int i = 0; i <= 2; i++) {
                monster.hp += tempScoreHistory[i];
                Log.i("BUST:", i + ". Eintrag, + " + tempScoreHistory[i] + " Leben!");
                tempScoreHistory[i] = 0;
            }
            throwCount = 0;
            resetBonusAfterTurn();
            battleLogHandler("heroBust");
            updateCharacterInfoViews();
        }
    }



    /*

    Adapter

     */



    public class InventoryAdapter extends BaseAdapter {
        private Context mContext;

        public InventoryAdapter(Context c) {
            mContext = c;
        }

        public int getCount() { return playerItemList.size(); }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {

            private TextView nameView, desView;
            private ImageView itemProfileView;

            ViewHolder(View v) {

                nameView = (TextView) v.findViewById(R.id.textiew_com_listitem_item_name);
                desView = (TextView) v.findViewById(R.id.textiew_com_listitem_item_description);
                itemProfileView = (ImageView) v.findViewById(R.id.imageview_com_listitem_item_profile);
            }
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listitem_com_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            /*
            holder.itemProfileView.setImageResource(R.mipmap.placeholder_dummy_0);
            holder.nameView.setText(position + ". " + playerItemList.get(position).getStrings("ITEM_NAME"));
            holder.desView.setText(playerItemList.get(position).getStrings("DES_MAIN"));
            */

            holder.nameView.setText(String.valueOf(intArray[position]));
            holder.desView.setText(String.valueOf(intArray[position+1]));
            return convertView;
        }
    }



    /*

    Auslagerung von Initialisierungen

     */



    private void iniValues(){

        try {
            HelperCSV helperCSV = new HelperCSV(this);
            List<String[]> heroresourcetable = helperCSV.getDataList("heroresourcetable");
            List<String[]> monsterresourcetable = helperCSV.getDataList("monsterresourcetable");

            ConstRes c = new ConstRes();
            h = new DBheroesAdapter(this);
            Bundle b = getIntent().getExtras();
            if(b != null){
                int index = b.getInt(c.HERO_DATABASE_INDEX, -1);
                if(index > 0){heroDbIndex = index;}
                else{Log.e("iniValues", "dbIndex from Bundle not delievered");}

                currentBiome = b.getString(c.CURRENT_BIOME);
                levelOfMonsters = b.getString(c.COMBAT_LEVEL_OF_MONSTERS);
                turnsBetweenRetreat = b.getInt(c.COMBAT_LENGTH);
                currentMonsterCounter = b.getInt(c.COMBAT_MONSTER_COUNTER);
                bountyTotal = b.getInt(c.COMBAT_BOUNTY_TOTAL);

                String currentBiomeFileName;
                switch (currentBiome){
                    case "Forest":
                        currentBiomeFileName = "journey_b0";
                        break;
                    case "Placeholder_Mountains":
                        currentBiomeFileName = "journey_b1";
                        break;
                    case "Placeholder_Dungeon":
                        currentBiomeFileName = "journey_b2";
                        break;
                    default:
                        currentBiomeFileName = "journey_b0";
                }

                //mainLayout.setBackgroundResource(getResources().getIdentifier(currentBiomeFileName, "id", getPackageName()));

                monster = new Monster(
                        b.getString(c.MONSTER_NAME),
                        b.getString(c.MONSTER_CHECKOUT),
                        b.getString(c.MONSTER_IMG_RES),
                        b.getInt(c.MONSTER_EVASION),
                        b.getInt(c.MONSTER_ACCURACY),
                        b.getInt(c.MONSTER_CRIT_CHANCE),
                        b.getInt(c.MONSTER_HITPOINTS_NOW),
                        b.getInt(c.MONSTER_HITPOINTS_TOTAL),
                        b.getInt(c.MONSTER_DAMAGE_MIN),
                        b.getInt(c.MONSTER_DAMAGE_MAX),
                        b.getInt(c.MONSTER_ARMOR),
                        b.getDouble(c.MONSTER_RESISTANCE),
                        b.getDouble(c.MONSTER_CRIT_MULTIPLIER),
                        b.getString(c.MONSTER_DIFFICULTY),
                        b.getInt(c.MONSTER_BOUNTY)
                );

            }else{
                Msg.msg(this, "ERROR @ iniValues : Bundle is null");
            }

            //Monster-Fähigkeiten werden ausgelesen
            for (int m = 0; m < monsterresourcetable.size(); m++) {
                if (helperCSV.getString("monsterresourcetable", m, "Name").equals(monster.name)) {
                    Log.i("INIVALUES: ", "Monsterdaten wurden ausgelesen");
                    monsterScalingDamage = Boolean.parseBoolean(helperCSV.getString("monsterresourcetable", m, "ScalingDamage"));
                }
            }

            //CritChance und Multiplier für Primär und Sekundär werden initialisiert
            for(int i = 0; i < heroresourcetable.size(); i++){
                if(heroresourcetable.get(i)[2].equals(h.getHeroPrimaryClass(heroDbIndex))){
                    heroCritChanceP = Integer.parseInt(helperCSV.getString("heroresourcetable", i, "CritChance"));
                }
                if(heroresourcetable.get(i)[2].equals(h.getHeroPrimaryClass(heroDbIndex))){
                    heroCritMultiplierP = Double.parseDouble(helperCSV.getString("heroresourcetable", i, "CritMultiplier"));
                }
                if(heroresourcetable.get(i)[2].equals(h.getHeroSecondaryClass(heroDbIndex))){
                    heroCritChanceS = Integer.parseInt(helperCSV.getString("heroresourcetable", i, "CritChance"));
                }
                if(heroresourcetable.get(i)[2].equals(h.getHeroSecondaryClass(heroDbIndex))){
                    heroCritMultiplierS = Double.parseDouble(helperCSV.getString("heroresourcetable", i, "CritMultiplier"));
                }
            }

            heroClassActive = h.getHeroPrimaryClass(heroDbIndex);
            heroCritChanceActive = heroCritChanceP;
            heroCritMultiplierActive = heroCritMultiplierP;

            scoreHelperList = new ArrayList<>();
            for(int i = 0; i < 20; i++){
                scoreHelperList.add(new ThrowInputHelper(i));
            }

            scoreHelper = new CountAndShowThrowsHelper();
            scoreMultiplier = 1;

            logList = new ArrayList<>();
            playerItemList = new ArrayList<>();

            itemHelper = new DBplayerItemsAdapter(this);

            for(long i = 1; i <= itemHelper.getTaskCount(); i++){
                if(!itemHelper.getItemName(i).equals(c.NOT_USED)){
                    playerItemList.add(new Item(
                            itemHelper.getItemName(i),
                            itemHelper.getItemDescriptionMain(i),
                            itemHelper.getItemDescriptionAdditonal(i),
                            itemHelper.getItemRarity(i),
                            itemHelper.getItemSkillsId(i),
                            itemHelper.getItemBuyCosts(i),
                            itemHelper.getItemSpellCosts(i)
                    ));
                }
            }

            intArray = new Integer[100];
            for(int i = 0; i < intArray.length; i++){
                intArray[i] = i+1;
            }

        }catch (Exception e){
            Msg.msg(this, String.valueOf(e));
            Log.e("iniValues()", String.valueOf(e));
        }
    }

    private void iniViews(){

        mainLayout = (LinearLayout) findViewById(R.id.layout_com_main);

        ImageView heroProfileView = (ImageView) findViewById(R.id.imageview_com_hero_profile);
        TextView heroNameView = (TextView) findViewById(R.id.textview_com_hero_name);
        monsterHpView = (TextView) findViewById(R.id.textview_com_monster_hp_now);
        heroHpView = (TextView) findViewById(R.id.textview_com_hero_hp_now);

        heroProfileView.setImageResource(getResources().getIdentifier(h.getHeroImgRes(heroDbIndex), "mipmap", getPackageName()));
        heroNameView.setText(h.getHeroName(heroDbIndex));

        updateCharacterInfoViews();

        lastSelectedShowBattleView = (TextView) findViewById(R.id.textview_com_show_battle_log);

        battleLogView = (TextView) findViewById(R.id.textview_com_battle_log);
        battleLogScrollView = (ScrollView) findViewById(R.id.scrollview_com_battle_log);
        defaultMultiView = (TextView) findViewById(R.id.cell_com_multi_single_out);

        playerItemGridView = (GridView) findViewById(R.id.gridview_com_inventory);
        playerItemGridView.setAdapter(new InventoryAdapter(this));
        lastMultiView = defaultMultiView;
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