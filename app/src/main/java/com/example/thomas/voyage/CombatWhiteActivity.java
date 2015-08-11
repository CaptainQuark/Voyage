package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class CombatWhiteActivity extends Activity {

    private String heroName = "", heroPrimaryClass = "", heroSecondaryClass = "", image = "", origin = "";
    private int heroHitpoints = -1, heroCosts  =-1;
    private ImageView heroProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_white);
        hideSystemUI();

        heroProfile = (ImageView) findViewById(R.id.combat_white_hero_profile);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            image = b.getString("IMAGE_RESOURCE", "hero_dummy_0");
            heroName = b.getString("HEROES_NAME", "");
            heroPrimaryClass = b.getString("HEROES_PRIMARY_CLASS", "");
            heroSecondaryClass = b.getString("HEROES_SECONDARY_CLASS", "");
            heroHitpoints = b.getInt("HEROES_HITPOINTS", -1);
            heroCosts = b.getInt("HEROES_COSTS", -1);
            origin = b.getString("ORIGIN", "");
        }

        if (origin.equals("HeroesPartyActivity")) {
            Message.message(this, "image resource: " + image);
            heroProfile.setImageResource(getResources().getIdentifier(image, "mipmap", getPackageName()));
            heroProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            heroProfile.setColorFilter(Color.TRANSPARENT);
        }
    }

    public void combatWhiteSelectHeroFromParty(View view){
        Intent i = new Intent(getApplicationContext(), HeroesPartyActivity.class);
        i.putExtra("ORIGIN", "CombatWhiteActivity");
        startActivity(i);
    }

    public void startCombat(View view){
        Intent i = new Intent(getApplicationContext(), CombatActivity.class);

        i.putExtra("HEROES_NAME",heroName);
        i.putExtra("HEROES_PRIMARY_CLASS", heroPrimaryClass);
        i.putExtra("HEROES_SECONDARY_CLASS",heroSecondaryClass);
        i.putExtra("HEROES HITPOINTS", heroHitpoints);
        i.putExtra("HEROES_COSTS", heroCosts);
        i.putExtra("IMAGE_RESOURCE", image);
        i.putExtra("ORIGIN", "CombatWhiteActivity");

        startActivity(i);
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
