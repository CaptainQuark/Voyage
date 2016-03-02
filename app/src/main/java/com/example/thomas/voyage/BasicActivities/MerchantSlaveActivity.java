package com.example.thomas.voyage.BasicActivities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantHeroesAdapter;
import com.example.thomas.voyage.Fragments.HeroAllDataCardFragment;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MerchantSlaveActivity extends Activity implements HeroAllDataCardFragment.onHeroAllDataCardListener {

    private RelativeLayout fragContainerLayout;
    private TextView tradeView, buyView, slotsView, fortuneView;
    private DBheroesAdapter h;
    private DBmerchantHeroesAdapter m;
    private HeroAllDataCardFragment heroAllDataCardFragment;
    private SharedPreferences prefsFortune, prefsMerchant;
    private List<HeroCardHolder> cardList;
    private MerchantCardHolder merchantCardHolder;
    private ConstRes c;
    private String origin;
    private long currentMoney;
    private int selectedHeroCardIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_slave);
        hideSystemUI();
        iniValues();
        iniViews();
        checkIfMerchantLeaves();

        refreshToolbarViews();
    }



    /*

    onClick-Methoden

     */



    public void onClick(View v){
        switch (v.getId()){

            case R.id.tv_merch_slave_buy:
                if(getUsedRowsHeroDb() < h.getTaskCount()){
                    if(currentMoney >= m.getHeroCosts(selectedHeroCardIndex + 1)){
                        copyHeroFromMerchToPlayerDb();
                        m.updateRow(selectedHeroCardIndex + 1, c.NOT_USED);

                        if(!checkIfMerchantLeaves()){
                            selectedHeroCardIndex = -1;
                            if(fragContainerLayout != null) fragContainerLayout.setVisibility(View.GONE);
                            for(int i = 0; i < cardList.size(); i++) cardList.get(i).showCard();

                            refreshToolbarViews();

                        }

                        prefsFortune.edit().putLong(c.MY_POCKET, prefsFortune.getLong(c.MY_POCKET, 0) - m.getHeroCosts(selectedHeroCardIndex + 1)).apply();
                        currentMoney = prefsFortune.getLong(c.MY_POCKET, 0);
                    }
                }

                break;

            case R.id.iv_merch_slave_back_button:
                if(heroAllDataCardFragment == null ){
                    if(origin.equals("HeroCampActivity")){
                        Intent i = new Intent(getApplicationContext(), HeroCampActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        onBackPressed();
                        finish();
                    }

                }else{
                    putFragmentToSleep();
                }

                break;

            case R.id.tv_merch_slave_slots:
                Intent i = new Intent(getApplicationContext(), HeroCampActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.iv_merch_slave_has_left_back_button:
                super.onBackPressed();
                finish();
                break;

            case R.id.tv_merch_slave_trade:
                if(getUsedRowsHeroDb() > 0){
                    Msg.msgShort(this, "Not yet implemented...");

                }else{
                    Msg.msgShort(this, "No hero to trade...");
                }

                break;

            default: Msg.msgShort(this, "ERROR @ onClick : switch : default called");
        }
    }



    /*

    Funktionen

     */



    private boolean checkIfMerchantLeaves(){
        if(getUsedRowsMerchDb() == 0){
            final FrameLayout merchLeftLayout = (FrameLayout) findViewById(R.id.layout_merch_slave_merch_has_left);
            merchLeftLayout.setVisibility(View.VISIBLE);

            TextView tv = (TextView) findViewById(R.id.tv_merch_slave_has_left_des);
            tv.setText("Ein neuer Händler erscheint in " + getTimeToShow() + " Minuten...");

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    merchLeftLayout.setVisibility(View.GONE);
                }
            });

            return true;
        }

        return false;
    }

    private int getUsedRowsMerchDb(){
        int num = 0;

        for(int i = 1; i <= m.getTaskCount(); i++){
            if(!m.getHeroName(i).equals(c.NOT_USED)) num++;
        }

        return num;
    }

    private int getUsedRowsHeroDb(){
        int num = 0;

        for(int i = 1; i <= h.getTaskCount(); i++){
            if(!h.getHeroName(i).equals(c.NOT_USED)) num++;
        }

        return num;
    }

    private int getNextFreeSlotInHeroDb(){
        for(int i = 1; i <= h.getTaskCount(); i++){
            if(h.getHeroName(i).equals(c.NOT_USED)) return i;
        }

        return -1;
    }

    private void copyHeroFromMerchToPlayerDb(){
        int i = selectedHeroCardIndex + 1;
        h.updateRowWithHeroData(
                getNextFreeSlotInHeroDb(),
                m.getHeroName(i),
                m.getHeroHitpoints(i),
                m.getHeroPrimaryClass(i),
                m.getHeroSecondaryClass(i),
                m.getHeroCosts(i),
                m.getHeroImgRes(i),
                m.getHeroHitpointsTotal(i),
                -1,
                0,
                m.getHeroEvasion(i),
                m.getHeroBonusNumber(i)
        );
    }

    private void refreshToolbarViews(){

        if(selectedHeroCardIndex != -1
                && m.getHeroCosts(selectedHeroCardIndex + 1) <= currentMoney
                && getUsedRowsHeroDb() < c.TOTAL_HEROES_PLAYER) {

            buyView.setTextColor(getColor(android.R.color.white));
            tradeView.setTextColor(getColor(android.R.color.white));

        }else{
            buyView.setTextColor(Color.parseColor("#707070"));
            tradeView.setTextColor(Color.parseColor("#707070"));
        }

        slotsView.setText(getUsedRowsHeroDb() + " / " + h.getTaskCount());
        fortuneView.setText("$ " + prefsFortune.getLong(c.MY_POCKET, -1));
    }

    private void refillMerchDatabase(){
        Log.e("UPDATE_MERCH_DATABASE", "updateMerchantDatabase, inserts: " + c.TOTAL_HEROES_MERCHANT);
        List<Hero> heroList = new ArrayList<>();

        for (int i = 0, id; i < c.TOTAL_HEROES_MERCHANT; i++) {
            heroList.add(new Hero(this));
            heroList.get(i).Initialize("Everywhere");

            id = m.updateRowComplete(
                    i + 1,
                    heroList.get(i).getHeroName(),
                    heroList.get(i).getHp(),
                    heroList.get(i).getClassPrimary(),
                    heroList.get(i).getClassSecondary(),
                    heroList.get(i).getCosts(),
                    heroList.get(i).getImageResource(),
                    heroList.get(i).getEvasion(),
                    heroList.get(i).getHpTotal(),
                    heroList.get(i).getBonusNumber());

            if (id < 0) Msg.msg(this, "error@insert of hero " + i + 1);
        }
    }

    private void setNewMerchant(){
        int currentMerchantId = prefsMerchant.getInt(c.MERCHANT_ID, 0);

        currentMerchantId = ++currentMerchantId % 4;

        prefsMerchant.edit().putInt(c.MERCHANT_ID, currentMerchantId).apply();
        merchantCardHolder = new MerchantCardHolder(this, currentMerchantId, getTimeToShow());

        refillMerchDatabase();
        for(int i = 0; i < cardList.size(); i++)
            cardList.get(i).showCard();

        selectedHeroCardIndex = -1;
        refreshToolbarViews();
    }

    private long getNowInSeconds(){
        return (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) * 60 *60
                + Calendar.getInstance().get(Calendar.MINUTE) * 60
                + Calendar.getInstance().get(Calendar.SECOND);
    }

    private long getNewMerchLeaveDaytime(){
        return ((Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) < 12) ? 12*60*60 : 24*60*60;
    }

    private long getNewMerchChangeDate(){

        // Wenn jetzt nach Mittag, dann Mitternacht neuer Merchant, sonst zu Mittag
        long newFinishDate = getNewMerchLeaveDaytime();

        long todayInSeconds = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) * 60 *60
                + Calendar.getInstance().get(Calendar.MINUTE) * 60
                + Calendar.getInstance().get(Calendar.SECOND);

        // neuer Abreise-Zeitpunkt des Händlers
        return System.currentTimeMillis() + (newFinishDate - todayInSeconds)*1000;
    }

    private long getTimeToShow() {
        final SharedPreferences prefs = getSharedPreferences("TIME_TO_LEAVE_PREF", MODE_PRIVATE);
        long timeToShow, merchToLeaveDaytime = prefs.getLong("merchToLeaveDaytime", -1), merchChangeDate = prefs.getLong("merchChangeDate", -1);

        if(merchToLeaveDaytime == -1) merchToLeaveDaytime = getNewMerchLeaveDaytime();
        if(merchChangeDate == -1) merchChangeDate = getNewMerchChangeDate();

        if(System.currentTimeMillis() >= merchChangeDate){
            timeToShow = (getNewMerchLeaveDaytime() - getNowInSeconds()) * 1000;
            prefs.edit().putLong("merchToLeaveDaytime", merchToLeaveDaytime);
            setNewMerchant();
            prefs.edit().putLong("merchChangeDate", getNewMerchChangeDate()).apply();
            prefs.edit().putLong("merchToLeaveDaytime", getNewMerchLeaveDaytime()).apply();

            final FrameLayout merchLeftLayout = (FrameLayout) findViewById(R.id.layout_merch_slave_merch_has_left);
            merchLeftLayout.setVisibility(View.GONE);

            return timeToShow/1000/60;

        }else{
            timeToShow = (merchToLeaveDaytime - getNowInSeconds()) * 1000;
            return timeToShow/1000/60;
        }

        /*
        currentMerchantId = prefs.getInt(MERCHANT_ID, 0);
        merchantProfile.setImageResource(ImgRes.res(this, "merch", currentMerchantId + ""));

        final TextView merchantTimeView = (TextView) findViewById(R.id.activity_merchant_textView_time_to_next_merchant);

        timer = new CountDownTimer(timeToShow, 1000 / 60) {

            public void onTick(long millisUntilFinished) {
                merchantTimeView.setText("" + millisUntilFinished / 1000 / 60);
            }

            public void onFinish() {
                prefs.edit().putLong("merchChangeDate", getNewMerchChangeDate()).apply();
                prefs.edit().putLong("merchToLeaveDaytime", getNewMerchLeaveDaytime()).apply();

                setNewMerchantProfile();
                currentMerchantId = prefs.getInt(MERCHANT_ID, 0);
                merchantProfile.setImageResource(ImgRes.res(getApplicationContext(), "merch", currentMerchantId + ""));

                if (updateMerchantsDatabase(3) < 0)
                    Log.e("ERROR @ ", "updateMerchantsDatabase");

                showExpirationDate();
            }
        }.start();
        */
    }



    /*

    Fragment-Listener

     */



    @Override
    public void putFragmentToSleep() {
        if(heroAllDataCardFragment != null){
            getFragmentManager().beginTransaction().remove(heroAllDataCardFragment).commit();
            fragContainerLayout.setVisibility(View.GONE);
            refreshToolbarViews();
            heroAllDataCardFragment = null;
        }
    }



    /*

    Klassen

     */



    private class HeroCardHolder{
        private int cardIndex;
        private TextView nameView, classesView, hpView, evasionView, costsView, constantHpView, constantEvasionView;
        private ImageView profileView;
        private FrameLayout cardLayout;

        public HeroCardHolder(int index){
            cardIndex = index;

            // Passende Views in XML-Datei finden und initialisieren,
            // um sie später ansprechen zu können
            nameView = (TextView) findViewById(this.getResId("tv_merch_slave_hero_name_" + cardIndex, "id"));
            classesView = (TextView) findViewById(this.getResId("tv_merch_slave_hero_classes_" + cardIndex, "id"));
            hpView = (TextView) findViewById(this.getResId("tv_merch_slave_hero_hp_" + cardIndex, "id"));
            constantHpView = (TextView) findViewById(this.getResId("tv_merch_slave_hero_hp_static_" + cardIndex, "id"));
            evasionView = (TextView) findViewById(this.getResId("tv_merch_slave_hero_evasion_" + cardIndex, "id"));
            constantEvasionView = (TextView) findViewById(this.getResId("tv_merch_slave_hero_evasion_static_" + cardIndex, "id"));
            costsView = (TextView) findViewById(this.getResId("tv_merch_slave_hero_costs_" + cardIndex, "id"));
            profileView = (ImageView) findViewById(this.getResId("iv_merch_slave_hero_profile_" + cardIndex, "id"));

            cardLayout = (FrameLayout) findViewById(this.getResId("layout_merch_slave_card_" + cardIndex, "id"));
            cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!m.getHeroName(cardIndex + 1).equals(c.NOT_USED)){
                        selectedHeroCardIndex = (cardIndex != selectedHeroCardIndex) ? cardIndex : -1;
                        refreshToolbarViews();
                        for(int i = 0; i < cardList.size(); i++)
                            cardList.get(i).showCard();
                    }
                }
            });

            cardLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    heroAllDataCardFragment = new HeroAllDataCardFragment();
                    selectedHeroCardIndex = cardIndex;
                    Bundle b = new Bundle();
                    b.putInt("DB_INDEX_MERCH", selectedHeroCardIndex + 1);

                    fragContainerLayout.setVisibility(View.VISIBLE);

                    heroAllDataCardFragment.setArguments(b);
                    fragmentTransaction.add(R.id.layout_merch_slave_frag_container, heroAllDataCardFragment);
                    fragmentTransaction.commit();

                    refreshToolbarViews();
                    return true;
                }
            });
        }

        public void showCard(){

            if(!m.getHeroName(cardIndex + 1).equals(c.NOT_USED)){

                nameView.setText(m.getHeroName(cardIndex + 1));
                classesView.setText(m.getHeroPrimaryClass(cardIndex + 1) + " & " + m.getHeroSecondaryClass(cardIndex + 1));
                hpView.setText(m.getHeroHitpoints(cardIndex + 1) + " / " + m.getHeroHitpoints(cardIndex + 1));
                evasionView.setText(String.valueOf((1000 - m.getHeroEvasion(cardIndex + 1))/10) + " %");
                costsView.setText("$ " + m.getHeroCosts(cardIndex + 1));
                profileView.setImageResource(this.getResId(m.getHeroImgRes(cardIndex + 1), "mipmap"));

                nameView.setVisibility(View.VISIBLE);
                classesView.setVisibility(View.VISIBLE);
                hpView.setVisibility(View.VISIBLE);
                evasionView.setVisibility(View.VISIBLE);
                costsView.setVisibility(View.VISIBLE);
                constantEvasionView.setVisibility(View.VISIBLE);
                constantHpView.setVisibility(View.VISIBLE);

                if(currentMoney >= m.getHeroCosts(cardIndex + 1))
                    costsView.setTextColor(getColor(android.R.color.holo_green_dark));
                else
                    costsView.setTextColor(getColor(android.R.color.holo_red_dark));

            }else{

                nameView.setVisibility(View.GONE);
                classesView.setVisibility(View.GONE);
                hpView.setVisibility(View.GONE);
                evasionView.setVisibility(View.GONE);
                costsView.setVisibility(View.GONE);
                constantEvasionView.setVisibility(View.GONE);
                constantHpView.setVisibility(View.GONE);
                profileView.setImageResource(this.getResId("market_dummy_1", "mipmap"));
            }

            if(cardIndex == selectedHeroCardIndex || selectedHeroCardIndex == -1 || m.getHeroName(cardIndex+1).equals(c.NOT_USED)) cardLayout.setForeground(null);
            else cardLayout.setForeground(new ColorDrawable(Color.parseColor("#ae000000")));
        }

        private int getResId(String resString, String resType){
            return getResources().getIdentifier(resString, resType, getPackageName());
        }

    }

    private class MerchantCardHolder{
        private TextView timeView, timeConstantView;
        private ImageView profileView;
        private int imageResource;
        private long timeToLeave;
        private boolean timeInMinutes;

        public MerchantCardHolder(Context con, int imgSuffix, long time){
            imageResource = con.getResources().getIdentifier("merchant_" + imgSuffix, "mipmap", con.getPackageName());
            timeToLeave = time;
            timeInMinutes = true;

            timeView = (TextView) findViewById(R.id.tv_merch_slave_time_to_leave);
            profileView = (ImageView) findViewById(R.id.iv_merch_slave_merch_profile);
            timeConstantView = (TextView) findViewById(R.id.tv_merch_slave_time_to_leave_constant);

            timeView.setText(String.valueOf(timeToLeave));
            profileView.setImageResource(imageResource);

            profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNewMerchant();
                    for (int i = 0; i < cardList.size(); i++)
                        cardList.get(i).showCard();
                }
            });

            timeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(timeInMinutes =! timeInMinutes || timeToLeave < 60){
                        timeView.setText(String.valueOf(timeToLeave));
                        timeConstantView.setText("abreise in minuten");
                    }
                    else{
                        timeView.setText(String.valueOf(timeToLeave/60) + " : " + timeToLeave % 60);
                        timeConstantView.setText("abreise in stunden");
                    }
                }
            });

            timeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    prefsFortune.edit().putLong(c.MY_POCKET, prefsFortune.getLong(c.MY_POCKET, 0) + 1500).apply();
                    currentMoney = prefsFortune.getLong(c.MY_POCKET, -1);
                    for (int i = 0; i < cardList.size(); i++)
                        cardList.get(i).showCard();
                    refreshToolbarViews();

                    return true;
                }
            });
        }
    }



    /*

    Funktionen zur Auslagerung von Initialisierungen

     */



    private void iniValues(){
        h = new DBheroesAdapter(this);
        m = new DBmerchantHeroesAdapter(this);
        c = new ConstRes();

        selectedHeroCardIndex = -1;

        Bundle b = getIntent().getExtras();
        if(b != null){
            origin = b.getString(c.ORIGIN);
        }else{
            origin = "Default";
        }

        prefsFortune = getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
        prefsMerchant = getSharedPreferences(c.SP_SLAVE_MERCHANT, Context.MODE_PRIVATE);
        currentMoney = prefsFortune.getLong(c.MY_POCKET, -1);

        cardList = new ArrayList<>();
        for(int i = 0; i < m.getTaskCount(); i++){
            cardList.add(new HeroCardHolder(i));
            cardList.get(i).showCard();
        }

        // Solange kein Händler nach dem 1. Start der App gekommen ist,
        // wird als Default-Wert '0' gewählt (da SP leer ist)
        merchantCardHolder = new MerchantCardHolder(this, prefsMerchant.getInt(c.MERCHANT_ID, 0), getTimeToShow());
    }

    private void iniViews(){
        tradeView = (TextView) findViewById(R.id.tv_merch_slave_trade);
        buyView = (TextView) findViewById(R.id.tv_merch_slave_buy);
        slotsView = (TextView) findViewById(R.id.tv_merch_slave_slots);
        fortuneView = (TextView) findViewById(R.id.tv_merch_slave_fortune);
        fragContainerLayout = (RelativeLayout) findViewById(R.id.layout_merch_slave_frag_container);
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