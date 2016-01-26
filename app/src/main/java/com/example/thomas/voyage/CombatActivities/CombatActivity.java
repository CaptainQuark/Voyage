package com.example.thomas.voyage.CombatActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thomas.voyage.BasicActivities.StartActivity;
import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.ContainerClasses.Monster;
import com.example.thomas.voyage.Databases.DBplayerItemsAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ImgRes;

import java.util.ArrayList;
import java.util.List;

public class CombatActivity extends Activity {

    private static int
            selectedCellPosition = 0,
            monsterHealthConst = -1,
            heroHitpointsConst = -1,
            scoreMultiplier = 1,
            dartCount = 1,
            heroClassActive = 1,
            roundCount = 0;
    private static String heroImgRes = "", chronicleString = "", eventString = "";
    private static String[] iconArray = {"X 1", "1.", "X 2", "2.", "X 3", "SP", "BULL", "IN", "EYE", "OUT"};
    private static int[]
            scoreHeroMultiplierArray = {-1, -1, -1},
            scoreHeroFieldArray = {-1, -1, -1};
    private static TextView monsterHealthView,
            heroHitpointsView,
            heroNameView,
            monsterNameView,
            eventsView,
            chronicleView;
    private static ImageView
            healthbarMonsterVital,
            healthBarMonsterDamaged,
            healthBarHeroVital,
            healthBarHeroDamaged,
            heroProfileView;
    private static List<Integer> undoListForHero, selectedItems, invetoryItems;
    private static List<Hero> heroList;
    private static List<Monster> monsterList;
    private static LinearLayout.LayoutParams
            paramsBarMonsterDamaged,
            paramsBarMonsterVital,
            paramsBarHeroDamaged,
            paramsBarHeroVital;
    private String origin = "";
    private GridView gridViewNumbers, gridViewSpecials;
    private static DBplayerItemsAdapter itemHelper;

    static int monsterResistance = 1;
    static int monsterBlock = 0;
    static int tempScore = 0;
    static int triggerScore = 0;
    static int triggerMulti = 0;
    static int bonusScore = 0;
    static int playerHealth = 0, bonusHealth = 0, monsterHealth = 300;
    static String monsterCheckout = "";
    static int throwCount = 0;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_combat);
        hideSystemUI();
        initializeViews();
        initializeValues();


    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }







    private static void combat(int scoreField, int multiField){
        throwCount++;

        tempScore += scoreField * multiField * monsterResistance - monsterBlock;

        if( scoreField == triggerScore && multiField == triggerMulti ){
            tempScore += bonusScore;
            playerHealth += bonusHealth;
        }

        if( monsterCheckout == "double" && checkOutPossible(2) ){
            if( monsterHealth == (scoreField * multiField) && multiField == 2 ){
                Message.message(context, "Double Win!");
            }

        }else if( monsterCheckout == "master" && checkOutPossible(1) ){
            if( monsterHealth == (scoreField * multiField) ){
                Message.message(context, "Master Win!");
            }

        }else{
            monsterHealth -= tempScore;
            Message.message(context, "Health: " + monsterHealth + ", tempScore: " + tempScore);
            monsterHealthView.setText(monsterHealth + "");

            if( monsterHealth <= 0 && monsterCheckout == "master" || monsterCheckout == "double"){
                Message.message(context, "Norm Win!");

            }else if( throwCount == 3 ){
                throwCount = 0;
                // Bedingungen
            }
        }

        tempScore = 0;
    }

    private static Boolean checkOutPossible(int checkOutType){
        switch(checkOutType){
            case 1:
                int tempVal = 0;
                for(int i = 0; i < 10; i++){
                    switch(i){
                        case 0: tempVal = 20; break;
                        //TODO Werte hinzufügen
                    }

                    if( (tempVal-monsterHealth) == 0 ){
                        return true;
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

    private static class ScoreDialAdapter extends BaseAdapter {

        int[] result;
        Context context;
        private LayoutInflater inflater = null;

        public ScoreDialAdapter(CombatActivity combatActivity, int[] list) {
            result = list;
            context = combatActivity;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            final Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.gridview_combat_left_panel, null);
            holder.tv = (TextView) rowView.findViewById(R.id.gridView_combat_textView);
            holder.tv.setText(result[position] + "");

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    combat(result[position], scoreMultiplier);
                }
            });

            return rowView;
        }

        public class Holder {
            TextView tv;
        }
    }

    private static class ScoreMultiAdapter extends BaseAdapter {

        int[] result;
        Context context;
        private LayoutInflater inflater = null;

        public ScoreMultiAdapter(CombatActivity combatActivity, int[] list) {
            result = list;
            context = combatActivity;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            final Holder holder = new Holder();
            View rowView = convertView;

            if(rowView == null){

                if (position < 6) {
                    rowView = inflater.inflate(R.layout.gridview_combat_right_panel, null);
                    holder.tv = (TextView) rowView.findViewById(R.id.gridView_combat_right_panel_textView);
                } else {
                    rowView = inflater.inflate(R.layout.gridview_combat_left_panel, null);
                    holder.tv = (TextView) rowView.findViewById(R.id.gridView_combat_textView);
                }

                holder.tv.setText(iconArray[position]);
            }

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //rowView.setBackgroundColor(context.getResources().getColor(R.color.highlight_cherryred));


                    if (selectedCellPosition != -1) {
                        View cellView = parent.getChildAt(selectedCellPosition);
                        cellView.setSelected(false);
                        cellView.postInvalidate();
                    }


                    v.setSelected(true);
                    selectedCellPosition = position;

                    switch (position) {
                        case 0:
                            scoreMultiplier = 1;
                            break;
                        case 1:
                            // Klasse mit 1 = Primärangriff
                            heroClassActive = 1;
                            break;
                        case 2:
                            scoreMultiplier = 2;
                            break;
                        case 3:
                            // Klasse mit 2 = Sekundärangriff
                            heroClassActive = 2;
                            break;
                        case 4:
                            scoreMultiplier = 3;
                            break;
                        case 5:
                            // Klasse mit 3 = Spezialangriff
                            heroClassActive = 3;
                            break;
                        case 6:
                            scoreMultiplier = 1;
                            break;
                        case 7:
                            break;
                        case 8:
                            scoreMultiplier = 1;
                            break;
                        case 9:
                            break;
                        default:
                            holder.tv.setText(":(");
                    }
                }
            });

            return rowView;
        }

        public class Holder {
            TextView tv;
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

    private void initializeValues(){
        heroList = new ArrayList<>();
        monsterList = new ArrayList<>();
        selectedItems = new ArrayList<>();

        itemHelper = new DBplayerItemsAdapter(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String heroName = b.getString("HEROES_NAME", "???");
            String heroPrimaryClass = b.getString("HEROES_PRIMARY_CLASS", "???");
            String heroSecondaryClass = b.getString("HEROES_SECONDARY_CLASS", "???");
            int heroHitpoints = b.getInt("HEROES_HITPOINTS", -3);
            int heroCosts = b.getInt("HEROES_COSTS", -1);
            String heroImgRes = b.getString("IMAGE_RESOURCE", "R.mipmap.hero_dummy_0");
            origin = b.getString("ORIGIN", "StartActivity");

            heroHitpointsConst = heroHitpoints;


            // .add für Anzahl der Helden sowie Anzahl der Monster
            heroList.add(new Hero(heroName, heroPrimaryClass, heroSecondaryClass, heroImgRes, heroHitpoints, heroCosts));
        }

        monsterList.add(new Monster());
        monsterHealthConst = monsterList.get(0).getInt("hp");

        heroNameView.setText(heroList.get(0).getStrings("heroName"));
        heroHitpointsView.setText(Integer.toString(heroList.get(0).getInts("hp")));
        monsterHealthView.setText(Integer.toString(monsterList.get(0).getInt("hp")));

        int[] numbersOfBoardList = new int[20];
        for (int i = 0; i < 20; i++) {
            numbersOfBoardList[i] = i + 1;
        }

        int[] specialSymbolsList = new int[10];
        for (int i = 0; i < 10; i++) {
            specialSymbolsList[i] = i + 1;
        }

        undoListForHero = new ArrayList<>();
        paramsBarMonsterDamaged.weight = 0.0f;
        healthBarMonsterDamaged.setLayoutParams(paramsBarMonsterDamaged);
        paramsBarHeroDamaged.weight = 0.0f;
        healthBarHeroDamaged.setLayoutParams(paramsBarHeroDamaged);
        gridViewNumbers.setAdapter(new ScoreDialAdapter(this, numbersOfBoardList));
        gridViewSpecials.setAdapter(new ScoreMultiAdapter(this, specialSymbolsList));
        heroProfileView.setImageResource(ImgRes.res(this, "hero", heroList.get(0).getStrings("imageResource")));
    }

    private void initializeViews() {
        gridViewNumbers = (GridView) findViewById(R.id.activity_combat_gridView_numbers);
        gridViewSpecials = (GridView) findViewById(R.id.activity_combat_gridView_specials);
        healthBarHeroVital = (ImageView) findViewById(R.id.healthbar_hero_vital);
        healthBarHeroDamaged = (ImageView) findViewById(R.id.healthbar_hero_damaged);
        paramsBarHeroDamaged = (LinearLayout.LayoutParams) healthBarHeroDamaged.getLayoutParams();
        paramsBarHeroVital = (LinearLayout.LayoutParams) healthBarHeroVital.getLayoutParams();
        heroNameView = (TextView) findViewById(R.id.heroNameView);
        monsterNameView = (TextView) findViewById(R.id.monsterNameView);
        eventsView = (TextView) findViewById(R.id.combat_events_log);
        monsterHealthView = (TextView) findViewById(R.id.monsterHealthView);
        healthbarMonsterVital = (ImageView) findViewById(R.id.healthbar_monster_vital);
        healthBarMonsterDamaged = (ImageView) findViewById(R.id.healthbar_monster_damaged);
        heroProfileView = (ImageView) findViewById(R.id.combat_hero_profile);
        heroHitpointsView = (TextView) findViewById(R.id.heroHealthView);
        chronicleView = (TextView) findViewById(R.id.textView_hitpoints_chronik);
        paramsBarMonsterDamaged = (LinearLayout.LayoutParams) healthBarMonsterDamaged.getLayoutParams();
        paramsBarMonsterVital = (LinearLayout.LayoutParams) healthbarMonsterVital.getLayoutParams();
    }
}