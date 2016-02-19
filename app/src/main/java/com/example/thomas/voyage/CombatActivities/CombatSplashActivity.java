package com.example.thomas.voyage.CombatActivities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Monster;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

public class CombatSplashActivity extends Activity {

    private ConstRes c = new ConstRes();
    private Monster monster;
    private long heroIndex;
    private int level = 0, length = 1, biome = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_splash);
        hideSystemUI();

        TextView nameView = (TextView) findViewById(R.id.textview_com_splash_monster_name);
        TextView desView = (TextView) findViewById(R.id.textview_com_splash_monster_des);
        ImageView backgroundView = (ImageView) findViewById(R.id.imageview_com_splash_background);
        ImageView monsterView = (ImageView) findViewById(R.id.imageview_com_splash_monster);

        // Es muss nicht in von jeder Activity level/length/biome
        // gesendet werden, deshalb sind Standardwerte für einen
        // einfach Kampf gesetzt
        Bundle b = getIntent().getExtras();
        if(b != null) {
            heroIndex = b.getLong(c.HERO_DATABASE_INDEX, -1);
            biome = b.getInt(c.CURRENT_BIOME, 0);
            level = b.getInt(c.COMBAT_LEVEL_OF_MONSTERS, 0);
            length = b.getInt(c.COMBAT_LENGTH, 1);
        }

        monster = new Monster(getCurrentBiome(biome), String.valueOf(level), this);
        nameView.setText(monster.name);
        desView.setText("Fürchtet sich (nicht) im Dunkeln...");
        backgroundView.setImageResource(getResources().getIdentifier("journey_b" + biome, "mipmap", getPackageName()));
        monsterView.setImageResource(getResources().getIdentifier(monster.imgRes, "mipmap", getPackageName()));
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        hideSystemUI();
    }



    /*

    onClick-Methoden

     */



    public void onClick(View v){
        switch (v.getId()){
            case R.id.textview_com_splah_start_combat:
                Intent i = new Intent(this, CombatMonsterHeroActivity.class);
                i.putExtra(c.HERO_DATABASE_INDEX, heroIndex);
                i.putExtra(c.CURRENT_BIOME, getCurrentBiome(biome));
                i.putExtra(c.COMBAT_LEVEL_OF_MONSTERS,level);
                i.putExtra(c.COMBAT_LENGTH, length);
                i.putExtra(c.MONSTER_NAME, monster.name);
                i.putExtra(c.MONSTER_DESCRIPTION, "");
                i.putExtra(c.MONSTER_IMG_RES, "placeholder_dummy_0");
                i.putExtra(c.MONSTER_CHECKOUT, monster.checkout);
                i.putExtra(c.MONSTER_IMG_RES, monster.imgRes);
                i.putExtra(c.MONSTER_HITPOINTS_NOW, monster.hp);
                i.putExtra(c.MONSTER_HITPOINTS_TOTAL, monster.hpTotal);
                i.putExtra(c.MONSTER_DAMAGE_MIN, monster.dmgMin);
                i.putExtra(c.MONSTER_DAMAGE_MAX, monster.dmgMax);
                i.putExtra(c.MONSTER_BLOCK, monster.block);
                i.putExtra(c.MONSTER_EVASION, monster.evasion);
                i.putExtra(c.MONSTER_ACCURACY, monster.accuracy);
                i.putExtra(c.MONSTER_CRIT_CHANCE, monster.critChance);
                i.putExtra(c.MONSTER_CRIT_MULTIPLIER, monster.critMultiplier);
                i.putExtra(c.MONSTER_RESISTANCE, monster.resistance);
                startActivity(i);
                finish();
        }
    }



    /*

    Funktionen

     */



    private String getCurrentBiome(int biomeId){
        String biome;

        switch(biomeId){
            case 0:
                biome = "Forest";
                break;
            case 1:
                biome = "Icelands";
                break;
            case 2:
                biome = "Waterfall";
                break;
            default:
                biome = "nowhere";
        }

        biome = "Forest";
        return biome;
    }



    /*

    Funktionen zur Auslagerung

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