package com.example.thomas.voyage.BasicActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.provider.CalendarContract;
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
    private DBheroesAdapter heroesHelper;
    private TextView slotsView, buyHeroView, toFightView, sellView;

    // 'heroList' wird erstellt, um Datenbank-Einträge zwischen zu speichern
    // -> Scrollen in GridView bleibt flüssig
    private List<Hero> heroList;
    private List<Integer> heroToDatabaseList;
    private String origin = "";
    private static boolean somethingSelected = false;

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

        heroesHelper = new DBheroesAdapter(this);
        heroList = new ArrayList<>();

        // Element n speichert für Element n in 'heroList' den Datenbank-Index i
        // -> diese Information würde in 'Hero' alleine fehlen
        heroToDatabaseList = new ArrayList<>();
        for(int i = 1; i <= heroesHelper.getTaskCount(); i++){
            if(!heroesHelper.getHeroName(i).equals(c.NOT_USED)){
                heroList.add(new Hero(
                        heroesHelper.getHeroName(i),
                        heroesHelper.getHeroPrimaryClass(i),
                        heroesHelper.getHeroSecondaryClass(i),
                        heroesHelper.getHeroImgRes(i),
                        heroesHelper.getHeroHitpoints(i),
                        heroesHelper.getHeroHitpointsTotal(i),
                        heroesHelper.getHeroCosts(i)
                ));

                heroToDatabaseList.add(i);
            }
        }

        heroGridView = (GridView) findViewById(R.id.heroes_camp_gridview);
        heroGridView.setAdapter(new HeroImagesAdapter(this));
        heroGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                lastSelectedHeroIndex = position;
                setSellAndFightViews();
                heroGridView.invalidateViews();
            }
        });

        slotsView = (TextView) findViewById(R.id.textview_camp_slots);
        buyHeroView = (TextView) findViewById(R.id.textview_camp_buy_hero);
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

    public void heroesCampBackButton(View v){
        super.onBackPressed();
        finish();
    }

    public void campBuyNewHero(View v){
        Intent i = new Intent(this, MerchantHeroActivity.class);
        startActivity(i);
        finish();
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

    public void passHeroesParameterstoNewActivity(Intent i){
        ConstRes co = new ConstRes();

        if(lastSelectedHeroIndex != -1){

             /*

             Um die Funktion nutzen zu können, muss ein Held ausgewählt sein -> für Kämpfe gedacht

            */

            i.putExtra(co.HEROES_NAME,heroList.get(lastSelectedHeroIndex).getHeroName());
            i.putExtra(co.HEROES_PRIMARY_CLASS, heroList.get(lastSelectedHeroIndex).getClassPrimary());
            i.putExtra(co.HEROES_SECONDARY_CLASS,heroList.get(lastSelectedHeroIndex).getClassSecondary());
            i.putExtra(co.HEROES_HITPOINTS,heroList.get(lastSelectedHeroIndex).getHp());
            i.putExtra(co.HEROES_COSTS, heroList.get(lastSelectedHeroIndex).getCosts());
            i.putExtra(co.IMAGE_RESOURCE,heroList.get(lastSelectedHeroIndex).getImageResource());
        }

        i.putExtra(co.ORIGIN, "HeroCampActivity");
    }

    public void dismissHero(View v){
        ConstRes c = new ConstRes();
        if((lastSelectedHeroIndex != -1)){

            SharedPreferences prefs = getSharedPreferences("CURRENT_MONEY_PREF", Context.MODE_PRIVATE);
            long money = prefs.getLong("currentMoneyLong", -1);

            // Annahme: 'costs' wurde bereits nach Kauf des Helden abgewertet und entspricht jetzt dem akutellen Verkaufswert
            prefs.edit().putLong("currentMoneyLong", money + heroesHelper.getHeroCosts(lastSelectedHeroIndex+1)).apply();

            heroesHelper.markOneRowAsUnused(heroToDatabaseList.get(lastSelectedHeroIndex));
            heroList.remove(lastSelectedHeroIndex);
            heroToDatabaseList.remove(lastSelectedHeroIndex);

            lastSelectedHeroIndex = -1;
            setSlotsView();
            setSellAndFightViews();
            heroGridView.invalidateViews();
        }
    }

    private void setSlotsView(){
        slotsView.setText(heroList.size() + " / " + heroesHelper.getTaskCount());
        if(heroList.size() == heroesHelper.getTaskCount()) buyHeroView.setTextColor(Color.parseColor("#707070"));
        else buyHeroView.setTextColor(Color.WHITE);
    }

    private void setSellAndFightViews(){
        if(lastSelectedHeroIndex == -1){
            sellView.setTextColor(Color.parseColor("#707070"));
            toFightView.setTextColor(Color.parseColor("#707070"));
        }
        else {
            sellView.setTextColor(Color.WHITE);
            toFightView.setTextColor(Color.WHITE);
        }
    }
















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

            private TextView nameView,classesView, hpView, costsView, battlesView;
            private ImageView profileView;
            private LinearLayout rightPanelLayout;

            ViewHolder(View v) {
                nameView = (TextView) v.findViewById(R.id.textView_camp_card_name);
                classesView = (TextView) v.findViewById(R.id.textView_camp_card_prim_and_sec);
                hpView = (TextView) v.findViewById(R.id.textView_camp_card_hp);
                costsView = (TextView) v.findViewById(R.id.textView_camp_card_costs);
                battlesView = (TextView) v.findViewById(R.id.textView_camp_card_battles);
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
            holder.hpView.setText(heroList.get(position).getHp() + " / " + heroList.get(position).getHpConst());
            holder.costsView.setText(heroList.get(position).getCosts() + "");
            holder.battlesView.setText("0");

            if(lastSelectedHeroIndex == position && !somethingSelected){
                holder.rightPanelLayout.setBackgroundColor(Color.WHITE);
                somethingSelected = true;

            }else{
                holder.rightPanelLayout.setBackgroundColor(Color.parseColor("#FFA8A8A8"));
            }

            return convertView;
        }
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
