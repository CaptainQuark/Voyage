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
import com.example.thomas.voyage.ResClasses.ImgRes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewMerchantHeroActivity extends Activity {

    private TextView hpView, evasionView, battlesView, costsView, nameView, classesView, timeView, slotsView, buyView, fortuneView;
    private ImageView heroProfileView, merchProfileView;
    private GridView gridViewHeroesSelectable;
    private SharedPreferences prefs;
    private CountDownTimer timer;
    private String origin;
    private int slotsInHeroesDatabase, currentMerchantId, selectedHeroId;
    private boolean tradeIsPossible;
    private List<Hero> heroList;
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
        gridViewHeroesSelectable.invalidateViews();
        fortuneView.setText("$ " + prefs.getLong("currentMoneyLong", -1));
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        hideSystemUI();
        setSlotsViewAppearence();
        gridViewHeroesSelectable.invalidateViews();
    }



    /*

    Funktionen

     */



    private void setSlotsViewAppearence(){
        slotsView.setText(Long.toString(getUsedSlotsInHeroesDatabase()) + " / " + slotsInHeroesDatabase);
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
            }

            public void onFinish() {
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putLong("TIME_TO_LEAVE", setNewCorrectedDate());
                editor.apply();

                setNewMerchantProfile();

                long validation = updateMerchantDatabase(3);
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
    }

    public void setMarketVisibility(){
        if(heroList.size() == 0){
            ImageView market = (ImageView) findViewById(R.id.imageview_merch_has_left);
            TextView des = (TextView) findViewById(R.id.textview_merch_has_left);
            LinearLayout main = (LinearLayout) findViewById(R.id.layout_merch_hero_main);

            market.setVisibility(View.VISIBLE);
            des.setVisibility(View.VISIBLE);
            main.setVisibility(View.GONE);
        }
    }

    /*

    OnClick-Methoden

     */



    public void addMoneyToCurrentPocket(View v){
        prefs.edit().putLong("currentMoneyLong", prefs.getLong("currentMoneyLong", -1) + 1000).apply();
        fortuneView.setText("$ " + prefs.getLong("currentMoneyLong", -1));
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
        updateMerchantDatabase(3);
        setNewMerchantProfile();
    }

    public void onMerchHeroBuy(View v){
        if(tradeIsPossible){
            prefs.edit().putLong("currentMoneyLong", prefs.getLong("currentMoneyLong", -1) - heroList.get(selectedHeroId).getCosts()).apply();

            int index = -1;

            for(int i = 1; i <= h.getTaskCount(); i++){
                if(h.getHeroName(i).equals("NOT_USED")){
                    index = i;
                    i = (int) h.getTaskCount();
                }
            }

            if(h.updateRowWithHeroData(index, heroList.get(selectedHeroId).getHeroName(), heroList.get(selectedHeroId).getHp(),
                    heroList.get(selectedHeroId).getClassPrimary(),heroList.get(selectedHeroId).getClassSecondary(),
                    heroList.get(selectedHeroId).getCosts(), heroList.get(selectedHeroId).getImageResource(),
                    heroList.get(selectedHeroId).getHpTotal(), -1, 0, heroList.get(selectedHeroId).getEvasion())
                    < 0) {

                Msg.msg(this, "ERROR @ onMerchHeroBuy : updateRowWithHeroData");
            }

            heroList.remove(selectedHeroId);
            m.updateRow(selectedHeroId + 1, "NOT_USED");
            buyView.setTextColor(getResources().getColor(R.color.tint_inactive));

            setMarketVisibility();
            fortuneView.setText("$ " + prefs.getLong("currentMoneyLong", -1));
            selectedHeroId = -1;

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
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }


            if(selectedHeroId != position){
                imageView.setImageResource(getResources().getIdentifier(heroList.get(position).getImageResource(), "mipmap", getPackageName()));

            }else{
                imageView.setImageResource(R.drawable.ripple_backbutton);
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
        prefs = getSharedPreferences("CURRENT_MONEY_PREF", Context.MODE_PRIVATE);
        slotsInHeroesDatabase = (int) h.getTaskCount();
        currentMerchantId = 0;
        selectedHeroId = -1;
        tradeIsPossible = false;

        heroList = new ArrayList<>();
        for(int i = 1; i <= m.getTaskCount(); i++){
            heroList.add(new Hero(
                    m.getHeroName(i), m.getHeroClassOne(i), m.getHeroClassTwo(i), m.getHeroImgRes(i),
                    m.getHeroHitpoints(i), m.getHpTotal(i), m.getHeroCosts(i), m.getHeroEvasion(i)
            ));
        }

        Bundle b = getIntent().getExtras();
        if(b != null){
            origin = b.getString("ORIGIN", "StartActivity");
        }else origin = "";

        gridViewHeroesSelectable.setAdapter(new HeroImagesAdapter(this));
        gridViewHeroesSelectable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedHeroId = position;
                heroProfileView.setImageResource(getResources().getIdentifier(heroList.get(position).getImageResource(), "mipmap", getPackageName()));
                setHeroShowcase();
                gridViewHeroesSelectable.invalidateViews();

                if(prefs.getLong("currentMoneyLong", -1) - heroList.get(position).getCosts() > 0 && h.getTaskCount() > getUsedSlotsInHeroesDatabase()){
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
