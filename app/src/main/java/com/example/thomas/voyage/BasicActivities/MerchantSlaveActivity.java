package com.example.thomas.voyage.BasicActivities;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantHeroesAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MerchantSlaveActivity extends Activity {

    private DBheroesAdapter h;
    private DBmerchantHeroesAdapter m;
    private List<HeroCardHolder> cardList;
    private ConstRes c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_slave);
        hideSystemUI();
        iniValues();
    }



    /*

    onClick-Methoden

     */



    // Code



    /*

    Funktionen

     */



    // Code



    /*

    Klassen

     */



    private class HeroCardHolder{
        private int cardIndex;
        private TextView nameView, classesView, hpView, evasionView, costsView, constantHpView, constantEvasionView;
        private ImageView profileView;

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
        }

        public void showCard(){

            if(!m.getHeroName(cardIndex + 1).equals(c.NOT_USED)){

                nameView.setText(m.getHeroName(cardIndex + 1));
                classesView.setText(m.getHeroClassOne(cardIndex + 1) + " & " + m.getHeroClassTwo(cardIndex + 1));
                hpView.setText(m.getHeroHitpoints(cardIndex + 1) + " / " + m.getHeroHitpoints(cardIndex + 1));
                evasionView.setText(String.valueOf(m.getHeroEvasion(cardIndex + 1)));
                costsView.setText("$ " + m.getHeroCosts(cardIndex + 1));
                profileView.setImageResource( this.getResId( m.getHeroImgRes(cardIndex + 1), "mipmap"));

            }else{

                nameView.setVisibility(View.GONE);
                classesView.setVisibility(View.GONE);
                hpView.setVisibility(View.GONE);
                evasionView.setVisibility(View.GONE);
                costsView.setVisibility(View.GONE);
                constantEvasionView.setVisibility(View.GONE);
                constantHpView.setVisibility(View.GONE);
                profileView.setImageResource(this.getResId("camp_0", "mipmap"));
            }
        }

        private int getResId(String resString, String resType){
            return getResources().getIdentifier(resString, resType, getPackageName());
        }

    }



    /*

    Funktionen zur Auslagerung von Initialisierungen

     */



    private void iniValues(){
        h = new DBheroesAdapter(this);
        m = new DBmerchantHeroesAdapter(this);

        c = new ConstRes();
        cardList = new ArrayList<>();
        for(int i = 0; i < m.getTaskCount(); i++){
            cardList.add(new HeroCardHolder(i));
            cardList.get(i).showCard();
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
