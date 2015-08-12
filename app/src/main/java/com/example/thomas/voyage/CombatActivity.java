package com.example.thomas.voyage;

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

import java.util.ArrayList;
import java.util.List;

public class CombatActivity extends Activity {

    private static final int monsterHealthConst = 500;
    private static boolean gridInitialized = true;
    private static int monsterHealth = 500,
            scoreMultiplier = 1,
            scoreMultiplier1 = 1,scoreMultiplier2 = 1,scoreMultiplier3 = 1,
            scoreField1 = 0,scoreField2 = 0,scoreField3 = 0,
            dartCount = 1;
    private static String finishMultiplier = "UniversalSingle",
            FinishMultiplier1 = "UniversalSingle",
            finishMultiplier2 = "UniversalSingle",
            finishMultiplier3 = "UniversalSingle",
            image = "";
    private static String[] iconArray = {"X 1", "1.", "X 2", "2.", "X 3", "SP", "BULL", "IN", "EYE", "OUT"};
    private static TextView monsterHealthView,
            heroHealthView,
            heroNameView,
            monsterNameView,
            eventsView;
    private static ImageView healthbarMonsterVital,
            healthBarMonsterDamaged,
            healthBarHeroVital,
            healthBarHeroDamaged,
            heroProfileView;
    private static List<Integer> undoListForHero;
    private static LinearLayout.LayoutParams paramsBarMonsterDamaged,
            paramsBarMonsterVital,
            paramsBarHeroDamaged,
            paramsBarHeroVital;
    private static String heroName = "Ritter Namenlos",
            heroPrimaryClass = "",
            heroSecondaryClass = "";
    private static int heroHitpoints = -1, heroCosts = -1;
    private static int heroHitpointsConst;
    private GridView gridViewNumbers, gridViewSpecials;

    private static void setHealthBarMonster() {

        try {
            if (monsterHealth > 0) {

                float x = monsterHealthConst * (float) 0.01;

                float weightRounded = monsterHealth / x * (float) 0.01;

                monsterHealthView.setText(monsterHealth + "");
                paramsBarMonsterDamaged.weight = 1 - weightRounded;
                paramsBarMonsterVital.weight = weightRounded;
                healthBarMonsterDamaged.setLayoutParams(paramsBarMonsterDamaged);
                healthbarMonsterVital.setLayoutParams((paramsBarMonsterVital));

            } else {
                paramsBarMonsterDamaged.weight = 1.0f;
                paramsBarMonsterVital.weight = 0f;
                healthBarMonsterDamaged.setLayoutParams(paramsBarMonsterDamaged);
                healthbarMonsterVital.setLayoutParams((paramsBarMonsterVital));
                monsterHealthView.setText("DU GEWINNER!");
                monsterHealth = monsterHealthConst;
            }
        } catch (ArithmeticException a) {
            Log.e("ARITHMETICH EXCEPTION", a + "");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        hideSystemUI();
        initializeViews();

        monsterHealth = monsterHealthConst;

        Bundle b = getIntent().getExtras();
        if(b != null){
            heroName = b.getString("HEROES_NAME", "???");
            heroPrimaryClass = b.getString("HEROES_PRIMARY_CLASS", "???");
            heroSecondaryClass = b.getString("HEROES_SECONDARY_CLASS", "???");
            heroHitpoints = b.getInt("HEROES_HITPOINTS", -1);
            heroCosts= b.getInt("HEROES_COSTS", -1);
            image = b.getString("IMAGE_RESOURCE", "R.mipmap.hero_dummy_0");

            heroHitpointsConst = heroHitpoints;
        }

        undoListForHero = new ArrayList<>();

        int[] numbersOfBoardList = new int[20];
        for (int i = 0; i < 20; i++) {
            numbersOfBoardList[i] = i + 1;
        }

        int[] specialSymbolsList = new int[10];
        for (int i = 0; i < 10; i++){
            specialSymbolsList[i] = i+1 ;
        }

        heroNameView.setText(heroName);
        monsterHealthView.setText(Integer.toString(monsterHealth));

        paramsBarMonsterDamaged = (LinearLayout.LayoutParams) healthBarMonsterDamaged.getLayoutParams();
        paramsBarMonsterVital = (LinearLayout.LayoutParams) healthbarMonsterVital.getLayoutParams();




        paramsBarMonsterDamaged.weight = 0.0f;
        healthBarMonsterDamaged.setLayoutParams(paramsBarMonsterDamaged);


        paramsBarHeroDamaged.weight = 0.0f;
        healthBarHeroDamaged.setLayoutParams(paramsBarHeroDamaged);

        gridViewNumbers.setAdapter(new NumbersAdapter(this, numbersOfBoardList));
        gridViewSpecials.setAdapter(new RightPanelAdapter(this, specialSymbolsList));


        heroProfileView.setImageResource(ImgRes.res(this, "hero", image));
    }

    /*
        private static void setHealthBarHero() {

            try {
                if (heroHitpoints > 0) {

                    float x = heroHitpointsConst * (float) 0.01;

                    float weightRounded = heroHitpoints / x * (float) 0.01;

                    monsterHealthView.setText(monsterHealth + "");
                    paramsBarMonsterDamaged.weight = 1 - weightRounded;
                    paramsBarMonsterVital.weight = weightRounded;
                    healthBarMonsterDamaged.setLayoutParams(paramsBarMonsterDamaged);
                    healthbarMonsterVital.setLayoutParams((paramsBarMonsterVital));

                } else {
                    paramsBarMonsterDamaged.weight = 1.0f;
                    paramsBarMonsterVital.weight = 0f;
                    healthBarMonsterDamaged.setLayoutParams(paramsBarMonsterDamaged);
                    healthbarMonsterVital.setLayoutParams((paramsBarMonsterVital));
                    monsterHealthView.setText("DU GEWINNER!");
                    monsterHealth = monsterHealthConst;
                }
            } catch (ArithmeticException a) {
                Log.e("ARITHMETICH EXCEPTION", a + "");
            }

        }
    */
    public void activityCombatBackToMain(View view) {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
        finish();
    }

    public void undoButtonClicked(View view){
        int size = undoListForHero.size();

        if (size > 0) {
            monsterHealth += undoListForHero.get(size - 1);
            setHealthBarMonster();
            undoListForHero.remove(size - 1);
            monsterHealthView.setText(Integer.toString(monsterHealth));
        }
    }

    public void missButtonClicked(View view){
        if (dartCount == 1) {
            scoreMultiplier1 = 1;
            scoreField1 = 0;
            dartCount++;

        } else if (dartCount == 2) {
            scoreMultiplier2 = 1;
            scoreField2 = 0;
            dartCount++;

        } else if (dartCount == 3) {
            scoreMultiplier3 = 1;
            scoreField2 = 0;
            dartCount = 1;

            monsterHealth -= (scoreMultiplier1 * scoreField1 + scoreMultiplier2 * scoreField2 + scoreMultiplier3 * scoreField3);

            undoListForHero.add(scoreMultiplier1 * scoreField1 + scoreMultiplier2 * scoreField2 + scoreMultiplier3 * scoreField3);
            setHealthBarMonster();
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

    public void initializeViews() {
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
        heroHealthView = (TextView) findViewById(R.id.heroHealthView);
    }

  /*  public void setOneThrow(int i){

        if(dartCount < 3){

            undoListForHero.add(i * scoreMultiplier);
            dartCount++;

        }else{

            dartCount = 1;
            int size = undoListForHero.size();

            for(int i = 1; i <= 3; i++){
                monsterHealth -= undoListForHero.get(size - i);
            }

        }


        if (dartCount == 1) {
            scoreMultiplier1 = scoreMultiplier;
            scoreField1 = result[position];
            dartCount++;
        } else if (dartCount == 2) {
            scoreMultiplier2 = scoreMultiplier;
            scoreField2 = result[position];
            dartCount++;
        } else if (dartCount == 3) {
            scoreMultiplier3 = scoreMultiplier;
            scoreField3 = result[position];
            dartCount = 1;

            monsterHealth -= (scoreMultiplier1 * scoreField1 + scoreMultiplier2 * scoreField2 + scoreMultiplier3 * scoreField3);

            undoListForHero.add(scoreMultiplier1 * scoreField1 + scoreMultiplier2 * scoreField2 + scoreMultiplier3 * scoreField3);
            setHealthBarMonster();
        }
    }*/

    private static class NumbersAdapter extends BaseAdapter {

        int[] result;
        int currentSelectedPosition = 0, lastSelectedPosition = 0;
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

                    if (dartCount == 1) {
                        scoreMultiplier1 = scoreMultiplier;
                        scoreField1 = result[position];
                        dartCount++;

                    } else if (dartCount == 2) {
                        scoreMultiplier2 = scoreMultiplier;
                        scoreField2 = result[position];
                        dartCount++;

                    } else if (dartCount == 3) {
                        scoreMultiplier3 = scoreMultiplier;
                        scoreField3 = result[position];
                        dartCount = 1;

                        monsterHealth -= (scoreMultiplier1 * scoreField1 + scoreMultiplier2 * scoreField2 + scoreMultiplier3 * scoreField3);

                        undoListForHero.add(scoreMultiplier1 * scoreField1 + scoreMultiplier2 * scoreField2 + scoreMultiplier3 * scoreField3);
                        setHealthBarMonster();
                    }
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
        private int selectedCellPosition = 0;


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

                        rowView = inflater.inflate(R.layout.gridview_combat_right_panel, null);
                        holder.tv = (TextView) rowView.findViewById(R.id.gridView_combat_right_panel_textView);


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

                            break;
                        case 2:
                            scoreMultiplier = 2;
                            break;
                        case 3:
                            break;
                        case 4:
                            scoreMultiplier = 3;
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:
                            break;
                        case 8:
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





