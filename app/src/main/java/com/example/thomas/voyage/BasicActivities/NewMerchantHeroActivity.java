package com.example.thomas.voyage.BasicActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantHeroesAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;
import com.example.thomas.voyage.ResClasses.ImgRes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewMerchantHeroActivity extends Activity {

    private TextView hpView, evasionView, battlesView, costsView, nameView, classesView, timeView, slotsView, buyView, fortuneView, des, desMinutes;
    private ImageView heroProfileView, merchProfileView, market;
    private LinearLayout main;
    private GridView gridViewHeroesSelectable;
    private SharedPreferences prefs;
    private ConstRes c = new ConstRes();
    private CountDownTimer timer;
    private String origin;
    private int slotsInHeroesDatabase, currentMerchantId, selectedHeroId;
    private boolean tradeIsPossible;
    private List<Hero> heroList;
    private List<Integer> databaseIndexForHeroList;
    private DBheroesAdapter h;
    private DBmerchantHeroesAdapter m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_merchant_hero);
        hideSystemUI();
        iniViews();
        iniValues();
        setSlotsViewAppearence();
        showExpirationDate();

        // automatisch 1. Helden wählen, wenn noch mind. ein Held verfügbar ist
        if(heroList.size() > 0){
            selectedHeroId = 0;
            setHeroShowcase();

        }else{
            setMarketVisibility();
        }

        gridViewHeroesSelectable.invalidateViews();
        fortuneView.setText("$ " + prefs.getLong(c.MY_POCKET, -1));
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
        setSlotsViewAppearence();
        gridViewHeroesSelectable.invalidateViews();
        fortuneView.setText("$ " + prefs.getLong(c.MY_POCKET, -1));
        setMarketVisibility();
    }



    /*

    Funktionen

     */



    private void setSlotsViewAppearence(){
        slotsView.setText(getUsedSlotsInHeroesDatabase() + " / " + slotsInHeroesDatabase);
    }

    private long getUsedSlotsInHeroesDatabase() {
        DBheroesAdapter helper = new DBheroesAdapter(this);

        long countUsed = 0;
        slotsInHeroesDatabase = (int) helper.getTaskCount();

        for (long i = 0; i < slotsInHeroesDatabase; i++) {
            if (!(helper.getHeroName(i + 1).equals(getResources().getString(R.string.indicator_unused_row)))) {
                countUsed++;
            }
        }

        return countUsed;
    }

    private void showExpirationDate() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        long finishDate = prefs.getLong("TIME_TO_LEAVE", 0);

        if(finishDate == 0)
            finishDate = setNewCorrectedDate();

        currentMerchantId = prefs.getInt("merchantId", 0);
        merchProfileView.setImageResource(ImgRes.res(this, "merch", currentMerchantId + ""));

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putLong("TIME_TO_LEAVE", finishDate);
        editor.apply();

        Date presentDate = new Date();
        long dateDiff = finishDate - presentDate.getTime();

        timer = new CountDownTimer(dateDiff, 1000 / 60) {

            public void onTick(long millisUntilFinished) {
                timeView.setText("" + millisUntilFinished / 1000 / 60);
                desMinutes.setText("Ein neuer erscheint in " + millisUntilFinished / 1000 / 60 + " Minuten.");
            }

            public void onFinish() {
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putLong("TIME_TO_LEAVE", setNewCorrectedDate());
                editor.apply();

                setNewMerchantProfile();

                long validation = updateMerchantDatabase(c.TOTAL_HEROES_MERCHANT);
                if (validation < 0) {
                    Log.e("ERROR @ ", "updateMerchantsDatabase");
                }

                showExpirationDate();
            }
        }.start();
    }

    private long setNewCorrectedDate(){
        long finishDate = prefs.getLong("TIME_TO_LEAVE", 0);

        if(finishDate == 0){
            finishDate = System.currentTimeMillis();
        }

        Date newExpirationDate = new Date();
        newExpirationDate.setTime((System.currentTimeMillis() + (60 * 60 * 1000)) - (System.currentTimeMillis() - finishDate));

        return newExpirationDate.getTime();
    }

    public void setNewMerchantProfile(){
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

        if(currentMerchantId >= 3){
            currentMerchantId = 0;
        } else currentMerchantId++;

        editor.putInt("merchantId", currentMerchantId);
        editor.apply();

        merchProfileView.setImageResource(ImgRes.res(this, "merch", Integer.toString(currentMerchantId)));
    }

    public long updateMerchantDatabase(int numberOfInserts) {
        Log.e("UPDATE_DATABASE", "updateMerchantDatabase, inserts: " + numberOfInserts);
        List<Hero> heroList = new ArrayList<>();
        long id = 0;

        for (int i = 0; i < numberOfInserts; i++) {
            heroList.add(new Hero(this));
            heroList.get(i).Initialize("Everywhere");

            // noch Vorgänger-unabhängig -> neue Zeilen werden einfach an Ende angehängt
            id = m.updateRowComplete(
                    i + 1,
                    heroList.get(i).getHeroName(),
                    heroList.get(i).getHp(),
                    heroList.get(i).getClassPrimary(),
                    heroList.get(i).getClassSecondary(),
                    heroList.get(i).getCosts(),
                    heroList.get(i).getImageResource(),
                    heroList.get(i).getEvasion(),
                    heroList.get(i).getHpTotal());

            //merchantHelper.updateImageResource(i + 1, "hero_dummy_" + (i));

            if (id < 0) Msg.msg(this, "error@insert of hero " + i + 1);
        }

        return id;
    }

    public void setHeroShowcase(){
        nameView.setText(heroList.get(selectedHeroId).getHeroName());
        classesView.setText(heroList.get(selectedHeroId).getClassPrimary() + " & " + heroList.get(selectedHeroId).getClassSecondary());
        hpView.setText(heroList.get(selectedHeroId).getHp() + " / " + heroList.get(selectedHeroId).getHpTotal());
        evasionView.setText(String.valueOf(heroList.get(selectedHeroId).getEvasion()));
        costsView.setText("$ " + heroList.get(selectedHeroId).getCosts());
        battlesView.setText(String.valueOf(0));
        heroProfileView.setImageResource(getResources().getIdentifier(heroList.get(selectedHeroId).getImageResource(), "mipmap", getPackageName()));
    }

    public void setMarketVisibility(){
        if(heroList.size() == 0){
            market.setVisibility(View.VISIBLE);
            des.setVisibility(View.VISIBLE);
            desMinutes.setVisibility(View.VISIBLE);
            main.setVisibility(View.GONE);
        }
    }



    /*

    OnClick-Methoden

     */



    public void addMoneyToCurrentPocket(View v){
        prefs.edit().putLong(c.MY_POCKET, prefs.getLong(c.MY_POCKET, -1) + 1000).apply();
        fortuneView.setText("$ " + prefs.getLong(c.MY_POCKET, -1));
    }

    public void onMerchHeroBack(View view) {
        timer.cancel();
        if(origin.equals("HeroCampActivity")){
            Intent i = new Intent(getApplicationContext(), HeroesPartyActivity.class);
            startActivity(i);
            finish();

        }else {
            onBackPressed();
            finish();
        }
    }

    public void resetMerchant(View view){
        updateMerchantDatabase(c.TOTAL_HEROES_MERCHANT);
        setNewMerchantProfile();
        setSlotsViewAppearence();
    }

    public void onMerchHeroBuy(View v){
        if(tradeIsPossible){
            prefs.edit().putLong(c.MY_POCKET, prefs.getLong(c.MY_POCKET, -1) - heroList.get(selectedHeroId).getCosts()).apply();

            int index = -1;

            for(int i = 1; i <= h.getTaskCount(); i++){
                if(h.getHeroName(i).equals(c.NOT_USED)){
                    index = i;
                    i = (int) h.getTaskCount();
                }
            }

            if(h.updateRowWithHeroData(index, heroList.get(selectedHeroId).getHeroName(), heroList.get(selectedHeroId).getHp(),
                    heroList.get(selectedHeroId).getClassPrimary(), heroList.get(selectedHeroId).getClassSecondary(),
                    heroList.get(selectedHeroId).getCosts(), heroList.get(selectedHeroId).getImageResource(),
                    heroList.get(selectedHeroId).getHpTotal(), -1, 0, heroList.get(selectedHeroId).getEvasion())
                    < 0) {

                Msg.msg(this, "ERROR @ onMerchHeroBuy : updateRowWithHeroData");
            }

            m.updateRow(databaseIndexForHeroList.get(selectedHeroId), c.NOT_USED);
            heroList.remove(selectedHeroId);
            databaseIndexForHeroList.remove(selectedHeroId);
            buyView.setTextColor(getResources().getColor(R.color.tint_inactive));

            if(heroList.size() > 0){
                selectedHeroId = 0;
                setHeroShowcase();
                setSlotsViewAppearence();
                fortuneView.setText("$ " + prefs.getLong(c.MY_POCKET, -1));
                gridViewHeroesSelectable.invalidateViews();

            }

            setMarketVisibility();

        }else{
            Msg.msg(this, "Kauf nicht möglich...");
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

        public int getCount() {
            return heroList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(getResources().getIdentifier(heroList.get(position).getImageResource(), "mipmap", getPackageName()));

            if(selectedHeroId != position){
                imageView.setColorFilter(getResources().getColor(R.color.tint_inactive), android.graphics.PorterDuff.Mode.MULTIPLY);

            }else{
                imageView.setColorFilter(getResources().getColor(R.color.tint_transparent), android.graphics.PorterDuff.Mode.ADD);
            }

            return imageView;
        }
    }



    /*

    Funktionen zur Auslagerung

     */



    public void iniValues(){
        h = new DBheroesAdapter(this);
        m = new DBmerchantHeroesAdapter(this);
        prefs = getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
        slotsInHeroesDatabase = (int) h.getTaskCount();
        currentMerchantId = 0;
        selectedHeroId = -1;
        tradeIsPossible = false;

        databaseIndexForHeroList = new ArrayList<>();
        heroList = new ArrayList<>();
        for(int i = 1; i <= m.getTaskCount(); i++){
            if(!m.getHeroName(i).equals(c.NOT_USED)){
                heroList.add(new Hero(
                        m.getHeroName(i), m.getHeroClassOne(i), m.getHeroClassTwo(i), m.getHeroImgRes(i),
                        m.getHeroHitpoints(i), m.getHpTotal(i), m.getHeroCosts(i), m.getHeroEvasion(i)
                ));

                // 'databaseIndexForHeroList' immer synchron mit 'heroList',
                // daher kann gespeichert werden, welche Datenbank-UID der Held
                // an Stelle x in der Liste hat
                databaseIndexForHeroList.add(i);
            }
        }

        Bundle b = getIntent().getExtras();
        if(b != null){
            origin = b.getString(c.ORIGIN, "StartActivity");
        }else origin = "";

        gridViewHeroesSelectable.setAdapter(new HeroImagesAdapter(this));
        gridViewHeroesSelectable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedHeroId = position;
                setHeroShowcase();
                gridViewHeroesSelectable.invalidateViews();

                if(prefs.getLong(c.MY_POCKET, -1) - heroList.get(position).getCosts() > 0 && h.getTaskCount() > getUsedSlotsInHeroesDatabase()){
                    tradeIsPossible = true;
                    buyView.setTextColor(Color.WHITE);
                }else{
                    buyView.setTextColor(getResources().getColor(R.color.tint_inactive));
                }
            }
        });

    }

    public void iniViews(){
        nameView = (TextView) findViewById(R.id.textview_merch_hero_name);
        classesView = (TextView) findViewById(R.id.textview_merch_hero_prim_and_sec_class);
        hpView = (TextView) findViewById(R.id.textview_merch_hero_hp);
        evasionView = (TextView) findViewById(R.id.textview_merch_hero_evasion);
        battlesView = (TextView) findViewById(R.id.textview_merch_hero_battles);
        costsView = (TextView) findViewById(R.id.textview_merch_hero_costs);
        heroProfileView = (ImageView) findViewById(R.id.imageview_merch_hero_large_profile);
        gridViewHeroesSelectable = (GridView) findViewById(R.id.gridview_merch_hero_other_heroes);
        timeView = (TextView) findViewById(R.id.textview_merch_hero_time);
        slotsView = (TextView) findViewById(R.id.textview_merch_hero_slots);
        buyView = (TextView) findViewById(R.id.textview_merch_hero_buy);
        fortuneView = (TextView) findViewById(R.id.textview_merch_hero_fortune);
        merchProfileView = (ImageView) findViewById(R.id.imageview_merch_hero_merchant_profile);
        market = (ImageView) findViewById(R.id.imageview_merch_has_left);
        des = (TextView) findViewById(R.id.textview_merch_has_left);
        desMinutes = (TextView) findViewById(R.id.textview_merch_has_left_minute_display);
        main = (LinearLayout) findViewById(R.id.layout_merch_hero_main);
    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
    }
}
