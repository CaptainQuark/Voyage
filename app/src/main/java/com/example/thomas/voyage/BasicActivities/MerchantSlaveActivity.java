package com.example.thomas.voyage.BasicActivities;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.R;

public class MerchantSlaveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_slave);
        hideSystemUI();
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



    private class HeroCard{
        private TextView nameView, classesView, hpView, evasionView, costsView, constantHpView, constantEvasionView;
        private ImageView profileView;

       // public
    }



    /*

    Funktionen zur Auslagerung von Initialisierungen

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
