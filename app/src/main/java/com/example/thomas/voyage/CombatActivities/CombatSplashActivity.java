package com.example.thomas.voyage.CombatActivities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.example.thomas.voyage.BasicActivities.StartActivity;
import com.example.thomas.voyage.ContainerClasses.PassParametersHelper;
import com.example.thomas.voyage.ContainerClasses.Monster;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Fragments.HeroAllDataCardFragment;
import com.example.thomas.voyage.Fragments.MonsterAllDataFragment;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import org.w3c.dom.Text;

public class CombatSplashActivity extends Activity implements MonsterAllDataFragment.OnFragmentInteractionListener{

    private ConstRes c = new ConstRes();
    private Monster monster;
    private MonsterAllDataFragment monsterAllDataFragment;
    private int heroIndex, length = 1, monsterCounter = -1, bountyTotal = -1;
    private String level = "", biome = "";
    private SharedPreferences prefsFortune;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_splash);
        hideSystemUI();

        prefsFortune = getSharedPreferences(c.SP_CURRENT_MONEY_PREF, this.MODE_PRIVATE);

        TextView nameView = (TextView) findViewById(R.id.textview_com_splash_monster_name);
        TextView desView = (TextView) findViewById(R.id.textview_com_splash_monster_des);
        TextView retreatView = (TextView) findViewById(R.id.textview_com_splash_retreat);
        ImageView backgroundView = (ImageView) findViewById(R.id.imageview_com_splash_background);
        ImageView monsterView = (ImageView) findViewById(R.id.imageview_com_splash_monster);

        // Es muss nicht von jeder Activity level/length/biome
        // gesendet werden, deshalb sind Standardwerte für einen
        // einfach Kampf gesetzt
        Bundle b = getIntent().getExtras();
        if(b != null) {
            heroIndex = b.getInt(c.HERO_DATABASE_INDEX, -1);
            biome = b.getString(c.CURRENT_BIOME, "Forest");
            level = b.getString(c.COMBAT_LEVEL_OF_MONSTERS, "Easy");
            length = b.getInt(c.COMBAT_LENGTH, 1);
            monsterCounter = b.getInt(c.COMBAT_MONSTER_COUNTER, 0);
            bountyTotal = b.getInt(c.COMBAT_BOUNTY_TOTAL, 0);
        }

        monster = new Monster(biome, String.valueOf(level), this);
        nameView.setText(monster.name);
        //Wenn kein Rückzug möglich wird das Feld ausgegraut
        if(monsterCounter % length != 0)retreatView.setTextColor(Color.parseColor("#707070"));

        //Je nach biome wird die Länge in einer anderen Einheit angegeben
        String unitSingural;
        String unitPlural;
        switch (biome){
            case "Forest":
            case "Placeholder_Mountains":
                unitSingural = " Tag bis zum nächsten Außenposten ...";
                unitPlural = " Tage bis zum nächsten Dorf ...";
                break;
            case "Placeholder_Dungeon":
                unitSingural = " Etage bis zum nächsten Ausgang ...";
                unitPlural = " Etagen bis zum nächsten Ausgang ...";
                break;
            default:
                unitSingural = "ERROR@: desView: Biome nicht erkannt";
                unitPlural = "ERROR@: desView: Biome nicht erkannt";
        }
        if(length == 1){
            desView.setText("Noch " + length + unitSingural);
        }else{
            desView.setText("Noch " + length + unitPlural);
        }


        backgroundView.setImageResource(getEnvironmentBackgroundPicture());
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
        try {
            switch (v.getId()){
                case R.id.textview_com_splash_start_combat:
                    startActivity(PassParametersHelper.toCombatMonsterHero(this, new ConstRes(), monster, heroIndex, biome, level, length, monsterCounter, bountyTotal));
                    finish();
                    break;

                case R.id.imageview_com_splash_monster:
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    monsterAllDataFragment = new MonsterAllDataFragment();
                    monsterAllDataFragment.setArguments(PassParametersHelper.toMonsterAllDataFragment(new ConstRes(), monster));
                    fragmentTransaction.add(R.id.layout_com_splash_main, monsterAllDataFragment);
                    fragmentTransaction.commit();
                    break;

                case R.id.textview_com_splash_retreat:
                    if(monsterCounter % length == 0){
                        prefsFortune.edit().putLong(c.MY_POCKET, prefsFortune.getLong(c.MY_POCKET, 0) + bountyTotal).apply();
                        Log.i("BOUNTYTOTAL", "bounty : " + bountyTotal);
                        Intent i = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Msg.msg(this, "Rückzug ist noch nicht möglich!");
                    }
                    break;

                default:
                    Msg.msgShort(this, "ERROR @ onClick : switch : default called");
            }

        }catch (Exception e) {
            Msg.msg(this, String.valueOf(e));
        }
    }



    /*

    Fragment-Listener

     */



    @Override
    public void putMonsterAllDataFragToSleep() {
        if(monsterAllDataFragment != null){
            getFragmentManager().beginTransaction().remove(monsterAllDataFragment).commit();
            monsterAllDataFragment = null;
        }
    }



    /*

    Funktionen

     */



    private int getEnvironmentBackgroundPicture(){
        String backgroundFileId;

        switch (biome){
            case "Forest":
                backgroundFileId = "journey_b0";
                break;
            case "Icecave":
                backgroundFileId = "journey_b1";
                break;
            default:
                backgroundFileId = "placeholder_dummy_0";
        }

        // Ressourcen-Datei wird in Android indexiert -> deshalb 'int' als return
        return getResources().getIdentifier(backgroundFileId, "mipmap", getPackageName());
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
