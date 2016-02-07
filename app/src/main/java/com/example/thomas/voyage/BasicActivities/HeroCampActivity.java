package com.example.thomas.voyage.BasicActivities;

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
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;

public class HeroCampActivity extends Activity {

    private GridView heroGridView;
    private DBheroesAdapter h;
    private TextView slotsView, healView, toFightView, sellView;

    // 'heroList' wird erstellt, um Datenbank-Einträge zwischen zu speichern
    // -> Scrollen in GridView bleibt flüssig
    private List<Hero> heroList;
    private List<Integer> heroToDatabaseList;
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
        heroList = new ArrayList<>();

        // Element n speichert für Element n in 'heroList' den Datenbank-Index i
        // -> diese Information würde in 'Hero' alleine fehlen
        heroToDatabaseList = new ArrayList<>();
        for(int i = 1; i <= h.getTaskCount(); i++){
            if(!h.getHeroName(i).equals(c.NOT_USED)){
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

                heroToDatabaseList.add(i);
            }
        }

        heroGridView = (GridView) findViewById(R.id.heroes_camp_gridview);
        heroGridView.setAdapter(new HeroImagesAdapter(this));

        heroGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                somethingSelected = lastSelectedHeroIndex != position;
                lastSelectedHeroIndex = (somethingSelected = !somethingSelected) ? -1 : position;

                setSellAndFightViews();
                heroGridView.invalidateViews();
            }
        });

        slotsView = (TextView) findViewById(R.id.textview_camp_slots);
        healView = (TextView) findViewById(R.id.textview_camp_heal_hero);
        toFightView = (TextView) findViewById(R.id.textview_camp_to_fight);
        sellView = (TextView) findViewById(R.id.textview_camp_dismiss_hero);
        setSlotsView();
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

            if(!h.updateMedSlotIndex(lastSelectedHeroIndex+1, slotIndex)) Msg.msg(this, "ERROR @ campHealHero : updateMedSlotIndex");

            Intent i = new Intent(this, HospitalActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void commitToQuest(View v){
        Intent i;

        if(lastSelectedHeroIndex != -1){
            switch (origin){
                case "PrepareCombatActivity":
                    i = new Intent(getApplicationContext(), PrepareCombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;

                case "WorldMapQuickCombatActivity":
                    i = new Intent(getApplicationContext(), WorldMapQuickCombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;

                default:
                    i = new Intent(getApplicationContext(), CombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;
            }

        }else{
            Msg.msg(this, "Commit To Quest?");
        }
    }

    public void dismissHero(View v){
        if((lastSelectedHeroIndex != -1)){

            SharedPreferences prefs = getSharedPreferences("CURRENT_MONEY_PREF", Context.MODE_PRIVATE);
            long money = prefs.getLong("currentMoneyLong", -1);

            // Annahme: 'costs' wurde bereits nach Kauf des Helden abgewertet und entspricht jetzt dem akutellen Verkaufswert
            prefs.edit().putLong("currentMoneyLong", money + h.getHeroCosts(lastSelectedHeroIndex+1)).apply();

            h.markOneRowAsUnused(heroToDatabaseList.get(lastSelectedHeroIndex));
            heroList.remove(lastSelectedHeroIndex);
            heroToDatabaseList.remove(lastSelectedHeroIndex);

            lastSelectedHeroIndex = -1;
            setSlotsView();
            setSellAndFightViews();
            heroGridView.invalidateViews();
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

    private void setSellAndFightViews(){
        if(lastSelectedHeroIndex == -1){
            sellView.setTextColor(Color.parseColor("#707070"));
            toFightView.setTextColor(Color.parseColor("#707070"));
            healView.setTextColor(Color.parseColor("#707070"));
        }
        else {
            sellView.setTextColor(Color.WHITE);
            toFightView.setTextColor(Color.WHITE);
            if(h.getHeroHitpoints(lastSelectedHeroIndex+1) == h.getHeroHitpointsTotal(lastSelectedHeroIndex+1))
                healView.setTextColor(Color.parseColor("#707070"));
            else toFightView.setTextColor(Color.WHITE);
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

            private TextView nameView,classesView, hpView, costsView, evasionView;
            private ImageView profileView;
            private LinearLayout rightPanelLayout;

            ViewHolder(View v) {
                nameView = (TextView) v.findViewById(R.id.textView_camp_card_name);
                classesView = (TextView) v.findViewById(R.id.textView_camp_card_prim_and_sec);
                hpView = (TextView) v.findViewById(R.id.textView_camp_card_hp);
                costsView = (TextView) v.findViewById(R.id.textView_camp_card_costs);
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
            holder.nameView.setText(heroList.get(position).getHeroName() + "");
            holder.classesView.setText(heroList.get(position).getClassPrimary() + " und " + heroList.get(position).getClassSecondary());
            holder.hpView.setText(heroList.get(position).getHp() + " / " + heroList.get(position).getHpTotal());
            holder.costsView.setText(heroList.get(position).getCosts() + "");
            holder.evasionView.setText("0");

            if(lastSelectedHeroIndex == position && !somethingSelected){
                holder.rightPanelLayout.setBackgroundColor(Color.WHITE);

            }else{
                holder.rightPanelLayout.setBackgroundColor(Color.parseColor("#FFA8A8A8"));
            }

            holder.profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Msg.msg(getApplication(), "Profile tapped!");
                }
            });

            return convertView;
        }
    }



    /*

    Funktionen zur Auslagerungen

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
