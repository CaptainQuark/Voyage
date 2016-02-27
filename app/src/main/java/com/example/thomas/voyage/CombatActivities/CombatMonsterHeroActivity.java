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

    private int tempScore = 0, bonusHealth = 0, bonusScore = 0, scoreMultiplier, currentMonsterCounter = -1, monsterDmg = -1, heroCritChanceP = -1, heroCritChanceS = -1,
            heroCritChanceActive = -1;
    private double heroCritMultiplierP = 1, heroCritMultiplierS = 1,  heroCritMultiplierActive = -1;
    private int heroDbIndex;
    private String heroClassActive = "", logTopEntry = "", levelOfMonsters = "";
    private Monster monster;
    private DBheroesAdapter h;
    private DBplayerItemsAdapter itemHelper;
    private CountAndShowThrowsHelper scoreHelper;
    private HeroAllDataCardFragment heroAllDataCardFragment;
    private MonsterAllDataFragment monsterAllDataFragment;
    private List<ThrowInputHelper> scoreHelperList;
    private List<Item> playerItemList;
    private List<String> logList;
    private TextView monsterHpView, heroHpView, battleLogView, lastMultiView, lastClassView, defaultMultiView, lastSelectedShowBattleView;
    private GridView playerItemGridView;
    private ScrollView battleLogScrollView;

    Integer[] intArray;
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
                int tempMulti = scoreMultiplier;
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
                heroClassActive = h.getHeroPrimaryClass(heroDbIndex);
                heroCritChanceActive = heroCritChanceP;
                heroCritMultiplierActive = heroCritMultiplierP;
                TextView primTempView = (TextView) v;
                primTempView.setBackground(getDrawable(R.drawable.ripple_from_darkgrey_to_black));
                if(lastClassView != null) lastClassView.setBackground(getDrawable(R.drawable.ripple_grey_to_black));
                lastClassView = primTempView;
                break;

            case R.id.cell_com_secondary_attack:
                heroClassActive = h.getHeroSecondaryClass(heroDbIndex);
                heroCritChanceActive = heroCritChanceS;
                heroCritMultiplierActive = heroCritMultiplierS;
                TextView secTempView = (TextView) v;
                secTempView.setBackground(getDrawable(R.drawable.ripple_from_darkgrey_to_black));
                if(lastClassView != null) lastClassView.setBackground(getDrawable(R.drawable.ripple_grey_to_black));
                lastClassView = secTempView;
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

        switch (v.getId()){
            case R.id.cell_com_multi_single_in:
                scoreMultiplier = 1;
                v.setBackgroundColor(Color.WHITE);
                break;
            case R.id.cell_com_multi_single_out:
                scoreMultiplier = 1;
                v.setBackgroundColor(Color.WHITE);
                break;
            case R.id.cell_com_multi_x_2:
                scoreMultiplier = 2;
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark, null));
                break;
            case R.id.cell_com_multi_x_3:
                scoreMultiplier = 3;
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark, null));
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
        if(v.getId() == R.id.cell_com_multi_eye || v.getId() == R.id.cell_com_multi_bull){
            lastMultiView = defaultMultiView;
            defaultMultiView.setBackgroundColor(Color.WHITE);
        } else
            lastMultiView = (TextView) v;
    }

    public void onActionSceneToolbar(View v){
        TextView tv = (TextView) v;

        lastSelectedShowBattleView.setTextColor(getResources().getColor(R.color.grey_7000, null));
        lastSelectedShowBattleView = tv;

        switch(v.getId()){
            case R.id.textview_com_show_battle_log:
                tv.setTextColor(Color.BLACK);
                playerItemGridView.setVisibility(View.GONE);
                battleLogScrollView.setVisibility(View.VISIBLE);
                break;

            case R.id.textview_com_show_inventory:
                tv.setTextColor(Color.BLACK);
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
                    bonusScore += 5;
                }
                break;
            case "Kneipenschläger":
                break;
            case "Monsterjäger":
                break;
            //Secondaries:
            case "Schurke":
                if (scoreField == 1) {
                    bonusScore += 8 - scoreMultiplier;
                } else if (scoreField == 5 && scoreMultiplier == 1) {
                    bonusScore += 3;
                }
                break;
            case "Spion":
                break;
            case "Glaubenskrieger":
                break;
        }

        applyEffects();
        tempScore += scoreField * scoreMultiplier * monster.resistance - monster.block;
        if (tempScore < 0) tempScore = 0;

        if ((int) (Math.random() * 1000) <= monster.evasion || monster.hp <= 170) {
            //Weicht das Monster aus? Ist auch kein Checkout möglich (könnte dies sonst zsammhaun!)?

            if (monster.checkout.equals("double") && checkOutPossible(2)) {
                if (monster.hp == (scoreField * scoreMultiplier) && scoreMultiplier == 2) {
                    combatVictory();
                    monster.hp = 0;
                }

            } else if (monster.checkout.equals("master") && scoreField * scoreMultiplier == monster.hp) {
                combatVictory();
                monster.hp = 0;
            }

            battleLogHandler("heroAttack");
            monster.hp -= tempScore;

            if (monster.hp <= 0 && monster.checkout.equals("default")) {
                Log.v("CombatMonster", "before combatVictory");
                combatVictory();
                Log.v("CombatMonster", "after combatVictory");
            } else if (scoreHelper.addOneThrow(tempScore)) {
            //Schau ob 3. Wurf, falls ja: Berechne MonsterDmg
                calculateMonsterDamage();
            } else if (monster.hp <= 0) {
                calculateMonsterDamage();
                scoreHelper.bustReset();
                //Überworfen!
            }
        } else {
            battleLogHandler("monsterEvasion");
            if (scoreHelper.addOneThrow(0)) {
                calculateMonsterDamage();
            }
        }

        monsterHpView.setText(monster.hp + "");

        if (h.getHeroHitpoints(heroDbIndex) <= 0) {
            h.markOneRowAsUnused(heroDbIndex);
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
            finish();
        }
        resetBonus();
    }

    private void calculateMonsterDamage(){
        Random random = new Random();

        //Trifft das Monster?
        if (random.nextInt(1000) < monster.accuracy) {

            //Kann der Held ausweichen?
            if (random.nextInt(1000) < h.getHeroEvasion(heroDbIndex)) {
                monsterDmg = random.nextInt(monster.dmgMax - monster.dmgMin) + monster.dmgMin;
                h.updateHeroHitpoints( heroDbIndex, h.getHeroHitpoints(heroDbIndex) - monsterDmg);
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
                }
                else if(bonusScore == 0){
                    logTopEntry = h.getHeroName(heroDbIndex) + " attackiert mit " + (tempScore - bonusScore) + ". ";
                }
                else{
                    logTopEntry = h.getHeroName(heroDbIndex) + " attackiert mit " + (tempScore - bonusScore)
                            + " und " + bonusScore + " Bonusangriff!";
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
                logTopEntry = "   " + monster.name + " erwidert mit " + monsterDmg + ".";
                break;
            case "heroEvasion":
                logTopEntry = h.getHeroName(heroDbIndex) + "kann dem Angriff ausweichen. ";
                break;
            case "monsterMiss":
                logTopEntry = monster.name + " schlägt daneben. ";
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
                    this, new ConstRes(), heroDbIndex, b.getString(c.CURRENT_BIOME, "Forest"),
                    b.getString(c.COMBAT_LEVEL_OF_MONSTERS, "Easy"), b.getInt(c.COMBAT_LENGTH, 1) - 1));
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
                        if ((i*p - monster.hp) == 0 || 25 - monster.hp == 0 || 50 - monster.hp == 0) {
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
        tempScore += bonusScore;
        h.updateHeroHitpoints( heroDbIndex, h.getHeroHitpoints(heroDbIndex) + bonusHealth);
    }

    private void resetBonus(){
        tempScore = 0;
        bonusScore = 0;
        bonusHealth = 0;
    }

    private void updateCharacterInfoViews(){
        heroHpView.setText(String.valueOf(h.getHeroHitpoints(heroDbIndex)));
        monsterHpView.setText(String.valueOf(monster.hp));
    }



    /*

    Interaction-Listener für Fragmente

     */



    @Override
    public void putFragmentToSleep() {
        if(heroAllDataCardFragment != null){
            getFragmentManager().beginTransaction().remove(heroAllDataCardFragment).commit();
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
        private Integer[] tempScoreHistory;
        private int throwCount = 0, numAttacks = 0;

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
                    scoreTextViewList.get(i).setTextColor(Color.LTGRAY);
                }
            }

            scoreTextViewList.get(throwCount).setText(String.valueOf(tempScoreHistory[throwCount]));
            scoreTextViewList.get(throwCount).setTextColor(Color.BLACK);

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
            for (int i = 0; i <= 2; i++) {
                monster.hp += scoreHelper.tempScoreHistory[i];
                tempScoreHistory[i] = 0;
                throwCount = 0;
                battleLogHandler("heroBust");
                updateCharacterInfoViews();
            }
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

            ConstRes c = new ConstRes();
            h = new DBheroesAdapter(this);
            Bundle b = getIntent().getExtras();
            if(b != null){
                int index = b.getInt(c.HERO_DATABASE_INDEX, -1);
                if(index > 0){heroDbIndex = index;}
                else{Log.e("iniValues", "dbIndex from Bundle not delievered");}

                levelOfMonsters = b.getString(c.COMBAT_LEVEL_OF_MONSTERS);
                currentMonsterCounter = b.getInt(c.COMBAT_LENGTH);

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
                        b.getInt(c.MONSTER_BLOCK),
                        b.getDouble(c.MONSTER_RESISTANCE),
                        b.getDouble(c.MONSTER_CRIT_MULTIPLIER),
                        b.getString(c.MONSTER_DIFFICULTY),
                        b.getInt(c.MONSTER_BOUNTY)
                );

            }else{
                Msg.msg(this, "ERROR @ iniValues : Bundle is null");
            }

            //CritChance und Multiplier für Primär und Sekundär werden initialisiert
            for(int i = 0; i < heroresourcetable.size(); i++){
                if(heroresourcetable.get(i)[2].equals(h.getHeroPrimaryClass(heroDbIndex))){
                    heroCritChanceP = Integer.parseInt(heroresourcetable.get(i)[10]);
                }
                if(heroresourcetable.get(i)[2].equals(h.getHeroPrimaryClass(heroDbIndex))){
                    heroCritMultiplierP = Double.parseDouble(heroresourcetable.get(i)[11]);
                }
                if(heroresourcetable.get(i)[2].equals(h.getHeroSecondaryClass(heroDbIndex))){
                    heroCritChanceS = Integer.parseInt(heroresourcetable.get(i)[10]);
                }
                if(heroresourcetable.get(i)[2].equals(h.getHeroSecondaryClass(heroDbIndex))){
                    heroCritMultiplierS = Double.parseDouble(heroresourcetable.get(i)[11]);
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

        ImageView monsterProfileView = (ImageView) findViewById(R.id.imageview_com_monster_profile);
        ImageView heroProfileView = (ImageView) findViewById(R.id.imageview_com_hero_profile);
        TextView monsterNameView = (TextView) findViewById(R.id.textview_com_monster_name);
        TextView heroNameView = (TextView) findViewById(R.id.textview_com_hero_name);
        monsterHpView = (TextView) findViewById(R.id.textview_com_monster_hp_now);
        heroHpView = (TextView) findViewById(R.id.textview_com_hero_hp_now);

        monsterProfileView.setImageResource(getResources().getIdentifier(monster.imgRes, "mipmap", getPackageName()));
        monsterNameView.setText(monster.name);
        monsterHpView.setText(String.valueOf(monster.hp));

        heroProfileView.setImageResource(getResources().getIdentifier(h.getHeroImgRes(heroDbIndex), "mipmap", getPackageName()));
        heroNameView.setText(h.getHeroName(heroDbIndex));
        heroHpView.setText(String.valueOf(h.getHeroHitpoints(heroDbIndex)));

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
