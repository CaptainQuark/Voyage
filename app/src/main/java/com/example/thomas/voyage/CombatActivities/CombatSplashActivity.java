package com.example.thomas.voyage.CombatActivities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.IntentExtrasHelper;
import com.example.thomas.voyage.ContainerClasses.Monster;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

public class CombatSplashActivity extends Activity {

    private ConstRes c = new ConstRes();
    private Monster monster;
    private long heroIndex;
    private int length = 1;
    private String level = "", biome = "";

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
            biome = b.getString(c.CURRENT_BIOME, "Forest");
            level = b.getString(c.COMBAT_LEVEL_OF_MONSTERS, "Easy");
            length = b.getInt(c.COMBAT_LENGTH, 1);
        }

        monster = new Monster(biome, String.valueOf(level), this);
        nameView.setText(monster.name);
        desView.setText("Fürchtet sich (nicht) im Dunkeln...");

        String backgroundFileId = "";
        if(biome.equals("Forest")) backgroundFileId = "journey_b0";

        backgroundView.setImageResource(getResources().getIdentifier(backgroundFileId, "mipmap", getPackageName()));
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
                startActivity(IntentExtrasHelper.toCombatMonsterHero(this, new ConstRes(), monster, heroIndex, biome, level, length));
                finish();
        }
    }



    /*

    Funktionen

     */







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
