package com.example.thomas.voyage.BasicActivities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thomas.voyage.CombatActivities.CombatActivity;
import com.example.thomas.voyage.CombatActivities.PrepareCombatActivity;
import com.example.thomas.voyage.CombatActivities.WorldMapQuickCombatActivity;
import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Fragments.ClassicWorkoutFragment;
import com.example.thomas.voyage.Fragments.HeroAllDataCardFragment;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;

public class HeroCampActivity extends Activity implements HeroAllDataCardFragment.onHeroAllDataCardListener{

    private GridView heroGridView;
    private DBheroesAdapter h;
    private TextView slotsView, healView, toFightView, sellView, fortuneView;
    private List<Integer> dbIndexForHeroList;
    private HeroAllDataCardFragment heroAllDataCardFragment;

    // 'heroList' wird erstellt, um Datenbank-Einträge zwischen zu speichern
    // -> Scrollen in GridView bleibt flüssig
    private List<Hero> heroList;
    //private List<Integer> heroToDatabaseList;
    private String origin = "";
    private boolean somethingSelected = true;

    // Index des GridView, für Datenbankzugriff über
    // 'UID' muss immer +1 gerechnet werden (DB beginnt bei 1)
    private int lastSelectedHeroIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes_camp);
        hideSystemUI();
        ConstRes c = new ConstRes();

        Bundle b = getIntent().getExtras();
        if(b != null){
            origin = b.getString(c.ORIGIN, "StartActivity");
        }

        h = new DBheroesAdapter(this);
        dbIndexForHeroList = new ArrayList<>();
        heroList = new ArrayList<>();

        for(int i = 1; i <= h.getTaskCount(); i++){
            if(!h.getHeroName(i).equals(c.NOT_USED)){
                dbIndexForHeroList.add(i);
                heroList.add(new Hero(
                        h.getHeroName(i),
                        h.getHeroPrimaryClass(i),
                        h.getHeroSecondaryClass(i),
                        h.getHeroImgRes(i),
                        h.getHeroHitpoints(i),
                        h.getHeroHitpointsTotal(i),
                        h.getHeroCosts(i),
                        h.getEvasion(i)
                ));
            }
        }

        heroGridView = (GridView) findViewById(R.id.heroes_camp_gridview);
        heroGridView.setAdapter(new HeroImagesAdapter(this));

        heroGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                somethingSelected = lastSelectedHeroIndex != position;
                lastSelectedHeroIndex = (somethingSelected = !somethingSelected) ? -1 : position;

                setToolbarViews();
                heroGridView.invalidateViews();
            }
        });

        slotsView = (TextView) findViewById(R.id.textview_camp_slots);
        healView = (TextView) findViewById(R.id.textview_camp_heal_hero);
        toFightView = (TextView) findViewById(R.id.textview_camp_to_fight);
        sellView = (TextView) findViewById(R.id.textview_camp_dismiss_hero);
        fortuneView = (TextView) findViewById(R.id.textview_camp_fortune);
        setSlotsView();

        SharedPreferences prefs = getSharedPreferences("CURRENT_MONEY_PREF", Context.MODE_PRIVATE);
        fortuneView.setText("$ " + prefs.getLong("currentMoneyLong", -1));
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        heroGridView.invalidateViews();
        setSlotsView();
        hideSystemUI();
    }



    /*

    onClick-Events

     */



    public void heroesCampBackButton(View v){
        super.onBackPressed();
        finish();
    }

    public void campHealHero(View v){

        if(lastSelectedHeroIndex == -1){
            Msg.msg(this, "No hero selected");

        }else if(h.getHeroHitpoints(lastSelectedHeroIndex+1) == h.getHeroHitpointsTotal(lastSelectedHeroIndex+1)){
            Msg.msg(this, "Your hero doesn't need any medication");

        }else{
            int slotIndex = -1;
            Bundle b = getIntent().getExtras();
            if(b != null){ slotIndex = b.getInt("SLOT_INDEX", -1); }

            // nächsten freien Slot in HospitalActivity finden
            if(slotIndex == -1){
                int[] slotsArray = {1,1,1};

                for(int i = 1; i <= h.getTaskCount(); i++){
                    switch (h.getMedSlotIndex(i)){
                        case 0:
                            slotsArray[0] = -1;
                            break;
                        case 1:
                            slotsArray[1] = -1;
                            break;
                        case 2:
                            slotsArray[2] = -1;
                            break;
                    }
                }

                for(int i = 0; i < slotsArray.length; i++){
                    if(slotsArray[i] == 1){
                        slotIndex = i;
                        i = slotsArray.length;
                    }
                }

                if(slotIndex == -1) Msg.msg(this, "No free medic available");
            }

            if(!h.updateMedSlotIndex(lastSelectedHeroIndex+1, slotIndex)) Msg.msg(this, "ERROR @ campHealHero : updateMedSlotIndex");

            if(slotIndex >= 0 && slotIndex <= 2){
                putFragmentToSleep();
                SharedPreferences prefs = getSharedPreferences("CURRENT_MONEY_PREF", Context.MODE_PRIVATE);
                int costs = ((heroList.get(lastSelectedHeroIndex).getHpTotal() - (heroList.get(lastSelectedHeroIndex).getHp())) / (heroList.get(lastSelectedHeroIndex).getHpTotal()) * 100 + 1) * 100;
                long money = prefs.getLong("currentMoneyLong", -1) - costs;

                prefs.edit().putLong("currentMoneyLong", money).apply();
                fortuneView.setText("$ " + money);

                /*
                float diff = 1 / (heroList.get(lastSelectedHeroIndex).getHpTotal() / heroList.get(lastSelectedHeroIndex).getHp());
                long money = prefs.getLong("currentMoneyLong", -1) - ((long) ((1-diff)* heroList.get(lastSelectedHeroIndex).getCosts()) );
                */

                if(money >= 0){
                    prefs.edit().putLong("currentMoneyLong", money);
                    Intent i = new Intent(this, HospitalActivity.class);
                    startActivity(i);
                    finish();
                } else
                    Msg.msg(this, "Not enough money, boy!");

            }else{
                Msg.msg(this, "ERROR @ campHealHero : invalid slotIndex");
            }
        }
    }

    public void commitToQuest(View v){
        Intent i;

        if(lastSelectedHeroIndex != -1 && h.getMedSlotIndex(lastSelectedHeroIndex+1) == -1){
            switch (origin){
                case "PrepareCombatActivity":
                    putFragmentToSleep();
                    i = new Intent(getApplicationContext(), PrepareCombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;

                case "WorldMapQuickCombatActivity":
                    putFragmentToSleep();
                    i = new Intent(getApplicationContext(), WorldMapQuickCombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;

                default:
                    putFragmentToSleep();
                    i = new Intent(getApplicationContext(), CombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;
            }

        }else{
            Msg.msg(this, "Commit To Quest? Not possible now...");
        }
    }

    public void dismissHero(View v){
        if((lastSelectedHeroIndex != -1 && h.getMedSlotIndex(lastSelectedHeroIndex+1) == -1)){

            SharedPreferences prefs = getSharedPreferences("CURRENT_MONEY_PREF", Context.MODE_PRIVATE);
            long money = prefs.getLong("currentMoneyLong", -1) + h.getHeroCosts(lastSelectedHeroIndex+1);
            fortuneView.setText("$ " + money);

            // Annahme: 'costs' wurde bereits nach Kauf des Helden abgewertet und entspricht jetzt dem akutellen Verkaufswert
            prefs.edit().putLong("currentMoneyLong", money).apply();

            h.markOneRowAsUnused(lastSelectedHeroIndex + 1);
            dbIndexForHeroList.remove(lastSelectedHeroIndex);
            heroList.remove(lastSelectedHeroIndex);

            lastSelectedHeroIndex = -1;
            setSlotsView();
            setToolbarViews();
            putFragmentToSleep();
            heroGridView.invalidateViews();
        }else{
            Msg.msgShort(this, "No fight now.");
        }
    }



    /*

    Funktionen

     */



    public void passHeroesParameterstoNewActivity(Intent i){
        ConstRes co = new ConstRes();

        if(lastSelectedHeroIndex != -1){

             /*

             Um die Funktion nutzen zu können, muss ein Held ausgewählt sein -> für Kämpfe gedacht

            */

            i.putExtra(co.HEROES_NAME, heroList.get(lastSelectedHeroIndex).getHeroName());
            i.putExtra(co.HEROES_PRIMARY_CLASS, heroList.get(lastSelectedHeroIndex).getClassPrimary());
            i.putExtra(co.HEROES_SECONDARY_CLASS,heroList.get(lastSelectedHeroIndex).getClassSecondary());
            i.putExtra(co.HEROES_HITPOINTS, heroList.get(lastSelectedHeroIndex).getHp());
            i.putExtra(co.HEROES_COSTS, heroList.get(lastSelectedHeroIndex).getCosts());
            i.putExtra(co.IMAGE_RESOURCE, heroList.get(lastSelectedHeroIndex).getImageResource());
            i.putExtra("EVASION", heroList.get(lastSelectedHeroIndex).getEvasion());
        }

        i.putExtra(co.ORIGIN, "HeroCampActivity");
    }

    private void setSlotsView(){
        slotsView.setText(heroList.size() + " / " + h.getTaskCount());
    }

    private void setToolbarViews(){
        if(lastSelectedHeroIndex == -1){
            sellView.setTextColor(Color.parseColor("#707070"));
            toFightView.setTextColor(Color.parseColor("#707070"));
            healView.setTextColor(Color.parseColor("#707070"));
        }
        else {
            sellView.setTextColor(Color.WHITE);
            toFightView.setTextColor(Color.WHITE);

            SharedPreferences prefs = getSharedPreferences("CURRENT_MONEY_PREF", Context.MODE_PRIVATE);
            float diff = 1 / (heroList.get(lastSelectedHeroIndex).getHpTotal() / heroList.get(lastSelectedHeroIndex).getHp());
            long money = prefs.getLong("currentMoneyLong", -1) - ((long) ((1-diff)* heroList.get(lastSelectedHeroIndex).getCosts()) );

            if(h.getHeroHitpoints(lastSelectedHeroIndex+1) == h.getHeroHitpointsTotal(lastSelectedHeroIndex+1))
                healView.setTextColor(Color.parseColor("#707070"));
            else if(money < 0){
                healView.setTextColor(Color.parseColor("#707070"));
            }
            else healView.setTextColor(Color.WHITE);
        }
    }



    /*

    Fragment-Listener

     */



    @Override
    public void putFragmentToSleep() {
        if(heroAllDataCardFragment != null){
            getFragmentManager().beginTransaction().remove(heroAllDataCardFragment).commit();
            lastSelectedHeroIndex = -1;
            setToolbarViews();
        }
    }



    /*

    Adapter

     */



    public class HeroImagesAdapter extends BaseAdapter {
        private Context mContext;

        public HeroImagesAdapter(Context c) {
            mContext = c;
        }

        public int getCount() { return heroList.size(); }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // 'ViewHolder' ist ressourcenschonend, wodurch die GridView
        // schnell reagieren kann (Scrollen) -> Views können im Speicher abgelegt werden
        // und müssen nicht jedes Mal neu zugewiesen werden
        class ViewHolder {

            private TextView nameView,classesView, hpView, costsView, evasionView, hospitalCostsView;
            private ImageView profileView;
            private LinearLayout rightPanelLayout;

            ViewHolder(View v) {
                nameView = (TextView) v.findViewById(R.id.textView_camp_card_name);
                classesView = (TextView) v.findViewById(R.id.textView_camp_card_prim_and_sec);
                hpView = (TextView) v.findViewById(R.id.textView_camp_card_hp);
                costsView = (TextView) v.findViewById(R.id.textView_camp_card_costs);
                hospitalCostsView = (TextView) v.findViewById(R.id.textView_camp_card_hospital_costs);
                evasionView = (TextView) v.findViewById(R.id.textView_camp_card_evasion);
                profileView = (ImageView) v.findViewById(R.id.imageView_camp_card_profile);
                rightPanelLayout = (LinearLayout) v.findViewById(R.id.layout_camp_right_panel);
            }
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.hero_camp_card, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.profileView.setImageResource(getApplicationContext().getResources().getIdentifier(heroList.get(position).getImageResource(), "mipmap", getPackageName()));
            holder.nameView.setText(String.valueOf(heroList.get(position).getHeroName()));
            holder.classesView.setText(heroList.get(position).getClassPrimary() + " und " + heroList.get(position).getClassSecondary());
            holder.hpView.setText(heroList.get(position).getHp() + " / " + heroList.get(position).getHpTotal());
            holder.costsView.setText(String.valueOf(heroList.get(position).getCosts()));
            holder.evasionView.setText(String.valueOf(heroList.get(position).getEvasion()));

            if(lastSelectedHeroIndex == position && !somethingSelected){
                holder.rightPanelLayout.setBackgroundColor(Color.WHITE);

            }else{
                holder.rightPanelLayout.setBackgroundColor(Color.parseColor("#FFA8A8A8"));
            }

            if(h.getMedSlotIndex(position+1) != -1) {
                holder.hospitalCostsView.setText("IN HEILUNG");
                holder.hospitalCostsView.setTextColor(Color.WHITE);
            }
            else {
                if(heroList.get(position).getHpTotal() == heroList.get(position).getHpTotal())
                    holder.hospitalCostsView.setText("-");
                else
                    holder.hospitalCostsView.setText("$ " + ((heroList.get(position).getHpTotal() - (heroList.get(position).getHp())) / (heroList.get(position).getHpTotal()) * 100 + 1) * 100);

                holder.hospitalCostsView.setTextColor(Color.BLACK);
            }

            holder.profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedHeroIndex = position;

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    heroAllDataCardFragment = new HeroAllDataCardFragment();
                    Bundle b = new Bundle();
                    b.putInt("DB_INDEX", dbIndexForHeroList.get(lastSelectedHeroIndex));

                    heroAllDataCardFragment.setArguments(b);
                    fragmentTransaction.add(R.id.layout_camp_fragment_container, heroAllDataCardFragment);
                    fragmentTransaction.commit();

                    setToolbarViews();
                }
            });

            holder.profileView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    heroList.get(position).setHp(heroList.get(position).getHp() - 20);
                    h.updateHeroHitpoints(position + 1, heroList.get(position).getHp());
                    heroGridView.invalidateViews();
                    setToolbarViews();
                    return false;
                }
            });

            return convertView;
        }
    }



    /*

    Funktionen zur Auslagerung

     */



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
