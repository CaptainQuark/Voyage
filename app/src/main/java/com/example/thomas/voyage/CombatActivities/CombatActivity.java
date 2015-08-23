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

import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Monster;
import com.example.thomas.voyage.Databases.DBplayerItemsAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ImgRes;
import com.example.thomas.voyage.BasicActivities.StartActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        hideSystemUI();
        initializeViews();
        initializeValues();

        startRound();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    private static void startRound(){
        roundCount++;

        // TODO Wenn Monster durch Fähigkeit beginnt, dann hier.
        // TODO Danach wird der bool-Wert auf 'false' gesetzt.
        // TODO Ansonsten passiert in 'startRound' nichts mehr und Held beginnt.
    }

    private static void setHealthBarMonster() {
        int monsterHealth = monsterList.get(0).getInt("hp");

        try {
            if (monsterHealth > 0) {

                float x = monsterHealthConst * (float) 0.01;

                float weightRounded = monsterHealth / x * (float) 0.01;

                monsterHealthView.setText(monsterHealth + "");
                paramsBarMonsterDamaged.weight = 1 - weightRounded;
                paramsBarMonsterVital.weight = weightRounded;
                healthBarMonsterDamaged.setLayoutParams(paramsBarMonsterDamaged);
                healthbarMonsterVital.setLayoutParams(paramsBarMonsterVital);

            } else {
                paramsBarMonsterDamaged.weight = 1.0f;
                paramsBarMonsterVital.weight = 0f;
                healthBarMonsterDamaged.setLayoutParams(paramsBarMonsterDamaged);
                healthbarMonsterVital.setLayoutParams((paramsBarMonsterVital));
                monsterHealthView.setText("DU GEWINNER!");
                monsterList.get(0).setInt("hp", monsterHealthConst);
            }
        } catch (ArithmeticException a) {
            Log.e("ARITHMETIC EXCEPTION", a + "");
        }
    }

    private static void setHealthBarHero() {
        int heroHitpoints = heroList.get(0).getInts("hp");

        try {
            if (heroHitpoints > 0) {

                float x = heroHitpointsConst * (float) 0.01;

                float weightRounded = heroHitpoints / x * (float) 0.01;

                heroHitpointsView.setText(heroHitpoints + "");
                paramsBarHeroDamaged.weight = 1 - weightRounded;
                paramsBarHeroVital.weight = weightRounded;
                healthBarHeroDamaged.setLayoutParams(paramsBarHeroDamaged);
                healthBarHeroVital.setLayoutParams((paramsBarHeroVital));

            } else {
                paramsBarHeroDamaged.weight = 1.0f;
                paramsBarHeroVital.weight = 0f;
                healthBarHeroDamaged.setLayoutParams(paramsBarHeroDamaged);
                healthBarHeroVital.setLayoutParams((paramsBarHeroVital));
                heroHitpointsView.setText("DU GEWINNER!");
                heroList.get(0).setInt("hp", heroHitpointsConst);
            }
        } catch (ArithmeticException a) {
            Log.e("ARITHMETIC EXCEPTION", a + "");
        }
    }

    private static void handleAttackInputByHero(int result) {
        scoreHeroMultiplierArray[dartCount - 1] = scoreMultiplier;
        scoreHeroFieldArray[dartCount - 1] = result;

        if (dartCount == 3) {
            int appliedDamage = 0;
            for (int i = 0; i < 3; i++) {
                appliedDamage += scoreHeroMultiplierArray[i] * scoreHeroFieldArray[i];
            }

            switch (heroClassActive) {
                case 0:
                    handlePrimaryHeroClass objPrime = new handlePrimaryHeroClass();
                    objPrime.applyEffects();
                    break;
                case 1:
                    handleSecondaryHeroClass objSec = new handleSecondaryHeroClass();
                    //objSec.applyEffects();
                    break;
                case 2:
                    handleSpecialAttackHeroClass objSpecial = new handleSpecialAttackHeroClass();
                    //objSpecial.applyEffects();
                    break;
                default:
                    Log.e("-- ERROR - ", " @ 'handleAttackInputByHero");
                    break;
            }



            monsterList.get(0).setInt("hp", monsterList.get(0).getInt("hp") - appliedDamage);
            undoListForHero.add(scoreHeroMultiplierArray[dartCount - 1] * scoreHeroFieldArray[dartCount - 1]);
            /*
            int size = undoListForHero.size();
            String chronicle = "";

            for(int i = dartCount; i >= 1; i--){
                chronicle += undoListForHero.get(size - i);
                if(i != 1) chronicle += '|';
                chronicleView.setText(chronicle);
            }
            */

            eventString += "Der Held attackiert mit " + appliedDamage + " TP\n";

            handleAttackInputByMonster();
            handleSelectedItem();
            setHealthBarHero();

            heroHitpointsView.setText(Integer.toString(heroList.get(0).getInts("hp")));

            chronicleString += ( (undoListForHero.get(dartCount-1)) + "");
            chronicleView.setText(chronicleString);
            dartCount = 1;
            roundCount++;

            eventsView.setText(eventString);

            for(int i = 0; i < scoreHeroFieldArray.length; i++){
                scoreHeroFieldArray[i] = -1;
            }

            for(int i = 0; i < scoreHeroMultiplierArray.length; i++){
                scoreHeroMultiplierArray[i] = -1;
            }

            chronicleString = "";
            chronicleView.setText(chronicleString);
            undoListForHero = new ArrayList<>();

        } else {

            undoListForHero.add( scoreHeroMultiplierArray[dartCount-1] * scoreHeroFieldArray[dartCount-1] );
            handleSelectedItem();
            chronicleString += ( undoListForHero.get(dartCount-1) + "   |   ");
            chronicleView.setText(chronicleString);
            dartCount++;
        }
    }

    private static void handleAttackInputByMonster(){

        /*

        Monster greift erst nach 3. Wurf des Spielers an (dartCount == 3).

        Gewähltes Item wird nach dieser Funktion in 'handleAttackInputByHero' aufgerufen,
        da diese Funktion dort gerufen wird.

        Healthbar des Monsters wird erst am Ende der Funktion gesetzt, da zuvor durch
        Effekte des Monsters der zugefügte Schaden verändert werden kann

         */

        heroList.get(0).setInt("hp", heroList.get(0).getInts("hp") - 5);

        eventString += "Das Monster attackiert mit 5 TP\n";
        eventsView.setText(eventString);

        setHealthBarMonster();
    }

    public void activityCombatBackToMain(View view) {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
        finish();
    }

    public void undoButtonClicked(View view){
        int size = undoListForHero.size();

        if (size > 0) {

            if(dartCount == 3){
                monsterList.get(0).setInt("hp", monsterList.get(0).getInt("hp") + undoListForHero.get(size - 1));
                setHealthBarMonster();
                monsterHealthView.setText(Integer.toString(monsterList.get(0).getInt("hp")));
            }

            undoListForHero.remove(size - 1);
            chronicleString = "";
            dartCount--;

            for(int i = 0; i < undoListForHero.size(); i++){
                chronicleString += scoreHeroMultiplierArray[i] * scoreHeroFieldArray[i] + "   |   ";
            }

            chronicleView.setText(chronicleString);
        }
    }

    public void missButtonClicked(View view) {
        handleAttackInputByHero(0);
    }

    private static class NumbersAdapter extends BaseAdapter {

        int[] result;
        Context context;
        private LayoutInflater inflater = null;

        public NumbersAdapter(CombatActivity combatActivity, int[] list) {
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

                    handleAttackInputByHero(result[position]);
                }
            });

            return rowView;
        }

        public class Holder {
            TextView tv;
        }
    }

    private static class RightPanelAdapter extends BaseAdapter {

        int[] result;
        Context context;
        private LayoutInflater inflater = null;

        public RightPanelAdapter(CombatActivity combatActivity, int[] list) {
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
                            handleAttackInputByHero(25);
                            break;
                        case 7:
                            break;
                        case 8:
                            scoreMultiplier = 1;
                            handleAttackInputByHero(50);
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

    private static class handlePrimaryHeroClass {

        /*
        USAGE:

        1.
            Die Mulitplier können über 'scoreHeroMultiplierArray[i]' angesprochen werden,
            wobei 'i' von 0 - 2 geht und die Zeiteinheit des Angriffs ist (i = 0 ist erster Angriff,...)

            Die Score-Felder können über 'scoreHeroFieldArray[i] angesprochen werden.
            Funktionieren so wie die Multiplier.

        2.
            Die Effekte werden vor dem Aufruf dieses Objekts berechnet und gespeichert,
            aber noch nicht sichtbar gemacht. Auf jeden Score und Multiplier kann also
            noch nachträglich zugegriffen werden.

        3.
            Es gibt noch keine Runden, da kein Angriff des Monsters berechnet wird.
            Es gibt noch nicht die Möglichkeit, zukünftige Berechnungen zu beeinflussen.

         */

        public void applyEffects() {

            switch (heroList.get(0).getStrings("classPrimary")) {

                default:
                    // (noch) kein Effect
                    break;
            }
        }

    }

    private static class handleSecondaryHeroClass {

        public void applyEffects() {

            switch (heroList.get(0).getStrings("classSecondary")) {

                default:
                    // (noch) kein Effect
                    break;
            }
        }
    }

    private static class handleSpecialAttackHeroClass {

    }

    private void checkForSelectedItems(){

        // Arbeite jedes Item in Liste der ausgewählten Items anhand der Fähigkeiten-ID ab
        // case 1: ... etc.

        // Überprüfe, ob Item aus AusgewähltenListe und/oder Datenbank gelöscht wird
    }

    private static void handleSelectedItem(){

        for(int pos = 0; pos < selectedItems.size(); pos++){

            switch (itemHelper.getItemSkillsId(selectedItems.get(pos))){

                default:
                    Log.e("DEFAULT", "default called @ handleSelectedItem");
                    break;
            }
        }

        // Sollte kein Item ausgewählt sein, wird die 'for'-Schleife einfach übsprüngen
        // und keine Effekte angewandt
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
        gridViewNumbers.setAdapter(new NumbersAdapter(this, numbersOfBoardList));
        gridViewSpecials.setAdapter(new RightPanelAdapter(this, specialSymbolsList));
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







// CODE 2 LEARN

// MODULUS BEI GRIDVIEW, BESTIMMTE ELEMENT ZU MARKIEREN
/*if ((((result[position] - posCorrectionModFive) % (5 * (posCorrectionModFive + 1)) == 0))) {

                posCorrectionModFive++;
                rowView = inflater.inflate(R.layout.gridview_combat_right_panel, null);
                holder.tv = (ToggleButton) rowView.findViewById(R.id.gridView_combat_right_panel_textView);

            } else if ((((result[position]) % 6) == 0)) {

                rowView = inflater.inflate(R.layout.gridview_combat_right_panel, null);
                holder.tv = (ToggleButton) rowView.findViewById(R.id.gridView_combat_right_panel_textView);

            }else{

                rowView = inflater.inflate(R.layout.gridview_combat, null);
                holder.tv = (TextView) rowView.findViewById(R.id.gridView_combat_textView);
                holder.tv.setText(result[position] + "");

            }*/





