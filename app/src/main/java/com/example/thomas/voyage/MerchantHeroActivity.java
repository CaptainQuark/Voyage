package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MerchantHeroActivity extends Activity {

    private TextView debugView, buyHeroView;
    private int currentSelectedHeroId;
    private int currentSavings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_hero);

        hideSystemUI();

        debugView = (TextView) findViewById(R.id.debug_merchant_hero_textView);
        buyHeroView = (TextView) findViewById(R.id.merchant_hero_buy);





        setDebugText();
    }

    public void setDebugText() {

        List<Hero> herosList = new ArrayList<Hero>();
        String totalText = "";

        for (int i = 0; i < 5; i++) {
            herosList.add(new Hero());
            herosList.get(i).Initialize(null);
            totalText = totalText + "Held " + i + 1 + ": " + herosList.get(i).getHeroData() + '\n';
        }

        debugView.setText(totalText);
    }

    public void clickToHome(View view){
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
    }

    public void buyHero(View view) {

        debugView.setText("buyHero clicked");
    }

    public void selectedHeroLeft(View view) {
        processSelectedHero(0);
    }

    public void selectedHeroMiddle(View view) {
        processSelectedHero(1);
    }

    public void selectedHeroRight(View view) {
        processSelectedHero(2);
    }

    private void processSelectedHero(int index) {

        currentSelectedHeroId = index;

        if (index == 1) {
            buyHeroView.setBackgroundColor(Color.GREEN);
        } else buyHeroView.setBackgroundColor(Color.RED);

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

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
