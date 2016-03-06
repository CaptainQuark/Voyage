package com.example.thomas.voyage.BasicActivities;

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
import android.widget.TextView;

import com.example.thomas.voyage.CombatActivities.PrepareCombatActivity;
import com.example.thomas.voyage.CombatActivities.WorldMapQuickCombatActivity;
import com.example.thomas.voyage.ContainerClasses.HelperSharedPrefs;
import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
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
    private ConstRes c;
    private HelperSharedPrefs prefs = new HelperSharedPrefs();
    private List<Hero> heroList;
    private String origin = "";
    private boolean somethingSelected = true;

    // Index des GridView, für Datenbankzugriff über
    // 'UID' muss immer +1 gerechnet werden (DB beginnt bei 1)
    private int selectedHeroIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes_camp);
        hideSystemUI();
        iniValues();
        iniViews();
        setToolbarViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        heroGridView.invalidateViews();
        setToolbarViews();
        //setSlotsView();
        hideSystemUI();
    }



    /*

    onClick-Events

     */



    public void heroesCampBackButton(View v){
        if(heroAllDataCardFragment == null){
            super.onBackPressed();
            finish();
        }
        else putFragmentToSleep();
    }

    public void campHealHero(View v){

        if(selectedHeroIndex == -1){
            Msg.msg(this, "No hero selected");

        }else if(h.getHeroHitpoints(dbIndexForHeroList.get(selectedHeroIndex)) == h.getHeroHitpointsTotal(dbIndexForHeroList.get(selectedHeroIndex))){
            Msg.msg(this, "Your hero doesn't need any medication");

        }else{
            int medSlotIndex;
            Bundle b = getIntent().getExtras();
            medSlotIndex = (b != null) ? b.getInt("SLOT_INDEX", -1) : getFreeSlotAtHospital();

            if(b != null)
                medSlotIndex = b.getInt("SLOT_INDEX", -1);

            if(medSlotIndex == -1)
                medSlotIndex = getFreeSlotAtHospital();

            // nächsten freien Slot in HospitalActivity finden
            if(medSlotIndex == -1){
                Msg.msg(this, "No free medic available");

            }else{
                //SharedPreferences prefs = getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
                //int costs = ((heroList.get(selectedHeroIndex).getHpTotal() - (heroList.get(selectedHeroIndex).getHp())) / (heroList.get(selectedHeroIndex).getHpTotal()) * 100 + 1) * 100;

                // pro fehlendem hitpoint werden $ 100 angerechnet
                int costs = getCostsToHeal(selectedHeroIndex);

                if( prefs.getCurrentMoney(this, new ConstRes()) - costs >= 0){
                    putFragmentToSleep();

                    if(!h.updateMedSlotIndex(dbIndexForHeroList.get(selectedHeroIndex), medSlotIndex))
                        Msg.msg(this, "ERROR @ campHealHero : updateMedSlotIndex");

                    if(!h.updateTimeToLeave(dbIndexForHeroList.get(selectedHeroIndex),
                            System.currentTimeMillis()
                                    + (1000* 60 * ((heroList.get(selectedHeroIndex).getHpTotal() - heroList.get(selectedHeroIndex).getHp()) * c.MIN_TO_HEAL_PER_HP))))
                        Msg.msg(this, "ERROR @ campHealHero : updateTimeToLeave");

                    prefs.removeFromCurrentMoneyAndGetNewVal(costs, this, new ConstRes());
                    Intent i = new Intent(this, HospitalActivity.class);
                    startActivity(i);
                    finish();

                } else
                    Msg.msg(this, "Not enough money, boy!");
            }
        }
    }

    public void commitToQuest(View v){
        Intent i;

        if(selectedHeroIndex != -1 && h.getMedSlotIndex(dbIndexForHeroList.get(selectedHeroIndex)) == -1){
            switch (origin){
                case "PrepareCombatActivity":
                    i = new Intent(getApplicationContext(), PrepareCombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;

                default:
                    i = new Intent(getApplicationContext(), WorldMapQuickCombatActivity.class);
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
        if((selectedHeroIndex != -1 && h.getMedSlotIndex(dbIndexForHeroList.get(selectedHeroIndex)) == -1)){

            long money = (long) h.getHeroCosts(dbIndexForHeroList.get(selectedHeroIndex));
            Msg.msg(this, "Costs: " + money);
            fortuneView.setText("$ " + prefs.addToCurrentMoneyAndGetNewVal(money, getApplicationContext(), new ConstRes()));

            h.markOneRowAsUnused(dbIndexForHeroList.get(selectedHeroIndex));
            dbIndexForHeroList.remove(selectedHeroIndex);
            heroList.remove(selectedHeroIndex);

            selectedHeroIndex = -1;
            setSlotsView();
            setToolbarViews();
            putFragmentToSleep();
            heroGridView.invalidateViews();

        }else{
            Msg.msgShort(this, "No hero to sell (selected)");
        }
    }



    /*

    Funktionen

     */



    private int getCostsToHeal(int i){

        // Ausgangswert: Marktwert (= Kosten) des Helden
        // -> je mehr HP fehlen, desto mehr Prozent werden vom Marktwert als Heilungskosten genommen

        return (int) ( ((float)heroList.get(i).getHp() / (float)heroList.get(i).getHpTotal()) * heroList.get(i).getCosts() );
        //return (heroList.get(i).getHpTotal() - heroList.get(i).getHp()) * c.COSTS_TO_HEAL_PER_HP;
    }

    private int getFreeSlotAtHospital(){
        int[] slotsArray = {1,1,1};
        int slotIndex = -1;

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

        return slotIndex;
    }

    public void passHeroesParameterstoNewActivity(Intent i){
        ConstRes co = new ConstRes();

        if(selectedHeroIndex != -1){ i.putExtra(co.HERO_DATABASE_INDEX, dbIndexForHeroList.get(selectedHeroIndex)); }
        i.putExtra(co.ORIGIN, "HeroCampActivity");
    }

    private void setSlotsView(){
        slotsView.setText(heroList.size() + " / " + h.getTaskCount());
    }

    private void setToolbarViews(){
        try{
            if(selectedHeroIndex == -1){
                sellView.setTextColor(getColor(R.color.inactive_field));
                toFightView.setTextColor(getColor(R.color.inactive_field));
                healView.setTextColor(getColor(R.color.inactive_field));
            }
            else {
                sellView.setTextColor(Color.WHITE);
                toFightView.setTextColor(Color.WHITE);

                if(h.getHeroHitpoints(dbIndexForHeroList.get(selectedHeroIndex)) == h.getHeroHitpointsTotal(dbIndexForHeroList.get(selectedHeroIndex))
                        || prefs.getCurrentMoney(this, new ConstRes()) < getCostsToHeal(selectedHeroIndex)
                        || getFreeSlotAtHospital() == -1
                        || h.getMedSlotIndex(dbIndexForHeroList.get(selectedHeroIndex)) != -1){

                    healView.setTextColor(getColor(R.color.inactive_field));

                }else healView.setTextColor(Color.WHITE);
            }

            slotsView.setText(heroList.size() + " / " + h.getTaskCount());

            if(heroList.size() == 0){
                ImageView emptyCampImageView = (ImageView) findViewById(R.id.iv_camp_empty);
                emptyCampImageView.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            Log.e("EXCEPTION CAMP", String.valueOf(e));
            Msg.msg(this, String.valueOf(e));
        }

    }



    /*

    Fragment-Listener

     */



    @Override
    public void putFragmentToSleep() {
        if(heroAllDataCardFragment != null){
            getFragmentManager().beginTransaction().remove(heroAllDataCardFragment).commit();
            setToolbarViews();
            heroAllDataCardFragment = null;

            somethingSelected = false;
            heroGridView.invalidateViews();
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
            private LinearLayout rightCardLayout;

            ViewHolder(View v) {
                nameView = (TextView) v.findViewById(R.id.textView_camp_card_name);
                classesView = (TextView) v.findViewById(R.id.textView_camp_card_prim_and_sec);
                hpView = (TextView) v.findViewById(R.id.textView_camp_card_hp);
                costsView = (TextView) v.findViewById(R.id.textView_camp_card_costs);
                hospitalCostsView = (TextView) v.findViewById(R.id.textView_camp_card_hospital_costs);
                evasionView = (TextView) v.findViewById(R.id.textView_camp_card_evasion);
                profileView = (ImageView) v.findViewById(R.id.imageView_camp_card_profile);
                rightCardLayout = (LinearLayout) v.findViewById(R.id.layout_camp_right_panel);
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
            holder.costsView.setText("$ " + heroList.get(position).getCosts());
            holder.evasionView.setText((1000 - heroList.get(position).getEvasion())/10 + " %");

            if(selectedHeroIndex == position && !somethingSelected){
                //holder.rightCardLayout.setBackgroundColor(Color.WHITE);
                holder.rightCardLayout.setBackgroundColor(Color.WHITE);

            }else{
                holder.rightCardLayout.setBackgroundColor(Color.parseColor("#FFA8A8A8"));
            }

            if(h.getMedSlotIndex(dbIndexForHeroList.get(position)) != -1) {
                holder.hospitalCostsView.setText("In Heilung...");
            }
            else {
                if(heroList.get(position).getHp() == heroList.get(position).getHpTotal())
                    holder.hospitalCostsView.setText("-");
                else
                    holder.hospitalCostsView.setText("$ " + getCostsToHeal(position) + " / "
                            + (heroList.get(position).getHpTotal() - heroList.get(position).getHp()) / c.MIN_TO_HEAL_PER_HP + " min");

                holder.hospitalCostsView.setTextColor(Color.BLACK);
            }

            holder.profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //selectedHeroIndex = position;
                    somethingSelected = selectedHeroIndex != position;
                    selectedHeroIndex = position;

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    heroAllDataCardFragment = new HeroAllDataCardFragment();
                    Bundle b = new Bundle();
                    b.putInt("DB_INDEX_PLAYER", dbIndexForHeroList.get(selectedHeroIndex));

                    heroAllDataCardFragment.setArguments(b);
                    fragmentTransaction.add(R.id.layout_camp_fragment_container, heroAllDataCardFragment);
                    fragmentTransaction.commit();

                    setToolbarViews();
                }
            });

            holder.rightCardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    somethingSelected = selectedHeroIndex != position;
                    selectedHeroIndex = (somethingSelected = !somethingSelected) ? -1 : position;

                    setToolbarViews();
                    heroGridView.invalidateViews();
                }
            });

            holder.profileView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    heroList.get(position).setHp(heroList.get(position).getHp() - 20);
                    h.updateHeroHitpoints(dbIndexForHeroList.get(position), heroList.get(position).getHp());
                    heroGridView.invalidateViews();
                    setToolbarViews();
                    return false;
                }
            });

            return convertView;
        }
    }



    /*

    Auslagerung von Initialisierungen

     */



    private void iniValues(){
        c = new ConstRes();

        Bundle b = getIntent().getExtras();
        if(b != null){
            origin = b.getString(c.ORIGIN, "StartActivity");
        }

        h = new DBheroesAdapter(this);
        dbIndexForHeroList = new ArrayList<>();

        // 'heroList' wird erstellt, um Datenbank-Einträge zwischen zu speichern
        // -> Scrollen in GridView bleibt flüssig
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
                        h.getHeroEvasion(i),
                        h.getHeroBonusNumber(i)
                ));
            }
        }
    }

    private void iniViews(){
        heroGridView = (GridView) findViewById(R.id.heroes_camp_gridview);
        heroGridView.setAdapter(new HeroImagesAdapter(this));

        //somethingSelected = selectedHeroIndex != position;
        //selectedHeroIndex = (somethingSelected = !somethingSelected) ? -1 : position;

        slotsView = (TextView) findViewById(R.id.textview_camp_slots);
        healView = (TextView) findViewById(R.id.textview_camp_heal_hero);
        toFightView = (TextView) findViewById(R.id.textview_camp_to_fight);
        sellView = (TextView) findViewById(R.id.textview_camp_dismiss_hero);
        fortuneView = (TextView) findViewById(R.id.textview_camp_fortune);

        setSlotsView();
        fortuneView.setText("$ " + prefs.getCurrentMoney(this, new ConstRes()));
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
