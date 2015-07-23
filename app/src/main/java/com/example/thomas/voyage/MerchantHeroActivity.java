package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MerchantHeroActivity extends Activity {

    TextView debugView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_hero);

        hideSystemUI();

        debugView = (TextView) findViewById(R.id.debug_merchant_hero_textView);
        setDebugText();


    }

    public void setDebugText() {

        List<Hero> herosList = new ArrayList<Hero>();
        String totalText = "";

        for (int i = 0; i < 20; i++) {
            herosList.add(new Hero());
            herosList.get(i).Initialize(null);
            totalText = totalText + "Held " + i + 1 + ": " + herosList.get(i).getHeroData() + '\n';
        }

        debugView.setText(totalText);

/*
        Hero hero1 = new Hero();
        Hero hero2 = new Hero();
        Hero hero3 = new Hero();
        Hero hero4 = new Hero();
        Hero hero5 = new Hero();
        hero1.Initialize(null);
        hero2.Initialize(null);
        hero3.Initialize(null);
        hero4.Initialize(null);
        hero5.Initialize(null);

        debugView.setText(" h1: " + hero1.getHeroData()
                + "\n h2: " + hero2.getHeroData()
                + "\n h3: " + hero3.getHeroData()
                + "\n h4: " + hero4.getHeroData()
                + "\n h5: " + hero5.getHeroData());
*/
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

    public void clickToHome(View view){
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
    }

}
