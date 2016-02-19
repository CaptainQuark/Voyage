package com.example.thomas.voyage.CombatActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.BasicActivities.HeroCampActivity;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

public class PrepareCombatActivity extends Activity {

    private String origin = "";
    private long index = -1;
    private ConstRes co = new ConstRes();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_combat);
        hideSystemUI();
        getHeroData();

        ImageView heroProfile = (ImageView) findViewById(R.id.combat_white_hero_profile);
        TextView toCombatView = (TextView) findViewById(R.id.pre_combat_to_battle_view);

        if (origin.equals("HeroCampActivity")) {
            DBheroesAdapter h = new DBheroesAdapter(this);

            heroProfile.setImageResource(getResources().getIdentifier(h.getHeroImgRes(index), "mipmap", getPackageName()));
            heroProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            heroProfile.setColorFilter(Color.TRANSPARENT);

            toCombatView.setText("Auf in die Schlacht!");
            toCombatView.setBackground(getDrawable(R.drawable.ripple_go_in_combat));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
        getHeroData();
    }

    private void getHeroData(){
        Bundle b = getIntent().getExtras();
        if (b != null) {
            index = b.getLong(co.HERO_DATABASE_INDEX);
            origin = b.getString(co.ORIGIN);
        }
    }

    public void combatWhiteSelectHeroFromParty(View view){
        Intent i = new Intent(getApplicationContext(), HeroCampActivity.class);
        i.putExtra("ORIGIN", "PrepareCombatActivity");
        startActivity(i);
    }

    public void startCombat(View view){

        if(origin.equals("HeroCampActivity")){
            Intent i = new Intent(getApplicationContext(), CombatMonsterHeroActivity.class);

            i.putExtra(co.HERO_DATABASE_INDEX, index);
            i.putExtra(co.ORIGIN, origin);
            startActivity(i);
            finish();
        }
    }

    public void prepareBackButton(View view){
        super.onBackPressed();
        finish();
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
