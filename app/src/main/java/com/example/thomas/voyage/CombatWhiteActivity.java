package com.example.thomas.voyage;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class CombatWhiteActivity extends ActionBarActivity {

    private String heroName = "", heroPrimaryClass = "", heroSecondaryClass = "";
    private int heroHitpoints = -1, heroCosts  =-1;
    private ImageView heroProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_white);

        heroProfile = (ImageView)findViewById(R.id.combat_white_hero_profile);

        /*
        Bundle b = getIntent().getExtras();
        if(b != null){
            heroName = b.getString("HEROES_NAME", "???");
            heroPrimaryClass = b.getString("HEROES_PRIMARY_CLASS", "???");
            heroSecondaryClass = b.getString("HEROES_SECONDARY_CLASS", "???");
            heroHitpoints = b.getInt("HEROES_HITPOINTS", -1);
            heroCosts= b.getInt("HEROES_COSTS", -1);

        }else{
            //Falls Rückkehr von Heroes-Datenbank: Daten wurden nicht übertragen, Fehler!
        }*/
    }

    public void combatWhiteSelectHeroFromPool(View view){
        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
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

        startActivity(i);
    }

}
