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
    private int level = 0, length = 1;
    private String biome = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_splash);

        TextView nameView = (TextView) findViewById(R.id.textview_com_splash_monster_name);
        TextView desView = (TextView) findViewById(R.id.textview_com_splash_monster_des);
        ImageView backgroundView = (ImageView) findViewById(R.id.imageview_com_splash_background);

        // Es muss nicht in von jeder Activity level/length/biome
        // gesendet werden, deshalb sind Standardwerte für einen
        // einfach Kampf gesetzt
        Bundle b = getIntent().getExtras();
        if(b != null) {
            heroIndex = b.getLong(c.HERO_DATABASE_INDEX, -1);
            biome = b.getString(c.CURRENT_BIOME, "nowhere");
            level = b.getInt(c.COMBAT_LEVEL_OF_MONSTERS, 0);
            length = b.getInt(c.COMBAT_LENGTH, 1);
        }

        monster = new Monster(String.valueOf(biome), String.valueOf(level), this);
        nameView.setText(monster.name);
        desView.setText("Fürchtet sich (nicht) im Dunkeln...");
        backgroundView.setImageResource(getResources().getIdentifier(monster.imgRes, "mipmap", getPackageName()));
    }



    /*

    onClick-Methoden

     */



    public void onClick(View v){
        switch (v.getId()){
            case R.id.textview_com_splah_start_combat:
                Intent i = new Intent(this, CombatMonsterHeroActivity.class);
                i.putExtra(c.HERO_DATABASE_INDEX, heroIndex);
                i.putExtra(c.CURRENT_BIOME, biome);
                i.putExtra(c.COMBAT_LEVEL_OF_MONSTERS,level);
                i.putExtra(c.COMBAT_LENGTH, length);
                i.putExtra(c.MONSTER_NAME, monster.name);
                i.putExtra(c.MONSTER_DESCRIPTION, "");
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

}
